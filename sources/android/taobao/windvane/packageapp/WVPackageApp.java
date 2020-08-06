package android.taobao.windvane.packageapp;

import android.app.Application;
import android.content.Context;
import android.taobao.windvane.config.EnvEnum;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.packageapp.zipapp.update.WVPackageUpdateListener;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WVPackageApp {
    private static String PRELOAD_ZIP = "preload_packageapp.zip";
    private static Map<String, List<WVPackageUpdateListener>> PackageUpdateListenerMaps = null;
    private static final String TAG = "WVPackageApp";
    private static boolean isInited = false;

    public static void setPreunzipPackageName(String pacakageName) {
        PRELOAD_ZIP = pacakageName;
    }

    public static String getPreunzipPackageName() {
        return !TextUtils.isEmpty(PRELOAD_ZIP) ? PRELOAD_ZIP : "preload_packageapp.zip";
    }

    public static synchronized void init(Context context, boolean checkupdate) {
        synchronized (WVPackageApp.class) {
            if (context == null) {
                TaoLog.e(TAG, "init fail. context cannot be null");
            } else {
                if (GlobalConfig.context == null) {
                    if (!(context instanceof Application)) {
                        TaoLog.e(TAG, "init fail. context should be application");
                    } else {
                        GlobalConfig.context = (Application) context;
                    }
                }
                if (!isInited) {
                    WVPackageAppManager.getInstance().init(context, checkupdate);
                    isInited = true;
                }
            }
        }
    }

    public static synchronized void registerPackageUpdateListener(String appName, WVPackageUpdateListener listener) {
        synchronized (WVPackageApp.class) {
            if (TextUtils.isEmpty(appName)) {
                if (GlobalConfig.env == EnvEnum.DAILY) {
                    throw new NullPointerException("AppName 不可以为空!");
                }
                TaoLog.d(TAG, "appName is null!");
            } else if (listener != null) {
                TaoLog.d(TAG, "appName:" + appName + " listener:" + listener);
                if (PackageUpdateListenerMaps == null) {
                    PackageUpdateListenerMaps = new HashMap();
                }
                List<WVPackageUpdateListener> packageUpdateListeners = PackageUpdateListenerMaps.get(appName);
                if (packageUpdateListeners == null) {
                    packageUpdateListeners = new ArrayList<>();
                    PackageUpdateListenerMaps.put(appName, packageUpdateListeners);
                }
                packageUpdateListeners.add(listener);
            } else if (GlobalConfig.env == EnvEnum.DAILY) {
                throw new NullPointerException("PackageUpdateListener 不可以为空!");
            } else {
                TaoLog.d(TAG, "packageUpdateListener is null!");
            }
        }
    }

    public synchronized void unRegisterPackageUpdateListener(String appName, WVPackageUpdateListener listener) {
        TaoLog.d(TAG, "appName:" + appName + " Listener:" + listener);
        if (PackageUpdateListenerMaps != null) {
            List<WVPackageUpdateListener> packageUpdateListeners = PackageUpdateListenerMaps.get(appName);
            if (packageUpdateListeners != null) {
                packageUpdateListeners.remove(listener);
            }
        }
    }

    protected static synchronized void notifyPackageUpdateFinish(String appName) {
        synchronized (WVPackageApp.class) {
            if (TextUtils.isEmpty(appName)) {
                if (GlobalConfig.env == EnvEnum.DAILY) {
                    throw new NullPointerException("appName 不能为空!");
                }
                TaoLog.e(TAG, "notify package update finish appName is null!");
            }
            TaoLog.d(TAG, "appName:" + appName);
            if (PackageUpdateListenerMaps != null) {
                List<WVPackageUpdateListener> packageUpdateListeners = PackageUpdateListenerMaps.get(appName);
                if (packageUpdateListeners != null) {
                    for (WVPackageUpdateListener listener : packageUpdateListeners) {
                        if (listener != null) {
                            listener.onPackageUpdateFinish(appName);
                        }
                    }
                }
            }
        }
    }
}
