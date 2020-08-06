package com.tvtaobao.android.runtime;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.tvtaobao.android.runtime.RtBaseEnv;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RtCmdHandler {
    public static final String ACTION_BROADCAST = "c.t.a.r.rtservices.broadcast";
    public static final String ACTION_CFG_CLR = "c.t.a.r.rtservices.cfgClr";
    public static final String ACTION_CFG_RMV = "c.t.a.r.rtservices.cfgRmv";
    public static final String ACTION_CFG_SET = "c.t.a.r.rtservices.cfgSet";
    public static final String ACTION_CLR = "c.t.a.r.rtservices.clr";
    public static final String ACTION_RMV = "c.t.a.r.rtservices.rmv";
    public static final String ACTION_SET = "c.t.a.r.rtservices.set";
    public static final String KEY_KEY = "key";
    public static final String KEY_VAL = "val";
    private static final String TAG = RtCmdHandler.class.getSimpleName();
    private static List<WeakReference<ICmdHandler>> handlerList = new ArrayList();
    private static List<WeakReference<ICmdHandler>> handlerList2 = new ArrayList();

    public interface ICmdHandler {
        void onCMD(Context context, Intent intent);
    }

    static void onCMD(Context context, Intent intent) {
        if (context != null && intent != null) {
            String action = intent.getAction();
            if (ACTION_SET.equals(action)) {
                String key = intent.getStringExtra("key");
                String val = intent.getStringExtra(KEY_VAL);
                Log.d(TAG, key + "," + val);
                if (!TextUtils.isEmpty(key)) {
                    RtBaseEnv.set(key, val);
                }
            } else if (ACTION_RMV.equals(action)) {
                String key2 = intent.getStringExtra("key");
                Log.d(TAG, key2);
                if (!TextUtils.isEmpty(key2)) {
                    RtBaseEnv.rmv(key2);
                }
            } else if (ACTION_CLR.equals(action)) {
                RtBaseEnv.clr();
            } else if (ACTION_CFG_SET.equals(action)) {
                String key3 = intent.getStringExtra("key");
                String val2 = intent.getStringExtra(KEY_VAL);
                Log.d(TAG, key3 + "," + val2);
                if (!TextUtils.isEmpty(key3)) {
                    RtBaseEnv.cfgSet(key3, val2);
                }
            } else if (ACTION_CFG_RMV.equals(action)) {
                String key4 = intent.getStringExtra("key");
                Log.d(TAG, key4);
                if (!TextUtils.isEmpty(key4)) {
                    RtBaseEnv.cfgRmv(key4);
                }
            } else if (ACTION_CFG_CLR.equals(action)) {
                RtBaseEnv.cfgClr();
            } else if (ACTION_BROADCAST.equals(action)) {
                RtBaseEnv.broadcast(RtBaseEnv.Msg.obtain(intent.getStringExtra("key"), intent.getStringExtra(KEY_VAL)));
            }
            dispatch(context, intent);
        }
    }

    private static void dispatch(Context context, Intent intent) {
        synchronized (handlerList2) {
            handlerList2.clear();
            synchronized (handlerList) {
                handlerList2.addAll(handlerList);
            }
            for (int i = 0; i < handlerList2.size(); i++) {
                WeakReference<ICmdHandler> tmp = handlerList2.get(i);
                if (!(tmp == null || tmp.get() == null)) {
                    ((ICmdHandler) tmp.get()).onCMD(context, intent);
                }
            }
        }
    }

    public static void register(ICmdHandler handler) {
        if (handler != null) {
            synchronized (handlerList) {
                for (int i = handlerList.size() - 1; i >= 0; i--) {
                    WeakReference<ICmdHandler> tmp = handlerList.get(i);
                    if (tmp != null && tmp.get() == handler) {
                        handlerList.remove(i);
                    }
                }
                handlerList.add(new WeakReference(handler));
            }
        }
    }

    public static void unregister(ICmdHandler handler) {
        synchronized (handlerList) {
            for (int i = handlerList.size() - 1; i >= 0; i--) {
                WeakReference<ICmdHandler> tmp = handlerList.get(i);
                if (tmp != null && tmp.get() == handler) {
                    handlerList.remove(i);
                }
            }
        }
    }

    public static void onDestroy() {
        synchronized (handlerList2) {
            handlerList2.clear();
        }
        synchronized (handlerList) {
            handlerList.clear();
        }
    }
}
