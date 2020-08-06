package com.yunos.tvtaobao.biz.request.bo;

import com.taobao.detail.domain.tuwen.TuwenConstants;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemMO implements Serializable {
    private static final long serialVersionUID = -7971671584850072946L;
    private Long activityPrice;
    private String attributes;
    private Long categoryId;
    private String checkComment;
    private Long childCategory;
    private String city;
    private Double discount;
    private Date gmtCreate;
    private Date gmtModified;
    private Long groupId;
    private String imageLabel;
    private Integer isLock;
    private Integer itemCount;
    private String itemDesc;
    private String itemGuarantee;
    private Long itemId;
    private Integer itemStatus;
    private String itemUrl;
    private String key;
    private int limitNum;
    private String longName;
    private Long onlineEndTime;
    private Long onlineStartTime;
    private Long originalPrice;
    private Long parentCategory;
    private Integer payPostage;
    private String picUrl;
    private byte[] picWideBytes;
    private String picWideUrl;
    private Long platformId;
    private Boolean preOnline;
    private String pushName;
    private Integer sellerCredit;
    private Long sellerId;
    private String sellerNick;
    private Integer shopType;
    private String shortName;
    private int soldCount;

    private enum ItemMoStateEnum {
        WAIT_FOR_START,
        AVAIL_BUY,
        EXIST_HOLDER,
        NO_STOCK,
        OUT_OF_TIME
    }

    public String toString() {
        return "ItemMO [itemId=" + this.itemId + ", longName=" + this.longName + ", shortName=" + this.shortName + ", itemUrl=" + this.itemUrl + ", itemCount=" + this.itemCount + ", parentCategory=" + this.parentCategory + ", childCategory=" + this.childCategory + ", shopType=" + this.shopType + ", payPostage=" + this.payPostage + ", originalPrice=" + this.originalPrice + ", picUrl=" + this.picUrl + ", picWideUrl=" + this.picWideUrl + ", picWideBytes=" + Arrays.toString(this.picWideBytes) + ", activityPrice=" + this.activityPrice + ", city=" + this.city + ", itemDesc=" + this.itemDesc + ", itemStatus=" + this.itemStatus + ", itemGuarantee=" + this.itemGuarantee + ", discount=" + this.discount + ", checkComment=" + this.checkComment + ", platformId=" + this.platformId + ", groupId=" + this.groupId + ", sellerId=" + this.sellerId + ", sellerNick=" + this.sellerNick + ", sellerCredit=" + this.sellerCredit + ", categoryId=" + this.categoryId + ", soldCount=" + this.soldCount + ", gmtCreate=" + this.gmtCreate + ", gmtModified=" + this.gmtModified + ", limitNum=" + this.limitNum + ", isLock=" + this.isLock + ", onlineStartTime=" + this.onlineStartTime + ", onlineEndTime=" + this.onlineEndTime + ", key=" + this.key + ", imageLabel=" + this.imageLabel + ", preOnline=" + this.preOnline + ", pushName=" + this.pushName + ", attributes=" + this.attributes + "]";
    }

    public Long getItemId() {
        return this.itemId;
    }

    public void setItemId(Long itemId2) {
        this.itemId = itemId2;
    }

    public String getLongName() {
        return this.longName;
    }

    public void setLongName(String longName2) {
        this.longName = longName2;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName2) {
        this.shortName = shortName2;
    }

    public String getItemUrl() {
        return this.itemUrl;
    }

    public void setItemUrl(String itemUrl2) {
        this.itemUrl = itemUrl2;
    }

    public Integer getItemCount() {
        return this.itemCount;
    }

    public void setItemCount(Integer itemCount2) {
        this.itemCount = itemCount2;
    }

    public Long getParentCategory() {
        return this.parentCategory;
    }

    public void setParentCategory(Long parentCategory2) {
        this.parentCategory = parentCategory2;
    }

    public Long getChildCategory() {
        return this.childCategory;
    }

    public void setChildCategory(Long childCategory2) {
        this.childCategory = childCategory2;
    }

    public Integer getShopType() {
        return this.shopType;
    }

    public void setShopType(Integer shopType2) {
        this.shopType = shopType2;
    }

    public Integer getPayPostage() {
        return this.payPostage;
    }

    public void setPayPostage(Integer payPostage2) {
        this.payPostage = payPostage2;
    }

    public Long getOriginalPrice() {
        return this.originalPrice;
    }

    public void setOriginalPrice(Long originalPrice2) {
        this.originalPrice = originalPrice2;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String picUrl2) {
        this.picUrl = picUrl2;
    }

    public String getPicWideUrl() {
        return this.picWideUrl;
    }

    public void setPicWideUrl(String picWideUrl2) {
        this.picWideUrl = picWideUrl2;
    }

    public Long getActivityPrice() {
        return this.activityPrice;
    }

    public void setActivityPrice(Long activityPrice2) {
        this.activityPrice = activityPrice2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city2) {
        this.city = city2;
    }

    public String getItemDesc() {
        return this.itemDesc;
    }

    public void setItemDesc(String itemDesc2) {
        this.itemDesc = itemDesc2;
    }

    public Integer getItemStatus() {
        return this.itemStatus;
    }

    public void setItemStatus(Integer itemStatus2) {
        this.itemStatus = itemStatus2;
    }

    public String getItemGuarantee() {
        return this.itemGuarantee;
    }

    public void setItemGuarantee(String itemGuarantee2) {
        this.itemGuarantee = itemGuarantee2;
    }

    public Double getDiscount() {
        return this.discount;
    }

    public void setDiscount(Double discount2) {
        this.discount = discount2;
    }

    public String getCheckComment() {
        return this.checkComment;
    }

    public void setCheckComment(String checkComment2) {
        this.checkComment = checkComment2;
    }

    public Long getPlatformId() {
        return this.platformId;
    }

    public void setPlatformId(Long platformId2) {
        this.platformId = platformId2;
    }

    public Long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(Long groupId2) {
        this.groupId = groupId2;
    }

    public Long getSellerId() {
        return this.sellerId;
    }

    public void setSellerId(Long sellerId2) {
        this.sellerId = sellerId2;
    }

    public String getSellerNick() {
        return this.sellerNick;
    }

    public void setSellerNick(String sellerNick2) {
        this.sellerNick = sellerNick2;
    }

    public Integer getSellerCredit() {
        return this.sellerCredit;
    }

    public void setSellerCredit(Integer sellerCredit2) {
        this.sellerCredit = sellerCredit2;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(Long categoryId2) {
        this.categoryId = categoryId2;
    }

    public int getSoldCount() {
        return this.soldCount;
    }

    public void setSoldCount(int soldCount2) {
        this.soldCount = soldCount2;
    }

    public Date getGmtCreate() {
        return this.gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate2) {
        this.gmtCreate = gmtCreate2;
    }

    public Date getGmtModified() {
        return this.gmtModified;
    }

    public void setGmtModified(Date gmtModified2) {
        this.gmtModified = gmtModified2;
    }

    public int getLimitNum() {
        return this.limitNum;
    }

    public void setLimitNum(int limitNum2) {
        this.limitNum = limitNum2;
    }

    public Integer getIsLock() {
        return this.isLock;
    }

    public void setIsLock(Integer isLock2) {
        this.isLock = isLock2;
    }

    public Long getOnlineStartTime() {
        return this.onlineStartTime;
    }

    public void setOnlineStartTime(Long onlineStartTime2) {
        this.onlineStartTime = onlineStartTime2;
    }

    public Long getOnlineEndTime() {
        return this.onlineEndTime;
    }

    public void setOnlineEndTime(Long onlineEndTime2) {
        this.onlineEndTime = onlineEndTime2;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key2) {
        this.key = key2;
    }

    public String getAttributes() {
        return this.attributes;
    }

    public void setAttributes(String attributes2) {
        this.attributes = attributes2;
    }

    public String getImageLabel() {
        return this.imageLabel;
    }

    public void setImageLabel(String imageLabel2) {
        this.imageLabel = imageLabel2;
    }

    public Boolean getPreOnline() {
        return this.preOnline;
    }

    public void setPreOnline(Boolean preOnline2) {
        this.preOnline = preOnline2;
    }

    public String getPushName() {
        return this.pushName;
    }

    public void setPushName(String pushName2) {
        this.pushName = pushName2;
    }

    public boolean isNotStart() {
        return this.itemStatus.intValue() == ItemMoStateEnum.WAIT_FOR_START.ordinal();
    }

    public boolean isOver() {
        return this.itemStatus.intValue() == ItemMoStateEnum.OUT_OF_TIME.ordinal();
    }

    public boolean isNoStock() {
        return this.itemStatus.intValue() == ItemMoStateEnum.NO_STOCK.ordinal() || this.itemStatus.intValue() == ItemMoStateEnum.EXIST_HOLDER.ordinal();
    }

    public boolean isAbleBuy() {
        return this.itemStatus.intValue() == ItemMoStateEnum.AVAIL_BUY.ordinal();
    }

    public static ItemMO fromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        try {
            ItemMO item = new ItemMO();
            try {
                item.setActivityPrice(Long.valueOf(obj.optLong("activityPrice")));
                if (obj.has("categoryId")) {
                    item.setCategoryId(Long.valueOf(obj.optLong("categoryId")));
                }
                if (obj.has("childCategory")) {
                    item.setChildCategory(Long.valueOf(obj.optLong("childCategory")));
                }
                if (obj.has("city")) {
                    item.setCity(obj.optString("city"));
                }
                item.setDiscount(Double.valueOf(obj.optDouble("discount")));
                item.setGroupId(Long.valueOf(obj.optLong("groupId")));
                item.setIsLock(Integer.valueOf(obj.optInt("isLock")));
                item.setItemCount(Integer.valueOf(obj.optInt("itemCount")));
                item.setItemGuarantee(obj.optString("itemGuarantee"));
                item.setItemId(Long.valueOf(obj.optLong("itemId")));
                item.setItemStatus(Integer.valueOf(obj.optInt("itemStatus", -1)));
                item.setLimitNum(obj.optInt("limitNum"));
                item.setLongName(obj.optString("longName"));
                item.setOnlineEndTime(Long.valueOf(obj.optLong("onlineEndTime")));
                item.setOnlineStartTime(Long.valueOf(obj.optLong("onlineStartTime")));
                item.setOriginalPrice(Long.valueOf(obj.optLong("originalPrice")));
                item.setParentCategory(Long.valueOf(obj.optLong("parentCategory")));
                item.setPayPostage(Integer.valueOf(obj.optInt("payPostage")));
                item.setPicUrl(obj.optString(TuwenConstants.PARAMS.PIC_URL));
                item.setPicWideUrl(obj.optString("picWideUrl"));
                item.setPlatformId(Long.valueOf(obj.optLong("platformId")));
                item.setPreOnline(Boolean.valueOf(obj.optBoolean("preOnline")));
                item.setSellerId(Long.valueOf(obj.optLong("sellerId")));
                item.setSellerNick(obj.optString("sellerNick"));
                item.setShopType(Integer.valueOf(obj.optInt("shopType")));
                item.setShortName(obj.optString("shortName"));
                item.setSoldCount(obj.optInt("soldCount"));
                if (obj.has("sellerCredit")) {
                    item.setSellerCredit(Integer.valueOf(obj.getInt("sellerCredit")));
                }
                if (obj.has("attributes")) {
                    item.setAttributes(obj.getString("attributes"));
                }
                if (obj.has("checkComment")) {
                    item.setCheckComment(obj.getString("checkComment"));
                }
                if (obj.has("imageLabel")) {
                    item.setImageLabel(obj.getString("imageLabel"));
                }
                if (obj.has("itemDesc")) {
                    item.setItemDesc(obj.getString("itemDesc"));
                }
                if (obj.has("itemUrl")) {
                    item.setItemUrl(obj.getString("itemUrl"));
                }
                if (obj.has("key")) {
                    item.setKey(obj.getString("key"));
                }
                if (obj.has("pushName")) {
                    item.setPushName(obj.getString("pushName"));
                }
                return checkJuItem(item);
            } catch (Exception e) {
                e = e;
                ItemMO itemMO = item;
                e.printStackTrace();
                return null;
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            return null;
        }
    }

    public static ItemMO fromTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        try {
            ItemMO item = new ItemMO();
            try {
                item.setItemId(Long.valueOf(obj.optLong("item_id")));
                item.setPicUrl(obj.optString("pic_url"));
                item.setLongName(obj.optString("long_name"));
                item.setShortName(obj.optString("short_name"));
                item.setItemUrl(obj.optString("item_url"));
                item.setItemCount(Integer.valueOf(obj.optInt("item_count")));
                item.setParentCategory(Long.valueOf(obj.optLong("parent_category")));
                item.setChildCategory(Long.valueOf(obj.optLong("child_category")));
                item.setShopType(Integer.valueOf(obj.optInt("shop_type")));
                item.setPayPostage(Integer.valueOf(obj.optInt("pay_postage")));
                item.setOriginalPrice(Long.valueOf(obj.optLong("original_price")));
                item.setPicWideUrl(obj.optString("pic_wide_url"));
                item.setActivityPrice(Long.valueOf(obj.optLong("activity_price")));
                item.setCity(obj.optString("city"));
                item.setItemDesc(obj.optString("item_desc"));
                item.setItemStatus(Integer.valueOf(obj.optInt("item_status")));
                item.setItemGuarantee(obj.optString("item_guarantee"));
                item.setDiscount(Double.valueOf(obj.optDouble("discount")));
                item.setCheckComment(obj.optString("check_comment"));
                item.setPlatformId(Long.valueOf(obj.optLong("platform_id")));
                item.setGroupId(Long.valueOf(obj.optLong("group_id")));
                item.setSellerId(Long.valueOf(obj.optLong("seller_id")));
                item.setSellerNick(obj.optString("seller_nick"));
                item.setSellerCredit(Integer.valueOf(obj.optInt("seller_credit")));
                item.setCategoryId(Long.valueOf(obj.optLong("category_id")));
                item.setSoldCount(obj.optInt("sold_count"));
                item.setLimitNum(obj.optInt("limit_num"));
                item.setOnlineStartTime(Long.valueOf(obj.optLong("online_start_time")));
                item.setOnlineEndTime(Long.valueOf(obj.optLong("online_end_time")));
                item.setKey(obj.optString("key"));
                item.setImageLabel(obj.optString("image_label"));
                item.setPreOnline(Boolean.valueOf(obj.optBoolean("pre_online")));
                item.setPushName(obj.optString("push_name"));
                return item;
            } catch (Exception e) {
                e = e;
                ItemMO itemMO = item;
                e.printStackTrace();
                return null;
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            return null;
        }
    }

    private static ItemMO checkJuItem(ItemMO item) {
        if (item == null) {
            return null;
        }
        if (item.getItemStatus().intValue() <= 4 && item.getItemStatus().intValue() >= 0 && (item.getItemStatus().intValue() != 1 || item.getOnlineEndTime().longValue() > item.getOnlineStartTime().longValue())) {
            return item;
        }
        item.setItemStatus(4);
        return item;
    }

    public byte[] getPicWideBytes() {
        return this.picWideBytes;
    }

    public void setPicWideBytes(byte[] picWideBytes2) {
        this.picWideBytes = picWideBytes2;
    }

    public void updateItemData(ItemMO data) {
        if (data != null) {
            setActivityPrice(data.getActivityPrice());
            setCategoryId(data.getCategoryId());
            setChildCategory(data.getChildCategory());
            setCity(data.getCity());
            setDiscount(data.getDiscount());
            setGroupId(data.getGroupId());
            setIsLock(data.getIsLock());
            setItemCount(data.getItemCount());
            setItemGuarantee(data.getItemGuarantee());
            setItemStatus(data.getItemStatus());
            setLimitNum(data.getLimitNum());
            setLongName(data.getLongName());
            setOnlineEndTime(data.getOnlineEndTime());
            setOnlineStartTime(data.getOnlineStartTime());
            setOriginalPrice(data.getOriginalPrice());
            setParentCategory(data.getParentCategory());
            setPayPostage(data.getPayPostage());
            setPicUrl(data.getPicUrl());
            setPicWideUrl(data.getPicWideUrl());
            setPlatformId(data.getPlatformId());
            setPreOnline(data.getPreOnline());
            setSellerId(data.getSellerId());
            setSellerNick(data.getSellerNick());
            setShopType(data.getShopType());
            setShortName(data.getShortName());
            setSoldCount(data.getSoldCount());
            setSellerCredit(data.getSellerCredit());
            setAttributes(data.getAttributes());
            setCheckComment(data.getCheckComment());
            setImageLabel(data.getImageLabel());
            setItemDesc(data.getItemDesc());
            setItemUrl(data.getItemUrl());
            setKey(data.getKey());
            setPushName(data.getPushName());
        }
    }
}
