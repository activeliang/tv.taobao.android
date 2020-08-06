package com.taobao.wireless.trade.mbuy.sdk.co;

import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.wireless.trade.mcart.sdk.co.biz.PromotionComponent;
import java.util.HashMap;
import java.util.Map;

public enum ComponentTag {
    UNKNOWN(0, "unknown"),
    ROOT(1, "confirmOrder"),
    ADDRESS(2, "address"),
    ORDER_GROUP(3, "orderGroup"),
    ORDER_BOND(4, "orderBond"),
    ORDER(5, PromotionComponent.ORDER_TYPE),
    ORDER_INFO(6, "orderInfo"),
    ORDER_PAY(7, "orderPay"),
    ITEM(8, "item"),
    ITEM_INFO(9, "itemInfo"),
    QUANTITY(10, "quantity"),
    ITEM_PAY(11, "itemPay"),
    DELIVERY_METHOD(12, "deliveryMethod"),
    SHIP_DATE_PICKER(13, "shipDatePicker"),
    MEMO(14, "memo"),
    INVALID_GROUP(15, "invalidGroup"),
    REAL_PAY(16, "realPay"),
    INVOICE(17, "invoice"),
    PROMOTION(18, "promotion"),
    TBGOLD(19, "tbGold"),
    TMALL_POINT(20, "tmallPoint"),
    COUPON_CARD(21, "couponCard"),
    AGENCY_PAY(22, "agencyPay"),
    SERVICE(23, "service"),
    ANONYMOUS(24, "anonymous"),
    TERMS(25, "terms"),
    SUBMIT_ORDER(26, "submitOrder"),
    ACTIVITY(27, TuwenConstants.MODEL_LIST_KEY.ACTIVITY),
    INSTALLMENT(28, "installmentPurchase"),
    INSTALLMENT_TOGGLE(29, "installmentToggle"),
    INSTALLMENT_PICKER(30, "installmentPicker"),
    SERVICE_ADDRESS(31, "serviceAddress"),
    TAX_INFO(32, "taxInfo"),
    COUPON(33, "coupon"),
    SHOP_PROMOTION_DETAIL(34, "shoppromotiondetail"),
    TOWN_REMIND(35, "townRemind"),
    CARD_DECK(36, "cardDeck"),
    PRE_SELL_ADDRESS(37, "preSellAddress"),
    FINAL_PAY(38, "finalPay"),
    AGG_ITEM_INFO(39, "aggItemInfo"),
    AGG_ITEM_PAY(40, "aggItemPay"),
    MEMBERCARD(41, "tmallClubCard");
    
    private static Map<String, ComponentTag> m;
    public String desc;
    public int index;

    static {
        int i;
        m = new HashMap();
        for (ComponentTag tag : values()) {
            m.put(tag.desc, tag);
        }
    }

    public static ComponentTag getComponentTagByDesc(String desc2) {
        ComponentTag tag = m.get(desc2);
        return tag != null ? tag : UNKNOWN;
    }

    public static int size() {
        return values().length;
    }

    private ComponentTag(int index2, String desc2) {
        this.index = index2;
        this.desc = desc2;
    }

    public String toString() {
        return this.desc;
    }
}
