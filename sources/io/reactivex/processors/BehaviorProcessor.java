package io.reactivex.processors;

import io.reactivex.annotations.CheckReturnValue;
import io.reactivex.annotations.Experimental;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import java.lang.reflect.Array;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class BehaviorProcessor<T> extends FlowableProcessor<T> {
    static final BehaviorSubscription[] EMPTY = new BehaviorSubscription[0];
    static final Object[] EMPTY_ARRAY = new Object[0];
    static final BehaviorSubscription[] TERMINATED = new BehaviorSubscription[0];
    long index;
    final ReadWriteLock lock;
    final Lock readLock;
    final AtomicReference<BehaviorSubscription<T>[]> subscribers;
    final AtomicReference<Throwable> terminalEvent;
    final AtomicReference<Object> value;
    final Lock writeLock;

    @CheckReturnValue
    public static <T> BehaviorProcessor<T> create() {
        return new BehaviorProcessor<>();
    }

    @CheckReturnValue
    public static <T> BehaviorProcessor<T> createDefault(T defaultValue) {
        ObjectHelper.requireNonNull(defaultValue, "defaultValue is null");
        return new BehaviorProcessor<>(defaultValue);
    }

    BehaviorProcessor() {
        this.value = new AtomicReference<>();
        this.lock = new ReentrantReadWriteLock();
        this.readLock = this.lock.readLock();
        this.writeLock = this.lock.writeLock();
        this.subscribers = new AtomicReference<>(EMPTY);
        this.terminalEvent = new AtomicReference<>();
    }

    BehaviorProcessor(T defaultValue) {
        this();
        this.value.lazySet(ObjectHelper.requireNonNull(defaultValue, "defaultValue is null"));
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        BehaviorSubscription<T> bs = new BehaviorSubscription<>(s, this);
        s.onSubscribe(bs);
        if (!add(bs)) {
            Throwable ex = this.terminalEvent.get();
            if (ex == ExceptionHelper.TERMINATED) {
                s.onComplete();
            } else {
                s.onError(ex);
            }
        } else if (bs.cancelled) {
            remove(bs);
        } else {
            bs.emitFirst();
        }
    }

    public void onSubscribe(Subscription s) {
        if (this.terminalEvent.get() != null) {
            s.cancel();
        } else {
            s.request(Long.MAX_VALUE);
        }
    }

    public void onNext(T t) {
        ObjectHelper.requireNonNull(t, "onNext called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (this.terminalEvent.get() == null) {
            Object o = NotificationLite.next(t);
            setCurrent(o);
            for (BehaviorSubscription<T> bs : (BehaviorSubscription[]) this.subscribers.get()) {
                bs.emitNext(o, this.index);
            }
        }
    }

    public void onError(Throwable t) {
        ObjectHelper.requireNonNull(t, "onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        if (!this.terminalEvent.compareAndSet((Object) null, t)) {
            RxJavaPlugins.onError(t);
            return;
        }
        Object o = NotificationLite.error(t);
        for (BehaviorSubscription<T> bs : terminate(o)) {
            bs.emitNext(o, this.index);
        }
    }

    public void onComplete() {
        if (this.terminalEvent.compareAndSet((Object) null, ExceptionHelper.TERMINATED)) {
            Object o = NotificationLite.complete();
            for (BehaviorSubscription<T> bs : terminate(o)) {
                bs.emitNext(o, this.index);
            }
        }
    }

    @Experimental
    public boolean offer(T t) {
        if (t == null) {
            onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
            return true;
        }
        BehaviorSubscription<T>[] array = (BehaviorSubscription[]) this.subscribers.get();
        for (BehaviorSubscription<T> s : array) {
            if (s.isFull()) {
                return false;
            }
        }
        Object o = NotificationLite.next(t);
        setCurrent(o);
        for (BehaviorSubscription<T> bs : array) {
            bs.emitNext(o, this.index);
        }
        return true;
    }

    public boolean hasSubscribers() {
        return ((BehaviorSubscription[]) this.subscribers.get()).length != 0;
    }

    /* access modifiers changed from: package-private */
    public int subscriberCount() {
        return ((BehaviorSubscription[]) this.subscribers.get()).length;
    }

    public Throwable getThrowable() {
        Object o = this.value.get();
        if (NotificationLite.isError(o)) {
            return NotificationLite.getError(o);
        }
        return null;
    }

    public T getValue() {
        Object o = this.value.get();
        if (NotificationLite.isComplete(o) || NotificationLite.isError(o)) {
            return null;
        }
        return NotificationLite.getValue(o);
    }

    public Object[] getValues() {
        T[] b = getValues((Object[]) EMPTY_ARRAY);
        if (b == EMPTY_ARRAY) {
            return new Object[0];
        }
        return b;
    }

    public T[] getValues(T[] array) {
        Object o = this.value.get();
        if (o == null || NotificationLite.isComplete(o) || NotificationLite.isError(o)) {
            if (array.length != 0) {
                array[0] = null;
            }
            return array;
        }
        T v = NotificationLite.getValue(o);
        if (array.length != 0) {
            array[0] = v;
            if (array.length != 1) {
                array[1] = null;
            }
        } else {
            array = (Object[]) ((Object[]) Array.newInstance(array.getClass().getComponentType(), 1));
            array[0] = v;
        }
        return array;
    }

    public boolean hasComplete() {
        return NotificationLite.isComplete(this.value.get());
    }

    public boolean hasThrowable() {
        return NotificationLite.isError(this.value.get());
    }

    public boolean hasValue() {
        Object o = this.value.get();
        return o != null && !NotificationLite.isComplete(o) && !NotificationLite.isError(o);
    }

    /* access modifiers changed from: package-private */
    public boolean add(BehaviorSubscription<T> rs) {
        BehaviorSubscription<T>[] a;
        BehaviorSubscription<T>[] b;
        do {
            a = (BehaviorSubscription[]) this.subscribers.get();
            if (a == TERMINATED) {
                return false;
            }
            int len = a.length;
            b = new BehaviorSubscription[(len + 1)];
            System.arraycopy(a, 0, b, 0, len);
            b[len] = rs;
        } while (!this.subscribers.compareAndSet(a, b));
        return true;
    }

    /* access modifiers changed from: package-private */
    public void remove(BehaviorSubscription<T> rs) {
        BehaviorSubscription<T>[] a;
        BehaviorSubscription<T>[] b;
        do {
            a = (BehaviorSubscription[]) this.subscribers.get();
            if (a != TERMINATED && a != EMPTY) {
                int len = a.length;
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= len) {
                        break;
                    } else if (a[i] == rs) {
                        j = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (j < 0) {
                    return;
                }
                if (len == 1) {
                    b = EMPTY;
                } else {
                    b = new BehaviorSubscription[(len - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (len - j) - 1);
                }
            } else {
                return;
            }
        } while (!this.subscribers.compareAndSet(a, b));
    }

    /* access modifiers changed from: package-private */
    public BehaviorSubscription<T>[] terminate(Object terminalValue) {
        BehaviorSubscription<T>[] a = (BehaviorSubscription[]) this.subscribers.get();
        if (!(a == TERMINATED || (a = (BehaviorSubscription[]) this.subscribers.getAndSet(TERMINATED)) == TERMINATED)) {
            setCurrent(terminalValue);
        }
        return a;
    }

    /* access modifiers changed from: package-private */
    public void setCurrent(Object o) {
        Lock wl = this.writeLock;
        wl.lock();
        this.index++;
        this.value.lazySet(o);
        wl.unlock();
    }

    static final class BehaviorSubscription<T> extends AtomicLong implements Subscription, AppendOnlyLinkedArrayList.NonThrowingPredicate<Object> {
        private static final long serialVersionUID = 3293175281126227086L;
        final Subscriber<? super T> actual;
        volatile boolean cancelled;
        boolean emitting;
        boolean fastPath;
        long index;
        boolean next;
        AppendOnlyLinkedArrayList<Object> queue;
        final BehaviorProcessor<T> state;

        BehaviorSubscription(Subscriber<? super T> actual2, BehaviorProcessor<T> state2) {
            this.actual = actual2;
            this.state = state2;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this, n);
            }
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.state.remove(this);
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0032, code lost:
            if (r0 == null) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0038, code lost:
            if (test(r0) != false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x003a, code lost:
            emitLoop();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitFirst() {
            /*
                r6 = this;
                r3 = 1
                boolean r4 = r6.cancelled
                if (r4 == 0) goto L_0x0006
            L_0x0005:
                return
            L_0x0006:
                monitor-enter(r6)
                boolean r4 = r6.cancelled     // Catch:{ all -> 0x000d }
                if (r4 == 0) goto L_0x0010
                monitor-exit(r6)     // Catch:{ all -> 0x000d }
                goto L_0x0005
            L_0x000d:
                r3 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x000d }
                throw r3
            L_0x0010:
                boolean r4 = r6.next     // Catch:{ all -> 0x000d }
                if (r4 == 0) goto L_0x0016
                monitor-exit(r6)     // Catch:{ all -> 0x000d }
                goto L_0x0005
            L_0x0016:
                io.reactivex.processors.BehaviorProcessor<T> r2 = r6.state     // Catch:{ all -> 0x000d }
                java.util.concurrent.locks.Lock r1 = r2.readLock     // Catch:{ all -> 0x000d }
                r1.lock()     // Catch:{ all -> 0x000d }
                long r4 = r2.index     // Catch:{ all -> 0x000d }
                r6.index = r4     // Catch:{ all -> 0x000d }
                java.util.concurrent.atomic.AtomicReference<java.lang.Object> r4 = r2.value     // Catch:{ all -> 0x000d }
                java.lang.Object r0 = r4.get()     // Catch:{ all -> 0x000d }
                r1.unlock()     // Catch:{ all -> 0x000d }
                if (r0 == 0) goto L_0x003e
            L_0x002c:
                r6.emitting = r3     // Catch:{ all -> 0x000d }
                r3 = 1
                r6.next = r3     // Catch:{ all -> 0x000d }
                monitor-exit(r6)     // Catch:{ all -> 0x000d }
                if (r0 == 0) goto L_0x0005
                boolean r3 = r6.test(r0)
                if (r3 != 0) goto L_0x0005
                r6.emitLoop()
                goto L_0x0005
            L_0x003e:
                r3 = 0
                goto L_0x002c
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.processors.BehaviorProcessor.BehaviorSubscription.emitFirst():void");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0035, code lost:
            r6.fastPath = true;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitNext(java.lang.Object r7, long r8) {
            /*
                r6 = this;
                r4 = 1
                boolean r1 = r6.cancelled
                if (r1 == 0) goto L_0x0006
            L_0x0005:
                return
            L_0x0006:
                boolean r1 = r6.fastPath
                if (r1 != 0) goto L_0x0037
                monitor-enter(r6)
                boolean r1 = r6.cancelled     // Catch:{ all -> 0x0011 }
                if (r1 == 0) goto L_0x0014
                monitor-exit(r6)     // Catch:{ all -> 0x0011 }
                goto L_0x0005
            L_0x0011:
                r1 = move-exception
                monitor-exit(r6)     // Catch:{ all -> 0x0011 }
                throw r1
            L_0x0014:
                long r2 = r6.index     // Catch:{ all -> 0x0011 }
                int r1 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1))
                if (r1 != 0) goto L_0x001c
                monitor-exit(r6)     // Catch:{ all -> 0x0011 }
                goto L_0x0005
            L_0x001c:
                boolean r1 = r6.emitting     // Catch:{ all -> 0x0011 }
                if (r1 == 0) goto L_0x0031
                io.reactivex.internal.util.AppendOnlyLinkedArrayList<java.lang.Object> r0 = r6.queue     // Catch:{ all -> 0x0011 }
                if (r0 != 0) goto L_0x002c
                io.reactivex.internal.util.AppendOnlyLinkedArrayList r0 = new io.reactivex.internal.util.AppendOnlyLinkedArrayList     // Catch:{ all -> 0x0011 }
                r1 = 4
                r0.<init>(r1)     // Catch:{ all -> 0x0011 }
                r6.queue = r0     // Catch:{ all -> 0x0011 }
            L_0x002c:
                r0.add(r7)     // Catch:{ all -> 0x0011 }
                monitor-exit(r6)     // Catch:{ all -> 0x0011 }
                goto L_0x0005
            L_0x0031:
                r1 = 1
                r6.next = r1     // Catch:{ all -> 0x0011 }
                monitor-exit(r6)     // Catch:{ all -> 0x0011 }
                r6.fastPath = r4
            L_0x0037:
                r6.test(r7)
                goto L_0x0005
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.processors.BehaviorProcessor.BehaviorSubscription.emitNext(java.lang.Object, long):void");
        }

        public boolean test(Object o) {
            if (this.cancelled) {
                return true;
            }
            if (NotificationLite.isComplete(o)) {
                this.actual.onComplete();
                return true;
            } else if (NotificationLite.isError(o)) {
                this.actual.onError(NotificationLite.getError(o));
                return true;
            } else {
                long r = get();
                if (r != 0) {
                    this.actual.onNext(NotificationLite.getValue(o));
                    if (r != Long.MAX_VALUE) {
                        decrementAndGet();
                    }
                    return false;
                }
                cancel();
                this.actual.onError(new MissingBackpressureException("Could not deliver value due to lack of requests"));
                return true;
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0016, code lost:
            r0.forEachWhile(r2);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void emitLoop() {
            /*
                r2 = this;
            L_0x0000:
                boolean r1 = r2.cancelled
                if (r1 == 0) goto L_0x0005
            L_0x0004:
                return
            L_0x0005:
                monitor-enter(r2)
                io.reactivex.internal.util.AppendOnlyLinkedArrayList<java.lang.Object> r0 = r2.queue     // Catch:{ all -> 0x000f }
                if (r0 != 0) goto L_0x0012
                r1 = 0
                r2.emitting = r1     // Catch:{ all -> 0x000f }
                monitor-exit(r2)     // Catch:{ all -> 0x000f }
                goto L_0x0004
            L_0x000f:
                r1 = move-exception
                monitor-exit(r2)     // Catch:{ all -> 0x000f }
                throw r1
            L_0x0012:
                r1 = 0
                r2.queue = r1     // Catch:{ all -> 0x000f }
                monitor-exit(r2)     // Catch:{ all -> 0x000f }
                r0.forEachWhile(r2)
                goto L_0x0000
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.processors.BehaviorProcessor.BehaviorSubscription.emitLoop():void");
        }

        public boolean isFull() {
            return get() == 0;
        }
    }
}
