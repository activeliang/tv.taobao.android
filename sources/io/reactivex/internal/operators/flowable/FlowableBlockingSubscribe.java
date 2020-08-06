package io.reactivex.internal.operators.flowable;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.subscribers.LambdaSubscriber;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.BlockingIgnoringReceiver;
import io.reactivex.internal.util.ExceptionHelper;
import org.reactivestreams.Publisher;

public final class FlowableBlockingSubscribe {
    private FlowableBlockingSubscribe() {
        throw new IllegalStateException("No instances!");
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0013 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0014 A[Catch:{ InterruptedException -> 0x0038 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> void subscribe(org.reactivestreams.Publisher<? extends T> r5, org.reactivestreams.Subscriber<? super T> r6) {
        /*
            java.util.concurrent.LinkedBlockingQueue r2 = new java.util.concurrent.LinkedBlockingQueue
            r2.<init>()
            io.reactivex.internal.subscribers.BlockingSubscriber r0 = new io.reactivex.internal.subscribers.BlockingSubscriber
            r0.<init>(r2)
            r5.subscribe(r0)
        L_0x000d:
            boolean r4 = r0.isCancelled()     // Catch:{ InterruptedException -> 0x0038 }
            if (r4 == 0) goto L_0x0014
        L_0x0013:
            return
        L_0x0014:
            java.lang.Object r3 = r2.poll()     // Catch:{ InterruptedException -> 0x0038 }
            if (r3 != 0) goto L_0x0027
            boolean r4 = r0.isCancelled()     // Catch:{ InterruptedException -> 0x0038 }
            if (r4 != 0) goto L_0x0013
            io.reactivex.internal.util.BlockingHelper.verifyNonBlocking()     // Catch:{ InterruptedException -> 0x0038 }
            java.lang.Object r3 = r2.take()     // Catch:{ InterruptedException -> 0x0038 }
        L_0x0027:
            boolean r4 = r0.isCancelled()     // Catch:{ InterruptedException -> 0x0038 }
            if (r4 != 0) goto L_0x0013
            java.lang.Object r4 = io.reactivex.internal.subscribers.BlockingSubscriber.TERMINATED     // Catch:{ InterruptedException -> 0x0038 }
            if (r5 == r4) goto L_0x0013
            boolean r4 = io.reactivex.internal.util.NotificationLite.acceptFull((java.lang.Object) r3, r6)     // Catch:{ InterruptedException -> 0x0038 }
            if (r4 == 0) goto L_0x000d
            goto L_0x0013
        L_0x0038:
            r1 = move-exception
            r0.cancel()
            r6.onError(r1)
            goto L_0x0013
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.flowable.FlowableBlockingSubscribe.subscribe(org.reactivestreams.Publisher, org.reactivestreams.Subscriber):void");
    }

    public static <T> void subscribe(Publisher<? extends T> o) {
        BlockingIgnoringReceiver callback = new BlockingIgnoringReceiver();
        LambdaSubscriber<T> ls = new LambdaSubscriber<>(Functions.emptyConsumer(), callback, callback, Functions.REQUEST_MAX);
        o.subscribe(ls);
        BlockingHelper.awaitForComplete(callback, ls);
        Throwable e = callback.error;
        if (e != null) {
            throw ExceptionHelper.wrapOrThrow(e);
        }
    }

    public static <T> void subscribe(Publisher<? extends T> o, Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        subscribe(o, new LambdaSubscriber(onNext, onError, onComplete, Functions.REQUEST_MAX));
    }
}
