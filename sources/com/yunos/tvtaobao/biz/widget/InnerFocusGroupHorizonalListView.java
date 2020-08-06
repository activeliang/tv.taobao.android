package com.yunos.tvtaobao.biz.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import com.alibaba.wireless.security.SecExceptionCode;

public class InnerFocusGroupHorizonalListView extends FocusGroupHorizonalListView {
    private boolean mItemInnerFocusState;

    public InnerFocusGroupHorizonalListView(Context context) {
        super(context);
    }

    public InnerFocusGroupHorizonalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerFocusGroupHorizonalListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void clearInnerFocusState() {
        this.mItemInnerFocusState = false;
    }

    /* access modifiers changed from: protected */
    public void performSelect(boolean select) {
        if (!select) {
            View selectedView = getSelectedView();
            if (selectedView instanceof InnerFocusLayout) {
                ((InnerFocusLayout) selectedView).clearItemSelected();
            }
            this.mItemInnerFocusState = false;
        }
        super.performSelect(select);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (this.mItemInnerFocusState) {
            View selectedView = getSelectedView();
            if (selectedView instanceof InnerFocusLayout) {
                return ((InnerFocusLayout) selectedView).onKeyUp(keyCode, event);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (getChildCount() <= 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (checkState(keyCode)) {
            return true;
        }
        if (this.mItemInnerFocusState) {
            View selectedView = getSelectedView();
            if (!(selectedView instanceof InnerFocusLayout)) {
                this.mItemInnerFocusState = false;
            } else if (((InnerFocusLayout) selectedView).onKeyDown(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void manualFindFocusInner(int keyCode) {
        switch (keyCode) {
            case 19:
            case 20:
                if (!this.mItemInnerFocusState) {
                    View selectedView = getSelectedView();
                    if (selectedView instanceof InnerFocusLayout) {
                        InnerFocusLayout focusView = (InnerFocusLayout) selectedView;
                        if (focusView.isChangedInnerKey(keyCode) && focusView.findFirstFocus(keyCode)) {
                            this.mItemInnerFocusState = true;
                            focusView.setNextFocusSelected();
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            default:
                return;
        }
    }

    public boolean actionInnerFocus(int keyCode, KeyEvent event) {
        if (this.mItemInnerFocusState && innerFocus(keyCode, event)) {
            View selectedView = getSelectedView();
            if (selectedView instanceof InnerFocusLayout) {
                ((InnerFocusLayout) selectedView).setNextFocusSelected();
                return true;
            }
        }
        return false;
    }

    public boolean preOnKeyDown(int keyCode, KeyEvent event) {
        if (checkState(keyCode)) {
            return false;
        }
        switch (keyCode) {
            case 19:
            case 20:
                if (!this.mItemInnerFocusState) {
                    View selectedView = getSelectedView();
                    if (selectedView instanceof InnerFocusLayout) {
                        InnerFocusLayout focusView = (InnerFocusLayout) selectedView;
                        if (!focusView.isChangedInnerKey(keyCode) || !focusView.findFirstFocus(keyCode)) {
                            return false;
                        }
                        this.mItemInnerFocusState = true;
                        return true;
                    }
                } else if (innerFocus(keyCode, event)) {
                    return true;
                } else {
                    return false;
                }
                break;
            case 21:
            case 22:
            case SecExceptionCode.SEC_ERROR_INIT_NO_DATA_FILE:
            case 123:
                if (innerFocus(keyCode, event)) {
                    return true;
                }
                break;
        }
        return super.preOnKeyDown(keyCode, event);
    }

    private boolean innerFocus(int keyCode, KeyEvent event) {
        if (this.mItemInnerFocusState) {
            View selectedView = getSelectedView();
            if (!(selectedView instanceof InnerFocusLayout) || !((InnerFocusLayout) selectedView).findNextFocus(keyCode, event)) {
                return false;
            }
            return true;
        }
        return false;
    }
}
