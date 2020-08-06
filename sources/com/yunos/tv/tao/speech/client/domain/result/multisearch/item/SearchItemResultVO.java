package com.yunos.tv.tao.speech.client.domain.result.multisearch.item;

import com.yunos.tv.tao.speech.client.domain.result.multisearch.BaseResultVO;
import java.io.Serializable;

public class SearchItemResultVO extends BaseResultVO implements Serializable {
    private String keywords;
    private SearchItemDO searchItemDO;

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
}
