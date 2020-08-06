package io.reactivex.internal.operators.observable;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.FuseToObservable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Callable;

public final class ObservableCollectSingle<T, U> extends Single<U> implements FuseToObservable<U> {
    final BiConsumer<? super U, ? super T> collector;
    final Callable<? extends U> initialSupplier;
    final ObservableSource<T> source;

    public ObservableCollectSingle(ObservableSource<T> source2, Callable<? extends U> initialSupplier2, BiConsumer<? super U, ? super T> collector2) {
        this.source = source2;
        this.initialSupplier = initialSupplier2;
        this.collector = collector2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super U> t) {
        try {
            this.source.subscribe(new CollectObserver(t, ObjectHelper.requireNonNull(this.initialSupplier.call(), "The initialSupplier returned a null value"), this.collector));
        } catch (Throwable e) {
            EmptyDisposable.error(e, (SingleObserver<?>) t);
        }
    }

    public Observable<U> fuseToObservable() {
        return RxJavaPlugins.onAssembly(new ObservableCollect(this.source, this.initialSupplier, this.collector));
    }

    static final class CollectObserver<T, U> implements Observer<T>, Disposable {
        final SingleObserver<? super U> actual;
        final BiConsumer<? super U, ? super T> collector;
        boolean done;
        Disposable s;
        final U u;

        CollectObserver(SingleObserver<? super U> actual2, U u2, BiConsumer<? super U, ? super T> collector2) {
            this.actual = actual2;
            this.collector = collector2;
            this.u = u2;
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
            if (!this.done) {
                try {
                    this.collector.accept(this.u, t);
                } catch (Throwable e) {
                    this.s.dispose();
                    onError(e);
                }
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.actual.onSuccess(this.u);
            }
        }
    }
}
