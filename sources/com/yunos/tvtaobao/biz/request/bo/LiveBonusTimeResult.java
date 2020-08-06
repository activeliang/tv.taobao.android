package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class LiveBonusTimeResult {
    private String currentTime;
    private List<LiveBonusTimeItem> list;

    public String getCurrentTime() {
        return this.currentTime;
    }

    public void setCurrentTime(String currentTime2) {
        this.currentTime = currentTime2;
    }

    public List<LiveBonusTimeItem> getList() {
        return this.list;
    }

    public void setList(List<LiveBonusTimeItem> list2) {
        this.list = list2;
    }
}
