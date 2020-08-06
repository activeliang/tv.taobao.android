package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.ObserverFullArbiter;
import io.reactivex.internal.observers.FullArbiterObserver;
import io.reactivex.observers.SerializedObserver;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableTimeoutTimed<T> extends AbstractObservableWithUpstream<T, T> {
    static final Disposable NEW_TIMER = new EmptyDisposable();
    final ObservableSource<? extends T> other;
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    public ObservableTimeoutTimed(ObservableSource<T> source, long timeout2, TimeUnit unit2, Scheduler scheduler2, ObservableSource<? extends T> other2) {
        super(source);
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
        this.other = other2;
    }

    public void subscribeActual(Observer<? super T> t) {
        if (this.other == null) {
            this.source.subscribe(new TimeoutTimedObserver(new SerializedObserver(t), this.timeout, this.unit, this.scheduler.createWorker()));
            return;
        }
        this.source.subscribe(new TimeoutTimedOtherObserver(t, this.timeout, this.unit, this.scheduler.createWorker(), this.other));
    }

    static final class TimeoutTimedOtherObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
        private static final long serialVersionUID = -4619702551964128179L;
        final Observer<? super T> actual;
        final ObserverFullArbiter<T> arbiter;
        volatile boolean done;
        volatile long index;
        final ObservableSource<? extends T> other;
        Disposable s;
        final long timeout;
        final TimeUnit unit;
        final Scheduler.Worker worker;

        TimeoutTimedOtherObserver(Observer<? super T> actual2, long timeout2, TimeUnit unit2, Scheduler.Worker worker2, ObservableSource<? extends T> other2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
            this.other = other2;
            this.arbiter = new ObserverFullArbiter<>(actual2, this, 8);
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                if (this.arbiter.setDisposable(s2)) {
                    this.actual.onSubscribe(this.arbiter);
                    scheduleTimeout(0);
                }
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long idx = this.index + 1;
                this.index = idx;
                if (this.arbiter.onNext(t, this.s)) {
                    scheduleTimeout(idx);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void scheduleTimeout(long idx) {
            Disposable d = (Disposable) get();
            if (d != null) {
                d.dispose();
            }
            if (compareAndSet(d, ObservableTimeoutTimed.NEW_TIMER)) {
                DisposableHelper.replace(this, this.worker.schedule(new SubscribeNext(idx), this.timeout, this.unit));
            }
        }

        /* access modifiers changed from: package-private */
        public void subscribeNext() {
            this.other.subscribe(new FullArbiterObserver(this.arbiter));
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.arbiter.onError(t, this.s);
            this.worker.dispose();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.arbiter.onComplete(this.s);
                this.worker.dispose();
            }
        }

        public void dispose() {
            this.s.dispose();
            this.worker.dispose();
        }

        public boolean isDisposed() {
            return this.worker.isDisposed();
        }

        final class SubscribeNext implements Runnable {
            private final long idx;

            SubscribeNext(long idx2) {
                this.idx = idx2;
            }

            public void run() {
                if (this.idx == TimeoutTimedOtherObserver.this.index) {
                    TimeoutTimedOtherObserver.this.done = true;
                    TimeoutTimedOtherObserver.this.s.dispose();
                    DisposableHelper.dispose(TimeoutTimedOtherObserver.this);
                    TimeoutTimedOtherObserver.this.subscribeNext();
                    TimeoutTimedOtherObserver.this.worker.dispose();
                }
            }
        }
    }

    static final class TimeoutTimedObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
        private static final long serialVersionUID = -8387234228317808253L;
        final Observer<? super T> actual;
        volatile boolean done;
        volatile long index;
        Disposable s;
        final long timeout;
        final TimeUnit unit;
        final Scheduler.Worker worker;

        TimeoutTimedObserver(Observer<? super T> actual2, long timeout2, TimeUnit unit2, Scheduler.Worker worker2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
                scheduleTimeout(0);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long idx = this.index + 1;
                this.index = idx;
                this.actual.onNext(t);
                scheduleTimeout(idx);
            }
        }

        /* access modifiers changed from: package-private */
        public void scheduleTimeout(long idx) {
            Disposable d = (Disposable) get();
            if (d != null) {
                d.dispose();
            }
            if (compareAndSet(d, ObservableTimeoutTimed.NEW_TIMER)) {
                DisposableHelper.replace(this, this.worker.schedule(new TimeoutTask(idx), this.timeout, this.unit));
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.actual.onError(t);
            dispose();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onComplete();
                dispose();
            }
        }

        public void dispose() {
            this.s.dispose();
            this.worker.dispose();
        }

        public boolean isDisposed() {
            return this.worker.isDisposed();
        }

        final class TimeoutTask implements Runnable {
            private final long idx;

            TimeoutTask(long idx2) {
                this.idx = idx2;
            }

            public void run() {
                if (this.idx == TimeoutTimedObserver.this.index) {
                    TimeoutTimedObserver.this.done = true;
                    TimeoutTimedObserver.this.s.dispose();
                    DisposableHelper.dispose(TimeoutTimedObserver.this);
                    TimeoutTimedObserver.this.actual.onError(new TimeoutException());
                    TimeoutTimedObserver.this.worker.dispose();
                }
            }
        }
    }

    static final class EmptyDisposable implements Disposable {
        EmptyDisposable() {
        }

        public void dispose() {
        }

        public boolean isDisposed() {
            return true;
        }
    }
}
