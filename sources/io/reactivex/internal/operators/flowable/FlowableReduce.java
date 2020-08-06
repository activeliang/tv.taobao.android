package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BiFunction;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableReduce<T> extends AbstractFlowableWithUpstream<T, T> {
    final BiFunction<T, T, T> reducer;

    public FlowableReduce(Flowable<T> source, BiFunction<T, T, T> reducer2) {
        super(source);
        this.reducer = reducer2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe(new ReduceSubscriber(s, this.reducer));
    }

    static final class ReduceSubscriber<T> extends DeferredScalarSubscription<T> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -4663883003264602070L;
        final BiFunction<T, T, T> reducer;
        Subscription s;

        ReduceSubscriber(Subscriber<? super T> actual, BiFunction<T, T, T> reducer2) {
            super(actual);
            this.reducer = reducer2;
        }

        public void onSubscribe(Subscription s2) {
            if (SubscriptionHelper.validate(this.s, s2)) {
                this.s = s2;
                this.actual.onSubscribe(this);
                s2.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (this.s != SubscriptionHelper.CANCELLED) {
                T v = this.value;
                if (v == null) {
                    this.value = t;
                    return;
                }
                try {
                    this.value = ObjectHelper.requireNonNull(this.reducer.apply(v, t), "The reducer returned a null value");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    this.s.cancel();
                    onError(ex);
                }
            }
        }

        public void onError(Throwable t) {
            if (this.s == SubscriptionHelper.CANCELLED) {
                RxJavaPlugins.onError(t);
                return;
            }
            this.s = SubscriptionHelper.CANCELLED;
            this.actual.onError(t);
        }

        public void onComplete() {
            if (this.s != SubscriptionHelper.CANCELLED) {
                this.s = SubscriptionHelper.CANCELLED;
                T v = this.value;
                if (v != null) {
                    complete(v);
                } else {
                    this.actual.onComplete();
                }
            }
        }

        public void cancel() {
            super.cancel();
            this.s.cancel();
            this.s = SubscriptionHelper.CANCELLED;
        }
    }
}
