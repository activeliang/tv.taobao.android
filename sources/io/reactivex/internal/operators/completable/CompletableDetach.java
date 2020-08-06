package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.annotations.Experimental;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;

@Experimental
public final class CompletableDetach extends Completable {
    final CompletableSource source;

    public CompletableDetach(CompletableSource source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver observer) {
        this.source.subscribe(new DetachCompletableObserver(observer));
    }

    static final class DetachCompletableObserver implements CompletableObserver, Disposable {
        CompletableObserver actual;
        Disposable d;

        DetachCompletableObserver(CompletableObserver actual2) {
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

        public void onError(Throwable e) {
            this.d = DisposableHelper.DISPOSED;
            CompletableObserver a = this.actual;
            if (a != null) {
                this.actual = null;
                a.onError(e);
            }
        }

        public void onComplete() {
            this.d = DisposableHelper.DISPOSED;
            CompletableObserver a = this.actual;
            if (a != null) {
                this.actual = null;
                a.onComplete();
            }
        }
    }
}
