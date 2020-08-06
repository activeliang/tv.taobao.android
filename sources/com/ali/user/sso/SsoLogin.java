package com.ali.user.sso;

import android.accounts.Account;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import com.ali.auth.third.core.MemberSDK;
import com.ali.user.sso.SsoManager;
import com.ali.user.sso.internal.Authenticator;
import com.ali.user.sso.internal.CalledFromWrongThreadException;
import com.ali.user.sso.internal.Whitelist;
import com.taobao.tao.remotebusiness.IRemoteBaseListener;
import com.taobao.tao.remotebusiness.MtopBusiness;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import mtopsdk.mtop.common.MtopListener;
import mtopsdk.mtop.domain.MtopRequest;
import mtopsdk.mtop.domain.MtopResponse;
import org.json.JSONObject;

public class SsoLogin {
    public static final String DEFAULT_TAOBAO_USERNAME = "淘宝账号";
    private static final String PRE_REMOTE_VERSION = "remoteversion";
    private static final String PRE_TIME_IN_MILLIS = "pretimestamp";
    public static long SSO_EXPIRE_TIME_IN_MILLIS = 1209600000;
    private static final String SSO_EXPIRE_TIME_IN_MILLIS_KEY = "ssotimestamp";
    public static final String TOKEN_TYPE = "alibaba:ssotoken";
    private static final long WHITELIST_UPDATE_INTERVAL = 86400000;
    private final String TAG = "sso.SsoLogin";
    private String TAOBAO_ACCOUNT;
    /* access modifiers changed from: private */
    public Context mApplicationContext;
    private boolean mIsWhitelistUpdated = false;

    public SsoLogin(Context context) {
        this.mApplicationContext = context;
        this.TAOBAO_ACCOUNT = Authenticator.getTaobaoAccountType(this.mApplicationContext);
    }

    public String taobaoAccountType() {
        return this.TAOBAO_ACCOUNT;
    }

    public void getSsoTokenWithType(String accountType, SsoResultListener listener) throws SsoManager.UnauthorizedAccessException, AuthenticatorException {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            throw new CalledFromWrongThreadException();
        }
        updateWhiteList();
        fetchWhiteList();
        List<UserInfo> userInfos = peekSsoInfo();
        UserInfo accordingUserinfo = null;
        long timestamp = getSsoExpireTime();
        int i = 0;
        while (true) {
            if (i >= userInfos.size()) {
                break;
            }
            UserInfo userInfo = userInfos.get(i);
            if (accountType.equals(userInfo.mAccountType) && isSsoValid(userInfo.mTokenTimestamp, timestamp)) {
                accordingUserinfo = userInfo;
                break;
            }
            if (accordingUserinfo == null && i == userInfos.size() - 1 && isSsoValid(userInfos.get(0).mTokenTimestamp, timestamp)) {
                accordingUserinfo = userInfos.get(0);
            }
            i++;
        }
        if (listener == null) {
            return;
        }
        if (accordingUserinfo != null) {
            listener.onSuccess(accordingUserinfo);
        } else {
            listener.onFailed("-1");
        }
    }

    private Account getDefaultAccount(String accountType) {
        if (TextUtils.isEmpty(accountType)) {
            return null;
        }
        return new Account(DEFAULT_TAOBAO_USERNAME, accountType);
    }

    public UserInfo getUserInfo(String accountName, String accountType) throws SsoManager.UnauthorizedAccessException, AuthenticatorException {
        if (TextUtils.isEmpty(accountName)) {
            return null;
        }
        UserInfo userInfo = null;
        try {
            Bundle info = SsoManager.peekUserInfos(this.mApplicationContext, new String[]{accountName}, accountType, TOKEN_TYPE).getBundle(accountName);
            if (info == null) {
                return null;
            }
            UserInfo userInfo2 = new UserInfo();
            try {
                userInfo2.mNick = info.getString("accounts");
                userInfo2.mTokenTimestamp = info.getLong(Authenticator.KEY_TOKEN_TIMESTAMP);
                userInfo2.mSsoToken = info.getString("token");
                userInfo2.mAvatarUrl = info.getString(Authenticator.KEY_PHOTO_URL);
                userInfo2.mAccountType = accountType;
                return userInfo2;
            } catch (OperationCanceledException e) {
                e = e;
                userInfo = userInfo2;
                e.printStackTrace();
                return userInfo;
            } catch (IOException e2) {
                e = e2;
                userInfo = userInfo2;
                e.printStackTrace();
                return userInfo;
            } catch (RuntimeException e3) {
                e = e3;
                userInfo = userInfo2;
                e.printStackTrace();
                return userInfo;
            } catch (AuthenticatorException e4) {
                e = e4;
                userInfo = userInfo2;
                e.printStackTrace();
                return userInfo;
            }
        } catch (OperationCanceledException e5) {
            e = e5;
            e.printStackTrace();
            return userInfo;
        } catch (IOException e6) {
            e = e6;
            e.printStackTrace();
            return userInfo;
        } catch (RuntimeException e7) {
            e = e7;
            e.printStackTrace();
            return userInfo;
        } catch (AuthenticatorException e8) {
            e = e8;
            e.printStackTrace();
            return userInfo;
        }
    }

    private List<UserInfo> peekSsoInfo() throws SsoManager.UnauthorizedAccessException, AuthenticatorException {
        UserInfo userinfo;
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            throw new CalledFromWrongThreadException();
        }
        ArrayList<UserInfo> userinfos = new ArrayList<>();
        Account defaultAccount = getDefaultAccount(this.TAOBAO_ACCOUNT);
        if (!(defaultAccount == null || (userinfo = getUserInfo(defaultAccount.name, this.TAOBAO_ACCOUNT)) == null || TextUtils.isEmpty(userinfo.mSsoToken))) {
            userinfos.add(userinfo);
        }
        return userinfos;
    }

    public boolean shareSsoToken(String ssoToken, String nick, String avatarUrl, String accountType) throws SsoManager.UnauthorizedAccessException, AuthenticatorException {
        Log.d("sso.SsoLogin", "shareSsoToken: ssoToken " + ssoToken + " | nick " + nick + " | accountType " + accountType);
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            throw new CalledFromWrongThreadException();
        } else if (TextUtils.isEmpty(ssoToken) || TextUtils.isEmpty(nick) || TextUtils.isEmpty(accountType)) {
            return false;
        } else {
            updateWhiteList();
            try {
                logout(taobaoAccountType());
                SsoManager.addAccountWithToken(this.mApplicationContext, nick, DEFAULT_TAOBAO_USERNAME, avatarUrl, TOKEN_TYPE, ssoToken, ((Application) this.mApplicationContext).getApplicationInfo().loadLabel(this.mApplicationContext.getPackageManager()).toString(), accountType);
                return true;
            } catch (OperationCanceledException e) {
                Log.d("sso.SsoLogin", "share token has been canceled");
                return false;
            } catch (IOException e2) {
                e2.printStackTrace();
                return false;
            } catch (RuntimeException e3) {
                e3.printStackTrace();
                return false;
            }
        }
    }

    public boolean logout(String nick, String accountType) throws SsoManager.UnauthorizedAccessException, AuthenticatorException {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            throw new CalledFromWrongThreadException();
        }
        Log.v("SsoLogin", "logout: " + nick);
        if (!TextUtils.isEmpty(nick)) {
            updateWhiteList();
            Account defaultAccount = getDefaultAccount(accountType);
            if (defaultAccount != null) {
                Log.v("ssologin", "find defaultAccount");
                UserInfo info = getUserInfo(defaultAccount.name, accountType);
                if (info != null && nick.equals(info.mNick)) {
                    try {
                        SsoManager.removeAccount(this.mApplicationContext, defaultAccount);
                        if (!TextUtils.isEmpty(info.mSsoToken)) {
                            invalidRemoteSsoToken(info.mSsoToken);
                        }
                    } catch (OperationCanceledException e) {
                        e.printStackTrace();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    } catch (RuntimeException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    public boolean logout(String accountType) throws SsoManager.UnauthorizedAccessException, AuthenticatorException {
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            throw new CalledFromWrongThreadException();
        }
        updateWhiteList();
        Account defaultAccount = getDefaultAccount(accountType);
        if (defaultAccount != null) {
            Log.v("ssologin", "find defaultAccount");
            UserInfo info = getUserInfo(defaultAccount.name, accountType);
            if (info != null) {
                try {
                    SsoManager.removeAccount(this.mApplicationContext, defaultAccount);
                    if (!TextUtils.isEmpty(info.mSsoToken)) {
                        invalidRemoteSsoToken(info.mSsoToken);
                    }
                } catch (OperationCanceledException e) {
                    e.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                } catch (RuntimeException e3) {
                    e3.printStackTrace();
                }
            }
        }
        return false;
    }

    private void invalidRemoteSsoToken(String ssoToken) throws IOException {
    }

    private void updateWhiteList() throws SsoManager.UnauthorizedAccessException, AuthenticatorException {
        try {
            if (!this.mIsWhitelistUpdated) {
                long whitelistVersion = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(Whitelist.mWhitelistV).getTime();
                String[] whitelist = Whitelist.mWhitelist;
                ArrayList<Signature> signatures = new ArrayList<>();
                for (String wl : whitelist) {
                    signatures.add(new Signature(wl));
                }
                this.mIsWhitelistUpdated = SsoManager.updateWhitelist(this.mApplicationContext, signatures, whitelistVersion, taobaoAccountType());
            }
        } catch (OperationCanceledException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } catch (ParseException e3) {
            e3.printStackTrace();
        }
    }

    public void fetchWhiteList() {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this.mApplicationContext);
        long preTimeStamp = sp.getLong(PRE_TIME_IN_MILLIS, 0);
        String remoteVersion = sp.getString(PRE_REMOTE_VERSION, "");
        if (System.currentTimeMillis() - preTimeStamp > 86400000) {
            MtopRequest mtopRequest = new MtopRequest();
            mtopRequest.setApiName("com.taobao.mtop.login.sso.getSsoProperties");
            mtopRequest.setVersion("1.0");
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("version", remoteVersion);
                jsonObject.put("platform", 1);
                mtopRequest.setData(jsonObject.toString());
            } catch (Throwable th) {
            }
            MtopBusiness business = MtopBusiness.build(mtopRequest, MemberSDK.ttid);
            business.addListener((MtopListener) new IRemoteBaseListener() {
                /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
                /* Code decompiled incorrectly, please refer to instructions dump. */
                public void onSuccess(int r21, mtopsdk.mtop.domain.MtopResponse r22, mtopsdk.mtop.domain.BaseOutDo r23, java.lang.Object r24) {
                    /*
                        r20 = this;
                        java.lang.String r13 = "tag"
                        java.lang.String r14 = "success"
                        android.util.Log.i(r13, r14)
                        if (r22 == 0) goto L_0x009d
                        org.json.JSONObject r3 = r22.getDataJsonObject()
                        if (r3 == 0) goto L_0x009d
                        java.lang.String r13 = "currentTimeMillis"
                        java.lang.String r2 = r3.optString(r13)     // Catch:{ Throwable -> 0x009c }
                        java.lang.String r13 = "ssoExpireTime"
                        java.lang.String r10 = r3.optString(r13)     // Catch:{ Throwable -> 0x009c }
                        java.lang.String r13 = "version"
                        java.lang.String r11 = r3.optString(r13)     // Catch:{ Throwable -> 0x009c }
                        java.util.ArrayList r12 = new java.util.ArrayList     // Catch:{ Throwable -> 0x009c }
                        r12.<init>()     // Catch:{ Throwable -> 0x009c }
                        java.lang.String r13 = "whiteList"
                        boolean r13 = r3.has(r13)     // Catch:{ Throwable -> 0x009c }
                        if (r13 == 0) goto L_0x009e
                        java.lang.String r13 = "whiteList"
                        org.json.JSONArray r7 = r3.getJSONArray(r13)     // Catch:{ Throwable -> 0x009c }
                        r5 = 0
                    L_0x003c:
                        int r13 = r7.length()     // Catch:{ Throwable -> 0x009c }
                        if (r5 >= r13) goto L_0x009e
                        org.json.JSONObject r6 = r7.getJSONObject(r5)     // Catch:{ Throwable -> 0x009c }
                        java.lang.String r13 = "pubKey"
                        boolean r13 = r6.has(r13)     // Catch:{ Throwable -> 0x009c }
                        if (r13 == 0) goto L_0x0073
                        java.lang.String r13 = "pubKey"
                        java.lang.String r8 = r6.getString(r13)     // Catch:{ Exception -> 0x0076 }
                        boolean r13 = android.text.TextUtils.isEmpty(r8)     // Catch:{ Exception -> 0x0076 }
                        if (r13 != 0) goto L_0x0073
                        int r13 = r8.length()     // Catch:{ Exception -> 0x0076 }
                        r14 = 32
                        if (r13 < r14) goto L_0x0073
                        android.content.pm.Signature r9 = new android.content.pm.Signature     // Catch:{ Exception -> 0x0076 }
                        java.lang.String r13 = "pubKey"
                        java.lang.String r13 = r6.getString(r13)     // Catch:{ Exception -> 0x0076 }
                        r9.<init>(r13)     // Catch:{ Exception -> 0x0076 }
                        r12.add(r9)     // Catch:{ Exception -> 0x0076 }
                    L_0x0073:
                        int r5 = r5 + 1
                        goto L_0x003c
                    L_0x0076:
                        r4 = move-exception
                        r4.printStackTrace()     // Catch:{ Throwable -> 0x009c }
                        java.lang.String r13 = "ssologin"
                        java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x009c }
                        r14.<init>()     // Catch:{ Throwable -> 0x009c }
                        java.lang.String r15 = "whitelist pubkey fromat error! pubkey = "
                        java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Throwable -> 0x009c }
                        java.lang.String r15 = "pubKey"
                        java.lang.String r15 = r6.getString(r15)     // Catch:{ Throwable -> 0x009c }
                        java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Throwable -> 0x009c }
                        java.lang.String r14 = r14.toString()     // Catch:{ Throwable -> 0x009c }
                        android.util.Log.e(r13, r14)     // Catch:{ Throwable -> 0x009c }
                        goto L_0x0073
                    L_0x009c:
                        r13 = move-exception
                    L_0x009d:
                        return
                    L_0x009e:
                        r0 = r20
                        android.content.SharedPreferences r13 = r6     // Catch:{ Throwable -> 0x009c }
                        android.content.SharedPreferences$Editor r13 = r13.edit()     // Catch:{ Throwable -> 0x009c }
                        java.lang.String r14 = "ssotimestamp"
                        long r16 = java.lang.Long.parseLong(r10)     // Catch:{ Throwable -> 0x009c }
                        r18 = 1000(0x3e8, double:4.94E-321)
                        long r16 = r16 * r18
                        r0 = r16
                        android.content.SharedPreferences$Editor r13 = r13.putLong(r14, r0)     // Catch:{ Throwable -> 0x009c }
                        r13.apply()     // Catch:{ Throwable -> 0x009c }
                        r0 = r20
                        com.ali.user.sso.SsoLogin r13 = com.ali.user.sso.SsoLogin.this     // Catch:{ Throwable -> 0x009c }
                        android.content.Context r13 = r13.mApplicationContext     // Catch:{ Throwable -> 0x009c }
                        long r14 = java.lang.Long.parseLong(r2)     // Catch:{ Throwable -> 0x009c }
                        r0 = r20
                        com.ali.user.sso.SsoLogin r0 = com.ali.user.sso.SsoLogin.this     // Catch:{ Throwable -> 0x009c }
                        r16 = r0
                        java.lang.String r16 = r16.taobaoAccountType()     // Catch:{ Throwable -> 0x009c }
                        r0 = r16
                        com.ali.user.sso.SsoManager.updateWhitelist(r13, r12, r14, r0)     // Catch:{ Throwable -> 0x009c }
                        r0 = r20
                        android.content.SharedPreferences r13 = r6     // Catch:{ Throwable -> 0x009c }
                        android.content.SharedPreferences$Editor r13 = r13.edit()     // Catch:{ Throwable -> 0x009c }
                        java.lang.String r14 = "remoteversion"
                        android.content.SharedPreferences$Editor r13 = r13.putString(r14, r11)     // Catch:{ Throwable -> 0x009c }
                        r13.apply()     // Catch:{ Throwable -> 0x009c }
                        r0 = r20
                        android.content.SharedPreferences r13 = r6     // Catch:{ Throwable -> 0x009c }
                        android.content.SharedPreferences$Editor r13 = r13.edit()     // Catch:{ Throwable -> 0x009c }
                        java.lang.String r16 = "pretimestamp"
                        long r14 = java.lang.Long.parseLong(r2)     // Catch:{ Throwable -> 0x009c }
                        r18 = 0
                        int r14 = (r14 > r18 ? 1 : (r14 == r18 ? 0 : -1))
                        if (r14 <= 0) goto L_0x010a
                        long r14 = java.lang.Long.parseLong(r2)     // Catch:{ Throwable -> 0x009c }
                    L_0x0100:
                        r0 = r16
                        android.content.SharedPreferences$Editor r13 = r13.putLong(r0, r14)     // Catch:{ Throwable -> 0x009c }
                        r13.apply()     // Catch:{ Throwable -> 0x009c }
                        goto L_0x009d
                    L_0x010a:
                        long r14 = java.lang.System.currentTimeMillis()     // Catch:{ Throwable -> 0x009c }
                        goto L_0x0100
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.ali.user.sso.SsoLogin.AnonymousClass1.onSuccess(int, mtopsdk.mtop.domain.MtopResponse, mtopsdk.mtop.domain.BaseOutDo, java.lang.Object):void");
                }

                public void onError(int i, MtopResponse mtopResponse, Object o) {
                    Log.i("tag", BlitzServiceUtils.CSUCCESS);
                }

                public void onSystemError(int i, MtopResponse mtopResponse, Object o) {
                    Log.i("tag", BlitzServiceUtils.CSUCCESS);
                }
            });
            business.startRequest();
        }
    }

    private long getSsoExpireTime() {
        long timestamp = PreferenceManager.getDefaultSharedPreferences(this.mApplicationContext).getLong(SSO_EXPIRE_TIME_IN_MILLIS_KEY, -1);
        return timestamp <= 0 ? SSO_EXPIRE_TIME_IN_MILLIS : timestamp;
    }

    private boolean isSsoValid(long ssoTimeStamp, long expireTimeStamp) {
        if (System.currentTimeMillis() - ssoTimeStamp <= expireTimeStamp) {
            return true;
        }
        return false;
    }
}
