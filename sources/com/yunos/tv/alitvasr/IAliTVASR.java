package com.yunos.tv.alitvasr;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.yunos.tv.alitvasr.IAliTVASRCallback;
import com.yunos.tv.alitvasr.IAliTVASRTTSCallback;

public interface IAliTVASR extends IInterface {
    Bundle clientToAsr(int i, Bundle bundle) throws RemoteException;

    Bundle onFarDeviceData(Bundle bundle) throws RemoteException;

    boolean playTTS(String str) throws RemoteException;

    boolean playTTSWithCallback(String str, IAliTVASRTTSCallback iAliTVASRTTSCallback) throws RemoteException;

    void registerCallback(String str, IAliTVASRCallback iAliTVASRCallback, boolean z) throws RemoteException;

    void setASRMode(String str, int i) throws RemoteException;

    void setAliTVASREnable(String str, boolean z) throws RemoteException;

    void setResultMode(String str, int i) throws RemoteException;

    void showASRUI(String str, boolean z) throws RemoteException;

    void stopTTS() throws RemoteException;

    void unregisterCallback(String str) throws RemoteException;

    Bundle updateAppScene(Bundle bundle) throws RemoteException;

    public static abstract class Stub extends Binder implements IAliTVASR {
        private static final String DESCRIPTOR = "com.yunos.tv.alitvasr.IAliTVASR";
        static final int TRANSACTION_clientToAsr = 12;
        static final int TRANSACTION_onFarDeviceData = 11;
        static final int TRANSACTION_playTTS = 4;
        static final int TRANSACTION_playTTSWithCallback = 9;
        static final int TRANSACTION_registerCallback = 1;
        static final int TRANSACTION_setASRMode = 7;
        static final int TRANSACTION_setAliTVASREnable = 3;
        static final int TRANSACTION_setResultMode = 6;
        static final int TRANSACTION_showASRUI = 8;
        static final int TRANSACTION_stopTTS = 5;
        static final int TRANSACTION_unregisterCallback = 2;
        static final int TRANSACTION_updateAppScene = 10;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAliTVASR asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAliTVASR)) {
                return new Proxy(obj);
            }
            return (IAliTVASR) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Bundle _arg1;
            Bundle _arg0;
            Bundle _arg02;
            boolean _arg12;
            boolean _arg13;
            boolean _arg2 = false;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    IAliTVASRCallback _arg14 = IAliTVASRCallback.Stub.asInterface(data.readStrongBinder());
                    if (data.readInt() != 0) {
                        _arg2 = true;
                    }
                    registerCallback(_arg03, _arg14, _arg2);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterCallback(data.readString());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    if (data.readInt() != 0) {
                        _arg13 = true;
                    } else {
                        _arg13 = false;
                    }
                    setAliTVASREnable(_arg04, _arg13);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result = playTTS(data.readString());
                    reply.writeNoException();
                    if (_result) {
                        _arg2 = true;
                    }
                    reply.writeInt(_arg2 ? 1 : 0);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    stopTTS();
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    setResultMode(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    setASRMode(data.readString(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    if (data.readInt() != 0) {
                        _arg12 = true;
                    } else {
                        _arg12 = false;
                    }
                    showASRUI(_arg05, _arg12);
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result2 = playTTSWithCallback(data.readString(), IAliTVASRTTSCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    if (_result2) {
                        _arg2 = true;
                    }
                    reply.writeInt(_arg2 ? 1 : 0);
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    Bundle _result3 = updateAppScene(_arg02);
                    reply.writeNoException();
                    if (_result3 != null) {
                        reply.writeInt(1);
                        _result3.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    Bundle _result4 = onFarDeviceData(_arg0);
                    reply.writeNoException();
                    if (_result4 != null) {
                        reply.writeInt(1);
                        _result4.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg06 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    Bundle _result5 = clientToAsr(_arg06, _arg1);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IAliTVASR {
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void registerCallback(String packageName, IAliTVASRCallback callback, boolean showUI) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (!showUI) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterCallback(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAliTVASREnable(String packageName, boolean enable) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (enable) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean playTTS(String tts) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(tts);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopTTS() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setResultMode(String packageName, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(mode);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setASRMode(String packageName, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(mode);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showASRUI(String packageName, boolean showUI) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (showUI) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean playTTSWithCallback(String tts, IAliTVASRTTSCallback callback) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(tts);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle updateAppScene(Bundle appSceneBundle) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appSceneBundle != null) {
                        _data.writeInt(1);
                        appSceneBundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle onFarDeviceData(Bundle data) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle clientToAsr(int id, Bundle bundle) throws RemoteException {
                Bundle _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(id);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
