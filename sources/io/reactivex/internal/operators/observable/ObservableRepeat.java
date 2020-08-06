package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.SequentialDisposable;
import java.util.concurrent.atomic.AtomicInteger;

public final class ObservableRepeat<T> extends AbstractObservableWithUpstream<T, T> {
    final long count;

    public ObservableRepeat(Observable<T> source, long count2) {
        super(source);
        this.count = count2;
    }

    public void subscribeActual(Observer<? super T> s) {
        long j = Long.MAX_VALUE;
        SequentialDisposable sd = new SequentialDisposable();
        s.onSubscribe(sd);
        if (this.count != Long.MAX_VALUE) {
            j = this.count - 1;
        }
        new RepeatObserver<>(s, j, sd, this.source).subscribeNext();
    }

    static final class RepeatObserver<T> extends AtomicInteger implements Observer<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Observer<? super T> actual;
        long remaining;
        final SequentialDisposable sd;
        final ObservableSource<? extends T> source;

        RepeatObserver(Observer<? super T> actual2, long count, SequentialDisposable sd2, ObservableSource<? extends T> source2) {
            this.actual = actual2;
            this.sd = sd2;
            this.source = source2;
            this.remaining = count;
        }

        public void onSubscribe(Disposable s) {
            this.sd.replace(s);
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            long r = this.remaining;
            if (r != Long.MAX_VALUE) {
                this.remaining = r - 1;
            }
            if (r != 0) {
                subscribeNext();
            } else {
                this.actual.onComplete();
            }
        }

        /* access modifiers changed from: package-private */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                while (!this.sd.isDisposed()) {
                    this.source.subscribe(this);
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                }
            }
        }
    }
}
