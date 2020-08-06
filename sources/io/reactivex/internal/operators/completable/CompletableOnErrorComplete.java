package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Predicate;

public final class CompletableOnErrorComplete extends Completable {
    final Predicate<? super Throwable> predicate;
    final CompletableSource source;

    public CompletableOnErrorComplete(CompletableSource source2, Predicate<? super Throwable> predicate2) {
        this.source = source2;
        this.predicate = predicate2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        this.source.subscribe(new OnError(s));
    }

    final class OnError implements CompletableObserver {
        private final CompletableObserver s;

        OnError(CompletableObserver s2) {
            this.s = s2;
        }

        public void onComplete() {
            this.s.onComplete();
        }

        public void onError(Throwable e) {
            try {
                if (CompletableOnErrorComplete.this.predicate.test(e)) {
                    this.s.onComplete();
                } else {
                    this.s.onError(e);
                }
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.s.onError(new CompositeException(e, ex));
            }
        }

        public void onSubscribe(Disposable d) {
            this.s.onSubscribe(d);
        }
    }
}
