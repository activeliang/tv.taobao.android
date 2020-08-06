package com.yunos.tvtaobao.biz.widget.newsku;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yunos.tv.core.common.ImageLoaderManager;
import com.yunos.tvtaobao.biz.base.BaseMVPActivity;
import com.yunos.tvtaobao.biz.base.IPresenter;
import com.yunos.tvtaobao.biz.request.bo.SkuPriceNum;
import com.yunos.tvtaobao.biz.request.bo.TBDetailResultV6;
import com.yunos.tvtaobao.biz.widget.CustomDialog;
import com.yunos.tvtaobao.biz.widget.newsku.interfaces.SkuInfoUpdate;
import com.yunos.tvtaobao.biz.widget.newsku.view.ISkuView;
import com.yunos.tvtaobao.biz.widget.newsku.view.WaitProgressDialog;
import com.yunos.tvtaobao.biz.widget.newsku.widget.NumChooseLayout;
import com.yunos.tvtaobao.biz.widget.newsku.widget.SkuItem;
import com.yunos.tvtaobao.biz.widget.newsku.widget.SkuItemLayout;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.List;

public abstract class SkuActivity<T extends IPresenter> extends BaseMVPActivity implements ISkuView {
    private static final String TAG = "SkuActivity";
    private final int PROMPT_NOT_RESOURCE = -1;
    protected int buyCount = 1;
    private CustomDialog customDialog;
    /* access modifiers changed from: private */
    public FrameLayout focusBackgroundLayout;
    protected ImageView goodsImage;
    protected TextView goodsPrice;
    protected TextView goodsTitle;
    protected String itemId;
    protected T mSkuPresenter;
    private WaitProgressDialog mWaitProgressDialog;
    protected NumChooseLayout numChooseLayout;
    protected Button okBtn;
    protected String sellerId;
    protected String shopId;
    private SkuInfoUpdate skuInfoUpdate = new SkuInfoUpdate() {
        public void addSelectedPropData(long propId, long valueId) {
            ZpLogger.i(SkuActivity.TAG, "SkuActivity.SkuInfoUpdate propId : " + propId + " ,valueId : " + valueId);
            SkuActivity.this.addSelectedPropData(propId, valueId);
        }

        public void deleteSelectedPropData(long propId, long valueId) {
            SkuActivity.this.deleteSelectedPropData(propId, valueId);
        }
    };
    protected LinearLayout skuLayout;
    protected String tradeType = TradeType.ADD_CART;

    /* access modifiers changed from: protected */
    public abstract void addSelectedPropData(long j, long j2);

    /* access modifiers changed from: protected */
    public abstract T createPresenter();

    /* access modifiers changed from: protected */
    public abstract void deleteSelectedPropData(long j, long j2);

    /* access modifiers changed from: protected */
    public abstract void initPresenter();

    public void initView() {
        super.initView();
        showProgressDialog(true);
        this.goodsImage = (ImageView) findViewById(R.id.sku_goods_image);
        this.goodsTitle = (TextView) findViewById(R.id.sku_goods_title);
        this.goodsPrice = (TextView) findViewById(R.id.sku_goods_price);
        this.skuLayout = (LinearLayout) findViewById(R.id.activity_sku_info_layout);
        this.focusBackgroundLayout = (FrameLayout) findViewById(R.id.layout_focus_background);
        this.okBtn = (Button) findViewById(R.id.activity_sku_info_ok);
        this.numChooseLayout = new NumChooseLayout(this);
        this.tradeType = getIntent().getStringExtra("type");
        this.itemId = getIntent().getStringExtra("itemId");
        this.sellerId = getIntent().getStringExtra("sellerId");
        this.shopId = getIntent().getStringExtra("shopId");
        this.buyCount = getIntent().getIntExtra("buyCount", 1);
        this.mSkuPresenter = this.mPresenter;
        this.numChooseLayout.setTradeType(this.tradeType);
        this.okBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    SkuActivity.this.focusBackgroundLayout.setBackgroundDrawable(SkuActivity.this.getResources().getDrawable(R.drawable.bg_sku_ok_focus_layout));
                } else {
                    SkuActivity.this.focusBackgroundLayout.setBackgroundDrawable((Drawable) null);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initPresenter();
    }

    public void initTitle(String title) {
        this.goodsTitle.setText(title);
    }

    public void initSkuView(List<TBDetailResultV6.SkuBaseBean.PropsBeanX> propsList) {
        if (propsList != null) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -1);
            lp.setMargins(0, (int) getResources().getDimension(R.dimen.dp_34), 0, 0);
            int propsSize = propsList.size();
            for (int i = 0; i < propsSize; i++) {
                SkuItemLayout skuItemLayout = new SkuItemLayout(this);
                skuItemLayout.setSkuUpdateListener(this.skuInfoUpdate);
                skuItemLayout.setProps(propsList.get(i));
                if (i > 0) {
                    skuItemLayout.setLayoutParams(lp);
                } else {
                    SkuItem skuItem = skuItemLayout.getSkuItem(0);
                    if (skuItem != null) {
                        skuItem.requestFocus();
                    }
                }
                this.skuLayout.addView(skuItemLayout);
            }
            this.numChooseLayout.setLayoutParams(lp);
        } else {
            this.numChooseLayout.getNumChooseItem().requestFocus();
        }
        this.skuLayout.addView(this.numChooseLayout);
        showProgressDialog(false);
    }

    public void initSkuKuCunAndPrice(SkuPriceNum skuPriceNum) {
        SkuPriceNum.PriceBean price;
        if (skuPriceNum != null) {
            if (!(this.goodsPrice == null || (price = skuPriceNum.getPrice()) == null)) {
                this.goodsPrice.setText(getResources().getString(R.string.new_shop_cart_sku_presale) + price.getPriceText());
            }
            this.numChooseLayout.setBuyCount((long) this.buyCount);
            ZpLogger.d(TAG, "SkuActivity.updateSkuKuCunAndPrice limit : " + skuPriceNum.getLimit());
            if (skuPriceNum.getLimit() == 0) {
                this.numChooseLayout.setKuCunNum(skuPriceNum.getQuantity(), 0);
            } else {
                this.numChooseLayout.setKuCunNum(skuPriceNum.getQuantity(), skuPriceNum.getLimit());
            }
        }
    }

    public void showUnitBuy(int times) {
        ZpLogger.d(TAG, "SkuActivity.showUnitBuy times : " + times);
        this.numChooseLayout.showUnitBuy(times);
    }

    public void updateSkuKuCunAndPrice(SkuPriceNum skuPriceNum) {
        SkuPriceNum.PriceBean price;
        if (skuPriceNum != null) {
            if (!(this.goodsPrice == null || (price = skuPriceNum.getPrice()) == null)) {
                this.goodsPrice.setText(getResources().getString(R.string.new_shop_cart_sku_presale) + price.getPriceText());
            }
            ZpLogger.d(TAG, "SkuActivity.updateSkuKuCunAndPrice limit : " + skuPriceNum.getLimit());
            this.numChooseLayout.setBuyCount(1);
            if (skuPriceNum.getLimit() == 0) {
                this.numChooseLayout.setKuCunNum(skuPriceNum.getQuantity(), 0);
            } else {
                this.numChooseLayout.setKuCunNum(skuPriceNum.getQuantity(), skuPriceNum.getLimit());
            }
        }
    }

    public void updateImage(String url) {
        ZpLogger.i(TAG, "SkuActivity.updateImage url : " + url);
        if (!url.startsWith("http:")) {
            url = "http:" + url;
        }
        ImageLoaderManager.get().displayImage(url, this.goodsImage);
    }

    public void updateValueViewStatus(Long propId, Long valueId, SkuItemViewStatus status) {
        SkuItemLayout skuItemLayout = getSkuItemLayout(propId.longValue());
        if (skuItemLayout != null) {
            skuItemLayout.updateValueViewStatus(propId, valueId, status);
        }
    }

    public void onShowError(String prompt) {
        onPromptDialog(-1, prompt);
    }

    public void onPromptDialog(int resource, String prompt) {
        CustomDialog.Builder mBuilder = new CustomDialog.Builder(this).setType(1).setResultMessage(prompt);
        if (resource != -1) {
            mBuilder.setHasIcon(true).setIcon(resource);
        }
        this.customDialog = mBuilder.create();
        this.customDialog.show();
    }

    public void onDialogDismiss() {
        if (this.customDialog != null) {
            if (this.customDialog.isShowing()) {
                this.customDialog.dismiss();
            }
            this.customDialog = null;
        }
    }

    private SkuItemLayout getSkuItemLayout(long propId) {
        int count = this.skuLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            SkuItemLayout prop = (SkuItemLayout) this.skuLayout.getChildAt(i);
            if (prop != null && prop.getPropId() == propId) {
                return prop;
            }
        }
        return null;
    }

    public void showProgressDialog(boolean show) {
        if (!isFinishing()) {
            if (this.mWaitProgressDialog == null) {
                this.mWaitProgressDialog = new WaitProgressDialog(this);
            }
            if (show && this.mWaitProgressDialog.isShowing()) {
                return;
            }
            if (!show && !this.mWaitProgressDialog.isShowing()) {
                return;
            }
            if (show) {
                this.mWaitProgressDialog.show();
            } else {
                this.mWaitProgressDialog.dismiss();
            }
        }
    }
}
