package com.yunos.tvtaobao.biz.dialog;

import android.content.Context;
import android.util.AttributeSet;
import com.yunos.tvtaobao.biz.widget.FocusNoDeepRelativeLayout;

public class DialogFocusNoDeepRelativeout extends FocusNoDeepRelativeLayout {
    public DialogFocusNoDeepRelativeout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DialogFocusNoDeepRelativeout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogFocusNoDeepRelativeout(Context context) {
        super(context);
    }

    public boolean isScale() {
        return false;
    }
}
