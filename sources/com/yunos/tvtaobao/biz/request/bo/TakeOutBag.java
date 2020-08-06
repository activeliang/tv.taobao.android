package com.yunos.tvtaobao.biz.request.bo;

import com.yunos.tvtaobao.biz.request.bo.ItemListBean;
import java.util.List;

public class TakeOutBag {
    public List<CartItemListBean> __cartItemList;
    public int agentFee;
    public String buttonText;
    public boolean canBuy;
    public List<CartItemListBean> cartItemList;
    public int deliverAmount;
    public boolean inDeliverRange;
    public int outStoreId;
    public int packingFee;
    public String payType;
    public int storeId;
    public TipsBean tips;
    public String title;
    public int totalPrice;
    public int totalPromotionPrice;

    public static class TipsBean {
        public String categoryId;
        public String secondShowText;
        public String secondUrl;
        public String showText;
        public int type;
    }

    public static class CartItemListBean {
        public boolean __isCompare;
        public int amount;
        public long cartId;
        public int checkoutMode;
        public long createTime;
        public boolean isPackingFee;
        public String itemId;
        public String limitQuantity;
        public String outItemId;
        public String packingFee;
        public String pic;
        public String priceDesc;
        public int quantity;
        public int reducePrice;
        public long skuId;
        public String skuName;
        public List<SkuPropertiesBean> skuProperties;
        public String soldMode;
        public String title;
        public int totalPrice;
        public int totalPromotionPrice;
        public int type;
        public int unitPrice;
        public boolean valid;

        public static class SkuPropertiesBean {
            public String name;
            public String value;
        }

        public enum Status {
            normal,
            edit,
            outStock,
            rest
        }

        public ItemListBean.Status getGoodStatus() {
            if (this.quantity <= 0) {
                return ItemListBean.Status.outStock;
            }
            if (this.amount > 0) {
                return ItemListBean.Status.edit;
            }
            return ItemListBean.Status.normal;
        }
    }
}
