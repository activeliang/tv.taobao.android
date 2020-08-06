package okhttp3.internal.huc;

import java.io.IOException;
import okhttp3.Request;
import okio.Buffer;
import okio.BufferedSink;

final class BufferedRequestBody extends OutputStreamRequestBody {
    final Buffer buffer = new Buffer();
    long contentLength = -1;

    BufferedRequestBody(long expectedContentLength) {
        initOutputStream(this.buffer, expectedContentLength);
    }

    public long contentLength() throws IOException {
        return this.contentLength;
    }

    public Request prepareToSendRequest(Request request) throws IOException {
        if (request.header("Content-Length") != null) {
            return request;
        }
        outputStream().close();
        this.contentLength = this.buffer.size();
        return request.newBuilder().removeHeader("Transfer-Encoding").header("Content-Length", Long.toString(this.buffer.size())).build();
    }

    public void writeTo(BufferedSink sink) throws IOException {
        this.buffer.copyTo(sink.buffer(), 0, this.buffer.size());
    }
}
