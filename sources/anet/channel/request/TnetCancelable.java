package anet.channel.request;

import org.android.spdy.SpdySession;

public class TnetCancelable implements Cancelable {
    public static final TnetCancelable NULL = new TnetCancelable((SpdySession) null, 0, (String) null);
    private final String seq;
    private final SpdySession spdySession;
    private final int streamId;

    public TnetCancelable(SpdySession spdySession2, int streamId2, String seq2) {
        this.spdySession = spdySession2;
        this.streamId = streamId2;
        this.seq = seq2;
    }

    public void cancel() {
    }
}
