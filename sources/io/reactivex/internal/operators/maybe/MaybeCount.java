package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.HasUpstreamMaybeSource;

public final class MaybeCount<T> extends Single<Long> implements HasUpstreamMaybeSource<T> {
    final MaybeSource<T> source;

    public MaybeCount(MaybeSource<T> source2) {
        this.source = source2;
    }

    public MaybeSource<T> source() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Long> observer) {
        this.source.subscribe(new CountMaybeObserver(observer));
    }

    static final class CountMaybeObserver implements MaybeObserver<Object>, Disposable {
        final SingleObserver<? super Long> actual;
        Disposable d;

        CountMaybeObserver(SingleObserver<? super Long> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable d2) {
            if (DisposableHelper.validate(this.d, d2)) {
                this.d = d2;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(Object value) {
            this.d = DisposableHelper.DISPOSED;
            this.actual.onSuccess(1L);
        }

        public void onError(Throwable e) {
            this.d = DisposableHelper.DISPOSED;
            this.actual.onError(e);
        }

        public void onComplete() {
            this.d = DisposableHelper.DISPOSED;
            this.actual.onSuccess(0L);
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public void dispose() {
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }
    }
}
