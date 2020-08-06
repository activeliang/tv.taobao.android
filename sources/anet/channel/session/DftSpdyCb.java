package anet.channel.session;

import java.util.List;
import java.util.Map;
import org.android.spdy.SpdyByteArray;
import org.android.spdy.SpdySession;
import org.android.spdy.Spdycb;
import org.android.spdy.SuperviseData;

public class DftSpdyCb implements Spdycb {
    public void spdyOnStreamResponse(SpdySession spdySession, long l, Map<String, List<String>> map, Object o) {
    }

    public void spdyStreamCloseCallback(SpdySession spdySession, long l, int statusCode, Object o, SuperviseData superviseData) {
    }

    public void spdyDataChunkRecvCB(SpdySession spdySession, boolean b, long l, SpdyByteArray spdyByteArray, Object o) {
    }

    public void spdyDataRecvCallback(SpdySession spdySession, boolean b, long l, int i, Object o) {
    }

    public void spdyDataSendCallback(SpdySession spdySession, boolean b, long l, int i, Object o) {
    }

    public void spdyRequestRecvCallback(SpdySession spdySession, long l, Object o) {
    }
}
