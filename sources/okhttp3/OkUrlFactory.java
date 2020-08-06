package okhttp3;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;
import okhttp3.internal.URLFilter;
import okhttp3.internal.huc.OkHttpURLConnection;
import okhttp3.internal.huc.OkHttpsURLConnection;

public final class OkUrlFactory implements URLStreamHandlerFactory, Cloneable {
    private OkHttpClient client;
    private URLFilter urlFilter;

    public OkUrlFactory(OkHttpClient client2) {
        this.client = client2;
    }

    public OkHttpClient client() {
        return this.client;
    }

    public OkUrlFactory setClient(OkHttpClient client2) {
        this.client = client2;
        return this;
    }

    /* access modifiers changed from: package-private */
    public void setUrlFilter(URLFilter filter) {
        this.urlFilter = filter;
    }

    public OkUrlFactory clone() {
        return new OkUrlFactory(this.client);
    }

    public HttpURLConnection open(URL url) {
        return open(url, this.client.proxy());
    }

    /* access modifiers changed from: package-private */
    public HttpURLConnection open(URL url, Proxy proxy) {
        String protocol = url.getProtocol();
        OkHttpClient copy = this.client.newBuilder().proxy(proxy).build();
        if (protocol.equals("http")) {
            return new OkHttpURLConnection(url, copy, this.urlFilter);
        }
        if (protocol.equals("https")) {
            return new OkHttpsURLConnection(url, copy, this.urlFilter);
        }
        throw new IllegalArgumentException("Unexpected protocol: " + protocol);
    }

    public URLStreamHandler createURLStreamHandler(final String protocol) {
        if (protocol.equals("http") || protocol.equals("https")) {
            return new URLStreamHandler() {
                /* access modifiers changed from: protected */
                public URLConnection openConnection(URL url) {
                    return OkUrlFactory.this.open(url);
                }

                /* access modifiers changed from: protected */
                public URLConnection openConnection(URL url, Proxy proxy) {
                    return OkUrlFactory.this.open(url, proxy);
                }

                /* access modifiers changed from: protected */
                public int getDefaultPort() {
                    if (protocol.equals("http")) {
                        return 80;
                    }
                    if (protocol.equals("https")) {
                        return 443;
                    }
                    throw new AssertionError();
                }
            };
        }
        return null;
    }
}
