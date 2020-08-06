package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.List;

public class GoodsList implements Serializable {
    private static final long serialVersionUID = 3250538045046006676L;
    private String currentPage;
    private String currentSort;
    public boolean isHave;
    private List<GoodsListItem> itemsArray;
    private String pageSize;
    private String shopId;
    private String shopTitle;
    private int totalResults;

    public String getCurrentSort() {
        return this.currentSort;
    }

    public void setCurrentSort(String currentSort2) {
        this.currentSort = currentSort2;
    }

    public List<GoodsListItem> getItemsArray() {
        return this.itemsArray;
    }

    public void setItemsArray(List<GoodsListItem> itemsArray2) {
        this.itemsArray = itemsArray2;
    }

    public int getTotalResults() {
        return this.totalResults;
    }

    public void setTotalResults(int totalResults2) {
        this.totalResults = totalResults2;
    }

    public String getShopTitle() {
        return this.shopTitle;
    }

    public void setShopTitle(String shopTitle2) {
        this.shopTitle = shopTitle2;
    }

    public String getShopId() {
        return this.shopId;
    }

    public void setShopId(String shopId2) {
        this.shopId = shopId2;
    }

    public String getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(String pageSize2) {
        this.pageSize = pageSize2;
    }

    public String getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(String currentPage2) {
        this.currentPage = currentPage2;
    }

    public String toString() {
        return "GoodsList [currentSort=" + this.currentSort + ", itemsArray=" + this.itemsArray + ", totalResults=" + this.totalResults + ", shopTitle=" + this.shopTitle + ", shopId=" + this.shopId + ", pageSize=" + this.pageSize + ", currentPage=" + this.currentPage + "]";
    }
}
