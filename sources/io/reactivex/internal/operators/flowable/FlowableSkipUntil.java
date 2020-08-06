package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableSkipUntil<T, U> extends AbstractFlowableWithUpstream<T, T> {
    final Publisher<U> other;

    public FlowableSkipUntil(Flowable<T> source, Publisher<U> other2) {
        super(source);
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> child) {
        SkipUntilMainSubscriber<T> parent = new SkipUntilMainSubscriber<>(child);
        child.onSubscribe(parent);
        this.other.subscribe(parent.other);
        this.source.subscribe(parent);
    }

    static final class SkipUntilMainSubscriber<T> extends AtomicInteger implements ConditionalSubscriber<T>, Subscription {
        private static final long serialVersionUID = -6270983465606289181L;
        final Subscriber<? super T> actual;
        final AtomicThrowable error = new AtomicThrowable();
        volatile boolean gate;
        final SkipUntilMainSubscriber<T>.OtherSubscriber other = new OtherSubscriber();
        final AtomicLong requested = new AtomicLong();
        final AtomicReference<Subscription> s = new AtomicReference<>();

        SkipUntilMainSubscriber(Subscriber<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Subscription s2) {
            SubscriptionHelper.deferredSetOnce(this.s, this.requested, s2);
        }

        public void onNext(T t) {
            if (!tryOnNext(t)) {
                this.s.get().request(1);
            }
        }

        public boolean tryOnNext(T t) {
            if (!this.gate) {
                return false;
            }
            HalfSerializer.onNext(this.actual, t, (AtomicInteger) this, this.error);
            return true;
        }

        public void onError(Throwable t) {
            SubscriptionHelper.cancel(this.other);
            HalfSerializer.onError((Subscriber<?>) this.actual, t, (AtomicInteger) this, this.error);
        }

        public void onComplete() {
            SubscriptionHelper.cancel(this.other);
            HalfSerializer.onComplete((Subscriber<?>) this.actual, (AtomicInteger) this, this.error);
        }

        public void request(long n) {
            SubscriptionHelper.deferredRequest(this.s, this.requested, n);
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.s);
            SubscriptionHelper.cancel(this.other);
        }

        final class OtherSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
            private static final long serialVersionUID = -5592042965931999169L;

            OtherSubscriber() {
            }

            public void onSubscribe(Subscription s) {
                if (SubscriptionHelper.setOnce(this, s)) {
                    s.request(Long.MAX_VALUE);
                }
            }

            public void onNext(Object t) {
                SkipUntilMainSubscriber.this.gate = true;
                ((Subscription) get()).cancel();
            }

            public void onError(Throwable t) {
                SubscriptionHelper.cancel(SkipUntilMainSubscriber.this.s);
                HalfSerializer.onError((Subscriber<?>) SkipUntilMainSubscriber.this.actual, t, (AtomicInteger) SkipUntilMainSubscriber.this, SkipUntilMainSubscriber.this.error);
            }

            public void onComplete() {
                SkipUntilMainSubscriber.this.gate = true;
            }
        }
    }
}
