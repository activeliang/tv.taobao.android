package com.yunos.tv.tao.speech.client.domain.result.multisearch.takeout;

import com.yunos.tv.tao.speech.client.domain.result.multisearch.BaseResultVO;
import java.io.Serializable;

public class TakeoutSearchResultVO extends BaseResultVO implements Serializable {
    private String keywords;
    private String shopName;
    private TakeoutSearchItemDO takeOutSearchItemDO;

    public String getShopName() {
        return this.shopName;
    }

    public void setShopName(String shopName2) {
        this.shopName = shopName2;
    }

    public TakeoutSearchItemDO getTakeOutSearchItemDO() {
        return this.takeOutSearchItemDO;
    }

    public void setTakeOutSearchItemDO(TakeoutSearchItemDO takeOutSearchItemDO2) {
        this.takeOutSearchItemDO = takeOutSearchItemDO2;
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords2) {
        this.keywords = keywords2;
    }
}
