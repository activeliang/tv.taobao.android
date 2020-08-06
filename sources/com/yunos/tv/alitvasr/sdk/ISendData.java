package com.yunos.tv.alitvasr.sdk;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISendData extends IInterface {
    void onBroadcastPackageToClient(String str, byte[] bArr) throws RemoteException;

    void onSendPackageToClient(String str, byte[] bArr, int i) throws RemoteException;

    void sendKeyEvent(VKeyEvent vKeyEvent) throws RemoteException;

    public static abstract class Stub extends Binder implements ISendData {
        private static final String DESCRIPTOR = "com.yunos.tv.alitvasr.sdk.ISendData";
        static final int TRANSACTION_onBroadcastPackageToClient = 2;
        static final int TRANSACTION_onSendPackageToClient = 1;
        static final int TRANSACTION_sendKeyEvent = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISendData asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISendData)) {
                return new Proxy(obj);
            }
            return (ISendData) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            VKeyEvent _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    onSendPackageToClient(data.readString(), data.createByteArray(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    onBroadcastPackageToClient(data.readString(), data.createByteArray());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = VKeyEvent.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    sendKeyEvent(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements ISendData {
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

            public void onSendPackageToClient(String commandType, byte[] data, int cid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(commandType);
                    _data.writeByteArray(data);
                    _data.writeInt(cid);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onBroadcastPackageToClient(String commandType, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(commandType);
                    _data.writeByteArray(data);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendKeyEvent(VKeyEvent event) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (event != null) {
                        _data.writeInt(1);
                        event.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
