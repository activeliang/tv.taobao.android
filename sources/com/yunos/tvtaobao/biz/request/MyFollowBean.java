package com.yunos.tvtaobao.biz.request;

import java.util.List;

public class MyFollowBean {
    private String cursor;
    private String followCount;
    private List<ListBean> list;

    public String getCursor() {
        return this.cursor;
    }

    public void setCursor(String cursor2) {
        this.cursor = cursor2;
    }

    public String getFollowCount() {
        return this.followCount;
    }

    public void setFollowCount(String followCount2) {
        this.followCount = followCount2;
    }

    public List<ListBean> getList() {
        return this.list;
    }

    public void setList(List<ListBean> list2) {
        this.list = list2;
    }

    public static class ListBean {
        private String acceptDynamic;
        private String certTitle;
        private List<FeedsBean> feeds;
        private List<String> roleSet;
        private String shopId;
        private String specialFollow;
        private String tag;
        private List<TagListBean> tagList;
        private String type;
        private String userId;
        private String userLogo;
        private String userNick;
        private String userUrl;

        public String getAcceptDynamic() {
            return this.acceptDynamic;
        }

        public void setAcceptDynamic(String acceptDynamic2) {
            this.acceptDynamic = acceptDynamic2;
        }

        public String getCertTitle() {
            return this.certTitle;
        }

        public void setCertTitle(String certTitle2) {
            this.certTitle = certTitle2;
        }

        public String getShopId() {
            return this.shopId;
        }

        public void setShopId(String shopId2) {
            this.shopId = shopId2;
        }

        public String getSpecialFollow() {
            return this.specialFollow;
        }

        public void setSpecialFollow(String specialFollow2) {
            this.specialFollow = specialFollow2;
        }

        public String getTag() {
            return this.tag;
        }

        public void setTag(String tag2) {
            this.tag = tag2;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type2) {
            this.type = type2;
        }

        public String getUserId() {
            return this.userId;
        }

        public void setUserId(String userId2) {
            this.userId = userId2;
        }

        public String getUserLogo() {
            return this.userLogo;
        }

        public void setUserLogo(String userLogo2) {
            this.userLogo = userLogo2;
        }

        public String getUserNick() {
            return this.userNick;
        }

        public void setUserNick(String userNick2) {
            this.userNick = userNick2;
        }

        public String getUserUrl() {
            return this.userUrl;
        }

        public void setUserUrl(String userUrl2) {
            this.userUrl = userUrl2;
        }

        public List<FeedsBean> getFeeds() {
            return this.feeds;
        }

        public void setFeeds(List<FeedsBean> feeds2) {
            this.feeds = feeds2;
        }

        public List<String> getRoleSet() {
            return this.roleSet;
        }

        public void setRoleSet(List<String> roleSet2) {
            this.roleSet = roleSet2;
        }

        public List<TagListBean> getTagList() {
            return this.tagList;
        }

        public void setTagList(List<TagListBean> tagList2) {
            this.tagList = tagList2;
        }

        public static class FeedsBean {
            private String accountId;
            private String bizTxt;
            private String bizType;
            private String detailUrl;
            private String elementsLength;
            private String id;
            private List<String> picUrls;
            private String publishTime;
            private String publishTimeText;
            private String showText;

            public String getAccountId() {
                return this.accountId;
            }

            public void setAccountId(String accountId2) {
                this.accountId = accountId2;
            }

            public String getBizTxt() {
                return this.bizTxt;
            }

            public void setBizTxt(String bizTxt2) {
                this.bizTxt = bizTxt2;
            }

            public String getBizType() {
                return this.bizType;
            }

            public void setBizType(String bizType2) {
                this.bizType = bizType2;
            }

            public String getDetailUrl() {
                return this.detailUrl;
            }

            public void setDetailUrl(String detailUrl2) {
                this.detailUrl = detailUrl2;
            }

            public String getElementsLength() {
                return this.elementsLength;
            }

            public void setElementsLength(String elementsLength2) {
                this.elementsLength = elementsLength2;
            }

            public String getId() {
                return this.id;
            }

            public void setId(String id2) {
                this.id = id2;
            }

            public String getPublishTime() {
                return this.publishTime;
            }

            public void setPublishTime(String publishTime2) {
                this.publishTime = publishTime2;
            }

            public String getPublishTimeText() {
                return this.publishTimeText;
            }

            public void setPublishTimeText(String publishTimeText2) {
                this.publishTimeText = publishTimeText2;
            }

            public String getShowText() {
                return this.showText;
            }

            public void setShowText(String showText2) {
                this.showText = showText2;
            }

            public List<String> getPicUrls() {
                return this.picUrls;
            }

            public void setPicUrls(List<String> picUrls2) {
                this.picUrls = picUrls2;
            }
        }

        public static class TagListBean {
            private String height;
            private String url;
            private String width;

            public String getHeight() {
                return this.height;
            }

            public void setHeight(String height2) {
                this.height = height2;
            }

            public String getUrl() {
                return this.url;
            }

            public void setUrl(String url2) {
                this.url = url2;
            }

            public String getWidth() {
                return this.width;
            }

            public void setWidth(String width2) {
                this.width = width2;
            }
        }
    }
}
