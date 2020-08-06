package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.util.List;

public class DetailCoreBean {
    private List<String> children;
    private DataBeanXXX data;
    private String tag;

    public DataBeanXXX getData() {
        return this.data;
    }

    public void setData(DataBeanXXX data2) {
        this.data = data2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public List<String> getChildren() {
        return this.children;
    }

    public void setChildren(List<String> children2) {
        this.children = children2;
    }

    public static class DataBeanXXX {
        private String categoryId;
        private ShareInfoBean shareInfo;

        public String getCategoryId() {
            return this.categoryId;
        }

        public void setCategoryId(String categoryId2) {
            this.categoryId = categoryId2;
        }

        public ShareInfoBean getShareInfo() {
            return this.shareInfo;
        }

        public void setShareInfo(ShareInfoBean shareInfo2) {
            this.shareInfo = shareInfo2;
        }

        public static class ShareInfoBean {
            private String image_url;
            private String text_context;
            private String title_context;
            private String url_content;

            public String getImage_url() {
                return this.image_url;
            }

            public void setImage_url(String image_url2) {
                this.image_url = image_url2;
            }

            public String getText_context() {
                return this.text_context;
            }

            public void setText_context(String text_context2) {
                this.text_context = text_context2;
            }

            public String getTitle_context() {
                return this.title_context;
            }

            public void setTitle_context(String title_context2) {
                this.title_context = title_context2;
            }

            public String getUrl_content() {
                return this.url_content;
            }

            public void setUrl_content(String url_content2) {
                this.url_content = url_content2;
            }
        }
    }
}
