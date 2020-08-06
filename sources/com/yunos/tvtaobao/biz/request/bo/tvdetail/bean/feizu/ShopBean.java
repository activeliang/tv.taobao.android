package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

import java.util.List;

public class ShopBean {
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
        private JumpInfoBean jumpInfo;
        private String sellerId;
        private String sellerName;
        private List<ShopEvaluationBean> shopEvaluation;
        private String shopId;
        private String shopName;
        private String shopPic;
        private String shopTypePic;

        public JumpInfoBean getJumpInfo() {
            return this.jumpInfo;
        }

        public void setJumpInfo(JumpInfoBean jumpInfo2) {
            this.jumpInfo = jumpInfo2;
        }

        public String getSellerId() {
            return this.sellerId;
        }

        public void setSellerId(String sellerId2) {
            this.sellerId = sellerId2;
        }

        public String getSellerName() {
            return this.sellerName;
        }

        public void setSellerName(String sellerName2) {
            this.sellerName = sellerName2;
        }

        public String getShopId() {
            return this.shopId;
        }

        public void setShopId(String shopId2) {
            this.shopId = shopId2;
        }

        public String getShopName() {
            return this.shopName;
        }

        public void setShopName(String shopName2) {
            this.shopName = shopName2;
        }

        public String getShopPic() {
            return this.shopPic;
        }

        public void setShopPic(String shopPic2) {
            this.shopPic = shopPic2;
        }

        public String getShopTypePic() {
            return this.shopTypePic;
        }

        public void setShopTypePic(String shopTypePic2) {
            this.shopTypePic = shopTypePic2;
        }

        public List<ShopEvaluationBean> getShopEvaluation() {
            return this.shopEvaluation;
        }

        public void setShopEvaluation(List<ShopEvaluationBean> shopEvaluation2) {
            this.shopEvaluation = shopEvaluation2;
        }

        public static class JumpInfoBean {
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

        public static class ShopEvaluationBean {
            private String highGap;
            private String highGapBgColor;
            private String highGapText;
            private String highGapTextColor;
            private String score;
            private String title;

            public String getHighGap() {
                return this.highGap;
            }

            public void setHighGap(String highGap2) {
                this.highGap = highGap2;
            }

            public String getHighGapBgColor() {
                return this.highGapBgColor;
            }

            public void setHighGapBgColor(String highGapBgColor2) {
                this.highGapBgColor = highGapBgColor2;
            }

            public String getHighGapText() {
                return this.highGapText;
            }

            public void setHighGapText(String highGapText2) {
                this.highGapText = highGapText2;
            }

            public String getHighGapTextColor() {
                return this.highGapTextColor;
            }

            public void setHighGapTextColor(String highGapTextColor2) {
                this.highGapTextColor = highGapTextColor2;
            }

            public String getScore() {
                return this.score;
            }

            public void setScore(String score2) {
                this.score = score2;
            }

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title2) {
                this.title = title2;
            }
        }
    }
}
