package com.yunos.tv.blitz.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.yunos.tv.blitz.service.aidl.IBlitzServiceCallback;
import com.yunos.tv.blitz.service.aidl.IBlitzServiceInterface;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class BlitzServiceClientItem {
    /* access modifiers changed from: private */
    public static final String TAG = BlitzServiceClientItem.class.getSimpleName();
    /* access modifiers changed from: private */
    public static int mBlitzServiceCliendID = 0;
    /* access modifiers changed from: private */
    public IBlitzServiceCallback.Stub mBlitzServiceCallback = new IBlitzServiceCallback.Stub() {
        public void messageCallback(String params, int msgType) throws RemoteException {
            Log.i(BlitzServiceClientItem.TAG, "messageCallback, params = " + params + ", msgType = " + msgType);
            if (BlitzServiceClientItem.this.mBlitzServiceClientListener == null) {
                Log.e(BlitzServiceClientItem.TAG, "mBlitzServiceClientListener = " + BlitzServiceClientItem.this.mBlitzServiceClientListener);
            } else if (msgType == 0) {
                BlitzServiceClientItem.this.mBlitzServiceClientListener.returnItemCallServiceIfaceResult(params);
            } else if (msgType == 1) {
                BlitzServiceClientItem.this.mBlitzServiceClientListener.callItemServiceListenerIface(params);
            }
        }
    };
    /* access modifiers changed from: private */
    public IBlitzServiceClientListener mBlitzServiceClientListener;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public IBlitzServiceInterface mRemoteBlitzService;
    /* access modifiers changed from: private */
    public ServiceConnection mRemoteServerConn = new ServiceConnection() {
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            Log.d(BlitzServiceClientItem.TAG, "onServiceConnected...componentName = " + componentName.getClassName());
            IBlitzServiceInterface unused = BlitzServiceClientItem.this.mRemoteBlitzService = IBlitzServiceInterface.Stub.asInterface(service);
            if (BlitzServiceClientItem.this.mRemoteBlitzService != null) {
                try {
                    BlitzServiceClientItem.this.mRemoteBlitzService.registerCallback(BlitzServiceClientItem.this.mBlitzServiceCallback);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(BlitzServiceClientItem.TAG, "onServiceDisconnected componentName = " + componentName.getClassName());
        }
    };
    /* access modifiers changed from: private */
    public String mServiceUri;

    public BlitzServiceClientItem(Context context, String serviceUri) {
        this.mContext = context;
        this.mServiceUri = serviceUri;
        mBlitzServiceCliendID++;
    }

    public void setBlitzServiceClientListener(IBlitzServiceClientListener listener) {
        this.mBlitzServiceClientListener = listener;
    }

    public void bindBlitzService(final String params) {
        new Thread(new Runnable() {
            public void run() {
                boolean result = BlitzServiceClientItem.this.mContext.bindService(new Intent(BlitzServiceClientItem.this.mServiceUri), BlitzServiceClientItem.this.mRemoteServerConn, 1);
                Log.d(BlitzServiceClientItem.TAG, "bindService " + (result ? BlitzServiceUtils.CSUCCESS : "fail") + ", serviceUri = " + BlitzServiceClientItem.this.mServiceUri);
                try {
                    JSONObject jsonObject = new JSONObject(params);
                    jsonObject.put("result", result);
                    if (BlitzServiceClientItem.this.mBlitzServiceClientListener != null) {
                        BlitzServiceClientItem.this.mBlitzServiceClientListener.setItemStartServiceResult(jsonObject.toString());
                    } else {
                        Log.e(BlitzServiceClientItem.TAG, "mBlitzServiceClientListener = " + BlitzServiceClientItem.this.mBlitzServiceClientListener);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setStartServiceResult(final String params) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(params);
                    jsonObject.put("result", true);
                    if (BlitzServiceClientItem.this.mBlitzServiceClientListener != null) {
                        BlitzServiceClientItem.this.mBlitzServiceClientListener.setItemStartServiceResult(jsonObject.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void unBindBlitzService(String params) {
        Log.i(TAG, "unBindBlitzService params = " + params);
        new Thread(new Runnable() {
            public void run() {
                try {
                    BlitzServiceClientItem.this.mContext.unbindService(BlitzServiceClientItem.this.mRemoteServerConn);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void callServiceInterface(final String params) {
        new Thread(new Runnable() {
            public void run() {
                int waitCount = 20;
                while (true) {
                    try {
                        if (BlitzServiceClientItem.this.mRemoteBlitzService != null) {
                            int i = waitCount;
                            break;
                        }
                        int waitCount2 = waitCount - 1;
                        if (waitCount <= 0) {
                            break;
                        }
                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                            waitCount = waitCount2;
                        } catch (RemoteException e) {
                            e = e;
                        } catch (InterruptedException e2) {
                            e = e2;
                            e.printStackTrace();
                            return;
                        }
                    } catch (RemoteException e3) {
                        e = e3;
                        int i2 = waitCount;
                    } catch (InterruptedException e4) {
                        e = e4;
                        int i3 = waitCount;
                        e.printStackTrace();
                        return;
                    }
                }
                String paramsTemp = params;
                if (BlitzServiceClientItem.this.mRemoteBlitzService != null) {
                    try {
                        JSONObject paramsJson = new JSONObject(paramsTemp);
                        JSONObject msgJson = paramsJson.getJSONObject(BlitzServiceUtils.CMSG_INFO);
                        msgJson.put("clientName", BlitzServiceClientItem.mBlitzServiceCliendID + "");
                        paramsJson.put(BlitzServiceUtils.CMSG_INFO, msgJson);
                        paramsTemp = paramsJson.toString();
                    } catch (JSONException e5) {
                        e5.printStackTrace();
                    }
                    BlitzServiceClientItem.this.mRemoteBlitzService.callServiceInterface(paramsTemp);
                    return;
                }
                Log.e(BlitzServiceClientItem.TAG, "mRemoteBlitzService is null!");
                return;
                e.printStackTrace();
            }
        }).start();
    }
}
