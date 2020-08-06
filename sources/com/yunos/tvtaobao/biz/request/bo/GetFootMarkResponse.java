package com.yunos.tvtaobao.biz.request.bo;

import java.util.ArrayList;
import java.util.List;

public class GetFootMarkResponse {
    private String hasMore;
    private String lastEndTime;
    private Reminder reminder;
    private List<Result> result;
    private String resultCode;

    public void setHasMore(String hasMore2) {
        this.hasMore = hasMore2;
    }

    public String getHasMore() {
        return this.hasMore;
    }

    public void setLastEndTime(String lastEndTime2) {
        this.lastEndTime = lastEndTime2;
    }

    public String getLastEndTime() {
        return this.lastEndTime;
    }

    public void setReminder(Reminder reminder2) {
        this.reminder = reminder2;
    }

    public Reminder getReminder() {
        return this.reminder;
    }

    public void setResult(List<Result> result2) {
        this.result = result2;
    }

    public List<Result> getResult() {
        return this.result;
    }

    public void setResultCode(String resultCode2) {
        this.resultCode = resultCode2;
    }

    public String getResultCode() {
        return this.resultCode;
    }

    public void merge(GetFootMarkResponse response) {
        this.hasMore = response.hasMore;
        this.lastEndTime = response.lastEndTime;
        this.reminder = response.reminder;
        this.resultCode = response.resultCode;
        if (this.result == null || this.result.isEmpty()) {
            this.result = response.result;
            return;
        }
        List<Result> mergedList = new ArrayList<>();
        mergedList.addAll(this.result);
        Result lastItem = this.result.get(this.result.size() - 1);
        if (response.result != null && !response.result.isEmpty()) {
            for (int j = 0; j < response.result.size(); j++) {
                Result newItem = response.result.get(j);
                if (lastItem.date.equals(newItem.date)) {
                    lastItem.dataList.addAll(newItem.dataList);
                } else {
                    mergedList.add(newItem);
                }
            }
        }
        this.result = mergedList;
    }

    public static class Reminder {
        private String closeWaitTime;
        private String icon;
        private String id;
        private String name;
        private String text;

        public void setCloseWaitTime(String closeWaitTime2) {
            this.closeWaitTime = closeWaitTime2;
        }

        public String getCloseWaitTime() {
            return this.closeWaitTime;
        }

        public void setIcon(String icon2) {
            this.icon = icon2;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setId(String id2) {
            this.id = id2;
        }

        public String getId() {
            return this.id;
        }

        public void setName(String name2) {
            this.name = name2;
        }

        public String getName() {
            return this.name;
        }

        public void setText(String text2) {
            this.text = text2;
        }

        public String getText() {
            return this.text;
        }
    }

    public static class DataItem {
        private String catId;
        private String contentAccountId;
        private String contentItemCount;
        private String contentPicHeight;
        private String contentPicWidth;
        private String day;
        private String icons;
        private String id;
        private String pic;
        private int position;
        private String preTitleIconHeight;
        private String preTitleIconWidth;
        private String price;
        private String promotionIconHeight;
        private String promotionIconWidth;
        private String quantity;
        private String reason;
        private String reasonStyle;
        private String sxm;
        private String time;
        private String timeStamp;
        private String title;
        private String type;
        private String umpPrice;
        private String uniqueId;
        private String url;
        private String userId;

        public int getPosition() {
            return this.position;
        }

        public void setPosition(int position2) {
            this.position = position2;
        }

        public String getReason() {
            return this.reason;
        }

        public void setReason(String reason2) {
            this.reason = reason2;
        }

        public String getReasonStyle() {
            return this.reasonStyle;
        }

        public void setReasonStyle(String reasonStyle2) {
            this.reasonStyle = reasonStyle2;
        }

        public String getCatId() {
            return this.catId;
        }

        public void setCatId(String catId2) {
            this.catId = catId2;
        }

        public String getContentAccountId() {
            return this.contentAccountId;
        }

        public void setContentAccountId(String contentAccountId2) {
            this.contentAccountId = contentAccountId2;
        }

        public String getContentItemCount() {
            return this.contentItemCount;
        }

        public void setContentItemCount(String contentItemCount2) {
            this.contentItemCount = contentItemCount2;
        }

        public String getContentPicHeight() {
            return this.contentPicHeight;
        }

        public void setContentPicHeight(String contentPicHeight2) {
            this.contentPicHeight = contentPicHeight2;
        }

        public String getContentPicWidth() {
            return this.contentPicWidth;
        }

        public void setContentPicWidth(String contentPicWidth2) {
            this.contentPicWidth = contentPicWidth2;
        }

        public String getDay() {
            return this.day;
        }

        public void setDay(String day2) {
            this.day = day2;
        }

        public String getIcons() {
            return this.icons;
        }

        public void setIcons(String icons2) {
            this.icons = icons2;
        }

        public String getId() {
            return this.id;
        }

        public void setId(String id2) {
            this.id = id2;
        }

        public String getPic() {
            return this.pic;
        }

        public void setPic(String pic2) {
            this.pic = pic2;
        }

        public String getPreTitleIconHeight() {
            return this.preTitleIconHeight;
        }

        public void setPreTitleIconHeight(String preTitleIconHeight2) {
            this.preTitleIconHeight = preTitleIconHeight2;
        }

        public String getPreTitleIconWidth() {
            return this.preTitleIconWidth;
        }

        public void setPreTitleIconWidth(String preTitleIconWidth2) {
            this.preTitleIconWidth = preTitleIconWidth2;
        }

        public String getPrice() {
            return this.price;
        }

        public void setPrice(String price2) {
            this.price = price2;
        }

        public String getPromotionIconHeight() {
            return this.promotionIconHeight;
        }

        public void setPromotionIconHeight(String promotionIconHeight2) {
            this.promotionIconHeight = promotionIconHeight2;
        }

        public String getPromotionIconWidth() {
            return this.promotionIconWidth;
        }

        public void setPromotionIconWidth(String promotionIconWidth2) {
            this.promotionIconWidth = promotionIconWidth2;
        }

        public String getQuantity() {
            return this.quantity;
        }

        public void setQuantity(String quantity2) {
            this.quantity = quantity2;
        }

        public String getSxm() {
            return this.sxm;
        }

        public void setSxm(String sxm2) {
            this.sxm = sxm2;
        }

        public String getTime() {
            return this.time;
        }

        public void setTime(String time2) {
            this.time = time2;
        }

        public String getTimeStamp() {
            return this.timeStamp;
        }

        public void setTimeStamp(String timeStamp2) {
            this.timeStamp = timeStamp2;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type2) {
            this.type = type2;
        }

        public String getUmpPrice() {
            return this.umpPrice;
        }

        public void setUmpPrice(String umpPrice2) {
            this.umpPrice = umpPrice2;
        }

        public String getUniqueId() {
            return this.uniqueId;
        }

        public void setUniqueId(String uniqueId2) {
            this.uniqueId = uniqueId2;
        }

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url2) {
            this.url = url2;
        }

        public String getUserId() {
            return this.userId;
        }

        public void setUserId(String userId2) {
            this.userId = userId2;
        }
    }

    public static class Result {
        /* access modifiers changed from: private */
        public List<DataItem> dataList;
        /* access modifiers changed from: private */
        public String date;

        public void setDataList(List<DataItem> dataList2) {
            this.dataList = dataList2;
        }

        public List<DataItem> getDataList() {
            return this.dataList;
        }

        public String getDate() {
            return this.date;
        }

        public void setDate(String date2) {
            this.date = date2;
        }
    }
}
