package com.yunos.tvtaobao.biz.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.SingleLineTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import com.yunos.tvtaobao.businessview.R;
import java.util.ArrayList;
import java.util.Iterator;

public class AutoFitHelper {
    private static final int DEFAULT_MIN_TEXT_SIZE = 8;
    private static final float DEFAULT_PRECISION = 0.5f;
    private static final boolean SPEW = false;
    private static final String TAG = "AutoFitTextHelper";
    private boolean mEnabled;
    private boolean mIsAutofitting;
    private ArrayList<OnTextSizeChangeListener> mListeners;
    private int mMaxLines;
    private float mMaxTextSize;
    private float mMinTextSize;
    private View.OnLayoutChangeListener mOnLayoutChangeListener = new AutofitOnLayoutChangeListener();
    private TextPaint mPaint;
    private float mPrecision;
    private float mTextSize;
    private TextView mTextView;
    private TextWatcher mTextWatcher = new AutofitTextWatcher();

    public interface OnTextSizeChangeListener {
        void onTextSizeChange(float f, float f2);
    }

    public static AutoFitHelper create(TextView view) {
        return create(view, (AttributeSet) null, 0);
    }

    public static AutoFitHelper create(TextView view, AttributeSet attrs) {
        return create(view, attrs, 0);
    }

    public static AutoFitHelper create(TextView view, AttributeSet attrs, int defStyle) {
        AutoFitHelper helper = new AutoFitHelper(view);
        boolean sizeToFit = true;
        if (attrs != null) {
            Context context = view.getContext();
            int minTextSize = (int) helper.getMinTextSize();
            float precision = helper.getPrecision();
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TvTaoAutoFitTextView, defStyle, 0);
            sizeToFit = ta.getBoolean(R.styleable.TvTaoAutoFitTextView_sizeToFit, true);
            int minTextSize2 = ta.getDimensionPixelSize(R.styleable.TvTaoAutoFitTextView_minTextSize, minTextSize);
            float precision2 = ta.getFloat(R.styleable.TvTaoAutoFitTextView_precision, precision);
            ta.recycle();
            helper.setMinTextSize(0, (float) minTextSize2).setPrecision(precision2);
        }
        helper.setEnabled(sizeToFit);
        return helper;
    }

    private static void autofit(TextView view, TextPaint paint, float minTextSize, float maxTextSize, int maxLines, float precision) {
        int targetWidth;
        if (maxLines > 0 && maxLines != Integer.MAX_VALUE && (targetWidth = (view.getWidth() - view.getPaddingLeft()) - view.getPaddingRight()) > 0) {
            CharSequence text = view.getText();
            TransformationMethod method = view.getTransformationMethod();
            if (method != null) {
                text = method.getTransformation(text, view);
            }
            Context context = view.getContext();
            Resources r = Resources.getSystem();
            float size = maxTextSize;
            float high = size;
            if (context != null) {
                r = context.getResources();
            }
            DisplayMetrics displayMetrics = r.getDisplayMetrics();
            paint.set(view.getPaint());
            paint.setTextSize(size);
            if ((maxLines == 1 && paint.measureText(text, 0, text.length()) > ((float) targetWidth)) || getLineCount(text, paint, size, (float) targetWidth, displayMetrics) > maxLines) {
                size = getAutofitTextSize(text, paint, (float) targetWidth, maxLines, 0.0f, high, precision, displayMetrics);
            }
            if (size < minTextSize) {
                size = minTextSize;
            }
            view.setTextSize(0, size);
        }
    }

    private static float getAutofitTextSize(CharSequence text, TextPaint paint, float targetWidth, int maxLines, float low, float high, float precision, DisplayMetrics displayMetrics) {
        float mid = (low + high) / 2.0f;
        int lineCount = 1;
        StaticLayout layout = null;
        paint.setTextSize(TypedValue.applyDimension(0, mid, displayMetrics));
        if (maxLines != 1) {
            layout = new StaticLayout(text, paint, (int) targetWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
            lineCount = layout.getLineCount();
        }
        if (lineCount > maxLines) {
            if (high - low < precision) {
                return low;
            }
            return getAutofitTextSize(text, paint, targetWidth, maxLines, low, mid, precision, displayMetrics);
        } else if (lineCount < maxLines) {
            return getAutofitTextSize(text, paint, targetWidth, maxLines, mid, high, precision, displayMetrics);
        } else {
            float maxLineWidth = 0.0f;
            if (maxLines == 1) {
                maxLineWidth = paint.measureText(text, 0, text.length());
            } else {
                for (int i = 0; i < lineCount; i++) {
                    if (layout.getLineWidth(i) > maxLineWidth) {
                        maxLineWidth = layout.getLineWidth(i);
                    }
                }
            }
            if (high - low < precision) {
                return low;
            }
            if (maxLineWidth > targetWidth) {
                return getAutofitTextSize(text, paint, targetWidth, maxLines, low, mid, precision, displayMetrics);
            }
            return maxLineWidth < targetWidth ? getAutofitTextSize(text, paint, targetWidth, maxLines, mid, high, precision, displayMetrics) : mid;
        }
    }

    private static int getLineCount(CharSequence text, TextPaint paint, float size, float width, DisplayMetrics displayMetrics) {
        paint.setTextSize(TypedValue.applyDimension(0, size, displayMetrics));
        return new StaticLayout(text, paint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true).getLineCount();
    }

    private static int getMaxLines(TextView view) {
        TransformationMethod method = view.getTransformationMethod();
        if (method != null && (method instanceof SingleLineTransformationMethod)) {
            return 1;
        }
        if (Build.VERSION.SDK_INT >= 16) {
            return view.getMaxLines();
        }
        return -1;
    }

    private AutoFitHelper(TextView view) {
        float scaledDensity = view.getContext().getResources().getDisplayMetrics().scaledDensity;
        this.mTextView = view;
        this.mPaint = new TextPaint();
        setRawTextSize(view.getTextSize());
        this.mMaxLines = getMaxLines(view);
        this.mMinTextSize = 8.0f * scaledDensity;
        this.mMaxTextSize = this.mTextSize;
        this.mPrecision = DEFAULT_PRECISION;
    }

    public AutoFitHelper addOnTextSizeChangeListener(OnTextSizeChangeListener listener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<>();
        }
        this.mListeners.add(listener);
        return this;
    }

    public AutoFitHelper removeOnTextSizeChangeListener(OnTextSizeChangeListener listener) {
        if (this.mListeners != null) {
            this.mListeners.remove(listener);
        }
        return this;
    }

    public float getPrecision() {
        return this.mPrecision;
    }

    public AutoFitHelper setPrecision(float precision) {
        if (this.mPrecision != precision) {
            this.mPrecision = precision;
            autofit();
        }
        return this;
    }

    public float getMinTextSize() {
        return this.mMinTextSize;
    }

    public AutoFitHelper setMinTextSize(float size) {
        return setMinTextSize(2, size);
    }

    public AutoFitHelper setMinTextSize(int unit, float size) {
        Context context = this.mTextView.getContext();
        Resources r = Resources.getSystem();
        if (context != null) {
            r = context.getResources();
        }
        setRawMinTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
        return this;
    }

    private void setRawMinTextSize(float size) {
        if (size != this.mMinTextSize) {
            this.mMinTextSize = size;
            autofit();
        }
    }

    public float getMaxTextSize() {
        return this.mMaxTextSize;
    }

    public AutoFitHelper setMaxTextSize(float size) {
        return setMaxTextSize(2, size);
    }

    public AutoFitHelper setMaxTextSize(int unit, float size) {
        Context context = this.mTextView.getContext();
        Resources r = Resources.getSystem();
        if (context != null) {
            r = context.getResources();
        }
        setRawMaxTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
        return this;
    }

    private void setRawMaxTextSize(float size) {
        if (size != this.mMaxTextSize) {
            this.mMaxTextSize = size;
            autofit();
        }
    }

    public int getMaxLines() {
        return this.mMaxLines;
    }

    public AutoFitHelper setMaxLines(int lines) {
        if (this.mMaxLines != lines) {
            this.mMaxLines = lines;
            autofit();
        }
        return this;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    @SuppressLint({"NewApi"})
    public AutoFitHelper setEnabled(boolean enabled) {
        if (this.mEnabled != enabled) {
            this.mEnabled = enabled;
            if (enabled) {
                this.mTextView.addTextChangedListener(this.mTextWatcher);
                this.mTextView.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
                autofit();
            } else {
                this.mTextView.removeTextChangedListener(this.mTextWatcher);
                this.mTextView.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
                this.mTextView.setTextSize(0, this.mTextSize);
            }
        }
        return this;
    }

    public float getTextSize() {
        return this.mTextSize;
    }

    public void setTextSize(float size) {
        setTextSize(2, size);
    }

    public void setTextSize(int unit, float size) {
        if (!this.mIsAutofitting) {
            Context context = this.mTextView.getContext();
            Resources r = Resources.getSystem();
            if (context != null) {
                r = context.getResources();
            }
            setRawTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
        }
    }

    private void setRawTextSize(float size) {
        if (this.mTextSize != size) {
            this.mTextSize = size;
        }
    }

    /* access modifiers changed from: private */
    public void autofit() {
        float oldTextSize = this.mTextView.getTextSize();
        this.mIsAutofitting = true;
        autofit(this.mTextView, this.mPaint, this.mMinTextSize, this.mMaxTextSize, this.mMaxLines, this.mPrecision);
        this.mIsAutofitting = false;
        float textSize = this.mTextView.getTextSize();
        if (textSize != oldTextSize) {
            sendTextSizeChange(textSize, oldTextSize);
        }
    }

    private void sendTextSizeChange(float textSize, float oldTextSize) {
        if (this.mListeners != null) {
            Iterator<OnTextSizeChangeListener> it = this.mListeners.iterator();
            while (it.hasNext()) {
                it.next().onTextSizeChange(textSize, oldTextSize);
            }
        }
    }

    private class AutofitTextWatcher implements TextWatcher {
        private AutofitTextWatcher() {
        }

        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            AutoFitHelper.this.autofit();
        }

        public void afterTextChanged(Editable editable) {
        }
    }

    @SuppressLint({"NewApi"})
    private class AutofitOnLayoutChangeListener implements View.OnLayoutChangeListener {
        private AutofitOnLayoutChangeListener() {
        }

        public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            AutoFitHelper.this.autofit();
        }
    }
}
