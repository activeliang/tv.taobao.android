package io.reactivex.internal.operators.observable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableSource;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.QueueDisposable;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscLinkedArrayQueue;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class ObservableConcatMapCompletable<T> extends Completable {
    final int bufferSize;
    final Function<? super T, ? extends CompletableSource> mapper;
    final ObservableSource<T> source;

    public ObservableConcatMapCompletable(ObservableSource<T> source2, Function<? super T, ? extends CompletableSource> mapper2, int bufferSize2) {
        this.source = source2;
        this.mapper = mapper2;
        this.bufferSize = Math.max(8, bufferSize2);
    }

    public void subscribeActual(CompletableObserver observer) {
        this.source.subscribe(new SourceObserver(observer, this.mapper, this.bufferSize));
    }

    static final class SourceObserver<T> extends AtomicInteger implements Observer<T>, Disposable {
        private static final long serialVersionUID = 6893587405571511048L;
        volatile boolean active;
        final CompletableObserver actual;
        final int bufferSize;
        volatile boolean disposed;
        volatile boolean done;
        final InnerObserver inner;
        final Function<? super T, ? extends CompletableSource> mapper;
        SimpleQueue<T> queue;
        Disposable s;
        int sourceMode;

        SourceObserver(CompletableObserver actual2, Function<? super T, ? extends CompletableSource> mapper2, int bufferSize2) {
            this.actual = actual2;
            this.mapper = mapper2;
            this.bufferSize = bufferSize2;
            this.inner = new InnerObserver(actual2, this);
        }

        public void onSubscribe(Disposable s2) {
            if (DisposableHelper.validate(this.s, s2)) {
                this.s = s2;
                if (s2 instanceof QueueDisposable) {
                    QueueDisposable<T> qd = (QueueDisposable) s2;
                    int m = qd.requestFusion(3);
                    if (m == 1) {
                        this.sourceMode = m;
                        this.queue = qd;
                        this.done = true;
                        this.actual.onSubscribe(this);
                        drain();
                        return;
                    } else if (m == 2) {
                        this.sourceMode = m;
                        this.queue = qd;
                        this.actual.onSubscribe(this);
                        return;
                    }
                }
                this.queue = new SpscLinkedArrayQueue(this.bufferSize);
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                if (this.sourceMode == 0) {
                    this.queue.offer(t);
                }
                drain();
            }
        }

        public void onError(Throwable t) {
            if (this.done) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.done = true;
            dispose();
            this.actual.onError(t);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                drain();
            }
        }

        /* access modifiers changed from: package-private */
        public void innerComplete() {
            this.active = false;
            drain();
        }

        public boolean isDisposed() {
            return this.disposed;
        }

        public void dispose() {
            this.disposed = true;
            this.inner.dispose();
            this.s.dispose();
            if (getAndIncrement() == 0) {
                this.queue.clear();
            }
        }

        /* access modifiers changed from: package-private */
        public void drain() {
            if (getAndIncrement() == 0) {
                while (!this.disposed) {
                    if (!this.active) {
                        boolean d = this.done;
                        try {
                            T t = this.queue.poll();
                            boolean empty = t == null;
                            if (d && empty) {
                                this.disposed = true;
                                this.actual.onComplete();
                                return;
                            } else if (!empty) {
                                try {
                                    CompletableSource c = (CompletableSource) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null CompletableSource");
                                    this.active = true;
                                    c.subscribe(this.inner);
                                } catch (Throwable ex) {
                                    Exceptions.throwIfFatal(ex);
                                    dispose();
                                    this.queue.clear();
                                    this.actual.onError(ex);
                                    return;
                                }
                            }
                        } catch (Throwable ex2) {
                            Exceptions.throwIfFatal(ex2);
                            dispose();
                            this.queue.clear();
                            this.actual.onError(ex2);
                            return;
                        }
                    }
                    if (decrementAndGet() == 0) {
                        return;
                    }
                }
                this.queue.clear();
            }
        }

        static final class InnerObserver extends AtomicReference<Disposable> implements CompletableObserver {
            private static final long serialVersionUID = -5987419458390772447L;
            final CompletableObserver actual;
            final SourceObserver<?> parent;

            InnerObserver(CompletableObserver actual2, SourceObserver<?> parent2) {
                this.actual = actual2;
                this.parent = parent2;
            }

            public void onSubscribe(Disposable s) {
                DisposableHelper.set(this, s);
            }

            public void onError(Throwable t) {
                this.parent.dispose();
                this.actual.onError(t);
            }

            public void onComplete() {
                this.parent.innerComplete();
            }

            /* access modifiers changed from: package-private */
            public void dispose() {
                DisposableHelper.dispose(this);
            }
        }
    }
}
