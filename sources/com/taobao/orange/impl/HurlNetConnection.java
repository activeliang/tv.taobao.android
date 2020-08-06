package com.taobao.orange.impl;

import android.text.TextUtils;
import com.taobao.orange.inner.INetConnection;
import com.taobao.orange.util.OrangeUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class HurlNetConnection implements INetConnection {
    private HttpURLConnection httpURLConnection;
    private Map<String, String> params;

    public void openConnection(String url) throws IOException {
        String paramsStr = OrangeUtils.encodeQueryParams(this.params, "utf-8");
        StringBuilder sb = new StringBuilder(url);
        if (!TextUtils.isEmpty(paramsStr)) {
            sb.append('?').append(paramsStr);
        }
        this.httpURLConnection = (HttpURLConnection) new URL(sb.toString()).openConnection();
        this.httpURLConnection.setConnectTimeout(5000);
        this.httpURLConnection.setReadTimeout(5000);
        this.httpURLConnection.setUseCaches(false);
        this.httpURLConnection.setDoInput(true);
    }

    public void setMethod(String method) throws ProtocolException {
        if (!TextUtils.isEmpty(method)) {
            this.httpURLConnection.setRequestMethod(method);
            if ("POST".equalsIgnoreCase(method)) {
                this.httpURLConnection.setDoOutput(true);
            }
        }
    }

    public void setParams(Map<String, String> params2) {
        this.params = params2;
    }

    public void addHeader(String key, String value) {
        this.httpURLConnection.addRequestProperty(key, value);
    }

    public void setBody(byte[] body) throws IOException {
        DataOutputStream out = new DataOutputStream(this.httpURLConnection.getOutputStream());
        out.write(body);
        out.flush();
        OrangeUtils.close(out);
    }

    public Map<String, List<String>> getHeadFields() {
        if (this.httpURLConnection == null) {
            return null;
        }
        return this.httpURLConnection.getHeaderFields();
    }

    public int getResponseCode() throws IOException {
        if (this.httpURLConnection == null) {
            return 0;
        }
        return this.httpURLConnection.getResponseCode();
    }

    public String getResponse() throws IOException {
        if (this.httpURLConnection == null) {
            return null;
        }
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            is = this.httpURLConnection.getInputStream();
            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
            try {
                byte[] buffer = new byte[2048];
                while (true) {
                    int length = is.read(buffer);
                    if (length != -1) {
                        bos2.write(buffer, 0, length);
                    } else {
                        String str = new String(bos2.toByteArray(), "utf-8");
                        OrangeUtils.close(is);
                        OrangeUtils.close(bos2);
                        return str;
                    }
                }
            } catch (IOException e) {
                t = e;
                bos = bos2;
                try {
                    throw t;
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Throwable th2) {
                th = th2;
                bos = bos2;
                OrangeUtils.close(is);
                OrangeUtils.close(bos);
                throw th;
            }
        } catch (IOException e2) {
            t = e2;
            throw t;
        }
    }

    public void connect() throws IOException {
        this.httpURLConnection.connect();
    }

    public void disconnect() {
        if (this.httpURLConnection != null) {
            this.httpURLConnection.disconnect();
        }
    }
}
