package com.yunos.tvtaobao.biz.request.bo.juhuasuan.bo;

import com.yunos.tvtaobao.biz.request.core.JsonResolver;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ItemRate extends BaseMO {
    private static final long serialVersionUID = -8499072479420537523L;
    private Integer annoy;
    private ItemRateAppend appendedFeed;
    private Long auctionNumId;
    private String auctionTitle;
    private List<String> feedPicPathList;
    private String feedback;
    private String feedbackDate;
    private String headPicUrl;
    private Long id;
    private Integer rateType;
    private String reply;
    private Map<String, String> skuMap;
    private Long userId;
    private String userNick;
    private Integer userStar;

    public static ItemRate resolveFromMTOP(JSONObject obj) throws JSONException {
        ItemRate item = null;
        try {
            ItemRate item2 = new ItemRate();
            try {
                item2.setId(Long.valueOf(obj.getLong("id")));
                item2.setAuctionNumId(Long.valueOf(obj.getLong("auctionNumId")));
                if (obj.has("auctionTitle")) {
                    item2.setAuctionTitle(obj.getString("auctionTitle"));
                }
                if (obj.has("userId")) {
                    item2.setUserId(Long.valueOf(obj.getLong("userId")));
                }
                item2.setUserNick(obj.getString("userNick"));
                item2.setUserStar(Integer.valueOf(obj.getInt("userStar")));
                if (obj.has("headPicUrl")) {
                    item2.setHeadPicUrl(obj.getString("headPicUrl"));
                }
                if (obj.has("annoy")) {
                    try {
                        item2.setAnnoy(Integer.valueOf(obj.getInt("annoy")));
                    } catch (JSONException e) {
                        if (Boolean.valueOf(obj.getBoolean("annoy")).booleanValue()) {
                            item2.setAnnoy(1);
                        } else {
                            item2.setAnnoy(0);
                        }
                    }
                }
                item2.setRateType(Integer.valueOf(obj.getInt("rateType")));
                item2.setFeedback(obj.getString("feedback"));
                item2.setFeedbackDate(obj.getString("feedbackDate"));
                if (obj.has("reply")) {
                    item2.setReply(obj.getString("reply"));
                }
                if (obj.has("skuMap")) {
                    item2.setSkuMap(JsonResolver.jsonobjToMap(obj.getJSONObject("skuMap")));
                }
                if (obj.has("appendedFeed")) {
                    item2.setAppendedFeed(ItemRateAppend.resolveFromMTOP(obj.getJSONObject("appendedFeed")));
                }
                if (obj.has("feedPicPathList")) {
                    item2.setFeedPicPathList(JsonResolver.resolveStringArray(obj.getJSONArray("feedPicPathList")));
                }
                return item2;
            } catch (Exception e2) {
                e = e2;
                item = item2;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return item;
        }
    }

    public static class ItemRateAppend extends BaseMO {
        private static final long serialVersionUID = -4211318135284047036L;
        private List<String> appendFeedPicPathList;
        private String appendedFeedback;
        private Integer intervalDay;

        public static ItemRateAppend resolveFromMTOP(JSONObject obj) throws JSONException {
            ItemRateAppend item = null;
            try {
                ItemRateAppend item2 = new ItemRateAppend();
                try {
                    item2.setAppendedFeedback(obj.getString("appendedFeedback"));
                    item2.setIntervalDay(Integer.valueOf(obj.getInt("intervalDay")));
                    if (obj.has("appendFeedPicPathList")) {
                        item2.setAppendFeedPicPathList(JsonResolver.resolveStringArray(obj.getJSONArray("appendFeedPicPathList")));
                    }
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
            return "ItemRateAppend [appendedFeedback=" + this.appendedFeedback + ", intervalDay=" + this.intervalDay + ", appendFeedPicPathList=" + this.appendFeedPicPathList + "]";
        }

        public String getAppendedFeedback() {
            return this.appendedFeedback;
        }

        public void setAppendedFeedback(String appendedFeedback2) {
            this.appendedFeedback = appendedFeedback2;
        }

        public Integer getIntervalDay() {
            return this.intervalDay;
        }

        public void setIntervalDay(Integer intervalDay2) {
            this.intervalDay = intervalDay2;
        }

        public List<String> getAppendFeedPicPathList() {
            return this.appendFeedPicPathList;
        }

        public void setAppendFeedPicPathList(List<String> appendFeedPicPathList2) {
            this.appendFeedPicPathList = appendFeedPicPathList2;
        }
    }

    public String toString() {
        return "ItemRate [id=" + this.id + ", auctionNumId=" + this.auctionNumId + ", auctionTitle=" + this.auctionTitle + ", userId=" + this.userId + ", userNick=" + this.userNick + ", userStar=" + this.userStar + ", headPicUrl=" + this.headPicUrl + ", annoy=" + this.annoy + ", rateType=" + this.rateType + ", feedback=" + this.feedback + ", feedbackDate=" + this.feedbackDate + ", reply=" + this.reply + ", skuMap=" + this.skuMap + ", appendedFeed=" + this.appendedFeed + ", feedPicPathList=" + this.feedPicPathList + "]";
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id2) {
        this.id = id2;
    }

    public Long getAuctionNumId() {
        return this.auctionNumId;
    }

    public void setAuctionNumId(Long auctionNumId2) {
        this.auctionNumId = auctionNumId2;
    }

    public String getAuctionTitle() {
        return this.auctionTitle;
    }

    public void setAuctionTitle(String auctionTitle2) {
        this.auctionTitle = auctionTitle2;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId2) {
        this.userId = userId2;
    }

    public String getUserNick() {
        return this.userNick;
    }

    public void setUserNick(String userNick2) {
        this.userNick = userNick2;
    }

    public Integer getUserStar() {
        return this.userStar;
    }

    public void setUserStar(Integer userStar2) {
        this.userStar = userStar2;
    }

    public String getHeadPicUrl() {
        return this.headPicUrl;
    }

    public void setHeadPicUrl(String headPicUrl2) {
        this.headPicUrl = headPicUrl2;
    }

    public Integer getAnnoy() {
        return this.annoy;
    }

    public void setAnnoy(Integer annoy2) {
        this.annoy = annoy2;
    }

    public Integer getRateType() {
        return this.rateType;
    }

    public void setRateType(Integer rateType2) {
        this.rateType = rateType2;
    }

    public String getFeedback() {
        return this.feedback;
    }

    public void setFeedback(String feedback2) {
        this.feedback = feedback2;
    }

    public String getFeedbackDate() {
        return this.feedbackDate;
    }

    public void setFeedbackDate(String feedbackDate2) {
        this.feedbackDate = feedbackDate2;
    }

    public String getReply() {
        return this.reply;
    }

    public void setReply(String reply2) {
        this.reply = reply2;
    }

    public Map<String, String> getSkuMap() {
        return this.skuMap;
    }

    public void setSkuMap(Map<String, String> skuMap2) {
        this.skuMap = skuMap2;
    }

    public ItemRateAppend getAppendedFeed() {
        return this.appendedFeed;
    }

    public void setAppendedFeed(ItemRateAppend appendedFeed2) {
        this.appendedFeed = appendedFeed2;
    }

    public List<String> getFeedPicPathList() {
        return this.feedPicPathList;
    }

    public void setFeedPicPathList(List<String> feedPicPathList2) {
        this.feedPicPathList = feedPicPathList2;
    }
}
