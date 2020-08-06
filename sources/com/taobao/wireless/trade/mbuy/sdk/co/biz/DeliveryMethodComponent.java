package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentStatus;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.SelectBaseComponent;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;

public class DeliveryMethodComponent extends SelectBaseComponent<DeliveryMethodOption> {
    public ShipDatePickerComponent shipDatePickerComponent;

    public DeliveryMethodComponent(JSONObject data, BuyEngine engine) {
        super(data, engine);
    }

    /* access modifiers changed from: protected */
    public DeliveryMethodOption newOption(JSONObject data) throws Exception {
        return new DeliveryMethodOption(data, this);
    }

    public void reload(JSONObject data) {
        super.reload(data);
        if (this.shipDatePickerComponent != null) {
            this.shipDatePickerComponent.reload();
        }
    }

    /* access modifiers changed from: protected */
    public String getOptionId(DeliveryMethodOption option) {
        return option.getId();
    }

    public String getSelectedOptionName() {
        DeliveryMethodOption option = (DeliveryMethodOption) getSelectedOption();
        if (option != null) {
            return option.getName();
        }
        return null;
    }

    public void setSelectedId(String selectedId) {
        if (this.shipDatePickerComponent == null) {
            super.setSelectedId(selectedId);
        } else if (selectedId != null && !selectedId.equals(getSelectedId())) {
            DeliveryMethodOption option = (DeliveryMethodOption) getOptionById(selectedId);
            if (option.enableDatePicker() || !TextUtils.isEmpty(option.getTip())) {
                this.shipDatePickerComponent.setStatus(ComponentStatus.NORMAL);
            } else {
                this.shipDatePickerComponent.setStatus(ComponentStatus.HIDDEN);
            }
            super.setSelectedId(selectedId, true);
        }
    }

    public JSONObject convertToFinalSubmitData() {
        this.engine.getContext().addRecoveryEntry(this.fields, "title", getTitle());
        this.fields.remove("title");
        return super.convertToFinalSubmitData();
    }
}
