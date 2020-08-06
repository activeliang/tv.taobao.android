package mtopsdk.framework.filter.duplex;

import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HeaderHandlerUtil;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.config.AppConfigManager;
import mtopsdk.framework.domain.FilterResult;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.filter.IAfterFilter;
import mtopsdk.framework.filter.IBeforeFilter;
import mtopsdk.mtop.common.MtopNetworkProp;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopUnitStrategy;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.mtop.util.MtopStatistics;
import mtopsdk.mtop.xcommand.XcmdEventMgr;

public class AppConfigDuplexFilter implements IBeforeFilter, IAfterFilter {
    private static final String TAG = "mtopsdk.AppConfigDuplexFilter";

    public String getName() {
        return TAG;
    }

    public String doAfter(MtopContext mtopContext) {
        Map<String, List<String>> headers = mtopContext.mtopResponse.getHeaderFields();
        MtopConfig mtopConfig = mtopContext.mtopInstance.getMtopConfig();
        String xcmdOrange = HeaderHandlerUtil.getSingleHeaderFieldByKey(headers, HttpHeaderConstant.X_COMMAND_ORANGE);
        if (StringUtils.isNotBlank(xcmdOrange) && StringUtils.isNotBlank(xcmdOrange)) {
            try {
                XcmdEventMgr.getInstance().onOrangeEvent(URLDecoder.decode(xcmdOrange, "utf-8"));
            } catch (Exception e) {
                TBSdkLog.w(TAG, mtopContext.seqNo, "parse XCommand header field x-orange-p error,xcmdOrange=" + xcmdOrange, e);
            }
        }
        String xAppConfVersion = HeaderHandlerUtil.getSingleHeaderFieldByKey(headers, HttpHeaderConstant.X_APP_CONF_V);
        if (StringUtils.isBlank(xAppConfVersion)) {
            return FilterResult.CONTINUE;
        }
        long remoteAppConfigVersion = 0;
        try {
            remoteAppConfigVersion = Long.parseLong(xAppConfVersion);
        } catch (NumberFormatException e2) {
            TBSdkLog.e(TAG, mtopContext.seqNo, "parse remoteAppConfigVersion error.appConfigVersion=" + xAppConfVersion, e2);
        }
        if (remoteAppConfigVersion > mtopConfig.xAppConfigVersion) {
            updateAppConf(remoteAppConfigVersion, mtopContext);
        }
        return FilterResult.CONTINUE;
    }

    private void updateAppConf(long remoteAppConfigVersion, MtopContext mtopContext) {
        final MtopConfig mtopConfig = mtopContext.mtopInstance.getMtopConfig();
        final long j = remoteAppConfigVersion;
        final MtopContext mtopContext2 = mtopContext;
        MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
            /* JADX WARNING: Code restructure failed: missing block: B:23:0x0084, code lost:
                if (r6 == false) goto L_?;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
                mtopsdk.common.util.MtopUtils.writeObject(new mtopsdk.mtop.cache.domain.AppConfigDo(r0, r4), new java.io.File(r3.context.getExternalFilesDir((java.lang.String) null).getAbsoluteFile() + "/mtop"), "appConf");
             */
            /* JADX WARNING: Code restructure failed: missing block: B:26:0x00bf, code lost:
                if (mtopsdk.common.util.TBSdkLog.isLogEnable(mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable) == false) goto L_?;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:27:0x00c1, code lost:
                mtopsdk.common.util.TBSdkLog.i(mtopsdk.framework.filter.duplex.AppConfigDuplexFilter.TAG, r6.seqNo, "[updateAppConf] store appConf succeed. appConfVersion=" + r4);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:28:0x00e3, code lost:
                r3 = move-exception;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:29:0x00e4, code lost:
                mtopsdk.common.util.TBSdkLog.e(mtopsdk.framework.filter.duplex.AppConfigDuplexFilter.TAG, r6.seqNo, "[updateAppConf] store appConf error. appConfVersion=" + r4, r3);
             */
            /* JADX WARNING: Code restructure failed: missing block: B:36:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:37:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:38:?, code lost:
                return;
             */
            /* JADX WARNING: Code restructure failed: missing block: B:39:?, code lost:
                return;
             */
            /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void run() {
                /*
                    r14 = this;
                    r0 = 0
                    r6 = 0
                    mtopsdk.mtop.global.MtopConfig r8 = r3
                    byte[] r9 = r8.lock
                    monitor-enter(r9)
                    long r10 = r4     // Catch:{ all -> 0x001f }
                    mtopsdk.mtop.global.MtopConfig r8 = r3     // Catch:{ all -> 0x001f }
                    long r12 = r8.xAppConfigVersion     // Catch:{ all -> 0x001f }
                    int r8 = (r10 > r12 ? 1 : (r10 == r12 ? 0 : -1))
                    if (r8 > 0) goto L_0x0013
                    monitor-exit(r9)     // Catch:{ all -> 0x001f }
                L_0x0012:
                    return
                L_0x0013:
                    mtopsdk.framework.domain.MtopContext r8 = r6     // Catch:{ all -> 0x001f }
                    mtopsdk.mtop.domain.MtopResponse r8 = r8.mtopResponse     // Catch:{ all -> 0x001f }
                    byte[] r2 = r8.getBytedata()     // Catch:{ all -> 0x001f }
                    if (r2 != 0) goto L_0x0022
                    monitor-exit(r9)     // Catch:{ all -> 0x001f }
                    goto L_0x0012
                L_0x001f:
                    r8 = move-exception
                    monitor-exit(r9)     // Catch:{ all -> 0x001f }
                    throw r8
                L_0x0022:
                    java.lang.String r7 = new java.lang.String     // Catch:{ Exception -> 0x0106 }
                    java.lang.String r8 = "utf-8"
                    r7.<init>(r2, r8)     // Catch:{ Exception -> 0x0106 }
                    org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ Exception -> 0x0106 }
                    r5.<init>(r7)     // Catch:{ Exception -> 0x0106 }
                    java.lang.String r8 = "appConf"
                    java.lang.String r0 = r5.optString(r8)     // Catch:{ Exception -> 0x0106 }
                    boolean r8 = mtopsdk.common.util.StringUtils.isNotBlank(r0)     // Catch:{ Exception -> 0x0106 }
                    if (r8 == 0) goto L_0x0048
                    mtopsdk.config.AppConfigManager r8 = mtopsdk.config.AppConfigManager.getInstance()     // Catch:{ Exception -> 0x0106 }
                    mtopsdk.framework.domain.MtopContext r10 = r6     // Catch:{ Exception -> 0x0106 }
                    java.lang.String r10 = r10.seqNo     // Catch:{ Exception -> 0x0106 }
                    boolean r6 = r8.parseAppConfig(r0, r10)     // Catch:{ Exception -> 0x0106 }
                L_0x0048:
                    if (r6 == 0) goto L_0x0083
                    mtopsdk.mtop.global.MtopConfig r8 = r3     // Catch:{ Exception -> 0x0106 }
                    long r10 = r4     // Catch:{ Exception -> 0x0106 }
                    r8.xAppConfigVersion = r10     // Catch:{ Exception -> 0x0106 }
                    mtopsdk.common.util.TBSdkLog$LogEnable r8 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable     // Catch:{ Exception -> 0x0106 }
                    boolean r8 = mtopsdk.common.util.TBSdkLog.isLogEnable(r8)     // Catch:{ Exception -> 0x0106 }
                    if (r8 == 0) goto L_0x0083
                    java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0106 }
                    r8 = 64
                    r1.<init>(r8)     // Catch:{ Exception -> 0x0106 }
                    java.lang.String r8 = "[updateAppConf]update AppConf succeed!appConfVersion="
                    java.lang.StringBuilder r8 = r1.append(r8)     // Catch:{ Exception -> 0x0106 }
                    long r10 = r4     // Catch:{ Exception -> 0x0106 }
                    r8.append(r10)     // Catch:{ Exception -> 0x0106 }
                    java.lang.String r8 = ", appConf="
                    java.lang.StringBuilder r8 = r1.append(r8)     // Catch:{ Exception -> 0x0106 }
                    r8.append(r0)     // Catch:{ Exception -> 0x0106 }
                    java.lang.String r8 = "mtopsdk.AppConfigDuplexFilter"
                    mtopsdk.framework.domain.MtopContext r10 = r6     // Catch:{ Exception -> 0x0106 }
                    java.lang.String r10 = r10.seqNo     // Catch:{ Exception -> 0x0106 }
                    java.lang.String r11 = r1.toString()     // Catch:{ Exception -> 0x0106 }
                    mtopsdk.common.util.TBSdkLog.i((java.lang.String) r8, (java.lang.String) r10, (java.lang.String) r11)     // Catch:{ Exception -> 0x0106 }
                L_0x0083:
                    monitor-exit(r9)     // Catch:{ all -> 0x001f }
                    if (r6 == 0) goto L_0x0012
                    java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00e3 }
                    r8.<init>()     // Catch:{ Exception -> 0x00e3 }
                    mtopsdk.mtop.global.MtopConfig r9 = r3     // Catch:{ Exception -> 0x00e3 }
                    android.content.Context r9 = r9.context     // Catch:{ Exception -> 0x00e3 }
                    r10 = 0
                    java.io.File r9 = r9.getExternalFilesDir(r10)     // Catch:{ Exception -> 0x00e3 }
                    java.io.File r9 = r9.getAbsoluteFile()     // Catch:{ Exception -> 0x00e3 }
                    java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Exception -> 0x00e3 }
                    java.lang.String r9 = "/mtop"
                    java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Exception -> 0x00e3 }
                    java.lang.String r4 = r8.toString()     // Catch:{ Exception -> 0x00e3 }
                    mtopsdk.mtop.cache.domain.AppConfigDo r8 = new mtopsdk.mtop.cache.domain.AppConfigDo     // Catch:{ Exception -> 0x00e3 }
                    long r10 = r4     // Catch:{ Exception -> 0x00e3 }
                    r8.<init>(r0, r10)     // Catch:{ Exception -> 0x00e3 }
                    java.io.File r9 = new java.io.File     // Catch:{ Exception -> 0x00e3 }
                    r9.<init>(r4)     // Catch:{ Exception -> 0x00e3 }
                    java.lang.String r10 = "appConf"
                    mtopsdk.common.util.MtopUtils.writeObject(r8, r9, r10)     // Catch:{ Exception -> 0x00e3 }
                    mtopsdk.common.util.TBSdkLog$LogEnable r8 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable     // Catch:{ Exception -> 0x00e3 }
                    boolean r8 = mtopsdk.common.util.TBSdkLog.isLogEnable(r8)     // Catch:{ Exception -> 0x00e3 }
                    if (r8 == 0) goto L_0x0012
                    java.lang.String r8 = "mtopsdk.AppConfigDuplexFilter"
                    mtopsdk.framework.domain.MtopContext r9 = r6     // Catch:{ Exception -> 0x00e3 }
                    java.lang.String r9 = r9.seqNo     // Catch:{ Exception -> 0x00e3 }
                    java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00e3 }
                    r10.<init>()     // Catch:{ Exception -> 0x00e3 }
                    java.lang.String r11 = "[updateAppConf] store appConf succeed. appConfVersion="
                    java.lang.StringBuilder r10 = r10.append(r11)     // Catch:{ Exception -> 0x00e3 }
                    long r12 = r4     // Catch:{ Exception -> 0x00e3 }
                    java.lang.StringBuilder r10 = r10.append(r12)     // Catch:{ Exception -> 0x00e3 }
                    java.lang.String r10 = r10.toString()     // Catch:{ Exception -> 0x00e3 }
                    mtopsdk.common.util.TBSdkLog.i((java.lang.String) r8, (java.lang.String) r9, (java.lang.String) r10)     // Catch:{ Exception -> 0x00e3 }
                    goto L_0x0012
                L_0x00e3:
                    r3 = move-exception
                    java.lang.String r8 = "mtopsdk.AppConfigDuplexFilter"
                    mtopsdk.framework.domain.MtopContext r9 = r6
                    java.lang.String r9 = r9.seqNo
                    java.lang.StringBuilder r10 = new java.lang.StringBuilder
                    r10.<init>()
                    java.lang.String r11 = "[updateAppConf] store appConf error. appConfVersion="
                    java.lang.StringBuilder r10 = r10.append(r11)
                    long r12 = r4
                    java.lang.StringBuilder r10 = r10.append(r12)
                    java.lang.String r10 = r10.toString()
                    mtopsdk.common.util.TBSdkLog.e(r8, r9, r10, r3)
                    goto L_0x0012
                L_0x0106:
                    r3 = move-exception
                    java.lang.String r8 = "mtopsdk.AppConfigDuplexFilter"
                    mtopsdk.framework.domain.MtopContext r10 = r6     // Catch:{ all -> 0x001f }
                    java.lang.String r10 = r10.seqNo     // Catch:{ all -> 0x001f }
                    java.lang.String r11 = "[updateAppConf]parse and persist AppConf in data error"
                    mtopsdk.common.util.TBSdkLog.e(r8, r10, r11, r3)     // Catch:{ all -> 0x001f }
                    goto L_0x0083
                */
                throw new UnsupportedOperationException("Method not decompiled: mtopsdk.framework.filter.duplex.AppConfigDuplexFilter.AnonymousClass1.run():void");
            }
        });
    }

    public String doBefore(MtopContext mtopContext) {
        EnvModeEnum envMode;
        Mtop mtopInstance = mtopContext.mtopInstance;
        MtopStatistics stats = mtopContext.stats;
        MtopNetworkProp property = mtopContext.property;
        try {
            StringBuilder builder = new StringBuilder(64);
            builder.append(mtopInstance.getMtopConfig().utdid);
            builder.append(System.currentTimeMillis());
            builder.append(new DecimalFormat("0000").format((long) (stats.intSeqNo % 10000)));
            builder.append("1");
            builder.append(mtopInstance.getMtopConfig().processId);
            property.clientTraceId = builder.toString();
            stats.clientTraceId = property.clientTraceId;
        } catch (Exception e) {
            TBSdkLog.e(TAG, mtopContext.seqNo, "generate client-trace-id failed.", e);
        }
        try {
            if (!AppConfigManager.getInstance().isTradeUnitApi(mtopContext.mtopRequest.getKey()) || (envMode = mtopInstance.getMtopConfig().envMode) == null) {
                return FilterResult.CONTINUE;
            }
            switch (envMode) {
                case ONLINE:
                    property.customOnlineDomain = MtopUnitStrategy.TRADE_ONLINE_DOMAIN;
                    return FilterResult.CONTINUE;
                case PREPARE:
                    property.customPreDomain = MtopUnitStrategy.TRADE_PRE_DOMAIN;
                    return FilterResult.CONTINUE;
                case TEST:
                case TEST_SANDBOX:
                    property.customDailyDomain = MtopUnitStrategy.TRADE_DAILY_DOMAIN;
                    return FilterResult.CONTINUE;
                default:
                    return FilterResult.CONTINUE;
            }
            TBSdkLog.e(TAG, mtopContext.seqNo, "setCustomDomain for trade unit api error", e);
            return FilterResult.CONTINUE;
        } catch (Exception e2) {
            TBSdkLog.e(TAG, mtopContext.seqNo, "setCustomDomain for trade unit api error", e2);
            return FilterResult.CONTINUE;
        }
    }
}
