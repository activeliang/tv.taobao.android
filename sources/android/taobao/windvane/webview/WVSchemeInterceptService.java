package android.taobao.windvane.webview;

public class WVSchemeInterceptService {
    private static WVSchemeIntercepterInterface mIntercepter = null;

    public static void registerWVURLintercepter(WVSchemeIntercepterInterface intercepter) {
        mIntercepter = intercepter;
    }

    public static WVSchemeIntercepterInterface getWVSchemeIntercepter() {
        return mIntercepter;
    }
}
