package anetwork.channel.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import anetwork.channel.aidl.ParcelableInputStream;

public interface ParcelableNetworkListener extends IInterface {
    byte getListenerState() throws RemoteException;

    void onDataReceived(DefaultProgressEvent defaultProgressEvent) throws RemoteException;

    void onFinished(DefaultFinishEvent defaultFinishEvent) throws RemoteException;

    void onInputStreamGet(ParcelableInputStream parcelableInputStream) throws RemoteException;

    boolean onResponseCode(int i, ParcelableHeader parcelableHeader) throws RemoteException;

    public static abstract class Stub extends Binder implements ParcelableNetworkListener {
        private static final String DESCRIPTOR = "anetwork.channel.aidl.ParcelableNetworkListener";
        static final int TRANSACTION_getListenerState = 5;
        static final int TRANSACTION_onDataReceived = 1;
        static final int TRANSACTION_onFinished = 2;
        static final int TRANSACTION_onInputStreamGet = 4;
        static final int TRANSACTION_onResponseCode = 3;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ParcelableNetworkListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ParcelableNetworkListener)) {
                return new Proxy(obj);
            }
            return (ParcelableNetworkListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            ParcelableHeader _arg1;
            DefaultFinishEvent _arg0;
            DefaultProgressEvent _arg02;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = DefaultProgressEvent.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    onDataReceived(_arg02);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = DefaultFinishEvent.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    onFinished(_arg0);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg03 = data.readInt();
                    if (data.readInt() != 0) {
                        _arg1 = ParcelableHeader.CREATOR.createFromParcel(data);
                    } else {
                        _arg1 = null;
                    }
                    boolean _result = onResponseCode(_arg03, _arg1);
                    reply.writeNoException();
                    reply.writeInt(_result ? 1 : 0);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    onInputStreamGet(ParcelableInputStream.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    byte _result2 = getListenerState();
                    reply.writeNoException();
                    reply.writeByte(_result2);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements ParcelableNetworkListener {
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

            public void onDataReceived(DefaultProgressEvent event) throws RemoteException {
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
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onFinished(DefaultFinishEvent event) throws RemoteException {
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
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean onResponseCode(int code, ParcelableHeader header) throws RemoteException {
                boolean _result = true;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(code);
                    if (header != null) {
                        _data.writeInt(1);
                        header.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() == 0) {
                        _result = false;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onInputStreamGet(ParcelableInputStream inputStream) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(inputStream != null ? inputStream.asBinder() : null);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public byte getListenerState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readByte();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
