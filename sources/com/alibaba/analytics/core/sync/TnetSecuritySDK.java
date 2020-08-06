package com.alibaba.analytics.core.sync;

import android.content.Context;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.utils.Logger;
import com.taobao.orange.OConstant;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import com.ut.mini.core.sign.UTBaseRequestAuthentication;
import com.ut.mini.core.sign.UTSecurityThridRequestAuthentication;
import java.lang.reflect.Method;

public class TnetSecuritySDK {
    private static volatile TnetSecuritySDK mTnetSecuritySDK;
    private String authcode = "";
    private Method getByteArrayMethod = null;
    private Object mDynamicDataStoreCompObj = null;
    private boolean mInitSecurityCheck = false;
    private Object mSecurityGuardManagerObj = null;
    private Object mStaticDataEncryptCompObj = null;
    private Method putByteArrayMethod = null;
    private Method staticBinarySafeDecryptNoB64Method = null;

    private TnetSecuritySDK() {
    }

    public static TnetSecuritySDK getInstance() {
        TnetSecuritySDK tnetSecuritySDK;
        if (mTnetSecuritySDK != null) {
            return mTnetSecuritySDK;
        }
        synchronized (TnetSecuritySDK.class) {
            if (mTnetSecuritySDK == null) {
                mTnetSecuritySDK = new TnetSecuritySDK();
                mTnetSecuritySDK.initSecurityCheck();
            }
            tnetSecuritySDK = mTnetSecuritySDK;
        }
        return tnetSecuritySDK;
    }

    public boolean getInitSecurityCheck() {
        Logger.d("", "mInitSecurityCheck", Boolean.valueOf(this.mInitSecurityCheck));
        return this.mInitSecurityCheck;
    }

    private synchronized void initSecurityCheck() {
        Logger.d();
        try {
            IUTRequestAuthentication requestAuthentication = Variables.getInstance().getRequestAuthenticationInstance();
            if (requestAuthentication instanceof UTBaseRequestAuthentication) {
                this.mInitSecurityCheck = false;
            }
            if (requestAuthentication != null) {
                Class<?> clz0 = Class.forName(OConstant.REFLECT_SECURITYGUARD);
                Class<?> clz1 = Class.forName("com.alibaba.wireless.security.open.staticdataencrypt.IStaticDataEncryptComponent");
                Class<?> clz2 = Class.forName("com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent");
                if (requestAuthentication instanceof UTSecurityThridRequestAuthentication) {
                    this.authcode = ((UTSecurityThridRequestAuthentication) requestAuthentication).getAuthcode();
                }
                if (clz0 == null || clz1 == null || clz2 == null) {
                    this.mInitSecurityCheck = false;
                } else {
                    this.mSecurityGuardManagerObj = clz0.getMethod("getInstance", new Class[]{Context.class}).invoke((Object) null, new Object[]{Variables.getInstance().getContext()});
                    this.mStaticDataEncryptCompObj = clz0.getMethod("getStaticDataEncryptComp", new Class[0]).invoke(this.mSecurityGuardManagerObj, new Object[0]);
                    this.mDynamicDataStoreCompObj = clz0.getMethod("getDynamicDataStoreComp", new Class[0]).invoke(this.mSecurityGuardManagerObj, new Object[0]);
                    this.staticBinarySafeDecryptNoB64Method = clz1.getMethod("staticBinarySafeDecryptNoB64", new Class[]{Integer.TYPE, String.class, byte[].class, String.class});
                    this.putByteArrayMethod = clz2.getMethod("putByteArray", new Class[]{String.class, byte[].class});
                    this.getByteArrayMethod = clz2.getMethod("getByteArray", new Class[]{String.class});
                    this.mInitSecurityCheck = true;
                }
            }
        } catch (Throwable e) {
            this.mInitSecurityCheck = false;
            Logger.e("initSecurityCheck", "e.getCode", e.getCause(), e, e.getMessage());
        }
        return;
    }

    public byte[] staticBinarySafeDecryptNoB64(int i, String str, byte[] bArr) {
        if (this.staticBinarySafeDecryptNoB64Method == null || this.mStaticDataEncryptCompObj == null) {
            return null;
        }
        try {
            Object obj = this.staticBinarySafeDecryptNoB64Method.invoke(this.mStaticDataEncryptCompObj, new Object[]{Integer.valueOf(i), str, bArr, this.authcode});
            Logger.i("", "mStaticDataEncryptCompObj", this.mStaticDataEncryptCompObj, UploadQueueMgr.MSGTYPE_INTERVAL, Integer.valueOf(i), "str", str, "bArr", bArr, "authcode", this.authcode, "obj", obj);
            if (obj == null) {
                return null;
            }
            return (byte[]) obj;
        } catch (Throwable e) {
            Logger.e((String) null, e, new Object[0]);
            return null;
        }
    }

    public int putByteArray(String str, byte[] bArr) {
        if (this.putByteArrayMethod == null || this.mDynamicDataStoreCompObj == null) {
            return 0;
        }
        try {
            Object obj = this.putByteArrayMethod.invoke(this.mDynamicDataStoreCompObj, new Object[]{str, bArr});
            if (obj == null) {
                return 0;
            }
            int ret = ((Integer) obj).intValue();
            Logger.d("", "ret", Integer.valueOf(ret));
            return ret;
        } catch (Throwable e) {
            Logger.e((String) null, e, new Object[0]);
            return -1;
        }
    }

    public byte[] getByteArray(String str) {
        if (this.getByteArrayMethod == null || this.mDynamicDataStoreCompObj == null) {
            return null;
        }
        try {
            Object obj = this.getByteArrayMethod.invoke(this.mDynamicDataStoreCompObj, new Object[]{str});
            if (obj == null) {
                return null;
            }
            return (byte[]) obj;
        } catch (Throwable e) {
            Logger.e((String) null, e, new Object[0]);
            return null;
        }
    }
}
