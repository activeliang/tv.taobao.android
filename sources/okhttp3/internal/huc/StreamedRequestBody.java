package okhttp3.internal.huc;

import android.support.v4.media.session.PlaybackStateCompat;
import java.io.IOException;
import okhttp3.internal.http.UnrepeatableRequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Pipe;

final class StreamedRequestBody extends OutputStreamRequestBody implements UnrepeatableRequestBody {
    private final Pipe pipe = new Pipe(PlaybackStateCompat.ACTION_PLAY_FROM_URI);

    StreamedRequestBody(long expectedContentLength) {
        initOutputStream(Okio.buffer(this.pipe.sink()), expectedContentLength);
    }

    public void writeTo(BufferedSink sink) throws IOException {
        Buffer buffer = new Buffer();
        while (this.pipe.source().read(buffer, PlaybackStateCompat.ACTION_PLAY_FROM_URI) != -1) {
            sink.write(buffer, buffer.size());
        }
    }
}
