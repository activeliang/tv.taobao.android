package com.yunos.tvtaobao.biz.request.bo;

import java.util.ArrayList;
import java.util.List;

public class FollowItemInfo {
    private String acceptDynamic;
    private String certDesc;
    private String certTitle;
    private String shopId;
    private String specialFollow;
    private String tag;
    private List<FollowTagListItem> tagList;
    private String type;
    private String userId;
    private String userLogo;
    private String userNick;
    private String userUrl;

    public String getAcceptDynamic() {
        return this.acceptDynamic == null ? "" : this.acceptDynamic;
    }

    public void setAcceptDynamic(String acceptDynamic2) {
        this.acceptDynamic = acceptDynamic2;
    }

    public String getCertDesc() {
        return this.certDesc == null ? "" : this.certDesc;
    }

    public void setCertDesc(String certDesc2) {
        this.certDesc = certDesc2;
    }

    public String getCertTitle() {
        return this.certTitle == null ? "" : this.certTitle;
    }

    public void setCertTitle(String certTitle2) {
        this.certTitle = certTitle2;
    }

    public String getShopId() {
        return this.shopId == null ? "" : this.shopId;
    }

    public void setShopId(String shopId2) {
        this.shopId = shopId2;
    }

    public String getSpecialFollow() {
        return this.specialFollow == null ? "" : this.specialFollow;
    }

    public void setSpecialFollow(String specialFollow2) {
        this.specialFollow = specialFollow2;
    }

    public String getTag() {
        return this.tag == null ? "" : this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public List<FollowTagListItem> getTagList() {
        if (this.tagList == null) {
            return new ArrayList();
        }
        return this.tagList;
    }

    public void setTagList(List<FollowTagListItem> tagList2) {
        this.tagList = tagList2;
    }

    public String getType() {
        return this.type == null ? "" : this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public String getUserId() {
        return this.userId == null ? "" : this.userId;
    }

    public void setUserId(String userId2) {
        this.userId = userId2;
    }

    public String getUserLogo() {
        return this.userLogo == null ? "" : this.userLogo;
    }

    public void setUserLogo(String userLogo2) {
        this.userLogo = userLogo2;
    }

    public String getUserNick() {
        return this.userNick == null ? "" : this.userNick;
    }

    public void setUserNick(String userNick2) {
        this.userNick = userNick2;
    }

    public String getUserUrl() {
        return this.userUrl == null ? "" : this.userUrl;
    }

    public void setUserUrl(String userUrl2) {
        this.userUrl = userUrl2;
    }
}
