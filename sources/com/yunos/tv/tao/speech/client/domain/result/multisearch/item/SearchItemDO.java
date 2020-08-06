package com.yunos.tv.tao.speech.client.domain.result.multisearch.item;

import com.tvtaobao.voicesdk.bean.JinnangDo;
import java.io.Serializable;
import java.util.List;

public class SearchItemDO implements Serializable {
    private String docsFound;
    private String hasNextPage;
    private List<JinnangDo> jinNangItems;
    private String keyword;
    private List<ProductDo> model;
    private String recommend;
    private String searchURL;
    private String status;

    public String getDocsFound() {
        return this.docsFound;
    }

    public void setDocsFound(String docsFound2) {
        this.docsFound = docsFound2;
    }

    public String getHasNextPage() {
        return this.hasNextPage;
    }

    public void setHasNextPage(String hasNextPage2) {
        this.hasNextPage = hasNextPage2;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword2) {
        this.keyword = keyword2;
    }

    public String getRecommend() {
        return this.recommend;
    }

    public void setRecommend(String recommend2) {
        this.recommend = recommend2;
    }

    public String getSearchURL() {
        return this.searchURL;
    }

    public void setSearchURL(String searchURL2) {
        this.searchURL = searchURL2;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status2) {
        this.status = status2;
    }

    public List<ProductDo> getModel() {
        return this.model;
    }

    public void setModel(List<ProductDo> model2) {
        this.model = model2;
    }

    public List<JinnangDo> getJinNangItems() {
        return this.jinNangItems;
    }

    public void setJinNangItems(List<JinnangDo> jinNangItems2) {
        this.jinNangItems = jinNangItems2;
    }
}
