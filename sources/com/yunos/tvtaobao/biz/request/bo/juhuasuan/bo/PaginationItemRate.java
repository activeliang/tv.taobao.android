package com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PaginationItemRate extends BaseMO {
    private static final long serialVersionUID = 7467851418432984165L;
    private Long feedAllCount;
    private Long feedAppendCount;
    private Long feedBadCount;
    private Long feedGoodCount;
    private Long feedNormalCount;
    private Long feedPicCount;
    private List<ItemRate> rateList;
    private Long total;
    private Integer totalPage;
    private Integer userType;

    public static PaginationItemRate resolveFromMTOP(JSONObject obj) throws JSONException {
        JSONArray array;
        PaginationItemRate item = null;
        try {
            PaginationItemRate item2 = new PaginationItemRate();
            try {
                item2.setTotal(Long.valueOf(obj.getLong("total")));
                item2.setTotalPage(Integer.valueOf(obj.getInt("totalPage")));
                if (obj.has("feedAllCount")) {
                    item2.setFeedAllCount(Long.valueOf(obj.getLong("feedAllCount")));
                }
                if (obj.has("feedGoodCount")) {
                    item2.setFeedGoodCount(Long.valueOf(obj.getLong("feedGoodCount")));
                }
                if (obj.has("feedNormalCount")) {
                    item2.setFeedNormalCount(Long.valueOf(obj.getLong("feedNormalCount")));
                }
                if (obj.has("feedBadCount")) {
                    item2.setFeedBadCount(Long.valueOf(obj.getLong("feedBadCount")));
                }
                if (obj.has("feedAppendCount")) {
                    item2.setFeedAppendCount(Long.valueOf(obj.getLong("feedAppendCount")));
                }
                if (obj.has("feedPicCount")) {
                    item2.setFeedPicCount(Long.valueOf(obj.getLong("feedPicCount")));
                }
                if (obj.has("rateList") && (array = obj.getJSONArray("rateList")) != null && array.length() > 0) {
                    List<ItemRate> list = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        list.add(ItemRate.resolveFromMTOP(array.getJSONObject(i)));
                    }
                    item2.setRateList(list);
                }
                item2.setUserType(Integer.valueOf(obj.getInt("userType")));
                return item2;
            } catch (Exception e) {
                e = e;
                item = item2;
                e.printStackTrace();
                return item;
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            return item;
        }
    }

    public String toString() {
        return "PaginationItemRate [total=" + this.total + ", totalPage=" + this.totalPage + ", feedAllCount=" + this.feedAllCount + ", feedGoodCount=" + this.feedGoodCount + ", feedNormalCount=" + this.feedNormalCount + ", feedBadCount=" + this.feedBadCount + ", feedAppendCount=" + this.feedAppendCount + ", feedPicCount=" + this.feedPicCount + ", rateList=" + this.rateList + ", userType=" + this.userType + "]";
    }

    public Long getTotal() {
        return this.total;
    }

    public void setTotal(Long total2) {
        this.total = total2;
    }

    public Integer getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(Integer totalPage2) {
        this.totalPage = totalPage2;
    }

    public Long getFeedAllCount() {
        return this.feedAllCount;
    }

    public void setFeedAllCount(Long feedAllCount2) {
        this.feedAllCount = feedAllCount2;
    }

    public Long getFeedGoodCount() {
        return this.feedGoodCount;
    }

    public void setFeedGoodCount(Long feedGoodCount2) {
        this.feedGoodCount = feedGoodCount2;
    }

    public Long getFeedNormalCount() {
        return this.feedNormalCount;
    }

    public void setFeedNormalCount(Long feedNormalCount2) {
        this.feedNormalCount = feedNormalCount2;
    }

    public Long getFeedBadCount() {
        return this.feedBadCount;
    }

    public void setFeedBadCount(Long feedBadCount2) {
        this.feedBadCount = feedBadCount2;
    }

    public Long getFeedAppendCount() {
        return this.feedAppendCount;
    }

    public void setFeedAppendCount(Long feedAppendCount2) {
        this.feedAppendCount = feedAppendCount2;
    }

    public Long getFeedPicCount() {
        return this.feedPicCount;
    }

    public void setFeedPicCount(Long feedPicCount2) {
        this.feedPicCount = feedPicCount2;
    }

    public List<ItemRate> getRateList() {
        return this.rateList;
    }

    public void setRateList(List<ItemRate> rateList2) {
        this.rateList = rateList2;
    }

    public Integer getUserType() {
        return this.userType;
    }

    public void setUserType(Integer userType2) {
        this.userType = userType2;
    }
}
