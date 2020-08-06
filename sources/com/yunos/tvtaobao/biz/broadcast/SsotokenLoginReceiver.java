package com.yunos.tvtaobao.biz.broadcast;

import android.accounts.AuthenticatorException;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.ali.auth.third.core.MemberSDK;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.core.service.impl.CredentialManager;
import com.ali.auth.third.offline.login.LoginService;
import com.ali.auth.third.offline.login.callback.LogoutCallback;
import com.ali.user.sso.SsoLogin;
import com.ali.user.sso.SsoManager;
import com.ali.user.sso.SsoResultListener;
import com.ali.user.sso.UserInfo;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.tvtao.user.dclib.ZPDevice;
import com.ut.device.UTDevice;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.payment.MemberSDKLoginStatus;
import com.yunos.tvtaobao.payment.account.AccountUtil;
import com.yunos.tvtaobao.payment.broadcast.BroadcastLogin;
import com.yunos.tvtaobao.payment.request.RequestLoginCallBack;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import com.yunos.tvtaobao.payment.utils.TvTaoUtils;
import com.zhiping.dev.android.logger.ZpLogger;
import com.zhiping.tvtao.payment.alipay.request.ReleaseContractRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.mtop.common.ApiID;
import mtopsdk.mtop.common.DefaultMtopCallback;
import mtopsdk.mtop.common.MtopCallback;
import mtopsdk.mtop.common.MtopFinishEvent;
import mtopsdk.mtop.domain.MethodEnum;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.intf.Mtop;
import org.json.JSONException;
import org.json.JSONObject;

public class SsotokenLoginReceiver extends BroadcastReceiver {
    private static final String ACTION_INTENT = "action";
    private static final String ACTION_SSOTOKEN_LOGIN_RECOGNITION = "com.tvtaobao.common.login";
    /* access modifiers changed from: private */
    public String TAG = "SsotokenLoginReceiver";

    public void onReceive(Context context, Intent intent) {
        if (((Boolean) TvTaoSharedPerference.getSp(CoreApplication.getApplication(), TvTaoSharedPerference.LOGIN23, false)).booleanValue()) {
            String action = intent.getAction();
            ZpLogger.i(this.TAG, this.TAG + ".onReceive.intent = " + intent);
            if (action.equals(ACTION_SSOTOKEN_LOGIN_RECOGNITION) && !isAppOnForeground(context)) {
                ZpLogger.e(this.TAG, this.TAG + "==二三方接收广播");
                int login = intent.getIntExtra("action", 0);
                ZpLogger.i(this.TAG, this.TAG + ".onReceive.login = " + login);
                if (login == AccountUtil.ACTION.LOGIN_ACTION.getVal()) {
                    if (!CoreApplication.getLoginHelper(CoreApplication.getApplication()).isLogin()) {
                        ZpLogger.i(this.TAG, this.TAG + "onReceive.login" + intent);
                        if (((LoginService) MemberSDK.getService(LoginService.class)) != null) {
                            final SsoLogin ssoLogin = new SsoLogin(CoreApplication.getApplication());
                            ZpLogger.e(this.TAG, "new SsoLogin");
                            new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        Map<String, String> p = new HashMap<>();
                                        p.put("appkey", "23039499");
                                        Utils.utCustomHit("", "Request_GetSsoProperties", p);
                                        ssoLogin.getSsoTokenWithType(ssoLogin.taobaoAccountType(), new SsoResultListener() {
                                            public void onFailed(String s) {
                                                ZpLogger.e(SsotokenLoginReceiver.this.TAG, s == null ? "" : s.toString());
                                            }

                                            public void onSuccess(UserInfo userInfo) {
                                                LoginService loginService = (LoginService) MemberSDK.getService(LoginService.class);
                                                if (loginService != null) {
                                                    loginService.loginBySsoToken(userInfo.mSsoToken, new LoginCallback() {
                                                        public void onFailure(int i, String s) {
                                                            ZpLogger.e(SsotokenLoginReceiver.this.TAG, s.toString());
                                                        }

                                                        public void onSuccess(Session session) {
                                                            ZpLogger.e(SsotokenLoginReceiver.this.TAG, session.toString());
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } catch (SsoManager.UnauthorizedAccessException e) {
                                        e.printStackTrace();
                                    } catch (AuthenticatorException e2) {
                                        e2.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                } else if (CoreApplication.getLoginHelper(CoreApplication.getApplication()).isLogin()) {
                    try {
                        Mtop.instance(context).build((MtopRequest) new RequestLoginCallBack(User.getUserId(), getExtParams(), false), (String) null).useWua().reqMethod(MethodEnum.POST).addListener(new MtopCallback.MtopFinishListener() {
                            public void onFinished(MtopFinishEvent mtopFinishEvent, Object o) {
                                ZpLogger.d(SsotokenLoginReceiver.this.TAG, "logout callback: " + mtopFinishEvent.getMtopResponse().getDataJsonObject());
                            }
                        }).asyncRequest();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MemberSDKLoginStatus.setLoggingOut(true);
                    ((LoginService) MemberSDK.getService(LoginService.class)).logout(new LogoutCallback() {
                        public void onSuccess() {
                            ZpLogger.d(SsotokenLoginReceiver.this.TAG, " memberSDK loginOut succsss");
                            CredentialManager.INSTANCE.logout();
                            BroadcastLogin.sendBroadcastLogin(CoreApplication.getApplication(), false);
                            MemberSDKLoginStatus.setLoggingOut(false);
                        }

                        public void onFailure(int i, String s) {
                            ZpLogger.d(SsotokenLoginReceiver.this.TAG, " memberSDK loginOut failure");
                            MemberSDKLoginStatus.setLoggingOut(false);
                        }
                    });
                    ApiID asyncRequest = Mtop.instance(CoreApplication.getApplication()).build((Object) new ReleaseContractRequest(), (String) null).useWua().addListener(new DefaultMtopCallback() {
                        public void onFinished(MtopFinishEvent event, Object context) {
                            ZpLogger.d(SsotokenLoginReceiver.this.TAG, "releaseContract response: " + event.getMtopResponse().getDataJsonObject());
                            super.onFinished(event, context);
                        }
                    }).asyncRequest();
                    AccountUtil.clearAccountInfo(context);
                    AccountUtil.notifyListener(context, AccountUtil.ACTION.LOGOUT_ACTION);
                }
            }
        } else {
            ZpLogger.e(this.TAG, this.TAG + "==二三方没有没有没有接收广播");
        }
    }

    private String getExtParams() {
        JSONObject extParams = new JSONObject();
        try {
            extParams.put("uuid", CloudUUIDWrapper.getCloudUUID());
            extParams.put("wua", Config.getWua(CoreApplication.getApplication()));
            extParams.put("utdid", UTDevice.getUtdid(CoreApplication.getApplication()));
            extParams.put("umtoken", TvTaoUtils.getUmtoken(CoreApplication.getApplication()));
            String zpDid = ZPDevice.getZpDid((Context) null);
            String uid = ZPDevice.getAugurZpId((Context) null);
            if (!TextUtils.isEmpty(zpDid)) {
                extParams.put("zpDid", zpDid);
            }
            if (!TextUtils.isEmpty(uid)) {
                extParams.put("augurZpUid", uid);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return extParams.toString();
    }

    private boolean isAppOnForeground(Context context) {
        String packageName = context.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = ((ActivityManager) context.getApplicationContext().getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == 100) {
                return true;
            }
        }
        return false;
    }
}
