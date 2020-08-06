package com.taobao.wireless.trade.mcart.sdk.co;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mcart.sdk.co.base.LabelComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.AllItemComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.BannerComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.BundleComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.FoldingBarComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.FooterComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.GroupComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ItemComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.PromotionBarComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.PromotionComponent;
import com.taobao.wireless.trade.mcart.sdk.co.biz.ShopComponent;
import com.taobao.wireless.trade.mcart.sdk.constant.CartFrom;

public class ComponentFactory {
    public static Component make(JSONObject data, CartFrom cartFrom) {
        String tagDesc;
        ComponentTag tag;
        if (data == null || (tagDesc = data.getString("tag")) == null || (tag = ComponentTag.getComponentTagByDesc(tagDesc)) == null) {
            return null;
        }
        switch (tag) {
            case BANNER:
                return new BannerComponent(data, cartFrom);
            case ALL_ITEM:
                return new AllItemComponent(data, cartFrom);
            case BUNDLE:
                return new BundleComponent(data, cartFrom);
            case PROMOTION:
                return new PromotionComponent(data, cartFrom);
            case SHOP:
                return new ShopComponent(data, cartFrom);
            case GROUP:
                return new GroupComponent(data, cartFrom);
            case ITEM:
                return new ItemComponent(data, cartFrom);
            case FOOTER:
                return new FooterComponent(data, cartFrom);
            case LABEL:
                return new LabelComponent(data, cartFrom);
            case PROMOTIONBAR:
                return new PromotionBarComponent(data, cartFrom);
            case FOLDINGBAR:
                return new FoldingBarComponent(data, cartFrom);
            default:
                return null;
        }
    }
}
