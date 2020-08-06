package com.ali.auth.third.securityguard;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.ali.auth.third.core.config.ConfigManager;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.exception.SecRuntimeException;
import com.ali.auth.third.core.service.StorageService;
import com.alibaba.analytics.core.device.Constants;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.dynamicdataencrypt.IDynamicDataEncryptComponent;
import com.alibaba.wireless.security.open.safetoken.ISafeTokenComponent;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

public class SecurityGuardWrapper implements StorageService {
    private static final String HISTORY_LOGIN_ACCOUNTS = "taesdk_history_acounts";
    private static final String SEED_KEY = "seed_key";
    public static final String TAG = "SecurityGuardWrapper";

    private SecurityGuardManager getSecurityGuardManager() {
        try {
            return SecurityGuardManager.getInstance(KernelContext.context);
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

    public String symEncrypt(String str, String seedKeyName) {
        try {
            byte[] bytes = str.getBytes("UTF-8");
            if (TextUtils.isEmpty(seedKeyName)) {
                seedKeyName = SEED_KEY;
            }
            try {
                return Base64.encodeToString(_symEncrypt(bytes, seedKeyName), 11);
            } catch (SecRuntimeException e) {
                throw e;
            }
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException();
        }
    }

    private byte[] _symEncrypt(byte[] bytes, String seedKeyName) {
        try {
            return getSecurityGuardManager().getStaticKeyEncryptComp().encrypt(16, seedKeyName, bytes);
        } catch (SecException e) {
            throw new SecRuntimeException(e.getErrorCode(), e);
        }
    }

    public String symDecrypt(String str, String seedKeyName) {
        try {
            byte[] bytes = Base64.decode(str, 8);
            if (TextUtils.isEmpty(seedKeyName)) {
                seedKeyName = SEED_KEY;
            }
            return new String(_symDecrypt(bytes, seedKeyName), "UTF-8");
        } catch (SecRuntimeException e) {
            throw e;
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException(e2);
        }
    }

    private byte[] _symDecrypt(byte[] bytes, String seedKeyName) {
        try {
            return getSecurityGuardManager().getStaticKeyEncryptComp().decrypt(16, seedKeyName, bytes);
        } catch (SecException e) {
            throw new SecRuntimeException(e.getErrorCode(), e);
        }
    }

    public String getUmid() {
        try {
            return getSecurityGuardManager().getUMIDComp().getSecurityToken();
        } catch (SecException e) {
            throw new SecRuntimeException(e.getErrorCode(), e);
        }
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
        String beSigned = stringBuilder.substring(0, stringBuilder.length() - 1);
        Log.e("TAG", "map=" + beSigned);
        return signForLogin(key, beSigned);
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

    public String decrypt(String encodedData) {
        if (!TextUtils.isEmpty(encodedData)) {
            try {
                IDynamicDataEncryptComponent dataEncrypt = getSecurityGuardManager().getDynamicDataEncryptComp();
                if (dataEncrypt != null) {
                    if (encodedData.length() <= 4 || encodedData.charAt(3) != '&') {
                        return dataEncrypt.dynamicDecrypt(encodedData);
                    }
                    return dataEncrypt.dynamicDecryptDDp(encodedData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public String encode(String code) {
        if (TextUtils.isEmpty(code)) {
            return code;
        }
        try {
            IDynamicDataEncryptComponent dataEncrypt = getSecurityGuardManager().getDynamicDataEncryptComp();
            if (dataEncrypt != null) {
                String encodeCode = dataEncrypt.dynamicEncrypt(code);
                return TextUtils.isEmpty(encodeCode) ? code : encodeCode;
            }
            UTHitBuilders.UTCustomHitBuilder lHitBuilder = new UTHitBuilders.UTCustomHitBuilder("SessionManagerEncryptNull");
            lHitBuilder.setEventPage(Constants.USERTRACK_EXTEND_PAGE_NAME);
            UTAnalytics.getInstance().getDefaultTracker().send(lHitBuilder.build());
            return code;
        } catch (Exception e) {
            UTHitBuilders.UTCustomHitBuilder lHitBuilder2 = new UTHitBuilders.UTCustomHitBuilder("SessionManagerEncodeException");
            lHitBuilder2.setEventPage(Constants.USERTRACK_EXTEND_PAGE_NAME);
            UTAnalytics.getInstance().getDefaultTracker().send(lHitBuilder2.build());
            e.printStackTrace();
            return code;
        }
    }
}
