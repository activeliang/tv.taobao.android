package com.yunos.tv.alitvasr.sdk;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import com.yunos.tv.alitvasr.IAliTVASR;
import com.yunos.tv.alitvasr.IAliTVASRCallback;
import com.yunos.tv.alitvasr.sdk.ISendData;
import com.yunos.tv.alitvasr.sdk.IServer;
import com.yunos.tv.alitvasrsdk.CommonData;

public class AsrServerManager extends AbstractClientManager<IServer> {
    private static AsrServerManager gInstance;

    private AsrServerManager(Context context) {
        super(context, CommonData.ASR_SERVER_INNER_ACTION, CommonData.ASR_UI_PACKAGE_NAME);
    }

    /* access modifiers changed from: protected */
    public IServer asInterface(IBinder service) {
        return IServer.Stub.asInterface(service);
    }

    public static AsrServerManager getInstance(Context context) {
        if (gInstance == null) {
            synchronized (AsrServerManager.class) {
                if (gInstance == null) {
                    gInstance = new AsrServerManager(context);
                }
            }
        }
        return gInstance;
    }

    public ClientInfo getClientInfo(String packageName) {
        IServer iServer = (IServer) getService();
        if (iServer != null) {
            try {
                return iServer.getClientInfo(packageName);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Bundle oprClientProp(String packageName, int opr, String prop) {
        IServer iServer = (IServer) getService();
        if (iServer != null) {
            try {
                return iServer.oprClientProp(packageName, opr, prop);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public IAliTVASRCallback getClientCallback(String packageName) {
        IServer iServer = (IServer) getService();
        if (iServer != null) {
            try {
                return iServer.getClientCallback(packageName);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public IInterface getBinder(IServer iServer, int type) throws Throwable {
        IBinder binder = iServer.getIBinder(type);
        if (binder == null) {
            return null;
        }
        switch (type) {
            case 0:
                return IAliTVASR.Stub.asInterface(binder);
            case 1:
                return ISendData.Stub.asInterface(binder);
            default:
                return null;
        }
    }

    public void registerCallback(String packageName, IAliTVASRCallback callback, boolean showUI) {
        IAliTVASR iAliTVASR = (IAliTVASR) getInterface(0);
        if (iAliTVASR != null) {
            try {
                iAliTVASR.registerCallback(packageName, callback, showUI);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void unregisterCallback(String packageName) {
        IAliTVASR iAliTVASR = (IAliTVASR) getInterface(0);
        if (iAliTVASR != null) {
            try {
                iAliTVASR.unregisterCallback(packageName);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void setAliTVASREnable(String packageName, boolean enable) {
        IAliTVASR iAliTVASR = (IAliTVASR) getInterface(0);
        if (iAliTVASR != null) {
            try {
                iAliTVASR.setAliTVASREnable(packageName, enable);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void setResultMode(String packageName, int mode) {
        IAliTVASR iAliTVASR = (IAliTVASR) getInterface(0);
        if (iAliTVASR != null) {
            try {
                iAliTVASR.setResultMode(packageName, mode);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void setASRMode(String packageName, int mode) {
        IAliTVASR iAliTVASR = (IAliTVASR) getInterface(0);
        if (iAliTVASR != null) {
            try {
                iAliTVASR.setASRMode(packageName, mode);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void showASRUI(String packageName, boolean showUI) {
        IAliTVASR iAliTVASR = (IAliTVASR) getInterface(0);
        if (iAliTVASR != null) {
            try {
                iAliTVASR.showASRUI(packageName, showUI);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void sendPackageToClient(String packageName, String data, int cid) {
        try {
            ISendData iSendData = (ISendData) getInterface(1);
            byte[] sendData = data.getBytes();
            if (iSendData != null) {
                iSendData.onSendPackageToClient(packageName, sendData, cid);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void broadcastPackageToClient(String packageName, String data) {
        try {
            ISendData iSendData = (ISendData) getInterface(1);
            byte[] sendData = data.getBytes();
            if (iSendData != null) {
                iSendData.onBroadcastPackageToClient(packageName, sendData);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void sendKeyEvent(VKeyEvent event) {
    }
}
