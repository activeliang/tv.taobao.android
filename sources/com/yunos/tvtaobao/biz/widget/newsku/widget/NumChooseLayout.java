package com.yunos.tvtaobao.biz.widget.newsku.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.yunos.tvtaobao.biz.widget.newsku.TradeType;
import com.yunos.tvtaobao.businessview.R;

public class NumChooseLayout extends LinearLayout {
    private TextView canBuyQuantity;
    private int kucuns = 0;
    private int limit = 0;
    private TextView manytimes;
    private TextView name;
    private NumChooseItem numChooseItem;
    private int times = 1;
    private String tradeType = TradeType.ADD_CART;

    public NumChooseLayout(Context context) {
        super(context);
        initView(context);
    }

    public NumChooseLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NumChooseLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public void setTradeType(String tradeType2) {
        this.tradeType = tradeType2;
        if (this.numChooseItem != null) {
            this.numChooseItem.setTradeType(tradeType2);
        }
    }

    private void initView(Context context) {
        setOrientation(1);
        this.name = new TextView(context);
        this.name.setTextSize(0, (float) getResources().getDimensionPixelSize(R.dimen.dp_24));
        this.name.setTextColor(Color.parseColor("#202020"));
        this.name.setText("数量 ( 左右键选择 ) ");
        addView(this.name);
        View view = LayoutInflater.from(context).inflate(R.layout.item_numchoose_layout, (ViewGroup) null);
        this.numChooseItem = (NumChooseItem) view.findViewById(R.id.item_layout_numchooseitem);
        this.manytimes = (TextView) view.findViewById(R.id.item_layout_manytimes);
        this.canBuyQuantity = (TextView) view.findViewById(R.id.item_layout_canbuyquantity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -1);
        lp.setMargins(0, (int) getResources().getDimension(R.dimen.dp_6), 0, 0);
        view.setLayoutParams(lp);
        addView(view);
    }

    public NumChooseItem getNumChooseItem() {
        return this.numChooseItem;
    }

    public int getNum() {
        return this.numChooseItem.getNum() * this.times;
    }

    public int getKucuns() {
        return this.kucuns;
    }

    public int getLimit() {
        return this.limit;
    }

    public void setBuyCount(long num) {
        if (num / ((long) this.times) > 0) {
            this.numChooseItem.setBuyCount((int) (num / ((long) this.times)));
        } else {
            this.numChooseItem.setBuyCount(1);
        }
    }

    public void showUnitBuy(int times2) {
        if (times2 > 1) {
            this.times = times2;
            this.manytimes.setVisibility(0);
            this.manytimes.setText("X " + times2 + "件");
            updateCanBuyQuantity();
        }
    }

    public void setKuCunNum(int kucun, int limit2) {
        this.kucuns = kucun;
        this.limit = limit2;
        updateCanBuyQuantity();
    }

    private void updateCanBuyQuantity() {
        this.canBuyQuantity.setText("");
        if (this.limit > 0 && this.limit < this.kucuns) {
            this.canBuyQuantity.setText("( 限购" + this.limit + "件 )");
            this.numChooseItem.setMaxNum(1, this.limit / this.times);
        }
        if ((this.kucuns > 0 && this.kucuns < this.limit) || this.limit <= 0) {
            if (!this.tradeType.equals(TradeType.TAKE_OUT_ADD_CART)) {
                this.canBuyQuantity.setText("( 库存" + this.kucuns + "件 )");
            } else if (this.kucuns <= 10) {
                this.canBuyQuantity.setText("( 仅剩" + this.kucuns + "件 )");
            }
            this.numChooseItem.setMaxNum(0, this.kucuns / this.times);
        }
    }
}
