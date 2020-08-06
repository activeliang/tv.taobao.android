package com.taobao.wireless.trade.mbuy.sdk.co;

import com.alibaba.fastjson.JSONObject;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.BridgeComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.CardDeckComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.CascadeComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.DatePickerComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.DynamicComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.FloatTipsComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.InputComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.LabelComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.MultiSelectComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.RichSelectComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.SelectComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.TableComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.TipsComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.ToggleComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.basic.VerificationCodeComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.ActivityComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.AddressComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.CouponComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.DeliveryMethodComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.FinalPayComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.InstallmentComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.InstallmentPickerComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.InstallmentToggleComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.InvalidGroupComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.ItemComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.ItemInfoComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.ItemPayComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.MemberCardComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.OrderBondComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.OrderComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.OrderGroupComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.OrderInfoComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.OrderPayComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.PreSellAddressComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.QuantityComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.RealPayComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.RootComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.ServiceAddressComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.ShopPromotionDetailComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.SubmitOrderComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.TaxInfoComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.TermsComponent;
import com.taobao.wireless.trade.mbuy.sdk.co.biz.TownRemindComponent;
import com.taobao.wireless.trade.mbuy.sdk.engine.BuyEngine;
import com.taobao.wireless.trade.mbuy.sdk.engine.ExpandParseRule;

public class ComponentFactory {
    public static Component make(JSONObject data, BuyEngine engine) throws Exception {
        Component cmp;
        if (data == null) {
            return null;
        }
        ExpandParseRule expandParseRule = engine.getExpandParseRule();
        if (expandParseRule != null && (cmp = expandParseRule.expandComponent(data, engine)) != null) {
            return cmp;
        }
        String typeDesc = data.getString("type");
        String tagDesc = data.getString("tag");
        if (typeDesc == null || tagDesc == null) {
            return null;
        }
        switch (ComponentType.getComponentTypeByDesc(typeDesc)) {
            case CARDDECK:
                return new CardDeckComponent(data, engine);
            case DYNAMIC:
                return new DynamicComponent(data, engine);
            case LABEL:
                return new LabelComponent(data, engine);
            case INPUT:
                return new InputComponent(data, engine);
            case SELECT:
                return new SelectComponent(data, engine);
            case TOGGLE:
                return new ToggleComponent(data, engine);
            case MULTISELECT:
                return new MultiSelectComponent(data, engine);
            case TABLE:
                return new TableComponent(data, engine);
            case TIPS:
                return new TipsComponent(data, engine);
            case DATEPICKER:
                return new DatePickerComponent(data, engine);
            case CASCADE:
                return new CascadeComponent(data, engine);
            case BRIDGE:
                return new BridgeComponent(data, engine);
            case FLOATTIPS:
                return new FloatTipsComponent(data, engine);
            case VERIFICATION_CODE:
                return new VerificationCodeComponent(data, engine);
            case RICHSELECT:
                return new RichSelectComponent(data, engine);
            default:
                switch (ComponentTag.getComponentTagByDesc(tagDesc)) {
                    case ROOT:
                        return new RootComponent(data, engine);
                    case ADDRESS:
                        return new AddressComponent(data, engine);
                    case ORDER_GROUP:
                        return new OrderGroupComponent(data, engine);
                    case ORDER_BOND:
                        return new OrderBondComponent(data, engine);
                    case ORDER:
                        return new OrderComponent(data, engine);
                    case ORDER_INFO:
                        return new OrderInfoComponent(data, engine);
                    case ORDER_PAY:
                        return new OrderPayComponent(data, engine);
                    case ITEM:
                        return new ItemComponent(data, engine);
                    case ITEM_INFO:
                    case AGG_ITEM_INFO:
                        return new ItemInfoComponent(data, engine);
                    case QUANTITY:
                        return new QuantityComponent(data, engine);
                    case ITEM_PAY:
                    case AGG_ITEM_PAY:
                        return new ItemPayComponent(data, engine);
                    case DELIVERY_METHOD:
                        return new DeliveryMethodComponent(data, engine);
                    case INVALID_GROUP:
                        return new InvalidGroupComponent(data, engine);
                    case TERMS:
                        return new TermsComponent(data, engine);
                    case REAL_PAY:
                        return new RealPayComponent(data, engine);
                    case SUBMIT_ORDER:
                        return new SubmitOrderComponent(data, engine);
                    case ACTIVITY:
                        return new ActivityComponent(data, engine);
                    case INSTALLMENT:
                        return new InstallmentComponent(data, engine);
                    case INSTALLMENT_TOGGLE:
                        return new InstallmentToggleComponent(data, engine);
                    case MEMBERCARD:
                        return new MemberCardComponent(data, engine);
                    case INSTALLMENT_PICKER:
                        return new InstallmentPickerComponent(data, engine);
                    case SERVICE_ADDRESS:
                        return new ServiceAddressComponent(data, engine);
                    case TAX_INFO:
                        return new TaxInfoComponent(data, engine);
                    case COUPON:
                        return new CouponComponent(data, engine);
                    case SHOP_PROMOTION_DETAIL:
                        return new ShopPromotionDetailComponent(data, engine);
                    case TOWN_REMIND:
                        return new TownRemindComponent(data, engine);
                    case PRE_SELL_ADDRESS:
                        return new PreSellAddressComponent(data, engine);
                    case FINAL_PAY:
                        return new FinalPayComponent(data, engine);
                    default:
                        return null;
                }
        }
    }

    public static boolean isRecognizableComponent(JSONObject data) {
        if (data == null) {
            return false;
        }
        String typeDesc = data.getString("type");
        String tagDesc = data.getString("tag");
        if (typeDesc == null || tagDesc == null) {
            return false;
        }
        ComponentType type = ComponentType.getComponentTypeByDesc(typeDesc);
        ComponentTag tag = ComponentTag.getComponentTagByDesc(tagDesc);
        if (type == ComponentType.UNKNOWN && tag == ComponentTag.UNKNOWN) {
            return false;
        }
        return true;
    }
}
