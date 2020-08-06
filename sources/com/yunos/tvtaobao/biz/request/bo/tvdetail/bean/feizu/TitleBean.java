package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

public class TitleBean {
    private DataBean data;
    private String tag;

    public DataBean getData() {
        return this.data;
    }

    public void setData(DataBean data2) {
        this.data = data2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public static class DataBean {
        private String itemTitle;
        private ShareInfoBeanX shareInfo;
        private String titleIcon;

        public String getItemTitle() {
            return this.itemTitle;
        }

        public void setItemTitle(String itemTitle2) {
            this.itemTitle = itemTitle2;
        }

        public ShareInfoBeanX getShareInfo() {
            return this.shareInfo;
        }

        public void setShareInfo(ShareInfoBeanX shareInfo2) {
            this.shareInfo = shareInfo2;
        }

        public String getTitleIcon() {
            return this.titleIcon;
        }

        public void setTitleIcon(String titleIcon2) {
            this.titleIcon = titleIcon2;
        }

        public static class ShareInfoBeanX {
            private String image_url;
            private String shareIconFont;
            private String text_context;
            private String title_context;
            private String url_content;

            public String getImage_url() {
                return this.image_url;
            }

            public void setImage_url(String image_url2) {
                this.image_url = image_url2;
            }

            public String getShareIconFont() {
                return this.shareIconFont;
            }

            public void setShareIconFont(String shareIconFont2) {
                this.shareIconFont = shareIconFont2;
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
