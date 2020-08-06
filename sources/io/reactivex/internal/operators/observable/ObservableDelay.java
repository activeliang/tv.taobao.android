package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;
import java.util.concurrent.TimeUnit;

public final class ObservableDelay<T> extends AbstractObservableWithUpstream<T, T> {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final TimeUnit unit;

    public ObservableDelay(ObservableSource<T> source, long delay2, TimeUnit unit2, Scheduler scheduler2, boolean delayError2) {
        super(source);
        this.delay = delay2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.delayError = delayError2;
    }

    public void subscribeActual(Observer<? super T> t) {
        Observer<? super T> serializedObserver;
        if (this.delayError) {
            serializedObserver = t;
        } else {
            serializedObserver = new SerializedObserver<>(t);
        }
        this.source.subscribe(new DelayObserver(serializedObserver, this.delay, this.unit, this.scheduler.createWorker(), this.delayError));
    }

    static final class DelayObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;
        final long delay;
        final boolean delayError;
        Disposable s;
        final TimeUnit unit;
        final Scheduler.Worker w;

        DelayObserver(Observer<? super T> actual2, long delay2, TimeUnit unit2, Scheduler.Worker w2, boolean delayError2) {
            this.actual = actual2;
            this.delay = delay2;
            this.unit = unit2;
            this.w = w2;
            this.delayError = delayError2;
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.w.schedule(new OnNext(t), this.delay, this.unit);
        }

        public void onError(Throwable t) {
            this.w.schedule(new OnError(t), this.delayError ? this.delay : 0, this.unit);
        }

        public void onComplete() {
            this.w.schedule(new OnComplete(), this.delay, this.unit);
        }

        public void dispose() {
            this.s.dispose();
            this.w.dispose();
        }

        public boolean isDisposed() {
            return this.w.isDisposed();
        }

        final class OnNext implements Runnable {
            private final T t;

            OnNext(T t2) {
                this.t = t2;
            }

            public void run() {
                DelayObserver.this.actual.onNext(this.t);
            }
        }

        final class OnError implements Runnable {
            private final Throwable throwable;

            OnError(Throwable throwable2) {
                this.throwable = throwable2;
            }

            public void run() {
                try {
                    DelayObserver.this.actual.onError(this.throwable);
                } finally {
                    DelayObserver.this.w.dispose();
                }
            }
        }

        final class OnComplete implements Runnable {
            OnComplete() {
            }

            public void run() {
                try {
                    DelayObserver.this.actual.onComplete();
                } finally {
                    DelayObserver.this.w.dispose();
                }
            }
        }
    }
}
