package io.reactivex.internal.operators.flowable;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import java.util.NoSuchElementException;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class FlowableLastSingle<T> extends Single<T> {
    final T defaultItem;
    final Publisher<T> source;

    public FlowableLastSingle(Publisher<T> source2, T defaultItem2) {
        this.source = source2;
        this.defaultItem = defaultItem2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> observer) {
        this.source.subscribe(new LastSubscriber(observer, this.defaultItem));
    }

    static final class LastSubscriber<T> implements FlowableSubscriber<T>, Disposable {
        final SingleObserver<? super T> actual;
        final T defaultItem;
        T item;
        Subscription s;

        LastSubscriber(SingleObserver<? super T> actual2, T defaultItem2) {
            this.actual = actual2;
            this.defaultItem = defaultItem2;
        }

        public void dispose() {
            this.s.cancel();
            this.s = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.s == SubscriptionHelper.CANCELLED;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
                s2.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            this.item = t;
        }

        public void onError(Throwable t) {
            this.s = SubscriptionHelper.CANCELLED;
            this.item = null;
            this.actual.onError(t);
        }

        public void onComplete() {
            this.s = SubscriptionHelper.CANCELLED;
            T v = this.item;
            if (v != null) {
                this.item = null;
                this.actual.onSuccess(v);
                return;
            }
            T v2 = this.defaultItem;
            if (v2 != null) {
                this.actual.onSuccess(v2);
            } else {
                this.actual.onError(new NoSuchElementException());
            }
        }
    }
}
