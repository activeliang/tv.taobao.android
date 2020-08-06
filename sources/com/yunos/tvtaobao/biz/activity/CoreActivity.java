package com.yunos.tvtaobao.biz.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.support.annotation.NonNull;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;
import com.ali.user.open.core.AliMemberSDK;
import com.ali.user.open.core.Site;
import com.ali.user.open.ucc.UccCallback;
import com.ali.user.open.ucc.UccService;
import com.alibaba.analytics.core.Constants;
import com.alibaba.fastjson.JSON;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.tvtaobao.android.runtime.RtBaseEnv;
import com.tvtaobao.android.ui3.helper.ScreenCapture;
import com.tvtaobao.voicesdk.base.SDKInitConfig;
import com.ut.mini.internal.UTTeamWork;
import com.yunos.CloudUUIDWrapper;
import com.yunos.RunMode;
import com.yunos.ott.sdk.core.Environment;
import com.yunos.tv.blitz.activity.BzBaseActivity;
import com.yunos.tv.blitz.view.BlitzBridgeSurfaceView;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.RtEnv;
import com.yunos.tv.core.common.CoreIntentKey;
import com.yunos.tv.core.common.RequestListener;
import com.yunos.tv.core.common.SharePreferences;
import com.yunos.tv.core.config.AppInfo;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.disaster_tolerance.DisasterTolerance;
import com.yunos.tv.core.util.ActivityDataUtil;
import com.yunos.tv.core.util.ActivityPathRecorder;
import com.yunos.tv.core.util.IntentDataUtil;
import com.yunos.tv.core.util.MonitorUtil;
import com.yunos.tv.core.util.NetWorkUtil;
import com.yunos.tv.core.util.Utils;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import com.yunos.tvtaobao.biz.dialog.NewFeatureDialog;
import com.yunos.tvtaobao.biz.interfaces.IElemBindCallBack;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.yunos.tvtaobao.biz.request.BusinessRequest;
import com.yunos.tvtaobao.biz.request.bo.ElemBindBo;
import com.yunos.tvtaobao.biz.request.elem.BindComponentProxyImpl;
import com.yunos.tvtaobao.biz.updatesdk.UpdateClient;
import com.yunos.tvtaobao.biz.updatesdk.UpdateFromDX;
import com.yunos.tvtaobao.biz.util.StringUtil;
import com.yunos.tvtaobao.payment.PaymentApplication;
import com.yunos.tvtaobao.payment.config.DebugConfig;
import com.yunos.tvtaobao.payment.utils.ChannelUtils;
import com.zhiping.dev.android.logcat.ZpLogCat;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import org.json.JSONObject;

public abstract class CoreActivity extends BzBaseActivity implements ActivityPathRecorder.PathNode {
    public static final String INTENT_KEY_INHERIT_FLAGS = "inheritflags";
    public static final String INTENT_KEY_INNER = "frominner";
    public static final String INTENT_KEY_IS_FROM_OUTSIDE = "isFromOutside";
    /* access modifiers changed from: private */
    public static String TAG = CoreActivity.class.getSimpleName();
    protected static Handler exitHandler;
    private static SparseArray<Activity> mOpenedActivity = new SparseArray<>();
    private static long resumeCount = 0;
    protected final String OBJ = (getClass().getName() + Constant.NLP_CACHE_TYPE + Integer.toHexString(hashCode()));
    private long createTime = 0;
    private boolean guideFlag;
    public boolean hasbindElemAccount = false;
    /* access modifiers changed from: private */
    public IElemBindCallBack iElemBindCallBack;
    int localResumeCount = 0;
    private String mApp;
    private String mFrom;
    private BroadcastReceiver mHomeKeyEventReceiver = new BroadcastReceiver() {
        String SYSTEM_HOME_KEY = "homekey";
        String SYSTEM_HOME_KEY_LONG = "recentapps";
        String SYSTEM_REASON = "reason";

        public void onReceive(Context context, Intent intent) {
            try {
                if (intent.getAction().equals("android.intent.action.CLOSE_SYSTEM_DIALOGS")) {
                    String reason = intent.getStringExtra(this.SYSTEM_REASON);
                    if (TextUtils.equals(reason, this.SYSTEM_HOME_KEY)) {
                        if (DebugConfig.whetherIsMonkey()) {
                            Intent intent0 = new Intent("android.intent.action.MAIN");
                            intent0.addCategory("android.intent.category.LAUNCHER");
                            intent0.setComponent(new ComponentName(AppInfo.getPackageName(), "com.yunos.tvtaobao.splashscreen.activity.StartActivity"));
                            CoreActivity.this.startActivity(intent0);
                        }
                        CoreActivity.this.onPause();
                        CoreActivity.this.clearAllOpenedActivity(CoreActivity.this);
                        CoreActivity.this.exitChildProcessOfCoreActivity();
                        Intent intent1 = new Intent();
                        intent1.setPackage(CoreActivity.this.getPackageName());
                        intent1.setAction("tech.zhiping.audioAction");
                        CoreActivity.this.stopService(intent1);
                        CoreApplication.getApplication().clear();
                        CoreActivity.this.finish();
                        Process.killProcess(Process.myPid());
                        return;
                    }
                    if (TextUtils.equals(reason, this.SYSTEM_HOME_KEY_LONG)) {
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    };
    private String mHuoDong;
    private boolean mNetWorkCheck = true;
    /* access modifiers changed from: private */
    public NewFeatureDialog mNewFeatureDialog;
    private Intent mNewIntent;
    protected String mPageName;
    /* access modifiers changed from: private */
    public UpdateClient mUpdateClient;
    private long resumeTime = 0;
    private String uccId;

    /* access modifiers changed from: protected */
    public abstract String getAppName();

    /* access modifiers changed from: protected */
    public abstract String getAppTag();

    /* access modifiers changed from: protected */
    public abstract void onStartActivityNetWorkError();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        ZpLogger.i(TAG, this.OBJ + ".onCreate(" + savedInstanceState + ")" + getIntent());
        this.createTime = System.currentTimeMillis();
        super.onCreate(savedInstanceState);
        MonitorUtil.init();
        if (RunMode.isYunos() == Environment.getInstance().isYunos()) {
            if (exitHandler != null) {
                exitHandler.removeCallbacksAndMessages((Object) null);
            } else {
                exitHandler = new Handler();
            }
            initFromActApp();
            mOpenedActivity.put(hashCode(), this);
            this.mNewIntent = null;
            ActivityPathRecorder.getInstance().recordPathNode(this);
            if (Boolean.TRUE.equals(RtEnv.get(RtEnv.KEY_SHOULD_CHECK_UPDATE_ON_CREATE, true))) {
                getNewFeature();
                RtEnv.set(RtEnv.KEY_SHOULD_CHECK_UPDATE_ON_CREATE, false);
            }
            if (isHomeActivity()) {
                showfeaturesDialog();
            }
            registerReceiver(this.mHomeKeyEventReceiver, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        }
    }

    public void onAttachedToWindow() {
        ZpLogger.i(TAG, this.OBJ + ".onAttachedToWindow()");
        super.onAttachedToWindow();
        if (isTbs()) {
            long time = System.currentTimeMillis();
            DimensionValueSet dimensionValueSet = MonitorUtil.createDimensionValueSet(this);
            dimensionValueSet.setValue("activityName", getClass().getName());
            MeasureValueSet measureValueSet = MeasureValueSet.create();
            measureValueSet.setValue("loadTime", (double) (time - this.createTime));
            AppMonitor.Stat.commit("tvtaobao", "activityLoad", dimensionValueSet, measureValueSet);
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        ZpLogger.i(TAG, this.OBJ + ".onNewIntent(" + intent + ")");
        super.onNewIntent(intent);
        this.mNewIntent = intent;
        ActivityPathRecorder.getInstance().recordPathNode(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        ZpLogger.i(TAG, this.OBJ + ".onResume()");
        if (isLoginToElemGuideActivity() && PaymentApplication.isLoginFragmentHasShowed()) {
            getByTaobaoUserId();
            PaymentApplication.setLoginFragmentHasShowed(false);
        }
        resumeCount++;
        this.localResumeCount++;
        this.resumeTime = System.currentTimeMillis();
        ZpLogger.i(TAG, this.OBJ + ".onResume isTbs()=" + isTbs());
        if (RunMode.isYunos() == Environment.getInstance().isYunos()) {
            startUpdate();
            enterUT();
        }
        super.onResume();
        SDKInitConfig.setCurrentPage(getFullPageName());
        if (resumeCount == 1) {
            ZpLogger.i(TAG, this.OBJ + ".onResume reportAppLoadTime " + (this.resumeTime - CoreApplication.getOnCreateTime()));
            MonitorUtil.reportAppLoadTime(this, (double) (this.resumeTime - CoreApplication.getOnCreateTime()));
        }
    }

    public boolean isFirstResume() {
        return this.localResumeCount == 1;
    }

    /* access modifiers changed from: protected */
    public void startUpdate() {
        if (!isUpdateBlackList() && Boolean.TRUE.equals(RtEnv.get(RtEnv.KEY_SHOULD_START_UPDATE, true))) {
            RtEnv.set(RtEnv.KEY_SHOULD_START_UPDATE, false);
            startDXUpdate();
            startUpdateApp();
        }
    }

    public void enterUT() {
        if (SharePreferences.getString("debug_ut_immediately") != null) {
            String settingDate = SharePreferences.getString("debug_ut_immediately");
            HashMap<String, String> map = new HashMap<>();
            map.put(Constants.RealTimeDebug.DEBUG_API_URL, "https://service-usertrack.alibaba-inc.com/upload_records_from_client");
            String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
            if (!TextUtils.isEmpty(settingDate)) {
                dateStr = settingDate;
            }
            map.put(Constants.RealTimeDebug.DEBUG_KEY, "utupdate" + dateStr);
            map.put(Constants.RealTimeDebug.DEBUG_SAMPLING_OPTION, "true");
            UTTeamWork.getInstance().turnOnRealTimeDebug(map);
            Toast.makeText(this, "请于以下链接查看:\nhttps://usertrack.alibaba-inc.com/validate/verify?debugKey=utupdate" + dateStr, 1).show();
        }
        if (isTbs()) {
            this.mPageName = getFullPageName();
            if (TextUtils.isEmpty(this.mPageName)) {
                throw new IllegalArgumentException("The PageName was null and TBS is open");
            }
            Utils.utPageAppear(this.mPageName, this.mPageName);
            ZpLogger.i(TAG, this.OBJ + ".enterUT end mPageName=" + this.mPageName);
        }
    }

    /* access modifiers changed from: protected */
    public void exitUT() {
        try {
            if (isTbs() && !TextUtils.isEmpty(this.mPageName)) {
                Map<String, String> p = getPageProperties();
                ZpLogger.i(TAG, this.OBJ + ".exitUI TBS=updatePageProperties(" + p + ")");
                Utils.utUpdatePageProperties(this.mPageName, p);
                Utils.utPageDisAppear(this.mPageName);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        ZpLogger.i(TAG, this.OBJ + ".onPause()");
        if (RunMode.isYunos() == Environment.getInstance().isYunos()) {
            try {
                super.onPause();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            exitUT();
            return;
        }
        try {
            super.onPause();
        } catch (Throwable e2) {
            e2.printStackTrace();
        }
    }

    public void finish() {
        ZpLogger.i(TAG, this.OBJ + ".finish()");
        super.finish();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        ZpLogger.i(TAG, this.OBJ + ".onStop()");
        if (ChannelUtils.FJM.equals("SFA") && !com.yunos.tvtaobao.payment.analytics.Utils.isAppOnForeground(this) && exitHandler != null) {
            exitHandler.postDelayed(new Runnable() {
                public void run() {
                    CoreActivity.this.finishAndRemove();
                }
            }, 1);
        }
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        ZpLogger.i(TAG, this.OBJ + ".onDestroy()");
        try {
            getWindow().getDecorView().getHandler().removeCallbacks((Runnable) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (RunMode.isYunos() == Environment.getInstance().isYunos()) {
            mOpenedActivity.remove(hashCode());
            ActivityPathRecorder.getInstance().onDestroy(this);
            this.mNewIntent = null;
            unregisterReceiver(this.mHomeKeyEventReceiver);
        }
        try {
            Field[] fields = BzBaseActivity.class.getDeclaredFields();
            int i = 0;
            while (true) {
                if (i >= fields.length) {
                    break;
                }
                Field field = fields[i];
                boolean old = field.isAccessible();
                field.setAccessible(true);
                Object value = field.get(this);
                if (value != null && (value instanceof BlitzBridgeSurfaceView)) {
                    ((BlitzBridgeSurfaceView) value).getHolder().getSurface().release();
                    field.setAccessible(old);
                    break;
                }
                field.setAccessible(old);
                i++;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (getBlitzContext() != null) {
            getBlitzContext().deinitContext();
        }
        super.onDestroy();
    }

    private void stopUpdateApp() {
        if (this.mUpdateClient != null) {
            this.mUpdateClient.stopDownload();
        }
    }

    public void startActivity(Intent intent) {
        ZpLogger.i(TAG, this.OBJ + ".startActivity(" + intent + ")");
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                bundle = new Bundle();
            }
            if (!TextUtils.isEmpty(this.mFrom)) {
                bundle.putString(CoreIntentKey.URI_FROM_BUNDLE, this.mFrom);
            }
            if (!TextUtils.isEmpty(this.mHuoDong)) {
                bundle.putString(CoreIntentKey.URI_HUODONG_BUNDLE, this.mHuoDong);
            }
            if (TextUtils.isEmpty(getAppName())) {
                throw new IllegalArgumentException("The activity.getAppName() was empty");
            }
            if (!TextUtils.isEmpty(this.mApp)) {
                bundle.putString(CoreIntentKey.URI_FROM_APP_BUNDLE, this.mApp);
            } else {
                bundle.putString(CoreIntentKey.URI_FROM_APP_BUNDLE, getAppName());
            }
            recordPreviousNode(intent);
            intent.putExtras(bundle);
            if (this.mNetWorkCheck) {
                boolean isGotoNetworkSetting = false;
                if ("android.settings.WIFI_SETTINGS".equals(intent.getAction())) {
                    isGotoNetworkSetting = true;
                }
                ComponentName component = intent.getComponent();
                if (component != null && "com.android.settings".equals(component.getPackageName()) && "com.android.settings.network".equals(component.getClassName())) {
                    isGotoNetworkSetting = true;
                }
                if (!NetWorkUtil.isNetWorkAvailable() && !isGotoNetworkSetting && RunMode.isYunos() == Environment.getInstance().isYunos()) {
                    onStartActivityNetWorkError();
                    return;
                }
            }
            setInnerIntent(intent);
            intent.putExtra("_launch_flags", intent.getFlags());
            boolean needRoute = false;
            if (intent.getComponent() == null && "android.intent.action.VIEW".equals(intent.getAction()) && intent.getCategories() != null) {
                Iterator<String> iterator = intent.getCategories().iterator();
                while (true) {
                    if (iterator != null && iterator.hasNext()) {
                        if ("android.intent.category.DEFAULT".equals(iterator.next()) && intent.getData() != null && "home".equals(intent.getData().getHost()) && "tvtaobao".equals(intent.getData().getScheme())) {
                            needRoute = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            if (intent.getBooleanExtra(CoreIntentKey.IS_CATCH_EXCEPTION, true)) {
                if (!needRoute) {
                    try {
                        super.startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    startService(intent);
                }
            } else if (!needRoute) {
                super.startActivity(intent);
            } else {
                startService(intent);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle outState) {
        ZpLogger.i(TAG, this.OBJ + ".onSaveInstanceState(" + outState + ")");
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        ZpLogger.i(TAG, this.OBJ + ".startActivityForResult(" + intent + "," + requestCode + ")");
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                bundle = new Bundle();
            }
            if (!TextUtils.isEmpty(this.mFrom)) {
                bundle.putString(CoreIntentKey.URI_FROM_BUNDLE, this.mFrom);
            }
            if (!TextUtils.isEmpty(this.mHuoDong)) {
                bundle.putString(CoreIntentKey.URI_HUODONG_BUNDLE, this.mHuoDong);
            }
            if (TextUtils.isEmpty(getAppName())) {
                throw new IllegalArgumentException("The activity.getAppName() was empty");
            }
            if (!TextUtils.isEmpty(this.mApp)) {
                bundle.putString(CoreIntentKey.URI_FROM_APP_BUNDLE, this.mApp);
            } else {
                bundle.putString(CoreIntentKey.URI_FROM_APP_BUNDLE, getAppName());
            }
            recordPreviousNode(intent);
            intent.putExtras(bundle);
            if (this.mNetWorkCheck) {
                boolean isGotoNetworkSetting = false;
                if ("android.settings.WIFI_SETTINGS".equals(intent.getAction())) {
                    isGotoNetworkSetting = true;
                }
                ComponentName component = intent.getComponent();
                if (component != null && "com.android.settings".equals(component.getPackageName()) && "com.android.settings.network".equals(component.getClassName())) {
                    isGotoNetworkSetting = true;
                }
                if (!NetWorkUtil.isNetWorkAvailable() && !isGotoNetworkSetting && RunMode.isYunos() == Environment.getInstance().isYunos()) {
                    onStartActivityNetWorkError();
                    return;
                }
            }
            setInnerIntent(intent);
            intent.putExtra("_launch_flags", intent.getFlags());
            boolean needRoute = false;
            if (intent.getComponent() == null && "android.intent.action.VIEW".equals(intent.getAction()) && intent.getCategories() != null) {
                Iterator<String> iterator = intent.getCategories().iterator();
                while (true) {
                    if (iterator != null && iterator.hasNext()) {
                        if ("android.intent.category.DEFAULT".equals(iterator.next()) && intent.getData() != null && "home".equals(intent.getData().getHost()) && "tvtaobao".equals(intent.getData().getScheme())) {
                            needRoute = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            if (intent.getBooleanExtra(CoreIntentKey.IS_CATCH_EXCEPTION, true)) {
                if (!needRoute) {
                    try {
                        super.startActivityForResult(intent, requestCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    startService(intent);
                }
            } else if (!needRoute) {
                super.startActivityForResult(intent, requestCode);
            } else {
                startService(intent);
            }
        }
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        ZpLogger.i(TAG, this.OBJ + ".dispatchGenericMotionEvent(" + ev + ")");
        try {
            return super.dispatchGenericMotionEvent(ev);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        ZpLogger.i(TAG, this.OBJ + ".dispatchKeyEvent(" + event + ")");
        boolean rtn = false;
        try {
            checkScreenCaptureKey(event);
            rtn = super.dispatchKeyEvent(event);
            if (event.getKeyCode() == 82 && event.getAction() == 1 && ZpLogCat.getInstance((Application) null).getTurnOnState()) {
                if (RtEnv.get("logRecording") != null && RtEnv.get("logUploadDoing") == null) {
                    RtEnv.set("logUploadDoing", "yes");
                    ZpLogCat.getInstance((Application) null).dump(false);
                    ZpLogCat.getInstance((Application) null).tipTurnOnMsg(true, "上传中...");
                    RtEnv.doInBackground(new RtBaseEnv.BgTask() {
                        public Object doInBackground() {
                            ZpLogCat.getInstance((Application) null).uploadLogs(false, true, -1);
                            return null;
                        }

                        public void done(Object o, Throwable throwable) {
                            ZpLogCat.getInstance((Application) null).tipTurnOnMsg(true, "已完成。\n\n按下【\"菜单键\"】开始记录");
                            RtEnv.rmv("logUploadDoing");
                            RtEnv.rmv("logRecording");
                        }
                    });
                }
                if (RtEnv.get("logRecording") == null) {
                    RtEnv.set("logRecording", "yes");
                    ZpLogCat.getInstance((Application) null).dump(true);
                    ZpLogCat.getInstance((Application) null).tipTurnOnMsg(true, "记录中...\n\n按下【\"菜单键\"】结束记录并上传");
                }
            }
        } catch (Throwable e) {
            if (!DisasterTolerance.getInstance().catchKeyEventException(e, new String[0])) {
                throw e;
            }
        }
        return rtn;
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        ZpLogger.i(TAG, this.OBJ + ".dispatchKeyShortcutEvent(" + event + ")");
        return super.dispatchKeyShortcutEvent(event);
    }

    public void checkScreenCaptureKey(KeyEvent event) {
        try {
            if (Config.isDebug() && event != null && event.getAction() == 0 && event.getKeyCode() == 82) {
                if (RtEnv.get("checkScreenCaptureKey") != null) {
                    if (System.currentTimeMillis() - ((Long) RtEnv.get("checkScreenCaptureKey")).longValue() < 500) {
                        ScreenCapture.capture(this, (ScreenCapture.CaptureCallBack) null);
                    } else {
                        RtEnv.set("checkScreenCaptureKey", Long.valueOf(System.currentTimeMillis()));
                    }
                } else {
                    RtEnv.set("checkScreenCaptureKey", Long.valueOf(System.currentTimeMillis()));
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void setInnerIntent(Intent intent) {
        if (intent != null) {
            intent.putExtra(INTENT_KEY_INNER, true);
        }
    }

    public void setCheckNetWork(boolean check) {
        this.mNetWorkCheck = check;
    }

    /* access modifiers changed from: protected */
    public void initFromActApp() {
        this.mFrom = IntentDataUtil.getStringFromUri(getIntent(), "from", "");
        if (TextUtils.isEmpty(this.mFrom)) {
            this.mFrom = IntentDataUtil.getStringFromBundle(getIntent(), CoreIntentKey.URI_FROM_BUNDLE, "");
        }
        ZpLogger.i(TAG, this.OBJ + ".initFromActApp() mFrom=" + this.mFrom + ", intent=" + getIntent() + ", getExtras = " + getIntent().getExtras());
        if (!TextUtils.isEmpty(this.mFrom)) {
            this.mFrom = this.mFrom.replaceAll("[\\r\\n\\t\\s\\|\\\\\\/]+", "");
        }
        this.mHuoDong = IntentDataUtil.getStringFromUri(getIntent(), CoreIntentKey.URI_HUODONG, (String) null);
        if (TextUtils.isEmpty(this.mHuoDong)) {
            this.mHuoDong = IntentDataUtil.getStringFromBundle(getIntent(), CoreIntentKey.URI_HUODONG_BUNDLE, (String) null);
        }
        if (!TextUtils.isEmpty(this.mHuoDong)) {
            this.mHuoDong = this.mHuoDong.replaceAll("[\\r\\n\\t\\s\\|\\\\\\/]+", "");
        }
        this.mApp = IntentDataUtil.getStringFromUri(getIntent(), CoreIntentKey.URI_FROM_APP, (String) null);
        if (TextUtils.isEmpty(this.mApp)) {
            this.mApp = IntentDataUtil.getStringFromBundle(getIntent(), CoreIntentKey.URI_FROM_APP_BUNDLE, (String) null);
        }
        if (!TextUtils.isEmpty(this.mApp)) {
            this.mApp = this.mApp.replaceAll("[\\r\\n\\t\\s\\|\\\\\\/]+", "");
        }
        ZpLogger.i(TAG, this.OBJ + ".initFromActApp() mApp=" + this.mApp);
        if (this.mApp != null && this.mApp.equals(getAppName())) {
            this.mApp = null;
        }
        ZpLogger.i(TAG, this.OBJ + ".initFromActApp() mFrom=" + this.mFrom + ", mHuoDong=" + this.mHuoDong + ", mApp=" + this.mApp);
    }

    public void setFrom(String from) {
        this.mFrom = from;
    }

    public void setHuodong(String huodong) {
        this.mHuoDong = huodong;
    }

    public void setFromApp(String app) {
        this.mApp = app;
    }

    /* access modifiers changed from: protected */
    public boolean isTbs() {
        return true;
    }

    public String getFullPageName() {
        if (TextUtils.isEmpty(this.mPageName)) {
            this.mPageName = getAppTag() + getPageName();
        }
        return this.mPageName;
    }

    public boolean isUpdate() {
        return true;
    }

    public boolean isUpdateBlackList() {
        return false;
    }

    public boolean isHomeActivity() {
        return false;
    }

    public String getmFrom() {
        return this.mFrom;
    }

    public String getmHuoDong() {
        return this.mHuoDong;
    }

    public String getmApp() {
        return this.mApp;
    }

    public String getPageName() {
        return Pattern.compile("(activity|view|null|page|layout)$", 2).matcher(getClass().getSimpleName()).replaceAll("");
    }

    public Map<String, String> getPageProperties() {
        return Utils.getProperties(this.mFrom, this.mHuoDong, this.mApp);
    }

    public void clearAllOpenedActivity(Activity selfActivity) {
        Activity activity;
        int count = mOpenedActivity.size();
        for (int i = 0; i < count; i++) {
            Integer key = Integer.valueOf(mOpenedActivity.keyAt(i));
            if (!(key == null || (activity = mOpenedActivity.get(key.intValue())) == null || (selfActivity != null && selfActivity.equals(activity)))) {
                activity.finish();
            }
        }
    }

    public Uri getCurrentUri() {
        Intent intent = (!recordNewIntent() || this.mNewIntent == null) ? getIntent() : this.mNewIntent;
        Uri currentUri = (Uri) intent.getParcelableExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_URI);
        if (intent.hasExtra("from_voice")) {
            currentUri = Uri.parse("tvtaobao://home?module=voice");
        }
        if (currentUri == null) {
            currentUri = intent.getData();
        }
        if (currentUri == null) {
            currentUri = ActivityDataUtil.getInstance().getPreviousUri(getClass(), intent);
        }
        if (currentUri == null || currentUri.toString().startsWith("tvtaobao://home")) {
            return currentUri;
        }
        return ActivityDataUtil.getInstance().getAppHostUri(currentUri);
    }

    public boolean isEqualTo(ActivityPathRecorder.PathNode aNode) {
        if (aNode != this) {
            return false;
        }
        if (getCurrentUri() == aNode.getCurrentUri()) {
            return true;
        }
        if (getCurrentUri() == null || getCurrentUri().equals(aNode.getCurrentUri())) {
        }
        return false;
    }

    public boolean isFirstNode() {
        return IntentDataUtil.getBoolean((!recordNewIntent() || this.mNewIntent == null) ? getIntent() : this.mNewIntent, ActivityPathRecorder.INTENTKEY_FIRST, false).booleanValue();
    }

    public boolean isIgnored() {
        return false;
    }

    public void recordPreviousNode(@NonNull Intent intent) {
        if (isIgnored()) {
            intent.putExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_PREVIOUSACTIVITY, getPreviousNodeHash());
            intent.putExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_PREVIOUSINTENT, getPreviousSecondHashCode());
            intent.putExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_PREVIOUSURI, getPreviousNodeUri());
        } else {
            intent.putExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_PREVIOUSACTIVITY, getHashCode());
            intent.putExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_PREVIOUSINTENT, getSecondHashCode());
            intent.putExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_PREVIOUSURI, getCurrentUri());
        }
        if (intent.getData() != null || !isIgnored()) {
            intent.putExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_URI, intent.getData());
        } else {
            intent.putExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_URI, getCurrentUri());
        }
    }

    public int getPreviousNodeHash() {
        return ((!recordNewIntent() || this.mNewIntent == null) ? getIntent() : this.mNewIntent).getIntExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_PREVIOUSACTIVITY, -1);
    }

    public int getPreviousSecondHashCode() {
        return ((!recordNewIntent() || this.mNewIntent == null) ? getIntent() : this.mNewIntent).getIntExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_PREVIOUSINTENT, -1);
    }

    public Uri getPreviousNodeUri() {
        return (Uri) ((!recordNewIntent() || this.mNewIntent == null) ? getIntent() : this.mNewIntent).getParcelableExtra(ActivityPathRecorder.INTENTKEY_PATHRECORDER_PREVIOUSURI);
    }

    public int getHashCode() {
        return hashCode();
    }

    public int getSecondHashCode() {
        Intent intent = (!recordNewIntent() || this.mNewIntent == null) ? getIntent() : this.mNewIntent;
        if (intent == null) {
            return 0;
        }
        return intent.hashCode();
    }

    public boolean recordNewIntent() {
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        ZpLogger.e(TAG, "keyCode==" + keyCode);
        if (ChannelUtils.isThisChannel(ChannelUtils.DLT) && (event.getKeyCode() == 183 || event.getKeyCode() == 184 || event.getKeyCode() == 185 || event.getKeyCode() == 186)) {
            return true;
        }
        if (ChannelUtils.isThisChannel(ChannelUtils.FJM) && (event.getKeyCode() == 3 || event.getKeyCode() == 183 || event.getKeyCode() == 185 || event.getKeyCode() == 184 || event.getKeyCode() == 186 || event.getKeyCode() == 176)) {
            finishAndRemove();
        }
        if (event.getKeyCode() == 3 || event.getKeyCode() == 183 || event.getKeyCode() == 185) {
            finishAndRemove();
        } else if (event.getKeyCode() == 4 && CoreApplication.getApplication().getMyLifecycleHandler().isLastActivityInForeground()) {
            RtEnv.set(RtEnv.KEY_SHOULD_START_UPDATE, true);
        }
        return super.onKeyDown(keyCode, event);
    }

    /* access modifiers changed from: private */
    public void finishAndRemove() {
        onPause();
        clearAllOpenedActivity(this);
        exitChildProcessOfCoreActivity();
        Intent intent = new Intent();
        intent.setPackage(getPackageName());
        intent.setAction("tech.zhiping.audioAction");
        stopService(intent);
        CoreApplication.getApplication().clear();
        finish();
        Process.killProcess(Process.myPid());
    }

    public void exitChildProcessOfCoreActivity() {
        String packageName = getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : ((ActivityManager) getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningAppProcesses()) {
            if (appProcess.processName.compareTo(packageName + ":bs_webbroser") == 0) {
                ZpLogger.i(TAG, "kill processName=" + appProcess.processName);
                Process.killProcess(appProcess.pid);
            }
            if (appProcess.processName.compareTo(packageName + ":channel") == 0) {
                ZpLogger.i(TAG, "kill processName=" + appProcess.processName);
                Process.killProcess(appProcess.pid);
            }
            if (appProcess.processName.compareTo(packageName + ":dexmerge") == 0) {
                ZpLogger.i(TAG, "kill processName=" + appProcess.processName);
                Process.killProcess(appProcess.pid);
            }
            if (appProcess.processName.compareTo(packageName + ":dex2oat") == 0) {
                ZpLogger.i(TAG, "kill processName=" + appProcess.processName);
                Process.killProcess(appProcess.pid);
            }
        }
    }

    private void getNewFeature() {
        if (getSharedPreferences("updateInfo", 0).getInt(UpdatePreference.UPDATE_CURRENT_VERSION_CODE, 0) < AppInfo.getAppVersionNum()) {
            try {
                PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                String versionName = packInfo.versionName;
                String versioncode = String.valueOf(packInfo.versionCode);
                String channelId = Config.getChannel();
                String deviceId = CloudUUIDWrapper.getCloudUUID();
                String umtoken = Config.getUmtoken(this);
                JSONObject object = new JSONObject();
                object.put("umToken", Config.getUmtoken(this));
                object.put("wua", Config.getWua(this));
                object.put("isSimulator", Config.isSimulator(this));
                object.put("userAgent", Config.getAndroidSystem(this));
                BusinessRequest.getBusinessRequest().requestUpGrade(Build.VERSION.RELEASE, deviceId, channelId, "tvtaobao", versioncode, versionName, Build.MODEL + WVNativeCallbackUtil.SEPERATER + Build.VERSION.SDK, umtoken, Config.getModelInfo(this), object.toString(), new UpgradeAppListener(this));
            } catch (Exception e) {
                e.printStackTrace();
            } catch (NoClassDefFoundError e2) {
                e2.printStackTrace();
            }
        }
    }

    private static class UpgradeAppListener implements RequestListener<String> {
        private CoreActivity mContext;

        public UpgradeAppListener(CoreActivity context) {
            this.mContext = context;
        }

        public void onRequestDone(String data, int resultCode, String handleMessagemsg) {
            ZpLogger.d(CoreActivity.TAG, "UpgradeAppListener onRequestDone " + data);
            if (resultCode != 200 || data == null) {
                SharedPreferences.Editor editor = this.mContext.getSharedPreferences("updateInfo", 0).edit();
                editor.putBoolean(UpdatePreference.UPDATE_HAS_LATEREST_VERSION, false);
                editor.commit();
                return;
            }
            com.yunos.tvtaobao.biz.model.AppInfo appInfo = new com.yunos.tvtaobao.biz.model.AppInfo(JSON.parseObject(data));
            SharedPreferences.Editor e = this.mContext.getSharedPreferences("updateInfo", 0).edit();
            e.putString(UpdatePreference.UPDATE_TIPS, StringUtil.isEmpty(appInfo.getReleaseNote()) ? "" : appInfo.getReleaseNote());
            e.putBoolean(UpdatePreference.UPDATE_HAS_LATEREST_VERSION, true);
            e.commit();
        }
    }

    private void showfeaturesDialog() {
        if (getSharedPreferences("updateInfo", 0).getInt(UpdatePreference.UPDATE_CURRENT_VERSION_CODE, 0) < AppInfo.getAppVersionNum()) {
            try {
                PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                String versionName = packInfo.versionName;
                String versioncode = String.valueOf(packInfo.versionCode);
                String channelId = Config.getChannel();
                String deviceId = CloudUUIDWrapper.getCloudUUID();
                BusinessRequest.getBusinessRequest().requestNewFeature(Build.VERSION.RELEASE, deviceId, channelId, "tvtaobao", versioncode, versionName, Build.MODEL + WVNativeCallbackUtil.SEPERATER + Build.VERSION.SDK, Config.getUmtoken(this), Config.getModelInfo(this), new UpgradeNewFeatureListener(this));
            } catch (Exception e) {
                e.printStackTrace();
            } catch (NoClassDefFoundError e2) {
                e2.printStackTrace();
            }
        }
    }

    private static class UpgradeNewFeatureListener implements RequestListener<String> {
        private CoreActivity mContext;

        public UpgradeNewFeatureListener(CoreActivity context) {
            this.mContext = context;
        }

        public void onRequestDone(String data, int resultCode, String handleMessagemsg) {
            ZpLogger.d(CoreActivity.TAG, "UpgradeNewFeatureListener onRequestDone " + data);
            if (resultCode == 200 && data != null) {
                com.yunos.tvtaobao.biz.model.AppInfo appInfo = new com.yunos.tvtaobao.biz.model.AppInfo(JSON.parseObject(data));
                if (!StringUtil.isEmpty(appInfo.releaseAfterNote)) {
                    NewFeatureDialog unused = this.mContext.mNewFeatureDialog = new NewFeatureDialog((Context) this.mContext, appInfo.releaseAfterNote);
                    this.mContext.mNewFeatureDialog.show();
                    if (this.mContext.mUpdateClient == null) {
                        UpdateClient unused2 = this.mContext.mUpdateClient = UpdateClient.getInstance(this.mContext.getApplicationContext());
                    }
                    this.mContext.mUpdateClient.addCurrentVersionToSP();
                }
            }
        }
    }

    private void startUpdateApp() {
        try {
            PackageInfo packInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = packInfo.versionName;
            int versionCode = packInfo.versionCode;
            String channelId = Config.getChannel();
            String deviceId = CloudUUIDWrapper.getCloudUUID();
            boolean isDirectShowUpdate = getIntent().getBooleanExtra("isDirectShowUpdate", false);
            this.mUpdateClient = UpdateClient.getInstance(getApplicationContext());
            this.mUpdateClient.startDownload("tvtaobao", versionName, versionCode, deviceId, channelId, isDirectShowUpdate, (UpdateClient.IUpdateCallback) null);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e2) {
            e2.printStackTrace();
        }
    }

    private void startDXUpdate() {
        if ("2017050920".equals(Config.getChannel()) || "2015082715".equals(Config.getChannel())) {
            UpdateFromDX.getInstance(this).appCheckUpdate();
        }
    }

    public void setElemBindCallBack(IElemBindCallBack iElemBindCallBack2) {
        this.iElemBindCallBack = iElemBindCallBack2;
        getByTaobaoUserId();
    }

    private void getByTaobaoUserId() {
        BusinessRequest.getBusinessRequest().getByTaobaoUserId(new GetByTaobaoUserIdBusinessRequestListener(this));
    }

    private class GetByTaobaoUserIdBusinessRequestListener implements RequestListener<ElemBindBo> {
        private CoreActivity mContext;

        public GetByTaobaoUserIdBusinessRequestListener(CoreActivity context) {
            this.mContext = context;
        }

        public void onRequestDone(ElemBindBo data, int resultCode, String msg) {
            if (CoreApplication.getLoginHelper(CoreApplication.getApplication()).isLogin()) {
                ZpLogger.d(CoreActivity.TAG, "AliMemberSDK.getService(UccService.class).trustLogin");
                ((UccService) AliMemberSDK.getService(UccService.class)).setBindComponentProxy(new BindComponentProxyImpl(CoreActivity.this));
                ((UccService) AliMemberSDK.getService(UccService.class)).trustLogin((Activity) CoreActivity.this, Site.ELEME, (UccCallback) new UccCallback() {
                    public void onSuccess(String s, Map map) {
                        ZpLogger.d(CoreActivity.TAG, "AliMemberSDK.getService(UccService.class).trustLogin.onSuccess " + s + " " + map);
                        RtEnv.broadcast(RtBaseEnv.Msg.obtain("elemeBindSuccess", new Runnable() {
                            public void run() {
                                if (CoreActivity.this.iElemBindCallBack != null) {
                                    ZpLogger.d(CoreActivity.TAG, "AliMemberSDK.getService(UccService.class).trustLogin.onSuccess call iElemBindCallBack");
                                    CoreActivity.this.iElemBindCallBack.onSuccess(this);
                                }
                            }
                        }));
                    }

                    public void onFail(String s, int i, String s1) {
                        ZpLogger.d(CoreActivity.TAG, "AliMemberSDK.getService(UccService.class).trustLogin.onFail " + s + " " + i + " " + s1);
                        if (i == 1114) {
                            Intent intent = new Intent();
                            intent.setFlags(537001984);
                            intent.setClassName(CoreActivity.this, BaseConfig.SWITCH_TO_ELEM_BIND_GUILD);
                            CoreActivity.this.startActivityForResult(intent, 222);
                        }
                    }
                });
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 222) {
            return;
        }
        if (resultCode == 1 && this.iElemBindCallBack != null) {
            ZpLogger.d(TAG, "AliMemberSDK.getService(UccService.class).trustLogin.onSuccess call iElemBindCallBack");
            RtEnv.broadcast(RtBaseEnv.Msg.obtain("elemeBindSuccess", new Runnable() {
                public void run() {
                    if (CoreActivity.this.iElemBindCallBack != null) {
                        ZpLogger.d(CoreActivity.TAG, "AliMemberSDK.getService(UccService.class).trustLogin.onSuccess call iElemBindCallBack");
                        CoreActivity.this.iElemBindCallBack.onSuccess(this);
                    }
                }
            }));
        } else if (resultCode == 2 && this.iElemBindCallBack != null) {
            ZpLogger.d(TAG, "AliMemberSDK.getService(UccService.class).trustLogin.cancel call iElemBindCallBack");
            this.iElemBindCallBack.cancel();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isLoginToElemGuideActivity() {
        return false;
    }
}
