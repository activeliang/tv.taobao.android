package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class SingleDoAfterTerminate<T> extends Single<T> {
    final Action onAfterTerminate;
    final SingleSource<T> source;

    public SingleDoAfterTerminate(SingleSource<T> source2, Action onAfterTerminate2) {
        this.source = source2;
        this.onAfterTerminate = onAfterTerminate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe(new DoAfterTerminateObserver(s, this.onAfterTerminate));
    }

    static final class DoAfterTerminateObserver<T> implements SingleObserver<T>, Disposable {
        final SingleObserver<? super T> actual;
        Disposable d;
        final Action onAfterTerminate;

        DoAfterTerminateObserver(SingleObserver<? super T> actual2, Action onAfterTerminate2) {
            this.actual = actual2;
            this.onAfterTerminate = onAfterTerminate2;
        }

        public void onSubscribe(Disposable d2) {
            if (DisposableHelper.validate(this.d, d2)) {
                this.d = d2;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.actual.onSuccess(t);
            onAfterTerminate();
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
            onAfterTerminate();
        }

        public void dispose() {
            this.d.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        private void onAfterTerminate() {
            try {
                this.onAfterTerminate.run();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaPlugins.onError(ex);
            }
        }
    }
}
