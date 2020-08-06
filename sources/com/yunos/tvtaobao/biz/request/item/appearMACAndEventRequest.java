package com.yunos.tvtaobao.biz.request.item;

import com.alibaba.motu.videoplayermonitor.VPMConstants;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Map;
import org.json.JSONObject;

public class appearMACAndEventRequest extends BaseMtopRequest {
    public appearMACAndEventRequest(String appKey, String o2oShopId, String metaType, String currentMeta, String duration, String routerMAC, String timestamp, String signature, String signVersion) {
        addParams("appKey", appKey);
        addParams("o2oShopId", o2oShopId);
        addParams("metaType", metaType);
        addParams("currentMeta", currentMeta);
        addParams(VPMConstants.MEASURE_DURATION, duration);
        addParams("routerMAC", routerMAC);
        addParams("timestamp", timestamp);
        addParams("signature", signature);
        addParams("signVersion", signVersion);
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        obj.toString();
        ZpLogger.e(this.TAG, "上传mac和事件" + obj.toString());
        return obj.toString();
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.tvtao.tvtaoshakeservice.uploadrouterinfo";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }
}
