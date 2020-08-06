package com.yunos.tv.blitz.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.yunos.tv.blitz.account.BzDebugLog;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONException;
import org.json.JSONObject;

public class BlitzBroadcastManager {
    private static final String TAG = "BlitzBroadcastManager";
    private Map<String, BlitzBroadcastReceiver> mBroadcastMap = new TreeMap();
    private WeakReference<Context> mContext;

    /* access modifiers changed from: private */
    public native void nativeReceiveBroadcast(String str, String str2);

    private native void nativeReleaseBroadcast();

    public class BlitzBroadcastReceiver extends BroadcastReceiver {
        public BlitzBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Log.i(BlitzBroadcastManager.TAG, "onReceive--->");
            String action = intent.getAction();
            String data = null;
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                JSONObject json = new JSONObject();
                try {
                    for (String key : bundle.keySet()) {
                        if (Build.VERSION.SDK_INT >= 19) {
                            json.put(key, JSONObject.wrap(bundle.get(key)));
                        } else {
                            json.put(key, bundle.get(key));
                        }
                    }
                    if (intent.getDataString() != null && !intent.getDataString().isEmpty()) {
                        json.put("intentData", intent.getDataString());
                    }
                } catch (JSONException e) {
                }
                data = json.toString();
                Log.i(BlitzBroadcastManager.TAG, "data = " + data);
            }
            BlitzBroadcastManager.this.nativeReceiveBroadcast(action, data);
        }
    }

    public BlitzBroadcastManager(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    public void registerBroadcast(String param) {
        try {
            JSONObject paramJson = new JSONObject(param);
            String action = paramJson.getString("action");
            String dataScheme = paramJson.optString("dataScheme", "");
            String category = paramJson.optString("category", "");
            int priority = paramJson.optInt("priority");
            if (this.mContext == null || this.mContext.get() == null) {
                BzDebugLog.e(TAG, "mContext is Null");
                return;
            }
            if (this.mBroadcastMap.get(action) != null) {
                unregisterBroadcast(action);
            }
            BlitzBroadcastReceiver receiver = new BlitzBroadcastReceiver();
            this.mBroadcastMap.put(action, receiver);
            IntentFilter filter = new IntentFilter();
            filter.addAction(action);
            if (!dataScheme.isEmpty()) {
                filter.addDataScheme(dataScheme);
            }
            if (!category.isEmpty()) {
                filter.addCategory(category);
            }
            if (priority != 0) {
                filter.setPriority(priority);
            }
            BzDebugLog.i(TAG, "registerBroadcast: " + action + " dataScheme = " + dataScheme + " priority = " + priority + ", category = " + category);
            ((Context) this.mContext.get()).registerReceiver(receiver, filter);
        } catch (Exception e) {
            BzDebugLog.i(TAG, e.toString());
        }
    }

    public void unregisterBroadcast(String action) {
        BlitzBroadcastReceiver receiver = this.mBroadcastMap.get(action);
        if (!(receiver == null || this.mContext == null || this.mContext.get() == null)) {
            Log.i(TAG, "unregisterBroadcast find: " + action);
            ((Context) this.mContext.get()).unregisterReceiver(receiver);
        }
        this.mBroadcastMap.remove(action);
        Log.i(TAG, "unregisterBroadcast: " + action + " mapSize = " + this.mBroadcastMap.size());
    }

    public void onDestory() {
        Log.i(TAG, "onDestory");
        if (this.mBroadcastMap != null) {
            for (String action : this.mBroadcastMap.keySet()) {
                BlitzBroadcastReceiver receiver = this.mBroadcastMap.get(action);
                if (!(receiver == null || this.mContext == null || this.mContext.get() == null)) {
                    ((Context) this.mContext.get()).unregisterReceiver(receiver);
                }
            }
        }
        this.mContext.clear();
        this.mContext = null;
        this.mBroadcastMap.clear();
    }
}
