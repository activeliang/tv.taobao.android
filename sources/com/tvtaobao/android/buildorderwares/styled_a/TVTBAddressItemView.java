package com.tvtaobao.android.buildorderwares.styled_a;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import com.tvtaobao.android.buildorderwares.R;
import com.tvtaobao.android.buildorderwares.styled_a.LGBFrameLayout;

public class TVTBAddressItemView extends LGBFrameLayout {
    String addressStr;
    Drawable defaultFocus;
    Drawable defaultNormal;
    boolean isDefault;
    String nameNumberStr;
    private TextView userAddress;
    private TextView userNameNumber;

    public TVTBAddressItemView(Context context) {
        this(context, (AttributeSet) null);
    }

    public TVTBAddressItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TVTBAddressItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.buildorderwares_layout_address_item_view, this);
        setRadius(0.0f);
        setStyle(LGBFrameLayout.Style.single);
        setFocusable(true);
        setDescendantFocusability(393216);
        findViews();
        init();
    }

    private void findViews() {
        this.userNameNumber = (TextView) findViewById(R.id.user_name_number);
        this.userAddress = (TextView) findViewById(R.id.user_address);
    }

    private void init() {
        this.defaultFocus = getResources().getDrawable(R.drawable.buildorderwares_address_item_default_focus);
        this.defaultNormal = getResources().getDrawable(R.drawable.buildorderwares_address_item_default_normal);
    }

    public void setUserNameNumberAndAddress(String nameNumber, String address) {
        if (!TextUtils.isEmpty(nameNumber) && !TextUtils.isEmpty(address)) {
            this.nameNumberStr = nameNumber;
            this.addressStr = address;
            syncState();
        }
    }

    public boolean isDefault() {
        return this.isDefault;
    }

    public void setDefault(boolean aDefault) {
        this.isDefault = aDefault;
        syncState();
    }

    public void refresh() {
        syncState();
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        syncState();
    }

    private void syncState() {
        this.userNameNumber.setText(this.nameNumberStr);
        Paint.FontMetrics fontMetrics = this.userAddress.getPaint().getFontMetrics();
        int txtHeight = (int) (Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.descent));
        if (hasFocus()) {
            this.userNameNumber.setTextColor(-1);
            this.userAddress.setTextColor(-1);
        } else {
            this.userNameNumber.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            this.userAddress.setTextColor(getResources().getColor(R.color.values_color_202020));
        }
        if (!this.isDefault) {
            this.userAddress.setText(this.addressStr);
        } else if (hasFocus()) {
            SpannableString spannableString = new SpannableString("[]" + this.addressStr);
            final int w = getResources().getDimensionPixelOffset(R.dimen.values_dp_40);
            final int h = getResources().getDimensionPixelOffset(R.dimen.values_dp_20);
            final int top = (txtHeight - h) / 2;
            Drawable drawable = new Drawable() {
                public void draw(Canvas canvas) {
                    TVTBAddressItemView.this.defaultFocus.setBounds(0, top - 2, w, (top + h) - 2);
                    TVTBAddressItemView.this.defaultFocus.draw(canvas);
                }

                public void setAlpha(int alpha) {
                }

                public void setColorFilter(ColorFilter colorFilter) {
                }

                public int getOpacity() {
                    return -3;
                }
            };
            drawable.setBounds(0, 0, w + 4, txtHeight);
            spannableString.setSpan(new ImageSpan(drawable), 0, 2, 17);
            this.userAddress.setText(spannableString);
        } else {
            SpannableString spannableString2 = new SpannableString("[]" + this.addressStr);
            final int w2 = getResources().getDimensionPixelOffset(R.dimen.values_dp_40);
            final int h2 = getResources().getDimensionPixelOffset(R.dimen.values_dp_20);
            final int top2 = (txtHeight - h2) / 2;
            Drawable drawable2 = new Drawable() {
                public void draw(Canvas canvas) {
                    TVTBAddressItemView.this.defaultNormal.setBounds(0, top2 - 2, w2, (top2 + h2) - 2);
                    TVTBAddressItemView.this.defaultNormal.draw(canvas);
                }

                public void setAlpha(int alpha) {
                }

                public void setColorFilter(ColorFilter colorFilter) {
                }

                public int getOpacity() {
                    return -3;
                }
            };
            drawable2.setBounds(0, 0, w2 + 4, txtHeight);
            spannableString2.setSpan(new ImageSpan(drawable2), 0, 2, 17);
            this.userAddress.setText(spannableString2);
        }
        invalidateDrawParams();
    }
}
