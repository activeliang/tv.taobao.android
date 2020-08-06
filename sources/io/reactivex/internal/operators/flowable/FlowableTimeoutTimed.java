package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTimeoutTimed<T> extends AbstractFlowableWithUpstream<T, T> {
    final Publisher<? extends T> other;
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    interface TimeoutSupport {
        void onTimeout(long j);
    }

    public FlowableTimeoutTimed(Flowable<T> source, long timeout2, TimeUnit unit2, Scheduler scheduler2, Publisher<? extends T> other2) {
        super(source);
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        if (this.other == null) {
            TimeoutSubscriber<T> parent = new TimeoutSubscriber<>(s, this.timeout, this.unit, this.scheduler.createWorker());
            s.onSubscribe(parent);
            parent.startTimeout(0);
            this.source.subscribe(parent);
            return;
        }
        TimeoutFallbackSubscriber<T> parent2 = new TimeoutFallbackSubscriber<>(s, this.timeout, this.unit, this.scheduler.createWorker(), this.other);
        s.onSubscribe(parent2);
        parent2.startTimeout(0);
        this.source.subscribe(parent2);
    }

    static final class TimeoutSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription, TimeoutSupport {
        private static final long serialVersionUID = 3764492702657003550L;
        final Subscriber<? super T> actual;
        final AtomicLong requested = new AtomicLong();
        final SequentialDisposable task = new SequentialDisposable();
        final long timeout;
        final TimeUnit unit;
        final AtomicReference<Subscription> upstream = new AtomicReference<>();
        final Scheduler.Worker worker;

        TimeoutSubscriber(Subscriber<? super T> actual2, long timeout2, TimeUnit unit2, Scheduler.Worker worker2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
        }

        public void onSubscribe(Subscription s) {
            SubscriptionHelper.deferredSetOnce(this.upstream, this.requested, s);
        }

        public void onNext(T t) {
            long idx = get();
            if (idx != Long.MAX_VALUE && compareAndSet(idx, idx + 1)) {
                ((Disposable) this.task.get()).dispose();
                this.actual.onNext(t);
                startTimeout(idx + 1);
            }
        }

        /* access modifiers changed from: package-private */
        public void startTimeout(long nextIndex) {
            this.task.replace(this.worker.schedule(new TimeoutTask(nextIndex, this), this.timeout, this.unit));
        }

        public void onError(Throwable t) {
            if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onError(t);
                this.worker.dispose();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onComplete();
                this.worker.dispose();
            }
        }

        public void onTimeout(long idx) {
            if (compareAndSet(idx, Long.MAX_VALUE)) {
                SubscriptionHelper.cancel(this.upstream);
                this.actual.onError(new TimeoutException());
                this.worker.dispose();
            }
        }

        public void request(long n) {
            SubscriptionHelper.deferredRequest(this.upstream, this.requested, n);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.upstream);
            this.worker.dispose();
        }
    }

    static final class TimeoutTask implements Runnable {
        final long idx;
        final TimeoutSupport parent;

        TimeoutTask(long idx2, TimeoutSupport parent2) {
            this.idx = idx2;
            this.parent = parent2;
        }

        public void run() {
            this.parent.onTimeout(this.idx);
        }
    }

    static final class TimeoutFallbackSubscriber<T> extends SubscriptionArbiter implements FlowableSubscriber<T>, TimeoutSupport {
        private static final long serialVersionUID = 3764492702657003550L;
        final Subscriber<? super T> actual;
        long consumed;
        Publisher<? extends T> fallback;
        final AtomicLong index = new AtomicLong();
        final SequentialDisposable task = new SequentialDisposable();
        final long timeout;
        final TimeUnit unit;
        final AtomicReference<Subscription> upstream = new AtomicReference<>();
        final Scheduler.Worker worker;

        TimeoutFallbackSubscriber(Subscriber<? super T> actual2, long timeout2, TimeUnit unit2, Scheduler.Worker worker2, Publisher<? extends T> fallback2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
            this.fallback = fallback2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this.upstream, s)) {
                setSubscription(s);
            }
        }

        public void onNext(T t) {
            long idx = this.index.get();
            if (idx != Long.MAX_VALUE && this.index.compareAndSet(idx, idx + 1)) {
                ((Disposable) this.task.get()).dispose();
                this.consumed++;
                this.actual.onNext(t);
                startTimeout(idx + 1);
            }
        }

        /* access modifiers changed from: package-private */
        public void startTimeout(long nextIndex) {
            this.task.replace(this.worker.schedule(new TimeoutTask(nextIndex, this), this.timeout, this.unit));
        }

        public void onError(Throwable t) {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onError(t);
                this.worker.dispose();
                return;
            }
            RxJavaPlugins.onError(t);
        }

        public void onComplete() {
            if (this.index.getAndSet(Long.MAX_VALUE) != Long.MAX_VALUE) {
                this.task.dispose();
                this.actual.onComplete();
                this.worker.dispose();
            }
        }

        public void onTimeout(long idx) {
            if (this.index.compareAndSet(idx, Long.MAX_VALUE)) {
                SubscriptionHelper.cancel(this.upstream);
                long c = this.consumed;
                if (c != 0) {
                    produced(c);
                }
                Publisher<? extends T> f = this.fallback;
                this.fallback = null;
                f.subscribe(new FallbackSubscriber(this.actual, this));
                this.worker.dispose();
            }
        }

        public void cancel() {
            super.cancel();
            this.worker.dispose();
        }
    }

    static final class FallbackSubscriber<T> implements FlowableSubscriber<T> {
        final Subscriber<? super T> actual;
        final SubscriptionArbiter arbiter;

        FallbackSubscriber(Subscriber<? super T> actual2, SubscriptionArbiter arbiter2) {
            this.actual = actual2;
            this.arbiter = arbiter2;
        }

        public void onSubscribe(Subscription s) {
            this.arbiter.setSubscription(s);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }
}
