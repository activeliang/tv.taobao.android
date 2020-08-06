package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.LongConsumer;
import io.reactivex.internal.subscriptions.EmptySubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableDoOnLifecycle<T> extends AbstractFlowableWithUpstream<T, T> {
    private final Action onCancel;
    private final LongConsumer onRequest;
    private final Consumer<? super Subscription> onSubscribe;

    public FlowableDoOnLifecycle(Flowable<T> source, Consumer<? super Subscription> onSubscribe2, LongConsumer onRequest2, Action onCancel2) {
        super(source);
        this.onSubscribe = onSubscribe2;
        this.onRequest = onRequest2;
        this.onCancel = onCancel2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe(new SubscriptionLambdaSubscriber(s, this.onSubscribe, this.onRequest, this.onCancel));
    }

    static final class SubscriptionLambdaSubscriber<T> implements FlowableSubscriber<T>, Subscription {
        final Subscriber<? super T> actual;
        final Action onCancel;
        final LongConsumer onRequest;
        final Consumer<? super Subscription> onSubscribe;
        Subscription s;

        SubscriptionLambdaSubscriber(Subscriber<? super T> actual2, Consumer<? super Subscription> onSubscribe2, LongConsumer onRequest2, Action onCancel2) {
            this.actual = actual2;
            this.onSubscribe = onSubscribe2;
            this.onCancel = onCancel2;
            this.onRequest = onRequest2;
        }

        public void onSubscribe(Subscription s2) {
            try {
                this.onSubscribe.accept(s2);
                if (SubscriptionHelper.validate(this.s, s2)) {
                    this.s = s2;
                    this.actual.onSubscribe(this);
                }
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                s2.cancel();
                this.s = SubscriptionHelper.CANCELLED;
                EmptySubscription.error(e, this.actual);
            }
        }

        public void onNext(T t) {
            this.actual.onNext(t);
        }

        public void onError(Throwable t) {
            if (this.s != SubscriptionHelper.CANCELLED) {
                this.actual.onError(t);
            } else {
                RxJavaPlugins.onError(t);
            }
        }

        public void onComplete() {
            if (this.s != SubscriptionHelper.CANCELLED) {
                this.actual.onComplete();
            }
        }

        public void request(long n) {
            try {
                this.onRequest.accept(n);
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
            this.s.request(n);
        }

        public void cancel() {
            try {
                this.onCancel.run();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
            this.s.cancel();
        }
    }
}
