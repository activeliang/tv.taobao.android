package io.reactivex.internal.operators.completable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.SequentialDisposable;

public final class CompletableResumeNext extends Completable {
    final Function<? super Throwable, ? extends CompletableSource> errorMapper;
    final CompletableSource source;

    public CompletableResumeNext(CompletableSource source2, Function<? super Throwable, ? extends CompletableSource> errorMapper2) {
        this.source = source2;
        this.errorMapper = errorMapper2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(CompletableObserver s) {
        SequentialDisposable sd = new SequentialDisposable();
        s.onSubscribe(sd);
        this.source.subscribe(new ResumeNext(s, sd));
    }

    final class ResumeNext implements CompletableObserver {
        final CompletableObserver s;
        final SequentialDisposable sd;

        ResumeNext(CompletableObserver s2, SequentialDisposable sd2) {
            this.s = s2;
            this.sd = sd2;
        }

        public void onComplete() {
            this.s.onComplete();
        }

        public void onError(Throwable e) {
            try {
                CompletableSource c = (CompletableSource) CompletableResumeNext.this.errorMapper.apply(e);
                if (c == null) {
                    NullPointerException npe = new NullPointerException("The CompletableConsumable returned is null");
                    npe.initCause(e);
                    this.s.onError(npe);
                    return;
                }
                c.subscribe(new OnErrorObserver());
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                this.s.onError(new CompositeException(ex, e));
            }
        }

        public void onSubscribe(Disposable d) {
            this.sd.update(d);
        }

        final class OnErrorObserver implements CompletableObserver {
            OnErrorObserver() {
            }

            public void onComplete() {
                ResumeNext.this.s.onComplete();
            }

            public void onError(Throwable e) {
                ResumeNext.this.s.onError(e);
            }

            public void onSubscribe(Disposable d) {
                ResumeNext.this.sd.update(d);
            }
        }
    }
}
