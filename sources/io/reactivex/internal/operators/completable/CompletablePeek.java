package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;

public final class CompletablePeek extends Completable {
    final Action onAfterTerminate;
    final Action onComplete;
    final Action onDispose;
    final Consumer<? super Throwable> onError;
    final Consumer<? super Disposable> onSubscribe;
    final Action onTerminate;
    final CompletableSource source;

    public CompletablePeek(CompletableSource source2, Consumer<? super Disposable> onSubscribe2, Consumer<? super Throwable> onError2, Action onComplete2, Action onTerminate2, Action onAfterTerminate2, Action onDispose2) {
        this.source = source2;
        this.onSubscribe = onSubscribe2;
        this.onError = onError2;
        this.onComplete = onComplete2;
        this.onTerminate = onTerminate2;
        this.onAfterTerminate = onAfterTerminate2;
        this.onDispose = onDispose2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.source.subscribe(new CompletableObserverImplementation(s));
    }

    final class CompletableObserverImplementation implements CompletableObserver, Disposable {
        final CompletableObserver actual;
        Disposable d;

        CompletableObserverImplementation(CompletableObserver actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable d2) {
            try {
                CompletablePeek.this.onSubscribe.accept(d2);
                if (DisposableHelper.validate(this.d, d2)) {
                    this.d = d2;
                    this.actual.onSubscribe(this);
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                d2.dispose();
                this.d = DisposableHelper.DISPOSED;
                EmptyDisposable.error(ex, this.actual);
            }
        }

        public void onError(Throwable e) {
            if (this.d == DisposableHelper.DISPOSED) {
                RxJavaPlugins.onError(e);
                return;
            }
            try {
                CompletablePeek.this.onError.accept(e);
                CompletablePeek.this.onTerminate.run();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                e = new CompositeException(e, ex);
            }
            this.actual.onError(e);
            doAfter();
        }

        public void onComplete() {
            if (this.d != DisposableHelper.DISPOSED) {
                try {
                    CompletablePeek.this.onComplete.run();
                    CompletablePeek.this.onTerminate.run();
                    this.actual.onComplete();
                    doAfter();
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.actual.onError(e);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void doAfter() {
            try {
                CompletablePeek.this.onAfterTerminate.run();
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaPlugins.onError(ex);
            }
        }

        public void dispose() {
            try {
                CompletablePeek.this.onDispose.run();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
            this.d.dispose();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }
    }
}
