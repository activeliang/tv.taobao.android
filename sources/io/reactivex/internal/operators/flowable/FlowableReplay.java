package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import io.reactivex.internal.subscribers.SubscriberResourceWrapper;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Timed;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableReplay<T> extends ConnectableFlowable<T> implements HasUpstreamPublisher<T>, Disposable {
    static final Callable DEFAULT_UNBOUNDED_FACTORY = new DefaultUnboundedFactory();
    final Callable<? extends ReplayBuffer<T>> bufferFactory;
    final AtomicReference<ReplaySubscriber<T>> current;
    final Publisher<T> onSubscribe;
    final Flowable<T> source;

    interface ReplayBuffer<T> {
        void complete();

        void error(Throwable th);

        void next(T t);

        void replay(InnerSubscription<T> innerSubscription);
    }

    public static <U, R> Flowable<R> multicastSelector(Callable<? extends ConnectableFlowable<U>> connectableFactory, Function<? super Flowable<U>, ? extends Publisher<R>> selector) {
        return Flowable.unsafeCreate(new MultiCastPublisher(connectableFactory, selector));
    }

    public static <T> ConnectableFlowable<T> observeOn(ConnectableFlowable<T> co, Scheduler scheduler) {
        return RxJavaPlugins.onAssembly(new ConnectableFlowableReplay(co, co.observeOn(scheduler)));
    }

    public static <T> ConnectableFlowable<T> createFrom(Flowable<? extends T> source2) {
        return create(source2, DEFAULT_UNBOUNDED_FACTORY);
    }

    public static <T> ConnectableFlowable<T> create(Flowable<T> source2, int bufferSize) {
        if (bufferSize == Integer.MAX_VALUE) {
            return createFrom(source2);
        }
        return create(source2, new ReplayBufferTask(bufferSize));
    }

    public static <T> ConnectableFlowable<T> create(Flowable<T> source2, long maxAge, TimeUnit unit, Scheduler scheduler) {
        return create(source2, maxAge, unit, scheduler, Integer.MAX_VALUE);
    }

    public static <T> ConnectableFlowable<T> create(Flowable<T> source2, long maxAge, TimeUnit unit, Scheduler scheduler, int bufferSize) {
        return create(source2, new ScheduledReplayBufferTask(bufferSize, maxAge, unit, scheduler));
    }

    static <T> ConnectableFlowable<T> create(Flowable<T> source2, Callable<? extends ReplayBuffer<T>> bufferFactory2) {
        AtomicReference<ReplaySubscriber<T>> curr = new AtomicReference<>();
        return RxJavaPlugins.onAssembly(new FlowableReplay(new ReplayPublisher<>(curr, bufferFactory2), source2, curr, bufferFactory2));
    }

    private FlowableReplay(Publisher<T> onSubscribe2, Flowable<T> source2, AtomicReference<ReplaySubscriber<T>> current2, Callable<? extends ReplayBuffer<T>> bufferFactory2) {
        this.onSubscribe = onSubscribe2;
        this.source = source2;
        this.current = current2;
        this.bufferFactory = bufferFactory2;
    }

    public Publisher<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.onSubscribe.subscribe(s);
    }

    public void dispose() {
        this.current.lazySet((Object) null);
    }

    public boolean isDisposed() {
        Disposable d = this.current.get();
        return d == null || d.isDisposed();
    }

    public void connect(Consumer<? super Disposable> connection) {
        ReplaySubscriber<T> ps;
        boolean doConnect;
        while (true) {
            ps = this.current.get();
            if (ps != null && !ps.isDisposed()) {
                break;
            }
            try {
                ReplaySubscriber<T> u = new ReplaySubscriber<>((ReplayBuffer) this.bufferFactory.call());
                if (this.current.compareAndSet(ps, u)) {
                    ps = u;
                    break;
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                throw ExceptionHelper.wrapOrThrow(ex);
            }
        }
        if (ps.shouldConnect.get() || !ps.shouldConnect.compareAndSet(false, true)) {
            doConnect = false;
        } else {
            doConnect = true;
        }
        try {
            connection.accept(ps);
            if (doConnect) {
                this.source.subscribe(ps);
            }
        } catch (Throwable ex2) {
            if (doConnect) {
                ps.shouldConnect.compareAndSet(true, false);
            }
            Exceptions.throwIfFatal(ex2);
            throw ExceptionHelper.wrapOrThrow(ex2);
        }
    }

    static final class ReplaySubscriber<T> extends AtomicReference<Subscription> implements FlowableSubscriber<T>, Disposable {
        static final InnerSubscription[] EMPTY = new InnerSubscription[0];
        static final InnerSubscription[] TERMINATED = new InnerSubscription[0];
        private static final long serialVersionUID = 7224554242710036740L;
        final ReplayBuffer<T> buffer;
        boolean done;
        final AtomicInteger management = new AtomicInteger();
        long maxChildRequested;
        long maxUpstreamRequested;
        final AtomicBoolean shouldConnect = new AtomicBoolean();
        final AtomicReference<InnerSubscription<T>[]> subscribers = new AtomicReference<>(EMPTY);

        ReplaySubscriber(ReplayBuffer<T> buffer2) {
            this.buffer = buffer2;
        }

        public boolean isDisposed() {
            return this.subscribers.get() == TERMINATED;
        }

        public void dispose() {
            this.subscribers.set(TERMINATED);
            SubscriptionHelper.cancel(this);
        }

        /* access modifiers changed from: package-private */
        public boolean add(InnerSubscription<T> producer) {
            InnerSubscription<T>[] c;
            InnerSubscription<T>[] u;
            if (producer == null) {
                throw new NullPointerException();
            }
            do {
                c = (InnerSubscription[]) this.subscribers.get();
                if (c == TERMINATED) {
                    return false;
                }
                int len = c.length;
                u = new InnerSubscription[(len + 1)];
                System.arraycopy(c, 0, u, 0, len);
                u[len] = producer;
            } while (!this.subscribers.compareAndSet(c, u));
            return true;
        }

        /* access modifiers changed from: package-private */
        public void remove(InnerSubscription<T> p) {
            InnerSubscription<T>[] c;
            InnerSubscription<T>[] u;
            do {
                c = (InnerSubscription[]) this.subscribers.get();
                int len = c.length;
                if (len != 0) {
                    int j = -1;
                    int i = 0;
                    while (true) {
                        if (i >= len) {
                            break;
                        } else if (c[i].equals(p)) {
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
                        u = EMPTY;
                    } else {
                        u = new InnerSubscription[(len - 1)];
                        System.arraycopy(c, 0, u, 0, j);
                        System.arraycopy(c, j + 1, u, j, (len - j) - 1);
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(c, u));
        }

        public void onSubscribe(Subscription p) {
            if (SubscriptionHelper.setOnce(this, p)) {
                manageRequests();
                for (InnerSubscription<T> rp : (InnerSubscription[]) this.subscribers.get()) {
                    this.buffer.replay(rp);
                }
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                this.buffer.next(t);
                for (InnerSubscription<T> rp : (InnerSubscription[]) this.subscribers.get()) {
                    this.buffer.replay(rp);
                }
            }
        }

        public void onError(Throwable e) {
            if (!this.done) {
                this.done = true;
                this.buffer.error(e);
                for (InnerSubscription<T> rp : (InnerSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                    this.buffer.replay(rp);
                }
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.buffer.complete();
                for (InnerSubscription<T> rp : (InnerSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                    this.buffer.replay(rp);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void manageRequests() {
            if (this.management.getAndIncrement() == 0) {
                int missed = 1;
                while (!isDisposed()) {
                    InnerSubscription<T>[] a = (InnerSubscription[]) this.subscribers.get();
                    long ri = this.maxChildRequested;
                    long maxTotalRequests = ri;
                    int length = a.length;
                    for (int i = 0; i < length; i++) {
                        maxTotalRequests = Math.max(maxTotalRequests, a[i].totalRequested.get());
                    }
                    long ur = this.maxUpstreamRequested;
                    Subscription p = (Subscription) get();
                    long diff = maxTotalRequests - ri;
                    if (diff != 0) {
                        this.maxChildRequested = maxTotalRequests;
                        if (p == null) {
                            long u = ur + diff;
                            if (u < 0) {
                                u = Long.MAX_VALUE;
                            }
                            this.maxUpstreamRequested = u;
                        } else if (ur != 0) {
                            this.maxUpstreamRequested = 0;
                            p.request(ur + diff);
                        } else {
                            p.request(diff);
                        }
                    } else if (!(ur == 0 || p == null)) {
                        this.maxUpstreamRequested = 0;
                        p.request(ur);
                    }
                    missed = this.management.addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                }
            }
        }
    }

    static final class InnerSubscription<T> extends AtomicLong implements Subscription, Disposable {
        static final long CANCELLED = Long.MIN_VALUE;
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        boolean emitting;
        Object index;
        boolean missed;
        final ReplaySubscriber<T> parent;
        final AtomicLong totalRequested = new AtomicLong();

        InnerSubscription(ReplaySubscriber<T> parent2, Subscriber<? super T> child2) {
            this.parent = parent2;
            this.child = child2;
        }

        public void request(long n) {
            long r;
            if (SubscriptionHelper.validate(n)) {
                do {
                    r = get();
                    if (r != Long.MIN_VALUE) {
                        if (r >= 0 && n == 0) {
                            return;
                        }
                    } else {
                        return;
                    }
                } while (!compareAndSet(r, BackpressureHelper.addCap(r, n)));
                BackpressureHelper.add(this.totalRequested, n);
                this.parent.manageRequests();
                this.parent.buffer.replay(this);
            }
        }

        public long produced(long n) {
            return BackpressureHelper.producedCancel(this, n);
        }

        public boolean isDisposed() {
            return get() == Long.MIN_VALUE;
        }

        public void cancel() {
            dispose();
        }

        public void dispose() {
            if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.parent.remove(this);
                this.parent.manageRequests();
            }
        }

        /* access modifiers changed from: package-private */
        public <U> U index() {
            return this.index;
        }
    }

    static final class UnboundedReplayBuffer<T> extends ArrayList<Object> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 7063189396499112664L;
        volatile int size;

        UnboundedReplayBuffer(int capacityHint) {
            super(capacityHint);
        }

        public void next(T value) {
            add(NotificationLite.next(value));
            this.size++;
        }

        public void error(Throwable e) {
            add(NotificationLite.error(e));
            this.size++;
        }

        public void complete() {
            add(NotificationLite.complete());
            this.size++;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0014, code lost:
            if (r15.isDisposed() != false) goto L_0x0009;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x0016, code lost:
            r7 = r14.size;
            r2 = (java.lang.Integer) r15.index();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x001e, code lost:
            if (r2 == null) goto L_0x004f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0020, code lost:
            r1 = r2.intValue();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0024, code lost:
            r8 = r15.get();
            r10 = r8;
            r4 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
            if (r8 == 0) goto L_0x0068;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0031, code lost:
            if (r1 >= r7) goto L_0x0068;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x0033, code lost:
            r6 = get(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x003b, code lost:
            if (io.reactivex.internal.util.NotificationLite.accept(r6, r0) != false) goto L_0x0009;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0041, code lost:
            if (r15.isDisposed() != false) goto L_0x0009;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0043, code lost:
            r1 = r1 + 1;
            r8 = r8 - 1;
            r4 = r4 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x004f, code lost:
            r1 = 0;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x0051, code lost:
            r3 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x0052, code lost:
            io.reactivex.exceptions.Exceptions.throwIfFatal(r3);
            r15.dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x005c, code lost:
            if (io.reactivex.internal.util.NotificationLite.isError(r6) != false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0064, code lost:
            r0.onError(r3);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x006c, code lost:
            if (r4 == 0) goto L_0x0080;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:38:0x006e, code lost:
            r15.index = java.lang.Integer.valueOf(r1);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x007b, code lost:
            if (r10 == Long.MAX_VALUE) goto L_0x0080;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x007d, code lost:
            r15.produced(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0080, code lost:
            monitor-enter(r15);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:44:0x0083, code lost:
            if (r15.missed != false) goto L_0x008d;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:45:0x0085, code lost:
            r15.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0088, code lost:
            monitor-exit(r15);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:53:?, code lost:
            r15.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:54:0x0090, code lost:
            monitor-exit(r15);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x000e, code lost:
            r0 = r15.child;
         */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void replay(io.reactivex.internal.operators.flowable.FlowableReplay.InnerSubscription<T> r15) {
            /*
                r14 = this;
                monitor-enter(r15)
                boolean r12 = r15.emitting     // Catch:{ all -> 0x004c }
                if (r12 == 0) goto L_0x000a
                r12 = 1
                r15.missed = r12     // Catch:{ all -> 0x004c }
                monitor-exit(r15)     // Catch:{ all -> 0x004c }
            L_0x0009:
                return
            L_0x000a:
                r12 = 1
                r15.emitting = r12     // Catch:{ all -> 0x004c }
                monitor-exit(r15)     // Catch:{ all -> 0x004c }
                org.reactivestreams.Subscriber<? super T> r0 = r15.child
            L_0x0010:
                boolean r12 = r15.isDisposed()
                if (r12 != 0) goto L_0x0009
                int r7 = r14.size
                java.lang.Object r2 = r15.index()
                java.lang.Integer r2 = (java.lang.Integer) r2
                if (r2 == 0) goto L_0x004f
                int r1 = r2.intValue()
            L_0x0024:
                long r8 = r15.get()
                r10 = r8
                r4 = 0
            L_0x002b:
                r12 = 0
                int r12 = (r8 > r12 ? 1 : (r8 == r12 ? 0 : -1))
                if (r12 == 0) goto L_0x0068
                if (r1 >= r7) goto L_0x0068
                java.lang.Object r6 = r14.get(r1)
                boolean r12 = io.reactivex.internal.util.NotificationLite.accept((java.lang.Object) r6, r0)     // Catch:{ Throwable -> 0x0051 }
                if (r12 != 0) goto L_0x0009
                boolean r12 = r15.isDisposed()
                if (r12 != 0) goto L_0x0009
                int r1 = r1 + 1
                r12 = 1
                long r8 = r8 - r12
                r12 = 1
                long r4 = r4 + r12
                goto L_0x002b
            L_0x004c:
                r12 = move-exception
                monitor-exit(r15)     // Catch:{ all -> 0x004c }
                throw r12
            L_0x004f:
                r1 = 0
                goto L_0x0024
            L_0x0051:
                r3 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r3)
                r15.dispose()
                boolean r12 = io.reactivex.internal.util.NotificationLite.isError(r6)
                if (r12 != 0) goto L_0x0009
                boolean r12 = io.reactivex.internal.util.NotificationLite.isComplete(r6)
                if (r12 != 0) goto L_0x0009
                r0.onError(r3)
                goto L_0x0009
            L_0x0068:
                r12 = 0
                int r12 = (r4 > r12 ? 1 : (r4 == r12 ? 0 : -1))
                if (r12 == 0) goto L_0x0080
                java.lang.Integer r12 = java.lang.Integer.valueOf(r1)
                r15.index = r12
                r12 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r12 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
                if (r12 == 0) goto L_0x0080
                r15.produced(r4)
            L_0x0080:
                monitor-enter(r15)
                boolean r12 = r15.missed     // Catch:{ all -> 0x008a }
                if (r12 != 0) goto L_0x008d
                r12 = 0
                r15.emitting = r12     // Catch:{ all -> 0x008a }
                monitor-exit(r15)     // Catch:{ all -> 0x008a }
                goto L_0x0009
            L_0x008a:
                r12 = move-exception
                monitor-exit(r15)     // Catch:{ all -> 0x008a }
                throw r12
            L_0x008d:
                r12 = 0
                r15.missed = r12     // Catch:{ all -> 0x008a }
                monitor-exit(r15)     // Catch:{ all -> 0x008a }
                goto L_0x0010
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.flowable.FlowableReplay.UnboundedReplayBuffer.replay(io.reactivex.internal.operators.flowable.FlowableReplay$InnerSubscription):void");
        }
    }

    static final class Node extends AtomicReference<Node> {
        private static final long serialVersionUID = 245354315435971818L;
        final long index;
        final Object value;

        Node(Object value2, long index2) {
            this.value = value2;
            this.index = index2;
        }
    }

    static class BoundedReplayBuffer<T> extends AtomicReference<Node> implements ReplayBuffer<T> {
        private static final long serialVersionUID = 2346567790059478686L;
        long index;
        int size;
        Node tail;

        BoundedReplayBuffer() {
            Node n = new Node((Object) null, 0);
            this.tail = n;
            set(n);
        }

        /* access modifiers changed from: package-private */
        public final void addLast(Node n) {
            this.tail.set(n);
            this.tail = n;
            this.size++;
        }

        /* access modifiers changed from: package-private */
        public final void removeFirst() {
            Node next = (Node) ((Node) get()).get();
            if (next == null) {
                throw new IllegalStateException("Empty list!");
            }
            this.size--;
            setFirst(next);
        }

        /* access modifiers changed from: package-private */
        public final void removeSome(int n) {
            Node head = (Node) get();
            while (n > 0) {
                head = (Node) head.get();
                n--;
                this.size--;
            }
            setFirst(head);
        }

        /* access modifiers changed from: package-private */
        public final void setFirst(Node n) {
            set(n);
        }

        public final void next(T value) {
            Object o = enterTransform(NotificationLite.next(value));
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(o, j));
            truncate();
        }

        public final void error(Throwable e) {
            Object o = enterTransform(NotificationLite.error(e));
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(o, j));
            truncateFinal();
        }

        public final void complete() {
            Object o = enterTransform(NotificationLite.complete());
            long j = this.index + 1;
            this.index = j;
            addLast(new Node(o, j));
            truncateFinal();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0014, code lost:
            r6 = r13.get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x001f, code lost:
            if (r6 != Long.MAX_VALUE) goto L_0x0078;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0021, code lost:
            r5 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x0022, code lost:
            r0 = 0;
            r3 = (io.reactivex.internal.operators.flowable.FlowableReplay.Node) r13.index();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x002a, code lost:
            if (r3 != null) goto L_0x0039;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x002c, code lost:
            r3 = getHead();
            r13.index = r3;
            io.reactivex.internal.util.BackpressureHelper.add(r13.totalRequested, r3.index);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x003d, code lost:
            if (r6 == 0) goto L_0x0088;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x003f, code lost:
            r8 = (io.reactivex.internal.operators.flowable.FlowableReplay.Node) r3.get();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0045, code lost:
            if (r8 == null) goto L_0x0088;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0047, code lost:
            r4 = leaveTransform(r8.value);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0053, code lost:
            if (io.reactivex.internal.util.NotificationLite.accept(r4, r13.child) == false) goto L_0x007a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0055, code lost:
            r13.index = null;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0059, code lost:
            r2 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x005a, code lost:
            io.reactivex.exceptions.Exceptions.throwIfFatal(r2);
            r13.index = null;
            r13.dispose();
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x0067, code lost:
            if (io.reactivex.internal.util.NotificationLite.isError(r4) != false) goto L_?;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x006f, code lost:
            r13.child.onError(r2);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x0078, code lost:
            r5 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x007a, code lost:
            r0 = r0 + 1;
            r6 = r6 - 1;
            r3 = r8;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x0085, code lost:
            if (r13.isDisposed() == false) goto L_0x0039;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:39:0x008c, code lost:
            if (r0 == 0) goto L_0x0095;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x008e, code lost:
            r13.index = r3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:41:0x0090, code lost:
            if (r5 != false) goto L_0x0095;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:42:0x0092, code lost:
            r13.produced(r0);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:43:0x0095, code lost:
            monitor-enter(r13);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:46:0x0098, code lost:
            if (r13.missed != false) goto L_0x00a3;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:47:0x009a, code lost:
            r13.emitting = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:48:0x009d, code lost:
            monitor-exit(r13);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:55:?, code lost:
            r13.missed = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:56:0x00a6, code lost:
            monitor-exit(r13);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:72:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:74:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0012, code lost:
            if (r13.isDisposed() != false) goto L_0x0009;
         */
        /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void replay(io.reactivex.internal.operators.flowable.FlowableReplay.InnerSubscription<T> r13) {
            /*
                r12 = this;
                monitor-enter(r13)
                boolean r9 = r13.emitting     // Catch:{ all -> 0x0075 }
                if (r9 == 0) goto L_0x000a
                r9 = 1
                r13.missed = r9     // Catch:{ all -> 0x0075 }
                monitor-exit(r13)     // Catch:{ all -> 0x0075 }
            L_0x0009:
                return
            L_0x000a:
                r9 = 1
                r13.emitting = r9     // Catch:{ all -> 0x0075 }
                monitor-exit(r13)     // Catch:{ all -> 0x0075 }
            L_0x000e:
                boolean r9 = r13.isDisposed()
                if (r9 != 0) goto L_0x0009
                long r6 = r13.get()
                r10 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r9 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
                if (r9 != 0) goto L_0x0078
                r5 = 1
            L_0x0022:
                r0 = 0
                java.lang.Object r3 = r13.index()
                io.reactivex.internal.operators.flowable.FlowableReplay$Node r3 = (io.reactivex.internal.operators.flowable.FlowableReplay.Node) r3
                if (r3 != 0) goto L_0x0039
                io.reactivex.internal.operators.flowable.FlowableReplay$Node r3 = r12.getHead()
                r13.index = r3
                java.util.concurrent.atomic.AtomicLong r9 = r13.totalRequested
                long r10 = r3.index
                io.reactivex.internal.util.BackpressureHelper.add(r9, r10)
            L_0x0039:
                r10 = 0
                int r9 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
                if (r9 == 0) goto L_0x0088
                java.lang.Object r8 = r3.get()
                io.reactivex.internal.operators.flowable.FlowableReplay$Node r8 = (io.reactivex.internal.operators.flowable.FlowableReplay.Node) r8
                if (r8 == 0) goto L_0x0088
                java.lang.Object r9 = r8.value
                java.lang.Object r4 = r12.leaveTransform(r9)
                org.reactivestreams.Subscriber<? super T> r9 = r13.child     // Catch:{ Throwable -> 0x0059 }
                boolean r9 = io.reactivex.internal.util.NotificationLite.accept((java.lang.Object) r4, r9)     // Catch:{ Throwable -> 0x0059 }
                if (r9 == 0) goto L_0x007a
                r9 = 0
                r13.index = r9     // Catch:{ Throwable -> 0x0059 }
                goto L_0x0009
            L_0x0059:
                r2 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r2)
                r9 = 0
                r13.index = r9
                r13.dispose()
                boolean r9 = io.reactivex.internal.util.NotificationLite.isError(r4)
                if (r9 != 0) goto L_0x0009
                boolean r9 = io.reactivex.internal.util.NotificationLite.isComplete(r4)
                if (r9 != 0) goto L_0x0009
                org.reactivestreams.Subscriber<? super T> r9 = r13.child
                r9.onError(r2)
                goto L_0x0009
            L_0x0075:
                r9 = move-exception
                monitor-exit(r13)     // Catch:{ all -> 0x0075 }
                throw r9
            L_0x0078:
                r5 = 0
                goto L_0x0022
            L_0x007a:
                r10 = 1
                long r0 = r0 + r10
                r10 = 1
                long r6 = r6 - r10
                r3 = r8
                boolean r9 = r13.isDisposed()
                if (r9 == 0) goto L_0x0039
                goto L_0x0009
            L_0x0088:
                r10 = 0
                int r9 = (r0 > r10 ? 1 : (r0 == r10 ? 0 : -1))
                if (r9 == 0) goto L_0x0095
                r13.index = r3
                if (r5 != 0) goto L_0x0095
                r13.produced(r0)
            L_0x0095:
                monitor-enter(r13)
                boolean r9 = r13.missed     // Catch:{ all -> 0x00a0 }
                if (r9 != 0) goto L_0x00a3
                r9 = 0
                r13.emitting = r9     // Catch:{ all -> 0x00a0 }
                monitor-exit(r13)     // Catch:{ all -> 0x00a0 }
                goto L_0x0009
            L_0x00a0:
                r9 = move-exception
                monitor-exit(r13)     // Catch:{ all -> 0x00a0 }
                throw r9
            L_0x00a3:
                r9 = 0
                r13.missed = r9     // Catch:{ all -> 0x00a0 }
                monitor-exit(r13)     // Catch:{ all -> 0x00a0 }
                goto L_0x000e
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.flowable.FlowableReplay.BoundedReplayBuffer.replay(io.reactivex.internal.operators.flowable.FlowableReplay$InnerSubscription):void");
        }

        /* access modifiers changed from: package-private */
        public Object enterTransform(Object value) {
            return value;
        }

        /* access modifiers changed from: package-private */
        public Object leaveTransform(Object value) {
            return value;
        }

        /* access modifiers changed from: package-private */
        public void truncate() {
        }

        /* access modifiers changed from: package-private */
        public void truncateFinal() {
        }

        /* access modifiers changed from: package-private */
        public final void collect(Collection<? super T> output) {
            Node n = getHead();
            while (true) {
                Node next = (Node) n.get();
                if (next != null) {
                    Object v = leaveTransform(next.value);
                    if (!NotificationLite.isComplete(v) && !NotificationLite.isError(v)) {
                        output.add(NotificationLite.getValue(v));
                        n = next;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean hasError() {
            return this.tail.value != null && NotificationLite.isError(leaveTransform(this.tail.value));
        }

        /* access modifiers changed from: package-private */
        public boolean hasCompleted() {
            return this.tail.value != null && NotificationLite.isComplete(leaveTransform(this.tail.value));
        }

        /* access modifiers changed from: package-private */
        public Node getHead() {
            return (Node) get();
        }
    }

    static final class SizeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = -5898283885385201806L;
        final int limit;

        SizeBoundReplayBuffer(int limit2) {
            this.limit = limit2;
        }

        /* access modifiers changed from: package-private */
        public void truncate() {
            if (this.size > this.limit) {
                removeFirst();
            }
        }
    }

    static final class SizeAndTimeBoundReplayBuffer<T> extends BoundedReplayBuffer<T> {
        private static final long serialVersionUID = 3457957419649567404L;
        final int limit;
        final long maxAge;
        final Scheduler scheduler;
        final TimeUnit unit;

        SizeAndTimeBoundReplayBuffer(int limit2, long maxAge2, TimeUnit unit2, Scheduler scheduler2) {
            this.scheduler = scheduler2;
            this.limit = limit2;
            this.maxAge = maxAge2;
            this.unit = unit2;
        }

        /* access modifiers changed from: package-private */
        public Object enterTransform(Object value) {
            return new Timed(value, this.scheduler.now(this.unit), this.unit);
        }

        /* access modifiers changed from: package-private */
        public Object leaveTransform(Object value) {
            return ((Timed) value).value();
        }

        /* access modifiers changed from: package-private */
        public void truncate() {
            long timeLimit = this.scheduler.now(this.unit) - this.maxAge;
            Node prev = (Node) get();
            Node next = (Node) prev.get();
            int e = 0;
            while (next != null) {
                if (this.size <= this.limit) {
                    if (((Timed) next.value).time() > timeLimit) {
                        break;
                    }
                    e++;
                    this.size--;
                    prev = next;
                    next = (Node) next.get();
                } else {
                    e++;
                    this.size--;
                    prev = next;
                    next = (Node) next.get();
                }
            }
            if (e != 0) {
                setFirst(prev);
            }
        }

        /* access modifiers changed from: package-private */
        public void truncateFinal() {
            long timeLimit = this.scheduler.now(this.unit) - this.maxAge;
            Node prev = (Node) get();
            Node next = (Node) prev.get();
            int e = 0;
            while (next != null && this.size > 1 && ((Timed) next.value).time() <= timeLimit) {
                e++;
                this.size--;
                prev = next;
                next = (Node) next.get();
            }
            if (e != 0) {
                setFirst(prev);
            }
        }

        /* access modifiers changed from: package-private */
        public Node getHead() {
            long timeLimit = this.scheduler.now(this.unit) - this.maxAge;
            Node prev = (Node) get();
            for (Node next = (Node) prev.get(); next != null; next = (Node) next.get()) {
                Timed<?> v = (Timed) next.value;
                if (NotificationLite.isComplete(v.value()) || NotificationLite.isError(v.value()) || v.time() > timeLimit) {
                    break;
                }
                prev = next;
            }
            return prev;
        }
    }

    static final class MultiCastPublisher<R, U> implements Publisher<R> {
        private final Callable<? extends ConnectableFlowable<U>> connectableFactory;
        private final Function<? super Flowable<U>, ? extends Publisher<R>> selector;

        MultiCastPublisher(Callable<? extends ConnectableFlowable<U>> connectableFactory2, Function<? super Flowable<U>, ? extends Publisher<R>> selector2) {
            this.connectableFactory = connectableFactory2;
            this.selector = selector2;
        }

        public void subscribe(Subscriber<? super R> child) {
            try {
                ConnectableFlowable<U> co = (ConnectableFlowable) ObjectHelper.requireNonNull(this.connectableFactory.call(), "The connectableFactory returned null");
                try {
                    Publisher<R> observable = (Publisher) ObjectHelper.requireNonNull(this.selector.apply(co), "The selector returned a null Publisher");
                    SubscriberResourceWrapper<R> srw = new SubscriberResourceWrapper<>(child);
                    observable.subscribe(srw);
                    co.connect(new DisposableConsumer(srw));
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    EmptySubscription.error(e, child);
                }
            } catch (Throwable e2) {
                Exceptions.throwIfFatal(e2);
                EmptySubscription.error(e2, child);
            }
        }

        final class DisposableConsumer implements Consumer<Disposable> {
            private final SubscriberResourceWrapper<R> srw;

            DisposableConsumer(SubscriberResourceWrapper<R> srw2) {
                this.srw = srw2;
            }

            public void accept(Disposable r) {
                this.srw.setResource(r);
            }
        }
    }

    static final class ConnectableFlowableReplay<T> extends ConnectableFlowable<T> {
        private final ConnectableFlowable<T> co;
        private final Flowable<T> observable;

        ConnectableFlowableReplay(ConnectableFlowable<T> co2, Flowable<T> observable2) {
            this.co = co2;
            this.observable = observable2;
        }

        public void connect(Consumer<? super Disposable> connection) {
            this.co.connect(connection);
        }

        /* access modifiers changed from: protected */
        public void subscribeActual(Subscriber<? super T> s) {
            this.observable.subscribe(s);
        }
    }

    static final class ReplayBufferTask<T> implements Callable<ReplayBuffer<T>> {
        private final int bufferSize;

        ReplayBufferTask(int bufferSize2) {
            this.bufferSize = bufferSize2;
        }

        public ReplayBuffer<T> call() {
            return new SizeBoundReplayBuffer(this.bufferSize);
        }
    }

    static final class ScheduledReplayBufferTask<T> implements Callable<ReplayBuffer<T>> {
        private final int bufferSize;
        private final long maxAge;
        private final Scheduler scheduler;
        private final TimeUnit unit;

        ScheduledReplayBufferTask(int bufferSize2, long maxAge2, TimeUnit unit2, Scheduler scheduler2) {
            this.bufferSize = bufferSize2;
            this.maxAge = maxAge2;
            this.unit = unit2;
            this.scheduler = scheduler2;
        }

        public ReplayBuffer<T> call() {
            return new SizeAndTimeBoundReplayBuffer(this.bufferSize, this.maxAge, this.unit, this.scheduler);
        }
    }

    static final class ReplayPublisher<T> implements Publisher<T> {
        private final Callable<? extends ReplayBuffer<T>> bufferFactory;
        private final AtomicReference<ReplaySubscriber<T>> curr;

        ReplayPublisher(AtomicReference<ReplaySubscriber<T>> curr2, Callable<? extends ReplayBuffer<T>> bufferFactory2) {
            this.curr = curr2;
            this.bufferFactory = bufferFactory2;
        }

        /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START, MTH_ENTER_BLOCK] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void subscribe(org.reactivestreams.Subscriber<? super T> r8) {
            /*
                r7 = this;
            L_0x0000:
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber<T>> r5 = r7.curr
                java.lang.Object r3 = r5.get()
                io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber r3 = (io.reactivex.internal.operators.flowable.FlowableReplay.ReplaySubscriber) r3
                if (r3 != 0) goto L_0x0021
                java.util.concurrent.Callable<? extends io.reactivex.internal.operators.flowable.FlowableReplay$ReplayBuffer<T>> r5 = r7.bufferFactory     // Catch:{ Throwable -> 0x0036 }
                java.lang.Object r0 = r5.call()     // Catch:{ Throwable -> 0x0036 }
                io.reactivex.internal.operators.flowable.FlowableReplay$ReplayBuffer r0 = (io.reactivex.internal.operators.flowable.FlowableReplay.ReplayBuffer) r0     // Catch:{ Throwable -> 0x0036 }
                io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber r4 = new io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber
                r4.<init>(r0)
                java.util.concurrent.atomic.AtomicReference<io.reactivex.internal.operators.flowable.FlowableReplay$ReplaySubscriber<T>> r5 = r7.curr
                r6 = 0
                boolean r5 = r5.compareAndSet(r6, r4)
                if (r5 == 0) goto L_0x0000
                r3 = r4
            L_0x0021:
                io.reactivex.internal.operators.flowable.FlowableReplay$InnerSubscription r2 = new io.reactivex.internal.operators.flowable.FlowableReplay$InnerSubscription
                r2.<init>(r3, r8)
                r8.onSubscribe(r2)
                r3.add(r2)
                boolean r5 = r2.isDisposed()
                if (r5 == 0) goto L_0x003f
                r3.remove(r2)
            L_0x0035:
                return
            L_0x0036:
                r1 = move-exception
                io.reactivex.exceptions.Exceptions.throwIfFatal(r1)
                java.lang.RuntimeException r5 = io.reactivex.internal.util.ExceptionHelper.wrapOrThrow(r1)
                throw r5
            L_0x003f:
                r3.manageRequests()
                io.reactivex.internal.operators.flowable.FlowableReplay$ReplayBuffer<T> r5 = r3.buffer
                r5.replay(r2)
                goto L_0x0035
            */
            throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.flowable.FlowableReplay.ReplayPublisher.subscribe(org.reactivestreams.Subscriber):void");
        }
    }

    static final class DefaultUnboundedFactory implements Callable<Object> {
        DefaultUnboundedFactory() {
        }

        public Object call() {
            return new UnboundedReplayBuffer(16);
        }
    }
}
