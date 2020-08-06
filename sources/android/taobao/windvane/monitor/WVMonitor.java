package android.taobao.windvane.monitor;

public class WVMonitor {
    public static final int OPTION_ERROR = 2;
    public static final int OPTION_JSBRIDGE = 4;
    public static final int OPTION_PACKAGEAPP = 8;
    public static final int OPTION_PERFORMANCE = 1;

    public static void init() {
        init(15);
        WVMonitorConfigManager.getInstance().init();
    }

    public static void init(int option) {
        WVMonitorImpl wvMonitor = new WVMonitorImpl();
        if ((option & 1) > 0) {
            WVMonitorService.registerPerformanceMonitor(wvMonitor);
            WVMonitorService.registerConfigMonitor(wvMonitor);
        }
        if ((option & 2) > 0) {
            WVMonitorService.registerErrorMonitor(wvMonitor);
        }
        if ((option & 4) > 0) {
            WVMonitorService.registerJsBridgeMonitor(new WVJsMonitor());
        }
        if ((option & 8) > 0) {
            WVMonitorService.registerPackageMonitorInterface(new WVPackageMonitorImpl());
        }
        AppMonitorUtil.init();
    }
}
