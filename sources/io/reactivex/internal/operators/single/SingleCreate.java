package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Cancellable;
import io.reactivex.internal.disposables.CancellableDisposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicReference;

public final class SingleCreate<T> extends Single<T> {
    final SingleOnSubscribe<T> source;

    public SingleCreate(SingleOnSubscribe<T> source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        Emitter<T> parent = new Emitter<>(s);
        s.onSubscribe(parent);
        try {
            this.source.subscribe(parent);
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            parent.onError(ex);
        }
    }

    static final class Emitter<T> extends AtomicReference<Disposable> implements SingleEmitter<T>, Disposable {
        private static final long serialVersionUID = -2467358622224974244L;
        final SingleObserver<? super T> actual;

        Emitter(SingleObserver<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSuccess(T value) {
            Disposable d;
            if (get() != DisposableHelper.DISPOSED && (d = (Disposable) getAndSet(DisposableHelper.DISPOSED)) != DisposableHelper.DISPOSED) {
                if (value == null) {
                    try {
                        this.actual.onError(new NullPointerException("onSuccess called with null. Null values are generally not allowed in 2.x operators and sources."));
                    } catch (Throwable th) {
                        if (d != null) {
                            d.dispose();
                        }
                        throw th;
                    }
                } else {
                    this.actual.onSuccess(value);
                }
                if (d != null) {
                    d.dispose();
                }
            }
        }

        public void onError(Throwable t) {
            if (!tryOnError(t)) {
                RxJavaPlugins.onError(t);
            }
        }

        public boolean tryOnError(Throwable t) {
            Disposable d;
            if (t == null) {
                t = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
            }
            if (get() == DisposableHelper.DISPOSED || (d = (Disposable) getAndSet(DisposableHelper.DISPOSED)) == DisposableHelper.DISPOSED) {
                return false;
            }
            try {
                this.actual.onError(t);
                return true;
            } finally {
                if (d != null) {
                    d.dispose();
                }
            }
        }

        public void setDisposable(Disposable d) {
            DisposableHelper.set(this, d);
        }

        public void setCancellable(Cancellable c) {
            setDisposable(new CancellableDisposable(c));
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }
    }
}
