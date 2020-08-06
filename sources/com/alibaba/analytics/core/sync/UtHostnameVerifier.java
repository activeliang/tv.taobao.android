package com.alibaba.analytics.core.sync;

import android.text.TextUtils;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

class UtHostnameVerifier implements HostnameVerifier {
    public String bizHost;

    public UtHostnameVerifier(String bizHost2) {
        this.bizHost = bizHost2;
    }

    public boolean verify(String hostname, SSLSession session) {
        return HttpsURLConnection.getDefaultHostnameVerifier().verify(this.bizHost, session);
    }

    public boolean equals(Object o) {
        if (TextUtils.isEmpty(this.bizHost) || !(o instanceof UtHostnameVerifier)) {
            return false;
        }
        String thatHost = ((UtHostnameVerifier) o).bizHost;
        if (!TextUtils.isEmpty(thatHost)) {
            return this.bizHost.equals(thatHost);
        }
        return false;
    }

    public String getHost() {
        return this.bizHost;
    }
}
