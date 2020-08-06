package com.tvtaobao.android.ui3.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import com.tvtaobao.android.ui3.R;

public class LoadingView extends ConstraintLayout {
    private int dp60;
    private ProgressBar progressBar;
    private Window window;

    private LoadingView() {
        super((Context) null);
    }

    private LoadingView(Context context, Window window2) {
        super(context);
        this.window = window2;
        init();
    }

    public static LoadingView showOn(Window window2) {
        if (window2 == null) {
            return null;
        }
        LoadingView rtn = new LoadingView(window2.getContext(), window2);
        View decorView = window2.getDecorView();
        if (!(decorView instanceof FrameLayout)) {
            return rtn;
        }
        ((FrameLayout) decorView).addView(rtn, new FrameLayout.LayoutParams(-1, -1));
        return rtn;
    }

    private void init() {
        this.dp60 = getResources().getDimensionPixelSize(R.dimen.values_dp_60);
        this.progressBar = new ProgressBar(getContext());
        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(this.dp60, this.dp60);
        lp.leftToLeft = 0;
        lp.rightToRight = 0;
        lp.topToTop = 0;
        lp.bottomToBottom = 0;
        addView(this.progressBar, lp);
        setBackgroundColor(getResources().getColor(R.color.values_color_606060));
    }

    public void dismiss() {
        if (this.window != null) {
            ViewParent vp = getParent();
            if (vp instanceof ViewGroup) {
                ((ViewGroup) vp).removeView(this);
            }
        }
    }

    public ProgressBar getProgressBar() {
        return this.progressBar;
    }
}
