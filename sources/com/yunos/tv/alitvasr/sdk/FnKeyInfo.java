package com.yunos.tv.alitvasr.sdk;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import com.yunos.tv.alitvasrsdk.CommonData;

public class FnKeyInfo implements Parcelable {
    public static final Parcelable.Creator<FnKeyInfo> CREATOR = new Parcelable.Creator<FnKeyInfo>() {
        public FnKeyInfo createFromParcel(Parcel in) {
            return new FnKeyInfo(in);
        }

        public FnKeyInfo[] newArray(int size) {
            return new FnKeyInfo[size];
        }
    };
    private String actionType;
    private String appAction;
    private String className;
    private boolean interested = true;
    private int keyCode = -1;
    private String packageName;

    public FnKeyInfo(Intent intent) {
        if (intent != null) {
            this.appAction = intent.getStringExtra("action");
            this.keyCode = intent.getIntExtra(CommonData.KEY_KEYCODE, -1);
            this.actionType = intent.getStringExtra("actionType");
            this.packageName = intent.getStringExtra(CommonData.KEY_PACKAGE_NAME);
            this.className = intent.getStringExtra(CommonData.KEY_CLASS_NAME);
            this.interested = intent.getBooleanExtra(CommonData.KEY_INTERESTED, true);
        }
    }

    public FnKeyInfo(int keyCode2) {
        this.keyCode = keyCode2;
    }

    public static Intent getIntent(FnKeyInfo keyInfo) {
        if (keyInfo == null || !keyInfo.isValid()) {
            return null;
        }
        Intent intent = new Intent();
        intent.putExtra("action", keyInfo.appAction);
        intent.putExtra(CommonData.KEY_KEYCODE, keyInfo.keyCode);
        intent.putExtra("actionType", keyInfo.actionType);
        intent.putExtra(CommonData.KEY_PACKAGE_NAME, keyInfo.packageName);
        intent.putExtra(CommonData.KEY_CLASS_NAME, keyInfo.className);
        intent.putExtra(CommonData.KEY_INTERESTED, keyInfo.interested);
        return intent;
    }

    public String getAppAction() {
        return this.appAction;
    }

    public void setAppAction(String appAction2) {
        this.appAction = appAction2;
    }

    public String getActionType() {
        return this.actionType;
    }

    public void setActionType(String actionType2) {
        this.actionType = actionType2;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName2) {
        this.packageName = packageName2;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className2) {
        this.className = className2;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public void setKeyCode(int keyCode2) {
        this.keyCode = keyCode2;
    }

    public boolean isInterested() {
        return this.interested;
    }

    public void setInterested(boolean interested2) {
        this.interested = interested2;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean equals(Object o) {
        if (o != null && this.keyCode == ((FnKeyInfo) o).keyCode) {
            return true;
        }
        return false;
    }

    public boolean isValid() {
        return this.keyCode >= 0;
    }

    public String toString() {
        return "FnKeyInfo{appAction='" + this.appAction + '\'' + ", actionType='" + this.actionType + '\'' + ", packageName='" + this.packageName + '\'' + ", className='" + this.className + '\'' + ", keyCode=" + this.keyCode + ", interested=" + this.interested + '}';
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.appAction);
        parcel.writeString(this.actionType);
        parcel.writeString(this.packageName);
        parcel.writeString(this.className);
        parcel.writeInt(this.keyCode);
    }

    public FnKeyInfo(Parcel in) {
        this.appAction = in.readString();
        this.actionType = in.readString();
        this.packageName = in.readString();
        this.className = in.readString();
        this.keyCode = in.readInt();
    }
}
