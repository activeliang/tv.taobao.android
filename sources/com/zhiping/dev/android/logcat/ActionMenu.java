package com.zhiping.dev.android.logcat;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ActionMenu extends Dialog {
    LinearLayout linearLayout;

    public interface IMenuItem {
        String getItemTxt();

        void onClick();
    }

    public ActionMenu(Context context) {
        super(context);
        ScrollView scrollView = new ScrollView(context);
        this.linearLayout = new LinearLayout(context);
        this.linearLayout.setBackgroundColor(-1);
        this.linearLayout.setOrientation(1);
        this.linearLayout.setGravity(17);
        scrollView.addView(this.linearLayout);
        setContentView(scrollView);
    }

    public ActionMenu cfgMenu(IMenuItem... menuItems) {
        this.linearLayout.removeAllViews();
        if (menuItems != null) {
            for (IMenuItem genMenuItemView : menuItems) {
                this.linearLayout.addView(genMenuItemView(genMenuItemView));
            }
        }
        return this;
    }

    private TextView genMenuItemView(final IMenuItem menuItem) {
        final TextView tv2 = new TextView(getContext());
        tv2.setTag(menuItem);
        tv2.setFocusable(true);
        tv2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv2.setTextColor(SupportMenu.CATEGORY_MASK);
                } else {
                    tv2.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                }
            }
        });
        tv2.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        tv2.setPadding(10, 10, 10, 10);
        tv2.setTextSize(1, 18.0f);
        tv2.setText(menuItem.getItemTxt());
        tv2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                menuItem.onClick();
            }
        });
        return tv2;
    }
}
