package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao;

import java.io.Serializable;
import java.util.List;

public class ItemBean implements Serializable {
    private String brandValueId;
    private String categoryId;
    private String commentCount;
    private String favcount;
    private List<String> images;
    private String itemId;
    private boolean openDecoration;
    private String rootCategoryId;
    private String skuText;
    private String subtitle;
    private String taobaoDescUrl;
    private String taobaoPcDescUrl;
    private String title;
    private String tmallDescUrl;

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId2) {
        this.itemId = itemId2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public void setSubtitle(String subtitle2) {
        this.subtitle = subtitle2;
    }

    public String getCategoryId() {
        return this.categoryId;
    }

    public void setCategoryId(String categoryId2) {
        this.categoryId = categoryId2;
    }

    public String getRootCategoryId() {
        return this.rootCategoryId;
    }

    public void setRootCategoryId(String rootCategoryId2) {
        this.rootCategoryId = rootCategoryId2;
    }

    public String getBrandValueId() {
        return this.brandValueId;
    }

    public void setBrandValueId(String brandValueId2) {
        this.brandValueId = brandValueId2;
    }

    public String getSkuText() {
        return this.skuText;
    }

    public void setSkuText(String skuText2) {
        this.skuText = skuText2;
    }

    public String getCommentCount() {
        return this.commentCount;
    }

    public void setCommentCount(String commentCount2) {
        this.commentCount = commentCount2;
    }

    public String getFavcount() {
        return this.favcount;
    }

    public void setFavcount(String favcount2) {
        this.favcount = favcount2;
    }

    public String getTaobaoDescUrl() {
        return this.taobaoDescUrl;
    }

    public void setTaobaoDescUrl(String taobaoDescUrl2) {
        this.taobaoDescUrl = taobaoDescUrl2;
    }

    public String getTmallDescUrl() {
        return this.tmallDescUrl;
    }

    public void setTmallDescUrl(String tmallDescUrl2) {
        this.tmallDescUrl = tmallDescUrl2;
    }

    public String getTaobaoPcDescUrl() {
        return this.taobaoPcDescUrl;
    }

    public void setTaobaoPcDescUrl(String taobaoPcDescUrl2) {
        this.taobaoPcDescUrl = taobaoPcDescUrl2;
    }

    public boolean isOpenDecoration() {
        return this.openDecoration;
    }

    public void setOpenDecoration(boolean openDecoration2) {
        this.openDecoration = openDecoration2;
    }

    public List<String> getImages() {
        return this.images;
    }

    public void setImages(List<String> images2) {
        this.images = images2;
    }
}
