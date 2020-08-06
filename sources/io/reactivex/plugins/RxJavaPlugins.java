package io.reactivex.plugins;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.Beta;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.exceptions.OnErrorNotImplementedException;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.schedulers.ComputationScheduler;
import io.reactivex.internal.schedulers.IoScheduler;
import io.reactivex.internal.schedulers.NewThreadScheduler;
import io.reactivex.internal.schedulers.SingleScheduler;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.parallel.ParallelFlowable;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadFactory;
import org.reactivestreams.Subscriber;

public final class RxJavaPlugins {
    @Nullable
    static volatile Consumer<? super Throwable> errorHandler;
    static volatile boolean failNonBlockingScheduler;
    static volatile boolean lockdown;
    @Nullable
    static volatile BooleanSupplier onBeforeBlocking;
    @Nullable
    static volatile Function<? super Completable, ? extends Completable> onCompletableAssembly;
    @Nullable
    static volatile BiFunction<? super Completable, ? super CompletableObserver, ? extends CompletableObserver> onCompletableSubscribe;
    @Nullable
    static volatile Function<? super Scheduler, ? extends Scheduler> onComputationHandler;
    @Nullable
    static volatile Function<? super ConnectableFlowable, ? extends ConnectableFlowable> onConnectableFlowableAssembly;
    @Nullable
    static volatile Function<? super ConnectableObservable, ? extends ConnectableObservable> onConnectableObservableAssembly;
    @Nullable
    static volatile Function<? super Flowable, ? extends Flowable> onFlowableAssembly;
    @Nullable
    static volatile BiFunction<? super Flowable, ? super Subscriber, ? extends Subscriber> onFlowableSubscribe;
    @Nullable
    static volatile Function<? super Callable<Scheduler>, ? extends Scheduler> onInitComputationHandler;
    @Nullable
    static volatile Function<? super Callable<Scheduler>, ? extends Scheduler> onInitIoHandler;
    @Nullable
    static volatile Function<? super Callable<Scheduler>, ? extends Scheduler> onInitNewThreadHandler;
    @Nullable
    static volatile Function<? super Callable<Scheduler>, ? extends Scheduler> onInitSingleHandler;
    @Nullable
    static volatile Function<? super Scheduler, ? extends Scheduler> onIoHandler;
    @Nullable
    static volatile Function<? super Maybe, ? extends Maybe> onMaybeAssembly;
    @Nullable
    static volatile BiFunction<? super Maybe, ? super MaybeObserver, ? extends MaybeObserver> onMaybeSubscribe;
    @Nullable
    static volatile Function<? super Scheduler, ? extends Scheduler> onNewThreadHandler;
    @Nullable
    static volatile Function<? super Observable, ? extends Observable> onObservableAssembly;
    @Nullable
    static volatile BiFunction<? super Observable, ? super Observer, ? extends Observer> onObservableSubscribe;
    @Nullable
    static volatile Function<? super ParallelFlowable, ? extends ParallelFlowable> onParallelAssembly;
    @Nullable
    static volatile Function<? super Runnable, ? extends Runnable> onScheduleHandler;
    @Nullable
    static volatile Function<? super Single, ? extends Single> onSingleAssembly;
    @Nullable
    static volatile Function<? super Scheduler, ? extends Scheduler> onSingleHandler;
    @Nullable
    static volatile BiFunction<? super Single, ? super SingleObserver, ? extends SingleObserver> onSingleSubscribe;

    public static void lockdown() {
        lockdown = true;
    }

    public static boolean isLockdown() {
        return lockdown;
    }

    public static void setFailOnNonBlockingScheduler(boolean enable) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        failNonBlockingScheduler = enable;
    }

    public static boolean isFailOnNonBlockingScheduler() {
        return failNonBlockingScheduler;
    }

    @Nullable
    public static Function<? super Scheduler, ? extends Scheduler> getComputationSchedulerHandler() {
        return onComputationHandler;
    }

    @Nullable
    public static Consumer<? super Throwable> getErrorHandler() {
        return errorHandler;
    }

    @Nullable
    public static Function<? super Callable<Scheduler>, ? extends Scheduler> getInitComputationSchedulerHandler() {
        return onInitComputationHandler;
    }

    @Nullable
    public static Function<? super Callable<Scheduler>, ? extends Scheduler> getInitIoSchedulerHandler() {
        return onInitIoHandler;
    }

    @Nullable
    public static Function<? super Callable<Scheduler>, ? extends Scheduler> getInitNewThreadSchedulerHandler() {
        return onInitNewThreadHandler;
    }

    @Nullable
    public static Function<? super Callable<Scheduler>, ? extends Scheduler> getInitSingleSchedulerHandler() {
        return onInitSingleHandler;
    }

    @Nullable
    public static Function<? super Scheduler, ? extends Scheduler> getIoSchedulerHandler() {
        return onIoHandler;
    }

    @Nullable
    public static Function<? super Scheduler, ? extends Scheduler> getNewThreadSchedulerHandler() {
        return onNewThreadHandler;
    }

    @Nullable
    public static Function<? super Runnable, ? extends Runnable> getScheduleHandler() {
        return onScheduleHandler;
    }

    @Nullable
    public static Function<? super Scheduler, ? extends Scheduler> getSingleSchedulerHandler() {
        return onSingleHandler;
    }

    @NonNull
    public static Scheduler initComputationScheduler(@NonNull Callable<Scheduler> defaultScheduler) {
        ObjectHelper.requireNonNull(defaultScheduler, "Scheduler Callable can't be null");
        Function<? super Callable<Scheduler>, ? extends Scheduler> f = onInitComputationHandler;
        if (f == null) {
            return callRequireNonNull(defaultScheduler);
        }
        return applyRequireNonNull(f, defaultScheduler);
    }

    @NonNull
    public static Scheduler initIoScheduler(@NonNull Callable<Scheduler> defaultScheduler) {
        ObjectHelper.requireNonNull(defaultScheduler, "Scheduler Callable can't be null");
        Function<? super Callable<Scheduler>, ? extends Scheduler> f = onInitIoHandler;
        if (f == null) {
            return callRequireNonNull(defaultScheduler);
        }
        return applyRequireNonNull(f, defaultScheduler);
    }

    @NonNull
    public static Scheduler initNewThreadScheduler(@NonNull Callable<Scheduler> defaultScheduler) {
        ObjectHelper.requireNonNull(defaultScheduler, "Scheduler Callable can't be null");
        Function<? super Callable<Scheduler>, ? extends Scheduler> f = onInitNewThreadHandler;
        if (f == null) {
            return callRequireNonNull(defaultScheduler);
        }
        return applyRequireNonNull(f, defaultScheduler);
    }

    @NonNull
    public static Scheduler initSingleScheduler(@NonNull Callable<Scheduler> defaultScheduler) {
        ObjectHelper.requireNonNull(defaultScheduler, "Scheduler Callable can't be null");
        Function<? super Callable<Scheduler>, ? extends Scheduler> f = onInitSingleHandler;
        if (f == null) {
            return callRequireNonNull(defaultScheduler);
        }
        return applyRequireNonNull(f, defaultScheduler);
    }

    @NonNull
    public static Scheduler onComputationScheduler(@NonNull Scheduler defaultScheduler) {
        Function<? super Scheduler, ? extends Scheduler> f = onComputationHandler;
        return f == null ? defaultScheduler : (Scheduler) apply(f, defaultScheduler);
    }

    public static void onError(@NonNull Throwable error) {
        Consumer<? super Throwable> f = errorHandler;
        if (error == null) {
            error = new NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.");
        } else if (!isBug(error)) {
            error = new UndeliverableException(error);
        }
        if (f != null) {
            try {
                f.accept(error);
                return;
            } catch (Throwable e) {
                e.printStackTrace();
                uncaught(e);
            }
        }
        error.printStackTrace();
        uncaught(error);
    }

    static boolean isBug(Throwable error) {
        if (!(error instanceof OnErrorNotImplementedException) && !(error instanceof MissingBackpressureException) && !(error instanceof IllegalStateException) && !(error instanceof NullPointerException) && !(error instanceof IllegalArgumentException) && !(error instanceof CompositeException)) {
            return false;
        }
        return true;
    }

    static void uncaught(@NonNull Throwable error) {
        Thread currentThread = Thread.currentThread();
        currentThread.getUncaughtExceptionHandler().uncaughtException(currentThread, error);
    }

    @NonNull
    public static Scheduler onIoScheduler(@NonNull Scheduler defaultScheduler) {
        Function<? super Scheduler, ? extends Scheduler> f = onIoHandler;
        return f == null ? defaultScheduler : (Scheduler) apply(f, defaultScheduler);
    }

    @NonNull
    public static Scheduler onNewThreadScheduler(@NonNull Scheduler defaultScheduler) {
        Function<? super Scheduler, ? extends Scheduler> f = onNewThreadHandler;
        return f == null ? defaultScheduler : (Scheduler) apply(f, defaultScheduler);
    }

    @NonNull
    public static Runnable onSchedule(@NonNull Runnable run) {
        ObjectHelper.requireNonNull(run, "run is null");
        Function<? super Runnable, ? extends Runnable> f = onScheduleHandler;
        return f == null ? run : (Runnable) apply(f, run);
    }

    @NonNull
    public static Scheduler onSingleScheduler(@NonNull Scheduler defaultScheduler) {
        Function<? super Scheduler, ? extends Scheduler> f = onSingleHandler;
        return f == null ? defaultScheduler : (Scheduler) apply(f, defaultScheduler);
    }

    public static void reset() {
        setErrorHandler((Consumer<? super Throwable>) null);
        setScheduleHandler((Function<? super Runnable, ? extends Runnable>) null);
        setComputationSchedulerHandler((Function<? super Scheduler, ? extends Scheduler>) null);
        setInitComputationSchedulerHandler((Function<? super Callable<Scheduler>, ? extends Scheduler>) null);
        setIoSchedulerHandler((Function<? super Scheduler, ? extends Scheduler>) null);
        setInitIoSchedulerHandler((Function<? super Callable<Scheduler>, ? extends Scheduler>) null);
        setSingleSchedulerHandler((Function<? super Scheduler, ? extends Scheduler>) null);
        setInitSingleSchedulerHandler((Function<? super Callable<Scheduler>, ? extends Scheduler>) null);
        setNewThreadSchedulerHandler((Function<? super Scheduler, ? extends Scheduler>) null);
        setInitNewThreadSchedulerHandler((Function<? super Callable<Scheduler>, ? extends Scheduler>) null);
        setOnFlowableAssembly((Function<? super Flowable, ? extends Flowable>) null);
        setOnFlowableSubscribe((BiFunction<? super Flowable, ? super Subscriber, ? extends Subscriber>) null);
        setOnObservableAssembly((Function<? super Observable, ? extends Observable>) null);
        setOnObservableSubscribe((BiFunction<? super Observable, ? super Observer, ? extends Observer>) null);
        setOnSingleAssembly((Function<? super Single, ? extends Single>) null);
        setOnSingleSubscribe((BiFunction<? super Single, ? super SingleObserver, ? extends SingleObserver>) null);
        setOnCompletableAssembly((Function<? super Completable, ? extends Completable>) null);
        setOnCompletableSubscribe((BiFunction<? super Completable, ? super CompletableObserver, ? extends CompletableObserver>) null);
        setOnConnectableFlowableAssembly((Function<? super ConnectableFlowable, ? extends ConnectableFlowable>) null);
        setOnConnectableObservableAssembly((Function<? super ConnectableObservable, ? extends ConnectableObservable>) null);
        setOnMaybeAssembly((Function<? super Maybe, ? extends Maybe>) null);
        setOnMaybeSubscribe((BiFunction<? super Maybe, MaybeObserver, ? extends MaybeObserver>) null);
        setOnParallelAssembly((Function<? super ParallelFlowable, ? extends ParallelFlowable>) null);
        setFailOnNonBlockingScheduler(false);
        setOnBeforeBlocking((BooleanSupplier) null);
    }

    public static void setComputationSchedulerHandler(@Nullable Function<? super Scheduler, ? extends Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onComputationHandler = handler;
    }

    public static void setErrorHandler(@Nullable Consumer<? super Throwable> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        errorHandler = handler;
    }

    public static void setInitComputationSchedulerHandler(@Nullable Function<? super Callable<Scheduler>, ? extends Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onInitComputationHandler = handler;
    }

    public static void setInitIoSchedulerHandler(@Nullable Function<? super Callable<Scheduler>, ? extends Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onInitIoHandler = handler;
    }

    public static void setInitNewThreadSchedulerHandler(@Nullable Function<? super Callable<Scheduler>, ? extends Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onInitNewThreadHandler = handler;
    }

    public static void setInitSingleSchedulerHandler(@Nullable Function<? super Callable<Scheduler>, ? extends Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onInitSingleHandler = handler;
    }

    public static void setIoSchedulerHandler(@Nullable Function<? super Scheduler, ? extends Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onIoHandler = handler;
    }

    public static void setNewThreadSchedulerHandler(@Nullable Function<? super Scheduler, ? extends Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onNewThreadHandler = handler;
    }

    public static void setScheduleHandler(@Nullable Function<? super Runnable, ? extends Runnable> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onScheduleHandler = handler;
    }

    public static void setSingleSchedulerHandler(@Nullable Function<? super Scheduler, ? extends Scheduler> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onSingleHandler = handler;
    }

    static void unlock() {
        lockdown = false;
    }

    @Nullable
    public static Function<? super Completable, ? extends Completable> getOnCompletableAssembly() {
        return onCompletableAssembly;
    }

    @Nullable
    public static BiFunction<? super Completable, ? super CompletableObserver, ? extends CompletableObserver> getOnCompletableSubscribe() {
        return onCompletableSubscribe;
    }

    @Nullable
    public static Function<? super Flowable, ? extends Flowable> getOnFlowableAssembly() {
        return onFlowableAssembly;
    }

    @Nullable
    public static Function<? super ConnectableFlowable, ? extends ConnectableFlowable> getOnConnectableFlowableAssembly() {
        return onConnectableFlowableAssembly;
    }

    @Nullable
    public static BiFunction<? super Flowable, ? super Subscriber, ? extends Subscriber> getOnFlowableSubscribe() {
        return onFlowableSubscribe;
    }

    @Nullable
    public static BiFunction<? super Maybe, ? super MaybeObserver, ? extends MaybeObserver> getOnMaybeSubscribe() {
        return onMaybeSubscribe;
    }

    @Nullable
    public static Function<? super Maybe, ? extends Maybe> getOnMaybeAssembly() {
        return onMaybeAssembly;
    }

    @Nullable
    public static Function<? super Single, ? extends Single> getOnSingleAssembly() {
        return onSingleAssembly;
    }

    @Nullable
    public static BiFunction<? super Single, ? super SingleObserver, ? extends SingleObserver> getOnSingleSubscribe() {
        return onSingleSubscribe;
    }

    @Nullable
    public static Function<? super Observable, ? extends Observable> getOnObservableAssembly() {
        return onObservableAssembly;
    }

    @Nullable
    public static Function<? super ConnectableObservable, ? extends ConnectableObservable> getOnConnectableObservableAssembly() {
        return onConnectableObservableAssembly;
    }

    @Nullable
    public static BiFunction<? super Observable, ? super Observer, ? extends Observer> getOnObservableSubscribe() {
        return onObservableSubscribe;
    }

    public static void setOnCompletableAssembly(@Nullable Function<? super Completable, ? extends Completable> onCompletableAssembly2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onCompletableAssembly = onCompletableAssembly2;
    }

    public static void setOnCompletableSubscribe(@Nullable BiFunction<? super Completable, ? super CompletableObserver, ? extends CompletableObserver> onCompletableSubscribe2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onCompletableSubscribe = onCompletableSubscribe2;
    }

    public static void setOnFlowableAssembly(@Nullable Function<? super Flowable, ? extends Flowable> onFlowableAssembly2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onFlowableAssembly = onFlowableAssembly2;
    }

    public static void setOnMaybeAssembly(@Nullable Function<? super Maybe, ? extends Maybe> onMaybeAssembly2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onMaybeAssembly = onMaybeAssembly2;
    }

    public static void setOnConnectableFlowableAssembly(@Nullable Function<? super ConnectableFlowable, ? extends ConnectableFlowable> onConnectableFlowableAssembly2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onConnectableFlowableAssembly = onConnectableFlowableAssembly2;
    }

    public static void setOnFlowableSubscribe(@Nullable BiFunction<? super Flowable, ? super Subscriber, ? extends Subscriber> onFlowableSubscribe2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onFlowableSubscribe = onFlowableSubscribe2;
    }

    public static void setOnMaybeSubscribe(@Nullable BiFunction<? super Maybe, MaybeObserver, ? extends MaybeObserver> onMaybeSubscribe2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onMaybeSubscribe = onMaybeSubscribe2;
    }

    public static void setOnObservableAssembly(@Nullable Function<? super Observable, ? extends Observable> onObservableAssembly2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onObservableAssembly = onObservableAssembly2;
    }

    public static void setOnConnectableObservableAssembly(@Nullable Function<? super ConnectableObservable, ? extends ConnectableObservable> onConnectableObservableAssembly2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onConnectableObservableAssembly = onConnectableObservableAssembly2;
    }

    public static void setOnObservableSubscribe(@Nullable BiFunction<? super Observable, ? super Observer, ? extends Observer> onObservableSubscribe2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onObservableSubscribe = onObservableSubscribe2;
    }

    public static void setOnSingleAssembly(@Nullable Function<? super Single, ? extends Single> onSingleAssembly2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onSingleAssembly = onSingleAssembly2;
    }

    public static void setOnSingleSubscribe(@Nullable BiFunction<? super Single, ? super SingleObserver, ? extends SingleObserver> onSingleSubscribe2) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onSingleSubscribe = onSingleSubscribe2;
    }

    @NonNull
    public static <T> Subscriber<? super T> onSubscribe(@NonNull Flowable<T> source, @NonNull Subscriber<? super T> subscriber) {
        BiFunction<? super Flowable, ? super Subscriber, ? extends Subscriber> f = onFlowableSubscribe;
        if (f != null) {
            return (Subscriber) apply(f, source, subscriber);
        }
        return subscriber;
    }

    @NonNull
    public static <T> Observer<? super T> onSubscribe(@NonNull Observable<T> source, @NonNull Observer<? super T> observer) {
        BiFunction<? super Observable, ? super Observer, ? extends Observer> f = onObservableSubscribe;
        if (f != null) {
            return (Observer) apply(f, source, observer);
        }
        return observer;
    }

    @NonNull
    public static <T> SingleObserver<? super T> onSubscribe(@NonNull Single<T> source, @NonNull SingleObserver<? super T> observer) {
        BiFunction<? super Single, ? super SingleObserver, ? extends SingleObserver> f = onSingleSubscribe;
        if (f != null) {
            return (SingleObserver) apply(f, source, observer);
        }
        return observer;
    }

    @NonNull
    public static CompletableObserver onSubscribe(@NonNull Completable source, @NonNull CompletableObserver observer) {
        BiFunction<? super Completable, ? super CompletableObserver, ? extends CompletableObserver> f = onCompletableSubscribe;
        if (f != null) {
            return (CompletableObserver) apply(f, source, observer);
        }
        return observer;
    }

    @NonNull
    public static <T> MaybeObserver<? super T> onSubscribe(@NonNull Maybe<T> source, @NonNull MaybeObserver<? super T> subscriber) {
        BiFunction<? super Maybe, ? super MaybeObserver, ? extends MaybeObserver> f = onMaybeSubscribe;
        if (f != null) {
            return (MaybeObserver) apply(f, source, subscriber);
        }
        return subscriber;
    }

    @NonNull
    public static <T> Maybe<T> onAssembly(@NonNull Maybe<T> source) {
        Function<? super Maybe, ? extends Maybe> f = onMaybeAssembly;
        if (f != null) {
            return (Maybe) apply(f, source);
        }
        return source;
    }

    @NonNull
    public static <T> Flowable<T> onAssembly(@NonNull Flowable<T> source) {
        Function<? super Flowable, ? extends Flowable> f = onFlowableAssembly;
        if (f != null) {
            return (Flowable) apply(f, source);
        }
        return source;
    }

    @NonNull
    public static <T> ConnectableFlowable<T> onAssembly(@NonNull ConnectableFlowable<T> source) {
        Function<? super ConnectableFlowable, ? extends ConnectableFlowable> f = onConnectableFlowableAssembly;
        if (f != null) {
            return (ConnectableFlowable) apply(f, source);
        }
        return source;
    }

    @NonNull
    public static <T> Observable<T> onAssembly(@NonNull Observable<T> source) {
        Function<? super Observable, ? extends Observable> f = onObservableAssembly;
        if (f != null) {
            return (Observable) apply(f, source);
        }
        return source;
    }

    @NonNull
    public static <T> ConnectableObservable<T> onAssembly(@NonNull ConnectableObservable<T> source) {
        Function<? super ConnectableObservable, ? extends ConnectableObservable> f = onConnectableObservableAssembly;
        if (f != null) {
            return (ConnectableObservable) apply(f, source);
        }
        return source;
    }

    @NonNull
    public static <T> Single<T> onAssembly(@NonNull Single<T> source) {
        Function<? super Single, ? extends Single> f = onSingleAssembly;
        if (f != null) {
            return (Single) apply(f, source);
        }
        return source;
    }

    @NonNull
    public static Completable onAssembly(@NonNull Completable source) {
        Function<? super Completable, ? extends Completable> f = onCompletableAssembly;
        if (f != null) {
            return (Completable) apply(f, source);
        }
        return source;
    }

    @Beta
    public static void setOnParallelAssembly(@Nullable Function<? super ParallelFlowable, ? extends ParallelFlowable> handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onParallelAssembly = handler;
    }

    @Beta
    @Nullable
    public static Function<? super ParallelFlowable, ? extends ParallelFlowable> getOnParallelAssembly() {
        return onParallelAssembly;
    }

    @NonNull
    @Beta
    public static <T> ParallelFlowable<T> onAssembly(@NonNull ParallelFlowable<T> source) {
        Function<? super ParallelFlowable, ? extends ParallelFlowable> f = onParallelAssembly;
        if (f != null) {
            return (ParallelFlowable) apply(f, source);
        }
        return source;
    }

    public static boolean onBeforeBlocking() {
        BooleanSupplier f = onBeforeBlocking;
        if (f == null) {
            return false;
        }
        try {
            return f.getAsBoolean();
        } catch (Throwable ex) {
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    public static void setOnBeforeBlocking(@Nullable BooleanSupplier handler) {
        if (lockdown) {
            throw new IllegalStateException("Plugins can't be changed anymore");
        }
        onBeforeBlocking = handler;
    }

    @Nullable
    public static BooleanSupplier getOnBeforeBlocking() {
        return onBeforeBlocking;
    }

    @NonNull
    public static Scheduler createComputationScheduler(@NonNull ThreadFactory threadFactory) {
        return new ComputationScheduler((ThreadFactory) ObjectHelper.requireNonNull(threadFactory, "threadFactory is null"));
    }

    @NonNull
    public static Scheduler createIoScheduler(@NonNull ThreadFactory threadFactory) {
        return new IoScheduler((ThreadFactory) ObjectHelper.requireNonNull(threadFactory, "threadFactory is null"));
    }

    @NonNull
    public static Scheduler createNewThreadScheduler(@NonNull ThreadFactory threadFactory) {
        return new NewThreadScheduler((ThreadFactory) ObjectHelper.requireNonNull(threadFactory, "threadFactory is null"));
    }

    @NonNull
    public static Scheduler createSingleScheduler(@NonNull ThreadFactory threadFactory) {
        return new SingleScheduler((ThreadFactory) ObjectHelper.requireNonNull(threadFactory, "threadFactory is null"));
    }

    @NonNull
    static <T, R> R apply(@NonNull Function<T, R> f, @NonNull T t) {
        try {
            return f.apply(t);
        } catch (Throwable ex) {
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    @NonNull
    static <T, U, R> R apply(@NonNull BiFunction<T, U, R> f, @NonNull T t, @NonNull U u) {
        try {
            return f.apply(t, u);
        } catch (Throwable ex) {
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    @NonNull
    static Scheduler callRequireNonNull(@NonNull Callable<Scheduler> s) {
        try {
            return (Scheduler) ObjectHelper.requireNonNull(s.call(), "Scheduler Callable result can't be null");
        } catch (Throwable ex) {
            throw ExceptionHelper.wrapOrThrow(ex);
        }
    }

    @NonNull
    static Scheduler applyRequireNonNull(@NonNull Function<? super Callable<Scheduler>, ? extends Scheduler> f, Callable<Scheduler> s) {
        return (Scheduler) ObjectHelper.requireNonNull(apply(f, s), "Scheduler Callable result can't be null");
    }

    private RxJavaPlugins() {
        throw new IllegalStateException("No instances!");
    }
}
