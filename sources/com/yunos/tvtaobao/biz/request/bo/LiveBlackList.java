package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class LiveBlackList {
    private List<String> taobaoLiveAccountIdList;

    public List<String> getTaobaoLiveAccountIdList() {
        return this.taobaoLiveAccountIdList;
    }

    public void setTaobaoLiveAccountIdList(List<String> taobaoLiveAccountIdList2) {
        this.taobaoLiveAccountIdList = taobaoLiveAccountIdList2;
    }
}
