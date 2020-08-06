package com.alibaba.analytics.core.sync;

import android.net.SSLCertificateSocketFactory;
import android.net.SSLSessionCache;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.utils.Logger;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

class UtSslSocketFactory extends SSLSocketFactory {
    private static final String TAG = "UtSslSocketFactory";
    private String bizHost;
    private Method setHostNameMethod = null;

    public UtSslSocketFactory(String host) {
        this.bizHost = host;
    }

    public Socket createSocket() throws IOException {
        return null;
    }

    public Socket createSocket(String host, int port) throws IOException {
        return null;
    }

    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        return null;
    }

    public Socket createSocket(InetAddress host, int port) throws IOException {
        return null;
    }

    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return null;
    }

    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    public String[] getSupportedCipherSuites() {
        return new String[0];
    }

    public Socket createSocket(Socket plainSocket, String host, int port, boolean autoClose) throws IOException {
        SSLSocket ssl;
        Logger.d(TAG, "bizHost", this.bizHost, "host", host, "port", Integer.valueOf(port), "autoClose", Boolean.valueOf(autoClose));
        if (TextUtils.isEmpty(this.bizHost)) {
            throw new IOException("SDK set empty bizHost");
        }
        Logger.d(TAG, "customized createSocket. host: " + this.bizHost);
        try {
            SSLCertificateSocketFactory sslSocketFactory = (SSLCertificateSocketFactory) SSLCertificateSocketFactory.getDefault(10000, new SSLSessionCache(Variables.getInstance().getContext()));
            sslSocketFactory.setTrustManagers(UtTrustManager.getTrustManagers());
            ssl = (SSLSocket) sslSocketFactory.createSocket(plainSocket, this.bizHost, port, autoClose);
            ssl.setEnabledProtocols(ssl.getSupportedProtocols());
            if (Build.VERSION.SDK_INT < 17) {
                if (this.setHostNameMethod == null) {
                    this.setHostNameMethod = ssl.getClass().getMethod("setHostname", new Class[]{String.class});
                    this.setHostNameMethod.setAccessible(true);
                }
                this.setHostNameMethod.invoke(ssl, new Object[]{this.bizHost});
            } else {
                sslSocketFactory.setUseSessionTickets(ssl, true);
                sslSocketFactory.setHostname(ssl, this.bizHost);
            }
        } catch (Exception e) {
            Logger.d("", "SNI not useable", e);
        } catch (Throwable e2) {
            throw new IOException("createSocket exception: " + e2);
        }
        ssl.startHandshake();
        Logger.d(TAG, "customized createSocket end");
        return ssl;
    }

    public boolean equals(Object o) {
        if (TextUtils.isEmpty(this.bizHost) || !(o instanceof UtSslSocketFactory)) {
            return false;
        }
        String thatHost = ((UtSslSocketFactory) o).bizHost;
        if (!TextUtils.isEmpty(thatHost)) {
            return this.bizHost.equals(thatHost);
        }
        return false;
    }

    public String getHost() {
        return this.bizHost;
    }
}
