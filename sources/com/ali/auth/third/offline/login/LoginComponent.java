package com.ali.auth.third.offline.login;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.ali.auth.third.core.config.AuthOption;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.cookies.LoginCookieUtils;
import com.ali.auth.third.core.history.AccountHistoryManager;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.core.model.HistoryAccount;
import com.ali.auth.third.core.model.LoginReturnData;
import com.ali.auth.third.core.model.RpcRequest;
import com.ali.auth.third.core.model.RpcRequestCallbackWithCode;
import com.ali.auth.third.core.model.RpcResponse;
import com.ali.auth.third.core.rpc.protocol.RpcException;
import com.ali.auth.third.core.service.RpcService;
import com.ali.auth.third.core.service.StorageService;
import com.ali.auth.third.core.service.UserTrackerService;
import com.ali.auth.third.core.service.impl.CredentialManager;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.CommonUtils;
import com.ali.auth.third.core.util.JSONUtils;
import com.ali.auth.third.core.util.RSAKey;
import com.ali.auth.third.core.util.ResourceUtils;
import com.ali.auth.third.core.util.Rsa;
import com.ali.auth.third.core.util.SystemUtils;
import com.ali.auth.third.offline.LoginWebViewActivity;
import com.ali.auth.third.offline.QrLoginActivity;
import com.ali.auth.third.offline.login.context.LoginContext;
import com.ali.auth.third.offline.model.ContractModel;
import com.ali.auth.third.offline.model.LoginParam;
import com.ali.user.open.tbauth.TbAuthConstants;
import com.ali.user.open.ucc.data.ApiConstants;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginComponent {
    public static final LoginComponent INSTANCE = new LoginComponent();
    private static final String OAUTH_API = "taobao.oauth.code.create";
    private static final String TAG = "login";

    private LoginComponent() {
    }

    public RpcResponse<LoginReturnData> loginByUserName(String jsRequestJson) {
        HistoryAccount account;
        RpcRequest rpcRequest = new RpcRequest();
        try {
            JSONObject jsbridge = new JSONObject(jsRequestJson);
            String name = JSONUtils.optString(jsbridge, "loginid");
            if (TextUtils.isEmpty(name)) {
                name = JSONUtils.optString(jsbridge, "loginId");
            }
            rpcRequest.target = "com.taobao.mtop.mloginService.login";
            rpcRequest.version = "1.0";
            JSONObject infoJsonObject = new JSONObject();
            if (KernelContext.isMini) {
                infoJsonObject.put("app_id", KernelContext.getApplicationContext().getPackageName() + "|" + SystemUtils.getApkPublicKeyDigest());
            } else {
                infoJsonObject.put("utdid", ((RpcService) KernelContext.getService(RpcService.class)).getDeviceId());
            }
            infoJsonObject.put("appName", KernelContext.getAppKey());
            infoJsonObject.put("loginId", name);
            infoJsonObject.put("clientIp", CommonUtils.getLocalIPAddress());
            infoJsonObject.put("site", ConfigManager.getSite());
            long t = System.currentTimeMillis();
            if (!TextUtils.isEmpty(name) && (account = AccountHistoryManager.getInstance().matchHistoryAccount(name)) != null) {
                String tokenKey = account.tokenKey;
                if (!TextUtils.isEmpty(tokenKey)) {
                    TreeMap<String, String> treeMap = new TreeMap<>();
                    addKey(treeMap, LoginConstants.KEY_APPKEY, KernelContext.getAppKey());
                    addKey(treeMap, LoginConstants.KEY_HAVANAID, account.userId);
                    addKey(treeMap, LoginConstants.KEY_TIMESTAMP, String.valueOf(t));
                    addKey(treeMap, LoginConstants.KEY_APPVERSION, CommonUtils.getAndroidAppVersion());
                    addKey(treeMap, LoginConstants.KEY_SDKVERSION, KernelContext.sdkVersion);
                    String sign = ((StorageService) KernelContext.getService(StorageService.class)).signMap(tokenKey, treeMap);
                    if (!TextUtils.isEmpty(sign)) {
                        infoJsonObject.put("deviceTokenSign", sign);
                        infoJsonObject.put(ApiConstants.ApiField.DEVICE_TOKEN_KEY, tokenKey);
                        infoJsonObject.put("hid", account.userId);
                    }
                }
            }
            infoJsonObject.put("password", Rsa.encrypt(JSONUtils.optString(jsbridge, "password"), RSAKey.getRsaPubkey()));
            infoJsonObject.put("pwdEncrypted", true);
            infoJsonObject.put("appVersion", CommonUtils.getAndroidAppVersion());
            infoJsonObject.put("sdkVersion", KernelContext.sdkVersion);
            infoJsonObject.put("t", t + "");
            infoJsonObject.put("ccId", JSONUtils.optString(jsbridge, "checkCodeId"));
            infoJsonObject.put("checkCode", JSONUtils.optString(jsbridge, "checkCode"));
            try {
                JSONObject extObject = LoginCookieUtils.getKeyValues("alimm_");
                extObject.put("miid", LoginCookieUtils.getValue("miid"));
                infoJsonObject.put("ext", extObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            rpcRequest.addParam(TbAuthConstants.PARAN_LOGIN_INFO, infoJsonObject);
            JSONObject risk = new JSONObject();
            if (KernelContext.isMini) {
                String umidToken = JSONUtils.optString(jsbridge, Constants.UMID);
                risk.put("umidToken", umidToken);
                ((StorageService) KernelContext.getService(StorageService.class)).setUmid(umidToken);
            } else {
                risk.put("umidToken", ((StorageService) KernelContext.getService(StorageService.class)).getUmid());
            }
            risk.put("ua", JSONUtils.optString(jsbridge, "ua"));
            rpcRequest.addParam("riskControlInfo", risk);
            rpcRequest.addParam("ext", new JSONObject());
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return ((RpcService) KernelContext.getService(RpcService.class)).invoke(rpcRequest, LoginReturnData.class);
    }

    public RpcResponse<LoginReturnData> pwdLogin(LoginParam loginParam) {
        HistoryAccount account;
        RpcRequest rpcRequest = new RpcRequest();
        try {
            rpcRequest.target = "com.taobao.mtop.mloginService.login";
            rpcRequest.version = "1.0";
            JSONObject infoJsonObject = new JSONObject();
            if (KernelContext.isMini) {
                infoJsonObject.put("app_id", KernelContext.getApplicationContext().getPackageName() + "|" + SystemUtils.getApkPublicKeyDigest());
            } else {
                infoJsonObject.put("utdid", ((RpcService) KernelContext.getService(RpcService.class)).getDeviceId());
            }
            infoJsonObject.put("appName", KernelContext.getAppKey());
            String username = loginParam.username;
            infoJsonObject.put("loginId", username);
            infoJsonObject.put("clientIp", CommonUtils.getLocalIPAddress());
            infoJsonObject.put("site", ConfigManager.getSite());
            long t = System.currentTimeMillis();
            if (!TextUtils.isEmpty(username) && (account = AccountHistoryManager.getInstance().matchHistoryAccount(username)) != null) {
                String tokenKey = account.tokenKey;
                if (!TextUtils.isEmpty(tokenKey)) {
                    TreeMap<String, String> treeMap = new TreeMap<>();
                    addKey(treeMap, LoginConstants.KEY_APPKEY, KernelContext.getAppKey());
                    addKey(treeMap, LoginConstants.KEY_HAVANAID, account.userId);
                    addKey(treeMap, LoginConstants.KEY_TIMESTAMP, String.valueOf(t));
                    addKey(treeMap, LoginConstants.KEY_APPVERSION, CommonUtils.getAndroidAppVersion());
                    addKey(treeMap, LoginConstants.KEY_SDKVERSION, KernelContext.sdkVersion);
                    String sign = ((StorageService) KernelContext.getService(StorageService.class)).signMap(tokenKey, treeMap);
                    if (!TextUtils.isEmpty(sign)) {
                        infoJsonObject.put("deviceTokenSign", sign);
                        infoJsonObject.put(ApiConstants.ApiField.DEVICE_TOKEN_KEY, tokenKey);
                        infoJsonObject.put("hid", account.userId);
                    }
                }
            }
            String str = "password";
            infoJsonObject.put(str, Rsa.encrypt(loginParam.password, RSAKey.getRsaPubkey()));
            infoJsonObject.put("pwdEncrypted", true);
            infoJsonObject.put("appVersion", CommonUtils.getAndroidAppVersion());
            infoJsonObject.put("sdkVersion", KernelContext.sdkVersion);
            infoJsonObject.put("t", t + "");
            try {
                JSONObject extObject = LoginCookieUtils.getKeyValues("alimm_");
                extObject.put("miid", LoginCookieUtils.getValue("miid"));
                extObject.put("useSlipCheckCode", "true");
                infoJsonObject.put("ext", extObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            rpcRequest.addParam(TbAuthConstants.PARAN_LOGIN_INFO, infoJsonObject);
            JSONObject risk = new JSONObject();
            if (KernelContext.isMini) {
                risk.put("umidToken", "");
                ((StorageService) KernelContext.getService(StorageService.class)).setUmid("");
            } else {
                risk.put("umidToken", ((StorageService) KernelContext.getService(StorageService.class)).getUmid());
            }
            rpcRequest.addParam("riskControlInfo", risk);
            JSONObject ext = new JSONObject();
            String aliusersdk_querystring = loginParam.ext.get("aliusersdk_querystring");
            if (!TextUtils.isEmpty(aliusersdk_querystring)) {
                ext.put(Constants.QUERY_STRING, aliusersdk_querystring);
            }
            String posSN = loginParam.ext.get("posSN");
            if (!TextUtils.isEmpty(posSN)) {
                ext.put("posSN", posSN);
            }
            rpcRequest.addParam("ext", ext);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return ((RpcService) KernelContext.getService(RpcService.class)).invoke(rpcRequest, LoginReturnData.class);
    }

    public RpcResponse<LoginReturnData> pwdLogin(String username, String password, String aliusersdk_querystring) {
        HistoryAccount account;
        RpcRequest rpcRequest = new RpcRequest();
        try {
            rpcRequest.target = "com.taobao.mtop.mloginService.login";
            rpcRequest.version = "1.0";
            JSONObject infoJsonObject = new JSONObject();
            if (KernelContext.isMini) {
                infoJsonObject.put("app_id", KernelContext.getApplicationContext().getPackageName() + "|" + SystemUtils.getApkPublicKeyDigest());
            } else {
                infoJsonObject.put("utdid", ((RpcService) KernelContext.getService(RpcService.class)).getDeviceId());
            }
            infoJsonObject.put("appName", KernelContext.getAppKey());
            infoJsonObject.put("loginId", username);
            infoJsonObject.put("clientIp", CommonUtils.getLocalIPAddress());
            infoJsonObject.put("site", ConfigManager.getSite());
            long t = System.currentTimeMillis();
            if (!TextUtils.isEmpty(username) && (account = AccountHistoryManager.getInstance().matchHistoryAccount(username)) != null) {
                String tokenKey = account.tokenKey;
                if (!TextUtils.isEmpty(tokenKey)) {
                    TreeMap<String, String> treeMap = new TreeMap<>();
                    addKey(treeMap, LoginConstants.KEY_APPKEY, KernelContext.getAppKey());
                    addKey(treeMap, LoginConstants.KEY_HAVANAID, account.userId);
                    addKey(treeMap, LoginConstants.KEY_TIMESTAMP, String.valueOf(t));
                    addKey(treeMap, LoginConstants.KEY_APPVERSION, CommonUtils.getAndroidAppVersion());
                    addKey(treeMap, LoginConstants.KEY_SDKVERSION, KernelContext.sdkVersion);
                    String sign = ((StorageService) KernelContext.getService(StorageService.class)).signMap(tokenKey, treeMap);
                    if (!TextUtils.isEmpty(sign)) {
                        infoJsonObject.put("deviceTokenSign", sign);
                        infoJsonObject.put(ApiConstants.ApiField.DEVICE_TOKEN_KEY, tokenKey);
                        infoJsonObject.put("hid", account.userId);
                    }
                }
            }
            infoJsonObject.put("password", Rsa.encrypt(password, RSAKey.getRsaPubkey()));
            infoJsonObject.put("pwdEncrypted", true);
            infoJsonObject.put("appVersion", CommonUtils.getAndroidAppVersion());
            infoJsonObject.put("sdkVersion", KernelContext.sdkVersion);
            infoJsonObject.put("t", t + "");
            try {
                JSONObject extObject = LoginCookieUtils.getKeyValues("alimm_");
                extObject.put("miid", LoginCookieUtils.getValue("miid"));
                extObject.put("useSlipCheckCode", "true");
                infoJsonObject.put("ext", extObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            rpcRequest.addParam(TbAuthConstants.PARAN_LOGIN_INFO, infoJsonObject);
            JSONObject risk = new JSONObject();
            if (KernelContext.isMini) {
                risk.put("umidToken", "");
                ((StorageService) KernelContext.getService(StorageService.class)).setUmid("");
            } else {
                risk.put("umidToken", ((StorageService) KernelContext.getService(StorageService.class)).getUmid());
            }
            rpcRequest.addParam("riskControlInfo", risk);
            JSONObject ext = new JSONObject();
            if (!TextUtils.isEmpty(aliusersdk_querystring)) {
                ext.put(Constants.QUERY_STRING, aliusersdk_querystring);
            }
            rpcRequest.addParam("ext", ext);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return ((RpcService) KernelContext.getService(RpcService.class)).invoke(rpcRequest, LoginReturnData.class);
    }

    public void showLogin(final Activity activity) {
        SDKLogger.d("login", "showLogin");
        if (KernelContext.authOption == AuthOption.H5ONLY) {
            showH5Login(activity);
            return;
        }
        final String apkSign = SystemUtils.getApkSignNumber();
        new AsyncTask<Object, Void, String>() {
            /* access modifiers changed from: protected */
            public String doInBackground(Object... params) {
                SDKLogger.d("login", "showLogin doInBackground");
                if (TextUtils.isEmpty(apkSign)) {
                    return "";
                }
                try {
                    return LoginComponent.this.generateTopAppLinkToken(apkSign);
                } catch (Exception e) {
                    return "";
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(String signResult) {
                SDKLogger.d("login", "showLogin onPostExecute signResult = " + signResult);
                if (!TextUtils.isEmpty(signResult)) {
                    Intent intent = new Intent();
                    intent.setAction("com.taobao.open.intent.action.GETWAY");
                    intent.setData(Uri.parse("tbopen://m.taobao.com/getway/oauth?" + "&appkey=" + KernelContext.getAppKey() + "&pluginName=" + LoginComponent.OAUTH_API + "&apkSign=" + apkSign + "&sign=" + signResult));
                    if (activity.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
                        activity.startActivityForResult(intent, RequestCode.OPEN_TAOBAO);
                    } else {
                        LoginComponent.this.showH5Login(activity);
                    }
                } else {
                    LoginComponent.this.showH5Login(activity);
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
    }

    public String generateTopAppLinkToken(String apkSign) {
        ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send(UTConstants.E_GENERATE_TAOBAO_SIGN, (Map<String, String>) null);
        TreeMap<String, String> tree = new TreeMap<>();
        tree.put("appKey", KernelContext.getAppKey());
        tree.put("apkSign", apkSign);
        tree.put("apiName", OAUTH_API);
        String beSigned = params2Str(tree);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = "com.alibaba.havana.login.applink.generateTopAppLinkToken";
        rpcRequest.version = "1.0";
        String appName = KernelContext.getAppKey();
        try {
            JSONObject baseInfo = new JSONObject();
            baseInfo.put("appName", appName);
            baseInfo.put("t", "" + System.currentTimeMillis());
            baseInfo.put("clientIp", CommonUtils.getLocalIPAddress());
            if (KernelContext.isMini) {
                baseInfo.put("app_id", KernelContext.getApplicationContext().getPackageName() + "|" + SystemUtils.getApkPublicKeyDigest());
            }
            baseInfo.put("sdkVersion", KernelContext.sdkVersion);
            rpcRequest.addParam("baseInfo", baseInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("content", beSigned);
        try {
            RpcResponse<String> response = ((RpcService) KernelContext.getService(RpcService.class)).invoke(rpcRequest, String.class);
            if (response != null) {
                return (String) response.returnValue;
            }
            return null;
        } catch (RpcException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public void showH5Login(Activity activity) {
        SDKLogger.d("login", "open H5 login");
        Intent h5Intent = new Intent(activity, LoginWebViewActivity.class);
        h5Intent.putExtra("url", ConfigManager.LOGIN_HOST);
        h5Intent.putExtra("title", ResourceUtils.getString(activity.getApplicationContext(), "com_taobao_tae_sdk_authorize_title"));
        activity.startActivityForResult(h5Intent, RequestCode.OPEN_H5_LOGIN);
    }

    private String params2Str(TreeMap<String, String> params) {
        StringBuilder paramsSb = new StringBuilder();
        for (Map.Entry<String, String> paramEntry : params.entrySet()) {
            String key = paramEntry.getKey();
            String value = paramEntry.getValue();
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                paramsSb.append(key).append(value);
            }
        }
        return paramsSb.toString();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0107, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0107 A[ExcHandler: JSONException (r0v1 'e' org.json.JSONException A[CUSTOM_DECLARE]), Splitter:B:3:0x001f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.ali.auth.third.core.model.RpcResponse<com.ali.auth.third.core.model.LoginReturnData> loginByCode(java.lang.String r11) {
        /*
            r10 = this;
            r3 = 0
            java.lang.Class<com.ali.auth.third.core.service.UserTrackerService> r6 = com.ali.auth.third.core.service.UserTrackerService.class
            java.lang.Object r6 = com.ali.auth.third.core.context.KernelContext.getService(r6)     // Catch:{ Exception -> 0x010c }
            com.ali.auth.third.core.service.UserTrackerService r6 = (com.ali.auth.third.core.service.UserTrackerService) r6     // Catch:{ Exception -> 0x010c }
            java.lang.String r7 = "TOP_TOKEN_LOGIN"
            r8 = 0
            r6.send(r7, r8)     // Catch:{ Exception -> 0x010c }
            com.ali.auth.third.core.model.RpcRequest r5 = new com.ali.auth.third.core.model.RpcRequest     // Catch:{ Exception -> 0x010c }
            r5.<init>()     // Catch:{ Exception -> 0x010c }
            java.lang.String r6 = "com.taobao.mtop.mloginService.topTokenLogin"
            r5.target = r6     // Catch:{ Exception -> 0x010c }
            java.lang.String r6 = "1.0"
            r5.version = r6     // Catch:{ Exception -> 0x010c }
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0107 }
            r2.<init>()     // Catch:{ JSONException -> 0x0107 }
            boolean r6 = com.ali.auth.third.core.context.KernelContext.isMini     // Catch:{ JSONException -> 0x0107 }
            if (r6 == 0) goto L_0x00f3
            java.lang.String r6 = "app_id"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0107 }
            r7.<init>()     // Catch:{ JSONException -> 0x0107 }
            android.content.Context r8 = com.ali.auth.third.core.context.KernelContext.getApplicationContext()     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r8 = r8.getPackageName()     // Catch:{ JSONException -> 0x0107 }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r8 = "|"
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r8 = com.ali.auth.third.core.util.SystemUtils.getApkPublicKeyDigest()     // Catch:{ JSONException -> 0x0107 }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r7 = r7.toString()     // Catch:{ JSONException -> 0x0107 }
            r2.put(r6, r7)     // Catch:{ JSONException -> 0x0107 }
        L_0x0052:
            java.lang.String r6 = "appName"
            java.lang.String r7 = com.ali.auth.third.core.context.KernelContext.getAppKey()     // Catch:{ JSONException -> 0x0107 }
            r2.put(r6, r7)     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r6 = "token"
            r2.put(r6, r11)     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r6 = "t"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x0107 }
            r7.<init>()     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r8 = ""
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ JSONException -> 0x0107 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ JSONException -> 0x0107 }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r7 = r7.toString()     // Catch:{ JSONException -> 0x0107 }
            r2.put(r6, r7)     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r6 = "sdkVersion"
            java.lang.String r7 = com.ali.auth.third.core.context.KernelContext.sdkVersion     // Catch:{ JSONException -> 0x0107 }
            r2.put(r6, r7)     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r6 = "clientIp"
            java.lang.String r7 = com.ali.auth.third.core.util.CommonUtils.getLocalIPAddress()     // Catch:{ JSONException -> 0x0107 }
            r2.put(r6, r7)     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r6 = "site"
            int r7 = com.ali.auth.third.core.config.ConfigManager.getSite()     // Catch:{ JSONException -> 0x0107 }
            r2.put(r6, r7)     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r6 = "alimm_"
            org.json.JSONObject r1 = com.ali.auth.third.core.cookies.LoginCookieUtils.getKeyValues(r6)     // Catch:{ Exception -> 0x0111, JSONException -> 0x0107 }
            java.lang.String r6 = "miid"
            java.lang.String r7 = "miid"
            java.lang.String r7 = com.ali.auth.third.core.cookies.LoginCookieUtils.getValue(r7)     // Catch:{ Exception -> 0x0111, JSONException -> 0x0107 }
            r1.put(r6, r7)     // Catch:{ Exception -> 0x0111, JSONException -> 0x0107 }
            java.lang.String r6 = "ext"
            r2.put(r6, r1)     // Catch:{ Exception -> 0x0111, JSONException -> 0x0107 }
        L_0x00b6:
            java.lang.String r6 = "tokenInfo"
            r5.addParam(r6, r2)     // Catch:{ JSONException -> 0x0107 }
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0107 }
            r4.<init>()     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r7 = "umidToken"
            java.lang.Class<com.ali.auth.third.core.service.StorageService> r6 = com.ali.auth.third.core.service.StorageService.class
            java.lang.Object r6 = com.ali.auth.third.core.context.KernelContext.getService(r6)     // Catch:{ JSONException -> 0x0107 }
            com.ali.auth.third.core.service.StorageService r6 = (com.ali.auth.third.core.service.StorageService) r6     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r6 = r6.getUmid()     // Catch:{ JSONException -> 0x0107 }
            r4.put(r7, r6)     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r6 = "riskControlInfo"
            r5.addParam(r6, r4)     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r6 = "ext"
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ JSONException -> 0x0107 }
            r7.<init>()     // Catch:{ JSONException -> 0x0107 }
            r5.addParam(r6, r7)     // Catch:{ JSONException -> 0x0107 }
        L_0x00e4:
            java.lang.Class<com.ali.auth.third.core.service.RpcService> r6 = com.ali.auth.third.core.service.RpcService.class
            java.lang.Object r6 = com.ali.auth.third.core.context.KernelContext.getService(r6)     // Catch:{ Exception -> 0x010c }
            com.ali.auth.third.core.service.RpcService r6 = (com.ali.auth.third.core.service.RpcService) r6     // Catch:{ Exception -> 0x010c }
            java.lang.Class<com.ali.auth.third.core.model.LoginReturnData> r7 = com.ali.auth.third.core.model.LoginReturnData.class
            com.ali.auth.third.core.model.RpcResponse r3 = r6.invoke(r5, r7)     // Catch:{ Exception -> 0x010c }
        L_0x00f2:
            return r3
        L_0x00f3:
            java.lang.String r7 = "utdid"
            java.lang.Class<com.ali.auth.third.core.service.RpcService> r6 = com.ali.auth.third.core.service.RpcService.class
            java.lang.Object r6 = com.ali.auth.third.core.context.KernelContext.getService(r6)     // Catch:{ JSONException -> 0x0107 }
            com.ali.auth.third.core.service.RpcService r6 = (com.ali.auth.third.core.service.RpcService) r6     // Catch:{ JSONException -> 0x0107 }
            java.lang.String r6 = r6.getDeviceId()     // Catch:{ JSONException -> 0x0107 }
            r2.put(r7, r6)     // Catch:{ JSONException -> 0x0107 }
            goto L_0x0052
        L_0x0107:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Exception -> 0x010c }
            goto L_0x00e4
        L_0x010c:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00f2
        L_0x0111:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ JSONException -> 0x0107 }
            goto L_0x00b6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.auth.third.offline.login.LoginComponent.loginByCode(java.lang.String):com.ali.auth.third.core.model.RpcResponse");
    }

    public RpcResponse<LoginReturnData> loginByQRCode(String token, long time, boolean supportNativeIvOnly) {
        try {
            ((UserTrackerService) KernelContext.getService(UserTrackerService.class)).send("TOP_TOKEN_LOGIN", (Map<String, String>) null);
            RpcRequest rpcRequest = new RpcRequest();
            rpcRequest.target = "mtop.taobao.havana.mlogin.qrcodelogin";
            rpcRequest.version = "1.0";
            try {
                JSONObject param = new JSONObject();
                if (KernelContext.isMini) {
                    param.put("app_id", KernelContext.getApplicationContext().getPackageName() + "|" + SystemUtils.getApkPublicKeyDigest());
                } else {
                    param.put("utdid", ((RpcService) KernelContext.getService(RpcService.class)).getDeviceId());
                }
                param.put("appName", KernelContext.getAppKey());
                param.put("token", token);
                param.put("t", "" + time);
                param.put("sdkVersion", KernelContext.sdkVersion);
                param.put("clientIp", CommonUtils.getLocalIPAddress());
                param.put("site", ConfigManager.getSite());
                rpcRequest.addParam("tokenInfo", param);
                JSONObject risk = new JSONObject();
                risk.put("umidToken", ((StorageService) KernelContext.getService(StorageService.class)).getUmid());
                rpcRequest.addParam("riskControlInfo", risk);
                JSONObject ext = new JSONObject();
                if (!TextUtils.isEmpty(ConfigManager.getInstance().getOfflineDeviceID())) {
                    ext.put("posSN", ConfigManager.getInstance().getOfflineDeviceID());
                }
                ext.put("supportNativeIvOnly", supportNativeIvOnly);
                rpcRequest.addParam("ext", ext);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return ((RpcService) KernelContext.getService(RpcService.class)).invoke(rpcRequest, LoginReturnData.class);
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public RpcResponse<LoginReturnData> loginByIVToken(String ivToken, String scene, String aliusersdk_string) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = "com.taobao.mtop.mloginService.mloginTokenLogin";
        rpcRequest.version = "1.0";
        try {
            JSONObject param = new JSONObject();
            if (KernelContext.isMini) {
                param.put("app_id", KernelContext.getApplicationContext().getPackageName() + "|" + SystemUtils.getApkPublicKeyDigest());
            } else {
                param.put("utdid", ((RpcService) KernelContext.getService(RpcService.class)).getDeviceId());
            }
            param.put("appName", KernelContext.getAppKey());
            param.put("token", ivToken);
            param.put("t", "" + System.currentTimeMillis());
            param.put("scene", scene);
            param.put("sdkVersion", KernelContext.sdkVersion);
            param.put("clientIp", CommonUtils.getLocalIPAddress());
            param.put("site", ConfigManager.getSite());
            try {
                JSONObject extObject = LoginCookieUtils.getKeyValues("alimm_");
                extObject.put("miid", LoginCookieUtils.getValue("miid"));
                extObject.put(Constants.QUERY_STRING, aliusersdk_string);
                param.put("ext", extObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            rpcRequest.addParam("tokenInfo", param);
            JSONObject risk = new JSONObject();
            risk.put("umidToken", ((StorageService) KernelContext.getService(StorageService.class)).getUmid());
            rpcRequest.addParam("riskControlInfo", risk);
            rpcRequest.addParam("ext", JSONUtils.toJsonObject(new HashMap<>()));
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return LoginContext.rpcService.invoke(rpcRequest, LoginReturnData.class);
    }

    public static RpcResponse<LoginReturnData> loginBySSOToken() {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = "mtop.com.taobao.mloginService.appOldAutoLogin";
        rpcRequest.version = "1.0";
        try {
            JSONObject param = new JSONObject();
            param.put("token", ConfigManager.getInstance().getSsoToken());
            param.put("tokenType", "taoTVsso");
            param.put("site", ConfigManager.getSite());
            rpcRequest.addParam("tokenInfo", param);
            JSONObject risk = new JSONObject();
            risk.put("umidToken", ((StorageService) KernelContext.getService(StorageService.class)).getUmid());
            rpcRequest.addParam("riskControlInfo", risk);
        } catch (Exception e) {
            e.printStackTrace();
        }
        rpcRequest.addParam("hid", "");
        return LoginContext.rpcService.invoke(rpcRequest, LoginReturnData.class);
    }

    public static RpcResponse<LoginReturnData> loginByAlipayLoginToken(String loginToken) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = "mtop.taobao.havana.mlogin.alipayaso";
        rpcRequest.version = "2.0";
        try {
            JSONObject param = new JSONObject();
            if (KernelContext.isMini) {
                param.put("app_id", KernelContext.getApplicationContext().getPackageName() + "|" + SystemUtils.getApkPublicKeyDigest());
            } else {
                param.put("utdid", ((RpcService) KernelContext.getService(RpcService.class)).getDeviceId());
            }
            param.put("appName", KernelContext.getAppKey());
            param.put("token", loginToken);
            param.put("t", "" + System.currentTimeMillis());
            param.put("sdkVersion", KernelContext.sdkVersion);
            param.put("site", ConfigManager.getSite());
            try {
                JSONObject extObject = LoginCookieUtils.getKeyValues("alimm_");
                extObject.put("miid", LoginCookieUtils.getValue("miid"));
                param.put("ext", extObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            rpcRequest.addParam("tokenInfo", param);
            JSONObject risk = new JSONObject();
            risk.put("umidToken", ((StorageService) KernelContext.getService(StorageService.class)).getUmid());
            rpcRequest.addParam("riskControlInfo", risk);
            rpcRequest.addParam("ext", JSONUtils.toJsonObject(new HashMap<>()));
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return LoginContext.rpcService.invoke(rpcRequest, LoginReturnData.class);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x019f, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x01a0, code lost:
        r1.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x01a4, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:?, code lost:
        r1.printStackTrace();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x019f A[ExcHandler: JSONException (r1v1 'e' org.json.JSONException A[CUSTOM_DECLARE]), Splitter:B:1:0x000f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.ali.auth.third.core.model.RpcResponse<com.ali.auth.third.core.model.LoginReturnData> loginByRefreshToken() {
        /*
            com.ali.auth.third.core.model.RpcRequest r4 = new com.ali.auth.third.core.model.RpcRequest
            r4.<init>()
            java.lang.String r13 = "com.taobao.mtop.mLoginUnitService.autoLogin"
            r4.target = r13
            java.lang.String r13 = "1.0"
            r4.version = r13
            com.ali.auth.third.core.service.impl.CredentialManager r13 = com.ali.auth.third.core.service.impl.CredentialManager.INSTANCE     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            com.ali.auth.third.core.model.InternalSession r13 = r13.getInternalSession()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            com.ali.auth.third.core.model.User r13 = r13.user     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r12 = r13.userId     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = "userId"
            long r14 = java.lang.Long.parseLong(r12)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.Long r14 = java.lang.Long.valueOf(r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r4.addParam(r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r3.<init>()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = "appName"
            java.lang.String r14 = com.ali.auth.third.core.context.KernelContext.getAppKey()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r3.put(r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = "token"
            com.ali.auth.third.core.service.impl.CredentialManager r14 = com.ali.auth.third.core.service.impl.CredentialManager.INSTANCE     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            com.ali.auth.third.core.model.InternalSession r14 = r14.getInternalSession()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r14 = r14.autoLoginToken     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r3.put(r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = "sdkVersion"
            java.lang.String r14 = com.ali.auth.third.core.context.KernelContext.sdkVersion     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r3.put(r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = "site"
            int r14 = com.ali.auth.third.core.config.ConfigManager.getSite()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r3.put(r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            long r8 = java.lang.System.currentTimeMillis()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = "t"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r14.<init>()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r15 = ""
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.StringBuilder r14 = r14.append(r8)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r14 = r14.toString()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r3.put(r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = "clientIp"
            java.lang.String r14 = com.ali.auth.third.core.util.CommonUtils.getLocalIPAddress()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r3.put(r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            boolean r13 = com.ali.auth.third.core.context.KernelContext.isMini     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            if (r13 == 0) goto L_0x018b
            java.lang.String r13 = "app_id"
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r14.<init>()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            android.content.Context r15 = com.ali.auth.third.core.context.KernelContext.getApplicationContext()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r15 = r15.getPackageName()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r15 = "|"
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r15 = com.ali.auth.third.core.util.SystemUtils.getApkPublicKeyDigest()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r14 = r14.toString()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r3.put(r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
        L_0x00ac:
            boolean r13 = android.text.TextUtils.isEmpty(r12)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            if (r13 != 0) goto L_0x0118
            com.ali.auth.third.core.history.AccountHistoryManager r13 = com.ali.auth.third.core.history.AccountHistoryManager.getInstance()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            com.ali.auth.third.core.model.HistoryAccount r0 = r13.findHistoryAccount(r12)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            if (r0 == 0) goto L_0x0118
            java.lang.String r10 = r0.tokenKey     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            boolean r13 = android.text.TextUtils.isEmpty(r10)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            if (r13 != 0) goto L_0x0118
            java.util.TreeMap r11 = new java.util.TreeMap     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r11.<init>()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = com.ali.auth.third.offline.login.LoginConstants.KEY_APPKEY     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r14 = com.ali.auth.third.core.context.KernelContext.getAppKey()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            addKey(r11, r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = com.ali.auth.third.offline.login.LoginConstants.KEY_HAVANAID     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r14 = r0.userId     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            addKey(r11, r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = com.ali.auth.third.offline.login.LoginConstants.KEY_TIMESTAMP     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r14 = java.lang.String.valueOf(r8)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            addKey(r11, r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = com.ali.auth.third.offline.login.LoginConstants.KEY_APPVERSION     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r14 = com.ali.auth.third.core.util.CommonUtils.getAndroidAppVersion()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            addKey(r11, r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = com.ali.auth.third.offline.login.LoginConstants.KEY_SDKVERSION     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r14 = com.ali.auth.third.core.context.KernelContext.sdkVersion     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            addKey(r11, r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.Class<com.ali.auth.third.core.service.StorageService> r13 = com.ali.auth.third.core.service.StorageService.class
            java.lang.Object r13 = com.ali.auth.third.core.context.KernelContext.getService(r13)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            com.ali.auth.third.core.service.StorageService r13 = (com.ali.auth.third.core.service.StorageService) r13     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r7 = r13.signMap(r10, r11)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            boolean r13 = android.text.TextUtils.isEmpty(r7)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            if (r13 != 0) goto L_0x0118
            java.lang.String r13 = "deviceTokenSign"
            r3.put(r13, r7)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = "deviceTokenKey"
            r3.put(r13, r10)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = "hid"
            java.lang.String r14 = r0.userId     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r3.put(r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
        L_0x0118:
            java.lang.String r13 = "alimm_"
            org.json.JSONObject r2 = com.ali.auth.third.core.cookies.LoginCookieUtils.getKeyValues(r13)     // Catch:{ Exception -> 0x01a4, JSONException -> 0x019f }
            java.lang.String r13 = "miid"
            java.lang.String r14 = "miid"
            java.lang.String r14 = com.ali.auth.third.core.cookies.LoginCookieUtils.getValue(r14)     // Catch:{ Exception -> 0x01a4, JSONException -> 0x019f }
            r2.put(r13, r14)     // Catch:{ Exception -> 0x01a4, JSONException -> 0x019f }
            com.ali.auth.third.core.config.ConfigManager r13 = com.ali.auth.third.core.config.ConfigManager.getInstance()     // Catch:{ Exception -> 0x01a4, JSONException -> 0x019f }
            java.lang.String r13 = r13.getOfflineDeviceID()     // Catch:{ Exception -> 0x01a4, JSONException -> 0x019f }
            boolean r13 = android.text.TextUtils.isEmpty(r13)     // Catch:{ Exception -> 0x01a4, JSONException -> 0x019f }
            if (r13 != 0) goto L_0x0148
            java.lang.String r13 = "posSN"
            com.ali.auth.third.core.config.ConfigManager r14 = com.ali.auth.third.core.config.ConfigManager.getInstance()     // Catch:{ Exception -> 0x01a4, JSONException -> 0x019f }
            java.lang.String r14 = r14.getOfflineDeviceID()     // Catch:{ Exception -> 0x01a4, JSONException -> 0x019f }
            r2.put(r13, r14)     // Catch:{ Exception -> 0x01a4, JSONException -> 0x019f }
        L_0x0148:
            java.lang.String r13 = "ext"
            r3.put(r13, r2)     // Catch:{ Exception -> 0x01a4, JSONException -> 0x019f }
        L_0x014e:
            java.lang.String r13 = "tokenInfo"
            r4.addParam(r13, r3)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            org.json.JSONObject r6 = new org.json.JSONObject     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r6.<init>()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r14 = "umidToken"
            java.lang.Class<com.ali.auth.third.core.service.StorageService> r13 = com.ali.auth.third.core.service.StorageService.class
            java.lang.Object r13 = com.ali.auth.third.core.context.KernelContext.getService(r13)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            com.ali.auth.third.core.service.StorageService r13 = (com.ali.auth.third.core.service.StorageService) r13     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = r13.getUmid()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r6.put(r14, r13)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = "riskControlInfo"
            r4.addParam(r13, r6)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = "ext"
            org.json.JSONObject r14 = new org.json.JSONObject     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r14.<init>()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r4.addParam(r13, r14)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
        L_0x017c:
            java.lang.Class<com.ali.auth.third.core.service.RpcService> r13 = com.ali.auth.third.core.service.RpcService.class
            java.lang.Object r13 = com.ali.auth.third.core.context.KernelContext.getService(r13)
            com.ali.auth.third.core.service.RpcService r13 = (com.ali.auth.third.core.service.RpcService) r13
            java.lang.Class<com.ali.auth.third.core.model.LoginReturnData> r14 = com.ali.auth.third.core.model.LoginReturnData.class
            com.ali.auth.third.core.model.RpcResponse r5 = r13.invoke(r4, r14)
            return r5
        L_0x018b:
            java.lang.String r14 = "utdid"
            java.lang.Class<com.ali.auth.third.core.service.RpcService> r13 = com.ali.auth.third.core.service.RpcService.class
            java.lang.Object r13 = com.ali.auth.third.core.context.KernelContext.getService(r13)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            com.ali.auth.third.core.service.RpcService r13 = (com.ali.auth.third.core.service.RpcService) r13     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            java.lang.String r13 = r13.getDeviceId()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            r3.put(r14, r13)     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            goto L_0x00ac
        L_0x019f:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x017c
        L_0x01a4:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ JSONException -> 0x019f, Exception -> 0x01a9 }
            goto L_0x014e
        L_0x01a9:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x017c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.auth.third.offline.login.LoginComponent.loginByRefreshToken():com.ali.auth.third.core.model.RpcResponse");
    }

    public static void addKey(Map<String, String> map, String key, String value) {
        map.put(key, value);
    }

    public static RpcResponse<String> logout() {
        RpcRequest request = new RpcRequest();
        request.target = "mtop.taobao.havana.mlogin.logout";
        request.version = "1.0";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("appKey", KernelContext.getAppKey());
            jsonObject.put("sid", CredentialManager.INSTANCE.getInternalSession().sid);
            jsonObject.put(TbAuthConstants.IP, CommonUtils.getLocalIPAddress());
            request.addParam("userId", Long.valueOf(Long.parseLong(CredentialManager.INSTANCE.getInternalSession().user.userId)));
            request.addParam("request", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ((RpcService) KernelContext.getService(RpcService.class)).invoke(request, String.class);
    }

    public static void releaseContract(ContractModel model, RpcRequestCallbackWithCode callbackWithCode) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = "mtop.taobao.tbmpc.relieveContract";
        rpcRequest.version = "1.0";
        try {
            rpcRequest.addParam("appId", model.appId);
            rpcRequest.addParam("alipayUserId", model.alipayUserId);
            rpcRequest.addParam("agreementNo", model.agreementNo);
            rpcRequest.addParam("privateKey", model.privateKey);
            rpcRequest.addParam("publicKey", model.publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((RpcService) KernelContext.getService(RpcService.class)).remoteBusiness(rpcRequest, RpcResponse.class, callbackWithCode);
    }

    public static RpcResponse<String> releaseContract(ContractModel model) {
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.target = "mtop.taobao.tbmpc.relieveContract";
        rpcRequest.version = "1.0";
        try {
            rpcRequest.addParam("appId", model.appId);
            rpcRequest.addParam("alipayUserId", model.alipayUserId);
            rpcRequest.addParam("agreementNo", model.agreementNo);
            rpcRequest.addParam("privateKey", model.privateKey);
            rpcRequest.addParam("publicKey", model.publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return LoginContext.rpcService.invoke(rpcRequest, String.class);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00e9, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x00e9 A[ExcHandler: JSONException (r0v1 'e' org.json.JSONException A[CUSTOM_DECLARE]), Splitter:B:3:0x001f] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.ali.auth.third.core.model.RpcResponse<com.ali.auth.third.core.model.LoginReturnData> loginBySsoToken(java.lang.String r10) {
        /*
            r9 = this;
            r3 = 0
            java.lang.Class<com.ali.auth.third.core.service.UserTrackerService> r6 = com.ali.auth.third.core.service.UserTrackerService.class
            java.lang.Object r6 = com.ali.auth.third.core.context.KernelContext.getService(r6)     // Catch:{ Exception -> 0x00ee }
            com.ali.auth.third.core.service.UserTrackerService r6 = (com.ali.auth.third.core.service.UserTrackerService) r6     // Catch:{ Exception -> 0x00ee }
            java.lang.String r7 = "SSO_TOKEN_LOGIN"
            r8 = 0
            r6.send(r7, r8)     // Catch:{ Exception -> 0x00ee }
            com.ali.auth.third.core.model.RpcRequest r5 = new com.ali.auth.third.core.model.RpcRequest     // Catch:{ Exception -> 0x00ee }
            r5.<init>()     // Catch:{ Exception -> 0x00ee }
            java.lang.String r6 = "com.taobao.mtop.mloginService.ssoLogin"
            r5.target = r6     // Catch:{ Exception -> 0x00ee }
            java.lang.String r6 = "1.0"
            r5.version = r6     // Catch:{ Exception -> 0x00ee }
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00e9 }
            r2.<init>()     // Catch:{ JSONException -> 0x00e9 }
            boolean r6 = com.ali.auth.third.core.context.KernelContext.isMini     // Catch:{ JSONException -> 0x00e9 }
            if (r6 == 0) goto L_0x00d5
            java.lang.String r6 = "app_id"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x00e9 }
            r7.<init>()     // Catch:{ JSONException -> 0x00e9 }
            android.content.Context r8 = com.ali.auth.third.core.context.KernelContext.getApplicationContext()     // Catch:{ JSONException -> 0x00e9 }
            java.lang.String r8 = r8.getPackageName()     // Catch:{ JSONException -> 0x00e9 }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ JSONException -> 0x00e9 }
            java.lang.String r8 = "|"
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ JSONException -> 0x00e9 }
            java.lang.String r8 = com.ali.auth.third.core.util.SystemUtils.getApkPublicKeyDigest()     // Catch:{ JSONException -> 0x00e9 }
            java.lang.StringBuilder r7 = r7.append(r8)     // Catch:{ JSONException -> 0x00e9 }
            java.lang.String r7 = r7.toString()     // Catch:{ JSONException -> 0x00e9 }
            r2.put(r6, r7)     // Catch:{ JSONException -> 0x00e9 }
        L_0x0052:
            java.lang.String r6 = "appName"
            java.lang.String r7 = com.ali.auth.third.core.context.KernelContext.getAppKey()     // Catch:{ JSONException -> 0x00e9 }
            r2.put(r6, r7)     // Catch:{ JSONException -> 0x00e9 }
            java.lang.String r6 = "token"
            r2.put(r6, r10)     // Catch:{ JSONException -> 0x00e9 }
            java.lang.String r6 = "sdkVersion"
            java.lang.String r7 = com.ali.auth.third.core.context.KernelContext.sdkVersion     // Catch:{ JSONException -> 0x00e9 }
            r2.put(r6, r7)     // Catch:{ JSONException -> 0x00e9 }
            java.lang.String r6 = "tokenType"
            java.lang.String r7 = "ssoToken"
            r2.put(r6, r7)     // Catch:{ JSONException -> 0x00e9 }
            java.lang.String r6 = "scene"
            java.lang.String r7 = ""
            r2.put(r6, r7)     // Catch:{ JSONException -> 0x00e9 }
            java.lang.String r6 = "ttid"
            java.lang.String r7 = ""
            r2.put(r6, r7)     // Catch:{ JSONException -> 0x00e9 }
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x00f3, JSONException -> 0x00e9 }
            r1.<init>()     // Catch:{ Exception -> 0x00f3, JSONException -> 0x00e9 }
            java.lang.String r6 = "deviceName"
            java.lang.String r7 = android.os.Build.MODEL     // Catch:{ Exception -> 0x00f3, JSONException -> 0x00e9 }
            r1.put(r6, r7)     // Catch:{ Exception -> 0x00f3, JSONException -> 0x00e9 }
            java.lang.String r6 = "ext"
            r2.put(r6, r1)     // Catch:{ Exception -> 0x00f3, JSONException -> 0x00e9 }
        L_0x0098:
            java.lang.String r6 = "tokenInfo"
            r5.addParam(r6, r2)     // Catch:{ JSONException -> 0x00e9 }
            org.json.JSONObject r4 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00e9 }
            r4.<init>()     // Catch:{ JSONException -> 0x00e9 }
            java.lang.String r7 = "umidToken"
            java.lang.Class<com.ali.auth.third.core.service.StorageService> r6 = com.ali.auth.third.core.service.StorageService.class
            java.lang.Object r6 = com.ali.auth.third.core.context.KernelContext.getService(r6)     // Catch:{ JSONException -> 0x00e9 }
            com.ali.auth.third.core.service.StorageService r6 = (com.ali.auth.third.core.service.StorageService) r6     // Catch:{ JSONException -> 0x00e9 }
            java.lang.String r6 = r6.getUmid()     // Catch:{ JSONException -> 0x00e9 }
            r4.put(r7, r6)     // Catch:{ JSONException -> 0x00e9 }
            java.lang.String r6 = "riskControlInfo"
            r5.addParam(r6, r4)     // Catch:{ JSONException -> 0x00e9 }
            java.lang.String r6 = "ext"
            org.json.JSONObject r7 = new org.json.JSONObject     // Catch:{ JSONException -> 0x00e9 }
            r7.<init>()     // Catch:{ JSONException -> 0x00e9 }
            r5.addParam(r6, r7)     // Catch:{ JSONException -> 0x00e9 }
        L_0x00c6:
            java.lang.Class<com.ali.auth.third.core.service.RpcService> r6 = com.ali.auth.third.core.service.RpcService.class
            java.lang.Object r6 = com.ali.auth.third.core.context.KernelContext.getService(r6)     // Catch:{ Exception -> 0x00ee }
            com.ali.auth.third.core.service.RpcService r6 = (com.ali.auth.third.core.service.RpcService) r6     // Catch:{ Exception -> 0x00ee }
            java.lang.Class<com.ali.auth.third.core.model.LoginReturnData> r7 = com.ali.auth.third.core.model.LoginReturnData.class
            com.ali.auth.third.core.model.RpcResponse r3 = r6.invoke(r5, r7)     // Catch:{ Exception -> 0x00ee }
        L_0x00d4:
            return r3
        L_0x00d5:
            java.lang.String r7 = "utdid"
            java.lang.Class<com.ali.auth.third.core.service.RpcService> r6 = com.ali.auth.third.core.service.RpcService.class
            java.lang.Object r6 = com.ali.auth.third.core.context.KernelContext.getService(r6)     // Catch:{ JSONException -> 0x00e9 }
            com.ali.auth.third.core.service.RpcService r6 = (com.ali.auth.third.core.service.RpcService) r6     // Catch:{ JSONException -> 0x00e9 }
            java.lang.String r6 = r6.getDeviceId()     // Catch:{ JSONException -> 0x00e9 }
            r2.put(r7, r6)     // Catch:{ JSONException -> 0x00e9 }
            goto L_0x0052
        L_0x00e9:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ Exception -> 0x00ee }
            goto L_0x00c6
        L_0x00ee:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x00d4
        L_0x00f3:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ JSONException -> 0x00e9 }
            goto L_0x0098
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.auth.third.offline.login.LoginComponent.loginBySsoToken(java.lang.String):com.ali.auth.third.core.model.RpcResponse");
    }

    public void goQrCodeLogin(Activity activity, Map<String, Object> params) {
        String str;
        String str2;
        SDKLogger.d("login", "goQrCodeLogin start");
        Class cls = QrLoginActivity.class;
        if (params != null) {
            if (params.get("userDefActivity") == null) {
                str2 = "";
            } else {
                str2 = (String) params.get("userDefActivity");
            }
            if (!TextUtils.isEmpty(str2)) {
                try {
                    cls = Class.forName((String) params.get("userDefActivity"));
                } catch (ClassNotFoundException e) {
                }
            }
        }
        Intent intent = new Intent(activity, cls);
        StringBuilder qrCodeUrlSB = new StringBuilder(String.format(ConfigManager.qrCodeLoginUrl, new Object[]{KernelContext.getAppKey()}));
        if (params != null) {
            if (!TextUtils.isEmpty(params.get("domain") == null ? "" : (String) params.get("domain"))) {
                qrCodeUrlSB.append("_").append(params.get("domain"));
            }
        }
        if (params != null) {
            if (params.get(LoginConstants.CONFIG) == null) {
                str = "";
            } else {
                str = (String) params.get(LoginConstants.CONFIG);
            }
            if (!TextUtils.isEmpty(str)) {
                String urlParams = urlParamsFormat((String) params.get(LoginConstants.CONFIG));
                if (TextUtils.isEmpty(urlParams)) {
                    urlParams = "";
                }
                qrCodeUrlSB.append(urlParams);
            }
        }
        intent.putExtra("qrCodeLoginUrl", qrCodeUrlSB.toString());
        intent.putExtra("passwordLoginUrl", ConfigManager.LOGIN_HOST);
        activity.startActivityForResult(intent, RequestCode.OPEN_QR_LOGIN);
    }

    private String urlParamsFormat(String configJson) {
        if (TextUtils.isEmpty(configJson)) {
            return "";
        }
        StringBuilder retStr = new StringBuilder("");
        try {
            JSONObject object = new JSONObject(configJson);
            Iterator<String> itt = object.keys();
            while (itt.hasNext()) {
                String key = itt.next().toString();
                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(object.getString(key))) {
                    retStr.append("&");
                    retStr.append(key);
                    retStr.append("=");
                    retStr.append(object.getString(key));
                }
            }
        } catch (JSONException e) {
        }
        return retStr.toString();
    }
}
