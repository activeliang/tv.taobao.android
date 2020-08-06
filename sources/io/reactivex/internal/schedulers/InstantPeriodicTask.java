package io.reactivex.internal.schedulers;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.Functions;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

final class InstantPeriodicTask implements Callable<Void>, Disposable {
    static final FutureTask<Void> CANCELLED = new FutureTask<>(Functions.EMPTY_RUNNABLE, (Object) null);
    final ExecutorService executor;
    final AtomicReference<Future<?>> first = new AtomicReference<>();
    final AtomicReference<Future<?>> rest = new AtomicReference<>();
    Thread runner;
    final Runnable task;

    InstantPeriodicTask(Runnable task2, ExecutorService executor2) {
        this.task = task2;
        this.executor = executor2;
    }

    public Void call() throws Exception {
        try {
            this.runner = Thread.currentThread();
            this.task.run();
            setRest(this.executor.submit(this));
        } catch (Throwable th) {
            this.runner = null;
            throw th;
        }
        this.runner = null;
        return null;
    }

    public void dispose() {
        boolean z = true;
        Future<?> current = this.first.getAndSet(CANCELLED);
        if (!(current == null || current == CANCELLED)) {
            current.cancel(this.runner != Thread.currentThread());
        }
        Future<?> current2 = this.rest.getAndSet(CANCELLED);
        if (current2 != null && current2 != CANCELLED) {
            if (this.runner == Thread.currentThread()) {
                z = false;
            }
            current2.cancel(z);
        }
    }

    public boolean isDisposed() {
        return this.first.get() == CANCELLED;
    }

    /* access modifiers changed from: package-private */
    public void setFirst(Future<?> f) {
        Future<?> current;
        do {
            current = this.first.get();
            if (current == CANCELLED) {
                f.cancel(this.runner != Thread.currentThread());
            }
        } while (!this.first.compareAndSet(current, f));
    }

    /* access modifiers changed from: package-private */
    public void setRest(Future<?> f) {
        Future<?> current;
        do {
            current = this.rest.get();
            if (current == CANCELLED) {
                f.cancel(this.runner != Thread.currentThread());
            }
        } while (!this.rest.compareAndSet(current, f));
    }
}
