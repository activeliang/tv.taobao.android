package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.taobao.wireless.detail.api.ApiUnitHelper;
import com.yunos.tv.core.RtEnv;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.TvOptionsConfig;
import com.yunos.tv.core.util.DeviceUtil;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.BuildOrderRequestBo;
import com.yunos.tvtaobao.payment.utils.ChannelUtils;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class BuildOrderRequest extends BaseMtopRequest {
    private static final String API = "mtop.trade.buildOrder";
    private static final long serialVersionUID = -7010317316195443367L;
    private BuildOrderRequestBo mBuildOrderRequestBo;
    private boolean mHasAddCart;
    private boolean presale = false;

    public BuildOrderRequest(BuildOrderRequestBo buildOrderRequestBo, boolean hasAddCart) {
        this.mBuildOrderRequestBo = buildOrderRequestBo;
        this.mHasAddCart = hasAddCart;
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        JSONObject TvTaoEx;
        Map<String, String> obj = new HashMap<>();
        if (this.mBuildOrderRequestBo != null) {
            if (!TextUtils.isEmpty(this.mBuildOrderRequestBo.getDeliveryId())) {
                obj.put("deliveryId", this.mBuildOrderRequestBo.getDeliveryId());
            }
            if (!TextUtils.isEmpty(this.mBuildOrderRequestBo.getCartIds())) {
                obj.put("cartIds", this.mBuildOrderRequestBo.getCartIds());
            }
            if (this.mBuildOrderRequestBo.isSettlementAlone()) {
                obj.put("isSettlementAlone", "true");
                obj.put("buyParam", this.mBuildOrderRequestBo.getBuyParam());
            } else if (!TextUtils.isEmpty(this.mBuildOrderRequestBo.getCartIds())) {
                obj.put("cartIds", this.mBuildOrderRequestBo.getCartIds());
            }
            String s = this.mBuildOrderRequestBo.getExtParams();
            ZpLogger.i(this.TAG, "1 exParams : " + s);
            if (TextUtils.isEmpty(s)) {
                JSONObject job = new JSONObject();
                try {
                    job.put("coVersion", "2.0");
                    job.put("coupon", "true");
                    job.put("biz_scene", "TV_PAY");
                    job.put("notAutoAgreementPay", 1);
                    JSONObject tvtaoEx = new JSONObject();
                    String appkey = SharePreferences.getString("device_appkey", "");
                    String brandName = SharePreferences.getString("device_brandname", "");
                    if (!appkey.equals(Config.YITIJI) || !brandName.equals("海尔")) {
                        tvtaoEx.put("appKey", Config.getChannel());
                    } else {
                        tvtaoEx.put("appKey", "2017092310");
                    }
                    if (ChannelUtils.isThisTag(ChannelUtils.HY)) {
                        tvtaoEx.put("appKey", (String) RtEnv.get("APPKEY"));
                        tvtaoEx.put("subkey", (String) RtEnv.get(RtEnv.SUBKEY));
                    }
                    tvtaoEx.put("deviceId", DeviceUtil.getStbID());
                    tvtaoEx.put("cartFlag", "cart".equals(this.mBuildOrderRequestBo.getFrom()) ? "1" : "0");
                    String tvOptions = TvOptionsConfig.getTvOptions();
                    if ("cart".equals(this.mBuildOrderRequestBo.getFrom())) {
                        tvtaoEx.put("tvOptions", tvOptions.substring(0, 5) + "1");
                    } else {
                        tvtaoEx.put("tvOptions", tvOptions);
                    }
                    job.put("TvTaoEx", tvtaoEx);
                    ZpLogger.i("[tvOptionsDebug] mtop.trade.buildOrder tvTaoEx ----> ", tvtaoEx != null ? tvtaoEx.toString() : "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                s = job.toString();
            } else {
                try {
                    JSONObject jobj = new JSONObject(s);
                    jobj.put("coVersion", "2.0");
                    jobj.put("coupon", "true");
                    jobj.put("biz_scene", "TV_PAY");
                    jobj.put("notAutoAgreementPay", 1);
                    if (jobj.has("TvTaoEx")) {
                        TvTaoEx = jobj.getJSONObject("TvTaoEx");
                    } else {
                        TvTaoEx = new JSONObject();
                    }
                    String appkey2 = SharePreferences.getString("device_appkey", "");
                    String brandName2 = SharePreferences.getString("device_brandname", "");
                    if (!appkey2.equals(Config.YITIJI) || !brandName2.equals("海尔")) {
                        TvTaoEx.put("appKey", Config.getChannel());
                    } else {
                        TvTaoEx.put("appKey", "2017092310");
                    }
                    if (ChannelUtils.isThisTag(ChannelUtils.HY)) {
                        TvTaoEx.put("appKey", (String) RtEnv.get("APPKEY"));
                        TvTaoEx.put("subkey", (String) RtEnv.get(RtEnv.SUBKEY));
                    }
                    TvTaoEx.put("deviceId", DeviceUtil.getStbID());
                    TvTaoEx.put("cartFlag", this.mBuildOrderRequestBo.getFrom().equals("cart") ? "1" : "0");
                    String tvOptions2 = TvOptionsConfig.getTvOptions();
                    if (this.mBuildOrderRequestBo.getFrom().equals("cart")) {
                        TvTaoEx.put("tvOptions", tvOptions2.substring(0, 5) + "1");
                    } else {
                        TvTaoEx.put("tvOptions", tvOptions2);
                    }
                    if (!jobj.has("TvTaoEx")) {
                        jobj.put("TvTaoEx", TvTaoEx);
                    }
                    s = jobj.toString();
                    ZpLogger.i("[tvOptionsDebug] mtop.trade.buildOrder tvTaoEx ----> ", TvTaoEx != null ? TvTaoEx.toString() : "");
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
            ZpLogger.w(this.TAG, "2 exParams : " + s);
            obj.put(ApiUnitHelper.EX_QUERY_KEY, s);
            obj.put("buyNow", String.valueOf(this.mBuildOrderRequestBo.isBuyNow()));
            if (this.mBuildOrderRequestBo.isBuyNow()) {
                if (!TextUtils.isEmpty(this.mBuildOrderRequestBo.getItemId())) {
                    obj.put("itemId", this.mBuildOrderRequestBo.getItemId());
                }
                obj.put("quantity", String.valueOf(this.mBuildOrderRequestBo.getQuantity()));
                if (!TextUtils.isEmpty(this.mBuildOrderRequestBo.getSkuId())) {
                    obj.put("skuId", this.mBuildOrderRequestBo.getSkuId());
                }
                if (!TextUtils.isEmpty(this.mBuildOrderRequestBo.getServiceId())) {
                    obj.put(BaseConfig.INTENT_KEY_SERVIECID, this.mBuildOrderRequestBo.getServiceId());
                }
                if (!TextUtils.isEmpty(this.mBuildOrderRequestBo.getActivityId())) {
                    obj.put(BaseConfig.ACTIVITY_ID, this.mBuildOrderRequestBo.getActivityId());
                }
                if (!TextUtils.isEmpty(this.mBuildOrderRequestBo.getTgKey())) {
                    obj.put("tgKey", this.mBuildOrderRequestBo.getTgKey());
                }
            }
        }
        return obj;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return API;
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "3.0";
    }

    public boolean getNeedEcode() {
        return true;
    }

    public boolean getNeedSession() {
        return true;
    }

    /* access modifiers changed from: protected */
    public String resolveResponse(JSONObject obj) throws Exception {
        if (obj == null) {
            return null;
        }
        return obj.toString();
    }
}
