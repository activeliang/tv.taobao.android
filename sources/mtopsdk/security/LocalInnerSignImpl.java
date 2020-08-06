package mtopsdk.security;

import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.security.ISign;
import mtopsdk.security.util.HmacSha1Utils;
import mtopsdk.security.util.SecurityUtils;
import mtopsdk.xstate.util.XStateConstants;

public class LocalInnerSignImpl extends AbstractSignImpl {
    private static final String TAG = "mtopsdk.LocalInnerSignImpl";
    String appKey;
    String appSecret;

    public LocalInnerSignImpl(String appKey2, String appSecret2) {
        this.appKey = appKey2;
        this.appSecret = appSecret2;
    }

    public String getAppKey(ISign.SignCtx ctx) {
        return this.appKey;
    }

    public String getMtopApiSign(HashMap<String, String> params, String appKey2, String authCode) {
        String instanceId = getInstanceId();
        if (params == null) {
            TBSdkLog.e(TAG, instanceId + " [getMtopApiSign] params is null.appKey=" + appKey2);
            return null;
        } else if (appKey2 == null) {
            params.put(XStateConstants.KEY_SG_ERROR_CODE, "AppKey is null");
            TBSdkLog.e(TAG, instanceId + " [getMtopApiSign] AppKey is null.");
            return null;
        } else {
            try {
                return getCommonHmacSha1Sign(convertInnerBaseStrMap(params, appKey2), appKey2);
            } catch (Exception e) {
                TBSdkLog.e(TAG, instanceId + " [getMtopApiSign] ISecureSignatureComponent signRequest error", (Throwable) e);
                return null;
            }
        }
    }

    private String convertInnerBaseStrMap(Map<String, String> originMap, String appkey) {
        if (originMap == null || originMap.size() < 1) {
            return null;
        }
        String ext = originMap.get("extdata");
        String x_features = originMap.get("x-features");
        StringBuilder baseStr = new StringBuilder(64);
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get("utdid"))).append("&");
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get(XStateConstants.KEY_UID))).append("&");
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get(XStateConstants.KEY_REQBIZ_EXT))).append("&");
        baseStr.append(appkey).append("&");
        baseStr.append(SecurityUtils.getMd5(originMap.get("data"))).append("&");
        baseStr.append(originMap.get("t")).append("&");
        baseStr.append(originMap.get("api")).append("&");
        baseStr.append(originMap.get("v")).append("&");
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get("sid"))).append("&");
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get("ttid"))).append("&");
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get("deviceId"))).append("&");
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get("lat"))).append("&");
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get("lng"))).append("&");
        if (StringUtils.isNotBlank(ext)) {
            baseStr.append(ext).append("&");
        }
        baseStr.append(x_features);
        return baseStr.toString();
    }

    public String getCommonHmacSha1Sign(String baseStr, String appKey2) {
        String instanceId = getInstanceId();
        if (StringUtils.isBlank(baseStr)) {
            TBSdkLog.e(TAG, instanceId + " [getCommonHmacSha1Sign] baseStr is null.appKey=" + appKey2);
            return null;
        } else if (appKey2 != null && appKey2.equals(this.appKey)) {
            return HmacSha1Utils.hmacSha1Hex(baseStr, this.appSecret);
        } else {
            StringBuilder builder = new StringBuilder(64);
            builder.append(instanceId).append(" [getCommonHmacSha1Sign] request appKey mismatches global appKey.requestAppKey=");
            builder.append(appKey2).append(",globalAppKey=").append(this.appKey);
            TBSdkLog.e(TAG, builder.toString());
            return null;
        }
    }

    public String getSecBodyDataEx(String time, String appKey2, String authCode, int flag) {
        return null;
    }
}
