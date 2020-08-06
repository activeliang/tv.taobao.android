package anetwork.channel.unified;

import anetwork.channel.entity.RequestConfig;
import anetwork.channel.interceptor.Callback;
import anetwork.channel.statist.StatisticData;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

class RequestContext {
    public Callback callback;
    public final RequestConfig config;
    public volatile AtomicBoolean isDone = new AtomicBoolean();
    public volatile IUnifiedTask runningTask = null;
    public final String seqNum;
    public volatile StatisticData statisticData = new StatisticData();
    public volatile Future timeoutTask = null;

    public RequestContext(RequestConfig config2, Callback callback2) {
        this.config = config2;
        this.seqNum = config2.getSeqNo();
        this.callback = callback2;
        this.statisticData.host = config2.getHttpUrl().host();
    }

    public void cancelTimeoutTask() {
        Future future = this.timeoutTask;
        if (future != null) {
            future.cancel(true);
            this.timeoutTask = null;
        }
    }

    public void cancelRunningTask() {
        if (this.runningTask != null) {
            this.runningTask.cancel();
            this.runningTask = null;
        }
    }
}
