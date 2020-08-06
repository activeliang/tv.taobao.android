package com.taobao.atlas.dexmerge;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDexMergeCallback extends IInterface {
    void onMergeAllFinish(boolean z, String str) throws RemoteException;

    void onMergeFinish(String str, boolean z, String str2) throws RemoteException;

    public static abstract class Stub extends Binder implements IDexMergeCallback {
        private static final String DESCRIPTOR = "com.taobao.atlas.dexmerge.IDexMergeCallback";
        static final int TRANSACTION_onMergeAllFinish = 2;
        static final int TRANSACTION_onMergeFinish = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDexMergeCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDexMergeCallback)) {
                return new Proxy(obj);
            }
            return (IDexMergeCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            boolean _arg0;
            boolean _arg1 = false;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg02 = data.readString();
                    if (data.readInt() != 0) {
                        _arg1 = true;
                    }
                    onMergeFinish(_arg02, _arg1, data.readString());
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = true;
                    } else {
                        _arg0 = false;
                    }
                    onMergeAllFinish(_arg0, data.readString());
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IDexMergeCallback {
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

            public void onMergeFinish(String filePath, boolean result, String reason) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(filePath);
                    if (!result) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeString(reason);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onMergeAllFinish(boolean result, String reason) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (result) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    _data.writeString(reason);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
