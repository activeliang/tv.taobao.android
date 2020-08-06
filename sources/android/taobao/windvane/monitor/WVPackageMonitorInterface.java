package android.taobao.windvane.monitor;

import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;

public interface WVPackageMonitorInterface {
    public static final String CONFIG_CLOSED_FAILED = "23";
    public static final String CREATE_STREAM_FAILED = "11";
    public static final String FORCE_ONLINE_FAILED = "22";
    public static final String FORCE_UPDATE_FAILED = "21";
    public static final String MAPPING_URL_MATCH_FAILED = "14";
    public static final String MAPPING_URL_NULL_FAILED = "13";
    public static final String NOT_INSTALL_FAILED = "20";
    public static final String READ_LOCAL_FILE_FAILED = "12";
    public static final String SECURITY_FAILED = "10";
    public static final String UNKNOWN_FAILED = "9";

    void commitPackageQueueInfo(String str, long j, long j2);

    void commitPackageUpdateStartInfo(long j, long j2);

    void commitPackageVisitError(String str, String str2, String str3);

    void commitPackageVisitInfo(String str, String str2, long j, long j2, long j3, long j4, long j5);

    void commitPackageVisitSuccess(String str, long j);

    void onStartCleanAppCache(long j, int i, int i2, int i3, float f, int i4, int i5, float f2, int i6);

    void packageApp(ZipAppInfo zipAppInfo, String str, String str2, String str3, boolean z, long j, long j2, int i, String str4, boolean z2, long j3);

    void uploadDiffTimeTime(long j);

    void uploadStartAppTime(long j);
}
