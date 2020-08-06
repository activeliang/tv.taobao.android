package mtopsdk.mtop.intf;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import com.taobao.orange.model.NameSpaceDO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.MtopUtils;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.framework.manager.FilterManager;
import mtopsdk.framework.util.FilterUtils;
import mtopsdk.mtop.common.ApiID;
import mtopsdk.mtop.common.DefaultMtopCallback;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.common.MtopNetworkProp;
import mtopsdk.mtop.common.listener.MtopBaseListenerProxy;
import mtopsdk.mtop.common.listener.MtopCacheListenerProxy;
import mtopsdk.mtop.domain.ApiTypeEnum;
import mtopsdk.mtop.domain.IMTOPDataObject;
import mtopsdk.mtop.domain.JsonTypeEnum;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import mtopsdk.mtop.domain.ProtocolEnum;
import mtopsdk.mtop.util.ErrorConstant;
import mtopsdk.mtop.util.MtopConvert;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.mtop.util.MtopStatistics;
import mtopsdk.network.Call;
import mtopsdk.xstate.XState;
import mtopsdk.xstate.util.XStateConstants;

public class MtopBuilder {
    private static final String TAG = "mtopsdk.MtopBuilder";
    public MtopListener listener;
    protected Mtop mtopInstance;
    public final MtopNetworkProp mtopProp;
    public MtopRequest request;
    @Deprecated
    public Object requestContext;
    protected MtopStatistics stat;

    public MtopBuilder(Mtop mtopInstance2, IMTOPDataObject mtopData, String ttid) {
        this(mtopInstance2, MtopConvert.inputDoToMtopRequest(mtopData), ttid);
    }

    public MtopBuilder(Mtop mtopInstance2, MtopRequest request2, String ttid) {
        this.mtopProp = new MtopNetworkProp();
        this.listener = null;
        this.requestContext = null;
        this.stat = null;
        this.mtopInstance = mtopInstance2;
        this.request = request2;
        this.mtopProp.ttid = ttid;
        this.mtopProp.pageName = XState.getValue(XStateConstants.KEY_CURRENT_PAGE_NAME);
        this.mtopProp.pageUrl = XState.getValue(XStateConstants.KEY_CURRENT_PAGE_URL);
        this.mtopProp.backGround = XState.isAppBackground();
        this.stat = new MtopStatistics(mtopInstance2.getMtopConfig().uploadStats, this.mtopProp);
    }

    @Deprecated
    public MtopBuilder(Mtop mtopInstance2, Object inputDo, String ttid) {
        this(mtopInstance2, MtopConvert.inputDoToMtopRequest(inputDo), ttid);
    }

    @Deprecated
    public MtopBuilder(IMTOPDataObject mtopData, String ttid) {
        this(Mtop.instance((Context) null), mtopData, ttid);
    }

    @Deprecated
    public MtopBuilder(MtopRequest request2, String ttid) {
        this(Mtop.instance((Context) null), request2, ttid);
    }

    @Deprecated
    public MtopBuilder(Object inputDo, String ttid) {
        this(Mtop.instance((Context) null), inputDo, ttid);
    }

    public MtopBuilder ttid(String ttid) {
        this.mtopProp.ttid = ttid;
        return this;
    }

    public MtopBuilder reqContext(Object requestContext2) {
        this.mtopProp.reqContext = requestContext2;
        return this;
    }

    public Object getReqContext() {
        return this.mtopProp.reqContext;
    }

    public Mtop getMtopInstance() {
        return this.mtopInstance;
    }

    public MtopBuilder retryTime(int retryTimes) {
        this.mtopProp.retryTimes = retryTimes;
        return this;
    }

    public MtopBuilder headers(Map<String, String> requestHeaders) {
        if (requestHeaders != null && !requestHeaders.isEmpty()) {
            if (this.mtopProp.requestHeaders != null) {
                this.mtopProp.requestHeaders.putAll(requestHeaders);
            } else {
                this.mtopProp.requestHeaders = requestHeaders;
            }
        }
        return this;
    }

    public MtopBuilder setCacheControlNoCache() {
        Map<String, String> requestHeaders = this.mtopProp.requestHeaders;
        if (requestHeaders == null) {
            requestHeaders = new HashMap<>();
        }
        requestHeaders.put("cache-control", HttpHeaderConstant.NO_CACHE);
        this.mtopProp.requestHeaders = requestHeaders;
        return this;
    }

    public MtopBuilder protocol(ProtocolEnum protocol) {
        if (protocol != null) {
            this.mtopProp.protocol = protocol;
        }
        return this;
    }

    public MtopBuilder setCustomDomain(String customDomain) {
        if (customDomain != null) {
            this.mtopProp.customDomain = customDomain;
        }
        return this;
    }

    public MtopBuilder setCustomDomain(String onlineDomain, String preDomain, String dailyDomain) {
        if (StringUtils.isNotBlank(onlineDomain)) {
            this.mtopProp.customOnlineDomain = onlineDomain;
        }
        if (StringUtils.isNotBlank(preDomain)) {
            this.mtopProp.customPreDomain = preDomain;
        }
        if (StringUtils.isNotBlank(dailyDomain)) {
            this.mtopProp.customDailyDomain = dailyDomain;
        }
        return this;
    }

    public MtopBuilder setUnitStrategy(String unitStrategy) {
        if (unitStrategy != null) {
            char c = 65535;
            switch (unitStrategy.hashCode()) {
                case -366328735:
                    if (unitStrategy.equals(MtopUnitStrategy.UNIT_GUIDE)) {
                        c = 1;
                        break;
                    }
                    break;
                case -354420023:
                    if (unitStrategy.equals(MtopUnitStrategy.UNIT_TRADE)) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    setCustomDomain(MtopUnitStrategy.TRADE_ONLINE_DOMAIN, MtopUnitStrategy.TRADE_PRE_DOMAIN, MtopUnitStrategy.TRADE_DAILY_DOMAIN);
                    break;
                case 1:
                    setCustomDomain(MtopUnitStrategy.GUIDE_ONLINE_DOMAIN, MtopUnitStrategy.GUIDE_PRE_DOMAIN, MtopUnitStrategy.GUIDE_DAILY_DOMAIN);
                    break;
            }
        }
        return this;
    }

    public MtopBuilder addListener(MtopListener listener2) {
        this.listener = listener2;
        return this;
    }

    public MtopBuilder setNetInfo(int param) {
        this.mtopProp.netParam = param;
        return this;
    }

    public MtopBuilder addMteeUa(String mteeUa) {
        addHttpQueryParameter("ua", mteeUa);
        return this;
    }

    public MtopBuilder addHttpQueryParameter(String key, String value) {
        if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
            if (this.mtopProp.queryParameterMap == null) {
                this.mtopProp.queryParameterMap = new HashMap();
            }
            this.mtopProp.queryParameterMap.put(key, value);
        } else if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, "[addHttpQueryParameter]add HttpQueryParameter error,key=" + key + ",value=" + value);
        }
        return this;
    }

    public MtopBuilder handler(Handler handler) {
        this.mtopProp.handler = handler;
        return this;
    }

    public MtopBuilder useCache() {
        this.mtopProp.useCache = true;
        return this;
    }

    public MtopBuilder forceRefreshCache() {
        this.mtopProp.forceRefreshCache = true;
        return this;
    }

    public MtopBuilder useWua() {
        return useWua(4);
    }

    @Deprecated
    public MtopBuilder useWua(int type) {
        this.mtopProp.wuaFlag = type;
        return this;
    }

    public MtopBuilder reqMethod(MethodEnum method) {
        if (method != null) {
            this.mtopProp.method = method;
        }
        return this;
    }

    public MtopBuilder addCacheKeyParamBlackList(List<String> blackList) {
        if (blackList != null) {
            this.mtopProp.cacheKeyBlackList = blackList;
        }
        return this;
    }

    public MtopBuilder setJsonType(JsonTypeEnum jsonTypeEnum) {
        if (jsonTypeEnum != null) {
            addHttpQueryParameter("type", jsonTypeEnum.getJsonType());
        }
        return this;
    }

    public MtopBuilder addOpenApiParams(String openAppKey, String accessToken) {
        this.mtopProp.apiType = ApiTypeEnum.ISV_OPEN_API;
        this.mtopProp.openAppKey = openAppKey;
        this.mtopProp.accessToken = accessToken;
        return this;
    }

    public MtopBuilder setConnectionTimeoutMilliSecond(int connTimeout) {
        if (connTimeout > 0) {
            this.mtopProp.connTimeout = connTimeout;
        }
        return this;
    }

    public MtopBuilder setSocketTimeoutMilliSecond(int socketTimeout) {
        if (socketTimeout > 0) {
            this.mtopProp.socketTimeout = socketTimeout;
        }
        return this;
    }

    public MtopBuilder setBizId(int bizId) {
        this.mtopProp.bizId = bizId;
        return this;
    }

    public MtopBuilder setReqBizExt(String reqBizExt) {
        this.mtopProp.reqBizExt = reqBizExt;
        return this;
    }

    public MtopBuilder setReqUserId(String reqUserId) {
        this.mtopProp.reqUserId = reqUserId;
        return this;
    }

    public MtopBuilder setReqAppKey(String reqAppKey, String authCode) {
        this.mtopProp.reqAppKey = reqAppKey;
        this.mtopProp.authCode = authCode;
        return this;
    }

    public MtopBuilder setPageUrl(String pageUrl) {
        if (pageUrl != null) {
            this.mtopProp.pageUrl = MtopUtils.convertUrl(pageUrl);
            this.stat.pageUrl = this.mtopProp.pageUrl;
        }
        return this;
    }

    public MtopBuilder setReqSource(int reqSource) {
        this.mtopProp.reqSource = reqSource;
        return this;
    }

    public MtopBuilder enableProgressListener() {
        this.mtopProp.enableProgressListener = true;
        return this;
    }

    public MtopResponse syncRequest() {
        MtopBaseListenerProxy callback = createListenerProxy(this.listener);
        asyncRequest(callback);
        synchronized (callback) {
            try {
                if (callback.response == null) {
                    callback.wait(60000);
                }
            } catch (Exception e) {
                TBSdkLog.e(TAG, "[syncRequest] callback wait error", (Throwable) e);
            }
        }
        MtopResponse mtopResponse = callback.response;
        if (callback.reqContext != null) {
            this.mtopProp.reqContext = callback.reqContext;
        }
        return mtopResponse != null ? mtopResponse : handleAsyncTimeoutException();
    }

    /* access modifiers changed from: protected */
    public MtopResponse handleAsyncTimeoutException() {
        MtopResponse mtopResponse = new MtopResponse(this.request.getApiName(), this.request.getVersion(), ErrorConstant.ERRCODE_MTOP_APICALL_ASYNC_TIMEOUT, ErrorConstant.MappingMsg.SERVICE_MAPPING_MSG);
        mtopResponse.mappingCodeSuffix = ErrorConstant.getMappingCodeByErrorCode(mtopResponse.getRetCode());
        mtopResponse.mappingCode = ErrorConstant.appendMappingCode(mtopResponse.getResponseCode(), mtopResponse.mappingCodeSuffix);
        this.stat.retCode = mtopResponse.getRetCode();
        this.stat.mappingCode = mtopResponse.getMappingCode();
        this.stat.retType = 2;
        mtopResponse.setMtopStat(this.stat);
        this.stat.onEndAndCommit();
        return mtopResponse;
    }

    private ApiID asyncRequest(MtopListener listener2) {
        this.stat.startTime = this.stat.currentTimeMillis();
        final MtopContext mtopContext = createMtopContext(listener2);
        mtopContext.apiId = new ApiID((Call) null, mtopContext);
        try {
            if (MtopUtils.isMainThread() || !this.mtopInstance.isInited()) {
                MtopSDKThreadPoolExecutorFactory.getRequestThreadPoolExecutor().submit(new Runnable() {
                    public void run() {
                        mtopContext.stats.startExecuteTime = MtopBuilder.this.stat.currentTimeMillis();
                        MtopBuilder.this.mtopInstance.checkMtopSDKInit();
                        FilterManager filterManager = MtopBuilder.this.mtopInstance.getMtopConfig().filterManager;
                        if (filterManager != null) {
                            filterManager.start((String) null, mtopContext);
                        }
                        FilterUtils.checkFilterManager(filterManager, mtopContext);
                    }
                });
            } else {
                FilterManager filterManager = this.mtopInstance.getMtopConfig().filterManager;
                if (filterManager != null) {
                    filterManager.start((String) null, mtopContext);
                }
                FilterUtils.checkFilterManager(filterManager, mtopContext);
            }
            return mtopContext.apiId;
        } catch (Throwable th) {
            return mtopContext.apiId;
        }
    }

    public ApiID asyncRequest() {
        return asyncRequest(this.listener);
    }

    public MtopContext createMtopContext(MtopListener listener2) {
        MtopContext mtopContext = new MtopContext();
        mtopContext.mtopInstance = this.mtopInstance;
        mtopContext.stats = this.stat;
        mtopContext.seqNo = this.stat.seqNo;
        mtopContext.mtopRequest = this.request;
        mtopContext.property = this.mtopProp;
        mtopContext.mtopListener = listener2;
        mtopContext.mtopBuilder = this;
        if (this.request != null) {
            this.stat.apiKey = this.request.getKey();
        }
        if (StringUtils.isBlank(mtopContext.property.ttid)) {
            mtopContext.property.ttid = this.mtopInstance.getTtid();
        }
        if (this.requestContext != null) {
            reqContext(this.requestContext);
        }
        return mtopContext;
    }

    private MtopBaseListenerProxy createListenerProxy(MtopListener listener2) {
        if (listener2 == null) {
            return new MtopBaseListenerProxy(new DefaultMtopCallback());
        }
        if (listener2 instanceof MtopCallback.MtopCacheListener) {
            return new MtopCacheListenerProxy(listener2);
        }
        return new MtopBaseListenerProxy(listener2);
    }

    /* access modifiers changed from: protected */
    public void mtopCommitStatData(boolean commitStat) {
        this.stat.commitStat = commitStat;
    }

    public MtopBuilder setUserInfo(@Nullable String userInfo) {
        MtopNetworkProp mtopNetworkProp = this.mtopProp;
        if (StringUtils.isBlank(userInfo)) {
            userInfo = NameSpaceDO.LEVEL_DEFAULT;
        }
        mtopNetworkProp.userInfo = userInfo;
        return this;
    }
}
