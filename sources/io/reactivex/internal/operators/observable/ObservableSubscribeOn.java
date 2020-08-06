package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableHelper;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableSubscribeOn<T> extends AbstractObservableWithUpstream<T, T> {
    final Scheduler scheduler;

    public ObservableSubscribeOn(ObservableSource<T> source, Scheduler scheduler2) {
        super(source);
        this.scheduler = scheduler2;
    }

    public void subscribeActual(Observer<? super T> s) {
        SubscribeOnObserver<T> parent = new SubscribeOnObserver<>(s);
        s.onSubscribe(parent);
        parent.setDisposable(this.scheduler.scheduleDirect(new SubscribeTask(parent)));
    }

    static final class SubscribeOnObserver<T> extends AtomicReference<Disposable> implements Observer<T>, Disposable {
        private static final long serialVersionUID = 8094547886072529208L;
        final Observer<? super T> actual;
        final AtomicReference<Disposable> s = new AtomicReference<>();

        SubscribeOnObserver(Observer<? super T> actual2) {
            this.actual = actual2;
        }

        public void onSubscribe(Disposable s2) {
            DisposableHelper.setOnce(this.s, s2);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onComplete();
        }

        public void dispose() {
            DisposableHelper.dispose(this.s);
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        /* access modifiers changed from: package-private */
        public void setDisposable(Disposable d) {
            DisposableHelper.setOnce(this, d);
        }
    }

    final class SubscribeTask implements Runnable {
        private final SubscribeOnObserver<T> parent;

        SubscribeTask(SubscribeOnObserver<T> parent2) {
            this.parent = parent2;
        }

        public void run() {
            ObservableSubscribeOn.this.source.subscribe(this.parent);
        }
    }
}
