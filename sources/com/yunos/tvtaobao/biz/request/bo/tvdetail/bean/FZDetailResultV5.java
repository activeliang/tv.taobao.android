package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean;

import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.AlitripPromTagBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.AskAllBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.BannerBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.BrandBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.BuyBannerBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.CouponBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.DetailCoreBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.FliggyShopRecomdBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.ItemExtraBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.MileageBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.PageContainerBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.PriceBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.RateBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.SelectSkuBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.Services;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.ShopBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.SkuBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.SoldBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.SpmBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.TitleBean;

public class FZDetailResultV5 {
    private AlitripPromTagBean alitripPromTag;
    private AskAllBean askAll;
    private BannerBean banner;
    private BrandBean brand;
    private BuyBannerBean buyBanner;
    private CouponBean coupon;
    private DetailCoreBean detailCore;
    private FliggyShopRecomdBean fliggyShopRecomd;
    private ItemExtraBean itemExtra;
    private MileageBean mileage;
    private PageContainerBean pageContainer;
    private PriceBean price;
    private RateBean rate;
    private SelectSkuBean selectSku;
    private Services services;
    private ShopBean shop;
    private SkuBean sku;
    private SoldBean sold;
    private SpmBean spm;
    private TitleBean title;

    public SoldBean getSold() {
        return this.sold;
    }

    public void setSold(SoldBean sold2) {
        this.sold = sold2;
    }

    public PageContainerBean getPageContainer() {
        return this.pageContainer;
    }

    public void setPageContainer(PageContainerBean pageContainer2) {
        this.pageContainer = pageContainer2;
    }

    public ShopBean getShop() {
        return this.shop;
    }

    public void setShop(ShopBean shop2) {
        this.shop = shop2;
    }

    public DetailCoreBean getDetailCore() {
        return this.detailCore;
    }

    public void setDetailCore(DetailCoreBean detailCore2) {
        this.detailCore = detailCore2;
    }

    public CouponBean getCoupon() {
        return this.coupon;
    }

    public void setCoupon(CouponBean coupon2) {
        this.coupon = coupon2;
    }

    public AskAllBean getAskAll() {
        return this.askAll;
    }

    public void setAskAll(AskAllBean askAll2) {
        this.askAll = askAll2;
    }

    public BannerBean getBanner() {
        return this.banner;
    }

    public void setBanner(BannerBean banner2) {
        this.banner = banner2;
    }

    public ItemExtraBean getItemExtra() {
        return this.itemExtra;
    }

    public void setItemExtra(ItemExtraBean itemExtra2) {
        this.itemExtra = itemExtra2;
    }

    public BuyBannerBean getBuyBanner() {
        return this.buyBanner;
    }

    public void setBuyBanner(BuyBannerBean buyBanner2) {
        this.buyBanner = buyBanner2;
    }

    public TitleBean getTitle() {
        return this.title;
    }

    public void setTitle(TitleBean title2) {
        this.title = title2;
    }

    public SpmBean getSpm() {
        return this.spm;
    }

    public void setSpm(SpmBean spm2) {
        this.spm = spm2;
    }

    public RateBean getRate() {
        return this.rate;
    }

    public void setRate(RateBean rate2) {
        this.rate = rate2;
    }

    public PriceBean getPrice() {
        return this.price;
    }

    public void setPrice(PriceBean price2) {
        this.price = price2;
    }

    public AlitripPromTagBean getAlitripPromTag() {
        return this.alitripPromTag;
    }

    public void setAlitripPromTag(AlitripPromTagBean alitripPromTag2) {
        this.alitripPromTag = alitripPromTag2;
    }

    public FliggyShopRecomdBean getFliggyShopRecomd() {
        return this.fliggyShopRecomd;
    }

    public void setFliggyShopRecomd(FliggyShopRecomdBean fliggyShopRecomd2) {
        this.fliggyShopRecomd = fliggyShopRecomd2;
    }

    public SelectSkuBean getSelectSku() {
        return this.selectSku;
    }

    public void setSelectSku(SelectSkuBean selectSku2) {
        this.selectSku = selectSku2;
    }

    public SkuBean getSku() {
        return this.sku;
    }

    public void setSku(SkuBean sku2) {
        this.sku = sku2;
    }

    public BrandBean getBrand() {
        return this.brand;
    }

    public void setBrand(BrandBean brand2) {
        this.brand = brand2;
    }

    public MileageBean getMileage() {
        return this.mileage;
    }

    public void setMileage(MileageBean mileage2) {
        this.mileage = mileage2;
    }

    public Services getServices() {
        return this.services;
    }

    public void setServices(Services services2) {
        this.services = services2;
    }
}
