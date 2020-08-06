package com.tvtaobao.android.takeoutwares;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.tvtaobao.android.takeoutwares.AddAndReduceView;

public class CartItemView extends FrameLayout {
    /* access modifiers changed from: private */
    public AddAndReduceView addAndReduce;
    Drawable focusBg;
    /* access modifiers changed from: private */
    public TextView goodName;
    private TextView goodNumLimit;
    /* access modifiers changed from: private */
    public TextView goodOriPrice;
    int goodOriPriceClrN;
    /* access modifiers changed from: private */
    public TextView goodPrice;
    int goodPriceClrN;
    /* access modifiers changed from: private */
    public TextView goodSku;
    int goodSkuClrN;
    Drawable unfocusBg;

    public CartItemView(Context context) {
        this(context, (AttributeSet) null);
    }

    public CartItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CartItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.takeoutwares_layout_shop_cart_item, this);
        findViews();
    }

    private void findViews() {
        this.goodName = (TextView) findViewById(R.id.good_name);
        this.addAndReduce = (AddAndReduceView) findViewById(R.id.add_and_reduce);
        this.goodPrice = (TextView) findViewById(R.id.good_price);
        this.goodOriPrice = (TextView) findViewById(R.id.good_ori_price);
        this.goodNumLimit = (TextView) findViewById(R.id.good_num_limit);
        this.goodSku = (TextView) findViewById(R.id.good_sku);
        this.goodPriceClrN = this.goodPrice.getCurrentTextColor();
        this.goodOriPriceClrN = this.goodOriPrice.getCurrentTextColor();
        this.goodSkuClrN = this.goodSku.getCurrentTextColor();
        this.focusBg = getContext().getResources().getDrawable(R.drawable.takeoutwares_incartitem_bg_focus);
        this.unfocusBg = getContext().getResources().getDrawable(R.drawable.takeoutwares_incartitem_bg_unfocus);
        int tmpDp24 = getContext().getResources().getDimensionPixelSize(R.dimen.values_dp_24);
        this.addAndReduce.setCountSizeInPx(tmpDp24);
        this.addAndReduce.setBtnWHInPx(tmpDp24, tmpDp24);
        this.addAndReduce.setCountStyle(AddAndReduceView.CountStyle.withX);
        this.addAndReduce.setBtnStyle(AddAndReduceView.BtnStyle.inCartItem);
        this.addAndReduce.addFocusChangeListener(new AddAndReduceView.AARVFocusChangeListener() {
            public void onFocusChange(boolean hasFocus) {
                if (hasFocus) {
                    CartItemView.this.setBackgroundDrawable(CartItemView.this.focusBg);
                    CartItemView.this.addAndReduce.setStyle(AddAndReduceView.Style.lmr).setCountStyle(AddAndReduceView.CountStyle.noX).setFocusPos(AddAndReduceView.FocusPos.addBtn).apply((Runnable) null);
                    CartItemView.this.goodPrice.setTextColor(-1);
                    CartItemView.this.goodOriPrice.setTextColor(-1);
                    CartItemView.this.goodSku.setTextColor(-1);
                    CartItemView.this.goodName.setSelected(true);
                    return;
                }
                CartItemView.this.setBackgroundDrawable(CartItemView.this.unfocusBg);
                CartItemView.this.addAndReduce.setStyle(AddAndReduceView.Style._m_).setCountStyle(AddAndReduceView.CountStyle.withX).setFocusPos((AddAndReduceView.FocusPos) null).apply(new Runnable() {
                    public void run() {
                        CartItemView.this.goodName.setSelected(false);
                    }
                });
                CartItemView.this.goodPrice.setTextColor(CartItemView.this.goodPriceClrN);
                CartItemView.this.goodOriPrice.setTextColor(CartItemView.this.goodOriPriceClrN);
                CartItemView.this.goodSku.setTextColor(CartItemView.this.goodSkuClrN);
            }
        });
    }

    public TextView getGoodName() {
        return this.goodName;
    }

    public AddAndReduceView getAddAndReduce() {
        return this.addAndReduce;
    }

    public TextView getGoodPrice() {
        return this.goodPrice;
    }

    public TextView getGoodOriPrice() {
        return this.goodOriPrice;
    }

    public TextView getGoodNumLimit() {
        return this.goodNumLimit;
    }

    public TextView getGoodSku() {
        return this.goodSku;
    }

    public Drawable getFocusBg() {
        return this.focusBg;
    }

    public Drawable getUnfocusBg() {
        return this.unfocusBg;
    }

    public boolean isInEditMode() {
        if (TOWConfig.DEBUG) {
            return true;
        }
        return super.isInEditMode();
    }
}
