package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.Experimental;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.subscriptions.BasicIntQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Experimental
public final class FlowableDoFinally<T> extends AbstractFlowableWithUpstream<T, T> {
    final Action onFinally;

    public FlowableDoFinally(Flowable<T> source, Action onFinally2) {
        super(source);
        this.onFinally = onFinally2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        if (s instanceof ConditionalSubscriber) {
            this.source.subscribe(new DoFinallyConditionalSubscriber((ConditionalSubscriber) s, this.onFinally));
        } else {
            this.source.subscribe(new DoFinallySubscriber(s, this.onFinally));
        }
    }

    static final class DoFinallySubscriber<T> extends BasicIntQueueSubscription<T> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = 4109457741734051389L;
        final Subscriber<? super T> actual;
        final Action onFinally;
        QueueSubscription<T> qs;
        Subscription s;
        boolean syncFused;

        DoFinallySubscriber(Subscriber<? super T> actual2, Action onFinally2) {
            this.actual = actual2;
            this.onFinally = onFinally2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                if (s2 instanceof QueueSubscription) {
                    this.qs = (QueueSubscription) s2;
                }
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
            runFinally();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void cancel() {
            this.s.cancel();
            runFinally();
        }

        public void request(long n) {
            this.s.request(n);
        }

        public int requestFusion(int mode) {
            boolean z = true;
            QueueSubscription<T> qs2 = this.qs;
            if (qs2 == null || (mode & 4) != 0) {
                return 0;
            }
            int m = qs2.requestFusion(mode);
            if (m == 0) {
                return m;
            }
            if (m != 1) {
                z = false;
            }
            this.syncFused = z;
            return m;
        }

        public void clear() {
            this.qs.clear();
        }

        public boolean isEmpty() {
            return this.qs.isEmpty();
        }

        @Nullable
        public T poll() throws Exception {
            T v = this.qs.poll();
            if (v == null && this.syncFused) {
                runFinally();
            }
            return v;
        }

        /* access modifiers changed from: package-private */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }

    static final class DoFinallyConditionalSubscriber<T> extends BasicIntQueueSubscription<T> implements ConditionalSubscriber<T> {
        private static final long serialVersionUID = 4109457741734051389L;
        final ConditionalSubscriber<? super T> actual;
        final Action onFinally;
        QueueSubscription<T> qs;
        Subscription s;
        boolean syncFused;

        DoFinallyConditionalSubscriber(ConditionalSubscriber<? super T> actual2, Action onFinally2) {
            this.actual = actual2;
            this.onFinally = onFinally2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                if (s2 instanceof QueueSubscription) {
                    this.qs = (QueueSubscription) s2;
                }
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public boolean tryOnNext(T t) {
            return this.actual.tryOnNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
            runFinally();
        }

        public void onComplete() {
            this.actual.onComplete();
            runFinally();
        }

        public void cancel() {
            this.s.cancel();
            runFinally();
        }

        public void request(long n) {
            this.s.request(n);
        }

        public int requestFusion(int mode) {
            boolean z = true;
            QueueSubscription<T> qs2 = this.qs;
            if (qs2 == null || (mode & 4) != 0) {
                return 0;
            }
            int m = qs2.requestFusion(mode);
            if (m == 0) {
                return m;
            }
            if (m != 1) {
                z = false;
            }
            this.syncFused = z;
            return m;
        }

        public void clear() {
            this.qs.clear();
        }

        public boolean isEmpty() {
            return this.qs.isEmpty();
        }

        @Nullable
        public T poll() throws Exception {
            T v = this.qs.poll();
            if (v == null && this.syncFused) {
                runFinally();
            }
            return v;
        }

        /* access modifiers changed from: package-private */
        public void runFinally() {
            if (compareAndSet(0, 1)) {
                try {
                    this.onFinally.run();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    RxJavaPlugins.onError(ex);
                }
            }
        }
    }
}
