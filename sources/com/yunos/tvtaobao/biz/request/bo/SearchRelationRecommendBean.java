package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class SearchRelationRecommendBean {
    private String pvid;
    private List<Result> result;
    private String scm;
    private String suggest_rn;
    private List<String> templates;
    private String tpp_trace;
    private int version;

    public void setResult(List<Result> result2) {
        this.result = result2;
    }

    public List<Result> getResult() {
        return this.result;
    }

    public void setSuggest_rn(String suggest_rn2) {
        this.suggest_rn = suggest_rn2;
    }

    public String getSuggest_rn() {
        return this.suggest_rn;
    }

    public void setPvid(String pvid2) {
        this.pvid = pvid2;
    }

    public String getPvid() {
        return this.pvid;
    }

    public void setTemplates(List<String> templates2) {
        this.templates = templates2;
    }

    public List<String> getTemplates() {
        return this.templates;
    }

    public void setScm(String scm2) {
        this.scm = scm2;
    }

    public String getScm() {
        return this.scm;
    }

    public void setVersion(int version2) {
        this.version = version2;
    }

    public int getVersion() {
        return this.version;
    }

    public void setTpp_trace(String tpp_trace2) {
        this.tpp_trace = tpp_trace2;
    }

    public String getTpp_trace() {
        return this.tpp_trace;
    }

    public class Result {
        private SearchRelationRecommendItemBean data;
        private String tItemType;
        private String traceBizType;
        private String traceTmplType;

        public Result() {
        }

        public void setData(SearchRelationRecommendItemBean data2) {
            this.data = data2;
        }

        public SearchRelationRecommendItemBean getData() {
            return this.data;
        }

        public void setTraceBizType(String traceBizType2) {
            this.traceBizType = traceBizType2;
        }

        public String getTraceBizType() {
            return this.traceBizType;
        }

        public void setTItemType(String tItemType2) {
            this.tItemType = tItemType2;
        }

        public String getTItemType() {
            return this.tItemType;
        }

        public void setTraceTmplType(String traceTmplType2) {
            this.traceTmplType = traceTmplType2;
        }

        public String getTraceTmplType() {
            return this.traceTmplType;
        }
    }
}
