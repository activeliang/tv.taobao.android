package anet.channel.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class PriorityExecutor extends ThreadPoolExecutor {
    public PriorityExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, corePoolSize, 60, TimeUnit.SECONDS, new PriorityBlockingQueue(), threadFactory);
    }

    /* access modifiers changed from: protected */
    public <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new ComparableFutureTask(runnable, value);
    }

    /* access modifiers changed from: protected */
    public <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new ComparableFutureTask(callable);
    }

    class ComparableFutureTask<V> extends FutureTask<V> implements Comparable<ComparableFutureTask<V>> {
        private Object object;

        public ComparableFutureTask(Callable<V> callable) {
            super(callable);
            this.object = callable;
        }

        public ComparableFutureTask(Runnable runnable, V result) {
            super(runnable, result);
            this.object = runnable;
        }

        public int compareTo(ComparableFutureTask<V> o) {
            if (this == o) {
                return 0;
            }
            if (o == null) {
                return -1;
            }
            if (this.object == null || o.object == null || !this.object.getClass().equals(o.object.getClass()) || !(this.object instanceof Comparable)) {
                return 0;
            }
            return ((Comparable) this.object).compareTo(o.object);
        }
    }
}
