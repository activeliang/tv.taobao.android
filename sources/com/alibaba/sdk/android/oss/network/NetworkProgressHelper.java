package com.alibaba.sdk.android.oss.network;

import java.io.IOException;
import java.io.InputStream;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class NetworkProgressHelper {
    public static OkHttpClient addProgressResponseListener(OkHttpClient client, final ExecutionContext context) {
        return client.newBuilder().addNetworkInterceptor(new Interceptor() {
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(new ProgressTouchableResponseBody(originalResponse.body(), context)).build();
            }
        }).build();
    }

    public static ProgressTouchableRequestBody addProgressRequestBody(InputStream input, long contentLength, String contentType, ExecutionContext context) {
        return new ProgressTouchableRequestBody(input, contentLength, contentType, context);
    }
}
