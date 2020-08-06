package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.observers.ResumeSingleObserver;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleDelayWithSingle<T, U> extends Single<T> {
    final SingleSource<U> other;
    final SingleSource<T> source;

    public SingleDelayWithSingle(SingleSource<T> source2, SingleSource<U> other2) {
        this.source = source2;
        this.other = other2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> subscriber) {
        this.other.subscribe(new OtherObserver(subscriber, this.source));
    }

    static final class OtherObserver<T, U> extends AtomicReference<Disposable> implements SingleObserver<U>, Disposable {
        private static final long serialVersionUID = -8565274649390031272L;
        final SingleObserver<? super T> actual;
        final SingleSource<T> source;

        OtherObserver(SingleObserver<? super T> actual2, SingleSource<T> source2) {
            this.actual = actual2;
            this.source = source2;
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.set(this, d)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(U u) {
            this.source.subscribe(new ResumeSingleObserver(this, this.actual));
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }
}
