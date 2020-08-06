package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaginationItemRates implements Serializable {
    private static final long serialVersionUID = -1347926490161531370L;
    private String feedAllCount;
    private String feedAppendCount;
    private String feedBadCount;
    private String feedGoodCount;
    private String feedNormalCount;
    private String feedPicCount;
    private ItemRates[] itemRates;
    private String total;
    private String totalPage;

    public String getFeedAllCount() {
        return this.feedAllCount;
    }

    public void setFeedAllCount(String feedAllCount2) {
        this.feedAllCount = feedAllCount2;
    }

    public String getFeedPicCount() {
        return this.feedPicCount;
    }

    public void setFeedPicCount(String feedPicCount2) {
        this.feedPicCount = feedPicCount2;
    }

    public String getFeedAppendCount() {
        return this.feedAppendCount;
    }

    public void setFeedAppendCount(String feedAppendCount2) {
        this.feedAppendCount = feedAppendCount2;
    }

    public String getTotal() {
        return this.total;
    }

    public void setTotal(String total2) {
        this.total = total2;
    }

    public String getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(String totalPage2) {
        this.totalPage = totalPage2;
    }

    public String getFeedGoodCount() {
        return this.feedGoodCount;
    }

    public void setFeedGoodCount(String feedGoodCount2) {
        this.feedGoodCount = feedGoodCount2;
    }

    public String getFeedNormalCount() {
        return this.feedNormalCount;
    }

    public void setFeedNormalCount(String feedNormalCount2) {
        this.feedNormalCount = feedNormalCount2;
    }

    public String getFeedBadCount() {
        return this.feedBadCount;
    }

    public void setFeedBadCount(String feedBadCount2) {
        this.feedBadCount = feedBadCount2;
    }

    public ItemRates[] getItemRates() {
        return this.itemRates;
    }

    public void setItemRates(ItemRates[] itemRates2) {
        this.itemRates = itemRates2;
    }

    public static PaginationItemRates resolveFromMTOP(JSONObject obj) throws JSONException {
        if (obj == null) {
            return null;
        }
        PaginationItemRates paginationItemRates = new PaginationItemRates();
        if (!obj.isNull("total")) {
            paginationItemRates.setTotal(obj.getString("total"));
        }
        if (!obj.isNull("totalPage")) {
            paginationItemRates.setTotalPage(obj.getString("totalPage"));
        }
        if (!obj.isNull("feedGoodCount")) {
            paginationItemRates.setFeedGoodCount(obj.getString("feedGoodCount"));
        }
        if (!obj.isNull("feedNormalCount")) {
            paginationItemRates.setFeedNormalCount(obj.getString("feedNormalCount"));
        }
        if (!obj.isNull("feedBadCount")) {
            paginationItemRates.setFeedBadCount(obj.getString("feedBadCount"));
        }
        if (!obj.isNull("feedAllCount")) {
            paginationItemRates.setFeedAllCount(obj.getString("feedAllCount"));
        }
        if (!obj.isNull("feedPicCount")) {
            paginationItemRates.setFeedPicCount(obj.getString("feedPicCount"));
        }
        if (!obj.isNull("feedAppendCount")) {
            paginationItemRates.setFeedAppendCount(obj.getString("feedAppendCount"));
        }
        if (obj.isNull("rateList")) {
            return paginationItemRates;
        }
        JSONArray array = obj.getJSONArray("rateList");
        ItemRates[] temp = new ItemRates[array.length()];
        for (int i = 0; i < array.length(); i++) {
            temp[i] = ItemRates.resolveFromMTOP(array.getJSONObject(i));
        }
        paginationItemRates.setItemRates(temp);
        return paginationItemRates;
    }
}
