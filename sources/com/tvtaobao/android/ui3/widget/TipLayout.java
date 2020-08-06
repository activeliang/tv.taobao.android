package com.tvtaobao.android.ui3.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.tvtaobao.android.ui3.R;
import com.tvtaobao.android.ui3.UI3Util;
import java.lang.ref.WeakReference;

public class TipLayout extends FrameLayout {
    private WeakReference<View> anchor;
    private int bgColor;
    private float[] corners;
    private WeakReference<FrameLayout> decor;
    private Rect margins;
    int[] oldWH;
    private FrameLayout outContentArea;
    Paint pathPaint;
    private TipPos tipPos;
    private float[] triangle;

    public interface DismissListener {
        void onDismiss();
    }

    public enum TipPos {
        left,
        top,
        right,
        bottom,
        center
    }

    private TipLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    private TipLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private TipLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.bgColor = Color.parseColor("#cc000000");
        this.corners = null;
        this.triangle = null;
        this.tipPos = TipPos.left;
        this.oldWH = new int[]{0, 0};
        init();
    }

    public static TipLayout obtain(View anchor2) {
        if (anchor2 != null) {
            try {
                TipLayout tipLayout = new TipLayout(anchor2.getContext());
                tipLayout.setVisibility(4);
                tipLayout.anchor = new WeakReference<>(anchor2);
                FrameLayout decor2 = UI3Util.findDecorView(anchor2);
                if (decor2 == null) {
                    return tipLayout;
                }
                tipLayout.decor = new WeakReference<>(decor2);
                decor2.addView(tipLayout, new FrameLayout.LayoutParams(-2, -2));
                return tipLayout;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void init() {
        float dp4 = getResources().getDimension(R.dimen.values_dp_4);
        setRadius(dp4);
        setTriangle(new float[]{dp4, dp4, dp4});
        setWillNotDraw(false);
        setFocusable(false);
        setDescendantFocusability(393216);
        this.outContentArea = new FrameLayout(getContext());
        this.outContentArea.setPadding((int) dp4, (int) dp4, (int) dp4, (int) dp4);
        addView(this.outContentArea, new FrameLayout.LayoutParams(-2, -2));
    }

    private Path calculatePath() {
        Rect bounds = new Rect(0, 0, getWidth(), getHeight());
        RectF tmp = new RectF();
        if (this.tipPos == TipPos.left) {
            Path path = new Path();
            path.moveTo(0.0f, this.corners[1] / 2.0f);
            tmp.set(0.0f, 0.0f, this.corners[0], this.corners[1]);
            path.arcTo(tmp, 180.0f, 90.0f);
            path.lineTo((((float) bounds.right) - this.triangle[3]) - (this.corners[2] / 2.0f), 0.0f);
            tmp.set((((float) bounds.right) - this.triangle[3]) - this.corners[2], 0.0f, ((float) bounds.right) - this.triangle[3], this.corners[3]);
            path.arcTo(tmp, 270.0f, 90.0f);
            path.lineTo(((float) bounds.right) - this.triangle[3], ((float) bounds.centerY()) - (this.triangle[0] / 2.0f));
            path.lineTo((float) bounds.right, (((float) bounds.centerY()) - (this.triangle[0] / 2.0f)) + this.triangle[4]);
            path.lineTo(((float) bounds.right) - this.triangle[3], ((float) bounds.centerY()) + (this.triangle[0] / 2.0f));
            path.lineTo(((float) bounds.right) - this.triangle[3], ((float) bounds.bottom) - (this.corners[5] / 2.0f));
            tmp.set((((float) bounds.right) - this.triangle[3]) - this.corners[4], ((float) bounds.bottom) - this.corners[5], ((float) bounds.right) - this.triangle[3], (float) bounds.bottom);
            path.arcTo(tmp, 0.0f, 90.0f);
            path.lineTo(this.corners[6] / 2.0f, (float) bounds.bottom);
            tmp.set(0.0f, ((float) bounds.bottom) - this.corners[7], this.corners[6], (float) bounds.bottom);
            path.arcTo(tmp, 90.0f, 90.0f);
            path.close();
            return path;
        } else if (this.tipPos == TipPos.right) {
            Path path2 = new Path();
            path2.moveTo(this.triangle[3], this.corners[1] / 2.0f);
            tmp.set(this.triangle[3], 0.0f, this.triangle[3] + this.corners[0], this.corners[1]);
            path2.arcTo(tmp, 180.0f, 90.0f);
            path2.lineTo(((float) bounds.right) - (this.corners[2] / 2.0f), 0.0f);
            tmp.set(((float) bounds.right) - this.corners[2], 0.0f, (float) bounds.right, this.corners[3]);
            path2.arcTo(tmp, 270.0f, 90.0f);
            path2.lineTo((float) bounds.right, ((float) bounds.bottom) - (this.triangle[0] / 2.0f));
            tmp.set(((float) bounds.right) - this.corners[4], ((float) bounds.bottom) - this.corners[5], (float) bounds.right, (float) bounds.bottom);
            path2.arcTo(tmp, 0.0f, 90.0f);
            path2.lineTo(this.triangle[3] + (this.corners[6] / 2.0f), (float) bounds.bottom);
            tmp.set(this.triangle[3], ((float) bounds.bottom) - this.corners[7], this.triangle[3] + this.corners[6], (float) bounds.bottom);
            path2.arcTo(tmp, 90.0f, 90.0f);
            path2.lineTo(this.triangle[3], ((float) bounds.centerY()) + (this.triangle[0] / 2.0f));
            path2.lineTo(0.0f, (((float) bounds.centerY()) + (this.triangle[0] / 2.0f)) - this.triangle[4]);
            path2.lineTo(this.triangle[3], ((float) bounds.centerY()) - (this.triangle[0] / 2.0f));
            path2.close();
            return path2;
        } else if (this.tipPos == TipPos.top) {
            Path path3 = new Path();
            path3.moveTo(0.0f, this.corners[1] / 2.0f);
            tmp.set(0.0f, 0.0f, this.corners[0], this.corners[1]);
            path3.arcTo(tmp, 180.0f, 90.0f);
            path3.lineTo(((float) bounds.right) - (this.corners[2] / 2.0f), 0.0f);
            tmp.set(((float) bounds.right) - this.corners[2], 0.0f, (float) bounds.right, this.corners[3]);
            path3.arcTo(tmp, 270.0f, 90.0f);
            path3.lineTo((float) bounds.right, (((float) bounds.bottom) - (this.corners[5] / 2.0f)) - this.triangle[3]);
            tmp.set(((float) bounds.right) - this.corners[4], (((float) bounds.bottom) - this.corners[5]) - this.triangle[3], (float) bounds.right, ((float) bounds.bottom) - this.triangle[3]);
            path3.arcTo(tmp, 0.0f, 90.0f);
            path3.lineTo(((float) bounds.centerX()) + (this.triangle[0] / 2.0f), ((float) bounds.bottom) - this.triangle[3]);
            path3.lineTo((((float) bounds.centerX()) + (this.triangle[0] / 2.0f)) - this.triangle[4], (float) bounds.bottom);
            path3.lineTo(((float) bounds.centerX()) - (this.triangle[0] / 2.0f), ((float) bounds.bottom) - this.triangle[3]);
            path3.lineTo(this.corners[6] / 2.0f, ((float) bounds.bottom) - this.triangle[3]);
            tmp.set(0.0f, (((float) bounds.bottom) - this.corners[7]) - this.triangle[3], this.corners[6], ((float) bounds.bottom) - this.triangle[3]);
            path3.arcTo(tmp, 90.0f, 90.0f);
            path3.close();
            return path3;
        } else if (this.tipPos == TipPos.bottom) {
            Path path4 = new Path();
            path4.moveTo(0.0f, this.triangle[3] + (this.corners[1] / 2.0f));
            tmp.set(0.0f, this.triangle[3], this.corners[0], this.triangle[3] + this.corners[1]);
            path4.arcTo(tmp, 180.0f, 90.0f);
            path4.lineTo(((float) bounds.centerX()) - (this.triangle[0] / 2.0f), this.triangle[3]);
            path4.lineTo((((float) bounds.centerX()) - (this.triangle[0] / 2.0f)) + this.triangle[4], 0.0f);
            path4.lineTo(((float) bounds.centerX()) + (this.triangle[0] / 2.0f), this.triangle[3]);
            path4.lineTo(((float) bounds.right) - (this.corners[2] / 2.0f), this.triangle[3]);
            tmp.set(((float) bounds.right) - this.corners[2], this.triangle[3], (float) bounds.right, this.triangle[3] + this.corners[3]);
            path4.arcTo(tmp, 270.0f, 90.0f);
            path4.lineTo((float) bounds.right, ((float) bounds.bottom) - (this.triangle[0] / 2.0f));
            tmp.set(((float) bounds.right) - this.corners[4], ((float) bounds.bottom) - this.corners[5], (float) bounds.right, (float) bounds.bottom);
            path4.arcTo(tmp, 0.0f, 90.0f);
            path4.lineTo(this.corners[6] / 2.0f, (float) bounds.bottom);
            tmp.set(0.0f, ((float) bounds.bottom) - this.corners[7], this.corners[6], (float) bounds.bottom);
            path4.arcTo(tmp, 90.0f, 90.0f);
            path4.close();
            return path4;
        } else if (this.tipPos != TipPos.center) {
            return null;
        } else {
            Path path5 = new Path();
            path5.moveTo(0.0f, this.corners[1] / 2.0f);
            tmp.set(0.0f, 0.0f, this.corners[0], this.corners[1]);
            path5.arcTo(tmp, 180.0f, 90.0f);
            path5.lineTo(((float) bounds.right) - (this.corners[2] / 2.0f), 0.0f);
            tmp.set(((float) bounds.right) - this.corners[2], 0.0f, (float) bounds.right, this.corners[3]);
            path5.arcTo(tmp, 270.0f, 90.0f);
            path5.lineTo((float) bounds.right, ((float) bounds.bottom) - (this.corners[5] / 2.0f));
            tmp.set(((float) bounds.right) - this.corners[4], ((float) bounds.bottom) - this.corners[5], (float) bounds.right, (float) bounds.bottom);
            path5.arcTo(tmp, 0.0f, 90.0f);
            path5.lineTo(this.corners[6] / 2.0f, (float) bounds.bottom);
            tmp.set(0.0f, ((float) bounds.bottom) - this.corners[7], this.corners[6], (float) bounds.bottom);
            path5.arcTo(tmp, 90.0f, 90.0f);
            path5.close();
            return path5;
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.pathPaint == null) {
            this.pathPaint = new Paint();
            this.pathPaint.setAntiAlias(true);
            this.pathPaint.setStyle(Paint.Style.FILL);
        }
        Path path = calculatePath();
        if (canvas != null && path != null) {
            this.pathPaint.setColor(this.bgColor);
            canvas.drawPath(path, this.pathPaint);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.oldWH[0] != getMeasuredWidth() || this.oldWH[1] != getMeasuredHeight()) {
            this.oldWH[0] = getMeasuredWidth();
            this.oldWH[1] = getMeasuredHeight();
            post(new Runnable() {
                public void run() {
                    TipLayout.this.resetMyLayoutParams();
                }
            });
        }
    }

    private void resetContentAreaLayoutParams() {
        try {
            if (this.tipPos == TipPos.left) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) this.outContentArea.getLayoutParams();
                lp.rightMargin = (int) (((double) this.triangle[3]) + 0.5d);
                lp.leftMargin = 0;
                lp.topMargin = 0;
                lp.bottomMargin = 0;
                this.outContentArea.setLayoutParams(lp);
            } else if (this.tipPos == TipPos.right) {
                FrameLayout.LayoutParams lp2 = (FrameLayout.LayoutParams) this.outContentArea.getLayoutParams();
                lp2.rightMargin = 0;
                lp2.leftMargin = (int) (((double) this.triangle[3]) + 0.5d);
                lp2.topMargin = 0;
                lp2.bottomMargin = 0;
                this.outContentArea.setLayoutParams(lp2);
            } else if (this.tipPos == TipPos.top) {
                FrameLayout.LayoutParams lp3 = (FrameLayout.LayoutParams) this.outContentArea.getLayoutParams();
                lp3.rightMargin = 0;
                lp3.leftMargin = 0;
                lp3.topMargin = 0;
                lp3.bottomMargin = (int) (((double) this.triangle[3]) + 0.5d);
                this.outContentArea.setLayoutParams(lp3);
            } else if (this.tipPos == TipPos.bottom) {
                FrameLayout.LayoutParams lp4 = (FrameLayout.LayoutParams) this.outContentArea.getLayoutParams();
                lp4.rightMargin = 0;
                lp4.leftMargin = 0;
                lp4.topMargin = (int) (((double) this.triangle[3]) + 0.5d);
                lp4.bottomMargin = 0;
                this.outContentArea.setLayoutParams(lp4);
            } else if (this.tipPos == TipPos.center) {
                FrameLayout.LayoutParams lp5 = (FrameLayout.LayoutParams) this.outContentArea.getLayoutParams();
                lp5.rightMargin = 0;
                lp5.leftMargin = 0;
                lp5.topMargin = 0;
                lp5.bottomMargin = 0;
                this.outContentArea.setLayoutParams(lp5);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void resetMyLayoutParams() {
        try {
            Rect anchorRect = new Rect();
            ((View) this.anchor.get()).getDrawingRect(anchorRect);
            ((FrameLayout) this.decor.get()).offsetDescendantRectToMyCoords((View) this.anchor.get(), anchorRect);
            int w = this.oldWH[0];
            int h = this.oldWH[1];
            if (w == 0 || h == 0) {
                measure(View.MeasureSpec.makeMeasureSpec(((FrameLayout) this.decor.get()).getWidth(), 0), View.MeasureSpec.makeMeasureSpec(((FrameLayout) this.decor.get()).getWidth(), 0));
                w = getMeasuredWidth();
                h = getMeasuredHeight();
            }
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
            if (this.tipPos == TipPos.left) {
                lp.gravity = 53;
                lp.rightMargin = ((FrameLayout) this.decor.get()).getWidth() - anchorRect.left;
                if (this.margins != null) {
                    lp.rightMargin += this.margins.right;
                }
                lp.leftMargin = 0;
                lp.topMargin = anchorRect.centerY() - (h / 2);
                lp.bottomMargin = 0;
                setLayoutParams(lp);
            } else if (this.tipPos == TipPos.right) {
                lp.gravity = 51;
                lp.rightMargin = 0;
                lp.leftMargin = anchorRect.right;
                if (this.margins != null) {
                    lp.leftMargin += this.margins.left;
                }
                lp.topMargin = anchorRect.centerY() - (h / 2);
                lp.bottomMargin = 0;
                setLayoutParams(lp);
            } else if (this.tipPos == TipPos.top) {
                lp.gravity = 83;
                lp.rightMargin = 0;
                lp.leftMargin = anchorRect.centerX() - (w / 2);
                lp.topMargin = 0;
                lp.bottomMargin = ((FrameLayout) this.decor.get()).getHeight() - anchorRect.top;
                if (this.margins != null) {
                    lp.bottomMargin += this.margins.bottom;
                }
                setLayoutParams(lp);
            } else if (this.tipPos == TipPos.bottom) {
                lp.gravity = 51;
                lp.rightMargin = 0;
                lp.leftMargin = anchorRect.centerX() - (w / 2);
                lp.topMargin = anchorRect.bottom;
                if (this.margins != null) {
                    lp.topMargin += this.margins.top;
                }
                lp.bottomMargin = 0;
                setLayoutParams(lp);
            } else if (this.tipPos == TipPos.center) {
                lp.gravity = 17;
                lp.rightMargin = 0;
                lp.leftMargin = 0;
                lp.topMargin = 0;
                lp.bottomMargin = 0;
                setLayoutParams(lp);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private TextView genTextView(Context context) {
        TextView tmp = new TextView(context);
        tmp.setTextSize(0, context.getResources().getDimension(R.dimen.values_dp_18));
        tmp.setTextColor(-1);
        tmp.setGravity(16);
        tmp.setIncludeFontPadding(false);
        return tmp;
    }

    private void syncState() {
        postInvalidate();
    }

    public void release() {
        if (getParent() != null && (getParent() instanceof ViewGroup)) {
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    public TipLayout setRadius(float radius) {
        return setCorners(new float[]{radius * 2.0f, radius * 2.0f, radius * 2.0f, radius * 2.0f, radius * 2.0f, radius * 2.0f, radius * 2.0f, radius * 2.0f});
    }

    public TipLayout setCorners(float[] corners2) {
        if (corners2 != null && corners2.length == 8) {
            this.corners = corners2;
        }
        syncState();
        return this;
    }

    public TipLayout setTriangle(float[] triangle2) {
        if (triangle2 != null && triangle2.length == 3 && triangle2[0] > 0.0f && triangle2[1] > 0.0f && triangle2[2] > 0.0f && triangle2[0] + triangle2[1] > triangle2[2] && triangle2[2] + triangle2[1] > triangle2[0] && triangle2[2] + triangle2[0] > triangle2[1]) {
            this.triangle = new float[]{triangle2[0], triangle2[1], triangle2[2], 0.0f, 0.0f, 0.0f};
            double a = Math.pow((double) triangle2[0], 2.0d);
            double y = ((a + Math.pow((double) triangle2[2], 2.0d)) - Math.pow((double) triangle2[1], 2.0d)) / ((double) (2.0f * triangle2[0]));
            double x = ((double) triangle2[0]) - y;
            this.triangle[3] = (float) Math.sqrt(Math.pow((double) triangle2[2], 2.0d) - Math.pow(y, 2.0d));
            this.triangle[4] = (float) x;
            this.triangle[5] = (float) y;
        }
        syncState();
        return this;
    }

    public TipLayout setBgColor(int color) {
        this.bgColor = color;
        syncState();
        return this;
    }

    public Rect getMargins() {
        return this.margins;
    }

    public TipLayout setMargins(Rect margins2) {
        this.margins = margins2;
        syncState();
        return this;
    }

    public void tip(String msg, TipPos tp) {
        this.outContentArea.removeAllViews();
        TextView tv2 = genTextView(getContext());
        tv2.setText(msg);
        this.outContentArea.addView(tv2);
        if (!(tp == null || tp == this.tipPos)) {
            this.tipPos = tp;
        }
        if (!(this.anchor == null || this.anchor.get() == null || this.decor == null || this.decor.get() == null)) {
            resetContentAreaLayoutParams();
            resetMyLayoutParams();
        }
        syncState();
        setVisibility(0);
    }

    public void tipAndDismiss(String msg, TipPos tp, long dismissTime, final DismissListener listener) {
        tip(msg, tp);
        postDelayed(new Runnable() {
            public void run() {
                ValueAnimator animator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
                animator.setTarget(this);
                animator.setDuration(300);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        TipLayout.this.setAlpha(((Float) animation.getAnimatedValue()).floatValue());
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        TipLayout.this.release();
                        if (listener != null) {
                            listener.onDismiss();
                        }
                    }
                });
                animator.start();
            }
        }, dismissTime);
    }

    public void tipAndDismiss(String msg, TipPos tp) {
        tipAndDismiss(msg, tp, 2700, (DismissListener) null);
    }

    public void tipAndDismiss(String msg, TipPos tp, DismissListener listener) {
        tipAndDismiss(msg, tp, 2700, listener);
    }

    public void tip(View view, TipPos tp) {
        this.outContentArea.removeAllViews();
        this.outContentArea.addView(view);
        if (!(tp == null || tp == this.tipPos)) {
            this.tipPos = tp;
        }
        if (!(this.anchor == null || this.anchor.get() == null || this.decor == null || this.decor.get() == null)) {
            resetContentAreaLayoutParams();
            resetMyLayoutParams();
        }
        syncState();
        setVisibility(0);
    }

    public void tipAndDismiss(View view, TipPos tp, long dismissTime, final DismissListener listener) {
        tip(view, tp);
        postDelayed(new Runnable() {
            public void run() {
                ValueAnimator animator = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
                animator.setTarget(this);
                animator.setDuration(300);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        TipLayout.this.setAlpha(((Float) animation.getAnimatedValue()).floatValue());
                    }
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        TipLayout.this.release();
                        if (listener != null) {
                            listener.onDismiss();
                        }
                    }
                });
                animator.start();
            }
        }, dismissTime);
    }

    public void tipAndDismiss(View view, TipPos tp) {
        tipAndDismiss(view, tp, 2700, (DismissListener) null);
    }

    public void tipAndDismiss(View view, TipPos tp, DismissListener listener) {
        tipAndDismiss(view, tp, 2700, listener);
    }
}
