package com.bftv.fui.thirdparty;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.bftv.fui.thirdparty.IRemoteFeedback;

public interface IUserStatusNotice extends IInterface {
    void onAsr(String str, int i, int i2, IRemoteFeedback iRemoteFeedback) throws RemoteException;

    void onInterception(InterceptionData interceptionData) throws RemoteException;

    void onRecyclingNotice(RecyclingData recyclingData) throws RemoteException;

    void onShow(boolean z) throws RemoteException;

    public static abstract class Stub extends Binder implements IUserStatusNotice {
        private static final String DESCRIPTOR = "com.bftv.fui.thirdparty.IUserStatusNotice";
        static final int TRANSACTION_onAsr = 3;
        static final int TRANSACTION_onInterception = 1;
        static final int TRANSACTION_onRecyclingNotice = 2;
        static final int TRANSACTION_onShow = 4;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IUserStatusNotice asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IUserStatusNotice)) {
                return new Proxy(obj);
            }
            return (IUserStatusNotice) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            RecyclingData _arg0;
            InterceptionData _arg02;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = InterceptionData.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    onInterception(_arg02);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = RecyclingData.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    onRecyclingNotice(_arg0);
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    onAsr(data.readString(), data.readInt(), data.readInt(), IRemoteFeedback.Stub.asInterface(data.readStrongBinder()));
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    onShow(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IUserStatusNotice {
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

            public void onInterception(InterceptionData interceptionData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (interceptionData != null) {
                        _data.writeInt(1);
                        interceptionData.writeToParcel(_data, 0);
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

            public void onRecyclingNotice(RecyclingData recyclingData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (recyclingData != null) {
                        _data.writeInt(1);
                        recyclingData.writeToParcel(_data, 0);
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

            public void onAsr(String result, int age, int sex, IRemoteFeedback feedBack) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(result);
                    _data.writeInt(age);
                    _data.writeInt(sex);
                    if (feedBack != null) {
                        iBinder = feedBack.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    this.mRemote.transact(3, _data, (Parcel) null, 1);
                } finally {
                    _data.recycle();
                }
            }

            public void onShow(boolean isFar) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (isFar) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
