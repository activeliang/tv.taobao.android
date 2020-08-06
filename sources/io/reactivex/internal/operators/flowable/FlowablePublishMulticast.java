package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.QueueDrainHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowablePublishMulticast<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final boolean delayError;
    final int prefetch;
    final Function<? super Flowable<T>, ? extends Publisher<? extends R>> selector;

    public FlowablePublishMulticast(Flowable<T> source, Function<? super Flowable<T>, ? extends Publisher<? extends R>> selector2, int prefetch2, boolean delayError2) {
        super(source);
        this.selector = selector2;
        this.prefetch = prefetch2;
        this.delayError = delayError2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> s) {
        MulticastProcessor<T> mp = new MulticastProcessor<>(this.prefetch, this.delayError);
        try {
            ((Publisher) ObjectHelper.requireNonNull(this.selector.apply(mp), "selector returned a null Publisher")).subscribe(new OutputCanceller<>(s, mp));
            this.source.subscribe(mp);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptySubscription.error(ex, s);
        }
    }

    static final class OutputCanceller<R> implements FlowableSubscriber<R>, Subscription {
        final Subscriber<? super R> actual;
        final MulticastProcessor<?> processor;
        Subscription s;

        OutputCanceller(Subscriber<? super R> actual2, MulticastProcessor<?> processor2) {
            this.actual = actual2;
            this.processor = processor2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(R t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
            this.processor.dispose();
        }

        public void onComplete() {
            this.actual.onComplete();
            this.processor.dispose();
        }

        public void request(long n) {
            this.s.request(n);
        }

        public void cancel() {
            this.s.cancel();
            this.processor.dispose();
        }
    }

    static final class MulticastProcessor<T> extends Flowable<T> implements FlowableSubscriber<T>, Disposable {
        static final MulticastSubscription[] EMPTY = new MulticastSubscription[0];
        static final MulticastSubscription[] TERMINATED = new MulticastSubscription[0];
        int consumed;
        final boolean delayError;
        volatile boolean done;
        Throwable error;
        final int limit;
        final int prefetch;
        volatile SimpleQueue<T> queue;
        final AtomicReference<Subscription> s = new AtomicReference<>();
        int sourceMode;
        final AtomicReference<MulticastSubscription<T>[]> subscribers = new AtomicReference<>(EMPTY);
        final AtomicInteger wip = new AtomicInteger();

        MulticastProcessor(int prefetch2, boolean delayError2) {
            this.prefetch = prefetch2;
            this.limit = prefetch2 - (prefetch2 >> 2);
            this.delayError = delayError2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.setOnce(this.s, s2)) {
                if (s2 instanceof QueueSubscription) {
                    QueueSubscription<T> qs = (QueueSubscription) s2;
                    int m = qs.requestFusion(3);
                    if (m == 1) {
                        this.sourceMode = m;
                        this.queue = qs;
                        this.done = true;
                        drain();
                        return;
                    } else if (m == 2) {
                        this.sourceMode = m;
                        this.queue = qs;
                        QueueDrainHelper.request(s2, this.prefetch);
                        return;
                    }
                }
                this.queue = QueueDrainHelper.createQueue(this.prefetch);
                QueueDrainHelper.request(s2, this.prefetch);
            }
        }

        public void dispose() {
            SimpleQueue<T> q;
            SubscriptionHelper.cancel(this.s);
            if (this.wip.getAndIncrement() == 0 && (q = this.queue) != null) {
                q.clear();
            }
        }

        public boolean isDisposed() {
            return SubscriptionHelper.isCancelled(this.s.get());
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.sourceMode != 0 || this.queue.offer(t)) {
                    drain();
                    return;
                }
                this.s.get().cancel();
                onError(new MissingBackpressureException());
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.error = t;
            this.done = true;
            drain();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        /* access modifiers changed from: package-private */
        public boolean add(MulticastSubscription<T> s2) {
            MulticastSubscription<T>[] current;
            MulticastSubscription<T>[] next;
            do {
                current = (MulticastSubscription[]) this.subscribers.get();
                if (current == TERMINATED) {
                    return false;
                }
                int n = current.length;
                next = new MulticastSubscription[(n + 1)];
                System.arraycopy(current, 0, next, 0, n);
                next[n] = s2;
            } while (!this.subscribers.compareAndSet(current, next));
            return true;
        }

        /* access modifiers changed from: package-private */
        public void remove(MulticastSubscription<T> s2) {
            MulticastSubscription<T>[] current;
            MulticastSubscription<T>[] next;
            do {
                current = (MulticastSubscription[]) this.subscribers.get();
                if (current != TERMINATED && current != EMPTY) {
                    int n = current.length;
                    int j = -1;
                    int i = 0;
                    while (true) {
                        if (i >= n) {
                            break;
                        } else if (current[i] == s2) {
                            j = i;
                            break;
                        } else {
                            i++;
                        }
                    }
                    if (j < 0) {
                        return;
                    }
                    if (n == 1) {
                        next = EMPTY;
                    } else {
                        next = new MulticastSubscription[(n - 1)];
                        System.arraycopy(current, 0, next, 0, j);
                        System.arraycopy(current, j + 1, next, j, (n - j) - 1);
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(current, next));
        }

        /* access modifiers changed from: protected */
        public void subscribeActual(Subscriber<? super T> s2) {
            MulticastSubscription<T> ms = new MulticastSubscription<>(s2, this);
            s2.onSubscribe(ms);
            if (!add(ms)) {
                Throwable ex = this.error;
                if (ex != null) {
                    s2.onError(ex);
                } else {
                    s2.onComplete();
                }
            } else if (ms.isCancelled()) {
                remove(ms);
            } else {
                drain();
            }
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            Throwable ex;
            Throwable ex2;
            if (this.wip.getAndIncrement() == 0) {
                int missed = 1;
                SimpleQueue<T> q = this.queue;
                int upstreamConsumed = this.consumed;
                int localLimit = this.limit;
                boolean canRequest = this.sourceMode != 1;
                while (true) {
                    MulticastSubscription<T>[] array = (MulticastSubscription[]) this.subscribers.get();
                    int n = array.length;
                    if (!(q == null || n == 0)) {
                        long r = Long.MAX_VALUE;
                        int length = array.length;
                        for (int i = 0; i < length; i++) {
                            long u = array[i].get();
                            if (u != Long.MIN_VALUE && r > u) {
                                r = u;
                            }
                        }
                        long e = 0;
                        while (e != r) {
                            if (isDisposed()) {
                                q.clear();
                                return;
                            }
                            boolean d = this.done;
                            if (!d || this.delayError || (ex2 = this.error) == null) {
                                try {
                                    T v = q.poll();
                                    boolean empty = v == null;
                                    if (d && empty) {
                                        Throwable ex3 = this.error;
                                        if (ex3 != null) {
                                            errorAll(ex3);
                                            return;
                                        } else {
                                            completeAll();
                                            return;
                                        }
                                    } else if (empty) {
                                        break;
                                    } else {
                                        int length2 = array.length;
                                        for (int i2 = 0; i2 < length2; i2++) {
                                            MulticastSubscription<T> ms = array[i2];
                                            if (ms.get() != Long.MIN_VALUE) {
                                                ms.actual.onNext(v);
                                            }
                                        }
                                        e++;
                                        if (canRequest) {
                                            upstreamConsumed++;
                                            if (upstreamConsumed == localLimit) {
                                                upstreamConsumed = 0;
                                                this.s.get().request((long) localLimit);
                                            }
                                        }
                                    }
                                } catch (Throwable ex4) {
                                    Exceptions.throwIfFatal(ex4);
                                    SubscriptionHelper.cancel(this.s);
                                    errorAll(ex4);
                                    return;
                                }
                            } else {
                                errorAll(ex2);
                                return;
                            }
                        }
                        if (e == r) {
                            if (isDisposed()) {
                                q.clear();
                                return;
                            }
                            boolean d2 = this.done;
                            if (d2 && !this.delayError && (ex = this.error) != null) {
                                errorAll(ex);
                                return;
                            } else if (d2 && q.isEmpty()) {
                                Throwable ex5 = this.error;
                                if (ex5 != null) {
                                    errorAll(ex5);
                                    return;
                                } else {
                                    completeAll();
                                    return;
                                }
                            }
                        }
                        int length3 = array.length;
                        for (int i3 = 0; i3 < length3; i3++) {
                            BackpressureHelper.produced(array[i3], e);
                        }
                    }
                    this.consumed = upstreamConsumed;
                    missed = this.wip.addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                    if (q == null) {
                        q = this.queue;
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void errorAll(Throwable ex) {
            for (MulticastSubscription<T> ms : (MulticastSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                if (ms.get() != Long.MIN_VALUE) {
                    ms.actual.onError(ex);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void completeAll() {
            for (MulticastSubscription<T> ms : (MulticastSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                if (ms.get() != Long.MIN_VALUE) {
                    ms.actual.onComplete();
                }
            }
        }
    }

    static final class MulticastSubscription<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = 8664815189257569791L;
        final Subscriber<? super T> actual;
        final MulticastProcessor<T> parent;

        MulticastSubscription(Subscriber<? super T> actual2, MulticastProcessor<T> parent2) {
            this.actual = actual2;
            this.parent = parent2;
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.addCancel(this, n);
                this.parent.drain();
            }
        }

        public void cancel() {
            if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.parent.remove(this);
                this.parent.drain();
            }
        }

        public boolean isCancelled() {
            return get() == Long.MIN_VALUE;
        }
    }
}
