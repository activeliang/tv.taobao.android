package anet.channel.util;

import android.net.SSLCertificateSocketFactory;
import android.os.Build;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class TlsSniSocketFactory extends SSLSocketFactory {
    private final String TAG = "awcn.TlsSniSocketFactory";
    private String peerHost;
    private Method setHostNameMethod = null;

    public TlsSniSocketFactory(String host) {
        this.peerHost = host;
    }

    public Socket createSocket() throws IOException {
        return null;
    }

    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return null;
    }

    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
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
        if (ALog.isPrintLog(1)) {
            ALog.i("awcn.TlsSniSocketFactory", "customized createSocket", (String) null, "host", this.peerHost);
        }
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
                ALog.w("awcn.TlsSniSocketFactory", "SNI not useable", (String) null, e, new Object[0]);
            }
        }
        SSLSession session = ssl.getSession();
        if (ALog.isPrintLog(1)) {
            ALog.d("awcn.TlsSniSocketFactory", (String) null, (String) null, "SSLSession PeerHost", session.getPeerHost());
        }
        return ssl;
    }
}
