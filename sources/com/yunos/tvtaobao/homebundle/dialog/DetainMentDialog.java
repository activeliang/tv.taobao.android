package com.yunos.tvtaobao.homebundle.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.yunos.tv.blitz.account.LoginHelper;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.account.LoginHelperImpl;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.common.User;
import com.yunos.tv.core.config.TvOptionsChannel;
import com.yunos.tv.core.config.TvOptionsConfig;
import com.yunos.tv.core.util.DeviceUtil;
import com.yunos.tv.core.util.NetWorkUtil;
import com.yunos.tv.core.util.TaokeConst;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.activity.BaseActivity;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.dialog.util.SnapshotUtil;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.bo.DetainMentBo;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeBannerBean;
import com.yunos.tvtaobao.biz.request.bo.GuessLikeGoodsData;
import com.yunos.tvtaobao.biz.request.bo.KMGoods;
import com.yunos.tvtaobao.biz.util.GuessLikeUtils;
import com.yunos.tvtaobao.homebundle.R;
import com.yunos.tvtaobao.homebundle.activity.HomeActivity;
import com.yunos.tvtaobao.homebundle.adapter.DetainMentAdapter;
import com.yunos.tvtaobao.homebundle.bean.DetainmentDataBuider;
import com.yunos.tvtaobao.homebundle.listener.DetainMentFronstedGlassListenner;
import com.yunos.tvtaobao.homebundle.view.DetainMentItemLayout;
import com.yunos.tvtaobao.homebundle.view.DetainMentListView;
import com.yunos.tvtaobao.homebundle.view.DetainMentTextView;
import com.yunos.tvtaobao.homebundle.view.TouchFocusLinearLayout;
import com.yunos.tvtaobao.payment.config.DebugConfig;
import com.yunos.tvtaobao.tvsdk.widget.AdapterView;
import com.yunos.tvtaobao.tvsdk.widget.focus.FocusPositionManager;
import com.yunos.tvtaobao.tvsdk.widget.focus.StaticFocusDrawable;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.ItemSelectedListener;
import com.yunos.tvtaobao.tvsdk.widget.focus.listener.OnScrollListener;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class DetainMentDialog extends Dialog {
    public static final String OPT_RESULT = "opt_result";
    private static final String PAGENAME = "Home";
    public static final int REQUEST_CODE = 1;
    public static final int RESULT_CODE = 10;
    private static final String TAG = "DetainMentDialog";
    /* access modifiers changed from: private */
    public int firstExposePositionmin = 0;
    /* access modifiers changed from: private */
    public int lastExposePosition = -1;
    /* access modifiers changed from: private */
    public Context mContext;
    private DetainMentAdapter mDetainMentAdapter;
    private List<GuessLikeGoodsData> mDetainMentBo_Data;
    private DetainMentFronstedGlassListenner mDetainMentFronstedGlassListenner;
    private DetainMentSyncLoginListener mDetainMentSyncLoginListener;
    private DetainmentDataBuider mDetainmentDataBuider;
    /* access modifiers changed from: private */
    public DetainMentListView mFocusHListView;
    private TouchFocusLinearLayout mFocusLinearLayout;
    /* access modifiers changed from: private */
    public FocusPositionManager mFocusPositionManager;

    public DetainMentDialog(Context context) {
        super(context, R.style.ytbv_homeDialog);
        onInitDetainMentActivity(context);
        onHandleTaokeBtoc();
    }

    private void onHandleTaokeBtoc() {
        if (LoginHelperImpl.getJuLoginHelper().isLogin()) {
            BusinessRequest.getBusinessRequest().requestTaokeJHSListAnalysis(DeviceUtil.initMacAddress(this.mContext), User.getNick(), "tvtaobao", TaokeConst.REFERER_HOME_ACTIVITY$DETAIN_MENT_DIALOG, (RequestListener<JSONObject>) null);
        }
    }

    public void onDestroy() {
        if (this.mDetainMentFronstedGlassListenner != null) {
            this.mDetainMentFronstedGlassListenner.onRecycleBitmap();
        }
        if (this.mDetainMentFronstedGlassListenner != null) {
            this.mDetainMentFronstedGlassListenner.onDestroy();
        }
        if (this.mDetainMentAdapter != null) {
            this.mDetainMentAdapter.onDestroy();
        }
        removeAccountListen();
    }

    public void show() {
        super.show();
        if (this.mContext instanceof Activity) {
            SnapshotUtil.getFronstedSreenShot(new WeakReference((Activity) this.mContext), 5, 0.0f, this.mDetainMentFronstedGlassListenner);
        }
        if (this.mDetainMentAdapter == null) {
            this.mDetainMentAdapter = new DetainMentAdapter(this.mContext);
            this.mFocusHListView.setAdapter((ListAdapter) this.mDetainMentAdapter);
        }
        if (this.mFocusLinearLayout != null) {
            this.mFocusLinearLayout.setOnFocusChanged();
        }
        ZpLogger.v(TAG, "DetainMentDialog.show --> mDetainMentBo_Data = " + this.mDetainMentBo_Data);
        if (!(this.mDetainMentBo_Data == null || this.mDetainMentAdapter == null)) {
            this.mDetainMentAdapter.setData(this.mDetainMentBo_Data);
            this.mDetainMentAdapter.notifyDataSetChanged();
            firstRequestFocus();
        }
        this.lastExposePosition = this.mFocusHListView.getLastVisiblePosition();
        this.firstExposePositionmin = this.mFocusHListView.getFirstVisiblePosition();
        ZpLogger.v(TAG, "DetainMentDialog.show --> lastExposePosition = " + this.lastExposePosition);
        ZpLogger.v(TAG, "DetainMentDialog.show --> firstExposePositionmin = " + this.firstExposePositionmin);
    }

    public void onBackPressed() {
        if (this.mFocusPositionManager.getFocused() instanceof DetainMentListView) {
            firstRequestFocus();
            return;
        }
        Utils.utControlHit(PAGENAME, "Button_Back", getProperties());
        if (this.mContext instanceof HomeActivity) {
            ((HomeActivity) this.mContext).handleExit();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 || DebugConfig.whetherIsMonkey()) {
            return super.onKeyDown(keyCode, event);
        }
        onBackPressed();
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int keycode;
        if (event != null && (((keycode = event.getKeyCode()) == 20 || keycode == 19 || keycode == 21 || keycode == 22) && this.mFocusPositionManager != null && !this.mFocusPositionManager.IsFocusStarted())) {
            this.mFocusPositionManager.focusShow();
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action;
        if (ev != null && (((action = ev.getAction()) == 0 || action == 2) && this.mFocusPositionManager != null && this.mFocusPositionManager.IsFocusStarted())) {
            this.mFocusPositionManager.focusHide();
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean hasData() {
        if (this.mDetainMentAdapter != null && !this.mDetainMentAdapter.isEmpty()) {
            return true;
        }
        if (this.mDetainMentBo_Data == null || this.mDetainMentBo_Data.size() <= 0) {
            return false;
        }
        return true;
    }

    private void addAccountListen() {
        if (this.mDetainMentSyncLoginListener == null) {
            this.mDetainMentSyncLoginListener = new DetainMentSyncLoginListener(new WeakReference(this));
        }
        CoreApplication.getLoginHelper(this.mContext.getApplicationContext()).addSyncLoginListener(this.mDetainMentSyncLoginListener);
    }

    private void removeAccountListen() {
        if (this.mDetainMentSyncLoginListener != null) {
            CoreApplication.getLoginHelper(this.mContext.getApplicationContext()).removeSyncLoginListener(this.mDetainMentSyncLoginListener);
        }
    }

    private void onInitDetainMentActivity(Context context) {
        setContentView(R.layout.ytm_activity_detainment);
        this.mContext = context;
        this.mFocusPositionManager = (FocusPositionManager) findViewById(R.id.detainment_focusmanager);
        this.mFocusPositionManager.setSelector(new StaticFocusDrawable(this.mContext.getResources().getDrawable(R.drawable.ytbv_common_focus)));
        this.mDetainMentFronstedGlassListenner = new DetainMentFronstedGlassListenner(new WeakReference(this.mFocusPositionManager));
        onInitDetainMent();
        addAccountListen();
        this.mDetainMentBo_Data = null;
        this.mDetainmentDataBuider = new DetainmentDataBuider(context);
        onRequestDetainMent();
    }

    private void onInitDetainMent() {
        this.mFocusHListView = (DetainMentListView) findViewById(R.id.listview);
        this.mFocusHListView.setSpacing(this.mContext.getResources().getDimensionPixelSize(R.dimen.dp_18));
        this.mFocusHListView.setFlipScrollFrameCount(3);
        this.mFocusHListView.setOnItemSelectedListener(new ItemSelectedListener() {
            public void onItemSelected(View v, int position, boolean isSelected, View view) {
                if (!isSelected) {
                    DetainMentDialog.this.HandleSelectView(v, position, isSelected);
                } else if (position < DetainMentAdapter.PING_COUNT || position > DetainMentAdapter.TOTAL_COUNT - (DetainMentAdapter.PING_COUNT / 2)) {
                    DetainMentDialog.this.HandleSelectView(v, position, isSelected);
                    if (isSelected && DetainMentDialog.this.mFocusPositionManager != null) {
                        DetainMentDialog.this.mFocusPositionManager.forceDrawFocus();
                    }
                }
            }
        });
        this.mFocusHListView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(ViewGroup view, int scrollState) {
                if (scrollState == 0) {
                    int position = DetainMentDialog.this.mFocusHListView.getSelectedItemPosition();
                    DetainMentDialog.this.HandleSelectView(DetainMentDialog.this.mFocusHListView.getSelectedView(), position, true);
                    if (DetainMentDialog.this.mFocusPositionManager != null) {
                        DetainMentDialog.this.mFocusPositionManager.forceDrawFocus();
                    }
                    int lastPosition = DetainMentDialog.this.mFocusHListView.getLastVisiblePosition();
                    if (lastPosition > DetainMentDialog.this.lastExposePosition) {
                        int unused = DetainMentDialog.this.lastExposePosition = lastPosition;
                    }
                    int firstVisiblePosition = DetainMentDialog.this.mFocusHListView.getFirstVisiblePosition();
                    if (firstVisiblePosition < DetainMentDialog.this.firstExposePositionmin) {
                        int unused2 = DetainMentDialog.this.firstExposePositionmin = firstVisiblePosition;
                    }
                    ZpLogger.i(DetainMentDialog.TAG, "lastExposePosition  = " + DetainMentDialog.this.mFocusHListView.getLastVisiblePosition());
                    ZpLogger.i(DetainMentDialog.TAG, "firstExposePositionmin  = " + DetainMentDialog.this.mFocusHListView.getFirstVisiblePosition());
                }
            }

            public void onScroll(ViewGroup view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        this.mFocusHListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ListAdapter listAdapter;
                Object object;
                if (DetainMentDialog.this.mFocusHListView != null && (listAdapter = DetainMentDialog.this.mFocusHListView.getAdapter()) != null && (object = listAdapter.getItem(position)) != null && (object instanceof GuessLikeGoodsData)) {
                    GuessLikeGoodsData guessLikeGoodsData = (GuessLikeGoodsData) object;
                    if (GuessLikeGoodsData.TYPE_ITEM.equals(guessLikeGoodsData.getType())) {
                        String itemId = guessLikeGoodsData.getGuessLikeGoods().getTid();
                        DetainMentDialog.this.enterDisplayDetail(itemId, (String) null, (String) null, (Map<String, String>) null);
                        Map<String, String> pMap = DetainMentDialog.this.getProperties();
                        pMap.put("item_id", itemId);
                        pMap.put("position", position + "");
                        Utils.utControlHit(DetainMentDialog.PAGENAME, "Button_Card", pMap);
                    }
                    if (GuessLikeGoodsData.TYPE_ZTC.equals(guessLikeGoodsData.getType())) {
                        KMGoods kmGoods = guessLikeGoodsData.getKmGoods();
                        Map<String, String> pMap2 = DetainMentDialog.this.getProperties();
                        pMap2.put("item_id", kmGoods.getResourceid());
                        pMap2.put("position", position + "");
                        Utils.utControlHit(DetainMentDialog.PAGENAME, "Button_Card", pMap2);
                        Map<String, String> exparams = new HashMap<>();
                        exparams.put(BaseConfig.INTENT_KEY_ISZTC, "true");
                        DetainMentDialog.this.enterDisplayDetail(kmGoods.getResourceid(), kmGoods.getSdkurl(), kmGoods.getEurl(), exparams);
                    }
                    if (GuessLikeGoodsData.TYPE_BANNER.equals(guessLikeGoodsData.getType())) {
                        Map<String, String> pMap3 = DetainMentDialog.this.getProperties();
                        pMap3.put("position", position + "");
                        Utils.utControlHit(DetainMentDialog.PAGENAME, "Fulibao", pMap3);
                        GuessLikeBannerBean guessLikeBannerBean = guessLikeGoodsData.getGuessLikeBannerBean();
                        if (!TextUtils.isEmpty(guessLikeBannerBean.getUri())) {
                            ZpLogger.i(DetainMentDialog.TAG, guessLikeBannerBean.getUri());
                            Intent intent = new Intent("android.intent.action.VIEW");
                            intent.setData(Uri.parse(guessLikeBannerBean.getUri()));
                            DetainMentDialog.this.mContext.startActivity(intent);
                        }
                    }
                }
            }
        });
        this.mFocusHListView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View arg0, boolean arg1) {
                if (arg1) {
                    int position = DetainMentDialog.this.mFocusHListView.getSelectedItemPosition();
                    DetainMentDialog.this.HandleSelectView(DetainMentDialog.this.mFocusHListView.getSelectedView(), position, true);
                }
            }
        });
        ((DetainMentTextView) findViewById(R.id.leave_app)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Utils.utControlHit(DetainMentDialog.PAGENAME, "Button_Leave", DetainMentDialog.this.getProperties());
                if ((DetainMentDialog.this.mContext instanceof HomeActivity) && !DebugConfig.whetherIsMonkey()) {
                    ((HomeActivity) DetainMentDialog.this.mContext).handleExit();
                }
            }
        });
        ((DetainMentTextView) findViewById(R.id.goshoping)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Utils.utControlHit(DetainMentDialog.PAGENAME, "Button_Stay", DetainMentDialog.this.getProperties());
                DetainMentDialog.this.dismiss();
            }
        });
        initButton();
    }

    /* access modifiers changed from: private */
    public void enterDisplayDetail(String itemId, String sdkurl, String eurl, Map<String, String> exParams) {
        ZpLogger.i(TAG, "enterDisplayDetail itemId  = " + itemId);
        if (this.mContext instanceof HomeActivity) {
            HomeActivity baseActivity = (HomeActivity) this.mContext;
            if (!TextUtils.isEmpty(itemId)) {
                if (!NetWorkUtil.isNetWorkAvailable()) {
                    baseActivity.showNetworkErrorDialog(false);
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
                    if (sdkurl == null) {
                        sdkurl = "";
                    }
                    intent.putExtra(BaseConfig.INTENT_KEY_SDKURL, sdkurl);
                    if (eurl == null) {
                        eurl = "";
                    }
                    intent.putExtra("eurl", eurl);
                    TvOptionsConfig.setTvOptionsChannel(TvOptionsChannel.GUESSLIKE);
                    baseActivity.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(baseActivity, baseActivity.getResources().getString(R.string.ytbv_not_open), 0).show();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void HandleSelectView(View selectview, int position, boolean isSelect) {
        ZpLogger.i(TAG, "HandleSelectView  -->    position = " + position + "; isSelect  = " + isSelect + "; selectview = " + selectview);
        if ((selectview instanceof DetainMentItemLayout) && selectview != null) {
            DetainMentItemLayout detainMentItemLayout = (DetainMentItemLayout) selectview;
            detainMentItemLayout.setSelect(isSelect);
            TextView title = (TextView) detainMentItemLayout.findViewById(R.id.detainment_item_title);
            if (title != null) {
                int item_W = selectview.getWidth();
                if (isSelect) {
                    int select_H = selectview.getResources().getDimensionPixelSize(R.dimen.dp_85);
                    int select_top = selectview.getResources().getDimensionPixelSize(R.dimen.dp_196);
                    FrameLayout.LayoutParams titleLp = new FrameLayout.LayoutParams(item_W, select_H);
                    titleLp.setMargins(0, select_top, 0, 0);
                    title.setLayoutParams(titleLp);
                    title.setLines(2);
                } else {
                    int no_select_H = selectview.getResources().getDimensionPixelSize(R.dimen.dp_50);
                    int no_select_top = selectview.getResources().getDimensionPixelSize(R.dimen.dp_220);
                    FrameLayout.LayoutParams titleLp2 = new FrameLayout.LayoutParams(item_W, no_select_H);
                    titleLp2.setMargins(0, no_select_top, 0, 0);
                    title.setLayoutParams(titleLp2);
                    title.setLines(1);
                }
                detainMentItemLayout.invalidate();
            }
        }
    }

    public void getAccount() {
        CoreApplication.getLoginHelper(this.mContext.getApplicationContext()).login(this.mContext);
    }

    public void onRequestDetainMent() {
        String current_userId = User.getUserId();
        ZpLogger.v(TAG, "DetainMentDialog.onRequestDetainMent --> current_userId = " + current_userId);
        if (this.mDetainmentDataBuider != null && (this.mContext instanceof BaseActivity)) {
            this.mDetainmentDataBuider.onRequestData(current_userId, new DetainmentRequestDonelistener(new WeakReference(this)));
        }
    }

    public void backgroundRequest() {
        if (this.mDetainmentDataBuider != null) {
            this.mDetainmentDataBuider.checkDetainmentData();
        }
    }

    private void firstRequestFocus() {
        DetainMentTextView detainMentTextView_shopping = (DetainMentTextView) findViewById(R.id.goshoping);
        detainMentTextView_shopping.setSelected(false);
        detainMentTextView_shopping.setScaleX(1.0f);
        detainMentTextView_shopping.setScaleY(1.0f);
        DetainMentTextView detainMentTextView_leave = (DetainMentTextView) findViewById(R.id.leave_app);
        detainMentTextView_leave.setSelected(true);
        if (this.mFocusPositionManager == null || !this.mFocusPositionManager.IsFocusStarted()) {
            setButtonBackground(detainMentTextView_shopping, false);
            setButtonBackground(detainMentTextView_leave, false);
            return;
        }
        this.mFocusPositionManager.setFirstFocusChild(this.mFocusLinearLayout);
        this.mFocusLinearLayout.forceInitNode();
        setButtonBackground(detainMentTextView_shopping, false);
        setButtonBackground(detainMentTextView_leave, true);
        this.mFocusLinearLayout.setSelectedView(detainMentTextView_leave, 0);
        this.mFocusPositionManager.requestFocus(this.mFocusLinearLayout, 130);
        ZpLogger.i(TAG, "firstRequestFocus -->   detainMentTextView_leave = " + detainMentTextView_leave + "; detainMentTextView_shopping = " + detainMentTextView_shopping);
    }

    /* access modifiers changed from: private */
    public void setButtonBackground(View button, boolean isSelected) {
        if (button == null) {
            return;
        }
        if (!isSelected || this.mFocusPositionManager == null || !this.mFocusPositionManager.IsFocusStarted()) {
            button.setBackgroundResource(R.drawable.ytm_detainment_button_bg);
        } else {
            button.setBackgroundResource(R.drawable.ytm_button_common_focus);
        }
    }

    private void initButton() {
        this.mFocusLinearLayout = (TouchFocusLinearLayout) findViewById(R.id.button_linearLayout);
        this.mFocusLinearLayout.setAutoSearchFocus(false);
        this.mFocusLinearLayout.setOnItemSelectedListener(new ItemSelectedListener() {
            public void onItemSelected(View v, int position, boolean isSelected, View view) {
                DetainMentDialog.this.setButtonBackground(v, isSelected);
            }
        });
    }

    public void onResponseSuccess(List<GuessLikeGoodsData> guessLikeGoodsDatas) {
        if (guessLikeGoodsDatas != null && guessLikeGoodsDatas.size() > 0 && this.mFocusHListView != null) {
            if (this.mDetainMentAdapter == null || this.mDetainMentAdapter.getCount() > 0) {
                this.mDetainMentBo_Data = guessLikeGoodsDatas;
                return;
            }
            this.mDetainMentAdapter.setData(guessLikeGoodsDatas);
            this.mDetainMentAdapter.notifyDataSetChanged();
            firstRequestFocus();
        }
    }

    private static class DetainmentRequestDonelistener implements DetainmentDataBuider.DetainmentRequestListener {
        protected final WeakReference<DetainMentDialog> mRefDetainMentDialog;

        public DetainmentRequestDonelistener(WeakReference<DetainMentDialog> baseActivityRef) {
            this.mRefDetainMentDialog = baseActivityRef;
        }

        public boolean onDetainmentRequestDone(List<GuessLikeGoodsData> guessLikeGoodsBean) {
            if (this.mRefDetainMentDialog == null || this.mRefDetainMentDialog.get() == null) {
                return true;
            }
            ((DetainMentDialog) this.mRefDetainMentDialog.get()).onResponseSuccess(guessLikeGoodsBean);
            return true;
        }

        private DetainMentBo[] filterRecommendData(DetainMentBo[] data) {
            int index;
            if (data != null && data.length > 0) {
                String filterText = CoreApplication.getApplication().getResources().getString(R.string.ytsdk_detainment_weinituijian);
                for (DetainMentBo bo : data) {
                    if (bo != null) {
                        String title_temp = bo.getTitle();
                        if (!TextUtils.isEmpty(title_temp) && (index = title_temp.indexOf(filterText, 0)) >= 0 && index < filterText.length()) {
                            try {
                                bo.setTitle(title_temp.substring(index + filterText.length(), title_temp.length()));
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
            return data;
        }
    }

    private static class DetainMentSyncLoginListener implements LoginHelper.SyncLoginListener {
        private WeakReference<DetainMentDialog> mRefDetainMentDialog;

        public DetainMentSyncLoginListener(WeakReference<DetainMentDialog> ref) {
            this.mRefDetainMentDialog = ref;
        }

        public void onLogin(boolean success) {
            ZpLogger.i(DetainMentDialog.TAG, "refreshData onLogin -->   =" + success);
            if (success && this.mRefDetainMentDialog != null && this.mRefDetainMentDialog.get() != null) {
                ((DetainMentDialog) this.mRefDetainMentDialog.get()).onRequestDetainMent();
            }
        }
    }

    /* access modifiers changed from: private */
    public Map<String, String> getProperties() {
        Map<String, String> properties = Utils.getProperties();
        properties.put("is_login", User.isLogined() ? "true" : "false");
        return properties;
    }

    public void dismiss() {
        String itemIds = getExposeItemIds();
        ZpLogger.i(TAG, "dismiss lastExposePosition");
        Map<String, String> p = new HashMap<>();
        p.put("itemId", itemIds);
        Utils.utCustomHit(((HomeActivity) this.mContext).getFullPageName(), "shengyicanmou", p);
        Map<String, String> map = getProperties();
        map.put("item_id", itemIds);
        Utils.utCustomHit("Expore_Exit", map);
        super.dismiss();
    }

    private String getExposeItemIds() {
        if (this.lastExposePosition < 0 && this.mDetainMentBo_Data != null) {
            if (this.mDetainMentBo_Data.size() > 6) {
                this.lastExposePosition = 5;
            } else {
                this.lastExposePosition = this.mDetainMentBo_Data.size();
            }
        }
        return GuessLikeUtils.getItemString(this.firstExposePositionmin, this.lastExposePosition, this.mDetainMentBo_Data);
    }
}
