package mtopsdk.mtop.util;

import anetwork.channel.statist.StatisticData;
import com.taobao.tao.remotebusiness.js.MtopJSBridge;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.MtopNetworkProp;
import mtopsdk.mtop.stat.IUploadStats;
import mtopsdk.network.domain.NetworkStats;

public class MtopStatistics implements Cloneable {
    private static final String MTOP_EXCEPTIONS_MONITOR_POINT = "mtopExceptions";
    private static final String MTOP_STATS_MODULE = "mtopsdk";
    private static final String MTOP_STATS_MONITOR_POINT = "mtopStats";
    private static final String TAG = "mtopsdk.MtopStatistics";
    private static volatile AtomicBoolean isRegistered = new AtomicBoolean(false);
    public String apiKey;
    public boolean backGround;
    public long buildParamsTime;
    public long cacheCostTime;
    public int cacheHitType;
    public long cacheResponseParseEndTime;
    public long cacheResponseParseStartTime;
    public long cacheResponseParseTime;
    public long cacheReturnTime;
    public int cacheSwitch;
    public String clientTraceId;
    public boolean commitStat;
    public long computeMiniWuaTime;
    public long computeSignTime;
    public long computeWuaTime;
    public String domain;
    protected long endTime;
    public int intSeqNo;
    public String mappingCode;
    public long netSendEndTime;
    public long netSendStartTime;
    public NetworkStats netStats;
    public long netTotalTime;
    public String pageName;
    public String pageUrl;
    private RbStatisticData rbStatData;
    public String retCode;
    public int retType;
    public final String seqNo;
    public String serverTraceId;
    public long startCallbackTime;
    public long startExecuteTime;
    public long startTime;
    protected String statSum;
    public int statusCode;
    public long totalTime;
    private IUploadStats uploadStats;
    public long waitCallbackTime;
    public long waitExecuteTime;

    public interface RetType {
        public static final int BIZ_ERROR = 3;
        public static final int NETWORK_ERROR = 1;
        public static final int SUCCESS = 0;
        public static final int SYSTEM_ERROR = 2;

        @Retention(RetentionPolicy.SOURCE)
        public @interface Definition {
        }
    }

    public MtopStatistics(IUploadStats uploadStats2) {
        this.commitStat = true;
        this.cacheHitType = 0;
        this.retType = 0;
        this.statSum = "";
        this.apiKey = "";
        this.uploadStats = uploadStats2;
        this.intSeqNo = MtopUtils.createIntSeqNo();
        this.seqNo = "MTOP" + this.intSeqNo;
    }

    public MtopStatistics(IUploadStats uploadStats2, MtopNetworkProp mtopProperty) {
        this(uploadStats2);
        if (mtopProperty != null) {
            this.pageName = mtopProperty.pageName;
            this.pageUrl = mtopProperty.pageUrl;
            this.backGround = mtopProperty.backGround;
        }
    }

    public long currentTimeMillis() {
        return System.nanoTime() / 1000000;
    }

    public void onEndAndCommit() {
        long j;
        long j2;
        long j3 = 0;
        this.endTime = currentTimeMillis();
        this.totalTime = this.endTime - this.startTime;
        if (this.startExecuteTime > this.startTime) {
            j = this.startExecuteTime - this.startTime;
        } else {
            j = 0;
        }
        this.waitExecuteTime = j;
        if (this.cacheReturnTime > 0) {
            j2 = this.cacheReturnTime - this.startTime;
        } else {
            j2 = 0;
        }
        this.cacheCostTime = j2;
        this.cacheResponseParseTime = this.cacheResponseParseEndTime - this.cacheResponseParseStartTime;
        this.netTotalTime = this.netSendEndTime - this.netSendStartTime;
        if (this.startCallbackTime > this.netSendEndTime) {
            j3 = this.startCallbackTime - this.netSendEndTime;
        }
        this.waitCallbackTime = j3;
        StringBuilder builder = new StringBuilder(128);
        builder.append("apiKey=").append(this.apiKey);
        builder.append(",httpResponseStatus=").append(this.statusCode);
        builder.append(",retCode=").append(this.retCode);
        builder.append(",retType=").append(this.retType);
        builder.append(",mappingCode=").append(this.mappingCode);
        builder.append(",mtopTotalTime=").append(this.totalTime);
        builder.append(",networkTotalTime=").append(this.netTotalTime);
        builder.append(",waitExecuteTime=").append(this.waitExecuteTime);
        builder.append(",buildParamsTime=").append(this.buildParamsTime);
        builder.append(",computeSignTime=").append(this.computeSignTime);
        builder.append(",computeMiniWuaTime=").append(this.computeMiniWuaTime);
        builder.append(",computeWuaTime=").append(this.computeWuaTime);
        builder.append(",waitCallbackTime=").append(this.waitCallbackTime);
        builder.append(",cacheSwitch=").append(this.cacheSwitch);
        builder.append(",cacheHitType=").append(this.cacheHitType);
        builder.append(",cacheCostTime=").append(this.cacheCostTime);
        builder.append(",cacheResponseParseTime=").append(this.cacheResponseParseTime);
        if (this.netStats != null) {
            builder.append(",");
            if (StringUtils.isBlank(this.netStats.netStatSum)) {
                builder.append(this.netStats.sumNetStat());
            } else {
                builder.append(this.netStats.netStatSum);
            }
        }
        this.statSum = builder.toString();
        commitStatData();
        TBSdkLog.logTraceId(this.clientTraceId, this.serverTraceId);
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, this.seqNo, toString());
        }
    }

    public NetworkStats getNetworkStats() {
        return this.netStats;
    }

    @Deprecated
    public StatisticData getNetStat() {
        if (this.netStats == null) {
            return null;
        }
        StatisticData statisticData = new StatisticData();
        statisticData.isRequestSuccess = this.netStats.isRequestSuccess;
        statisticData.connectionType = this.netStats.connectionType;
        statisticData.oneWayTime_ANet = this.netStats.oneWayTime_ANet;
        statisticData.serverRT = this.netStats.serverRT;
        statisticData.totalSize = this.netStats.recvSize;
        return statisticData;
    }

    public long getTotalTime() {
        return this.totalTime;
    }

    public String getStatSum() {
        if (this.rbStatData == null) {
            return this.statSum;
        }
        if ("".equals(this.statSum)) {
            return this.rbStatData.getStatSum();
        }
        return this.statSum + "," + this.rbStatData.getStatSum();
    }

    public void commitStatData(boolean commitStat2) {
        this.commitStat = commitStat2;
        commitStatData();
    }

    private void commitStatData() {
        int i;
        int i2 = 1;
        if (this.commitStat && this.uploadStats != null) {
            if (isRegistered.compareAndSet(false, true)) {
                registerMtopStats();
            }
            try {
                Map<String, String> dimensions = new HashMap<>();
                dimensions.put("api", this.apiKey);
                dimensions.put("ret", this.retCode);
                dimensions.put("retType", String.valueOf(this.retType));
                dimensions.put("httpResponseStatus", String.valueOf(this.statusCode));
                dimensions.put("domain", this.domain);
                dimensions.put("cacheSwitch", String.valueOf(this.cacheSwitch));
                dimensions.put("cacheHitType", String.valueOf(this.cacheHitType));
                dimensions.put("clientTraceId", this.clientTraceId);
                dimensions.put("serverTraceId", this.serverTraceId);
                dimensions.put("pageName", this.pageName);
                dimensions.put(MtopJSBridge.MtopJSParam.PAGE_URL, this.pageUrl);
                if (this.backGround) {
                    i = 1;
                } else {
                    i = 0;
                }
                dimensions.put("backGround", String.valueOf(i));
                NetworkStats netStats2 = getNetworkStats();
                if (netStats2 != null) {
                    dimensions.put("connType", netStats2.connectionType);
                    dimensions.put("isSSL", netStats2.isSSL ? "1" : "0");
                    dimensions.put("retryTimes", String.valueOf(netStats2.retryTimes));
                    dimensions.put("ip_port", netStats2.ip_port);
                }
                Map<String, Double> measures = new HashMap<>();
                measures.put("totalTime", Double.valueOf((double) this.totalTime));
                measures.put("networkExeTime", Double.valueOf((double) this.netTotalTime));
                measures.put("cacheCostTime", Double.valueOf((double) this.cacheCostTime));
                measures.put("cacheResponseParseTime", Double.valueOf((double) this.cacheResponseParseTime));
                measures.put("waitExecuteTime", Double.valueOf((double) this.waitExecuteTime));
                measures.put("waitCallbackTime", Double.valueOf((double) this.waitCallbackTime));
                measures.put("signTime", Double.valueOf((double) this.computeSignTime));
                measures.put("wuaTime", Double.valueOf((double) this.computeWuaTime));
                measures.put("miniWuaTime", Double.valueOf((double) this.computeMiniWuaTime));
                if (netStats2 != null) {
                    measures.put("firstDataTime", Double.valueOf((double) netStats2.firstDataTime));
                    measures.put("recDataTime", Double.valueOf((double) netStats2.recDataTime));
                    measures.put("oneWayTime_ANet", Double.valueOf((double) netStats2.oneWayTime_ANet));
                    measures.put("serverRT", Double.valueOf((double) netStats2.serverRT));
                    measures.put("revSize", Double.valueOf((double) netStats2.recvSize));
                    measures.put("dataSpeed", Double.valueOf((double) netStats2.dataSpeed));
                }
                if (this.rbStatData != null) {
                    measures.put("rbReqTime", Double.valueOf((double) this.rbStatData.rbReqTime));
                    measures.put("toMainThTime", Double.valueOf((double) this.rbStatData.toMainThTime));
                    measures.put("mtopJsonParseTime", Double.valueOf((double) this.rbStatData.jsonParseTime));
                    measures.put("mtopReqTime", Double.valueOf((double) this.rbStatData.mtopReqTime));
                }
                if (this.uploadStats != null) {
                    this.uploadStats.onCommit(MTOP_STATS_MODULE, MTOP_STATS_MONITOR_POINT, dimensions, measures);
                }
                if (!ErrorConstant.isSuccess(this.retCode)) {
                    Map<String, String> dimensions2 = new HashMap<>();
                    dimensions2.put("api", this.apiKey);
                    dimensions2.put("ret", this.retCode);
                    dimensions2.put("retType", String.valueOf(this.retType));
                    dimensions2.put("mappingCode", this.mappingCode);
                    dimensions2.put("httpResponseStatus", String.valueOf(this.statusCode));
                    dimensions2.put("domain", this.domain);
                    dimensions2.put("refer", this.pageUrl);
                    dimensions2.put("clientTraceId", this.clientTraceId);
                    dimensions2.put("serverTraceId", this.serverTraceId);
                    dimensions2.put("pageName", this.pageName);
                    dimensions2.put(MtopJSBridge.MtopJSParam.PAGE_URL, this.pageUrl);
                    if (!this.backGround) {
                        i2 = 0;
                    }
                    dimensions2.put("backGround", String.valueOf(i2));
                    if (this.uploadStats != null) {
                        this.uploadStats.onCommit(MTOP_STATS_MODULE, MTOP_EXCEPTIONS_MONITOR_POINT, dimensions2, (Map<String, Double>) null);
                    }
                }
            } catch (Throwable e) {
                TBSdkLog.e(TAG, this.seqNo, "[commitStatData] commit mtopStats error ---" + e.toString());
            } finally {
                this.commitStat = false;
            }
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(128);
        builder.append("MtopStatistics ").append(hashCode());
        builder.append("[SumStat(ms)]:").append(this.statSum);
        if (this.rbStatData != null) {
            builder.append(" [rbStatData]:").append(this.rbStatData);
        }
        return builder.toString();
    }

    public synchronized RbStatisticData getRbStatData() {
        if (this.rbStatData == null) {
            this.rbStatData = new RbStatisticData();
        }
        return this.rbStatData;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private void registerMtopStats() {
        try {
            if (this.uploadStats == null) {
                TBSdkLog.e(TAG, this.seqNo, "[registerMtopStats]register MtopStats error, uploadStats=null");
                return;
            }
            Set<String> dimensions = new HashSet<>();
            dimensions.add("api");
            dimensions.add("domain");
            dimensions.add("httpResponseStatus");
            dimensions.add("ret");
            dimensions.add("retType");
            dimensions.add("cacheSwitch");
            dimensions.add("cacheHitType");
            dimensions.add("clientTraceId");
            dimensions.add("serverTraceId");
            dimensions.add("connType");
            dimensions.add("isSSL");
            dimensions.add("retryTimes");
            dimensions.add("ip_port");
            dimensions.add("pageName");
            dimensions.add(MtopJSBridge.MtopJSParam.PAGE_URL);
            dimensions.add("backGround");
            Set<String> measures = new HashSet<>();
            measures.add("totalTime");
            measures.add("networkExeTime");
            measures.add("cacheCostTime");
            measures.add("cacheResponseParseTime");
            measures.add("waitExecuteTime");
            measures.add("waitCallbackTime");
            measures.add("signTime");
            measures.add("wuaTime");
            measures.add("miniWuaTime");
            measures.add("rbReqTime");
            measures.add("toMainThTime");
            measures.add("mtopJsonParseTime");
            measures.add("mtopReqTime");
            measures.add("firstDataTime");
            measures.add("recDataTime");
            measures.add("revSize");
            measures.add("dataSpeed");
            measures.add("oneWayTime_ANet");
            measures.add("serverRT");
            if (this.uploadStats != null) {
                this.uploadStats.onRegister(MTOP_STATS_MODULE, MTOP_STATS_MONITOR_POINT, dimensions, measures, false);
            }
            Set<String> dimens = new HashSet<>();
            dimens.add("api");
            dimens.add("domain");
            dimens.add("ret");
            dimens.add("retType");
            dimens.add("mappingCode");
            dimens.add("httpResponseStatus");
            dimens.add("refer");
            dimens.add("clientTraceId");
            dimens.add("serverTraceId");
            dimens.add("pageName");
            dimens.add(MtopJSBridge.MtopJSParam.PAGE_URL);
            dimens.add("backGround");
            if (this.uploadStats != null) {
                this.uploadStats.onRegister(MTOP_STATS_MODULE, MTOP_EXCEPTIONS_MONITOR_POINT, dimens, (Set<String>) null, false);
            }
            TBSdkLog.i(TAG, this.seqNo, "[registerMtopStats]register MtopStats executed.uploadStats=" + this.uploadStats);
        } catch (Throwable e) {
            TBSdkLog.e(TAG, this.seqNo, "[registerMtopStats] register MtopStats error ---" + e.toString());
        }
    }

    public class RbStatisticData implements Cloneable {
        public long afterReqTime;
        public long beforeReqTime;
        public int isCache;
        public long jsonParseTime;
        @Deprecated
        public long jsonTime;
        public long mtopReqTime;
        public long parseTime;
        public long rbReqTime;
        public long toMainThTime;
        @Deprecated
        public long totalTime;

        private RbStatisticData() {
            this.isCache = 0;
        }

        public String getStatSum() {
            StringBuilder sb = new StringBuilder(32);
            sb.append("rbReqTime=").append(this.rbReqTime);
            sb.append(",mtopReqTime=").append(this.mtopReqTime);
            sb.append(",mtopJsonParseTime=").append(this.jsonParseTime);
            sb.append(",toMainThTime=").append(this.toMainThTime);
            sb.append(",isCache=").append(this.isCache);
            return sb.toString();
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(64);
            sb.append("rbReqTime=").append(this.rbReqTime);
            sb.append(",mtopReqTime=").append(this.mtopReqTime);
            sb.append(",mtopJsonParseTime=").append(this.jsonParseTime);
            sb.append(",toMainThTime=").append(this.toMainThTime);
            sb.append(",isCache=").append(this.isCache);
            sb.append(",beforeReqTime=").append(this.beforeReqTime);
            sb.append(",afterReqTime=").append(this.afterReqTime);
            sb.append(",parseTime=").append(this.parseTime);
            return sb.toString();
        }

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
}
