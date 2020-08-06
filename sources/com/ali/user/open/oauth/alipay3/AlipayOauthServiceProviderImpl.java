package com.ali.user.open.oauth.alipay3;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.service.MemberExecutorService;
import com.ali.user.open.core.service.UserTrackerService;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.ParamsConstants;
import com.ali.user.open.oauth.AppCredential;
import com.ali.user.open.oauth.OauthCallback;
import com.ali.user.open.oauth.base.BaseOauthServiceProviderImpl;
import com.ali.user.open.tbauth.context.TbAuthContext;
import com.alipay.sdk.app.AuthTask;
import java.util.HashMap;
import java.util.Map;

public class AlipayOauthServiceProviderImpl extends BaseOauthServiceProviderImpl {
    public static final String TAG = "AlipayOauthServiceProviderImpl";
    private boolean mLoginAfterOauth = true;

    public boolean isLoginUrl(String url) {
        return false;
    }

    public void oauth(Activity activity, String oauthSite, AppCredential appCredential, Map<String, String> map, OauthCallback oauthCallback) {
        if (activity == null || appCredential == null) {
            oauthCallback.onFail(oauthSite, 102, "param is null");
        } else if (TextUtils.isEmpty(appCredential.appKey) || TextUtils.isEmpty(appCredential.pid) || TextUtils.isEmpty(appCredential.signType)) {
            oauthCallback.onFail(oauthSite, 101, "app credential is null");
        } else {
            this.mLoginAfterOauth = true;
            Map<String, String> props = new HashMap<>();
            props.put(ParamsConstants.Key.PARAM_TRACE_ID, TbAuthContext.traceId);
            ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_AlipayOauth", "Page_AlipayOauth_Invoke", props);
            authTask(activity, oauthSite, appCredential, oauthCallback);
        }
    }

    private void authTask(final Activity activity, final String oauthSite, AppCredential appCredential, final OauthCallback oauthCallback) {
        SignRequest signRequest = new SignRequest();
        signRequest.app_id = appCredential.appKey;
        signRequest.pid = appCredential.pid;
        signRequest.sign_type = appCredential.signType;
        signRequest.target_id = appCredential.targetId;
        AlipayRpcPresenter.getAlipaySign((GetSignCallback) new GetSignCallback() {
            public void onGetSignSuccessed(final String sign) {
                ((MemberExecutorService) AliMemberSDK.getService(MemberExecutorService.class)).postTask(new Runnable() {
                    public void run() {
                        Map<String, String> props = new HashMap<>();
                        props.put(ParamsConstants.Key.PARAM_TRACE_ID, TbAuthContext.traceId);
                        ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_AlipayOauth", "Page_AlipayOauth_signResult", props);
                        SDKLogger.d(AlipayOauthServiceProviderImpl.TAG, "sign=" + sign);
                        Map<String, String> result = new AuthTask(activity).authV2(sign, true);
                        if (result != null) {
                            SDKLogger.d(AlipayOauthServiceProviderImpl.TAG, "result=" + result.toString());
                            String resultString = result.get("result");
                            if (!TextUtils.isEmpty(resultString)) {
                                Map<String, String> resultparams = new HashMap<>();
                                String[] arrs = resultString.split("&");
                                String auth_code = "";
                                if (arrs != null) {
                                    for (String arr : arrs) {
                                        String[] key_value = arr.split("=");
                                        if (key_value != null && key_value.length == 2) {
                                            if ("auth_code".equals(key_value[0])) {
                                                auth_code = key_value[1];
                                                resultparams.put("authCode", auth_code);
                                            } else if ("alipay_open_id".equals(key_value[0])) {
                                                resultparams.put("openId", key_value[1]);
                                            } else if ("user_id".equals(key_value[0])) {
                                                resultparams.put("userId", key_value[1]);
                                            }
                                        }
                                    }
                                }
                                Map<String, String> prop = new HashMap<>();
                                if (!TextUtils.isEmpty(auth_code)) {
                                    prop.put("is_success", "T");
                                    prop.put("authCode", resultparams.get("authCode"));
                                    prop.put("openId", resultparams.get("openId"));
                                    prop.put("userId", resultparams.get("userId"));
                                    oauthCallback.onSuccess(oauthSite, resultparams);
                                } else {
                                    prop.put("is_success", "F");
                                    prop.put("code", "202");
                                    oauthCallback.onFail(oauthSite, 202, "");
                                }
                                prop.put(ParamsConstants.Key.PARAM_TRACE_ID, TbAuthContext.traceId);
                                ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_AlipayOauth", "Page_AlipayOauth_Result", prop);
                                return;
                            }
                            oauthCallback.onFail(oauthSite, 201, result.get("memo"));
                        }
                    }
                });
            }

            public void onFailure(int code, String msg) {
                Map<String, String> props = new HashMap<>();
                props.put("code", code + "");
                props.put(ParamsConstants.Key.PARAM_TRACE_ID, TbAuthContext.traceId);
                ((UserTrackerService) AliMemberSDK.getService(UserTrackerService.class)).send("Page_AlipayOauth", "Page_AlipayOauth_signResult", props);
                oauthCallback.onFail(oauthSite, code, msg);
            }
        }, signRequest);
    }

    public void logout(Context context) {
    }

    public boolean isAppAuthSurpport(Context context) {
        if (context == null) {
            context = KernelContext.getApplicationContext();
        }
        try {
            context.getPackageManager().getPackageInfo("com.eg.android.AlipayGphone", 0);
            return true;
        } catch (Throwable th) {
            return false;
        }
    }
}
