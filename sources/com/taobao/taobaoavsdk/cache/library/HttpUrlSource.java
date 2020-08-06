package com.taobao.taobaoavsdk.cache.library;

import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import anet.channel.request.Request;
import anetwork.channel.Network;
import anetwork.channel.Request;
import anetwork.channel.aidl.Connection;
import anetwork.channel.degrade.DegradableNetwork;
import anetwork.channel.entity.RequestImpl;
import com.alibaba.sdk.android.oss.common.utils.HttpHeaders;
import com.taobao.taobaoavsdk.cache.ApplicationUtils;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUrlSource implements Source {
    private static final int MAX_EXTEND_DATA_REQUIRE = 1048576;
    private static final int MAX_REDIRECTS = 5;
    private String cdnIp;
    private volatile ConnectionProxy connection;
    private volatile InputStreamProxy inputStream;
    private volatile int length;
    private volatile String mime;
    private IMimeCache mimeCache;
    private Network network;
    private String playToken;
    private String statisticData;
    public String url;
    private boolean useNet;
    private String userAgent;

    public HttpUrlSource(String url2) {
        this((IMimeCache) null, url2, (String) null, false, "", "", Integer.MIN_VALUE);
    }

    public HttpUrlSource(IMimeCache mimeCache2, String url2, String userAgent2, boolean useNewNet, String playToken2, String cdnIp2, int length2) {
        this(mimeCache2, url2, userAgent2, ProxyCacheUtils.getSupposablyMime(url2), useNewNet, playToken2, cdnIp2, length2);
    }

    public HttpUrlSource(HttpUrlSource source) {
        this.length = Integer.MIN_VALUE;
        this.url = source.url;
        this.mime = source.mime;
        this.length = source.length;
        this.userAgent = source.userAgent;
        this.useNet = source.useNet;
        if (!this.useNet || ApplicationUtils.sApplication == null) {
            this.useNet = false;
        } else {
            this.network = new DegradableNetwork(ApplicationUtils.sApplication);
        }
        this.playToken = source.playToken;
        this.mimeCache = source.mimeCache;
        this.cdnIp = source.cdnIp;
    }

    public HttpUrlSource(IMimeCache mimeCache2, String url2, String userAgent2, String mime2, boolean useNewNet, String playToken2, String cdnIp2, int length2) {
        this.length = Integer.MIN_VALUE;
        this.url = (String) Preconditions.checkNotNull(url2);
        this.mime = mime2;
        this.userAgent = userAgent2;
        this.useNet = useNewNet;
        this.length = length2;
        if (!this.useNet || ApplicationUtils.sApplication == null) {
            this.useNet = false;
        } else {
            this.network = new DegradableNetwork(ApplicationUtils.sApplication);
        }
        this.playToken = playToken2;
        this.mimeCache = mimeCache2;
        this.cdnIp = cdnIp2;
    }

    public synchronized int length() throws ProxyCacheException {
        if (this.length == Integer.MIN_VALUE) {
            loadMimeCache();
        }
        if (this.length == Integer.MIN_VALUE) {
            fetchContentInfo();
        }
        return this.length;
    }

    public void open(int offset, boolean withoutCache) throws ProxyCacheException {
        try {
            if (this.useNet) {
                this.connection = new ConnectionProxy(openConnection1(offset, -1, withoutCache));
                if (this.connection.getResponseCode() < 0) {
                    throw new ProxyCacheException("Error opening connection for " + this.url + " with offset " + offset + " error");
                }
            } else {
                this.connection = new ConnectionProxy(openConnection(offset, -1, withoutCache));
            }
            this.mime = this.connection.getHeaderField("Content-Type");
            this.inputStream = this.connection.getInputStream();
        } catch (Exception e) {
            throw new ProxyCacheException("Error opening connection for " + this.url + " with offset " + offset + " error message:" + e.getMessage(), e);
        }
    }

    private int readSourceAvailableBytes(ConnectionProxy connection2, int offset, int responseCode) throws Exception {
        int contentLength = connection2.getHeaderFieldInt("Content-Length", -1);
        if (responseCode == 200) {
            return contentLength;
        }
        return responseCode == 206 ? contentLength + offset : this.length;
    }

    public synchronized void close() throws ProxyCacheException {
        if (this.inputStream != null) {
            try {
                this.inputStream.close();
                this.inputStream = null;
            } catch (Exception e) {
                throw new ProxyCacheException("Error disconnecting HttpUrlConnection", e);
            } catch (Exception e2) {
                Log.e("HttpUrlSource", " HttpUrlSource inputStream close error:" + e2.getMessage());
            }
        }
        if (this.connection != null) {
            this.connection.disconnect();
            this.statisticData = "playToken=" + this.playToken + "," + this.connection.getStatisticData() + ",url=" + this.url;
            this.connection = null;
        }
        return;
    }

    public int read(byte[] buffer) throws ProxyCacheException {
        if (this.inputStream == null) {
            throw new ProxyCacheException("Error reading data from " + this.url + ": connection is absent!");
        }
        try {
            return this.inputStream.read(buffer);
        } catch (InterruptedIOException e1) {
            throw new InterruptedProxyCacheException("Reading source " + this.url + " is interrupted", e1);
        } catch (Exception e) {
            throw new ProxyCacheException("Error reading data from " + this.url, e);
        }
    }

    private void fetchContentInfo() throws ProxyCacheException {
        ConnectionProxy urlConnection;
        ConnectionProxy urlConnection2 = null;
        try {
            if (this.useNet) {
                urlConnection = new ConnectionProxy(getConnectionHead1(10000));
            } else {
                urlConnection = new ConnectionProxy(getConnectionHead(10000));
            }
            this.mime = urlConnection.getHeaderField("Content-Type");
            this.length = urlConnection.getHeaderFieldInt("Content-Length", -1);
            putMimeCache();
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                    this.statisticData = "playToken=" + this.playToken + "," + urlConnection.getStatisticData() + ",url=" + this.url;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            if (urlConnection2 != null) {
                try {
                    urlConnection2.disconnect();
                    this.statisticData = "playToken=" + this.playToken + "," + urlConnection2.getStatisticData() + ",url=" + this.url;
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (urlConnection2 != null) {
                try {
                    urlConnection2.disconnect();
                    this.statisticData = "playToken=" + this.playToken + "," + urlConnection2.getStatisticData() + ",url=" + this.url;
                } catch (Exception e4) {
                    e4.printStackTrace();
                }
            }
            throw th;
        }
    }

    private void putMimeCache() {
        if (this.mimeCache != null) {
            this.mimeCache.putMime(this.url, this.length, this.mime);
        }
    }

    private void loadMimeCache() {
        UrlMime urlMime;
        if (this.mimeCache != null && (urlMime = this.mimeCache.getMime(this.url)) != null && !TextUtils.isEmpty(urlMime.getMime()) && urlMime.getLength() != Integer.MIN_VALUE) {
            this.mime = urlMime.getMime();
            this.length = urlMime.getLength();
        }
    }

    private Connection openConnection1(int offset, int timeout, boolean withoutCache) throws IOException, ProxyCacheException, RemoteException {
        Connection connection2;
        boolean redirected;
        int redirectCount = 0;
        String url2 = this.url;
        if (this.network == null) {
            this.network = new DegradableNetwork(ApplicationUtils.sApplication);
        }
        do {
            Request request = new RequestImpl(url2);
            if (offset < 0) {
                offset = 0;
            }
            if (!withoutCache) {
                int end = offset + 1048576 >= length() ? -1 : offset + 1048576;
                if (end < 0) {
                    request.addHeader(HttpHeaders.RANGE, "bytes=" + offset + "-");
                } else {
                    request.addHeader(HttpHeaders.RANGE, "bytes=" + offset + "-" + end);
                }
            } else if (offset > 0) {
                request.addHeader(HttpHeaders.RANGE, "bytes=" + offset + "-");
            }
            if (!TextUtils.isEmpty(this.userAgent)) {
                request.addHeader(HttpHeaders.USER_AGENT, this.userAgent);
            }
            if (timeout > 0) {
                request.setConnectTimeout(timeout);
                request.setReadTimeout(timeout);
                request.setFollowRedirects(true);
            }
            connection2 = this.network.getConnection(request, (Object) null);
            int code = connection2.getStatusCode();
            redirected = code == 301 || code == 302 || code == 303;
            if (redirected) {
                url2 = new ConnectionProxy(connection2).getHeaderField("Location");
                this.url = url2;
                redirectCount++;
                connection2.cancel();
            }
            if (redirectCount > 5) {
                throw new ProxyCacheException("Too many redirects: " + redirectCount);
            }
        } while (redirected);
        return connection2;
    }

    private Connection getConnectionHead1(int timeout) throws IOException, ProxyCacheException, RemoteException {
        Connection connection2;
        boolean redirected;
        int redirectCount = 0;
        if (this.network == null) {
            this.network = new DegradableNetwork(ApplicationUtils.sApplication);
        }
        do {
            Request request = new RequestImpl(this.url);
            request.setMethod(Request.Method.HEAD);
            if (timeout > 0) {
                request.setConnectTimeout(timeout);
                request.setReadTimeout(timeout);
            }
            if (!TextUtils.isEmpty(this.userAgent)) {
                request.addHeader(HttpHeaders.USER_AGENT, this.userAgent);
            }
            connection2 = this.network.getConnection(request, (Object) null);
            int code = connection2.getStatusCode();
            redirected = code == 301 || code == 302 || code == 303;
            if (redirected) {
                redirectCount++;
                connection2.cancel();
            }
            if (redirectCount > 5) {
                throw new ProxyCacheException("Too many redirects: " + redirectCount);
            }
        } while (redirected);
        return connection2;
    }

    private HttpURLConnection openConnection(int offset, int timeout, boolean withoutCache) throws IOException, ProxyCacheException {
        HttpURLConnection connection2;
        boolean redirected;
        int redirectCount = 0;
        String url2 = this.url;
        do {
            Uri uri = Uri.parse(url2);
            if (!TextUtils.isEmpty(this.cdnIp)) {
                url2 = url2.replaceFirst(uri.getHost(), this.cdnIp);
            }
            connection2 = (HttpURLConnection) new URL(url2).openConnection();
            if (offset < 0) {
                offset = 0;
            }
            if (!withoutCache) {
                int end = offset + 1048576 >= length() ? -1 : offset + 1048576;
                if (end < 0) {
                    connection2.setRequestProperty(HttpHeaders.RANGE, "bytes=" + offset + "-");
                } else {
                    connection2.setRequestProperty(HttpHeaders.RANGE, "bytes=" + offset + "-" + end);
                }
            } else if (offset > 0) {
                connection2.setRequestProperty(HttpHeaders.RANGE, "bytes=" + offset + "-");
            }
            if (timeout > 0) {
                connection2.setConnectTimeout(timeout);
                connection2.setReadTimeout(timeout);
            }
            if (!TextUtils.isEmpty(this.cdnIp)) {
                connection2.setRequestProperty("Host", uri.getHost());
            }
            if (!TextUtils.isEmpty(this.userAgent)) {
                connection2.setRequestProperty(HttpHeaders.USER_AGENT, this.userAgent);
            }
            int code = connection2.getResponseCode();
            redirected = code == 301 || code == 302 || code == 303;
            if (redirected) {
                url2 = connection2.getHeaderField("Location");
                this.url = url2;
                redirectCount++;
                connection2.disconnect();
            }
            if (redirectCount > 5) {
                throw new ProxyCacheException("Too many redirects: " + redirectCount);
            }
        } while (redirected);
        return connection2;
    }

    private HttpURLConnection getConnectionHead(int timeout) throws IOException, ProxyCacheException {
        HttpURLConnection connection2;
        boolean redirected;
        int redirectCount = 0;
        String url2 = this.url;
        do {
            Uri uri = Uri.parse(url2);
            if (!TextUtils.isEmpty(this.cdnIp)) {
                url2 = url2.replaceFirst(uri.getHost(), this.cdnIp);
            }
            connection2 = (HttpURLConnection) new URL(url2).openConnection();
            connection2.setRequestMethod(Request.Method.HEAD);
            if (timeout > 0) {
                connection2.setConnectTimeout(timeout);
                connection2.setReadTimeout(timeout);
            }
            if (!TextUtils.isEmpty(this.cdnIp)) {
                connection2.setRequestProperty("Host", uri.getHost());
            }
            if (!TextUtils.isEmpty(this.userAgent)) {
                connection2.setRequestProperty(HttpHeaders.USER_AGENT, this.userAgent);
            }
            int code = connection2.getResponseCode();
            redirected = code == 301 || code == 302 || code == 303;
            if (redirected) {
                url2 = connection2.getHeaderField("Location");
                this.url = url2;
                redirectCount++;
                connection2.disconnect();
            }
            if (redirectCount > 5) {
                throw new ProxyCacheException("Too many redirects: " + redirectCount);
            }
        } while (redirected);
        return connection2;
    }

    public synchronized String getMime() throws ProxyCacheException {
        if (TextUtils.isEmpty(this.mime)) {
            loadMimeCache();
        }
        if (TextUtils.isEmpty(this.mime)) {
            fetchContentInfo();
        }
        return this.mime;
    }

    public String toString() {
        return "HttpUrlSource{url='" + this.url + "}";
    }

    public String getStatisticData() {
        return this.statisticData;
    }
}
