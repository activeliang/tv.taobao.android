package anetwork.channel.unified;

import anet.channel.appmonitor.AppMonitor;
import anet.channel.request.Request;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.statist.RequestStatistic;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.cache.Cache;
import anetwork.channel.cache.CacheManager;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.entity.Repeater;
import anetwork.channel.entity.RequestConfig;
import anetwork.channel.interceptor.Callback;
import anetwork.channel.interceptor.Interceptor;
import anetwork.channel.interceptor.InterceptorManager;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import mtopsdk.common.util.HttpHeaderConstant;

class UnifiedRequestTask {
    private static final String TAG = "anet.UnifiedRequestTask";
    /* access modifiers changed from: private */
    public RequestContext rc;

    public UnifiedRequestTask(RequestConfig config, Repeater repeater) {
        repeater.setSeqNo(config.getSeqNo());
        this.rc = new RequestContext(config, repeater);
        config.getStatistic().start = System.currentTimeMillis();
    }

    class UnifiedRequestChain implements Interceptor.Chain {
        private Callback callback = null;
        private int index = 0;
        private Request request = null;

        UnifiedRequestChain(int index2, Request request2, Callback callback2) {
            this.index = index2;
            this.request = request2;
            this.callback = callback2;
        }

        public Request request() {
            return this.request;
        }

        public Callback callback() {
            return this.callback;
        }

        public Future proceed(Request request2, Callback callback2) {
            if (UnifiedRequestTask.this.rc.isDone.get()) {
                ALog.i(UnifiedRequestTask.TAG, "request canneled or timeout in processing interceptor", request2.getSeq(), new Object[0]);
                return null;
            } else if (this.index < InterceptorManager.getSize()) {
                return InterceptorManager.getInterceptor(this.index).intercept(new UnifiedRequestChain(this.index + 1, request2, callback2));
            } else {
                UnifiedRequestTask.this.rc.config.setAwcnRequest(request2);
                UnifiedRequestTask.this.rc.callback = callback2;
                Cache cache = null;
                if (NetworkConfigCenter.isHttpCacheEnable() && !HttpHeaderConstant.NO_CACHE.equals(request2.getHeaders().get("Cache-Control"))) {
                    cache = CacheManager.getCache(UnifiedRequestTask.this.rc.config.getUrlString(), UnifiedRequestTask.this.rc.config.getHeaders());
                }
                UnifiedRequestTask.this.rc.runningTask = cache != null ? new CacheTask(UnifiedRequestTask.this.rc, cache) : new NetworkTask(UnifiedRequestTask.this.rc, (Cache) null, (Cache.Entry) null);
                ThreadPoolExecutorFactory.submitPriorityTask(UnifiedRequestTask.this.rc.runningTask, 0);
                UnifiedRequestTask.this.commitTimeoutTask();
                return null;
            }
        }
    }

    public Future request() {
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "request", this.rc.seqNum, "Url", this.rc.config.getUrlString());
        }
        ThreadPoolExecutorFactory.submitPriorityTask(new Runnable() {
            public void run() {
                new UnifiedRequestChain(0, UnifiedRequestTask.this.rc.config.getAwcnRequest(), UnifiedRequestTask.this.rc.callback).proceed(UnifiedRequestTask.this.rc.config.getAwcnRequest(), UnifiedRequestTask.this.rc.callback);
            }
        }, 0);
        return new FutureResponse(this);
    }

    /* access modifiers changed from: private */
    public void commitTimeoutTask() {
        this.rc.timeoutTask = ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
            public void run() {
                if (UnifiedRequestTask.this.rc.isDone.compareAndSet(false, true)) {
                    ALog.e(UnifiedRequestTask.TAG, "task time out", UnifiedRequestTask.this.rc.seqNum, new Object[0]);
                    UnifiedRequestTask.this.rc.cancelRunningTask();
                    UnifiedRequestTask.this.rc.statisticData.resultCode = ErrorConstant.ERROR_REQUEST_TIME_OUT;
                    UnifiedRequestTask.this.rc.callback.onFinish(new DefaultFinishEvent(ErrorConstant.ERROR_REQUEST_TIME_OUT, (String) null, UnifiedRequestTask.this.rc.statisticData));
                    RequestStatistic rs = UnifiedRequestTask.this.rc.config.getStatistic();
                    rs.ret = 0;
                    rs.statusCode = ErrorConstant.ERROR_REQUEST_TIME_OUT;
                    rs.msg = ErrorConstant.getErrMsg(ErrorConstant.ERROR_REQUEST_TIME_OUT);
                    rs.oneWayTime = System.currentTimeMillis() - rs.start;
                    AppMonitor.getInstance().commitStat(rs);
                    AppMonitor.getInstance().commitStat(new ExceptionStatistic(ErrorConstant.ERROR_REQUEST_TIME_OUT, (String) null, rs, (Throwable) null));
                }
            }
        }, (long) this.rc.config.getWaitTimeout(), TimeUnit.MILLISECONDS);
    }

    /* access modifiers changed from: package-private */
    public void cancelTask() {
        if (this.rc.isDone.compareAndSet(false, true)) {
            if (ALog.isPrintLog(2)) {
                ALog.i(TAG, "task cancelled", this.rc.seqNum, new Object[0]);
            }
            this.rc.cancelRunningTask();
            this.rc.cancelTimeoutTask();
            this.rc.statisticData.resultCode = ErrorConstant.ERROR_REQUEST_CANCEL;
            this.rc.callback.onFinish(new DefaultFinishEvent(ErrorConstant.ERROR_REQUEST_CANCEL, ErrorConstant.getErrMsg(ErrorConstant.ERROR_REQUEST_CANCEL), this.rc.statisticData));
            RequestStatistic rs = this.rc.config.getStatistic();
            rs.ret = 2;
            rs.statusCode = ErrorConstant.ERROR_REQUEST_CANCEL;
            rs.msg = ErrorConstant.getErrMsg(ErrorConstant.ERROR_REQUEST_CANCEL);
            rs.oneWayTime = System.currentTimeMillis() - rs.start;
            AppMonitor.getInstance().commitStat(rs);
            AppMonitor.getInstance().commitStat(new ExceptionStatistic(ErrorConstant.ERROR_REQUEST_CANCEL, (String) null, rs, (Throwable) null));
        }
    }
}
