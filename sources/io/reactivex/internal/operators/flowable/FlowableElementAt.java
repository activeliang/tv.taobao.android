package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.NoSuchElementException;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowableElementAt<T> extends AbstractFlowableWithUpstream<T, T> {
    final T defaultValue;
    final boolean errorOnFewer;
    final long index;

    public FlowableElementAt(Flowable<T> source, long index2, T defaultValue2, boolean errorOnFewer2) {
        super(source);
        this.index = index2;
        this.defaultValue = defaultValue2;
        this.errorOnFewer = errorOnFewer2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> s) {
        this.source.subscribe(new ElementAtSubscriber(s, this.index, this.defaultValue, this.errorOnFewer));
    }

    static final class ElementAtSubscriber<T> extends DeferredScalarSubscription<T> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = 4066607327284737757L;
        long count;
        final T defaultValue;
        boolean done;
        final boolean errorOnFewer;
        final long index;
        Subscription s;

        ElementAtSubscriber(Subscriber<? super T> actual, long index2, T defaultValue2, boolean errorOnFewer2) {
            super(actual);
            this.index = index2;
            this.defaultValue = defaultValue2;
            this.errorOnFewer = errorOnFewer2;
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
                long c = this.count;
                if (c == this.index) {
                    this.done = true;
                    this.s.cancel();
                    complete(t);
                    return;
                }
                this.count = 1 + c;
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
                T v = this.defaultValue;
                if (v != null) {
                    complete(v);
                } else if (this.errorOnFewer) {
                    this.actual.onError(new NoSuchElementException());
                } else {
                    this.actual.onComplete();
                }
            }
        }

        public void cancel() {
            super.cancel();
            this.s.cancel();
        }
    }
}
