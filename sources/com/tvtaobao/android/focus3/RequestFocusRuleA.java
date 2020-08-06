package com.tvtaobao.android.focus3;

import android.graphics.Rect;
import com.tvtaobao.android.focus3.FocusAssist;

public class RequestFocusRuleA implements FocusAssist.RequestFocusRule {
    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        return false;
    }

    public String getRuleName() {
        return "RequestFocusRuleA";
    }
}
