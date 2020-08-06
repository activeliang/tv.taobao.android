package com.ali.auth.third.offline.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class AUAccountAutoCompleteTextView extends AutoCompleteTextView {
    private boolean mAutoCompleteEnable = true;
    private DismissDropDownListener mDismissDropDownListener;
    private int mSmartThreshold = 0;

    public interface DismissDropDownListener {
        void dismissDropDown();
    }

    public AUAccountAutoCompleteTextView(Context context) {
        super(context);
    }

    public AUAccountAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AUAccountAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setAutoComplete(boolean enable) {
        this.mAutoCompleteEnable = enable;
    }

    public void setThreshold(int threshold) {
        if (threshold < 0) {
            threshold = 0;
        }
        this.mSmartThreshold = threshold;
    }

    public int getThreshold() {
        return this.mSmartThreshold;
    }

    public boolean enoughToFilter() {
        return this.mAutoCompleteEnable && getText().length() >= this.mSmartThreshold;
    }

    public void dismissDropDown() {
        super.dismissDropDown();
        if (this.mDismissDropDownListener != null) {
            this.mDismissDropDownListener.dismissDropDown();
        }
    }

    public void registerDismissDropDownListener(DismissDropDownListener l) {
        this.mDismissDropDownListener = l;
    }
}
