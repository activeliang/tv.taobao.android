package com.yunos.tvtaobao.biz.net.network;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.yunos.tvtaobao.biz.net.exception.NoNetworkException;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.HashSet;
import java.util.Iterator;

public class NetworkManager {
    public static final String TAG = "NetworkManager";
    public static final int UNCONNECTED = -9999;
    private static NetworkManager networkManager = null;
    private Context applicationContext;
    /* access modifiers changed from: private */
    public boolean isConnected = true;
    /* access modifiers changed from: private */
    public HashSet<INetworkListener> listenerSet = new HashSet<>();
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                boolean unused = NetworkManager.this.mLastIsConnected = NetworkManager.this.isConnected;
                boolean unused2 = NetworkManager.this.isConnected = NetworkManager.isNetworkAvailable(context);
                Iterator it = NetworkManager.this.listenerSet.iterator();
                while (it.hasNext()) {
                    ((INetworkListener) it.next()).onNetworkChanged(NetworkManager.this.isConnected, NetworkManager.this.mLastIsConnected);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mLastIsConnected = false;

    public interface INetworkListener {
        void onNetworkChanged(boolean z, boolean z2);
    }

    private NetworkManager() {
    }

    public static NetworkManager instance() {
        if (networkManager == null) {
            networkManager = new NetworkManager();
        }
        return networkManager;
    }

    public void init(Context context) {
        init(context, (NoNetworkException.NoNetworkHanler) null);
    }

    public void init(Context context, NoNetworkException.NoNetworkHanler noNetworkHanler) {
        Context applicationContext2;
        this.applicationContext = context;
        if ((context instanceof Activity) && (applicationContext2 = context.getApplicationContext()) != null) {
            this.applicationContext = applicationContext2;
        }
        this.applicationContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        NoNetworkException.setNoNetworkHanler(noNetworkHanler);
        this.isConnected = isNetworkAvailable(context);
        this.mLastIsConnected = this.isConnected;
    }

    public void release() {
        this.applicationContext.unregisterReceiver(this.mBroadcastReceiver);
    }

    public void registerStateChangedListener(INetworkListener l) {
        if (this.listenerSet.add(l)) {
        }
        ZpLogger.i(TAG, "registerStateChangedListener, size:" + this.listenerSet.size());
    }

    public void unregisterStateChangedListener(INetworkListener l) {
        this.listenerSet.remove(l);
        ZpLogger.i(TAG, "unregisterStateChangedListener, size:" + this.listenerSet.size());
    }

    public boolean isNetworkConnected() {
        return this.isConnected;
    }

    public Context getApplicationContext() {
        return this.applicationContext;
    }

    public static int getNetworkType(Context context) {
        Context applicationContext2;
        if ((context instanceof Activity) && (applicationContext2 = context.getApplicationContext()) != null) {
            context = applicationContext2;
        }
        NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info == null || !info.isConnected() || !info.isAvailable()) {
            return UNCONNECTED;
        }
        return info.getType();
    }

    public static boolean isNetworkAvailable(Context context) {
        return -9999 != getNetworkType(context);
    }
}
