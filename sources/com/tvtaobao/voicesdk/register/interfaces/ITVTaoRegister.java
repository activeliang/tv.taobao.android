package com.tvtaobao.voicesdk.register.interfaces;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.tvtaobao.voicesdk.register.bo.Register;

public interface ITVTaoRegister extends IInterface {
    void onRegister(Register register) throws RemoteException;

    void unRegister(Register register) throws RemoteException;

    public static abstract class Stub extends Binder implements ITVTaoRegister {
        private static final String DESCRIPTOR = "com.tvtaobao.voicesdk.register.interfaces.ITVTaoRegister";
        static final int TRANSACTION_onRegister = 1;
        static final int TRANSACTION_unRegister = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITVTaoRegister asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITVTaoRegister)) {
                return new Proxy(obj);
            }
            return (ITVTaoRegister) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Register _arg0;
            Register _arg02;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = Register.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    onRegister(_arg02);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = Register.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    unRegister(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements ITVTaoRegister {
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

            public void onRegister(Register register) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (register != null) {
                        _data.writeInt(1);
                        register.writeToParcel(_data, 0);
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

            public void unRegister(Register register) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (register != null) {
                        _data.writeInt(1);
                        register.writeToParcel(_data, 0);
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
        }
    }
}
