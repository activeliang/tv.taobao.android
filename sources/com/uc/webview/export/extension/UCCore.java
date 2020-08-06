package com.uc.webview.export.extension;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.media.session.PlaybackStateCompat;
import android.taobao.windvane.monitor.WVMonitorConstants;
import android.util.Pair;
import android.webkit.ValueCallback;
import com.uc.webview.export.WebResourceResponse;
import com.uc.webview.export.WebView;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.internal.b;
import com.uc.webview.export.internal.c.a;
import com.uc.webview.export.internal.c.c;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.INetwork;
import com.uc.webview.export.internal.interfaces.INetworkDecider;
import com.uc.webview.export.internal.interfaces.INetworkDelegate;
import com.uc.webview.export.internal.interfaces.IWaStat;
import com.uc.webview.export.internal.interfaces.InvokeObject;
import com.uc.webview.export.internal.setup.UCMPackageInfo;
import com.uc.webview.export.internal.setup.z;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.ReflectionUtil;
import com.uc.webview.export.utility.SetupTask;
import com.uc.webview.export.utility.download.UpdateTask;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;
import java.util.concurrent.Callable;

@Api
/* compiled from: ProGuard */
public class UCCore {
    public static final String CD_DISABLE_UCDNS = "disable_ucdns";
    public static final String CD_ENABLE_NET_THREAD_REDUCE = "dec_thread";
    public static final String CD_ENABLE_TRAFFIC_STAT = "traffic_stat";
    public static final int COMPATIBLE_POLICY_ALL = 7;
    public static final int COMPATIBLE_POLICY_ARMV5 = 1;
    public static final int COMPATIBLE_POLICY_ARMV7 = 2;
    public static final int COMPATIBLE_POLICY_X86 = 4;
    public static final int CORE_EVENT_CLEAR_DNS_CACHE = 0;
    public static final int CORE_EVENT_GET_HTTP_CACHE_SIZE = 1;
    public static final int DELETE_CORE_POLICY_ALL = 7;
    public static final int DELETE_CORE_POLICY_LOAD_SO_ERROR = 2;
    public static final int DELETE_CORE_POLICY_MULTI_CRASH = 4;
    public static final int DELETE_CORE_POLICY_NONE = 0;
    public static final int DELETE_CORE_POLICY_SO_SIZE_MISMATCH = 1;
    public static final String LOAD_POLICY_SPECIFIED_ONLY = "SPECIFIED_ONLY";
    public static final String LOAD_POLICY_SPECIFIED_OR_UCMOBILE = "SPECIFIED_OR_UCMOBILE";
    public static final String LOAD_POLICY_UCMOBILE_ONLY = "UCMOBILE_ONLY";
    public static final String LOAD_POLICY_UCMOBILE_OR_SPECIFIED = "UCMOBILE_OR_SPECIFIED";
    public static final String OPTION_BREAKPAD_CONFIG = "BREAKPAD_CONFIG";
    public static final String OPTION_COMPATIBLE_POLICY = "COMPATIBLE_POLICY";
    public static final String OPTION_CONTEXT = "CONTEXT";
    public static final String OPTION_CORE_VERSION_EXCLUDE = "core_ver_excludes";
    public static final String OPTION_DELETE_CORE_POLICY = "delete_core";
    public static final String OPTION_DEX_FILE_PATH = "dexFilePath";
    public static final String OPTION_DOWNLOAD_CHECKER = "dlChecker";
    public static final String OPTION_DWN_RETRY_MAX_WAIT_MILIS = "dwnRetryMaxWait";
    public static final String OPTION_DWN_RETRY_WAIT_MILIS = "dwnRetryWait";
    public static final String OPTION_GRANT_ALL_BUILDS = "grant_all_builds";
    public static final String OPTION_HARDWARE_ACCELERATED = "AC";
    public static final String OPTION_INIT_IN_SETUP_THREAD = "init_setup_thread";
    public static final String OPTION_LOAD_POLICY = "loadPolicy";
    public static final String OPTION_MULTI_CORE_TYPE = "MULTI_CORE_TYPE";
    public static final String OPTION_PROVIDED_KEYS = "provided_keys";
    public static final String OPTION_RES_FILE_PATH = "resFilePath";
    public static final String OPTION_SETUP_THREAD_PRIORITY = "setup_priority";
    public static final String OPTION_SHARE_CORE = "share_core";
    public static final String OPTION_SO_FILE_PATH = "soFilePath";
    public static final String OPTION_UCM_CFG_FILE = "ucmCfgFile";
    public static final String OPTION_UCM_KRL_DIR = "ucmKrlDir";
    public static final String OPTION_UCM_LIB_DIR = "ucmLibDir";
    public static final String OPTION_UCM_UPD_URL = "ucmUpdUrl";
    public static final String OPTION_UCM_ZIP_DIR = "ucmZipDir";
    public static final String OPTION_UCM_ZIP_FILE = "ucmZipFile";
    public static final String OPTION_UC_PLAYER_ROOT = "ucPlayerRoot";
    public static final String OPTION_UC_PROXY_ADBLOCK = "proxy_adblock";
    public static final String OPTION_UPD_SETUP_TASK_WAIT_MILIS = "updWait";
    public static final String OPTION_USE_SDK_SETUP = "sdk_setup";
    public static final String OPTION_USE_SYSTEM_WEBVIEW = "SYSTEM_WEBVIEW";
    public static final String OPTION_USE_UC_PLAYER = "ucPlayer";
    public static final String OPTION_VERIFY_POLICY = "VERIFY_POLICY";
    public static final String OPTION_VIDEO_HARDWARE_ACCELERATED = "VIDEO_AC";
    public static final String OPTION_WAP_DENY = "wap_deny";
    public static final String OPTION_WEBVIEW_POLICY = "WEBVIEW_POLICY";
    public static final int VERIFY_POLICY_ALL = 1073741839;
    public static final int VERIFY_POLICY_BROWSER_IF = 2;
    public static final int VERIFY_POLICY_CORE_IMPL = 4;
    public static final int VERIFY_POLICY_NONE = 0;
    public static final int VERIFY_POLICY_QUICK = 1073741824;
    public static final int VERIFY_POLICY_SDK_SHELL = 1;
    public static final int VERIFY_POLICY_SO = 8;
    public static final int WEBVIEW_POLICY_WAIT_UNTIL_EXCEPTION = 3;
    public static final int WEBVIEW_POLICY_WAIT_UNTIL_FALLBACK_SYSTEM = 2;
    public static final int WEBVIEW_POLICY_WAIT_UNTIL_LOADED = 1;
    private static z a = new z();

    static /* synthetic */ void a(Context context, String str, Callable callable, Map map) {
        File uCPlayerRoot = UpdateTask.getUCPlayerRoot(context);
        d.a(10002, Long.valueOf(PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID));
        d.a(10002, Long.valueOf(PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH));
        d.a(10002, Long.valueOf(PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM));
        d.a(10002, Long.valueOf(PlaybackStateCompat.ACTION_PLAY_FROM_URI));
        d.a(10002, Long.valueOf(PlaybackStateCompat.ACTION_PREPARE));
        d.a(10002, Long.valueOf(PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID));
        d.a(10002, Long.valueOf(PlaybackStateCompat.ACTION_PREPARE_FROM_SEARCH));
        d.v.remove(IWaStat.VIDEO_ERROR_CODE_UPDATE_CHECK_REQUEST);
        d.v.remove(IWaStat.VIDEO_ERROR_CODE_DOWNLOAD);
        d.v.remove(IWaStat.VIDEO_ERROR_CODE_VERIFY);
        d.v.remove(IWaStat.VIDEO_ERROR_CODE_UNZIP);
        Context context2 = context;
        String str2 = str;
        new UpdateTask(context2, str2, uCPlayerRoot.getAbsolutePath(), "libu3player.so", new c(), (Long) null, (Long) null).onEvent("beginDownload", new b()).onEvent("beginUnZip", new k()).onEvent("unzipSuccess", new j()).onEvent("check", new i(callable)).onEvent(BlitzServiceUtils.CSUCCESS, new h(context, map)).onEvent("failed", new g()).onEvent("exception", new e(map)).onEvent("exists", new d(map, uCPlayerRoot)).start();
    }

    public static int init(Context context, boolean z, BreakpadConfig breakpadConfig, Map<String, String> map) {
        ((SetupTask) ((SetupTask) a.setup("CONTEXT", context)).setup(OPTION_BREAKPAD_CONFIG, breakpadConfig)).setup(OPTION_USE_SYSTEM_WEBVIEW, Boolean.valueOf(z));
        if (map != null) {
            for (Map.Entry next : map.entrySet()) {
                a.setup((String) next.getKey(), next.getValue());
            }
        }
        d.v.remove(IWaStat.ERROR_CODE_INIT);
        a.startSync();
        return 0;
    }

    public static SetupTask setup(String str, Object obj) {
        return (SetupTask) a.setup(str, obj);
    }

    public static void update(Context context, String str, Callable<Boolean> callable) {
        a.a(str, callable);
    }

    public static void updateUCPlayer(Context context, String str, Callable<Boolean> callable) {
        updateUCPlayer(context, str, callable, (Map<String, ValueCallback>) null);
    }

    public static void updateUCPlayer(Context context, String str, Callable<Boolean> callable, Map<String, ValueCallback> map) {
        new a(context, str, callable, map).start();
    }

    public static void setLocationManager(ILocationManager iLocationManager) {
        if (d.d != null) {
            d.d.setLocationManagerUC(iLocationManager);
        }
    }

    public static void onLowMemory() {
        if (d.d != null) {
            try {
                d.d.onLowMemory();
            } catch (Throwable th) {
            }
        }
    }

    public static void onTrimMemory(int i) {
        if (d.d != null) {
            try {
                d.d.onTrimMemory(i);
            } catch (Throwable th) {
            }
        }
    }

    public static void uploadCrashLogs() {
        b.a();
    }

    public static void setNotAvailableUCListener(NotAvailableUCListener notAvailableUCListener) {
        d.a = notAvailableUCListener;
    }

    public static String getCoreInfo() {
        return (String) d.a(10046, new Object[0]);
    }

    public static void setPrintLog(boolean z) {
        Log.sPrintLog = z;
    }

    public static void setDrawableResource(String str, Drawable drawable) {
        if (d.d != null) {
            d.d.getWebResources().setDrawable(str, drawable);
        }
    }

    public static Pair<Long, Long> getTraffic() {
        return (Pair) d.a((int) UCMPackageInfo.initUCMBuildInfo, new Object[0]);
    }

    public static WebResourceResponse getResponseByUrl(String str) {
        return (WebResourceResponse) d.a(10031, str);
    }

    public static void setThirdNetwork(INetwork iNetwork, INetworkDecider iNetworkDecider) {
        if (WebView.getCoreType() != 2 && d.d != null) {
            d.d.setThirdNetwork(iNetwork, iNetworkDecider);
        }
    }

    public static void setNetworkDelegate(INetworkDelegate iNetworkDelegate) {
        if (WebView.getCoreType() != 2 && d.d != null) {
            Log.e("network delegate", "invoke setNetworkDelegate");
            try {
                ReflectionUtil.invoke((Object) d.d, "setNetworkDelegate", new Class[]{InvokeObject.class}, new Object[]{new c(iNetworkDelegate)});
            } catch (Exception e) {
                Log.e("network delegate", "setNetworkDelegate", e);
            }
        }
    }

    public static void extractWebCoreLibraryIfNeeded(Context context, String str, String str2, boolean z) {
        UCCyclone.decompressIfNeeded(context, true, new File(str), new File(str2), (FilenameFilter) null, z);
    }

    public static void setInitCallback(InitCallback initCallback) {
        d.m = initCallback;
    }

    public static void setStatDataCallback(ValueCallback<String> valueCallback) {
        d.t = valueCallback;
    }

    public static void setParam(String str) {
        d.a(10004, str);
    }

    public static String getParam(String str) {
        return (String) d.a(10005, str);
    }

    public static void updateTypefacePath(String str, Runnable runnable) {
        d.a((int) UCMPackageInfo.deleteTempDecFiles, str, runnable);
    }

    public static void clearHttpCache() {
        if (WebView.getCoreType() != 2 && d.d != null) {
            d.d.updateBussinessInfo(3, 0, "SETTING_CLEAR_RECORD", WVMonitorConstants.FORCE_ONLINE_FAILED);
        }
    }

    public static Object notifyCoreEvent(int i, Object obj) {
        if (!(WebView.getCoreType() == 2 || d.d == null)) {
            Log.d("notifyCoreEvent", "notifyCoreEvent");
            try {
                return ReflectionUtil.invoke((Object) d.d, "notifyCoreEvent", new Class[]{InvokeObject.class}, new Object[]{new a(i, obj)});
            } catch (Throwable th) {
                Log.e("notifyCoreEvent", "notifyCoreEvent", th);
            }
        }
        return null;
    }
}
