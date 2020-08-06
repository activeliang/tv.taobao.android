package io.reactivex.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.EndConsumerHelper;
import org.reactivestreams.Subscription;

public abstract class DefaultSubscriber<T> implements FlowableSubscriber<T> {
    private Subscription s;

    public final void onSubscribe(Subscription s2) {
        if (EndConsumerHelper.validate(this.s, s2, getClass())) {
            this.s = s2;
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public final void request(long n) {
        Subscription s2 = this.s;
        if (s2 != null) {
            s2.request(n);
        }
    }

    /* access modifiers changed from: protected */
    public final void cancel() {
        Subscription s2 = this.s;
        this.s = SubscriptionHelper.CANCELLED;
        s2.cancel();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        request(Long.MAX_VALUE);
    }
}
