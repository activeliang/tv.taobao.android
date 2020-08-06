package com.yunos.tv.blitz.service;

import android.content.Context;
import android.content.Intent;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class BlitzServiceClient implements IBlitzServiceClientListener {
    private static final String TAG = BlitzServiceClient.class.getSimpleName();
    private static BlitzServiceClient mBlitzServiceClient = null;
    private IBlitzServiceCallAndroidListener mBlitzServiceCallAndroidListener = null;
    /* access modifiers changed from: private */
    public Context mContext;
    private HashMap<String, BlitzServiceClientItem> mServiceClientMap = new HashMap<>();

    private native boolean callServiceListenerIface(String str);

    private native boolean registerJavaMethodToNative();

    private native boolean release();

    private native boolean returnCallServiceIfaceResult(String str);

    private native boolean setStartServiceResult(String str);

    public static BlitzServiceClient getInstance(Context context) {
        if (mBlitzServiceClient == null) {
            synchronized (BlitzServiceClient.class) {
                if (mBlitzServiceClient == null) {
                    mBlitzServiceClient = new BlitzServiceClient(context);
                }
            }
        }
        return mBlitzServiceClient;
    }

    public static void destroyInstance() {
        if (mBlitzServiceClient != null) {
            mBlitzServiceClient.unBindAllService();
            mBlitzServiceClient = null;
        }
    }

    private void unBindAllService() {
        for (Map.Entry<String, BlitzServiceClientItem> entry : this.mServiceClientMap.entrySet()) {
            entry.getValue().unBindBlitzService("");
        }
        this.mServiceClientMap.clear();
    }

    public void setOnBlitzServiceCallAndroidListener(IBlitzServiceCallAndroidListener listener) {
        this.mBlitzServiceCallAndroidListener = listener;
    }

    public void setStartServiceResultForAndroid(String params) {
        setStartServiceResult(params);
    }

    public void returnCallServiceIfaceResultForAndroid(String params) {
        returnCallServiceIfaceResult(params);
    }

    private String native2JavaMethodCall(String params, int cbId, int operType) {
        Log.i(TAG, "native2JavaMethodCall, params = " + params + ", cbId = " + cbId + ", operType = " + operType);
        switch (operType) {
            case 0:
                startBlitzService(params);
                return BlitzServiceUtils.CSUCCESS;
            case 1:
                stopBlitzService(params);
                return BlitzServiceUtils.CSUCCESS;
            case 2:
                bindBlitzService(params);
                return BlitzServiceUtils.CSUCCESS;
            case 3:
                unBlitzService(params);
                return BlitzServiceUtils.CSUCCESS;
            case 4:
                callServiceInterface(params);
                return BlitzServiceUtils.CSUCCESS;
            default:
                return BlitzServiceUtils.CSUCCESS;
        }
    }

    public BlitzServiceClient(Context context) {
        this.mContext = context;
    }

    private boolean isConnectToAndroidService(String uri) {
        return uri.substring(uri.lastIndexOf(WVUtils.URL_DATA_CHAR) + 1, uri.length()).equalsIgnoreCase("platform=android");
    }

    private void startBlitzService(final String params) {
        Log.i(TAG, "startBlitzService params = " + params);
        new Thread(new Runnable() {
            public void run() {
                BlitzServiceClient.this.mContext.startService(new Intent(BlitzServiceUtils.getServiceUriWithoutPlatformInfo(params)));
            }
        }).start();
    }

    private void stopBlitzService(final String params) {
        Log.i(TAG, "stopBlitzService params = " + params);
        new Thread(new Runnable() {
            public void run() {
                try {
                    BlitzServiceClient.this.mContext.stopService(new Intent(BlitzServiceUtils.getServiceUriWithoutPlatformInfo(new JSONObject(params).getString(BlitzServiceUtils.CSERVICE_URI))));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void bindBlitzService(String params) {
        Log.i(TAG, "bindBlitzService params = " + params);
        try {
            String serviceUri = new JSONObject(params).getString(BlitzServiceUtils.CSERVICE_URI);
            if (isConnectToAndroidService(serviceUri) && this.mBlitzServiceCallAndroidListener != null) {
                Log.d(TAG, "[android service],  serviceUri = " + serviceUri);
                this.mBlitzServiceCallAndroidListener.bindService(params);
            } else if (!this.mServiceClientMap.containsKey(serviceUri)) {
                BlitzServiceClientItem clientItem = new BlitzServiceClientItem(this.mContext, serviceUri);
                clientItem.setBlitzServiceClientListener(this);
                clientItem.bindBlitzService(params);
                this.mServiceClientMap.put(serviceUri, clientItem);
            } else {
                Log.w(TAG, "service has in use, serviceUri = " + serviceUri);
                this.mServiceClientMap.get(serviceUri).setStartServiceResult(params);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void unBlitzService(String params) {
        Log.i(TAG, "unBlitzService params = " + params);
        try {
            String serviceUri = new JSONObject(params).getString(BlitzServiceUtils.CSERVICE_URI);
            if (isConnectToAndroidService(serviceUri) && this.mBlitzServiceCallAndroidListener != null) {
                this.mBlitzServiceCallAndroidListener.unBindService(params);
            } else if (this.mServiceClientMap.containsKey(serviceUri)) {
                this.mServiceClientMap.get(serviceUri).unBindBlitzService(params);
                this.mServiceClientMap.remove(serviceUri);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callServiceInterface(String params) {
        Log.i(TAG, "callServiceInterface params = " + params);
        try {
            String serviceUri = new JSONObject(params).getString(BlitzServiceUtils.CSERVICE_URI);
            if (!isConnectToAndroidService(serviceUri) || this.mBlitzServiceCallAndroidListener == null) {
                this.mServiceClientMap.get(serviceUri).callServiceInterface(params);
            } else {
                this.mBlitzServiceCallAndroidListener.callInterfaceMethod(params);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean init() {
        return registerJavaMethodToNative();
    }

    public void deinit() {
        release();
    }

    public void setItemStartServiceResult(String params) {
        setStartServiceResult(params);
    }

    public void returnItemCallServiceIfaceResult(String params) {
        returnCallServiceIfaceResult(params);
    }

    public void callItemServiceListenerIface(String params) {
        callServiceListenerIface(params);
    }
}
