package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

public class AppCompatCheckedTextView extends CheckedTextView {
    private static final int[] TINT_ATTRS = {16843016};
    private AppCompatTextHelper mTextHelper;

    public AppCompatCheckedTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public AppCompatCheckedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 16843720);
    }

    public AppCompatCheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(TintContextWrapper.wrap(context), attrs, defStyleAttr);
        this.mTextHelper = AppCompatTextHelper.create(this);
        this.mTextHelper.loadFromAttributes(attrs, defStyleAttr);
        this.mTextHelper.applyCompoundDrawablesTints();
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs, TINT_ATTRS, defStyleAttr, 0);
        setCheckMarkDrawable(a.getDrawable(0));
        a.recycle();
    }

    public void setCheckMarkDrawable(@DrawableRes int resId) {
        setCheckMarkDrawable(AppCompatResources.getDrawable(getContext(), resId));
    }

    public void setTextAppearance(Context context, int resId) {
        super.setTextAppearance(context, resId);
        if (this.mTextHelper != null) {
            this.mTextHelper.onSetTextAppearance(context, resId);
        }
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mTextHelper != null) {
            this.mTextHelper.applyCompoundDrawablesTints();
        }
    }
}
