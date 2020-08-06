package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class SearchRelationRecommendItemBean {
    private List<DataResult> result;

    public void setResult(List<DataResult> result2) {
        this.result = result2;
    }

    public List<DataResult> getResult() {
        return this.result;
    }

    public class DataResult {
        private String searchtext;
        private String showtext;

        public DataResult() {
        }

        public void setSearchtext(String searchtext2) {
            this.searchtext = searchtext2;
        }

        public String getSearchtext() {
            return this.searchtext;
        }

        public void setShowtext(String showtext2) {
            this.showtext = showtext2;
        }

        public String getShowtext() {
            return this.showtext;
        }
    }
}
