package com.yunos.tv.paysdk;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class AliTVPayResult implements Parcelable {
    public static final Parcelable.Creator<AliTVPayResult> CREATOR = new Parcelable.Creator<AliTVPayResult>() {
        public AliTVPayResult createFromParcel(Parcel source) {
            AliTVPayResult mYunOSPayResult = new AliTVPayResult();
            boolean unused = mYunOSPayResult.payResult = source.readInt() != 0;
            String unused2 = mYunOSPayResult.payFeedback = source.readString();
            Bundle unused3 = mYunOSPayResult.payInformation = source.readBundle();
            return mYunOSPayResult;
        }

        public AliTVPayResult[] newArray(int size) {
            return new AliTVPayResult[size];
        }
    };
    /* access modifiers changed from: private */
    public String payFeedback;
    /* access modifiers changed from: private */
    public Bundle payInformation;
    /* access modifiers changed from: private */
    public boolean payResult;

    public boolean getPayResult() {
        return this.payResult;
    }

    public String getPayFeedback() {
        return this.payFeedback;
    }

    public Bundle getPayInformation() {
        return this.payInformation;
    }

    public void setPayResult(boolean result) {
        this.payResult = result;
    }

    public void setPayFeedback(String feedback) {
        this.payFeedback = feedback;
    }

    public void setPayInformation(Bundle bundle) {
        this.payInformation = bundle;
    }

    public String getPayInformation(String requestcode) {
        return this.payInformation.getString(requestcode);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.payResult ? 1 : 0);
        dest.writeString(this.payFeedback);
        if (this.payInformation != null) {
            dest.writeBundle(this.payInformation);
        }
    }
}
