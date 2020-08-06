package com.tvtaobao.voicesdk.utils;

import com.yunos.tv.tao.speech.client.domain.result.multisearch.integration.SearchIntegrationResultVO;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.item.SearchItemResultVO;
import com.yunos.tv.tao.speech.client.domain.result.multisearch.takeout.TakeoutSearchResultVO;

public class IntentSearchData {
    private static IntentSearchData intentSearchData;
    private SearchIntegrationResultVO searchIntegrationResultVO;
    private SearchItemResultVO searchItemResultVO;
    private TakeoutSearchResultVO takeoutSearchResultVO;

    public static IntentSearchData getIntence() {
        if (intentSearchData == null) {
            intentSearchData = new IntentSearchData();
        }
        return intentSearchData;
    }

    public SearchIntegrationResultVO getSearchIntegrationResultVO() {
        return this.searchIntegrationResultVO;
    }

    public void setSearchIntegrationResultVO(SearchIntegrationResultVO searchIntegrationResultVO2) {
        this.searchIntegrationResultVO = searchIntegrationResultVO2;
    }

    public SearchItemResultVO getSearchItemResultVO() {
        return this.searchItemResultVO;
    }

    public void setSearchItemResultVO(SearchItemResultVO searchItemResultVO2) {
        this.searchItemResultVO = searchItemResultVO2;
    }

    public TakeoutSearchResultVO getTakeoutSearchResultVO() {
        return this.takeoutSearchResultVO;
    }

    public void setTakeoutSearchResultVO(TakeoutSearchResultVO takeoutSearchResultVO2) {
        this.takeoutSearchResultVO = takeoutSearchResultVO2;
    }

    public void clear() {
        this.searchIntegrationResultVO = null;
        this.searchItemResultVO = null;
        this.takeoutSearchResultVO = null;
    }
}
