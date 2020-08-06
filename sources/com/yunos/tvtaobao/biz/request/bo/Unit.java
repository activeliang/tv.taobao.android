package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class Unit {
    private BuyerBean buyer;
    private ConsumerProtectionBean consumerProtection;
    private DebugDataBean debugData;
    private DeliveryBean delivery;
    private FeatureBean feature;
    private ItemBean item;
    private List<ItemsBean> items;
    private LayoutBean layout;
    private List<?> modules;
    private OtherInfoBean otherInfo;
    private ParamsBean params;
    private PriceBeanX price;
    private ResourceBean resource;
    private ServicesBean services;
    private SkuBaseBean skuBase;
    private SkuCoreBean skuCore;
    private SkuVerticalBean skuVertical;
    private TradeBean trade;
    private VerticalBean vertical;

    public static class DebugDataBean {
    }

    public List<ItemsBean> getItems() {
        return this.items;
    }

    public void setItems(List<ItemsBean> items2) {
        this.items = items2;
    }

    public FeatureBean getFeature() {
        return this.feature;
    }

    public void setFeature(FeatureBean feature2) {
        this.feature = feature2;
    }

    public TradeBean getTrade() {
        return this.trade;
    }

    public void setTrade(TradeBean trade2) {
        this.trade = trade2;
    }

    public ItemBean getItem() {
        return this.item;
    }

    public void setItem(ItemBean item2) {
        this.item = item2;
    }

    public BuyerBean getBuyer() {
        return this.buyer;
    }

    public void setBuyer(BuyerBean buyer2) {
        this.buyer = buyer2;
    }

    public PriceBeanX getPrice() {
        return this.price;
    }

    public void setPrice(PriceBeanX price2) {
        this.price = price2;
    }

    public ConsumerProtectionBean getConsumerProtection() {
        return this.consumerProtection;
    }

    public void setConsumerProtection(ConsumerProtectionBean consumerProtection2) {
        this.consumerProtection = consumerProtection2;
    }

    public ResourceBean getResource() {
        return this.resource;
    }

    public void setResource(ResourceBean resource2) {
        this.resource = resource2;
    }

    public VerticalBean getVertical() {
        return this.vertical;
    }

    public void setVertical(VerticalBean vertical2) {
        this.vertical = vertical2;
    }

    public DeliveryBean getDelivery() {
        return this.delivery;
    }

    public void setDelivery(DeliveryBean delivery2) {
        this.delivery = delivery2;
    }

    public SkuBaseBean getSkuBase() {
        return this.skuBase;
    }

    public void setSkuBase(SkuBaseBean skuBase2) {
        this.skuBase = skuBase2;
    }

    public SkuCoreBean getSkuCore() {
        return this.skuCore;
    }

    public void setSkuCore(SkuCoreBean skuCore2) {
        this.skuCore = skuCore2;
    }

    public ServicesBean getServices() {
        return this.services;
    }

    public void setServices(ServicesBean services2) {
        this.services = services2;
    }

    public SkuVerticalBean getSkuVertical() {
        return this.skuVertical;
    }

    public void setSkuVertical(SkuVerticalBean skuVertical2) {
        this.skuVertical = skuVertical2;
    }

    public DebugDataBean getDebugData() {
        return this.debugData;
    }

    public void setDebugData(DebugDataBean debugData2) {
        this.debugData = debugData2;
    }

    public ParamsBean getParams() {
        return this.params;
    }

    public void setParams(ParamsBean params2) {
        this.params = params2;
    }

    public OtherInfoBean getOtherInfo() {
        return this.otherInfo;
    }

    public void setOtherInfo(OtherInfoBean otherInfo2) {
        this.otherInfo = otherInfo2;
    }

    public LayoutBean getLayout() {
        return this.layout;
    }

    public void setLayout(LayoutBean layout2) {
        this.layout = layout2;
    }

    public List<?> getModules() {
        return this.modules;
    }

    public void setModules(List<?> modules2) {
        this.modules = modules2;
    }

    public static class FeatureBean {
        private String bundleItem;
        private String cainiaoNoramal;
        private String fmcgRecommend;
        private String hasAddCartCoudan;
        private String hasApparelIcon;
        private String hasCartRecommend;
        private String hasCombo;
        private String hasCoupon;
        private String hasIntervalPrice;
        private String hasMeal;
        private String hasNewCombo;
        private String hasQualification;
        private String hasSku;
        private String hideSMww;
        private String hideShopDsr;
        private String hotItem;
        private String includeSkuData;
        private String isTspace;
        private String makeup;
        private String multistage;
        private String nabundleItem;
        private String o2O;
        private String openGradient;
        private String pintuan;
        private String pricedCoupon;
        private String recommendReason;
        private String renovation;
        private String showCuntaoTag;
        private String showSMww;
        private String showShopCard;
        private String showSku;
        private String showYaoDai;
        private String superActTime;
        private String switchToOldApp;
        private String useMeiLiHuiPrice;
        private String waitForStart;

        public String getMakeup() {
            return this.makeup;
        }

        public void setMakeup(String makeup2) {
            this.makeup = makeup2;
        }

        public String getRenovation() {
            return this.renovation;
        }

        public void setRenovation(String renovation2) {
            this.renovation = renovation2;
        }

        public String getHasAddCartCoudan() {
            return this.hasAddCartCoudan;
        }

        public void setHasAddCartCoudan(String hasAddCartCoudan2) {
            this.hasAddCartCoudan = hasAddCartCoudan2;
        }

        public String getShowCuntaoTag() {
            return this.showCuntaoTag;
        }

        public void setShowCuntaoTag(String showCuntaoTag2) {
            this.showCuntaoTag = showCuntaoTag2;
        }

        public String getRecommendReason() {
            return this.recommendReason;
        }

        public void setRecommendReason(String recommendReason2) {
            this.recommendReason = recommendReason2;
        }

        public String getSwitchToOldApp() {
            return this.switchToOldApp;
        }

        public void setSwitchToOldApp(String switchToOldApp2) {
            this.switchToOldApp = switchToOldApp2;
        }

        public String getUseMeiLiHuiPrice() {
            return this.useMeiLiHuiPrice;
        }

        public void setUseMeiLiHuiPrice(String useMeiLiHuiPrice2) {
            this.useMeiLiHuiPrice = useMeiLiHuiPrice2;
        }

        public String getHideShopDsr() {
            return this.hideShopDsr;
        }

        public void setHideShopDsr(String hideShopDsr2) {
            this.hideShopDsr = hideShopDsr2;
        }

        public String getHasCombo() {
            return this.hasCombo;
        }

        public void setHasCombo(String hasCombo2) {
            this.hasCombo = hasCombo2;
        }

        public String getO2O() {
            return this.o2O;
        }

        public void setO2O(String o2O2) {
            this.o2O = o2O2;
        }

        public String getPricedCoupon() {
            return this.pricedCoupon;
        }

        public void setPricedCoupon(String pricedCoupon2) {
            this.pricedCoupon = pricedCoupon2;
        }

        public String getHasCoupon() {
            return this.hasCoupon;
        }

        public void setHasCoupon(String hasCoupon2) {
            this.hasCoupon = hasCoupon2;
        }

        public String getCainiaoNoramal() {
            return this.cainiaoNoramal;
        }

        public void setCainiaoNoramal(String cainiaoNoramal2) {
            this.cainiaoNoramal = cainiaoNoramal2;
        }

        public String getShowSku() {
            return this.showSku;
        }

        public void setShowSku(String showSku2) {
            this.showSku = showSku2;
        }

        public String getShowShopCard() {
            return this.showShopCard;
        }

        public void setShowShopCard(String showShopCard2) {
            this.showShopCard = showShopCard2;
        }

        public String getShowYaoDai() {
            return this.showYaoDai;
        }

        public void setShowYaoDai(String showYaoDai2) {
            this.showYaoDai = showYaoDai2;
        }

        public String getNabundleItem() {
            return this.nabundleItem;
        }

        public void setNabundleItem(String nabundleItem2) {
            this.nabundleItem = nabundleItem2;
        }

        public String getHasSku() {
            return this.hasSku;
        }

        public void setHasSku(String hasSku2) {
            this.hasSku = hasSku2;
        }

        public String getBundleItem() {
            return this.bundleItem;
        }

        public void setBundleItem(String bundleItem2) {
            this.bundleItem = bundleItem2;
        }

        public String getHasNewCombo() {
            return this.hasNewCombo;
        }

        public void setHasNewCombo(String hasNewCombo2) {
            this.hasNewCombo = hasNewCombo2;
        }

        public String getPintuan() {
            return this.pintuan;
        }

        public void setPintuan(String pintuan2) {
            this.pintuan = pintuan2;
        }

        public String getHasCartRecommend() {
            return this.hasCartRecommend;
        }

        public void setHasCartRecommend(String hasCartRecommend2) {
            this.hasCartRecommend = hasCartRecommend2;
        }

        public String getFmcgRecommend() {
            return this.fmcgRecommend;
        }

        public void setFmcgRecommend(String fmcgRecommend2) {
            this.fmcgRecommend = fmcgRecommend2;
        }

        public String getSuperActTime() {
            return this.superActTime;
        }

        public void setSuperActTime(String superActTime2) {
            this.superActTime = superActTime2;
        }

        public String getHasApparelIcon() {
            return this.hasApparelIcon;
        }

        public void setHasApparelIcon(String hasApparelIcon2) {
            this.hasApparelIcon = hasApparelIcon2;
        }

        public String getMultistage() {
            return this.multistage;
        }

        public void setMultistage(String multistage2) {
            this.multistage = multistage2;
        }

        public String getHasQualification() {
            return this.hasQualification;
        }

        public void setHasQualification(String hasQualification2) {
            this.hasQualification = hasQualification2;
        }

        public String getOpenGradient() {
            return this.openGradient;
        }

        public void setOpenGradient(String openGradient2) {
            this.openGradient = openGradient2;
        }

        public String getIsTspace() {
            return this.isTspace;
        }

        public void setIsTspace(String isTspace2) {
            this.isTspace = isTspace2;
        }

        public String getHasIntervalPrice() {
            return this.hasIntervalPrice;
        }

        public void setHasIntervalPrice(String hasIntervalPrice2) {
            this.hasIntervalPrice = hasIntervalPrice2;
        }

        public String getHotItem() {
            return this.hotItem;
        }

        public void setHotItem(String hotItem2) {
            this.hotItem = hotItem2;
        }

        public String getHasMeal() {
            return this.hasMeal;
        }

        public void setHasMeal(String hasMeal2) {
            this.hasMeal = hasMeal2;
        }

        public String getShowSMww() {
            return this.showSMww;
        }

        public void setShowSMww(String showSMww2) {
            this.showSMww = showSMww2;
        }

        public String getIncludeSkuData() {
            return this.includeSkuData;
        }

        public void setIncludeSkuData(String includeSkuData2) {
            this.includeSkuData = includeSkuData2;
        }

        public String getWaitForStart() {
            return this.waitForStart;
        }

        public void setWaitForStart(String waitForStart2) {
            this.waitForStart = waitForStart2;
        }

        public String getHideSMww() {
            return this.hideSMww;
        }

        public void setHideSMww(String hideSMww2) {
            this.hideSMww = hideSMww2;
        }
    }

    public static class TradeBean {
        private String buyEnable;
        private String buyText;
        private String cartEnable;
        private HintBannerBean hintBanner;
        private String isWap;
        private String redirectUrl;
        private TradeParamsBean tradeParams;
        private String tradeType;
        private String useWap;

        public static class TradeParamsBean {
        }

        public String getRedirectUrl() {
            return this.redirectUrl;
        }

        public void setRedirectUrl(String redirectUrl2) {
            this.redirectUrl = redirectUrl2;
        }

        public HintBannerBean getHintBanner() {
            return this.hintBanner;
        }

        public void setHintBanner(HintBannerBean hintBanner2) {
            this.hintBanner = hintBanner2;
        }

        public String getBuyEnable() {
            return this.buyEnable;
        }

        public void setBuyEnable(String buyEnable2) {
            this.buyEnable = buyEnable2;
        }

        public String getCartEnable() {
            return this.cartEnable;
        }

        public void setCartEnable(String cartEnable2) {
            this.cartEnable = cartEnable2;
        }

        public String getBuyText() {
            return this.buyText;
        }

        public void setBuyText(String buyText2) {
            this.buyText = buyText2;
        }

        public String getIsWap() {
            return this.isWap;
        }

        public void setIsWap(String isWap2) {
            this.isWap = isWap2;
        }

        public String getUseWap() {
            return this.useWap;
        }

        public void setUseWap(String useWap2) {
            this.useWap = useWap2;
        }

        public TradeParamsBean getTradeParams() {
            return this.tradeParams;
        }

        public void setTradeParams(TradeParamsBean tradeParams2) {
            this.tradeParams = tradeParams2;
        }

        public String getTradeType() {
            return this.tradeType;
        }

        public void setTradeType(String tradeType2) {
            this.tradeType = tradeType2;
        }

        public static class HintBannerBean {
            private String text;

            public String getText() {
                return this.text;
            }

            public void setText(String text2) {
                this.text = text2;
            }
        }
    }

    public static class ItemBean {
        private String commentCount;
        private String descType;
        private InfoTextBean infoText;
        private String itemId;
        private String itemPoint;
        private LimitInfoBean limitInfo;
        private String sellCount;
        private String skuText;
        private String themeType;
        private String title;
        private String vagueSellCount;
        private List<?> videos;

        public static class InfoTextBean {
        }

        public String getVagueSellCount() {
            return this.vagueSellCount;
        }

        public void setVagueSellCount(String vagueSellCount2) {
            this.vagueSellCount = vagueSellCount2;
        }

        public String getSkuText() {
            return this.skuText;
        }

        public void setSkuText(String skuText2) {
            this.skuText = skuText2;
        }

        public String getThemeType() {
            return this.themeType;
        }

        public void setThemeType(String themeType2) {
            this.themeType = themeType2;
        }

        public String getDescType() {
            return this.descType;
        }

        public void setDescType(String descType2) {
            this.descType = descType2;
        }

        public InfoTextBean getInfoText() {
            return this.infoText;
        }

        public void setInfoText(InfoTextBean infoText2) {
            this.infoText = infoText2;
        }

        public LimitInfoBean getLimitInfo() {
            return this.limitInfo;
        }

        public void setLimitInfo(LimitInfoBean limitInfo2) {
            this.limitInfo = limitInfo2;
        }

        public String getSellCount() {
            return this.vagueSellCount;
        }

        public void setSellCount(String sellCount2) {
            this.vagueSellCount = sellCount2;
        }

        public String getItemPoint() {
            return this.itemPoint;
        }

        public void setItemPoint(String itemPoint2) {
            this.itemPoint = itemPoint2;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getCommentCount() {
            return this.commentCount;
        }

        public void setCommentCount(String commentCount2) {
            this.commentCount = commentCount2;
        }

        public String getItemId() {
            return this.itemId;
        }

        public void setItemId(String itemId2) {
            this.itemId = itemId2;
        }

        public List<?> getVideos() {
            return this.videos;
        }

        public void setVideos(List<?> videos2) {
            this.videos = videos2;
        }

        public static class LimitInfoBean {
            private List<String> content;
            private String title;

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title2) {
                this.title = title2;
            }

            public List<String> getContent() {
                return this.content;
            }

            public void setContent(List<String> content2) {
                this.content = content2;
            }
        }
    }

    public static class BuyerBean {
        private String tmallMemberLevel;

        public String getTmallMemberLevel() {
            return this.tmallMemberLevel;
        }

        public void setTmallMemberLevel(String tmallMemberLevel2) {
            this.tmallMemberLevel = tmallMemberLevel2;
        }
    }

    public static class PriceBeanX {
        private DepositPriceBean depositPrice;
        private String depositPriceTip;
        private List<ExtraPricesBean> extraPrices;
        private List<ExtraPricesBean> newExtraPrices;
        private PriceBean price;
        private List<ShopPromBean> shopProm;
        private SubPriceBean subPrice;

        public List<ExtraPricesBean> getNewExtraPrices() {
            return this.newExtraPrices;
        }

        public void setNewExtraPrices(List<ExtraPricesBean> newExtraPrices2) {
            this.newExtraPrices = newExtraPrices2;
        }

        public String getDepositPriceTip() {
            return this.depositPriceTip;
        }

        public void setDepositPriceTip(String depositPriceTip2) {
            this.depositPriceTip = depositPriceTip2;
        }

        public DepositPriceBean getDepositPrice() {
            return this.depositPrice;
        }

        public void setDepositPrice(DepositPriceBean depositPrice2) {
            this.depositPrice = depositPrice2;
        }

        public List<ShopPromBean> getShopProm() {
            return this.shopProm;
        }

        public void setShopProm(List<ShopPromBean> shopProm2) {
            this.shopProm = shopProm2;
        }

        public PriceBean getPrice() {
            return this.price;
        }

        public void setPrice(PriceBean price2) {
            this.price = price2;
        }

        public List<ExtraPricesBean> getExtraPrices() {
            return this.extraPrices;
        }

        public void setExtraPrices(List<ExtraPricesBean> extraPrices2) {
            this.extraPrices = extraPrices2;
        }

        public SubPriceBean getSubPrice() {
            return this.subPrice;
        }

        public void setSubPrice(SubPriceBean subPrice2) {
            this.subPrice = subPrice2;
        }

        public static class ShopPromBean {
            private String activityId;
            private List<String> content;
            private String iconText;
            private String period;
            private String title;
            private String type;

            public void setActivityId(String activityId2) {
                this.activityId = activityId2;
            }

            public String getActivityId() {
                return this.activityId;
            }

            public void setIconText(String iconText2) {
                this.iconText = iconText2;
            }

            public String getIconText() {
                return this.iconText;
            }

            public void setTitle(String title2) {
                this.title = title2;
            }

            public String getTitle() {
                return this.title;
            }

            public void setPeriod(String period2) {
                this.period = period2;
            }

            public String getPeriod() {
                return this.period;
            }

            public void setContent(List<String> content2) {
                this.content = content2;
            }

            public List<String> getContent() {
                return this.content;
            }

            public void setType(String type2) {
                this.type = type2;
            }

            public String getType() {
                return this.type;
            }
        }

        public class DepositPriceBean {
            private String priceDesc;
            private String priceTextSize;
            private String sugProm;

            public DepositPriceBean() {
            }

            public void setPriceTextSize(String priceTextSize2) {
                this.priceTextSize = priceTextSize2;
            }

            public String getPriceTextSize() {
                return this.priceTextSize;
            }

            public void setPriceDesc(String priceDesc2) {
                this.priceDesc = priceDesc2;
            }

            public String getPriceDesc() {
                return this.priceDesc;
            }

            public void setSugProm(String sugProm2) {
                this.sugProm = sugProm2;
            }

            public String getSugProm() {
                return this.sugProm;
            }
        }

        public static class PriceBean {
            private String priceText;
            private String priceTitle;
            private String showTitle;

            public String getPriceTitle() {
                return this.priceTitle;
            }

            public void setPriceTitle(String priceTitle2) {
                this.priceTitle = priceTitle2;
            }

            public String getPriceText() {
                return this.priceText;
            }

            public void setPriceText(String priceText2) {
                this.priceText = priceText2;
            }

            public String getShowTitle() {
                return this.showTitle;
            }

            public void setShowTitle(String showTitle2) {
                this.showTitle = showTitle2;
            }
        }

        public static class ExtraPricesBean {
            private String lineThrough;
            private String priceText;
            private String priceTitle;
            private String showTitle;

            public String getPriceText() {
                return this.priceText;
            }

            public void setPriceText(String priceText2) {
                this.priceText = priceText2;
            }

            public String getPriceTitle() {
                return this.priceTitle;
            }

            public void setPriceTitle(String priceTitle2) {
                this.priceTitle = priceTitle2;
            }

            public String getLineThrough() {
                return this.lineThrough;
            }

            public void setLineThrough(String lineThrough2) {
                this.lineThrough = lineThrough2;
            }

            public String getShowTitle() {
                return this.showTitle;
            }

            public void setShowTitle(String showTitle2) {
                this.showTitle = showTitle2;
            }
        }

        public static class SubPriceBean {
            private String priceText;
            private String priceTitle;
            private String showTitle;

            public String getPriceText() {
                return this.priceText;
            }

            public void setPriceText(String priceText2) {
                this.priceText = priceText2;
            }

            public String getPriceTitle() {
                return this.priceTitle;
            }

            public void setPriceTitle(String priceTitle2) {
                this.priceTitle = priceTitle2;
            }

            public String getShowTitle() {
                return this.showTitle;
            }

            public void setShowTitle(String showTitle2) {
                this.showTitle = showTitle2;
            }
        }
    }

    public static class ConsumerProtectionBean {
        private ChannelBean channel;
        private List<ItemsBeanX> items;
        private String params;
        private String passValue;
        private SpecialBean special;
        private String strength;

        public SpecialBean getSpecial() {
            return this.special;
        }

        public void setSpecial(SpecialBean special2) {
            this.special = special2;
        }

        public String getPassValue() {
            return this.passValue;
        }

        public void setPassValue(String passValue2) {
            this.passValue = passValue2;
        }

        public String getParams() {
            return this.params;
        }

        public void setParams(String params2) {
            this.params = params2;
        }

        public String getStrength() {
            return this.strength;
        }

        public void setStrength(String strength2) {
            this.strength = strength2;
        }

        public ChannelBean getChannel() {
            return this.channel;
        }

        public void setChannel(ChannelBean channel2) {
            this.channel = channel2;
        }

        public List<ItemsBeanX> getItems() {
            return this.items;
        }

        public void setItems(List<ItemsBeanX> items2) {
            this.items = items2;
        }

        public static class SpecialBean {
            private String id;
            private List<ItemsBean> items;
            private String logo;
            private String text;

            public String getLogo() {
                return this.logo;
            }

            public void setLogo(String logo2) {
                this.logo = logo2;
            }

            public String getText() {
                return this.text;
            }

            public void setText(String text2) {
                this.text = text2;
            }

            public String getId() {
                return this.id;
            }

            public void setId(String id2) {
                this.id = id2;
            }

            public List<ItemsBean> getItems() {
                return this.items;
            }

            public void setItems(List<ItemsBean> items2) {
                this.items = items2;
            }

            public static class ItemsBean {
                private String desc;
                private String serviceId;
                private String title;
                private String type;

                public String getServiceId() {
                    return this.serviceId;
                }

                public void setServiceId(String serviceId2) {
                    this.serviceId = serviceId2;
                }

                public String getTitle() {
                    return this.title;
                }

                public void setTitle(String title2) {
                    this.title = title2;
                }

                public String getDesc() {
                    return this.desc;
                }

                public void setDesc(String desc2) {
                    this.desc = desc2;
                }

                public String getType() {
                    return this.type;
                }

                public void setType(String type2) {
                    this.type = type2;
                }
            }
        }

        public static class ChannelBean {
            private String forced;
            private String logo;
            private String title;

            public String getLogo() {
                return this.logo;
            }

            public void setLogo(String logo2) {
                this.logo = logo2;
            }

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title2) {
                this.title = title2;
            }

            public String getForced() {
                return this.forced;
            }

            public void setForced(String forced2) {
                this.forced = forced2;
            }
        }

        public static class ItemsBeanX {
            private String desc;
            private String serviceId;
            private String title;
            private String type;

            public String getServiceId() {
                return this.serviceId;
            }

            public void setServiceId(String serviceId2) {
                this.serviceId = serviceId2;
            }

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title2) {
                this.title = title2;
            }

            public String getDesc() {
                return this.desc;
            }

            public void setDesc(String desc2) {
                this.desc = desc2;
            }

            public String getType() {
                return this.type;
            }

            public void setType(String type2) {
                this.type = type2;
            }
        }
    }

    public static class ResourceBean {
        private CouponBeanFirst coupon;
        private EntrancesBean entrances;
        private List<PriceBeanX.ShopPromBean> shopProm;

        public CouponBeanFirst getCoupon() {
            return this.coupon;
        }

        public void setCoupon(CouponBeanFirst coupon2) {
            this.coupon = coupon2;
        }

        public List<PriceBeanX.ShopPromBean> getShopProm() {
            return this.shopProm;
        }

        public void setShopProm(List<PriceBeanX.ShopPromBean> shopProm2) {
            this.shopProm = shopProm2;
        }

        public EntrancesBean getEntrances() {
            return this.entrances;
        }

        public void setEntrances(EntrancesBean entrances2) {
            this.entrances = entrances2;
        }

        public static class CouponBeanFirst {
            private List<CouponBeanItem> couponList;

            public List<CouponBeanItem> getCouponList() {
                return this.couponList;
            }

            public void setCouponList(List<CouponBeanItem> couponList2) {
                this.couponList = couponList2;
            }

            public static class CouponBeanItem {
                private String icon;
                private String title;

                public String getIcon() {
                    return this.icon;
                }

                public void setIcon(String icon2) {
                    this.icon = icon2;
                }

                public String getTitle() {
                    return this.title;
                }

                public void setTitle(String title2) {
                    this.title = title2;
                }
            }
        }

        public static class EntrancesBean {
            private CouponBean coupon;
            private TmallCouponBean tmallCoupon;

            public CouponBean getCoupon() {
                return this.coupon;
            }

            public void setCoupon(CouponBean coupon2) {
                this.coupon = coupon2;
            }

            public TmallCouponBean getTmallCoupon() {
                return this.tmallCoupon;
            }

            public void setTmallCoupon(TmallCouponBean tmallCoupon2) {
                this.tmallCoupon = tmallCoupon2;
            }

            public static class CouponBean {
                private String icon;
                private String link;
                private String linkText;
                private String text;

                public String getIcon() {
                    return this.icon;
                }

                public void setIcon(String icon2) {
                    this.icon = icon2;
                }

                public String getText() {
                    return this.text;
                }

                public void setText(String text2) {
                    this.text = text2;
                }

                public String getLink() {
                    return this.link;
                }

                public void setLink(String link2) {
                    this.link = link2;
                }

                public String getLinkText() {
                    return this.linkText;
                }

                public void setLinkText(String linkText2) {
                    this.linkText = linkText2;
                }
            }

            public static class TmallCouponBean {
                private String icon;
                private String link;
                private String linkText;
                private String text;

                public String getIcon() {
                    return this.icon;
                }

                public void setIcon(String icon2) {
                    this.icon = icon2;
                }

                public String getText() {
                    return this.text;
                }

                public void setText(String text2) {
                    this.text = text2;
                }

                public String getLink() {
                    return this.link;
                }

                public void setLink(String link2) {
                    this.link = link2;
                }

                public String getLinkText() {
                    return this.linkText;
                }

                public void setLinkText(String linkText2) {
                    this.linkText = linkText2;
                }
            }
        }
    }

    public static class VerticalBean {
        private AskAllBean askAll;
        private CuntaoBean cuntao;
        private FreshFoodBean freshFood;
        private InterBean inter;
        private JhsBean jhs;
        private PresaleBean presale;
        private QianggouBean qianggou;
        private SupermarketBean supermarket;

        public static class CuntaoBean {
        }

        public static class FreshFoodBean {
        }

        public InterBean getInter() {
            return this.inter;
        }

        public void setInter(InterBean inter2) {
            this.inter = inter2;
        }

        public QianggouBean getQianggou() {
            return this.qianggou;
        }

        public void setQianggou(QianggouBean qianggou2) {
            this.qianggou = qianggou2;
        }

        public SupermarketBean getSupermarket() {
            return this.supermarket;
        }

        public void setSupermarket(SupermarketBean supermarket2) {
            this.supermarket = supermarket2;
        }

        public JhsBean getJhs() {
            return this.jhs;
        }

        public void setJhs(JhsBean jhs2) {
            this.jhs = jhs2;
        }

        public PresaleBean getPresale() {
            return this.presale;
        }

        public void setPresale(PresaleBean presale2) {
            this.presale = presale2;
        }

        public AskAllBean getAskAll() {
            return this.askAll;
        }

        public void setAskAll(AskAllBean askAll2) {
            this.askAll = askAll2;
        }

        public FreshFoodBean getFreshFood() {
            return this.freshFood;
        }

        public void setFreshFood(FreshFoodBean freshFood2) {
            this.freshFood = freshFood2;
        }

        public CuntaoBean getCuntao() {
            return this.cuntao;
        }

        public void setCuntao(CuntaoBean cuntao2) {
            this.cuntao = cuntao2;
        }

        public static class InterBean {
            private TariffBean tariff;

            public TariffBean getTariff() {
                return this.tariff;
            }

            public void setTariff(TariffBean tariff2) {
                this.tariff = tariff2;
            }

            public static class TariffBean {
                private String name;
                private String value;

                public String getName() {
                    return this.name;
                }

                public void setName(String name2) {
                    this.name = name2;
                }

                public String getValue() {
                    return this.value;
                }

                public void setValue(String value2) {
                    this.value = value2;
                }
            }
        }

        public static class SupermarketBean {
            private String weight;

            public String getWeight() {
                return this.weight;
            }

            public void setWeight(String weight2) {
                this.weight = weight2;
            }
        }

        public static class QianggouBean {
            private String endTime;
            private String progress;
            private String progressText;
            private String soldText;
            private String startTime;
            private String status;

            public String getStartTime() {
                return this.startTime;
            }

            public void setStartTime(String startTime2) {
                this.startTime = startTime2;
            }

            public String getEndTime() {
                return this.endTime;
            }

            public void setEndTime(String endTime2) {
                this.endTime = endTime2;
            }

            public String getStatus() {
                return this.status;
            }

            public void setStatus(String status2) {
                this.status = status2;
            }

            public String getSoldText() {
                return this.soldText;
            }

            public void setSoldText(String soldText2) {
                this.soldText = soldText2;
            }

            public String getProgress() {
                return this.progress;
            }

            public void setProgress(String progress2) {
                this.progress = progress2;
            }

            public String getProgressText() {
                return this.progressText;
            }

            public void setProgressText(String progressText2) {
                this.progressText = progressText2;
            }
        }

        public static class JhsBean {
            private String endTime;
            private String hasIntervalPrice;
            private String juCollection;
            private String needJoin;
            private String remindCount;
            private String sellingPoints;
            private String soldCount;
            private String startTime;
            private String status;
            private String verticalBiz;

            public String getJuCollection() {
                return this.juCollection;
            }

            public void setJuCollection(String juCollection2) {
                this.juCollection = juCollection2;
            }

            public String getHasIntervalPrice() {
                return this.hasIntervalPrice;
            }

            public void setHasIntervalPrice(String hasIntervalPrice2) {
                this.hasIntervalPrice = hasIntervalPrice2;
            }

            public String getSoldCount() {
                return this.soldCount;
            }

            public void setSoldCount(String soldCount2) {
                this.soldCount = soldCount2;
            }

            public String getSellingPoints() {
                return this.sellingPoints;
            }

            public void setSellingPoints(String sellingPoints2) {
                this.sellingPoints = sellingPoints2;
            }

            public String getNeedJoin() {
                return this.needJoin;
            }

            public void setNeedJoin(String needJoin2) {
                this.needJoin = needJoin2;
            }

            public String getRemindCount() {
                return this.remindCount;
            }

            public void setRemindCount(String remindCount2) {
                this.remindCount = remindCount2;
            }

            public String getVerticalBiz() {
                return this.verticalBiz;
            }

            public void setVerticalBiz(String verticalBiz2) {
                this.verticalBiz = verticalBiz2;
            }

            public String getStartTime() {
                return this.startTime;
            }

            public void setStartTime(String startTime2) {
                this.startTime = startTime2;
            }

            public String getEndTime() {
                return this.endTime;
            }

            public void setEndTime(String endTime2) {
                this.endTime = endTime2;
            }

            public String getStatus() {
                return this.status;
            }

            public void setStatus(String status2) {
                this.status = status2;
            }
        }

        public static class PresaleBean {
            private String depositText;
            private String endTime;
            private String extraText;
            private String orderedItemAmount;
            private String startTime;
            private String status;
            private String text;
            private String tip;

            public String getExtraText() {
                return this.extraText;
            }

            public void setExtraText(String extraText2) {
                this.extraText = extraText2;
            }

            public String getOrderedItemAmount() {
                return this.orderedItemAmount;
            }

            public void setOrderedItemAmount(String orderedItemAmount2) {
                this.orderedItemAmount = orderedItemAmount2;
            }

            public String getTip() {
                return this.tip;
            }

            public void setTip(String tip2) {
                this.tip = tip2;
            }

            public String getStartTime() {
                return this.startTime;
            }

            public void setStartTime(String startTime2) {
                this.startTime = startTime2;
            }

            public String getEndTime() {
                return this.endTime;
            }

            public void setEndTime(String endTime2) {
                this.endTime = endTime2;
            }

            public String getText() {
                return this.text;
            }

            public void setText(String text2) {
                this.text = text2;
            }

            public String getStatus() {
                return this.status;
            }

            public void setStatus(String status2) {
                this.status = status2;
            }

            public String getDepositText() {
                return this.depositText;
            }

            public void setDepositText(String depositText2) {
                this.depositText = depositText2;
            }
        }

        public static class AskAllBean {
            private String answerIcon;
            private String answerText;
            private String askIcon;
            private String askText;
            private String linkUrl;
            private List<ModelListBean> modelList;
            private String questNum;
            private String showNum;
            private String title;

            public String getShowNum() {
                return this.showNum;
            }

            public void setShowNum(String showNum2) {
                this.showNum = showNum2;
            }

            public String getAskIcon() {
                return this.askIcon;
            }

            public void setAskIcon(String askIcon2) {
                this.askIcon = askIcon2;
            }

            public String getAnswerIcon() {
                return this.answerIcon;
            }

            public void setAnswerIcon(String answerIcon2) {
                this.answerIcon = answerIcon2;
            }

            public String getAnswerText() {
                return this.answerText;
            }

            public void setAnswerText(String answerText2) {
                this.answerText = answerText2;
            }

            public String getAskText() {
                return this.askText;
            }

            public void setAskText(String askText2) {
                this.askText = askText2;
            }

            public String getLinkUrl() {
                return this.linkUrl;
            }

            public void setLinkUrl(String linkUrl2) {
                this.linkUrl = linkUrl2;
            }

            public String getTitle() {
                return this.title;
            }

            public void setTitle(String title2) {
                this.title = title2;
            }

            public String getQuestNum() {
                return this.questNum;
            }

            public void setQuestNum(String questNum2) {
                this.questNum = questNum2;
            }

            public List<ModelListBean> getModelList() {
                return this.modelList;
            }

            public void setModelList(List<ModelListBean> modelList2) {
                this.modelList = modelList2;
            }

            public static class ModelListBean {
                private String answerCountText;
                private String askText;
                private String firstAnswer;

                public String getAskText() {
                    return this.askText;
                }

                public void setAskText(String askText2) {
                    this.askText = askText2;
                }

                public String getAnswerCountText() {
                    return this.answerCountText;
                }

                public void setAnswerCountText(String answerCountText2) {
                    this.answerCountText = answerCountText2;
                }

                public String getFirstAnswer() {
                    return this.firstAnswer;
                }

                public void setFirstAnswer(String firstAnswer2) {
                    this.firstAnswer = firstAnswer2;
                }
            }
        }
    }

    public static class DeliveryBean {
        private String areaId;
        private String areaSell;
        private String completedTo;
        private String from;
        private String postage;
        private String to;

        public String getCompletedTo() {
            return this.completedTo;
        }

        public void setCompletedTo(String completedTo2) {
            this.completedTo = completedTo2;
        }

        public String getAreaSell() {
            return this.areaSell;
        }

        public void setAreaSell(String areaSell2) {
            this.areaSell = areaSell2;
        }

        public String getPostage() {
            return this.postage;
        }

        public void setPostage(String postage2) {
            this.postage = postage2;
        }

        public String getTo() {
            return this.to;
        }

        public void setTo(String to2) {
            this.to = to2;
        }

        public String getFrom() {
            return this.from;
        }

        public void setFrom(String from2) {
            this.from = from2;
        }

        public String getAreaId() {
            return this.areaId;
        }

        public void setAreaId(String areaId2) {
            this.areaId = areaId2;
        }
    }

    public static class SkuBaseBean {
        private List<PropsBean> props;
        private List<SkusBean> skus;

        public List<PropsBean> getProps() {
            return this.props;
        }

        public void setProps(List<PropsBean> props2) {
            this.props = props2;
        }

        public List<SkusBean> getSkus() {
            return this.skus;
        }

        public void setSkus(List<SkusBean> skus2) {
            this.skus = skus2;
        }

        public static class PropsBean {
            private String name;
            private String pid;
            private List<ValuesBean> values;

            public String getPid() {
                return this.pid;
            }

            public void setPid(String pid2) {
                this.pid = pid2;
            }

            public String getName() {
                return this.name;
            }

            public void setName(String name2) {
                this.name = name2;
            }

            public List<ValuesBean> getValues() {
                return this.values;
            }

            public void setValues(List<ValuesBean> values2) {
                this.values = values2;
            }

            public static class ValuesBean {
                private String name;
                private String sortOrder;
                private String vid;

                public String getVid() {
                    return this.vid;
                }

                public void setVid(String vid2) {
                    this.vid = vid2;
                }

                public String getName() {
                    return this.name;
                }

                public void setName(String name2) {
                    this.name = name2;
                }

                public String getSortOrder() {
                    return this.sortOrder;
                }

                public void setSortOrder(String sortOrder2) {
                    this.sortOrder = sortOrder2;
                }
            }
        }

        public static class SkusBean {
            private String propPath;
            private String skuId;

            public String getSkuId() {
                return this.skuId;
            }

            public void setSkuId(String skuId2) {
                this.skuId = skuId2;
            }

            public String getPropPath() {
                return this.propPath;
            }

            public void setPropPath(String propPath2) {
                this.propPath = propPath2;
            }
        }
    }

    public static class SkuCoreBean {
        private Sku2infoBean sku2info;
        private SkuItemBean skuItem;

        public SkuItemBean getSkuItem() {
            return this.skuItem;
        }

        public void setSkuItem(SkuItemBean skuItem2) {
            this.skuItem = skuItem2;
        }

        public Sku2infoBean getSku2info() {
            return this.sku2info;
        }

        public void setSku2info(Sku2infoBean sku2info2) {
            this.sku2info = sku2info2;
        }

        public static class SkuItemBean {
            private String hideQuantity;
            private String location;
            private String showAddress;

            public String getShowAddress() {
                return this.showAddress;
            }

            public void setShowAddress(String showAddress2) {
                this.showAddress = showAddress2;
            }

            public String getHideQuantity() {
                return this.hideQuantity;
            }

            public void setHideQuantity(String hideQuantity2) {
                this.hideQuantity = hideQuantity2;
            }

            public String getLocation() {
                return this.location;
            }

            public void setLocation(String location2) {
                this.location = location2;
            }
        }

        public static class Sku2infoBean {
            private Unit$SkuCoreBean$Sku2infoBean$_$0Bean _$0;

            public Unit$SkuCoreBean$Sku2infoBean$_$0Bean get_$0() {
                return this._$0;
            }

            public void set_$0(Unit$SkuCoreBean$Sku2infoBean$_$0Bean _$02) {
                this._$0 = _$02;
            }
        }
    }

    public static class ServicesBean {
        private List<AllServicesBean> allServices;
        private String multiSelect;
        private String mustSelect;
        private String serviceType;
        private Sku2serviceMapBean sku2serviceMap;

        public String getMultiSelect() {
            return this.multiSelect;
        }

        public void setMultiSelect(String multiSelect2) {
            this.multiSelect = multiSelect2;
        }

        public String getMustSelect() {
            return this.mustSelect;
        }

        public void setMustSelect(String mustSelect2) {
            this.mustSelect = mustSelect2;
        }

        public Sku2serviceMapBean getSku2serviceMap() {
            return this.sku2serviceMap;
        }

        public void setSku2serviceMap(Sku2serviceMapBean sku2serviceMap2) {
            this.sku2serviceMap = sku2serviceMap2;
        }

        public String getServiceType() {
            return this.serviceType;
        }

        public void setServiceType(String serviceType2) {
            this.serviceType = serviceType2;
        }

        public List<AllServicesBean> getAllServices() {
            return this.allServices;
        }

        public void setAllServices(List<AllServicesBean> allServices2) {
            this.allServices = allServices2;
        }

        public static class Sku2serviceMapBean {
            private List<Unit$ServicesBean$Sku2serviceMapBean$_$0BeanX> _$0;

            public List<Unit$ServicesBean$Sku2serviceMapBean$_$0BeanX> get_$0() {
                return this._$0;
            }

            public void set_$0(List<Unit$ServicesBean$Sku2serviceMapBean$_$0BeanX> _$02) {
                this._$0 = _$02;
            }
        }

        public static class AllServicesBean {
            private String autoSelect;
            private String mustSelect;
            private String name;
            private String serviceId;
            private List<UniqueServicesBean> uniqueServices;

            public String getServiceId() {
                return this.serviceId;
            }

            public void setServiceId(String serviceId2) {
                this.serviceId = serviceId2;
            }

            public String getName() {
                return this.name;
            }

            public void setName(String name2) {
                this.name = name2;
            }

            public String getAutoSelect() {
                return this.autoSelect;
            }

            public void setAutoSelect(String autoSelect2) {
                this.autoSelect = autoSelect2;
            }

            public String getMustSelect() {
                return this.mustSelect;
            }

            public void setMustSelect(String mustSelect2) {
                this.mustSelect = mustSelect2;
            }

            public List<UniqueServicesBean> getUniqueServices() {
                return this.uniqueServices;
            }

            public void setUniqueServices(List<UniqueServicesBean> uniqueServices2) {
                this.uniqueServices = uniqueServices2;
            }

            public static class UniqueServicesBean {
                private String autoSelect;
                private String name;
                private String uniqueId;

                public String getUniqueId() {
                    return this.uniqueId;
                }

                public void setUniqueId(String uniqueId2) {
                    this.uniqueId = uniqueId2;
                }

                public String getName() {
                    return this.name;
                }

                public void setName(String name2) {
                    this.name = name2;
                }

                public String getAutoSelect() {
                    return this.autoSelect;
                }

                public void setAutoSelect(String autoSelect2) {
                    this.autoSelect = autoSelect2;
                }
            }
        }
    }

    public static class SkuVerticalBean {

        public static class InstallmentBean {

            public static class PeriodBean {
            }
        }

        public static class JhsBeanX {
            private String needJoin;

            public String getNeedJoin() {
                return this.needJoin;
            }

            public void setNeedJoin(String needJoin2) {
                this.needJoin = needJoin2;
            }
        }
    }

    public static class ParamsBean {
        private TrackParamsBean trackParams;

        public TrackParamsBean getTrackParams() {
            return this.trackParams;
        }

        public void setTrackParams(TrackParamsBean trackParams2) {
            this.trackParams = trackParams2;
        }

        public static class TrackParamsBean {
            private String shop_id;

            public String getShop_id() {
                return this.shop_id;
            }

            public void setShop_id(String shop_id2) {
                this.shop_id = shop_id2;
            }
        }
    }

    public static class ItemsBean {
        private String desc;
        private String serviceId;
        private String title;
        private String type;

        public String getServiceId() {
            return this.serviceId;
        }

        public void setServiceId(String serviceId2) {
            this.serviceId = serviceId2;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setDesc(String desc2) {
            this.desc = desc2;
        }

        public String getType() {
            return this.type;
        }

        public void setType(String type2) {
            this.type = type2;
        }
    }

    public static class OtherInfoBean {
        private String bucketId;
        private String systemTime;

        public String getBucketId() {
            return this.bucketId;
        }

        public void setBucketId(String bucketId2) {
            this.bucketId = bucketId2;
        }

        public String getSystemTime() {
            return this.systemTime;
        }

        public void setSystemTime(String systemTime2) {
            this.systemTime = systemTime2;
        }
    }

    public static class LayoutBean {
        private ModuleSceneBean moduleScene;

        public ModuleSceneBean getModuleScene() {
            return this.moduleScene;
        }

        public void setModuleScene(ModuleSceneBean moduleScene2) {
            this.moduleScene = moduleScene2;
        }

        public static class ModuleSceneBean {
            private ConsumerProtectionBeanX consumerProtection;

            public ConsumerProtectionBeanX getConsumerProtection() {
                return this.consumerProtection;
            }

            public void setConsumerProtection(ConsumerProtectionBeanX consumerProtection2) {
                this.consumerProtection = consumerProtection2;
            }

            public static class ConsumerProtectionBeanX {
                private String icon;
                private String title;

                public String getIcon() {
                    return this.icon;
                }

                public void setIcon(String icon2) {
                    this.icon = icon2;
                }

                public String getTitle() {
                    return this.title;
                }

                public void setTitle(String title2) {
                    this.title = title2;
                }
            }
        }
    }
}
