package anet.channel.strategy.dispatch;

import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.utils.AmdcThreadPoolExecutor;
import anet.channel.util.ALog;
import java.util.Map;
import java.util.Set;

class AmdcTaskExecutor {
    public static final String TAG = "awcn.AmdcThreadPoolExecutor";
    private static int cnt = 2;
    /* access modifiers changed from: private */
    public Map<String, Object> cachedParams;

    AmdcTaskExecutor() {
    }

    public void addTask(Map<String, Object> params) {
        params.put("Env", GlobalAppRuntimeInfo.getEnv());
        synchronized (this) {
            if (this.cachedParams == null) {
                this.cachedParams = params;
                int i = cnt;
                cnt = i - 1;
                int delayTime = i > 0 ? 500 : 3000;
                ALog.i(TAG, "merge amdc request", (String) null, "delay", Integer.valueOf(delayTime));
                AmdcThreadPoolExecutor.scheduleTask(new AmdcTask(), (long) delayTime);
            } else {
                Set<String> cachedHostSet = (Set) this.cachedParams.get(DispatchConstants.HOSTS);
                Set<String> currentHostSet = (Set) params.get(DispatchConstants.HOSTS);
                if (params.get("Env") != this.cachedParams.get("Env")) {
                    this.cachedParams = params;
                } else if (cachedHostSet.size() + currentHostSet.size() <= 40) {
                    currentHostSet.addAll(cachedHostSet);
                    this.cachedParams = params;
                } else {
                    AmdcThreadPoolExecutor.submitTask(new AmdcTask(params));
                }
            }
        }
    }

    private class AmdcTask implements Runnable {
        private Map<String, Object> params;

        AmdcTask(Map<String, Object> params2) {
            this.params = params2;
        }

        AmdcTask() {
        }

        public void run() {
            try {
                Map<String, Object> tmp = this.params;
                if (tmp == null) {
                    synchronized (AmdcTaskExecutor.class) {
                        tmp = AmdcTaskExecutor.this.cachedParams;
                        Map unused = AmdcTaskExecutor.this.cachedParams = null;
                    }
                }
                if (NetworkStatusHelper.isConnected()) {
                    if (GlobalAppRuntimeInfo.getEnv() != tmp.get("Env")) {
                        ALog.w(AmdcTaskExecutor.TAG, "task's env changed", (String) null, new Object[0]);
                    } else {
                        DispatchCore.sendRequest(DispatchParamBuilder.buildParamMap(tmp));
                    }
                }
            } catch (Exception e) {
                ALog.e(AmdcTaskExecutor.TAG, "exec amdc task failed.", (String) null, e, new Object[0]);
            }
        }
    }
}
