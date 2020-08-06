package com.ali.auth.third.offline.iv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import com.ali.auth.third.offline.R;

public class AliUserSmsCodeView extends View {
    private boolean autoHideKeyboard = true;
    private float mDividerWidth = 6.0f;
    private int mHeight;
    private int mNextUnderLineColor = ViewCompat.MEASURED_STATE_MASK;
    private OnCompletedListener mOnCompletedListener;
    private OnTextChangedListener mOnTextChangedListener;
    private RectF[] mOuterRects;
    private StringBuilder mTextBuilder;
    private int mTextColor = ViewCompat.MEASURED_STATE_MASK;
    private int mTextCount = 4;
    private Paint mTextPaint;
    private PointF[] mTextPositions;
    private RectF[] mTextRects;
    private float mTextSize = 36.0f;
    private int mUnderLineColor = ViewCompat.MEASURED_STATE_MASK;
    private Paint mUnderLinePaint;
    private float mUnderLineStrokeWidth = 1.0f;
    private int mWidth;

    public interface OnCompletedListener {
        void onCompleted(String str);
    }

    public interface OnTextChangedListener {
        void onTextChanged(String str);
    }

    public AliUserSmsCodeView(Context context) {
        super(context);
        init(context, (AttributeSet) null);
    }

    public AliUserSmsCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AliUserSmsCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AliUserSmsCodeView);
            this.mTextCount = typedArray.getInt(R.styleable.AliUserSmsCodeView_scTextCount, this.mTextCount);
            this.mTextColor = typedArray.getColor(R.styleable.AliUserSmsCodeView_scTextColor, this.mTextColor);
            this.mTextSize = typedArray.getDimension(R.styleable.AliUserSmsCodeView_scTextSize, TypedValue.applyDimension(2, this.mTextSize, context.getResources().getDisplayMetrics()));
            this.mDividerWidth = typedArray.getDimension(R.styleable.AliUserSmsCodeView_scDividerWidth, TypedValue.applyDimension(1, this.mDividerWidth, context.getResources().getDisplayMetrics()));
            this.mUnderLineStrokeWidth = typedArray.getDimension(R.styleable.AliUserSmsCodeView_scUnderLineStrokeWidth, TypedValue.applyDimension(1, this.mUnderLineStrokeWidth, context.getResources().getDisplayMetrics()));
            this.mUnderLineColor = typedArray.getColor(R.styleable.AliUserSmsCodeView_scUnderLineColor, this.mUnderLineColor);
            this.mNextUnderLineColor = typedArray.getColor(R.styleable.AliUserSmsCodeView_scNextUnderLineColor, this.mNextUnderLineColor);
            typedArray.recycle();
        }
        this.mTextBuilder = new StringBuilder(this.mTextCount);
        this.mTextPositions = new PointF[this.mTextCount];
        this.mOuterRects = new RectF[this.mTextCount];
        this.mTextRects = new RectF[this.mTextCount];
        initPaint();
        setFocusableInTouchMode(true);
    }

    private void initPaint() {
        if (this.mTextPaint == null) {
            this.mTextPaint = new Paint(1);
        }
        this.mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setTextSize(this.mTextSize);
        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
        this.mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        if (this.mUnderLinePaint == null) {
            this.mUnderLinePaint = new Paint(1);
        }
        this.mUnderLinePaint.setStyle(Paint.Style.STROKE);
        this.mUnderLinePaint.setColor(this.mUnderLineColor);
        this.mUnderLinePaint.setStrokeWidth(this.mUnderLineStrokeWidth);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        this.mHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        measureChildPositions();
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    private void measureChildPositions() {
        Paint.FontMetricsInt fontMetricsInt = this.mTextPaint.getFontMetricsInt();
        float textWidth = this.mTextPaint.measureText("0");
        float baseLine = (((float) (this.mHeight / 2)) + (((float) (fontMetricsInt.bottom - fontMetricsInt.top)) / 2.0f)) - ((float) fontMetricsInt.bottom);
        float itemWidth = (((float) this.mWidth) - (((float) (this.mTextCount - 1)) * this.mDividerWidth)) / ((float) this.mTextCount);
        for (int i = 0; i < this.mTextCount; i++) {
            this.mTextPositions[i] = new PointF((((float) i) * itemWidth) + (((float) i) * this.mDividerWidth) + (itemWidth / 2.0f), baseLine);
            this.mOuterRects[i] = new RectF((((float) i) * itemWidth) + (((float) i) * this.mDividerWidth), 0.0f, (((float) (i + 1)) * itemWidth) + (((float) i) * this.mDividerWidth), (float) this.mHeight);
            this.mTextRects[i] = new RectF(this.mTextPositions[i].x - (textWidth / 2.0f), this.mTextPositions[i].y + ((float) fontMetricsInt.top), this.mTextPositions[i].x + (textWidth / 2.0f), this.mTextPositions[i].y + ((float) fontMetricsInt.bottom));
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initPaint();
        int realTextLen = this.mTextBuilder.length();
        for (int i = 0; i < this.mTextCount; i++) {
            if (i < realTextLen) {
                canvas.drawText(this.mTextBuilder.toString(), i, i + 1, this.mTextPositions[i].x, this.mTextPositions[i].y, this.mTextPaint);
            }
            int underLineColor = this.mUnderLineColor;
            if (hasFocus() && i == realTextLen) {
                underLineColor = this.mNextUnderLineColor;
            } else if (i < realTextLen) {
                underLineColor = this.mNextUnderLineColor;
            }
            this.mUnderLinePaint.setColor(underLineColor);
            drawUnderLine(canvas, this.mUnderLinePaint, this.mOuterRects[i], this.mTextRects[i]);
            setBackgroundColor(-1);
        }
    }

    public void drawUnderLine(Canvas canvas, Paint paint, RectF rectF, RectF textRectF) {
        Canvas canvas2 = canvas;
        canvas2.drawLine(textRectF.left - (textRectF.width() / 2.0f), rectF.bottom, (textRectF.width() / 2.0f) + textRectF.right, rectF.bottom, paint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction() != 0) {
            return true;
        }
        focus();
        return true;
    }

    public void focus() {
        requestFocus();
        postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager) AliUserSmsCodeView.this.getContext().getSystemService("input_method");
                if (imm != null) {
                    imm.showSoftInput(AliUserSmsCodeView.this, 2);
                }
            }
        }, 100);
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        BaseInputConnection fic = new BaseInputConnection(this, false) {
            public boolean deleteSurroundingText(int beforeLength, int afterLength) {
                return sendKeyEvent(new KeyEvent(0, 67)) && sendKeyEvent(new KeyEvent(1, 67));
            }
        };
        outAttrs.actionLabel = null;
        outAttrs.inputType = 3;
        outAttrs.imeOptions = 5;
        return fic;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 67 && this.mTextBuilder.length() > 0) {
            this.mTextBuilder.deleteCharAt(this.mTextBuilder.length() - 1);
            if (this.mOnTextChangedListener != null) {
                this.mOnTextChangedListener.onTextChanged(this.mTextBuilder.toString());
            }
            invalidate();
        } else if (keyCode >= 7 && keyCode <= 16 && this.mTextBuilder.length() < this.mTextCount) {
            this.mTextBuilder.append(event.getDisplayLabel());
            if (this.mOnTextChangedListener != null) {
                this.mOnTextChangedListener.onTextChanged(this.mTextBuilder.toString());
            }
            invalidate();
        }
        if (this.mTextBuilder.length() >= this.mTextCount && this.autoHideKeyboard) {
            if (this.mOnCompletedListener != null) {
                this.mOnCompletedListener.onCompleted(this.mTextBuilder.toString());
            }
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService("input_method");
            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public String getText() {
        return this.mTextBuilder != null ? this.mTextBuilder.toString() : "";
    }

    public void setTextCount(int mTextCount2) {
        if (this.mTextCount != mTextCount2) {
            this.mTextCount = mTextCount2;
            invalidate();
        }
    }

    public int getTextCount() {
        return this.mTextCount;
    }

    public void clearText() {
        if (this.mTextBuilder.length() != 0) {
            this.mTextBuilder.delete(0, this.mTextBuilder.length());
            if (this.mOnTextChangedListener != null) {
                this.mOnTextChangedListener.onTextChanged(this.mTextBuilder.toString());
            }
            invalidate();
        }
    }

    public void setOnTextChangedListener(OnTextChangedListener mOnTextChangedListener2) {
        this.mOnTextChangedListener = mOnTextChangedListener2;
    }

    public void setOnCompletedListener(OnCompletedListener onCompletedListener) {
        this.mOnCompletedListener = onCompletedListener;
    }
}
