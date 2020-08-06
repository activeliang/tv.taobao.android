package com.bftv.fui.thirdparty;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.bftv.fui.thirdparty.IRemoteVoice;

public interface IAsynRomoteVoice extends IInterface {
    void asynMessage(IRemoteVoice iRemoteVoice, String str, String str2) throws RemoteException;

    public static abstract class Stub extends Binder implements IAsynRomoteVoice {
        private static final String DESCRIPTOR = "com.bftv.fui.thirdparty.IAsynRomoteVoice";
        static final int TRANSACTION_asynMessage = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAsynRomoteVoice asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAsynRomoteVoice)) {
                return new Proxy(obj);
            }
            return (IAsynRomoteVoice) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    asynMessage(IRemoteVoice.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readString());
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IAsynRomoteVoice {
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

            public void asynMessage(IRemoteVoice callBack, String userTxt, String nlpJson) throws RemoteException {
                IBinder iBinder = null;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (callBack != null) {
                        iBinder = callBack.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeString(userTxt);
                    _data.writeString(nlpJson);
                    this.mRemote.transact(1, _data, (Parcel) null, 1);
                } finally {
                    _data.recycle();
                }
            }
        }
    }
}
