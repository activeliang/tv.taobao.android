package com.yunos.tv.core.security;

import android.content.Context;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.SecurityGuardParamContext;
import com.taobao.wireless.security.sdk.dynamicdataencrypt.IDynamicDataEncryptComponent;
import com.taobao.wireless.security.sdk.securesignature.ISecureSignatureComponent;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.HashMap;
import java.util.Map;

public class SecureSigner {
    private static SecureSigner signerInstance_;
    private Context context_;
    private SecurityGuardManager securityGuardManager_;

    private SecureSigner(Context ctx) {
        this.context_ = ctx;
        this.securityGuardManager_ = SecurityGuardManager.getInstance(ctx);
    }

    public static SecureSigner getInstance(Context ctx) {
        if (signerInstance_ == null) {
            signerInstance_ = new SecureSigner(ctx);
        }
        return signerInstance_;
    }

    public final String getMtopSign(String appkey, String api, String v, String imei, String imsi, String data, String t, String ecode) {
        return getSignInner(this.securityGuardManager_, getParamMap(api, v, imei, imsi, data, t, ecode), appkey, 3);
    }

    public final String getTopSign(String appkey, Map<String, String> map) {
        return getSignInner(this.securityGuardManager_, map, appkey, 2);
    }

    public final String getMTopSign(String appkey, Map<String, String> map) {
        return getSignInner(this.securityGuardManager_, map, appkey, 3);
    }

    public final String getHttpSign(String appkey, Map<String, String> map) {
        return getSignInner(this.securityGuardManager_, map, appkey, 10);
    }

    public static String getMtopSign(Context ctx, String appkey, String api, String v, String imei, String imsi, String data, String t, String ecode) {
        return getSignInner(SecurityGuardManager.getInstance(ctx), getParamMap(api, v, imei, imsi, data, t, ecode), appkey, 3);
    }

    public String dynamicEncrypt(String plainText) {
        String encStr;
        IDynamicDataEncryptComponent dynamicDataEncryptComponent = this.securityGuardManager_.getDynamicDataEncryptComp();
        if (dynamicDataEncryptComponent == null || (encStr = dynamicDataEncryptComponent.dynamicEncrypt(plainText)) == null) {
            return "";
        }
        return encStr;
    }

    public String dynamicDecrypt(String cipherText) {
        String origStr;
        IDynamicDataEncryptComponent dynamicDataEncryptComponent = this.securityGuardManager_.getDynamicDataEncryptComp();
        if (dynamicDataEncryptComponent == null || (origStr = dynamicDataEncryptComponent.dynamicDecrypt(cipherText)) == null) {
            return "";
        }
        return origStr;
    }

    private static Map<String, String> getParamMap(String api, String v, String imei, String imsi, String data, String t, String ecode) {
        Map<String, String> argMap = new HashMap<>();
        argMap.put("V", v);
        argMap.put("IMEI", imei);
        argMap.put("IMSI", imsi);
        argMap.put("TIME", t);
        argMap.put("API", api);
        argMap.put("DATA", data);
        if (ecode != null) {
            argMap.put("ECODE", ecode);
        }
        return argMap;
    }

    private static String getSignInner(SecurityGuardManager sgManager, Map<String, String> map, String appkey, int requestType) {
        ISecureSignatureComponent ssComp;
        if (sgManager == null || (ssComp = sgManager.getSecureSignatureComp()) == null) {
            return "";
        }
        SecurityGuardParamContext sgc = new SecurityGuardParamContext();
        sgc.appKey = appkey;
        sgc.paramMap = map;
        sgc.requestType = requestType;
        ZpLogger.i("Mtop", "appkey: " + appkey + "params: " + map + "request " + sgc.requestType);
        String ssStr = ssComp.signRequest(sgc);
        ZpLogger.i("Mtop", "get sign is : " + ssStr);
        if (ssStr != null) {
            return ssStr;
        }
        return "";
    }
}
