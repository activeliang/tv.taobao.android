package com.yunos.tvtaobao.biz.request.bo;

public class HasActivieBean {
    private Double11ShopCartBean double11_shop_cart;
    private ShopCartFlagBean shop_cart_flag;
    private UpgradeBean upgrade;

    public UpgradeBean getUpgrade() {
        return this.upgrade;
    }

    public void setUpgrade(UpgradeBean upgrade2) {
        this.upgrade = upgrade2;
    }

    public Double11ShopCartBean getDouble11_shop_cart() {
        return this.double11_shop_cart;
    }

    public void setDouble11_shop_cart(Double11ShopCartBean double11_shop_cart2) {
        this.double11_shop_cart = double11_shop_cart2;
    }

    public ShopCartFlagBean getShop_cart_flag() {
        return this.shop_cart_flag;
    }

    public void setShop_cart_flag(ShopCartFlagBean shop_cart_flag2) {
        this.shop_cart_flag = shop_cart_flag2;
    }

    public static class UpgradeBean {
        private String background;
        private String content;
        private String title;

        public String getBackground() {
            return this.background;
        }

        public void setBackground(String background2) {
            this.background = background2;
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String content2) {
            this.content = content2;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }
    }

    public static class Double11ShopCartBean {
        private boolean bool_shop_cart_merge_orders;
        private String date_only_create_order_end;
        private String date_only_create_order_start;

        public String getDate_only_create_order_start() {
            return this.date_only_create_order_start;
        }

        public void setDate_only_create_order_start(String date_only_create_order_start2) {
            this.date_only_create_order_start = date_only_create_order_start2;
        }

        public String getDate_only_create_order_end() {
            return this.date_only_create_order_end;
        }

        public void setDate_only_create_order_end(String date_only_create_order_end2) {
            this.date_only_create_order_end = date_only_create_order_end2;
        }

        public boolean isBool_shop_cart_merge_orders() {
            return this.bool_shop_cart_merge_orders;
        }

        public void setBool_shop_cart_merge_orders(boolean bool_shop_cart_merge_orders2) {
            this.bool_shop_cart_merge_orders = bool_shop_cart_merge_orders2;
        }
    }

    public static class ShopCartFlagBean {
        private String detail_icon;
        private boolean is_acting;
        private String shop_cat_icon;
        private String side_bar_icon;

        public boolean isIs_acting() {
            return this.is_acting;
        }

        public void setIs_acting(boolean is_acting2) {
            this.is_acting = is_acting2;
        }

        public String getDetail_icon() {
            return this.detail_icon;
        }

        public void setDetail_icon(String detail_icon2) {
            this.detail_icon = detail_icon2;
        }

        public String getShop_cat_icon() {
            return this.shop_cat_icon;
        }

        public void setShop_cat_icon(String shop_cat_icon2) {
            this.shop_cat_icon = shop_cat_icon2;
        }

        public String getSide_bar_icon() {
            return this.side_bar_icon;
        }

        public void setSide_bar_icon(String side_bar_icon2) {
            this.side_bar_icon = side_bar_icon2;
        }
    }
}
