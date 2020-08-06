package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;

public final class SingleDoOnSuccess<T> extends Single<T> {
    final Consumer<? super T> onSuccess;
    final SingleSource<T> source;

    public SingleDoOnSuccess(SingleSource<T> source2, Consumer<? super T> onSuccess2) {
        this.source = source2;
        this.onSuccess = onSuccess2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe(new DoOnSuccess(s));
    }

    final class DoOnSuccess implements SingleObserver<T> {
        private final SingleObserver<? super T> s;

        DoOnSuccess(SingleObserver<? super T> s2) {
            this.s = s2;
        }

        public void onSubscribe(Disposable d) {
            this.s.onSubscribe(d);
        }

        public void onSuccess(T value) {
            try {
                SingleDoOnSuccess.this.onSuccess.accept(value);
                this.s.onSuccess(value);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.s.onError(ex);
            }
        }

        public void onError(Throwable e) {
            this.s.onError(e);
        }
    }
}
