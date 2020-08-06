package com.alibaba.mtl.appmonitor;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import com.alibaba.analytics.AnalyticsMgr;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import java.util.UUID;

public class Transaction implements Parcelable {
    public static Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        public Transaction createFromParcel(Parcel source) {
            return Transaction.readFromParcel(source);
        }

        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };
    public DimensionValueSet dimensionValues;
    public Integer eventId;
    private Object lock;
    public String module;
    public String monitorPoint;
    public String transactionId;

    public Transaction(Integer eventId2, String module2, String monitorPoint2, DimensionValueSet dimensionValues2) {
        this.eventId = eventId2;
        this.module = module2;
        this.monitorPoint = monitorPoint2;
        this.transactionId = UUID.randomUUID().toString();
        this.dimensionValues = dimensionValues2;
        this.lock = new Object();
    }

    public Transaction() {
    }

    public void begin(String measureName) {
        Logger.d();
        if (AnalyticsMgr.iAnalytics != null) {
            try {
                AnalyticsMgr.iAnalytics.transaction_begin(this, measureName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void end(String measureName) {
        Logger.d();
        if (AnalyticsMgr.iAnalytics != null) {
            try {
                AnalyticsMgr.iAnalytics.transaction_end(this, measureName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void addDimensionValues(DimensionValueSet dimensionValues2) {
        synchronized (this.lock) {
            if (this.dimensionValues == null) {
                this.dimensionValues = dimensionValues2;
            } else {
                this.dimensionValues.addValues(dimensionValues2);
            }
        }
    }

    public void addDimensionValues(String dimensionName, String dimensionValue) {
        synchronized (this.lock) {
            if (this.dimensionValues == null) {
                this.dimensionValues = (DimensionValueSet) BalancedPool.getInstance().poll(DimensionValueSet.class, new Object[0]);
            }
            this.dimensionValues.setValue(dimensionName, dimensionValue);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.dimensionValues, flags);
        dest.writeInt(this.eventId.intValue());
        dest.writeString(this.module);
        dest.writeString(this.monitorPoint);
        dest.writeString(this.transactionId);
    }

    static Transaction readFromParcel(Parcel source) {
        Transaction ret = new Transaction();
        try {
            ret.dimensionValues = (DimensionValueSet) source.readParcelable(Transaction.class.getClassLoader());
            ret.eventId = Integer.valueOf(source.readInt());
            ret.module = source.readString();
            ret.monitorPoint = source.readString();
            ret.transactionId = source.readString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return ret;
    }
}
