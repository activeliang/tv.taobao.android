package com.alibaba.analytics.core.sync;

import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.UTRealtimeConfBiz;
import com.alibaba.analytics.core.model.Log;
import com.alibaba.analytics.core.network.NetworkUtil;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather;
import com.alibaba.analytics.core.sync.UploadLog;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadLogFromCache extends UploadLog {
    private static final int MAX_NUM = 300;
    private static UploadLogFromCache s_instance = new UploadLogFromCache();
    private volatile boolean bRunning = false;
    private boolean hasSuccess = false;
    private List<Log> mCacheLogs = new ArrayList();
    public final SelfMonitorEventDispather mMonitor = new SelfMonitorEventDispather();
    private int mTNetFailTimes = 0;
    private int mUploadByteSize = 0;
    private List<Log> mUploadingLogs = new ArrayList();

    public static UploadLogFromCache getInstance() {
        return s_instance;
    }

    public void addLog(Log log) {
        synchronized (this) {
            if (this.mCacheLogs.size() >= 300) {
                for (int i = 99; i >= 0; i--) {
                    this.mCacheLogs.remove(i);
                }
            }
            this.mCacheLogs.add(log);
        }
        UploadQueueMgr.getInstance().add(UploadQueueMgr.MSGTYPE_REALTIME);
    }

    private void removeUploadingLogs() {
        synchronized (this) {
            this.mCacheLogs.removeAll(this.mUploadingLogs);
            this.mUploadingLogs.clear();
        }
    }

    /* access modifiers changed from: package-private */
    public void upload() {
        Logger.d();
        try {
            if (!UTRealtimeConfBiz.getInstance().isRealtimeClosed()) {
                uploadEventLog();
            }
        } catch (Throwable e) {
            Logger.e((String) null, e, new Object[0]);
        }
    }

    private void uploadEventLog() {
        Logger.d();
        if (NetworkUtil.isConnectInternet(Variables.getInstance().getContext())) {
            if (UploadLog.NetworkStatus.ALL != this.mAllowedNetworkStatus && this.mAllowedNetworkStatus != getNetworkStatus()) {
                Logger.w("network not match,return", "current networkstatus", getNetworkStatus(), "mAllowedNetworkStatus", this.mAllowedNetworkStatus);
            } else if (!this.bRunning) {
                this.bRunning = true;
                int i = 0;
                while (true) {
                    try {
                        if (i < this.mMaxUploadTimes) {
                            if (this.mCacheLogs.size() != 0) {
                                if (uploadByTnet()) {
                                    break;
                                }
                                i++;
                            } else {
                                this.bRunning = false;
                                break;
                            }
                        } else {
                            break;
                        }
                    } catch (Throwable e) {
                        Logger.e((String) null, e, new Object[0]);
                        return;
                    } finally {
                        this.bRunning = false;
                    }
                }
            }
        }
    }

    private boolean uploadByTnet() throws Exception {
        Logger.d();
        Map<String, String> postDataMap = buildEventRequestMap();
        if (postDataMap == null || postDataMap.size() == 0) {
            this.bRunning = false;
            return true;
        }
        byte[] packRequest = null;
        try {
            packRequest = BizRequest.getPackRequestByRealtime(postDataMap);
        } catch (Exception e) {
            Logger.e((String) null, e, new Object[0]);
        }
        if (packRequest == null) {
            Logger.d("", "packRequest is null");
            return false;
        }
        long start = System.currentTimeMillis();
        BizResponse bizResponse = TnetUtil.sendRequest(packRequest);
        boolean isSendSuccess = bizResponse.isSuccess();
        if (isSendSuccess) {
            Variables.getInstance().turnOnSelfMonitor();
            this.hasSuccess = true;
            this.mTNetFailTimes = 0;
            removeUploadingLogs();
            try {
                parserConfig(bizResponse.data);
            } catch (Exception e2) {
                Logger.d((String) null, e2);
            }
        } else {
            this.mTNetFailTimes++;
            if (Variables.getInstance().isHttpService()) {
                return true;
            }
            if (Variables.getInstance().isSelfMonitorTurnOn() && this.hasSuccess && this.mTNetFailTimes <= 10) {
                Map<String, String> args = new HashMap<>();
                args.put("rt", String.valueOf(bizResponse.rt));
                args.put("pSize", String.valueOf(this.mUploadByteSize));
                args.put("errCode", String.valueOf(bizResponse.errCode));
                args.put("type", "2");
                this.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.UPLOAD_FAILED, JSON.toJSONString(args), Double.valueOf(1.0d)));
            }
        }
        if (Logger.isDebug()) {
            Logger.d("", "isSendSuccess", Boolean.valueOf(isSendSuccess), "cost time", Long.valueOf(System.currentTimeMillis() - start));
        }
        try {
            Thread.sleep(100);
        } catch (Throwable e3) {
            Logger.w((String) null, "thread sleep interrupted", e3);
        }
        return false;
    }

    private Map<String, String> buildEventRequestMap() {
        if (this.mCacheLogs.size() == 0) {
            return null;
        }
        int totalUploadSize = 0;
        HashMap<String, StringBuilder> temp = new HashMap<>();
        List<Log> timeoutLogs = new ArrayList<>();
        synchronized (this) {
            this.mUploadingLogs.clear();
            int effectiveTime = UTRealtimeConfBiz.getInstance().getEffectiveTime() * 1000;
            long time = System.currentTimeMillis();
            for (int i = 0; i < this.mCacheLogs.size(); i++) {
                Log log = this.mCacheLogs.get(i);
                if (time - Long.parseLong(log.time) > ((long) effectiveTime)) {
                    timeoutLogs.add(log);
                } else {
                    this.mUploadingLogs.add(log);
                    StringBuilder vBuilder = temp.get("" + log.getTopicId());
                    if (vBuilder == null) {
                        vBuilder = new StringBuilder();
                        temp.put("" + log.getTopicId(), vBuilder);
                    } else {
                        vBuilder.append(1);
                        totalUploadSize++;
                    }
                    String uploadContent = this.mCacheLogs.get(i).getContent();
                    vBuilder.append(uploadContent);
                    totalUploadSize += uploadContent.length();
                }
            }
            if (!timeoutLogs.isEmpty()) {
                if (Variables.getInstance().isSelfMonitorTurnOn()) {
                    this.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.LOGS_TIMEOUT, (String) null, Double.valueOf((double) timeoutLogs.size())));
                }
                this.mCacheLogs.removeAll(timeoutLogs);
            }
        }
        HashMap<String, String> ret = new HashMap<>();
        this.mUploadByteSize = totalUploadSize;
        for (String key : temp.keySet()) {
            ret.put(key, temp.get(key).toString());
        }
        if (!Logger.isDebug()) {
            return ret;
        }
        Logger.d("", "mUploadByteSize", Integer.valueOf(this.mUploadByteSize), "count", Integer.valueOf(this.mUploadingLogs.size()), "timeoutLogs count", Integer.valueOf(timeoutLogs.size()));
        return ret;
    }
}
