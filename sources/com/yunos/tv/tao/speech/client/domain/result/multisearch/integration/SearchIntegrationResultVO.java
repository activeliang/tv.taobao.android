package com.yunos.tv.tao.speech.client.domain.result.multisearch.integration;

import com.yunos.tv.tao.speech.client.domain.result.multisearch.BaseResultVO;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.item.SearchItemDO;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.takeout.TakeoutSearchItemDO;
import java.io.Serializable;

public class SearchIntegrationResultVO extends BaseResultVO implements Serializable {
    private String keywords;
    private SearchItemDO searchItemDO;
    private TakeoutSearchItemDO takeoutSearchItemDO;

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords2) {
        this.keywords = keywords2;
    }

    public SearchItemDO getSearchItemDO() {
        return this.searchItemDO;
    }

    public void setSearchItemDO(SearchItemDO searchItemDO2) {
        this.searchItemDO = searchItemDO2;
    }

    public TakeoutSearchItemDO getTakeoutSearchItemDO() {
        return this.takeoutSearchItemDO;
    }

    public void setTakeoutSearchItemDO(TakeoutSearchItemDO takeoutSearchItemDO2) {
        this.takeoutSearchItemDO = takeoutSearchItemDO2;
    }
}
