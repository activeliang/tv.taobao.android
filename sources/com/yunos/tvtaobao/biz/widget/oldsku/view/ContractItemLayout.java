package com.yunos.tvtaobao.biz.widget.oldsku.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yunos.tv.core.config.Config;
import com.yunos.tvtaobao.biz.request.bo.TBDetailResultV6;
import com.yunos.tvtaobao.biz.widget.SkuView;
import com.yunos.tvtaobao.biz.widget.oldsku.view.SkuPropItemLayout;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.List;

public class ContractItemLayout extends RelativeLayout {
    /* access modifiers changed from: private */
    public String TAG = "ContractItemLayout";
    private Context mContext;
    private int mMarginLeft;
    private SkuPropItemLayout.OnPropViewFocusListener mOnPropViewFocusListener;
    /* access modifiers changed from: private */
    public boolean mSelectedOnFirstFocus;
    /* access modifiers changed from: private */
    public View mSelectedView;
    private TextView mSkuPropName;
    /* access modifiers changed from: private */
    public SkuView mSkuPropView;
    /* access modifiers changed from: private */
    public boolean manualCancel = false;
    /* access modifiers changed from: private */
    public TBDetailResultV6.ContractData.VersionData selectedVersionData;

    public enum VALUE_VIEW_STATUS {
        UNKNOWN,
        UNSELECT,
        SELECTED,
        DISABLE,
        ENABLE
    }

    public ContractItemLayout(Context context) {
        super(context);
        this.mContext = context;
        this.mMarginLeft = (int) context.getResources().getDimension(R.dimen.dp_60);
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.trade_sku_contract_item_layout, (ViewGroup) null);
        addView(view);
        this.mSkuPropName = (TextView) view.findViewById(R.id.sku_prop_item_name);
        this.mSkuPropView = (SkuView) view.findViewById(R.id.sku_prop_item_content);
        ((RelativeLayout) view.findViewById(R.id.sku_prop_item_layout)).setVisibility(0);
        initSkuView();
    }

    private void initSkuView() {
        this.mSkuPropView.setFocusable(true);
        this.mSkuPropView.setFocusDrawable(new ColorDrawable(getResources().getColor(R.color.ytm_sku_fucus_bg)));
        this.mSkuPropView.setReferenceDistance(this.mMarginLeft);
        this.mSkuPropView.setItemSpace((int) getResources().getDimension(R.dimen.dp_28));
        this.mSkuPropView.setDefaultSelectedItem(0);
        this.mSkuPropView.setScrollDuration(500);
        this.mSkuPropView.setNextFocusLeftId(R.id.sku_prop_item_content);
        this.mSkuPropView.setNextFocusRightId(R.id.sku_prop_item_content);
        this.mSkuPropView.setOnScrollStateChangedListener(new SkuView.OnScrollStateChangedListener() {
            public void onScrollStateChanged(SkuView.ScrollState state) {
                ZpLogger.v(ContractItemLayout.this.TAG, ContractItemLayout.this.TAG + ".initSkuView.state = " + state);
                if (state == SkuView.ScrollState.IDLE) {
                    for (int i = 0; i < ContractItemLayout.this.mSkuPropView.getChildCount(); i++) {
                        View child = ContractItemLayout.this.mSkuPropView.getChildAt(i);
                        if (child != ContractItemLayout.this.mSelectedView) {
                            child.setBackgroundDrawable((Drawable) null);
                        } else {
                            child.setBackgroundResource(R.drawable.ytm_sku_select_tag);
                        }
                    }
                }
            }
        });
        this.mSkuPropView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                ContractItemLayout.this.onLayouFocusChange(hasFocus);
                if (hasFocus) {
                    boolean unused = ContractItemLayout.this.mSelectedOnFirstFocus = true;
                }
            }
        });
        this.mSkuPropView.setOnSelectedItemListener(new SkuView.OnSelectedItemListener() {
            public void onSelectedItemListener(int position, View view, boolean selected) {
                if (Config.isDebug()) {
                    ZpLogger.i(ContractItemLayout.this.TAG, ContractItemLayout.this.TAG + ".initSkuView.onSelectedItemListener position =" + position + " view=" + view + " selected=" + selected + ", mSelectedOnFirstFocus = " + ContractItemLayout.this.mSelectedOnFirstFocus);
                }
                if (selected) {
                    View unused = ContractItemLayout.this.mSelectedView = view;
                    TBDetailResultV6.ContractData.VersionData unused2 = ContractItemLayout.this.selectedVersionData = (TBDetailResultV6.ContractData.VersionData) view.getTag();
                    if (ContractItemLayout.this.mSelectedOnFirstFocus) {
                        ContractItemLayout.this.mSelectedView.setBackgroundResource(R.drawable.ytm_sku_select_tag);
                        boolean unused3 = ContractItemLayout.this.mSelectedOnFirstFocus = false;
                    }
                } else if (!ContractItemLayout.this.manualCancel) {
                    ContractItemLayout.this.updateValueViewStatus(view, VALUE_VIEW_STATUS.SELECTED);
                } else {
                    ContractItemLayout.this.mSelectedView.setBackgroundDrawable((Drawable) null);
                    View unused4 = ContractItemLayout.this.mSelectedView = null;
                }
                boolean unused5 = ContractItemLayout.this.manualCancel = false;
            }
        });
    }

    public void updateValueViewStatus(View view, VALUE_VIEW_STATUS status) {
        if (view != null && (view instanceof TextView)) {
            TextView textView = (TextView) view;
            switch (status) {
                case UNSELECT:
                    textView.setEnabled(true);
                    textView.setTextColor(getResources().getColor(17170443));
                    textView.setBackgroundDrawable((Drawable) null);
                    return;
                case SELECTED:
                    textView.setEnabled(true);
                    textView.setTextColor(getResources().getColor(17170443));
                    textView.setBackgroundResource(R.drawable.ytm_sku_prop_selected);
                    return;
                case DISABLE:
                    textView.setEnabled(false);
                    textView.setTextColor(getResources().getColor(R.color.ytm_sku_text_disable));
                    textView.setBackgroundDrawable((Drawable) null);
                    return;
                case ENABLE:
                    textView.setEnabled(true);
                    textView.setTextColor(getResources().getColor(17170443));
                    textView.setBackgroundDrawable((Drawable) null);
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void onLayouFocusChange(boolean focus) {
        if (this.mOnPropViewFocusListener != null) {
            this.mOnPropViewFocusListener.OnPorpViewFocus(this, focus);
        }
    }

    public void setSkuPropName(String text) {
        this.mSkuPropName.setText(text);
    }

    public void setSkuPropView(List<TBDetailResultV6.ContractData.VersionData> values) {
        if (values != null && values.size() != 0) {
            int size = values.size();
            for (int i = 0; i < size; i++) {
                TextView textView = new TextView(this.mContext);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(-2, -1);
                textView.setBackgroundColor(getResources().getColor(17170445));
                textView.setTextColor(getResources().getColorStateList(17170443));
                textView.setText(values.get(i).versionName);
                textView.setTextSize(0, (float) ((int) getResources().getDimension(R.dimen.sp_20)));
                textView.setMinWidth((int) getResources().getDimension(R.dimen.dp_84));
                textView.setMaxWidth((int) getResources().getDimension(R.dimen.dp_160));
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.setSingleLine();
                textView.setTag(values.get(i));
                textView.setMarqueeRepeatLimit(1);
                textView.setGravity(17);
                textView.setIncludeFontPadding(false);
                textView.setPadding(5, 0, 5, 0);
                textView.setFocusable(false);
                this.mSkuPropView.addView(textView, params);
                textView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ZpLogger.v(ContractItemLayout.this.TAG, ContractItemLayout.this.TAG + ".setOnClickListener v = " + v);
                        View unused = ContractItemLayout.this.mSelectedView = v;
                        TBDetailResultV6.ContractData.VersionData data = (TBDetailResultV6.ContractData.VersionData) ContractItemLayout.this.mSelectedView.getTag();
                        if (data == ContractItemLayout.this.selectedVersionData) {
                            boolean unused2 = ContractItemLayout.this.manualCancel = true;
                            ContractItemLayout.this.mSelectedView.setBackgroundDrawable((Drawable) null);
                            TBDetailResultV6.ContractData.VersionData unused3 = ContractItemLayout.this.selectedVersionData = null;
                            return;
                        }
                        ContractItemLayout.this.mSelectedView.setBackgroundResource(R.drawable.ytm_sku_select_tag);
                        TBDetailResultV6.ContractData.VersionData unused4 = ContractItemLayout.this.selectedVersionData = data;
                        boolean unused5 = ContractItemLayout.this.manualCancel = false;
                    }
                });
            }
        }
    }

    public TBDetailResultV6.ContractData.VersionData getSelectedVersionData() {
        return this.selectedVersionData;
    }

    public void onDestroy() {
        if (this.mSkuPropView != null) {
            this.mSkuPropView.removeAllViews();
            this.mSkuPropView = null;
        }
    }

    public void setOnPropViewFocusListener(SkuPropItemLayout.OnPropViewFocusListener l) {
        this.mOnPropViewFocusListener = l;
    }
}
