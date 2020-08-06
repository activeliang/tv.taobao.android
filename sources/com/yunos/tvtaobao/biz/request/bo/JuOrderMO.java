package com.yunos.tvtaobao.biz.request.bo;

import com.yunos.tv.core.util.SystemUtil;
import java.util.Date;
import org.json.JSONException;
import org.json.JSONObject;

public class JuOrderMO extends BaseMO {
    private static final long serialVersionUID = 1532851975519956232L;
    private Integer activityType;
    private String attributes;
    private long bizOrderId;
    private int bizType;
    private int buyNum;
    private Date deliveryDate;
    private long finalPrice;
    private Date gmtCreate;
    private Date gmtModified;
    private long id;
    private long itemId;
    private ItemMO itemMO;
    private Integer logisticsStatus;
    private Integer noticeStatus;
    private Date orderCreateTime;
    private Integer orderStatus;
    private Integer orderType;
    private Date payTime;
    private long postFee;
    private int refundStatus;
    private int requestType;
    private String sku;
    private long skuId;
    private long userId;

    public String toString() {
        return "JuOrderMO [itemMO=" + this.itemMO + ", gmtCreate=" + this.gmtCreate + ", gmtModified=" + this.gmtModified + ", id=" + this.id + ", bizOrderId=" + this.bizOrderId + ", itemId=" + this.itemId + ", userId=" + this.userId + ", orderStatus=" + this.orderStatus + ", skuId=" + this.skuId + ", sku=" + this.sku + ", buyNum=" + this.buyNum + ", bizType=" + this.bizType + ", requestType=" + this.requestType + ", orderType=" + this.orderType + ", attributes=" + this.attributes + ", noticeStatus=" + this.noticeStatus + ", activityType=" + this.activityType + ", postFee=" + this.postFee + ", logisticsStatus=" + this.logisticsStatus + ", deliveryDate=" + this.deliveryDate + ", payTime=" + this.payTime + ", orderCreateTime=" + this.orderCreateTime + ", refundStatus=" + this.refundStatus + ", finalPrice=" + this.finalPrice + "]";
    }

    public long getPostFee() {
        return this.postFee;
    }

    public void setPostFee(long postFee2) {
        this.postFee = postFee2;
    }

    public ItemMO getItemMO() {
        return this.itemMO;
    }

    public void setItemMO(ItemMO itemMO2) {
        this.itemMO = itemMO2;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id2) {
        this.id = id2;
    }

    public long getBizOrderId() {
        return this.bizOrderId;
    }

    public String getSku() {
        return this.sku;
    }

    public void setSku(String sku2) {
        this.sku = sku2;
    }

    public void setBizOrderId(long bizOrderId2) {
        this.bizOrderId = bizOrderId2;
    }

    public long getItemId() {
        return this.itemId;
    }

    public void setItemId(long itemId2) {
        this.itemId = itemId2;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId2) {
        this.userId = userId2;
    }

    public Integer getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(Integer orderStatus2) {
        this.orderStatus = orderStatus2;
    }

    public long getSkuId() {
        return this.skuId;
    }

    public void setSkuId(long skuId2) {
        this.skuId = skuId2;
    }

    public int getBuyNum() {
        return this.buyNum;
    }

    public void setBuyNum(int buyNum2) {
        this.buyNum = buyNum2;
    }

    public int getBizType() {
        return this.bizType;
    }

    public void setBizType(int bizType2) {
        this.bizType = bizType2;
    }

    public int getRequestType() {
        return this.requestType;
    }

    public void setRequestType(int requestType2) {
        this.requestType = requestType2;
    }

    public Integer getOrderType() {
        return this.orderType;
    }

    public void setOrderType(Integer orderType2) {
        this.orderType = orderType2;
    }

    public String getAttributes() {
        return this.attributes;
    }

    public void setAttributes(String attributes2) {
        this.attributes = attributes2;
    }

    public Integer getNoticeStatus() {
        return this.noticeStatus;
    }

    public void setNoticeStatus(Integer noticeStatus2) {
        this.noticeStatus = noticeStatus2;
    }

    public Integer getActivityType() {
        return this.activityType;
    }

    public void setActivityType(Integer activityType2) {
        this.activityType = activityType2;
    }

    public Integer getLogisticsStatus() {
        return this.logisticsStatus;
    }

    public void setLogisticsStatus(Integer logisticsStatus2) {
        this.logisticsStatus = logisticsStatus2;
    }

    public Date getDeliveryDate() {
        return this.deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate2) {
        this.deliveryDate = deliveryDate2;
    }

    public Date getPayTime() {
        return this.payTime;
    }

    public void setPayTime(Date payTime2) {
        this.payTime = payTime2;
    }

    public Date getOrderCreateTime() {
        return this.orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime2) {
        this.orderCreateTime = orderCreateTime2;
    }

    public int getRefundStatus() {
        return this.refundStatus;
    }

    public void setRefundStatus(int refundStatus2) {
        this.refundStatus = refundStatus2;
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

    public long getFinalPrice() {
        return this.finalPrice;
    }

    public void setFinalPrice(long finalPrice2) {
        this.finalPrice = finalPrice2;
    }

    public static JuOrderMO fromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        JuOrderMO juOrder = new JuOrderMO();
        if (obj.has("activityType")) {
            juOrder.setActivityType(Integer.valueOf(obj.getInt("activityType")));
        }
        if (obj.has("attributes")) {
            juOrder.setAttributes(obj.getString("attributes"));
        }
        if (obj.has("bizOrderId")) {
            juOrder.setBizOrderId(obj.getLong("bizOrderId"));
        }
        if (obj.has("bizType")) {
            juOrder.setBizType(obj.getInt("bizType"));
        }
        if (obj.has("buyNum")) {
            juOrder.setBuyNum(obj.getInt("buyNum"));
        }
        if (obj.has("finalPrice")) {
            juOrder.setFinalPrice(obj.getLong("finalPrice"));
        }
        if (obj.has("gmtCreate")) {
            juOrder.setGmtCreate(SystemUtil.convertStringToDate(obj.getString("gmtCreate")));
        }
        if (obj.has("gmtModified")) {
            juOrder.setGmtModified(SystemUtil.convertStringToDate(obj.getString("gmtModified")));
        }
        if (obj.has("id")) {
            juOrder.setId(obj.getLong("id"));
        }
        if (obj.has("itemId")) {
            juOrder.setItemId(obj.getLong("itemId"));
        }
        if (obj.has("logisticsStatus")) {
            juOrder.setLogisticsStatus(Integer.valueOf(obj.getInt("logisticsStatus")));
        }
        if (obj.has("noticeStatus")) {
            juOrder.setNoticeStatus(Integer.valueOf(obj.getInt("noticeStatus")));
        }
        if (obj.has("orderCreateTime")) {
            juOrder.setOrderCreateTime(SystemUtil.convertStringToDate(obj.getString("orderCreateTime")));
        }
        if (obj.has("orderStatus")) {
            juOrder.setOrderStatus(Integer.valueOf(obj.getInt("orderStatus")));
        }
        if (obj.has("orderType")) {
            juOrder.setOrderType(Integer.valueOf(obj.getInt("orderType")));
        }
        if (obj.has("payTime")) {
            juOrder.setPayTime(SystemUtil.convertStringToDate(obj.getString("payTime")));
        }
        if (obj.has("postFee")) {
            juOrder.setPostFee(obj.getLong("postFee"));
        }
        if (obj.has("refundStatus")) {
            juOrder.setRefundStatus(obj.getInt("refundStatus"));
        }
        if (obj.has("requestType")) {
            juOrder.setRequestType(obj.getInt("requestType"));
        }
        if (obj.has("skuId")) {
            juOrder.setSkuId(obj.getLong("skuId"));
        }
        if (obj.has("sku")) {
            juOrder.setSku(obj.getString("sku"));
        }
        if (!obj.has("userId")) {
            return juOrder;
        }
        juOrder.setUserId(obj.getLong("userId"));
        return juOrder;
    }
}
