package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.taobao.wireless.detail.api.ApiUnitHelper;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.TvOptionsConfig;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.ProductTagBo;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetProductTagRequest extends BaseMtopRequest {
    private static final String TAG = GetProductTagRequest.class.getSimpleName();
    private final String API = "mtop.taobao.tvtao.itemservice.getdetail";
    private final String VERSION = "1.0";
    private String detail_v;

    public GetProductTagRequest(String itemId, List<String> list, boolean isZTC, String source, boolean isPre, String amount, String extParams) {
        addParams("itemId", itemId);
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                array.put(list.get(i));
            }
        }
        try {
            object.put("traceRoutes", array);
            object.put("price", amount);
            String tvOptions = TvOptionsConfig.getTvOptions();
            if (!TextUtils.isEmpty(tvOptions)) {
                object.put("tvOptions", tvOptions);
            }
            ZpLogger.e("GetProductTagRequest", object.toString());
            addParams(ApiUnitHelper.EX_QUERY_KEY, object.toString());
        } catch (JSONException e) {
        }
        if (this.detail_v != null) {
            addParams("detail_v", this.detail_v);
        }
        String appkey = SharePreferences.getString("device_appkey", "");
        String brandName = SharePreferences.getString("device_brandname", "");
        if (!appkey.equals(Config.YITIJI) || !brandName.equals("海尔")) {
            addParams("appKey", Config.getChannel());
        } else {
            addParams("appKey", "2017092310");
        }
        ZpLogger.e("GetProductTagRequest", "isPre = " + isPre);
        ZpLogger.e("GetProductTagRequest", "amount = " + amount);
        addParams(BaseConfig.INTENT_KEY_IS_PRE, String.valueOf(isPre));
        if (amount != null) {
            addParams("amount", amount);
        }
        addParams("v", "2.0");
        addParams("extParams", extParams);
    }

    /* access modifiers changed from: protected */
    public ProductTagBo resolveResponse(JSONObject obj) throws Exception {
        ZpLogger.e(TAG, "obj = " + obj);
        ZpLogger.e(TAG, "resolveResponse has tag " + obj.has("tag"));
        ProductTagBo productTagBo = new ProductTagBo();
        if (obj.has("tag")) {
            productTagBo = (ProductTagBo) JSON.parseObject(obj.getJSONObject("tag").toString(), productTagBo.getClass());
        }
        if (obj.has("item")) {
            JSONObject item = obj.getJSONObject("item");
            if (item.has("pointBlacklisted")) {
                productTagBo.setPointBlacklisted(item.getString("pointBlacklisted"));
            }
        }
        if (obj.has("couponType")) {
            productTagBo.setCouponType(obj.getString("couponType"));
        }
        return productTagBo;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.tvtao.itemservice.getdetail";
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
