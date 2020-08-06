package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu;

public class BuyBannerBean {
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
        private String buttonShowState;
        private String buyButtonDesc;
        private String buyButtonStyle;
        private String buyButtonSupport;
        private BuyJumpInfoBean buyJumpInfo;
        private String carBizType;
        private String carButtonStyle;
        private String carButtonSupport;
        private String carDesc;
        private CarItemJumpInfoBean carItemJumpInfo;
        private String carType;
        private String pageStaySecond;
        private String saveIcon;
        private SellerContactBean sellerContact;
        private String sellerId;
        private String sellerName;
        private String shopIcon;
        private ShopJumpInfoBean shopJumpInfo;

        public String getButtonShowState() {
            return this.buttonShowState;
        }

        public void setButtonShowState(String buttonShowState2) {
            this.buttonShowState = buttonShowState2;
        }

        public String getBuyButtonDesc() {
            return this.buyButtonDesc;
        }

        public void setBuyButtonDesc(String buyButtonDesc2) {
            this.buyButtonDesc = buyButtonDesc2;
        }

        public String getBuyButtonStyle() {
            return this.buyButtonStyle;
        }

        public void setBuyButtonStyle(String buyButtonStyle2) {
            this.buyButtonStyle = buyButtonStyle2;
        }

        public String getBuyButtonSupport() {
            return this.buyButtonSupport;
        }

        public void setBuyButtonSupport(String buyButtonSupport2) {
            this.buyButtonSupport = buyButtonSupport2;
        }

        public BuyJumpInfoBean getBuyJumpInfo() {
            return this.buyJumpInfo;
        }

        public void setBuyJumpInfo(BuyJumpInfoBean buyJumpInfo2) {
            this.buyJumpInfo = buyJumpInfo2;
        }

        public String getCarBizType() {
            return this.carBizType;
        }

        public void setCarBizType(String carBizType2) {
            this.carBizType = carBizType2;
        }

        public String getCarButtonStyle() {
            return this.carButtonStyle;
        }

        public void setCarButtonStyle(String carButtonStyle2) {
            this.carButtonStyle = carButtonStyle2;
        }

        public String getCarButtonSupport() {
            return this.carButtonSupport;
        }

        public void setCarButtonSupport(String carButtonSupport2) {
            this.carButtonSupport = carButtonSupport2;
        }

        public String getCarDesc() {
            return this.carDesc;
        }

        public void setCarDesc(String carDesc2) {
            this.carDesc = carDesc2;
        }

        public CarItemJumpInfoBean getCarItemJumpInfo() {
            return this.carItemJumpInfo;
        }

        public void setCarItemJumpInfo(CarItemJumpInfoBean carItemJumpInfo2) {
            this.carItemJumpInfo = carItemJumpInfo2;
        }

        public String getCarType() {
            return this.carType;
        }

        public void setCarType(String carType2) {
            this.carType = carType2;
        }

        public String getPageStaySecond() {
            return this.pageStaySecond;
        }

        public void setPageStaySecond(String pageStaySecond2) {
            this.pageStaySecond = pageStaySecond2;
        }

        public String getSaveIcon() {
            return this.saveIcon;
        }

        public void setSaveIcon(String saveIcon2) {
            this.saveIcon = saveIcon2;
        }

        public SellerContactBean getSellerContact() {
            return this.sellerContact;
        }

        public void setSellerContact(SellerContactBean sellerContact2) {
            this.sellerContact = sellerContact2;
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

        public String getShopIcon() {
            return this.shopIcon;
        }

        public void setShopIcon(String shopIcon2) {
            this.shopIcon = shopIcon2;
        }

        public ShopJumpInfoBean getShopJumpInfo() {
            return this.shopJumpInfo;
        }

        public void setShopJumpInfo(ShopJumpInfoBean shopJumpInfo2) {
            this.shopJumpInfo = shopJumpInfo2;
        }

        public static class BuyJumpInfoBean {
            private String buyUrlType;
            private String jumpH5Url;
            private String jumpNative;
            private String newBuy;
            private String newJumpH5Url;
            private String showBuyButton;

            public String getBuyUrlType() {
                return this.buyUrlType;
            }

            public void setBuyUrlType(String buyUrlType2) {
                this.buyUrlType = buyUrlType2;
            }

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

            public String getNewBuy() {
                return this.newBuy;
            }

            public void setNewBuy(String newBuy2) {
                this.newBuy = newBuy2;
            }

            public String getNewJumpH5Url() {
                return this.newJumpH5Url;
            }

            public void setNewJumpH5Url(String newJumpH5Url2) {
                this.newJumpH5Url = newJumpH5Url2;
            }

            public String getShowBuyButton() {
                return this.showBuyButton;
            }

            public void setShowBuyButton(String showBuyButton2) {
                this.showBuyButton = showBuyButton2;
            }
        }

        public static class CarItemJumpInfoBean {
            private String jumpH5Url;
            private String showBuyButton;

            public String getJumpH5Url() {
                return this.jumpH5Url;
            }

            public void setJumpH5Url(String jumpH5Url2) {
                this.jumpH5Url = jumpH5Url2;
            }

            public String getShowBuyButton() {
                return this.showBuyButton;
            }

            public void setShowBuyButton(String showBuyButton2) {
                this.showBuyButton = showBuyButton2;
            }
        }

        public static class SellerContactBean {
            private String itemPic;
            private String itemTitle;
            private String itemUrl;
            private String sellerId;
            private String sellerNick;

            public String getItemPic() {
                return this.itemPic;
            }

            public void setItemPic(String itemPic2) {
                this.itemPic = itemPic2;
            }

            public String getItemTitle() {
                return this.itemTitle;
            }

            public void setItemTitle(String itemTitle2) {
                this.itemTitle = itemTitle2;
            }

            public String getItemUrl() {
                return this.itemUrl;
            }

            public void setItemUrl(String itemUrl2) {
                this.itemUrl = itemUrl2;
            }

            public String getSellerId() {
                return this.sellerId;
            }

            public void setSellerId(String sellerId2) {
                this.sellerId = sellerId2;
            }

            public String getSellerNick() {
                return this.sellerNick;
            }

            public void setSellerNick(String sellerNick2) {
                this.sellerNick = sellerNick2;
            }
        }

        public static class ShopJumpInfoBean {
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
    }
}
