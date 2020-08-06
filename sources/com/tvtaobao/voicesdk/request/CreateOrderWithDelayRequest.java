package com.tvtaobao.voicesdk.request;

import com.taobao.wireless.detail.api.ApiUnitHelper;
import com.tvtaobao.voicesdk.utils.LogPrint;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import java.util.Map;
import mtopsdk.xstate.util.XStateConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateOrderWithDelayRequest extends BaseMtopRequest {
    private final String API = "mtop.taobao.tvtao.speech.order.createDelayOrderByAgreementPay";
    private final String VERSION = "1.0";

    public CreateOrderWithDelayRequest(String outPreferentialId, String tagId, String tvOptions, String itemId, String skuId, String deliveryAddressId, int quantity) {
        addParams("itemId", itemId);
        addParams("skuId", skuId);
        addParams("deliveryAddressId", deliveryAddressId);
        addParams("buyQuantity", quantity + "");
        addParams("appkey", Config.getAppKey());
        addParams("deviceId", CloudUUIDWrapper.getCloudUUID());
        addParams("ttid", Config.getTTid());
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("coVersion", "2.0");
            jobj.put("coupon", "true");
            JSONObject mTvTaoEx = new JSONObject();
            mTvTaoEx.put("appkey", Config.getChannel());
            mTvTaoEx.put("isFromVoice", true);
            mTvTaoEx.put("deviceId", CloudUUIDWrapper.getCloudUUID());
            mTvTaoEx.put("outPreferentialId", outPreferentialId);
            mTvTaoEx.put("tagId", tagId);
            mTvTaoEx.put("tvOptions", tvOptions);
            jobj.put("TvTaoEx", mTvTaoEx);
            LogPrint.e(this.TAG, "exParams : " + jobj.toString());
            addParams(ApiUnitHelper.EX_QUERY_KEY, jobj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            addParams("uuid", CloudUUIDWrapper.getCloudUUID());
            addParams(XStateConstants.KEY_UMID_TOKEN, Config.getUmtoken(CoreApplication.getApplication()));
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public Object resolveResponse(JSONObject obj) throws Exception {
        LogPrint.e("TVTao_CreateOrder", "obj : " + obj);
        return obj.toString();
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.tvtao.speech.order.createDelayOrderByAgreementPay";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "1.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }
}
