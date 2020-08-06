package com.yunos.tvtaobao.biz.widget.newsku.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tvtaobao.android.ui3.widget.UI3Toast;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.util.BitMapUtil;
import com.yunos.tvtaobao.biz.widget.newsku.SkuActivity;
import com.yunos.tvtaobao.biz.widget.newsku.TradeType;
import com.yunos.tvtaobao.businessview.R;

public class NumChooseItem extends LinearLayout {
    private Context context;
    private Bitmap leftInvaildFocused;
    private Bitmap leftInvaildUnfocus;
    private Bitmap leftVaildFocused;
    private Bitmap leftVaildUnfocus;
    private ImageView leftView;
    private int maxNum = 1;
    private int num = 1;
    private NumChangeListener numChangeListener;
    private Bitmap rightInvaildFocused;
    private Bitmap rightInvaildUnfocus;
    private Bitmap rightVaildFocused;
    private Bitmap rightVaildUnfocus;
    private ImageView rightView = null;
    private TextView textView;
    private String tradeType = TradeType.ADD_CART;
    private int type = 0;

    public interface NumChangeListener {
        void onNumChange(int i);
    }

    public NumChooseItem(Context context2) {
        super(context2);
        initView(context2);
    }

    public NumChooseItem(Context context2, AttributeSet attrs) {
        super(context2, attrs);
        initView(context2);
    }

    public NumChooseItem(Context context2, AttributeSet attrs, int defStyleAttr) {
        super(context2, attrs, defStyleAttr);
        initView(context2);
    }

    public void setTradeType(String tradeType2) {
        this.tradeType = tradeType2;
    }

    private void initBitmap() {
        this.leftInvaildUnfocus = BitMapUtil.readBmp(this.context, R.drawable.iv_numchoose_left_invaild_unfocus);
        this.leftVaildUnfocus = BitMapUtil.readBmp(CoreApplication.getApplication(), R.drawable.iv_numchoose_left_vaild_unfocus);
        this.leftInvaildFocused = BitMapUtil.readBmp(CoreApplication.getApplication(), R.drawable.iv_numchoose_left_invaild_focused);
        this.leftVaildFocused = BitMapUtil.readBmp(CoreApplication.getApplication(), R.drawable.iv_numchoose_left_vaild_focused);
        this.rightInvaildUnfocus = BitMapUtil.readBmp(CoreApplication.getApplication(), R.drawable.iv_numchoose_right_invaild_unfocus);
        this.rightVaildUnfocus = BitMapUtil.readBmp(CoreApplication.getApplication(), R.drawable.iv_numchoose_right_vaild_unfocus);
        this.rightInvaildFocused = BitMapUtil.readBmp(CoreApplication.getApplication(), R.drawable.iv_numchoose_right_invaild_focused);
        this.rightVaildFocused = BitMapUtil.readBmp(CoreApplication.getApplication(), R.drawable.iv_numchoose_right_vaild_focused);
    }

    private void initView(Context context2) {
        this.context = context2;
        initBitmap();
        setFocusable(true);
        setOrientation(0);
        setBackgroundResource(R.drawable.bg_sku_item_unfocused_enable);
        View view = LayoutInflater.from(context2).inflate(R.layout.item_numchoose, (ViewGroup) null);
        this.leftView = (ImageView) view.findViewById(R.id.item_num_choose_left);
        this.rightView = (ImageView) view.findViewById(R.id.item_num_choose_right);
        this.textView = (TextView) view.findViewById(R.id.item_num_choose_num);
        this.textView.setTextColor(Color.parseColor("#202020"));
        addView(view);
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        updateArrow(gainFocus);
        if (gainFocus) {
            setBackgroundResource(R.drawable.bg_sku_item_focused_color);
            this.textView.setTextColor(Color.parseColor("#FFFFFF"));
            return;
        }
        setBackgroundResource(R.drawable.bg_sku_item_unfocused_enable);
        this.textView.setTextColor(Color.parseColor("#202020"));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isFocused()) {
            switch (keyCode) {
                case 21:
                    if (this.num > 1) {
                        this.num--;
                        updateNum(this.num);
                        return true;
                    } else if (this.tradeType.equals(TradeType.TAKE_OUT_ADD_CART)) {
                        showError(this.context.getString(R.string.take_out_sku_num_exceed_minimum));
                        return true;
                    } else {
                        showError(this.context.getString(R.string.new_shop_sku_num_exceed_minimum));
                        return true;
                    }
                case 22:
                    if (checkNum()) {
                        this.num++;
                        updateNum(this.num);
                        return true;
                    } else if (this.tradeType.equals(TradeType.TAKE_OUT_ADD_CART)) {
                        switch (this.type) {
                            case 0:
                                showError(this.context.getString(R.string.take_out_sku_num_exceed_kucun));
                                return true;
                            case 1:
                                showError(String.format(this.context.getString(R.string.take_out_sku_num_exceed_limit), new Object[]{String.valueOf(this.maxNum)}));
                                return true;
                            default:
                                return true;
                        }
                    } else {
                        switch (this.type) {
                            case 0:
                                showError(this.context.getString(R.string.new_shop_sku_num_exceed_kucun));
                                return true;
                            case 1:
                                showError(this.context.getString(R.string.new_shop_sku_num_exceed_limit));
                                return true;
                            default:
                                return true;
                        }
                    }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showError(String prompt) {
        if (this.context instanceof SkuActivity) {
            ((SkuActivity) this.context).onShowError(prompt);
        } else {
            UI3Toast.makeToast(this.context, prompt).show();
        }
    }

    public void setMaxNum(int type2, int maxNum2) {
        this.type = type2;
        this.maxNum = maxNum2;
        updateArrow(isFocused());
    }

    public boolean checkNum() {
        if (this.num >= this.maxNum) {
            return false;
        }
        return true;
    }

    private void updateArrow(boolean gainFocus) {
        if (gainFocus) {
            if (this.maxNum == 0) {
                this.leftView.setImageBitmap(this.leftInvaildFocused);
                this.rightView.setImageBitmap(this.rightInvaildFocused);
            } else if (this.num == 1) {
                if (!checkNum()) {
                    this.leftView.setImageBitmap(this.leftInvaildFocused);
                    this.rightView.setImageBitmap(this.rightInvaildFocused);
                    return;
                }
                this.leftView.setImageBitmap(this.leftInvaildFocused);
                this.rightView.setImageBitmap(this.rightVaildFocused);
            } else if (!checkNum()) {
                this.leftView.setImageBitmap(this.leftVaildFocused);
                this.rightView.setImageBitmap(this.rightInvaildFocused);
            } else {
                this.leftView.setImageBitmap(this.leftVaildFocused);
                this.rightView.setImageBitmap(this.rightVaildFocused);
            }
        } else if (this.maxNum == 0) {
            this.leftView.setImageBitmap(this.leftInvaildUnfocus);
            this.rightView.setImageBitmap(this.rightInvaildUnfocus);
        } else if (this.num == 1) {
            if (!checkNum()) {
                this.leftView.setImageBitmap(this.leftInvaildUnfocus);
                this.rightView.setImageBitmap(this.rightInvaildUnfocus);
                return;
            }
            this.leftView.setImageBitmap(this.leftInvaildUnfocus);
            this.rightView.setImageBitmap(this.rightVaildUnfocus);
        } else if (!checkNum()) {
            this.leftView.setImageBitmap(this.leftVaildUnfocus);
            this.rightView.setImageBitmap(this.rightInvaildUnfocus);
        } else {
            this.leftView.setImageBitmap(this.leftVaildUnfocus);
            this.rightView.setImageBitmap(this.rightVaildUnfocus);
        }
    }

    private void updateNum(int num2) {
        this.num = num2;
        this.textView.setText(String.valueOf(num2));
        updateArrow(true);
        if (this.numChangeListener != null) {
            this.numChangeListener.onNumChange(num2);
        }
    }

    public void setBuyCount(int num2) {
        this.num = num2;
        this.textView.setText(String.valueOf(num2));
        updateArrow(isFocused());
    }

    public int getNum() {
        if (this.maxNum == 0) {
            return this.maxNum;
        }
        return this.num;
    }

    public NumChangeListener getNumChangeListener() {
        return this.numChangeListener;
    }

    public void setNumChangeListener(NumChangeListener numChangeListener2) {
        this.numChangeListener = numChangeListener2;
    }
}
