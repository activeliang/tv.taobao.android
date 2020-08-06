package com.yunos.tvtaobao.biz.widget.oldsku;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import anetwork.channel.util.RequestConstant;
import com.alibaba.fastjson.JSON;
import com.taobao.statistic.CT;
import com.taobao.statistic.TBS;
import com.tvlife.imageloader.core.DisplayImageOptions;
import com.tvlife.imageloader.core.display.RoundedBitmapDisplayer;
import com.tvlife.imageloader.core.listener.SimpleImageLoadingListener;
import com.tvtaobao.voicesdk.bean.DomainResultVo;
import com.tvtaobao.voicesdk.bean.PageReturn;
import com.tvtaobao.voicesdk.register.type.ActionType;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.aqm.ActGloballyUnique;
import com.yunos.tv.core.common.ImageLoaderManager;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.SPMConfig;
import com.yunos.tv.core.config.SystemConfig;
import com.yunos.tv.core.config.TvOptionsConfig;
import com.yunos.tv.core.util.ActivityPathRecorder;
import com.yunos.tv.core.util.BitMapUtil;
import com.yunos.tv.core.util.NetWorkUtil;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.TradeBaseActivity;
import com.yunos.tvtaobao.biz.activity.BaseActivity;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.listener.BizRequestListener;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.bo.BuildOrderPreSale;
import com.yunos.tvtaobao.biz.request.bo.BuildOrderRequestBo;
import com.yunos.tvtaobao.biz.request.bo.CouponReceiveParamsBean;
import com.yunos.tvtaobao.biz.request.bo.JoinGroupResult;
import com.yunos.tvtaobao.biz.request.bo.MockData;
import com.yunos.tvtaobao.biz.request.bo.ProductTagBo;
import com.yunos.tvtaobao.biz.request.bo.SearchResult;
import com.yunos.tvtaobao.biz.request.bo.SkuPriceNum;
import com.yunos.tvtaobao.biz.request.bo.TBDetailResultV6;
import com.yunos.tvtaobao.biz.request.bo.Unit;
import com.yunos.tvtaobao.biz.request.core.ServiceCode;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import com.yunos.tvtaobao.biz.request.utils.DetailV6Utils;
import com.yunos.tvtaobao.biz.widget.newsku.TradeType;
import com.yunos.tvtaobao.biz.widget.oldsku.config.IResConfig;
import com.yunos.tvtaobao.biz.widget.oldsku.config.TaobaoResConfig;
import com.yunos.tvtaobao.biz.widget.oldsku.config.TmallResConfig;
import com.yunos.tvtaobao.biz.widget.oldsku.view.ContractItemLayout;
import com.yunos.tvtaobao.biz.widget.oldsku.view.SkuEngine;
import com.yunos.tvtaobao.biz.widget.oldsku.view.SkuPropItemLayout;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@ActGloballyUnique
public class SkuActivity extends TradeBaseActivity {
    public static int SHOP_CART_ACTIVITY_FOR_RESULT_CODE = 1;
    /* access modifiers changed from: private */
    public static String TAG = SkuActivity.class.getSimpleName();
    private BuildOrderPreSale buildOrderPreSale;
    /* access modifiers changed from: private */
    public SkuPropItemLayout buyCountView;
    private String cartFrom;
    /* access modifiers changed from: private */
    public String extParams;
    private boolean isPreSale;
    private boolean isSeckKill;
    private boolean isZTC;
    private Animation mAnimationIn;
    private Animation mAnimationOut;
    /* access modifiers changed from: private */
    public BusinessRequest mBusinessRequest;
    /* access modifiers changed from: private */
    public int mBuyCount;
    private int mBuyNumViewPropId = -2147483647;
    /* access modifiers changed from: private */
    public String mCurPrice;
    private ImageLoaderManager mImageLoaderManager;
    /* access modifiers changed from: private */
    public String mImageUrl;
    private boolean mIsJuhuasuan;
    /* access modifiers changed from: private */
    public String mItemId;
    private long mLimitCount;
    /* access modifiers changed from: private */
    public boolean mNeedShowMask;
    private SkuPropItemLayout.OnPropViewFocusListener mOnPropViewFocusListener = new SkuPropItemLayout.OnPropViewFocusListener() {
        public void OnPorpViewFocus(View view, boolean focus) {
            if (SkuActivity.this.mViewHolder != null && SkuActivity.this.mNeedShowMask && focus && view != null) {
                int index = 0;
                int count = SkuActivity.this.mViewHolder.mSkuPropLayout.getChildCount();
                int i = 0;
                while (true) {
                    if (i >= count) {
                        break;
                    } else if (view == SkuActivity.this.mViewHolder.mSkuPropLayout.getChildAt(i)) {
                        index = i;
                        break;
                    } else {
                        i++;
                    }
                }
                if (index == 0) {
                    SkuActivity.this.mViewHolder.mSkuPropTopMask.setVisibility(8);
                    SkuActivity.this.mViewHolder.mSkuPropBottomMask.setVisibility(0);
                } else if (index != 0 && index < count - SkuActivity.this.mShowMaskSkuNum) {
                    SkuActivity.this.mViewHolder.mSkuPropBottomMask.setVisibility(0);
                } else if (index >= SkuActivity.this.mShowMaskSkuNum && index != count - 1) {
                    SkuActivity.this.mViewHolder.mSkuPropTopMask.setVisibility(0);
                } else if (index == count - 1) {
                    SkuActivity.this.mViewHolder.mSkuPropTopMask.setVisibility(0);
                    SkuActivity.this.mViewHolder.mSkuPropBottomMask.setVisibility(8);
                }
            }
        }
    };
    private SkuEngine.OnSkuPropLayoutListener mOnSkuPropLayoutListener = new SkuEngine.OnSkuPropLayoutListener() {
        public void updateSkuProp(long propId, long valueId, SkuPropItemLayout.VALUE_VIEW_STATUS status) {
            SkuPropItemLayout layout = SkuActivity.this.getPropLayoutById(propId);
            if (layout != null) {
                layout.updateValueViewStatus(Long.valueOf(propId), Long.valueOf(valueId), status);
            }
            SkuActivity.this.showQRCode(false);
        }

        public void updateSkuKuCunAndrPrice(String skuId) {
            SkuActivity.this.updateSkuKuCunAndrPrice(skuId);
            if (SkuActivity.this.buyCountView != null) {
                SkuActivity.this.buyCountView.setCurBuyCount(1);
                ZpLogger.e(SkuActivity.TAG, "更新数量");
            }
        }

        public void updateItemImage(SkuEngine.PropData propData) {
            if (propData != null && !TextUtils.isEmpty(propData.imageUrl)) {
                SkuActivity.this.setItemImage(propData.imageUrl);
            }
        }
    };
    private ProductTagBo mProductTagBo;
    /* access modifiers changed from: private */
    public int mShowMaskSkuNum = 4;
    /* access modifiers changed from: private */
    public boolean mSku;
    /* access modifiers changed from: private */
    public SkuEngine mSkuEngine;
    /* access modifiers changed from: private */
    public String mSkuId;
    private String mSubPrice;
    /* access modifiers changed from: private */
    public String mType;
    /* access modifiers changed from: private */
    public ViewHolder mViewHolder;
    private DisplayImageOptions options;
    private IResConfig resConfig;
    private String source;
    private String tagId;
    /* access modifiers changed from: private */
    public TBDetailResultV6 tbDetailResultV6;
    private String tvtao_extra;
    /* access modifiers changed from: private */
    public int unitBuy = 1;
    private String uriPrice;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        JSONObject object;
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            finish();
            return;
        }
        initparameter();
        this.mType = bundle.getString("type");
        this.cartFrom = bundle.getString("cartFrom");
        ZpLogger.e(TAG, "进入页面的类型" + this.mType);
        if (TextUtils.isEmpty(this.mType)) {
            this.mType = TradeType.BUY;
        }
        this.mBuyCount = (int) bundle.getLong("buyCount", 1);
        this.mSkuId = bundle.getString("skuId");
        this.extParams = getIntent().getStringExtra("extParams");
        this.tbDetailResultV6 = (TBDetailResultV6) bundle.get("mTBDetailResultVO");
        this.mProductTagBo = (ProductTagBo) bundle.get("mProductTagBo");
        this.isZTC = bundle.getBoolean(BaseConfig.INTENT_KEY_ISZTC, false);
        this.source = bundle.getString(BaseConfig.INTENT_KEY_SOURCE);
        String itemId = bundle.getString("itemId");
        this.uriPrice = getIntent().getStringExtra("price");
        this.tvtao_extra = getIntent().getStringExtra("tvtao_extra");
        try {
            if (this.mProductTagBo != null) {
                if (this.extParams == null) {
                    object = new JSONObject();
                } else {
                    object = new JSONObject(this.extParams);
                }
                JSONArray array = new JSONArray();
                JSONObject obj = new JSONObject();
                obj.put("itemId", this.mProductTagBo.getItemId());
                obj.put("lastTraceKeyword", this.mProductTagBo.getLastTraceKeyword());
                obj.put(BaseConfig.INTENT_KEY_ISZTC, this.isZTC);
                if (this.source != null) {
                    obj.put(BaseConfig.INTENT_KEY_SOURCE, this.source);
                }
                if (this.mProductTagBo.getOutPreferentialId() != null) {
                    obj.put("outPreferentialId", this.mProductTagBo.getOutPreferentialId());
                }
                if (this.mProductTagBo.getPointSchemeId() != null) {
                    obj.put("pointSchemeId", this.mProductTagBo.getPointSchemeId());
                }
                if (this.mProductTagBo.getCoupon() != null) {
                    obj.put("couponAmount", this.mProductTagBo.getCoupon());
                }
                if (this.mProductTagBo.getCart() != null) {
                    obj.put("cart", this.mProductTagBo.getCart());
                }
                if (this.mProductTagBo.getCouponType() != null) {
                    obj.put("couponType", this.mProductTagBo.getCouponType());
                }
                if (this.mProductTagBo.getTagId() != null) {
                    obj.put("tagId", this.mProductTagBo.getTagId());
                    this.tagId = this.mProductTagBo.getTagId();
                }
                array.put(obj);
                JSONObject subOrders = new JSONObject();
                subOrders.put("subOrders", array);
                object.put("tvtaoExtra", buildAddBagExParams());
                object.put("TvTaoEx", subOrders);
                this.extParams = object.toString();
            } else if (!TextUtils.isEmpty(this.tvtao_extra)) {
                JSONObject object2 = new JSONObject();
                object2.put("tvtaoExtra", this.tvtao_extra);
                this.extParams = object2.toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Config.isDebug()) {
            ZpLogger.v(TAG, TAG + ".onCreate.mTBDetailResultVO = " + this.tbDetailResultV6 + ", itemId = " + itemId + ", mSkuId = " + this.mSkuId + ", mBuyCount = " + this.mBuyCount + ", extParams = " + this.extParams + ", mType = " + this.mType);
        }
        this.isPreSale = false;
        if (this.tbDetailResultV6 != null) {
            initView();
        } else if (!TextUtils.isEmpty(itemId)) {
            try {
                ZpLogger.e(TAG, "从编辑进来 itemid= " + itemId);
                requestGoodsDetail(Long.parseLong(itemId));
            } catch (Exception e2) {
                ZpLogger.e(TAG, TAG + "." + e2.getMessage());
                finish();
            }
        } else {
            ZpLogger.e(TAG, TAG + ".onCreate.params error");
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        ZpLogger.v(TAG, TAG + ".onResume, loginIsCanceled = " + loginIsCanceled());
        if (loginIsCanceled()) {
            showQRCode(true);
        } else {
            showQRCode(false);
        }
    }

    /* access modifiers changed from: protected */
    public PageReturn onVoiceAction(DomainResultVo object) {
        PageReturn pageReturn = new PageReturn();
        String intent = object.getIntent();
        char c = 65535;
        switch (intent.hashCode()) {
            case -1050206118:
                if (intent.equals(ActionType.SETTLE_ACCOUNTS)) {
                    c = 1;
                    break;
                }
                break;
            case -611891101:
                if (intent.equals(ActionType.CONTINUE_TO_VISITING)) {
                    c = 2;
                    break;
                }
                break;
            case 951117504:
                if (intent.equals(ActionType.CONFIRM)) {
                    c = 0;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                this.mViewHolder.mSkuDoneTextView.performClick();
                pageReturn.isHandler = true;
                pageReturn.feedback = "进入下一步";
                break;
            case 1:
                gotoShopCart();
                pageReturn.isHandler = true;
                pageReturn.feedback = "正在帮您打开购物车";
                break;
            case 2:
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        SkuActivity.this.finish();
                    }
                }, 300);
                pageReturn.isHandler = true;
                pageReturn.feedback = "好的";
                break;
        }
        return pageReturn;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mSkuEngine = null;
        if (!(this.mViewHolder == null || this.mViewHolder.mSkuPropLayout == null)) {
            int count = this.mViewHolder.mSkuPropLayout.getChildCount();
            for (int i = 0; i < count; i++) {
                View prop = this.mViewHolder.mSkuPropLayout.getChildAt(i);
                if (prop != null && (prop instanceof SkuPropItemLayout)) {
                    ((SkuPropItemLayout) prop).onDestroy();
                }
            }
            this.mViewHolder.mSkuPropLayout.removeAllViews();
            LinearLayout unused = this.mViewHolder.mSkuPropLayout = null;
            this.mViewHolder = null;
        }
        this.buildOrderPreSale = null;
        unRegisterLoginListener();
        super.onDestroy();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event);
    }

    /* access modifiers changed from: protected */
    public void onLogin() {
        ZpLogger.v(TAG, TAG + ",onLogin");
        showQRCode(false);
    }

    private void initparameter() {
        boolean beta;
        boolean z = true;
        this.mSku = false;
        this.mIsJuhuasuan = false;
        this.mBuyCount = 1;
        this.mLimitCount = -1;
        this.unitBuy = 1;
        this.mBusinessRequest = BusinessRequest.getBusinessRequest();
        this.mImageLoaderManager = ImageLoaderManager.get();
        if (GlobalConfigInfo.getInstance().getGlobalConfig() == null || !GlobalConfigInfo.getInstance().getGlobalConfig().isBeta()) {
            beta = false;
        } else {
            beta = true;
        }
        DisplayImageOptions.Builder displayer = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(0));
        if (beta) {
            z = false;
        }
        this.options = displayer.cacheInMemory(z).cacheOnDisc(beta).showImageForEmptyUri(R.drawable.ytm_sku_image_default).showImageOnFail(R.drawable.ytm_sku_image_default).bitmapConfig(Bitmap.Config.RGB_565).build();
        this.mAnimationIn = AnimationUtils.loadAnimation(this, R.anim.ytm_slide_in_left);
        this.mAnimationOut = AnimationUtils.loadAnimation(this, R.anim.ytm_slide_out_left);
        registerLoginListener();
    }

    /* access modifiers changed from: private */
    public void initView() {
        this.mViewHolder = new ViewHolder();
        setContentView(this.mViewHolder.mSkuRootLayout);
        if (this.tbDetailResultV6 != null && this.tbDetailResultV6.getItem() != null) {
            if (DetailV6Utils.getUnit(this.tbDetailResultV6) != null) {
                Unit unit = DetailV6Utils.getUnit(this.tbDetailResultV6);
                if (!(unit.getFeature() == null || unit.getFeature().getHasSku() == null || unit.getFeature().getHasSku().equals(""))) {
                    if (!unit.getFeature().getHasSku().equals("true") || this.tbDetailResultV6.getSkuBase() == null || this.tbDetailResultV6.getSkuBase().getProps() == null || this.tbDetailResultV6.getSkuBase().getProps().size() <= 0) {
                        this.mSku = false;
                    } else {
                        this.mSku = true;
                    }
                }
                if (unit == null || unit.getVertical() == null || unit.getVertical().getPresale() == null) {
                    this.mSubPrice = null;
                } else {
                    this.isPreSale = true;
                    if (unit.getPrice() == null || unit.getPrice().getSubPrice() == null || unit.getPrice().getSubPrice().getPriceText() == null) {
                        this.mSubPrice = null;
                    } else {
                        this.mSubPrice = unit.getPrice().getSubPrice().getPriceText();
                    }
                    this.buildOrderPreSale = new BuildOrderPreSale();
                    Unit.VerticalBean.PresaleBean presale = unit.getVertical().getPresale();
                    this.buildOrderPreSale.setEndTime(presale.getEndTime());
                    this.buildOrderPreSale.setStartTime(presale.getStartTime());
                    this.buildOrderPreSale.setExtraText(presale.getExtraText());
                    this.buildOrderPreSale.setOrderedItemAmount(presale.getOrderedItemAmount());
                    this.buildOrderPreSale.setStatus(presale.getStatus());
                    this.buildOrderPreSale.setTip(presale.getTip());
                    this.buildOrderPreSale.setText(presale.getText());
                }
            } else {
                MockData mockdata = DetailV6Utils.getMockdata(this.tbDetailResultV6);
                if (mockdata != null && mockdata.getFeature() != null && mockdata.getFeature().isHasSku()) {
                    this.mSku = true;
                } else if (this.tbDetailResultV6.getFeature() != null) {
                    if (this.tbDetailResultV6.getFeature().getSecKill() != null) {
                        this.isSeckKill = true;
                    }
                    if (this.tbDetailResultV6.getFeature().getHasSku() == null || !this.tbDetailResultV6.getFeature().getHasSku().equals("true")) {
                        this.mSku = false;
                    } else {
                        ZpLogger.e(TAG, "秒杀商品有sku");
                        this.mSku = true;
                    }
                }
            }
            TBDetailResultV6.SellerBean seller = this.tbDetailResultV6.getSeller();
            if (seller == null || TextUtils.isEmpty(seller.getSellerType())) {
                this.resConfig = new TaobaoResConfig(this);
            } else if (seller.getSellerType().toUpperCase().equals("B")) {
                this.resConfig = new TmallResConfig(this);
            } else {
                this.resConfig = new TaobaoResConfig(this);
            }
            getLimitCount();
            this.mItemId = this.tbDetailResultV6.getItem().getItemId();
            this.mSkuEngine = new SkuEngine(this.tbDetailResultV6);
            this.mSkuEngine.setOnSkuPropLayoutListener(this.mOnSkuPropLayoutListener);
            if (!TextUtils.isEmpty(this.tbDetailResultV6.getItem().getTitle())) {
                this.mViewHolder.mSkuItemTitle.setText(this.tbDetailResultV6.getItem().getTitle());
            }
            if (this.tbDetailResultV6.getItem().getImages() != null && this.tbDetailResultV6.getItem().getImages().size() > 0) {
                setItemImage(this.tbDetailResultV6.getItem().getImages().get(0));
            }
            if (DetailV6Utils.getUnit(this.tbDetailResultV6) != null) {
                Unit unit2 = DetailV6Utils.getUnit(this.tbDetailResultV6);
                if (!(unit2.getPrice() == null || unit2.getPrice().getPrice() == null)) {
                    this.mCurPrice = unit2.getPrice().getPrice().getPriceText();
                }
            } else {
                MockData mockdata2 = DetailV6Utils.getMockdata(this.tbDetailResultV6);
                if (!(mockdata2 == null || mockdata2.getPrice().getPrice().getPriceText() == null)) {
                    this.mCurPrice = mockdata2.getPrice().getPrice().getPriceText();
                }
            }
            if (!(!this.isSeckKill || this.tbDetailResultV6.getPrice() == null || this.tbDetailResultV6.getPrice().getPrice() == null || this.tbDetailResultV6.getPrice().getPrice().getPriceText() == null)) {
                this.mCurPrice = this.tbDetailResultV6.getPrice().getPrice().getPriceText();
            }
            initUnitBuy();
            setItemPrice();
            if (this.mSku) {
                addSkuView();
                return;
            }
            ZpLogger.i(TAG, TAG + ".initView no sku mType = " + this.mType);
            addContractView();
            addKuCunView();
            this.mViewHolder.mSkuDoneTextView.requestFocus();
            if (this.mType.equals(TradeType.EDIT_CART)) {
                SkuPropItemLayout itemLayout = getPropLayoutById((long) this.mBuyNumViewPropId);
                ZpLogger.v(TAG, TAG + ".itemLayout = " + itemLayout);
                if (itemLayout != null) {
                    itemLayout.requestFocus();
                }
            }
        }
    }

    private void addSkuView() {
        Map<Long, SkuEngine.PropData> selectMap;
        if (this.mSkuEngine != null && this.tbDetailResultV6 != null && this.tbDetailResultV6.getSkuBase() != null && this.tbDetailResultV6.getSkuBase().getProps() != null) {
            List<TBDetailResultV6.SkuBaseBean.PropsBeanX> props1 = this.tbDetailResultV6.getSkuBase().getProps();
            for (int i = 0; i < props1.size(); i++) {
                if (!(props1.get(i) == null || props1.get(i).getPid() == null || props1.get(i).getValues() == null || props1.get(i).getValues().size() == 0)) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
                    SkuPropItemLayout prop = new SkuPropItemLayout(this, Long.parseLong(props1.get(i).getPid()), true, this.mSkuEngine);
                    prop.setSkuPropName(props1.get(i).getName());
                    prop.setSkuPropView(props1.get(i).getValues());
                    prop.setOnPropViewFocusListener(this.mOnPropViewFocusListener);
                    if (i > 0) {
                        params.topMargin = (int) getResources().getDimension(R.dimen.dp_66);
                    }
                    this.mViewHolder.mSkuPropLayout.addView(prop, params);
                }
            }
            setDefaultSkuSelected();
            addContractView();
            addKuCunView();
            boolean hasFocus = false;
            if ((this.mType.equals(TradeType.BUY) || this.mType.equals(TradeType.ADD_CART)) && (selectMap = this.mSkuEngine.getSelectedPropDataMap()) != null && selectMap.size() > 0) {
                if (Config.isDebug()) {
                    ZpLogger.i(TAG, TAG + ".addSkuView.selectMap = " + selectMap + ".skuProps = " + props1);
                }
                if (selectMap.size() == props1.size()) {
                    new Handler().post(new Runnable() {
                        public void run() {
                            SkuActivity.this.mViewHolder.mSkuPropScrollView.fullScroll(130);
                            SkuActivity.this.mViewHolder.mSkuDoneTextView.requestFocus();
                            if (SkuActivity.this.mNeedShowMask) {
                                SkuActivity.this.mViewHolder.mSkuPropTopMask.setVisibility(0);
                                SkuActivity.this.mViewHolder.mSkuPropBottomMask.setVisibility(8);
                            }
                        }
                    });
                    hasFocus = true;
                } else {
                    TBDetailResultV6.SkuBaseBean.PropsBeanX unSelectProp = this.mSkuEngine.getUnSelectProp();
                    ZpLogger.v(TAG, TAG + ".addSkuView.skuProp = " + unSelectProp);
                    if (unSelectProp != null) {
                        int count = this.mViewHolder.mSkuPropLayout.getChildCount();
                        int i2 = 0;
                        while (true) {
                            if (i2 < count) {
                                SkuPropItemLayout view = (SkuPropItemLayout) this.mViewHolder.mSkuPropLayout.getChildAt(i2);
                                if (view != null && Config.isDebug()) {
                                    ZpLogger.v(TAG, TAG + ".addSkuView.view.getPropId() = " + view.getPropId() + ",skuProp.propId = " + unSelectProp.getPid());
                                }
                                if (view != null && view.getPropId() == Long.parseLong(unSelectProp.getPid())) {
                                    hasFocus = true;
                                    view.requestFocus();
                                    break;
                                }
                                i2++;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            if (!hasFocus) {
                this.mViewHolder.mSkuPropLayout.requestFocus();
            }
        }
    }

    private void addContractView() {
        if (this.tbDetailResultV6.getContractData() != null && this.tbDetailResultV6.getContractData().size() > 0) {
            List<TBDetailResultV6.ContractData.VersionData> versionDataList = new ArrayList<>();
            for (TBDetailResultV6.ContractData contractData : this.tbDetailResultV6.getContractData()) {
                TBDetailResultV6.ContractData.VersionData data = contractData.versionData;
                if (!data.noShopCart) {
                    versionDataList.add(data);
                }
            }
            if (versionDataList.size() > 0) {
                ContractItemLayout unused = this.mViewHolder.contractItemLayout = new ContractItemLayout(this);
                this.mViewHolder.contractItemLayout.setSkuPropName("购买方式");
                this.mViewHolder.contractItemLayout.setSkuPropView(versionDataList);
                this.mViewHolder.contractItemLayout.setOnPropViewFocusListener(this.mOnPropViewFocusListener);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
                params.topMargin = (int) getResources().getDimension(R.dimen.dp_66);
                this.mViewHolder.mSkuPropLayout.addView(this.mViewHolder.contractItemLayout, params);
            }
        }
    }

    private void addKuCunView() {
        if (this.mSkuEngine != null && this.tbDetailResultV6 != null) {
            this.buyCountView = new SkuPropItemLayout(this, (long) this.mBuyNumViewPropId, false, this.mSkuEngine);
            this.buyCountView.setSkuPropName(getString(R.string.ytm_sku_buy_num_text));
            this.buyCountView.setOnPropViewFocusListener(this.mOnPropViewFocusListener);
            this.buyCountView.setOnBuyCountChangedListener(new SkuPropItemLayout.OnBuyCountChangedListener() {
                public void OnBuyCountChanged(int buyCount) {
                    if (Config.isDebug()) {
                        ZpLogger.v(SkuActivity.TAG, SkuActivity.TAG + ".addKuCunView.buyCount = " + buyCount + ", mCurPrice = " + SkuActivity.this.mCurPrice);
                    }
                    int unused = SkuActivity.this.mBuyCount = buyCount;
                    SkuActivity.this.setItemPrice();
                    SkuActivity.this.showQRCode(false);
                }
            });
            this.buyCountView.setOnBuyCountClickedListener(new SkuPropItemLayout.OnBuyCountClickedListener() {
                public void OnBuyCountClicked() {
                    SkuActivity.this.mViewHolder.mSkuDoneTextView.requestFocus();
                }
            });
            if (!this.mSku || TextUtils.isEmpty(this.mSkuId)) {
                SkuPriceNum skuPriceNum2 = resolvePriceNum("0");
                if (skuPriceNum2.getQuantity() == 0) {
                    this.buyCountView.setKuCunNum(0, this.mLimitCount);
                    ZpLogger.e(RequestConstant.ENV_TEST, "test.quantity22 = 0");
                } else if (skuPriceNum2.getLimit() == 0) {
                    this.buyCountView.setKuCunNum((long) skuPriceNum2.getQuantity(), 0);
                } else {
                    this.buyCountView.setKuCunNum((long) skuPriceNum2.getQuantity(), (long) skuPriceNum2.getLimit());
                }
            } else {
                SkuPriceNum skuPriceNum = resolvePriceNum(this.mSkuId);
                if (skuPriceNum == null || skuPriceNum.getQuantity() <= 0) {
                    SkuPriceNum skuPriceNum1 = resolvePriceNum("0");
                    ZpLogger.e(TAG, "skuPriceNum1 = " + skuPriceNum1);
                    if (this.tbDetailResultV6.getApiStack() == null || this.tbDetailResultV6.getApiStack().get(0).getValue() == null || skuPriceNum1.getQuantity() == 0) {
                        if (this.isSeckKill) {
                            ZpLogger.e(TAG, "skuactivity秒杀");
                            if (skuPriceNum1 == null || skuPriceNum1.getLimit() != 0) {
                                this.buyCountView.setKuCunNum((long) skuPriceNum1.getQuantity(), (long) skuPriceNum1.getLimit());
                            } else {
                                ZpLogger.e(TAG, "skuactivity秒杀 限购为0");
                                this.buyCountView.setKuCunNum((long) skuPriceNum1.getQuantity(), 0);
                            }
                        }
                        ZpLogger.e(RequestConstant.ENV_TEST, "test.quantity11 = 0");
                    } else if (skuPriceNum1 == null || skuPriceNum1.getLimit() != 0) {
                        this.buyCountView.setKuCunNum((long) skuPriceNum1.getQuantity(), (long) skuPriceNum1.getLimit());
                    } else {
                        this.buyCountView.setKuCunNum((long) skuPriceNum1.getQuantity(), 0);
                    }
                } else if (skuPriceNum.getLimit() == 0) {
                    this.buyCountView.setKuCunNum((long) skuPriceNum.getQuantity(), 0);
                } else {
                    this.buyCountView.setKuCunNum((long) skuPriceNum.getQuantity(), (long) skuPriceNum.getLimit());
                }
            }
            if (this.unitBuy > 1) {
                this.buyCountView.setUnitBuy(this.unitBuy);
                this.mBuyCount /= this.unitBuy;
            }
            this.buyCountView.setCurBuyCount(this.mBuyCount);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
            params.topMargin = (int) getResources().getDimension(R.dimen.dp_66);
            this.mViewHolder.mSkuPropLayout.addView(this.buyCountView, params);
            if (this.mViewHolder.mSkuPropLayout.getChildCount() > this.mShowMaskSkuNum) {
                this.mNeedShowMask = true;
                this.mViewHolder.mSkuPropBottomMask.setVisibility(0);
                return;
            }
            this.mNeedShowMask = false;
            this.mViewHolder.mSkuPropBottomMask.setVisibility(8);
        }
    }

    private void initUnitBuy() {
        if (this.tbDetailResultV6.getApiStack() != null) {
            try {
                String value = this.tbDetailResultV6.getApiStack().get(0).getValue();
                if (value != null) {
                    JSONObject skuItem = new JSONObject(value).getJSONObject("skuCore").getJSONObject("skuItem");
                    if (skuItem.has("unitBuy") && Integer.parseInt(skuItem.getString("unitBuy")) > 0) {
                        this.unitBuy = Integer.parseInt(skuItem.getString("unitBuy"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: private */
    public SkuPriceNum resolvePriceNum(String skuid) {
        if (this.tbDetailResultV6.getApiStack() != null) {
            String value = this.tbDetailResultV6.getApiStack().get(0).getValue();
            if (value != null) {
                try {
                    JSONObject sku2info = new JSONObject(value).getJSONObject("skuCore").getJSONObject("sku2info");
                    if (sku2info.has(skuid)) {
                        SkuPriceNum skuPriceNum = (SkuPriceNum) JSON.parseObject(sku2info.getJSONObject(skuid).toString(), SkuPriceNum.class);
                        ZpLogger.e(TAG, "SkuPriceNum从apistack中拿");
                        return skuPriceNum;
                    }
                    if (this.tbDetailResultV6.getMockData() != null) {
                        ZpLogger.e(TAG, "1SkuPriceNum从mockdata中拿");
                        String mockData = this.tbDetailResultV6.getMockData();
                        if (mockData != null) {
                            try {
                                JSONObject msku2info = new JSONObject(mockData).getJSONObject("skuCore").getJSONObject("sku2info");
                                if (msku2info.has(skuid)) {
                                    return (SkuPriceNum) JSON.parseObject(msku2info.getJSONObject(skuid).toString(), SkuPriceNum.class);
                                }
                                return null;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return null;
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        } else if (this.tbDetailResultV6.getMockData() != null) {
            ZpLogger.e(TAG, "2SkuPriceNum从mockdata中拿");
            String mockData2 = this.tbDetailResultV6.getMockData();
            if (mockData2 != null) {
                try {
                    JSONObject sku2info2 = new JSONObject(mockData2).getJSONObject("skuCore").getJSONObject("sku2info");
                    if (sku2info2.has(skuid)) {
                        return (SkuPriceNum) JSON.parseObject(sku2info2.getJSONObject(skuid).toString(), SkuPriceNum.class);
                    }
                    return null;
                } catch (JSONException e3) {
                    e3.printStackTrace();
                }
            }
        } else if (this.isSeckKill && this.tbDetailResultV6.getSkuKore() != null) {
            ZpLogger.e(TAG, "tbDetailResultV6.getSkuKore()!=null");
            return resolveSeckSkucore(this.tbDetailResultV6.getSkuKore(), skuid);
        }
        return null;
    }

    private SkuPriceNum resolveSeckSkucore(String s, String skuid) {
        if (s.equals("")) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (jsonObject.has("sku2info")) {
                JSONObject sku2info = jsonObject.getJSONObject("sku2info");
                if (sku2info.has(skuid)) {
                    SkuPriceNum skuPriceNum = (SkuPriceNum) JSON.parseObject(sku2info.getJSONObject(skuid).toString(), SkuPriceNum.class);
                    ZpLogger.e(TAG, skuPriceNum.toString());
                    return skuPriceNum;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setDefaultSkuSelected() {
        String defaultValueId;
        SkuPropItemLayout prop;
        if (this.mSkuEngine != null && this.tbDetailResultV6 != null && this.tbDetailResultV6.getSkuBase() != null && this.tbDetailResultV6.getSkuBase().getProps() != null) {
            List<TBDetailResultV6.SkuBaseBean.PropsBeanX> props = this.tbDetailResultV6.getSkuBase().getProps();
            Map<String, String> defaultPropMap = null;
            if (!TextUtils.isEmpty(this.mSkuId)) {
                defaultPropMap = this.mSkuEngine.getDefaultPropFromSkuId(this.mSkuId);
            } else if (props.size() == 1) {
                for (Map.Entry<String, String> next : this.mSkuEngine.getPPathIdmap().entrySet()) {
                    defaultPropMap = this.mSkuEngine.getDefaultPropFromSkuId(next.getValue());
                }
            }
            for (int i = 0; i < props.size(); i++) {
                if (!(props.get(i) == null || props.get(i).getPid() == null || props.get(i).getValues() == null || props.get(i).getValues().size() == 0)) {
                    List<TBDetailResultV6.SkuBaseBean.PropsBeanX.ValuesBean> values = props.get(i).getValues();
                    int size = values.size();
                    Long valueId = null;
                    if (size == 1) {
                        valueId = Long.valueOf(Long.parseLong(values.get(0).getVid()));
                    } else if (size > 1 && (defaultValueId = this.mSkuEngine.getDefaultValueIdFromPropId(String.valueOf(props.get(i).getPid()), defaultPropMap)) != null) {
                        try {
                            valueId = Long.valueOf(defaultValueId);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    ZpLogger.v(TAG, TAG + ".addSkuView.defaultValueId = " + valueId);
                    if (!(valueId == null || (prop = (SkuPropItemLayout) this.mViewHolder.mSkuPropLayout.getChildAt(i)) == null)) {
                        prop.setDefaultSelectSku(Long.parseLong(props.get(i).getPid()), valueId.longValue());
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void setItemImage(String imageUrl) {
        String theImageUrl;
        if (Config.isDebug()) {
            ZpLogger.v(TAG, TAG + ".setItemImage.imageUrl = " + imageUrl);
        }
        this.mImageUrl = imageUrl;
        if (!TextUtils.isEmpty(imageUrl) && this.mViewHolder.mSkuItemImage != null) {
            boolean beta = GlobalConfigInfo.getInstance().getGlobalConfig() != null && GlobalConfigInfo.getInstance().getGlobalConfig().isBeta();
            if (((double) SystemConfig.DENSITY.floatValue()) > 1.0d) {
                theImageUrl = imageUrl + (beta ? "_480x480.jpg" : "_960x960.jpg");
            } else {
                theImageUrl = imageUrl + (beta ? "_320x320.jpg" : "_640x640.jpg");
            }
            if (Config.isDebug()) {
                ZpLogger.v(TAG, TAG + ".setItemImage.theImageUrl = " + theImageUrl);
            }
            this.mImageLoaderManager.displayImage(theImageUrl, this.mViewHolder.mSkuItemImage, this.options, new SimpleImageLoadingListener() {
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    if (imageUri.contains(SkuActivity.this.mImageUrl)) {
                        ImageView imageView = (ImageView) view;
                        int width = loadedImage.getWidth();
                        int height = loadedImage.getHeight();
                        Bitmap newBitmap = null;
                        if (width > height) {
                            newBitmap = Bitmap.createBitmap(loadedImage, (width - height) / 2, 0, height, height);
                        } else if (width < height) {
                            newBitmap = Bitmap.createBitmap(loadedImage, 0, 0, width, width);
                        }
                        if (newBitmap != null) {
                            imageView.setImageBitmap(newBitmap);
                        } else {
                            imageView.setImageBitmap(loadedImage);
                        }
                        super.onLoadingComplete(imageUri, view, loadedImage);
                    } else if (Config.isDebug()) {
                        ZpLogger.i(SkuActivity.TAG, SkuActivity.TAG + ".setItemImage.onLoadingComplete.imageUri = " + imageUri + ". mImageUrl = " + SkuActivity.this.mImageUrl);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void setItemPrice() {
        String price;
        String price2;
        String text;
        int end;
        double fPrice;
        ZpLogger.v(TAG, TAG + ".setItemPrice.price = " + this.mCurPrice);
        if (!TextUtils.isEmpty(this.mCurPrice)) {
            if (this.unitBuy > 1) {
                price = String.valueOf(Double.valueOf(this.mCurPrice).doubleValue() * ((double) this.unitBuy));
            } else {
                price = this.mCurPrice;
            }
            boolean bAllSelected = false;
            Map<Long, SkuEngine.PropData> selectMap = this.mSkuEngine.getSelectedPropDataMap();
            if (this.mSku) {
                List<TBDetailResultV6.SkuBaseBean.PropsBeanX> props = this.tbDetailResultV6.getSkuBase().getProps();
                if (selectMap != null && selectMap.size() > 0 && selectMap.size() == props.size()) {
                    bAllSelected = true;
                }
            } else {
                bAllSelected = true;
            }
            if (bAllSelected && this.mBuyCount > 1) {
                String point = null;
                try {
                    int index = this.mCurPrice.indexOf(".");
                    if (index >= 0) {
                        point = "0.";
                        for (int i = 0; i < (this.mCurPrice.length() - index) - 1; i++) {
                            point = point + "0";
                        }
                    }
                    double fPrice2 = Double.valueOf(this.mCurPrice).doubleValue();
                    if (this.unitBuy > 1) {
                        fPrice = fPrice2 * ((double) (this.mBuyCount * this.unitBuy));
                    } else {
                        fPrice = fPrice2 * ((double) this.mBuyCount);
                    }
                    if (!TextUtils.isEmpty(point)) {
                        price2 = new DecimalFormat(point).format(fPrice);
                    } else {
                        price2 = String.valueOf(fPrice);
                    }
                    ZpLogger.v(TAG, TAG + ".setItemPrice.fPrice = " + fPrice + ", price = " + price2 + ".point = " + point);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            if (!TextUtils.isEmpty(this.uriPrice)) {
                price2 = this.uriPrice;
            }
            ZpLogger.e(TAG, "isPreSale = " + this.isPreSale);
            if (!this.isPreSale || TextUtils.isEmpty(this.mSubPrice)) {
                text = getString(R.string.ytm_sku_jiesuan_text);
                this.mViewHolder.mSkuPreSaleTextView.setVisibility(4);
            } else {
                text = getString(R.string.ytm_sku_presale);
            }
            String text2 = text + price2;
            SpannableString spannableString = new SpannableString(text2);
            int end2 = text2.indexOf("¥") + 1;
            spannableString.setSpan(new AbsoluteSizeSpan((int) getResources().getDimension(R.dimen.sp_20)), 0, end2, 33);
            int start = end2;
            if (price2.contains(".")) {
                end = text2.indexOf(".");
            } else {
                end = text2.length();
            }
            if (price2.length() > 10) {
                spannableString.setSpan(new AbsoluteSizeSpan((int) getResources().getDimension(R.dimen.sp_34)), start, end, 33);
            } else if (price2.length() > 16) {
                spannableString.setSpan(new AbsoluteSizeSpan((int) getResources().getDimension(R.dimen.sp_28)), start, end, 33);
            } else {
                spannableString.setSpan(new AbsoluteSizeSpan((int) getResources().getDimension(R.dimen.sp_40)), start, end, 33);
            }
            if (price2.contains(".")) {
                spannableString.setSpan(new AbsoluteSizeSpan((int) getResources().getDimension(R.dimen.sp_20)), text2.indexOf("."), text2.length(), 33);
            }
            this.mViewHolder.mSkuPriceTextView.setText(spannableString);
            if (this.isPreSale && !TextUtils.isEmpty(this.mSubPrice)) {
                this.mViewHolder.mSkuPreSaleTextView.setText("预售价  ¥" + this.mSubPrice);
                this.mViewHolder.mSkuPreSaleTextView.setTextSize(0, getResources().getDimension(R.dimen.sp_20));
                this.mViewHolder.mSkuPreSaleTextView.setVisibility(0);
                if (this.buildOrderPreSale != null) {
                    this.buildOrderPreSale.setPriceText(this.mCurPrice);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateSkuKuCunAndrPrice(String skuId) {
        ZpLogger.v(TAG, TAG + ".updateSkuKuCunAndrPrice.skuId = " + skuId);
        this.mSkuId = skuId;
        if (TextUtils.isEmpty(skuId) || this.tbDetailResultV6 == null || this.tbDetailResultV6.getSkuBase() == null || this.tbDetailResultV6.getSkuBase().getProps() == null) {
            ZpLogger.e(TAG, TAG + ".updateSkuKuCunAndrPrice param error");
            return;
        }
        SkuPriceNum skuPriceNum = resolvePriceNum(skuId);
        if (skuPriceNum != null && skuPriceNum.getQuantity() != 0) {
            if (!(skuPriceNum == null || skuPriceNum.getPrice() == null)) {
                if (skuPriceNum.getPrice().getPriceText() != null) {
                    this.mCurPrice = skuPriceNum.getPrice().getPriceText();
                }
                if (!(!this.isPreSale || skuPriceNum.getSubPrice() == null || skuPriceNum.getSubPrice().getPriceText() == null)) {
                    this.mSubPrice = skuPriceNum.getSubPrice().getPriceText();
                }
            }
            setItemPrice();
            int quantity = skuPriceNum.getQuantity();
            SkuPropItemLayout layout = getPropLayoutById((long) this.mBuyNumViewPropId);
            if (layout == null) {
                return;
            }
            if (skuPriceNum.getLimit() == 0) {
                layout.setKuCunNum((long) skuPriceNum.getQuantity(), 0);
                return;
            }
            layout.setKuCunNum((long) quantity, (long) skuPriceNum.getLimit());
            layout.setCurBuyCount(1);
        }
    }

    /* access modifiers changed from: private */
    public SkuPropItemLayout getPropLayoutById(long propId) {
        if (this.mViewHolder.mSkuPropLayout == null) {
            return null;
        }
        for (int i = 0; i < this.mViewHolder.mSkuPropLayout.getChildCount(); i++) {
            View prop = this.mViewHolder.mSkuPropLayout.getChildAt(i);
            if ((prop instanceof SkuPropItemLayout) && prop != null && ((SkuPropItemLayout) prop).getPropId() == propId) {
                return (SkuPropItemLayout) prop;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void gotoOrder() {
        BuildOrderRequestBo buildOrderRequestBo = new BuildOrderRequestBo();
        ZpLogger.v(TAG, TAG + ".gotoBuy.mSkuId = " + this.mSkuId + ".mItemId = " + this.mItemId + ", mBuyCount = " + this.mBuyCount);
        buildOrderRequestBo.setBuyNow(true);
        buildOrderRequestBo.setSkuId(this.mSkuId);
        buildOrderRequestBo.setItemId(this.mItemId);
        buildOrderRequestBo.setQuantity(this.mBuyCount * this.unitBuy);
        buildOrderRequestBo.setFrom("item");
        buildOrderRequestBo.setExtParams(this.extParams);
        buildOrderRequestBo.setTagId(this.tagId);
        Intent intent = new Intent();
        intent.putExtra("mBuildOrderRequestBo", buildOrderRequestBo);
        ZpLogger.e(TAG, "mBuildOrderRequestBo = " + buildOrderRequestBo.toString());
        if (this.isPreSale && this.buildOrderPreSale != null && !TextUtils.isEmpty(this.mSubPrice)) {
            intent.putExtra("mBuildOrderPreSale", this.buildOrderPreSale);
        }
        if (this.mProductTagBo != null) {
            intent.putExtra("mProductTagBo", this.mProductTagBo);
        }
        if (GlobalConfigInfo.getInstance().getGlobalConfig() == null || !GlobalConfigInfo.getInstance().getGlobalConfig().isTradeDegrade()) {
            intent.setClassName(this, BaseConfig.SWITCH_TO_NEWBUILDORDER_ACTIVITY);
        } else {
            intent.setClassName(this, BaseConfig.SWITCH_TO_BUILDORDER_ACTIVITY);
        }
        ZpLogger.v(TAG, TAG + ".gotoBuy.mIsJuhuasuan = " + this.mIsJuhuasuan);
        if (this.mIsJuhuasuan) {
            OnWaitProgressDialog(true);
            this.mBusinessRequest.requestJoinGroup(this.mItemId, new GetJoinGroupRequestListener(new WeakReference(this), intent));
            return;
        }
        startActivityForResult(intent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* access modifiers changed from: private */
    public void gotoShopCart() {
        if (!NetWorkUtil.isNetWorkAvailable()) {
            showNetworkErrorDialog(false);
            return;
        }
        Intent intent = new Intent();
        intent.setClassName(this, BaseConfig.SWITCH_TO_SHOPCART_LIST_ACTIVITY);
        intent.putExtra(ActivityPathRecorder.INTENTKEY_FIRST, true);
        intent.addFlags(537001984);
        if (!TextUtils.isEmpty(this.cartFrom)) {
            intent.putExtra("cartFrom", this.cartFrom);
        }
        startNeedLoginActivity(intent);
        finish();
    }

    /* access modifiers changed from: private */
    public void checkIsJhs() {
        this.mIsJuhuasuan = false;
        if (DetailV6Utils.getUnit(this.tbDetailResultV6) != null && DetailV6Utils.getUnit(this.tbDetailResultV6).getVertical() != null && DetailV6Utils.getUnit(this.tbDetailResultV6).getVertical().getJhs() != null) {
            this.mIsJuhuasuan = true;
        }
    }

    private String getLimitCount() {
        if (this.tbDetailResultV6.getApiStack() != null) {
            try {
                String limit = new JSONObject(this.tbDetailResultV6.getApiStack().get(0).getValue()).getJSONObject("skuCore").getJSONObject("sku2info").getJSONObject("0").optString("limit", (String) null);
                if (limit != null) {
                    this.mLimitCount = Long.parseLong(limit);
                    return limit;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return "";
            }
        }
        return "";
    }

    /* access modifiers changed from: private */
    public Map<String, String> initDlgProperties(Map<String, String> p) {
        if (!(this.tbDetailResultV6 == null || this.tbDetailResultV6.getItem() == null)) {
            if (!TextUtils.isEmpty(this.tbDetailResultV6.getItem().getItemId())) {
                p.put("item_id", this.tbDetailResultV6.getItem().getItemId());
            }
            if (!TextUtils.isEmpty(this.tbDetailResultV6.getItem().getTitle())) {
                p.put("name", this.tbDetailResultV6.getItem().getTitle());
            }
        }
        if (CoreApplication.getLoginHelper(CoreApplication.getApplication()).isLogin()) {
            p.put("is_login", "1");
        } else {
            p.put("is_login", "0");
        }
        return p;
    }

    public Map<String, String> getPageProperties() {
        Map<String, String> p = super.getPageProperties();
        if (!(this.tbDetailResultV6 == null || this.tbDetailResultV6.getItem() == null)) {
            if (!TextUtils.isEmpty(this.tbDetailResultV6.getItem().getItemId())) {
                p.put("item_id", this.tbDetailResultV6.getItem().getItemId());
            }
            if (!TextUtils.isEmpty(this.tbDetailResultV6.getItem().getTitle())) {
                p.put("name", this.tbDetailResultV6.getItem().getTitle());
            }
        }
        p.put("spm-cnt", SPMConfig.PAGE_SKU);
        if (!TextUtils.isEmpty(this.mSkuId)) {
            p.put("sku", this.mSkuId);
        }
        String user_id = User.getUserId();
        if (!TextUtils.isEmpty(user_id)) {
            p.put("user_id", user_id);
        }
        return p;
    }

    /* access modifiers changed from: private */
    public void tbOnSuccessAddCart() {
        if (this.tbDetailResultV6 != null) {
            Map<String, String> p = getPageProperties();
            if (this.tbDetailResultV6.getApiStack() == null) {
                SkuPriceNum skuPriceNum = resolvePriceNum("0");
                if (skuPriceNum.getQuantity() != 0) {
                    p.put("ites_amount", String.valueOf(skuPriceNum.getQuantity()));
                }
            } else if (this.tbDetailResultV6.getApiStack().get(0).getValue() != null) {
                SkuPriceNum skuPriceNum2 = resolvePriceNum("0");
                if (skuPriceNum2.getQuantity() != 0) {
                    p.put("ites_amount", String.valueOf(skuPriceNum2.getQuantity()));
                }
            }
            if (this.tbDetailResultV6.getSeller() != null) {
                if (this.tbDetailResultV6.getSeller().getShopId() != null) {
                    p.put("shop_id", String.valueOf(this.tbDetailResultV6.getSeller().getShopId()));
                }
                if (this.tbDetailResultV6.getSeller().getUserId() != null) {
                    p.put("seller_id", String.valueOf(this.tbDetailResultV6.getSeller().getUserId()));
                }
            }
            Utils.utCustomHit(getFullPageName(), Utils.getControlName(getFullPageName(), "Add_Cart", (Integer) null, new String[0]), p);
        }
    }

    /* access modifiers changed from: private */
    public void showTheDlgOnAddCartSuccess() {
        showSuccessAddCartDialog(true, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Map<String, String> p = SkuActivity.this.initDlgProperties(Utils.getProperties());
                TBS.Adv.ctrlClicked(CT.Button, Utils.getControlName(SkuActivity.this.getFullPageName(), "Immediately_Buy", (Integer) null, "Click"), Utils.getKvs(p));
                SkuActivity.this.gotoShopCart();
            }
        }, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Map<String, String> p = SkuActivity.this.initDlgProperties(Utils.getProperties());
                TBS.Adv.ctrlClicked(CT.Button, Utils.getControlName(SkuActivity.this.getFullPageName(), "Keep_Visit", (Integer) null, "Click"), Utils.getKvs(p));
                SkuActivity.this.finish();
            }
        }, new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == 4 && event.getAction() == 0) {
                    Map<String, String> p = SkuActivity.this.initDlgProperties(Utils.getProperties());
                    TBS.Adv.ctrlClicked(CT.Button, Utils.getControlName(SkuActivity.this.getFullPageName(), ActionType.BACK, (Integer) null, "Click"), Utils.getKvs(p));
                }
                return false;
            }
        });
    }

    /* access modifiers changed from: private */
    public void showTheDlgOnCartFull(String msg) {
        showErrorDialog(msg, getString(R.string.ytm_sku_cart_full_buy), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Map<String, String> p = SkuActivity.this.initDlgProperties(Utils.getProperties());
                TBS.Adv.ctrlClicked(CT.Button, Utils.getControlName(SkuActivity.this.getFullPageName(), "cart_fulled_Immediately_Buy", (Integer) null, "Click"), Utils.getKvs(p));
                SkuActivity.this.gotoShopCart();
            }
        }, new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == 4 && event.getAction() == 0) {
                    Map<String, String> p = SkuActivity.this.initDlgProperties(Utils.getProperties());
                    TBS.Adv.ctrlClicked(CT.Button, Utils.getControlName(SkuActivity.this.getFullPageName(), "cart_fulled_back", (Integer) null, "Click"), Utils.getKvs(p));
                }
                return false;
            }
        });
    }

    /* access modifiers changed from: private */
    public void showQRCode() {
        if (this.tbDetailResultV6 != null && this.tbDetailResultV6.getSeller() != null && this.tbDetailResultV6.getSeller().getSellerType() != null && this.resConfig != null) {
            showItemQRCodeFromItemId(getString(R.string.ytbv_qr_buy_item), this.mItemId, (Bitmap) null, true, (DialogInterface.OnKeyListener) null, false);
        }
    }

    /* access modifiers changed from: private */
    public void showQRCode(boolean show) {
        if (this.mViewHolder != null && this.resConfig != null && !show && this.mViewHolder.mSkuQrCode.getVisibility() == 0) {
            this.mViewHolder.mSkuQrCode.setVisibility(8);
            this.mViewHolder.mSkuQrCode.startAnimation(this.mAnimationOut);
        }
    }

    private void requestGoodsDetail(long itemId) {
        if (itemId < 0) {
            finish();
            return;
        }
        OnWaitProgressDialog(true);
        String params = Utils.jsonString2HttpParam(this.extParams);
        if (Config.isDebug()) {
            ZpLogger.v(TAG, TAG + ".requestGoodsDetail.itemId = " + itemId + ".extParams = " + this.extParams + ",params = " + params);
        }
        this.mBusinessRequest.requestGetItemDetailV6New(String.valueOf(itemId), params, new GetGoodsDetailRequestListener(new WeakReference(this)));
    }

    /* access modifiers changed from: protected */
    public String getAppTag() {
        return "Tb";
    }

    public String getPageName() {
        return "SureJoin";
    }

    private class ViewHolder {
        /* access modifiers changed from: private */
        public ContractItemLayout contractItemLayout = null;
        /* access modifiers changed from: private */
        public TextView mSkuDoneTextView;
        /* access modifiers changed from: private */
        public ImageView mSkuItemImage;
        /* access modifiers changed from: private */
        public TextView mSkuItemTitle;
        /* access modifiers changed from: private */
        public TextView mSkuPreSaleTextView;
        /* access modifiers changed from: private */
        public TextView mSkuPriceTextView;
        /* access modifiers changed from: private */
        public ImageView mSkuPropBottomMask;
        /* access modifiers changed from: private */
        public LinearLayout mSkuPropLayout;
        /* access modifiers changed from: private */
        public ScrollView mSkuPropScrollView;
        /* access modifiers changed from: private */
        public ImageView mSkuPropTopMask;
        /* access modifiers changed from: private */
        public RelativeLayout mSkuQrCode;
        /* access modifiers changed from: private */
        public View mSkuRootLayout;

        public ViewHolder() {
            this.mSkuRootLayout = LayoutInflater.from(SkuActivity.this).inflate(R.layout.ytm_sku_activity, (ViewGroup) null);
            this.mSkuItemImage = (ImageView) this.mSkuRootLayout.findViewById(R.id.sku_item_image);
            try {
                this.mSkuItemImage.setImageDrawable(BitMapUtil.getBmpDrawable(SkuActivity.this, R.drawable.ytm_sku_image_default));
            } catch (Throwable th) {
                this.mSkuItemImage.setImageDrawable(new ColorDrawable(-7829368));
            }
            this.mSkuItemTitle = (TextView) this.mSkuRootLayout.findViewById(R.id.sku_item_title);
            this.mSkuQrCode = (RelativeLayout) this.mSkuRootLayout.findViewById(R.id.sku_qr_code);
            this.mSkuPropLayout = (LinearLayout) this.mSkuRootLayout.findViewById(R.id.sku_prop_layout);
            this.mSkuDoneTextView = (TextView) this.mSkuRootLayout.findViewById(R.id.sku_done_text_view);
            this.mSkuPriceTextView = (TextView) this.mSkuRootLayout.findViewById(R.id.sku_price);
            this.mSkuPreSaleTextView = (TextView) this.mSkuRootLayout.findViewById(R.id.sku_price_presale);
            this.mSkuPropTopMask = (ImageView) this.mSkuRootLayout.findViewById(R.id.sku_prop_top_mask);
            this.mSkuPropBottomMask = (ImageView) this.mSkuRootLayout.findViewById(R.id.sku_prop_bottom_mask);
            this.mSkuPropScrollView = (ScrollView) this.mSkuRootLayout.findViewById(R.id.sku_prop_scrollview);
            this.mSkuQrCode.setBackgroundColor(SkuActivity.this.getResources().getColor(R.color.ytm_qr_code_bg));
            this.mSkuDoneTextView.setNextFocusRightId(R.id.sku_done_text_view);
            this.mSkuDoneTextView.setOnFocusChangeListener(new View.OnFocusChangeListener(SkuActivity.this) {
                public void onFocusChange(View v, boolean hasFocus) {
                    ZpLogger.v(SkuActivity.TAG, SkuActivity.TAG + ".v = " + v + ", hasFocus = " + hasFocus);
                    if (v != ViewHolder.this.mSkuDoneTextView) {
                        return;
                    }
                    if (hasFocus) {
                        ViewHolder.this.mSkuDoneTextView.setBackgroundResource(R.drawable.ytm_background_focus);
                        ViewHolder.this.mSkuDoneTextView.setTextColor(SkuActivity.this.getResources().getColor(17170443));
                        return;
                    }
                    ViewHolder.this.mSkuDoneTextView.setBackgroundColor(SkuActivity.this.getResources().getColor(R.color.ytm_button_normal));
                    ViewHolder.this.mSkuDoneTextView.setTextColor(SkuActivity.this.getResources().getColor(R.color.ytm_sku_done_unfocus_text_color));
                }
            });
            this.mSkuDoneTextView.setOnClickListener(new View.OnClickListener(SkuActivity.this) {
                public void onClick(View v) {
                    JSONObject jsonObject;
                    if (SkuActivity.this.tbDetailResultV6 == null) {
                        ZpLogger.e(SkuActivity.TAG, "确定购买按钮点击，但是tbDetailResultV6=null返回");
                    } else if (SkuActivity.this.tbDetailResultV6 == null || SkuActivity.this.tbDetailResultV6.getTrade() == null || SkuActivity.this.tbDetailResultV6.getTrade().getRedirectUrl() == null || !SkuActivity.this.tbDetailResultV6.getTrade().getRedirectUrl().contains("trip")) {
                        boolean bAllSelected = false;
                        boolean needChooseContract = SkuActivity.this.mViewHolder.contractItemLayout != null && SkuActivity.this.mViewHolder.contractItemLayout.getSelectedVersionData() == null;
                        Map<Long, SkuEngine.PropData> selectMap = SkuActivity.this.mSkuEngine.getSelectedPropDataMap();
                        if (SkuActivity.this.mSku) {
                            List<TBDetailResultV6.SkuBaseBean.PropsBeanX> props = SkuActivity.this.tbDetailResultV6.getSkuBase().getProps();
                            if (selectMap != null && selectMap.size() > 0 && selectMap.size() == props.size()) {
                                bAllSelected = true;
                            }
                        } else {
                            bAllSelected = true;
                        }
                        Map<String, String> p = SkuActivity.this.getPageProperties();
                        String controlName = Utils.getControlName(SkuActivity.this.getFullPageName(), "Next", (Integer) null, "Click");
                        if (bAllSelected && !needChooseContract) {
                            ZpLogger.e(SkuActivity.TAG, "所有属性已经选择");
                            if (!CoreApplication.getLoginHelper(SkuActivity.this).isLogin()) {
                                SkuActivity.this.setLoginActivityStartShowing();
                                CoreApplication.getLoginHelper(SkuActivity.this).startYunosAccountActivity(SkuActivity.this, false);
                                p.put("is_login", "0");
                                TBS.Adv.ctrlClicked(CT.Button, controlName, Utils.getKvs(p));
                                return;
                            }
                            p.put("is_login", "1");
                            if (SkuActivity.this.mSku) {
                                if (TextUtils.isEmpty(SkuActivity.this.mSkuId)) {
                                    String unused = SkuActivity.this.mSkuId = SkuActivity.this.mSkuEngine.getSkuId();
                                }
                                ZpLogger.e(SkuActivity.TAG, SkuActivity.TAG + ".mSkuDoneTextView.setOnClickListener.mSkuId = " + SkuActivity.this.mSkuId);
                                if (!TextUtils.isEmpty(SkuActivity.this.mSkuId) && SkuActivity.this.tbDetailResultV6.getSkuBase().getProps() != null) {
                                    SkuPriceNum skuPriceNum = SkuActivity.this.resolvePriceNum(SkuActivity.this.mSkuId);
                                    ZpLogger.e(SkuActivity.TAG, SkuActivity.TAG + ".mSkuDoneTextView.setOnClickListener.skuPriceAndQuanity = " + skuPriceNum);
                                    if (skuPriceNum == null || skuPriceNum.getQuantity() <= 0) {
                                        Toast.makeText(SkuActivity.this, SkuActivity.this.getString(R.string.ytm_sku_zero_cucun), 0).show();
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            }
                            if (!(SkuActivity.this.mViewHolder.contractItemLayout == null || SkuActivity.this.mViewHolder.contractItemLayout.getSelectedVersionData() == null)) {
                                try {
                                    if (TextUtils.isEmpty(SkuActivity.this.extParams)) {
                                        jsonObject = new JSONObject();
                                    } else {
                                        jsonObject = new JSONObject(SkuActivity.this.extParams);
                                    }
                                    String versionCode = SkuActivity.this.mViewHolder.contractItemLayout.getSelectedVersionData().versionCode;
                                    String planId = SkuActivity.this.mViewHolder.contractItemLayout.getSelectedVersionData().planId;
                                    jsonObject.put("合约机的key", versionCode);
                                    if (!TextUtils.isEmpty(planId)) {
                                        jsonObject.put("alicom_wtt_param", planId + "_0_0_0_0_0_" + versionCode);
                                    }
                                    jsonObject.put("alicom_wtt_cart", "1");
                                    String unused2 = SkuActivity.this.extParams = jsonObject.toString();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (SkuActivity.this.mType.equals(TradeType.BUY)) {
                                p.put("type", TradeType.BUY.toString());
                                if (SkuActivity.this.getmFrom() == null || !SkuActivity.this.getmFrom().toLowerCase().contains("tvhongbao")) {
                                    SkuActivity.this.gotoOrder();
                                } else {
                                    SkuActivity.this.OnWaitProgressDialog(true);
                                    SkuActivity.this.mBusinessRequest.requestDoItemCoupon(SkuActivity.this.mItemId, new DoItemCouponListener(new WeakReference(SkuActivity.this)));
                                }
                            } else if (SkuActivity.this.mType.equals(TradeType.ADD_CART)) {
                                p.put("type", TradeType.ADD_CART.toString());
                                SkuActivity.this.OnWaitProgressDialog(true);
                                CouponReceiveParamsBean couponReceiveParamsBean = new CouponReceiveParamsBean();
                                couponReceiveParamsBean.setPage(Utils.utGetCurrentPage());
                                ZpLogger.i(SkuActivity.TAG, "current = " + Utils.utGetCurrentPage());
                                couponReceiveParamsBean.setReferer(ActivityPathRecorder.getInstance().getCurrentPath(SkuActivity.this));
                                if (SkuActivity.this.tbDetailResultV6.getSeller() != null) {
                                    if (SkuActivity.this.tbDetailResultV6.getSeller().getShopId() != null) {
                                        couponReceiveParamsBean.setShopId(SkuActivity.this.tbDetailResultV6.getSeller().getShopId());
                                    }
                                    if (SkuActivity.this.tbDetailResultV6.getSeller().getUserId() != null) {
                                        couponReceiveParamsBean.setSellerId(SkuActivity.this.tbDetailResultV6.getSeller().getUserId());
                                    }
                                }
                                couponReceiveParamsBean.setItemId(SkuActivity.this.mItemId);
                                couponReceiveParamsBean.setItemSku(SkuActivity.this.mSkuId);
                                couponReceiveParamsBean.setItemNum(SkuActivity.this.mBuyCount * SkuActivity.this.unitBuy);
                                couponReceiveParamsBean.setBehaviorType("itemAddShoppingCart");
                                BusinessRequest.getBusinessRequest().shopFavorites(couponReceiveParamsBean, (RequestListener<JSONObject>) null);
                                SkuActivity.this.mBusinessRequest.addBag(SkuActivity.this.mItemId, SkuActivity.this.mBuyCount * SkuActivity.this.unitBuy, SkuActivity.this.mSkuId, SkuActivity.this.extParams, new GetAddCartRequestListener(new WeakReference(SkuActivity.this)));
                            } else if (SkuActivity.this.mType.equals(TradeType.EDIT_CART)) {
                                if (!NetWorkUtil.isNetWorkAvailable()) {
                                    SkuActivity.this.showNetworkErrorDialog(false);
                                    return;
                                }
                                p.put("type", TradeType.EDIT_CART.toString());
                                Intent intent = new Intent();
                                intent.putExtra("skuId", SkuActivity.this.mSkuId);
                                intent.putExtra("buyCount", SkuActivity.this.mBuyCount * SkuActivity.this.unitBuy);
                                SkuActivity.this.setResult(-1, intent);
                                SkuActivity.this.finish();
                            }
                            p.put("all_select", "true");
                            TBS.Adv.ctrlClicked(CT.Button, controlName, Utils.getKvs(p));
                        } else if (!bAllSelected) {
                            TBDetailResultV6.SkuBaseBean.PropsBeanX unSelectProp = SkuActivity.this.mSkuEngine.getUnSelectProp();
                            if (unSelectProp != null) {
                                p.put("all_select", "false");
                                p.put("info_id", String.valueOf(unSelectProp.getPid()));
                                p.put("info", unSelectProp.getName());
                                Utils.utControlHit(controlName, p);
                                Toast.makeText(SkuActivity.this, unSelectProp.getName() + SkuActivity.this.getString(R.string.ytm_sku_no_select), 0).show();
                            } else if (Config.isDebug()) {
                                ZpLogger.e(SkuActivity.TAG, SkuActivity.TAG + ".mSkuDoneTextView.setOnClickListener params error");
                                return;
                            } else {
                                return;
                            }
                        } else {
                            Toast.makeText(SkuActivity.this, "请选择购买方式", 0).show();
                        }
                        p.put(SPMConfig.SPM, SPMConfig.EVENT_SKU_BUTTON_COMFIRM_CLICK);
                        if (!(SkuActivity.this.tbDetailResultV6 == null || SkuActivity.this.tbDetailResultV6.getSeller() == null || SkuActivity.this.tbDetailResultV6.getSeller().getShopId() == null)) {
                            p.put("shop_id", SkuActivity.this.tbDetailResultV6.getSeller().getShopId());
                        }
                        Utils.updateNextPageProperties(SPMConfig.EVENT_SKU_BUTTON_COMFIRM_CLICK);
                        Utils.utControlHit(SkuActivity.this.getFullPageName(), "Button_Comfirm", p);
                    } else {
                        SkuActivity.this.showQRCode();
                    }
                }
            });
        }
    }

    private static class GetGoodsDetailRequestListener extends BizRequestListener<TBDetailResultV6> {
        public GetGoodsDetailRequestListener(WeakReference<BaseActivity> baseActivityRef) {
            super(baseActivityRef);
        }

        public boolean onError(int resultCode, String msg) {
            SkuActivity activity = (SkuActivity) this.mBaseActivityRef.get();
            if (activity != null && !activity.isFinishing()) {
                if (Config.isDebug()) {
                    ZpLogger.i(SkuActivity.TAG, SkuActivity.TAG + ".GetGoodsDetailRequestListener.onError.resultCode = " + resultCode + ".msg = " + msg);
                }
                activity.OnWaitProgressDialog(false);
                Toast.makeText(activity, msg, 0).show();
                activity.finish();
            }
            return true;
        }

        public void onSuccess(TBDetailResultV6 data) {
            SkuActivity activity = (SkuActivity) this.mBaseActivityRef.get();
            if (activity != null && !activity.isFinishing()) {
                if (Config.isDebug()) {
                    ZpLogger.i(SkuActivity.TAG, SkuActivity.TAG + ".GetGoodsDetailRequestListener.onSuccess.data = " + data);
                }
                activity.OnWaitProgressDialog(false);
                TBDetailResultV6 unused = activity.tbDetailResultV6 = data;
                activity.initView();
                activity.checkIsJhs();
            }
        }

        public boolean ifFinishWhenCloseErrorDialog() {
            return false;
        }
    }

    private static class GetJoinGroupRequestListener extends BizRequestListener<JoinGroupResult> {
        private final Intent intent;

        public GetJoinGroupRequestListener(WeakReference<BaseActivity> mBaseActivityRef, Intent intent2) {
            super(mBaseActivityRef);
            this.intent = intent2;
        }

        public boolean onError(int resultCode, String msg) {
            SkuActivity activity = (SkuActivity) this.mBaseActivityRef.get();
            if (activity == null) {
                return false;
            }
            if (activity.isFinishing()) {
                return true;
            }
            if (Config.isDebug()) {
                ZpLogger.i(SkuActivity.TAG, SkuActivity.TAG + ".GetJoinGroupRequestListener.onError.resultCode = " + resultCode + ".msg = " + msg);
            }
            activity.OnWaitProgressDialog(false);
            return false;
        }

        public void onSuccess(JoinGroupResult data) {
            SkuActivity activity = (SkuActivity) this.mBaseActivityRef.get();
            if (activity != null && !activity.isFinishing()) {
                if (Config.isDebug()) {
                    ZpLogger.i(SkuActivity.TAG, SkuActivity.TAG + ".GetJoinGroupRequestListener.onSuccess.data = " + data);
                }
                activity.OnWaitProgressDialog(false);
                activity.startActivityForResult(this.intent, 1);
            }
        }

        public boolean ifFinishWhenCloseErrorDialog() {
            return false;
        }
    }

    private static class GetAddCartRequestListener extends BizRequestListener<ArrayList<SearchResult>> {
        public GetAddCartRequestListener(WeakReference<BaseActivity> mBaseActivityRef) {
            super(mBaseActivityRef);
        }

        public boolean onError(int resultCode, String msg) {
            SkuActivity activity = (SkuActivity) this.mBaseActivityRef.get();
            if (activity == null) {
                return false;
            }
            if (Config.isDebug()) {
                ZpLogger.i(SkuActivity.TAG, SkuActivity.TAG + ".GetAddCartRequestListener.onError.resultCode = " + resultCode + ".msg = " + msg);
            }
            activity.OnWaitProgressDialog(false);
            if (resultCode != ServiceCode.ADD_CART_FAILURE.getCode()) {
                return false;
            }
            activity.showTheDlgOnCartFull(msg);
            return true;
        }

        public void onSuccess(ArrayList<SearchResult> data) {
            SkuActivity activity = (SkuActivity) this.mBaseActivityRef.get();
            if (activity != null) {
                if (Config.isDebug()) {
                    ZpLogger.i(SkuActivity.TAG, SkuActivity.TAG + ".GetAddCartRequestListener.onSuccess.data = " + data);
                }
                activity.tbOnSuccessAddCart();
                activity.OnWaitProgressDialog(false);
                activity.showTheDlgOnAddCartSuccess();
            }
        }

        public boolean ifFinishWhenCloseErrorDialog() {
            return false;
        }
    }

    private static class DoItemCouponListener extends BizRequestListener<String> {
        public DoItemCouponListener(WeakReference<BaseActivity> mBaseActivityRef) {
            super(mBaseActivityRef);
        }

        public boolean onError(int resultCode, String msg) {
            SkuActivity activity = (SkuActivity) this.mBaseActivityRef.get();
            if (activity == null) {
                return true;
            }
            activity.OnWaitProgressDialog(false);
            if (resultCode == ServiceCode.BONUS_BALANCES_INSUFFICIENT.getCode()) {
                Toast.makeText(activity.getApplicationContext(), "阿里V代金券余额不足", 0).show();
            }
            activity.gotoOrder();
            return true;
        }

        public void onSuccess(String data) {
            SkuActivity activity = (SkuActivity) this.mBaseActivityRef.get();
            if (activity != null) {
                activity.OnWaitProgressDialog(false);
                activity.gotoOrder();
            }
        }

        public boolean ifFinishWhenCloseErrorDialog() {
            return false;
        }
    }

    public boolean isUpdateBlackList() {
        return true;
    }

    private String buildAddBagExParams() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (this.mProductTagBo != null) {
                jsonObject.put("outPreferentialId", this.mProductTagBo.getOutPreferentialId());
                jsonObject.put("tagId", this.mProductTagBo.getTagId());
            }
            if (this.mType.equals(TradeType.ADD_CART)) {
                TvOptionsConfig.setTvOptionsCart(true);
            }
            jsonObject.put("tvOptions", TvOptionsConfig.getTvOptions());
            TvOptionsConfig.setTvOptionsCart(false);
            jsonObject.put("appKey", Config.getChannel());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getFullPageName() {
        return "Page_Sku";
    }
}
