package com.yunos.tv.blitz.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.yunos.tv.blitz.BlitzContextWrapper;
import com.yunos.tv.blitz.broadcast.BlitzBroadcastManager;
import com.yunos.tv.blitz.global.BzAppConfig;
import com.yunos.tv.blitz.service.aidl.IBlitzServiceCallback;
import com.yunos.tv.blitz.service.aidl.IBlitzServiceInterface;
import com.yunos.tv.tvsdk.media.data.HuasuVideo;
import org.json.JSONException;
import org.json.JSONObject;

public class BaseBlitzService extends Service {
    /* access modifiers changed from: private */
    public static final String TAG = BaseBlitzService.class.getSimpleName();
    private BlitzContextWrapper mBlitzContext;
    IBlitzServiceInterface.Stub mBlitzServiceInterfaceService = new IBlitzServiceInterface.Stub() {
        public void registerCallback(IBlitzServiceCallback callback) throws RemoteException {
            Log.d(BaseBlitzService.TAG, "registerCallback...");
            IBlitzServiceCallback unused = BaseBlitzService.this.mReplyCallback = callback;
        }

        public boolean callServiceInterface(String params) throws RemoteException {
            Log.d(BaseBlitzService.TAG, "callServiceInterface, params = " + params);
            if (BaseBlitzService.this.callNativeSepcInterface(params)) {
                return true;
            }
            Log.d(BaseBlitzService.TAG, "callNativeSepcInterface failed!");
            return false;
        }
    };
    private BlitzBroadcastManager mBroadcastManager;
    /* access modifiers changed from: private */
    public IBlitzServiceCallback mReplyCallback;
    private String mServiceEntry;
    private String mServiceUri;

    /* access modifiers changed from: private */
    public native boolean callNativeSepcInterface(String str);

    private native boolean onBindService(String str);

    private native int onCreateService(String str);

    private native boolean onDestroyService(String str);

    private native boolean onStartService(String str);

    private native boolean onStopService(String str);

    private native boolean onUnbindService(String str);

    private native boolean registerJavaMethodToNative();

    public void onCreate() {
        super.onCreate();
        this.mBroadcastManager = new BlitzBroadcastManager(this);
        Log.d(TAG, "onCreate...");
    }

    public void onDestroy() {
        Log.d(TAG, "onDestroy...");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(HuasuVideo.TAG_URI, this.mServiceUri);
            jsonObject.put(BlitzServiceUtils.CCLIENT_EXTRAS, "");
            onDestroyService(jsonObject.toString());
            BzAppConfig.context.removeContext(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (this.mBroadcastManager != null) {
            this.mBroadcastManager.onDestory();
        }
        this.mBroadcastManager = null;
        super.onDestroy();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(HuasuVideo.TAG_URI, this.mServiceUri);
            jsonObject.put(BlitzServiceUtils.CCLIENT_EXTRAS, "");
            onStartService(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind...");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(HuasuVideo.TAG_URI, this.mServiceUri);
            jsonObject.put(BlitzServiceUtils.CCLIENT_EXTRAS, "");
            onBindService(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this.mBlitzServiceInterfaceService;
    }

    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind...");
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(HuasuVideo.TAG_URI, this.mServiceUri);
            jsonObject.put(BlitzServiceUtils.CCLIENT_EXTRAS, "");
            onUnbindService(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.onUnbind(intent);
    }

    public void createBlitzService(String entryFile, String serviceUri) {
        this.mServiceEntry = entryFile;
        this.mServiceUri = serviceUri;
        registerJavaMethodToNative();
        if (this.mServiceEntry.isEmpty() || this.mServiceUri.isEmpty()) {
            Log.w(TAG, "mServiceEntry is empty or mServiceUri is empty!");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(HuasuVideo.TAG_URI, this.mServiceUri);
            jsonObject.put("entry", this.mServiceEntry);
            jsonObject.put(BlitzServiceUtils.CCLIENT_EXTRAS, "");
            BzAppConfig.context.addContext(onCreateService(jsonObject.toString()), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String native2JavaMethodCall(String params, int cbId, int operType) {
        Log.d(TAG, "native2JavaMethodCall, params = " + params + ", cbId = " + cbId + ", operType = " + operType);
        return sendMessageToClient(params, operType) ? BlitzServiceUtils.CSUCCESS : "fail";
    }

    private boolean sendMessageToClient(String params, int msgType) {
        try {
            this.mReplyCallback.messageCallback(params, msgType);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public BlitzBroadcastManager getBroadcastManager() {
        return this.mBroadcastManager;
    }
}
