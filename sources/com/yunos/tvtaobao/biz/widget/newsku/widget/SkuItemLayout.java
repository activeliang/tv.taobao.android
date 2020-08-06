package com.yunos.tvtaobao.biz.widget.newsku.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.tvtaobao.android.ui3.widget.UI3Toast;
import com.yunos.tvtaobao.biz.request.bo.TBDetailResultV6;
import com.yunos.tvtaobao.biz.widget.newsku.SkuItemViewStatus;
import com.yunos.tvtaobao.biz.widget.newsku.interfaces.SkuInfoUpdate;
import com.yunos.tvtaobao.biz.widget.newsku.widget.SkuItem;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.List;
import mtopsdk.common.util.SymbolExpUtil;

public class SkuItemLayout extends LinearLayout {
    private static final String TAG = "SkuItemLayout";
    /* access modifiers changed from: private */
    public Context context;
    private int[] itemReso = {R.id.sku_item_1, R.id.sku_item_2, R.id.sku_item_3};
    private String name;
    private TextView nameTxt;
    /* access modifiers changed from: private */
    public long propId;
    /* access modifiers changed from: private */
    public SkuInfoUpdate skuInfoUpdate;
    private View.OnClickListener skuItemClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (!(view instanceof SkuItem) || ((SkuItem) view).getCurrentState() != SkuItem.State.DISABLE) {
                for (int i = 0; i < SkuItemLayout.this.skuItemSize; i++) {
                    long valueId = Long.parseLong(((TBDetailResultV6.SkuBaseBean.PropsBeanX.ValuesBean) SkuItemLayout.this.valuesBeans.get(i)).getVid());
                    SkuItem skuItem = SkuItemLayout.this.getSkuItemById(Long.valueOf(SkuItemLayout.this.propId), Long.valueOf(valueId));
                    if (view == skuItem) {
                        if (skuItem.getCurrentState() == SkuItem.State.SELECT) {
                            skuItem.setState(SkuItem.State.UNSELECT);
                            SkuItemLayout.this.skuInfoUpdate.deleteSelectedPropData(SkuItemLayout.this.propId, valueId);
                        } else {
                            skuItem.setState(SkuItem.State.SELECT);
                            SkuItemLayout.this.skuInfoUpdate.addSelectedPropData(SkuItemLayout.this.propId, valueId);
                        }
                    } else if (skuItem.getCurrentState() == SkuItem.State.SELECT) {
                        skuItem.setState(SkuItem.State.UNSELECT);
                    }
                }
                return;
            }
            UI3Toast.makeToast(SkuItemLayout.this.context, SkuItemLayout.this.context.getResources().getString(R.string.new_shop_product_stockout)).show();
        }
    };
    /* access modifiers changed from: private */
    public int skuItemSize = 0;
    private LinearLayout valueLayout;
    /* access modifiers changed from: private */
    public List<TBDetailResultV6.SkuBaseBean.PropsBeanX.ValuesBean> valuesBeans;

    public SkuItemLayout(Context context2) {
        super(context2);
        initView(context2);
    }

    public SkuItemLayout(Context context2, AttributeSet attrs) {
        super(context2, attrs);
        initView(context2);
    }

    public SkuItemLayout(Context context2, AttributeSet attrs, int defStyleAttr) {
        super(context2, attrs, defStyleAttr);
        initView(context2);
    }

    private void initView(Context context2) {
        this.context = context2;
        setOrientation(1);
        this.nameTxt = new TextView(context2);
        this.nameTxt.setTextSize(0, (float) getResources().getDimensionPixelSize(R.dimen.dp_24));
        this.nameTxt.setTextColor(Color.parseColor("#202020"));
        addView(this.nameTxt);
        this.valueLayout = new LinearLayout(context2);
        this.valueLayout.setOrientation(1);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -1);
        lp.setMargins(0, (int) getResources().getDimension(R.dimen.dp_6), 0, 0);
        this.valueLayout.setLayoutParams(lp);
        addView(this.valueLayout);
    }

    public void setProps(TBDetailResultV6.SkuBaseBean.PropsBeanX props) {
        initData(props);
    }

    private void initData(TBDetailResultV6.SkuBaseBean.PropsBeanX props) {
        int line;
        this.name = props.getName();
        this.nameTxt.setText(this.name);
        this.propId = Long.parseLong(props.getPid());
        this.valuesBeans = props.getValues();
        int size = this.valuesBeans.size();
        this.skuItemSize = size;
        if (size % 3 == 0) {
            line = size / 3;
        } else {
            line = (size / 3) + 1;
        }
        for (int i = 0; i < line; i++) {
            View view = LayoutInflater.from(this.context).inflate(R.layout.item_sku_layout, (ViewGroup) null);
            if (i > 0) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -1);
                lp.setMargins(0, getResources().getDimensionPixelSize(R.dimen.dp_f2), 0, 0);
                view.setLayoutParams(lp);
            }
            int column = 3;
            if (size > 3) {
                size -= 3;
            } else {
                column = size;
            }
            for (int j = 0; j < column; j++) {
                int pos = ((i + 1) * 3) - (3 - j);
                SkuItem item = (SkuItem) view.findViewById(this.itemReso[j]);
                long valueId = Long.parseLong(this.valuesBeans.get(pos).getVid());
                item.setTag(getPropKey(this.propId, valueId));
                item.setVisibility(0);
                item.setHeight((int) getResources().getDimension(R.dimen.dp_60));
                item.setText(this.valuesBeans.get(pos).getName());
                item.setTextSize(0, (float) getResources().getDimensionPixelSize(R.dimen.dp_24));
                item.setPadding(getResources().getDimensionPixelSize(R.dimen.dp_6), 0, getResources().getDimensionPixelSize(R.dimen.dp_6), getResources().getDimensionPixelSize(R.dimen.dp_3));
                item.setFocusable(true);
                item.setOnClickListener(this.skuItemClickListener);
                item.setValueId(valueId);
                if (this.skuItemSize == 1) {
                    item.setState(SkuItem.State.SELECT);
                    this.skuInfoUpdate.addSelectedPropData(this.propId, valueId);
                }
            }
            this.valueLayout.addView(view);
        }
    }

    public void setSkuUpdateListener(SkuInfoUpdate update) {
        this.skuInfoUpdate = update;
    }

    public String getName() {
        return this.name;
    }

    public long getPropId() {
        return this.propId;
    }

    private long getSelectedValueId() {
        long valueId = 0;
        for (int i = 0; i < this.skuItemSize; i++) {
            SkuItem skuItem = getSkuItemById(Long.valueOf(this.propId), Long.valueOf(this.valuesBeans.get(i).getVid()));
            if (skuItem.getCurrentState() == SkuItem.State.SELECT) {
                valueId = skuItem.getValueId();
                ZpLogger.i(TAG, "SkuItemLayout.getPropsId valueId ——> " + valueId);
            }
        }
        return valueId;
    }

    public void updateValueViewStatus(Long propId2, Long valueId, SkuItemViewStatus status) {
        SkuItem skuItem = getSkuItemById(propId2, valueId);
        if (skuItem != null) {
            switch (status) {
                case UNKNOWN:
                case UNSELECT:
                    skuItem.setState(SkuItem.State.UNSELECT);
                    return;
                case SELECTED:
                    skuItem.setState(SkuItem.State.SELECT);
                    this.skuInfoUpdate.addSelectedPropData(propId2.longValue(), valueId.longValue());
                    return;
                case DISABLE:
                    skuItem.setState(SkuItem.State.DISABLE);
                    return;
                case ENABLE:
                    if (skuItem.getCurrentState() != SkuItem.State.SELECT) {
                        skuItem.setState(SkuItem.State.UNSELECT);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public SkuItem getSkuItemById(Long propId2, Long valueId) {
        String textViewTag = getPropKey(propId2.longValue(), valueId.longValue());
        if (TextUtils.isEmpty(textViewTag)) {
            return null;
        }
        int count = this.valueLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            if (this.valueLayout.getChildAt(i) instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) this.valueLayout.getChildAt(i);
                int counts = linearLayout.getChildCount();
                for (int j = 0; j < counts; j++) {
                    if (textViewTag.equals((String) linearLayout.getChildAt(j).getTag())) {
                        return (SkuItem) linearLayout.getChildAt(j);
                    }
                }
                continue;
            }
        }
        return null;
    }

    public SkuItem getSkuItem(int pos) {
        int p = 0;
        int count = this.valueLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            if (this.valueLayout.getChildAt(i) instanceof LinearLayout) {
                LinearLayout linearLayout = (LinearLayout) this.valueLayout.getChildAt(i);
                int counts = linearLayout.getChildCount();
                for (int j = 0; j < counts; j++) {
                    if ((linearLayout.getChildAt(j) instanceof SkuItem) && pos == p) {
                        return (SkuItem) linearLayout.getChildAt(j);
                    }
                    p++;
                }
                continue;
            }
        }
        return null;
    }

    private String getPropKey(long propId2, long valueId) {
        return propId2 + SymbolExpUtil.SYMBOL_COLON + valueId;
    }
}
