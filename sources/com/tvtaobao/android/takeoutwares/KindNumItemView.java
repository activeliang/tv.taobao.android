package com.tvtaobao.android.takeoutwares;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class KindNumItemView extends FrameLayout {
    private static final String TAG = KindNumItemView.class.getSimpleName();
    /* access modifiers changed from: private */
    public Drawable focusBg;
    /* access modifiers changed from: private */
    public FocusChangeListener focusChangeListener;
    /* access modifiers changed from: private */
    public int focusClr;
    private Drawable hintBg;
    /* access modifiers changed from: private */
    public TextView kindName;
    private TextView kindNum;
    private Drawable kindNumBg;
    /* access modifiers changed from: private */
    public Drawable normalBg;
    /* access modifiers changed from: private */
    public int normalClr;
    /* access modifiers changed from: private */
    public OnKindNameClickListener onKindNameClickListener;

    public interface FocusChangeListener {
        void onFocusChange(boolean z);
    }

    public interface OnKindNameClickListener {
        void onClick();
    }

    public KindNumItemView(Context context) {
        this(context, (AttributeSet) null);
    }

    public KindNumItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KindNumItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.takeoutwares_layout_kind_num, this, true);
        findViews();
        this.focusBg = getResources().getDrawable(R.drawable.takeoutwares_kind_num_focus_enter_bg);
        this.normalBg = getResources().getDrawable(R.drawable.takeoutwares_kind_num_focus_leave_bg);
        this.hintBg = getResources().getDrawable(R.drawable.takeoutwares_kind_num_focus_hint_bg);
        this.kindNumBg = getResources().getDrawable(R.drawable.takeoutwares_kind_num_select_count_bg);
        this.focusClr = Color.parseColor("#ededed");
        this.normalClr = Color.parseColor("#909ca7");
        this.kindNum.setBackgroundDrawable(this.kindNumBg);
        this.kindName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (KindNumItemView.this.onKindNameClickListener != null) {
                    KindNumItemView.this.onKindNameClickListener.onClick();
                }
            }
        });
        this.kindName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                boolean z = true;
                if (hasFocus) {
                    KindNumItemView.this.kindName.setBackgroundDrawable(KindNumItemView.this.focusBg);
                    KindNumItemView.this.kindName.setTextColor(KindNumItemView.this.focusClr);
                    KindNumItemView.this.kindName.setSelected(true);
                } else {
                    KindNumItemView.this.kindName.setBackgroundDrawable(KindNumItemView.this.normalBg);
                    KindNumItemView.this.kindName.setTextColor(KindNumItemView.this.normalClr);
                    KindNumItemView.this.kindName.setSelected(false);
                }
                if (KindNumItemView.this.focusChangeListener != null) {
                    FocusChangeListener access$600 = KindNumItemView.this.focusChangeListener;
                    if (!KindNumItemView.this.hasFocus() || !KindNumItemView.this.kindName.hasFocus()) {
                        z = false;
                    }
                    access$600.onFocusChange(z);
                }
            }
        });
    }

    private void findViews() {
        this.kindName = (TextView) findViewById(R.id.kind_name);
        this.kindNum = (TextView) findViewById(R.id.kind_num);
    }

    public void focusableViewAvailable(View v) {
        if (v == this.kindNum) {
            TOWLogger.i(TAG, "focusableViewAvailable a");
            return;
        }
        TOWLogger.i(TAG, "focusableViewAvailable b");
        super.focusableViewAvailable(v);
    }

    public boolean isInEditMode() {
        if (TOWConfig.DEBUG) {
            return true;
        }
        return super.isInEditMode();
    }

    public TextView getKindName() {
        return this.kindName;
    }

    public TextView getKindNum() {
        return this.kindNum;
    }

    public KindNumItemView setKindName(String txt) {
        if (!TextUtils.isEmpty(txt)) {
            this.kindName.setText(txt);
        } else {
            this.kindName.setText("");
        }
        return this;
    }

    public KindNumItemView setKindNum(int count) {
        if (count > 0) {
            this.kindNum.setText("" + count);
            this.kindNum.setVisibility(0);
        } else {
            this.kindNum.setVisibility(8);
        }
        return this;
    }

    public KindNumItemView setFocusBg(Drawable focusBg2) {
        this.focusBg = focusBg2;
        return this;
    }

    public KindNumItemView setNormalBg(Drawable normalBg2) {
        this.normalBg = normalBg2;
        return this;
    }

    public KindNumItemView setHintBg(Drawable hintBg2) {
        this.hintBg = hintBg2;
        return this;
    }

    public KindNumItemView setKindNumBg(Drawable kindNumBg2) {
        this.kindNumBg = kindNumBg2;
        return this;
    }

    public KindNumItemView setFocusClr(int focusClr2) {
        this.focusClr = focusClr2;
        return this;
    }

    public KindNumItemView setNormalClr(int normalClr2) {
        this.normalClr = normalClr2;
        return this;
    }

    public KindNumItemView hint() {
        this.kindName.setBackgroundDrawable(this.hintBg);
        this.kindName.setTextColor(this.focusClr);
        return this;
    }

    public Drawable getFocusBg() {
        return this.focusBg;
    }

    public Drawable getNormalBg() {
        return this.normalBg;
    }

    public Drawable getHintBg() {
        return this.hintBg;
    }

    public Drawable getKindNumBg() {
        return this.kindNumBg;
    }

    public int getFocusClr() {
        return this.focusClr;
    }

    public int getNormalClr() {
        return this.normalClr;
    }

    public OnKindNameClickListener getOnKindNameClickListener() {
        return this.onKindNameClickListener;
    }

    public KindNumItemView setOnKindNameClickListener(OnKindNameClickListener onKindNameClickListener2) {
        this.onKindNameClickListener = onKindNameClickListener2;
        return this;
    }

    public FocusChangeListener getFocusChangeListener() {
        return this.focusChangeListener;
    }

    public KindNumItemView setFocusChangeListener(FocusChangeListener focusChangeListener2) {
        this.focusChangeListener = focusChangeListener2;
        return this;
    }
}
