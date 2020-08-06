package com.ali.user.open.core.webview;

import com.ali.user.open.core.callback.DataProvider;
import com.ali.user.open.core.config.ConfigManager;
import org.json.JSONObject;

public class UserInfoBridge {
    @BridgeMethod
    public void getInfoByNative(BridgeCallbackContext bridgeCallbackContext, String paramString) {
        if (bridgeCallbackContext != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                DataProvider loginEntrenceCallback = ConfigManager.getInstance().getLoginEntrenceCallback();
                if (loginEntrenceCallback != null) {
                    jsonObject.put("loginEntrance", loginEntrenceCallback.getLoginEntrance());
                }
                bridgeCallbackContext.success(jsonObject.toString());
            } catch (Throwable e) {
                bridgeCallbackContext.onFailure(1001, e.getMessage());
            }
        }
    }
}
