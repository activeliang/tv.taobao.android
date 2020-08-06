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
import android.view.View;
import android.widget.FrameLayout;
import com.tvtaobao.android.buildorderwares.BOWConfig;
import com.tvtaobao.android.buildorderwares.BOWLogger;
import com.tvtaobao.android.buildorderwares.R;

public class LGBFrameLayout extends FrameLayout {
    private static final String TAG = LGBFrameLayout.class.getSimpleName();
    private float[] corners;
    private RectF fitRectF;
    private int[] gradientColors;
    private Paint gradientPaint;
    private Path gradientPath;
    private float[] gradientPoints;
    private int initColorFrom;
    private int initColorTo;
    private int initInvalidColor;
    private float initRadius;
    private Style initStyle;
    private int invalidColor;
    private Paint invalidPaint;
    private Path invalidPath;
    private LinearGradient linearGradientShader;
    float radius;
    private Style style;

    public enum Style {
        blank,
        invalid,
        single,
        vGroupBgn,
        vGroupMid,
        vGroupEnd,
        hGroupBgn,
        hGroupMid,
        hGroupEnd
    }

    public LGBFrameLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public LGBFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LGBFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.corners = null;
        this.gradientColors = null;
        this.gradientPoints = null;
        setWillNotDraw(false);
        this.initInvalidColor = Color.parseColor("#4a5059");
        this.initColorFrom = Color.parseColor("#ff9121");
        this.initColorTo = Color.parseColor("#ff6621");
        this.initRadius = context.getResources().getDimension(R.dimen.values_dp_3);
        this.initStyle = Style.single;
        if (!(context == null || attrs == null)) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.buildorderwares_styled_a);
            this.initInvalidColor = ta.getColor(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_invalid_color, this.initInvalidColor);
            this.initColorFrom = ta.getColor(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_gradient_from, this.initColorFrom);
            this.initColorTo = ta.getColor(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_gradient_from, this.initColorTo);
            this.initRadius = ta.getDimension(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_radius, this.initRadius);
            int tmpVar = ta.getInteger(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_lgbFrameLayout_style, Style.single.ordinal());
            this.initStyle = (tmpVar < 0 || tmpVar >= Style.values().length) ? Style.single : Style.values()[tmpVar];
            ta.recycle();
        }
        init();
    }

    private void init() {
        setInvalidColor(this.initInvalidColor);
        setStyle(this.initStyle);
        setRadius(this.initRadius);
        setGradient(new int[]{this.initColorFrom, this.initColorTo}, (float[]) null);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        BOWLogger.i(TAG, ".onDraw");
        if (canvas != null) {
            super.onDraw(canvas);
            if (this.style == Style.invalid) {
                drawInvalid(canvas);
            } else if (hasFocus()) {
                drawGradient(canvas);
            }
            BOWLogger.i(TAG, ".onDraw done");
        }
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        postInvalidate();
    }

    private boolean calculateFitRectF() {
        if (this.fitRectF == null) {
            this.fitRectF = new RectF(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            return true;
        } else if (this.fitRectF.width() == ((float) getWidth()) && this.fitRectF.height() == ((float) getHeight())) {
            return false;
        } else {
            this.fitRectF.set(0.0f, 0.0f, (float) getWidth(), (float) getHeight());
            return true;
        }
    }

    private void drawInvalid(Canvas canvas) {
        if (this.invalidPaint == null) {
            this.invalidPaint = new Paint();
            this.invalidPaint.setAntiAlias(true);
            this.invalidPaint.setStyle(Paint.Style.FILL);
        }
        boolean calculateFitRectF = calculateFitRectF();
        if (this.invalidPath == null) {
            this.invalidPath = new Path();
            this.invalidPath.addRoundRect(this.fitRectF, this.corners, Path.Direction.CW);
        } else {
            this.invalidPath.reset();
            this.invalidPath.addRoundRect(this.fitRectF, this.corners, Path.Direction.CW);
        }
        this.invalidPaint.setColor(this.invalidColor);
        if (this.invalidPaint != null && this.invalidPath != null) {
            canvas.drawPath(this.invalidPath, this.invalidPaint);
        }
    }

    private void drawGradient(Canvas canvas) {
        if (this.gradientPaint == null) {
            this.gradientPaint = new Paint();
            this.gradientPaint.setAntiAlias(true);
            this.gradientPaint.setStyle(Paint.Style.FILL);
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
        if (this.linearGradientShader != null && this.gradientPath != null) {
            this.gradientPaint.setShader(this.linearGradientShader);
            canvas.drawPath(this.gradientPath, this.gradientPaint);
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
        if (corners2 != null && corners2.length == 8) {
            this.corners = corners2;
        }
        invalidateDrawParams();
    }

    public void setStyle(Style style2) {
        if (style2 != null) {
            this.style = style2;
            if (style2 == Style.invalid) {
                setCorners(new float[]{this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f});
            } else if (style2 == Style.blank) {
                setCorners((float[]) null);
            } else if (style2 == Style.single) {
                setCorners(new float[]{this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f});
            } else if (style2 == Style.vGroupBgn) {
                setCorners(new float[]{this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, 0.0f, 0.0f, 0.0f, 0.0f});
            } else if (style2 == Style.vGroupMid) {
                setCorners(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f});
            } else if (style2 == Style.vGroupEnd) {
                setCorners(new float[]{0.0f, 0.0f, 0.0f, 0.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f});
            } else if (style2 == Style.hGroupBgn) {
                setCorners(new float[]{this.radius * 2.0f, this.radius * 2.0f, 0.0f, 0.0f, 0.0f, 0.0f, this.radius * 2.0f, this.radius * 2.0f});
            } else if (style2 == Style.hGroupMid) {
                setCorners(new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f});
            } else if (style2 == Style.hGroupEnd) {
                setCorners(new float[]{0.0f, 0.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, this.radius * 2.0f, 0.0f, 0.0f});
            }
        }
    }

    public Style getStyle() {
        return this.style;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius2) {
        this.radius = radius2;
        setStyle(this.style);
    }

    public int getInvalidColor() {
        return this.invalidColor;
    }

    public void setInvalidColor(int invalidColor2) {
        this.invalidColor = invalidColor2;
        postInvalidate();
    }

    /* access modifiers changed from: protected */
    public void invalidateDrawParams() {
        if (this.gradientPath != null) {
            this.gradientPath.reset();
            this.gradientPath = null;
        }
        if (this.linearGradientShader != null) {
            this.linearGradientShader = null;
        }
        postInvalidate();
    }

    public boolean isInEditMode() {
        if (BOWConfig.DEBUG) {
            return true;
        }
        return super.isInEditMode();
    }
}
