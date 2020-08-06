package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.annotations.Experimental;
import io.reactivex.disposables.Disposable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Experimental
public final class CompletableCache extends Completable implements CompletableObserver {
    static final InnerCompletableCache[] EMPTY = new InnerCompletableCache[0];
    static final InnerCompletableCache[] TERMINATED = new InnerCompletableCache[0];
    Throwable error;
    final AtomicReference<InnerCompletableCache[]> observers = new AtomicReference<>(EMPTY);
    final AtomicBoolean once = new AtomicBoolean();
    final CompletableSource source;

    public CompletableCache(CompletableSource source2) {
        this.source = source2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        InnerCompletableCache inner = new InnerCompletableCache(s);
        s.onSubscribe(inner);
        if (add(inner)) {
            if (inner.isDisposed()) {
                remove(inner);
            }
            if (this.once.compareAndSet(false, true)) {
                this.source.subscribe(this);
                return;
            }
            return;
        }
        Throwable ex = this.error;
        if (ex != null) {
            s.onError(ex);
        } else {
            s.onComplete();
        }
    }

    public void onSubscribe(Disposable d) {
    }

    public void onError(Throwable e) {
        this.error = e;
        for (InnerCompletableCache inner : this.observers.getAndSet(TERMINATED)) {
            if (!inner.get()) {
                inner.actual.onError(e);
            }
        }
    }

    public void onComplete() {
        for (InnerCompletableCache inner : this.observers.getAndSet(TERMINATED)) {
            if (!inner.get()) {
                inner.actual.onComplete();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean add(InnerCompletableCache inner) {
        InnerCompletableCache[] a;
        InnerCompletableCache[] b;
        do {
            a = this.observers.get();
            if (a == TERMINATED) {
                return false;
            }
            int n = a.length;
            b = new InnerCompletableCache[(n + 1)];
            System.arraycopy(a, 0, b, 0, n);
            b[n] = inner;
        } while (!this.observers.compareAndSet(a, b));
        return true;
    }

    /* access modifiers changed from: package-private */
    public void remove(InnerCompletableCache inner) {
        InnerCompletableCache[] a;
        InnerCompletableCache[] b;
        do {
            a = this.observers.get();
            int n = a.length;
            if (n != 0) {
                int j = -1;
                int i = 0;
                while (true) {
                    if (i >= n) {
                        break;
                    } else if (a[i] == inner) {
                        j = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (j < 0) {
                    return;
                }
                if (n == 1) {
                    b = EMPTY;
                } else {
                    b = new InnerCompletableCache[(n - 1)];
                    System.arraycopy(a, 0, b, 0, j);
                    System.arraycopy(a, j + 1, b, j, (n - j) - 1);
                }
            } else {
                return;
            }
        } while (!this.observers.compareAndSet(a, b));
    }

    final class InnerCompletableCache extends AtomicBoolean implements Disposable {
        private static final long serialVersionUID = 8943152917179642732L;
        final CompletableObserver actual;

        InnerCompletableCache(CompletableObserver actual2) {
            this.actual = actual2;
        }

        public boolean isDisposed() {
            return get();
        }

        public void dispose() {
            if (compareAndSet(false, true)) {
                CompletableCache.this.remove(this);
            }
        }
    }
}
