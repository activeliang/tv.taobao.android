package com.tvtaobao.android.ui3.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Constraints;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.tvtaobao.android.ui3.R;
import com.tvtaobao.android.ui3.UI3Config;

public class DefaultBackgroundView extends ConstraintLayout {
    private ImageView ivIcon;

    public DefaultBackgroundView(Context context) {
        this(context, (AttributeSet) null);
    }

    public DefaultBackgroundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ivIcon = new ImageView(context);
        float defaultIconAlpha = 0.5f;
        int defaultBackgroundColor = getResources().getColor(R.color.values_color_606060);
        int defaultSrc = R.drawable.ui3wares_tvtaobao_word;
        int defaultWidth = getResources().getDimensionPixelOffset(R.dimen.values_dp_172);
        int defaultHeight = getResources().getDimensionPixelOffset(R.dimen.values_dp_32);
        ConstraintLayout.LayoutParams layoutParams = new Constraints.LayoutParams(defaultWidth, defaultHeight);
        layoutParams.topToTop = 0;
        layoutParams.bottomToBottom = 0;
        layoutParams.leftToLeft = 0;
        layoutParams.rightToRight = 0;
        addView(this.ivIcon, layoutParams);
        if (!(context == null || attrs == null)) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ui3wares_DefaultBackgroundView);
            defaultSrc = ta.getResourceId(R.styleable.ui3wares_DefaultBackgroundView_ui3wares_dbv_iconSrc, defaultSrc);
            defaultWidth = ta.getDimensionPixelOffset(R.styleable.ui3wares_DefaultBackgroundView_ui3wares_dbv_iconWidth, defaultWidth);
            defaultHeight = ta.getDimensionPixelOffset(R.styleable.ui3wares_DefaultBackgroundView_ui3wares_dbv_iconHeight, defaultHeight);
            defaultIconAlpha = ta.getFloat(R.styleable.ui3wares_DefaultBackgroundView_ui3wares_dbv_iconAlpha, 0.5f);
            if (ta != null) {
                ta.recycle();
            }
        }
        setIcon(defaultSrc);
        setIconAlpha(defaultIconAlpha);
        if (getBackground() == null) {
            setBackgroundColor(defaultBackgroundColor);
        }
        setWidthAndHeight(defaultWidth, defaultHeight);
    }

    public void setIcon(int iconResId) {
        if (this.ivIcon != null) {
            this.ivIcon.setImageResource(iconResId);
        }
    }

    public void setIconAlpha(float alpha) {
        if (this.ivIcon != null && alpha >= 0.0f && alpha <= 1.0f) {
            this.ivIcon.setAlpha(alpha);
        }
    }

    public void setWidthAndHeight(int width, int height) {
        if (this.ivIcon != null) {
            ConstraintLayout.LayoutParams layoutParams = new Constraints.LayoutParams(width, height);
            layoutParams.topToTop = 0;
            layoutParams.bottomToBottom = 0;
            layoutParams.leftToLeft = 0;
            layoutParams.rightToRight = 0;
            this.ivIcon.setLayoutParams(layoutParams);
        }
    }

    public boolean isInEditMode() {
        if (UI3Config.DEBUG) {
            return true;
        }
        return super.isInEditMode();
    }
}
