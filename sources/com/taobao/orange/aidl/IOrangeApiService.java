package com.taobao.orange.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.taobao.orange.OConfig;
import com.taobao.orange.aidl.ParcelableCandidateCompare;
import com.taobao.orange.aidl.ParcelableConfigListener;
import java.util.Map;

public interface IOrangeApiService extends IInterface {
    void addCandidate(String str, String str2, ParcelableCandidateCompare parcelableCandidateCompare) throws RemoteException;

    void addFails(String[] strArr) throws RemoteException;

    void forceCheckUpdate() throws RemoteException;

    Map getConfigs(String str) throws RemoteException;

    void init(OConfig oConfig) throws RemoteException;

    void registerListener(String str, ParcelableConfigListener parcelableConfigListener, boolean z) throws RemoteException;

    void setUserId(String str) throws RemoteException;

    void unregisterListener(String str, ParcelableConfigListener parcelableConfigListener) throws RemoteException;

    void unregisterListeners(String str) throws RemoteException;

    public static abstract class Stub extends Binder implements IOrangeApiService {
        private static final String DESCRIPTOR = "com.taobao.orange.aidl.IOrangeApiService";
        static final int TRANSACTION_addCandidate = 9;
        static final int TRANSACTION_addFails = 7;
        static final int TRANSACTION_forceCheckUpdate = 6;
        static final int TRANSACTION_getConfigs = 2;
        static final int TRANSACTION_init = 1;
        static final int TRANSACTION_registerListener = 3;
        static final int TRANSACTION_setUserId = 8;
        static final int TRANSACTION_unregisterListener = 4;
        static final int TRANSACTION_unregisterListeners = 5;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IOrangeApiService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IOrangeApiService)) {
                return new Proxy(obj);
            }
            return (IOrangeApiService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            OConfig _arg0;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = OConfig.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    init(_arg0);
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    Map _result = getConfigs(data.readString());
                    reply.writeNoException();
                    reply.writeMap(_result);
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    registerListener(data.readString(), ParcelableConfigListener.Stub.asInterface(data.readStrongBinder()), data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterListener(data.readString(), ParcelableConfigListener.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    unregisterListeners(data.readString());
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    forceCheckUpdate();
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    addFails(data.createStringArray());
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    setUserId(data.readString());
                    reply.writeNoException();
                    return true;
                case 9:
                    data.enforceInterface(DESCRIPTOR);
                    addCandidate(data.readString(), data.readString(), ParcelableCandidateCompare.Stub.asInterface(data.readStrongBinder()));
                    reply.writeNoException();
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IOrangeApiService {
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

            public void init(OConfig config) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
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

            public Map getConfigs(String groupName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(groupName);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readHashMap(getClass().getClassLoader());
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerListener(String groupname, ParcelableConfigListener listener, boolean append) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(groupname);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (append) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterListener(String groupname, ParcelableConfigListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(groupname);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterListeners(String groupname) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(groupname);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forceCheckUpdate() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addFails(String[] groupnames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(groupnames);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUserId(String userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(userId);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addCandidate(String key, String clientVal, ParcelableCandidateCompare compare) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(clientVal);
                    _data.writeStrongBinder(compare != null ? compare.asBinder() : null);
                    this.mRemote.transact(9, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
