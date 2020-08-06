package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;

public final class SingleDoOnSubscribe<T> extends Single<T> {
    final Consumer<? super Disposable> onSubscribe;
    final SingleSource<T> source;

    public SingleDoOnSubscribe(SingleSource<T> source2, Consumer<? super Disposable> onSubscribe2) {
        this.source = source2;
        this.onSubscribe = onSubscribe2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe(new DoOnSubscribeSingleObserver(s, this.onSubscribe));
    }

    static final class DoOnSubscribeSingleObserver<T> implements SingleObserver<T> {
        final SingleObserver<? super T> actual;
        boolean done;
        final Consumer<? super Disposable> onSubscribe;

        DoOnSubscribeSingleObserver(SingleObserver<? super T> actual2, Consumer<? super Disposable> onSubscribe2) {
            this.actual = actual2;
            this.onSubscribe = onSubscribe2;
        }

        public void onSubscribe(Disposable d) {
            try {
                this.onSubscribe.accept(d);
                this.actual.onSubscribe(d);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.done = true;
                d.dispose();
                EmptyDisposable.error(ex, (SingleObserver<?>) this.actual);
            }
        }

        public void onSuccess(T value) {
            if (!this.done) {
                this.actual.onSuccess(value);
            }
        }

        public void onError(Throwable e) {
            if (this.done) {
                RxJavaPlugins.onError(e);
            } else {
                this.actual.onError(e);
            }
        }
    }
}
