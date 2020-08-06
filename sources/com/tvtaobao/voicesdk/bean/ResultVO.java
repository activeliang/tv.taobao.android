package com.tvtaobao.voicesdk.bean;

import java.util.List;

public class ResultVO {
    private String beginTime;
    private String categoryName;
    private Object data;
    private List<DetailListVO> detailList;
    private String endTime;
    private String firstCategoryId;
    private String firstCategoryName;
    private String itemResult;
    private String keywords;
    private String norm;
    private String pageNo;
    private String pageSize;
    private String secondCategoryId;
    private String secondCategoryName;
    private String storeId;
    private String tbMainOrderId;
    private String timeText;
    private String totalCount;
    private String totalPage;

    public String toString() {
        return "ResultVO{pageNo='" + this.pageNo + '\'' + ", pageSize='" + this.pageSize + '\'' + ", totalCount='" + this.totalCount + '\'' + ", tbMainOrderId='" + this.tbMainOrderId + '\'' + ", keywords='" + this.keywords + '\'' + ", storeId='" + this.storeId + '\'' + ", detailList=" + this.detailList + ", categoryName='" + this.categoryName + '\'' + ", firstCategoryId='" + this.firstCategoryId + '\'' + ", firstCategoryName='" + this.firstCategoryName + '\'' + ", secondCategoryId='" + this.secondCategoryId + '\'' + ", secondCategoryName='" + this.secondCategoryName + '\'' + ", totalPage='" + this.totalPage + '\'' + '}';
    }

    public String getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(String pageNo2) {
        this.pageNo = pageNo2;
    }

    public String getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(String pageSize2) {
        this.pageSize = pageSize2;
    }

    public String getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(String totalCount2) {
        this.totalCount = totalCount2;
    }

    public String getTbMainOrderId() {
        return this.tbMainOrderId;
    }

    public void setTbMainOrderId(String tbMainOrderId2) {
        this.tbMainOrderId = tbMainOrderId2;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords2) {
        this.keywords = keywords2;
    }

    public String getStoreId() {
        return this.storeId;
    }

    public void setStoreId(String storeId2) {
        this.storeId = storeId2;
    }

    public List<DetailListVO> getDetailList() {
        return this.detailList;
    }

    public void setDetailList(List<DetailListVO> detailList2) {
        this.detailList = detailList2;
    }

    public String getFirstCategoryId() {
        return this.firstCategoryId;
    }

    public void setFirstCategoryId(String firstCategoryId2) {
        this.firstCategoryId = firstCategoryId2;
    }

    public String getFirstCategoryName() {
        return this.firstCategoryName;
    }

    public void setFirstCategoryName(String firstCategoryName2) {
        this.firstCategoryName = firstCategoryName2;
    }

    public String getSecondCategoryId() {
        return this.secondCategoryId;
    }

    public void setSecondCategoryId(String secondCategoryId2) {
        this.secondCategoryId = secondCategoryId2;
    }

    public String getSecondCategoryName() {
        return this.secondCategoryName;
    }

    public void setSecondCategoryName(String secondCategoryName2) {
        this.secondCategoryName = secondCategoryName2;
    }

    public String getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(String totalPage2) {
        this.totalPage = totalPage2;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime2) {
        this.endTime = endTime2;
    }

    public String getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(String beginTime2) {
        this.beginTime = beginTime2;
    }

    public String getTimeText() {
        return this.timeText;
    }

    public void setTimeText(String timeText2) {
        this.timeText = timeText2;
    }

    public String getNorm() {
        return this.norm;
    }

    public void setNorm(String norm2) {
        this.norm = norm2;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName2) {
        this.categoryName = categoryName2;
    }

    public <T> T getData() {
        return this.data;
    }

    public void setData(Object data2) {
        this.data = data2;
    }

    public String getItemResult() {
        return this.itemResult;
    }

    public void setItemResult(String itemResult2) {
        this.itemResult = itemResult2;
    }
}
