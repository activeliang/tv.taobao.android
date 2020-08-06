package com.yunos.tvtaobao.biz.request.bo;

import com.yunos.tv.core.util.PriceFormator;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TbItemDetail extends BaseMO implements Serializable {
    private static final long serialVersionUID = 3295625841796722989L;
    private Delivery delivery;
    private Guarantee[] guarantees;
    private Item item;
    private JhsItemInfo jhsItemInfo;
    private PriceUnits[] priceUnits;
    private Promotion[] promotions;
    private Prop[] props;
    private Seller seller;
    private Sku sku;
    private Trade trade;

    public static TbItemDetail resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TbItemDetail detail = new TbItemDetail();
        detail.setItem(Item.resolveFromMTOP(obj.getJSONObject("item")));
        if (!obj.isNull("trade")) {
            detail.setTrade(Trade.resolveFromMTOP(obj.getJSONObject("trade")));
        }
        if (!obj.isNull("guarantees")) {
            JSONArray array = obj.getJSONArray("guarantees");
            Guarantee[] temp = new Guarantee[array.length()];
            for (int i = 0; i < array.length(); i++) {
                temp[i] = Guarantee.resolveFromMTOP(array.getJSONObject(i));
            }
            detail.setGuarantees(temp);
        }
        if (detail.getItem() != null && detail.getItem().getSku().booleanValue() && !obj.isNull("sku")) {
            detail.setSku(Sku.resolveFromMTOP(obj.getJSONObject("sku")));
        }
        if (!obj.isNull("delivery")) {
            detail.setDelivery(Delivery.resolveFromMTOP(obj.getJSONObject("delivery")));
        }
        if (!obj.isNull("seller")) {
            detail.setSeller(Seller.resolveFromMTOP(obj.getJSONObject("seller")));
        }
        if (!obj.isNull("props")) {
            JSONArray array2 = obj.getJSONArray("props");
            Prop[] temp2 = new Prop[array2.length()];
            for (int i2 = 0; i2 < array2.length(); i2++) {
                temp2[i2] = Prop.resolveFromMTOP(array2.getJSONObject(i2));
            }
            detail.setProps(temp2);
        }
        if (!obj.isNull("priceUnits")) {
            JSONArray array3 = obj.getJSONArray("priceUnits");
            PriceUnits[] temp3 = new PriceUnits[array3.length()];
            for (int i3 = 0; i3 < array3.length(); i3++) {
                temp3[i3] = PriceUnits.resolveFromMTOP(array3.getJSONObject(i3));
            }
            detail.setPriceUnits(temp3);
        }
        if (obj.isNull("jhsItemInfo")) {
            return detail;
        }
        detail.setJhsItemInfo(JhsItemInfo.resolveFromMTOP(obj.getJSONObject("jhsItemInfo")));
        return detail;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item2) {
        this.item = item2;
    }

    public Seller getSeller() {
        return this.seller;
    }

    public void setSeller(Seller seller2) {
        this.seller = seller2;
    }

    public Guarantee[] getGuarantees() {
        return this.guarantees;
    }

    public void setGuarantees(Guarantee[] guarantees2) {
        this.guarantees = guarantees2;
    }

    public Delivery getDelivery() {
        return this.delivery;
    }

    public void setDelivery(Delivery delivery2) {
        this.delivery = delivery2;
    }

    public Trade getTrade() {
        return this.trade;
    }

    public void setTrade(Trade trade2) {
        this.trade = trade2;
    }

    public Promotion[] getPromotions() {
        return this.promotions;
    }

    public void setPromotions(Promotion[] promotions2) {
        this.promotions = promotions2;
    }

    public Prop[] getProps() {
        return this.props;
    }

    public void setProps(Prop[] props2) {
        this.props = props2;
    }

    public Sku getSku() {
        return this.sku;
    }

    public void setSku(Sku sku2) {
        this.sku = sku2;
    }

    public PriceUnits[] getPriceUnits() {
        return this.priceUnits;
    }

    public void setPriceUnits(PriceUnits[] priceUnits2) {
        this.priceUnits = priceUnits2;
    }

    public JhsItemInfo getJhsItemInfo() {
        return this.jhsItemInfo;
    }

    public void setJhsItemInfo(JhsItemInfo jhsItemInfo2) {
        this.jhsItemInfo = jhsItemInfo2;
    }

    public String getActivityPrice() {
        String activityPrice = PriceFormator.formatNoSymbolLong(this.item.getPrice());
        if (!(this.priceUnits == null || this.priceUnits.length <= 0 || this.priceUnits[0] == null)) {
            activityPrice = this.priceUnits[0].getPrice();
        }
        if (this.jhsItemInfo != null) {
            return PriceFormator.formatNoSymbolLong(this.jhsItemInfo.getActivityPrice());
        }
        return activityPrice;
    }

    public String getPrice() {
        String price = PriceFormator.formatNoSymbolLong(this.item.getPrice());
        if (this.priceUnits == null || this.priceUnits.length <= 1 || this.priceUnits[1] == null) {
            return price;
        }
        return this.priceUnits[1].getPrice();
    }

    public String getActivityTitle() {
        String activityTitle = "一口价";
        if (this.priceUnits != null) {
            int i = 0;
            while (true) {
                if (i >= this.priceUnits.length || this.priceUnits.length <= 0) {
                    break;
                }
                PriceUnits temp = this.priceUnits[i];
                if (temp.getValid().booleanValue()) {
                    activityTitle = temp.getName();
                    break;
                }
                i++;
            }
        }
        if (this.jhsItemInfo != null) {
            return "聚划算价";
        }
        return activityTitle;
    }

    public boolean isActivity() {
        return (this.priceUnits == null && this.jhsItemInfo == null) ? false : true;
    }
}
