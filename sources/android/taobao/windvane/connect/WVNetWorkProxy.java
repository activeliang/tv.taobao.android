package android.taobao.windvane.connect;

public class WVNetWorkProxy {
    private static WVNetWorkProxy mConnectManager = null;
    private WVNetWorkProxyInterface mNetWorkProxy = null;

    public static synchronized WVNetWorkProxy getInstance() {
        WVNetWorkProxy wVNetWorkProxy;
        synchronized (WVNetWorkProxy.class) {
            if (mConnectManager == null) {
                mConnectManager = new WVNetWorkProxy();
            }
            wVNetWorkProxy = mConnectManager;
        }
        return wVNetWorkProxy;
    }

    public WVNetWorkProxyInterface getNetWorkProxy() {
        return this.mNetWorkProxy;
    }

    public void registerNetWork(WVNetWorkProxyInterface NetWorkProxy) {
        this.mNetWorkProxy = NetWorkProxy;
    }
}
