package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class CompletableFromPublisher<T> extends Completable {
    final Publisher<T> flowable;

    public CompletableFromPublisher(Publisher<T> flowable2) {
        this.flowable = flowable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver cs) {
        this.flowable.subscribe(new FromPublisherSubscriber(cs));
    }

    static final class FromPublisherSubscriber<T> implements FlowableSubscriber<T>, Disposable {
        final CompletableObserver cs;
        Subscription s;

        FromPublisherSubscriber(CompletableObserver actual) {
            this.cs = actual;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.cs.onSubscribe(this);
                s2.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
        }

        public void onError(Throwable t) {
            this.cs.onError(t);
        }

        public void onComplete() {
            this.cs.onComplete();
        }

        public void dispose() {
            this.s.cancel();
            this.s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.s == SubscriptionHelper.CANCELLED;
        }
    }
}
