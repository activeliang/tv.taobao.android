package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao;

import java.util.List;

public class VerticalBean {
    private AskAllBean askAll;

    public AskAllBean getAskAll() {
        return this.askAll;
    }

    public void setAskAll(AskAllBean askAll2) {
        this.askAll = askAll2;
    }

    public static class AskAllBean {
        private String askIcon;
        private String askText;
        private String linkUrl;
        private List<Model4XListBean> model4XList;
        private List<ModelListBean> modelList;
        private String questNum;
        private String showNum;
        private String title;

        public String getAskText() {
            return this.askText;
        }

        public void setAskText(String askText2) {
            this.askText = askText2;
        }

        public String getAskIcon() {
            return this.askIcon;
        }

        public void setAskIcon(String askIcon2) {
            this.askIcon = askIcon2;
        }

        public String getLinkUrl() {
            return this.linkUrl;
        }

        public void setLinkUrl(String linkUrl2) {
            this.linkUrl = linkUrl2;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getQuestNum() {
            return this.questNum;
        }

        public void setQuestNum(String questNum2) {
            this.questNum = questNum2;
        }

        public String getShowNum() {
            return this.showNum;
        }

        public void setShowNum(String showNum2) {
            this.showNum = showNum2;
        }

        public List<ModelListBean> getModelList() {
            return this.modelList;
        }

        public void setModelList(List<ModelListBean> modelList2) {
            this.modelList = modelList2;
        }

        public List<Model4XListBean> getModel4XList() {
            return this.model4XList;
        }

        public void setModel4XList(List<Model4XListBean> model4XList2) {
            this.model4XList = model4XList2;
        }

        public static class ModelListBean {
            private String answerCountText;
            private String askText;

            public String getAskText() {
                return this.askText;
            }

            public void setAskText(String askText2) {
                this.askText = askText2;
            }

            public String getAnswerCountText() {
                return this.answerCountText;
            }

            public void setAnswerCountText(String answerCountText2) {
                this.answerCountText = answerCountText2;
            }
        }

        public static class Model4XListBean {
            private String answerCountText;
            private String askIcon;
            private String askText;
            private String askTextColor;

            public String getAskText() {
                return this.askText;
            }

            public void setAskText(String askText2) {
                this.askText = askText2;
            }

            public String getAnswerCountText() {
                return this.answerCountText;
            }

            public void setAnswerCountText(String answerCountText2) {
                this.answerCountText = answerCountText2;
            }

            public String getAskIcon() {
                return this.askIcon;
            }

            public void setAskIcon(String askIcon2) {
                this.askIcon = askIcon2;
            }

            public String getAskTextColor() {
                return this.askTextColor;
            }

            public void setAskTextColor(String askTextColor2) {
                this.askTextColor = askTextColor2;
            }
        }
    }
}
