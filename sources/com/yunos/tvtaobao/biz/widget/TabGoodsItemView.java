package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yunos.tvtaobao.businessview.R;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.params.FocusRectParams;
import com.zhiping.dev.android.logger.ZpLogger;

public abstract class TabGoodsItemView extends RelativeLayout implements ItemListener {
    protected final String TAG = "TabGoodsItemView";
    protected View mBackgroudImageView;
    protected Context mContext;
    protected TextView mGoodsPriceTextView;
    protected TextView mGoodsRebateCoupon;
    protected ImageView mGoodsRebateIcon;
    protected TextView mGoodsTitleTextView;
    protected boolean mIsSelect;
    private Rect mItemFocusBound;
    private int mPosition;
    protected boolean mSetGoodsDrawable;
    protected boolean mSetInfoDrawable;
    private String mTabKey;
    protected boolean mbHeaderView;

    /* access modifiers changed from: protected */
    public abstract int getBackgroudViewResId();

    /* access modifiers changed from: protected */
    public abstract int getGoodsDrawableViewResId();

    /* access modifiers changed from: protected */
    public abstract int getGoodsPriceViewResId();

    /* access modifiers changed from: protected */
    public abstract int getGoodsRebateCouponViewResId();

    /* access modifiers changed from: protected */
    public abstract int getGoodsRebateIconViewResId();

    /* access modifiers changed from: protected */
    public abstract int getGoodsTitleViewResId();

    /* access modifiers changed from: protected */
    public abstract int getInfoDrawableViewResId();

    /* access modifiers changed from: protected */
    public abstract void handlerItemSelected(String str, int i, boolean z, View view);

    /* access modifiers changed from: protected */
    public abstract boolean isShowTitleOfNotSelect();

    public TabGoodsItemView(Context context) {
        super(context);
        onInitTabGoodsItemView(context);
    }

    public TabGoodsItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInitTabGoodsItemView(context);
    }

    public TabGoodsItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        onInitTabGoodsItemView(context);
    }

    public boolean isScale() {
        return true;
    }

    public FocusRectParams getFocusParams() {
        Rect focuse = getFocusedRect();
        onAjustItemFouceBound(focuse);
        return new FocusRectParams(focuse, 0.5f, 0.5f);
    }

    public int getItemWidth() {
        if (this.mBackgroudImageView == null) {
            this.mBackgroudImageView = findViewById(getBackgroudViewResId());
        }
        return this.mBackgroudImageView.getWidth();
    }

    public int getItemHeight() {
        if (this.mBackgroudImageView == null) {
            this.mBackgroudImageView = findViewById(getBackgroudViewResId());
        }
        return this.mBackgroudImageView.getHeight();
    }

    public Rect getManualPadding() {
        return null;
    }

    public void drawBeforeFocus(Canvas canvas) {
    }

    public void drawAfterFocus(Canvas canvas) {
    }

    public boolean isFinished() {
        return true;
    }

    private void onInitTabGoodsItemView(Context context) {
        this.mContext = context;
        this.mSetGoodsDrawable = false;
        this.mSetInfoDrawable = false;
        this.mIsSelect = false;
        this.mItemFocusBound = new Rect();
        this.mItemFocusBound.setEmpty();
        this.mItemFocusBound.left = this.mContext.getResources().getDimensionPixelSize(R.dimen.dp_4);
        this.mItemFocusBound.right = this.mContext.getResources().getDimensionPixelSize(R.dimen.dp_4);
        this.mItemFocusBound.top = this.mContext.getResources().getDimensionPixelSize(R.dimen.dp_2);
        this.mItemFocusBound.bottom = this.mContext.getResources().getDimensionPixelSize(R.dimen.dp_4);
        this.mBackgroudImageView = (ImageView) findViewById(getBackgroudViewResId());
        this.mGoodsTitleTextView = (TextView) findViewById(getGoodsTitleViewResId());
        this.mGoodsPriceTextView = (TextView) findViewById(getGoodsPriceViewResId());
        this.mGoodsRebateCoupon = (TextView) findViewById(getGoodsRebateCouponViewResId());
        this.mGoodsRebateIcon = (ImageView) findViewById(getGoodsRebateIconViewResId());
        this.mbHeaderView = false;
    }

    private Rect getFocusedRect() {
        Rect r = new Rect();
        getFocusedRect(r);
        return r;
    }

    private void onAjustItemFouceBound(Rect rt) {
        Rect rect = new Rect();
        rect.left = getPaddingLeft();
        rect.right = getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = getPaddingBottom();
        rt.left += rect.left;
        rt.top += rect.top;
        rt.right -= rect.right;
        rt.bottom -= rect.bottom;
        Rect focusbound = getFocusBoundRect();
        rt.left += focusbound.left;
        rt.top += focusbound.top;
        rt.right -= focusbound.right;
        rt.bottom -= focusbound.bottom;
    }

    public void setIsHeaderView(boolean bHeaderView) {
        this.mbHeaderView = bHeaderView;
    }

    public boolean isSetDefaultDrawable(int position) {
        if (position != this.mPosition || !this.mSetGoodsDrawable) {
            return true;
        }
        return false;
    }

    public void setDefaultDrawable(boolean setGoodsDrawable) {
        this.mSetGoodsDrawable = setGoodsDrawable;
    }

    public void setHideInfoDrawable(boolean setInfoDrawable) {
        this.mSetInfoDrawable = setInfoDrawable;
    }

    public boolean isHideInfoDrawable(int position) {
        if (position != this.mPosition || !this.mSetInfoDrawable) {
            return true;
        }
        return false;
    }

    private void handlerTitleView() {
        TextView title;
        if (isShowTitleOfNotSelect() || (title = (TextView) findViewById(getGoodsTitleViewResId())) == null) {
            return;
        }
        if (this.mIsSelect) {
            title.setVisibility(0);
        } else {
            title.setVisibility(4);
        }
    }

    private void onSetTitleVisibility(int visibility) {
        if (isShowTitleOfNotSelect()) {
            if (this.mGoodsTitleTextView == null) {
                this.mGoodsTitleTextView = (TextView) findViewById(getGoodsTitleViewResId());
            }
            this.mGoodsTitleTextView.setVisibility(visibility);
            if (this.mGoodsPriceTextView == null) {
                this.mGoodsPriceTextView = (TextView) findViewById(getGoodsPriceViewResId());
            }
            this.mGoodsPriceTextView.setVisibility(visibility);
            if (this.mGoodsRebateCoupon == null) {
                this.mGoodsRebateCoupon = (TextView) findViewById(getGoodsRebateCouponViewResId());
            }
            this.mGoodsRebateCoupon.setVisibility(visibility);
            if (this.mGoodsRebateIcon == null) {
                this.mGoodsRebateIcon = (ImageView) findViewById(getGoodsRebateIconViewResId());
            }
            this.mGoodsRebateIcon.setVisibility(visibility);
        }
    }

    public void onSetGoodsListDefaultDrawable(Drawable d, int position) {
        ImageView imageView;
        if (position == this.mPosition && (imageView = (ImageView) findViewById(getGoodsDrawableViewResId())) != null) {
            imageView.setBackgroundDrawable(d);
        }
    }

    public void onSetGoodsListDrawable(Drawable drawable, int position) {
        ImageView imageView;
        ZpLogger.i("TabGoodsItemView", "onSetGoodsListDrawable   position =  " + position + ";    mPosition = " + this.mPosition + "; drawable = " + drawable);
        if (position == this.mPosition) {
            handlerTitleView();
            if (!this.mSetGoodsDrawable && (imageView = (ImageView) findViewById(getGoodsDrawableViewResId())) != null) {
                imageView.setBackgroundDrawable(drawable);
                this.mSetGoodsDrawable = true;
            }
        }
    }

    public ImageView onGetGoodsListImageView() {
        return (ImageView) findViewById(getGoodsDrawableViewResId());
    }

    public void onSetInfoListDrawable(Drawable drawable, int position) {
        ZpLogger.i("TabGoodsItemView", "onSetInfoListDrawable   position =  " + position + ";    mPosition = " + this.mPosition + "; drawable = " + drawable);
        if (position == this.mPosition) {
            handlerTitleView();
            if (!this.mSetInfoDrawable) {
                ImageView imageView = (ImageView) findViewById(getInfoDrawableViewResId());
                imageView.setBackgroundDrawable(drawable);
                imageView.setVisibility(0);
                this.mSetInfoDrawable = true;
                onSetTitleVisibility(0);
            }
        }
    }

    public ImageView onGetInfoImageView() {
        return (ImageView) findViewById(getInfoDrawableViewResId());
    }

    public void onHideInfoImageView() {
        ((ImageView) findViewById(getInfoDrawableViewResId())).setVisibility(4);
        onSetTitleVisibility(4);
    }

    public void onItemSelected(boolean isSelected, View fatherView) {
        handlerItemSelected(this.mTabKey, this.mPosition, isSelected, fatherView);
        this.mIsSelect = isSelected;
        requestLayout();
    }

    public void onSetTabKey(String tabkey) {
        this.mTabKey = tabkey;
    }

    public String onGetTabKey() {
        return this.mTabKey;
    }

    public void onInitVariableValue() {
        if (!this.mIsSelect) {
            onSetTitleVisibility(4);
        }
    }

    public void onSetPosition(int position) {
        ZpLogger.i("TabGoodsItemView", "onSetPosition  mPosition = " + this.mPosition);
        this.mPosition = position;
    }

    public int onGetPosition() {
        return this.mPosition;
    }

    public void onDestroyAndClear() {
    }

    /* access modifiers changed from: protected */
    public Rect getFocusBoundRect() {
        return this.mItemFocusBound;
    }
}
