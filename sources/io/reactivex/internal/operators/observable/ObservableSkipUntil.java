package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ArrayCompositeDisposable;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.observers.SerializedObserver;

public final class ObservableSkipUntil<T, U> extends AbstractObservableWithUpstream<T, T> {
    final ObservableSource<U> other;

    public ObservableSkipUntil(ObservableSource<T> source, ObservableSource<U> other2) {
        super(source);
        this.other = other2;
    }

    public void subscribeActual(Observer<? super T> child) {
        SerializedObserver<T> serial = new SerializedObserver<>(child);
        ArrayCompositeDisposable frc = new ArrayCompositeDisposable(2);
        serial.onSubscribe(frc);
        SkipUntilObserver<T> sus = new SkipUntilObserver<>(serial, frc);
        this.other.subscribe(new SkipUntil(frc, sus, serial));
        this.source.subscribe(sus);
    }

    static final class SkipUntilObserver<T> implements Observer<T> {
        final Observer<? super T> actual;
        final ArrayCompositeDisposable frc;
        volatile boolean notSkipping;
        boolean notSkippingLocal;
        Disposable s;

        SkipUntilObserver(Observer<? super T> actual2, ArrayCompositeDisposable frc2) {
            this.actual = actual2;
            this.frc = frc2;
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.frc.setResource(0, s2);
            }
        }

        public void onNext(T t) {
            if (this.notSkippingLocal) {
                this.actual.onNext(t);
            } else if (this.notSkipping) {
                this.notSkippingLocal = true;
                this.actual.onNext(t);
            }
        }

        public void onError(Throwable t) {
            this.frc.dispose();
            this.actual.onError(t);
        }

        public void onComplete() {
            this.frc.dispose();
            this.actual.onComplete();
        }
    }

    final class SkipUntil implements Observer<U> {
        private final ArrayCompositeDisposable frc;
        Disposable s;
        private final SerializedObserver<T> serial;
        private final SkipUntilObserver<T> sus;

        SkipUntil(ArrayCompositeDisposable frc2, SkipUntilObserver<T> sus2, SerializedObserver<T> serial2) {
            this.frc = frc2;
            this.sus = sus2;
            this.serial = serial2;
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                this.frc.setResource(1, s2);
            }
        }

        public void onNext(U u) {
            this.s.dispose();
            this.sus.notSkipping = true;
        }

        public void onError(Throwable t) {
            this.frc.dispose();
            this.serial.onError(t);
        }

        public void onComplete() {
            this.sus.notSkipping = true;
        }
    }
}
