package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.plugins.RxJavaPlugins;

public final class CompletableDisposeOn extends Completable {
    final Scheduler scheduler;
    final CompletableSource source;

    public CompletableDisposeOn(CompletableSource source2, Scheduler scheduler2) {
        this.source = source2;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.source.subscribe(new CompletableObserverImplementation(s, this.scheduler));
    }

    static final class CompletableObserverImplementation implements CompletableObserver, Disposable, Runnable {
        Disposable d;
        volatile boolean disposed;
        final CompletableObserver s;
        final Scheduler scheduler;

        CompletableObserverImplementation(CompletableObserver s2, Scheduler scheduler2) {
            this.s = s2;
            this.scheduler = scheduler2;
        }

        public void onComplete() {
            if (!this.disposed) {
                this.s.onComplete();
            }
        }

        public void onError(Throwable e) {
            if (this.disposed) {
                RxJavaPlugins.onError(e);
            } else {
                this.s.onError(e);
            }
        }

        public void onSubscribe(Disposable d2) {
            if (DisposableHelper.validate(this.d, d2)) {
                this.d = d2;
                this.s.onSubscribe(this);
            }
        }

        public void dispose() {
            this.disposed = true;
            this.scheduler.scheduleDirect(this);
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        public void run() {
            this.d.dispose();
            this.d = DisposableHelper.DISPOSED;
        }
    }
}
