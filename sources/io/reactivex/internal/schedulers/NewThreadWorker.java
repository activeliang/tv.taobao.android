package io.reactivex.internal.schedulers;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.DisposableContainer;
import io.reactivex.internal.disposables.EmptyDisposable;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class NewThreadWorker extends Scheduler.Worker implements Disposable {
    volatile boolean disposed;
    private final ScheduledExecutorService executor;

    public NewThreadWorker(ThreadFactory threadFactory) {
        this.executor = SchedulerPoolFactory.create(threadFactory);
    }

    @NonNull
    public Disposable schedule(@NonNull Runnable run) {
        return schedule(run, 0, (TimeUnit) null);
    }

    @NonNull
    public Disposable schedule(@NonNull Runnable action, long delayTime, @NonNull TimeUnit unit) {
        if (this.disposed) {
            return EmptyDisposable.INSTANCE;
        }
        return scheduleActual(action, delayTime, unit, (DisposableContainer) null);
    }

    public Disposable scheduleDirect(Runnable run, long delayTime, TimeUnit unit) {
        Future<?> f;
        ScheduledDirectTask task = new ScheduledDirectTask(RxJavaPlugins.onSchedule(run));
        if (delayTime <= 0) {
            try {
                f = this.executor.submit(task);
            } catch (RejectedExecutionException ex) {
                RxJavaPlugins.onError(ex);
                return EmptyDisposable.INSTANCE;
            }
        } else {
            f = this.executor.schedule(task, delayTime, unit);
        }
        task.setFuture(f);
        return task;
    }

    public Disposable schedulePeriodicallyDirect(Runnable run, long initialDelay, long period, TimeUnit unit) {
        Future<?> f;
        Runnable decoratedRun = RxJavaPlugins.onSchedule(run);
        if (period <= 0) {
            InstantPeriodicTask periodicWrapper = new InstantPeriodicTask(decoratedRun, this.executor);
            if (initialDelay <= 0) {
                try {
                    f = this.executor.submit(periodicWrapper);
                } catch (RejectedExecutionException ex) {
                    RxJavaPlugins.onError(ex);
                    return EmptyDisposable.INSTANCE;
                }
            } else {
                f = this.executor.schedule(periodicWrapper, initialDelay, unit);
            }
            periodicWrapper.setFirst(f);
            return periodicWrapper;
        }
        ScheduledDirectPeriodicTask task = new ScheduledDirectPeriodicTask(decoratedRun);
        try {
            task.setFuture(this.executor.scheduleAtFixedRate(task, initialDelay, period, unit));
            return task;
        } catch (RejectedExecutionException ex2) {
            RxJavaPlugins.onError(ex2);
            return EmptyDisposable.INSTANCE;
        }
    }

    @NonNull
    public ScheduledRunnable scheduleActual(Runnable run, long delayTime, @NonNull TimeUnit unit, @Nullable DisposableContainer parent) {
        Future<?> f;
        ScheduledRunnable sr = new ScheduledRunnable(RxJavaPlugins.onSchedule(run), parent);
        if (parent == null || parent.add(sr)) {
            if (delayTime <= 0) {
                try {
                    f = this.executor.submit(sr);
                } catch (RejectedExecutionException ex) {
                    if (parent != null) {
                        parent.remove(sr);
                    }
                    RxJavaPlugins.onError(ex);
                }
            } else {
                f = this.executor.schedule(sr, delayTime, unit);
            }
            sr.setFuture(f);
        }
        return sr;
    }

    public void dispose() {
        if (!this.disposed) {
            this.disposed = true;
            this.executor.shutdownNow();
        }
    }

    public void shutdown() {
        if (!this.disposed) {
            this.disposed = true;
            this.executor.shutdown();
        }
    }

    public boolean isDisposed() {
        return this.disposed;
    }
}
