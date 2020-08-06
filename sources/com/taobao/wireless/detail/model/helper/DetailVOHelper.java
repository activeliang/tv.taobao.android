package com.taobao.wireless.detail.model.helper;

import com.alibaba.fastjson.JSON;
import com.taobao.detail.clientDomain.TBDetailResultVO;
import com.taobao.detail.domain.DetailVO;
import com.taobao.detail.domain.base.PriceUnit;
import com.taobao.wireless.detail.model.vo.BaseControl;
import com.taobao.wireless.lang.CheckUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mtopsdk.common.util.SymbolExpUtil;

public class DetailVOHelper {
    public static boolean isSkuItem(TBDetailResultVO tbDetailResultVO) {
        return (tbDetailResultVO.skuModel == null || tbDetailResultVO.skuModel.skuProps == null) ? false : true;
    }

    public static boolean isAreaSold(TBDetailResultVO tbDetailResultVO) {
        return (tbDetailResultVO.delivery == null || tbDetailResultVO.delivery.saleRegionInfo == null) ? false : true;
    }

    public static boolean hasService(TBDetailResultVO tbDetailResultVO) {
        return (tbDetailResultVO.serviceInfo == null || tbDetailResultVO.serviceInfo.serIdMap == null) ? false : true;
    }

    public static String getItemNumId(TBDetailResultVO tbDetailResultVO) {
        return tbDetailResultVO.itemInfoModel.itemId;
    }

    public static boolean isCascade(TBDetailResultVO tbDetailResultVO) {
        return (tbDetailResultVO.skuModel == null || tbDetailResultVO.skuModel.cascadeInfo == null) ? false : true;
    }

    public static String getMainPic(TBDetailResultVO tbDetailResultVO) {
        if (tbDetailResultVO.itemInfoModel == null || CheckUtils.isEmpty((List) tbDetailResultVO.itemInfoModel.picsPath)) {
            return null;
        }
        return tbDetailResultVO.itemInfoModel.picsPath.get(0);
    }

    public static String getSkuTitles(TBDetailResultVO tbDetailResultVO) {
        if (tbDetailResultVO.skuModel == null || CheckUtils.isEmpty((List) tbDetailResultVO.skuModel.skuProps)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (DetailVO.StaticItem.SaleInfo.SkuProp item : tbDetailResultVO.skuModel.skuProps) {
            sb.append(item.propName);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static void calControl(BaseControl controlVO, TBDetailResultVO tbDetailResultVO, String skuId) {
        DetailVO.DynamicItem.SkuPriceAndQuanitiy skuPriceAndQuanitiy;
        int i = 0;
        if (CheckUtils.isEmpty(skuId)) {
            TBDetailResultVO.ItemInfoModel itemInfoModel = tbDetailResultVO.itemInfoModel;
            controlVO.quantity = (int) (itemInfoModel.quantity == null ? 0 : itemInfoModel.quantity.longValue());
            controlVO.quantityText = itemInfoModel.quantityText == null ? "" : itemInfoModel.quantityText;
            if (itemInfoModel.priceUnits == null || itemInfoModel.priceUnits.size() == 0) {
                controlVO.price = "";
            } else {
                PriceUnit priceUnit = itemInfoModel.priceUnits.get(0);
                controlVO.price = priceUnit.price;
                if (!CheckUtils.isEmpty(priceUnit.prePayPrice)) {
                    controlVO.otherPrice = priceUnit.prePayPrice;
                    controlVO.otherPriceName = CheckUtils.isEmpty(priceUnit.prePayName) ? "订金" : priceUnit.prePayName;
                }
            }
        } else if (!(tbDetailResultVO.skuModel.skus == null || (skuPriceAndQuanitiy = tbDetailResultVO.skuModel.skus.get(skuId)) == null)) {
            controlVO.quantity = skuPriceAndQuanitiy.quantity == null ? 0 : skuPriceAndQuanitiy.quantity.intValue();
            controlVO.quantityText = skuPriceAndQuanitiy.quantityText == null ? "" : skuPriceAndQuanitiy.quantityText;
            if (skuPriceAndQuanitiy.priceUnits == null || skuPriceAndQuanitiy.priceUnits.size() == 0) {
                controlVO.price = "";
            } else {
                PriceUnit priceUnit2 = skuPriceAndQuanitiy.priceUnits.get(0);
                controlVO.price = priceUnit2.price;
                if (!CheckUtils.isEmpty(priceUnit2.prePayPrice)) {
                    controlVO.otherPrice = priceUnit2.prePayPrice;
                    controlVO.otherPriceName = CheckUtils.isEmpty(priceUnit2.prePayName) ? "订金" : priceUnit2.prePayName;
                }
            }
        }
        DetailVO.DynamicItem.ItemControl.UnitControl unitControl = getTradeControl(tbDetailResultVO, skuId);
        if (unitControl != null) {
            controlVO.buySupport = unitControl.buySupport;
            controlVO.cartSupport = unitControl.cartSupport;
            controlVO.msgTip = unitControl.errorMessage;
            controlVO.buyText = unitControl.buyText;
            controlVO.cartText = unitControl.cartText;
            controlVO.beforeBuyApi = unitControl.beforeBuyApi;
            controlVO.beforeCartApi = unitControl.beforeCartApi;
            if (unitControl.limitCount != null) {
                i = unitControl.limitCount.intValue();
            }
            controlVO.limitCount = i;
            controlVO.limitMultiCount = unitControl.limitMultipleCount == null ? 1 : unitControl.limitMultipleCount.intValue();
            controlVO.limitMultiText = unitControl.limitMultipleText;
            controlVO.hintBanner = unitControl.hintBanner;
            controlVO.basetime = unitControl.baseTime;
        }
    }

    public static Set<String> descartes(Set<String> ppath) {
        Set<String> result = new HashSet<>();
        for (String path : ppath) {
            String[] ids = path.split(SymbolExpUtil.SYMBOL_SEMICOLON);
            int times = ((int) Math.pow(2.0d, (double) ids.length)) - 1;
            for (int i = 1; i <= times; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < ids.length; j++) {
                    if (((1 << j) & i) > 0) {
                        sb.append(SymbolExpUtil.SYMBOL_SEMICOLON).append(ids[j]);
                    }
                }
                result.add(sb.substring(1));
            }
        }
        return result;
    }

    public static DetailVO.DynamicItem.ItemControl.UnitControl getTradeControl(TBDetailResultVO detailResultVO, String skuId) {
        if (detailResultVO.itemControl == null) {
            return mockUnsupportControl();
        }
        DetailVO.DynamicItem.ItemControl itemControl = detailResultVO.itemControl;
        if (CheckUtils.isEmpty(skuId) && itemControl.unitControl == null) {
            return mockUnsupportControl();
        }
        if (CheckUtils.isEmpty(skuId) || itemControl.skuControl == null) {
            return itemControl.unitControl;
        }
        DetailVO.DynamicItem.ItemControl.UnitControl skuControl = itemControl.skuControl.get(skuId);
        return skuControl == null ? mockUnsupportControl() : skuControl;
    }

    private static DetailVO.DynamicItem.ItemControl.UnitControl mockUnsupportControl() {
        DetailVO.DynamicItem.ItemControl.UnitControl unitControl = new DetailVO.DynamicItem.ItemControl.UnitControl();
        unitControl.buySupport = false;
        unitControl.cartSupport = false;
        unitControl.errorMessage = "暂不支持";
        return unitControl;
    }

    public static final <T> T getFeatureObj(TBDetailResultVO tbDetailResultVO, String key, Class<T> clazz) {
        Object obj;
        if (tbDetailResultVO.featureMap == null || (obj = tbDetailResultVO.featureMap.get(key)) == null) {
            return null;
        }
        String json = obj.toString();
        if (CheckUtils.isEmpty(json)) {
            return null;
        }
        try {
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static final boolean hasFeatureType(TBDetailResultVO tbDetailResultVO, String proType) {
        if (tbDetailResultVO.displayType == null) {
            return false;
        }
        for (String type : tbDetailResultVO.displayType) {
            if (proType.equals(type)) {
                return true;
            }
        }
        return false;
    }
}
