package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableTimer extends Observable<Long> {
    final long delay;
    final Scheduler scheduler;
    final TimeUnit unit;

    public ObservableTimer(long delay2, TimeUnit unit2, Scheduler scheduler2) {
        this.delay = delay2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    public void subscribeActual(Observer<? super Long> s) {
        TimerObserver ios = new TimerObserver(s);
        s.onSubscribe(ios);
        ios.setResource(this.scheduler.scheduleDirect(ios, this.delay, this.unit));
    }

    static final class TimerObserver extends AtomicReference<Disposable> implements Disposable, Runnable {
        private static final long serialVersionUID = -2809475196591179431L;
        final Observer<? super Long> actual;

        TimerObserver(Observer<? super Long> actual2) {
            this.actual = actual2;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return get() == DisposableHelper.DISPOSED;
        }

        public void run() {
            if (!isDisposed()) {
                this.actual.onNext(0L);
                lazySet(EmptyDisposable.INSTANCE);
                this.actual.onComplete();
            }
        }

        public void setResource(Disposable d) {
            DisposableHelper.trySet(this, d);
        }
    }
}
