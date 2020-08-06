package anetwork.channel.unified;

import anetwork.channel.Response;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class FutureResponse implements Future<Response> {
    private boolean isCanceled;
    private UnifiedRequestTask task;

    public FutureResponse(UnifiedRequestTask task2) {
        this.task = task2;
    }

    public boolean cancel(boolean b) {
        if (!this.isCanceled) {
            this.task.cancelTask();
            this.isCanceled = true;
        }
        return true;
    }

    public boolean isCancelled() {
        return this.isCanceled;
    }

    public boolean isDone() {
        throw new RuntimeException("NOT SUPPORT!");
    }

    public Response get() throws InterruptedException, ExecutionException {
        throw new RuntimeException("NOT SUPPORT!");
    }

    public Response get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new RuntimeException("NOT SUPPORT!");
    }
}
