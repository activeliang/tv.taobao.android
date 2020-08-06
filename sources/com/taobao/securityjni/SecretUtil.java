package com.taobao.securityjni;

import android.content.ContextWrapper;
import com.taobao.atlas.dexmerge.dx.io.Opcodes;
import com.taobao.securityjni.tools.DataContext;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.SecurityGuardParamContext;
import com.taobao.wireless.security.sdk.indiekit.IIndieKitComponent;
import com.taobao.wireless.security.sdk.indiekit.IndieKitDefine;
import com.taobao.wireless.security.sdk.securesignature.ISecureSignatureComponent;
import com.taobao.wireless.security.sdk.securesignature.SecureSignatureDefine;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

@Deprecated
public class SecretUtil {
    public static final String M_API = "API";
    public static final String M_DATA = "DATA";
    public static final String M_DEV = "DEV";
    public static final String M_ECODE = "ECODE";
    public static final String M_IMEI = "IMEI";
    public static final String M_IMSI = "IMSI";
    public static final String M_SSO = "SSO";
    public static final String M_TIME = "TIME";
    public static final String M_V = "V";
    private IIndieKitComponent indieKitProxy;
    private SecurityGuardManager manager;
    private ISecureSignatureComponent signProxy;

    public SecretUtil(ContextWrapper context) {
        this.manager = SecurityGuardManager.getInstance(context);
        if (this.manager != null) {
            this.signProxy = this.manager.getSecureSignatureComp();
            this.indieKitProxy = this.manager.getIndieKitComp();
        }
    }

    public String signRequest(SecurityGuardParamContext paramContext) {
        if (this.signProxy == null) {
            return null;
        }
        return this.signProxy.signRequest(paramContext);
    }

    public String getSign(String api, String v, String imei, String imsi, String data, String time) {
        return getSign(api, v, imei, imsi, data, (String) null, time);
    }

    public String getSign(String api, String v, String imei, String imsi, String data, String ecode, String time) {
        if (api == null || v == null || imei == null || imsi == null || time == null) {
            return null;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("API", api);
        map.put("V", v);
        map.put("IMEI", imei);
        map.put("IMSI", imsi);
        if (data != null) {
            map.put("DATA", data);
        }
        if (ecode != null) {
            map.put("ECODE", ecode);
        }
        map.put("TIME", time);
        return getSign(map, new DataContext(0, (byte[]) null));
    }

    public String getSign(HashMap<String, String> map, DataContext ctx) {
        return getMtopSign(map, ctx);
    }

    public String getMtopSign(HashMap<String, String> map, DataContext ctx) {
        if (this.signProxy == null || map == null || ctx == null) {
            return null;
        }
        SecurityGuardParamContext signParam = new SecurityGuardParamContext();
        signParam.paramMap = map;
        signParam.requestType = 3;
        if (ctx.extData == null) {
            ctx.index = ctx.index < 0 ? 0 : ctx.index;
            String appKey = this.manager.getStaticDataStoreComp().getAppKeyByIndex(ctx.index);
            if (appKey == null || "".equals(appKey)) {
                return null;
            }
            signParam.appKey = appKey;
        } else if (ctx.extData.length == 0) {
            return null;
        } else {
            signParam.appKey = new String(ctx.extData);
        }
        return this.signProxy.signRequest(signParam);
    }

    public String getLoginTopToken(String userName, String time) {
        return getLoginTopToken(userName, time, new DataContext(0, (byte[]) null));
    }

    public String getLoginTopToken(String userName, String time, DataContext ctx) {
        int i = 0;
        if (this.indieKitProxy == null || userName == null || time == null || ctx == null) {
            return null;
        }
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put(IndieKitDefine.SG_KEY_INDIE_KIT_USERNAME, userName);
        paramMap.put("timestamp", time);
        SecurityGuardParamContext paramContext = new SecurityGuardParamContext();
        paramContext.paramMap = paramMap;
        paramContext.requestType = 0;
        if (ctx.extData == null) {
            if (ctx.index >= 0) {
                i = ctx.index;
            }
            ctx.index = i;
            String appKey = this.manager.getStaticDataStoreComp().getAppKeyByIndex(ctx.index);
            if (appKey == null || "".equals(appKey)) {
                return null;
            }
            paramContext.appKey = appKey;
        } else if (ctx.extData.length == 0) {
            return null;
        } else {
            paramContext.appKey = new String(ctx.extData);
        }
        return this.indieKitProxy.indieKitRequest(paramContext);
    }

    public String getTopSign(TreeMap<String, String> params) {
        return getTopSign(params, new DataContext(0, (byte[]) null));
    }

    public String getTopSign(TreeMap<String, String> params, DataContext ctx) {
        if (this.signProxy == null || params == null || params.isEmpty()) {
            return null;
        }
        StringBuilder orgin = new StringBuilder(512);
        for (String name : params.keySet()) {
            String value = params.get(name);
            if (value != null) {
                orgin.append(name).append(value);
            } else {
                orgin.append(name);
            }
        }
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("INPUT", orgin.toString());
        SecurityGuardParamContext signParam = new SecurityGuardParamContext();
        signParam.paramMap = paramMap;
        signParam.requestType = 2;
        if (ctx.extData == null) {
            ctx.index = ctx.index < 0 ? 0 : ctx.index;
            String appKey = this.manager.getStaticDataStoreComp().getAppKeyByIndex(ctx.index);
            if (appKey == null || "".equals(appKey)) {
                return null;
            }
            signParam.appKey = appKey;
        } else if (ctx.extData.length == 0) {
            return null;
        } else {
            signParam.appKey = new String(ctx.extData);
        }
        return this.signProxy.signRequest(signParam);
    }

    public String getExternalSign(LinkedHashMap<String, String> params, DataContext ctx) {
        int i = 0;
        String result = null;
        if (this.signProxy == null || params == null || params.isEmpty() || ctx == null) {
            return null;
        }
        int signType = -1;
        switch (ctx.category) {
            case 0:
                if (ctx.type == 0) {
                    signType = 10;
                    break;
                }
                break;
            case 1:
                if (ctx.type == 0) {
                    signType = 11;
                    break;
                }
                break;
            case 2:
                if (ctx.type == 0) {
                    signType = 12;
                    break;
                }
                break;
            case 3:
                if (ctx.type == 0) {
                    signType = 8;
                    break;
                }
                break;
            case 4:
                if (ctx.type == 0) {
                    signType = 14;
                    break;
                }
                break;
        }
        if (signType != -1) {
            StringBuilder origin = new StringBuilder(Opcodes.FILL_ARRAY_DATA_PAYLOAD);
            for (String name : params.keySet()) {
                if (name != null) {
                    String value = params.get(name);
                    if (value != null) {
                        origin.append(name).append('=').append(value);
                    } else {
                        origin.append(name);
                    }
                    origin.append('&');
                }
            }
            if (origin.length() < 1) {
                return null;
            }
            HashMap<String, String> paramMap = new HashMap<>();
            paramMap.put("INPUT", origin.substring(0, origin.length() - 1));
            SecurityGuardParamContext signParam = new SecurityGuardParamContext();
            signParam.paramMap = paramMap;
            signParam.requestType = signType;
            if (ctx.extData == null) {
                if (ctx.index >= 0) {
                    i = ctx.index;
                }
                ctx.index = i;
                String appKey = this.manager.getStaticDataStoreComp().getAppKeyByIndex(ctx.index);
                if (appKey == null || "".equals(appKey)) {
                    return null;
                }
                signParam.appKey = appKey;
            } else if (ctx.extData.length == 0) {
                return null;
            } else {
                signParam.appKey = new String(ctx.extData);
            }
            result = this.signProxy.signRequest(signParam);
        }
        return result;
    }

    public String getQianNiuSign(byte[] str1, byte[] str2) {
        if (this.signProxy == null || str1 == null || str2 == null) {
            return null;
        }
        HashMap<String, String> params = new HashMap<>();
        String param1 = new String(str1);
        String param2 = new String(str2);
        params.put(SecureSignatureDefine.SG_KEY_SIGN_STR1, param1);
        params.put(SecureSignatureDefine.SG_KEY_SIGN_STR2, param2);
        SecurityGuardParamContext signParam = new SecurityGuardParamContext();
        signParam.paramMap = params;
        signParam.requestType = 9;
        return this.signProxy.signRequest(signParam);
    }

    public String getMtopV4Sign(String ecode, String data, String t, String api, String v, String sid, String ttid, String deviceId, String lat, String lng, DataContext ctx) {
        if (this.signProxy == null || ctx == null) {
            return null;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("ECODE", ecode);
        params.put("DATA", data);
        params.put("TIME", t);
        params.put("API", api);
        params.put("V", v);
        params.put(SecureSignatureDefine.SG_KEY_SIGN_SID, sid);
        params.put(SecureSignatureDefine.SG_KEY_SIGN_TTID, ttid);
        params.put(SecureSignatureDefine.SG_KEY_SIGN_DEVICDEID, deviceId);
        params.put(SecureSignatureDefine.SG_KEY_SIGN_LAT, lat);
        params.put(SecureSignatureDefine.SG_KEY_SIGN_LNG, lng);
        SecurityGuardParamContext signParam = new SecurityGuardParamContext();
        signParam.paramMap = params;
        signParam.requestType = 4;
        if (ctx.extData == null) {
            ctx.index = ctx.index < 0 ? 0 : ctx.index;
            String appKey = this.manager.getStaticDataStoreComp().getAppKeyByIndex(ctx.index);
            if (appKey == null || "".equals(appKey)) {
                return null;
            }
            signParam.appKey = appKey;
        } else if (ctx.extData.length == 0) {
            return null;
        } else {
            signParam.appKey = new String(ctx.extData);
        }
        return this.signProxy.signRequest(signParam);
    }

    public String getMtopV4RespSign(String output, DataContext ctx) {
        if (this.signProxy == null || ctx == null) {
            return null;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("INPUT", output);
        SecurityGuardParamContext signParam = new SecurityGuardParamContext();
        signParam.paramMap = params;
        signParam.requestType = 5;
        if (ctx.extData == null) {
            ctx.index = ctx.index < 0 ? 0 : ctx.index;
            String appKey = this.manager.getStaticDataStoreComp().getAppKeyByIndex(ctx.index);
            if (appKey == null || "".equals(appKey)) {
                return null;
            }
            signParam.appKey = appKey;
        } else if (ctx.extData.length == 0) {
            return null;
        } else {
            signParam.appKey = new String(ctx.extData);
        }
        return this.signProxy.signRequest(signParam);
    }

    public String getLaiwangSign(String input, String encryptedAppSecret, DataContext ctx) {
        if (this.signProxy == null || ctx == null) {
            return null;
        }
        HashMap<String, String> params = new HashMap<>();
        params.put("INPUT", input);
        params.put(SecureSignatureDefine.SG_KEY_SIGN_KEY, encryptedAppSecret);
        SecurityGuardParamContext signParam = new SecurityGuardParamContext();
        signParam.paramMap = params;
        signParam.requestType = 7;
        if (ctx.extData == null) {
            ctx.index = ctx.index < 0 ? 0 : ctx.index;
            String appKey = this.manager.getStaticDataStoreComp().getAppKeyByIndex(ctx.index);
            if (appKey == null || "".equals(appKey)) {
                return null;
            }
            signParam.appKey = appKey;
        } else if (ctx.extData.length == 0) {
            return null;
        } else {
            signParam.appKey = new String(ctx.extData);
        }
        return this.signProxy.signRequest(signParam);
    }

    public String indieKitRequest(SecurityGuardParamContext paramContext) {
        if (this.indieKitProxy == null) {
            return null;
        }
        return this.indieKitProxy.indieKitRequest(paramContext);
    }

    public int validateFileSignature(String fileSignature, String fileHash, String secretKey) {
        return -1;
    }

    public int reportSusText(String text, String extraKey) {
        throw new UnsupportedOperationException();
    }
}
