package com.ut.mini.core.sign;

import android.content.Context;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.utils.Logger;
import com.taobao.orange.OConstant;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class UTSecurityThridRequestAuthentication implements IUTRequestAuthentication {
    private static final String TAG = "UTSecurityThridRequestAuthentication";
    private String authcode;
    private String mAppkey = null;
    private boolean mBInitSecurityCheck = false;
    private int s_secureIndex = 3;
    private Object s_secureSignatureCompObj = null;
    private Object s_securityGuardManagerObj = null;
    private Class s_securityGuardParamContextClz = null;
    private Field s_securityGuardParamContext_appKey = null;
    private Field s_securityGuardParamContext_paramMap = null;
    private Field s_securityGuardParamContext_requestType = null;
    private Method s_signRequestMethod = null;

    public String getAppkey() {
        return this.mAppkey;
    }

    public String getAuthcode() {
        return this.authcode;
    }

    public UTSecurityThridRequestAuthentication(String aAppkey, String authCode) {
        this.mAppkey = aAppkey;
        this.authcode = authCode;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v19, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: java.lang.String} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getSign(java.lang.String r15) {
        /*
            r14 = this;
            r6 = 0
            r13 = 2
            r12 = 1
            r11 = 0
            java.lang.String r5 = ""
            java.lang.Object[] r7 = new java.lang.Object[r13]
            java.lang.String r8 = "toBeSignedStr"
            r7[r11] = r8
            r7[r12] = r15
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r5, (java.lang.Object[]) r7)
            boolean r5 = r14.mBInitSecurityCheck
            if (r5 != 0) goto L_0x001a
            r14.initSecurityCheck()
        L_0x001a:
            java.lang.String r5 = r14.mAppkey
            if (r5 != 0) goto L_0x002d
            java.lang.String r5 = "UTSecurityThridRequestAuthentication"
            java.lang.Object[] r7 = new java.lang.Object[r12]
            java.lang.String r8 = "There is no appkey,please check it!"
            r7[r11] = r8
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r5, (java.lang.Object[]) r7)
            r3 = r6
        L_0x002c:
            return r3
        L_0x002d:
            if (r15 != 0) goto L_0x0031
            r3 = r6
            goto L_0x002c
        L_0x0031:
            r3 = 0
            java.lang.Object r5 = r14.s_securityGuardManagerObj
            if (r5 == 0) goto L_0x00a2
            java.lang.Class r5 = r14.s_securityGuardParamContextClz
            if (r5 == 0) goto L_0x00a2
            java.lang.reflect.Field r5 = r14.s_securityGuardParamContext_appKey
            if (r5 == 0) goto L_0x00a2
            java.lang.reflect.Field r5 = r14.s_securityGuardParamContext_paramMap
            if (r5 == 0) goto L_0x00a2
            java.lang.reflect.Field r5 = r14.s_securityGuardParamContext_requestType
            if (r5 == 0) goto L_0x00a2
            java.lang.reflect.Method r5 = r14.s_signRequestMethod
            if (r5 == 0) goto L_0x00a2
            java.lang.Object r5 = r14.s_secureSignatureCompObj
            if (r5 == 0) goto L_0x00a2
            java.lang.Class r5 = r14.s_securityGuardParamContextClz     // Catch:{ Exception -> 0x009b }
            java.lang.Object r4 = r5.newInstance()     // Catch:{ Exception -> 0x009b }
            java.lang.reflect.Field r5 = r14.s_securityGuardParamContext_appKey     // Catch:{ Exception -> 0x009b }
            java.lang.String r7 = r14.mAppkey     // Catch:{ Exception -> 0x009b }
            r5.set(r4, r7)     // Catch:{ Exception -> 0x009b }
            java.lang.reflect.Field r5 = r14.s_securityGuardParamContext_paramMap     // Catch:{ Exception -> 0x009b }
            java.lang.Object r2 = r5.get(r4)     // Catch:{ Exception -> 0x009b }
            java.util.Map r2 = (java.util.Map) r2     // Catch:{ Exception -> 0x009b }
            java.lang.String r5 = "INPUT"
            r2.put(r5, r15)     // Catch:{ Exception -> 0x009b }
            java.lang.reflect.Field r5 = r14.s_securityGuardParamContext_requestType     // Catch:{ Exception -> 0x009b }
            int r7 = r14.s_secureIndex     // Catch:{ Exception -> 0x009b }
            java.lang.Integer r7 = java.lang.Integer.valueOf(r7)     // Catch:{ Exception -> 0x009b }
            r5.set(r4, r7)     // Catch:{ Exception -> 0x009b }
            java.lang.reflect.Method r5 = r14.s_signRequestMethod     // Catch:{ Exception -> 0x009b }
            java.lang.Object r7 = r14.s_secureSignatureCompObj     // Catch:{ Exception -> 0x009b }
            r8 = 2
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ Exception -> 0x009b }
            r9 = 0
            r8[r9] = r4     // Catch:{ Exception -> 0x009b }
            r9 = 1
            java.lang.String r10 = r14.authcode     // Catch:{ Exception -> 0x009b }
            r8[r9] = r10     // Catch:{ Exception -> 0x009b }
            java.lang.Object r5 = r5.invoke(r7, r8)     // Catch:{ Exception -> 0x009b }
            r0 = r5
            java.lang.String r0 = (java.lang.String) r0     // Catch:{ Exception -> 0x009b }
            r3 = r0
        L_0x008b:
            java.lang.String r5 = ""
            java.lang.Object[] r6 = new java.lang.Object[r13]
            java.lang.String r7 = "lSignedStr"
            r6[r11] = r7
            r6[r12] = r3
            com.alibaba.analytics.utils.Logger.d((java.lang.String) r5, (java.lang.Object[]) r6)
            goto L_0x002c
        L_0x009b:
            r1 = move-exception
            java.lang.Object[] r5 = new java.lang.Object[r11]
            com.alibaba.analytics.utils.Logger.e(r6, r1, r5)
            goto L_0x008b
        L_0x00a2:
            java.lang.String r5 = "UTSecurityThridRequestAuthentication.getSign"
            r6 = 12
            java.lang.Object[] r6 = new java.lang.Object[r6]
            java.lang.String r7 = "s_securityGuardManagerObj"
            r6[r11] = r7
            java.lang.Object r7 = r14.s_securityGuardManagerObj
            r6[r12] = r7
            java.lang.String r7 = "s_securityGuardParamContextClz"
            r6[r13] = r7
            r7 = 3
            java.lang.Class r8 = r14.s_securityGuardParamContextClz
            r6[r7] = r8
            r7 = 4
            java.lang.String r8 = "s_securityGuardParamContext_appKey"
            r6[r7] = r8
            r7 = 5
            java.lang.reflect.Field r8 = r14.s_securityGuardParamContext_appKey
            r6[r7] = r8
            r7 = 6
            java.lang.String r8 = "s_securityGuardParamContext_paramMap"
            r6[r7] = r8
            r7 = 7
            java.lang.reflect.Field r8 = r14.s_securityGuardParamContext_paramMap
            r6[r7] = r8
            r7 = 8
            java.lang.String r8 = "s_securityGuardParamContext_requestType"
            r6[r7] = r8
            r7 = 9
            java.lang.reflect.Field r8 = r14.s_securityGuardParamContext_requestType
            r6[r7] = r8
            r7 = 10
            java.lang.String r8 = "s_signRequestMethod"
            r6[r7] = r8
            r7 = 11
            java.lang.reflect.Method r8 = r14.s_signRequestMethod
            r6[r7] = r8
            com.alibaba.analytics.utils.Logger.w((java.lang.String) r5, (java.lang.Object[]) r6)
            goto L_0x008b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.core.sign.UTSecurityThridRequestAuthentication.getSign(java.lang.String):java.lang.String");
    }

    private synchronized void initSecurityCheck() {
        if (!this.mBInitSecurityCheck) {
            Class<?> clz = null;
            try {
                clz = Class.forName(OConstant.REFLECT_SECURITYGUARD);
                this.s_securityGuardManagerObj = clz.getMethod("getInstance", new Class[]{Context.class}).invoke((Object) null, new Object[]{Variables.getInstance().getContext()});
                this.s_secureSignatureCompObj = clz.getMethod("getSecureSignatureComp", new Class[0]).invoke(this.s_securityGuardManagerObj, new Object[0]);
            } catch (Throwable e) {
                Logger.w(TAG, "initSecurityCheck", e);
            }
            if (clz != null) {
                try {
                    this.s_securityGuardParamContextClz = Class.forName("com.alibaba.wireless.security.open.SecurityGuardParamContext");
                    this.s_securityGuardParamContext_appKey = this.s_securityGuardParamContextClz.getDeclaredField("appKey");
                    this.s_securityGuardParamContext_paramMap = this.s_securityGuardParamContextClz.getDeclaredField("paramMap");
                    this.s_securityGuardParamContext_requestType = this.s_securityGuardParamContextClz.getDeclaredField("requestType");
                    this.s_signRequestMethod = Class.forName("com.alibaba.wireless.security.open.securesignature.ISecureSignatureComponent").getMethod("signRequest", new Class[]{this.s_securityGuardParamContextClz, String.class});
                } catch (Throwable e2) {
                    Logger.w(TAG, "initSecurityCheck", e2);
                }
            }
            this.mBInitSecurityCheck = true;
        }
    }
}
