package com.yunos.tvtaobao.biz.focus_impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class FocusRoot extends FocusArea {
    public static final String TAG_FLAG_FOR_ROOT = "root";
    private FocusNode currFocusOn;

    public FocusRoot(@NonNull Context context) {
        this(context, (AttributeSet) null);
    }

    public FocusRoot(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusRoot(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getNode().setRoot(true);
        setTag(TAG_FLAG_FOR_ROOT);
    }

    public View focusSearch(int direction) {
        return null;
    }

    public View focusSearch(View focused, int direction) {
        return null;
    }

    public FocusNode getCurrFocusOn() {
        return this.currFocusOn;
    }

    public void setCurrFocusOn(FocusNode currFocusOn2) {
        this.currFocusOn = currFocusOn2;
    }
}
