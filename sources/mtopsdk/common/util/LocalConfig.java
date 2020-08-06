package mtopsdk.common.util;

public class LocalConfig {
    private static final String TAG = "mtopsdk.LocalConfig";
    public boolean enableBizErrorCodeMapping;
    public boolean enableErrorCodeMapping;
    public boolean enableProperty;
    @Deprecated
    public boolean enableRemoteNetworkService;
    public boolean enableSpdy;
    public boolean enableSsl;
    @Deprecated
    public boolean enableUnit;

    private static class LocalConfigInstanceHolder {
        /* access modifiers changed from: private */
        public static LocalConfig instance = new LocalConfig();

        private LocalConfigInstanceHolder() {
        }
    }

    public static LocalConfig getInstance() {
        return LocalConfigInstanceHolder.instance;
    }

    private LocalConfig() {
        this.enableErrorCodeMapping = true;
        this.enableBizErrorCodeMapping = true;
        this.enableSpdy = true;
        this.enableUnit = true;
        this.enableSsl = true;
        this.enableProperty = true;
        this.enableRemoteNetworkService = true;
    }
}
