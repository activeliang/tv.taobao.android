package io.reactivex.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.EndConsumerHelper;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public abstract class DisposableSubscriber<T> implements FlowableSubscriber<T>, Disposable {
    final AtomicReference<Subscription> s = new AtomicReference<>();

    public final void onSubscribe(Subscription s2) {
        if (EndConsumerHelper.setOnce(this.s, s2, getClass())) {
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.s.get().request(Long.MAX_VALUE);
    }

    /* access modifiers changed from: protected */
    public final void request(long n) {
        this.s.get().request(n);
    }

    /* access modifiers changed from: protected */
    public final void cancel() {
        dispose();
    }

    public final boolean isDisposed() {
        return this.s.get() == SubscriptionHelper.CANCELLED;
    }

    public final void dispose() {
        SubscriptionHelper.cancel(this.s);
    }
}
