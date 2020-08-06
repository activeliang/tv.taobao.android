package mtopsdk.mtop.common;

import android.os.Handler;
import com.taobao.orange.model.NameSpaceDO;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import mtopsdk.mtop.domain.ApiTypeEnum;
import mtopsdk.mtop.domain.EnvModeEnum;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.ProtocolEnum;
import mtopsdk.mtop.global.SwitchConfig;

public class MtopNetworkProp implements Serializable {
    private static final long serialVersionUID = -3528337805304245196L;
    public String accessToken;
    public ApiTypeEnum apiType;
    public String authCode;
    public boolean autoRedirect = true;
    public boolean backGround;
    public int bizId;
    public List<String> cacheKeyBlackList = null;
    public String clientTraceId;
    public int connTimeout = 10000;
    public String customDailyDomain;
    public String customDomain;
    public String customOnlineDomain;
    public String customPreDomain;
    public boolean enableProgressListener;
    public EnvModeEnum envMode = EnvModeEnum.ONLINE;
    public boolean forceRefreshCache = false;
    public Handler handler;
    public boolean isInnerOpen;
    public MethodEnum method = MethodEnum.GET;
    public int netParam;
    public String openAppKey = "DEFAULT_AUTH";
    public String pageName;
    public String pageUrl;
    public Map<String, String> priorityData = null;
    public boolean priorityFlag;
    public ProtocolEnum protocol = ProtocolEnum.HTTPSECURE;
    public Map<String, String> queryParameterMap;
    public String reqAppKey;
    public String reqBizExt;
    public Object reqContext = null;
    public int reqSource;
    public String reqUserId;
    public Map<String, String> requestHeaders;
    public int retryTimes = 1;
    public boolean skipCacheCallback = false;
    public int socketTimeout = 15000;
    public boolean timeCalibrated = false;
    public String ttid;
    public boolean useCache = false;
    public String userInfo = NameSpaceDO.LEVEL_DEFAULT;
    public int wuaFlag = -1;

    @Deprecated
    public ProtocolEnum getProtocol() {
        if (!SwitchConfig.getInstance().isGlobalSpdySslSwitchOpen()) {
            return ProtocolEnum.HTTP;
        }
        return this.protocol;
    }

    @Deprecated
    public void setProtocol(ProtocolEnum protocol2) {
        if (protocol2 != null) {
            this.protocol = protocol2;
        }
    }

    @Deprecated
    public MethodEnum getMethod() {
        return this.method;
    }

    @Deprecated
    public void setMethod(MethodEnum method2) {
        if (method2 != null) {
            this.method = method2;
        }
    }

    @Deprecated
    public Map<String, String> getRequestHeaders() {
        return this.requestHeaders;
    }

    @Deprecated
    public void setRequestHeaders(Map<String, String> requestHeaders2) {
        this.requestHeaders = requestHeaders2;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder(256);
        builder.append("MtopNetworkProp [ protocol=").append(this.protocol);
        builder.append(", method=").append(this.method);
        builder.append(", envMode=").append(this.envMode);
        builder.append(", autoRedirect=").append(this.autoRedirect);
        builder.append(", retryTimes=").append(this.retryTimes);
        builder.append(", requestHeaders=").append(this.requestHeaders);
        builder.append(", timeCalibrated=").append(this.timeCalibrated);
        builder.append(", ttid=").append(this.ttid);
        builder.append(", useCache=").append(this.useCache);
        builder.append(", forceRefreshCache=").append(this.forceRefreshCache);
        builder.append(", cacheKeyBlackList=").append(this.cacheKeyBlackList);
        if (this.apiType != null) {
            builder.append(", apiType=").append(this.apiType.getApiType());
            builder.append(", openAppKey=").append(this.openAppKey);
            builder.append(", accessToken=").append(this.accessToken);
        }
        builder.append(", queryParameterMap=").append(this.queryParameterMap);
        builder.append(", connTimeout=").append(this.connTimeout);
        builder.append(", socketTimeout=").append(this.socketTimeout);
        builder.append(", bizId=").append(this.bizId);
        builder.append(", reqBizExt=").append(this.reqBizExt);
        builder.append(", reqUserId=").append(this.reqUserId);
        builder.append(", reqAppKey=").append(this.reqAppKey);
        builder.append(", authCode=").append(this.authCode);
        builder.append(", clientTraceId =").append(this.clientTraceId);
        builder.append(", netParam=").append(this.netParam);
        builder.append(", reqSource=").append(this.reqSource);
        builder.append("]");
        return builder.toString();
    }
}
