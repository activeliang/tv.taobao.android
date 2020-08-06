package anet.channel.security;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.util.ALog;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.alibaba.wireless.security.open.dynamicdatastore.IDynamicDataStoreComponent;
import com.alibaba.wireless.security.open.securesignature.ISecureSignatureComponent;
import com.alibaba.wireless.security.open.staticdataencrypt.IStaticDataEncryptComponent;
import com.taobao.orange.OConstant;
import java.util.HashMap;
import java.util.Map;

class SecurityGuardImpl implements ISecurity {
    private static String TAG = "awcn.DefaultSecurityGuard";
    private static Map<String, Integer> algorithMap;
    private static boolean mSecurityGuardValid;
    private String authCode = null;

    static {
        mSecurityGuardValid = false;
        algorithMap = null;
        try {
            Class.forName(OConstant.REFLECT_SECURITYGUARD);
            mSecurityGuardValid = true;
            algorithMap = new HashMap();
            algorithMap.put(ISecurity.SIGN_ALGORITHM_HMAC_SHA1, 3);
            algorithMap.put(ISecurity.CIPHER_ALGORITHM_AES128, 16);
        } catch (Throwable th) {
            mSecurityGuardValid = false;
        }
    }

    SecurityGuardImpl(String authCode2) {
        this.authCode = authCode2;
    }

    public String sign(Context context, String signAlgorithm, String appkey, String data) {
        if (!mSecurityGuardValid || context == null || TextUtils.isEmpty(appkey) || !algorithMap.containsKey(signAlgorithm)) {
            return null;
        }
        try {
            ISecureSignatureComponent ssComp = SecurityGuardManager.getInstance(context).getSecureSignatureComp();
            if (ssComp != null) {
                SecurityGuardParamContext sgc = new SecurityGuardParamContext();
                sgc.appKey = appkey;
                sgc.paramMap.put("INPUT", data);
                sgc.requestType = algorithMap.get(signAlgorithm).intValue();
                return ssComp.signRequest(sgc, this.authCode);
            }
        } catch (Throwable e) {
            ALog.e(TAG, "Securityguard sign request failed.", (String) null, e, new Object[0]);
        }
        return null;
    }

    public byte[] decrypt(Context context, String cipherAlgorithm, String key, byte[] needProcessValue) {
        Integer cipherMode;
        IStaticDataEncryptComponent comp;
        if (!mSecurityGuardValid || context == null || needProcessValue == null || TextUtils.isEmpty(key) || !algorithMap.containsKey(cipherAlgorithm) || (cipherMode = algorithMap.get(cipherAlgorithm)) == null) {
            return null;
        }
        try {
            SecurityGuardManager sgm = SecurityGuardManager.getInstance(context);
            if (sgm == null || (comp = sgm.getStaticDataEncryptComp()) == null) {
                return null;
            }
            return comp.staticBinarySafeDecryptNoB64(cipherMode.intValue(), key, needProcessValue, this.authCode);
        } catch (Throwable t) {
            ALog.e(TAG, "staticBinarySafeDecryptNoB64", (String) null, t, new Object[0]);
            return null;
        }
    }

    public boolean saveBytes(Context context, String key, byte[] value) {
        IDynamicDataStoreComponent ddsComp;
        boolean ret = false;
        if (context == null || value == null || TextUtils.isEmpty(key)) {
            return false;
        }
        try {
            SecurityGuardManager sgMgr = SecurityGuardManager.getInstance(context);
            if (!(sgMgr == null || (ddsComp = sgMgr.getDynamicDataStoreComp()) == null)) {
                ret = ddsComp.putByteArray(key, value) != 0;
            }
        } catch (Throwable t) {
            ALog.e(TAG, "saveBytes", (String) null, t, new Object[0]);
        }
        return ret;
    }

    public byte[] getBytes(Context context, String key) {
        IDynamicDataStoreComponent ddsComp;
        byte[] bytes = null;
        if (context == null || TextUtils.isEmpty(key)) {
            return null;
        }
        try {
            SecurityGuardManager sgMgr = SecurityGuardManager.getInstance(context);
            if (!(sgMgr == null || (ddsComp = sgMgr.getDynamicDataStoreComp()) == null)) {
                bytes = ddsComp.getByteArray(key);
            }
        } catch (Throwable t) {
            ALog.e(TAG, "getBytes", (String) null, t, new Object[0]);
        }
        return bytes;
    }

    public boolean isSecOff() {
        return false;
    }
}
