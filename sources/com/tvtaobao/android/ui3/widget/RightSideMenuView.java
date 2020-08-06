package com.tvtaobao.android.ui3.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Space;
import com.tvtaobao.android.ui3.R;

public class RightSideMenuView extends FrameLayout {
    private View background;
    private int bgColor;
    private int[] ids;
    private ConstraintLayout menuContainer;
    private View[] menuItems;
    private int separatorColor;
    private int separatorHeight;
    private Space start;
    private Runnable syncState;

    public RightSideMenuView(Context context) {
        this(context, (AttributeSet) null);
    }

    public RightSideMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RightSideMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.ids = new int[]{R.id.id_place_holder_1, R.id.id_place_holder_2, R.id.id_place_holder_3, R.id.id_place_holder_4, R.id.id_place_holder_5, R.id.id_place_holder_6, R.id.id_place_holder_7, R.id.id_place_holder_8, R.id.id_place_holder_9, R.id.id_place_holder_10, R.id.id_place_holder_11, R.id.id_place_holder_12, R.id.id_place_holder_13, R.id.id_place_holder_14, R.id.id_place_holder_15, R.id.id_place_holder_16, R.id.id_place_holder_17, R.id.id_place_holder_18, R.id.id_place_holder_19, R.id.id_place_holder_20, R.id.id_place_holder_21};
        this.bgColor = Color.parseColor("#B2000000");
        this.separatorHeight = 1;
        this.separatorColor = Color.parseColor("#59AABBCC");
        this.syncState = new Runnable() {
            public void run() {
                RightSideMenuView.this.doInflate();
            }
        };
        LayoutInflater.from(getContext()).inflate(R.layout.ui3wares_layout_right_side_menu, this);
        findViews();
        init();
    }

    private void findViews() {
        this.menuContainer = (ConstraintLayout) findViewById(R.id.menu_container);
        this.background = findViewById(R.id.background);
        this.start = (Space) findViewById(R.id.start);
    }

    private void init() {
    }

    public void inflateWith(View[] a) {
        this.menuItems = a;
        refresh();
    }

    /* access modifiers changed from: private */
    public void doInflate() {
        for (int i = this.menuContainer.getChildCount() - 1; i >= 0; i--) {
            View tmp = this.menuContainer.getChildAt(i);
            if (!(tmp == this.background || tmp == this.start)) {
                this.menuContainer.removeView(tmp);
            }
        }
        if (this.menuItems != null) {
            View iterator = this.start;
            int idIndex = 0;
            for (int i2 = 0; i2 < this.menuItems.length && idIndex < this.ids.length; i2++) {
                FrameLayout itemWrapper = new FrameLayout(getContext());
                int idIndex2 = idIndex + 1;
                itemWrapper.setId(this.ids[idIndex]);
                itemWrapper.addView(this.menuItems[i2]);
                ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(-2, -2);
                lp.rightToRight = 0;
                lp.topToBottom = iterator.getId();
                this.menuContainer.addView(itemWrapper, lp);
                iterator = itemWrapper;
                if (i2 < this.menuItems.length - 1) {
                    View line = new View(getContext());
                    idIndex = idIndex2 + 1;
                    line.setId(this.ids[idIndex2]);
                    line.setBackgroundColor(this.separatorColor);
                    ConstraintLayout.LayoutParams lp2 = new ConstraintLayout.LayoutParams(0, this.separatorHeight);
                    lp2.rightToRight = this.background.getId();
                    lp2.leftToLeft = this.background.getId();
                    lp2.topToBottom = iterator.getId();
                    this.menuContainer.addView(line, lp2);
                    if (this.menuItems[i2].getVisibility() != 0) {
                        line.setVisibility(4);
                    }
                    iterator = line;
                } else {
                    idIndex = idIndex2;
                }
            }
        }
    }

    public int getBgColor() {
        return this.bgColor;
    }

    public void setBgColor(int bgColor2) {
        this.bgColor = bgColor2;
        refresh();
    }

    public int getSeparatorHeight() {
        return this.separatorHeight;
    }

    public void setSeparatorHeight(int separatorHeight2) {
        this.separatorHeight = separatorHeight2;
        refresh();
    }

    public int getSeparatorColor() {
        return this.separatorColor;
    }

    public void setSeparatorColor(int separatorColor2) {
        this.separatorColor = separatorColor2;
        refresh();
    }

    private void refresh() {
        removeCallbacks(this.syncState);
        postDelayed(this.syncState, 100);
    }

    public boolean isInEditMode() {
        return super.isInEditMode();
    }
}
