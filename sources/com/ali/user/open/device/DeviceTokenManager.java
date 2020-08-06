package com.ali.user.open.device;

import android.text.TextUtils;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.config.ConfigManager;
import com.ali.user.open.core.service.StorageService;
import org.json.JSONException;
import org.json.JSONObject;

public class DeviceTokenManager {
    private static final String DEVICE_TOKEN_ACCOUNT = "device_token";
    private static volatile DeviceTokenManager singleton;

    private DeviceTokenManager() {
    }

    public static DeviceTokenManager getInstance() {
        if (singleton == null) {
            synchronized (DeviceTokenManager.class) {
                if (singleton == null) {
                    singleton = new DeviceTokenManager();
                }
            }
        }
        return singleton;
    }

    public void clearDeviceToken() {
        ((StorageService) AliMemberSDK.getService(StorageService.class)).removeDDpExValue(DEVICE_TOKEN_ACCOUNT);
    }

    public void removeDeviceToken(DeviceTokenAccount deviceTokenAccount) {
        if (deviceTokenAccount != null) {
            try {
                ((StorageService) AliMemberSDK.getService(StorageService.class)).removeSafeToken(deviceTokenAccount.tokenKey);
                ((StorageService) AliMemberSDK.getService(StorageService.class)).removeDDpExValue(DEVICE_TOKEN_ACCOUNT);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public void putDeviceToken(DeviceTokenAccount deviceTokenAccount, String salt) {
        if (!ConfigManager.getInstance().isSaveHistoryWithSalt() || ((StorageService) AliMemberSDK.getService(StorageService.class)).saveSafeToken(deviceTokenAccount.tokenKey, salt)) {
            ((StorageService) AliMemberSDK.getService(StorageService.class)).putDDpExValue(DEVICE_TOKEN_ACCOUNT, toJSONString(deviceTokenAccount));
        }
    }

    public DeviceTokenAccount getDeviceToken() {
        try {
            return parseObject(((StorageService) AliMemberSDK.getService(StorageService.class)).getDDpExValue(DEVICE_TOKEN_ACCOUNT));
        } catch (JSONException e) {
            ((StorageService) AliMemberSDK.getService(StorageService.class)).removeDDpExValue(DEVICE_TOKEN_ACCOUNT);
            return null;
        }
    }

    private DeviceTokenAccount parseObject(String deviceJson) throws JSONException {
        JSONObject obj;
        DeviceTokenAccount deviceTokenAccount = new DeviceTokenAccount();
        if (!TextUtils.isEmpty(deviceJson) && (obj = new JSONObject(deviceJson)) != null) {
            deviceTokenAccount.openId = obj.optString("openId");
            deviceTokenAccount.tokenKey = obj.optString("tokenKey");
            deviceTokenAccount.site = obj.optString("site");
            deviceTokenAccount.hid = obj.optString("userId");
            deviceTokenAccount.t = obj.optString("t");
        }
        return deviceTokenAccount;
    }

    private String toJSONString(DeviceTokenAccount deviceTokenAccount) {
        if (deviceTokenAccount == null) {
            return "";
        }
        JSONObject object = new JSONObject();
        try {
            object.put("openId", deviceTokenAccount.openId);
            object.put("tokenKey", deviceTokenAccount.tokenKey);
            object.put("site", deviceTokenAccount.site);
            object.put("userId", deviceTokenAccount.hid);
            object.put("t", String.valueOf(System.currentTimeMillis()));
            return object.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
