package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;
import io.reactivex.internal.disposables.DisposableHelper;

public final class ObservableSkipWhile<T> extends AbstractObservableWithUpstream<T, T> {
    final Predicate<? super T> predicate;

    public ObservableSkipWhile(ObservableSource<T> source, Predicate<? super T> predicate2) {
        super(source);
        this.predicate = predicate2;
    }

    public void subscribeActual(Observer<? super T> s) {
        this.source.subscribe(new SkipWhileObserver(s, this.predicate));
    }

    static final class SkipWhileObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> actual;
        boolean notSkipping;
        final Predicate<? super T> predicate;
        Disposable s;

        SkipWhileObserver(Observer<? super T> actual2, Predicate<? super T> predicate2) {
            this.actual = actual2;
            this.predicate = predicate2;
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
            if (this.notSkipping) {
                this.actual.onNext(t);
                return;
            }
            try {
                if (!this.predicate.test(t)) {
                    this.notSkipping = true;
                    this.actual.onNext(t);
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.s.dispose();
                this.actual.onError(e);
            }
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            this.actual.onComplete();
        }
    }
}
