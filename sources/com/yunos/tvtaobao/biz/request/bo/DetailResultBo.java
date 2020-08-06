package com.yunos.tvtaobao.biz.request.bo;

import android.text.TextUtils;
import com.ali.user.open.core.util.ParamsConstants;
import com.alibaba.appmonitor.sample.SampleConfigConstant;
import com.taobao.detail.clientDomain.TBDetailResultVO;
import com.taobao.detail.domain.DetailVO;
import com.taobao.detail.domain.base.ActionUnit;
import com.taobao.detail.domain.base.PriceUnit;
import com.taobao.detail.domain.base.TipDO;
import com.taobao.detail.domain.base.Unit;
import com.taobao.detail.domain.meal.ComboInfo;
import com.taobao.detail.domain.rate.RateDetail;
import com.taobao.detail.domain.rate.RateTag;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tvtaobao.payment.utils.TvTaoSharedPerference;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailResultBo implements Serializable {
    private static final String TAG = "DetailResultBo";
    private static final long serialVersionUID = 7476040158039373150L;

    public static TBDetailResultVO resolve(JSONObject response) throws JSONException {
        if (response == null) {
            return null;
        }
        ZpLogger.i(TAG, "use DetailResultBo ... ");
        TBDetailResultVO tbDetailResultVO = new TBDetailResultVO();
        JSONArray array_apiStack = response.optJSONArray("apiStack");
        if (array_apiStack != null && array_apiStack.length() > 0) {
            resolve_ApiStack(tbDetailResultVO, array_apiStack);
        }
        JSONArray displayType = response.optJSONArray("displayType");
        if (displayType != null && displayType.length() > 0) {
            resolve_DisplayType(tbDetailResultVO, displayType);
        }
        JSONObject itemInfoModel = response.optJSONObject("itemInfoModel");
        if (itemInfoModel != null) {
            resolve_ItemInfoModel(tbDetailResultVO, itemInfoModel);
        }
        JSONObject itemControl = response.optJSONObject("itemControl");
        if (itemControl != null) {
            resolve_ItemControl(tbDetailResultVO, itemControl);
        }
        JSONObject seller = response.optJSONObject("seller");
        if (seller != null) {
            resolve_Seller(tbDetailResultVO, seller);
        }
        JSONObject delivery = response.optJSONObject("delivery");
        if (delivery != null) {
            resolve_Delivery(tbDetailResultVO, delivery);
        }
        JSONObject tips = response.optJSONObject("tips");
        if (tips != null) {
            resolve_Tips(tbDetailResultVO, tips);
        }
        JSONArray props = response.optJSONArray("props");
        if (props != null && props.length() > 0) {
            resolve_Props(tbDetailResultVO, props);
        }
        JSONObject descInfo = response.optJSONObject("descInfo");
        if (descInfo != null) {
            resolve_DescInfo(tbDetailResultVO, descInfo);
        }
        JSONObject guaranteeInfo = response.optJSONObject("guaranteeInfo");
        if (guaranteeInfo != null) {
            resolve_GuaranteeInfo(tbDetailResultVO, guaranteeInfo);
        }
        JSONObject rateInfo = response.optJSONObject("rateInfo");
        if (rateInfo != null) {
            resolve_RateInfo(tbDetailResultVO, rateInfo);
        }
        JSONObject comboInfo = response.optJSONObject("comboInfo");
        if (comboInfo != null) {
            resolve_ComboInfo(tbDetailResultVO, comboInfo);
        }
        JSONObject skuModel = response.optJSONObject("skuModel");
        if (skuModel == null) {
            return tbDetailResultVO;
        }
        resolve_SkuModel(tbDetailResultVO, skuModel);
        return tbDetailResultVO;
    }

    private static void resolve_ApiStack(TBDetailResultVO tbDetailResultVO, JSONArray array) {
        int length = array.length();
        tbDetailResultVO.apiStack = new ArrayList();
        for (int i = 0; i < length; i++) {
            JSONObject obj = array.optJSONObject(i);
            if (obj != null) {
                Unit unit = new Unit();
                unit.name = obj.optString("name", (String) null);
                unit.value = obj.optString("value", (String) null);
                tbDetailResultVO.apiStack.add(unit);
            }
        }
    }

    private static void resolve_ItemInfoModel(TBDetailResultVO tbDetailResultVO, JSONObject itemInfoModel) {
        tbDetailResultVO.itemInfoModel = new TBDetailResultVO.ItemInfoModel();
        tbDetailResultVO.itemInfoModel.itemId = itemInfoModel.optString("itemId", (String) null);
        tbDetailResultVO.itemInfoModel.title = itemInfoModel.optString("title", (String) null);
        tbDetailResultVO.itemInfoModel.favcount = Long.valueOf(itemInfoModel.optLong("favcount"));
        tbDetailResultVO.itemInfoModel.stuffStatus = itemInfoModel.optString("stuffStatus", (String) null);
        tbDetailResultVO.itemInfoModel.itemUrl = itemInfoModel.optString("itemUrl", (String) null);
        tbDetailResultVO.itemInfoModel.sku = Boolean.valueOf(itemInfoModel.optBoolean("sku", false));
        tbDetailResultVO.itemInfoModel.location = itemInfoModel.optString("location", (String) null);
        tbDetailResultVO.itemInfoModel.saleLine = itemInfoModel.optString("saleLine", (String) null);
        tbDetailResultVO.itemInfoModel.categoryId = itemInfoModel.optString("categoryId", (String) null);
        tbDetailResultVO.itemInfoModel.itemTypeName = itemInfoModel.optString("itemTypeName", (String) null);
        tbDetailResultVO.itemInfoModel.itemTypeLogo = itemInfoModel.optString("itemTypeLogo", (String) null);
        tbDetailResultVO.itemInfoModel.itemIcon = itemInfoModel.optString("itemIcon", (String) null);
        tbDetailResultVO.itemInfoModel.isMakeup = itemInfoModel.optBoolean("isMakeup", false);
        tbDetailResultVO.itemInfoModel.itemUrl = itemInfoModel.optString("itemUrl", (String) null);
        tbDetailResultVO.itemInfoModel.payedCount = itemInfoModel.optString("payedCount", (String) null);
        tbDetailResultVO.itemInfoModel.points = itemInfoModel.optString("points", (String) null);
        tbDetailResultVO.itemInfoModel.quantity = Long.valueOf(itemInfoModel.optLong("quantity"));
        tbDetailResultVO.itemInfoModel.quantityText = itemInfoModel.optString("quantityText", (String) null);
        tbDetailResultVO.itemInfoModel.soldQuantityText = itemInfoModel.optString("soldQuantityText", (String) null);
        tbDetailResultVO.itemInfoModel.startTime = itemInfoModel.optString("startTime", (String) null);
        tbDetailResultVO.itemInfoModel.totalSoldQuantity = Integer.valueOf(itemInfoModel.optInt("totalSoldQuantity"));
        tbDetailResultVO.itemInfoModel.weight = itemInfoModel.optString("weight", (String) null);
        tbDetailResultVO.itemInfoModel.subTitle = itemInfoModel.optString("subTitle", (String) null);
        JSONArray array = itemInfoModel.optJSONArray("picsPath");
        if (array != null && array.length() > 0) {
            tbDetailResultVO.itemInfoModel.picsPath = new ArrayList();
            int length = array.length();
            for (int i = 0; i < length; i++) {
                String pic = array.optString(i);
                if (!TextUtils.isEmpty(pic)) {
                    tbDetailResultVO.itemInfoModel.picsPath.add(pic);
                }
            }
        }
        JSONArray priceUnits = itemInfoModel.optJSONArray("priceUnits");
        tbDetailResultVO.itemInfoModel.priceUnits = resolve_PriceUnit(priceUnits);
        JSONArray videosPath = itemInfoModel.optJSONArray("videosPath");
        if (videosPath != null && videosPath.length() > 0) {
            tbDetailResultVO.itemInfoModel.videosPath = new ArrayList();
            int length2 = videosPath.length();
            for (int i2 = 0; i2 < length2; i2++) {
                String videoPath = videosPath.optString(i2);
                if (!TextUtils.isEmpty(videoPath)) {
                    tbDetailResultVO.itemInfoModel.videosPath.add(videoPath);
                }
            }
        }
    }

    private static void resolve_Seller(TBDetailResultVO tbDetailResultVO, JSONObject seller) {
        tbDetailResultVO.seller = new DetailVO.StaticItem.Seller();
        tbDetailResultVO.seller.userNumId = Long.valueOf(seller.optLong("userNumId"));
        tbDetailResultVO.seller.type = seller.optString("type", (String) null);
        tbDetailResultVO.seller.nick = seller.optString(TvTaoSharedPerference.NICK, (String) null);
        tbDetailResultVO.seller.certificateLogo = seller.optString("certificateLogo", (String) null);
        tbDetailResultVO.seller.certify = seller.optString("certify", (String) null);
        tbDetailResultVO.seller.creditLevel = Integer.valueOf(seller.optInt("creditLevel"));
        tbDetailResultVO.seller.goodRatePercentage = seller.optString("goodRatePercentage", (String) null);
        tbDetailResultVO.seller.shopTitle = seller.optString("shopId", (String) null);
        tbDetailResultVO.seller.weitaoId = Long.valueOf(seller.optLong("weitaoId"));
        tbDetailResultVO.seller.fansCount = seller.optString("fansCount", (String) null);
        tbDetailResultVO.seller.fansCountText = seller.optString("fansCountText", (String) null);
        tbDetailResultVO.seller.picUrl = seller.optString(TuwenConstants.PARAMS.PIC_URL, (String) null);
        tbDetailResultVO.seller.starts = seller.optString("starts", (String) null);
        tbDetailResultVO.seller.shopPromtionType = seller.optString("shopPromtionType", (String) null);
        tbDetailResultVO.seller.bailAmount = seller.optString("bailAmount", (String) null);
        tbDetailResultVO.seller.distance = seller.optString("distance", (String) null);
        tbDetailResultVO.seller.distance = seller.optString("distance", (String) null);
        tbDetailResultVO.seller.hide = seller.optString("hide", (String) null);
        tbDetailResultVO.seller.hideDsr = seller.optString("hideDsr", (String) null);
        tbDetailResultVO.seller.hideWangwang = seller.optString("hideWangwang", (String) null);
        tbDetailResultVO.seller.html = seller.optString("html", (String) null);
        tbDetailResultVO.seller.o2oMapUrl = seller.optString("o2oMapUrl", (String) null);
        tbDetailResultVO.seller.shopBrand = seller.optString("shopBrand", (String) null);
        tbDetailResultVO.seller.shopCollectorCount = Integer.valueOf(seller.optInt("shopCollectorCount"));
        tbDetailResultVO.seller.shopIcon = seller.optString("shopIcon", (String) null);
        tbDetailResultVO.seller.shopId = Integer.valueOf(seller.optInt("shopId"));
        tbDetailResultVO.seller.shopLocation = seller.optString("shopLocation", (String) null);
        tbDetailResultVO.seller.tollFreeNumber = seller.optString("tollFreeNumber", (String) null);
        tbDetailResultVO.seller.tollFreeSubNumber = seller.optString("tollFreeSubNumber", (String) null);
        JSONArray evaluateInfo = seller.optJSONArray("evaluateInfo");
        if (evaluateInfo != null && evaluateInfo.length() > 0) {
            tbDetailResultVO.seller.evaluateInfo = new ArrayList();
            int length = evaluateInfo.length();
            for (int i = 0; i < length; i++) {
                JSONObject obj = evaluateInfo.optJSONObject(i);
                if (obj != null) {
                    DetailVO.StaticItem.Seller.EvaluateInfoVO evaluateInfoVO = new DetailVO.StaticItem.Seller.EvaluateInfoVO();
                    evaluateInfoVO.highGap = obj.optString("highGap", (String) null);
                    evaluateInfoVO.name = obj.optString("name", (String) null);
                    evaluateInfoVO.score = obj.optString("score", (String) null);
                    evaluateInfoVO.title = obj.optString("title", (String) null);
                    tbDetailResultVO.seller.evaluateInfo.add(evaluateInfoVO);
                }
            }
        }
        JSONArray actionUnits = seller.optJSONArray("actionUnits");
        if (actionUnits != null && actionUnits.length() > 0) {
            tbDetailResultVO.seller.actionUnits = new ArrayList();
            int length2 = actionUnits.length();
            for (int i2 = 0; i2 < length2; i2++) {
                JSONObject obj2 = actionUnits.optJSONObject(i2);
                if (obj2 != null) {
                    ActionUnit actionUnit = new ActionUnit();
                    actionUnit.name = obj2.optString("name", (String) null);
                    actionUnit.track = obj2.optString("track", (String) null);
                    actionUnit.url = obj2.optString("url", (String) null);
                    actionUnit.value = obj2.optString("value", (String) null);
                    tbDetailResultVO.seller.actionUnits.add(actionUnit);
                }
            }
        }
    }

    private static void resolve_Props(TBDetailResultVO tbDetailResultVO, JSONArray props) {
        int length = props.length();
        tbDetailResultVO.props = new ArrayList();
        for (int i = 0; i < length; i++) {
            JSONObject obj = props.optJSONObject(i);
            if (obj != null) {
                Unit unit = new Unit();
                unit.name = obj.optString("name", (String) null);
                unit.value = obj.optString("value", (String) null);
                tbDetailResultVO.props.add(unit);
            }
        }
    }

    private static void resolve_DescInfo(TBDetailResultVO tbDetailResultVO, JSONObject descInfo) {
        tbDetailResultVO.descInfo = new DetailVO.StaticItem.DescInfo();
        tbDetailResultVO.descInfo.briefDescUrl = descInfo.optString("briefDescUrl", (String) null);
        tbDetailResultVO.descInfo.fullDescUrl = descInfo.optString("fullDescUrl", (String) null);
        tbDetailResultVO.descInfo.showFullDetailDesc = descInfo.optString("showFullDetailDesc", (String) null);
        tbDetailResultVO.descInfo.pcDescUrl = descInfo.optString("showFullDetailDesc", (String) null);
        tbDetailResultVO.descInfo.h5DescUrl = descInfo.optString("h5DescUrl", (String) null);
        tbDetailResultVO.descInfo.h5DescUrl2 = descInfo.optString("h5DescUrl2", (String) null);
        tbDetailResultVO.descInfo.moduleDescUrl2 = descInfo.optString("moduleDescUrl2", (String) null);
    }

    private static void resolve_RateInfo(TBDetailResultVO tbDetailResultVO, JSONObject rateInfo) {
        tbDetailResultVO.rateInfo = new DetailVO.StaticItem.RateInfo();
        tbDetailResultVO.rateInfo.rateCounts = Integer.valueOf(rateInfo.optInt("rateCounts"));
        JSONArray rateDetailList = rateInfo.optJSONArray("rateDetailList");
        if (rateDetailList != null && rateDetailList.length() > 0) {
            int length = rateDetailList.length();
            tbDetailResultVO.rateInfo.rateDetailList = new ArrayList();
            for (int i = 0; i < length; i++) {
                JSONObject obj = rateDetailList.optJSONObject(i);
                if (obj != null) {
                    RateDetail rateDetail = new RateDetail();
                    rateDetail.feedback = obj.optString("feedback", (String) null);
                    rateDetail.nick = obj.optString(TvTaoSharedPerference.NICK, (String) null);
                    rateDetail.headPic = obj.optString("headPic", (String) null);
                    rateDetail.star = Integer.valueOf(obj.optInt("star"));
                    rateDetail.subInfo = obj.optString("subInfo", (String) null);
                    tbDetailResultVO.rateInfo.rateDetailList.add(rateDetail);
                }
            }
        }
        JSONArray tagList = rateInfo.optJSONArray("tagList");
        if (tagList != null && tagList.length() > 0) {
            int length2 = tagList.length();
            tbDetailResultVO.rateInfo.tagList = new ArrayList();
            for (int i2 = 0; i2 < length2; i2++) {
                JSONObject obj2 = tagList.optJSONObject(i2);
                if (obj2 != null) {
                    RateTag rateTag = new RateTag();
                    rateTag.attribute = obj2.optString("attribute", (String) null);
                    rateTag.title = obj2.optString("title", (String) null);
                    rateTag.count = obj2.optString("count", (String) null);
                    rateTag.score = obj2.optString("score", (String) null);
                    tbDetailResultVO.rateInfo.tagList.add(rateTag);
                }
            }
        }
    }

    private static void resolve_ComboInfo(TBDetailResultVO tbDetailResultVO, JSONObject comboInfo) {
        tbDetailResultVO.comboInfo = new ComboInfo();
        JSONObject asynUrl = comboInfo.optJSONObject("asynUrl");
        if (asynUrl != null) {
            tbDetailResultVO.comboInfo.asynUrl = new Unit();
            tbDetailResultVO.comboInfo.asynUrl.name = asynUrl.optString("name", (String) null);
            tbDetailResultVO.comboInfo.asynUrl.value = asynUrl.optString("value", (String) null);
        }
        JSONObject h5Url = comboInfo.optJSONObject(ParamsConstants.Key.PARAM_H5URL);
        if (h5Url != null) {
            tbDetailResultVO.comboInfo.h5Url = new Unit();
            tbDetailResultVO.comboInfo.h5Url.name = h5Url.optString("name", (String) null);
            tbDetailResultVO.comboInfo.h5Url.value = h5Url.optString("value", (String) null);
        }
    }

    private static void resolve_SkuModel(TBDetailResultVO tbDetailResultVO, JSONObject skuModel) {
        JSONObject sku_json;
        tbDetailResultVO.skuModel = new TBDetailResultVO.SkuModel();
        tbDetailResultVO.skuModel.installmentEnable = skuModel.optBoolean("installmentEnable");
        tbDetailResultVO.skuModel.skuTitle = skuModel.optString("skuTitle");
        JSONArray skuProps = skuModel.optJSONArray("skuProps");
        tbDetailResultVO.skuModel.skuProps = resolve_SkuModel_skuProps(skuProps);
        JSONObject ppathIdmap = skuModel.optJSONObject("ppathIdmap");
        tbDetailResultVO.skuModel.ppathIdmap = resolveToMap_String(ppathIdmap);
        JSONObject skus = skuModel.optJSONObject("skus");
        if (skus != null) {
            tbDetailResultVO.skuModel.skus = new HashMap();
            Iterator<String> keys = skus.keys();
            while (keys.hasNext()) {
                String key = String.valueOf(keys.next());
                if (!TextUtils.isEmpty(key) && (sku_json = skus.optJSONObject(key)) != null) {
                    DetailVO.DynamicItem.SkuPriceAndQuanitiy skuPriceAndQuanitiy = new DetailVO.DynamicItem.SkuPriceAndQuanitiy();
                    skuPriceAndQuanitiy.quantity = Integer.valueOf(sku_json.optInt("quantity"));
                    skuPriceAndQuanitiy.hide = sku_json.optString("hide", (String) null);
                    skuPriceAndQuanitiy.html = sku_json.optString("html", (String) null);
                    skuPriceAndQuanitiy.quantityText = sku_json.optString("quantityText", (String) null);
                    skuPriceAndQuanitiy.simplePrice = sku_json.optString("simplePrice", (String) null);
                    skuPriceAndQuanitiy.priceUnits = resolve_PriceUnit(sku_json.optJSONArray("priceUnits"));
                    tbDetailResultVO.skuModel.skus.put(key, skuPriceAndQuanitiy);
                }
            }
        }
    }

    protected static void resolve_Extras(TBDetailResultVO tbDetailResultVO, JSONObject extras) {
        Object value;
        tbDetailResultVO.extras = new HashMap();
        Iterator<String> keys = extras.keys();
        while (keys.hasNext()) {
            String key = String.valueOf(keys.next());
            if (!TextUtils.isEmpty(key) && (value = extras.opt(key)) != null) {
                tbDetailResultVO.extras.put(key, value);
            }
        }
        ZpLogger.i(TAG, "tbDetailResultVO.extras = " + tbDetailResultVO.extras);
    }

    private static void resolve_DisplayType(TBDetailResultVO tbDetailResultVO, JSONArray displayType) {
        int length = displayType.length();
        tbDetailResultVO.displayType = new String[length];
        for (int i = 0; i < length; i++) {
            tbDetailResultVO.displayType[i] = displayType.optString(i);
        }
    }

    private static void resolve_Delivery(TBDetailResultVO tbDetailResultVO, JSONObject delivery) {
        tbDetailResultVO.delivery = new DetailVO.DynamicItem.Delivery();
        tbDetailResultVO.delivery.destination = delivery.optString("destination", (String) null);
        JSONArray deliveryFees = delivery.optJSONArray("deliveryFees");
        if (deliveryFees != null && deliveryFees.length() > 0) {
            tbDetailResultVO.delivery.deliveryFees = new ArrayList();
            int length = deliveryFees.length();
            for (int i = 0; i < length; i++) {
                String deliveryFee = deliveryFees.optString(i);
                if (!TextUtils.isEmpty(deliveryFee)) {
                    tbDetailResultVO.delivery.deliveryFees.add(deliveryFee);
                }
            }
        }
    }

    private static void resolve_GuaranteeInfo(TBDetailResultVO tbDetailResultVO, JSONObject guaranteeInfo) {
        tbDetailResultVO.guaranteeInfo = new DetailVO.StaticItem.GuaranteeInfo();
        JSONArray guarantees = guaranteeInfo.optJSONArray("guarantees");
        if (guarantees != null && guarantees.length() > 0) {
            tbDetailResultVO.guaranteeInfo.guarantees = resolve_Guarantees(guarantees);
        }
        JSONArray beforeGuarantees = guaranteeInfo.optJSONArray("beforeGuarantees");
        if (beforeGuarantees != null && beforeGuarantees.length() > 0) {
            tbDetailResultVO.guaranteeInfo.beforeGuarantees = resolve_Guarantees(beforeGuarantees);
        }
        JSONArray afterGuarantees = guaranteeInfo.optJSONArray("afterGuarantees");
        if (afterGuarantees != null && afterGuarantees.length() > 0) {
            tbDetailResultVO.guaranteeInfo.afterGuarantees = resolve_Guarantees(afterGuarantees);
        }
    }

    private static void resolve_ItemControl(TBDetailResultVO tbDetailResultVO, JSONObject itemControl) {
        tbDetailResultVO.itemControl = new DetailVO.DynamicItem.ItemControl();
        tbDetailResultVO.itemControl.degradedItemUrl = itemControl.optString("degradedItemUrl", (String) null);
        tbDetailResultVO.itemControl.buyUrl = itemControl.optString("buyUrl", (String) null);
        tbDetailResultVO.itemControl.smartbanner = Boolean.valueOf(itemControl.optBoolean("smartbanner"));
        JSONObject unitControl = itemControl.optJSONObject("unitControl");
        if (unitControl != null) {
            tbDetailResultVO.itemControl.unitControl = new DetailVO.DynamicItem.ItemControl.UnitControl();
            tbDetailResultVO.itemControl.unitControl.baseTime = unitControl.optString("baseTime", (String) null);
            tbDetailResultVO.itemControl.unitControl.buySupport = unitControl.optBoolean("buySupport");
            tbDetailResultVO.itemControl.unitControl.buyText = unitControl.optString("buyText", (String) null);
            tbDetailResultVO.itemControl.unitControl.cartSupport = unitControl.optBoolean("cartSupport");
            tbDetailResultVO.itemControl.unitControl.cartText = unitControl.optString("cartText", (String) null);
            tbDetailResultVO.itemControl.unitControl.errorCode = unitControl.optString("errorCode", (String) null);
            tbDetailResultVO.itemControl.unitControl.errorLink = unitControl.optString("errorLink", (String) null);
            tbDetailResultVO.itemControl.unitControl.errorMessage = unitControl.optString("errorMessage", (String) null);
            tbDetailResultVO.itemControl.unitControl.limitCount = Integer.valueOf(unitControl.optInt("limitCount"));
            tbDetailResultVO.itemControl.unitControl.limitMultipleCount = Integer.valueOf(unitControl.optInt("limitMultipleCount"));
            tbDetailResultVO.itemControl.unitControl.limitMultipleText = unitControl.optString("limitMultipleText", (String) null);
            tbDetailResultVO.itemControl.unitControl.offShelfUrl = unitControl.optString("offShelfUrl", (String) null);
            tbDetailResultVO.itemControl.unitControl.submitText = unitControl.optString("submitText", (String) null);
            tbDetailResultVO.itemControl.unitControl.unitTip = unitControl.optString("unitTip", (String) null);
            JSONObject beforeBuyApi = unitControl.optJSONObject("beforeBuyApi");
            if (beforeBuyApi != null) {
                tbDetailResultVO.itemControl.unitControl.beforeBuyApi = new Unit();
                tbDetailResultVO.itemControl.unitControl.beforeBuyApi.name = beforeBuyApi.optString("name", (String) null);
                tbDetailResultVO.itemControl.unitControl.beforeBuyApi.value = beforeBuyApi.optString("value", (String) null);
            }
            JSONObject beforeCartApi = unitControl.optJSONObject("beforeCartApi");
            if (beforeCartApi != null) {
                tbDetailResultVO.itemControl.unitControl.beforeCartApi = new Unit();
                tbDetailResultVO.itemControl.unitControl.beforeCartApi.name = beforeCartApi.optString("name", (String) null);
                tbDetailResultVO.itemControl.unitControl.beforeCartApi.value = beforeCartApi.optString("value", (String) null);
            }
        }
    }

    private static void resolve_Tips(TBDetailResultVO tbDetailResultVO, JSONObject tips) {
        JSONArray tip_list_json;
        tbDetailResultVO.tips = new HashMap();
        Iterator<String> keys = tips.keys();
        while (keys.hasNext()) {
            String key = String.valueOf(keys.next());
            if (!TextUtils.isEmpty(key) && (tip_list_json = tips.optJSONArray(key)) != null && tip_list_json.length() > 0) {
                List<TipDO> tipdo_list = new ArrayList<>();
                int length = tip_list_json.length();
                for (int i = 0; i < length; i++) {
                    JSONObject tip_json = tip_list_json.optJSONObject(i);
                    if (tip_json != null) {
                        TipDO tipDO = new TipDO();
                        tipDO.html = tip_json.optString("html", (String) null);
                        tipDO.logo = tip_json.optString("logo", (String) null);
                        tipDO.txt = tip_json.optString("txt", (String) null);
                        tipDO.url = tip_json.optString("url", (String) null);
                        tipdo_list.add(tipDO);
                    }
                    tbDetailResultVO.tips.put(key, tipdo_list);
                }
            }
        }
    }

    private static List<DetailVO.StaticItem.GuaranteeInfo.Guarantee> resolve_Guarantees(JSONArray guarantees) {
        List<DetailVO.StaticItem.GuaranteeInfo.Guarantee> guarantees_list = new ArrayList<>();
        int length = guarantees.length();
        for (int i = 0; i < length; i++) {
            JSONObject guarantee_obj = guarantees.optJSONObject(i);
            if (guarantee_obj != null) {
                DetailVO.StaticItem.GuaranteeInfo.Guarantee guarantee = new DetailVO.StaticItem.GuaranteeInfo.Guarantee();
                guarantee.icon = guarantee_obj.optString("icon", (String) null);
                guarantee.title = guarantee_obj.optString("title", (String) null);
                JSONObject actionUrl = guarantee_obj.optJSONObject("actionUrl");
                if (actionUrl != null) {
                    guarantee.actionUrl = new Unit();
                    guarantee.actionUrl.name = actionUrl.optString("name", (String) null);
                    guarantee.actionUrl.value = actionUrl.optString("value", (String) null);
                }
                guarantees_list.add(guarantee);
            }
        }
        return guarantees_list;
    }

    private static List<PriceUnit> resolve_PriceUnit(JSONArray priceUnits) {
        if (priceUnits == null || priceUnits.length() <= 0) {
            return null;
        }
        List<PriceUnit> priceUnits_list = new ArrayList<>();
        int length = priceUnits.length();
        for (int i = 0; i < length; i++) {
            JSONObject price_obj = priceUnits.optJSONObject(i);
            if (price_obj != null) {
                PriceUnit priceUnit = new PriceUnit();
                priceUnit.display = Integer.valueOf(price_obj.optInt("display"));
                priceUnit.name = price_obj.optString("name", (String) null);
                priceUnit.preName = price_obj.optString("preName", (String) null);
                priceUnit.prePayName = price_obj.optString("prePayName", (String) null);
                priceUnit.prePayPrice = price_obj.optString("prePayPrice", (String) null);
                priceUnit.price = price_obj.optString("price", (String) null);
                priceUnit.priceTitle = price_obj.optString("priceTitle", (String) null);
                priceUnits_list.add(priceUnit);
            }
        }
        return priceUnits_list;
    }

    private static List<DetailVO.StaticItem.SaleInfo.SkuProp> resolve_SkuModel_skuProps(JSONArray skuProps) {
        if (skuProps == null || skuProps.length() <= 0) {
            return null;
        }
        List<DetailVO.StaticItem.SaleInfo.SkuProp> skuProps_List = new ArrayList<>();
        int length = skuProps.length();
        for (int i = 0; i < length; i++) {
            JSONObject obj = skuProps.optJSONObject(i);
            if (obj != null) {
                DetailVO.StaticItem.SaleInfo.SkuProp skuProp = new DetailVO.StaticItem.SaleInfo.SkuProp();
                skuProp.propId = Long.valueOf(obj.optLong("propId"));
                skuProp.propName = obj.optString("propName", (String) null);
                JSONArray values = obj.optJSONArray(SampleConfigConstant.VALUES);
                if (values != null && values.length() > 0) {
                    skuProp.values = new ArrayList();
                    int len = values.length();
                    for (int k = 0; k < len; k++) {
                        JSONObject obj_value = values.optJSONObject(k);
                        if (obj_value != null) {
                            DetailVO.StaticItem.SaleInfo.SkuProp.SkuPropValue skuPropValue = new DetailVO.StaticItem.SaleInfo.SkuProp.SkuPropValue();
                            skuPropValue.valueId = Long.valueOf(obj_value.optLong("valueId"));
                            skuPropValue.name = obj_value.optString("name", (String) null);
                            skuPropValue.imgUrl = obj_value.optString("imgUrl", (String) null);
                            skuPropValue.propId = obj_value.optString("propId", (String) null);
                            skuPropValue.valueAlias = obj_value.optString("valueAlias", (String) null);
                            skuProp.values.add(skuPropValue);
                        }
                    }
                }
                skuProps_List.add(skuProp);
            }
        }
        return skuProps_List;
    }

    private static Map<String, String> resolveToMap_String(JSONObject obj_map) {
        if (obj_map == null) {
            return null;
        }
        Map<String, String> json_map = new HashMap<>();
        Iterator<String> keys = obj_map.keys();
        while (keys.hasNext()) {
            String key = String.valueOf(keys.next());
            String value = (String) obj_map.opt(key);
            if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                json_map.put(key, value);
            }
        }
        return json_map;
    }
}
