package com.yunos.tvtaobao.biz.request.bo;

import com.yunos.tv.core.util.PriceFormator;
import com.yunos.tvtaobao.biz.request.bo.PropPath;
import java.io.Serializable;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SkuItem implements Serializable {
    private static final long serialVersionUID = 659841578861572201L;
    private PropPath path;
    private String ppath;
    private Long price;
    private PriceUnits[] priceUnits;
    private Long quantity = 0L;
    private Long skuId;

    public Long getSkuId() {
        return this.skuId;
    }

    public void setSkuId(Long skuId2) {
        this.skuId = skuId2;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity2) {
        this.quantity = quantity2;
    }

    public boolean contains(PropPath.Pvid pvid, Long quantity2) {
        boolean flag = this.path.contains(pvid);
        if (!flag || quantity2 == null) {
            return flag;
        }
        return flag && quantity2.longValue() == this.quantity.longValue();
    }

    public boolean contains(List<PropPath.Pvid> idList, Long quantity2) {
        if (idList != null) {
            boolean flag = this.path.contains(idList);
            if (flag && quantity2 != null) {
                flag = flag && quantity2.longValue() == this.quantity.longValue();
            }
            return flag;
        } else if (quantity2 == null || quantity2.longValue() == this.quantity.longValue()) {
            return true;
        } else {
            return false;
        }
    }

    public static SkuItem resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        SkuItem k = new SkuItem();
        if (!obj.isNull("skuId")) {
            k.setSkuId(Long.valueOf(obj.getLong("skuId")));
        }
        if (!obj.isNull("ppath")) {
            k.setPpath(obj.getString("ppath"));
        }
        if (!obj.isNull("price")) {
            k.setPrice(Long.valueOf(obj.getLong("price")));
        }
        if (!obj.isNull("quantity")) {
            k.setQuantity(Long.valueOf(obj.getLong("quantity")));
        }
        if (obj.isNull("priceUnits")) {
            return k;
        }
        JSONArray array = obj.getJSONArray("priceUnits");
        PriceUnits[] temp = new PriceUnits[array.length()];
        for (int i = 0; i < array.length(); i++) {
            temp[i] = PriceUnits.resolveFromMTOP(array.getJSONObject(i));
        }
        k.setPriceUnits(temp);
        return k;
    }

    public Long getPrice() {
        return this.price;
    }

    public void setPrice(Long price2) {
        this.price = price2;
    }

    public String getPpath() {
        return this.ppath;
    }

    public void setPpath(String ppath2) {
        this.ppath = ppath2;
        this.path = new PropPath(ppath2);
    }

    public PropPath getPath() {
        return this.path;
    }

    public PriceUnits[] getPriceUnits() {
        return this.priceUnits;
    }

    public void setPriceUnits(PriceUnits[] priceUnits2) {
        this.priceUnits = priceUnits2;
    }

    public String getActivityPrice() {
        String activityPrice = PriceFormator.formatNoSymbolLong(this.price);
        if (this.priceUnits == null || this.priceUnits.length <= 0) {
            return activityPrice;
        }
        for (int i = 0; i < this.priceUnits.length; i++) {
            if (this.priceUnits[i].getValid().booleanValue()) {
                return this.priceUnits[i].getPrice();
            }
        }
        return activityPrice;
    }
}
