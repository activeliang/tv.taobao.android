package anet.channel;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.entity.ConnInfo;
import anet.channel.entity.ConnType;
import anet.channel.entity.Event;
import anet.channel.entity.EventCb;
import anet.channel.entity.EventType;
import anet.channel.request.Cancelable;
import anet.channel.request.Request;
import anet.channel.statist.SessionStatistic;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpHelper;
import anet.channel.util.StringUtils;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.android.spdy.SpdyAgent;
import org.android.spdy.SpdySessionKind;
import org.android.spdy.SpdyVersion;

public abstract class Session implements Comparable<Session> {
    private static final String TAG = "awcn.Session";
    protected boolean autoReCreate = false;
    private List<Long> errorTimeList = null;
    private long lastAmdcRequestSend = 0;
    /* access modifiers changed from: protected */
    public IConnStrategy mConnStrategy;
    protected int mConnTimeout;
    protected ConnType mConnType;
    /* access modifiers changed from: protected */
    public Context mContext;
    Map<EventCb, Integer> mEventCallBacks = new LinkedHashMap();
    protected String mHost;
    protected String mIp;
    private boolean mIsConnTimeOut = false;
    protected int mPort;
    protected String mProxyIp;
    protected int mProxyPort;
    /* access modifiers changed from: protected */
    public String mRealHost;
    protected Runnable mRecvTimeOutRunnable;
    protected int mReqTimeout;
    public final String mSeq;
    public final SessionStatistic mSessionStat;
    protected Status mStatus = Status.DISCONNECTED;
    private Future<?> timeoutTaskFuture;
    protected boolean tryNextWhenFail = true;
    protected String unit = null;

    public enum Status {
        CONNECTED,
        CONNECTING,
        CONNETFAIL,
        AUTHING,
        AUTH_SUCC,
        AUTH_FAIL,
        DISCONNECTED,
        DISCONNECTING
    }

    public abstract void close();

    /* access modifiers changed from: protected */
    public abstract Runnable getRecvTimeOutRunnable();

    public abstract boolean isAvailable();

    public abstract Cancelable request(Request request, RequestCb requestCb);

    public int compareTo(Session session) {
        return ConnType.compare(this.mConnType, session.mConnType);
    }

    public Session(Context context, ConnInfo info) {
        this.mContext = context;
        this.mIp = info.getIp();
        this.mPort = info.getPort();
        this.mConnType = info.getConnType();
        this.mHost = info.getHost();
        this.mRealHost = this.mHost.substring(this.mHost.indexOf(HttpConstant.SCHEME_SPLIT) + 3);
        this.mReqTimeout = info.getReadTimeout();
        this.mConnTimeout = info.getConnectionTimeout();
        this.mConnStrategy = info.strategy;
        this.mSeq = info.getSeq();
        this.mSessionStat = new SessionStatistic(info);
        this.mSessionStat.host = this.mRealHost;
    }

    public void sendCustomFrame(int dataId, byte[] data, int type) {
    }

    public void checkAvailable() {
        ping(true);
    }

    public static void configTnetALog(Context context, String path, int fileSize, int fileNum) {
        SpdyAgent agent = SpdyAgent.getInstance(context, SpdyVersion.SPDY3, SpdySessionKind.NONE_SESSION);
        if (agent == null || !SpdyAgent.checkLoadSucc()) {
            ALog.e("agent null or configTnetALog load so fail!!!", (String) null, "loadso", Boolean.valueOf(SpdyAgent.checkLoadSucc()));
            return;
        }
        agent.configLogFile(path, fileSize, fileNum);
    }

    public void connect() {
    }

    public void close(boolean autoReCreate2) {
        this.autoReCreate = autoReCreate2;
        close();
    }

    public void ping(boolean force) {
    }

    public void registerEventcb(int eventType, EventCb callback) {
        if (this.mEventCallBacks != null) {
            this.mEventCallBacks.put(callback, Integer.valueOf(eventType));
        }
    }

    /* access modifiers changed from: protected */
    public void unReceiveEventCb(EventCb callback) {
        if (this.mEventCallBacks != null) {
            this.mEventCallBacks.remove(callback);
        }
    }

    public String getIp() {
        return this.mIp;
    }

    public int getPort() {
        return this.mPort;
    }

    public ConnType getConnType() {
        return this.mConnType;
    }

    public String getHost() {
        return this.mHost;
    }

    public String getRealHost() {
        return this.mRealHost;
    }

    public IConnStrategy getConnStrategy() {
        return this.mConnStrategy;
    }

    public String getUnit() {
        return this.unit;
    }

    /* access modifiers changed from: protected */
    public void handleCallbacks(final EventType eventType, final Event event) {
        ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
            public void run() {
                try {
                    if (Session.this.mEventCallBacks != null && eventType != null) {
                        for (EventCb callback : Session.this.mEventCallBacks.keySet()) {
                            if (callback != null) {
                                if ((eventType.getType() & Session.this.mEventCallBacks.get(callback).intValue()) != 0) {
                                    try {
                                        callback.onEvent(Session.this, eventType, event);
                                    } catch (Exception e) {
                                        ALog.e(Session.TAG, e.toString(), Session.this.mSeq, new Object[0]);
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e2) {
                    ALog.e(Session.TAG, "handleCallbacks", Session.this.mSeq, e2, new Object[0]);
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onDisconnect() {
    }

    /* access modifiers changed from: protected */
    public synchronized void notifyStatus(Status status, Event event) {
        ALog.e(TAG, "notifyStatus", this.mSeq, "status", status.name());
        if (!status.equals(this.mStatus)) {
            this.mStatus = status;
            switch (this.mStatus) {
                case CONNECTING:
                case DISCONNECTING:
                case AUTHING:
                    break;
                case CONNECTED:
                    handleCallbacks(EventType.CONNECTED, event);
                    break;
                case CONNETFAIL:
                    handleCallbacks(EventType.CONNECT_FAIL, event);
                    break;
                case DISCONNECTED:
                    onDisconnect();
                    if (!this.mIsConnTimeOut) {
                        handleCallbacks(EventType.DISCONNECTED, event);
                        break;
                    }
                    break;
                case AUTH_SUCC:
                    this.unit = StrategyCenter.getInstance().getUnitByHost(this.mRealHost);
                    handleCallbacks(EventType.AUTH_SUCC, event);
                    break;
                case AUTH_FAIL:
                    handleCallbacks(EventType.AUTH_FAIL, event);
                    break;
            }
        } else {
            ALog.i(TAG, "ignore notifyStatus", this.mSeq, new Object[0]);
        }
    }

    /* access modifiers changed from: protected */
    public void setPingTimeout() {
        if (this.mRecvTimeOutRunnable == null) {
            this.mRecvTimeOutRunnable = getRecvTimeOutRunnable();
        }
        cancelTimeout();
        if (this.mRecvTimeOutRunnable != null) {
            this.timeoutTaskFuture = ThreadPoolExecutorFactory.submitScheduledTask(this.mRecvTimeOutRunnable, Constants.RECV_TIMEOUT, TimeUnit.MILLISECONDS);
        }
    }

    /* access modifiers changed from: protected */
    public void cancelTimeout() {
        if (this.mRecvTimeOutRunnable != null && this.timeoutTaskFuture != null) {
            this.timeoutTaskFuture.cancel(true);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Session@[");
        sb.append(this.mSeq).append('|').append(this.mConnType).append(']');
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void handleResponseCode(Request request, int statusCode) {
        if (request.getHeaders().containsKey("x-pv") && statusCode >= 500 && statusCode < 600) {
            synchronized (this) {
                if (this.errorTimeList == null) {
                    this.errorTimeList = new LinkedList();
                }
                if (this.errorTimeList.size() < 5) {
                    this.errorTimeList.add(Long.valueOf(System.currentTimeMillis()));
                } else {
                    long first = this.errorTimeList.remove(0).longValue();
                    long now = System.currentTimeMillis();
                    if (now - first <= 60000) {
                        StrategyCenter.getInstance().forceRefreshStrategy(request.getHost());
                        this.errorTimeList.clear();
                    } else {
                        this.errorTimeList.add(Long.valueOf(now));
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void handleResponseHeaders(Request request, Map<String, List<String>> headers) {
        try {
            if (StrategyCenter.getInstance().getUnitByHost(request.getHost()) != null && headers.containsKey(HttpConstant.X_SWITCH_UNIT)) {
                String unit2 = HttpHelper.getSingleHeaderFieldByKey(headers, HttpConstant.X_SWITCH_UNIT);
                if (TextUtils.isEmpty(unit2)) {
                    unit2 = null;
                }
                if (!StringUtils.isStringEqual(this.unit, unit2)) {
                    long now = System.currentTimeMillis();
                    if (now - this.lastAmdcRequestSend > 60000) {
                        StrategyCenter.getInstance().forceRefreshStrategy(request.getHost());
                        this.lastAmdcRequestSend = now;
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
