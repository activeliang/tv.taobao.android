package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.List;

public class ShopSearchResultBean implements Serializable {
    private String haveNext;
    private List<ItemListBean> itemInfoList;
    private String pageNo;
    private String pageSize;
    private String totalNum;
    private String totalPage;

    public String toString() {
        return "ShopSearchResultBean{haveNext='" + this.haveNext + '\'' + ", pageNo='" + this.pageNo + '\'' + ", pageSize='" + this.pageSize + '\'' + ", totalNum='" + this.totalNum + '\'' + ", totalPage='" + this.totalPage + '\'' + ", itemInfoList=" + this.itemInfoList + '}';
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

    public String getTotalNum() {
        return this.totalNum;
    }

    public void setTotalNum(String totalNum2) {
        this.totalNum = totalNum2;
    }

    public String getTotalPage() {
        return this.totalPage;
    }

    public void setTotalPage(String totalPage2) {
        this.totalPage = totalPage2;
    }

    public String getHaveNext() {
        return this.haveNext;
    }

    public void setHaveNext(String haveNext2) {
        this.haveNext = haveNext2;
    }

    public List<ItemListBean> getItemInfoList() {
        return this.itemInfoList;
    }

    public void setItemInfoList(List<ItemListBean> itemInfoList2) {
        this.itemInfoList = itemInfoList2;
    }
}
