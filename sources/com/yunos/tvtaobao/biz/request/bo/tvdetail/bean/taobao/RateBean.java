package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao;

import java.util.List;

public class RateBean {
    private List<KeywordsBean> keywords;
    private List<PropRateBean> propRate;
    private List<RateListBean> rateList;
    private String totalCount;

    public String getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(String totalCount2) {
        this.totalCount = totalCount2;
    }

    public List<KeywordsBean> getKeywords() {
        return this.keywords;
    }

    public void setKeywords(List<KeywordsBean> keywords2) {
        this.keywords = keywords2;
    }

    public List<RateListBean> getRateList() {
        return this.rateList;
    }

    public void setRateList(List<RateListBean> rateList2) {
        this.rateList = rateList2;
    }

    public List<PropRateBean> getPropRate() {
        return this.propRate;
    }

    public void setPropRate(List<PropRateBean> propRate2) {
        this.propRate = propRate2;
    }

    public static class KeywordsBean {
        private String attribute;
        private String count;
        private String type;
        private String word;

        public String getAttribute() {
            return this.attribute;
        }

        public void setAttribute(String attribute2) {
            this.attribute = attribute2;
        }

        public String getWord() {
            return this.word;
        }

        public void setWord(String word2) {
            this.word = word2;
        }

        public String getCount() {
            return this.count;
        }

        public void setCount(String count2) {
            this.count = count2;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type2) {
            this.type = type2;
        }
    }

    public static class RateListBean {
        private String content;
        private String dateTime;
        private String headExtraPic;
        private String headPic;
        private List<String> images;
        private String isVip;
        private String memberIcon;
        private String memberLevel;
        private String skuInfo;
        private String tmallMemberLevel;
        private String userName;

        public String getContent() {
            return this.content;
        }

        public void setContent(String content2) {
            this.content = content2;
        }

        public String getUserName() {
            return this.userName;
        }

        public void setUserName(String userName2) {
            this.userName = userName2;
        }

        public String getHeadPic() {
            return this.headPic;
        }

        public void setHeadPic(String headPic2) {
            this.headPic = headPic2;
        }

        public String getMemberLevel() {
            return this.memberLevel;
        }

        public void setMemberLevel(String memberLevel2) {
            this.memberLevel = memberLevel2;
        }

        public String getDateTime() {
            return this.dateTime;
        }

        public void setDateTime(String dateTime2) {
            this.dateTime = dateTime2;
        }

        public String getSkuInfo() {
            return this.skuInfo;
        }

        public void setSkuInfo(String skuInfo2) {
            this.skuInfo = skuInfo2;
        }

        public String getTmallMemberLevel() {
            return this.tmallMemberLevel;
        }

        public void setTmallMemberLevel(String tmallMemberLevel2) {
            this.tmallMemberLevel = tmallMemberLevel2;
        }

        public String getHeadExtraPic() {
            return this.headExtraPic;
        }

        public void setHeadExtraPic(String headExtraPic2) {
            this.headExtraPic = headExtraPic2;
        }

        public String getMemberIcon() {
            return this.memberIcon;
        }

        public void setMemberIcon(String memberIcon2) {
            this.memberIcon = memberIcon2;
        }

        public String getIsVip() {
            return this.isVip;
        }

        public void setIsVip(String isVip2) {
            this.isVip = isVip2;
        }

        public List<String> getImages() {
            return this.images;
        }

        public void setImages(List<String> images2) {
            this.images = images2;
        }
    }

    public static class PropRateBean {
        private String avatar;
        private String comment;
        private String commentCount;
        private String feedId;
        private String image;
        private String propName;
        private String skuVids;

        public String getPropName() {
            return this.propName;
        }

        public void setPropName(String propName2) {
            this.propName = propName2;
        }

        public String getAvatar() {
            return this.avatar;
        }

        public void setAvatar(String avatar2) {
            this.avatar = avatar2;
        }

        public String getComment() {
            return this.comment;
        }

        public void setComment(String comment2) {
            this.comment = comment2;
        }

        public String getCommentCount() {
            return this.commentCount;
        }

        public void setCommentCount(String commentCount2) {
            this.commentCount = commentCount2;
        }

        public String getImage() {
            return this.image;
        }

        public void setImage(String image2) {
            this.image = image2;
        }

        public String getFeedId() {
            return this.feedId;
        }

        public void setFeedId(String feedId2) {
            this.feedId = feedId2;
        }

        public String getSkuVids() {
            return this.skuVids;
        }

        public void setSkuVids(String skuVids2) {
            this.skuVids = skuVids2;
        }
    }
}
