package com.taobao.tao.remotebusiness;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.taobao.tao.remotebusiness.listener.MtopListenerProxyFactory;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.common.ApiID;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.ApiTypeEnum;
import mtopsdk.mtop.domain.BaseOutDo;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.JsonTypeEnum;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.domain.ProtocolEnum;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.intf.MtopBuilder;

public class MtopBusiness extends MtopBuilder {
    public static final int MAX_RETRY_TIMES = 3;
    private static final String TAG = "mtopsdk.MtopBusiness";
    private static AtomicInteger seqGen = new AtomicInteger(0);
    private ApiID apiID;
    public String authParam = null;
    public Class<?> clazz;
    public boolean isCached = false;
    private boolean isCancelled = false;
    private boolean isErrorNotifyAfterCache = false;
    public MtopListener listener;
    private MtopResponse mtopResponse = null;
    private boolean needAuth = false;
    public long onBgFinishTime = 0;
    public long reqStartTime = 0;
    @Deprecated
    public Object requestContext = null;
    protected int requestType = 0;
    protected int retryTime = 0;
    public long sendStartTime = 0;
    private final String seqNo = genSeqNo();
    public boolean showAuthUI = true;
    private boolean showLoginUI = true;
    private boolean syncRequestFlag = false;

    protected MtopBusiness(@NonNull Mtop mtop, IMTOPDataObject request, String ttid) {
        super(mtop, request, ttid);
    }

    protected MtopBusiness(@NonNull Mtop mtop, MtopRequest request, String ttid) {
        super(mtop, request, ttid);
    }

    private String genSeqNo() {
        StringBuilder sb = new StringBuilder(16);
        sb.append("MB").append(seqGen.incrementAndGet()).append('.').append(this.stat.seqNo);
        return sb.toString();
    }

    public static MtopBusiness build(Mtop mtop, IMTOPDataObject inputData, String ttid) {
        return new MtopBusiness(mtop, inputData, ttid);
    }

    public static MtopBusiness build(Mtop mtop, IMTOPDataObject inputData) {
        return build(mtop, inputData, (String) null);
    }

    @Deprecated
    public static MtopBusiness build(IMTOPDataObject inputData, String ttid) {
        return build(Mtop.instance((Context) null, ttid), inputData, ttid);
    }

    @Deprecated
    public static MtopBusiness build(IMTOPDataObject inputData) {
        return build(Mtop.instance((Context) null), inputData);
    }

    public static MtopBusiness build(Mtop mtop, MtopRequest request, String ttid) {
        return new MtopBusiness(mtop, request, ttid);
    }

    public static MtopBusiness build(Mtop mtop, MtopRequest request) {
        return build(mtop, request, (String) null);
    }

    @Deprecated
    public static MtopBusiness build(MtopRequest request, String ttid) {
        return build(Mtop.instance((Context) null, ttid), request, ttid);
    }

    @Deprecated
    public static MtopBusiness build(MtopRequest request) {
        return build(Mtop.instance((Context) null), request, (String) null);
    }

    @Deprecated
    public MtopBusiness registerListener(MtopListener listener2) {
        this.listener = listener2;
        return this;
    }

    @Deprecated
    public MtopBusiness addListener(MtopListener listener2) {
        this.listener = listener2;
        return this;
    }

    public MtopBusiness registerListener(IRemoteListener listener2) {
        this.listener = listener2;
        return this;
    }

    public String getSeqNo() {
        return this.seqNo;
    }

    public void startRequest() {
        startRequest(0, (Class<?>) null);
    }

    public void startRequest(Class<?> clazz2) {
        startRequest(0, clazz2);
    }

    public void startRequest(int requestType2, Class<?> clazz2) {
        if (this.request == null) {
            TBSdkLog.e(TAG, this.seqNo, "MtopRequest is null!");
            return;
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, this.seqNo, "startRequest " + this.request);
        }
        this.reqStartTime = System.currentTimeMillis();
        this.isCancelled = false;
        this.isCached = false;
        this.clazz = clazz2;
        this.requestType = requestType2;
        if (this.requestContext != null) {
            reqContext(this.requestContext);
        }
        if (this.listener != null && !this.isCancelled) {
            super.addListener(MtopListenerProxyFactory.getMtopListenerProxy(this, this.listener));
        }
        mtopCommitStatData(false);
        this.sendStartTime = System.currentTimeMillis();
        this.apiID = super.asyncRequest();
    }

    public MtopResponse syncRequest() {
        String apiKey = this.request != null ? this.request.getKey() : "";
        if (MtopUtils.isMainThread()) {
            TBSdkLog.e(TAG, this.seqNo, "do syncRequest in UI main thread!");
        }
        this.syncRequestFlag = true;
        if (this.listener == null) {
            this.listener = new IRemoteBaseListener() {
                public void onSuccess(int requestType, MtopResponse response, BaseOutDo pojo, Object requestContext) {
                }

                public void onError(int requestType, MtopResponse response, Object requestContext) {
                }

                public void onSystemError(int requestType, MtopResponse response, Object requestContext) {
                }
            };
        }
        startRequest();
        synchronized (this.listener) {
            try {
                if (this.mtopResponse == null) {
                    this.listener.wait(60000);
                }
            } catch (InterruptedException e) {
                TBSdkLog.e(TAG, this.seqNo, "syncRequest InterruptedException. apiKey=" + apiKey);
            } catch (Exception e2) {
                TBSdkLog.e(TAG, this.seqNo, "syncRequest do wait Exception. apiKey=" + apiKey);
            }
        }
        if (this.mtopResponse == null) {
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.ErrorEnable)) {
                TBSdkLog.w(TAG, this.seqNo, "syncRequest timeout. apiKey=" + apiKey);
            }
            cancelRequest();
        }
        if (this.mtopResponse != null) {
            return this.mtopResponse;
        }
        return handleAsyncTimeoutException();
    }

    @Deprecated
    public ApiID asyncRequest() {
        startRequest();
        return this.apiID;
    }

    public int getRequestType() {
        return this.requestType;
    }

    public boolean isTaskCanceled() {
        return this.isCancelled;
    }

    public int getRetryTime() {
        return this.retryTime;
    }

    public void cancelRequest() {
        cancelRequest((String) null);
    }

    /* access modifiers changed from: package-private */
    public void cancelRequest(String bizInfo) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, this.seqNo, getRequestLogInfo("cancelRequest.", this));
        }
        this.isCancelled = true;
        if (this.apiID != null) {
            try {
                this.apiID.cancelApiCall();
            } catch (Throwable e) {
                TBSdkLog.w(TAG, this.seqNo, getRequestLogInfo("cancelRequest failed.", this), e);
            }
        }
        RequestPool.removeFromRequestPool(this.mtopInstance, bizInfo, this);
    }

    /* access modifiers changed from: package-private */
    public void retryRequest() {
        retryRequest((String) null);
    }

    /* access modifiers changed from: package-private */
    public void retryRequest(String bizInfo) {
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            TBSdkLog.i(TAG, this.seqNo, getRequestLogInfo("retryRequest.", this));
        }
        if (this.retryTime >= 3) {
            this.retryTime = 0;
            doFinish((MtopResponse) null, (BaseOutDo) null);
            return;
        }
        cancelRequest(bizInfo);
        startRequest(this.requestType, this.clazz);
        this.retryTime++;
    }

    public MtopBusiness showLoginUI(boolean showUI) {
        this.showLoginUI = showUI;
        return this;
    }

    public boolean isShowLoginUI() {
        return this.showLoginUI;
    }

    public MtopBusiness setNeedAuth(String authParam2, boolean showAuthUI2) {
        this.authParam = authParam2;
        this.showAuthUI = showAuthUI2;
        this.needAuth = true;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            StringBuilder builder = new StringBuilder(64);
            builder.append("[setNeedAuth] authParam=").append(authParam2);
            builder.append(", showAuthUI=").append(showAuthUI2);
            builder.append(", needAuth=").append(this.needAuth);
            TBSdkLog.d(TAG, this.seqNo, builder.toString());
        }
        return this;
    }

    public MtopBusiness setNeedAuth(@NonNull String openAppKey, String bizParam, boolean showAuthUI2) {
        this.mtopProp.apiType = ApiTypeEnum.ISV_OPEN_API;
        this.mtopProp.isInnerOpen = true;
        if (StringUtils.isNotBlank(openAppKey)) {
            this.mtopProp.openAppKey = openAppKey;
        }
        this.authParam = bizParam;
        this.showAuthUI = showAuthUI2;
        this.needAuth = true;
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            StringBuilder builder = new StringBuilder(64);
            builder.append("[setNeedAuth] openAppKey=").append(openAppKey);
            builder.append(", bizParam=").append(bizParam);
            builder.append(", showAuthUI=").append(showAuthUI2);
            builder.append(", needAuth=").append(this.needAuth);
            builder.append(", isInnerOpen=true");
            TBSdkLog.d(TAG, this.seqNo, builder.toString());
        }
        return this;
    }

    public boolean isNeedAuth() {
        return this.needAuth || this.authParam != null;
    }

    public MtopBusiness addOpenApiParams(String openAppKey, String accessToken) {
        return (MtopBusiness) super.addOpenApiParams(openAppKey, accessToken);
    }

    public MtopBusiness setErrorNotifyAfterCache(boolean bNotify) {
        this.isErrorNotifyAfterCache = bNotify;
        return this;
    }

    @Deprecated
    public void setErrorNotifyNeedAfterCache(boolean bNeed) {
        setErrorNotifyAfterCache(bNeed);
    }

    public void doFinish(MtopResponse response, BaseOutDo pojo) {
        if (this.syncRequestFlag) {
            this.mtopResponse = response;
            synchronized (this.listener) {
                try {
                    this.listener.notify();
                } catch (Exception e) {
                    TBSdkLog.e(TAG, this.seqNo, "[doFinish]syncRequest do notify Exception. apiKey=" + (response != null ? response.getFullKey() : ""), e);
                }
            }
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
            StringBuilder sb = new StringBuilder(32);
            sb.append("doFinish request=").append(this.request);
            if (response != null) {
                sb.append(", retCode=").append(response.getRetCode());
            }
            TBSdkLog.i(TAG, this.seqNo, sb.toString());
        }
        if (this.isCancelled) {
            TBSdkLog.w(TAG, this.seqNo, "request is cancelled,don't callback listener.");
        } else if (!(this.listener instanceof IRemoteListener)) {
            TBSdkLog.e(TAG, this.seqNo, "listener did't implement IRemoteBaseListener.apiKey=" + (response != null ? response.getFullKey() : ""));
        } else {
            IRemoteListener l = (IRemoteListener) this.listener;
            if (response != null && response.isApiSuccess()) {
                try {
                    l.onSuccess(this.requestType, response, pojo, getReqContext());
                } catch (Throwable e2) {
                    TBSdkLog.e(TAG, this.seqNo, "listener onSuccess callback error", e2);
                }
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, this.seqNo, "listener onSuccess callback.");
                }
            } else if (!this.isCached || this.isErrorNotifyAfterCache) {
                doErrorCallback(response, l);
            } else {
                TBSdkLog.i(TAG, this.seqNo, "listener onCached callback,doNothing in doFinish()");
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void doErrorCallback(mtopsdk.mtop.domain.MtopResponse r7, com.taobao.tao.remotebusiness.IRemoteListener r8) {
        /*
            r6 = this;
            r1 = 0
            if (r7 != 0) goto L_0x0065
            r1 = 1
            mtopsdk.common.util.TBSdkLog$LogEnable r2 = mtopsdk.common.util.TBSdkLog.LogEnable.ErrorEnable
            boolean r2 = mtopsdk.common.util.TBSdkLog.isLogEnable(r2)
            if (r2 == 0) goto L_0x002a
            java.lang.String r2 = "mtopsdk.MtopBusiness"
            java.lang.String r3 = r6.seqNo
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "mtopResponse is null."
            java.lang.StringBuilder r4 = r4.append(r5)
            mtopsdk.mtop.domain.MtopRequest r5 = r6.request
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r4)
        L_0x002a:
            if (r1 == 0) goto L_0x00c8
            boolean r2 = r8 instanceof com.taobao.tao.remotebusiness.IRemoteBaseListener     // Catch:{ Throwable -> 0x00d3 }
            if (r2 == 0) goto L_0x00c8
            com.taobao.tao.remotebusiness.IRemoteBaseListener r8 = (com.taobao.tao.remotebusiness.IRemoteBaseListener) r8     // Catch:{ Throwable -> 0x00d3 }
            int r2 = r6.requestType     // Catch:{ Throwable -> 0x00d3 }
            java.lang.Object r3 = r6.getReqContext()     // Catch:{ Throwable -> 0x00d3 }
            r8.onSystemError(r2, r7, r3)     // Catch:{ Throwable -> 0x00d3 }
        L_0x003b:
            mtopsdk.common.util.TBSdkLog$LogEnable r2 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable
            boolean r2 = mtopsdk.common.util.TBSdkLog.isLogEnable(r2)
            if (r2 == 0) goto L_0x0064
            java.lang.String r3 = "mtopsdk.MtopBusiness"
            java.lang.String r4 = r6.seqNo
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r5 = "listener onError callback, "
            java.lang.StringBuilder r5 = r2.append(r5)
            if (r1 == 0) goto L_0x00e1
            java.lang.String r2 = "sys error"
        L_0x0059:
            java.lang.StringBuilder r2 = r5.append(r2)
            java.lang.String r2 = r2.toString()
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r3, (java.lang.String) r4, (java.lang.String) r2)
        L_0x0064:
            return
        L_0x0065:
            boolean r2 = r7.isSessionInvalid()
            if (r2 == 0) goto L_0x0093
            r1 = 1
            mtopsdk.common.util.TBSdkLog$LogEnable r2 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable
            boolean r2 = mtopsdk.common.util.TBSdkLog.isLogEnable(r2)
            if (r2 == 0) goto L_0x002a
            java.lang.String r2 = "mtopsdk.MtopBusiness"
            java.lang.String r3 = r6.seqNo
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "session invalid error."
            java.lang.StringBuilder r4 = r4.append(r5)
            mtopsdk.mtop.domain.MtopRequest r5 = r6.request
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r4)
            goto L_0x002a
        L_0x0093:
            boolean r2 = r7.isMtopServerError()
            if (r2 != 0) goto L_0x009f
            boolean r2 = r7.isMtopSdkError()
            if (r2 == 0) goto L_0x002a
        L_0x009f:
            r1 = 1
            mtopsdk.common.util.TBSdkLog$LogEnable r2 = mtopsdk.common.util.TBSdkLog.LogEnable.InfoEnable
            boolean r2 = mtopsdk.common.util.TBSdkLog.isLogEnable(r2)
            if (r2 == 0) goto L_0x002a
            java.lang.String r2 = "mtopsdk.MtopBusiness"
            java.lang.String r3 = r6.seqNo
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "mtopServerError or mtopSdkError."
            java.lang.StringBuilder r4 = r4.append(r5)
            mtopsdk.mtop.domain.MtopRequest r5 = r6.request
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            mtopsdk.common.util.TBSdkLog.i((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r4)
            goto L_0x002a
        L_0x00c8:
            int r2 = r6.requestType     // Catch:{ Throwable -> 0x00d3 }
            java.lang.Object r3 = r6.getReqContext()     // Catch:{ Throwable -> 0x00d3 }
            r8.onError(r2, r7, r3)     // Catch:{ Throwable -> 0x00d3 }
            goto L_0x003b
        L_0x00d3:
            r0 = move-exception
            java.lang.String r2 = "mtopsdk.MtopBusiness"
            java.lang.String r3 = r6.seqNo
            java.lang.String r4 = "listener onError callback error"
            mtopsdk.common.util.TBSdkLog.e(r2, r3, r4, r0)
            goto L_0x003b
        L_0x00e1:
            java.lang.String r2 = "biz error"
            goto L_0x0059
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.tao.remotebusiness.MtopBusiness.doErrorCallback(mtopsdk.mtop.domain.MtopResponse, com.taobao.tao.remotebusiness.IRemoteListener):void");
    }

    public MtopBusiness setBizId(int bizId) {
        return (MtopBusiness) super.setBizId(bizId);
    }

    public MtopBusiness ttid(String ttid) {
        return (MtopBusiness) super.ttid(ttid);
    }

    public MtopBusiness useCache() {
        return (MtopBusiness) super.useCache();
    }

    public MtopBusiness useWua() {
        return (MtopBusiness) super.useWua();
    }

    @Deprecated
    public MtopBusiness useWua(int type) {
        return (MtopBusiness) super.useWua(type);
    }

    public MtopBusiness addHttpQueryParameter(String key, String value) {
        return (MtopBusiness) super.addHttpQueryParameter(key, value);
    }

    public MtopBusiness addCacheKeyParamBlackList(List<String> blackList) {
        return (MtopBusiness) super.addCacheKeyParamBlackList(blackList);
    }

    public MtopBusiness addMteeUa(String mteeUa) {
        return (MtopBusiness) super.addMteeUa(mteeUa);
    }

    public MtopBusiness enableProgressListener() {
        return (MtopBusiness) super.enableProgressListener();
    }

    public MtopBusiness forceRefreshCache() {
        return (MtopBusiness) super.forceRefreshCache();
    }

    public MtopBusiness handler(Handler handler) {
        return (MtopBusiness) super.handler(handler);
    }

    public MtopBusiness headers(Map<String, String> requestHeaders) {
        return (MtopBusiness) super.headers(requestHeaders);
    }

    public MtopBusiness protocol(ProtocolEnum protocol) {
        return (MtopBusiness) super.protocol(protocol);
    }

    public MtopBusiness reqContext(Object requestContext2) {
        return (MtopBusiness) super.reqContext(requestContext2);
    }

    public MtopBusiness reqMethod(MethodEnum method) {
        return (MtopBusiness) super.reqMethod(method);
    }

    public MtopBusiness retryTime(int retryTimes) {
        return (MtopBusiness) super.retryTime(retryTimes);
    }

    public MtopBusiness setCacheControlNoCache() {
        return (MtopBusiness) super.setCacheControlNoCache();
    }

    public MtopBusiness setConnectionTimeoutMilliSecond(int connTimeout) {
        return (MtopBusiness) super.setConnectionTimeoutMilliSecond(connTimeout);
    }

    public MtopBusiness setCustomDomain(String customDomain) {
        return (MtopBusiness) super.setCustomDomain(customDomain);
    }

    public MtopBusiness setCustomDomain(String onlineDomain, String preDomain, String dailyDomain) {
        return (MtopBusiness) super.setCustomDomain(onlineDomain, preDomain, dailyDomain);
    }

    public MtopBusiness setUnitStrategy(String unitStrategy) {
        return (MtopBusiness) super.setUnitStrategy(unitStrategy);
    }

    public MtopBusiness setJsonType(JsonTypeEnum jsonTypeEnum) {
        return (MtopBusiness) super.setJsonType(jsonTypeEnum);
    }

    public MtopBusiness setNetInfo(int param) {
        return (MtopBusiness) super.setNetInfo(param);
    }

    public MtopBusiness setPageUrl(String pageUrl) {
        return (MtopBusiness) super.setPageUrl(pageUrl);
    }

    public MtopBusiness setReqAppKey(String reqAppKey, String authCode) {
        return (MtopBusiness) super.setReqAppKey(reqAppKey, authCode);
    }

    public MtopBusiness setReqBizExt(String reqBizExt) {
        return (MtopBusiness) super.setReqBizExt(reqBizExt);
    }

    public MtopBusiness setReqSource(int reqSource) {
        return (MtopBusiness) super.setReqSource(reqSource);
    }

    public MtopBusiness setReqUserId(String reqUserId) {
        return (MtopBusiness) super.setReqUserId(reqUserId);
    }

    public MtopBusiness setSocketTimeoutMilliSecond(int socketTimeout) {
        return (MtopBusiness) super.setSocketTimeoutMilliSecond(socketTimeout);
    }

    public MtopBusiness setPriorityFlag(boolean priorityFlag) {
        this.mtopProp.priorityFlag = priorityFlag;
        return this;
    }

    public MtopBusiness setPriorityData(Map<String, String> priorityData) {
        this.mtopProp.priorityData = priorityData;
        return this;
    }

    public MtopBusiness setUserInfo(@Nullable String userInfo) {
        return (MtopBusiness) super.setUserInfo(userInfo);
    }

    private String getRequestLogInfo(String msg, MtopBusiness mtopBusiness) {
        StringBuilder sb = new StringBuilder(32);
        sb.append(msg).append(" [");
        if (mtopBusiness != null) {
            sb.append("apiName=").append(mtopBusiness.request.getApiName()).append(";version=").append(mtopBusiness.request.getVersion()).append(";requestType=").append(mtopBusiness.getRequestType());
        }
        sb.append("]");
        return sb.toString();
    }
}
