package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiConsumer;

public final class SingleDoOnEvent<T> extends Single<T> {
    final BiConsumer<? super T, ? super Throwable> onEvent;
    final SingleSource<T> source;

    public SingleDoOnEvent(SingleSource<T> source2, BiConsumer<? super T, ? super Throwable> onEvent2) {
        this.source = source2;
        this.onEvent = onEvent2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        this.source.subscribe(new DoOnEvent(s));
    }

    final class DoOnEvent implements SingleObserver<T> {
        private final SingleObserver<? super T> s;

        DoOnEvent(SingleObserver<? super T> s2) {
            this.s = s2;
        }

        public void onSubscribe(Disposable d) {
            this.s.onSubscribe(d);
        }

        public void onSuccess(T value) {
            try {
                SingleDoOnEvent.this.onEvent.accept(value, null);
                this.s.onSuccess(value);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.s.onError(ex);
            }
        }

        public void onError(Throwable e) {
            try {
                SingleDoOnEvent.this.onEvent.accept(null, e);
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                e = new CompositeException(e, ex);
            }
            this.s.onError(e);
        }
    }
}
