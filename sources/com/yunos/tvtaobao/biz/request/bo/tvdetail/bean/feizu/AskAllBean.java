package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.util.List;

public class AskAllBean {
    private DataBeanXXXXX data;
    private String tag;

    public DataBeanXXXXX getData() {
        return this.data;
    }

    public void setData(DataBeanXXXXX data2) {
        this.data = data2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public static class DataBeanXXXXX {
        private JumpInfoBeanX jumpInfo;
        private List<QuestionsBean> questions;
        private String size;

        public JumpInfoBeanX getJumpInfo() {
            return this.jumpInfo;
        }

        public void setJumpInfo(JumpInfoBeanX jumpInfo2) {
            this.jumpInfo = jumpInfo2;
        }

        public String getSize() {
            return this.size;
        }

        public void setSize(String size2) {
            this.size = size2;
        }

        public List<QuestionsBean> getQuestions() {
            return this.questions;
        }

        public void setQuestions(List<QuestionsBean> questions2) {
            this.questions = questions2;
        }

        public static class JumpInfoBeanX {
            private String jumpH5Url;
            private String jumpNative;
            private String showBuyButton;

            public String getJumpH5Url() {
                return this.jumpH5Url;
            }

            public void setJumpH5Url(String jumpH5Url2) {
                this.jumpH5Url = jumpH5Url2;
            }

            public String getJumpNative() {
                return this.jumpNative;
            }

            public void setJumpNative(String jumpNative2) {
                this.jumpNative = jumpNative2;
            }

            public String getShowBuyButton() {
                return this.showBuyButton;
            }

            public void setShowBuyButton(String showBuyButton2) {
                this.showBuyButton = showBuyButton2;
            }
        }

        public static class QuestionsBean {
            private String count;
            private String question;

            public String getCount() {
                return this.count;
            }

            public void setCount(String count2) {
                this.count = count2;
            }

            public String getQuestion() {
                return this.question;
            }

            public void setQuestion(String question2) {
                this.question = question2;
            }
        }
    }
}
