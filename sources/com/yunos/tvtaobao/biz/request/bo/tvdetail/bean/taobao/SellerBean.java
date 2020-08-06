package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao;

import java.io.Serializable;
import java.util.List;

public class SellerBean implements Serializable {
    private String allItemCount;
    private String brandIcon;
    private String brandIconRatio;
    private String creditLevel;
    private String creditLevelIcon;
    private List<EvaluatesBean> evaluates;
    private String fans;
    private String fbt2User;
    private String goodRatePercentage;
    private String sellerNick;
    private String sellerType;
    private String shopCard;
    private String shopIcon;
    private String shopId;
    private String shopName;
    private String shopType;
    private String shopUrl;
    private boolean showShopLinkIcon;
    private String simpleShopDOStatus;
    private String starts;
    private String taoShopUrl;
    private String userId;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId2) {
        this.userId = userId2;
    }

    public String getShopId() {
        return this.shopId;
    }

    public void setShopId(String shopId2) {
        this.shopId = shopId2;
    }

    public String getShopName() {
        return this.shopName;
    }

    public void setShopName(String shopName2) {
        this.shopName = shopName2;
    }

    public String getShopUrl() {
        return this.shopUrl;
    }

    public void setShopUrl(String shopUrl2) {
        this.shopUrl = shopUrl2;
    }

    public String getTaoShopUrl() {
        return this.taoShopUrl;
    }

    public void setTaoShopUrl(String taoShopUrl2) {
        this.taoShopUrl = taoShopUrl2;
    }

    public String getShopIcon() {
        return this.shopIcon;
    }

    public void setShopIcon(String shopIcon2) {
        this.shopIcon = shopIcon2;
    }

    public String getFans() {
        return this.fans;
    }

    public void setFans(String fans2) {
        this.fans = fans2;
    }

    public String getAllItemCount() {
        return this.allItemCount;
    }

    public void setAllItemCount(String allItemCount2) {
        this.allItemCount = allItemCount2;
    }

    public boolean isShowShopLinkIcon() {
        return this.showShopLinkIcon;
    }

    public void setShowShopLinkIcon(boolean showShopLinkIcon2) {
        this.showShopLinkIcon = showShopLinkIcon2;
    }

    public String getShopCard() {
        return this.shopCard;
    }

    public void setShopCard(String shopCard2) {
        this.shopCard = shopCard2;
    }

    public String getSellerType() {
        return this.sellerType;
    }

    public void setSellerType(String sellerType2) {
        this.sellerType = sellerType2;
    }

    public String getShopType() {
        return this.shopType;
    }

    public void setShopType(String shopType2) {
        this.shopType = shopType2;
    }

    public String getSellerNick() {
        return this.sellerNick;
    }

    public void setSellerNick(String sellerNick2) {
        this.sellerNick = sellerNick2;
    }

    public String getCreditLevel() {
        return this.creditLevel;
    }

    public void setCreditLevel(String creditLevel2) {
        this.creditLevel = creditLevel2;
    }

    public String getCreditLevelIcon() {
        return this.creditLevelIcon;
    }

    public void setCreditLevelIcon(String creditLevelIcon2) {
        this.creditLevelIcon = creditLevelIcon2;
    }

    public String getBrandIcon() {
        return this.brandIcon;
    }

    public void setBrandIcon(String brandIcon2) {
        this.brandIcon = brandIcon2;
    }

    public String getBrandIconRatio() {
        return this.brandIconRatio;
    }

    public void setBrandIconRatio(String brandIconRatio2) {
        this.brandIconRatio = brandIconRatio2;
    }

    public String getStarts() {
        return this.starts;
    }

    public void setStarts(String starts2) {
        this.starts = starts2;
    }

    public String getGoodRatePercentage() {
        return this.goodRatePercentage;
    }

    public void setGoodRatePercentage(String goodRatePercentage2) {
        this.goodRatePercentage = goodRatePercentage2;
    }

    public String getFbt2User() {
        return this.fbt2User;
    }

    public void setFbt2User(String fbt2User2) {
        this.fbt2User = fbt2User2;
    }

    public String getSimpleShopDOStatus() {
        return this.simpleShopDOStatus;
    }

    public void setSimpleShopDOStatus(String simpleShopDOStatus2) {
        this.simpleShopDOStatus = simpleShopDOStatus2;
    }

    public List<EvaluatesBean> getEvaluates() {
        return this.evaluates;
    }

    public void setEvaluates(List<EvaluatesBean> evaluates2) {
        this.evaluates = evaluates2;
    }

    public static class EvaluatesBean implements Serializable {
        private String level;
        private String levelBackgroundColor;
        private String levelText;
        private String levelTextColor;
        private String score;
        private String title;
        private String tmallLevelBackgroundColor;
        private String tmallLevelTextColor;
        private String type;

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getScore() {
            return this.score;
        }

        public void setScore(String score2) {
            this.score = score2;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type2) {
            this.type = type2;
        }

        public String getLevel() {
            return this.level;
        }

        public void setLevel(String level2) {
            this.level = level2;
        }

        public String getLevelText() {
            return this.levelText;
        }

        public void setLevelText(String levelText2) {
            this.levelText = levelText2;
        }

        public String getLevelTextColor() {
            return this.levelTextColor;
        }

        public void setLevelTextColor(String levelTextColor2) {
            this.levelTextColor = levelTextColor2;
        }

        public String getLevelBackgroundColor() {
            return this.levelBackgroundColor;
        }

        public void setLevelBackgroundColor(String levelBackgroundColor2) {
            this.levelBackgroundColor = levelBackgroundColor2;
        }

        public String getTmallLevelTextColor() {
            return this.tmallLevelTextColor;
        }

        public void setTmallLevelTextColor(String tmallLevelTextColor2) {
            this.tmallLevelTextColor = tmallLevelTextColor2;
        }

        public String getTmallLevelBackgroundColor() {
            return this.tmallLevelBackgroundColor;
        }

        public void setTmallLevelBackgroundColor(String tmallLevelBackgroundColor2) {
            this.tmallLevelBackgroundColor = tmallLevelBackgroundColor2;
        }
    }
}
