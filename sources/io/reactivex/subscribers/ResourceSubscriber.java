package io.reactivex.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.EndConsumerHelper;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public abstract class ResourceSubscriber<T> implements FlowableSubscriber<T>, Disposable {
    private final AtomicLong missedRequested = new AtomicLong();
    private final ListCompositeDisposable resources = new ListCompositeDisposable();
    private final AtomicReference<Subscription> s = new AtomicReference<>();

    public final void add(Disposable resource) {
        ObjectHelper.requireNonNull(resource, "resource is null");
        this.resources.add(resource);
    }

    public final void onSubscribe(Subscription s2) {
        if (EndConsumerHelper.setOnce(this.s, s2, getClass())) {
            long r = this.missedRequested.getAndSet(0);
            if (r != 0) {
                s2.request(r);
            }
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        request(Long.MAX_VALUE);
    }

    /* access modifiers changed from: protected */
    public final void request(long n) {
        SubscriptionHelper.deferredRequest(this.s, this.missedRequested, n);
    }

    public final void dispose() {
        if (SubscriptionHelper.cancel(this.s)) {
            this.resources.dispose();
        }
    }

    public final boolean isDisposed() {
        return SubscriptionHelper.isCancelled(this.s.get());
    }
}
