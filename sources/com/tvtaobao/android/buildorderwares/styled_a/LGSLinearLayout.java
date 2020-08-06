package com.tvtaobao.android.buildorderwares.styled_a;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.tvtaobao.android.buildorderwares.BOWConfig;
import com.tvtaobao.android.buildorderwares.BOWLogger;
import com.tvtaobao.android.buildorderwares.R;

public class LGSLinearLayout extends LinearLayout {
    private static final int FLAG_DRAW_CASE_separator_skip_focus = 1;
    private static final String TAG = LGSLinearLayout.class.getSimpleName();
    private float[] corners;
    private RectF fitRectF;
    private int flag;
    private int[] gradientColors;
    private Path gradientPath;
    private float[] gradientPoints;
    private int initColorFrom;
    private int initColorTo;
    private float initRadius;
    private int initStrokeColor;
    private float initStrokeWidth;
    private Style initStyle;
    private Paint linePaint;
    private LinearGradient linearGradientShader;
    private Paint pathPaint;
    private float radius;
    private float strokeWidth;
    private Style style;

    public enum Style {
        blank,
        round,
        rect
    }

    public LGSLinearLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public LGSLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LGSLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.flag = 0;
        setWillNotDraw(false);
        this.initStyle = Style.round;
        this.initStrokeColor = Color.parseColor("#334558");
        this.initColorFrom = this.initStrokeColor;
        this.initColorTo = this.initStrokeColor;
        this.initRadius = context.getResources().getDimension(R.dimen.values_dp_3);
        this.initStrokeWidth = 3.0f;
        if (!(context == null || attrs == null)) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.buildorderwares_styled_a);
            this.initRadius = ta.getDimension(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_radius, this.initRadius);
            this.initStrokeWidth = ta.getDimension(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_strokeWidth, this.initStrokeWidth);
            this.initColorFrom = ta.getColor(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_gradient_from, this.initColorFrom);
            this.initColorTo = ta.getColor(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_gradient_to, this.initColorTo);
            this.initStrokeColor = ta.getColor(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_gradient_to, 0);
            if (this.initStrokeColor != 0) {
                this.initColorFrom = this.initStrokeColor;
                this.initColorTo = this.initStrokeColor;
            }
            int tmpVar = ta.getInteger(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_lgsLinearLayout_style, Style.round.ordinal());
            this.initStyle = (tmpVar < 0 || tmpVar >= Style.values().length) ? Style.round : Style.values()[tmpVar];
            ta.recycle();
        }
        init();
    }

    private void init() {
        setStrokeWidth(this.initStrokeWidth);
        setStyle(this.initStyle);
        setRadius(this.initRadius);
        setGradient(new int[]{this.initColorFrom, this.initColorTo}, (float[]) null);
        setSeparatorSkipFocus(true);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        BOWLogger.i(TAG, ".onDraw");
        if (canvas != null) {
            super.onDraw(canvas);
            if (this.style != Style.blank) {
                drawGradientStroke(canvas);
            }
            BOWLogger.i(TAG, ".onDraw done");
        }
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        postInvalidate();
    }

    public void onViewAdded(View child) {
        BOWLogger.i(TAG, ".onViewAdded");
        super.onViewAdded(child);
        invalidateDrawParams();
    }

    public void onViewRemoved(View child) {
        BOWLogger.i(TAG, ".onViewRemoved");
        super.onViewRemoved(child);
        invalidateDrawParams();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        postInvalidate();
        return super.dispatchKeyEvent(event);
    }

    private boolean calculateFitRectF() {
        if (this.fitRectF == null) {
            this.fitRectF = new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            float d = this.strokeWidth / 2.0f;
            this.fitRectF.left += d;
            this.fitRectF.top += d;
            this.fitRectF.right -= d;
            this.fitRectF.bottom -= d;
            return true;
        } else if (this.fitRectF.width() == ((float) getWidth()) && this.fitRectF.height() == ((float) getHeight())) {
            return false;
        } else {
            this.fitRectF.set(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            float d2 = this.strokeWidth / 2.0f;
            this.fitRectF.left += d2;
            this.fitRectF.top += d2;
            this.fitRectF.right -= d2;
            this.fitRectF.bottom -= d2;
            return true;
        }
    }

    private void drawGradientStroke(Canvas canvas) {
        int y;
        int x;
        if (this.pathPaint == null) {
            this.pathPaint = new Paint();
            this.pathPaint.setAntiAlias(true);
            this.pathPaint.setStyle(Paint.Style.STROKE);
            this.pathPaint.setStrokeWidth(this.strokeWidth);
        }
        if (this.linePaint == null) {
            this.linePaint = new Paint();
            this.linePaint.setAntiAlias(true);
            this.linePaint.setStyle(Paint.Style.FILL);
            this.linePaint.setStrokeWidth(this.strokeWidth);
        }
        boolean changed = calculateFitRectF();
        if (this.gradientPath == null) {
            this.gradientPath = new Path();
            this.gradientPath.addRoundRect(this.fitRectF, this.corners, Path.Direction.CW);
        } else {
            this.gradientPath.reset();
            this.gradientPath.addRoundRect(this.fitRectF, this.corners, Path.Direction.CW);
        }
        if (this.linearGradientShader == null) {
            this.linearGradientShader = new LinearGradient(0.0f, 0.0f, (float) getWidth(), 0.0f, this.gradientColors, this.gradientPoints, Shader.TileMode.CLAMP);
        } else if (changed) {
            this.linearGradientShader = new LinearGradient(0.0f, 0.0f, (float) getWidth(), 0.0f, this.gradientColors, this.gradientPoints, Shader.TileMode.CLAMP);
        }
        if (!(this.linearGradientShader == null || this.gradientPath == null)) {
            this.pathPaint.setShader(this.linearGradientShader);
            canvas.drawPath(this.gradientPath, this.pathPaint);
            this.linePaint.setShader(this.linearGradientShader);
        }
        boolean isSeparatorSkipFocus = isSeparatorSkipFocus();
        if (getOrientation() == 0) {
            int i = 0;
            while (i < getChildCount() && i != getChildCount() - 1) {
                View child = getChildAt(i);
                if (!child.hasFocus() || !isSeparatorSkipFocus) {
                    if (i + 1 < getChildCount()) {
                        if (getChildAt(i + 1).hasFocus() && isSeparatorSkipFocus) {
                        }
                    }
                    if (child != null && (x = child.getRight()) < getWidth() && x >= 0) {
                        canvas.drawLine((float) x, 0.0f, (float) x, (float) getHeight(), this.linePaint);
                    }
                }
                i++;
            }
        } else if (getOrientation() == 1) {
            int i2 = 0;
            while (i2 < getChildCount() && i2 != getChildCount() - 1) {
                View child2 = getChildAt(i2);
                if (!child2.hasFocus() || !isSeparatorSkipFocus) {
                    if (i2 + 1 < getChildCount()) {
                        if (getChildAt(i2 + 1).hasFocus() && isSeparatorSkipFocus) {
                        }
                    }
                    if (child2 != null && (y = child2.getBottom()) < getHeight() && y >= 0) {
                        canvas.drawLine(0.0f, (float) y, (float) getWidth(), (float) y, this.linePaint);
                    }
                }
                i2++;
            }
        }
    }

    public void setGradient(int[] gradientColors2, float[] gradientPoints2) {
        invalidateDrawParams();
        if (!(gradientPoints2 == null || gradientColors2 == null || gradientColors2.length != gradientPoints2.length)) {
            this.gradientColors = gradientColors2;
            this.gradientPoints = gradientPoints2;
        }
        if (gradientPoints2 == null && gradientColors2 != null && gradientColors2.length >= 2) {
            this.gradientColors = gradientColors2;
            this.gradientPoints = null;
        }
        if (gradientPoints2 == null && gradientColors2 != null && gradientColors2.length == 1) {
            this.gradientColors = new int[]{gradientColors2[0], gradientColors2[0]};
            this.gradientPoints = null;
        }
    }

    public void setCorners(float[] corners2) {
        BOWLogger.i(TAG, ".setCorners " + corners2);
        if (corners2 != null && corners2.length == 8) {
            this.corners = corners2;
        }
        invalidateDrawParams();
    }

    public Style getStyle() {
        return this.style;
    }

    public void setStyle(Style style2) {
        BOWLogger.i(TAG, ".setStyle " + style2);
        if (style2 != null) {
            this.style = style2;
            if (style2 == Style.blank) {
                setCorners((float[]) null);
            } else if (style2 == Style.round) {
                setCorners(new float[]{this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f});
            } else if (style2 == Style.rect) {
                setCorners(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f});
            }
        }
    }

    public void setRadius(float radius2) {
        BOWLogger.i(TAG, ".setRadius " + radius2);
        this.radius = radius2;
        setStyle(this.style);
    }

    public float getRadius() {
        return this.radius;
    }

    public float getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth2) {
        this.strokeWidth = strokeWidth2;
        invalidateDrawParams();
    }

    private void invalidateDrawParams() {
        if (this.gradientPath != null) {
            this.gradientPath.reset();
            this.gradientPath = null;
        }
        if (this.linearGradientShader != null) {
            this.linearGradientShader = null;
        }
        postInvalidate();
    }

    public boolean isSeparatorSkipFocus() {
        if ((this.flag & 1) == 1) {
            return true;
        }
        return false;
    }

    public void setSeparatorSkipFocus(boolean enable) {
        if (enable) {
            this.flag |= 1;
        } else {
            this.flag &= -2;
        }
        invalidateDrawParams();
    }

    public boolean isInEditMode() {
        if (BOWConfig.DEBUG) {
            return true;
        }
        return super.isInEditMode();
    }
}
