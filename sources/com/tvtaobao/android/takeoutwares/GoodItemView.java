package com.tvtaobao.android.takeoutwares;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tvtaobao.android.takeoutwares.AddAndReduceView;
import com.tvtaobao.android.ui3.widget.SimpleCardView;

public class GoodItemView extends FrameLayout {
    private static final String TAG = GoodItemView.class.getSimpleName();
    private AddAndReduceView addAndReduce;
    private SimpleCardView goodCardView;
    /* access modifiers changed from: private */
    public View goodFocusStatus;
    private ImageView goodItemMark;
    private TextView goodItemNum;
    private TextView goodMake;
    /* access modifiers changed from: private */
    public TextView goodName;
    private SaleTipView goodNumLimit;
    private ImageView goodPic;
    private FrameLayout goodPicToast;
    private TextView goodPrice;
    private TextView goodRealPrice;
    private TextView goodSellCount;
    private LinearLayout saleTips;

    public GoodItemView(Context context) {
        this(context, (AttributeSet) null);
    }

    public GoodItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoodItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.takeoutwares_good_item, this);
        findViews();
    }

    private void findViews() {
        this.goodFocusStatus = findViewById(R.id.good_focus_status);
        this.goodCardView = (SimpleCardView) findViewById(R.id.good_card_view);
        this.goodPic = (ImageView) findViewById(R.id.good_pic);
        this.goodPicToast = (FrameLayout) findViewById(R.id.good_pic_toast);
        this.goodName = (TextView) findViewById(R.id.good_name);
        this.goodMake = (TextView) findViewById(R.id.good_make);
        this.goodSellCount = (TextView) findViewById(R.id.good_sell_count);
        this.saleTips = (LinearLayout) findViewById(R.id.sale_tips);
        this.goodNumLimit = (SaleTipView) findViewById(R.id.good_num_limit);
        this.goodRealPrice = (TextView) findViewById(R.id.good_real_price);
        this.goodPrice = (TextView) findViewById(R.id.good_price);
        this.addAndReduce = (AddAndReduceView) findViewById(R.id.add_and_reduce);
        this.goodItemMark = (ImageView) findViewById(R.id.good_item_mark);
        this.goodItemNum = (TextView) findViewById(R.id.good_item_num);
        this.goodFocusStatus.setVisibility(4);
        int tmpDp24 = getContext().getResources().getDimensionPixelSize(R.dimen.values_dp_24);
        int tmpDp32 = getContext().getResources().getDimensionPixelSize(R.dimen.values_dp_32);
        this.addAndReduce.setCountSizeInPx(tmpDp24).setBtnWHInPx(tmpDp32, tmpDp32).setCountStyle(AddAndReduceView.CountStyle.noX).setBtnStyle(AddAndReduceView.BtnStyle.inGoodItem).addFocusChangeListener(new AddAndReduceView.AARVFocusChangeListener() {
            public void onFocusChange(boolean hasFocus) {
                if (hasFocus) {
                    GoodItemView.this.goodFocusStatus.setVisibility(0);
                    GoodItemView.this.goodName.setSelected(true);
                    return;
                }
                GoodItemView.this.goodFocusStatus.setVisibility(4);
                GoodItemView.this.goodName.setSelected(false);
            }
        }).apply((Runnable) null);
    }

    public void focusableViewAvailable(View v) {
        if (v == this.goodFocusStatus) {
            TOWLogger.i(TAG, "focusableViewAvailable a");
            return;
        }
        TOWLogger.i(TAG, "focusableViewAvailable b");
        super.focusableViewAvailable(v);
    }

    public View getGoodFocusStatus() {
        return this.goodFocusStatus;
    }

    public SimpleCardView getGoodCardView() {
        return this.goodCardView;
    }

    public ImageView getGoodPic() {
        return this.goodPic;
    }

    public FrameLayout getGoodPicToast() {
        return this.goodPicToast;
    }

    public TextView getGoodName() {
        return this.goodName;
    }

    public TextView getGoodMake() {
        return this.goodMake;
    }

    public TextView getGoodSellCount() {
        return this.goodSellCount;
    }

    public LinearLayout getSaleTips() {
        return this.saleTips;
    }

    public TextView getGoodRealPrice() {
        return this.goodRealPrice;
    }

    public TextView getGoodPrice() {
        return this.goodPrice;
    }

    public AddAndReduceView getAddAndReduce() {
        return this.addAndReduce;
    }

    public ImageView getGoodItemMark() {
        return this.goodItemMark;
    }

    public SaleTipView getGoodNumLimit() {
        return this.goodNumLimit;
    }

    public TextView getGoodItemNum() {
        return this.goodItemNum;
    }
}
