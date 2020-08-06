package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTakeLast<T> extends AbstractFlowableWithUpstream<T, T> {
    final int count;

    public FlowableTakeLast(Flowable<T> source, int count2) {
        super(source);
        this.count = count2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe(new TakeLastSubscriber(s, this.count));
    }

    static final class TakeLastSubscriber<T> extends ArrayDeque<T> implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = 7240042530241604978L;
        final Subscriber<? super T> actual;
        volatile boolean cancelled;
        final int count;
        volatile boolean done;
        final AtomicLong requested = new AtomicLong();
        Subscription s;
        final AtomicInteger wip = new AtomicInteger();

        TakeLastSubscriber(Subscriber<? super T> actual2, int count2) {
            this.actual = actual2;
            this.count = count2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
                s2.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (this.count == size()) {
                poll();
            }
            offer(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this.requested, n);
                drain();
            }
        }

        public void cancel() {
            this.cancelled = true;
            this.s.cancel();
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (this.wip.getAndIncrement() == 0) {
                Subscriber<? super T> a = this.actual;
                long r = this.requested.get();
                while (!this.cancelled) {
                    if (this.done) {
                        long e = 0;
                        while (e != r) {
                            if (!this.cancelled) {
                                T v = poll();
                                if (v == null) {
                                    a.onComplete();
                                    return;
                                } else {
                                    a.onNext(v);
                                    e++;
                                }
                            } else {
                                return;
                            }
                        }
                        if (!(e == 0 || r == Long.MAX_VALUE)) {
                            r = this.requested.addAndGet(-e);
                        }
                    }
                    if (this.wip.decrementAndGet() == 0) {
                        return;
                    }
                }
            }
        }
    }
}
