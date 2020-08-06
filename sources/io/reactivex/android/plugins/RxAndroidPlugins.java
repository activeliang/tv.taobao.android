package io.reactivex.android.plugins;

import io.reactivex.Scheduler;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Function;
import java.util.concurrent.Callable;

public final class RxAndroidPlugins {
    private static volatile Function<Callable<Scheduler>, Scheduler> onInitMainThreadHandler;
    private static volatile Function<Scheduler, Scheduler> onMainThreadHandler;

    public static void setInitMainThreadSchedulerHandler(Function<Callable<Scheduler>, Scheduler> handler) {
        onInitMainThreadHandler = handler;
    }

    public static Scheduler initMainThreadScheduler(Callable<Scheduler> scheduler) {
        if (scheduler == null) {
            throw new NullPointerException("scheduler == null");
        }
        Function<Callable<Scheduler>, Scheduler> f = onInitMainThreadHandler;
        if (f == null) {
            return callRequireNonNull(scheduler);
        }
        return applyRequireNonNull(f, scheduler);
    }

    public static void setMainThreadSchedulerHandler(Function<Scheduler, Scheduler> handler) {
        onMainThreadHandler = handler;
    }

    public static Scheduler onMainThreadScheduler(Scheduler scheduler) {
        if (scheduler == null) {
            throw new NullPointerException("scheduler == null");
        }
        Function function = onMainThreadHandler;
        return function == null ? scheduler : (Scheduler) apply(function, scheduler);
    }

    public static Function<Callable<Scheduler>, Scheduler> getInitMainThreadSchedulerHandler() {
        return onInitMainThreadHandler;
    }

    public static Function<Scheduler, Scheduler> getOnMainThreadSchedulerHandler() {
        return onMainThreadHandler;
    }

    public static void reset() {
        setInitMainThreadSchedulerHandler((Function<Callable<Scheduler>, Scheduler>) null);
        setMainThreadSchedulerHandler((Function<Scheduler, Scheduler>) null);
    }

    static Scheduler callRequireNonNull(Callable<Scheduler> s) {
        try {
            Scheduler scheduler = s.call();
            if (scheduler != null) {
                return scheduler;
            }
            throw new NullPointerException("Scheduler Callable returned null");
        } catch (Throwable ex) {
            throw Exceptions.propagate(ex);
        }
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [io.reactivex.functions.Function, io.reactivex.functions.Function<java.util.concurrent.Callable<io.reactivex.Scheduler>, io.reactivex.Scheduler>] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static io.reactivex.Scheduler applyRequireNonNull(io.reactivex.functions.Function<java.util.concurrent.Callable<io.reactivex.Scheduler>, io.reactivex.Scheduler> r3, java.util.concurrent.Callable<io.reactivex.Scheduler> r4) {
        /*
            java.lang.Object r0 = apply(r3, r4)
            io.reactivex.Scheduler r0 = (io.reactivex.Scheduler) r0
            if (r0 != 0) goto L_0x0011
            java.lang.NullPointerException r1 = new java.lang.NullPointerException
            java.lang.String r2 = "Scheduler Callable returned null"
            r1.<init>(r2)
            throw r1
        L_0x0011:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.android.plugins.RxAndroidPlugins.applyRequireNonNull(io.reactivex.functions.Function, java.util.concurrent.Callable):io.reactivex.Scheduler");
    }

    static <T, R> R apply(Function<T, R> f, T t) {
        try {
            return f.apply(t);
        } catch (Throwable ex) {
            throw Exceptions.propagate(ex);
        }
    }

    private RxAndroidPlugins() {
        throw new AssertionError("No instances.");
    }
}
