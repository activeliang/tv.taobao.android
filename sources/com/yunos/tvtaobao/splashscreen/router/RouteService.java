package com.yunos.tvtaobao.splashscreen.router;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.taobao.detail.DisplayTypeConstants;
import com.tvtaobao.voicesdk.request.ASRUTRequest;
import com.tvtaobao.voicesdk.type.DomainType;
import com.yunos.RunMode;
import com.yunos.ott.sdk.core.Environment;
import com.yunos.tv.core.RtEnv;
import com.yunos.tv.core.aqm.ActivityQueueManager;
import com.yunos.tv.core.common.CoreIntentKey;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.config.SystemConfig;
import com.yunos.tv.core.util.DeviceUtil;
import com.yunos.tv.core.util.IntentDataUtil;
import com.yunos.tv.core.util.NetWorkUtil;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.activity.CoreActivity;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.base.BaseMtopRequest;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import com.yunos.tvtaobao.biz.util.StringUtil;
import com.yunos.tvtaobao.splashscreen.activity.StartActivity;
import com.yunos.tvtaobao.splashscreen.router.Page404Activity;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RouteService extends Service {
    private static final String INTENT_KEY_APP = "app";
    private static final String INTENT_KEY_FROM_PROCESS = "fromprocess";
    private static final String INTENT_KEY_INNER_URI = "inneruri";
    private static final String INTENT_KEY_MODULE = "module";
    private static final String INTENT_KEY_NOT_SHOW_LOADING = "notshowloading";
    private static final String INTENT_KEY_SPM = "spm";
    /* access modifiers changed from: private */
    public static final String TAG = RouteService.class.getSimpleName();
    RouteImpl routeImpl = new RouteImpl();

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        int onStartCommand = super.onStartCommand(intent, flags, startId);
        processCommand(intent, flags, startId);
        return 2;
    }

    private void processCommand(Intent intent, int flags, int startId) {
        ZpLogger.d(TAG, ".processCommand(" + intent + ", " + flags + ", " + startId + ")");
        if (intent != null) {
            if (GlobalConfigInfo.getInstance().getGlobalConfig() == null) {
                GlobalConfigInfo.getInstance().getTvtaobaoSwitch(this);
            }
            if (TextUtils.isEmpty(DeviceUtil.getStbID())) {
                DeviceUtil.initMacAddress(this);
                ZpLogger.d(TAG, "DeviceUtil.initMacAddress() rtn :[" + DeviceUtil.getStbID() + "]");
            }
            if (!NetWorkUtil.isNetWorkAvailable() || isTest404(intent)) {
                ZpLogger.d(TAG, "network not available");
                Page404Activity.start(this, Page404Activity.Type.networkError);
            } else if (RunMode.isYunos() != Environment.getInstance().isYunos()) {
                ZpLogger.d(TAG, "YunOS D-mode not fit");
                smartStartActivity((Context) this, BaseConfig.SWITCH_TO_YUNOSORDMODE_ACTIVITY);
            } else if (intent.getData() != null) {
                ZpLogger.i(TAG, ".routeImpl.processIntent() rtn=" + this.routeImpl.processIntent(intent));
            } else if (isNotShowLoading(intent)) {
                smartStartActivity((Context) this, (Class<? extends Activity>) StartActivity.class);
            } else if (isFromProcess(intent)) {
                ZpLogger.i(TAG, ".routeImpl.processIntent() rtn=" + this.routeImpl.processIntent(intent));
            } else {
                ZpLogger.v(TAG, " go home ");
                smartStartActivity((Context) this, BaseConfig.SWITCH_TO_HOME_ACTIVITY);
            }
        }
    }

    public static Bundle decodeUri(Uri uri) {
        String value;
        ZpLogger.i(TAG, TAG + ".decodeUri uri=" + uri.toString());
        if (uri.getEncodedQuery() == null) {
            return null;
        }
        try {
            Bundle bundle = new Bundle();
            Set<String> params = null;
            if (Build.VERSION.SDK_INT >= 11) {
                params = uri.getQueryParameterNames();
            }
            for (String key : params) {
                if (BaseConfig.INTENT_KEY_SDKURL.equals(key) || "eurl".equals(key)) {
                    value = uri.getEncodedFragment();
                } else {
                    value = uri.getQueryParameter(key);
                }
                bundle.putString(key, value);
                ZpLogger.i(TAG, TAG + ".decodeUri key=" + key + " value=" + value);
            }
            return bundle;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isFromProcess(Intent intent) {
        Bundle bundle = null;
        if (intent != null) {
            bundle = intent.getExtras();
        }
        boolean fromProcess = false;
        if (bundle != null) {
            fromProcess = bundle.getBoolean(INTENT_KEY_FROM_PROCESS, false);
        }
        ZpLogger.i(TAG, "isFromProcess fromProcess=" + fromProcess);
        return fromProcess;
    }

    public static boolean isNotShowLoading(Intent intent) {
        Bundle bundle;
        boolean notShowLoading = false;
        if (!(intent == null || (bundle = intent.getExtras()) == null)) {
            String notShowLoadingValue = null;
            if (Build.VERSION.SDK_INT >= 12) {
                notShowLoadingValue = bundle.getString(INTENT_KEY_NOT_SHOW_LOADING, "");
            }
            if (!TextUtils.isEmpty(notShowLoadingValue)) {
                notShowLoading = notShowLoadingValue.toLowerCase().equals("true");
            }
        }
        ZpLogger.i(TAG, "isNotShowLoading notShowLoading=" + notShowLoading);
        return notShowLoading;
    }

    public static boolean isUriInExtra(Intent intent) {
        if (intent == null || intent.getExtras() == null || intent.getExtras().getString(INTENT_KEY_INNER_URI, (String) null) == null) {
            return false;
        }
        return true;
    }

    private static boolean isTest404(Intent intent) {
        if (intent == null || intent.getData() == null || !intent.getData().getBooleanQueryParameter("test404", false)) {
            return false;
        }
        return true;
    }

    public static void smartStartActivity(Context con, Class<? extends Activity> act) {
        Context context = ActivityQueueManager.getTop();
        if (context == null) {
            context = con;
        }
        if (context == null) {
            throw new RuntimeException("context can not be null !!!");
        }
        Intent intent1 = new Intent(context, act);
        if (!(context instanceof Activity)) {
            intent1.addFlags(268435456);
        }
        context.startActivity(intent1);
    }

    public static void smartStartActivity(Context con, String actName) {
        Context context = ActivityQueueManager.getTop();
        if (context == null) {
            context = con;
        }
        if (context == null) {
            throw new RuntimeException("context can not be null !!!");
        }
        Intent intent1 = new Intent();
        if (!(context instanceof Activity)) {
            intent1.addFlags(268435456);
        }
        intent1.setClassName(context, actName);
        context.startActivity(intent1);
    }

    class PageLoadingAnim {
        PageLoadingAnim() {
        }

        public void show() {
        }

        public void hide() {
        }
    }

    class RouteImpl {
        private HashMap<String, String> appHostMap;
        private HashMap<String, String> selfActMap;

        public RouteImpl() {
            initAppHostMap();
            initSelfActivityMap(true);
        }

        private void initAppHostMap() {
            if (this.appHostMap == null) {
                this.appHostMap = new HashMap<>();
                this.appHostMap.put("zhuanti", "tvtaobao://zhuanti?");
                this.appHostMap.put("browser", "tvtaobao://browser?");
                this.appHostMap.put("taobaosdk", "tvtaobao://taobaosdk?");
                this.appHostMap.put(BaseConfig.INTENT_KEY_MODULE_JUHUASUAN, "tvtaobao://juhuasuan?");
                this.appHostMap.put(DisplayTypeConstants.SEC_KILL, "tvtaobao://seckill?");
                this.appHostMap.put("chaoshi", "tvtaobao://chaoshi?");
                this.appHostMap.put("caipiao", "tvtaobao://caipiao?");
                this.appHostMap.put(BaseConfig.INTENT_KEY_MODULE_TVBUY_SHOPPING, "tvtaobao://tvshopping?");
                this.appHostMap.put(BaseConfig.INTENT_KEY_MODULE_FLASHSALE_MAIN, "tvtaobao://flashsale?");
                this.appHostMap.put("voice", "tvtaobao://voice?");
                this.appHostMap.put("takeout", "tvtaobao://takeout?");
            }
        }

        private void initSelfActivityMap(boolean force) {
            if (this.selfActMap == null || force) {
                this.selfActMap = new HashMap<>();
                boolean isBlitzShop = SharePreferences.getBoolean("isBlitzShop", true).booleanValue();
                if ("yes".equals(RtEnv.get("test_isBlitzShop"))) {
                    isBlitzShop = true;
                } else if ("no".equals(RtEnv.get("test_isBlitzShop"))) {
                    isBlitzShop = false;
                }
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_TAKEOUT_ORDER_LIST, BaseConfig.SWITCH_TO_TAKEOUT_ORDER_LIST_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_TAKEOUT_ORDER_DETAIL, BaseConfig.SWITCH_TO_TAKEOUT_ORDER_DETAIL_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_SHOP_SEARCH, BaseConfig.SWITCH_TO_TAKEOUT_SHOP_SEARCH_ACTIVITY);
                this.selfActMap.put("detail", BaseConfig.SWITCH_TO_DETAIL_ACTIVITY);
                this.selfActMap.put("goodsList", BaseConfig.SWITCH_TO_GOODLIST_ACTIVITY);
                this.selfActMap.put("mytaobao", "com.yunos.tvtaobao.mytaobao.activity.NewMyTaoBaoActivity");
                this.selfActMap.put("orderList", BaseConfig.SWITCH_TO_ORDERLIST_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_NEW_TVBUY, BaseConfig.SWITCH_TO_NEW_TVBUY_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_TVBUY_SHOPPING, BaseConfig.SWITCH_TO_TVBUY_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_TAOBAOTV, BaseConfig.SWITCH_TO_TAOBAOTV_ACTIVITY);
                this.selfActMap.put("collects", BaseConfig.SWITCH_TO_COLLECTS_ACTIVITY);
                this.selfActMap.put("coupon", BaseConfig.SWITCH_TO_COUPON_ACTIVITY);
                if (!isBlitzShop) {
                    this.selfActMap.put("shop", BaseConfig.SWITCH_TO_SHOP_ACTIVITY);
                } else {
                    this.selfActMap.put("shop", BaseConfig.SWITCH_TO_SHOP_BLIZ_ACTIVITY);
                }
                this.selfActMap.put("sureJoin", BaseConfig.getOldSkuActivity());
                this.selfActMap.put("search", BaseConfig.SWITCH_TO_SEARCH_ACTIVITY);
                this.selfActMap.put("recommend", BaseConfig.SWITCH_TO_RECOMMEND_ACTIVITY);
                this.selfActMap.put("main", BaseConfig.SWITCH_TO_HOME_ACTIVITY);
                this.selfActMap.put("cart", BaseConfig.SWITCH_TO_SHOPCART_LIST_ACTIVITY);
                this.selfActMap.put("menu", BaseConfig.SWITCH_TO_MENU_ACTIVITY);
                this.selfActMap.put("common", BaseConfig.SWITCH_TO_COMMON_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_POING, BaseConfig.SWITCH_TO_POINT_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_TAKEOUT_WEB, BaseConfig.SWITCH_TO_TAKEOUT_WEB_ACTIVITY);
                this.selfActMap.put("address", BaseConfig.SWITCH_TO_ADDRESS_WEB_ACTIVITY);
                this.selfActMap.put("relative_recomment", BaseConfig.SWITCH_TO_RELATIVERECOMMEND_ACTIVITY);
                this.selfActMap.put("chaoshi", BaseConfig.SWITCH_TO_CHAOSHI_ACTIVITY);
                this.selfActMap.put("todayGoods", BaseConfig.SWITCH_TO_TODAYGOODS_ACTIVITY);
                this.selfActMap.put("chongzhi", BaseConfig.SWITCH_TO_CHONGZHI_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_FLASHSALE_MAIN, BaseConfig.SWITCH_TO_FLASHSALE_MAIN_ACTIVITY);
                this.selfActMap.put("mytaobao", "com.yunos.tvtaobao.mytaobao.activity.NewMyTaoBaoActivity");
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_JUHUASUAN, BaseConfig.SWITCH_TO_JUHUASUAN_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_PAYRESULT, BaseConfig.SWITCH_TO_PAYRESULT_ACTIVITY);
                this.selfActMap.put("graphicDetails", BaseConfig.SWITCH_TO_SKILL_GRAPHICDETAILS_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_TAKEOUT_HOME, BaseConfig.SWITCH_TO_TAKEOUT_SHOP_HOME);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_ANSWER, BaseConfig.SWITCH_TO_ANSWER_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_HQLIVE, BaseConfig.SWITCH_TO_HQLIVE_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_LIKE, BaseConfig.SWITCH_TO_GUESS_YOU_LIKE_ACTIVITY);
                if (Config.isDebug()) {
                    if (GlobalConfigInfo.getInstance().getGlobalConfig() == null || !GlobalConfigInfo.getInstance().getGlobalConfig().isTradeDegrade()) {
                        this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_TESTORDER, BaseConfig.SWITCH_TO_NEWBUILDORDER_ACTIVITY);
                    } else {
                        this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_TESTORDER, BaseConfig.SWITCH_TO_BUILDORDER_ACTIVITY);
                    }
                }
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_CREATE_ORDER, BaseConfig.SWITCH_TO_VOICE_CREATE_ORDER_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_LOGOUT, BaseConfig.SWITCH_TO_LOGINOUT_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_TAOBAOLIVE, BaseConfig.SWITCH_TO_TABPBAO_LIVE_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_ERROR_PAGE, BaseConfig.SWITCH_TO_ERROR_PAGE_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_NATIVE_JHS, BaseConfig.SWITCH_TO_JHS_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_MYFOLLOW, BaseConfig.SWITCH_TO_FOLLOW_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_DARENTV, BaseConfig.SWITCH_TO_DARENTV_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_DARENHOME, BaseConfig.SWITCH_TO_DAREN_ACTIVITY);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_NATIVE_TAKEOUTHOME, BaseConfig.SWITCH_TO_TAKEOUT_HOME_TEST);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_TAKEOUT_MINE, BaseConfig.SWITCH_TO_TAKEOUT_MINE);
                this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_GAMES, BaseConfig.SWITCH_TO_GAMES);
                if (Config.isDebug()) {
                    this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_TAKEOUTSEARCHTEST, BaseConfig.SWITCH_TO_TEST_SEARCH);
                    this.selfActMap.put(BaseConfig.INTENT_KEY_MODULE_ELEM_BIND_GUIDE, BaseConfig.SWITCH_TO_ELEM_BIND_GUILD);
                }
            }
        }

        private void routeFail(String failReason) {
            ZpLogger.i(RouteService.TAG, RouteService.TAG + ".routeFail failReason=" + failReason);
            Map<String, String> p = Utils.getProperties();
            p.put("failReason", failReason);
            Activity top = ActivityQueueManager.getTop();
            if (top instanceof CoreActivity) {
                Utils.utCustomHit(((CoreActivity) top).getFullPageName(), "OpenFail", p);
            } else {
                Utils.utCustomHit("OpenFail", p);
            }
            Intent intent = new Intent();
            Context context = top;
            if (context == null) {
                context = RouteService.this;
                intent.setFlags(268435456);
            }
            boolean hasLaterestVersion = RouteService.this.getSharedPreferences("updateInfo", 0).getBoolean(UpdatePreference.UPDATE_HAS_LATEREST_VERSION, true);
            ZpLogger.i(RouteService.TAG, RouteService.TAG + ".routeFail hasLaterestVersion = " + hasLaterestVersion);
            if (hasLaterestVersion) {
                ZpLogger.i(RouteService.TAG, RouteService.TAG + ".routeFail has laterest goHome");
                intent.setClassName(context, BaseConfig.SWITCH_TO_HOME_ACTIVITY);
                intent.putExtra("isDirectShowUpdate", true);
            } else {
                ZpLogger.i(RouteService.TAG, RouteService.TAG + ".routeFail goErrorPage");
                intent.setClassName(context, BaseConfig.SWITCH_TO_ERROR_PAGE_ACTIVITY);
            }
            context.startActivity(intent);
        }

        private boolean gotoOtherAppActivity(Intent originIntent, String app, String query, int flags) {
            ZpLogger.i(RouteService.TAG, ".gotoOtherAppActivity(" + originIntent + "," + app + "," + query + "," + Integer.toHexString(flags) + ")" + app);
            if (originIntent != null) {
                originIntent.getExtras();
            }
            if (query == null) {
                routeFail("NoQuery");
                return false;
            }
            String appHost = this.appHostMap.get(app);
            if (appHost == null) {
                routeFail("NoAppHost");
                return false;
            }
            try {
                String uri = appHost + query;
                Uri theUri = Uri.parse(uri);
                ZpLogger.i(RouteService.TAG, ".gotoOtherAppActivity.uri=" + uri + ", flags = " + flags + ", theUri = " + theUri);
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addFlags(flags);
                intent.setData(theUri);
                if (0 != 0) {
                    intent.putExtras((Bundle) null);
                }
                Context context = ActivityQueueManager.getTop();
                if (context == null) {
                    context = RouteService.this;
                    intent.addFlags(268435456);
                }
                context.startActivity(intent);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                routeFail("StartActivityError");
                return false;
            }
        }

        private boolean gotoSelfAppActivity(Intent originIntent, Bundle bundle, int flags) {
            ZpLogger.i(RouteService.TAG, ".gotoSelfAppActivity(" + bundle + "," + Integer.toHexString(flags) + ")");
            String selfModule = bundle.getString("module");
            if (selfModule != null) {
                Context context = ActivityQueueManager.getTop();
                Intent intent = new Intent();
                if (context == null) {
                    context = RouteService.this;
                    intent.addFlags(268435456);
                }
                String activityClass = this.selfActMap.get(selfModule);
                if (activityClass == null) {
                    routeFail("NoModule");
                    return false;
                }
                intent.setClassName(context, activityClass);
                if ("cart".equalsIgnoreCase(selfModule) && "tsm_client_native".equalsIgnoreCase(bundle.getString("cartFrom"))) {
                    intent.setClassName(context, BaseConfig.SWITCH_TO_SHOPCART_LIST_ACTIVITY);
                }
                if (intent != null) {
                    intent.addFlags(flags);
                    intent.putExtras(bundle);
                    boolean fromSystem = false;
                    if (BaseConfig.INTENT_KEY_FROM_VAL_VS.equals(bundle.getString("from"))) {
                        fromSystem = true;
                    }
                    intent.putExtra(CoreIntentKey.URI_FROM_SYSTEM, fromSystem);
                    context.startActivity(intent);
                    return true;
                }
                routeFail("NoIntent");
                return false;
            }
            routeFail("NoSelfModule");
            return false;
        }

        public boolean processIntent(Intent intent) {
            String uriString;
            if (intent == null) {
                routeFail("NoIntent");
                return false;
            }
            if (RtEnv.get("test_isBlitzShop") != null) {
                initSelfActivityMap(true);
            }
            Bundle mParamsExtrasBundle = intent.getExtras();
            ZpLogger.v(RouteService.TAG, RouteService.TAG + ".processActivity.mParamsExtrasBundle = " + mParamsExtrasBundle);
            Uri uri = intent.getData();
            ZpLogger.v(RouteService.TAG, RouteService.TAG + ".processActivity.uri = " + uri);
            if (!(uri != null || mParamsExtrasBundle == null || (uriString = mParamsExtrasBundle.getString(RouteService.INTENT_KEY_INNER_URI)) == null)) {
                uri = Uri.parse(uriString);
                ZpLogger.i(RouteService.TAG, RouteService.TAG + ".INTENT_KEY_INNER_URI uriString=" + uriString + ".uri = " + uri);
            }
            if (uri == null) {
                routeFail("NoUri");
                return false;
            }
            Bundle bundle = RouteService.decodeUri(uri);
            if (bundle == null) {
                routeFail("NoBundle");
                return false;
            }
            boolean inner = false;
            boolean inheritFlags = false;
            if (mParamsExtrasBundle != null) {
                bundle.putAll(mParamsExtrasBundle);
                inner = bundle.getBoolean(CoreActivity.INTENT_KEY_INNER, false);
                inheritFlags = bundle.getBoolean(CoreActivity.INTENT_KEY_INHERIT_FLAGS, false);
                mParamsExtrasBundle.remove(CoreActivity.INTENT_KEY_INNER);
                mParamsExtrasBundle.remove(RouteService.INTENT_KEY_FROM_PROCESS);
                mParamsExtrasBundle.remove(RouteService.INTENT_KEY_NOT_SHOW_LOADING);
                mParamsExtrasBundle.remove(CoreActivity.INTENT_KEY_INHERIT_FLAGS);
                ZpLogger.i(RouteService.TAG, RouteService.TAG + ".processActivity.spm_url=" + bundle.getString("spm"));
            }
            if (bundle.getString(CoreIntentKey.URI_FROM_APP) != null) {
                if (BaseConfig.INTENT_KEY_FROM_VAL_VS.equals(bundle.getString("from"))) {
                    BusinessRequest.getBusinessRequest().baseRequest((BaseMtopRequest) new ASRUTRequest(DomainType.TYPE_OPEN_PAGE, "system"), (RequestListener) null, false);
                }
            }
            ZpLogger.i(RouteService.TAG, RouteService.TAG + ".processActivity.inner=" + inner + ".inheritFlags = " + inheritFlags);
            int intentFlag = 0;
            if (inheritFlags) {
                intentFlag = intent.getFlags();
                if (intent.hasExtra("_launch_flags")) {
                    intentFlag = intent.getIntExtra("_launch_flags", intentFlag);
                }
            }
            String app = bundle.getString(RouteService.INTENT_KEY_APP);
            ZpLogger.i(RouteService.TAG, RouteService.TAG + ".processActivity.bundle=" + bundle + ".SYSTEM_YUNOS_4_0 = " + SystemConfig.SYSTEM_YUNOS_4_0);
            String string = bundle.getString("module");
            if (Build.VERSION.SDK_INT < 9) {
                return false;
            }
            if (StringUtil.isEmpty(app)) {
                return gotoSelfAppActivity(intent, bundle, intentFlag);
            }
            if (app.equals("tvtaobao") || app.equals("taobaosdk") || app.equals("takeout")) {
                return gotoSelfAppActivity(intent, bundle, intentFlag);
            }
            if (app.equals("chongzhi")) {
                bundle.putString("module", "chongzhi");
                return gotoSelfAppActivity(intent, bundle, intentFlag);
            } else if (app.equals("zhuanti")) {
                bundle.putString("module", IntentDataUtil.getString(intent, "module", (String) null));
                return gotoSelfAppActivity(intent, bundle, intentFlag);
            } else if (!app.equals(BaseConfig.INTENT_KEY_MODULE_JUHUASUAN)) {
                return gotoOtherAppActivity(intent, app, uri.getEncodedQuery(), intentFlag);
            } else {
                bundle.putString("module", BaseConfig.INTENT_KEY_MODULE_JUHUASUAN);
                return gotoSelfAppActivity(intent, bundle, intentFlag);
            }
        }
    }
}
