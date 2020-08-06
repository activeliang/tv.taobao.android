package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AtomicThrowable;
import io.reactivex.internal.util.HalfSerializer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class StrictSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Subscription {
    private static final long serialVersionUID = -4945028590049415624L;
    final Subscriber<? super T> actual;
    volatile boolean done;
    final AtomicThrowable error = new AtomicThrowable();
    final AtomicBoolean once = new AtomicBoolean();
    final AtomicLong requested = new AtomicLong();
    final AtomicReference<Subscription> s = new AtomicReference<>();

    public StrictSubscriber(Subscriber<? super T> actual2) {
        this.actual = actual2;
    }

    public void request(long n) {
        if (n <= 0) {
            cancel();
            onError(new IllegalArgumentException("§3.9 violated: positive request amount required but it was " + n));
            return;
        }
        SubscriptionHelper.deferredRequest(this.s, this.requested, n);
    }

    public void cancel() {
        if (!this.done) {
            SubscriptionHelper.cancel(this.s);
        }
    }

    public void onSubscribe(Subscription s2) {
        if (this.once.compareAndSet(false, true)) {
            this.actual.onSubscribe(this);
            SubscriptionHelper.deferredSetOnce(this.s, this.requested, s2);
            return;
        }
        s2.cancel();
        cancel();
        onError(new IllegalStateException("§2.12 violated: onSubscribe must be called at most once"));
    }

    public void onNext(T t) {
        HalfSerializer.onNext(this.actual, t, (AtomicInteger) this, this.error);
    }

    public void onError(Throwable t) {
        this.done = true;
        HalfSerializer.onError((Subscriber<?>) this.actual, t, (AtomicInteger) this, this.error);
    }

    public void onComplete() {
        this.done = true;
        HalfSerializer.onComplete((Subscriber<?>) this.actual, (AtomicInteger) this, this.error);
    }
}
