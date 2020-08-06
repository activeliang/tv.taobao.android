package com.tvtaobao.android.buildorderwares.styled_a;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;
import com.tvtaobao.android.buildorderwares.BOWConfig;
import com.tvtaobao.android.buildorderwares.R;

public class NFITextView extends TextView {
    private int focusColor;
    private int initFocusColor;
    private int initInvalidColor;
    private int initNormalColor;
    private State initState;
    private int invalidColor;
    private int normalColor;
    private State oldState;
    private State state;

    public enum State {
        normal,
        focus,
        invalid
    }

    public NFITextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public NFITextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NFITextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initNormalColor = Color.parseColor("#99bbdd");
        this.initFocusColor = Color.parseColor("#ffffff");
        this.initInvalidColor = Color.parseColor("#707680");
        this.initState = State.normal;
        if (!(context == null || attrs == null)) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.buildorderwares_styled_a);
            this.initNormalColor = ta.getColor(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_nfiTextView_normal_color, this.initNormalColor);
            this.initFocusColor = ta.getColor(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_nfiTextView_focus_color, this.initFocusColor);
            this.initInvalidColor = ta.getColor(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_nfiTextView_invalid_color, this.initInvalidColor);
            int tmpVar = ta.getInteger(R.styleable.buildorderwares_styled_a_buildorderwares_style_a_nfiTextView_state, State.normal.ordinal());
            this.initState = (tmpVar < 0 || tmpVar >= State.values().length) ? State.normal : State.values()[tmpVar];
            ta.recycle();
        }
        init();
    }

    private void init() {
        setNormalColor(this.initNormalColor);
        setFocusColor(this.initFocusColor);
        setInvalidColor(this.initInvalidColor);
        setState(this.initState);
        syncState();
    }

    private void syncState() {
        if (this.oldState != this.state) {
            if (this.state == State.normal) {
                setTextColor(this.normalColor);
            } else if (this.state == State.focus) {
                setTextColor(this.focusColor);
            } else if (this.state == State.invalid) {
                setTextColor(this.invalidColor);
            }
        }
        this.oldState = this.state;
    }

    public int getNormalColor() {
        return this.normalColor;
    }

    public void setNormalColor(int normalColor2) {
        this.normalColor = normalColor2;
        syncState();
    }

    public int getFocusColor() {
        return this.focusColor;
    }

    public void setFocusColor(int focusColor2) {
        this.focusColor = focusColor2;
        syncState();
    }

    public int getInvalidColor() {
        return this.invalidColor;
    }

    public void setInvalidColor(int invalidColor2) {
        this.invalidColor = invalidColor2;
        syncState();
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state2) {
        this.state = state2;
        syncState();
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (!isEnabled()) {
            setState(State.invalid);
        } else if (hasFocus()) {
            setState(State.focus);
        } else {
            setState(State.normal);
        }
    }

    public boolean isInEditMode() {
        if (BOWConfig.DEBUG) {
            return true;
        }
        return super.isInEditMode();
    }
}
