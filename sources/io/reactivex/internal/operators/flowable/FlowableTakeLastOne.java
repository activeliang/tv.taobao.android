package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableTakeLastOne<T> extends AbstractFlowableWithUpstream<T, T> {
    public FlowableTakeLastOne(Flowable<T> source) {
        super(source);
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe(new TakeLastOneSubscriber(s));
    }

    static final class TakeLastOneSubscriber<T> extends DeferredScalarSubscription<T> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -5467847744262967226L;
        Subscription s;

        TakeLastOneSubscriber(Subscriber<? super T> actual) {
            super(actual);
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
                s2.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            this.value = t;
        }

        public void onError(Throwable t) {
            this.value = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            T v = this.value;
            if (v != null) {
                complete(v);
            } else {
                this.actual.onComplete();
            }
        }

        public void cancel() {
            super.cancel();
            this.s.cancel();
        }
    }
}
