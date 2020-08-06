package com.yunos.tvtaobao.biz.request.item;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.taobao.detail.domain.base.Unit;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.bo.TBDetailResultV6;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TBDetailV6HttpRequest extends BaseMtopRequest {
    private String data = null;
    private String host = "https://acs.m.taobao.com/gw/mtop.taobao.detail.getdetail/6.0/?data=";
    private String itemId = "";
    private String tag = "%7B%22itemNumId%22%3A%22";
    private String tag2 = "%22%2C%22detail_v%22%3A%223.1.0%22%7D";

    public TBDetailV6HttpRequest(String itemId2, String areaId) {
        addParams("itemNumId", itemId2);
        if (!TextUtils.isEmpty(areaId)) {
            addParams(BaseConfig.INTENT_KEY_AREAID, areaId);
        }
        addParams("detail_v", "3.1.0");
        this.itemId = itemId2;
    }

    public String getCustomDomain() {
        return "acs.m.taobao.com";
    }

    /* access modifiers changed from: protected */
    public String getApi() {
        return "mtop.taobao.detail.getdetail";
    }

    /* access modifiers changed from: protected */
    public String getApiVersion() {
        return "6.0";
    }

    /* access modifiers changed from: protected */
    public Map<String, String> getAppData() {
        return null;
    }

    /* access modifiers changed from: protected */
    public TBDetailResultV6 resolveResponse(JSONObject obj) throws Exception {
        TBDetailResultV6 tbDetailResultV6 = (TBDetailResultV6) JSON.parseObject(obj.toString(), TBDetailResultV6.class);
        List<Unit> units = resolveDomainUnit(obj);
        if (units != null) {
            tbDetailResultV6.setDomainList(units);
        }
        TBDetailResultV6.Feature feature = resolveSeckKillFeature(obj);
        TBDetailResultV6.Delivery delivery = rexolveSeckKillDelivery(obj);
        TBDetailResultV6.PriceBeanX priceBeanX = resolveSeckKillPrice(obj);
        String s = resolveSeckKillSkuCore(obj);
        if (tbDetailResultV6.getApiStack() != null && tbDetailResultV6.getApiStack().size() > 0) {
            tbDetailResultV6.setContractData(resolveContractData(tbDetailResultV6.getApiStack().get(0)));
        }
        if (feature != null) {
            tbDetailResultV6.setFeature(feature);
        }
        if (delivery != null) {
            tbDetailResultV6.setDelivery(delivery);
        }
        if (priceBeanX != null) {
            tbDetailResultV6.setPrice(priceBeanX);
        }
        if (!"".equals(s)) {
            tbDetailResultV6.setSkuKore(s);
        }
        return tbDetailResultV6;
    }

    public boolean getNeedEcode() {
        return false;
    }

    public boolean getNeedSession() {
        return false;
    }

    public String getTTid() {
        return "142857@taobao_iphone_7.10.3";
    }

    private TBDetailResultV6.PriceBeanX resolveSeckKillPrice(JSONObject data2) {
        JSONObject price1;
        try {
            if (data2.has("price")) {
                TBDetailResultV6.PriceBeanX priceBeanX = new TBDetailResultV6.PriceBeanX();
                JSONObject price = data2.getJSONObject("price");
                if (price.has("price") && (price1 = price.getJSONObject("price")) != null) {
                    TBDetailResultV6.PriceBeanX.PriceBean priceBean = new TBDetailResultV6.PriceBeanX.PriceBean();
                    if (price1.has("priceText")) {
                        priceBean.setPriceText(price1.getString("priceText"));
                    }
                    if (price1.has("priceTitle")) {
                        priceBean.setPriceTitle(price1.getString("priceTitle"));
                    }
                    priceBeanX.setPrice(priceBean);
                    return priceBeanX;
                }
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String resolveSeckKillSkuCore(JSONObject data2) {
        JSONObject data1;
        try {
            if (!data2.has("skuCore") || (data1 = data2.getJSONObject("skuCore")) == null) {
                return "";
            }
            return data1.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    private TBDetailResultV6.Feature resolveSeckKillFeature(JSONObject data2) {
        try {
            if (!data2.has("feature")) {
                return null;
            }
            TBDetailResultV6.Feature feature = new TBDetailResultV6.Feature();
            JSONObject featureBean = data2.getJSONObject("feature");
            if (featureBean.has("secKill")) {
                feature.setSecKill(featureBean.getString("secKill"));
            }
            if (!featureBean.has("hasSku")) {
                return feature;
            }
            feature.setHasSku(featureBean.getString("hasSku"));
            return feature;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private TBDetailResultV6.Delivery rexolveSeckKillDelivery(JSONObject data2) {
        if (!data2.has("delivery")) {
            return null;
        }
        try {
            TBDetailResultV6.Delivery delivery = new TBDetailResultV6.Delivery();
            JSONObject deliveryBean = data2.getJSONObject("delivery");
            if (!deliveryBean.has("postage")) {
                return null;
            }
            delivery.setPostage(deliveryBean.getString("postage"));
            return delivery;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<TBDetailResultV6.ContractData> resolveContractData(TBDetailResultV6.ApiStackBean apiStackBean) {
        try {
            JSONObject object = new JSONObject(apiStackBean.getValue());
            if (object.has("skuVertical")) {
                JSONObject skuVertical = object.getJSONObject("skuVertical");
                if (!skuVertical.has("contractData")) {
                    return null;
                }
                JSONArray contract = skuVertical.getJSONArray("contractData");
                List<TBDetailResultV6.ContractData> result = new ArrayList<>();
                for (int i = 0; i < contract.length(); i++) {
                    JSONObject contractJson = contract.getJSONObject(i);
                    TBDetailResultV6.ContractData contractData = new TBDetailResultV6.ContractData();
                    contractData.versionData = TBDetailResultV6.ContractData.VersionData.resolveVersionData(contractJson.getJSONObject("version"));
                    result.add(contractData);
                }
                return result;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Unit> resolveDomainUnit(JSONObject data2) {
        try {
            JSONArray jsonArray = ((JSONObject) data2.getJSONObject("props").getJSONArray("groupProps").get(0)).getJSONArray("基本信息");
            List<Unit> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2 = (JSONObject) jsonArray.get(i);
                Iterator keys = jsonObject2.keys();
                while (keys.hasNext()) {
                    Unit unit = new Unit();
                    String next = keys.next();
                    String value = jsonObject2.optString(next);
                    unit.name = next;
                    unit.value = value;
                    list.add(unit);
                }
            }
            for (int i2 = 0; i2 < list.size(); i2++) {
                ZpLogger.e("props数据", list.get(i2).name);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
