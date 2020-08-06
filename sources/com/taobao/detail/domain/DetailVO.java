package com.taobao.detail.domain;

import com.taobao.detail.clientDomain.TaobaoPreSaleInfo;
import com.taobao.detail.domain.base.ActionUnit;
import com.taobao.detail.domain.base.BaseVO;
import com.taobao.detail.domain.base.ButtonUnit;
import com.taobao.detail.domain.base.PriceUnit;
import com.taobao.detail.domain.base.TipDO;
import com.taobao.detail.domain.base.Unit;
import com.taobao.detail.domain.biz.InstallmentInfo;
import com.taobao.detail.domain.biz.SaleRegionInfo;
import com.taobao.detail.domain.biz.ServiceInfo;
import com.taobao.detail.domain.component.BaseInputView;
import com.taobao.detail.domain.control.WaitingControl;
import com.taobao.detail.domain.o2o.O2OInfo;
import com.taobao.detail.domain.rate.RateDetail;
import com.taobao.detail.domain.rate.RateTag;
import com.taobao.detail.domain.template.LayoutInfo;
import com.taobao.wireless.lang.CheckUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DetailVO implements Serializable {
    public static final String MOCK_DYN = "defDyn";
    public List<Unit> apiStack;
    public long buyerId;
    public DynamicItem dynamicItem;
    public Map<String, Object> extras;
    public Map<String, Object> featureMap = new HashMap();
    public boolean forbid;
    public Map<String, Object> queryParams;
    public StaticItem staticItem;

    public static class StaticItem implements Serializable {
        public DescInfo descInfo = new DescInfo();
        public List<String> displayType;
        public GuaranteeInfo guaranteeInfo = new GuaranteeInfo();
        public ItemInfo itemInfo = new ItemInfo();
        public O2OInfo o2oInfo;
        public ParamInfo paramInfo;
        public List<Unit> props = new ArrayList();
        public RateInfo rateInfo = new RateInfo();
        public SaleInfo saleInfo = new SaleInfo();
        public Seller seller = new Seller();
        public TaobaoPreSaleInfo taobaoPreSaleInfo;
        public Map<String, String> trackAllParams;
        public Map<String, String> trackParams;

        public static class DescInfo extends BaseVO {
            public String briefDescUrl;
            public String fullDescUrl;
            public String h5DescUrl;
            public String h5DescUrl2;
            public String moduleDescUrl;
            public String moduleDescUrl2;
            public String pcDescUrl;
            public String showFullDetailDesc;
            public String wapDescUrl;
        }

        public static class ItemInfo extends BaseVO {
            public String categoryId;
            public String comboH5Url;
            public Unit comboUrl;
            public Long favcount;
            public String itemIcon;
            public String itemId;
            public String itemTypeLogo;
            public String itemTypeName;
            public String itemUrl;
            public String location;
            public List<String> picsPath;
            public String saleLine;
            public Boolean sku;
            public String startTime;
            public String stuffStatus;
            public String subTitle;
            public String title;
            public List<String> videosPath;
            public String weight;
        }

        public static class ParamInfo implements Serializable {
            public Map<String, String> buyParam;
            public Map<String, String> cartParam;
        }

        public static class RateInfo extends BaseVO {
            public Integer rateCounts;
            public List<RateDetail> rateDetailList;
            public List<RateTag> tagList;
        }

        public static class SaleInfo extends BaseVO {
            public CascadeInfo cascadeInfo;
            public ArrayList<BaseInputView> components;
            public String h5SkuUrl;
            public Map<String, String> ppathIdmap;
            public List<SkuProp> skuProps;
            public String skuTitle;

            public static class CascadeInfo implements Serializable {
                public Map<Long, Integer> casPropDepthMap = new HashMap();
                public Set<Long> rootPropIds = new HashSet();
                public Map<String, List<SKUCascadeVO>> skuCascadeMap = new HashMap();
                public Map<Long, List<SKUCascadeVO>> topSkuPVMap = new HashMap();

                public static class SKUCascadeVO implements Serializable {
                    public String actualValueText;
                    public String propertyText;
                    public String propertyValueId;
                    public Long valueId;
                }
            }

            public static class SkuProp implements Serializable {
                public Long propId;
                public String propName;
                public List<SkuPropValue> values;

                public static class SkuPropValue implements Serializable {
                    public List<String> cascadeTitles;
                    public String imgUrl;
                    public String name;
                    public String propId;
                    public List<SkuPropValue> subProps;
                    public String valueAlias;
                    public Long valueId;
                }
            }
        }

        public static class Seller extends BaseVO {
            public List<ActionUnit> actionUnits;
            public String bailAmount;
            public List<ButtonUnit> buttons;
            public String certificateLogo;
            public String certify;
            public Integer creditLevel;
            public String distance;
            public List<EvaluateInfoVO> evaluateInfo;
            public String fansCount;
            public String fansCountText;
            public String goodRatePercentage;
            public String hideDsr;
            public String hideWangwang;
            public String nick;
            public String o2oMapUrl;
            public String picUrl;
            public String shopBrand;
            public Integer shopCollectorCount;
            public String shopIcon;
            public Integer shopId;
            public String shopLocation;
            public String shopPromtionType;
            public String shopTitle;
            public String shopType;
            public String starts;
            public String tollFreeNumber;
            public String tollFreeSubNumber;
            public String type;
            public Long userNumId;
            public Long weitaoId;

            public static class EvaluateInfoVO implements Serializable {
                public String highGap;
                public String name;
                public String score;
                public String title;
            }
        }

        public static class GuaranteeInfo extends BaseVO {
            @Deprecated
            public List<Guarantee> afterGuarantees;
            @Deprecated
            public List<Guarantee> beforeGuarantees;
            @Deprecated
            public List<Guarantee> guarantees;

            public List<Guarantee> list() {
                List<Guarantee> list = new ArrayList<>();
                if (!CheckUtils.isEmpty((List) this.beforeGuarantees)) {
                    list.addAll(this.beforeGuarantees);
                }
                if (!CheckUtils.isEmpty((List) this.guarantees)) {
                    list.addAll(this.guarantees);
                }
                if (!CheckUtils.isEmpty((List) this.afterGuarantees)) {
                    list.addAll(this.afterGuarantees);
                }
                return list;
            }

            public static class Guarantee implements Serializable {
                public Unit actionUrl;
                public String icon;
                public String title;

                public Guarantee(String title2, String icon2) {
                    this.title = title2;
                    this.icon = icon2;
                }

                public Guarantee(String title2) {
                    this.title = title2;
                }

                public Guarantee() {
                }
            }
        }
    }

    public static class DynamicItem implements Serializable {
        public Map<String, String> abTestInfo;
        public Delivery delivery;
        public Double11Coupon double11Coupon;
        public boolean installmentEnable = false;
        public Map<String, List<InstallmentInfo>> installmentInfos;
        public boolean isMakeup;
        public ItemControl itemControl;
        public LayoutInfo layoutInfo;
        public String payedCount;
        public String points;
        public List<PriceUnit> priceUnits;
        public Long quantity;
        public String quantityText;
        public ServiceInfo serviceInfo;
        public ShareInfo shareInfo;
        public ShopPromotion shopPromotion;
        public String skuTitle;
        public Map<String, SkuPriceAndQuanitiy> skus;
        public String soldQuantityText;
        public StageInfo stageInfo;
        public List<String> subInfos;
        public Map<String, List<TipDO>> tips;
        public Integer totalSoldQuantity;

        public static class Coupon implements Serializable {
            public Long conponId;
            public String couponName;
            public String end;
            public String hasApply;
            public String start;
            public String startFee;
            public Integer type;
            public String value;
        }

        public static class Delivery extends BaseVO {
            @Deprecated
            public String areaId;
            public Unit areaListApi;
            public List<String> deliveryFees;
            public String destination;
            @Deprecated
            public Unit getAreaApi;
            @Deprecated
            public SaleRegionInfo saleRegionInfo;
            @Deprecated
            public Unit updateAreaApi;
        }

        public static class Double11Coupon extends BaseVO {
            public String logo;
            public String txt;
            public String url;
        }

        public static class ShareInfo extends BaseVO {
            public int iconType = 1;
            public String name = "分享";
            public Map<String, String> params;
        }

        public static class ShopPromotion extends BaseVO {
            public List<Coupon> couponList;
            public List<String> descriptions;
            public String freeText;
            public String freeUrl;
            public String promotionData;
            public Boolean superAct;
            public String title;
        }

        public static class SkuPriceAndQuanitiy extends BaseVO {
            public List<PriceUnit> priceUnits;
            public Integer quantity;
            public String quantityText;
            public String simplePrice;
        }

        public static class StageInfo extends BaseVO {
            public List<String> descriptions;
            public String title;
        }

        public static class ItemControl implements Serializable {
            public String buyUrl;
            public String degradedItemUrl;
            public Map<String, UnitControl> skuControl;
            public Boolean smartbanner;
            public UnitControl unitControl;
            public WaitingControl waitingControl;

            public static class UnitControl implements Serializable {
                public String baseTime;
                public Unit beforeBuyApi;
                public Unit beforeCartApi;
                public boolean buySupport = true;
                public String buyText = "立即购买";
                public boolean cartSupport = true;
                public String cartText = "加入购物车";
                public String errorCode;
                public String errorLink;
                public String errorMessage;
                public HintBanner hintBanner;
                public Integer limitCount;
                public Integer limitMultipleCount = 1;
                public String limitMultipleText;
                public String offShelfUrl;
                public String submitText = "立即购买";
                public String unitTip;

                public void setSupportCartTrue() {
                    this.cartSupport = this.cartSupport && this.cartSupport;
                }

                public void setSupportBuyTrue() {
                    this.buySupport = this.buySupport && this.buySupport;
                }

                public void setSupportCartFalse() {
                    this.cartSupport = false;
                }

                public void setSupportBuyFalse() {
                    this.buySupport = false;
                }

                public void setCartSupport(boolean cartSupport2) {
                    this.cartSupport = cartSupport2 && this.cartSupport;
                }

                public void setBuySupport(boolean buySupport2) {
                    this.buySupport = buySupport2 && this.buySupport;
                }
            }
        }
    }
}
