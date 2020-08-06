package com.amap.api.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.bftv.fui.constantplugin.Constant;
import com.loc.en;
import com.yunos.tv.alitvasr.sdk.AbstractClientManager;

public class AMapLocationClientOption implements Parcelable, Cloneable {
    private static boolean A = true;
    public static final Parcelable.Creator<AMapLocationClientOption> CREATOR = new Parcelable.Creator<AMapLocationClientOption>() {
        public final /* synthetic */ Object createFromParcel(Parcel parcel) {
            return new AMapLocationClientOption(parcel);
        }

        public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
            return new AMapLocationClientOption[i];
        }
    };
    public static boolean OPEN_ALWAYS_SCAN_WIFI = true;
    public static long SCAN_WIFI_INTERVAL = 30000;
    static String a = "";
    private static int d = 0;
    private static int e = 1;
    private static int f = 2;
    private static int g = 4;
    private static AMapLocationProtocol p = AMapLocationProtocol.HTTP;
    private float B = 0.0f;
    private AMapLocationPurpose C = null;
    boolean b = false;
    String c = null;
    private long h = AbstractClientManager.BIND_SERVICE_TIMEOUT;
    private long i = ((long) en.g);
    private boolean j = false;
    private boolean k = true;
    private boolean l = true;
    private boolean m = true;
    private boolean n = true;
    private AMapLocationMode o = AMapLocationMode.Hight_Accuracy;
    private boolean q = false;
    private boolean r = false;
    private boolean s = true;
    private boolean t = true;
    private boolean u = false;
    private boolean v = false;
    private boolean w = true;
    private long x = 30000;
    private long y = 30000;
    private GeoLanguage z = GeoLanguage.DEFAULT;

    public enum AMapLocationMode {
        Battery_Saving,
        Device_Sensors,
        Hight_Accuracy
    }

    public enum AMapLocationProtocol {
        HTTP(0),
        HTTPS(1);
        
        private int a;

        private AMapLocationProtocol(int i) {
            this.a = i;
        }

        public final int getValue() {
            return this.a;
        }
    }

    public enum AMapLocationPurpose {
        SignIn,
        Transport,
        Sport
    }

    public enum GeoLanguage {
        DEFAULT,
        ZH,
        EN
    }

    public AMapLocationClientOption() {
    }

    protected AMapLocationClientOption(Parcel parcel) {
        boolean z2 = true;
        this.h = parcel.readLong();
        this.i = parcel.readLong();
        this.j = parcel.readByte() != 0;
        this.k = parcel.readByte() != 0;
        this.l = parcel.readByte() != 0;
        this.m = parcel.readByte() != 0;
        this.n = parcel.readByte() != 0;
        int readInt = parcel.readInt();
        this.o = readInt == -1 ? AMapLocationMode.Hight_Accuracy : AMapLocationMode.values()[readInt];
        this.q = parcel.readByte() != 0;
        this.r = parcel.readByte() != 0;
        this.s = parcel.readByte() != 0;
        this.t = parcel.readByte() != 0;
        this.u = parcel.readByte() != 0;
        this.v = parcel.readByte() != 0;
        this.w = parcel.readByte() != 0;
        this.x = parcel.readLong();
        int readInt2 = parcel.readInt();
        p = readInt2 == -1 ? AMapLocationProtocol.HTTP : AMapLocationProtocol.values()[readInt2];
        int readInt3 = parcel.readInt();
        this.z = readInt3 == -1 ? GeoLanguage.DEFAULT : GeoLanguage.values()[readInt3];
        A = parcel.readByte() != 0;
        this.B = parcel.readFloat();
        int readInt4 = parcel.readInt();
        this.C = readInt4 == -1 ? null : AMapLocationPurpose.values()[readInt4];
        OPEN_ALWAYS_SCAN_WIFI = parcel.readByte() == 0 ? false : z2;
        this.y = parcel.readLong();
    }

    public static String getAPIKEY() {
        return a;
    }

    public static boolean isDownloadCoordinateConvertLibrary() {
        return A;
    }

    public static boolean isOpenAlwaysScanWifi() {
        return OPEN_ALWAYS_SCAN_WIFI;
    }

    public static void setDownloadCoordinateConvertLibrary(boolean z2) {
        A = z2;
    }

    public static void setLocationProtocol(AMapLocationProtocol aMapLocationProtocol) {
        p = aMapLocationProtocol;
    }

    public static void setOpenAlwaysScanWifi(boolean z2) {
        OPEN_ALWAYS_SCAN_WIFI = z2;
    }

    public static void setScanWifiInterval(long j2) {
        SCAN_WIFI_INTERVAL = j2;
    }

    public AMapLocationClientOption clone() {
        try {
            super.clone();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        AMapLocationClientOption aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.h = this.h;
        aMapLocationClientOption.j = this.j;
        aMapLocationClientOption.o = this.o;
        aMapLocationClientOption.k = this.k;
        aMapLocationClientOption.q = this.q;
        aMapLocationClientOption.r = this.r;
        aMapLocationClientOption.l = this.l;
        aMapLocationClientOption.m = this.m;
        aMapLocationClientOption.i = this.i;
        aMapLocationClientOption.s = this.s;
        aMapLocationClientOption.t = this.t;
        aMapLocationClientOption.u = this.u;
        aMapLocationClientOption.v = isSensorEnable();
        aMapLocationClientOption.w = isWifiScan();
        aMapLocationClientOption.x = this.x;
        setLocationProtocol(getLocationProtocol());
        aMapLocationClientOption.z = this.z;
        setDownloadCoordinateConvertLibrary(isDownloadCoordinateConvertLibrary());
        aMapLocationClientOption.B = this.B;
        aMapLocationClientOption.C = this.C;
        setOpenAlwaysScanWifi(isOpenAlwaysScanWifi());
        setScanWifiInterval(getScanWifiInterval());
        aMapLocationClientOption.y = this.y;
        return aMapLocationClientOption;
    }

    public int describeContents() {
        return 0;
    }

    public float getDeviceModeDistanceFilter() {
        return this.B;
    }

    public GeoLanguage getGeoLanguage() {
        return this.z;
    }

    public long getGpsFirstTimeout() {
        return this.y;
    }

    public long getHttpTimeOut() {
        return this.i;
    }

    public long getInterval() {
        return this.h;
    }

    public long getLastLocationLifeCycle() {
        return this.x;
    }

    public AMapLocationMode getLocationMode() {
        return this.o;
    }

    public AMapLocationProtocol getLocationProtocol() {
        return p;
    }

    public AMapLocationPurpose getLocationPurpose() {
        return this.C;
    }

    public long getScanWifiInterval() {
        return SCAN_WIFI_INTERVAL;
    }

    public boolean isGpsFirst() {
        return this.r;
    }

    public boolean isKillProcess() {
        return this.q;
    }

    public boolean isLocationCacheEnable() {
        return this.t;
    }

    public boolean isMockEnable() {
        return this.k;
    }

    public boolean isNeedAddress() {
        return this.l;
    }

    public boolean isOffset() {
        return this.s;
    }

    public boolean isOnceLocation() {
        return this.j;
    }

    public boolean isOnceLocationLatest() {
        return this.u;
    }

    public boolean isSensorEnable() {
        return this.v;
    }

    public boolean isWifiActiveScan() {
        return this.m;
    }

    public boolean isWifiScan() {
        return this.w;
    }

    public AMapLocationClientOption setDeviceModeDistanceFilter(float f2) {
        this.B = f2;
        return this;
    }

    public AMapLocationClientOption setGeoLanguage(GeoLanguage geoLanguage) {
        this.z = geoLanguage;
        return this;
    }

    public AMapLocationClientOption setGpsFirst(boolean z2) {
        this.r = z2;
        return this;
    }

    public AMapLocationClientOption setGpsFirstTimeout(long j2) {
        long j3 = 30000;
        long j4 = 5000;
        if (j2 >= 5000) {
            j4 = j2;
        }
        if (j4 <= 30000) {
            j3 = j4;
        }
        this.y = j3;
        return this;
    }

    public AMapLocationClientOption setHttpTimeOut(long j2) {
        this.i = j2;
        return this;
    }

    public AMapLocationClientOption setInterval(long j2) {
        if (j2 <= 800) {
            j2 = 800;
        }
        this.h = j2;
        return this;
    }

    public AMapLocationClientOption setKillProcess(boolean z2) {
        this.q = z2;
        return this;
    }

    public AMapLocationClientOption setLastLocationLifeCycle(long j2) {
        this.x = j2;
        return this;
    }

    public AMapLocationClientOption setLocationCacheEnable(boolean z2) {
        this.t = z2;
        return this;
    }

    public AMapLocationClientOption setLocationMode(AMapLocationMode aMapLocationMode) {
        this.o = aMapLocationMode;
        return this;
    }

    public AMapLocationClientOption setLocationPurpose(AMapLocationPurpose aMapLocationPurpose) {
        this.C = aMapLocationPurpose;
        if (aMapLocationPurpose != null) {
            switch (aMapLocationPurpose) {
                case SignIn:
                    this.o = AMapLocationMode.Hight_Accuracy;
                    this.j = true;
                    this.u = true;
                    this.r = false;
                    this.k = false;
                    this.w = true;
                    if ((d & e) == 0) {
                        this.b = true;
                        d |= e;
                        this.c = "signin";
                        break;
                    }
                    break;
                case Transport:
                    if ((d & f) == 0) {
                        this.b = true;
                        d |= f;
                        this.c = "transport";
                    }
                    this.o = AMapLocationMode.Hight_Accuracy;
                    this.j = false;
                    this.u = false;
                    this.r = true;
                    this.k = false;
                    this.w = true;
                    break;
                case Sport:
                    if ((d & g) == 0) {
                        this.b = true;
                        d |= g;
                        this.c = "sport";
                    }
                    this.o = AMapLocationMode.Hight_Accuracy;
                    this.j = false;
                    this.u = false;
                    this.r = true;
                    this.k = false;
                    this.w = true;
                    break;
            }
        }
        return this;
    }

    public AMapLocationClientOption setMockEnable(boolean z2) {
        this.k = z2;
        return this;
    }

    public AMapLocationClientOption setNeedAddress(boolean z2) {
        this.l = z2;
        return this;
    }

    public AMapLocationClientOption setOffset(boolean z2) {
        this.s = z2;
        return this;
    }

    public AMapLocationClientOption setOnceLocation(boolean z2) {
        this.j = z2;
        return this;
    }

    public AMapLocationClientOption setOnceLocationLatest(boolean z2) {
        this.u = z2;
        return this;
    }

    public AMapLocationClientOption setSensorEnable(boolean z2) {
        this.v = z2;
        return this;
    }

    public AMapLocationClientOption setWifiActiveScan(boolean z2) {
        this.m = z2;
        this.n = z2;
        return this;
    }

    public AMapLocationClientOption setWifiScan(boolean z2) {
        this.w = z2;
        if (this.w) {
            this.m = this.n;
        } else {
            this.m = false;
        }
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("interval:").append(String.valueOf(this.h)).append(Constant.INTENT_JSON_MARK);
        sb.append("isOnceLocation:").append(String.valueOf(this.j)).append(Constant.INTENT_JSON_MARK);
        sb.append("locationMode:").append(String.valueOf(this.o)).append(Constant.INTENT_JSON_MARK);
        sb.append("locationProtocol:").append(String.valueOf(p)).append(Constant.INTENT_JSON_MARK);
        sb.append("isMockEnable:").append(String.valueOf(this.k)).append(Constant.INTENT_JSON_MARK);
        sb.append("isKillProcess:").append(String.valueOf(this.q)).append(Constant.INTENT_JSON_MARK);
        sb.append("isGpsFirst:").append(String.valueOf(this.r)).append(Constant.INTENT_JSON_MARK);
        sb.append("isNeedAddress:").append(String.valueOf(this.l)).append(Constant.INTENT_JSON_MARK);
        sb.append("isWifiActiveScan:").append(String.valueOf(this.m)).append(Constant.INTENT_JSON_MARK);
        sb.append("wifiScan:").append(String.valueOf(this.w)).append(Constant.INTENT_JSON_MARK);
        sb.append("httpTimeOut:").append(String.valueOf(this.i)).append(Constant.INTENT_JSON_MARK);
        sb.append("isLocationCacheEnable:").append(String.valueOf(this.t)).append(Constant.INTENT_JSON_MARK);
        sb.append("isOnceLocationLatest:").append(String.valueOf(this.u)).append(Constant.INTENT_JSON_MARK);
        sb.append("sensorEnable:").append(String.valueOf(this.v)).append(Constant.INTENT_JSON_MARK);
        sb.append("geoLanguage:").append(String.valueOf(this.z)).append(Constant.INTENT_JSON_MARK);
        sb.append("locationPurpose:").append(String.valueOf(this.C)).append(Constant.INTENT_JSON_MARK);
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i2) {
        int i3 = -1;
        int i4 = 1;
        parcel.writeLong(this.h);
        parcel.writeLong(this.i);
        parcel.writeByte(this.j ? (byte) 1 : 0);
        parcel.writeByte(this.k ? (byte) 1 : 0);
        parcel.writeByte(this.l ? (byte) 1 : 0);
        parcel.writeByte(this.m ? (byte) 1 : 0);
        parcel.writeByte(this.n ? (byte) 1 : 0);
        parcel.writeInt(this.o == null ? -1 : this.o.ordinal());
        parcel.writeByte(this.q ? (byte) 1 : 0);
        parcel.writeByte(this.r ? (byte) 1 : 0);
        parcel.writeByte(this.s ? (byte) 1 : 0);
        parcel.writeByte(this.t ? (byte) 1 : 0);
        parcel.writeByte(this.u ? (byte) 1 : 0);
        parcel.writeByte(this.v ? (byte) 1 : 0);
        parcel.writeByte(this.w ? (byte) 1 : 0);
        parcel.writeLong(this.x);
        parcel.writeInt(p == null ? -1 : getLocationProtocol().ordinal());
        parcel.writeInt(this.z == null ? -1 : this.z.ordinal());
        parcel.writeByte(A ? (byte) 1 : 0);
        parcel.writeFloat(this.B);
        if (this.C != null) {
            i3 = this.C.ordinal();
        }
        parcel.writeInt(i3);
        if (!OPEN_ALWAYS_SCAN_WIFI) {
            i4 = 0;
        }
        parcel.writeInt(i4);
        parcel.writeLong(this.y);
    }
}
