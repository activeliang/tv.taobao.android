package com.bftv.fui.tell;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Tell implements Parcelable {
    public static final Parcelable.Creator<Tell> CREATOR = new Parcelable.Creator<Tell>() {
        public Tell createFromParcel(Parcel in) {
            return new Tell(in);
        }

        public Tell[] newArray(int size) {
            return new Tell[size];
        }
    };
    private ConcurrentHashMap<String, String> appCacheMap = new ConcurrentHashMap<>();
    public String className;
    private ConcurrentHashMap<String, String> correctMap = new ConcurrentHashMap<>();
    public String flag;
    public int functionSupportType;
    public int groupId = -1;
    public boolean isAppend;
    public boolean isEnableBetterAsr;
    public boolean isEnableContinuousRecognition;
    public boolean isHideAnimation;
    public boolean isNeedDDZGameConversion;
    public boolean isNeedPinYin;
    public boolean isNeedViewCacheRecyclingNotice;
    public boolean isSupportGroup = false;
    public String key;
    public String pck;
    public int sequencecode;
    public int tellType;
    public String temp1;
    public String temp2;
    public List<String> tipList;
    private ConcurrentHashMap<String, String> tipsMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> viewCacheMap = new ConcurrentHashMap<>();

    public Tell() {
    }

    protected Tell(Parcel in) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7 = true;
        this.pck = in.readString();
        this.className = in.readString();
        this.flag = in.readString();
        this.tellType = in.readInt();
        this.tipList = in.createStringArrayList();
        this.temp1 = in.readString();
        this.temp2 = in.readString();
        this.sequencecode = in.readInt();
        in.readMap(this.viewCacheMap, getClass().getClassLoader());
        in.readMap(this.appCacheMap, getClass().getClassLoader());
        in.readMap(this.correctMap, getClass().getClassLoader());
        this.isAppend = in.readByte() != 0;
        this.functionSupportType = in.readInt();
        in.readMap(this.tipsMap, getClass().getClassLoader());
        if (in.readByte() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.isNeedPinYin = z;
        if (in.readByte() != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.isNeedViewCacheRecyclingNotice = z2;
        if (in.readByte() != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.isNeedDDZGameConversion = z3;
        if (in.readByte() != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.isHideAnimation = z4;
        if (in.readByte() != 0) {
            z5 = true;
        } else {
            z5 = false;
        }
        this.isEnableContinuousRecognition = z5;
        if (in.readByte() != 0) {
            z6 = true;
        } else {
            z6 = false;
        }
        this.isEnableBetterAsr = z6;
        this.key = in.readString();
        this.isSupportGroup = in.readByte() == 0 ? false : z7;
        this.groupId = in.readInt();
    }

    public ConcurrentHashMap<String, String> getViewCacheMap() {
        return this.viewCacheMap;
    }

    public void setViewCacheMap(ConcurrentHashMap<String, String> viewCacheMap2) {
        this.viewCacheMap = viewCacheMap2;
    }

    public void setHashMapViewCacheMap(HashMap<String, String> viewCacheMap2) {
        this.viewCacheMap = new ConcurrentHashMap<>();
        this.viewCacheMap.putAll(viewCacheMap2);
    }

    public ConcurrentHashMap<String, String> getAppCacheMap() {
        return this.appCacheMap;
    }

    public void setAppCacheMap(ConcurrentHashMap<String, String> appCacheMap2) {
        this.appCacheMap = appCacheMap2;
    }

    public void setHashMapAppCacheMap(HashMap<String, String> appCacheMap2) {
        this.appCacheMap = new ConcurrentHashMap<>();
        this.appCacheMap.putAll(appCacheMap2);
    }

    public ConcurrentHashMap<String, String> getCorrectMap() {
        return this.correctMap;
    }

    public void setCorrectMap(ConcurrentHashMap<String, String> correctMap2) {
        this.correctMap = correctMap2;
    }

    public void setHashMapCorrectMap(HashMap<String, String> correctMap2) {
        this.correctMap = new ConcurrentHashMap<>();
        this.correctMap.putAll(correctMap2);
    }

    public ConcurrentHashMap<String, String> getTipsMap() {
        return this.tipsMap;
    }

    public void setTipsMap(ConcurrentHashMap<String, String> tipsMap2) {
        this.tipsMap = tipsMap2;
    }

    public void setHashMapTipsMap(HashMap<String, String> tipsMap2) {
        this.tipsMap = new ConcurrentHashMap<>();
        this.tipsMap.putAll(tipsMap2);
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7 = 1;
        dest.writeString(this.pck);
        dest.writeString(this.className);
        dest.writeString(this.flag);
        dest.writeInt(this.tellType);
        dest.writeStringList(this.tipList);
        dest.writeString(this.temp1);
        dest.writeString(this.temp2);
        dest.writeInt(this.sequencecode);
        dest.writeMap(this.viewCacheMap);
        dest.writeMap(this.appCacheMap);
        dest.writeMap(this.correctMap);
        dest.writeByte((byte) (this.isAppend ? 1 : 0));
        dest.writeInt(this.functionSupportType);
        dest.writeMap(this.tipsMap);
        if (this.isNeedPinYin) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeByte((byte) i);
        if (this.isNeedViewCacheRecyclingNotice) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        dest.writeByte((byte) i2);
        if (this.isNeedDDZGameConversion) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        dest.writeByte((byte) i3);
        if (this.isHideAnimation) {
            i4 = 1;
        } else {
            i4 = 0;
        }
        dest.writeByte((byte) i4);
        if (this.isEnableContinuousRecognition) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        dest.writeByte((byte) i5);
        if (this.isEnableBetterAsr) {
            i6 = 1;
        } else {
            i6 = 0;
        }
        dest.writeByte((byte) i6);
        dest.writeString(this.key);
        if (!this.isSupportGroup) {
            i7 = 0;
        }
        dest.writeByte((byte) i7);
        dest.writeInt(this.groupId);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "Tell{pck='" + this.pck + '\'' + ", className='" + this.className + '\'' + ", flag='" + this.flag + '\'' + ", tellType=" + this.tellType + ", functionSupportType=" + this.functionSupportType + ", viewCacheMap=" + this.viewCacheMap + ", tipList=" + this.tipList + ", appCacheMap=" + this.appCacheMap + ", correctMap=" + this.correctMap + ", temp1='" + this.temp1 + '\'' + ", temp2='" + this.temp2 + '\'' + ", sequencecode=" + this.sequencecode + ", isAppend=" + this.isAppend + ", isNeedPinYin=" + this.isNeedPinYin + ", tipsMap=" + this.tipsMap + ", isNeedViewCacheRecyclingNotice=" + this.isNeedViewCacheRecyclingNotice + ", isNeedDDZGameConversion=" + this.isNeedDDZGameConversion + ", isHideAnimation=" + this.isHideAnimation + ", isEnableContinuousRecognition=" + this.isEnableContinuousRecognition + ", isEnableBetterAsr=" + this.isEnableBetterAsr + ", key='" + this.key + '\'' + ", isSupportGroup=" + this.isSupportGroup + ", groupId=" + this.groupId + '}';
    }
}
