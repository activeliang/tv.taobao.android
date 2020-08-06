package io.reactivex.internal.observers;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;

public final class DisposableLambdaObserver<T> implements Observer<T>, Disposable {
    final Observer<? super T> actual;
    final Action onDispose;
    final Consumer<? super Disposable> onSubscribe;
    Disposable s;

    public DisposableLambdaObserver(Observer<? super T> actual2, Consumer<? super Disposable> onSubscribe2, Action onDispose2) {
        this.actual = actual2;
        this.onSubscribe = onSubscribe2;
        this.onDispose = onDispose2;
    }

    public void onSubscribe(Disposable s2) {
        try {
            this.onSubscribe.accept(s2);
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            s2.dispose();
            this.s = DisposableHelper.DISPOSED;
            EmptyDisposable.error(e, (Observer<?>) this.actual);
        }
    }

    public void onNext(T t) {
        this.actual.onNext(t);
    }

    public void onError(Throwable t) {
        if (this.s != DisposableHelper.DISPOSED) {
            this.actual.onError(t);
        } else {
            RxJavaPlugins.onError(t);
        }
    }

    public void onComplete() {
        if (this.s != DisposableHelper.DISPOSED) {
            this.actual.onComplete();
        }
    }

    public void dispose() {
        try {
            this.onDispose.run();
        } catch (Throwable e) {
            Exceptions.throwIfFatal(e);
            RxJavaPlugins.onError(e);
        }
        this.s.dispose();
    }

    public boolean isDisposed() {
        return this.s.isDisposed();
    }
}
