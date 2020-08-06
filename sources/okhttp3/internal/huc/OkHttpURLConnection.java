package okhttp3.internal.huc;

import anet.channel.request.Request;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.SocketPermission;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.Permission;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import mtopsdk.common.util.SymbolExpUtil;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.Handshake;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Internal;
import okhttp3.internal.JavaNetHeaders;
import okhttp3.internal.URLFilter;
import okhttp3.internal.Version;
import okhttp3.internal.http.HttpDate;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.platform.Platform;
import okio.Buffer;

public final class OkHttpURLConnection extends HttpURLConnection implements Callback {
    private static final Set<String> METHODS = new LinkedHashSet(Arrays.asList(new String[]{Request.Method.OPTION, "GET", Request.Method.HEAD, "POST", Request.Method.PUT, "DELETE", "TRACE", "PATCH"}));
    public static final String RESPONSE_SOURCE = (Platform.get().getPrefix() + "-Response-Source");
    public static final String SELECTED_PROTOCOL = (Platform.get().getPrefix() + "-Selected-Protocol");
    Call call;
    private Throwable callFailure;
    OkHttpClient client;
    boolean connectPending;
    private boolean executed;
    private long fixedContentLength;
    Handshake handshake;
    /* access modifiers changed from: private */
    public final Object lock;
    private final NetworkInterceptor networkInterceptor;
    Response networkResponse;
    Proxy proxy;
    private Headers.Builder requestHeaders;
    private Response response;
    private Headers responseHeaders;
    URLFilter urlFilter;

    public OkHttpURLConnection(URL url, OkHttpClient client2) {
        super(url);
        this.networkInterceptor = new NetworkInterceptor();
        this.requestHeaders = new Headers.Builder();
        this.fixedContentLength = -1;
        this.lock = new Object();
        this.connectPending = true;
        this.client = client2;
    }

    public OkHttpURLConnection(URL url, OkHttpClient client2, URLFilter urlFilter2) {
        this(url, client2);
        this.urlFilter = urlFilter2;
    }

    public void connect() throws IOException {
        if (!this.executed) {
            Call call2 = buildCall();
            this.executed = true;
            call2.enqueue(this);
            synchronized (this.lock) {
                while (this.connectPending && this.response == null && this.callFailure == null) {
                    try {
                        this.lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new InterruptedIOException();
                    }
                }
                if (this.callFailure != null) {
                    throw propagate(this.callFailure);
                }
            }
        }
    }

    public void disconnect() {
        if (this.call != null) {
            this.networkInterceptor.proceed();
            this.call.cancel();
        }
    }

    public InputStream getErrorStream() {
        try {
            Response response2 = getResponse(true);
            if (!HttpHeaders.hasBody(response2) || response2.code() < 400) {
                return null;
            }
            return response2.body().byteStream();
        } catch (IOException e) {
            return null;
        }
    }

    private Headers getHeaders() throws IOException {
        if (this.responseHeaders == null) {
            Response response2 = getResponse(true);
            this.responseHeaders = response2.headers().newBuilder().add(SELECTED_PROTOCOL, response2.protocol().toString()).add(RESPONSE_SOURCE, responseSourceHeader(response2)).build();
        }
        return this.responseHeaders;
    }

    private static String responseSourceHeader(Response response2) {
        if (response2.networkResponse() == null) {
            if (response2.cacheResponse() == null) {
                return "NONE";
            }
            return "CACHE " + response2.code();
        } else if (response2.cacheResponse() == null) {
            return "NETWORK " + response2.code();
        } else {
            return "CONDITIONAL_CACHE " + response2.networkResponse().code();
        }
    }

    public String getHeaderField(int position) {
        try {
            Headers headers = getHeaders();
            if (position < 0 || position >= headers.size()) {
                return null;
            }
            return headers.value(position);
        } catch (IOException e) {
            return null;
        }
    }

    public String getHeaderField(String fieldName) {
        if (fieldName != null) {
            return getHeaders().get(fieldName);
        }
        try {
            return StatusLine.get(getResponse(true)).toString();
        } catch (IOException e) {
            return null;
        }
    }

    public String getHeaderFieldKey(int position) {
        try {
            Headers headers = getHeaders();
            if (position < 0 || position >= headers.size()) {
                return null;
            }
            return headers.name(position);
        } catch (IOException e) {
            return null;
        }
    }

    public Map<String, List<String>> getHeaderFields() {
        try {
            return JavaNetHeaders.toMultimap(getHeaders(), StatusLine.get(getResponse(true)).toString());
        } catch (IOException e) {
            return Collections.emptyMap();
        }
    }

    public Map<String, List<String>> getRequestProperties() {
        if (!this.connected) {
            return JavaNetHeaders.toMultimap(this.requestHeaders.build(), (String) null);
        }
        throw new IllegalStateException("Cannot access request header fields after connection is set");
    }

    public InputStream getInputStream() throws IOException {
        if (!this.doInput) {
            throw new ProtocolException("This protocol does not support input");
        }
        Response response2 = getResponse(false);
        if (response2.code() < 400) {
            return response2.body().byteStream();
        }
        throw new FileNotFoundException(this.url.toString());
    }

    public OutputStream getOutputStream() throws IOException {
        OutputStreamRequestBody requestBody = (OutputStreamRequestBody) buildCall().request().body();
        if (requestBody == null) {
            throw new ProtocolException("method does not support a request body: " + this.method);
        }
        if (requestBody instanceof StreamedRequestBody) {
            connect();
            this.networkInterceptor.proceed();
        }
        if (!requestBody.isClosed()) {
            return requestBody.outputStream();
        }
        throw new ProtocolException("cannot write request body after response has been read");
    }

    public Permission getPermission() throws IOException {
        int hostPort;
        URL url = getURL();
        String hostname = url.getHost();
        if (url.getPort() != -1) {
            hostPort = url.getPort();
        } else {
            hostPort = HttpUrl.defaultPort(url.getProtocol());
        }
        if (usingProxy()) {
            InetSocketAddress proxyAddress = (InetSocketAddress) this.client.proxy().address();
            hostname = proxyAddress.getHostName();
            hostPort = proxyAddress.getPort();
        }
        return new SocketPermission(hostname + SymbolExpUtil.SYMBOL_COLON + hostPort, "connect, resolve");
    }

    public String getRequestProperty(String field) {
        if (field == null) {
            return null;
        }
        return this.requestHeaders.get(field);
    }

    public void setConnectTimeout(int timeoutMillis) {
        this.client = this.client.newBuilder().connectTimeout((long) timeoutMillis, TimeUnit.MILLISECONDS).build();
    }

    public void setInstanceFollowRedirects(boolean followRedirects) {
        this.client = this.client.newBuilder().followRedirects(followRedirects).build();
    }

    public boolean getInstanceFollowRedirects() {
        return this.client.followRedirects();
    }

    public int getConnectTimeout() {
        return this.client.connectTimeoutMillis();
    }

    public void setReadTimeout(int timeoutMillis) {
        this.client = this.client.newBuilder().readTimeout((long) timeoutMillis, TimeUnit.MILLISECONDS).build();
    }

    public int getReadTimeout() {
        return this.client.readTimeoutMillis();
    }

    private Call buildCall() throws IOException {
        if (this.call != null) {
            return this.call;
        }
        this.connected = true;
        if (this.doOutput) {
            if (this.method.equals("GET")) {
                this.method = "POST";
            } else if (!HttpMethod.permitsRequestBody(this.method)) {
                throw new ProtocolException(this.method + " does not support writing");
            }
        }
        if (this.requestHeaders.get(com.alibaba.sdk.android.oss.common.utils.HttpHeaders.USER_AGENT) == null) {
            this.requestHeaders.add(com.alibaba.sdk.android.oss.common.utils.HttpHeaders.USER_AGENT, defaultUserAgent());
        }
        OutputStreamRequestBody requestBody = null;
        if (HttpMethod.permitsRequestBody(this.method)) {
            if (this.requestHeaders.get("Content-Type") == null) {
                this.requestHeaders.add("Content-Type", "application/x-www-form-urlencoded");
            }
            boolean stream = this.fixedContentLength != -1 || this.chunkLength > 0;
            long contentLength = -1;
            String contentLengthString = this.requestHeaders.get("Content-Length");
            if (this.fixedContentLength != -1) {
                contentLength = this.fixedContentLength;
            } else if (contentLengthString != null) {
                contentLength = Long.parseLong(contentLengthString);
            }
            if (stream) {
                requestBody = new StreamedRequestBody(contentLength);
            } else {
                requestBody = new BufferedRequestBody(contentLength);
            }
            requestBody.timeout().timeout((long) this.client.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
        }
        try {
            okhttp3.Request request = new Request.Builder().url(HttpUrl.get(getURL().toString())).headers(this.requestHeaders.build()).method(this.method, requestBody).build();
            if (this.urlFilter != null) {
                this.urlFilter.checkURLPermitted(request.url().url());
            }
            OkHttpClient.Builder clientBuilder = this.client.newBuilder();
            clientBuilder.interceptors().clear();
            clientBuilder.interceptors().add(UnexpectedException.INTERCEPTOR);
            clientBuilder.networkInterceptors().clear();
            clientBuilder.networkInterceptors().add(this.networkInterceptor);
            clientBuilder.dispatcher(new Dispatcher(this.client.dispatcher().executorService()));
            if (!getUseCaches()) {
                clientBuilder.cache((Cache) null);
            }
            Call newCall = clientBuilder.build().newCall(request);
            this.call = newCall;
            return newCall;
        } catch (IllegalArgumentException e) {
            if (Internal.instance.isInvalidHttpUrlHost(e)) {
                UnknownHostException unknownHost = new UnknownHostException();
                unknownHost.initCause(e);
                throw unknownHost;
            }
            MalformedURLException malformedUrl = new MalformedURLException();
            malformedUrl.initCause(e);
            throw malformedUrl;
        }
    }

    private String defaultUserAgent() {
        String agent = System.getProperty("http.agent");
        return agent != null ? toHumanReadableAscii(agent) : Version.userAgent();
    }

    private static String toHumanReadableAscii(String s) {
        int i;
        int i2 = 0;
        int length = s.length();
        while (i2 < length) {
            int c = s.codePointAt(i2);
            if (c <= 31 || c >= 127) {
                Buffer buffer = new Buffer();
                buffer.writeUtf8(s, 0, i2);
                buffer.writeUtf8CodePoint(63);
                int j = i2 + Character.charCount(c);
                while (j < length) {
                    int c2 = s.codePointAt(j);
                    if (c2 <= 31 || c2 >= 127) {
                        i = 63;
                    } else {
                        i = c2;
                    }
                    buffer.writeUtf8CodePoint(i);
                    j += Character.charCount(c2);
                }
                return buffer.readUtf8();
            }
            i2 += Character.charCount(c);
        }
        return s;
    }

    private Response getResponse(boolean networkResponseOnError) throws IOException {
        Response response2;
        synchronized (this.lock) {
            if (this.response != null) {
                response2 = this.response;
            } else if (this.callFailure == null) {
                Call call2 = buildCall();
                this.networkInterceptor.proceed();
                OutputStreamRequestBody requestBody = (OutputStreamRequestBody) call2.request().body();
                if (requestBody != null) {
                    requestBody.outputStream().close();
                }
                if (this.executed) {
                    synchronized (this.lock) {
                        while (this.response == null && this.callFailure == null) {
                            try {
                                this.lock.wait();
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                throw new InterruptedIOException();
                            }
                        }
                    }
                } else {
                    this.executed = true;
                    try {
                        onResponse(call2, call2.execute());
                    } catch (IOException e2) {
                        onFailure(call2, e2);
                    }
                }
                synchronized (this.lock) {
                    if (this.callFailure != null) {
                        throw propagate(this.callFailure);
                    } else if (this.response != null) {
                        response2 = this.response;
                    } else {
                        throw new AssertionError();
                    }
                }
            } else if (!networkResponseOnError || this.networkResponse == null) {
                throw propagate(this.callFailure);
            } else {
                response2 = this.networkResponse;
            }
        }
        return response2;
    }

    public boolean usingProxy() {
        if (this.proxy != null) {
            return true;
        }
        Proxy clientProxy = this.client.proxy();
        if (clientProxy == null || clientProxy.type() == Proxy.Type.DIRECT) {
            return false;
        }
        return true;
    }

    public String getResponseMessage() throws IOException {
        return getResponse(true).message();
    }

    public int getResponseCode() throws IOException {
        return getResponse(true).code();
    }

    public void setRequestProperty(String field, String newValue) {
        if (this.connected) {
            throw new IllegalStateException("Cannot set request property after connection is made");
        } else if (field == null) {
            throw new NullPointerException("field == null");
        } else if (newValue == null) {
            Platform.get().log(5, "Ignoring header " + field + " because its value was null.", (Throwable) null);
        } else {
            this.requestHeaders.set(field, newValue);
        }
    }

    public void setIfModifiedSince(long newValue) {
        super.setIfModifiedSince(newValue);
        if (this.ifModifiedSince != 0) {
            this.requestHeaders.set("If-Modified-Since", HttpDate.format(new Date(this.ifModifiedSince)));
        } else {
            this.requestHeaders.removeAll("If-Modified-Since");
        }
    }

    public void addRequestProperty(String field, String value) {
        if (this.connected) {
            throw new IllegalStateException("Cannot add request property after connection is made");
        } else if (field == null) {
            throw new NullPointerException("field == null");
        } else if (value == null) {
            Platform.get().log(5, "Ignoring header " + field + " because its value was null.", (Throwable) null);
        } else {
            this.requestHeaders.add(field, value);
        }
    }

    public void setRequestMethod(String method) throws ProtocolException {
        if (!METHODS.contains(method)) {
            throw new ProtocolException("Expected one of " + METHODS + " but was " + method);
        }
        this.method = method;
    }

    public void setFixedLengthStreamingMode(int contentLength) {
        setFixedLengthStreamingMode((long) contentLength);
    }

    public void setFixedLengthStreamingMode(long contentLength) {
        if (this.connected) {
            throw new IllegalStateException("Already connected");
        } else if (this.chunkLength > 0) {
            throw new IllegalStateException("Already in chunked mode");
        } else if (contentLength < 0) {
            throw new IllegalArgumentException("contentLength < 0");
        } else {
            this.fixedContentLength = contentLength;
            this.fixedContentLength = (int) Math.min(contentLength, 2147483647L);
        }
    }

    public void onFailure(Call call2, IOException e) {
        synchronized (this.lock) {
            boolean z = e instanceof UnexpectedException;
            Throwable th = e;
            if (z) {
                th = e.getCause();
            }
            this.callFailure = th;
            this.lock.notifyAll();
        }
    }

    public void onResponse(Call call2, Response response2) {
        synchronized (this.lock) {
            this.response = response2;
            this.handshake = response2.handshake();
            this.url = response2.request().url().url();
            this.lock.notifyAll();
        }
    }

    static final class UnexpectedException extends IOException {
        static final Interceptor INTERCEPTOR = new Interceptor() {
            public Response intercept(Interceptor.Chain chain) throws IOException {
                try {
                    return chain.proceed(chain.request());
                } catch (Error | RuntimeException e) {
                    throw new UnexpectedException(e);
                }
            }
        };

        UnexpectedException(Throwable cause) {
            super(cause);
        }
    }

    private static IOException propagate(Throwable throwable) throws IOException {
        if (throwable instanceof IOException) {
            throw ((IOException) throwable);
        } else if (throwable instanceof Error) {
            throw ((Error) throwable);
        } else if (throwable instanceof RuntimeException) {
            throw ((RuntimeException) throwable);
        } else {
            throw new AssertionError();
        }
    }

    final class NetworkInterceptor implements Interceptor {
        private boolean proceed;

        NetworkInterceptor() {
        }

        public void proceed() {
            synchronized (OkHttpURLConnection.this.lock) {
                this.proceed = true;
                OkHttpURLConnection.this.lock.notifyAll();
            }
        }

        public Response intercept(Interceptor.Chain chain) throws IOException {
            okhttp3.Request request = chain.request();
            if (OkHttpURLConnection.this.urlFilter != null) {
                OkHttpURLConnection.this.urlFilter.checkURLPermitted(request.url().url());
            }
            synchronized (OkHttpURLConnection.this.lock) {
                OkHttpURLConnection.this.connectPending = false;
                OkHttpURLConnection.this.proxy = chain.connection().route().proxy();
                OkHttpURLConnection.this.handshake = chain.connection().handshake();
                OkHttpURLConnection.this.lock.notifyAll();
                while (!this.proceed) {
                    try {
                        OkHttpURLConnection.this.lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new InterruptedIOException();
                    }
                }
            }
            if (request.body() instanceof OutputStreamRequestBody) {
                request = ((OutputStreamRequestBody) request.body()).prepareToSendRequest(request);
            }
            Response response = chain.proceed(request);
            synchronized (OkHttpURLConnection.this.lock) {
                OkHttpURLConnection.this.networkResponse = response;
                URL unused = OkHttpURLConnection.this.url = response.request().url().url();
            }
            return response;
        }
    }
}
