package com.yunos.tvtaobao.biz.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.statistic.CT;
import com.taobao.statistic.TBS;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tv.core.common.CoreIntentKey;
import com.yunos.tv.core.common.DeviceJudge;
import com.yunos.tv.core.common.ImageLoaderManager;
import com.yunos.tv.core.config.SPMConfig;
import com.yunos.tv.core.util.NetWorkUtil;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.adapter.TabGoodsBaseAdapter;
import com.yunos.tvtaobao.biz.listener.TabFocusFlipGridViewListener;
import com.yunos.tvtaobao.biz.listener.TabFocusListViewListener;
import com.yunos.tvtaobao.biz.listener.VerticalItemHandleListener;
import com.yunos.tvtaobao.biz.widget.TabFocusPositionManager;
import com.yunos.tvtaobao.biz.widget.TabGoodsItemView;
import com.yunos.tvtaobao.businessview.R;
import com.yunos.tvtaobao.tvsdk.widget.AdapterView;
import com.yunos.tvtaobao.tvsdk.widget.FlipGridView;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusFlipGridView;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusImageView;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusListView;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusPositionManager;
import com.yunos.tvtaobao.tvsdk.widget.focus.StaticFocusDrawable;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemSelectedListener;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public abstract class TabBaseActivity extends BaseActivity {
    protected static final int WHAT_KEYUPEVENT = 1000;
    protected static final int WHAT_TAB_LAYOUT_END = 1002;
    protected final int ALL_VALUE = -5;
    private final long KEY_DELAY = 500;
    protected final String TAG = (getClass().getSimpleName() + Constant.NLP_CACHE_TYPE + Integer.toHexString(hashCode()));
    protected ImageView fiv_pierce_background;
    protected FocusImageView fiv_pierce_block_focusd;
    protected FocusImageView fiv_pierce_cart_focusd;
    protected FocusImageView fiv_pierce_come_back_focusd;
    protected FocusImageView fiv_pierce_contact_focusd;
    protected FocusImageView fiv_pierce_home_focusd;
    protected FocusImageView fiv_pierce_my_focusd;
    protected FocusImageView fiv_pierce_red_jifen_focusd;
    protected FocusImageView fiv_pierce_red_packet_focusd;
    protected ImageView iv_pierce_cart_active;
    private boolean mCanMoveRight;
    protected int mCurrentSelectTabNumBer;
    protected TextView mEmptyView;
    private boolean mFirstRequestClassify;
    private boolean mFirstRequestGoodsData;
    protected int mGirdViewHeight;
    protected Rect mGirdViewMargin;
    protected Rect mGirdViewPadding;
    protected int mGirdViewWidth;
    protected StaticFocusDrawable mGoodsFocusDrawable;
    private HashMap<String, FocusFlipGridView> mGoodsGirdViewMap;
    protected FrameLayout mGoodsListDisplayContainer;
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    public boolean mMenuFocusGain;
    protected ImageView mShadowBottom;
    protected ImageView mShadowTop;
    private boolean mTabFirstGain;
    protected StaticFocusDrawable mTabFocusDrawable;
    /* access modifiers changed from: private */
    public TabFocusFlipGridViewListener mTabFocusFlipGridViewListener;
    protected FocusListView mTabFocusListView;
    /* access modifiers changed from: private */
    public TabFocusListViewListener mTabFocusListViewListener;
    protected TabFocusPositionManager mTabFocusPositionManager;
    private HashMap<String, Boolean> mTabGoodsFirstRequestMap;
    protected int newTabNumBer;
    protected int oldTabNumBer;
    protected TextView tv_pierce_block;
    protected TextView tv_pierce_cart;
    protected TextView tv_pierce_come_back;
    protected TextView tv_pierce_contact_focusd;
    protected TextView tv_pierce_home;
    protected TextView tv_pierce_my;
    protected TextView tv_pierce_red_jifen;
    protected TextView tv_pierce_red_packet;

    /* access modifiers changed from: protected */
    public abstract void enterDisplayDetailAsync(String str, String str2, String str3, String str4);

    /* access modifiers changed from: protected */
    public abstract String getEurlOfEnterdetail(FocusFlipGridView focusFlipGridView, String str, AdapterView<?> adapterView, View view, int i, long j);

    /* access modifiers changed from: protected */
    public abstract int getGoodsFocusDrawableId();

    /* access modifiers changed from: protected */
    public abstract String getItemIdOfEnterdetail(FocusFlipGridView focusFlipGridView, String str, AdapterView<?> adapterView, View view, int i, long j);

    /* access modifiers changed from: protected */
    public abstract String getPicUrlOfEnterDetail(FocusFlipGridView focusFlipGridView, String str, AdapterView<?> adapterView, View view, int i, long j);

    /* access modifiers changed from: protected */
    public abstract BaseAdapter getTabAdapter();

    /* access modifiers changed from: protected */
    public abstract int getTabFocusDrawableId();

    /* access modifiers changed from: protected */
    public abstract TabGoodsBaseAdapter getTabGoodsAdapter(String str, int i);

    /* access modifiers changed from: protected */
    public abstract FocusFlipGridView getTabGoodsGridView(String str, int i);

    /* access modifiers changed from: protected */
    public abstract String getTabKeyWordOfTabNumBer(int i);

    /* access modifiers changed from: protected */
    public abstract String getTitleOfEnterDetail(FocusFlipGridView focusFlipGridView, String str, AdapterView<?> adapterView, View view, int i, long j);

    /* access modifiers changed from: protected */
    public abstract String getUriOfEnterDetail(FocusFlipGridView focusFlipGridView, String str, AdapterView<?> adapterView, View view, int i, long j);

    /* access modifiers changed from: protected */
    public abstract void handlerChangeTab(int i, FocusFlipGridView focusFlipGridView, int i2, FocusFlipGridView focusFlipGridView2, boolean z);

    /* access modifiers changed from: protected */
    public abstract void initGirdViewInfo(FocusFlipGridView focusFlipGridView, TabGoodsBaseAdapter tabGoodsBaseAdapter, String str, int i);

    /* access modifiers changed from: protected */
    public abstract void onReAdjustCommonLayout(TabFocusPositionManager tabFocusPositionManager);

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ytbv_common_havetab_layout);
        this.mTabFocusPositionManager = (TabFocusPositionManager) findViewById(R.id.common_havetab_mainLayout);
        initPierceViews();
        onHandleCommonBackgroudColor(getResources().getColor(R.color.ytbv_havetab_black));
        onInitTabBaseVariableValue();
        onTabBaseCreatGoodsFocusDrawable();
    }

    public void onInitTabBaseActivity() {
        this.mHandler = new TabBaseHandler(new WeakReference(this));
        onTabBaseReadLayoutValue();
        onReAdjustCommonLayout(this.mTabFocusPositionManager);
        onTabBaseCreatTabFocusDrawable();
        setTabFocusListViewListen();
        onStartRefreshTabFocusListView();
        onRightPierceFocus();
    }

    /* access modifiers changed from: protected */
    public int getDefaultSelection() {
        return 0;
    }

    private void onInitTabBaseVariableValue() {
        this.mTabFirstGain = true;
        this.mMenuFocusGain = true;
        this.mCanMoveRight = true;
        this.mCurrentSelectTabNumBer = -1;
        this.oldTabNumBer = -1;
        this.newTabNumBer = -1;
        this.mGoodsFocusDrawable = null;
        this.mTabFocusDrawable = null;
        this.mFirstRequestClassify = true;
        this.mFirstRequestGoodsData = true;
        this.mGoodsGirdViewMap = new HashMap<>();
        this.mGoodsGirdViewMap.clear();
        this.mTabGoodsFirstRequestMap = new HashMap<>();
        this.mTabGoodsFirstRequestMap.clear();
    }

    private void onTabBaseReadLayoutValue() {
        this.mGirdViewPadding = new Rect();
        this.mGirdViewPadding.setEmpty();
        this.mGirdViewPadding.left = getResources().getDimensionPixelSize(R.dimen.dp_25);
        this.mGirdViewPadding.top = getResources().getDimensionPixelSize(R.dimen.dp_110);
        this.mGirdViewPadding.right = getResources().getDimensionPixelSize(R.dimen.dp_46);
        this.mGirdViewPadding.bottom = getResources().getDimensionPixelSize(R.dimen.dp_90);
        this.mGirdViewMargin = new Rect();
        this.mGirdViewMargin.setEmpty();
        this.mGirdViewMargin.left = getResources().getDimensionPixelSize(R.dimen.dp_0);
        this.mGirdViewMargin.top = getResources().getDimensionPixelSize(R.dimen.dp_0);
        this.mGirdViewWidth = getResources().getDimensionPixelSize(R.dimen.dp_1053);
        this.mGirdViewHeight = -1;
        this.mGoodsListDisplayContainer = (FrameLayout) findViewById(R.id.common_gridview_container);
        this.mGoodsListDisplayContainer.setVisibility(0);
        this.mShadowTop = (ImageView) findViewById(R.id.common_top_mask_view);
        this.mShadowTop.setVisibility(4);
        this.mShadowBottom = (ImageView) findViewById(R.id.common_bottom_mask_view);
        this.mEmptyView = (TextView) findViewById(R.id.common_nodata_view);
        this.mTabFocusListView = (FocusListView) findViewById(R.id.common_focuslistview);
    }

    private void initPierceViews() {
        this.fiv_pierce_home_focusd = (FocusImageView) findViewById(R.id.fiv_pierce_home_focusd);
        this.fiv_pierce_my_focusd = (FocusImageView) findViewById(R.id.fiv_pierce_my_focusd);
        this.fiv_pierce_cart_focusd = (FocusImageView) findViewById(R.id.fiv_pierce_cart_focusd);
        this.fiv_pierce_red_packet_focusd = (FocusImageView) findViewById(R.id.fiv_pierce_red_packet_focusd);
        this.fiv_pierce_block_focusd = (FocusImageView) findViewById(R.id.fiv_pierce_block_focusd);
        this.fiv_pierce_contact_focusd = (FocusImageView) findViewById(R.id.fiv_pierce_contact_focusd);
        this.fiv_pierce_come_back_focusd = (FocusImageView) findViewById(R.id.fiv_pierce_come_back_focusd);
        this.fiv_pierce_red_jifen_focusd = (FocusImageView) findViewById(R.id.fiv_pierce_red_jifen_focusd);
        this.fiv_pierce_background = (ImageView) findViewById(R.id.fiv_pierce_background);
        this.tv_pierce_home = (TextView) findViewById(R.id.tv_pierce_home);
        this.tv_pierce_my = (TextView) findViewById(R.id.tv_pierce_my);
        this.tv_pierce_cart = (TextView) findViewById(R.id.tv_pierce_cart);
        this.tv_pierce_red_packet = (TextView) findViewById(R.id.tv_pierce_red_packet);
        this.tv_pierce_block = (TextView) findViewById(R.id.tv_pierce_block);
        this.tv_pierce_contact_focusd = (TextView) findViewById(R.id.tv_pierce_contact_focusd);
        this.tv_pierce_come_back = (TextView) findViewById(R.id.tv_pierce_come_back);
        this.tv_pierce_red_jifen = (TextView) findViewById(R.id.tv_pierce_red_jifen);
        this.iv_pierce_cart_active = (ImageView) findViewById(R.id.iv_pierce_cart_active);
    }

    private void onRightPierceFocus() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                TabBaseActivity.this.fiv_pierce_home_focusd.setFocusable(true);
                TabBaseActivity.this.fiv_pierce_my_focusd.setFocusable(true);
                TabBaseActivity.this.fiv_pierce_cart_focusd.setFocusable(true);
                TabBaseActivity.this.fiv_pierce_red_packet_focusd.setFocusable(true);
                TabBaseActivity.this.fiv_pierce_block_focusd.setFocusable(true);
                TabBaseActivity.this.fiv_pierce_contact_focusd.setFocusable(true);
                TabBaseActivity.this.fiv_pierce_come_back_focusd.setFocusable(true);
                TabBaseActivity.this.fiv_pierce_red_jifen_focusd.setFocusable(true);
            }
        }, 1000);
    }

    private void setTabFocusListViewListen() {
        this.mTabFocusListView.setAnimateWhenGainFocus(false, false, false, false);
        this.mTabFocusListView.setFocusBackground(true);
        this.mTabFocusListView.setFlipScrollFrameCount(8);
        this.mTabFocusListView.setAdapter((ListAdapter) getTabAdapter());
        this.mTabFocusListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                ZpLogger.i(TabBaseActivity.this.TAG, "mTabFocusListView --> setOnFocusChangeListener --> onFocusChange --> v = " + v + "; hasFocus = " + hasFocus);
                if (TabBaseActivity.this.mTabFocusListViewListener != null) {
                    TabBaseActivity.this.mTabFocusListViewListener.onFocusChange(v, hasFocus);
                }
                if (hasFocus) {
                    boolean unused = TabBaseActivity.this.mMenuFocusGain = true;
                    if (TabBaseActivity.this.mTabFocusPositionManager != null) {
                        TabBaseActivity.this.mTabFocusPositionManager.setSelector(TabBaseActivity.this.mTabFocusDrawable);
                    }
                }
            }
        });
        this.mTabFocusListView.setOnItemSelectedListener(new ItemSelectedListener() {
            public void onItemSelected(View select, int position, boolean isSelect, View fatherView) {
                ZpLogger.v(TabBaseActivity.this.TAG, TabBaseActivity.this.TAG + ".mTabFocusListView.onItemSelected.selectView = " + select + ".position = " + position + ".isSelect = " + isSelect + ".fatherView = " + fatherView);
                if (TabBaseActivity.this.mTabFocusListViewListener != null) {
                    TabBaseActivity.this.mTabFocusListViewListener.onItemSelected(select, position, isSelect, fatherView);
                }
                if (isSelect) {
                    TabBaseActivity.this.newTabNumBer = position;
                }
            }
        });
        this.mTabFocusListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ZpLogger.i(TabBaseActivity.this.TAG, TabBaseActivity.this.TAG + ".mTabFocusListView.onItemClick.AdapterView = " + parent + ".selectedView = " + view + ".position = " + position + ".row_id = " + id);
                TabBaseActivity.this.newTabNumBer = position;
                if (TabBaseActivity.this.mHandler != null) {
                    if (TabBaseActivity.this.mHandler.hasMessages(1000)) {
                        TabBaseActivity.this.mHandler.removeMessages(1000);
                    }
                    TabBaseActivity.this.mHandler.sendEmptyMessageDelayed(1000, 500);
                }
                if (TabBaseActivity.this.mTabFocusListViewListener != null) {
                    TabBaseActivity.this.mTabFocusListViewListener.onItemClick(parent, view, position, id);
                }
            }
        });
    }

    private void onStartRefreshTabFocusListView() {
        BaseAdapter baseAdapter = getTabAdapter();
        if (baseAdapter != null) {
            if (this.mFirstRequestClassify && this.mTabFocusListView != null) {
                this.mFirstRequestClassify = false;
                this.mTabFocusListView.setVisibility(0);
            }
            baseAdapter.notifyDataSetChanged();
        }
        HandleFirstRequestGoodsData();
    }

    private void HandleFirstRequestGoodsData() {
        ZpLogger.i(this.TAG, "HandleFirstRequestGoodsData --> mFirstRequestGoodsData = " + this.mFirstRequestGoodsData + "; mTabFocusListView = " + this.mTabFocusListView);
        if (this.mFirstRequestGoodsData && this.mTabFocusListView != null) {
            this.mFirstRequestGoodsData = false;
            this.mTabFocusListView.setSelection(getDefaultSelection());
            if (this.mHandler != null) {
                this.mHandler.sendEmptyMessageDelayed(1002, 500);
            }
        }
    }

    /* access modifiers changed from: private */
    public void focusPositionManagerrequestFocus() {
        ZpLogger.i(this.TAG, "focusPositionManagerrequestFocus --> mTabFocusPositionManager = " + this.mTabFocusPositionManager + "; mTabFocusListView = " + this.mTabFocusListView);
        if (this.mTabFocusPositionManager != null) {
            this.mTabFocusPositionManager.resetFocused();
            this.mTabFocusListView.requestFocus();
        }
        if (this.mTabFirstGain) {
            this.mTabFirstGain = false;
            this.oldTabNumBer = 0;
            this.newTabNumBer = -1;
            onHandleTabFoucus(getDefaultSelection(), true);
        }
    }

    private void onTabBaseCreatTabFocusDrawable() {
        if (getTabFocusDrawableId() != 0) {
            this.mTabFocusDrawable = new StaticFocusDrawable(getResources().getDrawable(getTabFocusDrawableId()));
        }
        ZpLogger.i(this.TAG, "onTabBaseCreatTabFocusDrawable -->    mTabFocusDrawable  = " + this.mTabFocusDrawable);
        if (this.mTabFocusPositionManager != null) {
            this.mTabFocusPositionManager.setSelector(this.mTabFocusDrawable);
        }
    }

    private void onTabBaseCreatGoodsFocusDrawable() {
        this.mGoodsFocusDrawable = new StaticFocusDrawable(getResources().getDrawable(getGoodsFocusDrawableId()));
        ZpLogger.i(this.TAG, "onTabBaseCreatGoodsFocusDrawable -->    mGoodsFocusDrawable  = " + this.mGoodsFocusDrawable);
        if (this.mTabFocusPositionManager != null) {
            this.mTabFocusPositionManager.setSelector(this.mGoodsFocusDrawable);
        }
    }

    private void onCreatGoodsListGridView(final String tabKey, final int position) {
        ZpLogger.i(this.TAG, "onCreatGoodsListGridView -->    position  = " + position + "; tabKey = " + tabKey);
        if (this.mGoodsGirdViewMap != null && tabKey != null && this.mGirdViewPadding != null) {
            if (this.mGoodsGirdViewMap.containsKey(tabKey)) {
                ZpLogger.i(this.TAG, "onCreatGoodsListGridView -->   GoodListLifeUiGridView  is contains ");
                return;
            }
            final TabGoodsBaseAdapter adapter = getTabGoodsAdapter(tabKey, position);
            final FocusFlipGridView goodListLifeUiGridView = getTabGoodsGridView(tabKey, position);
            if (DeviceJudge.isLowDevice()) {
                goodListLifeUiGridView.setDelayAnim(false);
            }
            goodListLifeUiGridView.setNumColumns(4);
            goodListLifeUiGridView.setFlipScrollFrameCount(5);
            goodListLifeUiGridView.setNeedAutoSearchFocused(false);
            goodListLifeUiGridView.setAnimateWhenGainFocus(false, true, true, true);
            goodListLifeUiGridView.setVerticalSpacing(getResources().getDimensionPixelSize(R.dimen.dp_20));
            goodListLifeUiGridView.setHorizontalSpacing(getResources().getDimensionPixelSize(R.dimen.dp_16));
            goodListLifeUiGridView.setStretchMode(0);
            initGirdViewInfo(goodListLifeUiGridView, adapter, tabKey, position);
            ZpLogger.i(this.TAG, "onCreatGoodsListGridView --> mGirdViewPadding = " + this.mGirdViewPadding);
            goodListLifeUiGridView.setPadding(this.mGirdViewPadding.left, this.mGirdViewPadding.top, this.mGirdViewPadding.right, this.mGirdViewPadding.bottom);
            adapter.onSetFocusFlipGridView(goodListLifeUiGridView);
            adapter.onSetMainHandler(this.mHandler);
            goodListLifeUiGridView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    ZpLogger.i(TabBaseActivity.this.TAG, TabBaseActivity.this.TAG + ".goodListLifeUiGridView.onFocusChange ---> v = " + v + "; hasFocus = " + hasFocus);
                    if (hasFocus) {
                        boolean unused = TabBaseActivity.this.mMenuFocusGain = false;
                        if (TabBaseActivity.this.mTabFocusPositionManager != null) {
                            if (TabBaseActivity.this.mGoodsFocusDrawable == null) {
                                TabBaseActivity.this.mGoodsFocusDrawable = new StaticFocusDrawable(TabBaseActivity.this.getResources().getDrawable(TabBaseActivity.this.getGoodsFocusDrawableId()));
                            }
                            ZpLogger.i(TabBaseActivity.this.TAG, TabBaseActivity.this.TAG + ".goodListLifeUiGridView.onFocusChange ---> mGoodsFocusDrawable = " + TabBaseActivity.this.mGoodsFocusDrawable);
                            TabBaseActivity.this.mTabFocusPositionManager.setSelector(TabBaseActivity.this.mGoodsFocusDrawable);
                        }
                    }
                    if (TabBaseActivity.this.mTabFocusFlipGridViewListener != null) {
                        TabBaseActivity.this.mTabFocusFlipGridViewListener.onFocusChange(goodListLifeUiGridView, tabKey, v, hasFocus);
                    }
                }
            });
            goodListLifeUiGridView.setOnFocusFlipGridViewListener(new FocusFlipGridView.OnFocusFlipGridViewListener() {
                public void onLayoutDone(boolean isFirst) {
                    TabBaseActivity.this.showGirdView(position);
                    if (TabBaseActivity.this.mTabFocusFlipGridViewListener != null) {
                        TabBaseActivity.this.mTabFocusFlipGridViewListener.onLayoutDone(goodListLifeUiGridView, tabKey, isFirst);
                    }
                    if (DeviceJudge.isLowDevice()) {
                    }
                }

                public void onOutAnimationDone() {
                    goodListLifeUiGridView.setVisibility(8);
                }

                public void onReachGridViewBottom() {
                }

                public void onReachGridViewTop() {
                }
            });
            goodListLifeUiGridView.setOnItemSelectedListener(new ItemSelectedListener() {
                public void onItemSelected(View selectview, int position, boolean isSelect, View parent) {
                    ZpLogger.i(TabBaseActivity.this.TAG, "goodListLifeUiGridView--> onItemSelected -->  selectview = " + selectview + "; position = " + position + ";  isSelect = " + isSelect);
                    if (TabBaseActivity.this.mTabFocusFlipGridViewListener != null) {
                        TabBaseActivity.this.mTabFocusFlipGridViewListener.onItemSelected(goodListLifeUiGridView, tabKey, selectview, position, isSelect, parent);
                    }
                    if ((selectview instanceof TabGoodsItemView) && selectview != null) {
                        TabBaseActivity.this.HandleSelectView(selectview, position, isSelect);
                    }
                }
            });
            adapter.setOnVerticalItemHandleListener(new VerticalItemHandleListener() {
                public boolean onGetview(ViewGroup container, String ordey, int position) {
                    if (TabBaseActivity.this.mTabFocusFlipGridViewListener == null) {
                        return true;
                    }
                    TabBaseActivity.this.mTabFocusFlipGridViewListener.onGetview(goodListLifeUiGridView, tabKey, position, TabBaseActivity.this.mCurrentSelectTabNumBer);
                    return true;
                }
            });
            goodListLifeUiGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View selectedView, int position, long row) {
                    TabGoodsBaseAdapter baseAdapter;
                    ZpLogger.i(TabBaseActivity.this.TAG, TabBaseActivity.this.TAG + ".goodListLifeUiGridView.onItemClick.AdapterView = " + adapterView + ".selectedView = " + selectedView + ".position = " + position + ".row_id = " + row);
                    if (TabBaseActivity.this.mTabFocusFlipGridViewListener == null || !TabBaseActivity.this.mTabFocusFlipGridViewListener.onItemClick(goodListLifeUiGridView, tabKey, adapterView, selectedView, position, row)) {
                        if (goodListLifeUiGridView.isScrolling()) {
                            goodListLifeUiGridView.forceResetFocusParams(TabBaseActivity.this.mTabFocusPositionManager);
                        }
                        String itemId = TabBaseActivity.this.getItemIdOfEnterdetail(goodListLifeUiGridView, tabKey, adapterView, selectedView, position, row);
                        String eurl = TabBaseActivity.this.getEurlOfEnterdetail(goodListLifeUiGridView, tabKey, adapterView, selectedView, position, row);
                        String uri = TabBaseActivity.this.getUriOfEnterDetail(goodListLifeUiGridView, tabKey, adapterView, selectedView, position, row);
                        String title = TabBaseActivity.this.getTitleOfEnterDetail(goodListLifeUiGridView, tabKey, adapterView, selectedView, position, row);
                        String picUrl = TabBaseActivity.this.getPicUrlOfEnterDetail(goodListLifeUiGridView, tabKey, adapterView, selectedView, position, row);
                        if (!TextUtils.isEmpty(itemId)) {
                            TabBaseActivity.this.enterDisplayDetail(itemId, (Map<String, String>) null);
                        } else if (!TextUtils.isEmpty(eurl)) {
                            TabBaseActivity.this.enterDisplayDetailAsync(title, picUrl, eurl, itemId);
                        } else if (!TextUtils.isEmpty(uri)) {
                            TabBaseActivity.this.enterDisplayDetailUri(uri);
                        }
                        ListAdapter listAdapter = (ListAdapter) adapterView.getAdapter();
                        if ((listAdapter instanceof TabGoodsBaseAdapter) && listAdapter != null && (baseAdapter = (TabGoodsBaseAdapter) listAdapter) != null) {
                            if (TextUtils.isEmpty(eurl)) {
                                ZpLogger.i("getFullPageName", "getFullPageName" + TabBaseActivity.this.getFullPageName());
                                if (TextUtils.isEmpty(TabBaseActivity.this.getFullPageName()) || !TabBaseActivity.this.getFullPageName().equals("TbGoodsSeachResult")) {
                                    TBS.Adv.ctrlClicked(CT.Button, TabBaseActivity.this.getFullPageName() + "_P_Goods_" + position, "itemId=" + itemId, "name=" + baseAdapter.getGoodsTitle(tabKey, position), "uuid=" + CloudUUIDWrapper.getCloudUUID(), "from_channel=" + TabBaseActivity.this.getmFrom());
                                    return;
                                }
                                Map<String, String> p = new HashMap<>();
                                p.put("itemId", itemId);
                                p.put("name", baseAdapter.getGoodsTitle(tabKey, position));
                                p.put("uuid", CloudUUIDWrapper.getCloudUUID());
                                p.put("from_channel", TabBaseActivity.this.getmFrom());
                                p.put(SPMConfig.SPM, SPMConfig.GOODS_LIST_SPM_ITEM_P_NAME);
                                Utils.utControlHit(TabBaseActivity.this.getFullPageName(), "Button-TbGoodsSeachResult_P_Goods_" + position, p);
                                Utils.updateNextPageProperties(SPMConfig.GOODS_LIST_SPM_ITEM_P_NAME);
                                return;
                            }
                            TBS.Adv.ctrlClicked(CT.Button, TabBaseActivity.this.getFullPageName() + "_Z_P_Goods_" + position, "itemId=" + itemId, "name=" + baseAdapter.getGoodsTitle(tabKey, position), "uuid=" + CloudUUIDWrapper.getCloudUUID(), "from_channel=" + TabBaseActivity.this.getmFrom());
                        }
                    }
                }
            });
            goodListLifeUiGridView.setOnFlipGridViewRunnableListener(new FlipGridView.OnFlipRunnableListener() {
                public void onFinished() {
                    ZpLogger.i(TabBaseActivity.this.TAG, "setOnFlipGridViewRunnableListener   onFinished ... mMenuFocusGain = " + TabBaseActivity.this.mMenuFocusGain);
                    int selectPos = goodListLifeUiGridView.getSelectedItemPosition();
                    adapter.onItemSelected(selectPos, true, goodListLifeUiGridView);
                    adapter.setIsNotifyDataSetChanged(false);
                    TabBaseActivity.this.onSetCheckVisibleItemOfAdapter(goodListLifeUiGridView, adapter);
                    if (!TabBaseActivity.this.mMenuFocusGain) {
                        View view = goodListLifeUiGridView.getSelectedView();
                        if ((view instanceof TabGoodsItemView) && view != null) {
                            TabBaseActivity.this.HandleSelectView(view, selectPos, true);
                        }
                    }
                    if (TabBaseActivity.this.mTabFocusFlipGridViewListener != null) {
                        TabBaseActivity.this.mTabFocusFlipGridViewListener.onFinished(goodListLifeUiGridView, tabKey);
                    }
                }

                public void onFlipItemRunnable(float arg0, View arg1, int arg2) {
                }

                public void onStart() {
                    ZpLogger.i(TabBaseActivity.this.TAG, "setOnFlipGridViewRunnableListener.onStart ... ");
                    if (TabBaseActivity.this.mTabFocusFlipGridViewListener != null) {
                        TabBaseActivity.this.mTabFocusFlipGridViewListener.onStart(goodListLifeUiGridView, tabKey);
                    }
                    ImageLoaderManager.get().cancelLoadAllTaskFor();
                }
            });
            adapter.onSetTabKey(tabKey);
            goodListLifeUiGridView.setAdapter((ListAdapter) adapter);
            goodListLifeUiGridView.setVisibility(8);
            if (this.mGoodsListDisplayContainer != null) {
                FrameLayout.LayoutParams contanerLp = new FrameLayout.LayoutParams(this.mGirdViewWidth, this.mGirdViewHeight);
                contanerLp.setMargins(this.mGirdViewMargin.left, this.mGirdViewMargin.top, 0, 0);
                contanerLp.gravity = 1;
                this.mGoodsListDisplayContainer.addView(goodListLifeUiGridView, contanerLp);
                if (this.mTabFocusPositionManager != null) {
                    this.mTabFocusPositionManager.setGridView(goodListLifeUiGridView);
                }
            }
            this.mGoodsGirdViewMap.put(tabKey, goodListLifeUiGridView);
            this.mTabGoodsFirstRequestMap.put(tabKey, true);
        }
    }

    /* access modifiers changed from: protected */
    public void onSetCheckVisibleItemOfAdapter(FocusFlipGridView fenLeiGoodsGridView, TabGoodsBaseAdapter adapter) {
        adapter.onSetCheckVisibleItem(true);
        adapter.onCheckVisibleItemAndLoadBitmap();
    }

    public void setTabFocusListViewListener(TabFocusListViewListener tabFocusListViewListener) {
        this.mTabFocusListViewListener = tabFocusListViewListener;
    }

    public void setTabFocusFlipGridViewListener(TabFocusFlipGridViewListener tabFocusFlipGridViewListener) {
        this.mTabFocusFlipGridViewListener = tabFocusFlipGridViewListener;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0 && this.mTabFocusPositionManager != null) {
            this.mTabFocusPositionManager.setCanScroll(false);
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyAction = event.getAction();
        int keyCode = event.getKeyCode();
        ZpLogger.i(this.TAG, "dispatchKeyEvent --> keyCode = " + keyCode + "; mCanMoveRight = " + this.mCanMoveRight + "; mMenuFocusGain = " + this.mMenuFocusGain);
        if (event.getAction() == 0 && this.mTabFocusPositionManager != null) {
            this.mTabFocusPositionManager.setCanScroll(true);
        }
        if (keyCode == 22 && !this.mCanMoveRight && this.mMenuFocusGain) {
            return true;
        }
        boolean dispatchKeyEvent = super.dispatchKeyEvent(event);
        if (keyAction == 1) {
            if (!this.mMenuFocusGain) {
                return dispatchKeyEvent;
            }
            if (this.newTabNumBer == -1) {
                this.mCanMoveRight = true;
                return dispatchKeyEvent;
            } else if (this.mHandler == null) {
                return dispatchKeyEvent;
            } else {
                this.mCanMoveRight = false;
                this.mHandler.sendEmptyMessageDelayed(1000, 500);
                return dispatchKeyEvent;
            }
        } else if (keyAction != 0 || this.mHandler == null) {
            return dispatchKeyEvent;
        } else {
            this.mHandler.removeMessages(1000);
            return dispatchKeyEvent;
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        onClearBufferData();
        super.onDestroy();
    }

    public void onClearBufferData() {
        if (this.mGoodsGirdViewMap != null) {
            this.mGoodsGirdViewMap.clear();
        }
        if (this.mTabGoodsFirstRequestMap != null) {
            this.mTabGoodsFirstRequestMap.clear();
        }
        if (this.mGoodsListDisplayContainer != null) {
            this.mGoodsListDisplayContainer.removeAllViews();
        }
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages((Object) null);
        }
    }

    /* access modifiers changed from: protected */
    public void onHandleCommonBackgroudColor(int backgroudcolor) {
        if (this.mTabFocusPositionManager != null) {
            this.mTabFocusPositionManager.setBackgroudColor(backgroudcolor);
        }
    }

    /* access modifiers changed from: private */
    public void onHandleKeyUpEvent() {
        ZpLogger.i(this.TAG, "onHandleKeyUpEvent -->    newTabNumBer  = " + this.newTabNumBer + "; oldTabNumBer = " + this.oldTabNumBer);
        if (this.newTabNumBer != -1) {
            onHandleTabFoucus(this.oldTabNumBer, false);
            onHandleTabFoucus(this.newTabNumBer, true);
            this.oldTabNumBer = this.newTabNumBer;
            this.newTabNumBer = -1;
        }
    }

    private void onHandleTabFoucus(int postion, boolean hasFocus) {
        if (this.mGoodsGirdViewMap != null) {
            String tabKeyString = getTabKeyWordOfTabNumBer(postion);
            ZpLogger.i(this.TAG, "onHandleTabFoucus -->   mCurrentSelectTabNumBer   = " + this.mCurrentSelectTabNumBer + ";  tabKeyString  = " + tabKeyString + "; hasFocus = " + hasFocus + "; postion = " + postion);
            if (!TextUtils.isEmpty(tabKeyString)) {
                onCreatGoodsListGridView(tabKeyString, postion);
                if (hasFocus) {
                    if (this.mCurrentSelectTabNumBer != postion) {
                        String oldtabText = null;
                        if (this.mCurrentSelectTabNumBer >= 0) {
                            oldtabText = getTabKeyWordOfTabNumBer(this.mCurrentSelectTabNumBer);
                        }
                        FocusFlipGridView oldGirdview = null;
                        if (!TextUtils.isEmpty(oldtabText)) {
                            oldGirdview = this.mGoodsGirdViewMap.get(oldtabText);
                        }
                        String currenttabText = null;
                        if (postion >= 0) {
                            currenttabText = getTabKeyWordOfTabNumBer(postion);
                        }
                        FocusFlipGridView currentGirdview = null;
                        if (!TextUtils.isEmpty(currenttabText)) {
                            currentGirdview = this.mGoodsGirdViewMap.get(currenttabText);
                        }
                        if (!(this.mTabFocusPositionManager == null || currentGirdview == null)) {
                            this.mTabFocusPositionManager.setGridView(currentGirdview);
                        }
                        handlerChangeTab(postion, currentGirdview, this.mCurrentSelectTabNumBer, oldGirdview, hasFocus);
                    }
                    this.mCurrentSelectTabNumBer = postion;
                    this.mCanMoveRight = true;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void showGirdView(int currentIndex) {
        ZpLogger.i(this.TAG, "hideGirdView --> currentIndex = " + currentIndex + "; mGoodsGirdViewMap = " + this.mGoodsGirdViewMap);
        if (this.mGoodsGirdViewMap != null) {
            for (Map.Entry<String, FocusFlipGridView> entry : this.mGoodsGirdViewMap.entrySet()) {
                String tabKey = entry.getKey();
                FocusFlipGridView focusFlipGridView = entry.getValue();
                String currentKey = null;
                if (currentIndex >= 0) {
                    currentKey = getTabKeyWordOfTabNumBer(currentIndex);
                }
                boolean isCurrent = false;
                if (currentKey != null) {
                    isCurrent = currentKey.equals(tabKey);
                }
                ZpLogger.i(this.TAG, "showGirdView --> focusFlipGridView = " + focusFlipGridView + "; isCurrent = " + isCurrent);
                if (focusFlipGridView != null && !isCurrent) {
                    focusFlipGridView.setVisibility(8);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void enterDisplayDetailUri(String uri) {
        ZpLogger.i("url", "url = " + uri);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(uri));
        intent.putExtra(CoreIntentKey.URI_FROM_APP_BUNDLE, getAppName());
        startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void enterDisplayDetail(String itemId, Map<String, String> exParams) {
        ZpLogger.i(this.TAG, "enterDisplayDetail itemId = " + itemId + "is");
        if (!TextUtils.isEmpty(itemId)) {
            if (!NetWorkUtil.isNetWorkAvailable()) {
                showNetworkErrorDialog(false);
                return;
            }
            try {
                StringBuilder urlBuilder = new StringBuilder("tvtaobao://home?app=taobaosdk&module=detail&itemId=");
                urlBuilder.append(itemId);
                if (exParams != null) {
                    for (String key : exParams.keySet()) {
                        urlBuilder.append(String.format("&%s=%s", new Object[]{key, exParams.get(key)}));
                    }
                }
                String url = urlBuilder.toString();
                ZpLogger.i("url", "url = " + url);
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(url));
                intent.putExtra(CoreIntentKey.URI_FROM_APP_BUNDLE, getAppName());
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, getResources().getString(R.string.ytbv_not_open), 0).show();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void HandleSelectView(View selectview, int position, boolean isSelect) {
        ZpLogger.i(this.TAG, "HandleSelectView  -->    position = " + position + "; isSelect  = " + isSelect + "; selectview = " + selectview);
        if ((selectview instanceof TabGoodsItemView) && selectview != null) {
            ((TabGoodsItemView) selectview).onItemSelected(isSelect, (View) null);
        }
    }

    private static final class TabBaseHandler extends Handler {
        private final WeakReference<TabBaseActivity> weakReference;

        public TabBaseHandler(WeakReference<TabBaseActivity> weakReference2) {
            this.weakReference = weakReference2;
        }

        public void handleMessage(Message msg) {
            TabBaseActivity tabBaseActivity = (TabBaseActivity) this.weakReference.get();
            if (tabBaseActivity != null) {
                switch (msg.what) {
                    case 1000:
                        tabBaseActivity.onHandleKeyUpEvent();
                        return;
                    case 1002:
                        tabBaseActivity.focusPositionManagerrequestFocus();
                        return;
                    default:
                        return;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public FocusPositionManager getFocusPositionManager() {
        return (FocusPositionManager) findViewById(R.id.common_havetab_mainLayout);
    }
}
