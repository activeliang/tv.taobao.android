package io.reactivex.internal.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscription;

public final class FutureSubscriber<T> extends CountDownLatch implements FlowableSubscriber<T>, Future<T>, Subscription {
    Throwable error;
    final AtomicReference<Subscription> s = new AtomicReference<>();
    T value;

    public FutureSubscriber() {
        super(1);
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        Subscription a;
        do {
            a = this.s.get();
            if (a == this || a == SubscriptionHelper.CANCELLED) {
                return false;
            }
        } while (!this.s.compareAndSet(a, SubscriptionHelper.CANCELLED));
        if (a != null) {
            a.cancel();
        }
        countDown();
        return true;
    }

    public boolean isCancelled() {
        return SubscriptionHelper.isCancelled(this.s.get());
    }

    public boolean isDone() {
        return getCount() == 0;
    }

    public T get() throws InterruptedException, ExecutionException {
        if (getCount() != 0) {
            BlockingHelper.verifyNonBlocking();
            await();
        }
        if (isCancelled()) {
            throw new CancellationException();
        }
        Throwable ex = this.error;
        if (ex == null) {
            return this.value;
        }
        throw new ExecutionException(ex);
    }

    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (getCount() != 0) {
            BlockingHelper.verifyNonBlocking();
            if (!await(timeout, unit)) {
                throw new TimeoutException();
            }
        }
        if (isCancelled()) {
            throw new CancellationException();
        }
        Throwable ex = this.error;
        if (ex == null) {
            return this.value;
        }
        throw new ExecutionException(ex);
    }

    public void onSubscribe(Subscription s2) {
        if (SubscriptionHelper.setOnce(this.s, s2)) {
            s2.request(Long.MAX_VALUE);
        }
    }

    public void onNext(T t) {
        if (this.value != null) {
            this.s.get().cancel();
            onError(new IndexOutOfBoundsException("More than one element received"));
            return;
        }
        this.value = t;
    }

    public void onError(Throwable t) {
        Subscription a;
        do {
            a = this.s.get();
            if (a == this || a == SubscriptionHelper.CANCELLED) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.error = t;
        } while (!this.s.compareAndSet(a, this));
        countDown();
    }

    public void onComplete() {
        Subscription a;
        if (this.value == null) {
            onError(new NoSuchElementException("The source is empty"));
            return;
        }
        do {
            a = this.s.get();
            if (a == this || a == SubscriptionHelper.CANCELLED) {
                return;
            }
        } while (!this.s.compareAndSet(a, this));
        countDown();
    }

    public void cancel() {
    }

    public void request(long n) {
    }
}
