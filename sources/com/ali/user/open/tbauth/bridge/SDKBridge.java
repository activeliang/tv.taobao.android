package com.ali.user.open.tbauth.bridge;

import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.service.StorageService;
import com.ali.user.open.core.webview.BridgeCallbackContext;
import com.ali.user.open.core.webview.BridgeMethod;
import com.alibaba.fastjson.JSON;
import org.json.JSONObject;

public class SDKBridge {
    @BridgeMethod
    public void getUmid(BridgeCallbackContext bridgeCallbackContext, String request) {
        StorageService storageService = (StorageService) AliMemberSDK.getService(StorageService.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("umidToken", storageService.getUmid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        bridgeCallbackContext.success(jsonObject.toString());
    }

    @BridgeMethod
    public void getWua(BridgeCallbackContext bridgeCallbackContext, String request) {
        StorageService storageService = (StorageService) AliMemberSDK.getService(StorageService.class);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("wua", JSON.toJSONString(storageService.getWUA()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        bridgeCallbackContext.success(jsonObject.toString());
    }
}
