package com.tvtaobao.android.focus3;

import android.view.View;
import com.tvtaobao.android.focus3.FocusAssist;
import java.lang.ref.WeakReference;

public abstract class FocusSearchRuleA implements FocusAssist.FocusSearchRule {
    private static final String TAG = FocusSearchRuleA.class.getSimpleName();
    protected HistoryItem[] history = {null, null, null};

    public String getRuleName() {
        return "FocusSearchRuleA";
    }

    public final View onFocusSearch(View focused, int direction, View androidFocusSearchResult) {
        Focus3Logger.i(TAG, ".focusSearch :" + direction + "," + Focus3Util.getString(focused) + "," + Focus3Util.getString(androidFocusSearchResult));
        if (androidFocusSearchResult != null) {
            if (this.history[1] != null) {
                this.history[0] = this.history[1];
            }
            if (this.history[2] != null) {
                this.history[1] = this.history[2];
            }
            this.history[2] = HistoryItem.get(focused, direction, androidFocusSearchResult);
        }
        View rtn = applyRule(focused, direction, androidFocusSearchResult);
        if (rtn != androidFocusSearchResult) {
            Focus3Logger.i(TAG, ".focusSearch rule applied !");
        }
        Focus3Logger.i(TAG, ".focusSearch done:" + Focus3Util.getString(rtn));
        return rtn;
    }

    public View applyRule(View focused, int direction, View androidFocusSearchResult) {
        View rtn = androidFocusSearchResult;
        if (this.history[2] == null || this.history[1] == null || !Focus3Util.isContraryDirection(this.history[2].direction, this.history[1].direction)) {
            return rtn;
        }
        View rtn2 = (View) this.history[1].focused.get();
        if (!Focus3Util.isBinA(Focus3Util.findDecorView(focused), rtn2)) {
            return androidFocusSearchResult;
        }
        return rtn2;
    }

    public static class HistoryItem {
        WeakReference<View> afsr;
        int direction;
        WeakReference<View> focused;

        /* access modifiers changed from: private */
        public static HistoryItem get(View focused2, int direction2, View androidFocusSearchResult) {
            HistoryItem rtn = new HistoryItem();
            rtn.afsr = new WeakReference<>(androidFocusSearchResult);
            rtn.focused = new WeakReference<>(focused2);
            rtn.direction = direction2;
            return rtn;
        }

        public String toString() {
            return "HistoryItem{direction=" + this.direction + ", focused=" + this.focused + ", afsr=" + this.afsr + '}';
        }
    }
}
