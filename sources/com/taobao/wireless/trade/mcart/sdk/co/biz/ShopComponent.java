package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.Component;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;
import com.taobao.wireless.trade.mcart.sdk.engine.CartEngine;
import com.taobao.wireless.trade.mcart.sdk.engine.CartEngineContext;
import com.taobao.wireless.trade.mcart.sdk.engine.CartParseModule;
import com.taobao.wireless.trade.mcart.sdk.utils.McartConstants;
import com.taobao.wireless.trade.mcart.sdk.utils.NotificationCenterImpl;
import java.util.List;

public class ShopComponent extends Component {
    private Coudan coudan = null;

    public ShopComponent(JSONObject data, CartFrom cartFrom) {
        super(data, cartFrom);
    }

    public String getSeller() {
        return this.fields.getString("seller");
    }

    public long getSellerId() {
        return this.fields.getLongValue("sellerId");
    }

    public String getShopId() {
        return this.fields.getString("shopId");
    }

    public String getType() {
        return this.fields.getString("sType");
    }

    public boolean isValid() {
        if (this.fields == null || !this.fields.containsKey("valid")) {
            return true;
        }
        return this.fields.getBooleanValue("valid");
    }

    public String getUrl() {
        JSONObject controlParas;
        String urltmp = this.fields.getString("url");
        if (urltmp == null) {
            return urltmp;
        }
        int beginIndex = urltmp.indexOf("${");
        int endIndex = urltmp.indexOf("}");
        if (beginIndex < 0 || endIndex < 0) {
            return urltmp;
        }
        String urlKey = urltmp.substring(beginIndex + 2, endIndex);
        CartEngineContext context = CartEngine.getInstance(this.cartFrom).getContext();
        if (context == null || (controlParas = context.getControlParas()) == null || !controlParas.containsKey(urlKey)) {
            return urltmp;
        }
        String urltmp2 = urltmp.replace("${" + urlKey + "}", controlParas.getString(urlKey));
        int beginShopIndex = urltmp2.indexOf("${");
        int endShopIndex = urltmp2.indexOf("}");
        if (beginShopIndex < 0 || endShopIndex < 0) {
            return urltmp2;
        }
        return urltmp2.replace("${shopId}", this.fields.getString("shopId"));
    }

    public String getIcon() {
        return this.fields.getString("icon");
    }

    public String getTitle() {
        return this.fields.getString("title");
    }

    public String getSubTitle() {
        return this.fields.getString("subTitle");
    }

    public boolean isChecked() {
        return this.fields.getBooleanValue("checked");
    }

    public boolean isDouble11Shop() {
        return this.fields.getBooleanValue("is11");
    }

    public void setChecked(boolean checked, boolean notifyMessage) {
        CartParseModule cartParseModule;
        List<ItemComponent> itemComponents;
        JSONObject fieldsTmp;
        this.fields.put("checked", (Object) Boolean.valueOf(checked));
        Component component = getParent();
        if (!(component == null || (cartParseModule = CartEngine.getInstance(this.cartFrom).getParseModule()) == null || (itemComponents = cartParseModule.getItemComponentsByBundleId(component.getComponentId())) == null || itemComponents.size() <= 0)) {
            for (ItemComponent itemComponent : itemComponents) {
                if (!(itemComponent == null || (fieldsTmp = itemComponent.getFields()) == null)) {
                    fieldsTmp.put("checked", (Object) Boolean.valueOf(checked));
                }
            }
        }
        refreshCheckAllComponent();
        refreshAllComponent();
        if (notifyMessage) {
            NotificationCenterImpl.getInstance().postNotification(McartConstants.CART_CHECK_SUCCESS, this);
        }
    }

    public void setChecked(boolean checked) {
        setChecked(checked, true);
    }

    public boolean isHasBonus() {
        return this.fields.getBooleanValue("hasBonus");
    }

    public Coudan getCoudan() {
        if (this.coudan == null) {
            this.coudan = generateCoudan();
        }
        return this.coudan;
    }

    private Coudan generateCoudan() {
        JSONObject coudan2 = this.fields.getJSONObject("coudan");
        if (coudan2 != null) {
            try {
                return new Coudan(coudan2);
            } catch (Throwable th) {
            }
        }
        return null;
    }

    public void updateCoudan(JSONObject coudan2) {
        this.fields.put("coudan", (Object) coudan2);
        this.coudan = generateCoudan();
    }

    public void reload(JSONObject data) {
        super.reload(data);
        this.coudan = generateCoudan();
    }

    public String toString() {
        return super.toString() + " - ShopComponent [seller=" + getSeller() + ",shopId=" + getShopId() + ",sellerId=" + getSellerId() + ",url=" + getUrl() + ",type=" + getType() + ",icon=" + getIcon() + ",title=" + getTitle() + ",hasCoupon=" + isHasBonus() + ",checked=" + isChecked() + ",coudan=" + getCoudan() + "]";
    }
}
