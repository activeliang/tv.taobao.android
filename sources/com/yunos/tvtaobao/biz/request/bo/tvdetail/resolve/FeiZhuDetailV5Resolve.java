package com.yunos.tvtaobao.biz.request.bo.tvdetail.resolve;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.DetailDataCenter;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.DetailPanelData;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.FZDetailResultV5;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.BannerBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.BuyBannerBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.MileageBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.PriceBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.Services;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.ShopBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.SoldBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.TitleBean;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.util.DetailShopType;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.util.ResolveResult;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.util.Source;
import com.zhiping.dev.android.logger.ZpLogger;

public class FeiZhuDetailV5Resolve {
    public static DetailDataCenter resolve(String json, Source source) {
        DetailPanelData detailPanelData = null;
        try {
            detailPanelData = onHandlerFeizhuDetailV5((FZDetailResultV5) JSON.parseObject(json, FZDetailResultV5.class));
        } catch (Exception e) {
            ZpLogger.e("ContentValues", "DetailDataCenter resolve fail");
            e.printStackTrace();
        }
        return new DetailDataCenter(detailPanelData, source);
    }

    public static DetailDataCenter resolve(JSONObject json, Source source) {
        DetailPanelData detailPanelData = null;
        try {
            detailPanelData = onHandlerFeizhuDetailV5((FZDetailResultV5) JSON.parseObject(json.toJSONString(), FZDetailResultV5.class));
        } catch (Exception e) {
            ZpLogger.e("ContentValues", "DetailDataCenter resolve fail");
            e.printStackTrace();
        }
        return new DetailDataCenter(detailPanelData, source);
    }

    private static DetailPanelData onHandlerFeizhuDetailV5(FZDetailResultV5 fzDetailResultV5) {
        MileageBean.DataBean data;
        BannerBean.DataBean data2;
        BuyBannerBean.DataBean data3;
        PriceBean.DataBean data4;
        SoldBean.DataBean soldData;
        TitleBean.DataBean data5;
        ShopBean.DataBean data6;
        DetailPanelData detailPanelData = new DetailPanelData();
        if (fzDetailResultV5 == null) {
            ZpLogger.e("ContentValues", "解析失败");
            detailPanelData.resolveResult = ResolveResult.RESOLVE_SUCCESS;
            return null;
        }
        ZpLogger.e("ContentValues", "飞猪商品");
        detailPanelData.detailShopType = DetailShopType.FEIZHU;
        ShopBean shop = fzDetailResultV5.getShop();
        if (!(shop == null || (data6 = shop.getData()) == null)) {
            detailPanelData.shopName = data6.getSellerName();
            ZpLogger.e("ContentValues", "店铺名称 = " + detailPanelData.shopName);
            detailPanelData.shopId = data6.getShopId();
            ZpLogger.e("ContentValues", "店铺id = " + detailPanelData.shopId);
        }
        detailPanelData.postage = "";
        ZpLogger.e("ContentValues", "飞猪快递费用不显示");
        TitleBean title = fzDetailResultV5.getTitle();
        if (!(title == null || (data5 = title.getData()) == null)) {
            detailPanelData.goodTitle = data5.getItemTitle();
            ZpLogger.e("ContentValues", "商品标题 = " + detailPanelData.goodTitle);
        }
        SoldBean soldBean = fzDetailResultV5.getSold();
        if (!(soldBean == null || (soldData = soldBean.getData()) == null)) {
            detailPanelData.soldNum = soldData.getSoldCount();
            ZpLogger.e("ContentValues", "月销量 = " + detailPanelData.soldNum);
        }
        Services services = fzDetailResultV5.getServices();
        if (services != null) {
            detailPanelData.feizuServices = services.getData().getCells();
            ZpLogger.e("ContentValues", "服务承诺 = " + detailPanelData.services);
        }
        PriceBean resultV5Price = fzDetailResultV5.getPrice();
        if (!(resultV5Price == null || (data4 = resultV5Price.getData()) == null)) {
            PriceBean.DataBean.AidedPriceBean aidedPrice = data4.getAidedPrice();
            if (aidedPrice != null) {
                detailPanelData.oldPrice = aidedPrice.getPriceText();
                ZpLogger.e("ContentValues", "原价 = " + detailPanelData.oldPrice);
            }
            PriceBean.DataBean.MainPriceBean mainPrice = data4.getMainPrice();
            if (mainPrice != null) {
                detailPanelData.nowPrice = mainPrice.getPriceText();
                ZpLogger.e("ContentValues", "现价 = " + detailPanelData.nowPrice);
            }
        }
        BuyBannerBean buyBanner = fzDetailResultV5.getBuyBanner();
        if (!(buyBanner == null || (data3 = buyBanner.getData()) == null)) {
            detailPanelData.buyText = data3.getBuyButtonDesc();
            ZpLogger.e("ContentValues", "商品状态 = " + detailPanelData.buyText);
        }
        BannerBean banner = fzDetailResultV5.getBanner();
        if (!(banner == null || (data2 = banner.getData()) == null)) {
            detailPanelData.goodsImages = data2.getPics();
            ZpLogger.e("ContentValues", "商品图 = " + detailPanelData.goodsImages);
            detailPanelData.rightDesc = data2.getRightDesc();
            ZpLogger.e("ContentValues", "飞猪出签率 = " + detailPanelData.rightDesc);
        }
        MileageBean mileage = fzDetailResultV5.getMileage();
        if (!(mileage == null || (data = mileage.getData()) == null)) {
            detailPanelData.flayerTitle = data.getFlayerTitle();
            ZpLogger.e("ContentValues", "飞猪里程标签 = " + detailPanelData.flayerTitle);
            detailPanelData.mileageTitle = data.getTitle();
            ZpLogger.e("ContentValues", "飞猪里程内容 = " + detailPanelData.mileageTitle);
        }
        detailPanelData.resolveResult = ResolveResult.RESOLVE_ERROR;
        ZpLogger.e("ContentValues", "解析成功");
        return detailPanelData;
    }
}
