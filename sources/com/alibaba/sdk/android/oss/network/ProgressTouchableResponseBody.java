package com.alibaba.sdk.android.oss.network;

import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class ProgressTouchableResponseBody<T extends OSSRequest> extends ResponseBody {
    private BufferedSource mBufferedSource;
    /* access modifiers changed from: private */
    public OSSProgressCallback mProgressListener;
    /* access modifiers changed from: private */
    public final ResponseBody mResponseBody;
    /* access modifiers changed from: private */
    public T request;

    public ProgressTouchableResponseBody(ResponseBody responseBody, ExecutionContext context) {
        this.mResponseBody = responseBody;
        this.mProgressListener = context.getProgressCallback();
        this.request = context.getRequest();
    }

    public MediaType contentType() {
        return this.mResponseBody.contentType();
    }

    public long contentLength() {
        return this.mResponseBody.contentLength();
    }

    public BufferedSource source() {
        if (this.mBufferedSource == null) {
            this.mBufferedSource = Okio.buffer(source(this.mResponseBody.source()));
        }
        return this.mBufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            private long totalBytesRead = 0;

            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                this.totalBytesRead = (bytesRead != -1 ? bytesRead : 0) + this.totalBytesRead;
                if (!(ProgressTouchableResponseBody.this.mProgressListener == null || bytesRead == -1 || this.totalBytesRead == 0)) {
                    ProgressTouchableResponseBody.this.mProgressListener.onProgress(ProgressTouchableResponseBody.this.request, this.totalBytesRead, ProgressTouchableResponseBody.this.mResponseBody.contentLength());
                }
                return bytesRead;
            }
        };
    }
}
