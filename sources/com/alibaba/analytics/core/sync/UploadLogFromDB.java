package com.alibaba.analytics.core.sync;

import android.os.SystemClock;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.core.logbuilder.LogAssemble;
import com.alibaba.analytics.core.model.Log;
import com.alibaba.analytics.core.network.NetworkUtil;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEventDispather;
import com.alibaba.analytics.core.store.LogStoreMgr;
import com.alibaba.analytics.core.sync.UploadLog;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.MutiProcessLock;
import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadLogFromDB extends UploadLog {
    private static final int Default_WIN_SIZE = 4;
    private static final int MAX_LOG_COUNT = 350;
    private static final String TAG = "UploadLogFromDB";
    private static final int TOTAL_MAX_POST_SIZE = 5242880;
    private static UploadLogFromDB s_instance = new UploadLogFromDB();
    private volatile boolean bRunning = false;
    private boolean hasSuccess = false;
    private float mAveragePackageSize = 200.0f;
    private int mFactor = 0;
    public final SelfMonitorEventDispather mMonitor = new SelfMonitorEventDispather();
    private int mTNetFailTimes = 0;
    private int mUploadByteSize = 0;
    private long mUploadCount = 0;
    private int mUploadIndex = this.mMaxUploadTimes;
    private int mWinSize = -1;

    public static UploadLogFromDB getInstance() {
        return s_instance;
    }

    public boolean hasSuccess() {
        return this.hasSuccess;
    }

    public void upload() {
        Logger.d();
        try {
            if (!Variables.getInstance().isAllServiceClosed()) {
                uploadEventLog();
            } else {
                Logger.w((String) null, "isAllServiceClosed");
            }
        } catch (Throwable e) {
            Logger.e((String) null, e, new Object[0]);
        }
        try {
            if (this.mIUploadExcuted != null) {
                this.mIUploadExcuted.onUploadExcuted(this.mUploadCount);
            }
        } catch (Throwable throwable) {
            Logger.e((String) null, throwable, new Object[0]);
        }
    }

    private void uploadEventLog() {
        Logger.d();
        if (NetworkUtil.isConnectInternet(Variables.getInstance().getContext())) {
            if (UploadLog.NetworkStatus.ALL != this.mAllowedNetworkStatus && this.mAllowedNetworkStatus != getNetworkStatus()) {
                Logger.w("network not match,return", "current networkstatus", getNetworkStatus(), "mAllowedNetworkStatus", this.mAllowedNetworkStatus);
            } else if (!this.bRunning) {
                this.bRunning = true;
                try {
                    this.mUploadCount = 0;
                    if (!MutiProcessLock.lock(Variables.getInstance().getContext())) {
                        Logger.d(TAG, "Other Process is Uploading, break");
                        return;
                    }
                    List<Log> logs = LogStoreMgr.getInstance().get(getLogCount());
                    if (logs == null || logs.size() == 0) {
                        Logger.d("", "logs is null");
                        this.bRunning = false;
                        this.bRunning = false;
                        MutiProcessLock.release();
                        return;
                    }
                    if (uploadLogs(logs)) {
                        this.mUploadIndex = this.mMaxUploadTimes;
                    } else {
                        this.mUploadIndex--;
                        if (this.mUploadIndex > 0) {
                            UploadQueueMgr.getInstance().add(UploadQueueMgr.MSGTYPE_INTERVAL);
                        } else {
                            this.mUploadIndex = this.mMaxUploadTimes;
                        }
                    }
                    this.bRunning = false;
                    MutiProcessLock.release();
                } catch (Throwable e) {
                    Logger.e((String) null, e.toString());
                } finally {
                    this.bRunning = false;
                    MutiProcessLock.release();
                }
            }
        }
    }

    private boolean uploadLogs(List<Log> logs) throws Exception {
        BizResponse bizResponse;
        Logger.d();
        Map<String, String> postDataMap = buildEventRequestMap(logs);
        if (postDataMap == null || postDataMap.size() == 0) {
            Logger.d("", "postDataMap is null");
            this.bRunning = false;
            return true;
        }
        byte[] packRequest = null;
        try {
            packRequest = BizRequest.getPackRequest(postDataMap);
        } catch (Exception e) {
            Logger.e((String) null, e.toString());
        }
        if (packRequest == null) {
            reduceWindowSize();
            return false;
        }
        long start = SystemClock.elapsedRealtime();
        if (Variables.getInstance().isHttpService()) {
            bizResponse = UrlWrapper.sendRequest(packRequest);
        } else {
            bizResponse = TnetUtil.sendRequest(packRequest);
        }
        boolean isSendSuccess = bizResponse.isSuccess();
        long uploadEnd = SystemClock.elapsedRealtime();
        calPackPackageWindowSize(Boolean.valueOf(isSendSuccess), uploadEnd - start);
        if (isSendSuccess) {
            Variables.getInstance().turnOnSelfMonitor();
            this.hasSuccess = true;
            this.mTNetFailTimes = 0;
            this.mUploadCount += (long) LogStoreMgr.getInstance().delete(logs);
            this.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.UPLOAD_TRAFFIC, (String) null, Double.valueOf((double) this.mUploadByteSize)));
            try {
                parserConfig(bizResponse.data);
            } catch (Exception e2) {
            }
        } else {
            this.mTNetFailTimes++;
            if (this.mTNetFailTimes > 10) {
                Variables.getInstance().setHttpService(true);
                Logger.d("", "setHttpService");
                return true;
            } else if (Variables.getInstance().isSelfMonitorTurnOn()) {
                if (!this.hasSuccess || this.mTNetFailTimes > 10) {
                    Variables.getInstance().turnOffSelfMonitor();
                } else {
                    Map<String, String> args = new HashMap<>();
                    args.put("rt", String.valueOf(bizResponse.rt));
                    args.put("pSize", String.valueOf(this.mUploadByteSize));
                    args.put("errCode", String.valueOf(bizResponse.errCode));
                    args.put("type", "1");
                    this.mMonitor.onEvent(SelfMonitorEvent.buildCountEvent(SelfMonitorEvent.UPLOAD_FAILED, JSON.toJSONString(args), Double.valueOf(1.0d)));
                }
            }
        }
        Logger.i("", "isSendSuccess", Boolean.valueOf(isSendSuccess), "upload log count", Integer.valueOf(logs.size()), "upload consume", Long.valueOf(uploadEnd - start), "delete consume", Long.valueOf(SystemClock.elapsedRealtime() - uploadEnd));
        try {
            Thread.sleep(100);
        } catch (Throwable e3) {
            Logger.w((String) null, e3, new Object[0]);
        }
        return false;
    }

    public Map<String, String> buildEventRequestMap(List<Log> logs) {
        if (logs == null || logs.size() == 0) {
            return null;
        }
        int totalUploadSize = 0;
        HashMap<String, StringBuilder> temp = new HashMap<>();
        List<Log> delayLogs = null;
        ArrayList<Log> priorityLogs = null;
        for (int i = 0; i < logs.size(); i++) {
            Log log = logs.get(i);
            if (totalUploadSize > 5242880) {
                delayLogs = addToDelayList(delayLogs, log);
                Logger.d("log delay to upload because totalUploadSize Exceed", "log", log, "totalUploadSize", Integer.valueOf(totalUploadSize));
            } else if (SystemConfigMgr.getInstance().checkDelayLog(LogAssemble.disassemble(log.getContent()))) {
                delayLogs = addToDelayList(delayLogs, log);
                if (logs.get(i).priority.compareToIgnoreCase("3") >= 0) {
                    if (priorityLogs == null) {
                        priorityLogs = new ArrayList<>();
                    }
                    priorityLogs.add(logs.get(i));
                }
                Logger.d("log delay to upload because delay config", "log", log);
            } else {
                StringBuilder vBuilder = temp.get(log.eventId);
                if (vBuilder == null) {
                    vBuilder = new StringBuilder();
                    temp.put(log.eventId, vBuilder);
                } else {
                    vBuilder.append(1);
                    totalUploadSize++;
                }
                String uploadContent = logs.get(i).getContent();
                vBuilder.append(uploadContent);
                totalUploadSize += uploadContent.length();
            }
        }
        if (delayLogs != null) {
            logs.removeAll(delayLogs);
        }
        if (priorityLogs != null) {
            for (int i2 = 0; i2 < priorityLogs.size(); i2++) {
                priorityLogs.get(i2).priority = "2";
            }
            LogStoreMgr.getInstance().updateLogPriority(priorityLogs);
        }
        HashMap<String, String> ret = new HashMap<>();
        this.mUploadByteSize = totalUploadSize;
        for (String key : temp.keySet()) {
            ret.put(key, temp.get(key).toString());
        }
        if (logs.size() > 0) {
            this.mAveragePackageSize = ((float) this.mUploadByteSize) / ((float) logs.size());
        }
        Logger.d(TAG, "averagePackageSize", Float.valueOf(this.mAveragePackageSize), "mUploadByteSize", Integer.valueOf(this.mUploadByteSize), "count", Integer.valueOf(logs.size()));
        return ret;
    }

    private List<Log> addToDelayList(List<Log> delayLogs, Log log) {
        if (delayLogs == null) {
            delayLogs = new ArrayList<>();
        }
        delayLogs.add(log);
        return delayLogs;
    }

    private void reduceWindowSize() {
        this.mWinSize /= 2;
        if (this.mWinSize < 1) {
            this.mWinSize = 1;
            this.mFactor = 0;
        } else if (this.mWinSize > MAX_LOG_COUNT) {
            this.mWinSize = MAX_LOG_COUNT;
        }
        Logger.d(TAG, null, "winsize", Integer.valueOf(this.mWinSize));
    }

    private int calPackPackageWindowSize(Boolean isSuccess, long pTransferInterval) {
        if (pTransferInterval < 0) {
            return this.mWinSize;
        }
        float currentSpeed = ((float) this.mUploadByteSize) / ((float) pTransferInterval);
        if (!isSuccess.booleanValue()) {
            this.mWinSize /= 2;
            this.mFactor++;
        } else if (pTransferInterval > 45000) {
            return this.mWinSize;
        } else {
            this.mWinSize = (int) ((((double) (45000.0f * currentSpeed)) / ((double) this.mAveragePackageSize)) - ((double) this.mFactor));
        }
        if (this.mWinSize < 1) {
            this.mWinSize = 1;
            this.mFactor = 0;
        } else if (this.mWinSize > MAX_LOG_COUNT) {
            this.mWinSize = MAX_LOG_COUNT;
        }
        Logger.d(TAG, "winsize", Integer.valueOf(this.mWinSize));
        return this.mWinSize;
    }

    private int getLogCount() {
        if (this.mWinSize == -1) {
            String type = NetworkUtil.getNetworkType();
            if ("Wi-Fi".equalsIgnoreCase(type)) {
                this.mWinSize = 20;
            } else if ("4G".equalsIgnoreCase(type)) {
                this.mWinSize = 16;
            } else if ("3G".equalsIgnoreCase(type)) {
                this.mWinSize = 12;
            } else {
                this.mWinSize = 8;
            }
        }
        return this.mWinSize;
    }
}
