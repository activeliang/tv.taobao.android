package com.alibaba.analytics;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.alibaba.mtl.appmonitor.Transaction;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import java.util.Map;

public interface IAnalytics extends IInterface {
    boolean alarm_checkSampled(String str, String str2) throws RemoteException;

    void alarm_commitFail1(String str, String str2, String str3, String str4) throws RemoteException;

    void alarm_commitFail2(String str, String str2, String str3, String str4, String str5) throws RemoteException;

    void alarm_commitSuccess1(String str, String str2) throws RemoteException;

    void alarm_commitSuccess2(String str, String str2, String str3) throws RemoteException;

    void alarm_setSampling(int i) throws RemoteException;

    void alarm_setStatisticsInterval(int i) throws RemoteException;

    boolean counter_checkSampled(String str, String str2) throws RemoteException;

    void counter_commit1(String str, String str2, double d) throws RemoteException;

    void counter_commit2(String str, String str2, String str3, double d) throws RemoteException;

    void counter_setSampling(int i) throws RemoteException;

    void counter_setStatisticsInterval(int i) throws RemoteException;

    void destroy() throws RemoteException;

    void dispatchLocalHits() throws RemoteException;

    void enableLog(boolean z) throws RemoteException;

    String getValue(String str) throws RemoteException;

    void init() throws RemoteException;

    void initUT() throws RemoteException;

    boolean offlinecounter_checkSampled(String str, String str2) throws RemoteException;

    void offlinecounter_commit(String str, String str2, double d) throws RemoteException;

    void offlinecounter_setSampling(int i) throws RemoteException;

    void offlinecounter_setStatisticsInterval(int i) throws RemoteException;

    void register1(String str, String str2, MeasureSet measureSet) throws RemoteException;

    void register2(String str, String str2, MeasureSet measureSet, boolean z) throws RemoteException;

    void register3(String str, String str2, MeasureSet measureSet, DimensionSet dimensionSet) throws RemoteException;

    void register4(String str, String str2, MeasureSet measureSet, DimensionSet dimensionSet, boolean z) throws RemoteException;

    void saveCacheDataToLocal() throws RemoteException;

    String selfCheck(String str) throws RemoteException;

    void setAppVersion(String str) throws RemoteException;

    void setChannel(String str) throws RemoteException;

    void setRequestAuthInfo(boolean z, boolean z2, String str, String str2) throws RemoteException;

    void setSampling(int i) throws RemoteException;

    void setSessionProperties(Map map) throws RemoteException;

    void setStatisticsInterval1(int i) throws RemoteException;

    void setStatisticsInterval2(int i, int i2) throws RemoteException;

    void stat_begin(String str, String str2, String str3) throws RemoteException;

    boolean stat_checkSampled(String str, String str2) throws RemoteException;

    void stat_commit1(String str, String str2, double d) throws RemoteException;

    void stat_commit2(String str, String str2, DimensionValueSet dimensionValueSet, double d) throws RemoteException;

    void stat_commit3(String str, String str2, DimensionValueSet dimensionValueSet, MeasureValueSet measureValueSet) throws RemoteException;

    void stat_end(String str, String str2, String str3) throws RemoteException;

    void stat_setSampling(int i) throws RemoteException;

    void stat_setStatisticsInterval(int i) throws RemoteException;

    void transaction_begin(Transaction transaction, String str) throws RemoteException;

    void transaction_end(Transaction transaction, String str) throws RemoteException;

    void transferLog(Map map) throws RemoteException;

    void triggerUpload() throws RemoteException;

    void turnOffRealTimeDebug() throws RemoteException;

    void turnOnDebug() throws RemoteException;

    void turnOnRealTimeDebug(Map map) throws RemoteException;

    void updateMeasure(String str, String str2, String str3, double d, double d2, double d3) throws RemoteException;

    void updateSessionProperties(Map map) throws RemoteException;

    void updateUserAccount(String str, String str2, String str3) throws RemoteException;

    public static abstract class Stub extends Binder implements IAnalytics {
        private static final String DESCRIPTOR = "com.alibaba.analytics.IAnalytics";
        static final int TRANSACTION_alarm_checkSampled = 39;
        static final int TRANSACTION_alarm_commitFail1 = 42;
        static final int TRANSACTION_alarm_commitFail2 = 43;
        static final int TRANSACTION_alarm_commitSuccess1 = 40;
        static final int TRANSACTION_alarm_commitSuccess2 = 41;
        static final int TRANSACTION_alarm_enableOfflineAgg = 54;
        static final int TRANSACTION_alarm_setSampling = 38;
        static final int TRANSACTION_alarm_setStatisticsInterval = 37;
        static final int TRANSACTION_counter_checkSampled = 30;
        static final int TRANSACTION_counter_commit1 = 31;
        static final int TRANSACTION_counter_commit2 = 32;
        static final int TRANSACTION_counter_setSampling = 29;
        static final int TRANSACTION_counter_setStatisticsInterval = 28;
        static final int TRANSACTION_destroy = 18;
        static final int TRANSACTION_disableNetworkStatusChecker = 9;
        static final int TRANSACTION_dispatchLocalHits = 10;
        static final int TRANSACTION_enableLog = 14;
        static final int TRANSACTION_getValue = 12;
        static final int TRANSACTION_init = 13;
        static final int TRANSACTION_initUT = 1;
        static final int TRANSACTION_offlinecounter_checkSampled = 35;
        static final int TRANSACTION_offlinecounter_commit = 36;
        static final int TRANSACTION_offlinecounter_setSampling = 34;
        static final int TRANSACTION_offlinecounter_setStatisticsInterval = 33;
        static final int TRANSACTION_register1 = 23;
        static final int TRANSACTION_register2 = 24;
        static final int TRANSACTION_register3 = 25;
        static final int TRANSACTION_register4 = 26;
        static final int TRANSACTION_saveCacheDataToLocal = 11;
        static final int TRANSACTION_selfCheck = 55;
        static final int TRANSACTION_setAppVersion = 3;
        static final int TRANSACTION_setChannel = 4;
        static final int TRANSACTION_setRequestAuthInfo = 15;
        static final int TRANSACTION_setSampling = 20;
        static final int TRANSACTION_setSessionProperties = 6;
        static final int TRANSACTION_setStatisticsInterval1 = 21;
        static final int TRANSACTION_setStatisticsInterval2 = 22;
        static final int TRANSACTION_stat_begin = 44;
        static final int TRANSACTION_stat_checkSampled = 48;
        static final int TRANSACTION_stat_commit1 = 49;
        static final int TRANSACTION_stat_commit2 = 50;
        static final int TRANSACTION_stat_commit3 = 51;
        static final int TRANSACTION_stat_end = 45;
        static final int TRANSACTION_stat_setSampling = 47;
        static final int TRANSACTION_stat_setStatisticsInterval = 46;
        static final int TRANSACTION_transaction_begin = 52;
        static final int TRANSACTION_transaction_end = 53;
        static final int TRANSACTION_transferLog = 8;
        static final int TRANSACTION_triggerUpload = 19;
        static final int TRANSACTION_turnOffRealTimeDebug = 17;
        static final int TRANSACTION_turnOnDebug = 7;
        static final int TRANSACTION_turnOnRealTimeDebug = 16;
        static final int TRANSACTION_updateMeasure = 27;
        static final int TRANSACTION_updateSessionProperties = 5;
        static final int TRANSACTION_updateUserAccount = 2;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAnalytics asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAnalytics)) {
                return new Proxy(obj);
            }
            return (IAnalytics) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Transaction _arg0;
            Transaction _arg02;
            DimensionValueSet _arg2;
            MeasureValueSet _arg3;
            DimensionValueSet _arg22;
            MeasureSet _arg23;
            DimensionSet _arg32;
            MeasureSet _arg24;
            DimensionSet _arg33;
            MeasureSet _arg25;
            MeasureSet _arg26;
            switch (code) {
                case 1:
                    data.enforceInterface(DESCRIPTOR);
                    initUT();
                    reply.writeNoException();
                    return true;
                case 2:
                    data.enforceInterface(DESCRIPTOR);
                    updateUserAccount(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 3:
                    data.enforceInterface(DESCRIPTOR);
                    setAppVersion(data.readString());
                    reply.writeNoException();
                    return true;
                case 4:
                    data.enforceInterface(DESCRIPTOR);
                    setChannel(data.readString());
                    reply.writeNoException();
                    return true;
                case 5:
                    data.enforceInterface(DESCRIPTOR);
                    updateSessionProperties(data.readHashMap(getClass().getClassLoader()));
                    reply.writeNoException();
                    return true;
                case 6:
                    data.enforceInterface(DESCRIPTOR);
                    setSessionProperties(data.readHashMap(getClass().getClassLoader()));
                    reply.writeNoException();
                    return true;
                case 7:
                    data.enforceInterface(DESCRIPTOR);
                    turnOnDebug();
                    reply.writeNoException();
                    return true;
                case 8:
                    data.enforceInterface(DESCRIPTOR);
                    transferLog(data.readHashMap(getClass().getClassLoader()));
                    reply.writeNoException();
                    return true;
                case 10:
                    data.enforceInterface(DESCRIPTOR);
                    dispatchLocalHits();
                    reply.writeNoException();
                    return true;
                case 11:
                    data.enforceInterface(DESCRIPTOR);
                    saveCacheDataToLocal();
                    reply.writeNoException();
                    return true;
                case 12:
                    data.enforceInterface(DESCRIPTOR);
                    String _result = getValue(data.readString());
                    reply.writeNoException();
                    reply.writeString(_result);
                    return true;
                case 13:
                    data.enforceInterface(DESCRIPTOR);
                    init();
                    reply.writeNoException();
                    return true;
                case 14:
                    data.enforceInterface(DESCRIPTOR);
                    enableLog(data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 15:
                    data.enforceInterface(DESCRIPTOR);
                    setRequestAuthInfo(data.readInt() != 0, data.readInt() != 0, data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 16:
                    data.enforceInterface(DESCRIPTOR);
                    turnOnRealTimeDebug(data.readHashMap(getClass().getClassLoader()));
                    reply.writeNoException();
                    return true;
                case 17:
                    data.enforceInterface(DESCRIPTOR);
                    turnOffRealTimeDebug();
                    reply.writeNoException();
                    return true;
                case 18:
                    data.enforceInterface(DESCRIPTOR);
                    destroy();
                    reply.writeNoException();
                    return true;
                case 19:
                    data.enforceInterface(DESCRIPTOR);
                    triggerUpload();
                    reply.writeNoException();
                    return true;
                case 20:
                    data.enforceInterface(DESCRIPTOR);
                    setSampling(data.readInt());
                    reply.writeNoException();
                    return true;
                case 21:
                    data.enforceInterface(DESCRIPTOR);
                    setStatisticsInterval1(data.readInt());
                    reply.writeNoException();
                    return true;
                case 22:
                    data.enforceInterface(DESCRIPTOR);
                    setStatisticsInterval2(data.readInt(), data.readInt());
                    reply.writeNoException();
                    return true;
                case 23:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg03 = data.readString();
                    String _arg1 = data.readString();
                    if (data.readInt() != 0) {
                        _arg26 = MeasureSet.CREATOR.createFromParcel(data);
                    } else {
                        _arg26 = null;
                    }
                    register1(_arg03, _arg1, _arg26);
                    reply.writeNoException();
                    return true;
                case 24:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg04 = data.readString();
                    String _arg12 = data.readString();
                    if (data.readInt() != 0) {
                        _arg25 = MeasureSet.CREATOR.createFromParcel(data);
                    } else {
                        _arg25 = null;
                    }
                    register2(_arg04, _arg12, _arg25, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 25:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg05 = data.readString();
                    String _arg13 = data.readString();
                    if (data.readInt() != 0) {
                        _arg24 = MeasureSet.CREATOR.createFromParcel(data);
                    } else {
                        _arg24 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg33 = DimensionSet.CREATOR.createFromParcel(data);
                    } else {
                        _arg33 = null;
                    }
                    register3(_arg05, _arg13, _arg24, _arg33);
                    reply.writeNoException();
                    return true;
                case 26:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg06 = data.readString();
                    String _arg14 = data.readString();
                    if (data.readInt() != 0) {
                        _arg23 = MeasureSet.CREATOR.createFromParcel(data);
                    } else {
                        _arg23 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg32 = DimensionSet.CREATOR.createFromParcel(data);
                    } else {
                        _arg32 = null;
                    }
                    register4(_arg06, _arg14, _arg23, _arg32, data.readInt() != 0);
                    reply.writeNoException();
                    return true;
                case 27:
                    data.enforceInterface(DESCRIPTOR);
                    updateMeasure(data.readString(), data.readString(), data.readString(), data.readDouble(), data.readDouble(), data.readDouble());
                    reply.writeNoException();
                    return true;
                case 28:
                    data.enforceInterface(DESCRIPTOR);
                    counter_setStatisticsInterval(data.readInt());
                    reply.writeNoException();
                    return true;
                case 29:
                    data.enforceInterface(DESCRIPTOR);
                    counter_setSampling(data.readInt());
                    reply.writeNoException();
                    return true;
                case 30:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result2 = counter_checkSampled(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result2 ? 1 : 0);
                    return true;
                case 31:
                    data.enforceInterface(DESCRIPTOR);
                    counter_commit1(data.readString(), data.readString(), data.readDouble());
                    reply.writeNoException();
                    return true;
                case 32:
                    data.enforceInterface(DESCRIPTOR);
                    counter_commit2(data.readString(), data.readString(), data.readString(), data.readDouble());
                    reply.writeNoException();
                    return true;
                case 33:
                    data.enforceInterface(DESCRIPTOR);
                    offlinecounter_setStatisticsInterval(data.readInt());
                    reply.writeNoException();
                    return true;
                case 34:
                    data.enforceInterface(DESCRIPTOR);
                    offlinecounter_setSampling(data.readInt());
                    reply.writeNoException();
                    return true;
                case 35:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result3 = offlinecounter_checkSampled(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result3 ? 1 : 0);
                    return true;
                case 36:
                    data.enforceInterface(DESCRIPTOR);
                    offlinecounter_commit(data.readString(), data.readString(), data.readDouble());
                    reply.writeNoException();
                    return true;
                case 37:
                    data.enforceInterface(DESCRIPTOR);
                    alarm_setStatisticsInterval(data.readInt());
                    reply.writeNoException();
                    return true;
                case 38:
                    data.enforceInterface(DESCRIPTOR);
                    alarm_setSampling(data.readInt());
                    reply.writeNoException();
                    return true;
                case 39:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result4 = alarm_checkSampled(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result4 ? 1 : 0);
                    return true;
                case 40:
                    data.enforceInterface(DESCRIPTOR);
                    alarm_commitSuccess1(data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 41:
                    data.enforceInterface(DESCRIPTOR);
                    alarm_commitSuccess2(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 42:
                    data.enforceInterface(DESCRIPTOR);
                    alarm_commitFail1(data.readString(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 43:
                    data.enforceInterface(DESCRIPTOR);
                    alarm_commitFail2(data.readString(), data.readString(), data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 44:
                    data.enforceInterface(DESCRIPTOR);
                    stat_begin(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 45:
                    data.enforceInterface(DESCRIPTOR);
                    stat_end(data.readString(), data.readString(), data.readString());
                    reply.writeNoException();
                    return true;
                case 46:
                    data.enforceInterface(DESCRIPTOR);
                    stat_setStatisticsInterval(data.readInt());
                    reply.writeNoException();
                    return true;
                case 47:
                    data.enforceInterface(DESCRIPTOR);
                    stat_setSampling(data.readInt());
                    reply.writeNoException();
                    return true;
                case 48:
                    data.enforceInterface(DESCRIPTOR);
                    boolean _result5 = stat_checkSampled(data.readString(), data.readString());
                    reply.writeNoException();
                    reply.writeInt(_result5 ? 1 : 0);
                    return true;
                case 49:
                    data.enforceInterface(DESCRIPTOR);
                    stat_commit1(data.readString(), data.readString(), data.readDouble());
                    reply.writeNoException();
                    return true;
                case 50:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg07 = data.readString();
                    String _arg15 = data.readString();
                    if (data.readInt() != 0) {
                        _arg22 = DimensionValueSet.CREATOR.createFromParcel(data);
                    } else {
                        _arg22 = null;
                    }
                    stat_commit2(_arg07, _arg15, _arg22, data.readDouble());
                    reply.writeNoException();
                    return true;
                case 51:
                    data.enforceInterface(DESCRIPTOR);
                    String _arg08 = data.readString();
                    String _arg16 = data.readString();
                    if (data.readInt() != 0) {
                        _arg2 = DimensionValueSet.CREATOR.createFromParcel(data);
                    } else {
                        _arg2 = null;
                    }
                    if (data.readInt() != 0) {
                        _arg3 = MeasureValueSet.CREATOR.createFromParcel(data);
                    } else {
                        _arg3 = null;
                    }
                    stat_commit3(_arg08, _arg16, _arg2, _arg3);
                    reply.writeNoException();
                    return true;
                case 52:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg02 = Transaction.CREATOR.createFromParcel(data);
                    } else {
                        _arg02 = null;
                    }
                    transaction_begin(_arg02, data.readString());
                    reply.writeNoException();
                    return true;
                case 53:
                    data.enforceInterface(DESCRIPTOR);
                    if (data.readInt() != 0) {
                        _arg0 = Transaction.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    transaction_end(_arg0, data.readString());
                    reply.writeNoException();
                    return true;
                case 55:
                    data.enforceInterface(DESCRIPTOR);
                    String ret = selfCheck(data.readString());
                    reply.writeNoException();
                    reply.writeString(ret);
                    return true;
                case 1598968902:
                    reply.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(code, data, reply, flags);
            }
        }

        private static class Proxy implements IAnalytics {
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

            public void initUT() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(1, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateUserAccount(String aUsernick, String aUserid, String openid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(aUsernick);
                    _data.writeString(aUserid);
                    _data.writeString(openid);
                    this.mRemote.transact(2, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAppVersion(String appVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(appVersion);
                    this.mRemote.transact(3, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setChannel(String channel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(channel);
                    this.mRemote.transact(4, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateSessionProperties(Map aMap) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeMap(aMap);
                    this.mRemote.transact(5, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSessionProperties(Map aMap) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeMap(aMap);
                    this.mRemote.transact(6, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void turnOnDebug() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(7, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void transferLog(Map aLogMap) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeMap(aLogMap);
                    this.mRemote.transact(8, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dispatchLocalHits() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(10, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void saveCacheDataToLocal() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(11, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getValue(String aKey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(aKey);
                    this.mRemote.transact(12, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void init() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(13, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableLog(boolean open) throws RemoteException {
                int i = 0;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (open) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(14, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String selfCheck(String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(value);
                    this.mRemote.transact(55, _data, _reply, 0);
                    _reply.readException();
                    return _reply.readString();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRequestAuthInfo(boolean isSecurity, boolean isThridSdk, String appkey, String secret) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isSecurity ? 1 : 0);
                    if (!isThridSdk) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeString(appkey);
                    _data.writeString(secret);
                    this.mRemote.transact(15, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void turnOnRealTimeDebug(Map params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeMap(params);
                    this.mRemote.transact(16, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void turnOffRealTimeDebug() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(17, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void destroy() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(18, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void triggerUpload() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(19, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSampling(int sampling) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sampling);
                    this.mRemote.transact(20, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setStatisticsInterval1(int statisticsInterval) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(statisticsInterval);
                    this.mRemote.transact(21, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setStatisticsInterval2(int eventType, int statisticsInterval) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(eventType);
                    _data.writeInt(statisticsInterval);
                    this.mRemote.transact(22, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void register1(String module, String monitorPoint, MeasureSet measures) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    if (measures != null) {
                        _data.writeInt(1);
                        measures.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(23, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void register2(String module, String monitorPoint, MeasureSet measures, boolean isCommitDetail) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    if (measures != null) {
                        _data.writeInt(1);
                        measures.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!isCommitDetail) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(24, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void register3(String module, String monitorPoint, MeasureSet measures, DimensionSet dimensions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    if (measures != null) {
                        _data.writeInt(1);
                        measures.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (dimensions != null) {
                        _data.writeInt(1);
                        dimensions.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(25, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void register4(String module, String monitorPoint, MeasureSet measures, DimensionSet dimensions, boolean isCommitDetail) throws RemoteException {
                int i = 1;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    if (measures != null) {
                        _data.writeInt(1);
                        measures.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (dimensions != null) {
                        _data.writeInt(1);
                        dimensions.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!isCommitDetail) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    this.mRemote.transact(26, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateMeasure(String module, String monitorPoint, String name, double min, double max, double defaultValue) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    _data.writeString(name);
                    _data.writeDouble(min);
                    _data.writeDouble(max);
                    _data.writeDouble(defaultValue);
                    this.mRemote.transact(27, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void counter_setStatisticsInterval(int statisticsInterval) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(statisticsInterval);
                    this.mRemote.transact(28, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void counter_setSampling(int sampling) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sampling);
                    this.mRemote.transact(29, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean counter_checkSampled(String module, String monitorPoint) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    this.mRemote.transact(30, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void counter_commit1(String module, String monitorPoint, double value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    _data.writeDouble(value);
                    this.mRemote.transact(31, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void counter_commit2(String module, String monitorPoint, String arg, double value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    _data.writeString(arg);
                    _data.writeDouble(value);
                    this.mRemote.transact(32, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void offlinecounter_setStatisticsInterval(int statisticsInterval) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(statisticsInterval);
                    this.mRemote.transact(33, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void offlinecounter_setSampling(int sampling) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sampling);
                    this.mRemote.transact(34, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean offlinecounter_checkSampled(String module, String monitorPoint) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    this.mRemote.transact(35, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void offlinecounter_commit(String module, String monitorPoint, double value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    _data.writeDouble(value);
                    this.mRemote.transact(36, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void alarm_setStatisticsInterval(int statisticsInterval) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(statisticsInterval);
                    this.mRemote.transact(37, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void alarm_setSampling(int sampling) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sampling);
                    this.mRemote.transact(38, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean alarm_checkSampled(String module, String monitorPoint) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    this.mRemote.transact(39, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void alarm_commitSuccess1(String module, String monitorPoint) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    this.mRemote.transact(40, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void alarm_commitSuccess2(String module, String monitorPoint, String arg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    _data.writeString(arg);
                    this.mRemote.transact(41, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void alarm_commitFail1(String module, String monitorPoint, String errorCode, String errorMsg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    _data.writeString(errorCode);
                    _data.writeString(errorMsg);
                    this.mRemote.transact(42, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void alarm_commitFail2(String module, String monitorPoint, String arg, String errorCode, String errorMsg) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    _data.writeString(arg);
                    _data.writeString(errorCode);
                    _data.writeString(errorMsg);
                    this.mRemote.transact(43, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stat_begin(String module, String monitorPoint, String measureName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    _data.writeString(measureName);
                    this.mRemote.transact(44, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stat_end(String module, String monitorPoint, String measureName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    _data.writeString(measureName);
                    this.mRemote.transact(45, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stat_setStatisticsInterval(int statisticsInterval) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(statisticsInterval);
                    this.mRemote.transact(46, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stat_setSampling(int sampling) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sampling);
                    this.mRemote.transact(47, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean stat_checkSampled(String module, String monitorPoint) throws RemoteException {
                boolean _result = false;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    this.mRemote.transact(48, _data, _reply, 0);
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        _result = true;
                    }
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stat_commit1(String module, String monitorPoint, double value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    _data.writeDouble(value);
                    this.mRemote.transact(49, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stat_commit2(String module, String monitorPoint, DimensionValueSet dimensionValues, double value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    if (dimensionValues != null) {
                        _data.writeInt(1);
                        dimensionValues.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeDouble(value);
                    this.mRemote.transact(50, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stat_commit3(String module, String monitorPoint, DimensionValueSet dimensionValues, MeasureValueSet measureValues) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(module);
                    _data.writeString(monitorPoint);
                    if (dimensionValues != null) {
                        _data.writeInt(1);
                        dimensionValues.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (measureValues != null) {
                        _data.writeInt(1);
                        measureValues.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    this.mRemote.transact(51, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void transaction_begin(Transaction transaction, String measureName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (transaction != null) {
                        _data.writeInt(1);
                        transaction.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(measureName);
                    this.mRemote.transact(52, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void transaction_end(Transaction transaction, String measureName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (transaction != null) {
                        _data.writeInt(1);
                        transaction.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(measureName);
                    this.mRemote.transact(53, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
