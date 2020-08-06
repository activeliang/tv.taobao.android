package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class TakeOutSearchBo {
    private List<EntityListBean> entityList;
    private String keyword;
    private String pageNo;
    private String pageSize;
    private String spoken;
    private String spokenTxt;
    private List<String> tips;
    private String totalNum;
    private String totalPage;

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword2) {
        this.keyword = keyword2;
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

    public String getSpoken() {
        return this.spoken;
    }

    public void setSpoken(String spoken2) {
        this.spoken = spoken2;
    }

    public String getSpokenTxt() {
        return this.spokenTxt;
    }

    public void setSpokenTxt(String spokenTxt2) {
        this.spokenTxt = spokenTxt2;
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

    public List<EntityListBean> getEntityList() {
        return this.entityList;
    }

    public void setEntityList(List<EntityListBean> entityList2) {
        this.entityList = entityList2;
    }

    public List<String> getTips() {
        return this.tips;
    }

    public void setTips(List<String> tips2) {
        this.tips = tips2;
    }
}
