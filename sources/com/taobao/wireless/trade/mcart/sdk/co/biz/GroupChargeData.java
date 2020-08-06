package com.taobao.wireless.trade.mcart.sdk.co.biz;

import com.taobao.wireless.trade.mcart.sdk.utils.StringUtils;
import java.util.HashSet;
import java.util.List;

public class GroupChargeData {
    private boolean fromServer;
    private GroupChargeType groupChargeType = GroupChargeType.BC;
    private List<ItemComponent> mItemComponents;
    private HashSet<ShopComponent> mShopComponents = new HashSet<>();
    private int quantity;
    private long totalPrice;

    public GroupChargeType getGroupChargeType() {
        return this.groupChargeType;
    }

    public void setGroupChargeType(GroupChargeType groupChargeType2) {
        this.groupChargeType = groupChargeType2;
    }

    public String getGroupShopTitle() {
        if (this.mShopComponents.size() != 1) {
            return this.groupChargeType == null ? "" : this.groupChargeType.getTitle();
        }
        ShopComponent shopComponent = this.mShopComponents.iterator().next();
        if (!StringUtils.isBlank(shopComponent.getSubTitle())) {
            return shopComponent.getTitle() + " " + shopComponent.getSubTitle();
        }
        return shopComponent.getTitle();
    }

    public void addShopComponent(ShopComponent shopComponent) {
        if (shopComponent != null) {
            this.mShopComponents.add(shopComponent);
        }
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity2) {
        this.quantity = quantity2;
    }

    public String getTotalPriceTitle() {
        return getCurrencySymbol() + String.format("%1$.2f", new Object[]{Double.valueOf(((double) this.totalPrice) / ((double) getCurrencyUnitFactor()))});
    }

    public String getCurrencySymbol() {
        if (this.mItemComponents == null || this.mItemComponents.isEmpty()) {
            return "￥";
        }
        return this.mItemComponents.get(0).getItemPay().getCurrencySymbol();
    }

    private int getCurrencyUnitFactor() {
        if (this.mItemComponents == null || this.mItemComponents.isEmpty()) {
            return 100;
        }
        return this.mItemComponents.get(0).getItemPay().getCurrencyUnitFactor();
    }

    public boolean isFromServer() {
        return this.fromServer;
    }

    public void setFromServer(boolean fromServer2) {
        this.fromServer = fromServer2;
    }

    public long getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(long totalPrice2) {
        this.totalPrice = totalPrice2;
    }

    public String getCartIds() {
        String cartIds = null;
        StringBuffer cartIdsBuffer = new StringBuffer();
        if (this.mItemComponents != null && !this.mItemComponents.isEmpty()) {
            for (ItemComponent itemComponent : this.mItemComponents) {
                cartIdsBuffer.append(itemComponent.getCartId()).append(",");
            }
            cartIds = cartIdsBuffer.toString();
            int indexlast = cartIds.lastIndexOf(",");
            if (indexlast > 0) {
                return cartIds.substring(0, indexlast);
            }
        }
        return cartIds;
    }

    public List<ItemComponent> getItemComponents() {
        return this.mItemComponents;
    }

    public void setItemComponents(List<ItemComponent> itemComponents) {
        this.mItemComponents = itemComponents;
    }
}
