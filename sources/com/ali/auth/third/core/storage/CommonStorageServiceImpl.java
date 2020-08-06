package com.ali.auth.third.core.storage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;
import com.ali.auth.third.core.context.KernelContext;
import com.ali.auth.third.core.exception.SecRuntimeException;
import com.ali.auth.third.core.model.Constants;
import com.ali.auth.third.core.service.StorageService;
import com.ali.auth.third.core.storage.aes.AESCrypt;
import com.ali.auth.third.core.storage.aes.MD5;
import java.security.GeneralSecurityException;
import java.util.TreeMap;

public class CommonStorageServiceImpl implements StorageService {
    private Context context = KernelContext.getApplicationContext();
    private SharedPreferences sp = this.context.getSharedPreferences(Constants.TB_SESSION, 0);
    private String umid;

    public String getValue(String key, boolean isDynamic) {
        String savedValue = this.sp.getString(key, "");
        if (!TextUtils.isEmpty(savedValue)) {
            return symDecrypt(savedValue, MD5.getMD5(KernelContext.timestamp + ""));
        }
        return savedValue;
    }

    @SuppressLint({"NewApi"})
    public void putValue(String key, String value, boolean isDynamic) {
        String value2 = symEncrypt(value, MD5.getMD5(KernelContext.timestamp + ""));
        if (Build.VERSION.SDK_INT >= 9) {
            this.sp.edit().putString(key, value2).apply();
        } else {
            this.sp.edit().putString(key, value2).commit();
        }
    }

    public void removeValue(String key, boolean isDynamic) {
        this.sp.edit().remove(key);
    }

    public void putDDpExValue(String key, String value) {
    }

    public String getDDpExValue(String key) {
        return null;
    }

    public void removeDDpExValue(String key) {
    }

    public String symEncrypt(String str, String seedKeyName) {
        try {
            return AESCrypt.encrypt(seedKeyName, str);
        } catch (GeneralSecurityException e) {
            throw new SecRuntimeException(-1, e);
        }
    }

    public String symDecrypt(String str, String seedKeyName) {
        try {
            return AESCrypt.decrypt(seedKeyName, str);
        } catch (GeneralSecurityException e) {
            throw new SecRuntimeException(-2, e);
        }
    }

    public String getUmid() {
        return this.umid;
    }

    public void setUmid(String umid2) {
        this.umid = umid2;
    }

    public String getAppKey() {
        Object value;
        try {
            ApplicationInfo applicationInfo = this.context.getPackageManager().getApplicationInfo(this.context.getPackageName(), 128);
            if (applicationInfo.metaData == null || (value = applicationInfo.metaData.get("com.alibaba.app.appkey")) == null) {
                return null;
            }
            return value.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean saveSafeToken(String key, String salt) {
        return false;
    }

    public void removeSafeToken(String key) {
    }

    public String signMap(String key, TreeMap<String, String> treeMap) {
        return null;
    }

    public String decrypt(String code) {
        return code;
    }

    public String encode(String code) {
        return code;
    }
}
