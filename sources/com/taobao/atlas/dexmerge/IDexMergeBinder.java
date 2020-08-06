package com.taobao.atlas.dexmerge;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.taobao.atlas.dexmerge.IDexMergeCallback;
import java.util.List;

public interface IDexMergeBinder extends IInterface {
    void dexMerge(String str, List<MergeObject> list, boolean z) throws RemoteException;

    void registerListener(IDexMergeCallback iDexMergeCallback) throws RemoteException;

    public static abstract class Stub extends Binder implements IDexMergeBinder {
        private static final String DESCRIPTOR = "com.taobao.atlas.dexmerge.IDexMergeBinder";
        static final int TRANSACTION_dexMerge = 1;
        static final int TRANSACTION_registerListener = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDexMergeBinder asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDexMergeBinder)) {
                return new Proxy(obj);
            }
            return (IDexMergeBinder) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    dexMerge(data.readString(), data.createTypedArrayList(MergeObject.CREATOR), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    registerListener(IDexMergeCallback.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IDexMergeBinder {
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

            public void dexMerge(String patchFilePath, List<MergeObject> toMergeList, boolean diffBundleDex) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(patchFilePath);
                    _data.writeTypedList(toMergeList);
                    if (!diffBundleDex) {
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

            public void registerListener(IDexMergeCallback listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
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
