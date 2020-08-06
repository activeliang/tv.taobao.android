package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSubscribeOn<T> extends AbstractFlowableWithUpstream<T, T> {
    final boolean nonScheduledRequests;
    final Scheduler scheduler;

    public FlowableSubscribeOn(Flowable<T> source, Scheduler scheduler2, boolean nonScheduledRequests2) {
        super(source);
        this.scheduler = scheduler2;
        this.nonScheduledRequests = nonScheduledRequests2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        Scheduler.Worker w = this.scheduler.createWorker();
        SubscribeOnSubscriber<T> sos = new SubscribeOnSubscriber<>(s, w, this.source, this.nonScheduledRequests);
        s.onSubscribe(sos);
        w.schedule(sos);
    }

    static final class SubscribeOnSubscriber<T> extends AtomicReference<Thread> implements FlowableSubscriber<T>, Subscription, Runnable {
        private static final long serialVersionUID = 8094547886072529208L;
        final Subscriber<? super T> actual;
        final boolean nonScheduledRequests;
        final AtomicLong requested = new AtomicLong();
        final AtomicReference<Subscription> s = new AtomicReference<>();
        Publisher<T> source;
        final Scheduler.Worker worker;

        SubscribeOnSubscriber(Subscriber<? super T> actual2, Scheduler.Worker worker2, Publisher<T> source2, boolean requestOn) {
            this.actual = actual2;
            this.worker = worker2;
            this.source = source2;
            this.nonScheduledRequests = !requestOn;
        }

        public void run() {
            lazySet(Thread.currentThread());
            Publisher<T> src = this.source;
            this.source = null;
            src.subscribe(this);
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.setOnce(this.s, s2)) {
                long r = this.requested.getAndSet(0);
                if (r != 0) {
                    requestUpstream(r, s2);
                }
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
            this.worker.dispose();
        }

        public void onComplete() {
            this.actual.onComplete();
            this.worker.dispose();
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                Subscription s2 = this.s.get();
                if (s2 != null) {
                    requestUpstream(n, s2);
                    return;
                }
                BackpressureHelper.add(this.requested, n);
                Subscription s3 = this.s.get();
                if (s3 != null) {
                    long r = this.requested.getAndSet(0);
                    if (r != 0) {
                        requestUpstream(r, s3);
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void requestUpstream(long n, Subscription s2) {
            if (this.nonScheduledRequests || Thread.currentThread() == get()) {
                s2.request(n);
            } else {
                this.worker.schedule(new Request(s2, n));
            }
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.s);
            this.worker.dispose();
        }

        static final class Request implements Runnable {
            private final long n;
            private final Subscription s;

            Request(Subscription s2, long n2) {
                this.s = s2;
                this.n = n2;
            }

            public void run() {
                this.s.request(this.n);
            }
        }
    }
}
