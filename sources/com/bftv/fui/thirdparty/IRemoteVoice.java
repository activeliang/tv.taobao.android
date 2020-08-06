package com.bftv.fui.thirdparty;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRemoteVoice extends IInterface {
    void sendMessage(VoiceFeedback voiceFeedback) throws RemoteException;

    public static abstract class Stub extends Binder implements IRemoteVoice {
        private static final String DESCRIPTOR = "com.bftv.fui.thirdparty.IRemoteVoice";
        static final int TRANSACTION_sendMessage = 1;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRemoteVoice asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRemoteVoice)) {
                return new Proxy(obj);
            }
            return (IRemoteVoice) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            VoiceFeedback _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = VoiceFeedback.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    sendMessage(_arg0);
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IRemoteVoice {
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

            public void sendMessage(VoiceFeedback voiceFeedback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (voiceFeedback != null) {
                        _data.writeInt(1);
                        voiceFeedback.writeToParcel(_data, 0);
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
