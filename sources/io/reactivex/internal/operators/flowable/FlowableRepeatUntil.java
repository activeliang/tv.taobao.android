package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import java.util.concurrent.atomic.AtomicInteger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableRepeatUntil<T> extends AbstractFlowableWithUpstream<T, T> {
    final BooleanSupplier until;

    public FlowableRepeatUntil(Flowable<T> source, BooleanSupplier until2) {
        super(source);
        this.until = until2;
    }

    public void subscribeActual(Subscriber<? super T> s) {
        SubscriptionArbiter sa = new SubscriptionArbiter();
        s.onSubscribe(sa);
        new RepeatSubscriber<>(s, this.until, sa, this.source).subscribeNext();
    }

    static final class RepeatSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Subscriber<? super T> actual;
        long produced;
        final SubscriptionArbiter sa;
        final Publisher<? extends T> source;
        final BooleanSupplier stop;

        RepeatSubscriber(Subscriber<? super T> actual2, BooleanSupplier until, SubscriptionArbiter sa2, Publisher<? extends T> source2) {
            this.actual = actual2;
            this.sa = sa2;
            this.source = source2;
            this.stop = until;
        }

        public void onSubscribe(Subscription s) {
            this.sa.setSubscription(s);
        }

        public void onNext(T t) {
            this.produced++;
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            this.actual.onError(t);
        }

        public void onComplete() {
            try {
                if (this.stop.getAsBoolean()) {
                    this.actual.onComplete();
                } else {
                    subscribeNext();
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                this.actual.onError(e);
            }
        }

        /* access modifiers changed from: package-private */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int missed = 1;
                while (!this.sa.isCancelled()) {
                    long p = this.produced;
                    if (p != 0) {
                        this.produced = 0;
                        this.sa.produced(p);
                    }
                    this.source.subscribe(this);
                    missed = addAndGet(-missed);
                    if (missed == 0) {
                        return;
                    }
                }
            }
        }
    }
}
