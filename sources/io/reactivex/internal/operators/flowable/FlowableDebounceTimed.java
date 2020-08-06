package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.internal.disposables.DisposableHelper;
import io.reactivex.internal.disposables.SequentialDisposable;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subscribers.SerializedSubscriber;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDebounceTimed<T> extends AbstractFlowableWithUpstream<T, T> {
    final Scheduler scheduler;
    final long timeout;
    final TimeUnit unit;

    public FlowableDebounceTimed(Flowable<T> source, long timeout2, TimeUnit unit2, Scheduler scheduler2) {
        super(source);
        this.timeout = timeout2;
        this.unit = unit2;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe(new DebounceTimedSubscriber(new SerializedSubscriber(s), this.timeout, this.unit, this.scheduler.createWorker()));
    }

    static final class DebounceTimedSubscriber<T> extends AtomicLong implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = -9102637559663639004L;
        final Subscriber<? super T> actual;
        boolean done;
        volatile long index;
        Subscription s;
        final long timeout;
        final SequentialDisposable timer = new SequentialDisposable();
        final TimeUnit unit;
        final Scheduler.Worker worker;

        DebounceTimedSubscriber(Subscriber<? super T> actual2, long timeout2, TimeUnit unit2, Scheduler.Worker worker2) {
            this.actual = actual2;
            this.timeout = timeout2;
            this.unit = unit2;
            this.worker = worker2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
                s2.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long idx = this.index + 1;
                this.index = idx;
                Disposable d = (Disposable) this.timer.get();
                if (d != null) {
                    d.dispose();
                }
                DebounceEmitter<T> de = new DebounceEmitter<>(t, idx, this);
                if (this.timer.replace(de)) {
                    de.setResource(this.worker.schedule(de, this.timeout, this.unit));
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
            this.worker.dispose();
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                Disposable d = (Disposable) this.timer.get();
                if (!DisposableHelper.isDisposed(d)) {
                    DebounceEmitter<T> de = (DebounceEmitter) d;
                    if (de != null) {
                        de.emit();
                    }
                    DisposableHelper.dispose(this.timer);
                    this.actual.onComplete();
                    this.worker.dispose();
                }
            }
        }

        public void request(long n) {
            if (SubscriptionHelper.validate(n)) {
                BackpressureHelper.add(this, n);
            }
        }

        public void cancel() {
            this.s.cancel();
            this.worker.dispose();
        }

        /* access modifiers changed from: package-private */
        public void emit(long idx, T t, DebounceEmitter<T> emitter) {
            if (idx != this.index) {
                return;
            }
            if (get() != 0) {
                this.actual.onNext(t);
                BackpressureHelper.produced(this, 1);
                emitter.dispose();
                return;
            }
            cancel();
            this.actual.onError(new MissingBackpressureException("Could not deliver value due to lack of requests"));
        }
    }

    static final class DebounceEmitter<T> extends AtomicReference<Disposable> implements Runnable, Disposable {
        private static final long serialVersionUID = 6812032969491025141L;
        final long idx;
        final AtomicBoolean once = new AtomicBoolean();
        final DebounceTimedSubscriber<T> parent;
        final T value;

        DebounceEmitter(T value2, long idx2, DebounceTimedSubscriber<T> parent2) {
            this.value = value2;
            this.idx = idx2;
            this.parent = parent2;
        }

        public void run() {
            emit();
        }

        /* access modifiers changed from: package-private */
        public void emit() {
            if (this.once.compareAndSet(false, true)) {
                this.parent.emit(this.idx, this.value, this);
            }
        }

        public void dispose() {
            DisposableHelper.dispose(this);
        }

        public boolean isDisposed() {
            return get() == DisposableHelper.DISPOSED;
        }

        public void setResource(Disposable d) {
            DisposableHelper.replace(this, d);
        }
    }
}
