package com.taobao.detail.clientDomain;

import com.taobao.detail.domain.DetailVO;
import com.taobao.detail.domain.base.BaseVO;
import com.taobao.detail.domain.base.PriceUnit;
import com.taobao.detail.domain.base.TipDO;
import com.taobao.detail.domain.base.Unit;
import com.taobao.detail.domain.biz.InstallmentInfo;
import com.taobao.detail.domain.biz.ServiceInfo;
import com.taobao.detail.domain.component.BaseInputView;
import com.taobao.detail.domain.meal.ComboInfo;
import com.taobao.detail.domain.o2o.O2OInfo;
import com.taobao.detail.domain.template.LayoutInfo;
import com.taobao.mtop.api.MtopRequest;
import com.taobao.wireless.detail.api.DetailResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TBDetailResultVO implements Serializable {
    public Map<String, String> abTestInfo;
    public List<Unit> apiStack;
    public ComboInfo comboInfo;
    public DetailVO.DynamicItem.Delivery delivery;
    public DetailVO.StaticItem.DescInfo descInfo;
    public String[] displayType;
    public DetailVO.DynamicItem.Double11Coupon double11Coupon;
    public String errorCode;
    public String errorMessage;
    @Deprecated
    public List<DetailResponse> errorResponse;
    public Map<String, Object> extras;
    public Map<String, Object> featureMap;
    public DetailVO.StaticItem.GuaranteeInfo guaranteeInfo;
    public DetailVO.DynamicItem.ItemControl itemControl;
    public ItemInfoModel itemInfoModel;
    public LayoutInfo layoutData;
    public O2OInfo o2oInfo;
    public DetailVO.StaticItem.ParamInfo paramInfo;
    public List<Unit> props;
    public DetailVO.StaticItem.RateInfo rateInfo;
    public String redirectUrl;
    public DetailVO.StaticItem.Seller seller;
    public ServiceInfo serviceInfo;
    public DetailVO.DynamicItem.ShareInfo shareInfo;
    public DetailVO.DynamicItem.ShopPromotion shopPromotion;
    public SkuModel skuModel;
    public DetailVO.DynamicItem.StageInfo stageInfo;
    public List<String> subInfos;
    public TaobaoPreSaleInfo taobaoPreSaleInfo;
    public Map<String, List<TipDO>> tips;
    public Map<String, String> trackAllParams;
    public Map<String, String> trackParams;
    public List<WeAppItemModel> weappList;

    public static class ItemInfoModel implements Serializable {
        public String categoryId;
        public Long favcount;
        public boolean isMakeup;
        public String itemIcon;
        public String itemId;
        public String itemTypeLogo;
        public String itemTypeName;
        public String itemUrl;
        public String location;
        public String payedCount;
        public List<String> picsPath;
        public String points;
        public List<PriceUnit> priceUnits;
        public Long quantity;
        public String quantityText;
        public String saleLine;
        public Boolean sku;
        public String soldQuantityText;
        public String startTime;
        public String stuffStatus;
        public String subTitle;
        public String title;
        public Integer totalSoldQuantity;
        public List<String> videosPath;
        public String weight;
    }

    public static class SkuModel extends BaseVO {
        public DetailVO.StaticItem.SaleInfo.CascadeInfo cascadeInfo;
        public ArrayList<BaseInputView> components;
        public boolean installmentEnable = false;
        public Map<String, List<InstallmentInfo>> installmentInfos;
        public Map<String, String> ppathIdmap;
        public List<DetailVO.StaticItem.SaleInfo.SkuProp> skuProps;
        public String skuTitle;
        public Map<String, DetailVO.DynamicItem.SkuPriceAndQuanitiy> skus;
    }

    public static class WeAppItemModel implements Serializable {
        public String identifier;
        public MtopRequest mtopModel;
    }
}
