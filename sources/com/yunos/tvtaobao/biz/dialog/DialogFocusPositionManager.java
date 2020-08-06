package com.yunos.tvtaobao.biz.dialog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.yunos.tvtaobao.businessview.R;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusPositionManager;

public class DialogFocusPositionManager extends FocusPositionManager {
    private CUR_SELECT mCurSelect;
    private Button mNegativeButton;
    private Button mPositiveButton;

    private enum CUR_SELECT {
        POSITIVE_BTN,
        NEGATIVE_BTN
    }

    public DialogFocusPositionManager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DialogFocusPositionManager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogFocusPositionManager(Context context) {
        super(context);
    }

    public void initView() {
        View view = findViewById(R.id.super_parent);
        this.mPositiveButton = (Button) view.findViewById(R.id.positiveButton);
        this.mNegativeButton = (Button) view.findViewById(R.id.negativeButton);
        this.mCurSelect = CUR_SELECT.POSITIVE_BTN;
        changeBtnSelectStatus();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            if (event.getKeyCode() == 20) {
                if (this.mCurSelect != CUR_SELECT.POSITIVE_BTN || this.mNegativeButton == null || this.mNegativeButton.getVisibility() != 0) {
                    return true;
                }
                this.mCurSelect = CUR_SELECT.NEGATIVE_BTN;
                changeBtnSelectStatus();
                return true;
            } else if (event.getKeyCode() == 19) {
                if (this.mCurSelect != CUR_SELECT.NEGATIVE_BTN || this.mPositiveButton == null || this.mPositiveButton.getVisibility() != 0) {
                    return true;
                }
                this.mCurSelect = CUR_SELECT.POSITIVE_BTN;
                changeBtnSelectStatus();
                return true;
            }
        }
        if (event.getKeyCode() == 23 || event.getKeyCode() == 66 || event.getKeyCode() == 160) {
            if (this.mCurSelect == CUR_SELECT.POSITIVE_BTN) {
                if (this.mPositiveButton != null) {
                    this.mPositiveButton.dispatchKeyEvent(event);
                }
            } else if (this.mNegativeButton != null) {
                this.mNegativeButton.dispatchKeyEvent(event);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void changeBtnSelectStatus() {
        if (this.mPositiveButton != null && this.mNegativeButton != null) {
            if (this.mCurSelect == CUR_SELECT.POSITIVE_BTN) {
                this.mPositiveButton.setTextColor(getResources().getColor(R.color.ytbv_white));
                this.mPositiveButton.setBackgroundColor(getResources().getColor(R.color.ytbv_button_focus));
                this.mNegativeButton.setTextColor(getResources().getColor(R.color.ytbv_unfocus_text_color));
                this.mNegativeButton.setBackgroundColor(getResources().getColor(17170445));
                return;
            }
            this.mPositiveButton.setTextColor(getResources().getColor(R.color.ytbv_unfocus_text_color));
            this.mPositiveButton.setBackgroundColor(getResources().getColor(17170445));
            this.mNegativeButton.setTextColor(getResources().getColor(R.color.ytbv_white));
            this.mNegativeButton.setBackgroundColor(getResources().getColor(R.color.ytbv_button_focus));
        }
    }
}
