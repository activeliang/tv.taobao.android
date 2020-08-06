package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class GuessLikeGoodsBean {
    private String currentPage;
    private String currentTime;
    private String empty;
    private ResultVO result;

    public String getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(String currentPage2) {
        this.currentPage = currentPage2;
    }

    public String getCurrentTime() {
        return this.currentTime;
    }

    public void setCurrentTime(String currentTime2) {
        this.currentTime = currentTime2;
    }

    public String getEmpty() {
        return this.empty;
    }

    public void setEmpty(String empty2) {
        this.empty = empty2;
    }

    public ResultVO getResult() {
        return this.result;
    }

    public void setResult(ResultVO result2) {
        this.result = result2;
    }

    public static class ResultVO {
        private BrandVO brand;
        private List<RecommendVO> recommedResult;

        public BrandVO getBrand() {
            return this.brand;
        }

        public void setBrand(BrandVO brand2) {
            this.brand = brand2;
        }

        public List<RecommendVO> getRecommedResult() {
            return this.recommedResult;
        }

        public void setRecommedResult(List<RecommendVO> recommedResult2) {
            this.recommedResult = recommedResult2;
        }

        private class BrandVO {
            private String tips;
            private String title;

            private BrandVO() {
            }

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title2) {
                this.title = title2;
            }

            public String getTips() {
                return this.tips;
            }

            public void setTips(String tips2) {
                this.tips = tips2;
            }
        }

        public static class RecommendVO {
            private String bizName;
            private GuessLikeFieldsVO fields;
            private RebateBo rebateBo;
            private DynamicRecommend recommend;
            private String type;

            public String toString() {
                return "RecommendVO{bizName='" + this.bizName + '\'' + ", type='" + this.type + '\'' + ", fields=" + this.fields + '}';
            }

            public RebateBo getRebateBo() {
                return this.rebateBo;
            }

            public void setRebateBo(RebateBo rebateBo2) {
                this.rebateBo = rebateBo2;
            }

            public String getBizName() {
                return this.bizName;
            }

            public void setBizName(String bizName2) {
                this.bizName = bizName2;
            }

            public String getType() {
                return this.type;
            }

            public void setType(String type2) {
                this.type = type2;
            }

            public GuessLikeFieldsVO getFields() {
                return this.fields;
            }

            public void setFields(GuessLikeFieldsVO fields2) {
                this.fields = fields2;
            }

            public DynamicRecommend getRecommend() {
                return this.recommend;
            }

            public void setRecommend(DynamicRecommend recommend2) {
                this.recommend = recommend2;
            }
        }
    }

    public String toString() {
        return "GuessLikeGoodsBean{currentPage='" + this.currentPage + '\'' + ", currentTime='" + this.currentTime + '\'' + ", empty='" + this.empty + '\'' + ", result=" + this.result + '}';
    }
}
