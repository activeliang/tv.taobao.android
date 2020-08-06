package com.alibaba.sdk.android.oss.network;

import android.support.v4.media.session.PlaybackStateCompat;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.OSSRequest;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

public class ProgressTouchableRequestBody<T extends OSSRequest> extends RequestBody {
    private static final int SEGMENT_SIZE = 2048;
    private OSSProgressCallback callback;
    private long contentLength;
    private String contentType;
    private InputStream inputStream;
    private T request;

    public ProgressTouchableRequestBody(InputStream input, long contentLength2, String contentType2, ExecutionContext context) {
        this.inputStream = input;
        this.contentType = contentType2;
        this.contentLength = contentLength2;
        this.callback = context.getProgressCallback();
        this.request = context.getRequest();
    }

    public MediaType contentType() {
        return MediaType.parse(this.contentType);
    }

    public long contentLength() throws IOException {
        return this.contentLength;
    }

    public void writeTo(BufferedSink sink) throws IOException {
        Source source = Okio.source(this.inputStream);
        long total = 0;
        while (total < this.contentLength) {
            long read = source.read(sink.buffer(), Math.min(this.contentLength - total, PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH));
            if (read == -1) {
                break;
            }
            total += read;
            sink.flush();
            if (!(this.callback == null || total == 0)) {
                this.callback.onProgress(this.request, total, this.contentLength);
            }
        }
        if (source != null) {
            source.close();
        }
    }
}
