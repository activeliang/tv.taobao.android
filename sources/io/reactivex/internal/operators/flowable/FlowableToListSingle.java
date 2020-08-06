package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.FuseToFlowable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.ArrayListSupplier;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Collection;
import java.util.concurrent.Callable;
import org.reactivestreams.Subscription;

public final class FlowableToListSingle<T, U extends Collection<? super T>> extends Single<U> implements FuseToFlowable<U> {
    final Callable<U> collectionSupplier;
    final Flowable<T> source;

    public FlowableToListSingle(Flowable<T> source2) {
        this(source2, ArrayListSupplier.asCallable());
    }

    public FlowableToListSingle(Flowable<T> source2, Callable<U> collectionSupplier2) {
        this.source = source2;
        this.collectionSupplier = collectionSupplier2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super U> s) {
        try {
            this.source.subscribe(new ToListSubscriber(s, (Collection) ObjectHelper.requireNonNull(this.collectionSupplier.call(), "The collectionSupplier returned a null collection. Null values are generally not allowed in 2.x operators and sources.")));
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            EmptyDisposable.error(e, (SingleObserver<?>) s);
        }
    }

    public Flowable<U> fuseToFlowable() {
        return RxJavaPlugins.onAssembly(new FlowableToList(this.source, this.collectionSupplier));
    }

    static final class ToListSubscriber<T, U extends Collection<? super T>> implements FlowableSubscriber<T>, Disposable {
        final SingleObserver<? super U> actual;
        Subscription s;
        U value;

        ToListSubscriber(SingleObserver<? super U> actual2, U collection) {
            this.actual = actual2;
            this.value = collection;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
                s2.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            this.value.add(t);
        }

        public void onError(Throwable t) {
            this.value = null;
            this.s = SubscriptionHelper.CANCELLED;
            this.actual.onError(t);
        }

        public void onComplete() {
            this.s = SubscriptionHelper.CANCELLED;
            this.actual.onSuccess(this.value);
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
