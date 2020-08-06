package com.ali.user.open.ucc.data;

import android.os.Build;
import android.text.TextUtils;
import android.webkit.CookieManager;
import com.ali.auth.third.core.model.Constants;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.device.DeviceInfo;
import com.ali.user.open.core.model.RpcRequest;
import com.ali.user.open.core.model.RpcRequestCallbackWithCode;
import com.ali.user.open.core.service.RpcService;
import com.ali.user.open.core.service.StorageService;
import com.ali.user.open.core.util.CommonUtils;
import com.ali.user.open.core.util.JSONUtils;
import com.ali.user.open.core.util.SystemUtils;
import com.ali.user.open.device.DeviceTokenAccount;
import com.ali.user.open.device.DeviceTokenManager;
import com.ali.user.open.history.AccountHistoryManager;
import com.ali.user.open.history.HistoryAccount;
import com.ali.user.open.tbauth.TbAuthConstants;
import com.ali.user.open.ucc.context.UccContext;
import com.ali.user.open.ucc.data.ApiConstants;
import com.ali.user.open.ucc.model.FetchBindPageUrlResult;
import com.ali.user.open.ucc.model.UccParams;
import com.ali.user.open.ucc.util.RiskControlInfoContext;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mtopsdk.common.util.SymbolExpUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class DataRepository {
    public static void fetchBindPageUrl(UccParams uccParams, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = ApiConstants.ApiName.FETCH_AUTH_URL;
        rpcRequest.version = "1.0";
        JSONObject jsonObject = new JSONObject();
        try {
            buildBaseParam(uccParams, jsonObject);
            jsonObject.put("site", uccParams.site);
            jsonObject.put("userToken", uccParams.userToken);
            jsonObject.put("bindSite", uccParams.bindSite);
            jsonObject.put("scene", uccParams.scene);
            jsonObject.put(ApiConstants.ApiField.CREATE_BIND_SITE_SESSION, uccParams.createBindSiteSession);
            jsonObject.put("miniAppId", uccParams.miniAppId);
            jsonObject.put(ApiConstants.ApiField.SDK_TRACE_ID, uccParams.traceId);
            if (UccContext.getBizParams() != null) {
                JSONObject extObject = new JSONObject();
                for (Map.Entry<String, String> entry : UccContext.getBizParams().entrySet()) {
                    extObject.put(entry.getKey(), entry.getValue());
                }
                jsonObject.put("ext", extObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("request", jsonObject.toString());
        rpcRequest.addParam("riskControlInfo", RiskControlInfoContext.buildRiskControlInfo());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, FetchBindPageUrlResult.class, callback);
    }

    public static void fetchNewBindPageUrl(UccParams uccParams, String ibb, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = "mtop.alibaba.ucc.localauthurl.get.bytoken";
        rpcRequest.version = "1.0";
        JSONObject jsonObject = new JSONObject();
        try {
            buildBaseParam(uccParams, jsonObject);
            jsonObject.put("site", uccParams.site);
            jsonObject.put("bindUserToken", ibb);
            jsonObject.put(ApiConstants.ApiField.BIND_USER_TOKEN_TYPE, "IBB");
            jsonObject.put("bindSite", uccParams.bindSite);
            jsonObject.put("scene", uccParams.scene);
            jsonObject.put(ApiConstants.ApiField.CREATE_BIND_SITE_SESSION, uccParams.createBindSiteSession);
            jsonObject.put(ApiConstants.ApiField.SDK_TRACE_ID, uccParams.traceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("request", jsonObject.toString());
        rpcRequest.addParam("riskControlInfo", RiskControlInfoContext.buildRiskControlInfo());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, FetchBindPageUrlResult.class, callback);
    }

    public static void bindByNativeAuth(UccParams uccParams, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = ApiConstants.ApiName.BIND_NATIVE_AUTH;
        rpcRequest.version = "1.0";
        JSONObject jsonObject = new JSONObject();
        try {
            buildBaseParam(uccParams, jsonObject);
            jsonObject.put("site", uccParams.site);
            jsonObject.put("scene", uccParams.scene);
            jsonObject.put("userToken", uccParams.userToken);
            jsonObject.put("bindSite", uccParams.bindSite);
            jsonObject.put("bindUserToken", uccParams.bindUserToken);
            jsonObject.put(ApiConstants.ApiField.BIND_USER_TOKEN_TYPE, uccParams.bindUserTokenType);
            jsonObject.put(ApiConstants.ApiField.CREATE_BIND_SITE_SESSION, uccParams.createBindSiteSession);
            jsonObject.put("miniAppId", uccParams.miniAppId);
            jsonObject.put(ApiConstants.ApiField.SDK_TRACE_ID, uccParams.traceId);
            if (UccContext.getBizParams() != null) {
                JSONObject extObject = new JSONObject();
                for (Map.Entry<String, String> entry : UccContext.getBizParams().entrySet()) {
                    extObject.put(entry.getKey(), entry.getValue());
                }
                jsonObject.put("ext", extObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("request", jsonObject.toString());
        rpcRequest.addParam("riskControlInfo", RiskControlInfoContext.buildRiskControlInfo());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, String.class, callback);
    }

    public static void bindByRequestToken(UccParams uccParams, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = ApiConstants.ApiName.BIND_BY_REQUEST_TOKEN;
        rpcRequest.version = "1.0";
        JSONObject jsonObject = new JSONObject();
        try {
            buildBaseParam(uccParams, jsonObject);
            jsonObject.put("site", uccParams.site);
            jsonObject.put("scene", uccParams.scene);
            jsonObject.put("requestToken", uccParams.requestToken);
            jsonObject.put("bindSite", uccParams.bindSite);
            jsonObject.put("bindUserToken", uccParams.bindUserToken);
            jsonObject.put(ApiConstants.ApiField.BIND_USER_TOKEN_TYPE, uccParams.bindUserTokenType);
            jsonObject.put(ApiConstants.ApiField.CREATE_BIND_SITE_SESSION, uccParams.createBindSiteSession);
            jsonObject.put("miniAppId", uccParams.miniAppId);
            jsonObject.put(ApiConstants.ApiField.SDK_TRACE_ID, uccParams.traceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("request", jsonObject.toString());
        rpcRequest.addParam("riskControlInfo", RiskControlInfoContext.buildRiskControlInfo());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, String.class, callback);
    }

    public static void unbind(UccParams uccParams, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = ApiConstants.ApiName.UNBIND;
        rpcRequest.version = "1.0";
        JSONObject jsonObject = new JSONObject();
        try {
            buildBaseParam(uccParams, jsonObject);
            jsonObject.put("site", uccParams.site);
            jsonObject.put("userToken", uccParams.userToken);
            jsonObject.put("bindSite", uccParams.bindSite);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("request", jsonObject.toString());
        rpcRequest.addParam("riskControlInfo", RiskControlInfoContext.buildRiskControlInfo());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, String.class, callback);
    }

    public static void bindAfterRecommend(UccParams uccParams, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = ApiConstants.ApiName.BIND_H5_AUTH;
        rpcRequest.version = "1.0";
        JSONObject jsonObject = new JSONObject();
        try {
            buildBaseParam(uccParams, jsonObject);
            jsonObject.put("requestToken", uccParams.requestToken);
            jsonObject.put("bindUserToken", uccParams.bindUserToken);
            if (!TextUtils.isEmpty(uccParams.needUpgrade)) {
                jsonObject.put("needUpgrade", uccParams.needUpgrade);
            }
            if (uccParams != null) {
                jsonObject.put("miniAppId", uccParams.miniAppId);
                jsonObject.put(ApiConstants.ApiField.SDK_TRACE_ID, uccParams.traceId);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("request", jsonObject.toString());
        rpcRequest.addParam("riskControlInfo", RiskControlInfoContext.buildRiskControlInfo());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, String.class, callback);
    }

    public static void bindIdentify(UccParams uccParams, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = ApiConstants.ApiName.BIND_IDENTIFY;
        rpcRequest.version = "1.0";
        JSONObject jsonObject = new JSONObject();
        try {
            buildBaseParam(uccParams, jsonObject);
            jsonObject.put("requestToken", uccParams.requestToken);
            jsonObject.put(ApiConstants.ApiField.BIND_IV_TOKEN, uccParams.ivToken);
            jsonObject.put("bindUserToken", uccParams.bindUserToken);
            jsonObject.put("miniAppId", uccParams.miniAppId);
            jsonObject.put(ApiConstants.ApiField.SDK_TRACE_ID, uccParams.traceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("request", jsonObject.toString());
        rpcRequest.addParam("riskControlInfo", RiskControlInfoContext.buildRiskControlInfo());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, String.class, callback);
    }

    public static void getUserInfo(UccParams uccParams, String callFrom, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = ApiConstants.ApiName.GET_AUTH_INFO;
        rpcRequest.version = "1.0";
        JSONObject jsonObject = new JSONObject();
        try {
            buildBaseParam(uccParams, jsonObject);
            jsonObject.put("bindUserToken", uccParams.bindUserToken);
            jsonObject.put(ApiConstants.ApiField.BIND_USER_TOKEN_TYPE, uccParams.bindUserTokenType);
            jsonObject.put("site", uccParams.site);
            jsonObject.put("userToken", uccParams.userToken);
            jsonObject.put("bindSite", uccParams.bindSite);
            jsonObject.put("miniAppId", uccParams.miniAppId);
            jsonObject.put("scene", uccParams.scene);
            jsonObject.put(ApiConstants.ApiField.SDK_TRACE_ID, uccParams.traceId);
            jsonObject.put("callFrom", callFrom);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("request", jsonObject.toString());
        rpcRequest.addParam("riskControlInfo", RiskControlInfoContext.buildRiskControlInfo());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, String.class, callback);
    }

    public static void skipUpgrade(UccParams uccParams, String callFrom, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = ApiConstants.ApiName.SKIP_UPGRADE;
        rpcRequest.version = "1.0";
        JSONObject jsonObject = new JSONObject();
        try {
            buildBaseParam(uccParams, jsonObject);
            jsonObject.put("site", uccParams.site);
            jsonObject.put("userToken", uccParams.userToken);
            jsonObject.put("requestToken", uccParams.requestToken);
            jsonObject.put("bindSite", uccParams.bindSite);
            jsonObject.put("miniAppId", uccParams.miniAppId);
            jsonObject.put("scene", uccParams.scene);
            jsonObject.put(ApiConstants.ApiField.USER_ACTION, uccParams.userAction);
            jsonObject.put(ApiConstants.ApiField.SDK_TRACE_ID, uccParams.traceId);
            jsonObject.put(ApiConstants.ApiField.CREATE_BIND_SITE_SESSION, uccParams.createBindSiteSession);
            jsonObject.put("callFrom", callFrom);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("request", jsonObject.toString());
        rpcRequest.addParam("riskControlInfo", RiskControlInfoContext.buildRiskControlInfo());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, String.class, callback);
    }

    public static void changeBind(UccParams uccParams, String changeBindToken, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = ApiConstants.ApiName.CHANGE_BIND;
        rpcRequest.version = "1.0";
        JSONObject jsonObject = new JSONObject();
        try {
            buildBaseParam(uccParams, jsonObject);
            jsonObject.put("requestToken", changeBindToken);
            jsonObject.put("miniAppId", uccParams.miniAppId);
            jsonObject.put(ApiConstants.ApiField.SDK_TRACE_ID, uccParams.traceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("request", jsonObject.toString());
        rpcRequest.addParam("riskControlInfo", RiskControlInfoContext.buildRiskControlInfo());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, String.class, callback);
    }

    public static void trustLogin(UccParams uccParams, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = ApiConstants.ApiName.TRUST_LOGIN;
        rpcRequest.version = "1.0";
        addAuthrizationRequestObject(uccParams, rpcRequest);
        Map<String, String> deviceTokenMap = getDeviceTokenKey();
        if (!TextUtils.isEmpty(deviceTokenMap.get(ApiConstants.ApiField.DEVICE_TOKEN_KEY))) {
            rpcRequest.addParam(ApiConstants.ApiField.DEVICE_TOKEN_KEY, deviceTokenMap.get(ApiConstants.ApiField.DEVICE_TOKEN_KEY));
            TreeMap<String, String> deviceTokenSignParam = new TreeMap<>();
            deviceTokenSignParam.put("appKey", ((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey());
            deviceTokenSignParam.put("appVersion", CommonUtils.getAndroidAppVersion());
            deviceTokenSignParam.put(TbAuthConstants.KEY_HAVANAID, String.valueOf(deviceTokenMap.get("userId")));
            deviceTokenSignParam.put("timestamp", String.valueOf(System.currentTimeMillis()));
            deviceTokenSignParam.put("sdkVersion", KernelContext.SDK_VERSION_STD);
            rpcRequest.addParam("deviceTokenSign", ((StorageService) AliMemberSDK.getService(StorageService.class)).signMap(deviceTokenMap.get(ApiConstants.ApiField.DEVICE_TOKEN_KEY), deviceTokenSignParam));
        }
        rpcRequest.addParam("riskControlInfo", RiskControlInfoContext.buildRiskControlInfo());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, String.class, callback);
    }

    public static void uccOAuthLogin(UccParams uccParams, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = ApiConstants.ApiName.OAUTH_LOGIN;
        rpcRequest.version = "1.0";
        JSONObject jsonObject = new JSONObject();
        try {
            buildBaseParam(uccParams, jsonObject);
            jsonObject.put("site", uccParams.site);
            jsonObject.put("userToken", uccParams.userToken);
            jsonObject.put("bindSite", uccParams.bindSite);
            jsonObject.put("bindUserToken", uccParams.bindUserToken);
            jsonObject.put(ApiConstants.ApiField.BIND_USER_TOKEN_TYPE, uccParams.bindUserTokenType);
            jsonObject.put("miniAppId", uccParams.miniAppId);
            jsonObject.put(ApiConstants.ApiField.SDK_TRACE_ID, uccParams.traceId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("request", jsonObject.toString());
        rpcRequest.addParam("riskControlInfo", RiskControlInfoContext.buildRiskControlInfo());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, String.class, callback);
    }

    public static void upgrade(UccParams uccParams, String scene, String requestToken, RpcRequestCallbackWithCode callbackWithCode) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = "mtop.alibaba.ucc.upgrade.account";
        rpcRequest.version = "1.0";
        JSONObject jsonObject = new JSONObject();
        try {
            buildBaseParam(uccParams, jsonObject);
            jsonObject.put("scene", scene);
            jsonObject.put("requestToken", requestToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("request", jsonObject.toString());
        rpcRequest.addParam("riskControlInfo", RiskControlInfoContext.buildRiskControlInfo());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, String.class, callbackWithCode);
    }

    private static void addAuthrizationRequestObject(UccParams uccParams, RpcRequest rpcRequest) {
        AuthorizationRequest request = new AuthorizationRequest();
        request.appName = ((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey();
        request.appVersion = CommonUtils.getAndroidAppVersion();
        request.utdid = DeviceInfo.deviceId;
        request.sdkVersion = KernelContext.SDK_VERSION_STD;
        request.deviceName = Build.MODEL;
        request.locale = DeviceInfo.getLocale(KernelContext.applicationContext);
        request.localSite = uccParams.site;
        request.userToken = uccParams.userToken;
        request.targetSite = uccParams.bindSite;
        request.scene = uccParams.scene;
        if (Site.ICBU.equals(uccParams.bindSite)) {
            Map<String, String> ext = new HashMap<>();
            Map<String, Map<String, String>> clientCookies = new HashMap<>();
            try {
                CookieManager.getInstance().setAcceptCookie(true);
                String cookie = CookieManager.getInstance().getCookie(".alibaba.com");
                if (!TextUtils.isEmpty(cookie)) {
                    String[] cookies = cookie.split(SymbolExpUtil.SYMBOL_SEMICOLON);
                    if (cookie != null && cookie.length() > 0) {
                        Map<String, String> cookieMap = new HashMap<>();
                        for (String trim : cookies) {
                            String coo = trim.trim();
                            if (!TextUtils.isEmpty(coo) && coo.contains("=")) {
                                int index = coo.indexOf("=");
                                String key = coo.substring(0, index);
                                String value = coo.substring(index + 1);
                                if ("xman_us_f".equals(key) || "xman_t".equals(key) || "xman_f".equals(key) || "intl_common_forever".equals(key) || "acs_usuc_t".equals(key)) {
                                    cookieMap.put(key, value);
                                }
                            }
                        }
                        clientCookies.put("alibaba.com", cookieMap);
                        ext.put("clientCookies", JSON.toJSONString(clientCookies));
                        request.ext = ext;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        rpcRequest.addParam("authorizationRequest", JSON.toJSONString(request));
    }

    public static void tokenLoginAfterBind(String trustToken, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = ApiConstants.ApiName.TOKEN_LOGIN_AFTER_BIND;
        rpcRequest.version = "1.0";
        rpcRequest.addParam("trustLoginToken", trustToken);
        rpcRequest.addParam("riskControlInfo", RiskControlInfoContext.buildRiskControlInfo());
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, String.class, callback);
    }

    private static Map<String, String> getDeviceTokenKey() {
        Map<String, String> deviceMap = new TreeMap<>();
        DeviceTokenAccount deviceTokenAccount = DeviceTokenManager.getInstance().getDeviceToken();
        List<HistoryAccount> historyAccountList = AccountHistoryManager.getInstance().getHistoryAccounts();
        if (deviceTokenAccount != null && !TextUtils.isEmpty(deviceTokenAccount.tokenKey) && (historyAccountList == null || historyAccountList.size() == 0)) {
            deviceMap.put(ApiConstants.ApiField.DEVICE_TOKEN_KEY, deviceTokenAccount.tokenKey);
            deviceMap.put("userId", deviceTokenAccount.hid);
        } else if (deviceTokenAccount == null && historyAccountList != null && historyAccountList.size() > 0) {
            deviceMap.put(ApiConstants.ApiField.DEVICE_TOKEN_KEY, historyAccountList.get(0).tokenKey);
            deviceMap.put("userId", historyAccountList.get(0).userId);
        } else if (deviceTokenAccount != null && !TextUtils.isEmpty(deviceTokenAccount.tokenKey) && historyAccountList != null && historyAccountList.size() > 0) {
            if (Long.parseLong(deviceTokenAccount.t) < Long.parseLong(historyAccountList.get(0).t)) {
                deviceMap.put(ApiConstants.ApiField.DEVICE_TOKEN_KEY, historyAccountList.get(0).tokenKey);
                deviceMap.put("userId", historyAccountList.get(0).userId);
            } else {
                deviceMap.put(ApiConstants.ApiField.DEVICE_TOKEN_KEY, deviceTokenAccount.tokenKey);
                deviceMap.put("userId", deviceTokenAccount.hid);
            }
        }
        return deviceMap;
    }

    public static void loginByIVToken(int site, String ivToken, String scene, String aliusersdk_string, RpcRequestCallbackWithCode callback) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = "com.taobao.mtop.mloginService.mloginTokenLogin";
        rpcRequest.version = "1.0";
        try {
            JSONObject param = new JSONObject();
            if (KernelContext.isMini) {
                param.put("app_id", KernelContext.getApplicationContext().getPackageName() + "|" + SystemUtils.getApkPublicKeyDigest());
            } else {
                param.put("utdid", ((RpcService) AliMemberSDK.getService(RpcService.class)).getDeviceId());
            }
            param.put("site", site);
            param.put("appName", ((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey());
            param.put("token", ivToken);
            param.put("t", "" + System.currentTimeMillis());
            param.put("scene", scene);
            param.put("sdkVersion", KernelContext.sdkVersion);
            param.put("clientIp", CommonUtils.getLocalIPAddress());
            try {
                JSONObject extObject = new JSONObject();
                if (!TextUtils.isEmpty(aliusersdk_string)) {
                    extObject.put(Constants.QUERY_STRING, aliusersdk_string);
                }
                param.put("ext", extObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            rpcRequest.addParam("tokenInfo", param);
            JSONObject risk = new JSONObject();
            risk.put("umidToken", ((StorageService) AliMemberSDK.getService(StorageService.class)).getUmid());
            rpcRequest.addParam("riskControlInfo", risk);
            rpcRequest.addParam("ext", JSONUtils.toJsonObject(new HashMap<>()));
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, String.class, callback);
    }

    private static void buildBaseParam(UccParams uccParams, JSONObject jsonObject) {
        try {
            jsonObject.put("appName", ((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey());
            jsonObject.put("appVersion", CommonUtils.getAndroidAppVersion());
            jsonObject.put("utdid", DeviceInfo.deviceId);
            jsonObject.put("sdkVersion", TextUtils.isEmpty(uccParams.sdkVersion) ? KernelContext.sdkVersion : uccParams.sdkVersion);
            jsonObject.put(ApiConstants.ApiField.DEVICE_NAME, Build.MODEL);
            jsonObject.put(ApiConstants.ApiField.LOCALE, DeviceInfo.getLocale(KernelContext.applicationContext));
        } catch (Throwable th) {
        }
    }
}
