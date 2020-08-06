package com.yunos.tvtaobao.biz.request.bo.tvdetail.bean;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import com.yunos.tvtaobao.biz.request.R;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.feizu.Services;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.bean.taobao.Unit;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.util.DetailModleType;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.util.DetailShopType;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.util.ImageSpanCentre;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.util.ResolveResult;
import com.yunos.tvtaobao.biz.request.bo.tvdetail.util.Source;
import java.util.List;
import mtopsdk.common.util.SymbolExpUtil;

public class DetailDataCenter {
    private DetailPanelData detailPanelData;
    private Source source;

    public DetailDataCenter(DetailPanelData detailPanelData2, Source source2) {
        this.detailPanelData = detailPanelData2 == null ? new DetailPanelData() : detailPanelData2;
        this.source = source2;
    }

    public ResolveResult checkResult() {
        return this.detailPanelData.resolveResult;
    }

    public TBDetailResultV6 getTBDetailResultV6() {
        return this.detailPanelData.tbDetailResultV6;
    }

    public Unit getUnit() {
        return this.detailPanelData.unit;
    }

    public DetailModleType getDetailModleType() {
        return this.detailPanelData.detailModleType;
    }

    public DetailShopType getDetailShopType() {
        return this.detailPanelData.detailShopType;
    }

    public String getItemId() {
        return this.detailPanelData.itemId;
    }

    public SpannableString getGoodTitle(Context context) {
        SpannableString ss;
        int viewHeight;
        int viewWidth;
        if (TextUtils.isEmpty(this.detailPanelData.goodTitle)) {
            ss = new SpannableString("");
        } else {
            ss = new SpannableString(this.detailPanelData.goodTitle);
        }
        if (context == null) {
            return ss;
        }
        Drawable d = null;
        DetailModleType modleType = getDetailModleType();
        if (modleType == DetailModleType.JUHUASUAN) {
            d = context.getResources().getDrawable(R.drawable.tvtao_icon_detail_logo_jhs);
        } else if (modleType == DetailModleType.QIANGOU) {
            d = context.getResources().getDrawable(R.drawable.tvtao_icon_detail_logo_tqg);
        } else if (modleType == DetailModleType.PRESALE || modleType == DetailModleType.ALLPAYPRESALE) {
            d = context.getResources().getDrawable(R.drawable.tvtao_icon_detail_logo_presale);
        }
        if (d != null) {
            float imgWidth = (float) d.getIntrinsicWidth();
            float imgHeight = (float) d.getIntrinsicHeight();
            if (this.source == Source.TVTAOBAO_SDK_TVBUY_V) {
                viewHeight = context.getResources().getDimensionPixelOffset(R.dimen.dp_24);
                viewWidth = (int) ((imgWidth / imgHeight) * ((float) viewHeight));
            } else if (this.source == Source.TVTAOBAO_SDK_TVBUY_H) {
                viewHeight = context.getResources().getDimensionPixelOffset(R.dimen.dp_27);
                viewWidth = (int) ((imgWidth / imgHeight) * ((float) viewHeight));
            } else if (this.source == Source.TVTAOBAO_SDK_VIDEO_VENUE) {
                viewHeight = context.getResources().getDimensionPixelOffset(R.dimen.dp_18);
                viewWidth = (int) ((imgWidth / imgHeight) * ((float) viewHeight));
            } else if (this.source == Source.TVTAOBAO_SDK_FULL) {
                viewHeight = context.getResources().getDimensionPixelOffset(R.dimen.dp_24);
                viewWidth = (int) ((imgWidth / imgHeight) * ((float) viewHeight));
            } else {
                viewHeight = (int) imgHeight;
                viewWidth = (int) imgWidth;
            }
            ss = new SpannableString("  " + this.detailPanelData.goodTitle);
            d.setBounds(0, 0, viewWidth, viewHeight);
            ss.setSpan(new ImageSpanCentre(d, 2), 0, 1, 17);
        }
        return ss;
    }

    public List<String> getGoodsImages() {
        return this.detailPanelData.goodsImages;
    }

    public boolean isSupportAddCart() {
        return "true".equals(this.detailPanelData.isSupportAddCart);
    }

    public boolean isSupportBuy() {
        return "true".equals(this.detailPanelData.isSupportBuy);
    }

    public String getSellerType() {
        return this.detailPanelData.sellerType;
    }

    public String getBuyText() {
        return this.detailPanelData.buyText;
    }

    public List<String> getServices() {
        return this.detailPanelData.services;
    }

    public List<Services.ServicesData.ServicesCells> getFeizhuServices() {
        return this.detailPanelData.feizuServices;
    }

    public String getSoldNum() {
        return this.detailPanelData.soldNum;
    }

    public String getNowPrice() {
        String price = this.detailPanelData.nowPrice;
        if (TextUtils.isEmpty(price)) {
            return "";
        }
        return price;
    }

    public String getNowPriceTitle() {
        return this.detailPanelData.nowPriceTitle;
    }

    public String getOldPrice() {
        String price = this.detailPanelData.oldPrice;
        if (TextUtils.isEmpty(price)) {
            return "";
        }
        return "¥" + price;
    }

    public String getStatus() {
        return this.detailPanelData.status;
    }

    public String getTypeTime() {
        return this.detailPanelData.typeTime;
    }

    public SpannableString getShopName(Context context) {
        SpannableString ss;
        int viewWidth;
        int viewHeight;
        if (TextUtils.isEmpty(this.detailPanelData.shopName)) {
            ss = new SpannableString("");
        } else {
            ss = new SpannableString(this.detailPanelData.shopName);
        }
        if (context == null) {
            return ss;
        }
        DetailShopType shopType = getDetailShopType();
        Drawable d = getShopTypeIcon(context);
        if (shopType == DetailShopType.TIANMAOGUOJI) {
            d = context.getResources().getDrawable(R.drawable.tvtao_icon_detail_logo_tmall_internation);
        } else if (shopType == DetailShopType.SUPERMARKET) {
            d = context.getResources().getDrawable(R.drawable.tvtao_icon_detail_logo_tmall_supermarket);
        } else if (shopType == DetailShopType.FEIZHU) {
            d = context.getResources().getDrawable(R.drawable.tvtao_icon_detail_logo_feizhu);
        } else if (shopType == DetailShopType.ALIHEALTH) {
            d = context.getResources().getDrawable(R.drawable.tvtao_icon_detail_logo_alihealth);
        } else if (shopType == DetailShopType.XINXUAN) {
            d = context.getResources().getDrawable(R.drawable.tvtao_icon_detail_logo_xinxuan);
        }
        if (d != null) {
            float imgWidth = (float) d.getIntrinsicWidth();
            float imgHeight = (float) d.getIntrinsicHeight();
            if (this.source == Source.TVTAOBAO_SDK_TVBUY_V || this.source == Source.TVTAOBAO_SDK_FULL) {
                viewHeight = context.getResources().getDimensionPixelOffset(R.dimen.dp_20);
                viewWidth = (int) ((imgWidth / imgHeight) * ((float) viewHeight));
            } else if (this.source == Source.TVTAOBAO_SDK_TVBUY_H) {
                viewHeight = context.getResources().getDimensionPixelOffset(R.dimen.dp_22);
                viewWidth = (int) ((imgWidth / imgHeight) * ((float) viewHeight));
            } else if (this.source == Source.TVTAOBAO_SDK_VIDEO_VENUE) {
                viewHeight = context.getResources().getDimensionPixelOffset(R.dimen.dp_16);
                viewWidth = (int) ((imgWidth / imgHeight) * ((float) viewHeight));
            } else {
                viewHeight = (int) imgHeight;
                viewWidth = (int) imgWidth;
            }
            ss = new SpannableString("  " + (TextUtils.isEmpty(this.detailPanelData.shopName) ? "" : this.detailPanelData.shopName));
            d.setBounds(0, 0, viewWidth, viewHeight);
            ss.setSpan(new ImageSpanCentre(d, 2), 0, 1, 17);
        }
        return ss;
    }

    public Drawable getShopTypeIcon(Context context) {
        if ("B".equals(this.detailPanelData.shopType)) {
            return context.getResources().getDrawable(R.drawable.tvtao_icon_detail_logo_tmall);
        }
        return context.getResources().getDrawable(R.drawable.tvtao_icon_detail_logo_taobao);
    }

    public boolean hasCoupon() {
        return this.detailPanelData.hasCoupon;
    }

    public String getSellerId() {
        return this.detailPanelData.sellerId;
    }

    public String getShopId() {
        return this.detailPanelData.shopId;
    }

    public String getShopType() {
        return this.detailPanelData.shopType;
    }

    public String getPostage() {
        return this.detailPanelData.postage;
    }

    public String getTax() {
        return this.detailPanelData.tax;
    }

    public String getWeight() {
        return this.detailPanelData.weight;
    }

    public String getCouponText() {
        return this.detailPanelData.couponText;
    }

    public String getCouponIcon() {
        return this.detailPanelData.couponIcon;
    }

    public String getSalesPromotionContents() {
        List<String> promotionContents = this.detailPanelData.salesPromotionContent;
        if (promotionContents == null || promotionContents.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < promotionContents.size(); i++) {
            if (i > 0) {
                sb.append(SymbolExpUtil.SYMBOL_SEMICOLON);
            }
            sb.append(promotionContents.get(i));
        }
        return sb.toString();
    }

    public String getSalesPromotionTitleText() {
        String salesPromotionIconText = this.detailPanelData.salesPromotionIconText;
        if (TextUtils.isEmpty(salesPromotionIconText)) {
            return "";
        }
        return salesPromotionIconText;
    }

    public String getLastPriceTip() {
        String lastPriceTip = this.detailPanelData.lastPriceTip;
        if (TextUtils.isEmpty(lastPriceTip)) {
            return "";
        }
        return lastPriceTip.replace("：", ": ");
    }

    public String getDeliverGoods() {
        String deliverGoods = this.detailPanelData.deliverGoods;
        if (TextUtils.isEmpty(deliverGoods)) {
            return "";
        }
        return "发货: " + deliverGoods;
    }

    public String getDepositText() {
        String depositPriceDesc = this.detailPanelData.depositText;
        if (TextUtils.isEmpty(depositPriceDesc)) {
            return "";
        }
        return depositPriceDesc.replace("￥", "¥");
    }

    public String getDepositPriceDesc() {
        String depositPriceDesc = this.detailPanelData.depositPriceDesc;
        if (TextUtils.isEmpty(depositPriceDesc)) {
            return "";
        }
        return depositPriceDesc;
    }

    public String getFlayerTitle() {
        String flayerTitle = this.detailPanelData.flayerTitle;
        if (TextUtils.isEmpty(flayerTitle)) {
            return "";
        }
        return flayerTitle;
    }

    public String getMileageTitle() {
        String mileageTitle = this.detailPanelData.mileageTitle;
        if (TextUtils.isEmpty(mileageTitle)) {
            return "";
        }
        return mileageTitle;
    }

    public String getRightDesc() {
        String rightDesc = this.detailPanelData.rightDesc;
        if (TextUtils.isEmpty(rightDesc)) {
            return "";
        }
        return rightDesc;
    }

    public Source getSource() {
        return this.source;
    }
}
