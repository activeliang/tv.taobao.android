package com.yunos.tv.blitz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import anet.channel.strategy.dispatch.DispatchConstants;
import com.alibaba.motu.videoplayermonitor.VPMConstants;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.taobao.tao.remotebusiness.js.MtopJSBridge;
import com.yunos.tv.alitvasrsdk.CommonData;
import com.yunos.tv.blitz.activity.BzBaseActivity;
import com.yunos.tv.blitz.data.BzResult;
import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.packagemanager.BzPackageManager;
import com.yunos.tv.blitz.request.BlitzMtopListener;
import com.yunos.tv.blitz.request.BusinessRequest;
import com.yunos.tv.blitz.request.base.BaseMtopRequest;
import com.yunos.tv.blitz.request.blitz.BlitzMtopRequest;
import com.yunos.tv.blitz.service.BaseBlitzService;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import com.yunos.tv.blitz.usertrack.BzUserTrackHandler;
import com.yunos.tv.blitz.video.BzDrmHelper;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsCallBridge {
    static final int E_JSCALL_ACCOUNT_APPLY_MTOPTOKEN = 45;
    static final int E_JSCALL_ACCOUNT_APPLY_NEWMTOPTOKEN = 46;
    static final int E_JSCALL_ACCOUNT_COMMON_API = 44;
    static final int E_JSCALL_CODE_CLICK = 7;
    static final int E_JSCALL_CODE_COMMIT_EVENT = 9;
    static final int E_JSCALL_CODE_DIALOG = 2;
    static final int E_JSCALL_CODE_GET_APP_INFO = 20;
    static final int E_JSCALL_CODE_GET_DEVICEINFO = 0;
    static final int E_JSCALL_CODE_GET_MTOP_HEADER_REQUEST = 13;
    static final int E_JSCALL_CODE_GET_MTOP_REQUEST = 15;
    static final int E_JSCALL_CODE_GET_TOKEN = 19;
    static final int E_JSCALL_CODE_GET_USERINFO = 5;
    static final int E_JSCALL_CODE_INVALIDATE = 34;
    static final int E_JSCALL_CODE_ISLOGIN = 6;
    static final int E_JSCALL_CODE_IS_NETWORK_AVAI = 12;
    static final int E_JSCALL_CODE_LOADING = 1;
    static final int E_JSCALL_CODE_LOGIN = 4;
    static final int E_JSCALL_CODE_NETWORK_DIALOG = 3;
    static final int E_JSCALL_CODE_PAGE_ENTER = 10;
    static final int E_JSCALL_CODE_PAGE_LEAVE = 11;
    static final int E_JSCALL_CODE_PAGE_LOAD_ERROR = 18;
    static final int E_JSCALL_CODE_PAGE_LOAD_FINISHED = 17;
    static final int E_JSCALL_CODE_PAGE_LOAD_START = 16;
    static final int E_JSCALL_CODE_START_ACTIVITY = 14;
    static final int E_JSCALL_CODE_STOP_LOADING = 27;
    static final int E_JSCALL_CODE_UPDATE_PAGE_PROP = 8;
    static final int E_JSCALL_CONTENT_REGISTER_OBSERVER = 32;
    static final int E_JSCALL_CONTENT_RESOLVER_CALL = 47;
    static final int E_JSCALL_CONTENT_RESOLVER_DELETE = 31;
    static final int E_JSCALL_CONTENT_RESOLVER_INSERT = 29;
    static final int E_JSCALL_CONTENT_RESOLVER_QUERY = 28;
    static final int E_JSCALL_CONTENT_RESOLVER_UPDATE = 30;
    static final int E_JSCALL_CONTENT_UNREGISTER_OBSERVER = 33;
    static final int E_JSCALL_DRM_GET_URL = 41;
    static final int E_JSCALL_EXIT = 42;
    static final int E_JSCALL_GET_IME_HEIGHT = 51;
    static final int E_JSCALL_GET_YOUKU_SECRET = 48;
    static final int E_JSCALL_HIDE_KEYBOARD = 50;
    static final int E_JSCALL_PACKAGE_APP_INFO = 37;
    static final int E_JSCALL_PACKAGE_CHECK_PERMISSION = 39;
    static final int E_JSCALL_PACKAGE_CHECK_SIGNATURES = 40;
    static final int E_JSCALL_PACKAGE_INSTALL = 35;
    static final int E_JSCALL_PACKAGE_INSTALLED_APPS = 38;
    static final int E_JSCALL_PACKAGE_UNINSTALL = 36;
    static final int E_JSCALL_PAY = 43;
    static final int E_JSCALL_REGISTER_BROADCAST = 21;
    static final int E_JSCALL_SEND_BROADCAST = 23;
    static final int E_JSCALL_SET_SCREENSAVER = 25;
    static final int E_JSCALL_SHOW_KEYBOARD = 49;
    static final int E_JSCALL_START_ACTIVITY_FOR_RESULT = 26;
    static final int E_JSCALL_TOAST = 24;
    static final int E_JSCALL_UNREGISTER_BROADCAST = 22;
    static final String TAG = JsCallBridge.class.getSimpleName();
    WeakReference<Context> mContext = null;
    Handler mHandler = new Handler();

    private native boolean nativeInitJsCallContext();

    private native boolean nativeReplyCallback(int i, boolean z, String str);

    public JsCallBridge(Context context) {
        this.mContext = new WeakReference<>(context.getApplicationContext());
        nativeInitJsCallContext();
    }

    public boolean replayCallback(int callback, boolean success, String result) {
        Log.d("JsCallBridge", "resultcallback =" + result);
        boolean ret = nativeReplyCallback(callback, success, result);
        if (!ret) {
            Log.w("JsCallBridge", "reply callback fail!");
        }
        return ret;
    }

    @SuppressLint({"Wakelock"})
    public String jsCallWithOperCode(String param, int callback, int operCode, int coreIndex) {
        Context currContext;
        JSONObject data;
        String param_append;
        String result = "HY_SUCCESS";
        WeakReference<Activity> activity = BzAppConfig.context.getCurrentActivity();
        WeakReference<Context> context = BzAppConfig.context.getCoreIndexContext(coreIndex);
        WeakReference<Dialog> dialog = BzAppConfig.context.getCurrentDialog();
        Log.d(TAG, "coreIndex = " + coreIndex + " mapsize = " + BzAppConfig.context.mContextMap.size() + " context = " + context);
        if (context != null && context.get() != null) {
            currContext = (Context) context.get();
        } else if (activity != null && activity.get() != null) {
            currContext = (Context) activity.get();
        } else if (dialog != null && dialog.get() != null) {
            currContext = ((Dialog) dialog.get()).getContext();
        } else if (coreIndex >= 0) {
            currContext = BzAppConfig.context.getContext();
        } else {
            Log.e(TAG, "activity or dialog is null, return fail!");
            return "HY_FAILED";
        }
        switch (operCode) {
            case 0:
                if (BzAppConfig.context.getJsCallBaseListener() != null) {
                    result = BzAppConfig.context.getJsCallBaseListener().onBaseGetDeviceInfo(currContext, param);
                    break;
                }
                break;
            case 1:
                if (BzAppConfig.context.getJsCallUIListener() != null) {
                    result = BzAppConfig.context.getJsCallUIListener().onUILoading(currContext, param);
                    break;
                }
                break;
            case 2:
                if (BzAppConfig.context.getJsCallUIListener() != null) {
                    result = BzAppConfig.context.getJsCallUIListener().onUIDialog(currContext, param);
                    break;
                }
                break;
            case 3:
                if (BzAppConfig.context.getJsCallUIListener() != null) {
                    result = BzAppConfig.context.getJsCallUIListener().onUINetworkDialog(currContext, param);
                    break;
                }
                break;
            case 4:
                if (BzAppConfig.context.getJsCallAccountListener() != null) {
                    result = BzAppConfig.context.getJsCallAccountListener().onAccountLogin(currContext, param);
                    break;
                }
                break;
            case 5:
                if (BzAppConfig.context.getJsCallAccountListener() != null) {
                    result = BzAppConfig.context.getJsCallAccountListener().onAccountGetUserInfo(currContext, param, callback);
                    break;
                }
                break;
            case 6:
                if (BzAppConfig.context.getJsCallAccountListener() != null) {
                    result = BzAppConfig.context.getJsCallAccountListener().onAccountIsLogin(currContext, param);
                    break;
                }
                break;
            case 7:
                result = BzUserTrackHandler.onUserTrackClick(param);
                break;
            case 8:
                result = BzUserTrackHandler.onUserTrackUpdatePageProperties(param);
                break;
            case 9:
                result = BzUserTrackHandler.onUserTrackCommitEvent(param);
                break;
            case 10:
                result = BzUserTrackHandler.onUserTrackPageEnter(param);
                break;
            case 11:
                result = BzUserTrackHandler.onUserTrackPageLeave(param);
                break;
            case 12:
                if (BzAppConfig.context.getJsCallNetListener() != null) {
                    result = BzAppConfig.context.getJsCallNetListener().onIsNetworkAvailable(currContext, param);
                    break;
                }
                break;
            case 14:
                if (BzAppConfig.context.getMiscListener() != null) {
                    BzAppConfig.context.getMiscListener().onStartActivity(currContext, param, callback);
                    break;
                }
                break;
            case 15:
                Log.d(TAG, "E_JSCALL_CODE_GET_MTOP_REQUEST param = " + param);
                String param_p = param;
                if (!(BzAppConfig.context.getMtopParamListener() == null || (param_append = BzAppConfig.context.getMtopParamListener().getAppendPamram(currContext, param)) == null)) {
                    Log.d(TAG, "E_JSCALL_CODE_GET_MTOP_REQUEST param_append=" + param_append);
                    try {
                        JSONObject jSONObject = new JSONObject(param);
                        JSONObject data2 = jSONObject.optJSONObject("data");
                        if (data2 == null) {
                            data2 = new JSONObject();
                        }
                        if (data2 != null) {
                            JSONObject appendparam = new JSONObject(param_append);
                            Iterator it = appendparam.keys();
                            while (it.hasNext()) {
                                String key = it.next().toString();
                                Log.d(TAG, "E_JSCALL_CODE_GET_MTOP_REQUEST key = " + key + ", value = " + appendparam.opt(key));
                                data2.put(key, appendparam.opt(key));
                            }
                            jSONObject.put("data", data2);
                        }
                        param_p = jSONObject.toString();
                    } catch (JSONException e3) {
                        e3.printStackTrace();
                    }
                }
                String param_f = param_p;
                Log.d(TAG, "E_JSCALL_CODE_GET_MTOP_REQUEST after trans param_f = " + param_f);
                final int _callback = callback;
                if (currContext == null) {
                    Log.d(TAG, "E_JSCALL_CODE_GET_MTOP_REQUEST no activity");
                    break;
                } else {
                    final String str = param_f;
                    final WeakReference weakReference = new WeakReference(currContext);
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            Log.d(JsCallBridge.TAG, "start ro load mtop request");
                            BlitzMtopRequest mtopRequest = new BlitzMtopRequest();
                            BusinessRequest mBusinessRequest = BusinessRequest.getBusinessRequest();
                            mtopRequest.resolveBlitzRequest(str);
                            if (mBusinessRequest != null) {
                                BlitzMtopListener bzlistener = new BlitzMtopListener(weakReference, _callback);
                                bzlistener.setRequestStr(str);
                                mBusinessRequest.baseRequest((BaseMtopRequest) mtopRequest, bzlistener, mtopRequest.getBlitzMtopNeedLogin(), mtopRequest.getBlitzMtopPost());
                            }
                        }
                    });
                    break;
                }
            case 16:
                BzAppConfig.context.pageLoadStart();
                if (BzAppConfig.context.getPageStatusListener() != null) {
                    BzAppConfig.context.getPageStatusListener().onPageLoadStart(currContext, param);
                    break;
                }
                break;
            case 17:
                BzAppConfig.context.pageLoadEnd(coreIndex);
                if (BzAppConfig.context.getPageStatusListener() != null) {
                    BzAppConfig.context.getPageStatusListener().onPageLoadFinished(currContext, param);
                    break;
                }
                break;
            case 18:
                if (BzAppConfig.context.getPageStatusListener() != null) {
                    String url = null;
                    try {
                        JSONObject jSONObject2 = new JSONObject(param);
                        try {
                            url = jSONObject2.getString("url");
                            JSONObject jSONObject3 = jSONObject2;
                        } catch (JSONException e) {
                            e = e;
                            JSONObject jSONObject4 = jSONObject2;
                            e.printStackTrace();
                            Log.d("JsCallBridge", "url:" + url + CommonData.PARAM + param);
                            BzAppConfig.context.getPageStatusListener().onPageLoadError(currContext, url, param);
                            return result;
                        }
                    } catch (JSONException e2) {
                        e = e2;
                        e.printStackTrace();
                        Log.d("JsCallBridge", "url:" + url + CommonData.PARAM + param);
                        BzAppConfig.context.getPageStatusListener().onPageLoadError(currContext, url, param);
                        return result;
                    }
                    Log.d("JsCallBridge", "url:" + url + CommonData.PARAM + param);
                    BzAppConfig.context.getPageStatusListener().onPageLoadError(currContext, url, param);
                }
            case 19:
                if (BzAppConfig.context.getJsCallAccountListener() != null) {
                    result = BzAppConfig.context.getJsCallAccountListener().onAccountGetToken(currContext, param, callback);
                    break;
                }
                break;
            case 20:
                BzResult bzRes = new BzResult();
                String pkgname = null;
                try {
                    pkgname = new JSONObject(param).getString("name");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                JSONArray jsonArray = new JSONArray();
                if (pkgname == null || pkgname.isEmpty()) {
                    for (PackageInfo pkginfo : BzAppConfig.context.getContext().getPackageManager().getInstalledPackages(0)) {
                        JSONObject pkgJson = new JSONObject();
                        try {
                            pkgJson.put("name", pkginfo.packageName);
                            pkgJson.put("versionname", pkginfo.versionName);
                            pkgJson.put("versioncode", pkginfo.versionCode);
                            jsonArray.put(pkgJson);
                        } catch (JSONException e4) {
                            e4.printStackTrace();
                        }
                    }
                    bzRes.addData("item", jsonArray);
                    bzRes.setSuccess();
                } else {
                    JSONObject itemJson = new JSONObject();
                    try {
                        PackageInfo info = BzAppConfig.context.getContext().getPackageManager().getPackageInfo(pkgname, 0);
                        itemJson.put("name", pkgname);
                        itemJson.put("versionname", info.versionName);
                        itemJson.put("versioncode", info.versionCode);
                        jsonArray.put(itemJson);
                        bzRes.addData("item", jsonArray);
                        bzRes.setSuccess();
                    } catch (PackageManager.NameNotFoundException e5) {
                        bzRes.setResult("HY_FAILED");
                    } catch (JSONException e6) {
                        bzRes.setResult("HY_FAILED");
                    }
                }
                result = bzRes.toJsonString();
                break;
            case 21:
                if (!(currContext instanceof BzBaseActivity)) {
                    if (currContext instanceof BaseBlitzService) {
                        ((BaseBlitzService) currContext).getBroadcastManager().registerBroadcast(param);
                        break;
                    }
                } else {
                    ((BzBaseActivity) currContext).getBroadcastManager().registerBroadcast(param);
                    break;
                }
                break;
            case 22:
                if (!(currContext instanceof BzBaseActivity)) {
                    if (currContext instanceof BzBaseActivity) {
                        ((BaseBlitzService) currContext).getBroadcastManager().unregisterBroadcast(param);
                        break;
                    }
                } else {
                    ((BzBaseActivity) currContext).getBroadcastManager().unregisterBroadcast(param);
                    break;
                }
                break;
            case 23:
                Log.i(TAG, "sendBroadCast: " + param);
                String action = null;
                String extras = null;
                if (BzAppConfig.context != null) {
                    try {
                        JSONObject jSONObject5 = new JSONObject(param);
                        if (jSONObject5.getString("blitzType").equals(DispatchConstants.ANDROID) && (data = jSONObject5.getJSONObject("data")) != null) {
                            action = data.getString("action");
                            try {
                                extras = data.getString(BlitzServiceUtils.CCLIENT_EXTRAS);
                            } catch (Exception e7) {
                                Log.i(TAG, "sendBroadCast: extras is empty.");
                            }
                        }
                        Log.i(TAG, "sendBroadCast: action = " + action + " extras = " + extras);
                        Intent intent = new Intent();
                        intent.setAction(action);
                        if (extras != null) {
                            try {
                                JSONObject jSONObject6 = new JSONObject(extras);
                                Iterator<String> keys = jSONObject6.keys();
                                while (keys.hasNext()) {
                                    String key2 = keys.next();
                                    intent.putExtra(key2, jSONObject6.getString(key2));
                                }
                            } catch (JSONException e8) {
                                Log.e(TAG, "extras isn't JSON");
                                intent.putExtra(BlitzServiceUtils.CCLIENT_EXTRAS, extras);
                            }
                        }
                        currContext.sendBroadcast(intent);
                        break;
                    } catch (JSONException e12) {
                        Log.e(TAG, "error:" + e12.toString());
                        e12.printStackTrace();
                        break;
                    }
                }
                break;
            case 24:
                try {
                    JSONObject jSONObject7 = new JSONObject(param);
                    String text = jSONObject7.getString(TuwenConstants.MODEL_LIST_KEY.TEXT);
                    int duration = 0;
                    try {
                        duration = jSONObject7.getInt(VPMConstants.MEASURE_DURATION);
                    } catch (JSONException e9) {
                        Log.d(TAG, "duration is empty.");
                    }
                    int fduration = duration;
                    Log.i("BLITZ", "duration = " + duration);
                    if (currContext != null && (currContext instanceof BzBaseActivity)) {
                        final BzBaseActivity baseActivity = (BzBaseActivity) currContext;
                        final String str2 = text;
                        final int i = fduration;
                        baseActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(baseActivity, str2, i).show();
                            }
                        });
                        break;
                    }
                } catch (JSONException e10) {
                    e10.printStackTrace();
                    break;
                }
            case 25:
                try {
                    JSONObject jSONObject8 = new JSONObject(param);
                    String state = jSONObject8.getString("state");
                    int timeout = -1;
                    try {
                        timeout = jSONObject8.getInt(MtopJSBridge.MtopJSParam.TIMEOUT);
                    } catch (JSONException e11) {
                        Log.d(TAG, "duration is empty.");
                    }
                    PowerManager.WakeLock wakeLock = null;
                    if (currContext != null) {
                        if (currContext instanceof BzBaseActivity) {
                            BzBaseActivity bzActivity = (BzBaseActivity) currContext;
                            if (bzActivity.getScreenWakeLock() == null) {
                                wakeLock = ((PowerManager) bzActivity.getSystemService("power")).newWakeLock(10, getClass().getName());
                                bzActivity.setScreenWakeLock(wakeLock);
                            } else {
                                wakeLock = bzActivity.getScreenWakeLock();
                            }
                        }
                    }
                    if (!state.equalsIgnoreCase("off")) {
                        if (state.equalsIgnoreCase("on")) {
                            if (timeout <= 0) {
                                wakeLock.acquire();
                                break;
                            } else {
                                wakeLock.acquire((long) timeout);
                                break;
                            }
                        }
                    } else {
                        wakeLock.release();
                        break;
                    }
                } catch (JSONException e13) {
                    e13.printStackTrace();
                    break;
                }
                break;
            case 26:
                if (BzAppConfig.context.getMiscListener() != null) {
                    try {
                        int resultCallback = new JSONObject(param).getInt("callback");
                        if (currContext instanceof BzBaseActivity) {
                            ((BzBaseActivity) currContext).setActivityResultCbData(resultCallback);
                        }
                    } catch (JSONException e14) {
                        e14.printStackTrace();
                    }
                    BzAppConfig.context.getMiscListener().onStartActivityForResult(currContext, param);
                    break;
                }
                break;
            case 27:
                if (BzAppConfig.context.getJsCallUIListener() != null) {
                    result = BzAppConfig.context.getJsCallUIListener().onUIStopLoading(currContext, param);
                    break;
                }
                break;
            case 28:
                result = BzAppConfig.context.getContentResolver().query(param);
                break;
            case 29:
                result = BzAppConfig.context.getContentResolver().insert(param);
                break;
            case 30:
                result = BzAppConfig.context.getContentResolver().update(param);
                break;
            case 31:
                result = BzAppConfig.context.getContentResolver().delete(param);
                break;
            case 32:
                result = BzAppConfig.context.getContentResolver().registerObserver(param);
                break;
            case 33:
                result = BzAppConfig.context.getContentResolver().unregisterObserver(param);
                break;
            case 34:
                SharedPreferences.Editor editor = ((Context) this.mContext.get()).getSharedPreferences("blitz_fps", 0).edit();
                editor.putString("fps_count", param);
                editor.apply();
                break;
            case 35:
                result = BzPackageManager.install(param);
                break;
            case 36:
                result = BzPackageManager.uninstall(param);
                break;
            case 37:
                result = BzPackageManager.getAppInfo(param);
                break;
            case 38:
                result = BzPackageManager.getInstalledApps(param);
                break;
            case 39:
                result = BzPackageManager.checkPermission(param);
                break;
            case 40:
                result = BzPackageManager.checkSignatures(param);
                break;
            case 41:
                try {
                    JSONObject jSONObject9 = new JSONObject(param);
                    String drm_token = null;
                    String url2 = null;
                    if (jSONObject9.has("drmToken")) {
                        drm_token = jSONObject9.getString("drmToken");
                    }
                    if (jSONObject9.has("url")) {
                        url2 = jSONObject9.getString("url");
                    }
                    BzBaseActivity act_drm = (BzBaseActivity) currContext;
                    if (currContext != null && (currContext instanceof BzBaseActivity)) {
                        new BzDrmHelper(new WeakReference(act_drm), callback).drmVideoPlay(url2, (String) null, drm_token, currContext);
                        break;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    break;
                }
            case 42:
                Process.killProcess(Process.myPid());
                break;
            case 43:
                if (BzAppConfig.context.getJsCallPayListener() != null) {
                    BzAppConfig.context.getJsCallPayListener().onPay(currContext, param, callback);
                    break;
                }
                break;
            case 44:
                if (BzAppConfig.context.getJsCallAccountListener() != null) {
                    result = BzAppConfig.context.getJsCallAccountListener().onAccountCommonApi(currContext, param, callback);
                    break;
                }
                break;
            case 45:
                if (BzAppConfig.context.getJsCallAccountListener() != null) {
                    result = BzAppConfig.context.getJsCallAccountListener().onAccountApplyMtopToken(currContext, param, callback);
                    break;
                }
                break;
            case 46:
                if (BzAppConfig.context.getJsCallAccountListener() != null) {
                    result = BzAppConfig.context.getJsCallAccountListener().onApplyNewMtopToken(currContext, param, callback);
                    break;
                }
                break;
            case 47:
                result = BzAppConfig.context.getContentResolver().call(param);
                break;
            case 48:
                if (BzAppConfig.context.getContext() != null) {
                    BzAppConfig.getInstance().getExtraDataFromSecurity(currContext, param, callback, "");
                    break;
                }
                break;
            case 49:
                if (currContext != null && (currContext instanceof BzBaseActivity)) {
                    ((BzBaseActivity) currContext).showInputMethod();
                    break;
                }
            case 50:
                if (currContext != null && (currContext instanceof BzBaseActivity)) {
                    ((BzBaseActivity) currContext).hideInputMethod();
                    break;
                }
            case 51:
                result = Integer.toString(BzAppConfig.context.getSoftKeyboardHeight(), 10);
                break;
        }
        return result;
    }
}
