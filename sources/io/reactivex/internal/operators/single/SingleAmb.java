package io.reactivex.internal.operators.single;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SingleAmb<T> extends Single<T> {
    private final SingleSource<? extends T>[] sources;
    private final Iterable<? extends SingleSource<? extends T>> sourcesIterable;

    public SingleAmb(SingleSource<? extends T>[] sources2, Iterable<? extends SingleSource<? extends T>> sourcesIterable2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> s) {
        int count;
        int count2;
        SingleSource<? extends T>[] sources2 = this.sources;
        int count3 = 0;
        if (sources2 == null) {
            sources2 = new SingleSource[8];
            try {
                Iterator<? extends SingleSource<? extends T>> it = this.sourcesIterable.iterator();
                while (true) {
                    try {
                        count2 = count3;
                        if (!it.hasNext()) {
                            count = count2;
                            break;
                        }
                        SingleSource<? extends T> element = (SingleSource) it.next();
                        if (element == null) {
                            EmptyDisposable.error((Throwable) new NullPointerException("One of the sources is null"), (SingleObserver<?>) s);
                            int i = count2;
                            return;
                        }
                        if (count2 == sources2.length) {
                            SingleSource<? extends T>[] b = new SingleSource[((count2 >> 2) + count2)];
                            System.arraycopy(sources2, 0, b, 0, count2);
                            sources2 = b;
                        }
                        count3 = count2 + 1;
                        sources2[count2] = element;
                    } catch (Throwable th) {
                        e = th;
                        int i2 = count2;
                        Exceptions.throwIfFatal(e);
                        EmptyDisposable.error(e, (SingleObserver<?>) s);
                        return;
                    }
                }
            } catch (Throwable th2) {
                e = th2;
            }
        } else {
            count = sources2.length;
        }
        CompositeDisposable set = new CompositeDisposable();
        AmbSingleObserver<T> shared = new AmbSingleObserver<>(s, set);
        s.onSubscribe(set);
        int i3 = 0;
        while (i3 < count) {
            SingleSource<? extends T> s1 = sources2[i3];
            if (shared.get()) {
                return;
            }
            if (s1 == null) {
                set.dispose();
                Throwable e = new NullPointerException("One of the sources is null");
                if (shared.compareAndSet(false, true)) {
                    s.onError(e);
                    return;
                } else {
                    RxJavaPlugins.onError(e);
                    return;
                }
            } else {
                s1.subscribe(shared);
                i3++;
            }
        }
    }

    static final class AmbSingleObserver<T> extends AtomicBoolean implements SingleObserver<T> {
        private static final long serialVersionUID = -1944085461036028108L;
        final SingleObserver<? super T> s;
        final CompositeDisposable set;

        AmbSingleObserver(SingleObserver<? super T> s2, CompositeDisposable set2) {
            this.s = s2;
            this.set = set2;
        }

        public void onSubscribe(Disposable d) {
            this.set.add(d);
        }

        public void onSuccess(T value) {
            if (compareAndSet(false, true)) {
                this.set.dispose();
                this.s.onSuccess(value);
            }
        }

        public void onError(Throwable e) {
            if (compareAndSet(false, true)) {
                this.set.dispose();
                this.s.onError(e);
                return;
            }
            RxJavaPlugins.onError(e);
        }
    }
}
