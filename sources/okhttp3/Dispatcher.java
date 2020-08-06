package okhttp3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import okhttp3.RealCall;
import okhttp3.internal.Util;

public final class Dispatcher {
    @Nullable
    private ExecutorService executorService;
    @Nullable
    private Runnable idleCallback;
    private int maxRequests = 64;
    private int maxRequestsPerHost = 5;
    private final Deque<RealCall.AsyncCall> readyAsyncCalls = new ArrayDeque();
    private final Deque<RealCall.AsyncCall> runningAsyncCalls = new ArrayDeque();
    private final Deque<RealCall> runningSyncCalls = new ArrayDeque();

    public Dispatcher(ExecutorService executorService2) {
        this.executorService = executorService2;
    }

    public Dispatcher() {
    }

    public synchronized ExecutorService executorService() {
        if (this.executorService == null) {
            this.executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp Dispatcher", false));
        }
        return this.executorService;
    }

    public synchronized void setMaxRequests(int maxRequests2) {
        if (maxRequests2 < 1) {
            throw new IllegalArgumentException("max < 1: " + maxRequests2);
        }
        this.maxRequests = maxRequests2;
        promoteCalls();
    }

    public synchronized int getMaxRequests() {
        return this.maxRequests;
    }

    public synchronized void setMaxRequestsPerHost(int maxRequestsPerHost2) {
        if (maxRequestsPerHost2 < 1) {
            throw new IllegalArgumentException("max < 1: " + maxRequestsPerHost2);
        }
        this.maxRequestsPerHost = maxRequestsPerHost2;
        promoteCalls();
    }

    public synchronized int getMaxRequestsPerHost() {
        return this.maxRequestsPerHost;
    }

    public synchronized void setIdleCallback(@Nullable Runnable idleCallback2) {
        this.idleCallback = idleCallback2;
    }

    /* access modifiers changed from: package-private */
    public synchronized void enqueue(RealCall.AsyncCall call) {
        if (this.runningAsyncCalls.size() >= this.maxRequests || runningCallsForHost(call) >= this.maxRequestsPerHost) {
            this.readyAsyncCalls.add(call);
        } else {
            this.runningAsyncCalls.add(call);
            executorService().execute(call);
        }
    }

    public synchronized void cancelAll() {
        for (RealCall.AsyncCall call : this.readyAsyncCalls) {
            call.get().cancel();
        }
        for (RealCall.AsyncCall call2 : this.runningAsyncCalls) {
            call2.get().cancel();
        }
        for (RealCall call3 : this.runningSyncCalls) {
            call3.cancel();
        }
    }

    private void promoteCalls() {
        if (this.runningAsyncCalls.size() < this.maxRequests && !this.readyAsyncCalls.isEmpty()) {
            Iterator<RealCall.AsyncCall> i = this.readyAsyncCalls.iterator();
            while (i.hasNext()) {
                RealCall.AsyncCall call = i.next();
                if (runningCallsForHost(call) < this.maxRequestsPerHost) {
                    i.remove();
                    this.runningAsyncCalls.add(call);
                    executorService().execute(call);
                }
                if (this.runningAsyncCalls.size() >= this.maxRequests) {
                    return;
                }
            }
        }
    }

    private int runningCallsForHost(RealCall.AsyncCall call) {
        int result = 0;
        for (RealCall.AsyncCall c : this.runningAsyncCalls) {
            if (!c.get().forWebSocket && c.host().equals(call.host())) {
                result++;
            }
        }
        return result;
    }

    /* access modifiers changed from: package-private */
    public synchronized void executed(RealCall call) {
        this.runningSyncCalls.add(call);
    }

    /* access modifiers changed from: package-private */
    public void finished(RealCall.AsyncCall call) {
        finished(this.runningAsyncCalls, call, true);
    }

    /* access modifiers changed from: package-private */
    public void finished(RealCall call) {
        finished(this.runningSyncCalls, call, false);
    }

    private <T> void finished(Deque<T> calls, T call, boolean promoteCalls) {
        int runningCallsCount;
        Runnable idleCallback2;
        synchronized (this) {
            if (!calls.remove(call)) {
                throw new AssertionError("Call wasn't in-flight!");
            }
            if (promoteCalls) {
                promoteCalls();
            }
            runningCallsCount = runningCallsCount();
            idleCallback2 = this.idleCallback;
        }
        if (runningCallsCount == 0 && idleCallback2 != null) {
            idleCallback2.run();
        }
    }

    public synchronized List<Call> queuedCalls() {
        List<Call> result;
        result = new ArrayList<>();
        for (RealCall.AsyncCall asyncCall : this.readyAsyncCalls) {
            result.add(asyncCall.get());
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized List<Call> runningCalls() {
        List<Call> result;
        result = new ArrayList<>();
        result.addAll(this.runningSyncCalls);
        for (RealCall.AsyncCall asyncCall : this.runningAsyncCalls) {
            result.add(asyncCall.get());
        }
        return Collections.unmodifiableList(result);
    }

    public synchronized int queuedCallsCount() {
        return this.readyAsyncCalls.size();
    }

    public synchronized int runningCallsCount() {
        return this.runningAsyncCalls.size() + this.runningSyncCalls.size();
    }
}
