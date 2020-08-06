package com.tvtao.sdk.voice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.tvtao.sdk.voice.ITVTaoCallBack;

public interface ITVTaoInterface extends IInterface {
    void nlpRequest(String str, String str2, String str3, ITVTaoCallBack iTVTaoCallBack) throws RemoteException;

    public static abstract class Stub extends Binder implements ITVTaoInterface {
        private static final String DESCRIPTOR = "com.tvtao.sdk.voice.ITVTaoInterface";
        static final int TRANSACTION_nlpRequest = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITVTaoInterface asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITVTaoInterface)) {
                return new Proxy(obj);
            }
            return (ITVTaoInterface) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    nlpRequest(data.readString(), data.readString(), data.readString(), ITVTaoCallBack.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements ITVTaoInterface {
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

            public void nlpRequest(String asr, String searchConfig, String json, ITVTaoCallBack callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(asr);
                    _data.writeString(searchConfig);
                    _data.writeString(json);
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
