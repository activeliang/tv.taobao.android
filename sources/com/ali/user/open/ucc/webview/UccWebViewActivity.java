package com.ali.user.open.ucc.webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Toast;
import com.ali.auth.third.core.model.Constants;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.callback.CallbackManager;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.service.StatusBarService;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.DialogHelper;
import com.ali.user.open.core.util.ParamsConstants;
import com.ali.user.open.core.webview.BaseWebViewActivity;
import com.ali.user.open.ucc.R;
import com.ali.user.open.ucc.UccCallback;
import com.ali.user.open.ucc.UccService;
import com.ali.user.open.ucc.biz.UccBindPresenter;
import com.ali.user.open.ucc.biz.UccTrustLoginPresenter;
import com.ali.user.open.ucc.context.UccContext;
import com.ali.user.open.ucc.data.ApiConstants;
import com.ali.user.open.ucc.model.UccParams;
import com.ali.user.open.ucc.util.UTHitConstants;
import com.ali.user.open.ucc.util.UTHitUtils;
import com.ali.user.open.ucc.util.UccConstants;
import com.ali.user.open.ucc.util.Utils;
import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;

public class UccWebViewActivity extends BaseWebViewActivity {
    private final String TAG = BaseWebViewActivity.class.getSimpleName();
    private String mH5RequestToken;
    private String mNeedCookieOnly = "0";
    private String mNeedLocalSession = "1";
    private String mNeedSession = "0";
    private String mNeedToast = "0";
    private Map<String, String> mParams;
    private UccParams mUccParams;
    public String scene;
    public String token;

    /* access modifiers changed from: protected */
    public int getLayout() {
        if (AliMemberSDK.getService(StatusBarService.class) == null || ((StatusBarService) AliMemberSDK.getService(StatusBarService.class)).getWebLayout() <= 0) {
            return R.layout.ali_user_ucc_webview;
        }
        return ((StatusBarService) AliMemberSDK.getService(StatusBarService.class)).getWebLayout();
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        try {
            if (AliMemberSDK.getService(StatusBarService.class) != null) {
                ((StatusBarService) AliMemberSDK.getService(StatusBarService.class)).setStatusBar(this);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        if (UccContext.getBindComponentProxy() != null) {
            KernelContext.IS_HAVE_WEBVIEW = false;
        }
        super.onCreate(savedInstanceState);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initParams(intent);
    }

    /* access modifiers changed from: protected */
    public void loadUrl(String url) {
        Uri uri = Uri.parse(url);
        Bundle bundle = serialBundle(uri.getQuery());
        if (bundle != null) {
            this.mH5RequestToken = bundle.getString(ParamsConstants.UrlConstant.H5_REQUEST_TOKEN);
        }
        if (checkWebviewBridge(url)) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
            overrideCallback(uri);
            return;
        }
        this.memberWebView.loadUrl(url);
    }

    /* access modifiers changed from: protected */
    public void initParams(Intent intent) {
        super.initParams(intent);
        if (intent != null) {
            this.mUccParams = (UccParams) JSON.parseObject(intent.getStringExtra(UccConstants.PARAM_UCC_PARAMS), UccParams.class);
            this.mNeedSession = intent.getStringExtra("needSession");
            this.mNeedCookieOnly = intent.getStringExtra(ParamsConstants.Key.PARAM_NEED_LOCAL_COOKIE_ONLY);
            this.token = intent.getStringExtra("token");
            this.scene = intent.getStringExtra("scene");
            this.mNeedToast = intent.getStringExtra(ParamsConstants.Key.PARAM_NEED_TOAST);
            String bizParamsStr = intent.getStringExtra("params");
            if (!TextUtils.isEmpty(bizParamsStr)) {
                this.mParams = Utils.convertJsonStrToMap(bizParamsStr);
            }
            this.mNeedLocalSession = intent.getStringExtra(ParamsConstants.Key.PARAM_NEED_LOCAL_SESSION);
        }
    }

    private void hideTitleBar(Uri uri) {
        if (uri != null) {
            String hideTitleBar = uri.getQueryParameter("hideTitleBar");
            if (getSupportActionBar() != null && TextUtils.equals(hideTitleBar, "true")) {
                getSupportActionBar().hide();
            }
        }
    }

    private void showTitleBar(Uri uri) {
        if (uri != null) {
            String hideTitleBar = uri.getQueryParameter("hideTitleBar");
            if (getSupportActionBar() != null && !TextUtils.equals(hideTitleBar, "true")) {
                getSupportActionBar().show();
            }
        }
    }

    private boolean overrideCallback(Uri uri) {
        Bundle bundle = serialBundle(uri.getQuery());
        if (bundle == null) {
            bundle = new Bundle();
        }
        checkUccParam(bundle);
        String action = bundle.getString("action");
        if (TextUtils.isEmpty(action) || TextUtils.equals(Constants.ACTION_QUIT, action)) {
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_Cancel", this.mUccParams, new HashMap());
            finish();
            UccCallback callback = (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE));
            if (callback != null && TextUtils.equals("true", bundle.getString("isSuc"))) {
                callback.onSuccess(this.mUccParams.bindSite, (Map) null);
            }
            return true;
        } else if (TextUtils.equals("close", action)) {
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_Cancel", this.mUccParams, new HashMap());
            UccCallback callback2 = (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE));
            finish();
            if (callback2 != null) {
                callback2.onFail(this.mUccParams.bindSite, 1403, getResources().getString(R.string.member_sdk_cancel));
            }
            return true;
        } else if (TextUtils.equals("bind", action)) {
            String requestToken = bundle.getString("requestToken");
            String bindUserToken = bundle.getString("bindUserToken");
            String type = bundle.getString("type");
            if (this.mUccParams == null) {
                this.mUccParams = new UccParams();
                this.mUccParams.traceId = Utils.generateTraceId("h5");
            }
            if ("needUpgrade".equals(type)) {
                this.mUccParams.needUpgrade = "true";
            }
            if (!TextUtils.isEmpty(bundle.getString("scene"))) {
                this.mUccParams.scene = bundle.getString("scene");
            }
            UccBindPresenter.getInstance().bindAfterRecommend(this, requestToken, this.mUccParams, bindUserToken, this.mNeedToast, this.mParams, (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE)));
            return true;
        } else if (TextUtils.equals("bindAfterIdentify", action)) {
            String requestToken2 = bundle.getString(ParamsConstants.UrlConstant.H5_REQUEST_TOKEN);
            String ivToken = bundle.getString(Constants.PARAM_HAVANA_IV_TOKEN);
            String bindUserToken2 = bundle.getString("userBindToken");
            addSessionParam(bundle);
            UccBindPresenter.getInstance().bindIdentify(this, requestToken2, this.mUccParams, ivToken, bindUserToken2, this.mNeedToast, this.mParams, (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE)));
            return true;
        } else if (TextUtils.equals(Constants.ACTION_CONTINUELOGIN, action)) {
            String h5QueryString = uri.getQuery();
            addSessionParam(bundle);
            UccTrustLoginPresenter.getInstance().tokenLogin(this, this.mUccParams, this.scene, this.token, h5QueryString, this.mParams, (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE)));
            return true;
        } else if (TextUtils.equals(Constants.ACTION_TRUSTLOGIN, action)) {
            String h5token = bundle.getString("token");
            String loginScene = bundle.getString("scene");
            String h5QueryString2 = uri.getQuery();
            if (TextUtils.isEmpty(loginScene)) {
                loginScene = this.scene;
            }
            UccTrustLoginPresenter.getInstance().tokenLogin(this, this.mUccParams, loginScene, h5token, h5QueryString2, (Map<String, String>) null, (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE)));
            return true;
        } else if (TextUtils.equals("login", action)) {
            Map<String, String> mapParams = generateMap(bundle);
            mapParams.put("needSession", "1");
            UccBindPresenter.getInstance().bindByRequestToken(this, this.mUccParams, bundle.getString(ParamsConstants.UrlConstant.H5_REQUEST_TOKEN), bundle.getString("userToken"), bundle.getString("tokenType"), mapParams, (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE)));
            return true;
        } else if (TextUtils.equals("taobao_auth_token", action)) {
            addSessionParam(bundle);
            if (this.mParams == null || !TextUtils.equals(this.mParams.get("scene"), ParamsConstants.UrlConstant.NEW_YOUKU_UPGRADE)) {
                Map<String, String> mapParams2 = generateMap(bundle);
                mapParams2.put("needSession", this.mNeedSession);
                mapParams2.put(ParamsConstants.Key.PARAM_NEED_LOCAL_SESSION, this.mNeedLocalSession);
                if (this.mParams != null) {
                    mapParams2.put(ParamsConstants.Key.PARAM_NEED_LOCAL_COOKIE_ONLY, this.mParams.get(ParamsConstants.Key.PARAM_NEED_LOCAL_COOKIE_ONLY));
                }
                UccBindPresenter.getInstance().bindByNativeAuth(this, this.mUccParams, bundle.getString("top_auth_code"), "oauthcode", mapParams2, (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE)));
            } else {
                Map<String, String> mapParams3 = generateMap(bundle);
                mapParams3.put("needSession", "1");
                if (ApiConstants.ApiName.BIND_BY_REQUEST_TOKEN.equals(bundle.getString("api"))) {
                    this.mUccParams.topAuthCode = bundle.getString("top_auth_code");
                    this.mUccParams.requestToken = bundle.getString(ParamsConstants.UrlConstant.H5_REQUEST_TOKEN);
                    UccBindPresenter.getInstance().bindByRequestToken(this, this.mUccParams, bundle.getString(ParamsConstants.UrlConstant.H5_REQUEST_TOKEN), bundle.getString("top_auth_code"), "oauthcode", mapParams3, (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE)));
                } else {
                    UccBindPresenter.getInstance().getUserInfo(this, this.mUccParams, bundle.getString("top_auth_code"), "oauthcode", "h5", mapParams3, (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE)));
                }
            }
            return true;
        } else if (TextUtils.equals("registerSuc", action) || TextUtils.equals("afterBindMobile", action)) {
            if (TextUtils.equals("registerSuc", action)) {
                UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_registerSuc", this.mUccParams, (Map<String, String>) null);
            }
            String message = bundle.getString("message");
            if (TextUtils.equals(this.mNeedSession, "1")) {
                UccBindPresenter.getInstance().tokenLoginAfterBind(this, this.mUccParams, bundle.getString("trustToken"), action, this.mNeedToast, message, this.mParams, (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE)));
            } else {
                if (!TextUtils.isEmpty(message) && TextUtils.equals(this.mNeedToast, "1")) {
                    Toast.makeText(getApplicationContext(), message, 0).show();
                }
                UccCallback uccCallback = (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE));
                if (uccCallback != null) {
                    uccCallback.onSuccess(this.mUccParams.bindSite, (Map) null);
                }
                finish();
            }
            return true;
        } else if (TextUtils.equals("UCC_ContinueLogin", action)) {
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccBindWithIbb_H5Skip", this.mUccParams, (Map<String, String>) null);
            String requestToken3 = bundle.getString("requestToken");
            String userToken = bundle.getString("userToken");
            String userAction = bundle.getString(ApiConstants.ApiField.USER_ACTION);
            if (TextUtils.isEmpty(requestToken3)) {
                finish();
            } else {
                Map<String, String> mapParams4 = generateMap(bundle);
                mapParams4.put("needSession", "1");
                mapParams4.put(ParamsConstants.Key.PARAM_NEED_LOCAL_SESSION, this.mNeedLocalSession);
                this.mUccParams.requestToken = requestToken3;
                this.mUccParams.userToken = userToken;
                this.mUccParams.userAction = userAction;
                UccBindPresenter.getInstance().skipUpgrade(this, this.mUccParams, "h5", mapParams4, (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE)));
            }
            finish();
            return true;
        } else if (TextUtils.equals("skip_bind", action)) {
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccBindWithIbb_H5Skip", this.mUccParams, (Map<String, String>) null);
            UccCallback uccCallback2 = (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE));
            if (uccCallback2 != null) {
                uccCallback2.onFail(this.mUccParams.bindSite, 1011, "跳过绑定");
            }
            finish();
            return true;
        } else if (!TextUtils.equals("UCC_Upgrade", action)) {
            return false;
        } else {
            if (this.mUccParams == null) {
                this.mUccParams = new UccParams();
                this.mUccParams.traceId = Utils.generateTraceId("h5");
            }
            String bindSite = bundle.getString("bindSite");
            String requestToken4 = bundle.getString("requestToken");
            String sceneCode = bundle.getString(ParamsConstants.Key.PARAM_SCENE_CODE);
            String scene2 = bundle.getString("scene");
            String string = bundle.getString("site");
            String h5Only = bundle.getString(ParamsConstants.Key.PARAM_H5ONLY);
            String h5Url = bundle.getString(ParamsConstants.Key.PARAM_H5URL);
            Map<String, String> map = generateMap(bundle);
            map.put("needSession", "1");
            map.put(ParamsConstants.Key.PARAM_NEED_LOCAL_SESSION, this.mNeedLocalSession);
            if (!TextUtils.isEmpty(sceneCode)) {
                map.put(ParamsConstants.Key.PARAM_SCENE_CODE, sceneCode);
            }
            String type2 = bundle.getString("type");
            if ("rpc".equals(type2)) {
                UccTrustLoginPresenter.getInstance().upgradeLogin(this, this.mUccParams, bindSite, scene2, requestToken4, "h5", map, (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE)));
            } else if ("launchTao".equals(type2)) {
                String userToken2 = bundle.getString("userToken");
                if ("true".equals(h5Only)) {
                    map.put(ParamsConstants.Key.PARAM_H5ONLY, "1");
                }
                if (!TextUtils.isEmpty(h5Url)) {
                    String h5Url2 = h5Url + "&request_token=" + requestToken4;
                    String env = bundle.getString("env");
                    if (!TextUtils.isEmpty(env)) {
                        h5Url2 = h5Url2 + "&env=" + env;
                    }
                    String appEntrance = bundle.getString("appEntrance");
                    if (!TextUtils.isEmpty(appEntrance)) {
                        h5Url2 = h5Url2 + "&appEntrance=" + appEntrance;
                    }
                    String needTopToken = bundle.getString("needTopToken");
                    if (!TextUtils.isEmpty(needTopToken)) {
                        h5Url2 = h5Url2 + "&needTopToken=" + needTopToken;
                    }
                    if (!TextUtils.isEmpty(bundle.getString("topTokenAppName"))) {
                        h5Url2 = h5Url2 + "&topTokenAppName=" + bundle.getString("topTokenAppName");
                    }
                    String redirectUri = bundle.getString("redirectUri");
                    if (!TextUtils.isEmpty(redirectUri)) {
                        h5Url2 = h5Url2 + "&redirectUri=" + redirectUri;
                    }
                    map.put(UccConstants.PARAM_BIND_URL, h5Url2);
                }
                map.put("from", action);
                map.put("requestToken", requestToken4);
                ((UccService) AliMemberSDK.getService(UccService.class)).bind(this, userToken2, bindSite, map, (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE)));
            } else {
                finish();
            }
            return true;
        }
    }

    private void addSessionParam(Bundle bundle) {
        if (this.mParams == null) {
            this.mParams = generateMap(bundle);
        }
        this.mParams.put("needSession", this.mNeedSession);
        this.mParams.put(ParamsConstants.Key.PARAM_NEED_LOCAL_SESSION, this.mNeedLocalSession);
    }

    private void checkUccParam(Bundle bundle) {
        if (this.mUccParams == null) {
            this.mUccParams = new UccParams();
            this.mUccParams.traceId = Utils.generateTraceId("h5");
        }
        String bindSite = bundle.getString("bindSite");
        String userToken = bundle.getString("userToken");
        if (!TextUtils.isEmpty(bindSite)) {
            this.mUccParams.bindSite = bindSite;
        }
        if (!TextUtils.isEmpty(userToken)) {
            this.mUccParams.userToken = userToken;
        }
    }

    @NonNull
    private Map<String, String> generateMap(Bundle bundle) {
        String scene2 = bundle.getString("scene");
        String site = bundle.getString("site");
        String bindSite = bundle.getString("bindSite");
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(site)) {
            map.put("site", site);
        }
        if (!TextUtils.isEmpty(bindSite)) {
            map.put("bindSite", bindSite);
        }
        if (!TextUtils.isEmpty(scene2)) {
            map.put("scene", scene2);
        }
        return map;
    }

    public void onBackHistory() {
        if (this.memberWebView == null || !this.memberWebView.canGoBack() || (!this.memberWebView.getUrl().contains("authorization-notice") && !this.memberWebView.getUrl().contains("agreement"))) {
            UTHitUtils.send(UTHitConstants.PageUccBind, "UccBind_Cancel", this.mUccParams, new HashMap<>());
            UccCallback callback = (UccCallback) CallbackManager.getCallback(Integer.valueOf(UccConstants.UCC_H5_CALLBACK_TYPE));
            if (callback != null) {
                if (this.mUccParams == null) {
                    this.mUccParams = new UccParams();
                }
                callback.onFail(this.mUccParams.bindSite, 1403, getResources().getString(R.string.member_sdk_cancel));
            }
            finish();
            return;
        }
        this.memberWebView.goBack();
    }

    public boolean shouldOverrideUrlLoading(String url) {
        SDKLogger.d(this.TAG, "shouldOverrideUrlLoading url=" + url);
        Uri uri = Uri.parse(url);
        if (checkWebviewBridge(url)) {
            return overrideCallback(uri);
        }
        this.memberWebView.loadUrl(url);
        return true;
    }

    public void onPageStarted(String url) {
        hideTitleBar(Uri.parse(url));
        SDKLogger.d(this.TAG, "onPageStarted url=" + url);
    }

    public void onPageFinished(String url) {
        SDKLogger.d(this.TAG, "onPageFinished url=" + url);
        showTitleBar(Uri.parse(url));
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        DialogHelper.getInstance().dismissAlertDialog(this);
        super.onDestroy();
    }
}
