package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.SelectBaseComponent;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngineContext;
import com.taobao.wireless.trade.mbuy.sdk.engine.RollbackProtocol;
import com.yunos.tvtaobao.biz.common.BaseConfig;

public class AddressComponent extends SelectBaseComponent<AddressOption> {
    private static final String FIELD_LATITUDE = "lat";
    private static final String FIELD_LONGITUDE = "lng";

    public AddressComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    /* access modifiers changed from: protected */
    public AddressOption newOption(JSONObject data) {
        return new AddressOption(data);
    }

    /* access modifiers changed from: protected */
    public String getOptionId(AddressOption option) {
        return option.getId();
    }

    public void setSelectedId(String selectedId, String longitude, String latitude) {
        if (selectedId != null && !selectedId.isEmpty()) {
            BuyEngineContext context = this.engine.getContext();
            final String originalSelectedId = getSelectedId();
            final String originalLongitude = this.fields.getString("lng");
            final String originalLatitude = this.fields.getString("lat");
            context.setRollbackProtocol(new RollbackProtocol() {
                public void rollback() {
                    AddressComponent.this.fields.put("selectedId", (Object) originalSelectedId);
                    if (originalLongitude != null) {
                        AddressComponent.this.fields.put("lng", (Object) originalLongitude);
                    }
                    if (originalLatitude != null) {
                        AddressComponent.this.fields.put("lat", (Object) originalLatitude);
                    }
                }
            });
            this.fields.put("selectedId", (Object) selectedId);
            if (longitude != null) {
                this.fields.put("lng", (Object) longitude);
            }
            if (latitude != null) {
                this.fields.put("lat", (Object) latitude);
            }
            notifyLinkageDelegate();
        }
    }

    public void setSelectedId(String selectedId) {
        setSelectedId(selectedId, (String) null, (String) null);
    }

    public String getAgencyReceive() {
        return this.fields.getString("agencyReceive");
    }

    public String getMdSellerId() {
        return this.fields.getString("mdSellerId");
    }

    public void setUseStation(boolean useStation) {
        this.fields.put("useStation", (Object) Boolean.valueOf(useStation));
    }

    public boolean isUseStation() {
        return this.fields.getBooleanValue("useStation");
    }

    public void setUseMDZT(boolean useMDZT) {
        this.data.put("useMDZT", (Object) Boolean.valueOf(useMDZT));
    }

    public String getAddressShowType() {
        return this.fields.getString("addressShowType");
    }

    public String getAgencyReceiveH5Url() {
        return this.fields.getString("agencyReceiveH5Url");
    }

    public String getAgencyReceiveHelpUrl() {
        return this.fields.getString("agencyReceiveHelpUrl");
    }

    public String getSource() {
        return this.fields.getString(BaseConfig.INTENT_KEY_SOURCE);
    }

    public String getLinkAddressId() {
        return this.fields.getString("linkAddressId");
    }

    public void setLinkAddressId(String linkAddressId) {
        this.fields.put("linkAddressId", (Object) linkAddressId);
    }

    public String getSites() {
        JSONArray sites = this.fields.getJSONArray("sites");
        if (sites == null || sites.isEmpty()) {
            return null;
        }
        return sites.toJSONString();
    }

    public void setSiteInfo(String siteInfoJsonStr) {
        if (!TextUtils.isEmpty(siteInfoJsonStr)) {
            JSONObject siteInfo = JSON.parseObject(siteInfoJsonStr);
            if (siteInfo != null) {
                final String originalSiteInfo = this.fields.getString("siteInfo");
                this.engine.getContext().setRollbackProtocol(new RollbackProtocol() {
                    public void rollback() {
                        if (originalSiteInfo != null) {
                            AddressComponent.this.fields.put("siteInfo", (Object) originalSiteInfo);
                        } else {
                            AddressComponent.this.fields.remove("siteInfo");
                        }
                    }
                });
                this.fields.put("siteInfo", (Object) siteInfo);
                notifyLinkageDelegate();
                return;
            }
            return;
        }
        this.fields.remove("siteInfo");
    }
}
