package io.reactivex.internal.operators.observable;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.observers.LambdaObserver;
import io.reactivex.internal.util.BlockingHelper;
import io.reactivex.internal.util.BlockingIgnoringReceiver;
import io.reactivex.internal.util.ExceptionHelper;

public final class ObservableBlockingSubscribe {
    private ObservableBlockingSubscribe() {
        throw new IllegalStateException("No instances!");
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x0017  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T> void subscribe(io.reactivex.ObservableSource<? extends T> r5, io.reactivex.Observer<? super T> r6) {
        /*
            java.util.concurrent.LinkedBlockingQueue r2 = new java.util.concurrent.LinkedBlockingQueue
            r2.<init>()
            io.reactivex.internal.observers.BlockingObserver r0 = new io.reactivex.internal.observers.BlockingObserver
            r0.<init>(r2)
            r6.onSubscribe(r0)
            r5.subscribe(r0)
        L_0x0010:
            boolean r4 = r0.isDisposed()
            if (r4 == 0) goto L_0x0017
        L_0x0016:
            return
        L_0x0017:
            java.lang.Object r3 = r2.poll()
            if (r3 != 0) goto L_0x0021
            java.lang.Object r3 = r2.take()     // Catch:{ InterruptedException -> 0x0032 }
        L_0x0021:
            boolean r4 = r0.isDisposed()
            if (r4 != 0) goto L_0x0016
            java.lang.Object r4 = io.reactivex.internal.observers.BlockingObserver.TERMINATED
            if (r5 == r4) goto L_0x0016
            boolean r4 = io.reactivex.internal.util.NotificationLite.acceptFull((java.lang.Object) r3, r6)
            if (r4 == 0) goto L_0x0010
            goto L_0x0016
        L_0x0032:
            r1 = move-exception
            r0.dispose()
            r6.onError(r1)
            goto L_0x0016
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.operators.observable.ObservableBlockingSubscribe.subscribe(io.reactivex.ObservableSource, io.reactivex.Observer):void");
    }

    public static <T> void subscribe(ObservableSource<? extends T> o) {
        BlockingIgnoringReceiver callback = new BlockingIgnoringReceiver();
        LambdaObserver<T> ls = new LambdaObserver<>(Functions.emptyConsumer(), callback, callback, Functions.emptyConsumer());
        o.subscribe(ls);
        BlockingHelper.awaitForComplete(callback, ls);
        Throwable e = callback.error;
        if (e != null) {
            throw ExceptionHelper.wrapOrThrow(e);
        }
    }

    public static <T> void subscribe(ObservableSource<? extends T> o, Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {
        ObjectHelper.requireNonNull(onNext, "onNext is null");
        ObjectHelper.requireNonNull(onError, "onError is null");
        ObjectHelper.requireNonNull(onComplete, "onComplete is null");
        subscribe(o, new LambdaObserver(onNext, onError, onComplete, Functions.emptyConsumer()));
    }
}
