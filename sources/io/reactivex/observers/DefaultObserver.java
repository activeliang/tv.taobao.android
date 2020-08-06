package io.reactivex.observers;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.util.EndConsumerHelper;

public abstract class DefaultObserver<T> implements Observer<T> {
    private Disposable s;

    public final void onSubscribe(@NonNull Disposable s2) {
        if (EndConsumerHelper.validate(this.s, s2, getClass())) {
            this.s = s2;
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public final void cancel() {
        Disposable s2 = this.s;
        this.s = DisposableHelper.DISPOSED;
        s2.dispose();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }
}
