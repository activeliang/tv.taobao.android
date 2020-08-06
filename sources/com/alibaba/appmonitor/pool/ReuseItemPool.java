package com.alibaba.appmonitor.pool;

import com.alibaba.appmonitor.pool.Reusable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class ReuseItemPool<T extends Reusable> {
    private static long poolSize;
    private static AtomicLong totalInvokeTimes = new AtomicLong(0);
    private static AtomicLong totalPollSuccessTimes = new AtomicLong(0);
    private final int MAX_ITEM_COUNT = 20;
    private Set<Integer> hashCodes = new HashSet();
    private AtomicLong invokeTimes = new AtomicLong(0);
    private ConcurrentLinkedQueue<T> items = new ConcurrentLinkedQueue<>();
    private Integer objectSize = null;
    private AtomicLong pollSuccessTimes = new AtomicLong(0);

    public static void reset(long poolSize2) {
        poolSize = poolSize2;
        totalInvokeTimes = new AtomicLong(0);
    }

    public T poll() {
        totalInvokeTimes.getAndIncrement();
        this.invokeTimes.getAndIncrement();
        T item = (Reusable) this.items.poll();
        if (item != null) {
            this.hashCodes.remove(Integer.valueOf(System.identityHashCode(item)));
            this.pollSuccessTimes.getAndIncrement();
            totalPollSuccessTimes.getAndIncrement();
        }
        return item;
    }

    public void offer(T t) {
        t.clean();
        if (this.items.size() < 20) {
            synchronized (this.hashCodes) {
                int hoseCode = System.identityHashCode(t);
                if (!this.hashCodes.contains(Integer.valueOf(hoseCode))) {
                    this.hashCodes.add(Integer.valueOf(hoseCode));
                    this.items.offer(t);
                }
            }
        }
    }
}
