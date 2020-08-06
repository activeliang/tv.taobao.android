package com.taobao.alimama.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.taobao.alimama.io.SharedPreferencesUtils;
import com.taobao.tao.remotebusiness.login.RemoteLogin;
import com.taobao.utils.ILoginInfoGetter;
import com.taobao.utils.LoginInfo;

public class LoginManager {
    private static volatile ILoginInfoGetter a;

    private static class a implements ILoginInfoGetter {
        private static final String a = "login_info";

        /* renamed from: a  reason: collision with other field name */
        private C0005a f4a;

        /* renamed from: a  reason: collision with other field name */
        private LoginInfo f5a;

        /* renamed from: com.taobao.alimama.login.LoginManager$a$a  reason: collision with other inner class name */
        private static class C0005a {
            private LoginInfo a;

            private C0005a() {
            }

            /* access modifiers changed from: private */
            public LoginInfo a() {
                if (this.a == null) {
                    try {
                        String string = SharedPreferencesUtils.getString(a.a, "");
                        if (!TextUtils.isEmpty(string)) {
                            this.a = (LoginInfo) JSON.parseObject(string, LoginInfo.class);
                        }
                    } catch (Exception e) {
                    }
                }
                return this.a;
            }

            /* access modifiers changed from: private */
            public void a(LoginInfo loginInfo) {
                this.a = loginInfo;
                SharedPreferencesUtils.putString(a.a, JSON.toJSONString(loginInfo));
            }
        }

        private a() {
            this.f5a = new LoginInfo();
            this.f4a = new C0005a();
        }

        private void a() {
            LoginInfo from = LoginInfo.from(RemoteLogin.getLoginContext());
            if (from.isValid() && !from.equals(this.f5a)) {
                this.f4a.a(from);
            }
            this.f5a = from;
        }

        public LoginInfo getLastLoginUserInfo() {
            a();
            return this.f5a.isValid() ? this.f5a : this.f4a.a();
        }

        public LoginInfo getLoginUserInfo() {
            a();
            return this.f5a;
        }
    }

    public static ILoginInfoGetter getLoginInfoGetter() {
        if (a == null) {
            a = new a();
        }
        return a;
    }

    public static void setLoginInfoGetter(@NonNull ILoginInfoGetter iLoginInfoGetter) {
        a = iLoginInfoGetter;
    }
}
