package com.taobao.wireless.trade.mbuy.sdk.co.biz;

import android.text.TextUtils;
import com.taobao.wireless.trade.mbuy.sdk.co.Component;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentStatus;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentTag;
import com.taobao.wireless.trade.mbuy.sdk.co.ComponentType;

public class ShipDatePickerComponent extends Component {
    private DeliveryMethodComponent dmComponent;

    public ShipDatePickerComponent(Component dmComponent2) {
        this.dmComponent = (DeliveryMethodComponent) dmComponent2;
        this.dmComponent.shipDatePickerComponent = this;
        setStatus();
    }

    public void reload() {
        setStatus();
    }

    public void setStatus() {
        DeliveryMethodOption option = (DeliveryMethodOption) this.dmComponent.getSelectedOption();
        setStatus((option == null || (!option.enableDatePicker() && TextUtils.isEmpty(option.getTip()))) ? ComponentStatus.HIDDEN : ComponentStatus.NORMAL);
    }

    public String getTag() {
        return ComponentTag.SHIP_DATE_PICKER.desc;
    }

    public ComponentType getType() {
        return ComponentType.BIZ;
    }

    public boolean enableDatePicker() {
        return ((DeliveryMethodOption) this.dmComponent.getSelectedOption()).enableDatePicker();
    }

    public DeliveryMethodDatePicker getDatePicker() {
        return ((DeliveryMethodOption) this.dmComponent.getSelectedOption()).getDatePicker();
    }

    public String getTip() {
        return ((DeliveryMethodOption) this.dmComponent.getSelectedOption()).getTip();
    }

    public String getServiceIcon() {
        return ((DeliveryMethodOption) this.dmComponent.getSelectedOption()).getServiceIcon();
    }

    public String getProtocolShipIcon() {
        return ((DeliveryMethodOption) this.dmComponent.getSelectedOption()).getProtocolShipIcon();
    }

    public String getTitle() {
        return "配送方式";
    }
}
