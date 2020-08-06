package com.tvtaobao.android.takeoutwares;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SaleTipView extends FrameLayout {
    private ConstraintLayout constraintLayout;
    private Style style;
    private TextView styleA;
    private LinearLayout styleB;
    private TextView tipExt;
    private TextView tipMain;

    public enum Style {
        limitTip,
        mainAndExtTip
    }

    public SaleTipView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SaleTipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SaleTipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.style = Style.mainAndExtTip;
        if (!(context == null || attrs == null)) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.takeoutwares);
            int tmpVar = ta.getInteger(R.styleable.takeoutwares_takeoutwares_saleTipStyle, Style.mainAndExtTip.ordinal());
            this.style = (tmpVar < 0 || tmpVar >= Style.values().length) ? Style.mainAndExtTip : Style.values()[tmpVar];
            ta.recycle();
        }
        LayoutInflater.from(context).inflate(R.layout.takeoutwares_layout_sale_tip, this);
        findViews();
    }

    private void findViews() {
        this.constraintLayout = (ConstraintLayout) findViewById(R.id.constraint_layout);
        this.styleA = (TextView) findViewById(R.id.style_a);
        this.styleB = (LinearLayout) findViewById(R.id.style_b);
        this.tipMain = (TextView) findViewById(R.id.tip_main);
        this.tipExt = (TextView) findViewById(R.id.tip_ext);
        syncState();
    }

    public void setTipTxt(String tipMaintxt, String tipExtTxt) {
        this.tipMain.setText(tipMaintxt);
        this.tipExt.setText(tipExtTxt);
        if (TextUtils.isEmpty(tipMaintxt)) {
            this.tipMain.setVisibility(8);
        } else {
            this.tipMain.setVisibility(0);
        }
        if (TextUtils.isEmpty(tipExtTxt)) {
            this.tipExt.setVisibility(8);
        } else {
            this.tipExt.setVisibility(0);
        }
        if (!TextUtils.isEmpty(tipMaintxt) || !TextUtils.isEmpty(tipExtTxt)) {
            setVisibility(0);
        } else {
            setVisibility(8);
        }
    }

    public void setTipTxt(String limitTxt) {
        this.styleA.setText(limitTxt);
    }

    public Style getStyle() {
        return this.style;
    }

    public void setStyle(Style style2) {
        if (style2 != null) {
            this.style = style2;
            syncState();
        }
    }

    private void syncState() {
        if (this.style == Style.mainAndExtTip) {
            this.styleA.setVisibility(8);
            this.styleB.setVisibility(0);
        } else if (this.style == Style.limitTip) {
            this.styleB.setVisibility(8);
            this.styleA.setVisibility(0);
        }
    }

    public boolean isInEditMode() {
        if (TOWConfig.DEBUG) {
            return true;
        }
        return super.isInEditMode();
    }
}
