package android.taobao.windvane.packageapp;

public class WVPackageAppService {
    private static WVPackageAppConfigInterface wvPackageApp;

    public static WVPackageAppConfigInterface getWvPackageAppConfig() {
        return wvPackageApp;
    }

    public static void registerWvPackageAppConfig(WVPackageAppConfigInterface wvPackageApp2) {
        wvPackageApp = wvPackageApp2;
    }
}
