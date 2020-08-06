package com.tvtaobao.voicesdk;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.tvtaobao.voicesdk.bean.PageReturn;

public interface ITVTaoCallBack extends IInterface {
    void onTVTaoIntercept(PageReturn pageReturn) throws RemoteException;

    public static abstract class Stub extends Binder implements ITVTaoCallBack {
        private static final String DESCRIPTOR = "com.tvtaobao.voicesdk.ITVTaoCallBack";
        static final int TRANSACTION_onTVTaoIntercept = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITVTaoCallBack asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITVTaoCallBack)) {
                return new Proxy(obj);
            }
            return (ITVTaoCallBack) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            PageReturn _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = PageReturn.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    onTVTaoIntercept(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements ITVTaoCallBack {
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

            public void onTVTaoIntercept(PageReturn pageReturn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pageReturn != null) {
                        _data.writeInt(1);
                        pageReturn.writeToParcel(_data, 0);
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
        }
    }
}
