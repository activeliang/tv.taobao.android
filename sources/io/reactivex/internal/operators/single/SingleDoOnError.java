package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;

public final class SingleDoOnError<T> extends Single<T> {
    final Consumer<? super Throwable> onError;
    final SingleSource<T> source;

    public SingleDoOnError(SingleSource<T> source2, Consumer<? super Throwable> onError2) {
        this.source = source2;
        this.onError = onError2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe(new DoOnError(s));
    }

    final class DoOnError implements SingleObserver<T> {
        private final SingleObserver<? super T> s;

        DoOnError(SingleObserver<? super T> s2) {
            this.s = s2;
        }

        public void onSubscribe(Disposable d) {
            this.s.onSubscribe(d);
        }

        public void onSuccess(T value) {
            this.s.onSuccess(value);
        }

        public void onError(Throwable e) {
            try {
                SingleDoOnError.this.onError.accept(e);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                e = new CompositeException(e, ex);
            }
            this.s.onError(e);
        }
    }
}
