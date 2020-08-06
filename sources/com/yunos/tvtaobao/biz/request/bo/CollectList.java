package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;
import java.util.ArrayList;

public class CollectList implements Serializable {
    private static final long serialVersionUID = -610484326142980002L;
    private int currentPage;
    private boolean havNextPage;
    private int pageSize;
    private ArrayList<Collect> resultList;
    private int totalCount;

    public int geTotalCount() {
        return this.totalCount;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public ArrayList<Collect> getResultList() {
        return this.resultList;
    }

    public void setTotalCount(int totalCount2) {
        this.totalCount = totalCount2;
    }

    public void setCurrentPage(int currentPage2) {
        this.currentPage = currentPage2;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize2) {
        this.pageSize = pageSize2;
    }

    public void setResultList(ArrayList<Collect> resultList2) {
        this.resultList = resultList2;
    }

    public boolean isHavNextPage() {
        return this.havNextPage;
    }

    public void setHavNextPage(boolean havNextPage2) {
        this.havNextPage = havNextPage2;
    }
}
