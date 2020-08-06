package com.ali.user.open.tbauth.task;

import android.text.TextUtils;
import com.ali.auth.third.core.model.Constants;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.model.LoginReturnData;
import com.ali.user.open.core.model.RpcRequest;
import com.ali.user.open.core.model.RpcRequestCallbackWithCode;
import com.ali.user.open.core.service.RpcService;
import com.ali.user.open.core.service.StorageService;
import com.ali.user.open.core.service.UserTrackerService;
import com.ali.user.open.core.util.CommonUtils;
import com.ali.user.open.core.util.JSONUtils;
import com.ali.user.open.core.util.SystemUtils;
import com.ali.user.open.history.AccountHistoryManager;
import com.ali.user.open.history.HistoryAccount;
import com.ali.user.open.service.impl.SessionManager;
import com.ali.user.open.tbauth.TbAuthConstants;
import com.ali.user.open.ucc.data.ApiConstants;
import com.ali.user.open.ucc.util.UccConstants;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONException;
import org.json.JSONObject;

public class RpcRepository {
    public static void refreshPageAfterOpenTb(String token, RpcRequestCallbackWithCode callback) {
        ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("TOP_TOKEN_LOGIN", (Map<String, String>) null);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = "com.taobao.mtop.mloginService.topTokenLogin";
        rpcRequest.version = "1.0";
        try {
            JSONObject param = new JSONObject();
            if (KernelContext.isMini) {
                param.put("app_id", KernelContext.getApplicationContext().getPackageName() + "|" + SystemUtils.getApkPublicKeyDigest());
            } else {
                param.put("utdid", ((RpcService) AliMemberSDK.getService(RpcService.class)).getDeviceId());
            }
            param.put("appName", ((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey());
            param.put("token", token);
            param.put("t", "" + System.currentTimeMillis());
            param.put("sdkVersion", KernelContext.sdkVersion);
            param.put("clientIp", CommonUtils.getLocalIPAddress());
            rpcRequest.addParam("tokenInfo", param);
            JSONObject risk = new JSONObject();
            risk.put("umidToken", ((StorageService) AliMemberSDK.getService(StorageService.class)).getUmid());
            rpcRequest.addParam("riskControlInfo", risk);
            rpcRequest.addParam("ext", new JSONObject());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, LoginReturnData.class, callback);
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
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(rpcRequest, LoginReturnData.class, callback);
    }

    public static void loginByRefreshToken(RpcRequestCallbackWithCode callback) {
        HistoryAccount account;
        RpcRequest request = new RpcRequest();
        request.target = "com.taobao.mtop.mLoginUnitService.autoLogin";
        request.version = "1.0";
        try {
            String userid = SessionManager.INSTANCE.getInternalSession().userId;
            request.addParam("userId", Long.valueOf(Long.parseLong(userid)));
            JSONObject refreshTokenInfo = new JSONObject();
            refreshTokenInfo.put("appName", ((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey());
            refreshTokenInfo.put("token", SessionManager.INSTANCE.getInternalSession().autoLoginToken);
            refreshTokenInfo.put("sdkVersion", KernelContext.sdkVersion);
            long t = System.currentTimeMillis();
            refreshTokenInfo.put("t", "" + t);
            refreshTokenInfo.put("clientIp", CommonUtils.getLocalIPAddress());
            if (KernelContext.isMini) {
                refreshTokenInfo.put("app_id", KernelContext.getApplicationContext().getPackageName() + "|" + SystemUtils.getApkPublicKeyDigest());
            } else {
                refreshTokenInfo.put("utdid", ((RpcService) AliMemberSDK.getService(RpcService.class)).getDeviceId());
            }
            if (!TextUtils.isEmpty(userid) && (account = AccountHistoryManager.getInstance().findHistoryAccount(userid)) != null) {
                String tokenKey = account.tokenKey;
                if (!TextUtils.isEmpty(tokenKey)) {
                    TreeMap<String, String> treeMap = new TreeMap<>();
                    addKey(treeMap, "appKey", ((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey());
                    addKey(treeMap, TbAuthConstants.KEY_HAVANAID, account.userId);
                    addKey(treeMap, "timestamp", String.valueOf(t));
                    addKey(treeMap, "appVersion", CommonUtils.getAndroidAppVersion());
                    addKey(treeMap, "sdkVersion", KernelContext.sdkVersion);
                    String sign = ((StorageService) AliMemberSDK.getService(StorageService.class)).signMap(tokenKey, treeMap);
                    if (!TextUtils.isEmpty(sign)) {
                        refreshTokenInfo.put("deviceTokenSign", sign);
                        refreshTokenInfo.put(ApiConstants.ApiField.DEVICE_TOKEN_KEY, tokenKey);
                        refreshTokenInfo.put("hid", account.userId);
                    }
                }
            }
            request.addParam("tokenInfo", refreshTokenInfo);
            JSONObject risk = new JSONObject();
            risk.put("umidToken", ((StorageService) AliMemberSDK.getService(StorageService.class)).getUmid());
            request.addParam("riskControlInfo", risk);
            request.addParam("ext", new JSONObject());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(request, LoginReturnData.class, callback);
    }

    public static void addKey(Map<String, String> map, String key, String value) {
        map.put(key, value);
    }

    public static void getAccessTokenWithAuthCode(String authCode, String targetSite, RpcRequestCallbackWithCode callback) {
        RpcRequest request = new RpcRequest();
        request.target = "mtop.alibaba.ucc.convertAuthCodeToAccessToken";
        request.version = "1.0";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appName", ((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey());
            jsonObject.put("authCode", authCode);
            jsonObject.put("site", targetSite);
            request.addParam("convertAccessTokenRequest", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(request, ConvertAuthCodeToAccessTokenData.class, callback);
    }

    public static void logout(RpcRequestCallbackWithCode callback) {
        RpcRequest request = new RpcRequest();
        request.target = "mtop.taobao.havana.mlogin.logout";
        request.version = "1.0";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appKey", ((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey());
            jsonObject.put("sid", SessionManager.INSTANCE.getInternalSession().sid);
            jsonObject.put(TbAuthConstants.IP, CommonUtils.getLocalIPAddress());
            request.addParam("userId", Long.valueOf(Long.parseLong(SessionManager.INSTANCE.getInternalSession().userId)));
            request.addParam("request", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(request, String.class, callback);
    }

    public static void validateAuthCode(String authCode, String site, String targetSite, RpcRequestCallbackWithCode callback) {
        RpcRequest request = new RpcRequest();
        request.target = "mtop.alibaba.ucc.authcodet.validate";
        request.version = "1.0";
        request.addParam("appKey", ((StorageService) AliMemberSDK.getService(StorageService.class)).getAppKey());
        request.addParam("authCode", authCode);
        request.addParam("site", site);
        request.addParam(UccConstants.PARAM_TARGET_SITE, targetSite);
        ((RpcService) AliMemberSDK.getService(RpcService.class)).remoteBusiness(request, ConvertAuthCodeToAccessTokenData.class, callback);
    }
}
