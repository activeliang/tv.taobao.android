package anetwork.channel.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ParcelableInputStream extends IInterface {
    int available() throws RemoteException;

    void close() throws RemoteException;

    int length() throws RemoteException;

    int read(byte[] bArr) throws RemoteException;

    int readByte() throws RemoteException;

    int readBytes(byte[] bArr, int i, int i2) throws RemoteException;

    long skip(int i) throws RemoteException;

    public static abstract class Stub extends Binder implements ParcelableInputStream {
        private static final String DESCRIPTOR = "anetwork.channel.aidl.ParcelableInputStream";
        static final int TRANSACTION_available = 1;
        static final int TRANSACTION_close = 2;
        static final int TRANSACTION_length = 7;
        static final int TRANSACTION_read = 5;
        static final int TRANSACTION_readByte = 3;
        static final int TRANSACTION_readBytes = 4;
        static final int TRANSACTION_skip = 6;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ParcelableInputStream asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ParcelableInputStream)) {
                return new Proxy(obj);
            }
            return (ParcelableInputStream) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            byte[] _arg0;
            byte[] _arg02;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    int _result = available();
                    reply.writeNoException();
                    reply.writeInt(_result);
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    close();
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    int _result2 = readByte();
                    reply.writeNoException();
                    reply.writeInt(_result2);
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0_length = data.readInt();
                    if (_arg0_length < 0) {
                        _arg02 = null;
                    } else {
                        _arg02 = new byte[_arg0_length];
                    }
                    int _result3 = readBytes(_arg02, data.readInt(), data.readInt());
                    reply.writeNoException();
                    reply.writeInt(_result3);
                    reply.writeByteArray(_arg02);
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    int _arg0_length2 = data.readInt();
                    if (_arg0_length2 < 0) {
                        _arg0 = null;
                    } else {
                        _arg0 = new byte[_arg0_length2];
                    }
                    int _result4 = read(_arg0);
                    reply.writeNoException();
                    reply.writeInt(_result4);
                    reply.writeByteArray(_arg0);
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    long _result5 = skip(data.readInt());
                    reply.writeNoException();
                    reply.writeLong(_result5);
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    int _result6 = length();
                    reply.writeNoException();
                    reply.writeInt(_result6);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements ParcelableInputStream {
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

            public int available() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void close() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int readByte() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int readBytes(byte[] b, int offest, int len) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (b == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(b.length);
                    }
                    _data.writeInt(offest);
                    _data.writeInt(len);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(b);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int read(byte[] b) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (b == null) {
                        _data.writeInt(-1);
                    } else {
                        _data.writeInt(b.length);
                    }
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                    int _result = _reply.readInt();
                    _reply.readByteArray(b);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long skip(int n) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(n);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readLong();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int length() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readInt();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
