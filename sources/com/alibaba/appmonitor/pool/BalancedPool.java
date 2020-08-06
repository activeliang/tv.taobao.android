package com.alibaba.appmonitor.pool;

import android.app.ActivityManager;
import android.content.Context;
import android.support.v4.media.session.PlaybackStateCompat;
import com.alibaba.analytics.core.selfmonitor.exception.ExceptionEventBuilder;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.util.HashMap;
import java.util.Map;

public class BalancedPool implements IPool {
    private static BalancedPool instance = new BalancedPool();
    private Map<Class<? extends Reusable>, ReuseItemPool<? extends Reusable>> reuseItemPools = new HashMap();

    public static BalancedPool getInstance() {
        return instance;
    }

    private BalancedPool() {
    }

    public <T extends Reusable> T poll(Class<T> type, Object... params) {
        T item = getPool(type).poll();
        if (item == null) {
            try {
                item = (Reusable) type.newInstance();
            } catch (Exception e) {
                ExceptionEventBuilder.log(ExceptionEventBuilder.ExceptionType.AP, e);
            }
        }
        if (item != null) {
            item.fill(params);
        }
        return item;
    }

    public <T extends Reusable> void offer(T item) {
        if (item != null) {
            getPool(item.getClass()).offer(item);
        }
    }

    private synchronized <T extends Reusable> ReuseItemPool<T> getPool(Class<T> type) {
        ReuseItemPool<T> pool;
        pool = this.reuseItemPools.get(type);
        if (pool == null) {
            pool = new ReuseItemPool<>();
            this.reuseItemPools.put(type, pool);
        }
        return pool;
    }

    protected static long getMaxMemAllocatedSize(Context context) {
        long maxRunMemory = Runtime.getRuntime().maxMemory();
        long memClassInt = 0;
        ActivityManager am = (ActivityManager) context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY);
        if (am != null) {
            memClassInt = (long) (1048576 * am.getMemoryClass());
        }
        if (Math.min(maxRunMemory, memClassInt) < 67108864) {
            return PlaybackStateCompat.ACTION_PREPARE_FROM_URI;
        }
        return PlaybackStateCompat.ACTION_SET_REPEAT_MODE;
    }
}
