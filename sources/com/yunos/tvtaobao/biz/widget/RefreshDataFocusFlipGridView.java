package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusFlipGridView;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusRelativeLayout;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.reflect.Field;

public class RefreshDataFocusFlipGridView extends FocusFlipGridView {
    private final String TAG = "RefreshDataFocusFlipGridView";

    public RefreshDataFocusFlipGridView(Context context) {
        super(context);
        init();
    }

    public RefreshDataFocusFlipGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RefreshDataFocusFlipGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setGroupFlags(getGroupFlags() & -35);
    }

    public boolean preOnKeyDown(int keyCode, KeyEvent event) {
        int selectedPos = getSelectedItemPosition();
        ZpLogger.i("RefreshDataFocusFlipGridView", "preOnKeyDown selectedPos=" + selectedPos + " count=" + getHeaderViewsCount());
        if (selectedPos < getHeaderViewsCount()) {
            View view = getSelectedView();
            if (view instanceof FocusRelativeLayout) {
                if ((keyCode == 19 || keyCode == 20) && !isFlipFinished()) {
                    return false;
                }
                if (((FocusRelativeLayout) view).preOnKeyDown(keyCode, event)) {
                    return true;
                }
            }
        }
        boolean ret = super.preOnKeyDown(keyCode, event);
        ZpLogger.i("RefreshDataFocusFlipGridView", "preOnKeyDown ret=" + ret);
        return ret;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean ret = super.onKeyDown(keyCode, event);
        ZpLogger.i("RefreshDataFocusFlipGridView", "onKeyDown ret=" + ret + " adapter=" + getAdapter() + " childCount=" + getChildCount());
        return ret;
    }

    /* access modifiers changed from: protected */
    public void layoutChildren() {
        ZpLogger.i("RefreshDataFocusFlipGridView", "layoutChildren mNextSelectedPosition=" + this.mNextSelectedPosition + " mSelectedPosition=" + this.mSelectedPosition);
        if (this.mNextSelectedPosition != this.mSelectedPosition) {
            ZpLogger.i("RefreshDataFocusFlipGridView", "layoutChildren success=" + setNeedResetParamEnable());
        }
        super.layoutChildren();
    }

    /* access modifiers changed from: protected */
    public boolean setNeedResetParamEnable() {
        try {
            Field mNeedResetParamField = FocusFlipGridView.class.getDeclaredField("mNeedResetParam");
            mNeedResetParamField.setAccessible(true);
            mNeedResetParamField.setBoolean(this, true);
            return true;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        return false;
    }
}
