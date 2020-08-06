package com.yunos.tv.blitz.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.yunos.tv.blitz.global.BzAppConfig;
import java.util.ArrayList;
import java.util.Iterator;

public class NetworkUtil {
    private static NetworkUtil mInstance = null;
    /* access modifiers changed from: private */
    public boolean isConnected = true;
    ArrayList<BzNetworkChangeListener> listenerList = new ArrayList<>();
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            NetworkInfo info;
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                boolean unused = NetworkUtil.this.mLastIsConnected = NetworkUtil.this.isConnected;
                boolean unused2 = NetworkUtil.this.isConnected = NetworkUtil.isNetworkAvailable();
                if (NetworkUtil.this.isConnected != NetworkUtil.this.mLastIsConnected) {
                    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService("connectivity");
                    String netType = null;
                    if (!(connManager == null || (info = connManager.getActiveNetworkInfo()) == null || !info.isConnectedOrConnecting())) {
                        netType = info.getTypeName();
                    }
                    Iterator<BzNetworkChangeListener> it = NetworkUtil.this.listenerList.iterator();
                    while (it.hasNext()) {
                        it.next().onNetworkChanged(NetworkUtil.this.isConnected, netType);
                    }
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mLastIsConnected = false;

    public interface BzNetworkChangeListener {
        void onNetworkChanged(boolean z, String str);
    }

    public enum HttpMethod {
        Get,
        Post
    }

    private NetworkUtil() {
    }

    public static NetworkUtil getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkUtil();
        }
        return mInstance;
    }

    public static boolean isNetworkAvailable() {
        boolean isAvailable = false;
        Context context = BzAppConfig.context.getContext();
        if (context == null) {
            isAvailable = false;
        }
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connManager == null) {
            isAvailable = false;
        }
        NetworkInfo info = connManager.getActiveNetworkInfo();
        if (info == null || !info.isConnectedOrConnecting()) {
            return isAvailable;
        }
        return info.getType() >= 0;
    }

    public void addNetworkChangeListner(BzNetworkChangeListener listener) {
        this.listenerList.add(listener);
    }

    public void removeNetworkChangeListener(BzNetworkChangeListener listener) {
        this.listenerList.remove(listener);
    }

    public void init() {
        BzAppConfig.context.getContext().registerReceiver(this.mBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        this.isConnected = isNetworkAvailable();
        this.mLastIsConnected = this.isConnected;
    }

    public void uninit() {
        BzAppConfig.context.getContext().unregisterReceiver(this.mBroadcastReceiver);
    }
}
