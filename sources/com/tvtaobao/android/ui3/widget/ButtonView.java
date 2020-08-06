package com.tvtaobao.android.ui3.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import com.tvtaobao.android.ui3.R;

public class ButtonView extends TextView {
    private static final int TYPE_ARC = 2;
    private static final int TYPE_BULE_GRAY = 0;
    private static final int TYPE_BULE_ORANGR = 1;
    private static final int TYPE_CIRCLE = 3;
    private static final int TYPE_HAIXIN = 4;

    public ButtonView(Context context) {
        super(context);
        init(context, (AttributeSet) null);
    }

    public ButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (!(context == null || attrs == null)) {
            setBackgroundLevel(context.obtainStyledAttributes(attrs, R.styleable.ui3wares_ButtonView).getInt(R.styleable.ui3wares_ButtonView_ui3wares_backgroundLevel, 0));
            if (!context.obtainStyledAttributes(attrs, R.styleable.ui3wares_CustomTextSize).getBoolean(R.styleable.ui3wares_CustomTextSize_ui3wares_custome_textsize, false)) {
                setTextSize(0, (float) getResources().getDimensionPixelSize(R.dimen.values_sp_24));
            }
        }
        setIncludeFontPadding(false);
    }

    public void setBackgroundLevel(int level) {
        switch (level) {
            case 0:
                setPadding(getResources().getDimensionPixelSize(R.dimen.values_dp_32), getResources().getDimensionPixelSize(R.dimen.values_dp_10), getResources().getDimensionPixelSize(R.dimen.values_dp_32), getResources().getDimensionPixelSize(R.dimen.values_dp_10));
                setBackgroundResource(R.drawable.ui3wares_buttonview_bule_gray_bg);
                setTextColor(getResources().getColorStateList(R.color.ui3wares_buttonview_bule_gray));
                return;
            case 1:
                setPadding(getResources().getDimensionPixelSize(R.dimen.values_dp_32), getResources().getDimensionPixelSize(R.dimen.values_dp_10), getResources().getDimensionPixelSize(R.dimen.values_dp_32), getResources().getDimensionPixelSize(R.dimen.values_dp_10));
                setBackgroundResource(R.drawable.ui3wares_buttonview_bule_orange_bg);
                setTextColor(getResources().getColorStateList(R.color.ui3wares_buttonview_bule_orange));
                return;
            case 2:
                setPadding(getResources().getDimensionPixelSize(R.dimen.values_dp_78), getResources().getDimensionPixelSize(R.dimen.values_dp_14), getResources().getDimensionPixelSize(R.dimen.values_dp_78), getResources().getDimensionPixelSize(R.dimen.values_dp_14));
                setBackgroundResource(R.drawable.ui3wares_buttonview_arc_bg);
                setTextColor(getResources().getColorStateList(R.color.ui3wares_buttonview_arc));
                return;
            case 3:
                setPadding(getResources().getDimensionPixelSize(R.dimen.values_dp_78), getResources().getDimensionPixelSize(R.dimen.values_dp_14), getResources().getDimensionPixelSize(R.dimen.values_dp_78), getResources().getDimensionPixelSize(R.dimen.values_dp_14));
                setBackgroundResource(R.drawable.ui3wares_buttonview_circle_bg);
                setTextColor(getResources().getColorStateList(R.color.ui3wares_buttonview_arc));
                return;
            case 4:
                setPadding(getResources().getDimensionPixelSize(R.dimen.values_dp_78), getResources().getDimensionPixelSize(R.dimen.values_dp_14), getResources().getDimensionPixelSize(R.dimen.values_dp_78), getResources().getDimensionPixelSize(R.dimen.values_dp_14));
                setBackgroundResource(R.drawable.ui3wares_buttonview_haixin_bg);
                setTextColor(getResources().getColorStateList(R.color.ui3wares_buttonview_arc));
                return;
            default:
                return;
        }
    }
}
