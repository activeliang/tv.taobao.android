package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class CollectionsInfo {
    private String categoryTips;
    private List<NewCollection> favList;
    private PageInfo pageInfo;

    public void setCategoryTips(String categoryTips2) {
        this.categoryTips = categoryTips2;
    }

    public String getCategoryTips() {
        return this.categoryTips;
    }

    public void setFavList(List<NewCollection> favList2) {
        this.favList = favList2;
    }

    public List<NewCollection> getFavList() {
        return this.favList;
    }

    public void setPageInfo(PageInfo pageInfo2) {
        this.pageInfo = pageInfo2;
    }

    public PageInfo getPageInfo() {
        return this.pageInfo;
    }
}
