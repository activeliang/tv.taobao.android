package com.yunos.tvtaobao.uuid.client;

import android.content.Context;
import com.yunos.tvtaobao.uuid.client.exception.GetServerResponseException;
import com.yunos.tvtaobao.uuid.client.exception.SendXmlCommException;
import com.yunos.tvtaobao.uuid.utils.Logger;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class UUIDHttps {
    private static final int TIMEOUT = 20000;
    private Context mContext;

    public UUIDHttps(Context context) {
        this.mContext = context;
    }

    public InputStream httpXmlCommunication(String url, String send_xml, boolean zip) throws SendXmlCommException, GetServerResponseException {
        return httpXmlCommunication(url, send_xml, (HttpHost) null, zip);
    }

    public InputStream httpXmlCommunication(String url, String send_xml, HttpHost proxy, boolean zip) throws SendXmlCommException, GetServerResponseException {
        HttpsURLConnection httpsConnection;
        byte[] send_byte;
        try {
            URL httpUrl = new URL(url);
            if (proxy == null) {
                Logger.log_d("httpXmlCommunication with no proxy");
                httpsConnection = (HttpsURLConnection) httpUrl.openConnection();
            } else {
                Logger.log_d("httpXmlCommunication with proxy: " + proxy.toHostString());
                httpsConnection = (HttpsURLConnection) httpUrl.openConnection(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy.getHostName(), proxy.getPort())));
            }
            httpsConnection.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            httpsConnection.setConnectTimeout(20000);
            httpsConnection.setReadTimeout(20000);
            httpsConnection.setDoOutput(true);
            httpsConnection.setDoInput(true);
            httpsConnection.setRequestMethod("POST");
            httpsConnection.setUseCaches(false);
            httpsConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (zip) {
                send_byte = GZipUtil.compress(send_xml.getBytes("utf-8"));
                Logger.log_d("zipd");
            } else {
                send_byte = send_xml.getBytes("utf-8");
                Logger.log_d("not zip");
            }
            Logger.log_d("start ...");
            httpsConnection.connect();
            Logger.log_d("stop ...");
            DataOutputStream out = new DataOutputStream(httpsConnection.getOutputStream());
            out.write(send_byte);
            out.flush();
            out.close();
            try {
                if (httpsConnection.getResponseCode() == 200) {
                    return httpsConnection.getInputStream();
                }
                return null;
            } catch (IOException e) {
                Logger.loge("Get Server Respond erro", e);
                e.printStackTrace();
                throw new GetServerResponseException();
            }
        } catch (Exception e2) {
            Logger.loge("Send Xml Communication erro");
            e2.printStackTrace();
            throw new SendXmlCommException();
        }
    }
}
