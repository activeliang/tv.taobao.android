package com.tvtaobao.voicesdk;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.tvtaobao.voicesdk.ITVTaoCallBack;
import com.tvtaobao.voicesdk.bean.DomainResultVo;

public interface ITVTaoIntercept extends IInterface {
    void onVoiceAction(DomainResultVo domainResultVo, ITVTaoCallBack iTVTaoCallBack) throws RemoteException;

    public static abstract class Stub extends Binder implements ITVTaoIntercept {
        private static final String DESCRIPTOR = "com.tvtaobao.voicesdk.ITVTaoIntercept";
        static final int TRANSACTION_onVoiceAction = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITVTaoIntercept asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITVTaoIntercept)) {
                return new Proxy(obj);
            }
            return (ITVTaoIntercept) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            DomainResultVo _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = DomainResultVo.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    onVoiceAction(_arg0, ITVTaoCallBack.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements ITVTaoIntercept {
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

            public void onVoiceAction(DomainResultVo domainResultVo, ITVTaoCallBack callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (domainResultVo != null) {
                        _data.writeInt(1);
                        domainResultVo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
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
