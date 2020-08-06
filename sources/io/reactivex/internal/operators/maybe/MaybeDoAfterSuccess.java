package io.reactivex.internal.operators.maybe;

import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.annotations.Experimental;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

@Experimental
public final class MaybeDoAfterSuccess<T> extends AbstractMaybeWithUpstream<T, T> {
    final Consumer<? super T> onAfterSuccess;

    public MaybeDoAfterSuccess(MaybeSource<T> source, Consumer<? super T> onAfterSuccess2) {
        super(source);
        this.onAfterSuccess = onAfterSuccess2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> s) {
        this.source.subscribe(new DoAfterObserver(s, this.onAfterSuccess));
    }

    static final class DoAfterObserver<T> implements MaybeObserver<T>, Disposable {
        final MaybeObserver<? super T> actual;
        Disposable d;
        final Consumer<? super T> onAfterSuccess;

        DoAfterObserver(MaybeObserver<? super T> actual2, Consumer<? super T> onAfterSuccess2) {
            this.actual = actual2;
            this.onAfterSuccess = onAfterSuccess2;
        }

        public void onSubscribe(Disposable d2) {
            if (DisposableHelper.validate(this.d, d2)) {
                this.d = d2;
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.actual.onSuccess(t);
            try {
                this.onAfterSuccess.accept(t);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaPlugins.onError(ex);
            }
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void dispose() {
            this.d.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }
    }
}
