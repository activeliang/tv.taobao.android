package anet.channel.util;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpSslUtil {
    public static final HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = new AllowAllHostnameVerifier();
    public static final SSLSocketFactory TRUST_ALL_SSL_SOCKET_FACTORY = SSLTrustAllSocketFactory.getSocketFactory();
    static SSLSocketFactory sslSocketFactory;
    static HostnameVerifier verifier;

    public static SSLSocketFactory getSSLSocketFactory() {
        return sslSocketFactory;
    }

    public static void setSslSocketFactory(SSLSocketFactory sslSocketFactory2) {
        sslSocketFactory = sslSocketFactory2;
    }

    public static HostnameVerifier getHostnameVerifier() {
        return verifier;
    }

    public static void setHostnameVerifier(HostnameVerifier verifier2) {
        verifier = verifier2;
    }

    private static class AllowAllHostnameVerifier implements HostnameVerifier {
        private AllowAllHostnameVerifier() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static class SSLTrustAllSocketFactory {
        private SSLTrustAllSocketFactory() {
        }

        private static class SSLTrustAllManager implements X509TrustManager {
            private SSLTrustAllManager() {
            }

            public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        }

        public static SSLSocketFactory getSocketFactory() {
            try {
                SSLContext context = SSLContext.getInstance("TLS");
                context.init((KeyManager[]) null, new TrustManager[]{new SSLTrustAllManager()}, (SecureRandom) null);
                return context.getSocketFactory();
            } catch (Throwable e) {
                ALog.w("awcn.SSLTrustAllSocketFactory", "getSocketFactory error :" + e.getMessage(), (String) null, new Object[0]);
                e.printStackTrace();
                return null;
            }
        }
    }
}
