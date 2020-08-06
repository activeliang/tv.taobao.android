package com.tvtaobao.android.loginverify;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Space;
import com.tvtaobao.android.loginverify.VerifyCodeItemView;

public class VerifyCodeView extends LinearLayout {
    Runnable refreshTask;
    private final String space;
    /* access modifiers changed from: private */
    public String verifyCode;
    /* access modifiers changed from: private */
    public int verifyCodeBitNum;

    public VerifyCodeView(Context context) {
        this(context, (AttributeSet) null);
    }

    public VerifyCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerifyCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.space = " ";
        this.refreshTask = new Runnable() {
            public void run() {
                VerifyCodeView.this.removeAllViews();
                for (int i = 0; i < VerifyCodeView.this.verifyCodeBitNum; i++) {
                    char val = ' ';
                    if (!TextUtils.isEmpty(VerifyCodeView.this.verifyCode) && i < VerifyCodeView.this.verifyCode.length()) {
                        val = VerifyCodeView.this.verifyCode.charAt(i);
                    }
                    VerifyCodeView.this.addView(VerifyCodeView.this.genItemView(val, i), VerifyCodeView.this.genItemLayoutParams(i));
                    if (i != VerifyCodeView.this.verifyCodeBitNum - 1) {
                        VerifyCodeView.this.addView(new Space(VerifyCodeView.this.getContext()), VerifyCodeView.this.genItemSeparatorLayoutParams());
                    }
                }
            }
        };
        setOrientation(0);
    }

    public boolean isInEditMode() {
        return super.isInEditMode();
    }

    public String getVerifyCode() {
        return this.verifyCode;
    }

    public void setVerifyCode(String verifyCode2) {
        this.verifyCode = verifyCode2;
        post(this.refreshTask);
    }

    public int getVerifyCodeBitNum() {
        return this.verifyCodeBitNum;
    }

    public void setVerifyCodeBitNum(int verifyCodeBitNum2) {
        this.verifyCodeBitNum = verifyCodeBitNum2;
        String str = "";
        for (int i = 0; i < verifyCodeBitNum2; i++) {
            str = str + " ";
        }
        setVerifyCode(str);
    }

    /* access modifiers changed from: private */
    public LinearLayout.LayoutParams genItemSeparatorLayoutParams() {
        return new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.values_dp_12), 1);
    }

    /* access modifiers changed from: private */
    public LinearLayout.LayoutParams genItemLayoutParams(int pos) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, getResources().getDimensionPixelSize(R.dimen.values_dp_80));
        lp.weight = 1.0f;
        return lp;
    }

    /* access modifiers changed from: private */
    public VerifyCodeItemView genItemView(char c, int pos) {
        VerifyCodeItemView verifyCodeItemView = new VerifyCodeItemView(getContext());
        verifyCodeItemView.setText("" + c);
        verifyCodeItemView.setTextColor(-1);
        verifyCodeItemView.setTextSize(0, (float) getResources().getDimensionPixelSize(R.dimen.values_dp_60));
        verifyCodeItemView.setGravity(17);
        verifyCodeItemView.setUnderLineStyle(VerifyCodeItemView.UnderLineStyle.normal);
        if (!TextUtils.isEmpty(this.verifyCode)) {
            if (pos == this.verifyCode.replace(" ", "").length()) {
                verifyCodeItemView.setUnderLineStyle(VerifyCodeItemView.UnderLineStyle.wait4Set);
            }
        } else if (pos == 0) {
            verifyCodeItemView.setUnderLineStyle(VerifyCodeItemView.UnderLineStyle.wait4Set);
        }
        return verifyCodeItemView;
    }
}
