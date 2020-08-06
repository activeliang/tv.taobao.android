package anetwork.channel.unified;

import anet.channel.appmonitor.AppMonitor;
import anet.channel.bytes.ByteArray;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.cache.Cache;
import com.uc.webview.export.internal.interfaces.IWaStat;

public class CacheTask implements IUnifiedTask {
    private static final String TAG = "anet.CacheTask";
    private Cache cache = null;
    private volatile boolean isCanceled = false;
    private RequestContext rc = null;

    public CacheTask(RequestContext rc2, Cache cache2) {
        this.rc = rc2;
        this.cache = cache2;
    }

    public void cancel() {
        this.isCanceled = true;
        this.rc.config.getStatistic().ret = 2;
    }

    public void run() {
        int i;
        if (!this.isCanceled) {
            RequestStatistic rs = this.rc.config.getStatistic();
            rs.sendBeforeTime = System.currentTimeMillis() - rs.start;
            if (this.cache != null) {
                String cacheKey = this.rc.config.getUrlString();
                long cacheStart = System.currentTimeMillis();
                Cache.Entry entry = this.cache.get(cacheKey);
                long cacheEnd = System.currentTimeMillis();
                rs.cacheTime = cacheEnd - cacheStart;
                if (ALog.isPrintLog(2)) {
                    String str = this.rc.seqNum;
                    Object[] objArr = new Object[8];
                    objArr[0] = "hit";
                    objArr[1] = Boolean.valueOf(entry != null);
                    objArr[2] = IWaStat.KEY_COST;
                    objArr[3] = Long.valueOf(rs.cacheTime);
                    objArr[4] = "length";
                    if (entry != null) {
                        i = entry.data.length;
                    } else {
                        i = 0;
                    }
                    objArr[5] = Integer.valueOf(i);
                    objArr[6] = "key";
                    objArr[7] = cacheKey;
                    ALog.i(TAG, "read cache", str, objArr);
                }
                if (entry == null || !entry.isFresh()) {
                    if (!this.isCanceled) {
                        NetworkTask task = new NetworkTask(this.rc, this.cache, entry);
                        this.rc.runningTask = task;
                        task.run();
                    }
                } else if (this.rc.isDone.compareAndSet(false, true)) {
                    this.rc.cancelTimeoutTask();
                    rs.ret = 1;
                    rs.statusCode = 200;
                    rs.protocolType = "cache";
                    rs.oneWayTime = cacheEnd - rs.start;
                    this.rc.statisticData.filledBy(rs);
                    if (ALog.isPrintLog(2)) {
                        ALog.i(TAG, "hit fresh cache", this.rc.seqNum, new Object[0]);
                        ALog.i(TAG, this.rc.statisticData.toString(), this.rc.seqNum, new Object[0]);
                    }
                    this.rc.callback.onResponseCode(200, entry.responseHeaders);
                    this.rc.callback.onDataReceiveSize(1, entry.data.length, ByteArray.wrap(entry.data));
                    this.rc.callback.onFinish(new DefaultFinishEvent(200, (String) null, this.rc.statisticData));
                    AppMonitor.getInstance().commitStat(rs);
                }
            }
        }
    }
}
