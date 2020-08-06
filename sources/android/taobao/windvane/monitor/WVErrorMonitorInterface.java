package android.taobao.windvane.monitor;

public interface WVErrorMonitorInterface {
    void didOccurJSError(String str, String str2, String str3, String str4);

    void didOccurNativeError(String str, int i, String str2);
}
