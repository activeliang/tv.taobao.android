package com.tvtaobao.android.buildorderwares.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ESSCLScrollView extends ScrollView {
    private static final String TAG = ESSCLScrollView.class.getSimpleName();
    EasySSCListener easySSCListener;

    public interface EasySSCListener {
        void onScrollChanged(int i, int i2, int i3, int i4);
    }

    public ESSCLScrollView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ESSCLScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ESSCLScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (this.easySSCListener != null) {
            this.easySSCListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public EasySSCListener getEasySSCListener() {
        return this.easySSCListener;
    }

    public void setEasySSCListener(EasySSCListener easySSCListener2) {
        this.easySSCListener = easySSCListener2;
    }
}
