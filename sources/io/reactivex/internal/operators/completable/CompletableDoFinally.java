package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.annotations.Experimental;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;

@Experimental
public final class CompletableDoFinally extends Completable {
    final Action onFinally;
    final CompletableSource source;

    public CompletableDoFinally(CompletableSource source2, Action onFinally2) {
        this.source = source2;
        this.onFinally = onFinally2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.source.subscribe(new DoFinallyObserver(s, this.onFinally));
    }

    static final class DoFinallyObserver extends AtomicInteger implements CompletableObserver, Disposable {
        private static final long serialVersionUID = 4109457741734051389L;
        final CompletableObserver actual;
        Disposable d;
        final Action onFinally;

        DoFinallyObserver(CompletableObserver actual2, Action onFinally2) {
            this.actual = actual2;
            this.onFinally = onFinally2;
        }

        public void onSubscribe(Disposable d2) {
            if (DisposableHelper.validate(this.d, d2)) {
                this.d = d2;
                this.actual.onSubscribe(this);
            }
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
            runFinally();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void dispose() {
            this.d.dispose();
            runFinally();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        /* access modifiers changed from: package-private */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }
}
