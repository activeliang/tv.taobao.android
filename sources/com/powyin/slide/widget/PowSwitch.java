package com.powyin.slide.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import com.powyin.slide.R;

public class PowSwitch extends View {
    private static final int INVALID_POINTER = -1;
    private Rect bacRect;
    private Rect iconFixedRect;
    private Rect iconRect;
    private int mActivePointerId;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private boolean mIsBeingDragged;
    private boolean mIsOpen;
    private float mLastMotionX;
    private OnToggleListener mOnToggleListener;
    private Drawable mSwitchBacOff;
    private Drawable mSwitchBacOn;
    private Drawable mSwitchIconOff;
    private Drawable mSwitchIconOn;
    private int mSwitchSuggestHei;
    private int mSwitchSuggestWei;
    private int mTouchSlop;
    /* access modifiers changed from: private */
    public int targetCurrent;
    private int targetMax;
    private ValueAnimator valueAnimator;

    public interface OnToggleListener {
        void onToggle(boolean z);
    }

    public PowSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mActivePointerId = -1;
        this.iconRect = new Rect();
        this.iconFixedRect = new Rect();
        this.bacRect = new Rect();
        float density = context.getResources().getDisplayMetrics().density;
        this.mSwitchSuggestWei = (int) (36.0f * density);
        this.mSwitchSuggestHei = (int) (16.0f * density);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PowSwitch);
        this.mSwitchBacOff = a.getDrawable(R.styleable.PowSwitch_pow_switch_bac_off);
        if (this.mSwitchBacOff == null) {
            this.mSwitchBacOff = context.getResources().getDrawable(R.drawable.powyin_switch_pow_switch_bac_off);
        }
        this.mSwitchBacOn = a.getDrawable(R.styleable.PowSwitch_pow_switch_bac_on);
        if (this.mSwitchBacOn == null) {
            this.mSwitchBacOn = context.getResources().getDrawable(R.drawable.powyin_switch_pow_switch_bac_on);
        }
        this.mSwitchIconOff = a.getDrawable(R.styleable.PowSwitch_pow_switch_icon_off);
        if (this.mSwitchIconOff == null) {
            this.mSwitchIconOff = context.getResources().getDrawable(R.drawable.powyin_switch_pow_switch_icon_off);
        }
        this.mSwitchIconOn = a.getDrawable(R.styleable.PowSwitch_pow_switch_icon_on);
        if (this.mSwitchIconOn == null) {
            this.mSwitchIconOn = context.getResources().getDrawable(R.drawable.powyin_switch_pow_switch_icon_on);
        }
        a.recycle();
        this.mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context)) / 2;
    }

    public PowSwitch(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PowSwitch(Context context) {
        this(context, (AttributeSet) null);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWei = Math.max(Math.max(Math.max(Math.max(0, this.mSwitchBacOff.getIntrinsicWidth()), this.mSwitchBacOn.getIntrinsicWidth()), this.mSwitchIconOff.getIntrinsicWidth()), this.mSwitchIconOn.getIntrinsicWidth()) + getPaddingLeft() + getPaddingRight();
        int maxHei = Math.max(Math.max(Math.max(Math.max(0, this.mSwitchBacOff.getIntrinsicHeight()), this.mSwitchBacOn.getIntrinsicHeight()), this.mSwitchIconOff.getIntrinsicHeight()), this.mSwitchIconOn.getIntrinsicHeight()) + getPaddingTop() + getPaddingBottom();
        int minWei = getPaddingTop() + getPaddingBottom();
        int minHei = getPaddingLeft() + getPaddingRight();
        if (maxWei <= minHei) {
            maxWei = this.mSwitchSuggestWei + minWei;
        }
        if (maxHei <= minHei) {
            maxHei = this.mSwitchSuggestHei + minHei;
        }
        setMeasuredDimension(resolveSize(maxWei, widthMeasureSpec), resolveSize(maxHei, heightMeasureSpec));
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initSize();
    }

    private void initSize() {
        this.bacRect.left = getPaddingLeft();
        this.bacRect.right = getWidth() - getPaddingRight();
        this.bacRect.top = getPaddingTop();
        this.bacRect.bottom = getHeight() - getPaddingBottom();
        Rect rect = this.iconRect;
        Rect rect2 = this.iconFixedRect;
        int paddingTop = getPaddingTop();
        rect2.top = paddingTop;
        rect.top = paddingTop;
        Rect rect3 = this.iconRect;
        Rect rect4 = this.iconFixedRect;
        int height = getHeight() - getPaddingBottom();
        rect4.bottom = height;
        rect3.bottom = height;
        int maxWid = Math.max(0, Math.max(this.mSwitchIconOff.getIntrinsicWidth(), this.mSwitchIconOn.getIntrinsicWidth()));
        int maxHei = Math.max(0, Math.max(this.mSwitchIconOff.getIntrinsicHeight(), this.mSwitchIconOn.getIntrinsicHeight()));
        this.iconFixedRect.left = getPaddingLeft();
        if (maxHei == 0 || maxWid == 0) {
            this.iconFixedRect.right = (this.iconFixedRect.left + this.iconFixedRect.bottom) - this.iconFixedRect.top;
        } else {
            this.iconFixedRect.right = this.iconFixedRect.left + ((int) (((1.0f * ((float) maxWid)) / ((float) maxHei)) * ((float) (this.iconFixedRect.bottom - this.iconFixedRect.top))));
        }
        this.targetMax = (((getWidth() - getPaddingRight()) - getPaddingLeft()) - this.iconFixedRect.right) + this.iconFixedRect.left;
        this.targetMax = Math.max(this.targetMax, 0);
        this.targetCurrent = 0;
        this.targetCurrent = 0;
        this.mSwitchBacOff.setBounds(this.bacRect);
        this.mSwitchBacOn.setBounds(this.bacRect);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            ensureTarget();
        }
    }

    public void setOnClickListener(@Nullable View.OnClickListener l) {
        throw new RuntimeException("not support");
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float radio;
        super.onDraw(canvas);
        if (this.targetMax > 0) {
            radio = (((float) this.targetCurrent) * 1.0f) / ((float) this.targetMax);
        } else {
            radio = 0.0f;
        }
        float radio2 = Math.max(0.0f, Math.min(1.0f, radio));
        this.mSwitchBacOff.setAlpha((int) ((1.0f - radio2) * 255.0f));
        this.mSwitchBacOff.draw(canvas);
        this.mSwitchBacOn.setAlpha((int) (255.0f * radio2));
        this.mSwitchBacOn.draw(canvas);
        this.iconRect.left = this.iconFixedRect.left + this.targetCurrent;
        this.iconRect.right = this.iconFixedRect.right + this.targetCurrent;
        this.mSwitchIconOff.setAlpha((int) ((1.0f - radio2) * 255.0f));
        this.mSwitchIconOff.setBounds(this.iconRect);
        this.mSwitchIconOff.draw(canvas);
        this.mSwitchIconOn.setAlpha((int) (255.0f * radio2));
        this.mSwitchIconOn.setBounds(this.iconRect);
        this.mSwitchIconOn.draw(canvas);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int activePointerIndex;
        int newPointerIndex = 0;
        switch (ev.getAction() & 255) {
            case 0:
                float x = ev.getX();
                this.mInitialMotionX = x;
                this.mLastMotionX = x;
                this.mInitialMotionY = ev.getY();
                this.mActivePointerId = ev.getPointerId(0);
                break;
            case 1:
                if (!this.mIsBeingDragged && Math.abs(ev.getY() - this.mInitialMotionY) <= ((float) this.mTouchSlop)) {
                    if (!this.mIsOpen || this.mInitialMotionX >= ((float) (getWidth() / 2))) {
                        if (!this.mIsOpen && this.mInitialMotionX > ((float) (getWidth() / 2))) {
                            this.mIsOpen = true;
                            if (this.mOnToggleListener != null) {
                                this.mOnToggleListener.onToggle(true);
                            }
                            ensureTarget();
                            break;
                        }
                    } else {
                        this.mIsOpen = false;
                        if (this.mOnToggleListener != null) {
                            this.mOnToggleListener.onToggle(false);
                        }
                        ensureTarget();
                        break;
                    }
                }
                break;
            case 2:
                if (this.mIsBeingDragged) {
                    if (this.mActivePointerId != -1 && (activePointerIndex = ev.findPointerIndex(this.mActivePointerId)) >= 0) {
                        offsetSwitch(ev.getX(activePointerIndex));
                        break;
                    } else {
                        return false;
                    }
                } else {
                    int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
                    if (pointerIndex >= 0) {
                        float x2 = ev.getX(pointerIndex);
                        if (Math.abs(x2 - this.mLastMotionX) > ((float) this.mTouchSlop)) {
                            this.mIsBeingDragged = true;
                            this.mLastMotionX = x2 - this.mInitialMotionX > 0.0f ? this.mInitialMotionX + ((float) this.mTouchSlop) : this.mInitialMotionX - ((float) this.mTouchSlop);
                            ViewParent parent = getParent();
                            if (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true);
                                break;
                            }
                        }
                    } else {
                        return false;
                    }
                }
                break;
            case 3:
                break;
            case 5:
                int index = MotionEventCompat.getActionIndex(ev);
                this.mLastMotionX = ev.getX(index);
                this.mActivePointerId = ev.getPointerId(index);
                break;
            case 6:
                int actionPointerIndex = MotionEventCompat.getActionIndex(ev);
                if (ev.getPointerId(actionPointerIndex) == this.mActivePointerId) {
                    if (actionPointerIndex == 0) {
                        newPointerIndex = 1;
                    }
                    this.mLastMotionX = ev.getX(newPointerIndex);
                    this.mActivePointerId = ev.getPointerId(newPointerIndex);
                    break;
                }
                break;
        }
        if (this.mIsBeingDragged) {
            if (this.mIsOpen && this.targetCurrent < this.targetMax / 2) {
                this.mIsOpen = false;
                if (this.mOnToggleListener != null) {
                    this.mOnToggleListener.onToggle(this.mIsOpen);
                }
            } else if (!this.mIsOpen && this.targetCurrent > this.targetMax / 2) {
                this.mIsOpen = true;
                if (this.mOnToggleListener != null) {
                    this.mOnToggleListener.onToggle(this.mIsOpen);
                }
            }
            ensureTarget();
        }
        this.mActivePointerId = -1;
        this.mIsBeingDragged = false;
        return true;
    }

    private void offsetSwitch(float x) {
        float deltaX = this.mLastMotionX - x;
        this.mLastMotionX = x;
        this.targetCurrent = (int) (((float) this.targetCurrent) - deltaX);
        this.targetCurrent = Math.min(this.targetMax, this.targetCurrent);
        this.targetCurrent = Math.max(0, this.targetCurrent);
        invalidate();
    }

    private void ensureTarget() {
        int animationTarget;
        if (this.mIsOpen) {
            animationTarget = this.targetMax;
        } else {
            animationTarget = 0;
        }
        if (animationTarget != this.targetCurrent && this.targetMax != 0) {
            if (this.valueAnimator != null) {
                this.valueAnimator.cancel();
            }
            this.valueAnimator = ValueAnimator.ofInt(new int[]{this.targetCurrent, animationTarget});
            this.valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    int unused = PowSwitch.this.targetCurrent = ((Integer) animation.getAnimatedValue()).intValue();
                    PowSwitch.this.invalidate();
                }
            });
            this.valueAnimator.setDuration((long) Math.max(50, (int) Math.abs((450.0f * ((float) (animationTarget - this.targetCurrent))) / ((float) this.targetMax))));
            this.valueAnimator.start();
        }
    }

    public void setOpen(boolean isOpen) {
        if (this.mIsOpen != isOpen) {
            this.mIsOpen = isOpen;
            ensureTarget();
        }
    }

    public boolean isOpen() {
        return this.mIsOpen;
    }

    public void setOnToggleListener(OnToggleListener listener) {
        this.mOnToggleListener = listener;
    }
}
