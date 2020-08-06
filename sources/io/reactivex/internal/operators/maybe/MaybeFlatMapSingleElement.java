package io.reactivex.internal.operators.maybe;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.MaybeSource;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.annotations.Experimental;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.atomic.AtomicReference;

@Experimental
public final class MaybeFlatMapSingleElement<T, R> extends Maybe<R> {
    final Function<? super T, ? extends SingleSource<? extends R>> mapper;
    final MaybeSource<T> source;

    public MaybeFlatMapSingleElement(MaybeSource<T> source2, Function<? super T, ? extends SingleSource<? extends R>> mapper2) {
        this.source = source2;
        this.mapper = mapper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super R> actual) {
        this.source.subscribe(new FlatMapMaybeObserver(actual, this.mapper));
    }

    static final class FlatMapMaybeObserver<T, R> extends AtomicReference<Disposable> implements MaybeObserver<T>, Disposable {
        private static final long serialVersionUID = 4827726964688405508L;
        final MaybeObserver<? super R> actual;
        final Function<? super T, ? extends SingleSource<? extends R>> mapper;

        FlatMapMaybeObserver(MaybeObserver<? super R> actual2, Function<? super T, ? extends SingleSource<? extends R>> mapper2) {
            this.actual = actual2;
            this.mapper = mapper2;
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return DisposableHelper.isDisposed((Disposable) get());
        }

        public void onSubscribe(Disposable d) {
            if (DisposableHelper.setOnce(this, d)) {
                this.actual.onSubscribe(this);
            }
        }

        public void onSuccess(T value) {
            try {
                ((SingleSource) ObjectHelper.requireNonNull(this.mapper.apply(value), "The mapper returned a null SingleSource")).subscribe(new FlatMapSingleObserver(this, this.actual));
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                onError(ex);
            }
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }

    static final class FlatMapSingleObserver<R> implements SingleObserver<R> {
        final MaybeObserver<? super R> actual;
        final AtomicReference<Disposable> parent;

        FlatMapSingleObserver(AtomicReference<Disposable> parent2, MaybeObserver<? super R> actual2) {
            this.parent = parent2;
            this.actual = actual2;
        }

        public void onSubscribe(Disposable d) {
            DisposableHelper.replace(this.parent, d);
        }

        public void onSuccess(R value) {
            this.actual.onSuccess(value);
        }

        public void onError(Throwable e) {
            this.actual.onError(e);
        }
    }
}
