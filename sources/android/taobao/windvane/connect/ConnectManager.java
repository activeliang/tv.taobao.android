package android.taobao.windvane.connect;

import android.taobao.windvane.thread.WVThreadPool;

public class ConnectManager {
    private static ConnectManager mConnectManager = null;

    public static synchronized ConnectManager getInstance() {
        ConnectManager connectManager;
        synchronized (ConnectManager.class) {
            if (mConnectManager == null) {
                mConnectManager = new ConnectManager();
            }
            connectManager = mConnectManager;
        }
        return connectManager;
    }

    private ConnectManager() {
    }

    public void connect(final String url, final HttpConnectListener<HttpResponse> listener) {
        if (url != null) {
            WVThreadPool.getInstance().execute(new Runnable() {
                public void run() {
                    try {
                        new HttpConnector().syncConnect(new HttpRequest(url), listener);
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    public void connect(final HttpRequest request, final HttpConnectListener<HttpResponse> listener) {
        if (request != null) {
            WVThreadPool.getInstance().execute(new Runnable() {
                public void run() {
                    new HttpConnector().syncConnect(request, listener);
                }
            });
        }
    }
}
