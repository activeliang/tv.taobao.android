package anet.channel.thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class DispatcherTask implements Runnable, Comparable<DispatcherTask>, Future {
    long createTime = System.currentTimeMillis();
    volatile Future<?> future = null;
    volatile boolean isCancelled = false;
    int priority = 0;
    Runnable rawTask = null;

    public DispatcherTask(Runnable rawTask2, int priority2) {
        this.rawTask = rawTask2;
        this.priority = priority2 < 0 ? 0 : priority2;
        this.createTime = System.currentTimeMillis();
    }

    public int compareTo(DispatcherTask o) {
        if (this.priority != o.priority) {
            return this.priority - o.priority;
        }
        return (int) (o.createTime - this.createTime);
    }

    public void run() {
        try {
            if (!this.isCancelled) {
                if (this.priority <= 6) {
                    this.future = WorkerTheadPoolExecutors.getHighPriorityExecutor().submit(this.rawTask);
                } else {
                    this.future = WorkerTheadPoolExecutors.getLowPriorityExecutor().submit(this.rawTask);
                }
            }
        } catch (RejectedExecutionException e) {
            this.priority++;
            ThreadPoolExecutorFactory.submitScheduledTask(this, (long) ((this.priority + 1) * 500), TimeUnit.MILLISECONDS);
        }
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        this.isCancelled = true;
        if (this.future != null) {
            return this.future.cancel(mayInterruptIfRunning);
        }
        return true;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public boolean isDone() {
        return false;
    }

    public Object get() throws InterruptedException, ExecutionException {
        throw new RuntimeException("NOT SUPPORT!");
    }

    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new RuntimeException("NOT SUPPORT!");
    }
}
