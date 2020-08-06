package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class ElemeRecommandReturnValueBo {
    private String hint;
    private List<ElemeRecommendAccount> recommendAccountList;

    public List<ElemeRecommendAccount> getRecommendAccountList() {
        return this.recommendAccountList;
    }

    public void setRecommendAccountList(List<ElemeRecommendAccount> recommendAccountList2) {
        this.recommendAccountList = recommendAccountList2;
    }

    public String getHint() {
        return this.hint;
    }

    public void setHint(String hint2) {
        this.hint = hint2;
    }
}
