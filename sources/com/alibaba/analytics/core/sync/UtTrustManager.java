package com.alibaba.analytics.core.sync;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

class UtTrustManager implements X509TrustManager {
    private static final String TAG = "X509TrustManager";
    private static TrustManager[] trustManagers = null;

    UtTrustManager() {
    }

    static synchronized TrustManager[] getTrustManagers() {
        TrustManager[] trustManagerArr;
        synchronized (UtTrustManager.class) {
            if (trustManagers == null) {
                trustManagers = new TrustManager[]{new UtTrustManager()};
            }
            trustManagerArr = trustManagers;
        }
        return trustManagerArr;
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (chain == null || chain.length <= 0) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate array is null");
        }
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init((KeyStore) null);
            if (tmf != null && tmf.getTrustManagers() != null) {
                TrustManager[] arr$ = tmf.getTrustManagers();
                int len$ = arr$.length;
                int i$ = 0;
                while (i$ < len$) {
                    try {
                        ((X509TrustManager) arr$[i$]).checkServerTrusted(chain, authType);
                        i$++;
                    } catch (CertificateException e) {
                        Throwable t = e;
                        while (t != null) {
                            if (!(t instanceof CertificateExpiredException) && !(t instanceof CertificateNotYetValidException)) {
                                t = t.getCause();
                            } else {
                                return;
                            }
                        }
                        throw e;
                    }
                }
            }
        } catch (NoSuchAlgorithmException e2) {
            throw new CertificateException(e2);
        } catch (KeyStoreException e3) {
            throw new CertificateException(e3);
        }
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
