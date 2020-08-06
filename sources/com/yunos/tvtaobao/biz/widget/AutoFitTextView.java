package com.yunos.tvtaobao.biz.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;
import com.yunos.tvtaobao.biz.widget.AutoFitHelper;

@SuppressLint({"AppCompatCustomView"})
public class AutoFitTextView extends TextView implements AutoFitHelper.OnTextSizeChangeListener {
    private AutoFitHelper mHelper;

    public AutoFitTextView(Context context) {
        super(context);
        init(context, (AttributeSet) null, 0);
    }

    public AutoFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AutoFitTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    @RequiresApi(api = 21)
    public AutoFitTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        this.mHelper = AutoFitHelper.create(this, attrs, defStyle).addOnTextSizeChangeListener(this);
    }

    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        if (this.mHelper != null) {
            this.mHelper.setTextSize(unit, size);
        }
    }

    public void setLines(int lines) {
        super.setLines(lines);
        if (this.mHelper != null) {
            this.mHelper.setMaxLines(lines);
        }
    }

    public void setMaxLines(int maxLines) {
        super.setMaxLines(maxLines);
        if (this.mHelper != null) {
            this.mHelper.setMaxLines(maxLines);
        }
    }

    public AutoFitHelper getAutofitHelper() {
        return this.mHelper;
    }

    public boolean isSizeToFit() {
        return this.mHelper.isEnabled();
    }

    public void setSizeToFit() {
        setSizeToFit(true);
    }

    public void setSizeToFit(boolean sizeToFit) {
        this.mHelper.setEnabled(sizeToFit);
    }

    public float getMaxTextSize() {
        return this.mHelper.getMaxTextSize();
    }

    public void setMaxTextSize(float size) {
        this.mHelper.setMaxTextSize(size);
    }

    public void setMaxTextSize(int unit, float size) {
        this.mHelper.setMaxTextSize(unit, size);
    }

    public float getMinTextSize() {
        return this.mHelper.getMinTextSize();
    }

    public void setMinTextSize(int minSize) {
        this.mHelper.setMinTextSize(2, (float) minSize);
    }

    public void setMinTextSize(int unit, float minSize) {
        this.mHelper.setMinTextSize(unit, minSize);
    }

    public float getPrecision() {
        return this.mHelper.getPrecision();
    }

    public void setPrecision(float precision) {
        this.mHelper.setPrecision(precision);
    }

    public void onTextSizeChange(float textSize, float oldTextSize) {
    }
}
