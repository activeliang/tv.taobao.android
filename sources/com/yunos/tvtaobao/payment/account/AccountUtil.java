package com.yunos.tvtaobao.payment.account;

import android.accounts.AuthenticatorException;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.ali.auth.third.core.MemberSDK;
import com.ali.auth.third.core.callback.LoginCallback;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.offline.login.LoginService;
import com.ali.user.sso.SsoLogin;
import com.ali.user.sso.SsoManager;
import com.tvtaobao.android.runtime.RtBaseEnv;
import com.ut.mini.UTAnalytics;
import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.HashMap;
import java.util.Map;

public class AccountUtil {
    public static final String EVENT_LOGIN = "EVENT_LOGIN";
    /* access modifiers changed from: private */
    public static final String TAG = AccountUtil.class.getSimpleName();

    public interface AlipayAuthListener {
        void onAuthQrGenerated(String str);

        void onAuthTokenGet(String str);
    }

    public interface AlipayLoginListener {
        void onLoginFailure(int i, String str);

        void onLoginSuccess(Session session);
    }

    public static void saveAccountInfo(final Context context, final Session session) {
        ZpLogger.e(TAG, "saveAccountInfo");
        final SsoLogin ssoLogin = new SsoLogin(context);
        new Thread(new Runnable() {
            public void run() {
                try {
                    ssoLogin.shareSsoToken(session.ssoToken, session.nick, "", ssoLogin.taobaoAccountType());
                    TvTaoSharedPerference.saveSp(context, TvTaoSharedPerference.NICK, session.nick);
                } catch (SsoManager.UnauthorizedAccessException e) {
                    e.printStackTrace();
                } catch (AuthenticatorException e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
    }

    public static void clearAccountInfo(Context context) {
        ZpLogger.e(TAG, "clearAccountInfo");
        final SsoLogin ssoLogin = new SsoLogin(context);
        new Thread(new Runnable() {
            public void run() {
                try {
                    ssoLogin.logout(ssoLogin.taobaoAccountType());
                } catch (AuthenticatorException e) {
                    e.printStackTrace();
                } catch (SsoManager.UnauthorizedAccessException e2) {
                    e2.printStackTrace();
                }
            }
        }).start();
    }

    public static void doLoginByAlipayToken(final Context context, String alipayToken, final AlipayLoginListener alipayLoginListener) {
        Map<String, String> map = new HashMap<>();
        map.put("token", alipayToken);
        ((LoginService) MemberSDK.getService(LoginService.class)).loginWithAuthCode(map, new LoginCallback() {
            public void onSuccess(Session session) {
                if (session != null && !TextUtils.isEmpty(session.nick)) {
                    UTAnalytics.getInstance().updateUserAccount(session.nick, session.userid, (String) null);
                    ZpLogger.e(AccountUtil.TAG, "UT用户信息传入：session.nick:" + session.nick + "++session.userid:" + session.userid);
                }
                RtBaseEnv.broadcast(new RtBaseEnv.Msg(AccountUtil.EVENT_LOGIN, new LoginResult(true, session)));
                AccountUtil.saveAccountInfo(context, session);
                AccountUtil.notifyListener(context, ACTION.LOGIN_ACTION);
                if (alipayLoginListener != null) {
                    try {
                        alipayLoginListener.onLoginSuccess(session);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onFailure(int i, String s) {
                RtBaseEnv.broadcast(new RtBaseEnv.Msg(AccountUtil.EVENT_LOGIN, new LoginResult(false, i, s)));
                if (alipayLoginListener != null) {
                    try {
                        alipayLoginListener.onLoginFailure(i, s);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public static void notifyListener(Context context, ACTION action) {
        ZpLogger.e(TAG, "notifyListener:" + action);
        Intent intent = new Intent();
        intent.putExtra("action", action.val);
        intent.putExtra(CommonData.KEY_PACKAGE_NAME, "com.yunos.tvtaobao");
        intent.setAction("com.tvtaobao.common.login");
        context.sendBroadcast(intent);
    }

    public enum ACTION {
        LOGOUT_ACTION(0),
        LOGIN_ACTION(1);
        
        /* access modifiers changed from: private */
        public int val;

        private ACTION(int action) {
            this.val = action;
        }

        public int getVal() {
            return this.val;
        }

        public void setVal(int val2) {
            this.val = val2;
        }
    }

    public static class LoginResult {
        public int i;
        public String s;
        public Session session;
        public boolean successResult = false;

        public LoginResult(boolean successResult2, Session session2) {
            this.successResult = successResult2;
            this.session = session2;
        }

        public LoginResult(boolean successResult2, int i2, String s2) {
            this.successResult = successResult2;
            this.i = i2;
            this.s = s2;
        }
    }
}
