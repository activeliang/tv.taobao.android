package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BoughtItemObject implements Serializable {
    private static final long serialVersionUID = 2232082809347042485L;
    private ArrayList<String> icon;
    private String itemId;
    private String pic;
    private String price;
    private String quantity;
    private String sPrice;
    private String skuDesc;
    private String title;

    public ArrayList<String> getIcon() {
        return this.icon;
    }

    public String getSkuDesc() {
        return this.skuDesc;
    }

    public void setSkuDesc(String skuDesc2) {
        this.skuDesc = skuDesc2;
    }

    public void setIcon(ArrayList<String> icon2) {
        this.icon = icon2;
    }

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public String getPic() {
        return this.pic;
    }

    public void setPic(String pic2) {
        this.pic = pic2;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price2) {
        this.price = price2;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity2) {
        this.quantity = quantity2;
    }

    public String getsPrice() {
        return this.sPrice;
    }

    public void setsPrice(String sPrice2) {
        this.sPrice = sPrice2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public static BoughtItemObject resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        BoughtItemObject boughtItemObject = new BoughtItemObject();
        boughtItemObject.setItemId(obj.optString("itemId"));
        boughtItemObject.setPic(obj.optString("pic"));
        boughtItemObject.setPrice(obj.optString("price"));
        boughtItemObject.setQuantity(obj.optString("quantity"));
        boughtItemObject.setSkuDesc(obj.optString("skuDesc"));
        boughtItemObject.setsPrice(obj.optString("sPrice"));
        boughtItemObject.setTitle(obj.optString("title"));
        if (!obj.has("icon")) {
            return boughtItemObject;
        }
        JSONArray iconList = obj.getJSONArray("icon");
        ArrayList<String> icons = new ArrayList<>();
        for (int i = 0; i < iconList.length(); i++) {
            icons.add(iconList.getString(i));
        }
        boughtItemObject.setIcon(icons);
        return boughtItemObject;
    }
}
