package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.annotations.Experimental;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

@Experimental
public final class SingleDetach<T> extends Single<T> {
    final SingleSource<T> source;

    public SingleDetach(SingleSource<T> source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> observer) {
        this.source.subscribe(new DetachSingleObserver(observer));
    }

    static final class DetachSingleObserver<T> implements SingleObserver<T>, Disposable {
        SingleObserver<? super T> actual;
        Disposable d;

        DetachSingleObserver(SingleObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            this.actual = null;
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void onSubscribe(Disposable d2) {
            if (DisposableHelper.validate(this.d, d2)) {
                this.d = d2;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            this.d = DisposableHelper.DISPOSED;
            SingleObserver<? super T> a = this.actual;
            if (a != null) {
                this.actual = null;
                a.onSuccess(value);
            }
        }

        public void onError(Throwable e) {
            this.d = DisposableHelper.DISPOSED;
            SingleObserver<? super T> a = this.actual;
            if (a != null) {
                this.actual = null;
                a.onError(e);
            }
        }
    }
}
