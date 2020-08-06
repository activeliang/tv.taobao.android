package com.yunos.tvtaobao.biz.request.bo.tvdetail.resolve;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.DetailDataCenter;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.DetailPanelData;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.TBDetailResultV6;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.Trade;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.ApiStackBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.ItemBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.SellerBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.Unit;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.util.DetailModleType;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.util.DetailShopType;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.util.ResolveResult;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.util.Source;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.List;

public class TaoBaoDetailV6Resolve {
    private static final String ALIHEALTH_ID = "163215406";
    private static final String TAG = "TaoBaoDetailV6Resolve";

    public static TBDetailResultV6 resolveTBDetailResultV6(String json) {
        ZpLogger.i(TAG, "good detail data : " + json);
        try {
            return (TBDetailResultV6) JSON.parseObject(JSONObject.parseObject(json).get("data").toString(), TBDetailResultV6.class);
        } catch (Exception e) {
            ZpLogger.e(TAG, "DetailDataCenter resolve fail");
            e.printStackTrace();
            return null;
        }
    }

    public static DetailDataCenter resolve(TBDetailResultV6 tbDetailResultV6, Source source) {
        DetailPanelData detailPanelData = null;
        try {
            detailPanelData = onHandlerDetailV6(tbDetailResultV6, getUnit(tbDetailResultV6));
        } catch (Exception e) {
            ZpLogger.e(TAG, "DetailDataCenter resolve fail");
            e.printStackTrace();
        }
        return new DetailDataCenter(detailPanelData, source);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002e, code lost:
        if (resolveUnitTrade(r5, r0) != false) goto L_0x0018;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0020, code lost:
        if (resolveDetailTrade(r4, r0) == false) goto L_0x0022;
     */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0027 A[SYNTHETIC, Splitter:B:12:0x0027] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.DetailPanelData onHandlerDetailV6(com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.TBDetailResultV6 r4, com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.Unit r5) {
        /*
            com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.DetailPanelData r0 = new com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.DetailPanelData
            r0.<init>()
            r0.tbDetailResultV6 = r4
            r0.unit = r5
            if (r4 != 0) goto L_0x0019
            com.yunos.tvtaobao.biz.request.bo.tvdetail.util.ResolveResult r2 = com.yunos.tvtaobao.biz.request.bo.tvdetail.util.ResolveResult.RESOLVE_ERROR
            r0.resolveResult = r2
            java.lang.String r2 = "TaoBaoDetailV6Resolve"
            java.lang.String r3 = "解析失败"
            com.zhiping.dev.android.logger.ZpLogger.e(r2, r3)
        L_0x0018:
            return r0
        L_0x0019:
            resolveItem(r4, r0)     // Catch:{ Exception -> 0x0054 }
        L_0x001c:
            boolean r2 = resolveDetailTrade(r4, r0)     // Catch:{ Exception -> 0x0059 }
            if (r2 != 0) goto L_0x0018
        L_0x0022:
            resolveSeller(r4, r0)     // Catch:{ Exception -> 0x005e }
        L_0x0025:
            if (r5 == 0) goto L_0x0046
            resolveXinxuan(r5, r0)     // Catch:{ Exception -> 0x0063 }
        L_0x002a:
            boolean r2 = resolveUnitTrade(r5, r0)     // Catch:{ Exception -> 0x0068 }
            if (r2 != 0) goto L_0x0018
        L_0x0030:
            resolveDelivery(r5, r0)     // Catch:{ Exception -> 0x006d }
        L_0x0033:
            resolveUnitItem(r5, r0)     // Catch:{ Exception -> 0x0072 }
        L_0x0036:
            resolvePrice(r5, r0)     // Catch:{ Exception -> 0x0077 }
        L_0x0039:
            resolveResource(r5, r0)     // Catch:{ Exception -> 0x007c }
        L_0x003c:
            resolveConsumerProtection(r5, r0)     // Catch:{ Exception -> 0x0081 }
        L_0x003f:
            com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.Unit$VerticalBean r2 = r5.getVertical()     // Catch:{ Exception -> 0x0086 }
            resolveVertical(r0, r2)     // Catch:{ Exception -> 0x0086 }
        L_0x0046:
            com.yunos.tvtaobao.biz.request.bo.tvdetail.util.ResolveResult r2 = com.yunos.tvtaobao.biz.request.bo.tvdetail.util.ResolveResult.RESOLVE_SUCCESS
            r0.resolveResult = r2
            java.lang.String r2 = "TaoBaoDetailV6Resolve"
            java.lang.String r3 = "解析成功"
            com.zhiping.dev.android.logger.ZpLogger.e(r2, r3)
            goto L_0x0018
        L_0x0054:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x001c
        L_0x0059:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0022
        L_0x005e:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0025
        L_0x0063:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x002a
        L_0x0068:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0030
        L_0x006d:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0033
        L_0x0072:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0036
        L_0x0077:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0039
        L_0x007c:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x003c
        L_0x0081:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x003f
        L_0x0086:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0046
        */
        throw new UnsupportedOperationException("Method not decompiled: com.yunos.tvtaobao.biz.request.bo.tvdetail.resolve.TaoBaoDetailV6Resolve.onHandlerDetailV6(com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.TBDetailResultV6, com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.Unit):com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.DetailPanelData");
    }

    private static void resolveXinxuan(Unit unit, DetailPanelData detailPanelData) {
        Unit.FeatureBean feature = unit.getFeature();
        if (feature != null) {
            if ("true".equals(feature.getIsXinxuan())) {
                detailPanelData.detailShopType = DetailShopType.XINXUAN;
            }
            detailPanelData.hasCoupon = "true".equals(feature.getHasCoupon());
        }
    }

    private static void resolveConsumerProtection(Unit unit, DetailPanelData detailPanelData) {
        List<String> guarantees = new ArrayList<>();
        Unit.ConsumerProtectionBean consumerProtection = unit.getConsumerProtection();
        if (consumerProtection != null) {
            List<Unit.ConsumerProtectionBean.ItemsBeanX> items = consumerProtection.getItems();
            if (items != null) {
                for (int i = 0; i < items.size(); i++) {
                    Unit.ConsumerProtectionBean.ItemsBeanX itemsBeanX = items.get(i);
                    if (itemsBeanX != null && !TextUtils.isEmpty(itemsBeanX.getTitle())) {
                        String titleConsumerProtection = itemsBeanX.getTitle();
                        if (!titleConsumerProtection.contains("花呗") && !titleConsumerProtection.contains("蚂蚁") && !titleConsumerProtection.contains("分期")) {
                            guarantees.add(itemsBeanX.getTitle());
                            if (guarantees.size() > 2) {
                                break;
                            }
                        }
                    }
                }
            }
            detailPanelData.services = guarantees;
            ZpLogger.e(TAG, "服务承诺 = " + detailPanelData.services);
            Unit.ConsumerProtectionBean.ChannelBean channel = consumerProtection.getChannel();
            if (channel != null) {
                detailPanelData.logo = channel.getLogo();
                ZpLogger.e(TAG, "行业logo = " + detailPanelData.logo);
                detailPanelData.slogo = channel.getTitle();
                ZpLogger.e(TAG, "行业说明 = " + detailPanelData.slogo);
            }
        }
    }

    private static void resolveResource(Unit unit, DetailPanelData detailPanelData) {
        Unit.ResourceBean.CouponBeanFirst coupon;
        List<Unit.ResourceBean.CouponBeanFirst.CouponBeanItem> couponList;
        Unit.ResourceBean resource = unit.getResource();
        if (resource != null && (coupon = resource.getCoupon()) != null && (couponList = coupon.getCouponList()) != null && couponList.size() > 0) {
            detailPanelData.couponBeanItems = couponList;
            ZpLogger.e(TAG, "购物券集合 = " + detailPanelData.couponBeanItems);
            if (!"领取优惠券".equals(couponList.get(0).getTitle())) {
                detailPanelData.couponText = couponList.get(0).getTitle();
                detailPanelData.couponIcon = couponList.get(0).getIcon();
            }
        }
    }

    private static void resolvePrice(Unit unit, DetailPanelData detailPanelData) {
        Unit.PriceBeanX.ShopPromBean shopPromBean;
        Unit.PriceBeanX.ExtraPricesBean extraPricesBean;
        Unit.PriceBeanX priceBeanX = unit.getPrice();
        if (priceBeanX != null) {
            Unit.PriceBeanX.PriceBean priceBeanXPrice = priceBeanX.getPrice();
            if (priceBeanXPrice != null) {
                detailPanelData.nowPrice = priceBeanXPrice.getPriceText();
                ZpLogger.e(TAG, "现价 = " + detailPanelData.nowPrice);
                detailPanelData.nowPriceTitle = priceBeanXPrice.getPriceTitle();
                ZpLogger.e(TAG, "现价标题 = " + detailPanelData.nowPriceTitle);
                if ("全款预售".equals(detailPanelData.nowPriceTitle)) {
                    detailPanelData.detailModleType = DetailModleType.ALLPAYPRESALE;
                    ZpLogger.e(TAG, "全款预售");
                }
            }
            Unit.PriceBeanX.SubPriceBean priceBeanXSubPrice = priceBeanX.getSubPrice();
            if (priceBeanXSubPrice != null) {
                detailPanelData.presellPrice = priceBeanXSubPrice.getPriceText();
                ZpLogger.e(TAG, "预售总价 = " + detailPanelData.presellPrice);
                detailPanelData.presellPriceTitle = priceBeanXSubPrice.getPriceTitle();
                ZpLogger.e(TAG, "预售标题 = " + detailPanelData.presellPriceTitle);
            }
            List<Unit.PriceBeanX.ExtraPricesBean> extraPrices = priceBeanX.getExtraPrices();
            if (!(extraPrices == null || extraPrices.size() <= 0 || (extraPricesBean = extraPrices.get(0)) == null)) {
                detailPanelData.oldPrice = extraPricesBean.getPriceText();
                ZpLogger.e(TAG, "原价 = " + detailPanelData.oldPrice);
                detailPanelData.oldPriceLineThrough = extraPricesBean.getLineThrough();
                ZpLogger.e(TAG, "原价是否要下划线 = " + detailPanelData.oldPriceLineThrough);
                detailPanelData.oldPriceTitle = extraPricesBean.getPriceTitle();
                ZpLogger.e(TAG, "原价标题 = " + detailPanelData.oldPriceTitle);
            }
            List<Unit.PriceBeanX.ShopPromBean> shopProm = priceBeanX.getShopProm();
            if (!(shopProm == null || shopProm.size() <= 0 || (shopPromBean = shopProm.get(0)) == null)) {
                detailPanelData.salesPromotionTitle = shopPromBean.getTitle();
                ZpLogger.e(TAG, "店铺优惠标题 = " + detailPanelData.salesPromotionTitle);
                detailPanelData.salesPromotionIconText = shopPromBean.getIconText();
                ZpLogger.e(TAG, "店铺优惠icon = " + detailPanelData.salesPromotionIconText);
                detailPanelData.salesPromotionContent = shopPromBean.getContent();
                ZpLogger.e(TAG, "店铺优惠内容 = " + detailPanelData.salesPromotionContent);
            }
            Unit.PriceBeanX.DepositPriceBean depositPrice = priceBeanX.getDepositPrice();
            if (depositPrice != null) {
                detailPanelData.depositPriceDesc = depositPrice.getPriceDesc();
                ZpLogger.e(TAG, "预售的描述 = " + detailPanelData.depositPriceDesc);
            }
        }
    }

    private static void resolveUnitItem(Unit unit, DetailPanelData detailPanelData) {
        Unit.ItemBean itemBean = unit.getItem();
        if (itemBean != null) {
            detailPanelData.soldNum = itemBean.getSellCount();
            ZpLogger.e(TAG, "月销量 = " + detailPanelData.soldNum);
        }
    }

    private static void resolveDelivery(Unit unit, DetailPanelData detailPanelData) {
        Unit.DeliveryBean delivery = unit.getDelivery();
        String deliveryFee = "";
        if (delivery != null) {
            String deliveryFee2 = delivery.getPostage();
            String deliveryTo = delivery.getTo();
            if (TextUtils.isEmpty(deliveryFee2)) {
                deliveryFee = "";
            } else {
                deliveryFee = deliveryFee2 + "(至 " + deliveryTo + ")";
            }
        }
        detailPanelData.postage = deliveryFee;
        ZpLogger.e(TAG, "快递费用 = " + deliveryFee);
    }

    private static boolean resolveUnitTrade(Unit unit, DetailPanelData detailPanelData) {
        Unit.TradeBean unitTrade = unit.getTrade();
        if (unitTrade != null) {
            String redirectUrl = unitTrade.getRedirectUrl();
            if (redirectUrl != null) {
                if (redirectUrl.contains("trip")) {
                    ZpLogger.e(TAG, "飞猪商品");
                    detailPanelData.resolveResult = ResolveResult.FEI_ZHU_REDIRECT;
                    detailPanelData.detailShopType = DetailShopType.FEIZHU;
                    return true;
                }
                ZpLogger.e(TAG, "h5重定向");
                detailPanelData.resolveResult = ResolveResult.OTHER_REDIRECT;
            }
            String buyText = unitTrade.getBuyText();
            String buyEnable = unitTrade.getBuyEnable();
            detailPanelData.isSupportAddCart = unitTrade.getCartEnable();
            detailPanelData.isSupportBuy = buyEnable;
            if (buyText != null) {
                detailPanelData.buyText = buyText;
            } else if ("true".equals(buyEnable)) {
                detailPanelData.buyText = "立即购买";
                ZpLogger.e(TAG, "立即购买 ");
            } else {
                ZpLogger.e(TAG, "暂不支持购买");
                detailPanelData.buyText = "暂不支持购买";
            }
            Unit.TradeBean.HintBannerBean hintBanner = unitTrade.getHintBanner();
            if (hintBanner != null) {
                detailPanelData.buyText = hintBanner.getText();
            }
        }
        return false;
    }

    private static void resolveSeller(TBDetailResultV6 tbDetailResultV6, DetailPanelData detailPanelData) {
        SellerBean seller = tbDetailResultV6.getSeller();
        if (seller != null) {
            if (ALIHEALTH_ID.equals(seller.getShopId())) {
                detailPanelData.detailShopType = DetailShopType.ALIHEALTH;
            }
            detailPanelData.shopName = seller.getShopName();
            ZpLogger.e(TAG, "店铺名称 = " + detailPanelData.shopName);
            detailPanelData.shopType = seller.getShopType();
            ZpLogger.e(TAG, "店铺类型 = " + detailPanelData.shopType);
            detailPanelData.sellerNick = seller.getSellerNick();
            ZpLogger.e(TAG, "卖家昵称 = " + detailPanelData.sellerNick);
            detailPanelData.sellerType = seller.getSellerType();
            ZpLogger.e(TAG, "卖家类型 = " + detailPanelData.sellerType);
            detailPanelData.sellerId = seller.getUserId();
            ZpLogger.e(TAG, "卖家id = " + detailPanelData.sellerId);
            detailPanelData.shopId = seller.getShopId();
            ZpLogger.e(TAG, "卖家shopId = " + detailPanelData.shopId);
        }
    }

    private static boolean resolveDetailTrade(TBDetailResultV6 tbDetailResultV6, DetailPanelData detailPanelData) {
        String redirectUrl;
        Trade trade = tbDetailResultV6.getTrade();
        if (trade == null || (redirectUrl = trade.getRedirectUrl()) == null || !redirectUrl.contains("trip")) {
            return false;
        }
        detailPanelData.resolveResult = ResolveResult.FEI_ZHU_REDIRECT;
        detailPanelData.detailShopType = DetailShopType.FEIZHU;
        ZpLogger.e(TAG, "飞猪商品");
        return true;
    }

    private static void resolveItem(TBDetailResultV6 tbDetailResultV6, DetailPanelData detailPanelData) {
        ItemBean detailResultV6Item = tbDetailResultV6.getItem();
        if (detailResultV6Item != null) {
            detailPanelData.itemId = detailResultV6Item.getItemId();
            ZpLogger.e(TAG, "商品Id = " + detailPanelData.itemId);
            detailPanelData.goodTitle = detailResultV6Item.getTitle();
            ZpLogger.e(TAG, "商品标题 = " + detailPanelData.goodTitle);
            detailPanelData.goodsImages = detailResultV6Item.getImages();
            ZpLogger.e(TAG, "商品图 = " + detailPanelData.goodsImages);
        }
    }

    private static void resolveVertical(DetailPanelData detailPanelData, Unit.VerticalBean vertical) {
        if (vertical != null) {
            Unit.VerticalBean.JhsBean jhs = vertical.getJhs();
            if (jhs != null) {
                detailPanelData.detailModleType = DetailModleType.JUHUASUAN;
                ZpLogger.e(TAG, "聚划算商品");
                detailPanelData.status = jhs.getStatus();
                if (!TextUtils.isEmpty(detailPanelData.status)) {
                    String str = detailPanelData.status;
                    char c = 65535;
                    switch (str.hashCode()) {
                        case 48:
                            if (str.equals("0")) {
                                c = 0;
                                break;
                            }
                            break;
                        case 49:
                            if (str.equals("1")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 50:
                            if (str.equals("2")) {
                                c = 2;
                                break;
                            }
                            break;
                        case 51:
                            if (str.equals("3")) {
                                c = 3;
                                break;
                            }
                            break;
                        case 52:
                            if (str.equals("4")) {
                                c = 4;
                                break;
                            }
                            break;
                    }
                    switch (c) {
                        case 0:
                            detailPanelData.typeTime = jhs.getStartTime();
                            detailPanelData.buyText = "即将开始";
                            break;
                        case 1:
                            detailPanelData.typeTime = jhs.getEndTime();
                            detailPanelData.buyText = "马上抢";
                            break;
                        case 2:
                            detailPanelData.buyText = "您还有机会,有买家未付款";
                            break;
                        case 3:
                            detailPanelData.buyText = "卖光了";
                            break;
                        case 4:
                            detailPanelData.buyText = "已结束";
                            break;
                    }
                }
                ZpLogger.e(TAG, "聚划算" + detailPanelData.buyText);
            }
            Unit.VerticalBean.InterBean inter = vertical.getInter();
            if (inter != null) {
                detailPanelData.detailShopType = DetailShopType.TIANMAOGUOJI;
                ZpLogger.e(TAG, "天猫国际商品");
                if (inter.getTariff() != null) {
                    detailPanelData.tax = inter.getTariff().getValue();
                    ZpLogger.e(TAG, "进口税 = " + detailPanelData.tax);
                }
            }
            Unit.VerticalBean.PresaleBean presale = vertical.getPresale();
            if (presale != null) {
                if (detailPanelData.detailModleType == null || detailPanelData.detailModleType != DetailModleType.ALLPAYPRESALE) {
                    detailPanelData.detailModleType = DetailModleType.PRESALE;
                }
                ZpLogger.e(TAG, "预售商品");
                detailPanelData.depositText = presale.getDepositText();
                ZpLogger.e(TAG, "预售价格 = " + detailPanelData.depositText);
                detailPanelData.deliverGoods = presale.getExtraText();
                ZpLogger.e(TAG, "预售发货 = " + detailPanelData.deliverGoods);
                detailPanelData.status = presale.getStatus();
                ZpLogger.e(TAG, "预售状态 = " + detailPanelData.status);
                detailPanelData.typeTime = presale.getText();
                ZpLogger.e(TAG, "预售时间 = " + detailPanelData.typeTime);
                detailPanelData.lastPriceTip = presale.getTip();
                ZpLogger.e(TAG, "预售支付尾款时间 = " + detailPanelData.lastPriceTip);
                detailPanelData.orderedItemAmount = presale.getOrderedItemAmount();
                ZpLogger.e(TAG, "预售已预订的数量 = " + detailPanelData.orderedItemAmount);
            }
            Unit.VerticalBean.QianggouBean qianggou = vertical.getQianggou();
            if (qianggou != null) {
                detailPanelData.detailModleType = DetailModleType.QIANGOU;
                ZpLogger.e(TAG, "淘抢购商品");
                String qianggouStatus = qianggou.getStatus();
                if ("2".equals(qianggouStatus)) {
                    detailPanelData.status = "0";
                    detailPanelData.typeTime = qianggou.getStartTime();
                    ZpLogger.e(TAG, "淘抢购未开团");
                    ZpLogger.e(TAG, "淘抢购开团时间 = " + detailPanelData.typeTime);
                } else if ("1".equals(qianggouStatus)) {
                    detailPanelData.status = "1";
                    detailPanelData.typeTime = qianggou.getEndTime();
                    ZpLogger.e(TAG, "淘抢购已开团");
                    ZpLogger.e(TAG, "淘抢购结束时间 = " + detailPanelData.typeTime);
                }
            }
            Unit.VerticalBean.SupermarketBean supermarket = vertical.getSupermarket();
            if (supermarket != null) {
                detailPanelData.detailShopType = DetailShopType.SUPERMARKET;
                if (supermarket.getWeight() != null) {
                    detailPanelData.weight = supermarket.getWeight();
                }
                detailPanelData.buyText = "加入购物车";
            }
        }
    }

    public static Unit getUnit(TBDetailResultV6 tbDetailResultV6) {
        List<ApiStackBean> apiStack;
        if (tbDetailResultV6 == null || (apiStack = tbDetailResultV6.getApiStack()) == null || apiStack.get(0) == null) {
            return null;
        }
        return (Unit) JSON.parseObject(apiStack.get(0).getValue(), Unit.class);
    }

    public static boolean isFeiZhu(TBDetailResultV6 tbDetailResultV6) {
        if (tbDetailResultV6 == null || tbDetailResultV6.getTrade() == null || tbDetailResultV6.getTrade().getRedirectUrl() == null || !tbDetailResultV6.getTrade().getRedirectUrl().contains("trip")) {
            return false;
        }
        return true;
    }
}
