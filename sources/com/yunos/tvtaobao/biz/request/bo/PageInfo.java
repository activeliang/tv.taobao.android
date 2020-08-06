package com.yunos.tvtaobao.biz.request.bo;

public class PageInfo {
    private String hasMore;
    private String nextStartTime;
    private String pageSize;
    private String preloadPage;
    private String startRow;
    private String totalCount;

    public void setHasMore(String hasMore2) {
        this.hasMore = hasMore2;
    }

    public String getHasMore() {
        return this.hasMore;
    }

    public void setNextStartTime(String nextStartTime2) {
        this.nextStartTime = nextStartTime2;
    }

    public String getNextStartTime() {
        return this.nextStartTime;
    }

    public void setPageSize(String pageSize2) {
        this.pageSize = pageSize2;
    }

    public String getPageSize() {
        return this.pageSize;
    }

    public void setPreloadPage(String preloadPage2) {
        this.preloadPage = preloadPage2;
    }

    public String getPreloadPage() {
        return this.preloadPage;
    }

    public void setStartRow(String startRow2) {
        this.startRow = startRow2;
    }

    public String getStartRow() {
        return this.startRow;
    }

    public void setTotalCount(String totalCount2) {
        this.totalCount = totalCount2;
    }

    public String getTotalCount() {
        return this.totalCount;
    }
}
