package mtopsdk.security;

import android.content.Context;
import android.support.annotation.NonNull;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent;
import com.alibaba.wireless.security.open.securitybody.ISecurityBodyComponent;
import com.alibaba.wireless.security.open.umid.IUMIDComponent;
import com.alibaba.wireless.security.open.umid.IUMIDInitListenerEx;
import java.util.HashMap;
import java.util.Map;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.mtop.global.MtopConfig;
import mtopsdk.mtop.util.MtopSDKThreadPoolExecutorFactory;
import mtopsdk.security.ISign;
import mtopsdk.security.util.SecurityUtils;
import mtopsdk.security.util.SignStatistics;
import mtopsdk.xstate.XState;
import mtopsdk.xstate.util.XStateConstants;

public class InnerSignImpl extends AbstractSignImpl {
    private static final String TAG = "mtopsdk.InnerSignImpl";
    private volatile IAVMPGenericComponent.IAVMPGenericInstance mAVMPInstance;
    private SecurityGuardManager sgMgr = null;

    public void init(@NonNull MtopConfig mtopConfig) {
        super.init(mtopConfig);
        final String instanceId = getInstanceId();
        try {
            SignStatistics.setIUploadStats(mtopConfig.uploadStats);
            long initStart = System.currentTimeMillis();
            this.sgMgr = SecurityGuardManager.getInstance(this.mtopConfig.context);
            initUmidToken(getAppKeyByIndex(mtopConfig.appKeyIndex, getAuthCode()), getAuthCode());
            final Context context = this.mtopConfig.context;
            MtopSDKThreadPoolExecutorFactory.submit(new Runnable() {
                public void run() {
                    try {
                        InnerSignImpl.this.getAVMPInstance(context);
                    } catch (Throwable e) {
                        TBSdkLog.e(InnerSignImpl.TAG, instanceId + " [init]getAVMPInstance error when async init AVMP.", e);
                    }
                }
            });
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, instanceId + " [init]ISign init SecurityGuard succeed.init time=" + (System.currentTimeMillis() - initStart));
            }
        } catch (SecException e) {
            int errorCode = e.getErrorCode();
            SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_SG_MANAGER, String.valueOf(errorCode), "");
            TBSdkLog.e(TAG, instanceId + " [init]ISign init SecurityGuard error.errorCode=" + errorCode, (Throwable) e);
        } catch (Exception e2) {
            TBSdkLog.e(TAG, instanceId + " [init]ISign init SecurityGuard error.", (Throwable) e2);
        }
    }

    private void initUmidToken(String appKey, String authCode) {
        final String instanceId = getInstanceId();
        try {
            IUMIDComponent umidComponent = this.sgMgr.getUMIDComp();
            if (umidComponent != null) {
                int env = getEnv();
                if (authCode == null) {
                    authCode = "";
                }
                umidComponent.initUMID(appKey, env, authCode, new IUMIDInitListenerEx() {
                    public void onUMIDInitFinishedEx(String token, int resultCode) {
                        if (resultCode == 200) {
                            XState.setValue(instanceId, XStateConstants.KEY_UMID_TOKEN, token);
                            TBSdkLog.i(InnerSignImpl.TAG, instanceId + " [initUmidToken]IUMIDComponent initUMID succeed,UMID token=" + token);
                            return;
                        }
                        TBSdkLog.w(InnerSignImpl.TAG, instanceId + " [initUmidToken]IUMIDComponent initUMID error,resultCode :" + resultCode);
                    }
                });
            }
        } catch (SecException e) {
            int errorCode = e.getErrorCode();
            SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_INIT_UMID, String.valueOf(errorCode), "");
            TBSdkLog.e(TAG, instanceId + "[initUmidToken]IUMIDComponent initUMID error,errorCode=" + errorCode, (Throwable) e);
        } catch (Exception e2) {
            TBSdkLog.e(TAG, instanceId + "[initUmidToken]IUMIDComponent initUMID error.", (Throwable) e2);
        }
    }

    public String getAppKey(ISign.SignCtx ctx) {
        if (ctx == null) {
            return null;
        }
        return getAppKeyByIndex(ctx.index, ctx.authCode);
    }

    public String getMtopApiSign(HashMap<String, String> params, String appKey, String authCode) {
        String instanceId = getInstanceId();
        if (params == null) {
            TBSdkLog.e(TAG, instanceId + " [getMtopApiSign] params is null.appKey=" + appKey);
            return null;
        } else if (appKey == null) {
            params.put(XStateConstants.KEY_SG_ERROR_CODE, "AppKey is null");
            TBSdkLog.e(TAG, instanceId + " [getMtopApiSign] AppKey is null.");
            return null;
        } else if (this.sgMgr == null) {
            params.put(XStateConstants.KEY_SG_ERROR_CODE, "SGManager is null");
            TBSdkLog.e(TAG, instanceId + " [getMtopApiSign]SecurityGuardManager is null,please call ISign init()");
            return null;
        } else {
            try {
                SecurityGuardParamContext sgContext = new SecurityGuardParamContext();
                sgContext.appKey = appKey;
                sgContext.requestType = 7;
                Map<String, String> paramsMap = convertInnerBaseStrMap(params, appKey);
                if (paramsMap != null && 2 == getEnv()) {
                    paramsMap.put("ATLAS", "daily");
                }
                sgContext.paramMap = paramsMap;
                return this.sgMgr.getSecureSignatureComp().signRequest(sgContext, authCode);
            } catch (SecException e) {
                int errorCode = e.getErrorCode();
                SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_SIGN_MTOP_REQUEST, String.valueOf(errorCode), "");
                params.put(XStateConstants.KEY_SG_ERROR_CODE, String.valueOf(errorCode));
                TBSdkLog.e(TAG, instanceId + " [getMtopApiSign] ISecureSignatureComponent signRequest error,errorCode=" + errorCode, (Throwable) e);
                return null;
            } catch (Exception e2) {
                TBSdkLog.e(TAG, instanceId + " [getMtopApiSign] ISecureSignatureComponent signRequest error", (Throwable) e2);
                return null;
            }
        }
    }

    public Map<String, String> convertInnerBaseStrMap(Map<String, String> originMap, String appkey) {
        if (originMap == null || originMap.size() < 1) {
            return null;
        }
        String ext = originMap.get("extdata");
        String x_features = originMap.get("x-features");
        StringBuilder baseStr = new StringBuilder(64);
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get("utdid"))).append("&");
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get(XStateConstants.KEY_UID))).append("&");
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get(XStateConstants.KEY_REQBIZ_EXT))).append("&");
        baseStr.append(appkey).append("&");
        baseStr.append(SecurityUtils.getMd5(originMap.get("data"))).append("&");
        baseStr.append(originMap.get("t")).append("&");
        baseStr.append(originMap.get("api")).append("&");
        baseStr.append(originMap.get("v")).append("&");
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get("sid"))).append("&");
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get("ttid"))).append("&");
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get("deviceId"))).append("&");
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get("lat"))).append("&");
        baseStr.append(SecurityUtils.convertNull2Default(originMap.get("lng"))).append("&");
        if (StringUtils.isNotBlank(ext)) {
            baseStr.append(ext).append("&");
        }
        baseStr.append(x_features);
        Map<String, String> paramsMap = new HashMap<>(2);
        paramsMap.put("INPUT", baseStr.toString());
        return paramsMap;
    }

    public String getCommonHmacSha1Sign(String baseStr, String appKey) {
        String instanceId = getInstanceId();
        if (baseStr == null || appKey == null) {
            return null;
        }
        if (this.sgMgr == null) {
            TBSdkLog.e(TAG, instanceId + " [getCommonHmacSha1Sign]SecurityGuardManager is null,please call ISign init()");
            return null;
        }
        try {
            Map<String, String> paramsMap = new HashMap<>(1);
            paramsMap.put("INPUT", baseStr);
            SecurityGuardParamContext sgContext = new SecurityGuardParamContext();
            sgContext.appKey = appKey;
            sgContext.requestType = 3;
            sgContext.paramMap = paramsMap;
            return this.sgMgr.getSecureSignatureComp().signRequest(sgContext, getAuthCode());
        } catch (SecException e) {
            int errorCode = e.getErrorCode();
            SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_SIGN_HMAC_SHA1, String.valueOf(errorCode), "");
            TBSdkLog.e(TAG, instanceId + " [getCommonHmacSha1Sign] ISecureSignatureComponent signRequest error,errorCode=" + errorCode, (Throwable) e);
            return null;
        } catch (Exception e2) {
            TBSdkLog.e(TAG, instanceId + " [getCommonHmacSha1Sign] ISecureSignatureComponent signRequest error", (Throwable) e2);
            return null;
        }
    }

    public String getSecBodyDataEx(String time, String appKey, String authCode, int flag) {
        try {
            return ((ISecurityBodyComponent) this.sgMgr.getInterface(ISecurityBodyComponent.class)).getSecurityBodyDataEx(time, appKey, authCode, (HashMap<String, String>) null, flag, getEnv());
        } catch (SecException e) {
            SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_GET_SECBODY, String.valueOf(e.getErrorCode()), String.valueOf(flag));
            TBSdkLog.e(TAG, getInstanceId() + " [getSecBodyDataEx] ISecurityBodyComponent getSecurityBodyDataEx  error.errorCode=" + e.getErrorCode() + ", flag=" + flag, (Throwable) e);
            return null;
        } catch (Exception e2) {
            TBSdkLog.e(TAG, getInstanceId() + " [getSecBodyDataEx] ISecurityBodyComponent getSecurityBodyDataEx  error,flag=" + flag, (Throwable) e2);
            return null;
        }
    }

    public String getAvmpSign(String input, String authCode, int flag) {
        String avmpSign = avmpSign(input);
        if (!StringUtils.isBlank(avmpSign)) {
            return avmpSign;
        }
        TBSdkLog.e(TAG, getInstanceId() + " [getAvmpSign] call avmpSign return null.degrade call getSecBodyDataEx ");
        return getSecBodyDataEx("", "", authCode, flag);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v4, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized java.lang.String avmpSign(java.lang.String r16) {
        /*
            r15 = this;
            monitor-enter(r15)
            r2 = 0
            java.lang.String r8 = ""
            r10 = 4
            byte[] r6 = new byte[r10]     // Catch:{ all -> 0x00ce }
            if (r16 != 0) goto L_0x002b
            java.lang.String r16 = ""
            java.lang.String r10 = "mtopsdk.InnerSignImpl"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x008e }
            r11.<init>()     // Catch:{ Exception -> 0x008e }
            java.lang.String r12 = r15.getInstanceId()     // Catch:{ Exception -> 0x008e }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ Exception -> 0x008e }
            java.lang.String r12 = " [avmpSign] input is null"
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ Exception -> 0x008e }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x008e }
            mtopsdk.common.util.TBSdkLog.e(r10, r11)     // Catch:{ Exception -> 0x008e }
        L_0x002b:
            mtopsdk.mtop.global.MtopConfig r10 = r15.mtopConfig     // Catch:{ Exception -> 0x008e }
            if (r10 == 0) goto L_0x003c
            mtopsdk.mtop.global.MtopConfig r10 = r15.mtopConfig     // Catch:{ Exception -> 0x008e }
            android.content.Context r4 = r10.context     // Catch:{ Exception -> 0x008e }
        L_0x0033:
            com.alibaba.wireless.security.open.avmp.IAVMPGenericComponent$IAVMPGenericInstance r1 = r15.getAVMPInstance(r4)     // Catch:{ Exception -> 0x008e }
            if (r1 != 0) goto L_0x0041
            r3 = r2
        L_0x003a:
            monitor-exit(r15)
            return r3
        L_0x003c:
            android.content.Context r4 = mtopsdk.common.util.MtopUtils.getContext()     // Catch:{ Exception -> 0x008e }
            goto L_0x0033
        L_0x0041:
            java.lang.String r10 = "sign"
            r11 = 0
            byte[] r11 = new byte[r11]     // Catch:{ Exception -> 0x008e }
            java.lang.Class r11 = r11.getClass()     // Catch:{ Exception -> 0x008e }
            r12 = 6
            java.lang.Object[] r12 = new java.lang.Object[r12]     // Catch:{ Exception -> 0x008e }
            r13 = 0
            r14 = 0
            java.lang.Integer r14 = java.lang.Integer.valueOf(r14)     // Catch:{ Exception -> 0x008e }
            r12[r13] = r14     // Catch:{ Exception -> 0x008e }
            r13 = 1
            byte[] r14 = r16.getBytes()     // Catch:{ Exception -> 0x008e }
            r12[r13] = r14     // Catch:{ Exception -> 0x008e }
            r13 = 2
            byte[] r14 = r16.getBytes()     // Catch:{ Exception -> 0x008e }
            int r14 = r14.length     // Catch:{ Exception -> 0x008e }
            java.lang.Integer r14 = java.lang.Integer.valueOf(r14)     // Catch:{ Exception -> 0x008e }
            r12[r13] = r14     // Catch:{ Exception -> 0x008e }
            r13 = 3
            r12[r13] = r8     // Catch:{ Exception -> 0x008e }
            r13 = 4
            r12[r13] = r6     // Catch:{ Exception -> 0x008e }
            r13 = 5
            int r14 = r15.getEnv()     // Catch:{ Exception -> 0x008e }
            java.lang.Integer r14 = java.lang.Integer.valueOf(r14)     // Catch:{ Exception -> 0x008e }
            r12[r13] = r14     // Catch:{ Exception -> 0x008e }
            java.lang.Object r10 = r1.invokeAVMP(r10, r11, r12)     // Catch:{ Exception -> 0x008e }
            byte[] r10 = (byte[]) r10     // Catch:{ Exception -> 0x008e }
            r0 = r10
            byte[] r0 = (byte[]) r0     // Catch:{ Exception -> 0x008e }
            r9 = r0
            if (r9 == 0) goto L_0x008c
            java.lang.String r3 = new java.lang.String     // Catch:{ Exception -> 0x008e }
            r3.<init>(r9)     // Catch:{ Exception -> 0x008e }
            r2 = r3
        L_0x008c:
            r3 = r2
            goto L_0x003a
        L_0x008e:
            r5 = move-exception
            r7 = 0
            java.nio.ByteBuffer r10 = java.nio.ByteBuffer.wrap(r6)     // Catch:{ Throwable -> 0x00d1 }
            java.nio.ByteOrder r11 = java.nio.ByteOrder.LITTLE_ENDIAN     // Catch:{ Throwable -> 0x00d1 }
            java.nio.ByteBuffer r10 = r10.order(r11)     // Catch:{ Throwable -> 0x00d1 }
            int r7 = r10.getInt()     // Catch:{ Throwable -> 0x00d1 }
            java.lang.String r10 = "InvokeAVMP"
            java.lang.String r11 = java.lang.String.valueOf(r7)     // Catch:{ Throwable -> 0x00d1 }
            java.lang.String r12 = ""
            mtopsdk.security.util.SignStatistics.commitStats(r10, r11, r12)     // Catch:{ Throwable -> 0x00d1 }
        L_0x00ab:
            java.lang.String r10 = "mtopsdk.InnerSignImpl"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x00ce }
            r11.<init>()     // Catch:{ all -> 0x00ce }
            java.lang.String r12 = r15.getInstanceId()     // Catch:{ all -> 0x00ce }
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ all -> 0x00ce }
            java.lang.String r12 = " [avmpSign] call avmpInstance.invokeAVMP error.errorCode="
            java.lang.StringBuilder r11 = r11.append(r12)     // Catch:{ all -> 0x00ce }
            java.lang.StringBuilder r11 = r11.append(r7)     // Catch:{ all -> 0x00ce }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x00ce }
            mtopsdk.common.util.TBSdkLog.e((java.lang.String) r10, (java.lang.String) r11, (java.lang.Throwable) r5)     // Catch:{ all -> 0x00ce }
            goto L_0x008c
        L_0x00ce:
            r10 = move-exception
            monitor-exit(r15)
            throw r10
        L_0x00d1:
            r10 = move-exception
            goto L_0x00ab
        */
        throw new UnsupportedOperationException("Method not decompiled: mtopsdk.security.InnerSignImpl.avmpSign(java.lang.String):java.lang.String");
    }

    /* access modifiers changed from: package-private */
    public IAVMPGenericComponent.IAVMPGenericInstance getAVMPInstance(@NonNull Context context) {
        if (this.mAVMPInstance == null) {
            synchronized (InnerSignImpl.class) {
                if (this.mAVMPInstance == null) {
                    try {
                        this.mAVMPInstance = ((IAVMPGenericComponent) SecurityGuardManager.getInstance(context).getInterface(IAVMPGenericComponent.class)).createAVMPInstance("mwua", "sgcipher");
                        if (this.mAVMPInstance == null) {
                            TBSdkLog.e(TAG, getInstanceId() + " [getAVMPInstance] call createAVMPInstance return null.");
                        }
                    } catch (SecException e) {
                        int errorCode = e.getErrorCode();
                        SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_AVMP_INSTANCE, String.valueOf(errorCode), "");
                        TBSdkLog.e(TAG, getInstanceId() + " [getAVMPInstance] call createAVMPInstance error,errorCode=" + errorCode, (Throwable) e);
                    } catch (Exception e2) {
                        TBSdkLog.e(TAG, getInstanceId() + " [getAVMPInstance] call createAVMPInstance error.", (Throwable) e2);
                    }
                }
            }
        }
        return this.mAVMPInstance;
    }

    private String getAppKeyByIndex(int appKeyIndex, String authCode) {
        String appKey = null;
        String instanceId = getInstanceId();
        try {
            appKey = this.sgMgr.getStaticDataStoreComp().getAppKeyByIndex(appKeyIndex, authCode);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                TBSdkLog.i(TAG, instanceId + " [getAppKeyByIndex]getAppKeyByIndex  appKey=" + appKey + ",appKeyIndex=" + appKeyIndex + ",authCode=" + authCode);
            }
        } catch (SecException e) {
            int errorCode = e.getErrorCode();
            SignStatistics.commitStats(SignStatistics.SignStatsType.TYPE_GET_APPKEY, String.valueOf(errorCode), "");
            TBSdkLog.e(TAG, instanceId + " [getAppKeyByIndex]getAppKeyByIndex error.errorCode=" + errorCode + ",appKeyIndex=" + appKeyIndex + ",authCode=" + authCode, (Throwable) e);
        } catch (Exception e2) {
            TBSdkLog.e(TAG, instanceId + " [getAppKeyByIndex]getAppKeyByIndex error.appKeyIndex=" + appKeyIndex + ",authCode=" + authCode, (Throwable) e2);
        }
        return appKey;
    }
}
