package com.yunos.tvtaobao.biz.dialog;

import android.content.Context;
import android.util.AttributeSet;
import com.yunos.tvtaobao.biz.widget.FocusNoDeepLinearLayout;

public class DialogFocusNoDeepLinearLayout extends FocusNoDeepLinearLayout {
    public DialogFocusNoDeepLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DialogFocusNoDeepLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogFocusNoDeepLinearLayout(Context context) {
        super(context);
    }

    public boolean isScale() {
        return false;
    }
}
