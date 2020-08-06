package com.yunos.tvtaobao.biz.request.bo;

import com.bftv.fui.constantplugin.Constant;
import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Item implements Serializable {
    private static final long serialVersionUID = 5315141269862627730L;
    private String category;
    private Integer evaluateCount;
    private Integer favcount;
    private String fullDescUrl;
    private Double itemGradeAvg;
    private Long itemNumId;
    private String itemStatus;
    private String itemUrl;
    private String location;
    private String[] picsPath;
    private Long price;
    private Integer quantity;
    private Boolean sku;
    private Boolean soldout;
    private String stuffStatus;
    private Tag tag;
    private String title;
    private Integer totalSoldQuantity;

    public String getFullDescUrl() {
        return this.fullDescUrl;
    }

    public void setFullDescUrl(String fullDescUrl2) {
        this.fullDescUrl = fullDescUrl2;
    }

    public Long getItemNumId() {
        return this.itemNumId;
    }

    public void setItemNumId(Long itemNumId2) {
        this.itemNumId = itemNumId2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public Long getPrice() {
        return this.price;
    }

    public void setPrice(Long price2) {
        this.price = price2;
    }

    public String getStuffStatus() {
        return this.stuffStatus;
    }

    public void setStuffStatus(String stuffStatus2) {
        this.stuffStatus = stuffStatus2;
    }

    public String getItemStatus() {
        return this.itemStatus;
    }

    public void setItemStatus(String itemStatus2) {
        this.itemStatus = itemStatus2;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location2) {
        this.location = location2;
    }

    public Integer getTotalSoldQuantity() {
        return this.totalSoldQuantity;
    }

    public void setTotalSoldQuantity(Integer totalSoldQuantity2) {
        this.totalSoldQuantity = totalSoldQuantity2;
    }

    public Integer getEvaluateCount() {
        return this.evaluateCount;
    }

    public void setEvaluateCount(Integer evaluateCount2) {
        this.evaluateCount = evaluateCount2;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity2) {
        this.quantity = quantity2;
    }

    public Boolean getSoldout() {
        return this.soldout;
    }

    public void setSoldout(Boolean soldout2) {
        this.soldout = soldout2;
    }

    public Integer getFavcount() {
        return this.favcount;
    }

    public void setFavcount(Integer favcount2) {
        this.favcount = favcount2;
    }

    public Double getItemGradeAvg() {
        return this.itemGradeAvg;
    }

    public void setItemGradeAvg(Double itemGradeAvg2) {
        this.itemGradeAvg = itemGradeAvg2;
    }

    public Boolean getSku() {
        return this.sku;
    }

    public void setSku(Boolean sku2) {
        this.sku = sku2;
    }

    public String getItemUrl() {
        return this.itemUrl;
    }

    public void setItemUrl(String itemUrl2) {
        this.itemUrl = itemUrl2;
    }

    public Tag getTag() {
        return this.tag;
    }

    public void setTag(Tag tag2) {
        this.tag = tag2;
    }

    public String[] getPicsPath() {
        return this.picsPath;
    }

    public void setPicsPath(String[] picsPath2) {
        this.picsPath = picsPath2;
    }

    public static Item resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        Item item = new Item();
        if (!obj.isNull("evaluateCount")) {
            item.setEvaluateCount(Integer.valueOf(obj.getInt("evaluateCount")));
        }
        if (!obj.isNull("favcount")) {
            item.setFavcount(Integer.valueOf(obj.getInt("favcount")));
        }
        if (!obj.isNull("itemGradeAvg")) {
            item.setItemGradeAvg(Double.valueOf(obj.getDouble("itemGradeAvg")));
        }
        if (!obj.isNull("itemNumId")) {
            item.setItemNumId(Long.valueOf(obj.getLong("itemNumId")));
        }
        if (!obj.isNull("itemStatus")) {
            item.setItemStatus(obj.getString("itemStatus"));
        }
        if (!obj.isNull("itemUrl")) {
            item.setItemUrl(obj.getString("itemUrl"));
        }
        if (!obj.isNull("location")) {
            item.setLocation(obj.getString("location"));
        }
        if (!obj.isNull("picsPath")) {
            JSONArray array = obj.getJSONArray("picsPath");
            String[] temp = new String[array.length()];
            for (int i = 0; i < array.length(); i++) {
                temp[i] = array.getString(i);
            }
            item.setPicsPath(temp);
        }
        if (!obj.isNull("price")) {
            item.setPrice(Long.valueOf(obj.getLong("price")));
        }
        if (!obj.isNull("quantity")) {
            item.setQuantity(Integer.valueOf(obj.getInt("quantity")));
        }
        if (!obj.isNull("sku")) {
            item.setSku(Boolean.valueOf(obj.getBoolean("sku")));
        }
        if (!obj.isNull("soldout")) {
            item.setSoldout(Boolean.valueOf(obj.getBoolean("soldout")));
        }
        if (!obj.isNull("stuffStatus")) {
            item.setStuffStatus(obj.getString("stuffStatus"));
        }
        if (!obj.isNull("title")) {
            item.setTitle(obj.getString("title"));
        }
        if (!obj.isNull("totalSoldQuantity")) {
            item.setTotalSoldQuantity(Integer.valueOf(obj.getInt("totalSoldQuantity")));
        }
        if (!obj.isNull("tag")) {
            item.setTag(Tag.resolveFromMTOP(obj.getJSONObject("tag")));
        }
        if (!obj.isNull("fullDescUrl")) {
            item.setFullDescUrl(obj.getString("fullDescUrl"));
        }
        if (obj.isNull("category")) {
            return item;
        }
        item.setFullDescUrl(obj.getString("category"));
        return item;
    }

    public String getItemBuyStatus() {
        if (getSoldout().booleanValue()) {
            return "已下架";
        }
        if ("正常".equals(this.itemStatus)) {
            return "立即购买";
        }
        if ("下架".equals(this.itemStatus)) {
            return "已下架";
        }
        if (Constant.DELETE.equals(this.itemStatus)) {
            return "已删除";
        }
        if ("从未上架".equals(this.itemStatus) || "CC".equals(this.itemStatus)) {
            return "即将开始";
        }
        if ("已售完".equals(this.itemStatus)) {
            return "已卖光";
        }
        return "立即购买";
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category2) {
        this.category = category2;
    }
}
