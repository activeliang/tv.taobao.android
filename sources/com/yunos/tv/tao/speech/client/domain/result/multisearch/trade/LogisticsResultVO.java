package com.yunos.tv.tao.speech.client.domain.result.multisearch.trade;

import com.yunos.tv.tao.speech.client.domain.result.multisearch.BaseResultVO;
import java.io.Serializable;

public class LogisticsResultVO extends BaseResultVO implements Serializable {
    private String keywords;
    private TradeLogisticsResultDo tradeLogisticsResultDo;

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords2) {
        this.keywords = keywords2;
    }

    public TradeLogisticsResultDo getTradeLogisticsResultDo() {
        return this.tradeLogisticsResultDo;
    }

    public void setTradeLogisticsResultDo(TradeLogisticsResultDo tradeLogisticsResultDo2) {
        this.tradeLogisticsResultDo = tradeLogisticsResultDo2;
    }
}
