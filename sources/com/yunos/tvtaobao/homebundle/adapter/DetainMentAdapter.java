package com.yunos.tvtaobao.homebundle.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.tvlife.imageloader.core.DisplayImageOptions;
import com.tvlife.imageloader.core.assist.FailReason;
import com.tvlife.imageloader.core.listener.ImageLoadingListener;
import com.yunos.tv.core.common.ImageLoaderManager;
import com.yunos.tv.core.util.DeviceUtil;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeBannerBean;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeGoods;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeGoodsData;
import com.yunos.tvtaobao.biz.request.bo.KMGoods;
import com.yunos.tvtaobao.biz.request.bo.RebateBo;
import com.yunos.tvtaobao.homebundle.R;
import com.yunos.tvtaobao.tvsdk.widget.AbsBaseListView;
import com.zhiping.dev.android.logger.ZpLogger;
import java.text.DecimalFormat;
import java.util.List;

public class DetainMentAdapter extends BaseAdapter {
    public static int PING_COUNT = 5;
    public static int TOTAL_COUNT = 20;
    private final String TAG = "DetainMentAdapter";
    private DecimalFormat df = new DecimalFormat("0.##");
    DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc(true).cacheInMemory(false).build();
    private Context mContext;
    private ImageLoaderManager mImageLoaderManager;
    private LayoutInflater mInflater;
    private boolean mIs1080P;
    private List<GuessLikeGoodsData> mItems;

    public DetainMentAdapter(Context context) {
        this.mContext = context;
        this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mImageLoaderManager = ImageLoaderManager.get();
        this.mIs1080P = is1080p(context);
        ZpLogger.i("DetainMentAdapter", "DetainMentAdapter --> getItem -->  mIs1080P = " + this.mIs1080P);
    }

    public int getCount() {
        int count = 0;
        if (this.mItems != null) {
            count = this.mItems.size();
        }
        return Math.min(count, TOTAL_COUNT);
    }

    public GuessLikeGoodsData getItem(int position) {
        if (this.mItems == null || this.mItems.size() == 0) {
            return null;
        }
        return this.mItems.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ZpLogger.i("DetainMentAdapter", "getView  -->    position = " + position + ";  convertView = " + convertView);
        if (convertView == null) {
            convertView = this.mInflater.inflate(R.layout.ytm_detainment_item, (ViewGroup) null);
        }
        GuessLikeGoodsData guessLikeGoodsData = getItem(position);
        if (guessLikeGoodsData != null) {
            TextView titleView = (TextView) convertView.findViewById(R.id.detainment_item_title);
            TextView priceView = (TextView) convertView.findViewById(R.id.detainment_item_price);
            final ImageView picImageView = (ImageView) convertView.findViewById(R.id.detainment_item_img);
            TextView sold = (TextView) convertView.findViewById(R.id.tv_buy_count);
            TextView kmAdvertise = (TextView) convertView.findViewById(R.id.tv_km_advertise_icon);
            ImageView ivRebateIcon = (ImageView) convertView.findViewById(R.id.goodlist_grid_item_rebate_icon);
            TextView tvRebateCoupon = (TextView) convertView.findViewById(R.id.goodlist_grid_item_rebate_coupon);
            RelativeLayout layoutRebate = (RelativeLayout) convertView.findViewById(R.id.layout_rebate);
            ImageView goodlistGridItemReturnRedPacket = (ImageView) convertView.findViewById(R.id.goodlist_grid_item_return_red_packet);
            ImageView ivFlbEntrance = (ImageView) convertView.findViewById(R.id.iv_flb_entrance);
            ivFlbEntrance.setVisibility(8);
            if (GuessLikeGoodsData.TYPE_ITEM.equals(guessLikeGoodsData.getType())) {
                GuessLikeGoods guessLikeGoods = guessLikeGoodsData.getGuessLikeGoods();
                if (titleView != null) {
                    String title = guessLikeGoods.getTitle();
                    if (!TextUtils.isEmpty(title)) {
                        titleView.setText(title);
                        titleView.setVisibility(0);
                        titleView.setLines(1);
                    } else {
                        titleView.setVisibility(8);
                    }
                }
                if (priceView != null) {
                    String priceText = guessLikeGoods.getCurPrice();
                    if (!TextUtils.isEmpty(priceText)) {
                        priceView.setText("¥ " + this.df.format(Double.parseDouble(priceText) / 100.0d));
                        priceView.setVisibility(0);
                    } else {
                        priceView.setVisibility(8);
                    }
                }
                if (guessLikeGoods.getSoldText() != null) {
                    sold.setText(guessLikeGoods.getSoldText());
                }
                if (picImageView != null) {
                    String picurl = getPicUrl(guessLikeGoods.getPicUrl());
                    if (!TextUtils.isEmpty(picurl) && this.mImageLoaderManager != null) {
                        this.mImageLoaderManager.loadImage(picurl, new ImageLoadingListener() {
                            public void onLoadingStarted(String imageUri, View view) {
                                picImageView.setImageResource(R.drawable.ytm_detainment_default);
                            }

                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                picImageView.setImageResource(R.drawable.ytm_detainment_default);
                            }

                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                if (loadedImage == null || loadedImage.isRecycled()) {
                                    picImageView.setImageResource(R.drawable.ytm_detainment_default);
                                } else {
                                    picImageView.setImageBitmap(loadedImage);
                                }
                            }

                            public void onLoadingCancelled(String imageUri, View view) {
                                picImageView.setImageResource(R.drawable.ytm_detainment_default);
                            }
                        });
                    }
                }
                if (guessLikeGoods.getRebateBo() != null) {
                    RebateBo rebateBo = guessLikeGoods.getRebateBo();
                    if (Boolean.valueOf(rebateBo.isMjf()).booleanValue()) {
                        goodlistGridItemReturnRedPacket.setVisibility(0);
                    } else {
                        goodlistGridItemReturnRedPacket.setVisibility(4);
                    }
                    String rebateBoCoupon = rebateBo.getCoupon();
                    if (!TextUtils.isEmpty(rebateBoCoupon)) {
                        String couponString = Utils.getRebateCoupon(rebateBoCoupon);
                        if (!TextUtils.isEmpty(couponString)) {
                            ZpLogger.e("DetainMentAdapter", "Rebate.couponString = " + couponString);
                            layoutRebate.setVisibility(0);
                            tvRebateCoupon.setVisibility(0);
                            if (!TextUtils.isEmpty(rebateBo.getCouponMessage())) {
                                tvRebateCoupon.setText(rebateBo.getCouponMessage() + " ¥ " + couponString);
                            } else {
                                tvRebateCoupon.setText("预估 ¥ " + couponString);
                            }
                            if (!TextUtils.isEmpty(rebateBo.getPicUrl())) {
                                ivRebateIcon.setVisibility(0);
                                ImageLoaderManager.get().displayImage(rebateBo.getPicUrl(), ivRebateIcon, this.imageOptions);
                            } else {
                                ivRebateIcon.setVisibility(8);
                            }
                        } else {
                            layoutRebate.setVisibility(4);
                        }
                    } else {
                        if (!TextUtils.isEmpty(rebateBo.getCouponMessage())) {
                            tvRebateCoupon.setText(rebateBo.getCouponMessage());
                        }
                        if (!TextUtils.isEmpty(rebateBo.getPicUrl())) {
                            ivRebateIcon.setVisibility(0);
                            ImageLoaderManager.get().displayImage(rebateBo.getPicUrl(), ivRebateIcon, this.imageOptions);
                        } else {
                            ivRebateIcon.setVisibility(8);
                        }
                        layoutRebate.setVisibility(0);
                    }
                } else {
                    layoutRebate.setVisibility(4);
                }
                if (kmAdvertise.getVisibility() == 0) {
                    kmAdvertise.setVisibility(8);
                }
            }
            if (GuessLikeGoodsData.TYPE_ZTC.equals(guessLikeGoodsData.getType())) {
                KMGoods kmGoods = guessLikeGoodsData.getKmGoods();
                if (titleView != null) {
                    String title2 = kmGoods.getTitle();
                    if (!TextUtils.isEmpty(title2)) {
                        titleView.setText(title2);
                        titleView.setVisibility(0);
                        titleView.setLines(1);
                    } else {
                        titleView.setVisibility(8);
                    }
                }
                if (priceView != null) {
                    String priceText2 = kmGoods.getCurPrice();
                    if (!TextUtils.isEmpty(priceText2)) {
                        priceView.setText("¥ " + this.df.format(Double.parseDouble(priceText2) / 100.0d));
                        priceView.setVisibility(0);
                    } else {
                        priceView.setVisibility(8);
                    }
                }
                if (kmGoods.getSoldText() != null) {
                    sold.setText(kmGoods.getSoldText());
                }
                if (picImageView != null) {
                    String picurl2 = getPicUrl(kmGoods.getTbgoodslink());
                    if (!TextUtils.isEmpty(picurl2) && this.mImageLoaderManager != null) {
                        this.mImageLoaderManager.loadImage(picurl2, new ImageLoadingListener() {
                            public void onLoadingStarted(String imageUri, View view) {
                                picImageView.setImageResource(R.drawable.ytm_detainment_default);
                            }

                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                picImageView.setImageResource(R.drawable.ytm_detainment_default);
                            }

                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                if (loadedImage == null || loadedImage.isRecycled()) {
                                    picImageView.setImageResource(R.drawable.ytm_detainment_default);
                                } else {
                                    picImageView.setImageBitmap(loadedImage);
                                }
                            }

                            public void onLoadingCancelled(String imageUri, View view) {
                                picImageView.setImageResource(R.drawable.ytm_detainment_default);
                            }
                        });
                    }
                }
                if (kmGoods.getRebateBo() != null) {
                    RebateBo rebateBo2 = kmGoods.getRebateBo();
                    if (Boolean.valueOf(rebateBo2.isMjf()).booleanValue()) {
                        goodlistGridItemReturnRedPacket.setVisibility(0);
                    } else {
                        goodlistGridItemReturnRedPacket.setVisibility(4);
                    }
                    String rebateBoCoupon2 = rebateBo2.getCoupon();
                    if (!TextUtils.isEmpty(rebateBoCoupon2)) {
                        String couponString2 = Utils.getRebateCoupon(rebateBoCoupon2);
                        if (!TextUtils.isEmpty(couponString2)) {
                            ZpLogger.e("DetainMentAdapter", "Rebate.couponString = " + couponString2);
                            layoutRebate.setVisibility(0);
                            tvRebateCoupon.setVisibility(0);
                            if (!TextUtils.isEmpty(rebateBo2.getCouponMessage())) {
                                tvRebateCoupon.setText(rebateBo2.getCouponMessage() + " ¥ " + couponString2);
                            } else {
                                tvRebateCoupon.setText("预估 ¥ " + couponString2);
                            }
                            if (!TextUtils.isEmpty(rebateBo2.getPicUrl())) {
                                ivRebateIcon.setVisibility(0);
                                ImageLoaderManager.get().displayImage(rebateBo2.getPicUrl(), ivRebateIcon, this.imageOptions);
                            } else {
                                ivRebateIcon.setVisibility(8);
                            }
                        } else {
                            layoutRebate.setVisibility(4);
                        }
                    } else {
                        if (!TextUtils.isEmpty(rebateBo2.getCouponMessage())) {
                            tvRebateCoupon.setText(rebateBo2.getCouponMessage());
                        }
                        if (!TextUtils.isEmpty(rebateBo2.getPicUrl())) {
                            ivRebateIcon.setVisibility(0);
                            ImageLoaderManager.get().displayImage(rebateBo2.getPicUrl(), ivRebateIcon, this.imageOptions);
                        } else {
                            ivRebateIcon.setVisibility(8);
                        }
                        layoutRebate.setVisibility(0);
                    }
                } else {
                    layoutRebate.setVisibility(4);
                }
                if (kmAdvertise.getVisibility() == 8) {
                    kmAdvertise.setVisibility(0);
                }
            }
            if (GuessLikeGoodsData.TYPE_BANNER.equals(guessLikeGoodsData.getType())) {
                ivFlbEntrance.setVisibility(0);
                GuessLikeBannerBean guessLikeBannerBean = guessLikeGoodsData.getGuessLikeBannerBean();
                if (!(guessLikeBannerBean == null || guessLikeBannerBean.getPicUrl() == null)) {
                    String picUrl = guessLikeBannerBean.getPicUrl();
                    if (!picUrl.equals((String) ivFlbEntrance.getTag())) {
                        ivFlbEntrance.setTag(picUrl);
                        ivFlbEntrance.setImageResource(R.drawable.new_shop_cart_img_default);
                        ImageLoaderManager.get().displayImage(picUrl, ivFlbEntrance, this.imageOptions);
                    }
                }
            }
        }
        convertView.setLayoutParams(new AbsBaseListView.LayoutParams(-1, -1));
        return convertView;
    }

    private boolean isEqualZero(String src) {
        boolean equal = false;
        if (TextUtils.isEmpty(src)) {
            equal = true;
        } else if (TextUtils.equals(src, "00") || TextUtils.equals(src, "0")) {
            equal = true;
        }
        ZpLogger.i("DetainMentAdapter", "isEqualZero --> src = " + src + "; equal = " + equal);
        return equal;
    }

    private String getPicUrl(String pic_src) {
        String pic_des;
        if (TextUtils.isEmpty(pic_src)) {
            return pic_src;
        }
        String str = pic_src;
        if (this.mIs1080P) {
            pic_des = pic_src + "_430x430.jpg";
        } else {
            pic_des = pic_src + "_250x250.jpg";
        }
        return pic_des;
    }

    private boolean is1080p(Context ctx) {
        if (DeviceUtil.getScreenScaleFromDevice(ctx) > 1.2f) {
            return true;
        }
        return false;
    }

    public void setData(List<GuessLikeGoodsData> items) {
        ZpLogger.e("DetainMentAdapter", "setData.items = " + items.size());
        this.mItems = items;
    }

    public void onDestroy() {
        this.mItems = null;
    }
}
