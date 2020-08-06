package android.taobao.windvane.monitor;

public class WVMonitorService {
    private static WVConfigMonitorInterface configMonitorInterface;
    private static WVErrorMonitorInterface errorMonitor;
    private static WVJSBrdigeMonitorInterface jsBridgeMonitor;
    private static WVPackageMonitorInterface packageMonitorInterface;
    private static WVPerformanceMonitorInterface performanceMonitor;

    public static WVConfigMonitorInterface getConfigMonitor() {
        return configMonitorInterface;
    }

    public static void registerConfigMonitor(WVConfigMonitorInterface configMonitorInterface2) {
        configMonitorInterface = configMonitorInterface2;
    }

    public static WVPerformanceMonitorInterface getPerformanceMonitor() {
        return performanceMonitor;
    }

    public static void registerPerformanceMonitor(WVPerformanceMonitorInterface performanceMonitor2) {
        performanceMonitor = performanceMonitor2;
    }

    public static WVErrorMonitorInterface getErrorMonitor() {
        return errorMonitor;
    }

    public static void registerErrorMonitor(WVErrorMonitorInterface errorMonitor2) {
        errorMonitor = errorMonitor2;
    }

    public static WVJSBrdigeMonitorInterface getJsBridgeMonitor() {
        return jsBridgeMonitor;
    }

    public static void registerJsBridgeMonitor(WVJSBrdigeMonitorInterface jsBridgeMonitor2) {
        jsBridgeMonitor = jsBridgeMonitor2;
    }

    public static WVPackageMonitorInterface getPackageMonitorInterface() {
        return packageMonitorInterface;
    }

    public static void registerPackageMonitorInterface(WVPackageMonitorInterface packageMonitorInterface2) {
        packageMonitorInterface = packageMonitorInterface2;
    }
}
