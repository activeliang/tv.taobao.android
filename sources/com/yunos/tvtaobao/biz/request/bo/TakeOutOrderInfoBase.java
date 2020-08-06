package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TakeOutOrderInfoBase implements Serializable {
    private static final long serialVersionUID = -610484326142980001L;
    private String appointOrderId;
    private String createGMT;
    private String fullProducts;
    private String fullProductsCount;
    private boolean isSellerDelivery;
    private String itemShowPic;
    private String itemShowTitle;
    private String outOrderId;
    private ArrayList<TakeOutOrderProductInfoBase> productInfoBases;
    private String status;
    private String storeId;
    private String storeLogo;
    private String storeName;
    private String tbMainOrderId;
    private int totalFee;

    public ArrayList<TakeOutOrderProductInfoBase> getProductInfoBases() {
        return this.productInfoBases;
    }

    public void setProductInfoBases(ArrayList<TakeOutOrderProductInfoBase> productInfoBases2) {
        this.productInfoBases = productInfoBases2;
    }

    public boolean isSellerDelivery() {
        return this.isSellerDelivery;
    }

    public void setSellerDelivery(boolean sellerDelivery) {
        this.isSellerDelivery = sellerDelivery;
    }

    public String getFullProductsCount() {
        return this.fullProductsCount;
    }

    public void setFullProductsCount(String fullProductsCount2) {
        this.fullProductsCount = fullProductsCount2;
    }

    public String getFullProducts() {
        return this.fullProducts;
    }

    public void setFullProducts(String fullProducts2) {
        this.fullProducts = fullProducts2;
    }

    public String getAppointOrderId() {
        return this.appointOrderId;
    }

    public void setAppointOrderId(String appointOrderId2) {
        this.appointOrderId = appointOrderId2;
    }

    public String getTbMainOrderId() {
        return this.tbMainOrderId;
    }

    public void setTbMainOrderId(String tbMainOrderId2) {
        this.tbMainOrderId = tbMainOrderId2;
    }

    public String getStoreName() {
        return this.storeName;
    }

    public void setStoreName(String storeName2) {
        this.storeName = storeName2;
    }

    public String getStoreLogo() {
        return this.storeLogo;
    }

    public void setStoreLogo(String storeLogo2) {
        this.storeLogo = storeLogo2;
    }

    public String getStoreId() {
        return this.storeId;
    }

    public void setStoreId(String storeId2) {
        this.storeId = storeId2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getOutOrderId() {
        return this.outOrderId;
    }

    public void setOutOrderId(String outOrderId2) {
        this.outOrderId = outOrderId2;
    }

    public String getItemShowTitle() {
        return this.itemShowTitle;
    }

    public void setItemShowTitle(String itemShowTitle2) {
        this.itemShowTitle = itemShowTitle2;
    }

    public String getItemShowPic() {
        return this.itemShowPic;
    }

    public void setItemShowPic(String itemShowPic2) {
        this.itemShowPic = itemShowPic2;
    }

    public String getCreateGMT() {
        return this.createGMT;
    }

    public void setCreateGMT(String createGMT2) {
        this.createGMT = createGMT2;
    }

    public int getTotalFee() {
        return this.totalFee;
    }

    public void setTotalFee(int totalFee2) {
        this.totalFee = totalFee2;
    }

    public static TakeOutOrderInfoBase resolverFromMtop(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        TakeOutOrderInfoBase orderInfoBase = new TakeOutOrderInfoBase();
        if (obj == null) {
            return orderInfoBase;
        }
        orderInfoBase.setTotalFee(obj.optInt("totalFee", 0));
        orderInfoBase.setTbMainOrderId(obj.optString("tbMainOrderId"));
        orderInfoBase.setStoreName(obj.optString("storeName"));
        orderInfoBase.setStoreLogo(obj.optString("storeLogo"));
        orderInfoBase.setStoreId(obj.optString("storeId"));
        orderInfoBase.setStatus(obj.optString("status"));
        orderInfoBase.setOutOrderId(obj.optString("outOrderId"));
        orderInfoBase.setItemShowPic(obj.optString("itemShowPic"));
        orderInfoBase.setItemShowTitle(obj.optString("itemShowTitle"));
        orderInfoBase.setCreateGMT(obj.optString("gmtCreate"));
        orderInfoBase.setAppointOrderId(obj.optString("appointOrderId"));
        if (obj.isNull("orderItems")) {
            return orderInfoBase;
        }
        JSONArray array = obj.getJSONArray("orderItems");
        StringBuilder builder = new StringBuilder();
        ArrayList<TakeOutOrderProductInfoBase> temp = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            TakeOutOrderProductInfoBase product = TakeOutOrderProductInfoBase.resolverFromMtop(array.getJSONObject(i));
            temp.add(product);
            builder.append(product.getProductTitle());
            if (i < array.length() - 1) {
                builder.append(" + ");
            }
        }
        orderInfoBase.setProductInfoBases(temp);
        orderInfoBase.setFullProducts(builder.toString());
        if (array.length() <= 0) {
            return orderInfoBase;
        }
        orderInfoBase.setFullProductsCount(array.length() + "件商品");
        return orderInfoBase;
    }
}
