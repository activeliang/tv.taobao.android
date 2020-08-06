package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.observers.BasicFuseableObserver;

@Experimental
public final class ObservableDoAfterNext<T> extends AbstractObservableWithUpstream<T, T> {
    final Consumer<? super T> onAfterNext;

    public ObservableDoAfterNext(ObservableSource<T> source, Consumer<? super T> onAfterNext2) {
        super(source);
        this.onAfterNext = onAfterNext2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> s) {
        this.source.subscribe(new DoAfterObserver(s, this.onAfterNext));
    }

    static final class DoAfterObserver<T> extends BasicFuseableObserver<T, T> {
        final Consumer<? super T> onAfterNext;

        DoAfterObserver(Observer<? super T> actual, Consumer<? super T> onAfterNext2) {
            super(actual);
            this.onAfterNext = onAfterNext2;
        }

        public void onNext(T t) {
            this.actual.onNext(t);
            if (this.sourceMode == 0) {
                try {
                    this.onAfterNext.accept(t);
                } catch (Throwable ex) {
                    fail(ex);
                }
            }
        }

        public int requestFusion(int mode) {
            return transitiveBoundaryFusion(mode);
        }

        @Nullable
        public T poll() throws Exception {
            T v = this.qs.poll();
            if (v != null) {
                this.onAfterNext.accept(v);
            }
            return v;
        }
    }
}
