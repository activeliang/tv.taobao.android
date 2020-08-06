package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;

public class UpdateButton extends FrameLayout {
    private static final String TAG = "UpdateButton";
    private ImageView mBg;
    private int mBgFocusColor;
    private int mBgNormalColor;
    private ImageView mFocus;
    private boolean mIsNoBlur;
    private CharSequence mText;
    private int mTextFocusColor;
    private int mTextNormalColor;
    private int mTextShadowColor;
    private int mTextShadowR;
    private int mTextShadowY;
    private float mTextSize;
    private TextView mTextView;

    public UpdateButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public UpdateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.buttonText);
        this.mText = a.getText(R.styleable.buttonText_text);
        this.mTextSize = a.getDimension(R.styleable.buttonText_textSize, 0.0f);
        this.mIsNoBlur = a.getBoolean(R.styleable.buttonText_isNoBlur, false);
        a.recycle();
        if (this.mIsNoBlur) {
            initNoBlur(context);
        } else {
            initBlur(context);
        }
    }

    public UpdateButton(Context context) {
        super(context);
    }

    private void initNoBlur(Context context) {
        if (!isInEditMode()) {
            initBgNoBlur(context);
            initTextViewNoBlur(context);
        }
    }

    private void initBlur(Context context) {
        if (!isInEditMode()) {
            initBg(context);
            initTextView(context);
            initFocus(context);
        }
    }

    private void initBgNoBlur(Context context) {
        this.mBg = new ImageView(context);
        this.mBgFocusColor = context.getResources().getColor(R.color.bs_up_no_blur_button_focus);
        this.mBgNormalColor = context.getResources().getColor(R.color.bs_up_update_transparent);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.bs_up_no_blur_button_width), context.getResources().getDimensionPixelSize(R.dimen.bs_up_no_blur_button_height), 17);
        this.mBg.setBackgroundColor(this.mBgNormalColor);
        addView(this.mBg, lp);
    }

    private void initBg(Context context) {
        this.mBg = new ImageView(context);
        this.mBgFocusColor = context.getResources().getColor(R.color.bs_up_update_button_focus);
        this.mBgNormalColor = context.getResources().getColor(R.color.bs_up_update_button_normal);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.bs_up_button_width), context.getResources().getDimensionPixelSize(R.dimen.bs_up_button_height), 17);
        lp.bottomMargin = context.getResources().getDimensionPixelSize(R.dimen.bs_up_button_bg_margin_bottom);
        this.mBg.setBackgroundColor(this.mBgNormalColor);
        addView(this.mBg, lp);
    }

    private void initTextViewNoBlur(Context context) {
        this.mTextView = new TextView(context);
        this.mTextFocusColor = context.getResources().getColor(R.color.bs_up_update_white);
        this.mTextNormalColor = context.getResources().getColor(R.color.bs_up_update_black_50);
        this.mTextShadowColor = context.getResources().getColor(R.color.bs_up_no_blur_button_text_focus_shadow);
        this.mTextShadowY = context.getResources().getDimensionPixelSize(R.dimen.bs_up_no_blur_button_text_shadow_y);
        this.mTextShadowR = context.getResources().getDimensionPixelSize(R.dimen.bs_up_no_blur_button_text_shadow_r);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2, 17);
        this.mTextView.setText(this.mText);
        this.mTextView.setTextSize(this.mTextSize);
        this.mTextView.setTextColor(this.mTextNormalColor);
        addView(this.mTextView, lp);
    }

    private void initTextView(Context context) {
        this.mTextView = new TextView(context);
        this.mTextFocusColor = context.getResources().getColor(R.color.bs_up_update_white);
        this.mTextNormalColor = context.getResources().getColor(R.color.bs_up_update_white_50);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2, 17);
        lp.bottomMargin = context.getResources().getDimensionPixelSize(R.dimen.bs_up_button_text_margin_bottom);
        this.mTextView.setText(this.mText);
        this.mTextView.setTextSize(this.mTextSize);
        this.mTextView.setTextColor(this.mTextNormalColor);
        addView(this.mTextView, lp);
    }

    private void initFocus(Context context) {
        this.mFocus = new ImageView(context);
        this.mFocus.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bs_up_focus));
        addView(this.mFocus, new FrameLayout.LayoutParams(context.getResources().getDimensionPixelSize(R.dimen.bs_up_focus_width), context.getResources().getDimensionPixelSize(R.dimen.bs_up_focus_height), 17));
        this.mFocus.setVisibility(4);
    }

    public void showFocus() {
        if (this.mFocus != null) {
            this.mFocus.setVisibility(0);
            this.mFocus.bringToFront();
            this.mFocus.startAnimation(getFocusAnimation(true, 100));
        }
        if (this.mTextView != null) {
            this.mTextView.setTextColor(this.mTextFocusColor);
            if (this.mIsNoBlur) {
                this.mTextView.setShadowLayer((float) this.mTextShadowR, 0.0f, (float) this.mTextShadowY, this.mTextShadowColor);
            }
        }
        if (this.mBg != null) {
            this.mBg.setBackgroundColor(this.mBgFocusColor);
        }
    }

    public void hideFocus() {
        if (this.mFocus != null) {
            this.mFocus.startAnimation(getFocusAnimation(false, 100));
        }
        if (this.mTextView != null) {
            this.mTextView.setTextColor(this.mTextNormalColor);
            if (this.mIsNoBlur) {
                this.mTextView.setShadowLayer(0.0f, 0.0f, 0.0f, 0);
            }
        }
        if (this.mBg != null) {
            this.mBg.setBackgroundColor(this.mBgNormalColor);
        }
    }

    private AnimationSet getFocusAnimation(boolean show, long duration) {
        int from = 1;
        int to = 0;
        if (show) {
            from = 0;
            to = 1;
        }
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(new AlphaAnimation((float) from, (float) to));
        set.setDuration(duration);
        set.setFillAfter(true);
        return set;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        ZpLogger.d(TAG, "onFocusChanged: " + gainFocus);
        if (gainFocus) {
            showFocus();
        } else {
            hideFocus();
        }
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }
}
