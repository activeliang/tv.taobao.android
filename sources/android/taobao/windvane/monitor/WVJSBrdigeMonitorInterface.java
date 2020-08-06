package android.taobao.windvane.monitor;

public interface WVJSBrdigeMonitorInterface {
    void didCallAtURL(String str, String str2, String str3);

    void didCallBackAtURL(String str, String str2, String str3, String str4);

    void didFireEvent(String str, String str2);

    void didOccurError(String str, String str2, String str3, String str4);
}
