package com.ali.user.open.core.webview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ali.user.open.core.R;
import java.lang.reflect.Field;

public class AliUccCustomToolbar extends Toolbar {
    private CharSequence mTitleText;
    private int mTitleTextAppearance;
    private int mTitleTextColor;
    private TextView mTitleTextView;

    public AliUccCustomToolbar(Context context) {
        super(context);
        resolveAttribute(context, (AttributeSet) null, R.attr.toolbarStyle);
    }

    public AliUccCustomToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        resolveAttribute(context, attrs, R.attr.toolbarStyle);
    }

    public AliUccCustomToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        resolveAttribute(context, attrs, defStyleAttr);
    }

    private void resolveAttribute(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        Context context2 = getContext();
        TypedArray a = context2.obtainStyledAttributes(attrs, R.styleable.Toolbar, defStyleAttr, 0);
        int titleTextAppearance = a.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
        if (titleTextAppearance != 0) {
            setTitleTextAppearance(context2, titleTextAppearance);
        }
        if (this.mTitleTextColor != 0) {
            setTitleTextColor(this.mTitleTextColor);
        }
        a.recycle();
        post(new Runnable() {
            public void run() {
                if (AliUccCustomToolbar.this.getLayoutParams() instanceof Toolbar.LayoutParams) {
                    ((Toolbar.LayoutParams) AliUccCustomToolbar.this.getLayoutParams()).gravity = 17;
                }
            }
        });
    }

    public CharSequence getTitle() {
        return this.mTitleText;
    }

    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            if (this.mTitleTextView == null) {
                Context context = getContext();
                this.mTitleTextView = new TextView(context);
                this.mTitleTextView.setSingleLine();
                this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
                if (this.mTitleTextAppearance != 0) {
                    this.mTitleTextView.setTextAppearance(context, this.mTitleTextAppearance);
                }
                if (this.mTitleTextColor != 0) {
                    this.mTitleTextView.setTextColor(this.mTitleTextColor);
                }
            }
            if (this.mTitleTextView.getParent() != this) {
                addCenterView(this.mTitleTextView);
            }
        } else if (this.mTitleTextView != null && this.mTitleTextView.getParent() == this) {
            removeView(this.mTitleTextView);
        }
        if (this.mTitleTextView != null) {
            this.mTitleTextView.setText(title);
        }
        this.mTitleText = title;
    }

    private void addCenterView(View v) {
        Toolbar.LayoutParams lp;
        ViewGroup.LayoutParams vlp = v.getLayoutParams();
        if (vlp == null) {
            lp = generateDefaultLayoutParams();
        } else if (!checkLayoutParams(vlp)) {
            lp = generateLayoutParams(vlp);
        } else {
            lp = (Toolbar.LayoutParams) vlp;
        }
        addView(v, lp);
    }

    public Toolbar.LayoutParams generateLayoutParams(AttributeSet attrs) {
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(getContext(), attrs);
        lp.gravity = 17;
        return lp;
    }

    /* access modifiers changed from: protected */
    public Toolbar.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        Toolbar.LayoutParams lp;
        if (p instanceof Toolbar.LayoutParams) {
            lp = new Toolbar.LayoutParams((Toolbar.LayoutParams) p);
        } else if (p instanceof ActionBar.LayoutParams) {
            lp = new Toolbar.LayoutParams((ActionBar.LayoutParams) p);
        } else if (p instanceof ViewGroup.MarginLayoutParams) {
            lp = new Toolbar.LayoutParams((ViewGroup.MarginLayoutParams) p);
        } else {
            lp = new Toolbar.LayoutParams(p);
        }
        lp.gravity = 17;
        return lp;
    }

    /* access modifiers changed from: protected */
    public Toolbar.LayoutParams generateDefaultLayoutParams() {
        Toolbar.LayoutParams lp = new Toolbar.LayoutParams(-2, -2);
        lp.gravity = 17;
        return lp;
    }

    public void setTitleTextAppearance(Context context, @StyleRes int resId) {
        this.mTitleTextAppearance = resId;
        if (this.mTitleTextView != null) {
            this.mTitleTextView.setTextAppearance(context, resId);
        }
    }

    public void setTitleTextColor(@ColorInt int color) {
        this.mTitleTextColor = color;
        if (this.mTitleTextView != null) {
            this.mTitleTextView.setTextColor(color);
        }
    }

    public void setNavigationIcon(@Nullable Drawable icon) {
        super.setNavigationIcon(icon);
        setGravityCenter();
    }

    public void setGravityCenter() {
        post(new Runnable() {
            public void run() {
                AliUccCustomToolbar.this.setCenter("mNavButtonView");
                AliUccCustomToolbar.this.setCenter("mMenuView");
            }
        });
    }

    /* access modifiers changed from: private */
    public void setCenter(String fieldName) {
        try {
            Field field = getClass().getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);
            Object obj = field.get(this);
            if (obj != null && (obj instanceof View)) {
                View view = (View) obj;
                ViewGroup.LayoutParams lp = view.getLayoutParams();
                if (lp instanceof ActionBar.LayoutParams) {
                    ((ActionBar.LayoutParams) lp).gravity = 17;
                    view.setLayoutParams(lp);
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
    }
}
