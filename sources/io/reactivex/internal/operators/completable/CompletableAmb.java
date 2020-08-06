package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public final class CompletableAmb extends Completable {
    private final CompletableSource[] sources;
    private final Iterable<? extends CompletableSource> sourcesIterable;

    public CompletableAmb(CompletableSource[] sources2, Iterable<? extends CompletableSource> sourcesIterable2) {
        this.sources = sources2;
        this.sourcesIterable = sourcesIterable2;
    }

    public void subscribeActual(CompletableObserver s) {
        int count;
        int count2;
        CompletableSource[] sources2 = this.sources;
        int count3 = 0;
        if (sources2 == null) {
            sources2 = new CompletableSource[8];
            try {
                Iterator<? extends CompletableSource> it = this.sourcesIterable.iterator();
                while (true) {
                    try {
                        count2 = count3;
                        if (!it.hasNext()) {
                            count = count2;
                            break;
                        }
                        CompletableSource element = (CompletableSource) it.next();
                        if (element == null) {
                            EmptyDisposable.error((Throwable) new NullPointerException("One of the sources is null"), s);
                            int i = count2;
                            return;
                        }
                        if (count2 == sources2.length) {
                            CompletableSource[] b = new CompletableSource[((count2 >> 2) + count2)];
                            System.arraycopy(sources2, 0, b, 0, count2);
                            sources2 = b;
                        }
                        count3 = count2 + 1;
                        sources2[count2] = element;
                    } catch (Throwable th) {
                        e = th;
                        int i2 = count2;
                        Exceptions.throwIfFatal(e);
                        EmptyDisposable.error(e, s);
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
        s.onSubscribe(set);
        AtomicBoolean once = new AtomicBoolean();
        CompletableObserver inner = new Amb(once, set, s);
        for (int i3 = 0; i3 < count; i3++) {
            CompletableSource c = sources2[i3];
            if (set.isDisposed()) {
                return;
            }
            if (c == null) {
                NullPointerException npe = new NullPointerException("One of the sources is null");
                if (once.compareAndSet(false, true)) {
                    set.dispose();
                    s.onError(npe);
                    return;
                }
                RxJavaPlugins.onError(npe);
                return;
            }
            c.subscribe(inner);
        }
        if (count == 0) {
            s.onComplete();
        }
    }

    static final class Amb implements CompletableObserver {
        private final AtomicBoolean once;
        private final CompletableObserver s;
        private final CompositeDisposable set;

        Amb(AtomicBoolean once2, CompositeDisposable set2, CompletableObserver s2) {
            this.once = once2;
            this.set = set2;
            this.s = s2;
        }

        public void onComplete() {
            if (this.once.compareAndSet(false, true)) {
                this.set.dispose();
                this.s.onComplete();
            }
        }

        public void onError(Throwable e) {
            if (this.once.compareAndSet(false, true)) {
                this.set.dispose();
                this.s.onError(e);
                return;
            }
            RxJavaPlugins.onError(e);
        }

        public void onSubscribe(Disposable d) {
            this.set.add(d);
        }
    }
}
