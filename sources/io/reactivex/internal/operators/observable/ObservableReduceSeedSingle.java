package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class ObservableReduceSeedSingle<T, R> extends Single<R> {
    final BiFunction<R, ? super T, R> reducer;
    final R seed;
    final ObservableSource<T> source;

    public ObservableReduceSeedSingle(ObservableSource<T> source2, R seed2, BiFunction<R, ? super T, R> reducer2) {
        this.source = source2;
        this.seed = seed2;
        this.reducer = reducer2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super R> observer) {
        this.source.subscribe(new ReduceSeedObserver(observer, this.reducer, this.seed));
    }

    static final class ReduceSeedObserver<T, R> implements Observer<T>, Disposable {
        final SingleObserver<? super R> actual;
        Disposable d;
        final BiFunction<R, ? super T, R> reducer;
        R value;

        ReduceSeedObserver(SingleObserver<? super R> actual2, BiFunction<R, ? super T, R> reducer2, R value2) {
            this.actual = actual2;
            this.value = value2;
            this.reducer = reducer2;
        }

        public void onSubscribe(Disposable d2) {
            if (DisposableHelper.validate(this.d, d2)) {
                this.d = d2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T value2) {
            R v = this.value;
            if (v != null) {
                try {
                    this.value = ObjectHelper.requireNonNull(this.reducer.apply(v, value2), "The reducer returned a null value");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    this.d.dispose();
                    onError(ex);
                }
            }
        }

        public void onError(Throwable e) {
            R v = this.value;
            this.value = null;
            if (v != null) {
                this.actual.onError(e);
            } else {
                RxJavaPlugins.onError(e);
            }
        }

        public void onComplete() {
            R v = this.value;
            this.value = null;
            if (v != null) {
                this.actual.onSuccess(v);
            }
        }

        public void dispose() {
            this.d.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }
    }
}
