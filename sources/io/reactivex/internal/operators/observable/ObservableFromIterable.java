package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.BasicQueueDisposable;
import java.util.Iterator;

public final class ObservableFromIterable<T> extends Observable<T> {
    final Iterable<? extends T> source;

    public ObservableFromIterable(Iterable<? extends T> source2) {
        this.source = source2;
    }

    public void subscribeActual(Observer<? super T> s) {
        try {
            Iterator<? extends T> it = this.source.iterator();
            try {
                if (!it.hasNext()) {
                    EmptyDisposable.complete((Observer<?>) s);
                    return;
                }
                FromIterableDisposable<T> d = new FromIterableDisposable<>(s, it);
                s.onSubscribe(d);
                if (!d.fusionMode) {
                    d.run();
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                EmptyDisposable.error(e, (Observer<?>) s);
            }
        } catch (Throwable e2) {
            Exceptions.throwIfFatal(e2);
            EmptyDisposable.error(e2, (Observer<?>) s);
        }
    }

    static final class FromIterableDisposable<T> extends BasicQueueDisposable<T> {
        final Observer<? super T> actual;
        boolean checkNext;
        volatile boolean disposed;
        boolean done;
        boolean fusionMode;
        final Iterator<? extends T> it;

        FromIterableDisposable(Observer<? super T> actual2, Iterator<? extends T> it2) {
            this.actual = actual2;
            this.it = it2;
        }

        /* access modifiers changed from: package-private */
        public void run() {
            while (!isDisposed()) {
                try {
                    this.actual.onNext(ObjectHelper.requireNonNull(this.it.next(), "The iterator returned a null value"));
                    if (!isDisposed()) {
                        try {
                            if (!this.it.hasNext()) {
                                if (!isDisposed()) {
                                    this.actual.onComplete();
                                    return;
                                }
                                return;
                            }
                        } catch (Throwable e) {
                            Exceptions.throwIfFatal(e);
                            this.actual.onError(e);
                            return;
                        }
                    } else {
                        return;
                    }
                } catch (Throwable e2) {
                    Exceptions.throwIfFatal(e2);
                    this.actual.onError(e2);
                    return;
                }
            }
        }

        public int requestFusion(int mode) {
            if ((mode & 1) == 0) {
                return 0;
            }
            this.fusionMode = true;
            return 1;
        }

        @Nullable
        public T poll() {
            if (this.done) {
                return null;
            }
            if (!this.checkNext) {
                this.checkNext = true;
            } else if (!this.it.hasNext()) {
                this.done = true;
                return null;
            }
            return ObjectHelper.requireNonNull(this.it.next(), "The iterator returned a null value");
        }

        public boolean isEmpty() {
            return this.done;
        }

        public void clear() {
            this.done = true;
        }

        public void dispose() {
            this.disposed = true;
        }

        public boolean isDisposed() {
            return this.disposed;
        }
    }
}
