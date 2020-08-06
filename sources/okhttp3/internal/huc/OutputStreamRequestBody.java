package okhttp3.internal.huc;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Timeout;

abstract class OutputStreamRequestBody extends RequestBody {
    boolean closed;
    private long expectedContentLength;
    private OutputStream outputStream;
    private Timeout timeout;

    OutputStreamRequestBody() {
    }

    /* access modifiers changed from: protected */
    public void initOutputStream(final BufferedSink sink, final long expectedContentLength2) {
        this.timeout = sink.timeout();
        this.expectedContentLength = expectedContentLength2;
        this.outputStream = new OutputStream() {
            private long bytesReceived;

            public void write(int b) throws IOException {
                write(new byte[]{(byte) b}, 0, 1);
            }

            public void write(byte[] source, int offset, int byteCount) throws IOException {
                if (OutputStreamRequestBody.this.closed) {
                    throw new IOException("closed");
                } else if (expectedContentLength2 == -1 || this.bytesReceived + ((long) byteCount) <= expectedContentLength2) {
                    this.bytesReceived += (long) byteCount;
                    try {
                        sink.write(source, offset, byteCount);
                    } catch (InterruptedIOException e) {
                        throw new SocketTimeoutException(e.getMessage());
                    }
                } else {
                    throw new ProtocolException("expected " + expectedContentLength2 + " bytes but received " + this.bytesReceived + byteCount);
                }
            }

            public void flush() throws IOException {
                if (!OutputStreamRequestBody.this.closed) {
                    sink.flush();
                }
            }

            public void close() throws IOException {
                OutputStreamRequestBody.this.closed = true;
                if (expectedContentLength2 == -1 || this.bytesReceived >= expectedContentLength2) {
                    sink.close();
                    return;
                }
                throw new ProtocolException("expected " + expectedContentLength2 + " bytes but received " + this.bytesReceived);
            }
        };
    }

    public final OutputStream outputStream() {
        return this.outputStream;
    }

    public final Timeout timeout() {
        return this.timeout;
    }

    public final boolean isClosed() {
        return this.closed;
    }

    public long contentLength() throws IOException {
        return this.expectedContentLength;
    }

    public final MediaType contentType() {
        return null;
    }

    public Request prepareToSendRequest(Request request) throws IOException {
        return request;
    }
}
