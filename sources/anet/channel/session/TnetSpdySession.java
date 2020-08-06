package anet.channel.session;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.Config;
import anet.channel.DataFrameCb;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.IAuth;
import anet.channel.RequestCb;
import anet.channel.Session;
import anet.channel.SessionInfo;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.bytes.ByteArray;
import anet.channel.bytes.ByteArrayPool;
import anet.channel.entity.ConnInfo;
import anet.channel.entity.ConnectedEvent;
import anet.channel.entity.DisconnectedEvent;
import anet.channel.entity.Event;
import anet.channel.entity.EventType;
import anet.channel.heartbeat.HeartbeatManager;
import anet.channel.heartbeat.IHeartbeat;
import anet.channel.request.Cancelable;
import anet.channel.request.Request;
import anet.channel.request.TnetCancelable;
import anet.channel.security.ISecurity;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.statist.RequestStatistic;
import anet.channel.statist.SessionStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.ConnEvent;
import anet.channel.strategy.StrategyCenter;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpHelper;
import anet.channel.util.Utils;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.SymbolExpUtil;
import org.android.spdy.AccsSSLCallback;
import org.android.spdy.RequestPriority;
import org.android.spdy.SessionCb;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdyByteArray;
import org.android.spdy.SpdyDataProvider;
import org.android.spdy.SpdyErrorException;
import org.android.spdy.SpdyProtocol;
import org.android.spdy.SpdyRequest;
import org.android.spdy.SpdySession;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;
import org.android.spdy.SuperviseConnectInfo;
import org.android.spdy.SuperviseData;
import org.android.spdy.TnetStatusCode;

public class TnetSpdySession extends Session implements SessionCb {
    private static final String SSL_TIKET_KEY2 = "accs_ssl_key2_";
    private static final String TAG = "awcn.TnetSpdySession";
    protected IAuth auth = null;
    protected DataFrameCb dataFrameCb = null;
    protected IHeartbeat heartbeat = null;
    protected ISecurity iSecurity = null;
    protected SpdyAgent mAgent;
    protected String mAppkey = null;
    protected long mConnectedTime = 0;
    protected volatile boolean mHasUnrevPing = false;
    protected long mLastPingTime;
    protected SpdySession mSession;
    /* access modifiers changed from: private */
    public int requestTimeoutCount = 0;
    protected int tnetPublicKey = -1;

    static /* synthetic */ int access$504(TnetSpdySession x0) {
        int i = x0.requestTimeoutCount + 1;
        x0.requestTimeoutCount = i;
        return i;
    }

    public TnetSpdySession(Context context, ConnInfo connInfo, Config config, SessionInfo sessionInfo, int tnetPublicKey2) {
        super(context, connInfo);
        this.mAppkey = config.getAppkey();
        this.iSecurity = config.getSecurity();
        initSpdyAgent();
        if (tnetPublicKey2 >= 0) {
            this.tnetPublicKey = tnetPublicKey2;
        } else {
            this.tnetPublicKey = this.mConnType.getTnetPublicKey(this.iSecurity.isSecOff());
        }
        if (sessionInfo != null) {
            this.dataFrameCb = sessionInfo.dataFrameCb;
            this.auth = sessionInfo.auth;
            if (sessionInfo.isKeepAlive) {
                this.mSessionStat.isKL = 1;
                this.autoReCreate = true;
                this.heartbeat = sessionInfo.heartbeat;
                if (this.heartbeat == null) {
                    this.heartbeat = HeartbeatManager.getHeartbeatFactory().createHeartbeat(this);
                }
            }
        }
    }

    public Cancelable request(Request request, RequestCb cb) {
        RequestStatistic rs;
        SpdyRequest req;
        Cancelable ret = TnetCancelable.NULL;
        if (request != null) {
            rs = request.rs;
        } else {
            rs = new RequestStatistic(this.mRealHost, (String) null);
        }
        rs.setConnType(this.mConnType);
        if (rs.start == 0) {
            rs.start = System.currentTimeMillis();
        }
        rs.setIPAndPort(this.mIp, this.mPort);
        rs.ipRefer = this.mConnStrategy.getIpSource();
        rs.ipType = this.mConnStrategy.getIpType();
        rs.unit = this.unit;
        if (request == null || cb == null) {
            if (cb != null) {
                cb.onFinish(-102, ErrorConstant.getErrMsg(-102), rs);
            }
            return ret;
        }
        try {
            if (this.mSession == null || !(this.mStatus == Session.Status.CONNECTED || this.mStatus == Session.Status.AUTH_SUCC)) {
                cb.onFinish(ErrorConstant.ERROR_SESSION_INVALID, ErrorConstant.getErrMsg(ErrorConstant.ERROR_SESSION_INVALID), request.rs);
                return ret;
            }
            request.setUrlScheme(this.mConnType.isSSL());
            URL realURL = request.getUrl();
            if (ALog.isPrintLog(2)) {
                ALog.i(TAG, "", request.getSeq(), "request URL", realURL.toString());
                ALog.i(TAG, "", request.getSeq(), "request Method", request.getMethod());
                ALog.i(TAG, "", request.getSeq(), "request headers", request.getHeaders());
            }
            if (TextUtils.isEmpty(this.mProxyIp) || this.mProxyPort <= 0) {
                req = new SpdyRequest(realURL, request.getMethod(), RequestPriority.DEFAULT_PRIORITY, request.getReadTimeout(), request.getConnectTimeout());
            } else {
                req = new SpdyRequest(realURL, realURL.getHost(), realURL.getPort(), this.mProxyIp, this.mProxyPort, request.getMethod(), RequestPriority.DEFAULT_PRIORITY, request.getReadTimeout(), request.getConnectTimeout(), 0);
            }
            Map<String, String> headers = request.getHeaders();
            if (!headers.containsKey("Host")) {
                req.addHeaders(headers);
                req.addHeader(":host", request.getHost());
            } else {
                HashMap hashMap = new HashMap(request.getHeaders());
                hashMap.put(":host", hashMap.remove("Host"));
                req.addHeaders(hashMap);
            }
            SpdyDataProvider provider = new SpdyDataProvider(request.getBodyBytes());
            request.rs.sendStart = System.currentTimeMillis();
            request.rs.processTime = request.rs.sendStart - request.rs.start;
            int streamId = this.mSession.submitRequest(req, provider, this, new RequestCallback(request, cb));
            if (ALog.isPrintLog(1)) {
                ALog.d(TAG, "", request.getSeq(), "streamId", Integer.valueOf(streamId));
            }
            TnetCancelable tnetCancelable = new TnetCancelable(this.mSession, streamId, request.getSeq());
            try {
                this.mSessionStat.requestCount++;
                this.mSessionStat.stdRCount++;
                this.mLastPingTime = System.currentTimeMillis();
                if (this.heartbeat != null) {
                    this.heartbeat.reSchedule();
                }
                ret = tnetCancelable;
            } catch (SpdyErrorException e) {
                e = e;
                ret = tnetCancelable;
                if (e.SpdyErrorGetCode() == -1104 || e.SpdyErrorGetCode() == -1103) {
                    ALog.e(TAG, "Send request on closed session!!!", this.mSeq, new Object[0]);
                    notifyStatus(Session.Status.DISCONNECTED, new DisconnectedEvent(EventType.DISCONNECTED, false, TnetStatusCode.TNET_JNI_ERR_ASYNC_CLOSE, "Session is closed!"));
                }
                cb.onFinish(ErrorConstant.ERROR_TNET_EXCEPTION, ErrorConstant.formatMsg(ErrorConstant.ERROR_TNET_EXCEPTION, String.valueOf(e.SpdyErrorGetCode())), rs);
                return ret;
            } catch (Exception e2) {
                ret = tnetCancelable;
                cb.onFinish(-101, ErrorConstant.getErrMsg(-101), rs);
                return ret;
            }
            return ret;
        } catch (SpdyErrorException e3) {
            e = e3;
        } catch (Exception e4) {
            cb.onFinish(-101, ErrorConstant.getErrMsg(-101), rs);
            return ret;
        }
    }

    public void sendCustomFrame(int dataId, byte[] data, int type) {
        try {
            if (this.dataFrameCb != null) {
                ALog.e(TAG, "sendCustomFrame", this.mSeq, "dataId", Integer.valueOf(dataId), "type", Integer.valueOf(type));
                if (this.mStatus != Session.Status.AUTH_SUCC || this.mSession == null) {
                    ALog.e(TAG, "sendCustomFrame", this.mSeq, "sendCustomFrame con invalid mStatus:" + this.mStatus);
                    onDataFrameException(dataId, ErrorConstant.ERROR_SESSION_INVALID, true, "session invalid");
                } else if (data == null || data.length <= 16384) {
                    this.mSession.sendCustomControlFrame(dataId, type, 0, data == null ? 0 : data.length, data);
                    this.mSessionStat.requestCount++;
                    this.mSessionStat.cfRCount++;
                    this.mLastPingTime = System.currentTimeMillis();
                    if (this.heartbeat != null) {
                        this.heartbeat.reSchedule();
                    }
                } else {
                    onDataFrameException(dataId, ErrorConstant.ERROR_DATA_TOO_LARGE, false, (String) null);
                }
            }
        } catch (SpdyErrorException e) {
            ALog.e(TAG, "sendCustomFrame error", this.mSeq, e, new Object[0]);
            onDataFrameException(dataId, ErrorConstant.ERROR_TNET_EXCEPTION, true, "SpdyErrorException: " + e.toString());
        } catch (Exception e2) {
            ALog.e(TAG, "sendCustomFrame error", this.mSeq, e2, new Object[0]);
            onDataFrameException(dataId, -101, true, e2.toString());
        }
    }

    private void onDataFrameException(int dataId, int errorId, boolean needRetry, String detail) {
        if (this.dataFrameCb != null) {
            this.dataFrameCb.onException(dataId, errorId, needRetry, detail);
        }
    }

    public void connect() {
        int i;
        if (this.mStatus != Session.Status.CONNECTING && this.mStatus != Session.Status.CONNECTED && this.mStatus != Session.Status.AUTH_SUCC) {
            try {
                if (this.mAgent != null) {
                    String sessionId = String.valueOf(System.currentTimeMillis());
                    ALog.e(TAG, "[connect]", this.mSeq, "host", this.mHost, "connect ", this.mIp + SymbolExpUtil.SYMBOL_COLON + this.mPort, "sessionId", sessionId, "SpdyProtocol,", this.mConnType, "proxyIp,", this.mProxyIp, "proxyPort,", Integer.valueOf(this.mProxyPort));
                    org.android.spdy.SessionInfo info = new org.android.spdy.SessionInfo(this.mIp, this.mPort, this.mHost + "_" + this.mAppkey, this.mProxyIp, this.mProxyPort, sessionId, this, this.mConnType.getTnetConType());
                    info.setConnectionTimeoutMs((int) (((float) this.mConnTimeout) * Utils.getNetworkTimeFactor()));
                    info.setPubKeySeqNum(this.tnetPublicKey);
                    this.mSession = this.mAgent.createSession(info);
                    if (this.mSession.getRefCount() > 1) {
                        ALog.e(TAG, "get session ref count > 1!!!", this.mSeq, new Object[0]);
                        notifyStatus(Session.Status.CONNECTED, new ConnectedEvent(EventType.CONNECTED));
                        auth();
                        return;
                    }
                    notifyStatus(Session.Status.CONNECTING, (Event) null);
                    this.mLastPingTime = System.currentTimeMillis();
                    SessionStatistic sessionStatistic = this.mSessionStat;
                    if (!TextUtils.isEmpty(this.mProxyIp)) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    sessionStatistic.isProxy = i;
                    this.mSessionStat.isTunnel = "false";
                    this.mSessionStat.isBackground = GlobalAppRuntimeInfo.isAppBackground();
                    this.mConnectedTime = 0;
                }
            } catch (Throwable t) {
                notifyStatus(Session.Status.CONNETFAIL, (Event) null);
                ALog.e(TAG, "connect exception ", this.mSeq, t, new Object[0]);
            }
        }
    }

    public void close() {
        ALog.e(TAG, "force close!", this.mSeq, "session", this);
        notifyStatus(Session.Status.DISCONNECTING, (Event) null);
        if (this.heartbeat != null) {
            this.heartbeat.stop();
            this.heartbeat = null;
        }
        try {
            if (this.mSession != null) {
                this.mSession.closeSession();
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: protected */
    public void onDisconnect() {
        this.mHasUnrevPing = false;
    }

    /* access modifiers changed from: protected */
    public Runnable getRecvTimeOutRunnable() {
        return new Runnable() {
            public void run() {
                if (TnetSpdySession.this.mHasUnrevPing) {
                    ALog.e(TnetSpdySession.TAG, "send msg time out!", TnetSpdySession.this.mSeq, "pingUnRcv:", Boolean.valueOf(TnetSpdySession.this.mHasUnrevPing));
                    try {
                        TnetSpdySession.this.handleCallbacks(EventType.DATA_TIMEOUT, (Event) null);
                        if (TnetSpdySession.this.mSessionStat != null) {
                            TnetSpdySession.this.mSessionStat.closeReason = "ping time out";
                        }
                        TnetSpdySession.this.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    public void ping(boolean force) {
        if (ALog.isPrintLog(1)) {
            ALog.d(TAG, "ping", this.mSeq, "host", this.mHost, "thread", Thread.currentThread().getName());
        }
        if (force) {
            try {
                if (this.mSession == null) {
                    if (this.mSessionStat != null) {
                        this.mSessionStat.closeReason = "session null";
                    }
                    ALog.e(TAG, this.mHost + " session null", this.mSeq, new Object[0]);
                    close();
                } else if (this.mStatus == Session.Status.CONNECTED || this.mStatus == Session.Status.AUTH_SUCC) {
                    handleCallbacks(EventType.PING_SEND, (Event) null);
                    this.mHasUnrevPing = true;
                    this.mSessionStat.ppkgCount++;
                    this.mSession.submitPing();
                    if (ALog.isPrintLog(1)) {
                        ALog.d(TAG, this.mHost + " submit ping ms:" + (System.currentTimeMillis() - this.mLastPingTime) + " force:" + force, this.mSeq, new Object[0]);
                    }
                    setPingTimeout();
                    this.mLastPingTime = System.currentTimeMillis();
                    if (this.heartbeat != null) {
                        this.heartbeat.reSchedule();
                    }
                }
            } catch (SpdyErrorException e) {
                if (e.SpdyErrorGetCode() == -1104 || e.SpdyErrorGetCode() == -1103) {
                    ALog.e(TAG, "Send request on closed session!!!", this.mSeq, new Object[0]);
                    notifyStatus(Session.Status.DISCONNECTED, new DisconnectedEvent(EventType.DISCONNECTED, false, TnetStatusCode.TNET_JNI_ERR_ASYNC_CLOSE, "Session is closed!"));
                }
                ALog.e(TAG, "ping", this.mSeq, e, new Object[0]);
            } catch (Exception e2) {
                ALog.e(TAG, "ping", this.mSeq, e2, new Object[0]);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void auth() {
        if (this.auth != null) {
            this.auth.auth(this, new IAuth.AuthCallback() {
                public void onAuthSuccess() {
                    TnetSpdySession.this.notifyStatus(Session.Status.AUTH_SUCC, (Event) null);
                    TnetSpdySession.this.mLastPingTime = System.currentTimeMillis();
                    if (TnetSpdySession.this.heartbeat != null) {
                        TnetSpdySession.this.heartbeat.start();
                    }
                    TnetSpdySession.this.mSessionStat.ret = 1;
                    ALog.d(TnetSpdySession.TAG, "spdyOnStreamResponse", TnetSpdySession.this.mSeq, "authTime", Long.valueOf(TnetSpdySession.this.mSessionStat.authTime));
                    if (TnetSpdySession.this.mConnectedTime > 0) {
                        TnetSpdySession.this.mSessionStat.authTime = System.currentTimeMillis() - TnetSpdySession.this.mConnectedTime;
                    }
                }

                public void onAuthFail(int errCode, String errMsg) {
                    TnetSpdySession.this.notifyStatus(Session.Status.AUTH_FAIL, (Event) null);
                    if (TnetSpdySession.this.mSessionStat != null) {
                        TnetSpdySession.this.mSessionStat.closeReason = "Accs_Auth_Fail:" + errCode;
                        TnetSpdySession.this.mSessionStat.errorCode = (long) errCode;
                    }
                    TnetSpdySession.this.close();
                }
            });
            return;
        }
        notifyStatus(Session.Status.AUTH_SUCC, (Event) null);
        this.mSessionStat.ret = 1;
        if (this.heartbeat != null) {
            this.heartbeat.start();
        }
    }

    public boolean isAvailable() {
        return this.mStatus == Session.Status.AUTH_SUCC;
    }

    private void initSpdyAgent() {
        try {
            SpdyAgent.enableDebug = false;
            this.mAgent = SpdyAgent.getInstance(this.mContext, SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
            if (this.iSecurity != null && !this.iSecurity.isSecOff()) {
                this.mAgent.setAccsSslCallback(new AccsSSLCallback() {
                    public byte[] getSSLPublicKey(int i, byte[] bytes) {
                        byte[] decrypt = null;
                        try {
                            decrypt = TnetSpdySession.this.iSecurity.decrypt(TnetSpdySession.this.mContext, ISecurity.CIPHER_ALGORITHM_AES128, SpdyProtocol.TNET_PUBKEY_SG_KEY, bytes);
                            if (decrypt != null && ALog.isPrintLog(2)) {
                                ALog.i("getSSLPublicKey", (String) null, "decrypt", new String(decrypt));
                            }
                        } catch (Throwable t) {
                            ALog.e(TnetSpdySession.TAG, "getSSLPublicKey", (String) null, t, new Object[0]);
                        }
                        return decrypt;
                    }
                });
            }
        } catch (Exception e) {
            ALog.e(TAG, "Init failed.", (String) null, e, new Object[0]);
        }
    }

    public void spdySessionConnectCB(SpdySession session, SuperviseConnectInfo data) {
        ConnectedEvent event = new ConnectedEvent(EventType.CONNECTED);
        event.mConnectedTime = (long) data.connectTime;
        event.mSSLTime = (long) data.handshakeTime;
        this.mSessionStat.connectionTime = (long) data.connectTime;
        this.mSessionStat.sslTime = (long) data.handshakeTime;
        this.mSessionStat.sslCalTime = (long) data.doHandshakeTime;
        this.mSessionStat.netType = NetworkStatusHelper.getNetworkSubType();
        this.mConnectedTime = System.currentTimeMillis();
        notifyStatus(Session.Status.CONNECTED, event);
        auth();
        ALog.e(TAG, "spdySessionConnectCB connect", this.mSeq, "connectTime", Integer.valueOf(data.connectTime), "sslTime:", Integer.valueOf(data.handshakeTime));
    }

    public void spdyPingRecvCallback(SpdySession session, long unique_id, Object sessionUserData) {
        if (ALog.isPrintLog(2)) {
            ALog.i(TAG, "ping receive", this.mSeq, "Host", this.mHost, "id", Long.valueOf(unique_id));
        }
        if (unique_id >= 0) {
            this.mHasUnrevPing = false;
            handleCallbacks(EventType.PIND_RECEIVE, (Event) null);
        }
    }

    public void bioPingRecvCallback(SpdySession arg0, int arg1) {
    }

    public void spdyCustomControlFrameRecvCallback(SpdySession session, Object sessionUserData, int dataId, int type, int flags, int length, byte[] data) {
        ALog.e(TAG, "[spdyCustomControlFrameRecvCallback]", this.mSeq, "len", Integer.valueOf(length), "frameCb", this.dataFrameCb);
        if (ALog.isPrintLog(1)) {
            String str = "";
            if (length < 512) {
                for (int i = 0; i < data.length; i++) {
                    str = str + Integer.toHexString(data[i] & OnReminderListener.RET_FULL) + " ";
                }
                ALog.e(TAG, (String) null, this.mSeq, "str", str);
            }
        }
        if (this.dataFrameCb != null) {
            this.dataFrameCb.onDataReceive(this, data, dataId, type);
        } else {
            ALog.e(TAG, "AccsFrameCb is null", this.mSeq, new Object[0]);
            AppMonitor.getInstance().commitStat(new ExceptionStatistic(-105, (String) null, "rt"));
        }
        this.mSessionStat.inceptCount++;
    }

    public void spdySessionFailedError(SpdySession session, int error, Object sessionUserData) {
        if (session != null) {
            try {
                session.cleanUp();
            } catch (Exception e) {
                ALog.e(TAG, "[spdySessionFailedError]session clean up failed!", (String) null, e, new Object[0]);
            }
        }
        notifyStatus(Session.Status.CONNETFAIL, new Event(EventType.CONNECT_FAIL, error, "tnet connect fail"));
        ALog.e(TAG, (String) null, this.mSeq, " errorId:", Integer.valueOf(error));
        this.mSessionStat.errorCode = (long) error;
        this.mSessionStat.ret = 0;
        this.mSessionStat.netType = NetworkStatusHelper.getNetworkSubType();
        AppMonitor.getInstance().commitStat(this.mSessionStat);
        AppMonitor.getInstance().commitAlarm(this.mSessionStat.getAlarmObject());
    }

    public void spdySessionCloseCallback(SpdySession session, Object userData, SuperviseConnectInfo connInfo, int error) {
        ALog.e(TAG, "spdySessionCloseCallback", this.mSeq, " errorCode:", Integer.valueOf(error));
        if (this.heartbeat != null) {
            this.heartbeat.stop();
            this.heartbeat = null;
        }
        if (session != null) {
            try {
                session.cleanUp();
            } catch (Exception e) {
                ALog.e(TAG, "[spdySessionCloseCallback]session clean up failed!", (String) null, e, new Object[0]);
            }
        }
        notifyStatus(Session.Status.DISCONNECTED, new DisconnectedEvent(EventType.DISCONNECTED, false, error, TextUtils.isEmpty(this.mSessionStat.closeReason) ? "tnet close error:" + error : this.mSessionStat.closeReason + SymbolExpUtil.SYMBOL_COLON + this.mSessionStat.errorCode));
        if (connInfo != null) {
            this.mSessionStat.requestCount = (long) connInfo.reused_counter;
            this.mSessionStat.liveTime = (long) connInfo.keepalive_period_second;
        }
        if (this.mSessionStat.errorCode == 0) {
            this.mSessionStat.errorCode = (long) error;
        }
        this.mSessionStat.lastPingInterval = (int) (System.currentTimeMillis() - this.mLastPingTime);
        AppMonitor.getInstance().commitStat(this.mSessionStat);
        AppMonitor.getInstance().commitAlarm(this.mSessionStat.getAlarmObject());
    }

    public void spdyCustomControlFrameFailCallback(SpdySession session, Object sessionUserData, int dataId, int error) {
        ALog.e(TAG, "spdyCustomControlFrameFailCallback", this.mSeq, "dataId", Integer.valueOf(dataId));
        onDataFrameException(dataId, error, true, "tnet error");
    }

    public byte[] getSSLMeta(SpdySession session) {
        byte[] ticket = null;
        String host = session.getDomain();
        if (TextUtils.isEmpty(host)) {
            ALog.i(TAG, "get sslticket host is null", (String) null, new Object[0]);
            return null;
        }
        try {
            ticket = this.iSecurity.getBytes(this.mContext, SSL_TIKET_KEY2 + host);
        } catch (Throwable t) {
            ALog.e(TAG, "getSSLMeta", (String) null, t, new Object[0]);
        }
        return ticket;
    }

    public int putSSLMeta(SpdySession session, byte[] value) {
        int ret = -1;
        String host = session.getDomain();
        if (TextUtils.isEmpty(host)) {
            return -1;
        }
        try {
            ret = this.iSecurity.saveBytes(this.mContext, new StringBuilder().append(SSL_TIKET_KEY2).append(host).toString(), value) ? 0 : -1;
        } catch (Throwable t) {
            ALog.e(TAG, "putSSLMeta", (String) null, t, new Object[0]);
        }
        return ret;
    }

    private class RequestCallback extends DftSpdyCb {
        private RequestCb callback;
        private long mSRT = 0;
        private Request request;

        public RequestCallback(Request request2, RequestCb cb) {
            this.request = request2;
            this.callback = cb;
        }

        public void spdyDataChunkRecvCB(SpdySession session, boolean fin, long streamId, SpdyByteArray data, Object streamUserData) {
            ALog.d(TnetSpdySession.TAG, "spdyDataChunkRecvCB", this.request.getSeq(), "len", Integer.valueOf(data.getDataLength()), "fin", Boolean.valueOf(fin));
            if (this.callback != null) {
                ByteArray byteArray = ByteArrayPool.getInstance().retrieveAndCopy(data.getByteArray(), data.getDataLength());
                data.recycle();
                this.callback.onDataReceive(byteArray, fin);
            }
            TnetSpdySession.this.handleCallbacks(EventType.DATA_RECEIVE, (Event) null);
        }

        public void spdyStreamCloseCallback(SpdySession session, long streamId, int statusCode, Object streamUserData, SuperviseData data) {
            ALog.d(TnetSpdySession.TAG, "spdyStreamCloseCallback", this.request.getSeq(), "streamId", Long.valueOf(streamId));
            collectStatisticData(data);
            int code = 0;
            String msg = "SUCCESS";
            if (statusCode != 0) {
                code = ErrorConstant.ERROR_TNET_REQUEST_FAIL;
                msg = ErrorConstant.formatMsg(ErrorConstant.ERROR_TNET_REQUEST_FAIL, String.valueOf(statusCode));
                if (statusCode != -2005) {
                    AppMonitor.getInstance().commitStat(new ExceptionStatistic(ErrorConstant.ERROR_TNET_EXCEPTION, msg, this.request.rs, (Throwable) null));
                }
                ALog.e(TnetSpdySession.TAG, "spdyStreamCloseCallback error", this.request.getSeq(), "status code", Integer.valueOf(statusCode));
            }
            if (this.callback != null) {
                this.callback.onFinish(code, msg, this.request.rs);
            }
            if (statusCode == -2004 && TnetSpdySession.access$504(TnetSpdySession.this) >= 2) {
                ConnEvent connEvent = new ConnEvent();
                connEvent.isSuccess = false;
                StrategyCenter.getInstance().notifyConnEvent(TnetSpdySession.this.mRealHost, TnetSpdySession.this.mConnStrategy, connEvent);
                TnetSpdySession.this.close(true);
            }
        }

        private void collectStatisticData(SuperviseData data) {
            try {
                this.request.rs.serverRT = this.mSRT;
                this.request.rs.oneWayTime = System.currentTimeMillis() - this.request.rs.start;
                if (data != null) {
                    this.request.rs.sendBeforeTime = data.sendStart - data.requestStart;
                    this.request.rs.sendDataTime = data.sendEnd - this.request.rs.sendStart;
                    this.request.rs.firstDataTime = data.responseStart - data.sendEnd;
                    this.request.rs.recDataTime = data.responseEnd - data.responseStart;
                    this.request.rs.sendDataSize = (long) (data.bodySize + data.compressSize);
                    this.request.rs.recDataSize = (long) (data.recvBodySize + data.recvCompressSize);
                    this.request.rs.reqHeadInflateSize = (long) data.uncompressSize;
                    this.request.rs.reqHeadDeflateSize = (long) data.compressSize;
                    this.request.rs.reqBodyInflateSize = (long) data.bodySize;
                    this.request.rs.reqBodyDeflateSize = (long) data.bodySize;
                    this.request.rs.rspHeadDeflateSize = (long) data.recvCompressSize;
                    this.request.rs.rspHeadInflateSize = (long) data.recvUncompressSize;
                    this.request.rs.rspBodyDeflateSize = (long) data.recvBodySize;
                    this.request.rs.rspBodyInflateSize = (long) data.recvBodySize;
                    TnetSpdySession.this.mSessionStat.recvSizeCount += (long) (data.recvBodySize + data.recvCompressSize);
                    TnetSpdySession.this.mSessionStat.sendSizeCount += (long) (data.bodySize + data.compressSize);
                }
            } catch (Exception e) {
            }
        }

        public void spdyOnStreamResponse(SpdySession session, long streamId, Map<String, List<String>> headers, Object streamUserData) {
            int httpStatusCode = 0;
            try {
                List<String> list = headers.get(HttpConstant.STATUS);
                if (list != null && !list.isEmpty()) {
                    httpStatusCode = Integer.parseInt(list.get(0));
                }
            } catch (NumberFormatException e) {
            }
            if (httpStatusCode > 0) {
                this.request.rs.ret = 1;
                int unused = TnetSpdySession.this.requestTimeoutCount = 0;
            }
            ALog.i(TnetSpdySession.TAG, "", this.request.getSeq(), "httpStatusCode", Integer.valueOf(httpStatusCode));
            ALog.i(TnetSpdySession.TAG, "", this.request.getSeq(), "response headers", headers);
            if (this.callback != null) {
                this.callback.onResponseCode(httpStatusCode, HttpHelper.cloneMap(headers));
            }
            TnetSpdySession.this.handleCallbacks(EventType.HEADER_RECEIVE, (Event) null);
            this.request.rs.contentEncoding = HttpHelper.getSingleHeaderFieldByKey(headers, "Content-Encoding");
            this.mSRT = HttpHelper.parseServerRT(headers);
            TnetSpdySession.this.handleResponseCode(this.request, httpStatusCode);
            TnetSpdySession.this.handleResponseHeaders(this.request, headers);
        }
    }
}
