package com.tvtaobao.android.loginverify;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.alibaba.analytics.core.Constants;
import com.tvtaobao.android.loginverify.VerifyNumpad;
import java.util.concurrent.atomic.AtomicInteger;

public class VerifyLayout extends FrameLayout {
    private static final int FLAG_EFFECTIVE_TIME_OUT = 2;
    /* access modifiers changed from: private */
    public ActionListener actionListener;
    /* access modifiers changed from: private */
    public TextView btnVerify;
    private Runnable countDownTask;
    /* access modifiers changed from: private */
    public int flag1;
    /* access modifiers changed from: private */
    public String inputStr;
    /* access modifiers changed from: private */
    public ScreenType screenType;
    private TextView title;
    private TextView titleSub;
    /* access modifiers changed from: private */
    public AtomicInteger verifyCodeEffectiveTime;
    /* access modifiers changed from: private */
    public VerifyCodeView verifyCodeView;
    private ConstraintLayout verifyLayoutRoot;
    /* access modifiers changed from: private */
    public VerifyNumpad verifyNumpad;

    public interface ActionListener {
        void onCountDownFinish();

        void onReVerifyClick();

        void onSureClick(String str);
    }

    public VerifyLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public VerifyLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        int layoutId;
        this.verifyCodeEffectiveTime = new AtomicInteger(0);
        this.flag1 = 0;
        this.countDownTask = new Runnable() {
            public void run() {
                String vertifyStr;
                String vertifyStr2;
                int now = VerifyLayout.this.verifyCodeEffectiveTime.decrementAndGet();
                if (now == 0) {
                    int unused = VerifyLayout.this.flag1 = VerifyLayout.this.flag1 | 2;
                    VerifyLayout.this.btnVerify.setEnabled(true);
                    VerifyLayout.this.btnVerify.setFocusable(true);
                    if (VerifyLayout.this.screenType == ScreenType.HORIZONTAL) {
                        vertifyStr2 = VerifyLayout.this.getResources().getString(R.string.loginverify_verification_reGetCode_h);
                    } else {
                        vertifyStr2 = VerifyLayout.this.getResources().getString(R.string.loginverify_verification_reGetCode);
                    }
                    VerifyLayout.this.btnVerify.setText(vertifyStr2);
                    VerifyLayout.this.btnVerify.requestFocus();
                    VerifyLayout.this.verifyNumpad.setInputEnable(false);
                    if (VerifyLayout.this.actionListener != null) {
                        VerifyLayout.this.actionListener.onCountDownFinish();
                        return;
                    }
                    return;
                }
                try {
                    if (VerifyLayout.this.screenType == ScreenType.HORIZONTAL) {
                        vertifyStr = VerifyLayout.this.getResources().getString(R.string.loginverify_sms_code_success_hint_h);
                    } else {
                        vertifyStr = VerifyLayout.this.getResources().getString(R.string.loginverify_sms_code_success_hint);
                    }
                    VerifyLayout.this.btnVerify.setText(String.format(vertifyStr, new Object[]{"" + now}));
                } catch (Throwable e) {
                    e.printStackTrace();
                    VerifyLayout.this.btnVerify.setText("" + now);
                }
                VerifyLayout.this.postDelayed(this, 1000);
            }
        };
        if (attrs != null) {
            this.screenType = ScreenType.values()[getContext().obtainStyledAttributes(attrs, R.styleable.loginverify).getInteger(R.styleable.loginverify_loginverify_screenType, 0) % ScreenType.values().length];
        }
        switch (this.screenType) {
            case FULL:
                layoutId = R.layout.loginverify_verifylayout;
                break;
            case HORIZONTAL:
                layoutId = R.layout.loginverify_verifylayout_h;
                break;
            case VERTICAL:
                layoutId = R.layout.loginverify_verifylayout_v;
                break;
            default:
                layoutId = R.layout.loginverify_verifylayout;
                break;
        }
        LayoutInflater.from(context).inflate(layoutId, this);
        findViews();
        applyRule();
    }

    public boolean isInEditMode() {
        return super.isInEditMode();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.countDownTask);
    }

    private void findViews() {
        this.verifyLayoutRoot = (ConstraintLayout) findViewById(R.id.verify_layout_root);
        this.verifyCodeView = (VerifyCodeView) findViewById(R.id.verify_code_view);
        this.verifyNumpad = (VerifyNumpad) findViewById(R.id.verify_numpad);
        this.title = (TextView) findViewById(R.id.title);
        this.titleSub = (TextView) findViewById(R.id.title_sub);
        this.verifyCodeView = (VerifyCodeView) findViewById(R.id.verify_code_view);
        this.btnVerify = (TextView) findViewById(R.id.btn_verify);
    }

    private void applyRule() {
        this.verifyNumpad.setInputListener(new VerifyNumpad.InputListener() {
            public void onInput(VerifyNumpad.InputKey key) {
                if (key == VerifyNumpad.InputKey.key_0) {
                    String unused = VerifyLayout.this.inputStr = VerifyLayout.this.inputStr + "0";
                } else if (key == VerifyNumpad.InputKey.key_1) {
                    String unused2 = VerifyLayout.this.inputStr = VerifyLayout.this.inputStr + "1";
                } else if (key == VerifyNumpad.InputKey.key_2) {
                    String unused3 = VerifyLayout.this.inputStr = VerifyLayout.this.inputStr + "2";
                } else if (key == VerifyNumpad.InputKey.key_3) {
                    String unused4 = VerifyLayout.this.inputStr = VerifyLayout.this.inputStr + "3";
                } else if (key == VerifyNumpad.InputKey.key_4) {
                    String unused5 = VerifyLayout.this.inputStr = VerifyLayout.this.inputStr + "4";
                } else if (key == VerifyNumpad.InputKey.key_5) {
                    String unused6 = VerifyLayout.this.inputStr = VerifyLayout.this.inputStr + "5";
                } else if (key == VerifyNumpad.InputKey.key_6) {
                    String unused7 = VerifyLayout.this.inputStr = VerifyLayout.this.inputStr + Constants.LogTransferLevel.L6;
                } else if (key == VerifyNumpad.InputKey.key_7) {
                    String unused8 = VerifyLayout.this.inputStr = VerifyLayout.this.inputStr + Constants.LogTransferLevel.L7;
                } else if (key == VerifyNumpad.InputKey.key_8) {
                    String unused9 = VerifyLayout.this.inputStr = VerifyLayout.this.inputStr + Constants.LogTransferLevel.HIGH;
                } else if (key == VerifyNumpad.InputKey.key_9) {
                    String unused10 = VerifyLayout.this.inputStr = VerifyLayout.this.inputStr + "9";
                } else if (key == VerifyNumpad.InputKey.key_clear) {
                    String unused11 = VerifyLayout.this.inputStr = "";
                } else if (key == VerifyNumpad.InputKey.key_del && !TextUtils.isEmpty(VerifyLayout.this.inputStr)) {
                    if (VerifyLayout.this.inputStr.length() > VerifyLayout.this.verifyCodeView.getVerifyCodeBitNum()) {
                        String unused12 = VerifyLayout.this.inputStr = VerifyLayout.this.inputStr.substring(0, VerifyLayout.this.verifyCodeView.getVerifyCodeBitNum());
                    }
                    String unused13 = VerifyLayout.this.inputStr = VerifyLayout.this.inputStr.substring(0, VerifyLayout.this.inputStr.length() - 1);
                }
                VerifyLayout.this.verifyCodeView.setVerifyCode(VerifyLayout.this.inputStr);
                if (TextUtils.isEmpty(VerifyLayout.this.inputStr) || VerifyLayout.this.inputStr.length() < VerifyLayout.this.verifyCodeView.getVerifyCodeBitNum()) {
                    VerifyLayout.this.btnVerify.setEnabled(false);
                    VerifyLayout.this.btnVerify.setFocusable(false);
                    return;
                }
                VerifyLayout.this.btnVerify.setEnabled(true);
                VerifyLayout.this.btnVerify.setFocusable(true);
            }
        });
        this.btnVerify.setEnabled(false);
        this.btnVerify.setFocusable(false);
        this.btnVerify.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ((VerifyLayout.this.flag1 & 2) == 2) {
                    if (VerifyLayout.this.actionListener != null) {
                        VerifyLayout.this.actionListener.onReVerifyClick();
                    }
                } else if (VerifyLayout.this.actionListener != null) {
                    if (!TextUtils.isEmpty(VerifyLayout.this.inputStr) && VerifyLayout.this.inputStr.length() > VerifyLayout.this.verifyCodeView.getVerifyCodeBitNum()) {
                        String unused = VerifyLayout.this.inputStr = VerifyLayout.this.inputStr.substring(0, VerifyLayout.this.verifyCodeView.getVerifyCodeBitNum());
                    }
                    VerifyLayout.this.actionListener.onSureClick(VerifyLayout.this.inputStr);
                }
            }
        });
    }

    public ActionListener getActionListener() {
        return this.actionListener;
    }

    public void setActionListener(ActionListener actionListener2) {
        this.actionListener = actionListener2;
    }

    public void setPhoneNum(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                this.titleSub.setText(String.format(getResources().getString(R.string.loginverify_sms_veri_title), new Object[]{str}));
            } catch (Throwable e) {
                e.printStackTrace();
                this.titleSub.setText(str);
            }
        }
    }

    public void countDownStart(int effectiveTime, int verifyCodeBitsNum) {
        removeCallbacks(this.countDownTask);
        this.inputStr = "";
        this.flag1 = 0;
        this.verifyCodeView.setVerifyCodeBitNum(verifyCodeBitsNum);
        this.verifyCodeEffectiveTime.set(effectiveTime);
        this.countDownTask.run();
        this.btnVerify.setEnabled(false);
        this.btnVerify.setFocusable(false);
        this.verifyNumpad.setInputEnable(true);
    }

    public void sendCodeError(int verifyCodeBitsNum, String message) {
        removeCallbacks(this.countDownTask);
        this.inputStr = "";
        this.flag1 = 0;
        this.verifyCodeView.setVerifyCodeBitNum(verifyCodeBitsNum);
        this.btnVerify.setEnabled(false);
        this.btnVerify.setFocusable(false);
        this.btnVerify.setText("确认");
        this.titleSub.setText(message);
        this.verifyNumpad.setInputEnable(true);
    }

    public void clearTxt(int verifyCodeBitsNum) {
        this.verifyCodeView.setVerifyCodeBitNum(verifyCodeBitsNum);
    }
}
