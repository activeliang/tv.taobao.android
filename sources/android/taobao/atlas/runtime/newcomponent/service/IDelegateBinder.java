package android.taobao.atlas.runtime.newcomponent.service;

import android.app.IServiceConnection;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDelegateBinder extends IInterface {
    IBinder bindService(Intent intent, IBinder iBinder, IServiceConnection iServiceConnection) throws RemoteException;

    Intent handleActivityStack(Intent intent, ActivityInfo activityInfo) throws RemoteException;

    void handleReceiver(Intent intent, ActivityInfo activityInfo) throws RemoteException;

    IBinder startService(Intent intent, ServiceInfo serviceInfo) throws RemoteException;

    int stopService(Intent intent) throws RemoteException;

    boolean unbindService(IServiceConnection iServiceConnection) throws RemoteException;

    public static abstract class Stub extends Binder implements IDelegateBinder {
        private static final String DESCRIPTOR = "android.taobao.atlas.runtime.newcomponent.service.IDelegateBinder";
        static final int TRANSACTION_bindService = 2;
        static final int TRANSACTION_handleActivityStack = 5;
        static final int TRANSACTION_handleReceiver = 6;
        static final int TRANSACTION_startService = 1;
        static final int TRANSACTION_stopService = 4;
        static final int TRANSACTION_unbindService = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDelegateBinder asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDelegateBinder)) {
                return new Proxy(obj);
            }
            return (IDelegateBinder) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Intent _arg0;
            ActivityInfo _arg1;
            Intent _arg02;
            ActivityInfo _arg12;
            Intent _arg03;
            Intent _arg04;
            Intent _arg05;
            ServiceInfo _arg13;
            int i = 0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg05 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg05 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg13 = (ServiceInfo) ServiceInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg13 = null;
                    }
                    IBinder _result = startService(_arg05, _arg13);
                    reply.writeNoException();
                    reply.writeStrongBinder(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg04 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg04 = null;
                    }
                    IBinder _result2 = bindService(_arg04, data.readStrongBinder(), IServiceConnection.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    reply.writeStrongBinder(_result2);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result3 = unbindService(IServiceConnection.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    if (_result3) {
                        i = 1;
                    }
                    reply.writeInt(i);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg03 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg03 = null;
                    }
                    int _result4 = stopService(_arg03);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg12 = (ActivityInfo) ActivityInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg12 = null;
                    }
                    Intent _result5 = handleActivityStack(_arg02, _arg12);
                    reply.writeNoException();
                    if (_result5 != null) {
                        reply.writeInt(1);
                        _result5.writeToParcel(reply, 1);
                        return true;
                    }
                    reply.writeInt(0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = (Intent) Intent.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg1 = (ActivityInfo) ActivityInfo.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    handleReceiver(_arg0, _arg1);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IDelegateBinder {
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

            public IBinder startService(Intent serviceIntent, ServiceInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (serviceIntent != null) {
                        _data.writeInt(1);
                        serviceIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readStrongBinder();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IBinder bindService(Intent serviceIntent, IBinder activityToken, IServiceConnection conn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (serviceIntent != null) {
                        _data.writeInt(1);
                        serviceIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(activityToken);
                    _data.writeStrongBinder(conn != null ? conn.asBinder() : null);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readStrongBinder();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean unbindService(IServiceConnection conn) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(conn != null ? conn.asBinder() : null);
                    this.mRemote.transact(3, _data, _reply, 0);
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

            public int stopService(Intent serviceIntent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (serviceIntent != null) {
                        _data.writeInt(1);
                        serviceIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Intent handleActivityStack(Intent intent, ActivityInfo info) throws RemoteException {
                Intent _result;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = (Intent) Intent.CREATOR.createFromParcel(_reply);
                    } else {
                        _result = null;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void handleReceiver(Intent intent, ActivityInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
