package com.yunos.tvtaobao.biz.request.bo;

import java.util.List;

public class NewFeiZhuBean {
    private Banner banner;
    private Brand brand;
    private BuyBanner buyBanner;
    private Coupon coupon;
    private Jhs jhs;
    private Mileage mileage;
    private Price price;
    private Services services;
    private Shop shop;
    private Sku sku;
    private Title title;

    public Shop getShop() {
        return this.shop;
    }

    public void setShop(Shop shop2) {
        this.shop = shop2;
    }

    public Coupon getCoupon() {
        return this.coupon;
    }

    public void setCoupon(Coupon coupon2) {
        this.coupon = coupon2;
    }

    public Banner getBanner() {
        return this.banner;
    }

    public void setBanner(Banner banner2) {
        this.banner = banner2;
    }

    public BuyBanner getBuyBanner() {
        return this.buyBanner;
    }

    public void setBuyBanner(BuyBanner buyBanner2) {
        this.buyBanner = buyBanner2;
    }

    public Services getServices() {
        return this.services;
    }

    public void setServices(Services services2) {
        this.services = services2;
    }

    public Title getTitle() {
        return this.title;
    }

    public void setTitle(Title title2) {
        this.title = title2;
    }

    public Price getPrice() {
        return this.price;
    }

    public void setPrice(Price price2) {
        this.price = price2;
    }

    public Brand getBrand() {
        return this.brand;
    }

    public void setBrand(Brand brand2) {
        this.brand = brand2;
    }

    public Mileage getMileage() {
        return this.mileage;
    }

    public void setMileage(Mileage mileage2) {
        this.mileage = mileage2;
    }

    public Sku getSku() {
        return this.sku;
    }

    public void setSku(Sku sku2) {
        this.sku = sku2;
    }

    public Jhs getJhs() {
        return this.jhs;
    }

    public void setJhs(Jhs jhs2) {
        this.jhs = jhs2;
    }

    public class JumpInfo {
        private String buyUrlType;
        private String jumpH5Url;
        private String jumpNative;
        private String showBuyButton;

        public JumpInfo() {
        }

        public void setBuyUrlType(String buyUrlType2) {
            this.buyUrlType = buyUrlType2;
        }

        public String getBuyUrlType() {
            return this.buyUrlType;
        }

        public void setJumpH5Url(String jumpH5Url2) {
            this.jumpH5Url = jumpH5Url2;
        }

        public String getJumpH5Url() {
            return this.jumpH5Url;
        }

        public void setJumpNative(String jumpNative2) {
            this.jumpNative = jumpNative2;
        }

        public String getJumpNative() {
            return this.jumpNative;
        }

        public void setShowBuyButton(String showBuyButton2) {
            this.showBuyButton = showBuyButton2;
        }

        public String getShowBuyButton() {
            return this.showBuyButton;
        }
    }

    public class ShopEvaluation {
        private String highGap;
        private String score;
        private String title;

        public ShopEvaluation() {
        }

        public void setHighGap(String highGap2) {
            this.highGap = highGap2;
        }

        public String getHighGap() {
            return this.highGap;
        }

        public void setScore(String score2) {
            this.score = score2;
        }

        public String getScore() {
            return this.score;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getTitle() {
            return this.title;
        }
    }

    public class ShopData {
        private JumpInfo jumpInfo;
        private String sellerId;
        private String sellerName;
        private List<ShopEvaluation> shopEvaluation;
        private String shopId;
        private String shopName;
        private String shopPic;
        private String shopTypePic;

        public ShopData() {
        }

        public void setJumpInfo(JumpInfo jumpInfo2) {
            this.jumpInfo = jumpInfo2;
        }

        public JumpInfo getJumpInfo() {
            return this.jumpInfo;
        }

        public void setSellerId(String sellerId2) {
            this.sellerId = sellerId2;
        }

        public String getSellerId() {
            return this.sellerId;
        }

        public void setSellerName(String sellerName2) {
            this.sellerName = sellerName2;
        }

        public String getSellerName() {
            return this.sellerName;
        }

        public void setShopEvaluation(List<ShopEvaluation> shopEvaluation2) {
            this.shopEvaluation = shopEvaluation2;
        }

        public List<ShopEvaluation> getShopEvaluation() {
            return this.shopEvaluation;
        }

        public void setShopId(String shopId2) {
            this.shopId = shopId2;
        }

        public String getShopId() {
            return this.shopId;
        }

        public void setShopName(String shopName2) {
            this.shopName = shopName2;
        }

        public String getShopName() {
            return this.shopName;
        }

        public void setShopPic(String shopPic2) {
            this.shopPic = shopPic2;
        }

        public String getShopPic() {
            return this.shopPic;
        }

        public void setShopTypePic(String shopTypePic2) {
            this.shopTypePic = shopTypePic2;
        }

        public String getShopTypePic() {
            return this.shopTypePic;
        }
    }

    public class Shop {
        private ShopData data;
        private String tag;

        public Shop() {
        }

        public void setData(ShopData data2) {
            this.data = data2;
        }

        public ShopData getData() {
            return this.data;
        }

        public void setTag(String tag2) {
            this.tag = tag2;
        }

        public String getTag() {
            return this.tag;
        }
    }

    public class Cells {
        private String icon;
        private String title;

        public Cells() {
        }

        public void setIcon(String icon2) {
            this.icon = icon2;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getTitle() {
            return this.title;
        }
    }

    public class CouponData {
        private List<Cells> cells;
        private String titleColor;

        public CouponData() {
        }

        public void setCells(List<Cells> cells2) {
            this.cells = cells2;
        }

        public List<Cells> getCells() {
            return this.cells;
        }

        public void setTitleColor(String titleColor2) {
            this.titleColor = titleColor2;
        }

        public String getTitleColor() {
            return this.titleColor;
        }
    }

    public class Coupon {
        private CouponData data;
        private String tag;

        public Coupon() {
        }

        public void setData(CouponData data2) {
            this.data = data2;
        }

        public CouponData getData() {
            return this.data;
        }

        public void setTag(String tag2) {
            this.tag = tag2;
        }

        public String getTag() {
            return this.tag;
        }
    }

    public class RightDetailDesc {
        private String desc;
        private String titleText;

        public RightDetailDesc() {
        }

        public void setDesc(String desc2) {
            this.desc = desc2;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setTitleText(String titleText2) {
            this.titleText = titleText2;
        }

        public String getTitleText() {
            return this.titleText;
        }
    }

    public class MainPicDescList {
        private List<String> descList;
        private String icon;
        private String title;

        public MainPicDescList() {
        }

        public void setDescList(List<String> descList2) {
            this.descList = descList2;
        }

        public List<String> getDescList() {
            return this.descList;
        }

        public void setIcon(String icon2) {
            this.icon = icon2;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getTitle() {
            return this.title;
        }
    }

    public class SpecificMainPicVO {
        private String mainPic;
        private List<MainPicDescList> mainPicDescList;
        private String title;

        public SpecificMainPicVO() {
        }

        public void setMainPic(String mainPic2) {
            this.mainPic = mainPic2;
        }

        public String getMainPic() {
            return this.mainPic;
        }

        public void setMainPicDescList(List<MainPicDescList> mainPicDescList2) {
            this.mainPicDescList = mainPicDescList2;
        }

        public List<MainPicDescList> getMainPicDescList() {
            return this.mainPicDescList;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getTitle() {
            return this.title;
        }
    }

    public class BannerData {
        private String leftDesc;
        private List<String> pics;
        private String rightDesc;
        private RightDetailDesc rightDetailDesc;
        private SpecificMainPicVO specificMainPicVO;

        public BannerData() {
        }

        public void setLeftDesc(String leftDesc2) {
            this.leftDesc = leftDesc2;
        }

        public String getLeftDesc() {
            return this.leftDesc;
        }

        public void setPics(List<String> pics2) {
            this.pics = pics2;
        }

        public List<String> getPics() {
            return this.pics;
        }

        public void setRightDesc(String rightDesc2) {
            this.rightDesc = rightDesc2;
        }

        public String getRightDesc() {
            return this.rightDesc;
        }

        public void setRightDetailDesc(RightDetailDesc rightDetailDesc2) {
            this.rightDetailDesc = rightDetailDesc2;
        }

        public RightDetailDesc getRightDetailDesc() {
            return this.rightDetailDesc;
        }

        public void setSpecificMainPicVO(SpecificMainPicVO specificMainPicVO2) {
            this.specificMainPicVO = specificMainPicVO2;
        }

        public SpecificMainPicVO getSpecificMainPicVO() {
            return this.specificMainPicVO;
        }
    }

    public class Banner {
        private BannerData data;
        private String tag;

        public Banner() {
        }

        public void setData(BannerData data2) {
            this.data = data2;
        }

        public BannerData getData() {
            return this.data;
        }

        public void setTag(String tag2) {
            this.tag = tag2;
        }

        public String getTag() {
            return this.tag;
        }
    }

    public class BuyJumpInfo {
        private String buyUrlType;
        private String jumpH5Url;
        private String jumpNative;
        private String showBuyButton;

        public BuyJumpInfo() {
        }

        public void setBuyUrlType(String buyUrlType2) {
            this.buyUrlType = buyUrlType2;
        }

        public String getBuyUrlType() {
            return this.buyUrlType;
        }

        public void setJumpH5Url(String jumpH5Url2) {
            this.jumpH5Url = jumpH5Url2;
        }

        public String getJumpH5Url() {
            return this.jumpH5Url;
        }

        public void setJumpNative(String jumpNative2) {
            this.jumpNative = jumpNative2;
        }

        public String getJumpNative() {
            return this.jumpNative;
        }

        public void setShowBuyButton(String showBuyButton2) {
            this.showBuyButton = showBuyButton2;
        }

        public String getShowBuyButton() {
            return this.showBuyButton;
        }
    }

    public class CarItemJumpInfo {
        private String jumpH5Url;
        private String showBuyButton;

        public CarItemJumpInfo() {
        }

        public void setJumpH5Url(String jumpH5Url2) {
            this.jumpH5Url = jumpH5Url2;
        }

        public String getJumpH5Url() {
            return this.jumpH5Url;
        }

        public void setShowBuyButton(String showBuyButton2) {
            this.showBuyButton = showBuyButton2;
        }

        public String getShowBuyButton() {
            return this.showBuyButton;
        }
    }

    public class SellerContact {
        private String itemTips;
        private String notifyTips;
        private String phoneNO;
        private String phoneTips;
        private String sellerId;
        private String sellerNick;

        public SellerContact() {
        }

        public void setItemTips(String itemTips2) {
            this.itemTips = itemTips2;
        }

        public String getItemTips() {
            return this.itemTips;
        }

        public void setNotifyTips(String notifyTips2) {
            this.notifyTips = notifyTips2;
        }

        public String getNotifyTips() {
            return this.notifyTips;
        }

        public void setPhoneNO(String phoneNO2) {
            this.phoneNO = phoneNO2;
        }

        public String getPhoneNO() {
            return this.phoneNO;
        }

        public void setPhoneTips(String phoneTips2) {
            this.phoneTips = phoneTips2;
        }

        public String getPhoneTips() {
            return this.phoneTips;
        }

        public void setSellerId(String sellerId2) {
            this.sellerId = sellerId2;
        }

        public String getSellerId() {
            return this.sellerId;
        }

        public void setSellerNick(String sellerNick2) {
            this.sellerNick = sellerNick2;
        }

        public String getSellerNick() {
            return this.sellerNick;
        }
    }

    public class ShopJumpInfo {
        private String jumpH5Url;
        private String jumpNative;
        private String showBuyButton;

        public ShopJumpInfo() {
        }

        public void setJumpH5Url(String jumpH5Url2) {
            this.jumpH5Url = jumpH5Url2;
        }

        public String getJumpH5Url() {
            return this.jumpH5Url;
        }

        public void setJumpNative(String jumpNative2) {
            this.jumpNative = jumpNative2;
        }

        public String getJumpNative() {
            return this.jumpNative;
        }

        public void setShowBuyButton(String showBuyButton2) {
            this.showBuyButton = showBuyButton2;
        }

        public String getShowBuyButton() {
            return this.showBuyButton;
        }
    }

    public class BuyBannerData {
        private String buyButtonDesc;
        private String buyButtonStyle;
        private String buyButtonSupport;
        private BuyJumpInfo buyJumpInfo;
        private String carBizType;
        private String carButtonStyle;
        private String carButtonSupport;
        private String carDesc;
        private CarItemJumpInfo carItemJumpInfo;
        private String carType;
        private String saveIcon;
        private SellerContact sellerContact;
        private String sellerId;
        private String sellerName;
        private String shopIcon;
        private ShopJumpInfo shopJumpInfo;

        public BuyBannerData() {
        }

        public void setBuyButtonDesc(String buyButtonDesc2) {
            this.buyButtonDesc = buyButtonDesc2;
        }

        public String getBuyButtonDesc() {
            return this.buyButtonDesc;
        }

        public void setBuyButtonStyle(String buyButtonStyle2) {
            this.buyButtonStyle = buyButtonStyle2;
        }

        public String getBuyButtonStyle() {
            return this.buyButtonStyle;
        }

        public void setBuyButtonSupport(String buyButtonSupport2) {
            this.buyButtonSupport = buyButtonSupport2;
        }

        public String getBuyButtonSupport() {
            return this.buyButtonSupport;
        }

        public void setBuyJumpInfo(BuyJumpInfo buyJumpInfo2) {
            this.buyJumpInfo = buyJumpInfo2;
        }

        public BuyJumpInfo getBuyJumpInfo() {
            return this.buyJumpInfo;
        }

        public void setCarBizType(String carBizType2) {
            this.carBizType = carBizType2;
        }

        public String getCarBizType() {
            return this.carBizType;
        }

        public void setCarButtonStyle(String carButtonStyle2) {
            this.carButtonStyle = carButtonStyle2;
        }

        public String getCarButtonStyle() {
            return this.carButtonStyle;
        }

        public void setCarButtonSupport(String carButtonSupport2) {
            this.carButtonSupport = carButtonSupport2;
        }

        public String getCarButtonSupport() {
            return this.carButtonSupport;
        }

        public void setCarDesc(String carDesc2) {
            this.carDesc = carDesc2;
        }

        public String getCarDesc() {
            return this.carDesc;
        }

        public void setCarItemJumpInfo(CarItemJumpInfo carItemJumpInfo2) {
            this.carItemJumpInfo = carItemJumpInfo2;
        }

        public CarItemJumpInfo getCarItemJumpInfo() {
            return this.carItemJumpInfo;
        }

        public void setCarType(String carType2) {
            this.carType = carType2;
        }

        public String getCarType() {
            return this.carType;
        }

        public void setSaveIcon(String saveIcon2) {
            this.saveIcon = saveIcon2;
        }

        public String getSaveIcon() {
            return this.saveIcon;
        }

        public void setSellerContact(SellerContact sellerContact2) {
            this.sellerContact = sellerContact2;
        }

        public SellerContact getSellerContact() {
            return this.sellerContact;
        }

        public void setSellerId(String sellerId2) {
            this.sellerId = sellerId2;
        }

        public String getSellerId() {
            return this.sellerId;
        }

        public void setSellerName(String sellerName2) {
            this.sellerName = sellerName2;
        }

        public String getSellerName() {
            return this.sellerName;
        }

        public void setShopIcon(String shopIcon2) {
            this.shopIcon = shopIcon2;
        }

        public String getShopIcon() {
            return this.shopIcon;
        }

        public void setShopJumpInfo(ShopJumpInfo shopJumpInfo2) {
            this.shopJumpInfo = shopJumpInfo2;
        }

        public ShopJumpInfo getShopJumpInfo() {
            return this.shopJumpInfo;
        }
    }

    public class BuyBanner {
        private BuyBannerData data;
        private String tag;

        public BuyBanner() {
        }

        public void setData(BuyBannerData data2) {
            this.data = data2;
        }

        public BuyBannerData getData() {
            return this.data;
        }

        public void setTag(String tag2) {
            this.tag = tag2;
        }

        public String getTag() {
            return this.tag;
        }
    }

    public class ServicesCells {
        private String icon;
        private String title;

        public ServicesCells() {
        }

        public void setIcon(String icon2) {
            this.icon = icon2;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getTitle() {
            return this.title;
        }
    }

    public class ServicesData {
        private List<ServicesCells> cells;
        private String titleColor;

        public ServicesData() {
        }

        public void setCells(List<ServicesCells> cells2) {
            this.cells = cells2;
        }

        public List<ServicesCells> getCells() {
            return this.cells;
        }

        public void setTitleColor(String titleColor2) {
            this.titleColor = titleColor2;
        }

        public String getTitleColor() {
            return this.titleColor;
        }
    }

    public class Services {
        private ServicesData data;
        private String tag;

        public Services() {
        }

        public void setData(ServicesData data2) {
            this.data = data2;
        }

        public ServicesData getData() {
            return this.data;
        }

        public void setTag(String tag2) {
            this.tag = tag2;
        }

        public String getTag() {
            return this.tag;
        }
    }

    public class TitleData {
        private String itemTitle;

        public TitleData() {
        }

        public void setItemTitle(String itemTitle2) {
            this.itemTitle = itemTitle2;
        }

        public String getItemTitle() {
            return this.itemTitle;
        }
    }

    public class Title {
        private TitleData data;
        private String tag;

        public Title() {
        }

        public void setData(TitleData data2) {
            this.data = data2;
        }

        public TitleData getData() {
            return this.data;
        }

        public void setTag(String tag2) {
            this.tag = tag2;
        }

        public String getTag() {
            return this.tag;
        }
    }

    public class Extra {
        private String content;
        private String type;

        public Extra() {
        }

        public void setContent(String content2) {
            this.content = content2;
        }

        public String getContent() {
            return this.content;
        }

        public void setType(String type2) {
            this.type = type2;
        }

        public String getType() {
            return this.type;
        }
    }

    public class AidedPrice {
        private List<Extra> extra;

        public AidedPrice() {
        }

        public void setExtra(List<Extra> extra2) {
            this.extra = extra2;
        }

        public List<Extra> getExtra() {
            return this.extra;
        }
    }

    public class MainPrice {
        private List<Extra> extra;
        private String price;

        public MainPrice() {
        }

        public void setExtra(List<Extra> extra2) {
            this.extra = extra2;
        }

        public List<Extra> getExtra() {
            return this.extra;
        }

        public void setPrice(String price2) {
            this.price = price2;
        }

        public String getPrice() {
            return this.price;
        }
    }

    public class PriceData {
        private AidedPrice aidedPrice;
        private List<Extra> extra;
        private MainPrice mainPrice;

        public PriceData() {
        }

        public void setAidedPrice(AidedPrice aidedPrice2) {
            this.aidedPrice = aidedPrice2;
        }

        public AidedPrice getAidedPrice() {
            return this.aidedPrice;
        }

        public void setExtra(List<Extra> extra2) {
            this.extra = extra2;
        }

        public List<Extra> getExtra() {
            return this.extra;
        }

        public void setMainPrice(MainPrice mainPrice2) {
            this.mainPrice = mainPrice2;
        }

        public MainPrice getMainPrice() {
            return this.mainPrice;
        }
    }

    public class Price {
        private PriceData data;
        private String tag;

        public Price() {
        }

        public void setData(PriceData data2) {
            this.data = data2;
        }

        public PriceData getData() {
            return this.data;
        }

        public void setTag(String tag2) {
            this.tag = tag2;
        }

        public String getTag() {
            return this.tag;
        }
    }

    public class SkuPropList {
        private String pvId;
        private String value;

        public SkuPropList() {
        }

        public void setPvId(String pvId2) {
            this.pvId = pvId2;
        }

        public String getPvId() {
            return this.pvId;
        }

        public void setValue(String value2) {
            this.value = value2;
        }

        public String getValue() {
            return this.value;
        }
    }

    public class Props {
        private String skuPropId;
        private List<SkuPropList> skuPropList;
        private String skuPropTitle;

        public Props() {
        }

        public void setSkuPropId(String skuPropId2) {
            this.skuPropId = skuPropId2;
        }

        public String getSkuPropId() {
            return this.skuPropId;
        }

        public void setSkuPropList(List<SkuPropList> skuPropList2) {
            this.skuPropList = skuPropList2;
        }

        public List<SkuPropList> getSkuPropList() {
            return this.skuPropList;
        }

        public void setSkuPropTitle(String skuPropTitle2) {
            this.skuPropTitle = skuPropTitle2;
        }

        public String getSkuPropTitle() {
            return this.skuPropTitle;
        }
    }

    public class SkuStrokeStatus {
        private String status;

        public SkuStrokeStatus() {
        }

        public void setStatus(String status2) {
            this.status = status2;
        }

        public String getStatus() {
            return this.status;
        }
    }

    public class SkuData {
        private String enableSelectCount;
        private String hideActionPrice;
        private String mainPic;
        private List<Props> props;
        private String selectedDateTitle;
        private String skuShareInventory;
        private SkuStrokeStatus skuStrokeStatus;
        private String startDateTitle;
        private String status;

        public SkuData() {
        }

        public void setEnableSelectCount(String enableSelectCount2) {
            this.enableSelectCount = enableSelectCount2;
        }

        public String getEnableSelectCount() {
            return this.enableSelectCount;
        }

        public void setHideActionPrice(String hideActionPrice2) {
            this.hideActionPrice = hideActionPrice2;
        }

        public String getHideActionPrice() {
            return this.hideActionPrice;
        }

        public void setMainPic(String mainPic2) {
            this.mainPic = mainPic2;
        }

        public String getMainPic() {
            return this.mainPic;
        }

        public void setProps(List<Props> props2) {
            this.props = props2;
        }

        public List<Props> getProps() {
            return this.props;
        }

        public void setSelectedDateTitle(String selectedDateTitle2) {
            this.selectedDateTitle = selectedDateTitle2;
        }

        public String getSelectedDateTitle() {
            return this.selectedDateTitle;
        }

        public void setSkuShareInventory(String skuShareInventory2) {
            this.skuShareInventory = skuShareInventory2;
        }

        public String getSkuShareInventory() {
            return this.skuShareInventory;
        }

        public void setSkuStrokeStatus(SkuStrokeStatus skuStrokeStatus2) {
            this.skuStrokeStatus = skuStrokeStatus2;
        }

        public SkuStrokeStatus getSkuStrokeStatus() {
            return this.skuStrokeStatus;
        }

        public void setStartDateTitle(String startDateTitle2) {
            this.startDateTitle = startDateTitle2;
        }

        public String getStartDateTitle() {
            return this.startDateTitle;
        }

        public void setStatus(String status2) {
            this.status = status2;
        }

        public String getStatus() {
            return this.status;
        }
    }

    public class Sku {
        private SkuData data;
        private String tag;

        public Sku() {
        }

        public void setData(SkuData data2) {
            this.data = data2;
        }

        public SkuData getData() {
            return this.data;
        }

        public void setTag(String tag2) {
            this.tag = tag2;
        }

        public String getTag() {
            return this.tag;
        }
    }

    public class BrandData {
        private String titleColor;
        private String titleIcon;
        private String titleText;

        public BrandData() {
        }

        public void setTitleColor(String titleColor2) {
            this.titleColor = titleColor2;
        }

        public String getTitleColor() {
            return this.titleColor;
        }

        public void setTitleIcon(String titleIcon2) {
            this.titleIcon = titleIcon2;
        }

        public String getTitleIcon() {
            return this.titleIcon;
        }

        public void setTitleText(String titleText2) {
            this.titleText = titleText2;
        }

        public String getTitleText() {
            return this.titleText;
        }
    }

    public class Brand {
        private BrandData data;
        private String tag;

        public Brand() {
        }

        public void setData(BrandData data2) {
            this.data = data2;
        }

        public BrandData getData() {
            return this.data;
        }

        public void setTag(String tag2) {
            this.tag = tag2;
        }

        public String getTag() {
            return this.tag;
        }
    }

    public class MileageData {
        private String desc;
        private String flayerTitle;
        private String icon;
        private String title;
        private String titleColor;

        public MileageData() {
        }

        public void setDesc(String desc2) {
            this.desc = desc2;
        }

        public String getDesc() {
            return this.desc;
        }

        public void setFlayerTitle(String flayerTitle2) {
            this.flayerTitle = flayerTitle2;
        }

        public String getFlayerTitle() {
            return this.flayerTitle;
        }

        public void setIcon(String icon2) {
            this.icon = icon2;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setTitle(String title2) {
            this.title = title2;
        }

        public String getTitle() {
            return this.title;
        }

        public void setTitleColor(String titleColor2) {
            this.titleColor = titleColor2;
        }

        public String getTitleColor() {
            return this.titleColor;
        }
    }

    public class Mileage {
        private MileageData data;
        private String tag;

        public Mileage() {
        }

        public void setData(MileageData data2) {
            this.data = data2;
        }

        public MileageData getData() {
            return this.data;
        }

        public void setTag(String tag2) {
            this.tag = tag2;
        }

        public String getTag() {
            return this.tag;
        }
    }

    public class Started {
        private String endSeconds;
        private String originalPrice;
        private String price;
        private String sold;

        public Started() {
        }

        public void setEndSeconds(String endSeconds2) {
            this.endSeconds = endSeconds2;
        }

        public String getEndSeconds() {
            return this.endSeconds;
        }

        public void setOriginalPrice(String originalPrice2) {
            this.originalPrice = originalPrice2;
        }

        public String getOriginalPrice() {
            return this.originalPrice;
        }

        public void setPrice(String price2) {
            this.price = price2;
        }

        public String getPrice() {
            return this.price;
        }

        public void setSold(String sold2) {
            this.sold = sold2;
        }

        public String getSold() {
            return this.sold;
        }
    }

    public class JhsData {
        private Started started;
        private String status;

        public JhsData() {
        }

        public void setStarted(Started started2) {
            this.started = started2;
        }

        public Started getStarted() {
            return this.started;
        }

        public void setStatus(String status2) {
            this.status = status2;
        }

        public String getStatus() {
            return this.status;
        }
    }

    public class Jhs {
        private JhsData data;
        private String tag;

        public Jhs() {
        }

        public void setData(JhsData data2) {
            this.data = data2;
        }

        public JhsData getData() {
            return this.data;
        }

        public void setTag(String tag2) {
            this.tag = tag2;
        }

        public String getTag() {
            return this.tag;
        }
    }
}
