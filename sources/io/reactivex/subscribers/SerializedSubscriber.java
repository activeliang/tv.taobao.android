package io.reactivex.subscribers;

import io.reactivex.FlowableSubscriber;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.internal.util.NotificationLite;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class SerializedSubscriber<T> implements FlowableSubscriber<T>, Subscription {
    static final int QUEUE_LINK_SIZE = 4;
    final Subscriber<? super T> actual;
    final boolean delayError;
    volatile boolean done;
    boolean emitting;
    AppendOnlyLinkedArrayList<Object> queue;
    Subscription subscription;

    public SerializedSubscriber(Subscriber<? super T> actual2) {
        this(actual2, false);
    }

    public SerializedSubscriber(Subscriber<? super T> actual2, boolean delayError2) {
        this.actual = actual2;
        this.delayError = delayError2;
    }

    public void onSubscribe(Subscription s) {
        if (SubscriptionHelper.validate(this.subscription, s)) {
            this.subscription = s;
            this.actual.onSubscribe(this);
        }
    }

    public void onNext(T t) {
        if (!this.done) {
            if (t == null) {
                this.subscription.cancel();
                onError(new NullPointerException("onNext called with null. Null values are generally not allowed in 2.x operators and sources."));
                return;
            }
            synchronized (this) {
                if (!this.done) {
                    if (this.emitting) {
                        AppendOnlyLinkedArrayList<Object> q = this.queue;
                        if (q == null) {
                            q = new AppendOnlyLinkedArrayList<>(4);
                            this.queue = q;
                        }
                        q.add(NotificationLite.next(t));
                        return;
                    }
                    this.emitting = true;
                    this.actual.onNext(t);
                    emitLoop();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0011, code lost:
        io.reactivex.plugins.RxJavaPlugins.onError(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0044, code lost:
        r4.actual.onError(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x000f, code lost:
        if (r2 == false) goto L_0x0044;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onError(java.lang.Throwable r5) {
        /*
            r4 = this;
            boolean r3 = r4.done
            if (r3 == 0) goto L_0x0008
            io.reactivex.plugins.RxJavaPlugins.onError(r5)
        L_0x0007:
            return
        L_0x0008:
            monitor-enter(r4)
            boolean r3 = r4.done     // Catch:{ all -> 0x0035 }
            if (r3 == 0) goto L_0x0015
            r2 = 1
        L_0x000e:
            monitor-exit(r4)     // Catch:{ all -> 0x0035 }
            if (r2 == 0) goto L_0x0044
            io.reactivex.plugins.RxJavaPlugins.onError(r5)
            goto L_0x0007
        L_0x0015:
            boolean r3 = r4.emitting     // Catch:{ all -> 0x0035 }
            if (r3 == 0) goto L_0x003c
            r3 = 1
            r4.done = r3     // Catch:{ all -> 0x0035 }
            io.reactivex.internal.util.AppendOnlyLinkedArrayList<java.lang.Object> r1 = r4.queue     // Catch:{ all -> 0x0035 }
            if (r1 != 0) goto L_0x0028
            io.reactivex.internal.util.AppendOnlyLinkedArrayList r1 = new io.reactivex.internal.util.AppendOnlyLinkedArrayList     // Catch:{ all -> 0x0035 }
            r3 = 4
            r1.<init>(r3)     // Catch:{ all -> 0x0035 }
            r4.queue = r1     // Catch:{ all -> 0x0035 }
        L_0x0028:
            java.lang.Object r0 = io.reactivex.internal.util.NotificationLite.error(r5)     // Catch:{ all -> 0x0035 }
            boolean r3 = r4.delayError     // Catch:{ all -> 0x0035 }
            if (r3 == 0) goto L_0x0038
            r1.add(r0)     // Catch:{ all -> 0x0035 }
        L_0x0033:
            monitor-exit(r4)     // Catch:{ all -> 0x0035 }
            goto L_0x0007
        L_0x0035:
            r3 = move-exception
            monitor-exit(r4)     // Catch:{ all -> 0x0035 }
            throw r3
        L_0x0038:
            r1.setFirst(r0)     // Catch:{ all -> 0x0035 }
            goto L_0x0033
        L_0x003c:
            r3 = 1
            r4.done = r3     // Catch:{ all -> 0x0035 }
            r3 = 1
            r4.emitting = r3     // Catch:{ all -> 0x0035 }
            r2 = 0
            goto L_0x000e
        L_0x0044:
            org.reactivestreams.Subscriber<? super T> r3 = r4.actual
            r3.onError(r5)
            goto L_0x0007
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.subscribers.SerializedSubscriber.onError(java.lang.Throwable):void");
    }

    public void onComplete() {
        if (!this.done) {
            synchronized (this) {
                if (!this.done) {
                    if (this.emitting) {
                        AppendOnlyLinkedArrayList<Object> q = this.queue;
                        if (q == null) {
                            q = new AppendOnlyLinkedArrayList<>(4);
                            this.queue = q;
                        }
                        q.add(NotificationLite.complete());
                        return;
                    }
                    this.done = true;
                    this.emitting = true;
                    this.actual.onComplete();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void emitLoop() {
        AppendOnlyLinkedArrayList<Object> q;
        do {
            synchronized (this) {
                q = this.queue;
                if (q == null) {
                    this.emitting = false;
                    return;
                }
                this.queue = null;
            }
        } while (!q.accept((Subscriber<? super U>) this.actual));
    }

    public void request(long n) {
        this.subscription.request(n);
    }

    public void cancel() {
        this.subscription.cancel();
    }
}
