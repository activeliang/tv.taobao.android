package com.tvtaobao.android.takeoutwares;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CartActionView extends FrameLayout {
    private Drawable focusBg;
    private String mActionStr;
    private String mTips;
    Runnable syncTask;
    private TextView txtAction;
    private TextView txtTips;
    private Drawable unfocusBg;
    private View viewVLine;

    public interface OnActionListener {
        void onActionCallBack();
    }

    public CartActionView(Context context) {
        this(context, (AttributeSet) null);
    }

    public CartActionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CartActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.syncTask = new Runnable() {
            public void run() {
                CartActionView.this.syncState();
                CartActionView.this.postInvalidate();
            }
        };
        LayoutInflater.from(context).inflate(R.layout.takeoutwares_layout_action_button, this, true);
        findViews();
    }

    private void findViews() {
        this.txtAction = (TextView) findViewById(R.id.txt_action);
        this.viewVLine = findViewById(R.id.view_devide_line);
        this.txtTips = (TextView) findViewById(R.id.txt_tips);
        setDescendantFocusability(393216);
        setFocusable(true);
        setWillNotDraw(false);
        this.focusBg = getContext().getResources().getDrawable(R.drawable.takeoutwares_action_button_bg_focus);
        this.unfocusBg = getContext().getResources().getDrawable(R.drawable.takeoutwares_action_button_bg_unfocus);
    }

    public TextView getTxtAction() {
        return this.txtAction;
    }

    public TextView getTxtTips() {
        return this.txtTips;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        apply();
    }

    public void setData(String actionStr, String tips) {
        this.mTips = tips;
        this.mActionStr = actionStr;
        apply();
    }

    public void clearData() {
        setData((String) null, (String) null);
        apply();
    }

    public void apply() {
        removeCallbacks(this.syncTask);
        postDelayed(this.syncTask, 100);
    }

    /* access modifiers changed from: private */
    public void syncState() {
        this.txtAction.setText(this.mActionStr);
        if (!TextUtils.isEmpty(this.mTips)) {
            this.txtTips.setVisibility(0);
            this.viewVLine.setVisibility(0);
            this.txtTips.setText(this.mTips);
        } else {
            this.txtTips.setVisibility(8);
            this.viewVLine.setVisibility(8);
        }
        if (hasFocus()) {
            this.txtAction.setTextColor(-1);
            this.viewVLine.setBackgroundColor(-1);
            this.txtTips.setTextColor(-1);
            this.txtAction.setSelected(true);
            this.txtTips.setSelected(true);
        } else {
            this.txtAction.setTextColor(Color.parseColor("#8396ae"));
            this.viewVLine.setBackgroundColor(Color.parseColor("#808396ae"));
            this.txtTips.setTextColor(Color.parseColor("#8396ae"));
        }
        if (hasFocus()) {
            setBackgroundDrawable(this.focusBg);
        } else {
            setBackgroundDrawable(this.unfocusBg);
        }
        requestLayout();
    }

    public boolean isInEditMode() {
        if (TOWConfig.DEBUG) {
            return true;
        }
        return super.isInEditMode();
    }
}
