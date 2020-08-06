package mtopsdk.xstate;

import android.content.Context;
import android.content.IntentFilter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import mtopsdk.common.util.TBSdkLog;
import mtopsdk.xstate.network.NetworkStateReceiver;

public class XStateDelegate {
    private static final String TAG = "mtopsdk.XStateDelegate";
    private static Context context;
    private static volatile boolean isInit = false;
    private static Lock lock = new ReentrantLock();
    private static NetworkStateReceiver netReceiver;
    private static ConcurrentHashMap<String, String> stateIDs = null;

    public static Context getContext() {
        return context;
    }

    private static void checkInit(Context context2) {
        lock.lock();
        try {
            if (!isInit) {
                if (context2 == null) {
                    TBSdkLog.e(TAG, "[checkInit]parameter context for init(Context context) is null.");
                    lock.unlock();
                    return;
                }
                if (stateIDs == null) {
                    stateIDs = new ConcurrentHashMap<>();
                }
                context = context2;
                if (netReceiver == null) {
                    netReceiver = new NetworkStateReceiver();
                    IntentFilter netfilter = new IntentFilter();
                    netfilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                    context2.registerReceiver(netReceiver, netfilter);
                }
                isInit = true;
                if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                    TBSdkLog.i(TAG, "[checkInit] init XState OK,isInit=" + isInit);
                }
            }
        } catch (Throwable e) {
            try {
                TBSdkLog.e(TAG, "[checkInit] checkInit error --" + e.toString());
                return;
            } finally {
                lock.unlock();
            }
        }
        lock.unlock();
    }

    public static void init(Context context2) {
        if (!isInit) {
            checkInit(context2);
        }
    }

    public static void unInit() {
        if (isInit) {
            lock.lock();
            try {
                if (isInit) {
                    if (stateIDs != null) {
                        stateIDs.clear();
                        stateIDs = null;
                    }
                    if (context == null) {
                        TBSdkLog.e(TAG, "[unInit] context in Class XState is null.");
                        return;
                    }
                    try {
                        if (netReceiver != null) {
                            context.unregisterReceiver(netReceiver);
                            netReceiver = null;
                        }
                    } catch (Throwable e) {
                        TBSdkLog.e(TAG, "[unRegisterReceive]unRegisterReceive failed", e);
                    }
                    isInit = false;
                    if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.InfoEnable)) {
                        TBSdkLog.i(TAG, "[unInit] unInit XState OK,isInit=" + isInit);
                    }
                }
                lock.unlock();
            } catch (Exception e2) {
                TBSdkLog.e(TAG, "[unInit] unInit error --" + e2.toString());
            } finally {
                lock.unlock();
            }
        }
    }

    public static String getValue(String key) {
        if (stateIDs == null || key == null) {
            return null;
        }
        return stateIDs.get(key);
    }

    public static void setValue(String key, String value) {
        if (stateIDs != null && key != null && value != null) {
            stateIDs.put(key, value);
            if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
                TBSdkLog.d(TAG, "[setValue]set  XStateID succeed," + key + "=" + value);
            }
        } else if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, "[setValue]set  XStateID failed,key=" + key + ",value=" + value);
        }
    }

    public static String removeKey(String key) {
        if (stateIDs == null || key == null) {
            return null;
        }
        if (TBSdkLog.isLogEnable(TBSdkLog.LogEnable.DebugEnable)) {
            TBSdkLog.d(TAG, "remove XState key=" + key);
        }
        return stateIDs.remove(key);
    }
}
