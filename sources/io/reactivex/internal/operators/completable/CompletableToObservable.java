package io.reactivex.internal.operators.completable;

import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public final class CompletableToObservable<T> extends Observable<T> {
    final CompletableSource source;

    public CompletableToObservable(CompletableSource source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new ObserverCompletableObserver(observer));
    }

    static final class ObserverCompletableObserver implements CompletableObserver {
        private final Observer<?> observer;

        ObserverCompletableObserver(Observer<?> observer2) {
            this.observer = observer2;
        }

        public void onComplete() {
            this.observer.onComplete();
        }

        public void onError(Throwable e) {
            this.observer.onError(e);
        }

        public void onSubscribe(Disposable d) {
            this.observer.onSubscribe(d);
        }
    }
}
