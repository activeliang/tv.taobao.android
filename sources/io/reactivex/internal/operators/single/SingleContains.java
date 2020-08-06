package io.reactivex.internal.operators.single;

import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiPredicate;

public final class SingleContains<T> extends io.reactivex.Single<Boolean> {
    final BiPredicate<Object, Object> comparer;
    final SingleSource<T> source;
    final Object value;

    public SingleContains(SingleSource<T> source2, Object value2, BiPredicate<Object, Object> comparer2) {
        this.source = source2;
        this.value = value2;
        this.comparer = comparer2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Boolean> s) {
        this.source.subscribe(new Single(s));
    }

    final class Single implements SingleObserver<T> {
        private final SingleObserver<? super Boolean> s;

        Single(SingleObserver<? super Boolean> s2) {
            this.s = s2;
        }

        public void onSubscribe(Disposable d) {
            this.s.onSubscribe(d);
        }

        public void onSuccess(T v) {
            try {
                this.s.onSuccess(Boolean.valueOf(SingleContains.this.comparer.test(v, SingleContains.this.value)));
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.s.onError(ex);
            }
        }

        public void onError(Throwable e) {
            this.s.onError(e);
        }
    }
}
