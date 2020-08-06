package com.yunos.tv.tao.speech.client.domain.result.multisearch.trade;

import com.yunos.tv.tao.speech.client.domain.result.multisearch.BaseResultVO;
import java.io.Serializable;

public class BillResultVO extends BaseResultVO implements Serializable {
    private String beginTime;
    private String endTime;
    private String timeText;
    private TradeBillResultDO tradeBillResultDO;

    public String getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(String beginTime2) {
        this.beginTime = beginTime2;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime2) {
        this.endTime = endTime2;
    }

    public String getTimeText() {
        return this.timeText;
    }

    public void setTimeText(String timeText2) {
        this.timeText = timeText2;
    }

    public TradeBillResultDO getTradeBillResultDO() {
        return this.tradeBillResultDO;
    }

    public void setTradeBillResultDO(TradeBillResultDO tradeBillResultDO2) {
        this.tradeBillResultDO = tradeBillResultDO2;
    }
}
