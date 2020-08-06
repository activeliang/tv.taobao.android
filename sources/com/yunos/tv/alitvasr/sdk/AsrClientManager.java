package com.yunos.tv.alitvasr.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import com.yunos.tv.alitvasr.IAliTVASRTTSCallback;
import com.yunos.tv.alitvasr.sdk.IClient;
import com.yunos.tv.alitvasr.sdk.IInput;
import com.yunos.tv.alitvasrsdk.CommonData;

public class AsrClientManager extends AbstractClientManager<IClient> {
    private static AsrClientManager gInstance;

    public static AsrClientManager getInstance(Context context) {
        if (gInstance == null) {
            synchronized (AsrClientManager.class) {
                if (gInstance == null) {
                    gInstance = new AsrClientManager(context);
                }
            }
        }
        return gInstance;
    }

    private AsrClientManager(Context context) {
        super(context, CommonData.ASR_CLIENT_ACTION, CommonData.ASR_UI_PACKAGE_NAME);
    }

    /* access modifiers changed from: protected */
    public IClient asInterface(IBinder service) {
        return IClient.Stub.asInterface(service);
    }

    /* access modifiers changed from: protected */
    public IInterface getBinder(IClient iService, int type) throws Throwable {
        IBinder binder = iService.getIBinder(type);
        if (binder == null) {
            return null;
        }
        switch (type) {
            case 2:
                return IInput.Stub.asInterface(binder);
            default:
                return null;
        }
    }

    private void onClientStateChanged(int type, int cid, int state, Bundle extra) {
        IInput iReceiveData = (IInput) getInterface(2);
        if (iReceiveData != null) {
            try {
                iReceiveData.onDeviceStateChanged(type, state, cid, extra);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private Bundle onDeviceReceive(int type, int cid, Bundle extra) {
        IInput iReceiveData = (IInput) getInterface(2);
        if (!(iReceiveData == null || extra == null)) {
            try {
                return iReceiveData.onDeviceReceive(type, cid, extra);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean playTTS(String tts) {
        IClient iClient = (IClient) getService();
        if (iClient != null) {
            try {
                return iClient.playTTSWithCallback(tts, (IAliTVASRTTSCallback) null);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void stopTTS() {
        IClient iClient = (IClient) getService();
        if (iClient != null) {
            try {
                iClient.stopTTS();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public boolean playTTSWithCallback(String tts, IAliTVASRTTSCallback callback) {
        IClient iClient = (IClient) getService();
        if (iClient != null) {
            try {
                return iClient.playTTSWithCallback(tts, callback);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Bundle updateAppScene(Bundle appSceneBundle) {
        IClient iClient = (IClient) getService();
        if (iClient != null) {
            try {
                return iClient.updateAppScene(appSceneBundle);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Bundle clientToAsr(int id, Bundle bundle) {
        IClient iClient = (IClient) getService();
        if (iClient != null) {
            try {
                return iClient.clientToAsr(id, bundle);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Bundle onFarDeviceData(Bundle data) {
        return onDeviceReceive(4, 0, data);
    }

    public void onAssistantStateChanged(int cid, int state, int clientSize) {
        if (clientSize <= 0) {
            state = 0;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("size", clientSize);
        onClientStateChanged(1, cid, state, bundle);
    }

    public Bundle onAssistantReceive(byte[] data, int cid) {
        if (data == null || data.length <= 0) {
            return null;
        }
        Bundle extra = new Bundle();
        extra.putByteArray("data", data);
        return onDeviceReceive(1, cid, extra);
    }

    public Bundle onAssistantReceive(byte[] data) {
        return onAssistantReceive(data, Integer.MAX_VALUE);
    }

    public void onRemoteControlStateChanged(int state) {
        onClientStateChanged(0, 0, state, (Bundle) null);
    }

    public void onRemoteControlReceive(boolean isStarted) {
        Bundle extra = new Bundle();
        extra.putInt("status", isStarted ? 1 : 2);
        onDeviceReceive(0, 0, extra);
    }

    public static void registerFnKey(Context context, FnKeyInfo info) {
        Intent intent = FnKeyInfo.getIntent(info);
        if (intent != null && context != null) {
            intent.setPackage(CommonData.ASR_UI_PACKAGE_NAME);
            intent.setAction(CommonData.ASR_SERVER_ACTION);
            try {
                context.startService(intent);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public static void unregisterFnKey(Context context, int keyCode) {
        FnKeyInfo info = new FnKeyInfo(keyCode);
        info.setInterested(false);
        Intent intent = FnKeyInfo.getIntent(info);
        if (context != null && intent != null) {
            intent.setPackage(CommonData.ASR_UI_PACKAGE_NAME);
            intent.setAction(CommonData.ASR_SERVER_ACTION);
            try {
                context.startService(intent);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public boolean onDeviceKeyEvent(VKeyEvent keyEvent) {
        IInput iReceiveData = (IInput) getInterface(2);
        if (iReceiveData != null) {
            try {
                return iReceiveData.onDeviceKeyEvent(keyEvent);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
