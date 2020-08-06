package com.ali.user.open.securityguard;

import android.text.TextUtils;
import com.ali.user.open.core.config.ConfigManager;
import com.ali.user.open.core.config.Environment;
import com.ali.user.open.core.context.KernelContext;
import com.ali.user.open.core.exception.SecRuntimeException;
import com.ali.user.open.core.model.WUAData;
import com.ali.user.open.core.service.StorageService;
import com.ali.user.open.core.trace.SDKLogger;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.dynamicdataencrypt.IDynamicDataEncryptComponent;
import com.alibaba.wireless.security.open.safetoken.ISafeTokenComponent;
import com.alibaba.wireless.security.open.securitybody.ISecurityBodyComponent;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SecurityGuardWrapper implements StorageService {
    private static final String SEED_KEY = "seed_key";
    public static final String TAG = "SecurityGuardWrapper";

    private SecurityGuardManager getSecurityGuardManager() {
        try {
            return SecurityGuardManager.getInstance(KernelContext.applicationContext);
        } catch (SecException e) {
            e.printStackTrace();
            throw new SecRuntimeException(e.getErrorCode(), e);
        }
    }

    public String getValue(String key, boolean isDynamic) {
        if (!isDynamic) {
            return getSecurityGuardManager().getStaticDataStoreComp().getExtraData(key, ConfigManager.POSTFIX_OF_SECURITY_JPG);
        }
        try {
            return getSecurityGuardManager().getDynamicDataStoreComp().getString(key);
        } catch (SecException e) {
            return null;
        }
    }

    public void putValue(String key, String value, boolean isDynamic) {
        try {
            getSecurityGuardManager().getDynamicDataStoreComp().putString(key, value);
        } catch (SecException e) {
            e.printStackTrace();
        }
    }

    public void removeValue(String key, boolean isDynamic) {
        if (isDynamic) {
            try {
                getSecurityGuardManager().getDynamicDataStoreComp().removeString(key);
            } catch (SecException e) {
                e.printStackTrace();
            }
        }
    }

    public void putDDpExValue(String key, String value) {
        try {
            getSecurityGuardManager().getDynamicDataStoreComp().putStringDDpEx(key, value, 0);
        } catch (SecException e) {
            e.printStackTrace();
        }
    }

    public String getDDpExValue(String key) {
        try {
            return getSecurityGuardManager().getDynamicDataStoreComp().getStringDDpEx(key, 0);
        } catch (SecException e) {
            return null;
        }
    }

    public void removeDDpExValue(String key) {
        try {
            getSecurityGuardManager().getDynamicDataStoreComp().removeStringDDpEx(key, 0);
        } catch (SecException e) {
            e.printStackTrace();
        }
    }

    public String getUmid() {
        try {
            return getSecurityGuardManager().getUMIDComp().getSecurityToken();
        } catch (SecException e) {
            throw new SecRuntimeException(e.getErrorCode(), e);
        }
    }

    public WUAData getWUA() {
        try {
            long t = System.currentTimeMillis();
            String tString = String.valueOf(t);
            String appkey = getAppKey();
            return new WUAData(appkey, tString, ((ISecurityBodyComponent) getSecurityGuardManager().getInterface(ISecurityBodyComponent.class)).getSecurityBodyDataEx(String.valueOf(t), appkey, "", (HashMap<String, String>) null, 4, convertEnvToMtop()));
        } catch (Exception e) {
            SDKLogger.e("SecurityGuardWrapper", e.getMessage());
            return null;
        }
    }

    private int convertEnvToMtop() {
        if (ConfigManager.getInstance().getEnvironment() == Environment.TEST) {
            return 2;
        }
        if (ConfigManager.getInstance().getEnvironment() == Environment.PRE) {
            return 1;
        }
        return 0;
    }

    public void setUmid(String umid) {
    }

    public String getAppKey() {
        try {
            return getSecurityGuardManager().getStaticDataStoreComp().getAppKeyByIndex(ConfigManager.getAppKeyIndex(), ConfigManager.POSTFIX_OF_SECURITY_JPG);
        } catch (SecException e) {
            throw new SecRuntimeException(e.getErrorCode(), e);
        }
    }

    public synchronized boolean saveSafeToken(String key, String salt) {
        boolean z = false;
        synchronized (this) {
            if (!TextUtils.isEmpty(key) && getSecurityGuardManager() != null) {
                try {
                    ISafeTokenComponent safeTokenComponent = getSecurityGuardManager().getSafeTokenComp();
                    if (safeTokenComponent != null) {
                        int index = 0;
                        String[] additions = {"", "", "", ""};
                        if (0 > additions.length) {
                            index = 0;
                        }
                        z = safeTokenComponent.saveToken(key, salt, additions[index], 0);
                    }
                } catch (SecException e) {
                    e.printStackTrace();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
        return z;
    }

    public void removeSafeToken(String key) {
        try {
            getSecurityGuardManager().getSafeTokenComp().removeToken(key);
        } catch (SecException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String signMap(String key, TreeMap<String, String> signMap) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : signMap.entrySet()) {
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return signForLogin(key, stringBuilder.substring(0, stringBuilder.length() - 1));
    }

    private String signForLogin(String key, String beSigned) {
        try {
            return getSecurityGuardManager().getSafeTokenComp().signWithToken(key, beSigned.getBytes("UTF-8"), 0);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SecException e2) {
            e2.printStackTrace();
        } catch (Throwable e3) {
            e3.printStackTrace();
        }
        return null;
    }

    public String decrypt(String code) {
        if (TextUtils.isEmpty(code)) {
            return code;
        }
        try {
            IDynamicDataEncryptComponent dataEncrypt = getSecurityGuardManager().getDynamicDataEncryptComp();
            if (dataEncrypt == null) {
                return "";
            }
            String decryptCode = dataEncrypt.dynamicDecryptDDp(code);
            return TextUtils.isEmpty(decryptCode) ? code : decryptCode;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String encode(String code) {
        if (TextUtils.isEmpty(code)) {
            return code;
        }
        try {
            IDynamicDataEncryptComponent dataEncrypt = getSecurityGuardManager().getDynamicDataEncryptComp();
            if (dataEncrypt == null) {
                return code;
            }
            String encodeCode = dataEncrypt.dynamicEncryptDDp(code);
            return TextUtils.isEmpty(encodeCode) ? code : encodeCode;
        } catch (Exception e) {
            e.printStackTrace();
            return code;
        }
    }
}
