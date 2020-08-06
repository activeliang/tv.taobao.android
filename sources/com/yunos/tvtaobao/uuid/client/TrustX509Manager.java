package com.yunos.tvtaobao.uuid.client;

import android.content.Context;
import com.yunos.tvtaobao.uuid.utils.Logger;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class TrustX509Manager implements X509TrustManager {
    private boolean mUserEnv = true;
    private X509Certificate mYunOSCert = null;

    public TrustX509Manager(Context context) throws Exception {
        InputStream ins = null;
        CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
        try {
            Logger.log_d("open verisign.cer!");
            ins = context.getAssets().open("uuid.cer");
            this.mYunOSCert = (X509Certificate) cerFactory.generateCertificate(ins);
            Logger.log_d("X.509");
        } finally {
            if (ins != null) {
                ins.close();
            }
        }
    }

    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String paramString) throws CertificateException {
        String errMsg = "certification error";
        Logger.log_d("checkServerTrusted 0 !");
        int subStartIndex = chain.length - 2;
        Logger.log_d("checkServerTrusted 1 !" + subStartIndex);
        if (subStartIndex < 0) {
            Logger.log_d("checkServerTrusted error 0 !");
            throw new CertificateException("X509Certificate[] chain too short");
        }
        Logger.log_d("checkServerTrusted 2 !");
        PublicKey publicKey = this.mYunOSCert.getPublicKey();
        int index = subStartIndex;
        while (index >= 0) {
            Logger.log_d("checkServerTrusted 3 !");
            X509Certificate cert = chain[index];
            cert.checkValidity();
            if (publicKey != null) {
                try {
                    cert.verify(publicKey);
                    Logger.log_d("verify ok !");
                    if (index != 0) {
                        publicKey = cert.getPublicKey();
                        index--;
                    } else {
                        return;
                    }
                } catch (InvalidKeyException e) {
                    errMsg = "InvalidKeyException:" + e.getMessage();
                } catch (NoSuchAlgorithmException e2) {
                    errMsg = "NoSuchAlgorithmException:" + e2.getMessage();
                } catch (NoSuchProviderException e3) {
                    errMsg = "NoSuchProviderException:" + e3.getMessage();
                } catch (SignatureException e4) {
                    errMsg = "SignatureException:" + e4.getMessage();
                } catch (CertificateException e5) {
                    errMsg = "CertificateException:" + e5.getMessage();
                } catch (Exception e6) {
                    errMsg = "Other Certificate exception:" + e6.getMessage();
                }
            } else {
                throw new CertificateException("no publicKey to verify");
            }
        }
        Logger.log_d("throw exception: " + errMsg);
        throw new CertificateException(errMsg);
    }

    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
