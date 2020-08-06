package com.yunos.tvtaobao.biz.request.bo;

import android.text.TextUtils;
import java.io.Serializable;
import java.util.List;

public class TbTvShoppingItemBo implements Serializable {
    private static final long serialVersionUID = 6850844999639375461L;
    private long endMillisecond;
    private String itemActionUri;
    private long itemId;
    private List<Long> itemIds;
    private String itemImage;
    private long startMillisecond;
    private String type;

    public enum ShopType {
        UNKNOWN,
        SINGLE,
        LIST
    }

    public void setItemId(long id) {
        this.itemId = id;
    }

    public long getItemId() {
        return this.itemId;
    }

    public String getItemActionUri() {
        return this.itemActionUri;
    }

    public void setItemActionUri(String itemActionUri2) {
        this.itemActionUri = itemActionUri2;
    }

    public void setItemImage(String image) {
        this.itemImage = image;
    }

    public String getItemImage() {
        return this.itemImage;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getType() {
        return this.type;
    }

    public ShopType getShopType() {
        ShopType shopType = ShopType.UNKNOWN;
        if (TextUtils.isEmpty(this.type)) {
            return shopType;
        }
        if (this.type.equals("SINGLE")) {
            return ShopType.SINGLE;
        }
        if (this.type.equals("LIST")) {
            return ShopType.LIST;
        }
        return shopType;
    }

    public long getStartMillisecond() {
        return this.startMillisecond;
    }

    public void setStartMillisecond(long startMillisecond2) {
        this.startMillisecond = startMillisecond2;
    }

    public long getEndMillisecond() {
        return this.endMillisecond;
    }

    public void setEndMillisecond(long endMillisecond2) {
        this.endMillisecond = endMillisecond2;
    }

    public void setStartTime(long time) {
        this.startMillisecond = time;
    }

    public long getStartTime() {
        return this.startMillisecond;
    }

    public void setEndTime(long time) {
        this.endMillisecond = time;
    }

    public long getEndTime() {
        return this.endMillisecond;
    }

    public void setItemIds(List<Long> itemIds2) {
        this.itemIds = itemIds2;
    }

    public List<Long> getItemIds() {
        return this.itemIds;
    }

    public boolean isList() {
        if (TextUtils.isEmpty(this.type) || this.type.compareTo("LIST") != 0) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "TbTvShoppingItemBo{itemId=" + this.itemId + ", itemImage='" + this.itemImage + '\'' + ", type='" + this.type + '\'' + ", startMillisecond=" + this.startMillisecond + ", endMillisecond=" + this.endMillisecond + ", itemIds=" + this.itemIds + ", itemActionUri=" + this.itemActionUri + '}';
    }
}
