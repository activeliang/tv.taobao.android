package com.tvtaobao.android.loginverify;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class VerifyNumpad extends FrameLayout {
    private static final String TAG = VerifyNumpad.class.getSimpleName();
    /* access modifiers changed from: private */
    public InputListener inputListener;
    /* access modifiers changed from: private */
    public TextView numPad0;
    /* access modifiers changed from: private */
    public TextView numPad1;
    /* access modifiers changed from: private */
    public TextView numPad2;
    /* access modifiers changed from: private */
    public TextView numPad3;
    /* access modifiers changed from: private */
    public TextView numPad4;
    /* access modifiers changed from: private */
    public TextView numPad5;
    /* access modifiers changed from: private */
    public TextView numPad6;
    /* access modifiers changed from: private */
    public TextView numPad7;
    /* access modifiers changed from: private */
    public TextView numPad8;
    /* access modifiers changed from: private */
    public TextView numPad9;
    /* access modifiers changed from: private */
    public ImageView numPadClear;
    /* access modifiers changed from: private */
    public ImageView numPadDel;
    private ScreenType screenType;

    public enum InputKey {
        key_0,
        key_1,
        key_2,
        key_3,
        key_4,
        key_5,
        key_6,
        key_7,
        key_8,
        key_9,
        key_clear,
        key_del
    }

    public interface InputListener {
        void onInput(InputKey inputKey);
    }

    public VerifyNumpad(Context context) {
        this(context, (AttributeSet) null);
    }

    public VerifyNumpad(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyNumpad(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int layoutId;
        this.screenType = ScreenType.FULL;
        if (attrs != null) {
            this.screenType = ScreenType.values()[getContext().obtainStyledAttributes(attrs, R.styleable.loginverify).getInteger(R.styleable.loginverify_loginverify_screenType, 0) % ScreenType.values().length];
        }
        switch (this.screenType) {
            case FULL:
                layoutId = R.layout.loginverify_verifynumpad;
                break;
            case HORIZONTAL:
                layoutId = R.layout.loginverify_verifynumpad_h;
                break;
            case VERTICAL:
                layoutId = R.layout.loginverify_verifynumpad_v;
                break;
            default:
                layoutId = R.layout.loginverify_verifynumpad;
                break;
        }
        LayoutInflater.from(context).inflate(layoutId, this);
        findViews();
        setFocusable(false);
    }

    public boolean isInEditMode() {
        return super.isInEditMode();
    }

    private void findViews() {
        this.numPad1 = (TextView) findViewById(R.id.num_pad_1);
        this.numPad2 = (TextView) findViewById(R.id.num_pad_2);
        this.numPad3 = (TextView) findViewById(R.id.num_pad_3);
        this.numPad4 = (TextView) findViewById(R.id.num_pad_4);
        this.numPad5 = (TextView) findViewById(R.id.num_pad_5);
        this.numPad6 = (TextView) findViewById(R.id.num_pad_6);
        this.numPad7 = (TextView) findViewById(R.id.num_pad_7);
        this.numPad8 = (TextView) findViewById(R.id.num_pad_8);
        this.numPad9 = (TextView) findViewById(R.id.num_pad_9);
        this.numPadClear = (ImageView) findViewById(R.id.num_pad_clear);
        this.numPad0 = (TextView) findViewById(R.id.num_pad_0);
        this.numPadDel = (ImageView) findViewById(R.id.num_pad_del);
        View.OnClickListener numpadClickListener = new View.OnClickListener() {
            public void onClick(View v) {
                if (VerifyNumpad.this.inputListener == null) {
                    return;
                }
                if (v == VerifyNumpad.this.numPad0) {
                    VerifyNumpad.this.inputListener.onInput(InputKey.key_0);
                } else if (v == VerifyNumpad.this.numPad1) {
                    VerifyNumpad.this.inputListener.onInput(InputKey.key_1);
                } else if (v == VerifyNumpad.this.numPad2) {
                    VerifyNumpad.this.inputListener.onInput(InputKey.key_2);
                } else if (v == VerifyNumpad.this.numPad3) {
                    VerifyNumpad.this.inputListener.onInput(InputKey.key_3);
                } else if (v == VerifyNumpad.this.numPad4) {
                    VerifyNumpad.this.inputListener.onInput(InputKey.key_4);
                } else if (v == VerifyNumpad.this.numPad5) {
                    VerifyNumpad.this.inputListener.onInput(InputKey.key_5);
                } else if (v == VerifyNumpad.this.numPad6) {
                    VerifyNumpad.this.inputListener.onInput(InputKey.key_6);
                } else if (v == VerifyNumpad.this.numPad7) {
                    VerifyNumpad.this.inputListener.onInput(InputKey.key_7);
                } else if (v == VerifyNumpad.this.numPad8) {
                    VerifyNumpad.this.inputListener.onInput(InputKey.key_8);
                } else if (v == VerifyNumpad.this.numPad9) {
                    VerifyNumpad.this.inputListener.onInput(InputKey.key_9);
                } else if (v == VerifyNumpad.this.numPadClear) {
                    VerifyNumpad.this.inputListener.onInput(InputKey.key_clear);
                } else if (v == VerifyNumpad.this.numPadDel) {
                    VerifyNumpad.this.inputListener.onInput(InputKey.key_del);
                }
            }
        };
        this.numPad0.setOnClickListener(numpadClickListener);
        this.numPad1.setOnClickListener(numpadClickListener);
        this.numPad2.setOnClickListener(numpadClickListener);
        this.numPad3.setOnClickListener(numpadClickListener);
        this.numPad4.setOnClickListener(numpadClickListener);
        this.numPad5.setOnClickListener(numpadClickListener);
        this.numPad6.setOnClickListener(numpadClickListener);
        this.numPad7.setOnClickListener(numpadClickListener);
        this.numPad8.setOnClickListener(numpadClickListener);
        this.numPad9.setOnClickListener(numpadClickListener);
        this.numPadClear.setOnClickListener(numpadClickListener);
        this.numPadDel.setOnClickListener(numpadClickListener);
        this.numPadClear.setAlpha(0.8f);
        this.numPadClear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    VerifyNumpad.this.numPadClear.setAlpha(1.0f);
                } else {
                    VerifyNumpad.this.numPadClear.setAlpha(0.8f);
                }
            }
        });
        this.numPadDel.setAlpha(0.8f);
        this.numPadDel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    VerifyNumpad.this.numPadDel.setAlpha(1.0f);
                } else {
                    VerifyNumpad.this.numPadDel.setAlpha(0.8f);
                }
            }
        });
    }

    public void setInputEnable(boolean enable) {
        if (enable) {
            setDescendantFocusability(131072);
            this.numPad5.requestFocus();
            return;
        }
        clearFocus();
        setDescendantFocusability(393216);
    }

    public InputListener getInputListener() {
        return this.inputListener;
    }

    public void setInputListener(InputListener inputListener2) {
        this.inputListener = inputListener2;
    }
}
