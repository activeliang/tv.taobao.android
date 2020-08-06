package com.ali.user.open.service.impl;

import android.content.Intent;
import android.text.TextUtils;
import com.ali.user.open.cookies.CookieManagerWrapper;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.core.config.ConfigManager;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.model.ResultCode;
import com.ali.user.open.core.service.RpcService;
import com.ali.user.open.core.service.StorageService;
import com.ali.user.open.core.trace.SDKLogger;
import com.ali.user.open.core.util.JSONUtils;
import com.ali.user.open.core.util.ReflectionUtils;
import com.ali.user.open.service.SessionService;
import com.ali.user.open.session.InternalSession;
import com.ali.user.open.session.Session;
import com.ali.user.open.tbauth.TbAuthConstants;
import com.ali.user.open.ucc.data.ApiConstants;
import com.ali.user.open.util.SessionUtils;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class SessionManager implements SessionService {
    public static final SessionManager INSTANCE = new SessionManager();
    private static final String TAG = SessionManager.class.getSimpleName();
    private volatile InternalSession internalSession;
    public String internalSessionMapKey = "internal_session_list";
    public String internalSessionStoreKey = "internal_session";
    private volatile Map<String, InternalSession> mIntenalSessions;

    public SessionManager() {
        try {
            preInit();
        } catch (Throwable th) {
            this.internalSession = new InternalSession();
            this.mIntenalSessions = new HashMap();
        }
    }

    private void preInit() {
        if (AliMemberSDK.getService(StorageService.class) == null) {
            registerStorage();
        }
        String lastEnvIndex = ((StorageService) AliMemberSDK.getService(StorageService.class)).getValue("loginEnvironmentIndex", true);
        String currentEnvIndex = String.valueOf(KernelContext.getEnvironment().ordinal());
        if (lastEnvIndex == null || lastEnvIndex.equals(currentEnvIndex)) {
            String internalSessionJson = ((StorageService) AliMemberSDK.getService(StorageService.class)).getValue(this.internalSessionStoreKey, true);
            if (TextUtils.isEmpty(internalSessionJson)) {
                internalSessionJson = ((StorageService) AliMemberSDK.getService(StorageService.class)).getValue(this.internalSessionStoreKey, true);
            }
            this.mIntenalSessions = new HashMap();
            if (!TextUtils.isEmpty(internalSessionJson)) {
                this.internalSession = createInternalSession(internalSessionJson);
                if (this.internalSession == null) {
                    return;
                }
                if (!TextUtils.isEmpty(this.internalSession.site)) {
                    this.mIntenalSessions.put(this.internalSession.site, this.internalSession);
                } else {
                    this.mIntenalSessions.put(Site.TAOBAO, this.internalSession);
                }
            } else {
                this.internalSession = new InternalSession();
            }
        } else {
            ((StorageService) AliMemberSDK.getService(StorageService.class)).putValue("loginEnvironmentIndex", currentEnvIndex, true);
            ((StorageService) AliMemberSDK.getService(StorageService.class)).removeValue(this.internalSessionStoreKey, true);
            this.internalSession = new InternalSession();
            this.mIntenalSessions = new HashMap();
        }
    }

    public boolean isSessionValid() {
        boolean z;
        boolean z2 = true;
        SDKLogger.d(TAG, "func isSessionValid");
        if (this.internalSession == null || TextUtils.isEmpty(this.internalSession.sid)) {
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
        return createInternalSession("", sessionJson);
    }

    public InternalSession createInternalSession(String site, String sessionJson) {
        InternalSession localInternalSession = new InternalSession();
        try {
            JSONObject jsonObject = new JSONObject(sessionJson);
            localInternalSession.sid = JSONUtils.optString(jsonObject, "sid");
            if (TextUtils.isEmpty(site)) {
                localInternalSession.site = JSONUtils.optString(jsonObject, "site");
            } else {
                localInternalSession.site = site;
            }
            localInternalSession.expireIn = (long) JSONUtils.optInteger(jsonObject, "expireIn").intValue();
            localInternalSession.avatarUrl = JSONUtils.optString(jsonObject, "avatarUrl");
            localInternalSession.userId = JSONUtils.optString(jsonObject, "userId");
            localInternalSession.nick = JSONUtils.optString(jsonObject, TvTaoSharedPerference.NICK);
            localInternalSession.openId = JSONUtils.optString(jsonObject, "openId");
            localInternalSession.openSid = JSONUtils.optString(jsonObject, "openSid");
            localInternalSession.deviceTokenKey = JSONUtils.optString(jsonObject, ApiConstants.ApiField.DEVICE_TOKEN_KEY);
            localInternalSession.deviceTokenSalt = JSONUtils.optString(jsonObject, "deviceTokenSalt");
            if (!TextUtils.isEmpty(localInternalSession.sid) && !TextUtils.isEmpty(localInternalSession.userId)) {
                if (ConfigManager.getInstance().isRegisterSidToMtopDefault()) {
                    ((RpcService) AliMemberSDK.getService(RpcService.class)).registerSessionInfo((String) null, localInternalSession.sid, localInternalSession.userId);
                } else if (!TextUtils.isEmpty(localInternalSession.site)) {
                    ((RpcService) AliMemberSDK.getService(RpcService.class)).registerSessionInfo(Site.getMtopInstanceTag(localInternalSession.site), localInternalSession.sid, localInternalSession.userId);
                }
            }
            localInternalSession.loginTime = JSONUtils.optLong(jsonObject, "loginTime").longValue();
            localInternalSession.mobile = JSONUtils.optString(jsonObject, "mobile");
            localInternalSession.loginId = JSONUtils.optString(jsonObject, "loginId");
            localInternalSession.autoLoginToken = JSONUtils.optString(jsonObject, TbAuthConstants.KEY_AUTOLOGINTOKEN);
            localInternalSession.topAccessToken = JSONUtils.optString(jsonObject, "topAccessToken");
            localInternalSession.topExpireTime = JSONUtils.optString(jsonObject, "topExpireTime");
            localInternalSession.topAuthCode = JSONUtils.optString(jsonObject, "topAuthCode");
            localInternalSession.otherInfo = JSONUtils.toMap(jsonObject.optJSONObject("otherInfo"));
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
        ((StorageService) AliMemberSDK.getService(StorageService.class)).putValue(this.internalSessionStoreKey, SessionUtils.toInternalSessionJSON(internalSession2), true);
        if (KernelContext.getApplicationContext() != null) {
            Intent intent = new Intent();
            intent.setAction("aliuser_sync_session");
            intent.setPackage(KernelContext.getApplicationContext().getPackageName());
            KernelContext.getApplicationContext().sendBroadcast(intent);
        }
    }

    public String getSessionData() {
        try {
            return ((StorageService) getServiceInstance("com.ali.user.open.securityguard.SecurityGuardWrapper", (String[]) null, (Object[]) null)).getValue(this.internalSessionStoreKey, true);
        } catch (Throwable th) {
            return "";
        }
    }

    public void reloadSession() {
        try {
            StorageService storageService = (StorageService) getServiceInstance("com.ali.user.open.securityguard.SecurityGuardWrapper", (String[]) null, (Object[]) null);
            String internalSessionJson = storageService.getValue(this.internalSessionStoreKey, true);
            if (TextUtils.isEmpty(internalSessionJson)) {
                internalSessionJson = storageService.getValue(this.internalSessionStoreKey, true);
            }
            if (!TextUtils.isEmpty(internalSessionJson)) {
                this.internalSession = createInternalSession(Site.TAOBAO, internalSessionJson);
                storageService.putValue(this.internalSessionStoreKey, SessionUtils.toInternalSessionJSON(this.internalSession), true);
                if (this.mIntenalSessions == null) {
                    this.mIntenalSessions = new HashMap();
                }
                if (!TextUtils.isEmpty(this.internalSession.site)) {
                    this.mIntenalSessions.put(this.internalSession.site, this.internalSession);
                } else {
                    this.mIntenalSessions.put(Site.TAOBAO, this.internalSession);
                }
            }
        } catch (Throwable th) {
        }
    }

    public void reloadSession(String site, String sessionData) {
        try {
            StorageService storageService = (StorageService) getServiceInstance("com.ali.user.open.securityguard.SecurityGuardWrapper", (String[]) null, (Object[]) null);
            if (!TextUtils.isEmpty(sessionData)) {
                this.internalSession = createInternalSession(site, sessionData);
                storageService.putValue(this.internalSessionStoreKey, SessionUtils.toInternalSessionJSON(this.internalSession), true);
                if (this.mIntenalSessions == null) {
                    this.mIntenalSessions = new HashMap();
                }
                if (!TextUtils.isEmpty(this.internalSession.site)) {
                    this.mIntenalSessions.put(this.internalSession.site, this.internalSession);
                } else {
                    this.mIntenalSessions.put(Site.TAOBAO, this.internalSession);
                }
            }
        } catch (Throwable th) {
        }
    }

    /* JADX WARNING: type inference failed for: r3v34, types: [java.lang.Object[]] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void refreshWhenLogin(java.lang.String r16, com.ali.user.open.core.model.LoginReturnData r17) {
        /*
            r15 = this;
            if (r17 == 0) goto L_0x000c
            r0 = r17
            java.lang.String r3 = r0.data
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 == 0) goto L_0x000d
        L_0x000c:
            return
        L_0x000d:
            com.ali.user.open.session.InternalSession r12 = new com.ali.user.open.session.InternalSession
            r12.<init>()
            org.json.JSONObject r8 = new org.json.JSONObject     // Catch:{ Exception -> 0x0152 }
            r0 = r17
            java.lang.String r3 = r0.data     // Catch:{ Exception -> 0x0152 }
            r8.<init>(r3)     // Catch:{ Exception -> 0x0152 }
            java.lang.Class<com.ali.user.open.core.model.LoginDataModel> r3 = com.ali.user.open.core.model.LoginDataModel.class
            java.lang.Object r11 = com.ali.user.open.core.util.JSONUtils.toPOJO(r8, r3)     // Catch:{ Exception -> 0x0152 }
            com.ali.user.open.core.model.LoginDataModel r11 = (com.ali.user.open.core.model.LoginDataModel) r11     // Catch:{ Exception -> 0x0152 }
            r0 = r16
            r12.site = r0     // Catch:{ Exception -> 0x0152 }
            java.lang.String[] r3 = r11.externalCookies     // Catch:{ Exception -> 0x0152 }
            r12.externalCookies = r3     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = r11.userId     // Catch:{ Exception -> 0x0152 }
            r12.userId = r3     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = r11.nick     // Catch:{ Exception -> 0x0152 }
            if (r3 == 0) goto L_0x003e
            java.lang.String r3 = r11.nick     // Catch:{ Exception -> 0x0146 }
            java.lang.String r4 = "UTF-8"
            java.lang.String r3 = java.net.URLDecoder.decode(r3, r4)     // Catch:{ Exception -> 0x0146 }
            r12.nick = r3     // Catch:{ Exception -> 0x0146 }
        L_0x003e:
            java.lang.String r3 = r11.openId     // Catch:{ Exception -> 0x0152 }
            r12.openId = r3     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = r11.openSid     // Catch:{ Exception -> 0x0152 }
            r12.openSid = r3     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = r11.headPicLink     // Catch:{ Exception -> 0x0152 }
            r12.avatarUrl = r3     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = r11.email     // Catch:{ Exception -> 0x0152 }
            r12.email = r3     // Catch:{ Exception -> 0x0152 }
            r0 = r17
            com.ali.user.open.core.model.DeviceTokenRO r3 = r0.deviceToken     // Catch:{ Exception -> 0x0152 }
            if (r3 == 0) goto L_0x0064
            r0 = r17
            com.ali.user.open.core.model.DeviceTokenRO r3 = r0.deviceToken     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = r3.salt     // Catch:{ Exception -> 0x0152 }
            r12.deviceTokenSalt = r3     // Catch:{ Exception -> 0x0152 }
            r0 = r17
            com.ali.user.open.core.model.DeviceTokenRO r3 = r0.deviceToken     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = r3.key     // Catch:{ Exception -> 0x0152 }
            r12.deviceTokenKey = r3     // Catch:{ Exception -> 0x0152 }
        L_0x0064:
            com.ali.user.open.history.HistoryAccount r2 = new com.ali.user.open.history.HistoryAccount     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = r11.userId     // Catch:{ Exception -> 0x0152 }
            java.lang.String r4 = r12.deviceTokenKey     // Catch:{ Exception -> 0x0152 }
            java.lang.String r5 = r11.nick     // Catch:{ Exception -> 0x0152 }
            java.lang.String r6 = r11.phone     // Catch:{ Exception -> 0x0152 }
            java.lang.String r7 = r11.email     // Catch:{ Exception -> 0x0152 }
            r2.<init>(r3, r4, r5, r6, r7)     // Catch:{ Exception -> 0x0152 }
            com.ali.user.open.history.AccountHistoryManager r3 = com.ali.user.open.history.AccountHistoryManager.getInstance()     // Catch:{ Exception -> 0x0152 }
            java.lang.String r4 = r12.deviceTokenSalt     // Catch:{ Exception -> 0x0152 }
            r3.putLoginHistory(r2, r4)     // Catch:{ Exception -> 0x0152 }
            long r4 = r11.loginTime     // Catch:{ Exception -> 0x0152 }
            r12.loginTime = r4     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = r11.sid     // Catch:{ Exception -> 0x0152 }
            r12.sid = r3     // Catch:{ Exception -> 0x0152 }
            long r4 = r11.expires     // Catch:{ Exception -> 0x0152 }
            long r6 = r11.loginTime     // Catch:{ Exception -> 0x0152 }
            long r4 = r15.adjustSessionExpireTime(r4, r6)     // Catch:{ Exception -> 0x0152 }
            r12.expireIn = r4     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = r11.loginPhone     // Catch:{ Exception -> 0x0152 }
            r12.mobile = r3     // Catch:{ Exception -> 0x0152 }
            r0 = r17
            java.lang.String r3 = r0.showLoginId     // Catch:{ Exception -> 0x0152 }
            r12.loginId = r3     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = r11.autoLoginToken     // Catch:{ Exception -> 0x0152 }
            r12.autoLoginToken = r3     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = r11.topAccessToken     // Catch:{ Exception -> 0x0152 }
            r12.topAccessToken = r3     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = r11.topAuthCode     // Catch:{ Exception -> 0x0152 }
            r12.topAuthCode = r3     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = r11.topExpireTime     // Catch:{ Exception -> 0x0152 }
            r12.topExpireTime = r3     // Catch:{ Exception -> 0x0152 }
            java.util.Map<java.lang.String, java.lang.Object> r3 = r11.extendAttribute     // Catch:{ Exception -> 0x0152 }
            r12.otherInfo = r3     // Catch:{ Exception -> 0x0152 }
            r0 = r17
            java.util.Map<java.lang.String, java.lang.String> r3 = r0.extMap     // Catch:{ Exception -> 0x0152 }
            if (r3 == 0) goto L_0x00c1
            r0 = r17
            java.util.Map<java.lang.String, java.lang.String> r3 = r0.extMap     // Catch:{ Exception -> 0x0152 }
            java.lang.String r4 = "bind_token"
            java.lang.Object r3 = r3.get(r4)     // Catch:{ Exception -> 0x0152 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x0152 }
            r12.bindToken = r3     // Catch:{ Exception -> 0x0152 }
        L_0x00c1:
            java.lang.Class<com.ali.user.open.core.service.RpcService> r3 = com.ali.user.open.core.service.RpcService.class
            java.lang.Object r3 = com.ali.user.open.core.AliMemberSDK.getService(r3)     // Catch:{ Exception -> 0x0152 }
            com.ali.user.open.core.service.RpcService r3 = (com.ali.user.open.core.service.RpcService) r3     // Catch:{ Exception -> 0x0152 }
            java.lang.String r4 = com.ali.user.open.core.Site.getMtopInstanceTag(r16)     // Catch:{ Exception -> 0x0152 }
            java.lang.String r5 = r11.sid     // Catch:{ Exception -> 0x0152 }
            java.lang.String r6 = r11.userId     // Catch:{ Exception -> 0x0152 }
            r3.registerSessionInfo(r4, r5, r6)     // Catch:{ Exception -> 0x0152 }
            r14 = 0
            java.util.Map<java.lang.String, java.lang.Object> r3 = r11.extendAttribute     // Catch:{ Exception -> 0x0157 }
            java.lang.String r4 = "ssoDomainList"
            java.lang.Object r13 = r3.get(r4)     // Catch:{ Exception -> 0x0157 }
            if (r13 == 0) goto L_0x00f3
            boolean r3 = r13 instanceof java.util.ArrayList     // Catch:{ Exception -> 0x0157 }
            if (r3 == 0) goto L_0x00f3
            r0 = r13
            java.util.ArrayList r0 = (java.util.ArrayList) r0     // Catch:{ Exception -> 0x0157 }
            r10 = r0
            r3 = 0
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch:{ Exception -> 0x0157 }
            java.lang.Object[] r3 = r10.toArray(r3)     // Catch:{ Exception -> 0x0157 }
            r0 = r3
            java.lang.String[] r0 = (java.lang.String[]) r0     // Catch:{ Exception -> 0x0157 }
            r14 = r0
        L_0x00f3:
            com.ali.user.open.cookies.CookieManagerWrapper r3 = com.ali.user.open.cookies.CookieManagerWrapper.INSTANCE     // Catch:{ Exception -> 0x0152 }
            java.lang.String r4 = ".taobao.com"
            java.lang.String[] r5 = r11.cookies     // Catch:{ Exception -> 0x0152 }
            r3.injectCookie(r4, r5, r14)     // Catch:{ Exception -> 0x0152 }
        L_0x00fd:
            java.lang.String r3 = "session"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "session = "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r5 = r12.toString()
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            com.ali.user.open.core.trace.SDKLogger.d(r3, r4)
            r15.refreshInternalSession(r12)
            java.util.Map<java.lang.String, com.ali.user.open.session.InternalSession> r3 = r15.mIntenalSessions
            r0 = r16
            r3.put(r0, r12)
            java.lang.String r3 = TAG
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            r4.<init>()
            java.lang.String r5 = "session: "
            java.lang.StringBuilder r4 = r4.append(r5)
            java.util.Map<java.lang.String, com.ali.user.open.session.InternalSession> r5 = r15.mIntenalSessions
            int r5 = r5.size()
            java.lang.StringBuilder r4 = r4.append(r5)
            java.lang.String r4 = r4.toString()
            android.util.Log.e(r3, r4)
            goto L_0x000c
        L_0x0146:
            r9 = move-exception
            java.lang.String r3 = TAG     // Catch:{ Exception -> 0x0152 }
            java.lang.String r4 = r9.getMessage()     // Catch:{ Exception -> 0x0152 }
            com.ali.user.open.core.trace.SDKLogger.e(r3, r4, r9)     // Catch:{ Exception -> 0x0152 }
            goto L_0x003e
        L_0x0152:
            r9 = move-exception
            r9.printStackTrace()
            goto L_0x00fd
        L_0x0157:
            r9 = move-exception
            r9.printStackTrace()     // Catch:{ Exception -> 0x0152 }
            goto L_0x00f3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.open.service.impl.SessionManager.refreshWhenLogin(java.lang.String, com.ali.user.open.core.model.LoginReturnData):void");
    }

    /* JADX WARNING: type inference failed for: r7v10, types: [java.lang.Object[]] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void refreshCookie(java.lang.String r10, com.ali.user.open.core.model.LoginReturnData r11) {
        /*
            r9 = this;
            if (r11 == 0) goto L_0x000a
            java.lang.String r7 = r11.data
            boolean r7 = android.text.TextUtils.isEmpty(r7)
            if (r7 == 0) goto L_0x000b
        L_0x000a:
            return
        L_0x000b:
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Exception -> 0x0041 }
            java.lang.String r7 = r11.data     // Catch:{ Exception -> 0x0041 }
            r1.<init>(r7)     // Catch:{ Exception -> 0x0041 }
            java.lang.Class<com.ali.user.open.core.model.LoginDataModel> r7 = com.ali.user.open.core.model.LoginDataModel.class
            java.lang.Object r4 = com.ali.user.open.core.util.JSONUtils.toPOJO(r1, r7)     // Catch:{ Exception -> 0x0041 }
            com.ali.user.open.core.model.LoginDataModel r4 = (com.ali.user.open.core.model.LoginDataModel) r4     // Catch:{ Exception -> 0x0041 }
            r6 = 0
            java.util.Map<java.lang.String, java.lang.Object> r7 = r4.extendAttribute     // Catch:{ Exception -> 0x0043 }
            java.lang.String r8 = "ssoDomainList"
            java.lang.Object r5 = r7.get(r8)     // Catch:{ Exception -> 0x0043 }
            if (r5 == 0) goto L_0x0039
            boolean r7 = r5 instanceof java.util.ArrayList     // Catch:{ Exception -> 0x0043 }
            if (r7 == 0) goto L_0x0039
            r0 = r5
            java.util.ArrayList r0 = (java.util.ArrayList) r0     // Catch:{ Exception -> 0x0043 }
            r3 = r0
            r7 = 0
            java.lang.String[] r7 = new java.lang.String[r7]     // Catch:{ Exception -> 0x0043 }
            java.lang.Object[] r7 = r3.toArray(r7)     // Catch:{ Exception -> 0x0043 }
            r0 = r7
            java.lang.String[] r0 = (java.lang.String[]) r0     // Catch:{ Exception -> 0x0043 }
            r6 = r0
        L_0x0039:
            com.ali.user.open.cookies.CookieManagerWrapper r7 = com.ali.user.open.cookies.CookieManagerWrapper.INSTANCE     // Catch:{ Exception -> 0x0041 }
            java.lang.String[] r8 = r4.cookies     // Catch:{ Exception -> 0x0041 }
            r7.injectCookie(r10, r8, r6)     // Catch:{ Exception -> 0x0041 }
            goto L_0x000a
        L_0x0041:
            r7 = move-exception
            goto L_0x000a
        L_0x0043:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ Exception -> 0x0041 }
            goto L_0x0039
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.open.service.impl.SessionManager.refreshCookie(java.lang.String, com.ali.user.open.core.model.LoginReturnData):void");
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
        long j = 0;
        Session session = new Session();
        session.nick = this.internalSession == null ? "" : this.internalSession.nick;
        session.hid = this.internalSession == null ? "" : this.internalSession.userId;
        session.sid = this.internalSession == null ? "" : this.internalSession.sid;
        session.loginTime = this.internalSession == null ? 0 : this.internalSession.loginTime;
        if (this.internalSession != null) {
            j = this.internalSession.expireIn;
        }
        session.expireIn = j;
        session.avatarUrl = this.internalSession == null ? "" : this.internalSession.avatarUrl;
        session.openId = this.internalSession == null ? "" : this.internalSession.openId;
        session.openSid = this.internalSession == null ? "" : this.internalSession.openSid;
        session.topAccessToken = this.internalSession == null ? "" : this.internalSession.topAccessToken;
        session.topAuthCode = this.internalSession == null ? "" : this.internalSession.topAuthCode;
        session.topExpireTime = this.internalSession == null ? "" : this.internalSession.topExpireTime;
        session.bindToken = this.internalSession == null ? "" : this.internalSession.bindToken;
        return session;
    }

    public Session getSession(String site) {
        InternalSession internalSession2;
        long j = 0;
        Session session = new Session();
        if (!(this.mIntenalSessions == null || (internalSession2 = this.mIntenalSessions.get(site)) == null)) {
            session.nick = internalSession2 == null ? "" : internalSession2.nick;
            session.hid = internalSession2 == null ? "" : internalSession2.userId;
            session.sid = internalSession2 == null ? "" : internalSession2.sid;
            session.loginTime = internalSession2 == null ? 0 : internalSession2.loginTime;
            if (internalSession2 != null) {
                j = internalSession2.expireIn;
            }
            session.expireIn = j;
            session.avatarUrl = internalSession2 == null ? "" : internalSession2.avatarUrl;
            session.openId = internalSession2 == null ? "" : internalSession2.openId;
            session.openSid = internalSession2 == null ? "" : internalSession2.openSid;
            session.topAccessToken = internalSession2 == null ? "" : internalSession2.topAccessToken;
            session.topAuthCode = internalSession2 == null ? "" : internalSession2.topAuthCode;
            session.topExpireTime = internalSession2 == null ? "" : internalSession2.topExpireTime;
            session.bindToken = internalSession2 == null ? "" : internalSession2.bindToken;
        }
        return session;
    }

    public ResultCode logout(String site) {
        CookieManagerWrapper.INSTANCE.clearCookies();
        refreshInternalSession(new InternalSession());
        this.mIntenalSessions = new HashMap();
        ((RpcService) AliMemberSDK.getService(RpcService.class)).logout(Site.getMtopInstanceTag(site));
        return ResultCode.SUCCESS;
    }

    private void registerStorage() {
        try {
            Class[] clsArr = {StorageService.class};
            KernelContext.registerService(clsArr, getServiceInstance("com.ali.user.open.securityguard.SecurityGuardWrapper", (String[]) null, (Object[]) null), (Map<String, String>) null);
        } catch (NoSuchMethodError e2) {
            e2.printStackTrace();
        }
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
