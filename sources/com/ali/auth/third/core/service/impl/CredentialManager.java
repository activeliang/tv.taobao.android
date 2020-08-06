package com.ali.auth.third.core.service.impl;

import android.text.TextUtils;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.cookies.CookieManagerWrapper;
import com.ali.auth.third.core.model.AccountContract;
import com.ali.auth.third.core.model.InternalSession;
import com.ali.auth.third.core.model.ResultCode;
import com.ali.auth.third.core.model.Session;
import com.ali.auth.third.core.model.User;
import com.ali.auth.third.core.registry.ServiceRegistry;
import com.ali.auth.third.core.service.CredentialService;
import com.ali.auth.third.core.service.RpcService;
import com.ali.auth.third.core.service.StorageService;
import com.ali.auth.third.core.trace.SDKLogger;
import com.ali.auth.third.core.util.JSONUtils;
import com.ali.auth.third.core.util.ReflectionUtils;
import com.ali.auth.third.core.util.SystemUtils;
import com.ali.user.open.tbauth.TbAuthConstants;
import com.ali.user.open.ucc.data.ApiConstants;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import java.util.Map;
import org.json.JSONObject;

public class CredentialManager implements CredentialService {
    public static final CredentialManager INSTANCE = new CredentialManager();
    private static final String TAG = CredentialManager.class.getSimpleName();
    private volatile InternalSession internalSession;
    public String internalSessionStoreKey = "internal_session";

    public CredentialManager() {
        try {
            preInit();
        } catch (Throwable th) {
            this.internalSession = new InternalSession();
            this.internalSession.user = new User();
        }
    }

    private void preInit() {
        if (KernelContext.storageService == null) {
            registerStorage(KernelContext.serviceRegistry);
        }
        SDKLogger.d(TAG, "CredentialManager init step 1");
        String lastEnvIndex = KernelContext.storageService.getValue("loginEnvironmentIndex", true);
        SDKLogger.d(TAG, "CredentialManager init step 2");
        String currentEnvIndex = String.valueOf(KernelContext.getEnvironment().ordinal());
        if (lastEnvIndex == null || lastEnvIndex.equals(currentEnvIndex)) {
            SDKLogger.d(TAG, "CredentialManager init step 3");
            String internalSessionJson = KernelContext.storageService.getValue(this.internalSessionStoreKey, true);
            SDKLogger.d(TAG, "CredentialManager init step 4");
            if (TextUtils.isEmpty(internalSessionJson)) {
                internalSessionJson = KernelContext.storageService.getValue(this.internalSessionStoreKey, true);
            }
            if (!TextUtils.isEmpty(internalSessionJson)) {
                this.internalSession = createInternalSession(internalSessionJson);
            } else {
                this.internalSession = new InternalSession();
                this.internalSession.user = new User();
            }
            SDKLogger.d(TAG, "CredentialManager init step 5");
            return;
        }
        KernelContext.storageService.putValue("loginEnvironmentIndex", currentEnvIndex, true);
        KernelContext.storageService.removeValue(this.internalSessionStoreKey, true);
        this.internalSession = new InternalSession();
        this.internalSession.user = new User();
    }

    public boolean isSessionValid() {
        boolean z;
        boolean z2 = true;
        SDKLogger.d(TAG, "func isSessionValid");
        if (this.internalSession == null) {
            SDKLogger.d(TAG, "isSessionValid()  internalSession is null");
            return false;
        } else if (this.internalSession.loginTime == 0 || this.internalSession.expireIn == 0) {
            SDKLogger.d(TAG, "isSessionValid()  loginTime is 0 or expireIn is 0");
            return false;
        } else {
            String str = TAG;
            StringBuilder append = new StringBuilder().append("isSessionValid()  ");
            if (System.currentTimeMillis() / 1000 < this.internalSession.expireIn) {
                z = true;
            } else {
                z = false;
            }
            SDKLogger.d(str, append.append(z).toString());
            if (System.currentTimeMillis() / 1000 >= this.internalSession.expireIn) {
                z2 = false;
            }
            return z2;
        }
    }

    public InternalSession createInternalSession(String sessionJson) {
        InternalSession localInternalSession = new InternalSession();
        try {
            JSONObject jsonObject = new JSONObject(sessionJson);
            localInternalSession.sid = JSONUtils.optString(jsonObject, "sid");
            localInternalSession.expireIn = (long) JSONUtils.optInteger(jsonObject, "expireIn").intValue();
            User user = new User();
            JSONObject userObject = jsonObject.optJSONObject("user");
            if (userObject != null) {
                user.avatarUrl = userObject.optString("avatarUrl");
                user.userId = userObject.optString("userId");
                user.nick = userObject.optString(TvTaoSharedPerference.NICK);
                user.openId = userObject.optString("openId");
                user.openSid = userObject.optString("openSid");
                user.deviceTokenKey = userObject.optString(ApiConstants.ApiField.DEVICE_TOKEN_KEY);
                user.deviceTokenSalt = userObject.optString("deviceTokenSalt");
                if (!TextUtils.isEmpty(localInternalSession.sid) && !TextUtils.isEmpty(user.userId)) {
                    ((RpcService) KernelContext.getService(RpcService.class)).registerSessionInfo(localInternalSession.sid, user.userId);
                }
            }
            localInternalSession.user = user;
            localInternalSession.loginTime = JSONUtils.optLong(jsonObject, "loginTime").longValue();
            localInternalSession.mobile = JSONUtils.optString(jsonObject, "mobile");
            localInternalSession.loginId = JSONUtils.optString(jsonObject, "loginId");
            localInternalSession.autoLoginToken = JSONUtils.optString(jsonObject, TbAuthConstants.KEY_AUTOLOGINTOKEN);
            localInternalSession.topAccessToken = JSONUtils.optString(jsonObject, "topAccessToken");
            localInternalSession.topExpireTime = JSONUtils.optString(jsonObject, "topExpireTime");
            localInternalSession.topAuthCode = JSONUtils.optString(jsonObject, "topAuthCode");
            localInternalSession.otherInfo = JSONUtils.toMap(jsonObject.optJSONObject("otherInfo"));
            localInternalSession.ssoToken = JSONUtils.optString(jsonObject, "ssoToken");
            localInternalSession.havanaSsoToken = JSONUtils.optString(jsonObject, "havanaSsoToken");
        } catch (Exception e) {
            SDKLogger.e(TAG, e.getMessage(), e);
        }
        return localInternalSession;
    }

    public InternalSession getInternalSession() {
        return this.internalSession;
    }

    private void refreshInternalSession(InternalSession internalSession2) {
        this.internalSession = internalSession2;
        KernelContext.storageService.putValue(this.internalSessionStoreKey, SystemUtils.toInternalSessionJSON(internalSession2), true);
    }

    /* JADX WARNING: type inference failed for: r3v35, types: [java.lang.Object[]] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void refreshWhenLogin(com.ali.auth.third.core.model.LoginReturnData r18) {
        /*
            r17 = this;
            if (r18 == 0) goto L_0x000c
            r0 = r18
            java.lang.String r3 = r0.data
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 == 0) goto L_0x000d
        L_0x000c:
            return
        L_0x000d:
            com.ali.auth.third.core.model.InternalSession r12 = new com.ali.auth.third.core.model.InternalSession
            r12.<init>()
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch:{ Exception -> 0x0174 }
            r0 = r18
            java.lang.String r3 = r0.data     // Catch:{ Exception -> 0x0174 }
            r8.<init>(r3)     // Catch:{ Exception -> 0x0174 }
            java.lang.Class<com.ali.auth.third.core.model.LoginDataModel> r3 = com.ali.auth.third.core.model.LoginDataModel.class
            java.lang.Object r11 = com.ali.auth.third.core.util.JSONUtils.toPOJO(r8, r3)     // Catch:{ Exception -> 0x0174 }
            com.ali.auth.third.core.model.LoginDataModel r11 = (com.ali.auth.third.core.model.LoginDataModel) r11     // Catch:{ Exception -> 0x0174 }
            java.lang.String[] r3 = r11.externalCookies     // Catch:{ Exception -> 0x0174 }
            r12.externalCookies = r3     // Catch:{ Exception -> 0x0174 }
            com.ali.auth.third.core.model.User r16 = new com.ali.auth.third.core.model.User     // Catch:{ Exception -> 0x0174 }
            r16.<init>()     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r11.userId     // Catch:{ Exception -> 0x0174 }
            r0 = r16
            r0.userId = r3     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r11.nick     // Catch:{ Exception -> 0x0174 }
            if (r3 == 0) goto L_0x0043
            java.lang.String r3 = r11.nick     // Catch:{ Exception -> 0x0168 }
            java.lang.String r4 = "UTF-8"
            java.lang.String r3 = java.net.URLDecoder.decode(r3, r4)     // Catch:{ Exception -> 0x0168 }
            r0 = r16
            r0.nick = r3     // Catch:{ Exception -> 0x0168 }
        L_0x0043:
            java.lang.String r3 = r11.openId     // Catch:{ Exception -> 0x0174 }
            r0 = r16
            r0.openId = r3     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r11.openSid     // Catch:{ Exception -> 0x0174 }
            r0 = r16
            r0.openSid = r3     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r11.headPicLink     // Catch:{ Exception -> 0x0174 }
            r0 = r16
            r0.avatarUrl = r3     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r11.email     // Catch:{ Exception -> 0x0174 }
            r0 = r16
            r0.email = r3     // Catch:{ Exception -> 0x0174 }
            java.util.Map<java.lang.String, java.lang.String> r3 = r11.loginServiceExt     // Catch:{ Exception -> 0x0174 }
            if (r3 == 0) goto L_0x0098
            java.util.Map<java.lang.String, java.lang.String> r3 = r11.loginServiceExt     // Catch:{ Exception -> 0x0174 }
            java.lang.String r4 = "1688ext"
            java.lang.Object r3 = r3.get(r4)     // Catch:{ Exception -> 0x0174 }
            java.lang.CharSequence r3 = (java.lang.CharSequence) r3     // Catch:{ Exception -> 0x0174 }
            boolean r3 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Exception -> 0x0174 }
            if (r3 != 0) goto L_0x0098
            org.json.JSONObject r13 = new org.json.JSONObject     // Catch:{ Exception -> 0x017b }
            java.util.Map<java.lang.String, java.lang.String> r3 = r11.loginServiceExt     // Catch:{ Exception -> 0x017b }
            java.lang.String r4 = "1688ext"
            java.lang.Object r3 = r3.get(r4)     // Catch:{ Exception -> 0x017b }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x017b }
            r13.<init>(r3)     // Catch:{ Exception -> 0x017b }
            if (r13 == 0) goto L_0x0098
            java.lang.String r3 = "loginId"
            java.lang.String r3 = r13.optString(r3)     // Catch:{ Exception -> 0x017b }
            r0 = r16
            r0.cbuLoginId = r3     // Catch:{ Exception -> 0x017b }
            java.lang.String r3 = "memberId"
            java.lang.String r3 = r13.optString(r3)     // Catch:{ Exception -> 0x017b }
            r0 = r16
            r0.memberId = r3     // Catch:{ Exception -> 0x017b }
        L_0x0098:
            r0 = r18
            com.ali.auth.third.core.model.DeviceTokenRO r3 = r0.deviceToken     // Catch:{ Exception -> 0x0174 }
            if (r3 == 0) goto L_0x00b2
            r0 = r18
            com.ali.auth.third.core.model.DeviceTokenRO r3 = r0.deviceToken     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r3.salt     // Catch:{ Exception -> 0x0174 }
            r0 = r16
            r0.deviceTokenSalt = r3     // Catch:{ Exception -> 0x0174 }
            r0 = r18
            com.ali.auth.third.core.model.DeviceTokenRO r3 = r0.deviceToken     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r3.key     // Catch:{ Exception -> 0x0174 }
            r0 = r16
            r0.deviceTokenKey = r3     // Catch:{ Exception -> 0x0174 }
        L_0x00b2:
            com.ali.auth.third.core.model.HistoryAccount r2 = new com.ali.auth.third.core.model.HistoryAccount     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r11.userId     // Catch:{ Exception -> 0x0174 }
            r0 = r16
            java.lang.String r4 = r0.deviceTokenKey     // Catch:{ Exception -> 0x0174 }
            java.lang.String r5 = r11.nick     // Catch:{ Exception -> 0x0174 }
            java.lang.String r6 = r11.phone     // Catch:{ Exception -> 0x0174 }
            java.lang.String r7 = r11.email     // Catch:{ Exception -> 0x0174 }
            r2.<init>(r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x0174 }
            com.ali.auth.third.core.history.AccountHistoryManager r3 = com.ali.auth.third.core.history.AccountHistoryManager.getInstance()     // Catch:{ Exception -> 0x0174 }
            r0 = r16
            java.lang.String r4 = r0.deviceTokenSalt     // Catch:{ Exception -> 0x0174 }
            r3.putLoginHistory(r2, r4)     // Catch:{ Exception -> 0x0174 }
            r0 = r16
            r12.user = r0     // Catch:{ Exception -> 0x0174 }
            long r4 = r11.loginTime     // Catch:{ Exception -> 0x0174 }
            r12.loginTime = r4     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r11.sid     // Catch:{ Exception -> 0x0174 }
            r12.sid = r3     // Catch:{ Exception -> 0x0174 }
            long r4 = r11.expires     // Catch:{ Exception -> 0x0174 }
            long r6 = r11.loginTime     // Catch:{ Exception -> 0x0174 }
            r0 = r17
            long r4 = r0.adjustSessionExpireTime(r4, r6)     // Catch:{ Exception -> 0x0174 }
            r12.expireIn = r4     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r11.loginPhone     // Catch:{ Exception -> 0x0174 }
            r12.mobile = r3     // Catch:{ Exception -> 0x0174 }
            r0 = r18
            java.lang.String r3 = r0.showLoginId     // Catch:{ Exception -> 0x0174 }
            r12.loginId = r3     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r11.autoLoginToken     // Catch:{ Exception -> 0x0174 }
            r12.autoLoginToken = r3     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r11.topAccessToken     // Catch:{ Exception -> 0x0174 }
            r12.topAccessToken = r3     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r11.topAuthCode     // Catch:{ Exception -> 0x0174 }
            r12.topAuthCode = r3     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r11.topExpireTime     // Catch:{ Exception -> 0x0174 }
            r12.topExpireTime = r3     // Catch:{ Exception -> 0x0174 }
            java.util.Map<java.lang.String, java.lang.Object> r3 = r11.extendAttribute     // Catch:{ Exception -> 0x0174 }
            r12.otherInfo = r3     // Catch:{ Exception -> 0x0174 }
            r0 = r18
            java.lang.String r3 = r0.ssoToken     // Catch:{ Exception -> 0x0174 }
            r12.ssoToken = r3     // Catch:{ Exception -> 0x0174 }
            java.lang.String r3 = r11.havanaSsoToken     // Catch:{ Exception -> 0x0174 }
            r12.havanaSsoToken = r3     // Catch:{ Exception -> 0x0174 }
            java.lang.Class<com.ali.auth.third.core.service.RpcService> r3 = com.ali.auth.third.core.service.RpcService.class
            java.lang.Object r3 = com.ali.auth.third.core.context.KernelContext.getService(r3)     // Catch:{ Exception -> 0x0174 }
            com.ali.auth.third.core.service.RpcService r3 = (com.ali.auth.third.core.service.RpcService) r3     // Catch:{ Exception -> 0x0174 }
            java.lang.String r4 = r11.sid     // Catch:{ Exception -> 0x0174 }
            java.lang.String r5 = r11.userId     // Catch:{ Exception -> 0x0174 }
            r3.registerSessionInfo(r4, r5)     // Catch:{ Exception -> 0x0174 }
            r15 = 0
            java.util.Map<java.lang.String, java.lang.Object> r3 = r11.extendAttribute     // Catch:{ Exception -> 0x0176 }
            java.lang.String r4 = "ssoDomainList"
            java.lang.Object r14 = r3.get(r4)     // Catch:{ Exception -> 0x0176 }
            if (r14 == 0) goto L_0x013c
            boolean r3 = r14 instanceof java.util.ArrayList     // Catch:{ Exception -> 0x0176 }
            if (r3 == 0) goto L_0x013c
            r0 = r14
            java.util.ArrayList r0 = (java.util.ArrayList) r0     // Catch:{ Exception -> 0x0176 }
            r10 = r0
            r3 = 0
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch:{ Exception -> 0x0176 }
            java.lang.Object[] r3 = r10.toArray(r3)     // Catch:{ Exception -> 0x0176 }
            r0 = r3
            java.lang.String[] r0 = (java.lang.String[]) r0     // Catch:{ Exception -> 0x0176 }
            r15 = r0
        L_0x013c:
            com.ali.auth.third.core.cookies.CookieManagerWrapper r3 = com.ali.auth.third.core.cookies.CookieManagerWrapper.INSTANCE     // Catch:{ Exception -> 0x0174 }
            java.lang.String[] r4 = r11.cookies     // Catch:{ Exception -> 0x0174 }
            r3.injectCookie(r4, r15)     // Catch:{ Exception -> 0x0174 }
        L_0x0143:
            java.lang.String r3 = "session"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "session = "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = r12.toString()
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            com.ali.auth.third.core.trace.SDKLogger.e(r3, r4)
            r0 = r17
            r0.refreshInternalSession(r12)
            goto L_0x000c
        L_0x0168:
            r9 = move-exception
            java.lang.String r3 = TAG     // Catch:{ Exception -> 0x0174 }
            java.lang.String r4 = r9.getMessage()     // Catch:{ Exception -> 0x0174 }
            com.ali.auth.third.core.trace.SDKLogger.e(r3, r4, r9)     // Catch:{ Exception -> 0x0174 }
            goto L_0x0043
        L_0x0174:
            r3 = move-exception
            goto L_0x0143
        L_0x0176:
            r9 = move-exception
            r9.printStackTrace()     // Catch:{ Exception -> 0x0174 }
            goto L_0x013c
        L_0x017b:
            r3 = move-exception
            goto L_0x0098
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.auth.third.core.service.impl.CredentialManager.refreshWhenLogin(com.ali.auth.third.core.model.LoginReturnData):void");
    }

    public void refreshWhenOfflineLogin(AccountContract accountContract) {
        InternalSession offlineInternalSession = new InternalSession();
        User user = new User();
        user.nick = KernelContext.storageService.decrypt(accountContract.getNick());
        user.openId = accountContract.getOpenid();
        user.userId = KernelContext.storageService.decrypt(accountContract.getUserid());
        offlineInternalSession.user = user;
        if (this.internalSession == null || this.internalSession.user == null || !TextUtils.equals(user.userId, this.internalSession.user.userId)) {
            ((RpcService) KernelContext.getService(RpcService.class)).logout();
        } else {
            offlineInternalSession.autoLoginToken = this.internalSession.autoLoginToken;
        }
        refreshInternalSession(offlineInternalSession);
    }

    public long adjustSessionExpireTime(long expire, long serverTime) {
        long currentTime = System.currentTimeMillis() / 1000;
        long adjustTime = expire;
        if (currentTime <= serverTime) {
            return adjustTime;
        }
        if (expire > 0) {
            return expire + (currentTime - serverTime);
        }
        return currentTime + 86400;
    }

    public Session getSession() {
        Session session = new Session();
        session.userid = (this.internalSession == null || this.internalSession.user == null) ? "" : this.internalSession.user.userId;
        session.nick = (this.internalSession == null || this.internalSession.user == null) ? "" : this.internalSession.user.nick;
        session.avatarUrl = (this.internalSession == null || this.internalSession.user == null) ? "" : this.internalSession.user.avatarUrl;
        session.openId = (this.internalSession == null || this.internalSession.user == null) ? "" : this.internalSession.user.openId;
        session.openSid = (this.internalSession == null || this.internalSession.user == null) ? "" : this.internalSession.user.openSid;
        session.topAccessToken = this.internalSession == null ? "" : this.internalSession.topAccessToken;
        session.topAuthCode = this.internalSession == null ? "" : this.internalSession.topAuthCode;
        session.topExpireTime = this.internalSession == null ? "" : this.internalSession.topExpireTime;
        session.ssoToken = this.internalSession == null ? "" : this.internalSession.ssoToken;
        session.havanaSsoToken = this.internalSession == null ? "" : this.internalSession.havanaSsoToken;
        return session;
    }

    public ResultCode logout() {
        CookieManagerWrapper.INSTANCE.clearCookies();
        InternalSession session = new InternalSession();
        session.user = new User();
        refreshInternalSession(session);
        return ResultCode.SUCCESS;
    }

    private void registerStorage(ServiceRegistry serviceRegistry) {
        Object instance;
        boolean isSecurityGuardDetected = false;
        try {
            Class.forName("com.ali.auth.third.securityguard.SecurityGuardWrapper");
            isSecurityGuardDetected = true;
            KernelContext.isMini = false;
            KernelContext.sdkVersion = KernelContext.SDK_VERSION_STD;
        } catch (Throwable th) {
        }
        if (isSecurityGuardDetected) {
            try {
                instance = getServiceInstance("com.ali.auth.third.securityguard.SecurityGuardWrapper", (String[]) null, (Object[]) null);
            } catch (NoSuchMethodError e2) {
                e2.printStackTrace();
                return;
            }
        } else {
            instance = getServiceInstance("com.ali.auth.third.core.storage.CommonStorageServiceImpl", (String[]) null, (Object[]) null);
        }
        serviceRegistry.registerService(new Class[]{StorageService.class}, instance, (Map<String, String>) null);
        KernelContext.storageService = (StorageService) serviceRegistry.getService(StorageService.class, (Map<String, String>) null);
    }

    private Object getServiceInstance(String clzName, String[] paramTypeNames, Object[] paramValues) {
        try {
            return ReflectionUtils.newInstance(clzName, paramTypeNames, paramValues);
        } catch (NoSuchMethodError e2) {
            e2.printStackTrace();
            return null;
        }
    }
}
