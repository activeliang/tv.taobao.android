package com.ta.audid.upload;

import android.net.SSLCertificateSocketFactory;
import android.os.Build;
import com.ta.audid.utils.UtdidLogger;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class UtdidSslSocketFactory extends SSLSocketFactory {
    private String peerHost;
    private Method setHostNameMethod = null;

    public UtdidSslSocketFactory(String host) {
        this.peerHost = host;
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
        if (this.peerHost == null) {
            this.peerHost = host;
        }
        UtdidLogger.sd("", "host", this.peerHost, "port", Integer.valueOf(port), "autoClose", Boolean.valueOf(autoClose));
        InetAddress address = plainSocket.getInetAddress();
        if (autoClose) {
            plainSocket.close();
        }
        SSLCertificateSocketFactory sslSocketFactory = (SSLCertificateSocketFactory) SSLCertificateSocketFactory.getDefault(0);
        SSLSocket ssl = (SSLSocket) sslSocketFactory.createSocket(address, port);
        ssl.setEnabledProtocols(ssl.getSupportedProtocols());
        if (Build.VERSION.SDK_INT >= 17) {
            sslSocketFactory.setHostname(ssl, this.peerHost);
        } else {
            try {
                if (this.setHostNameMethod == null) {
                    this.setHostNameMethod = ssl.getClass().getMethod("setHostname", new Class[]{String.class});
                    this.setHostNameMethod.setAccessible(true);
                }
                this.setHostNameMethod.invoke(ssl, new Object[]{this.peerHost});
            } catch (Exception e) {
                UtdidLogger.d("", "SNI not useable", null, e);
            }
        }
        UtdidLogger.sd("", "SSLSession PeerHost", ssl.getSession().getPeerHost());
        return ssl;
    }
}
