package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.Nullable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.BasicQueueDisposable;

public final class ObservableFromArray<T> extends Observable<T> {
    final T[] array;

    public ObservableFromArray(T[] array2) {
        this.array = array2;
    }

    public void subscribeActual(Observer<? super T> s) {
        FromArrayDisposable<T> d = new FromArrayDisposable<>(s, this.array);
        s.onSubscribe(d);
        if (!d.fusionMode) {
            d.run();
        }
    }

    static final class FromArrayDisposable<T> extends BasicQueueDisposable<T> {
        final Observer<? super T> actual;
        final T[] array;
        volatile boolean disposed;
        boolean fusionMode;
        int index;

        FromArrayDisposable(Observer<? super T> actual2, T[] array2) {
            this.actual = actual2;
            this.array = array2;
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
            int i = this.index;
            T[] a = this.array;
            if (i == a.length) {
                return null;
            }
            this.index = i + 1;
            return ObjectHelper.requireNonNull(a[i], "The array element is null");
        }

        public boolean isEmpty() {
            return this.index == this.array.length;
        }

        public void clear() {
            this.index = this.array.length;
        }

        public void dispose() {
            this.disposed = true;
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        /* access modifiers changed from: package-private */
        public void run() {
            T[] a = this.array;
            int n = a.length;
            int i = 0;
            while (i < n && !isDisposed()) {
                T value = a[i];
                if (value == null) {
                    this.actual.onError(new NullPointerException("The " + i + "th element is null"));
                    return;
                } else {
                    this.actual.onNext(value);
                    i++;
                }
            }
            if (!isDisposed()) {
                this.actual.onComplete();
            }
        }
    }
}
