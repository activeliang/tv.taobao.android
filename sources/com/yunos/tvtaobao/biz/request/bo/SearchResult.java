package com.yunos.tvtaobao.biz.request.bo;

import java.io.Serializable;

public class SearchResult implements Serializable {
    private static final long serialVersionUID = -76963708439296308L;
    private String keywords;
    private Long weight;

    public void setKeyword(String keyword) {
        this.keywords = keyword;
    }

    public String getKeyword() {
        return this.keywords;
    }

    public void setWeight(Long weight2) {
        this.weight = weight2;
    }

    public Long getWeight() {
        return this.weight;
    }

    public String toString() {
        return "keyword = " + this.keywords + ", weight = " + this.weight;
    }
}
