package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.TimeUnit;

public final class CompletableDelay extends Completable {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final CompletableSource source;
    final TimeUnit unit;

    public CompletableDelay(CompletableSource source2, long delay2, TimeUnit unit2, Scheduler scheduler2, boolean delayError2) {
        this.source = source2;
        this.delay = delay2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.delayError = delayError2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.source.subscribe(new Delay(new CompositeDisposable(), s));
    }

    final class Delay implements CompletableObserver {
        final CompletableObserver s;
        private final CompositeDisposable set;

        Delay(CompositeDisposable set2, CompletableObserver s2) {
            this.set = set2;
            this.s = s2;
        }

        public void onComplete() {
            this.set.add(CompletableDelay.this.scheduler.scheduleDirect(new OnComplete(), CompletableDelay.this.delay, CompletableDelay.this.unit));
        }

        public void onError(Throwable e) {
            this.set.add(CompletableDelay.this.scheduler.scheduleDirect(new OnError(e), CompletableDelay.this.delayError ? CompletableDelay.this.delay : 0, CompletableDelay.this.unit));
        }

        public void onSubscribe(Disposable d) {
            this.set.add(d);
            this.s.onSubscribe(this.set);
        }

        final class OnComplete implements Runnable {
            OnComplete() {
            }

            public void run() {
                Delay.this.s.onComplete();
            }
        }

        final class OnError implements Runnable {
            private final Throwable e;

            OnError(Throwable e2) {
                this.e = e2;
            }

            public void run() {
                Delay.this.s.onError(this.e);
            }
        }
    }
}
