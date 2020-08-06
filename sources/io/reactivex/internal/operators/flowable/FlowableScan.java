package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableScan<T> extends AbstractFlowableWithUpstream<T, T> {
    final BiFunction<T, T, T> accumulator;

    public FlowableScan(Flowable<T> source, BiFunction<T, T, T> accumulator2) {
        super(source);
        this.accumulator = accumulator2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe(new ScanSubscriber(s, this.accumulator));
    }

    static final class ScanSubscriber<T> implements FlowableSubscriber<T>, Subscription {
        final BiFunction<T, T, T> accumulator;
        final Subscriber<? super T> actual;
        boolean done;
        Subscription s;
        T value;

        ScanSubscriber(Subscriber<? super T> actual2, BiFunction<T, T, T> accumulator2) {
            this.actual = actual2;
            this.accumulator = accumulator2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                Subscriber<? super T> a = this.actual;
                T v = this.value;
                if (v == null) {
                    this.value = t;
                    a.onNext(t);
                    return;
                }
                try {
                    T u = ObjectHelper.requireNonNull(this.accumulator.apply(v, t), "The value returned by the accumulator is null");
                    this.value = u;
                    a.onNext(u);
                } catch (Throwable e) {
                    Exceptions.throwIfFatal(e);
                    this.s.cancel();
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
                this.actual.onComplete();
            }
        }

        public void request(long n) {
            this.s.request(n);
        }

        public void cancel() {
            this.s.cancel();
        }
    }
}
