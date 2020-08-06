package com.yunos.tvtaobao.biz.widget.oldsku.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.request.bo.TBDetailResultV6;
import com.yunos.tvtaobao.biz.widget.SkuView;
import com.yunos.tvtaobao.biz.widget.oldsku.view.SkuEngine;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.List;
import mtopsdk.common.util.SymbolExpUtil;

public class SkuPropItemLayout extends RelativeLayout {
    /* access modifiers changed from: private */
    public String TAG = "SkuPropItemLayout";
    private Context mContext;
    /* access modifiers changed from: private */
    public int mCurBuyCount;
    private boolean mIsSkuView;
    private long mLimitCount;
    /* access modifiers changed from: private */
    public boolean mManualCancel;
    private int mMarginLeft;
    /* access modifiers changed from: private */
    public OnBuyCountChangedListener mOnBuyCountChangedListener;
    /* access modifiers changed from: private */
    public OnBuyCountClickedListener mOnBuyCountClickedListener;
    private OnPropViewFocusListener mOnPropViewFocusListener;
    /* access modifiers changed from: private */
    public long mPropId;
    /* access modifiers changed from: private */
    public boolean mSelectedOnFirstFocus;
    /* access modifiers changed from: private */
    public View mSelectedView;
    private LinearLayout mSkuBuyNumLayout;
    private ImageView mSkuBuyNumLeftArray;
    private ImageView mSkuBuyNumRightArray;
    /* access modifiers changed from: private */
    public TextView mSkuBuyNumTextView;
    /* access modifiers changed from: private */
    public SkuEngine mSkuEngine;
    private long mSkuKuCunNum;
    private TextView mSkuKuCunTextView;
    private TextView mSkuPropName;
    private SkuView mSkuPropView;
    private TextView mSkuUnitBuy;
    private int mUnit = 1;
    /* access modifiers changed from: private */
    public long mValueId;

    public interface OnBuyCountChangedListener {
        void OnBuyCountChanged(int i);
    }

    public interface OnBuyCountClickedListener {
        void OnBuyCountClicked();
    }

    public interface OnPropViewFocusListener {
        void OnPorpViewFocus(View view, boolean z);
    }

    public enum VALUE_VIEW_STATUS {
        UNKNOWN,
        UNSELECT,
        SELECTED,
        DISABLE,
        ENABLE
    }

    static /* synthetic */ int access$1008(SkuPropItemLayout x0) {
        int i = x0.mCurBuyCount;
        x0.mCurBuyCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$1010(SkuPropItemLayout x0) {
        int i = x0.mCurBuyCount;
        x0.mCurBuyCount = i - 1;
        return i;
    }

    public SkuPropItemLayout(Context context, long propId, boolean isSkuView, SkuEngine skuEngine) {
        super(context);
        this.mContext = context;
        this.mPropId = propId;
        this.mSkuEngine = skuEngine;
        this.mIsSkuView = isSkuView;
        this.mMarginLeft = context.getResources().getDimensionPixelSize(R.dimen.dp_60);
        this.mManualCancel = false;
        this.mCurBuyCount = 1;
        this.mLimitCount = -1;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.ytm_sku_prop_item_layout, (ViewGroup) null);
        addView(view);
        this.mSkuPropName = (TextView) view.findViewById(R.id.sku_prop_item_name);
        this.mSkuPropView = (SkuView) view.findViewById(R.id.sku_prop_item_content);
        RelativeLayout mSkuPropItemLayout = (RelativeLayout) view.findViewById(R.id.sku_prop_item_layout);
        this.mSkuBuyNumLayout = (LinearLayout) view.findViewById(R.id.sku_buy_num_layout);
        this.mSkuBuyNumLeftArray = (ImageView) view.findViewById(R.id.sku_buy_num_left_array);
        this.mSkuBuyNumRightArray = (ImageView) view.findViewById(R.id.sku_buy_num_right_array);
        this.mSkuBuyNumTextView = (TextView) view.findViewById(R.id.sku_buy_num);
        this.mSkuKuCunTextView = (TextView) view.findViewById(R.id.sku_kucun_text);
        this.mSkuUnitBuy = (TextView) view.findViewById(R.id.sku_unit_buy_text);
        if (this.mIsSkuView) {
            mSkuPropItemLayout.setVisibility(0);
            this.mSkuBuyNumLayout.setVisibility(8);
            initSkuView();
            return;
        }
        mSkuPropItemLayout.setVisibility(8);
        this.mSkuBuyNumLayout.setVisibility(0);
        initBuyNumView();
    }

    private void initSkuView() {
        this.mSkuPropView.setTag(Long.valueOf(this.mPropId));
        this.mSkuPropView.setFocusable(true);
        this.mSkuPropView.setFocusDrawable(new ColorDrawable(getResources().getColor(R.color.ytm_sku_fucus_bg)));
        this.mSkuPropView.setReferenceDistance(this.mMarginLeft);
        this.mSkuPropView.setItemSpace(getResources().getDimensionPixelSize(R.dimen.dp_28));
        this.mSkuPropView.setDefaultSelectedItem(0);
        this.mSkuPropView.setScrollDuration(500);
        this.mSkuPropView.setNextFocusLeftId(R.id.sku_prop_item_content);
        this.mSkuPropView.setNextFocusRightId(R.id.sku_prop_item_content);
        this.mSkuPropView.setOnScrollStateChangedListener(new SkuView.OnScrollStateChangedListener() {
            public void onScrollStateChanged(SkuView.ScrollState state) {
                ZpLogger.v(SkuPropItemLayout.this.TAG, SkuPropItemLayout.this.TAG + ".initSkuView.state = " + state);
                if (state == SkuView.ScrollState.IDLE && SkuPropItemLayout.this.mSelectedView != null) {
                    SkuPropItemLayout.this.mSelectedView.setBackgroundResource(R.drawable.ytm_sku_select_tag);
                    SkuPropItemLayout.this.mSkuEngine.addSelectedPropData(SkuPropItemLayout.this.mPropId, SkuPropItemLayout.this.mValueId);
                }
            }
        });
        this.mSkuPropView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (Config.isDebug()) {
                    ZpLogger.i(SkuPropItemLayout.this.TAG, SkuPropItemLayout.this.TAG + ".initSkuView.onFocusChange skuView =" + v + " hasFocus=" + hasFocus + ", mManualCancel = " + SkuPropItemLayout.this.mManualCancel);
                }
                SkuPropItemLayout.this.onLayouFocusChange(hasFocus);
                if (hasFocus) {
                    boolean unused = SkuPropItemLayout.this.mSelectedOnFirstFocus = true;
                    SkuPropItemLayout.this.resetValueViewStatus(v);
                }
            }
        });
        this.mSkuPropView.setOnSelectedItemListener(new SkuView.OnSelectedItemListener() {
            public void onSelectedItemListener(int position, View view, boolean selected) {
                if (Config.isDebug()) {
                    ZpLogger.i(SkuPropItemLayout.this.TAG, SkuPropItemLayout.this.TAG + ".initSkuView.onSelectedItemListener position =" + position + " view=" + view + " selected=" + selected + ", mSelectedOnFirstFocus = " + SkuPropItemLayout.this.mSelectedOnFirstFocus);
                }
                long unused = SkuPropItemLayout.this.mValueId = SkuPropItemLayout.this.getValueIdFromKey((String) view.getTag());
                if (selected) {
                    View unused2 = SkuPropItemLayout.this.mSelectedView = view;
                    if (SkuPropItemLayout.this.mSelectedOnFirstFocus) {
                        SkuPropItemLayout.this.mSelectedView.setBackgroundResource(R.drawable.ytm_sku_select_tag);
                        SkuPropItemLayout.this.mSkuEngine.addSelectedPropData(SkuPropItemLayout.this.mPropId, SkuPropItemLayout.this.mValueId);
                        boolean unused3 = SkuPropItemLayout.this.mSelectedOnFirstFocus = false;
                    }
                } else if (!SkuPropItemLayout.this.mManualCancel) {
                    SkuPropItemLayout.this.updateValueViewStatus(Long.valueOf(SkuPropItemLayout.this.mPropId), Long.valueOf(SkuPropItemLayout.this.mValueId), VALUE_VIEW_STATUS.SELECTED);
                } else {
                    SkuPropItemLayout.this.mSelectedView.setBackgroundDrawable((Drawable) null);
                    View unused4 = SkuPropItemLayout.this.mSelectedView = null;
                }
                boolean unused5 = SkuPropItemLayout.this.mManualCancel = false;
            }
        });
    }

    private void initBuyNumView() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.mSkuBuyNumLayout.getLayoutParams();
        if (params == null) {
            params = new RelativeLayout.LayoutParams(-2, -2);
            params.leftMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.dp_70);
        }
        params.leftMargin += this.mMarginLeft;
        this.mSkuBuyNumLayout.setLayoutParams(params);
        this.mSkuBuyNumLayout.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean buyCountChanged = false;
                if (event.getAction() == 0) {
                    long canBuyCount = SkuPropItemLayout.this.getCanBuyCount();
                    if (keyCode == 22) {
                        if (((long) SkuPropItemLayout.this.mCurBuyCount) < canBuyCount) {
                            SkuPropItemLayout.access$1008(SkuPropItemLayout.this);
                            buyCountChanged = true;
                        }
                    } else if (keyCode == 21) {
                        if (SkuPropItemLayout.this.mCurBuyCount > 1) {
                            SkuPropItemLayout.access$1010(SkuPropItemLayout.this);
                            buyCountChanged = true;
                        }
                    } else if (keyCode == 23 && SkuPropItemLayout.this.mOnBuyCountClickedListener != null) {
                        SkuPropItemLayout.this.mOnBuyCountClickedListener.OnBuyCountClicked();
                    }
                }
                if (!buyCountChanged) {
                    return false;
                }
                if (SkuPropItemLayout.this.mOnBuyCountChangedListener != null) {
                    SkuPropItemLayout.this.mOnBuyCountChangedListener.OnBuyCountChanged(SkuPropItemLayout.this.mCurBuyCount);
                }
                SkuPropItemLayout.this.setBuyNumLayout();
                return false;
            }
        });
        this.mSkuBuyNumLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                SkuPropItemLayout.this.onLayouFocusChange(hasFocus);
                if (hasFocus) {
                    SkuPropItemLayout.this.mSkuBuyNumTextView.setBackgroundColor(SkuPropItemLayout.this.getResources().getColor(R.color.ytm_sku_fucus_bg));
                    SkuPropItemLayout.this.mSkuBuyNumTextView.setTextColor(SkuPropItemLayout.this.getResources().getColor(17170443));
                    return;
                }
                SkuPropItemLayout.this.mSkuBuyNumTextView.setBackgroundColor(SkuPropItemLayout.this.getResources().getColor(17170445));
                SkuPropItemLayout.this.mSkuBuyNumTextView.setTextColor(SkuPropItemLayout.this.getResources().getColor(R.color.ytm_sku_fucus_bg));
            }
        });
    }

    /* access modifiers changed from: private */
    public void onLayouFocusChange(boolean focus) {
        if (this.mOnPropViewFocusListener != null) {
            this.mOnPropViewFocusListener.OnPorpViewFocus(this, focus);
        }
    }

    /* access modifiers changed from: private */
    public void setBuyNumLayout() {
        long canBuyCount = getCanBuyCount();
        if (this.mCurBuyCount <= 1) {
            this.mSkuBuyNumLeftArray.setImageResource(R.drawable.ytm_sku_buy_num_left_disable);
        } else {
            this.mSkuBuyNumLeftArray.setImageResource(R.drawable.ytm_sku_buy_num_left_normal);
        }
        if (((long) this.mCurBuyCount) >= canBuyCount) {
            this.mSkuBuyNumRightArray.setImageResource(R.drawable.ytm_sku_buy_num_right_disable);
        } else {
            this.mSkuBuyNumRightArray.setImageResource(R.drawable.ytm_sku_buy_num_right_normal);
        }
        this.mSkuBuyNumTextView.setText(String.valueOf(this.mCurBuyCount));
    }

    public void setKuCunNum(long num, long limitCount) {
        this.mSkuKuCunNum = num;
        this.mLimitCount = limitCount;
        String text = String.format(getResources().getString(R.string.ytm_sku_kucun_text), new Object[]{Long.valueOf(num)});
        if (this.mLimitCount > 0 && this.mLimitCount <= this.mSkuKuCunNum) {
            ZpLogger.e(this.TAG, "商品存在限购，且<=库存，去显示限购信息");
            text = (text + " ") + String.format(getResources().getString(R.string.ytm_sku_limit_count_text), new Object[]{Long.valueOf(this.mLimitCount)});
        }
        this.mSkuKuCunTextView.setText(text);
        if (Config.isDebug()) {
            ZpLogger.v(this.TAG, this.TAG + ".setKuCunNum.mSkuKuCunNum = " + this.mSkuKuCunNum + ".mLimitCount = " + this.mLimitCount);
        }
    }

    public void setUnitBuy(int unit) {
        ZpLogger.v(this.TAG, this.TAG + ".setUnitBuy.unit = " + unit);
        this.mUnit = unit;
        String text = String.format(getResources().getString(R.string.ytm_sku_unit_buy_text), new Object[]{Integer.valueOf(this.mUnit)});
        this.mSkuUnitBuy.setVisibility(0);
        this.mSkuUnitBuy.setText(text);
    }

    public void setCurBuyCount(int count) {
        if (Config.isDebug()) {
            ZpLogger.v(this.TAG, this.TAG + ".setCurBuyCount.count = " + count + ", mSkuKuCunNum = " + this.mSkuKuCunNum);
        }
        long canBuyCount = getCanBuyCount();
        if (count <= 0) {
            this.mCurBuyCount = 1;
        } else if (((long) count) >= canBuyCount) {
            this.mCurBuyCount = (int) canBuyCount;
        } else {
            this.mCurBuyCount = count;
        }
        setBuyNumLayout();
        if (this.mOnBuyCountChangedListener != null) {
            this.mOnBuyCountChangedListener.OnBuyCountChanged(this.mCurBuyCount);
        }
    }

    /* access modifiers changed from: private */
    public long getCanBuyCount() {
        long canBuyCount;
        if (this.mLimitCount > 0) {
            canBuyCount = this.mLimitCount < this.mSkuKuCunNum ? this.mLimitCount : this.mSkuKuCunNum;
        } else {
            canBuyCount = this.mSkuKuCunNum;
        }
        if (this.mUnit > 1) {
            canBuyCount /= (long) this.mUnit;
        }
        ZpLogger.i(this.TAG, this.TAG + ".getCanBuyCount canBuyCount = " + canBuyCount);
        return canBuyCount;
    }

    public int getCurBuyNum() {
        return this.mCurBuyCount;
    }

    public void setSkuPropName(String text) {
        this.mSkuPropName.setText(text);
    }

    public void setSkuPropView(List<TBDetailResultV6.SkuBaseBean.PropsBeanX.ValuesBean> values) {
        if (values != null && values.size() != 0) {
            int size = values.size();
            for (int i = 0; i < size; i++) {
                TextView textView = new TextView(this.mContext);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -1);
                textView.setBackgroundColor(getResources().getColor(17170445));
                textView.setTextColor(getResources().getColorStateList(17170443));
                textView.setText(values.get(i).getName());
                textView.setTextSize(0, (float) getResources().getDimensionPixelSize(R.dimen.sp_20));
                textView.setMinWidth(getResources().getDimensionPixelSize(R.dimen.dp_84));
                textView.setMaxWidth(getResources().getDimensionPixelSize(R.dimen.dp_160));
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.setSingleLine();
                textView.setMarqueeRepeatLimit(1);
                textView.setGravity(17);
                textView.setIncludeFontPadding(false);
                textView.setPadding(5, 0, 5, 0);
                Long valueId = Long.valueOf(Long.parseLong(values.get(i).getVid()));
                textView.setTag(this.mSkuEngine.getPropKey(this.mPropId, valueId.longValue()));
                textView.setFocusable(false);
                this.mSkuPropView.addView(textView, params);
                SkuEngine.PropData propData = new SkuEngine.PropData();
                propData.propKey = this.mSkuEngine.getPropKey(this.mPropId, valueId.longValue());
                propData.propId = this.mPropId;
                propData.valueId = valueId.longValue();
                if (values.get(i).getImage() != null) {
                    propData.imageUrl = values.get(i).getImage();
                }
                this.mSkuEngine.addPropData(propData);
                textView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        SkuEngine.PropData data;
                        ZpLogger.v(SkuPropItemLayout.this.TAG, SkuPropItemLayout.this.TAG + ".setOnClickListener v = " + v);
                        View unused = SkuPropItemLayout.this.mSelectedView = v;
                        long unused2 = SkuPropItemLayout.this.mValueId = SkuPropItemLayout.this.getValueIdFromKey((String) v.getTag());
                        if (SkuPropItemLayout.this.mValueId > -1 && (data = SkuPropItemLayout.this.mSkuEngine.getPropDataFromList(SkuPropItemLayout.this.mPropId, SkuPropItemLayout.this.mValueId)) != null) {
                            if (data.selected) {
                                SkuPropItemLayout.this.mSelectedView.setBackgroundDrawable((Drawable) null);
                                SkuPropItemLayout.this.mSkuEngine.deleteSelectedPropData(SkuPropItemLayout.this.mPropId);
                                boolean unused3 = SkuPropItemLayout.this.mManualCancel = true;
                                return;
                            }
                            SkuPropItemLayout.this.mSelectedView.setBackgroundResource(R.drawable.ytm_sku_select_tag);
                            SkuPropItemLayout.this.mSkuEngine.addSelectedPropData(SkuPropItemLayout.this.mPropId, SkuPropItemLayout.this.mValueId);
                            boolean unused4 = SkuPropItemLayout.this.mManualCancel = false;
                        }
                    }
                });
            }
        }
    }

    public void setDefaultSelectSku(long propId, long defaultValueId) {
        if (this.mSkuPropView != null && this.mSkuPropView.getChildCount() > 0) {
            String textViewTag = this.mSkuEngine.getPropKey(propId, defaultValueId);
            if (!TextUtils.isEmpty(textViewTag)) {
                int index = -1;
                int i = 0;
                while (true) {
                    if (i >= this.mSkuPropView.getChildCount()) {
                        break;
                    } else if (textViewTag.equals((String) this.mSkuPropView.getChildAt(i).getTag())) {
                        index = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (index >= 0) {
                    this.mSkuPropView.setDefaultSelectedItem(index);
                    updateValueViewStatus(Long.valueOf(this.mPropId), Long.valueOf(defaultValueId), VALUE_VIEW_STATUS.SELECTED);
                }
            }
        }
    }

    public void updateValueViewStatus(Long propId, Long valueId, VALUE_VIEW_STATUS status) {
        TextView textView;
        SkuEngine.PropData propData = this.mSkuEngine.getPropDataFromList(propId.longValue(), valueId.longValue());
        if (propData != null && (textView = getViewFromId(propId, valueId)) != null) {
            if (Config.isDebug()) {
                ZpLogger.v(this.TAG, this.TAG + ".updateValueViewStatus.propData = " + propData + ", status = " + status);
            }
            switch (status) {
                case UNSELECT:
                    textView.setEnabled(true);
                    textView.setGravity(17);
                    textView.setTextColor(getResources().getColor(17170443));
                    textView.setBackgroundDrawable((Drawable) null);
                    return;
                case SELECTED:
                    textView.setEnabled(true);
                    textView.setGravity(17);
                    textView.setTextColor(getResources().getColor(17170443));
                    textView.setBackgroundResource(R.drawable.ytm_sku_prop_selected);
                    this.mSkuEngine.addSelectedPropData(propId.longValue(), valueId.longValue());
                    return;
                case DISABLE:
                    textView.setEnabled(false);
                    textView.setGravity(17);
                    textView.setTextColor(getResources().getColor(R.color.ytm_sku_text_disable));
                    textView.setBackgroundDrawable((Drawable) null);
                    return;
                case ENABLE:
                    textView.setEnabled(true);
                    textView.setGravity(17);
                    textView.setTextColor(getResources().getColor(17170443));
                    textView.setBackgroundDrawable((Drawable) null);
                    return;
                default:
                    return;
            }
        }
    }

    public void resetValueViewStatus(View v) {
        if (this.mSkuPropView != null && v != null) {
            for (int i = 0; i < this.mSkuPropView.getChildCount(); i++) {
                TextView textView = (TextView) this.mSkuPropView.getChildAt(i);
                String tag = (String) textView.getTag();
                if (!TextUtils.isEmpty(tag)) {
                    SkuEngine.PropData data = null;
                    String[] str = tag.split(SymbolExpUtil.SYMBOL_COLON);
                    if (str != null && str.length == 2) {
                        try {
                            data = this.mSkuEngine.getPropDataFromList(Long.valueOf(str[0]).longValue(), Long.valueOf(str[1]).longValue());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    if (data != null) {
                        if (data.enable) {
                            textView.setTextColor(getResources().getColor(17170443));
                        }
                        if (data.selected) {
                            textView.setBackgroundColor(getResources().getColor(17170445));
                        }
                    }
                }
            }
        }
    }

    public long getPropId() {
        return this.mPropId;
    }

    public TextView getViewFromId(Long propId, Long valueId) {
        if (Config.isDebug()) {
            ZpLogger.v(this.TAG, this.TAG + ".getViewFromId.propId = " + propId + ". valueId = " + valueId);
        }
        if (this.mSkuPropView == null || this.mSkuPropView.getChildCount() <= 0) {
            return null;
        }
        String textViewTag = this.mSkuEngine.getPropKey(propId.longValue(), valueId.longValue());
        if (TextUtils.isEmpty(textViewTag)) {
            return null;
        }
        for (int i = 0; i < this.mSkuPropView.getChildCount(); i++) {
            if (textViewTag.equals((String) this.mSkuPropView.getChildAt(i).getTag())) {
                return (TextView) this.mSkuPropView.getChildAt(i);
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public long getValueIdFromKey(String key) {
        String[] valueArray;
        ZpLogger.v(this.TAG, this.TAG + ". getValueIdFromKey key = " + key);
        if (TextUtils.isEmpty(key) || (valueArray = key.split(SymbolExpUtil.SYMBOL_COLON)) == null || valueArray.length != 2) {
            return -1;
        }
        try {
            return Long.parseLong(valueArray[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void onDestroy() {
        if (this.mSkuPropView != null) {
            this.mSkuPropView.removeAllViews();
            this.mSkuPropView = null;
        }
    }

    public void setOnPropViewFocusListener(OnPropViewFocusListener l) {
        this.mOnPropViewFocusListener = l;
    }

    public void setOnBuyCountChangedListener(OnBuyCountChangedListener l) {
        this.mOnBuyCountChangedListener = l;
    }

    public void setOnBuyCountClickedListener(OnBuyCountClickedListener l) {
        this.mOnBuyCountClickedListener = l;
    }
}
