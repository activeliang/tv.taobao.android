package org.android.spdy;

import android.os.Handler;
import android.os.HandlerThread;
import java.util.concurrent.atomic.AtomicBoolean;

public final class SpdySession {
    private static volatile int count = 0;
    private SpdyAgent agent;
    private String authority;
    private AtomicBoolean closed = new AtomicBoolean();
    private String domain;
    private Handler handler;
    Intenalcb intenalcb;
    private Object lock = new Object();
    private int mode;
    private int pubkey_seqnum = 0;
    volatile int refcount = 1;
    SessionCb sessionCallBack = null;
    private boolean sessionClearedFromSessionMgr = false;
    private volatile long sessionNativePtr;
    private NetSparseArray<SpdyStreamContext> spdyStream = null;
    private int streamcount = 1;
    private HandlerThread thread;
    private Object userData = null;

    private native int sendCustomControlFrameN(long j, int i, int i2, int i3, int i4, byte[] bArr);

    private native int sendHeadersN(long j, int i, String[] strArr, boolean z);

    private native int setOptionN(long j, int i, int i2);

    private native int streamCloseN(long j, int i, int i2);

    private native int streamSendDataN(long j, int i, byte[] bArr, int i2, int i3, boolean z);

    private native int submitBioPingN(long j);

    private native int submitPingN(long j);

    private native int submitRequestN(long j, String str, byte b, String[] strArr, byte[] bArr, boolean z, int i, int i2);

    SpdySession(long ptr, SpdyAgent agent2, String authority2, String domain2, SessionCb sessioncb, int mode2, int pubkey_seqnum2, Object userData2) {
        this.sessionNativePtr = ptr;
        this.agent = agent2;
        this.authority = authority2;
        this.intenalcb = new SpdySessionCallBack();
        this.domain = domain2;
        this.spdyStream = new NetSparseArray<>(5);
        this.sessionCallBack = sessioncb;
        this.pubkey_seqnum = pubkey_seqnum2;
        this.mode = mode2;
        this.userData = userData2;
        this.closed.set(false);
    }

    public int getRefCount() {
        return this.refcount;
    }

    /* access modifiers changed from: package-private */
    public void increRefCount() {
        this.refcount++;
    }

    private String getAuthority() {
        return this.authority;
    }

    public String getDomain() {
        return this.domain;
    }

    public Object getUserData() {
        return this.userData;
    }

    /* access modifiers changed from: package-private */
    public int putSpdyStreamCtx(SpdyStreamContext streamctx) {
        synchronized (this.lock) {
            try {
                int old = this.streamcount;
                this.streamcount = old + 1;
                try {
                    this.spdyStream.put(old, streamctx);
                    return old;
                } catch (Throwable th) {
                    th = th;
                    int i = old;
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public SpdyStreamContext getSpdyStream(int index) {
        SpdyStreamContext stm = null;
        if (index > 0) {
            synchronized (this.lock) {
                stm = this.spdyStream.get(index);
            }
        }
        return stm;
    }

    /* access modifiers changed from: package-private */
    public void removeSpdyStream(int index) {
        if (index > 0) {
            synchronized (this.lock) {
                this.spdyStream.remove(index);
            }
        }
    }

    public SpdyStreamContext[] getAllStreamCb() {
        SpdyStreamContext[] data = null;
        synchronized (this.lock) {
            int size = this.spdyStream.size();
            if (size > 0) {
                data = new SpdyStreamContext[size];
                this.spdyStream.toArray(data);
            }
        }
        return data;
    }

    public void clearAllStreamCb() {
        spduLog.Logd("tnet-jni", "[SpdySession.clearAllStreamCb] - ");
        synchronized (this.lock) {
            this.spdyStream.clear();
        }
    }

    /* access modifiers changed from: package-private */
    public SpdyAgent getSpdyAgent() {
        return this.agent;
    }

    /* access modifiers changed from: package-private */
    public Handler getMsgHandler() {
        return this.handler;
    }

    /* access modifiers changed from: package-private */
    public long getSessionNativePtr() {
        return this.sessionNativePtr;
    }

    /* access modifiers changed from: package-private */
    public void setSessionNativePtr(long ptr) {
        this.sessionNativePtr = ptr;
    }

    public int setOption(int optname, int optval) throws SpdyErrorException {
        sessionIsOpen();
        int code = setOptionN(this.sessionNativePtr, optname, optval);
        if (code == 0) {
            return code;
        }
        throw new SpdyErrorException("setOption error: " + code, code);
    }

    public int submitPing() throws SpdyErrorException {
        sessionIsOpen();
        int code = submitPingN(this.sessionNativePtr);
        if (code == 0) {
            return code;
        }
        throw new SpdyErrorException("submitPing error: " + code, code);
    }

    @Deprecated
    public int submitBioPing() throws SpdyErrorException {
        sessionIsOpen();
        int code = submitBioPingN(this.sessionNativePtr);
        if (code == 0) {
            return code;
        }
        throw new SpdyErrorException("submitBioPing error: " + code, code);
    }

    public int streamReset(long streamId, int statusCode) throws SpdyErrorException {
        sessionIsOpen();
        spduLog.Logd("tnet-jni", "[SpdySession.streamReset] - ");
        int code = streamCloseN(this.sessionNativePtr, (int) streamId, statusCode);
        if (code == 0) {
            return code;
        }
        throw new SpdyErrorException("streamReset error: " + code, code);
    }

    public int sendCustomControlFrame(int id, int type, int flags, int length, byte[] data) throws SpdyErrorException {
        sessionIsOpen();
        if (data != null && data.length <= 0) {
            data = null;
        }
        spduLog.Logi("tnet-jni", "[sendCustomControlFrame] - type: " + type);
        int code = sendCustomControlFrameN(this.sessionNativePtr, id, type, flags, length, data);
        if (code == 0) {
            return code;
        }
        throw new SpdyErrorException("sendCustomControlFrame error: " + code, code);
    }

    public int submitRequest(SpdyRequest req, SpdyDataProvider dataPro, Object streamUserData, Spdycb streamCallBack) throws SpdyErrorException {
        if (req == null || streamUserData == null || req.getAuthority() == null) {
            throw new SpdyErrorException("submitRequest error: -1102", (int) TnetStatusCode.TNET_JNI_ERR_INVLID_PARAM);
        }
        sessionIsOpen();
        byte[] data = SpdyAgent.dataproviderToByteArray(req, dataPro);
        if (data != null && data.length <= 0) {
            data = null;
        }
        boolean finish = true;
        if (dataPro != null) {
            finish = dataPro.finished;
        }
        SpdyStreamContext context = new SpdyStreamContext(streamUserData, streamCallBack);
        int index = putSpdyStreamCtx(context);
        String[] nv = SpdyAgent.mapToByteArray(req.getHeaders());
        spduLog.Logi("tnet-jni", "index=" + index + "  " + "starttime=" + System.currentTimeMillis());
        int code = submitRequestN(this.sessionNativePtr, req.getUrlPath(), (byte) req.getPriority(), nv, data, finish, index, req.getRequestTimeoutMs());
        spduLog.Logi("tnet-jni", "index=" + index + "  " + " calltime=" + System.currentTimeMillis());
        if (code < 0) {
            removeSpdyStream(index);
            throw new SpdyErrorException("submitRequest error: " + code, code);
        }
        context.streamId = code;
        return code;
    }

    /* access modifiers changed from: package-private */
    public void sessionIsOpen() {
        if (this.closed.get()) {
            throw new SpdyErrorException("session is already closed: -1104", (int) TnetStatusCode.TNET_JNI_ERR_ASYNC_CLOSE);
        }
    }

    public int cleanUp() {
        spduLog.Logd("tnet-jni", "[SpdySession.cleanUp] - ");
        if (this.closed.getAndSet(true)) {
            return 0;
        }
        this.agent.removeSession(this);
        return closeprivate();
    }

    /* access modifiers changed from: package-private */
    public int closeInternal() {
        if (!this.closed.getAndSet(true)) {
            return closeprivate();
        }
        return 0;
    }

    public int closeSession() {
        spduLog.Logd("tnet-jni", "[SpdySession.closeSession] - ");
        int code = 0;
        synchronized (this.lock) {
            if (!this.sessionClearedFromSessionMgr) {
                spduLog.Logd("tnet-jni", "[SpdySession.closeSession] - " + this.authority);
                this.agent.clearSpdySession(this.authority, this.domain, this.mode);
                this.sessionClearedFromSessionMgr = true;
                try {
                    code = this.agent.closeSession(this.sessionNativePtr);
                } catch (UnsatisfiedLinkError ep) {
                    ep.printStackTrace();
                }
            }
        }
        return code;
    }

    private int closeprivate() {
        synchronized (this.lock) {
            if (!this.sessionClearedFromSessionMgr) {
                this.agent.clearSpdySession(this.authority, this.domain, this.mode);
                this.sessionClearedFromSessionMgr = true;
            }
        }
        this.sessionNativePtr = 0;
        synchronized (this.lock) {
            SpdyStreamContext[] stmCbs = getAllStreamCb();
            if (stmCbs != null) {
                for (SpdyStreamContext stm : stmCbs) {
                    spduLog.Logi("tnet-jni", "[SpdySessionCallBack.spdyStreamCloseCallback] unfinished stm=" + stm.streamId);
                    stm.callBack.spdyStreamCloseCallback(this, (long) stm.streamId, TnetStatusCode.EASY_REASON_CONN_NOT_EXISTS, stm.streamContext, (SuperviseData) null);
                }
            }
            this.spdyStream.clear();
        }
        return 0;
    }
}
