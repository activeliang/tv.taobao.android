package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionArbiter;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDelaySubscriptionOther<T, U> extends Flowable<T> {
    final Publisher<? extends T> main;
    final Publisher<U> other;

    public FlowableDelaySubscriptionOther(Publisher<? extends T> main2, Publisher<U> other2) {
        this.main = main2;
        this.other = other2;
    }

    public void subscribeActual(Subscriber<? super T> child) {
        SubscriptionArbiter serial = new SubscriptionArbiter();
        child.onSubscribe(serial);
        this.other.subscribe(new DelaySubscriber(serial, child));
    }

    final class DelaySubscriber implements FlowableSubscriber<U> {
        final Subscriber<? super T> child;
        boolean done;
        final SubscriptionArbiter serial;

        DelaySubscriber(SubscriptionArbiter serial2, Subscriber<? super T> child2) {
            this.serial = serial2;
            this.child = child2;
        }

        public void onSubscribe(Subscription s) {
            this.serial.setSubscription(new DelaySubscription(s));
            s.request(Long.MAX_VALUE);
        }

        public void onNext(U u) {
            onComplete();
        }

        public void onError(Throwable e) {
            if (this.done) {
                RxJavaPlugins.onError(e);
                return;
            }
            this.done = true;
            this.child.onError(e);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                FlowableDelaySubscriptionOther.this.main.subscribe(new OnCompleteSubscriber());
            }
        }

        final class DelaySubscription implements Subscription {
            private final Subscription s;

            DelaySubscription(Subscription s2) {
                this.s = s2;
            }

            public void request(long n) {
            }

            public void cancel() {
                this.s.cancel();
            }
        }

        final class OnCompleteSubscriber implements FlowableSubscriber<T> {
            OnCompleteSubscriber() {
            }

            public void onSubscribe(Subscription s) {
                DelaySubscriber.this.serial.setSubscription(s);
            }

            public void onNext(T t) {
                DelaySubscriber.this.child.onNext(t);
            }

            public void onError(Throwable t) {
                DelaySubscriber.this.child.onError(t);
            }

            public void onComplete() {
                DelaySubscriber.this.child.onComplete();
            }
        }
    }
}
