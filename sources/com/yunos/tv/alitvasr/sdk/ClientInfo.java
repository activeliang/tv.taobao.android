package com.yunos.tv.alitvasr.sdk;

import android.os.Parcel;
import android.os.Parcelable;
import com.yunos.tv.alitvasr.IAliTVASRCallback;

public class ClientInfo implements Parcelable {
    public static final Parcelable.Creator<ClientInfo> CREATOR = new Parcelable.Creator<ClientInfo>() {
        public ClientInfo createFromParcel(Parcel in) {
            return new ClientInfo(in);
        }

        public ClientInfo[] newArray(int size) {
            return new ClientInfo[size];
        }
    };
    public static final int FLAG_ENABLE = 4;
    public static final int FLAG_EXTRA = 3;
    public static final int FLAG_GET_BACKGROUND_PACKAGE_NAME = 7;
    public static final int FLAG_GET_NLU_PACKAGE_NAME = 8;
    public static final int FLAG_GET_TOP_PACKAGE_NAME = 6;
    public static final int FLAG_IS_SHOW_UI = 0;
    public static final int FLAG_REMOVE_PACKAGE_NAME = 5;
    public static final int FLAG_RESULT_MODE = 1;
    public static final int FLAG_SERVER_MODE = 2;
    private int asrMode;
    private IAliTVASRCallback callback;
    private String extra;
    private boolean isShowUI;
    private String packageName;
    private int resultMode;

    public ClientInfo(String packageName2, IAliTVASRCallback callback2, boolean showUI, int resultMode2, int asrMode2) {
        this.packageName = packageName2;
        this.callback = callback2;
        this.isShowUI = showUI;
        this.resultMode = resultMode2;
        this.asrMode = asrMode2;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName2) {
        this.packageName = packageName2;
    }

    public boolean isShowUI() {
        return this.isShowUI;
    }

    public void setShowUI(boolean showUI) {
        this.isShowUI = showUI;
    }

    public int getResultMode() {
        return this.resultMode;
    }

    public void setResultMode(int resultMode2) {
        this.resultMode = resultMode2;
    }

    public int getAsrMode() {
        return this.asrMode;
    }

    public void setAsrMode(int serverMode) {
        this.asrMode = serverMode;
    }

    public IAliTVASRCallback getCallback() {
        return this.callback;
    }

    public void setCallback(IAliTVASRCallback callback2) {
        this.callback = callback2;
    }

    public String getExtra() {
        return this.extra;
    }

    public void setExtra(String extra2) {
        this.extra = extra2;
    }

    public String toString() {
        return "ClientInfo{packageName='" + this.packageName + '\'' + ", isShowUI=" + this.isShowUI + ", resultMode=" + this.resultMode + ", serverMode=" + this.asrMode + ", extra='" + this.extra + '\'' + ", callback='" + this.callback + '\'' + '}';
    }

    public int describeContents() {
        return 0;
    }

    public ClientInfo(Parcel parcel) {
        this.packageName = parcel.readString();
        this.isShowUI = parcel.readInt() != 0;
        this.resultMode = parcel.readInt();
        this.asrMode = parcel.readInt();
        this.callback = IAliTVASRCallback.Stub.asInterface(parcel.readStrongBinder());
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.packageName);
        parcel.writeInt(this.isShowUI ? 1 : 0);
        parcel.writeInt(this.resultMode);
        parcel.writeInt(this.asrMode);
        parcel.writeStrongInterface(this.callback);
        parcel.writeString(this.extra);
    }
}
