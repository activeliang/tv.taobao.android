package com.tvtaobao.android.ui3.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.tvtaobao.android.ui3.R;
import java.util.concurrent.atomic.AtomicInteger;

public class TipView extends ConstraintLayout {
    private boolean isTriangleBg;
    private String majorText;
    private TextView majorTextView;
    private String minorText;
    private TextView minorTextView;
    private Rect padding;
    private TipBgDrawable tipBgDrawable;

    public TipView(Context context) {
        this(context, (AttributeSet) null);
    }

    public TipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (context != null) {
            if (this.majorTextView == null) {
                this.majorTextView = getTipView(context);
                this.majorTextView.setId(generateViewSelfId());
                this.majorTextView.setGravity(1);
            }
            if (this.minorTextView == null) {
                this.minorTextView = getTipView(context);
            }
            this.tipBgDrawable = new TipBgDrawable();
            this.padding = this.tipBgDrawable.getPadding();
            if (attrs != null) {
                TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ui3wares_TipView);
                this.majorText = ta.getString(R.styleable.ui3wares_TipView_ui3wares_majorText);
                int majorTextSize = ta.getDimensionPixelSize(R.styleable.ui3wares_TipView_ui3wares_majorTextSize, getResources().getDimensionPixelOffset(R.dimen.values_sp_18));
                int majorTextColor = ta.getColor(R.styleable.ui3wares_TipView_ui3wares_majorTextColor, getResources().getColor(17170443));
                this.minorText = ta.getString(R.styleable.ui3wares_TipView_ui3wares_minorText);
                int minorTextSize = ta.getDimensionPixelSize(R.styleable.ui3wares_TipView_ui3wares_minorTextSize, getResources().getDimensionPixelOffset(R.dimen.values_sp_18));
                int minorColor = ta.getColor(R.styleable.ui3wares_TipView_ui3wares_minorTextColor, getResources().getColor(R.color.values_color_ff4400));
                this.isTriangleBg = ta.getBoolean(R.styleable.ui3wares_TipView_ui3wares_isTriangleBg, true);
                setIsTriangleBg(this.isTriangleBg);
                setMajorText(this.majorText);
                setMajorTextColor(majorTextColor);
                setMajorTextSize((float) majorTextSize);
                this.majorTextView.setIncludeFontPadding(false);
                setMinorText(this.minorText);
                setMinorTextSize((float) minorTextSize);
                setMinorTextColor(minorColor);
                this.minorTextView.setIncludeFontPadding(false);
                if (!TextUtils.isEmpty(this.majorText) || !TextUtils.isEmpty(this.minorText)) {
                    setVisibility(0);
                } else {
                    setVisibility(8);
                }
                if (ta != null) {
                    ta.recycle();
                }
            }
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-2, -2);
            layoutParams.topToTop = 0;
            layoutParams.leftToLeft = 0;
            addView(this.majorTextView, layoutParams);
            ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(-2, -2);
            lp.topToTop = 0;
            lp.rightToRight = 0;
            lp.leftToRight = this.majorTextView.getId();
            addView(this.minorTextView, lp);
        }
    }

    public void setMajorText(String majorText2) {
        if (this.majorTextView != null) {
            this.majorText = majorText2;
            if (!TextUtils.isEmpty(majorText2)) {
                this.majorTextView.setText(majorText2);
                setVisibility(0);
                this.majorTextView.setVisibility(0);
            } else if (!TextUtils.isEmpty(this.majorText) || !TextUtils.isEmpty(this.minorText)) {
                setVisibility(0);
                this.majorTextView.setVisibility(8);
            } else {
                setVisibility(8);
            }
        }
    }

    public void setMajorTextSize(float size) {
        if (this.majorTextView != null) {
            this.majorTextView.setTextSize(0, size);
        }
    }

    public void setMajorTextColor(int color) {
        if (this.majorTextView != null) {
            this.majorTextView.setTextColor(color);
        }
    }

    public void setMinorText(String minorText2) {
        if (this.minorTextView != null) {
            this.minorText = minorText2;
            if (!TextUtils.isEmpty(minorText2)) {
                this.minorTextView.setText(" " + minorText2);
                setVisibility(0);
                this.minorTextView.setVisibility(0);
            } else if (!TextUtils.isEmpty(this.majorText) || !TextUtils.isEmpty(this.minorText)) {
                setVisibility(0);
                this.minorTextView.setVisibility(8);
            } else {
                setVisibility(8);
            }
        }
    }

    public void setMinorTextSize(float size) {
        if (this.minorTextView != null) {
            this.minorTextView.setTextSize(0, size);
        }
    }

    public void setMinorTextColor(int color) {
        if (this.minorTextView != null) {
            this.minorTextView.setTextColor(color);
        }
    }

    public void setIsTriangleBg(boolean isTriangleBg2) {
        this.isTriangleBg = isTriangleBg2;
        if (!isTriangleBg2) {
            setPadding(getResources().getDimensionPixelSize(R.dimen.values_dp_20), getResources().getDimensionPixelSize(R.dimen.values_dp_9_4), getResources().getDimensionPixelSize(R.dimen.values_dp_20), getResources().getDimensionPixelSize(R.dimen.values_dp_9_4));
            setBackgroundResource(R.drawable.ui3wares_tip_view_bg1);
            this.majorTextView.setMaxLines(2);
            this.majorTextView.setLineSpacing((float) getResources().getDimensionPixelOffset(R.dimen.values_sp_24), 0.0f);
            this.majorTextView.setEllipsize(TextUtils.TruncateAt.END);
            return;
        }
        setPadding(this.padding.left, this.padding.top, this.padding.right, this.padding.bottom);
        setBackgroundDrawable(this.tipBgDrawable);
        this.majorTextView.setMaxLines(1);
        this.majorTextView.setEllipsize(TextUtils.TruncateAt.END);
    }

    private TextView getTipView(Context context) {
        TextView tmp = new TextView(context);
        tmp.setTextSize(0, context.getResources().getDimension(R.dimen.values_dp_18));
        tmp.setTextColor(-1);
        tmp.setGravity(16);
        tmp.setMaxLines(2);
        tmp.setEllipsize(TextUtils.TruncateAt.END);
        return tmp;
    }

    class TipBgDrawable extends Drawable {
        int paddingLeft = TipView.this.getResources().getDimensionPixelSize(R.dimen.values_dp_15);
        int paddingRight = TipView.this.getResources().getDimensionPixelSize(R.dimen.values_dp_15);
        Paint paint = new Paint();
        int radius = TipView.this.getResources().getDimensionPixelSize(R.dimen.values_dp_5);
        RectF tmp = new RectF();
        int triangleHeight = TipView.this.getResources().getDimensionPixelSize(R.dimen.values_dp_7);

        public Rect getPadding() {
            Rect rtn = new Rect();
            rtn.set(this.radius + this.paddingLeft, TipView.this.getResources().getDimensionPixelSize(R.dimen.values_dp_11), this.radius + this.paddingRight + this.triangleHeight, TipView.this.getResources().getDimensionPixelSize(R.dimen.values_dp_11));
            return rtn;
        }

        public TipBgDrawable() {
            this.paint.setAntiAlias(true);
            this.paint.setStyle(Paint.Style.FILL);
            this.paint.setColor(Color.parseColor("#cc000000"));
        }

        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            if (bounds != null) {
                Path path = new Path();
                path.moveTo(0.0f, (float) this.radius);
                this.tmp.set(0.0f, 0.0f, (float) (this.radius * 2), (float) (this.radius * 2));
                path.arcTo(this.tmp, 180.0f, 90.0f);
                path.lineTo((float) ((bounds.right - this.triangleHeight) - this.radius), 0.0f);
                this.tmp.set((float) (((bounds.right - this.triangleHeight) - this.radius) - this.radius), 0.0f, (float) (bounds.right - this.triangleHeight), (float) (this.radius * 2));
                path.arcTo(this.tmp, 270.0f, 90.0f);
                path.lineTo((float) (bounds.right - this.triangleHeight), (float) (bounds.centerY() - this.triangleHeight));
                path.lineTo((float) bounds.right, (float) bounds.centerY());
                path.lineTo((float) (bounds.right - this.triangleHeight), (float) (bounds.centerY() + this.triangleHeight));
                path.lineTo((float) (bounds.right - this.triangleHeight), (float) (bounds.bottom - this.radius));
                this.tmp.set((float) (((bounds.right - this.triangleHeight) - this.radius) - this.radius), (float) (bounds.bottom - (this.radius * 2)), (float) (bounds.right - this.triangleHeight), (float) bounds.bottom);
                path.arcTo(this.tmp, 0.0f, 90.0f);
                path.lineTo((float) (bounds.left - this.radius), (float) bounds.bottom);
                this.tmp.set(0.0f, (float) (bounds.bottom - (this.radius * 2)), (float) (this.radius * 2), (float) bounds.bottom);
                path.arcTo(this.tmp, 90.0f, 90.0f);
                path.close();
                canvas.drawPath(path, this.paint);
            }
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        public int getOpacity() {
            return -3;
        }
    }

    public static int generateViewSelfId() {
        int result;
        int newValue;
        if (Build.VERSION.SDK_INT >= 17) {
            return generateViewId();
        }
        AtomicInteger sNextGeneratedId = new AtomicInteger(1);
        do {
            result = sNextGeneratedId.get();
            newValue = result + 1;
            if (newValue > 16777215) {
                newValue = 1;
            }
        } while (!sNextGeneratedId.compareAndSet(result, newValue));
        return result;
    }
}
