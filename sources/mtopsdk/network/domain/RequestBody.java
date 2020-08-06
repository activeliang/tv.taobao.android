package mtopsdk.network.domain;

import java.io.IOException;
import java.io.OutputStream;

public abstract class RequestBody {
    public abstract String contentType();

    public abstract void writeTo(OutputStream outputStream) throws IOException;

    public long contentLength() {
        return -1;
    }

    public static RequestBody create(final String contentType, final byte[] content) throws Exception {
        if (content != null) {
            return new RequestBody() {
                public String contentType() {
                    return contentType;
                }

                public long contentLength() {
                    return (long) content.length;
                }

                public void writeTo(OutputStream os) throws IOException {
                    os.write(content);
                }
            };
        }
        throw new NullPointerException("content == null");
    }
}
