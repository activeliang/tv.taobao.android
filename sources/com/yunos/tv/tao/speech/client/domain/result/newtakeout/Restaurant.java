package com.yunos.tv.tao.speech.client.domain.result.newtakeout;

import java.util.List;

public class Restaurant {
    private static final String TAG = "Restaurant";
    private AdInfo adInfo;
    public String closingCountDown;
    private String description;
    private String distance;
    private String eid;
    private String floatDeliveryFee;
    private String floatMinimumOrderAmount;
    private String id;
    private String imagePath;
    private String isNew;
    private String isPremium;
    private String isRetailShop;
    private String isStockEmpty;
    private String isValid;
    private double latitude;
    private double longitude;
    private String name;
    private String nextBusinessTime;
    private List<String> openingHours;
    private String orderLeadTime;
    private PiecewiseAgentFee piecewiseAgentFee;
    private String promotionInfo;
    private String rating;
    private String ratingCount;
    private RatingInfo ratingInfo;
    private String recentOrderNum;
    private String recentOrderNumDisplay;
    private Recommend recommend;
    private String scheme;
    private String star;
    private String status;
    private List<SupportTags> supportTags;
    private List<Supports> supports;
    private Theme theme;

    public void setDistance(String distance2) {
        this.distance = distance2;
    }

    public String getDistance() {
        return this.distance;
    }

    public void setScheme(String scheme2) {
        this.scheme = scheme2;
    }

    public String getScheme() {
        return this.scheme;
    }

    public void setNextBusinessTime(String nextBusinessTime2) {
        this.nextBusinessTime = nextBusinessTime2;
    }

    public String getNextBusinessTime() {
        return this.nextBusinessTime;
    }

    public void setPiecewiseAgentFee(PiecewiseAgentFee piecewiseAgentFee2) {
        this.piecewiseAgentFee = piecewiseAgentFee2;
    }

    public PiecewiseAgentFee getPiecewiseAgentFee() {
        return this.piecewiseAgentFee;
    }

    public void setImagePath(String imagePath2) {
        this.imagePath = imagePath2;
    }

    public String getImagePath() {
        return this.imagePath;
    }

    public void setLatitude(double latitude2) {
        this.latitude = latitude2;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setRating(String rating2) {
        this.rating = rating2;
    }

    public String getRating() {
        return this.rating;
    }

    public void setDescription(String description2) {
        this.description = description2;
    }

    public String getDescription() {
        return this.description;
    }

    public void setIsRetailShop(String isRetailShop2) {
        this.isRetailShop = isRetailShop2;
    }

    public String getIsRetailShop() {
        return this.isRetailShop;
    }

    public void setRecommend(Recommend recommend2) {
        this.recommend = recommend2;
    }

    public Recommend getRecommend() {
        return this.recommend;
    }

    public void setRecentOrderNum(String recentOrderNum2) {
        this.recentOrderNum = recentOrderNum2;
    }

    public String getRecentOrderNum() {
        return this.recentOrderNum;
    }

    public void setIsStockEmpty(String isStockEmpty2) {
        this.isStockEmpty = isStockEmpty2;
    }

    public String getIsStockEmpty() {
        return this.isStockEmpty;
    }

    public void setRecentOrderNumDisplay(String recentOrderNumDisplay2) {
        this.recentOrderNumDisplay = recentOrderNumDisplay2;
    }

    public String getRecentOrderNumDisplay() {
        return this.recentOrderNumDisplay;
    }

    public void setSupports(List<Supports> supports2) {
        this.supports = supports2;
    }

    public List<Supports> getSupports() {
        return this.supports;
    }

    public String getClosingCountDown() {
        return this.closingCountDown == null ? "" : this.closingCountDown;
    }

    public void setClosingCountDown(String closingCountDown2) {
        this.closingCountDown = closingCountDown2;
    }

    public void setTheme(Theme theme2) {
        this.theme = theme2;
    }

    public Theme getTheme() {
        return this.theme;
    }

    public void setAdInfo(AdInfo adInfo2) {
        this.adInfo = adInfo2;
    }

    public AdInfo getAdInfo() {
        return this.adInfo;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public String getId() {
        return this.id;
    }

    public void setFloatDeliveryFee(String floatDeliveryFee2) {
        this.floatDeliveryFee = floatDeliveryFee2;
    }

    public String getFloatDeliveryFee() {
        return this.floatDeliveryFee;
    }

    public void setLongitude(double longitude2) {
        this.longitude = longitude2;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setStar(String star2) {
        this.star = star2;
    }

    public String getStar() {
        return this.star;
    }

    public void setOrderLeadTime(String orderLeadTime2) {
        this.orderLeadTime = orderLeadTime2;
    }

    public String getOrderLeadTime() {
        return this.orderLeadTime;
    }

    public void setFloatMinimumOrderAmount(String floatMinimumOrderAmount2) {
        this.floatMinimumOrderAmount = floatMinimumOrderAmount2;
    }

    public String getFloatMinimumOrderAmount() {
        return this.floatMinimumOrderAmount;
    }

    public void setIsValid(String isValid2) {
        this.isValid = isValid2;
    }

    public String getIsValid() {
        return this.isValid;
    }

    public void setIsNew(String isNew2) {
        this.isNew = isNew2;
    }

    public String getIsNew() {
        return this.isNew;
    }

    public void setPromotionInfo(String promotionInfo2) {
        this.promotionInfo = promotionInfo2;
    }

    public String getPromotionInfo() {
        return this.promotionInfo;
    }

    public void setRatingCount(String ratingCount2) {
        this.ratingCount = ratingCount2;
    }

    public String getRatingCount() {
        return this.ratingCount;
    }

    public void setSupportTags(List<SupportTags> supportTags2) {
        this.supportTags = supportTags2;
    }

    public List<SupportTags> getSupportTags() {
        return this.supportTags;
    }

    public void setRatingInfo(RatingInfo ratingInfo2) {
        this.ratingInfo = ratingInfo2;
    }

    public RatingInfo getRatingInfo() {
        return this.ratingInfo;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getName() {
        return this.name;
    }

    public void setOpeningHours(List<String> openingHours2) {
        this.openingHours = openingHours2;
    }

    public List<String> getOpeningHours() {
        return this.openingHours;
    }

    public void setIsPremium(String isPremium2) {
        this.isPremium = isPremium2;
    }

    public String getIsPremium() {
        return this.isPremium;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public String getStatus() {
        return this.status;
    }

    public String getEid() {
        return this.eid == null ? "" : this.eid;
    }

    public void setEid(String eid2) {
        this.eid = eid2;
    }
}
