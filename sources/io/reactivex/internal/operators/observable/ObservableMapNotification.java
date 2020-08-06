package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import java.util.concurrent.Callable;

public final class ObservableMapNotification<T, R> extends AbstractObservableWithUpstream<T, ObservableSource<? extends R>> {
    final Callable<? extends ObservableSource<? extends R>> onCompleteSupplier;
    final Function<? super Throwable, ? extends ObservableSource<? extends R>> onErrorMapper;
    final Function<? super T, ? extends ObservableSource<? extends R>> onNextMapper;

    public ObservableMapNotification(ObservableSource<T> source, Function<? super T, ? extends ObservableSource<? extends R>> onNextMapper2, Function<? super Throwable, ? extends ObservableSource<? extends R>> onErrorMapper2, Callable<? extends ObservableSource<? extends R>> onCompleteSupplier2) {
        super(source);
        this.onNextMapper = onNextMapper2;
        this.onErrorMapper = onErrorMapper2;
        this.onCompleteSupplier = onCompleteSupplier2;
    }

    public void subscribeActual(Observer<? super ObservableSource<? extends R>> t) {
        this.source.subscribe(new MapNotificationObserver(t, this.onNextMapper, this.onErrorMapper, this.onCompleteSupplier));
    }

    static final class MapNotificationObserver<T, R> implements Observer<T>, Disposable {
        final Observer<? super ObservableSource<? extends R>> actual;
        final Callable<? extends ObservableSource<? extends R>> onCompleteSupplier;
        final Function<? super Throwable, ? extends ObservableSource<? extends R>> onErrorMapper;
        final Function<? super T, ? extends ObservableSource<? extends R>> onNextMapper;
        Disposable s;

        MapNotificationObserver(Observer<? super ObservableSource<? extends R>> actual2, Function<? super T, ? extends ObservableSource<? extends R>> onNextMapper2, Function<? super Throwable, ? extends ObservableSource<? extends R>> onErrorMapper2, Callable<? extends ObservableSource<? extends R>> onCompleteSupplier2) {
            this.actual = actual2;
            this.onNextMapper = onNextMapper2;
            this.onErrorMapper = onErrorMapper2;
            this.onCompleteSupplier = onCompleteSupplier2;
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void dispose() {
            this.s.dispose();
        }

        public boolean isDisposed() {
            return this.s.isDisposed();
        }

        public void onNext(T t) {
            try {
                this.actual.onNext((ObservableSource) ObjectHelper.requireNonNull(this.onNextMapper.apply(t), "The onNext ObservableSource returned is null"));
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(e);
            }
        }

        public void onError(Throwable t) {
            try {
                this.actual.onNext((ObservableSource) ObjectHelper.requireNonNull(this.onErrorMapper.apply(t), "The onError ObservableSource returned is null"));
                this.actual.onComplete();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(new CompositeException(t, e));
            }
        }

        public void onComplete() {
            try {
                this.actual.onNext((ObservableSource) ObjectHelper.requireNonNull(this.onCompleteSupplier.call(), "The onComplete ObservableSource returned is null"));
                this.actual.onComplete();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(e);
            }
        }
    }
}
