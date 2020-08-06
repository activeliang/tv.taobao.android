package com.tvtaobao.android.buildorderwares.styled_a;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.tvtaobao.android.buildorderwares.BOWConfig;
import com.tvtaobao.android.buildorderwares.BOWLogger;
import com.tvtaobao.android.buildorderwares.R;

public class SSNFImageView extends ImageView {
    private static final int FLAG_IGNORE_FOCUS_STATE = 1;
    private static final String TAG = SSNFImageView.class.getSimpleName();
    private int flag;
    private Drawable moreFocus;
    private Drawable moreNormal;
    private State oldState;
    private State state;
    private Style style;
    private Drawable toggleSelectedFocus;
    private Drawable toggleSelectedNormal;
    private Drawable toggleUnselectedFocus;
    private Drawable toggleUnselectedNormal;

    public enum State {
        selected,
        unselected
    }

    public enum Style {
        Toggle,
        Select
    }

    public SSNFImageView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SSNFImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SSNFImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.state = State.unselected;
        this.style = Style.Toggle;
        this.flag = 0;
        init();
    }

    private void init() {
        this.toggleUnselectedNormal = getContext().getResources().getDrawable(R.drawable.buildorderwares_ssnfimageview_toggle_n);
        this.toggleUnselectedFocus = getContext().getResources().getDrawable(R.drawable.buildorderwares_ssnfimageview_toggle_f);
        this.toggleSelectedNormal = getContext().getResources().getDrawable(R.drawable.buildorderwares_ssnfimageview_toggle_ns);
        this.toggleSelectedFocus = getContext().getResources().getDrawable(R.drawable.buildorderwares_ssnfimageview_toggle_fs);
        this.moreNormal = getContext().getResources().getDrawable(R.drawable.buildorderwares_ssnfimageview_more_n);
        this.moreFocus = getContext().getResources().getDrawable(R.drawable.buildorderwares_ssnfimageview_more_f);
        syncState();
    }

    private void syncState() {
        BOWLogger.i(TAG, ".syncState " + this.oldState + "," + this.state + "," + hasFocus());
        if (this.style == Style.Toggle) {
            if (this.state == State.unselected && hasFocus()) {
                setImageDrawable(this.toggleUnselectedFocus);
            } else if (this.state == State.unselected && !hasFocus()) {
                setImageDrawable(this.toggleUnselectedNormal);
            } else if (this.state == State.selected && hasFocus()) {
                setImageDrawable(this.toggleSelectedFocus);
            } else if (this.state == State.selected && !hasFocus()) {
                setImageDrawable(this.toggleSelectedNormal);
            }
            this.oldState = this.state;
        } else if (hasFocus()) {
            setImageDrawable(this.moreFocus);
        } else {
            setImageDrawable(this.moreNormal);
        }
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state2) {
        this.state = state2;
        syncState();
    }

    public void setStyle(Style style2) {
        this.style = style2;
        syncState();
    }

    public Drawable getToggleUnselectedNormal() {
        return this.toggleUnselectedNormal;
    }

    public void setToggleUnselectedNormal(Drawable toggleUnselectedNormal2) {
        this.toggleUnselectedNormal = toggleUnselectedNormal2;
        syncState();
    }

    public Drawable getToggleUnselectedFocus() {
        return this.toggleUnselectedFocus;
    }

    public void setToggleUnselectedFocus(Drawable toggleUnselectedFocus2) {
        this.toggleUnselectedFocus = toggleUnselectedFocus2;
        syncState();
    }

    public Drawable getToggleSelectedNormal() {
        return this.toggleSelectedNormal;
    }

    public void setToggleSelectedNormal(Drawable toggleSelectedNormal2) {
        this.toggleSelectedNormal = toggleSelectedNormal2;
        syncState();
    }

    public Drawable getToggleSelectedFocus() {
        return this.toggleSelectedFocus;
    }

    public void setToggleSelectedFocus(Drawable toggleSelectedFocus2) {
        this.toggleSelectedFocus = toggleSelectedFocus2;
        syncState();
    }

    public Drawable getMoreNormal() {
        return this.moreNormal;
    }

    public void setMoreNormal(Drawable moreNormal2) {
        this.moreNormal = moreNormal2;
        syncState();
    }

    public Drawable getMoreFocus() {
        return this.moreFocus;
    }

    public void setMoreFocus(Drawable moreFocus2) {
        this.moreFocus = moreFocus2;
        syncState();
    }

    public void setIgnoreFocusState() {
        this.flag |= 1;
    }

    public boolean isIgnoreFocusState() {
        return (this.flag & 1) == 1;
    }

    public boolean isInEditMode() {
        if (BOWConfig.DEBUG) {
            return true;
        }
        return super.isInEditMode();
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (!isIgnoreFocusState()) {
            syncState();
        }
    }
}
