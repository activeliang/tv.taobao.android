package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.SelectBaseComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.misc.ServiceAddressItem;
import com.taobao.wireless.trade.mbuy.sdk.co.misc.ServiceAddressState;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServiceAddressComponent extends SelectBaseComponent<ServiceAddressOption> {
    private List<ServiceAddressItem> supportedItems;
    private List<ServiceAddressItem> unsupportedItems;

    public ServiceAddressComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    public void reload(JSONObject data) {
        super.reload(data);
        this.supportedItems = null;
        this.unsupportedItems = null;
    }

    /* access modifiers changed from: protected */
    public ServiceAddressOption newOption(JSONObject data) throws Exception {
        return new ServiceAddressOption(data);
    }

    public void setSelectedId(String selectedId) {
        if (selectedId != null) {
            if (selectedId.isEmpty()) {
                setState(ServiceAddressState.NOT_SELECTED);
            } else {
                setState(ServiceAddressState.DIFF_ADDRESS);
            }
        }
        super.setSelectedId(selectedId);
    }

    /* access modifiers changed from: protected */
    public String getOptionId(ServiceAddressOption option) {
        return option.getId();
    }

    public void addOption(ServiceAddressOption option) {
        if (this.options == null) {
            this.options = new ArrayList();
        }
        this.options.add(0, option);
    }

    public ServiceAddressState getState() {
        return ServiceAddressState.getStateByCode(this.fields.getIntValue("state"));
    }

    public String getTips() {
        return this.fields.getString("tips");
    }

    public List<ServiceAddressItem> getSupportedItems() {
        if (this.supportedItems == null) {
            this.supportedItems = getAddressItem("supportItems");
        }
        return this.supportedItems;
    }

    public List<ServiceAddressItem> getUnsupportedItems() {
        if (this.unsupportedItems == null) {
            this.unsupportedItems = getAddressItem("notSupportItems");
        }
        return this.unsupportedItems;
    }

    private List<ServiceAddressItem> getAddressItem(String key) {
        JSONArray itemArray = this.fields.getJSONArray(key);
        if (itemArray == null || itemArray.isEmpty()) {
            return null;
        }
        List<ServiceAddressItem> result = new ArrayList<>(itemArray.size());
        Iterator<Object> it = itemArray.iterator();
        while (it.hasNext()) {
            try {
                result.add(new ServiceAddressItem((JSONObject) it.next()));
            } catch (Throwable th) {
            }
        }
        return result;
    }

    private void setState(ServiceAddressState state) {
        this.fields.put("state", (Object) Integer.valueOf(state.getCode()));
    }
}
