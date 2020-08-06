package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.util.List;

public class FliggyShopRecomdBean {
    private DataBeanXXXXXXXXXXXXXX data;
    private String tag;

    public DataBeanXXXXXXXXXXXXXX getData() {
        return this.data;
    }

    public void setData(DataBeanXXXXXXXXXXXXXX data2) {
        this.data = data2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public static class DataBeanXXXXXXXXXXXXXX {
        private List<ItemLstBean> itemLst;
        private String title;
        private TripJumpInfoBean tripJumpInfo;

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public TripJumpInfoBean getTripJumpInfo() {
            return this.tripJumpInfo;
        }

        public void setTripJumpInfo(TripJumpInfoBean tripJumpInfo2) {
            this.tripJumpInfo = tripJumpInfo2;
        }

        public List<ItemLstBean> getItemLst() {
            return this.itemLst;
        }

        public void setItemLst(List<ItemLstBean> itemLst2) {
            this.itemLst = itemLst2;
        }

        public static class TripJumpInfoBean {
            private String jumpH5Url;
            private String jumpNative;

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
        }

        public static class ItemLstBean {
            private String categoryDesc;
            private String currPrice;
            private EventBeanX event;
            private String itemId;
            private String itemName;
            private String itemPic;
            private String sold;
            private String trackInfo;
            private TripJumpInfoBeanX tripJumpInfo;

            public String getCategoryDesc() {
                return this.categoryDesc;
            }

            public void setCategoryDesc(String categoryDesc2) {
                this.categoryDesc = categoryDesc2;
            }

            public String getCurrPrice() {
                return this.currPrice;
            }

            public void setCurrPrice(String currPrice2) {
                this.currPrice = currPrice2;
            }

            public EventBeanX getEvent() {
                return this.event;
            }

            public void setEvent(EventBeanX event2) {
                this.event = event2;
            }

            public String getItemId() {
                return this.itemId;
            }

            public void setItemId(String itemId2) {
                this.itemId = itemId2;
            }

            public String getItemName() {
                return this.itemName;
            }

            public void setItemName(String itemName2) {
                this.itemName = itemName2;
            }

            public String getItemPic() {
                return this.itemPic;
            }

            public void setItemPic(String itemPic2) {
                this.itemPic = itemPic2;
            }

            public String getSold() {
                return this.sold;
            }

            public void setSold(String sold2) {
                this.sold = sold2;
            }

            public String getTrackInfo() {
                return this.trackInfo;
            }

            public void setTrackInfo(String trackInfo2) {
                this.trackInfo = trackInfo2;
            }

            public TripJumpInfoBeanX getTripJumpInfo() {
                return this.tripJumpInfo;
            }

            public void setTripJumpInfo(TripJumpInfoBeanX tripJumpInfo2) {
                this.tripJumpInfo = tripJumpInfo2;
            }

            public static class EventBeanX {
                private List<OpenRecommendItemBean> openRecommendItem;

                public List<OpenRecommendItemBean> getOpenRecommendItem() {
                    return this.openRecommendItem;
                }

                public void setOpenRecommendItem(List<OpenRecommendItemBean> openRecommendItem2) {
                    this.openRecommendItem = openRecommendItem2;
                }

                public static class OpenRecommendItemBean {
                    private String key;
                    private ParamsBean params;

                    public String getKey() {
                        return this.key;
                    }

                    public void setKey(String key2) {
                        this.key = key2;
                    }

                    public ParamsBean getParams() {
                        return this.params;
                    }

                    public void setParams(ParamsBean params2) {
                        this.params = params2;
                    }

                    public static class ParamsBean {
                        private String trackName;
                        private String trackNamePre;
                        private TrackParamsBean trackParams;

                        public String getTrackName() {
                            return this.trackName;
                        }

                        public void setTrackName(String trackName2) {
                            this.trackName = trackName2;
                        }

                        public String getTrackNamePre() {
                            return this.trackNamePre;
                        }

                        public void setTrackNamePre(String trackNamePre2) {
                            this.trackNamePre = trackNamePre2;
                        }

                        public TrackParamsBean getTrackParams() {
                            return this.trackParams;
                        }

                        public void setTrackParams(TrackParamsBean trackParams2) {
                            this.trackParams = trackParams2;
                        }

                        public static class TrackParamsBean {
                            private String itemId;
                            private String spm;
                            private String trackInfo;

                            public String getSpm() {
                                return this.spm;
                            }

                            public void setSpm(String spm2) {
                                this.spm = spm2;
                            }

                            public String getTrackInfo() {
                                return this.trackInfo;
                            }

                            public void setTrackInfo(String trackInfo2) {
                                this.trackInfo = trackInfo2;
                            }

                            public String getItemId() {
                                return this.itemId;
                            }

                            public void setItemId(String itemId2) {
                                this.itemId = itemId2;
                            }
                        }
                    }
                }
            }

            public static class TripJumpInfoBeanX {
                private String jumpH5Url;
                private String jumpNative;

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
            }
        }
    }
}
