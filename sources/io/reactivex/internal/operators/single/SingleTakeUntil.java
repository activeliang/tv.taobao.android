package io.reactivex.internal.operators.single;

import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public final class SingleTakeUntil<T, U> extends Single<T> {
    final Publisher<U> other;
    final SingleSource<T> source;

    public SingleTakeUntil(SingleSource<T> source2, Publisher<U> other2) {
        this.source = source2;
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> observer) {
        TakeUntilMainObserver<T> parent = new TakeUntilMainObserver<>(observer);
        observer.onSubscribe(parent);
        this.other.subscribe(parent.other);
        this.source.subscribe(parent);
    }

    static final class TakeUntilMainObserver<T> extends AtomicReference<Disposable> implements SingleObserver<T>, Disposable {
        private static final long serialVersionUID = -622603812305745221L;
        final SingleObserver<? super T> actual;
        final TakeUntilOtherSubscriber other = new TakeUntilOtherSubscriber(this);

        TakeUntilMainObserver(SingleObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.setOnce(this, d);
        }

        public void onSuccess(T value) {
            this.other.dispose();
            if (((Disposable) get()) != DisposableHelper.DISPOSED && ((Disposable) getAndSet(DisposableHelper.DISPOSED)) != DisposableHelper.DISPOSED) {
                this.actual.onSuccess(value);
            }
        }

        public void onError(Throwable e) {
            this.other.dispose();
            if (((Disposable) get()) == DisposableHelper.DISPOSED || ((Disposable) getAndSet(DisposableHelper.DISPOSED)) == DisposableHelper.DISPOSED) {
                RxJavaPlugins.onError(e);
            } else {
                this.actual.onError(e);
            }
        }

        /* access modifiers changed from: package-private */
        public void otherError(Throwable e) {
            Disposable a;
            if (((Disposable) get()) == DisposableHelper.DISPOSED || (a = (Disposable) getAndSet(DisposableHelper.DISPOSED)) == DisposableHelper.DISPOSED) {
                RxJavaPlugins.onError(e);
                return;
            }
            if (a != null) {
                a.dispose();
            }
            this.actual.onError(e);
        }
    }

    static final class TakeUntilOtherSubscriber extends AtomicReference<Subscription> implements FlowableSubscriber<Object> {
        private static final long serialVersionUID = 5170026210238877381L;
        final TakeUntilMainObserver<?> parent;

        TakeUntilOtherSubscriber(TakeUntilMainObserver<?> parent2) {
            this.parent = parent2;
        }

        public void onSubscribe(Subscription s) {
            if (SubscriptionHelper.setOnce(this, s)) {
                s.request(Long.MAX_VALUE);
            }
        }

        public void onNext(Object t) {
            if (SubscriptionHelper.cancel(this)) {
                this.parent.otherError(new CancellationException());
            }
        }

        public void onError(Throwable t) {
            this.parent.otherError(t);
        }

        public void onComplete() {
            if (get() != SubscriptionHelper.CANCELLED) {
                lazySet(SubscriptionHelper.CANCELLED);
                this.parent.otherError(new CancellationException());
            }
        }

        public void dispose() {
            SubscriptionHelper.cancel(this);
        }
    }
}
