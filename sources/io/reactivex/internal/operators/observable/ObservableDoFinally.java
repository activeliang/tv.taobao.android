package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.fuseable.QueueDisposable;
import io.reactivex.internal.observers.BasicIntQueueDisposable;
import io.reactivex.plugins.RxJavaPlugins;

@Experimental
public final class ObservableDoFinally<T> extends AbstractObservableWithUpstream<T, T> {
    final Action onFinally;

    public ObservableDoFinally(ObservableSource<T> source, Action onFinally2) {
        super(source);
        this.onFinally = onFinally2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> s) {
        this.source.subscribe(new DoFinallyObserver(s, this.onFinally));
    }

    static final class DoFinallyObserver<T> extends BasicIntQueueDisposable<T> implements Observer<T> {
        private static final long serialVersionUID = 4109457741734051389L;
        final Observer<? super T> actual;
        Disposable d;
        final Action onFinally;
        QueueDisposable<T> qd;
        boolean syncFused;

        DoFinallyObserver(Observer<? super T> actual2, Action onFinally2) {
            this.actual = actual2;
            this.onFinally = onFinally2;
        }

        public void onSubscribe(Disposable d2) {
            if (DisposableHelper.validate(this.d, d2)) {
                this.d = d2;
                if (d2 instanceof QueueDisposable) {
                    this.qd = (QueueDisposable) d2;
                }
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
            runFinally();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void dispose() {
            this.d.dispose();
            runFinally();
        }

        public boolean isDisposed() {
            return this.d.isDisposed();
        }

        public int requestFusion(int mode) {
            boolean z = true;
            QueueDisposable<T> qd2 = this.qd;
            if (qd2 == null || (mode & 4) != 0) {
                return 0;
            }
            int m = qd2.requestFusion(mode);
            if (m == 0) {
                return m;
            }
            if (m != 1) {
                z = false;
            }
            this.syncFused = z;
            return m;
        }

        public void clear() {
            this.qd.clear();
        }

        public boolean isEmpty() {
            return this.qd.isEmpty();
        }

        @Nullable
        public T poll() throws Exception {
            T v = this.qd.poll();
            if (v == null && this.syncFused) {
                runFinally();
            }
            return v;
        }

        /* access modifiers changed from: package-private */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }
}
