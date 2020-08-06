package okhttp3.internal.http;

import anet.channel.util.HttpConstant;
import com.alibaba.sdk.android.oss.common.utils.HttpHeaders;
import java.io.IOException;
import java.util.List;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

public final class BridgeInterceptor implements Interceptor {
    private final CookieJar cookieJar;

    public BridgeInterceptor(CookieJar cookieJar2) {
        this.cookieJar = cookieJar2;
    }

    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request userRequest = chain.request();
        Request.Builder requestBuilder = userRequest.newBuilder();
        RequestBody body = userRequest.body();
        if (body != null) {
            MediaType contentType = body.contentType();
            if (contentType != null) {
                requestBuilder.header("Content-Type", contentType.toString());
            }
            long contentLength = body.contentLength();
            if (contentLength != -1) {
                requestBuilder.header("Content-Length", Long.toString(contentLength));
                requestBuilder.removeHeader("Transfer-Encoding");
            } else {
                requestBuilder.header("Transfer-Encoding", "chunked");
                requestBuilder.removeHeader("Content-Length");
            }
        }
        if (userRequest.header("Host") == null) {
            requestBuilder.header("Host", Util.hostHeader(userRequest.url(), false));
        }
        if (userRequest.header("Connection") == null) {
            requestBuilder.header("Connection", "Keep-Alive");
        }
        boolean transparentGzip = false;
        if (userRequest.header(HttpConstant.ACCEPT_ENCODING) == null && userRequest.header(HttpHeaders.RANGE) == null) {
            transparentGzip = true;
            requestBuilder.header(HttpConstant.ACCEPT_ENCODING, "gzip");
        }
        List<Cookie> cookies = this.cookieJar.loadForRequest(userRequest.url());
        if (!cookies.isEmpty()) {
            requestBuilder.header(HttpConstant.COOKIE, cookieHeader(cookies));
        }
        if (userRequest.header(HttpHeaders.USER_AGENT) == null) {
            requestBuilder.header(HttpHeaders.USER_AGENT, Version.userAgent());
        }
        Response networkResponse = chain.proceed(requestBuilder.build());
        HttpHeaders.receiveHeaders(this.cookieJar, userRequest.url(), networkResponse.headers());
        Response.Builder responseBuilder = networkResponse.newBuilder().request(userRequest);
        if (transparentGzip && "gzip".equalsIgnoreCase(networkResponse.header("Content-Encoding")) && HttpHeaders.hasBody(networkResponse)) {
            GzipSource responseBody = new GzipSource(networkResponse.body().source());
            responseBuilder.headers(networkResponse.headers().newBuilder().removeAll("Content-Encoding").removeAll("Content-Length").build());
            responseBuilder.body(new RealResponseBody(networkResponse.header("Content-Type"), -1, Okio.buffer((Source) responseBody)));
        }
        return responseBuilder.build();
    }

    private String cookieHeader(List<Cookie> cookies) {
        StringBuilder cookieHeader = new StringBuilder();
        int size = cookies.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                cookieHeader.append("; ");
            }
            Cookie cookie = cookies.get(i);
            cookieHeader.append(cookie.name()).append('=').append(cookie.value());
        }
        return cookieHeader.toString();
    }
}
