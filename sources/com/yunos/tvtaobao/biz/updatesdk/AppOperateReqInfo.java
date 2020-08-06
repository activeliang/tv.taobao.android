package com.yunos.tvtaobao.biz.updatesdk;

import android.os.Parcel;
import android.os.Parcelable;

public class AppOperateReqInfo implements Parcelable {
    public static final Parcelable.Creator<AppOperateReqInfo> CREATOR = new Parcelable.Creator<AppOperateReqInfo>() {
        public AppOperateReqInfo createFromParcel(Parcel source) {
            return new AppOperateReqInfo(source);
        }

        public AppOperateReqInfo[] newArray(int size) {
            return new AppOperateReqInfo[size];
        }
    };
    protected String apkPath;
    protected String appId;
    protected String appName;
    protected String operate;
    protected String packageName;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("appId                    = ").append(this.appId).append("\n");
        sb.append("appName                  = ").append(this.appName).append("\n");
        sb.append("packageName              = ").append(this.packageName).append("\n");
        sb.append("operate                  = ").append(this.operate).append("\n");
        sb.append("apkPath                  = ").append(this.apkPath).append("\n");
        return sb.toString();
    }

    public AppOperateReqInfo() {
    }

    public AppOperateReqInfo(String appId2, String appName2, String pkgName, String operate2) {
        this.appId = appId2;
        this.operate = operate2;
        this.appName = appName2;
        this.packageName = pkgName;
        this.operate = operate2;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId2) {
        this.appId = appId2;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String appName2) {
        this.appName = appName2;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName2) {
        this.packageName = packageName2;
    }

    public String getOperate() {
        return this.operate;
    }

    public void setOperate(String operate2) {
        this.operate = operate2;
    }

    public String getApkPath() {
        return this.apkPath;
    }

    public void setApkPath(String apkPath2) {
        this.apkPath = apkPath2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appId);
        dest.writeString(this.appName);
        dest.writeString(this.packageName);
        dest.writeString(this.operate);
        dest.writeString(this.apkPath);
    }

    public AppOperateReqInfo(Parcel source) {
        this.appId = source.readString();
        this.appName = source.readString();
        this.packageName = source.readString();
        this.operate = source.readString();
        this.apkPath = source.readString();
    }
}
