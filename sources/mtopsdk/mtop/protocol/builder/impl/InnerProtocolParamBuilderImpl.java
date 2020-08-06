package mtopsdk.mtop.protocol.builder.impl;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import mtopsdk.common.util.HttpHeaderConstant;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.SymbolExpUtil;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.framework.domain.MtopContext;
import mtopsdk.mtop.common.MtopNetworkProp;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.features.MtopFeatureManager;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.global.SDKUtils;
import mtopsdk.mtop.intf.Mtop;
import mtopsdk.mtop.network.NetParam;
import mtopsdk.mtop.protocol.builder.ProtocolParamBuilder;
import mtopsdk.security.ISign;
import mtopsdk.security.LocalInnerSignImpl;
import mtopsdk.xstate.XState;
import mtopsdk.xstate.network.NetworkStateReceiver;
import mtopsdk.xstate.util.XStateConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class InnerProtocolParamBuilderImpl implements ProtocolParamBuilder {
    private static final String TAG = "mtopsdk.InnerProtocolParamBuilderImpl";
    private MtopConfig mtopConfig = null;

    public Map<String, String> buildParams(MtopContext mtopContext) {
        String userId;
        long startTime = System.currentTimeMillis();
        Mtop mtopInstance = mtopContext.mtopInstance;
        this.mtopConfig = mtopInstance.getMtopConfig();
        ISign signGenerator = this.mtopConfig.sign;
        if (signGenerator == null) {
            TBSdkLog.e(TAG, mtopContext.seqNo, "ISign of mtopConfig in mtopInstance is null");
            return null;
        }
        MtopRequest mtopRequest = mtopContext.mtopRequest;
        MtopNetworkProp mtopProperty = mtopContext.property;
        HashMap hashMap = new HashMap(32);
        hashMap.put("utdid", mtopInstance.getUtdid());
        if (StringUtils.isNotBlank(mtopProperty.reqUserId)) {
            userId = mtopProperty.reqUserId;
        } else {
            userId = mtopInstance.getMultiAccountUserId(mtopProperty.userInfo);
        }
        hashMap.put(XStateConstants.KEY_UID, userId);
        if (StringUtils.isNotBlank(mtopProperty.reqBizExt)) {
            hashMap.put(XStateConstants.KEY_REQBIZ_EXT, mtopProperty.reqBizExt);
        }
        if (StringUtils.isBlank(mtopProperty.reqAppKey)) {
            mtopProperty.reqAppKey = this.mtopConfig.appKey;
            mtopProperty.authCode = this.mtopConfig.authCode;
        }
        String appKey = mtopProperty.reqAppKey;
        String authCode = mtopProperty.authCode;
        hashMap.put("appKey", appKey);
        String data = mtopRequest.getData();
        if (mtopProperty.priorityFlag && mtopProperty.priorityData != null) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                jsonObject.putOpt(HttpHeaderConstant.X_PRIORITY_DATA, JSON.toJSONString(mtopProperty.priorityData));
                data = jsonObject.toString();
            } catch (Exception e) {
                TBSdkLog.e(TAG, mtopContext.seqNo, "set api priority data error, priorityData:" + mtopProperty.priorityData, e);
            }
        }
        hashMap.put("data", data);
        String time = String.valueOf(SDKUtils.getCorrectionTime());
        hashMap.put("t", time);
        hashMap.put("api", mtopRequest.getApiName().toLowerCase(Locale.US));
        hashMap.put("v", mtopRequest.getVersion().toLowerCase(Locale.US));
        hashMap.put("sid", mtopInstance.getMultiAccountSid(mtopProperty.userInfo));
        hashMap.put("ttid", mtopProperty.ttid);
        hashMap.put("deviceId", mtopInstance.getDeviceId());
        String lat = XState.getValue("lat");
        if (StringUtils.isNotBlank(lat)) {
            String lng = XState.getValue("lng");
            if (StringUtils.isNotBlank(lng)) {
                hashMap.put("lat", lat);
                hashMap.put("lng", lng);
            }
        }
        long x_features = MtopFeatureManager.getMtopTotalFeatures(mtopInstance);
        if (mtopProperty.reqSource > 0) {
            x_features |= MtopFeatureManager.getMtopFeatureValue(11);
        }
        if (mtopProperty.priorityFlag) {
            x_features |= MtopFeatureManager.getMtopFeatureValue(12);
        }
        hashMap.put("x-features", String.valueOf(x_features));
        if (mtopProperty.apiType != null) {
            if (mtopProperty.isInnerOpen) {
                mtopProperty.accessToken = XState.getValue(StringUtils.concatStr(mtopInstance.getInstanceId(), mtopProperty.openAppKey), XStateConstants.KEY_ACCESS_TOKEN);
            }
            hashMap.put(HttpHeaderConstant.KEY_EXTTYPE, mtopProperty.apiType.getApiType());
            StringBuilder extData = new StringBuilder(64);
            if (StringUtils.isNotBlank(mtopProperty.openAppKey)) {
                extData.append(HttpHeaderConstant.KEY_EXTDATA_OPENAPPKEY).append("=").append(mtopProperty.openAppKey);
            }
            if (StringUtils.isNotBlank(mtopProperty.accessToken)) {
                extData.append(SymbolExpUtil.SYMBOL_SEMICOLON).append(HttpHeaderConstant.KEY_EXTDATA_ACCESSTOKEN).append("=").append(mtopProperty.accessToken);
            }
            hashMap.put("extdata", extData.toString());
        }
        long startSignTime = System.currentTimeMillis();
        String sign = signGenerator.getMtopApiSign(hashMap, appKey, authCode);
        mtopContext.stats.computeSignTime = System.currentTimeMillis() - startSignTime;
        if (StringUtils.isBlank(sign)) {
            StringBuilder logBuf = new StringBuilder(128);
            logBuf.append("apiKey=").append(mtopRequest.getKey());
            logBuf.append(" call getMtopApiSign failed.[appKey=").append(appKey);
            logBuf.append(", authCode=").append(authCode).append("]");
            TBSdkLog.e(TAG, mtopContext.seqNo, logBuf.toString());
            return hashMap;
        }
        hashMap.put("sign", sign);
        if (!(signGenerator instanceof LocalInnerSignImpl)) {
            if (mtopProperty.wuaFlag >= 0) {
                long startWuaTime = System.currentTimeMillis();
                String wua = signGenerator.getAvmpSign(sign, authCode, mtopProperty.wuaFlag);
                mtopContext.stats.computeWuaTime = System.currentTimeMillis() - startWuaTime;
                hashMap.put("wua", wua);
                if (StringUtils.isBlank(wua)) {
                    TBSdkLog.e(TAG, mtopContext.seqNo, mtopRequest.getKey() + " call getAvmpSign for wua fail.");
                }
            }
            long startMiniWuaTime = System.currentTimeMillis();
            String mini_wua = signGenerator.getSecBodyDataEx(time, appKey, authCode, 8);
            mtopContext.stats.computeMiniWuaTime = System.currentTimeMillis() - startMiniWuaTime;
            hashMap.put(HttpHeaderConstant.X_MINI_WUA, mini_wua);
            if (StringUtils.isBlank(mini_wua)) {
                TBSdkLog.e(TAG, mtopContext.seqNo, mtopRequest.getKey() + " call getSecurityBodyDataEx for mini_wua failed.");
            }
        }
        buildExtParams(mtopContext, hashMap);
        mtopContext.stats.buildParamsTime = System.currentTimeMillis() - startTime;
        return hashMap;
    }

    private void buildExtParams(MtopContext mtopContext, Map<String, String> params) {
        MtopNetworkProp mtopProperty = mtopContext.property;
        params.put(XStateConstants.KEY_PV, XStateConstants.VALUE_INNER_PV);
        params.put("netType", XState.getValue("netType"));
        params.put(XStateConstants.KEY_NQ, XState.getValue(XStateConstants.KEY_NQ));
        params.put(XStateConstants.KEY_UMID_TOKEN, XState.getValue(mtopContext.mtopInstance.getInstanceId(), XStateConstants.KEY_UMID_TOKEN));
        String appVersion = this.mtopConfig.appVersion;
        if (StringUtils.isNotBlank(appVersion)) {
            params.put(HttpHeaderConstant.X_APP_VER, appVersion);
        }
        String xOrangeQ = this.mtopConfig.xOrangeQ;
        if (StringUtils.isNotBlank(xOrangeQ)) {
            params.put(HttpHeaderConstant.X_ORANGE_Q, xOrangeQ);
        }
        params.put(HttpHeaderConstant.X_APP_CONF_V, String.valueOf(this.mtopConfig.xAppConfigVersion));
        String userAgent = XState.getValue("ua");
        if (userAgent != null) {
            params.put("user-agent", userAgent);
        }
        params.put(HttpHeaderConstant.CLIENT_TRACE_ID, mtopProperty.clientTraceId);
        params.put("f-refer", "mtop");
        if (mtopProperty.netParam > 0) {
            JSONObject netInfo = new JSONObject();
            if ((mtopProperty.netParam & 1) != 0) {
                String wifiSSID = NetworkStateReceiver.ssid;
                if (!TextUtils.isEmpty(wifiSSID)) {
                    try {
                        netInfo.put(NetParam.NetParamKey.SSID, wifiSSID);
                    } catch (JSONException e) {
                        TBSdkLog.w(TAG, "set wifi ssid error.", (Throwable) e);
                    }
                }
            }
            if ((mtopProperty.netParam & 2) != 0) {
                String wifiBSSID = NetworkStateReceiver.bssid;
                if (!TextUtils.isEmpty(wifiBSSID)) {
                    try {
                        netInfo.put(NetParam.NetParamKey.BSSID, wifiBSSID);
                    } catch (JSONException e2) {
                        TBSdkLog.w(TAG, "set wifi bssid error.", (Throwable) e2);
                    }
                }
            }
            if (netInfo.length() > 0) {
                params.put(HttpHeaderConstant.X_NETINFO, netInfo.toString());
            }
        }
        if (mtopProperty.pageName != null) {
            params.put(HttpHeaderConstant.X_PAGE_NAME, mtopProperty.pageName);
        }
        if (mtopProperty.pageUrl != null) {
            params.put(HttpHeaderConstant.X_PAGE_URL, mtopProperty.pageUrl);
            String abtestParam = this.mtopConfig.mtopGlobalABTestParams.get(mtopProperty.pageUrl);
            if (abtestParam != null) {
                params.put(HttpHeaderConstant.X_PAGE_MAB, abtestParam);
            }
        }
    }
}
