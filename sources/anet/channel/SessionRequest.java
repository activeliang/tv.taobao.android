package anet.channel;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.entity.ConnInfo;
import anet.channel.entity.ConnType;
import anet.channel.entity.Event;
import anet.channel.entity.EventCb;
import anet.channel.entity.EventType;
import anet.channel.session.HttpSession;
import anet.channel.session.TnetSpdySession;
import anet.channel.statist.AlarmObject;
import anet.channel.statist.SessionConnStat;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.ConnEvent;
import anet.channel.strategy.IConnStrategy;
import anet.channel.strategy.StrategyCenter;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpUrl;
import anet.channel.util.SessionSeq;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class SessionRequest {
    private static final String TAG = "awcn.SessionRequest";
    SessionConnStat connStat = null;
    volatile Session connectingSession;
    volatile boolean isConnecting = false;
    volatile boolean isToClose = false;
    private Object locked = new Object();
    /* access modifiers changed from: private */
    public String mHost;
    private String mRealHost;
    /* access modifiers changed from: private */
    public SessionCenter sessionCenter;
    private SessionInfo sessionInfo;
    /* access modifiers changed from: private */
    public SessionPool sessionPool;
    private volatile Future timeoutTask;

    private interface IConnCb {
        void onDisConnect(Session session, long j, EventType eventType);

        void onFailed(Session session, long j, EventType eventType, int i);

        void onSuccess(Session session, long j);
    }

    SessionRequest(String host, SessionCenter sessionCenter2) {
        this.mHost = host;
        this.mRealHost = this.mHost.substring(this.mHost.indexOf(HttpConstant.SCHEME_SPLIT) + 3);
        this.sessionCenter = sessionCenter2;
        this.sessionInfo = sessionCenter2.attributeManager.getSessionInfo(this.mRealHost);
        this.sessionPool = sessionCenter2.sessionPool;
    }

    /* access modifiers changed from: protected */
    public String getHost() {
        return this.mHost;
    }

    /* access modifiers changed from: private */
    public void setConnecting(boolean b) {
        this.isConnecting = b;
        if (!b) {
            if (this.timeoutTask != null) {
                this.timeoutTask.cancel(true);
                this.timeoutTask = null;
            }
            this.connectingSession = null;
        }
    }

    private class ConnectTimeoutTask implements Runnable {
        String seq = null;

        ConnectTimeoutTask(String seq2) {
            this.seq = seq2;
        }

        public void run() {
            if (SessionRequest.this.isConnecting) {
                ALog.e(SessionRequest.TAG, "Connecting timeout!!! reset status!", this.seq, new Object[0]);
                SessionRequest.this.connStat.ret = 2;
                SessionRequest.this.connStat.totalTime = System.currentTimeMillis() - SessionRequest.this.connStat.start;
                if (SessionRequest.this.connectingSession != null) {
                    SessionRequest.this.connectingSession.tryNextWhenFail = false;
                    SessionRequest.this.connectingSession.close();
                    SessionRequest.this.connStat.syncValueFromSession(SessionRequest.this.connectingSession);
                }
                AppMonitor.getInstance().commitStat(SessionRequest.this.connStat);
                SessionRequest.this.setConnecting(false);
            }
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void start(Context context, ConnType.TypeLevel typeClass, String seq) throws NoNetworkException, NoAvailStrategyException {
        if (this.sessionPool.getSession(this, typeClass) != null) {
            ALog.d(TAG, "Available Session exist!!!", seq, new Object[0]);
        } else {
            if (TextUtils.isEmpty(seq)) {
                seq = SessionSeq.createSequenceNo((String) null);
            }
            ALog.d(TAG, "SessionRequest start", seq, "host", this.mHost, "type", typeClass);
            if (this.isConnecting) {
                ALog.d(TAG, "session is connecting, return", seq, "host", getHost());
            } else {
                setConnecting(true);
                this.timeoutTask = ThreadPoolExecutorFactory.submitScheduledTask(new ConnectTimeoutTask(seq), 45, TimeUnit.SECONDS);
                this.connStat = new SessionConnStat();
                this.connStat.start = System.currentTimeMillis();
                if (!NetworkStatusHelper.isConnected()) {
                    if (ALog.isPrintLog(1)) {
                        ALog.d(TAG, "network is not available, can't create session", seq, "NetworkStatusHelper.isConnected()", Boolean.valueOf(NetworkStatusHelper.isConnected()));
                    }
                    finish();
                    throw new NoNetworkException(this);
                }
                List<IConnStrategy> strategyList = getAvailStrategy(typeClass, seq);
                if (strategyList.isEmpty()) {
                    ALog.i(TAG, "no avalible strategy, can't create session", seq, "host", this.mHost, "type", typeClass);
                    finish();
                    throw new NoAvailStrategyException(this);
                }
                List<ConnInfo> connInfoList = getConnInfoList(strategyList, seq);
                try {
                    ConnInfo connInfo = connInfoList.remove(0);
                    createSession(context, connInfo, new ConnCb(context, connInfoList, connInfo), connInfo.getSeq());
                } catch (Throwable th) {
                    finish();
                }
            }
        }
        return;
    }

    class ConnCb implements IConnCb {
        private ConnInfo connInfo;
        /* access modifiers changed from: private */
        public Context context;
        boolean isHandleFinish = false;
        private List<ConnInfo> strategys;

        ConnCb(Context context2, List<ConnInfo> strategyList, ConnInfo connInfo2) {
            this.context = context2;
            this.strategys = strategyList;
            this.connInfo = connInfo2;
        }

        public void onFailed(Session session, long start, EventType type, int errorcode) {
            if (ALog.isPrintLog(1)) {
                ALog.d(SessionRequest.TAG, "Connect failed", this.connInfo.getSeq(), "session", session, "host", SessionRequest.this.getHost(), "isHandleFinish", Boolean.valueOf(this.isHandleFinish));
            }
            if (SessionRequest.this.isToClose) {
                SessionRequest.this.isToClose = false;
            } else if (!this.isHandleFinish) {
                this.isHandleFinish = true;
                SessionRequest.this.sessionPool.remove(SessionRequest.this, session);
                if (!session.tryNextWhenFail || !NetworkStatusHelper.isConnected() || this.strategys.isEmpty()) {
                    SessionRequest.this.finish();
                    if (EventType.CONNECT_FAIL.equals(type) && errorcode != -2613 && errorcode != -2601) {
                        AlarmObject alarmObject = new AlarmObject();
                        alarmObject.module = "networkPrefer";
                        alarmObject.modulePoint = "policy";
                        alarmObject.arg = SessionRequest.this.mHost;
                        alarmObject.errorCode = String.valueOf(errorcode);
                        alarmObject.isSuccess = false;
                        AppMonitor.getInstance().commitAlarm(alarmObject);
                        SessionRequest.this.connStat.ret = 0;
                        SessionRequest.this.connStat.appendErrorTrace(errorcode);
                        SessionRequest.this.connStat.errorCode = String.valueOf(errorcode);
                        SessionRequest.this.connStat.totalTime = System.currentTimeMillis() - SessionRequest.this.connStat.start;
                        SessionRequest.this.connStat.syncValueFromSession(session);
                        AppMonitor.getInstance().commitStat(SessionRequest.this.connStat);
                        return;
                    }
                    return;
                }
                if (ALog.isPrintLog(1)) {
                    ALog.d(SessionRequest.TAG, "use next connInfo to create session", this.connInfo.getSeq(), "host", SessionRequest.this.getHost());
                }
                if (this.connInfo.retryTime == this.connInfo.maxRetryTime && (errorcode == -2003 || errorcode == -2410)) {
                    ListIterator<ConnInfo> itr = this.strategys.listIterator();
                    while (itr.hasNext()) {
                        if (session.getIp().equals(itr.next().strategy.getIp())) {
                            itr.remove();
                        }
                    }
                }
                ConnInfo ci = this.strategys.remove(0);
                SessionRequest.this.createSession(this.context, ci, new ConnCb(this.context, this.strategys, ci), ci.getSeq());
            }
        }

        public void onSuccess(Session session, long start) {
            ALog.d(SessionRequest.TAG, "Connect Success", this.connInfo.getSeq(), "session", session, "host", SessionRequest.this.getHost());
            try {
                if (SessionRequest.this.isToClose) {
                    SessionRequest.this.isToClose = false;
                    session.close(false);
                    return;
                }
                SessionRequest.this.sessionPool.add(SessionRequest.this, session);
                AlarmObject alarmObject = new AlarmObject();
                alarmObject.module = "networkPrefer";
                alarmObject.modulePoint = "policy";
                alarmObject.arg = SessionRequest.this.mHost;
                alarmObject.isSuccess = true;
                AppMonitor.getInstance().commitAlarm(alarmObject);
                SessionRequest.this.connStat.syncValueFromSession(session);
                SessionRequest.this.connStat.ret = 1;
                SessionRequest.this.connStat.totalTime = System.currentTimeMillis() - SessionRequest.this.connStat.start;
                AppMonitor.getInstance().commitStat(SessionRequest.this.connStat);
                SessionRequest.this.finish();
            } catch (Exception e) {
                ALog.e(SessionRequest.TAG, "[onSuccess]:", this.connInfo.getSeq(), e, new Object[0]);
            } finally {
                SessionRequest.this.finish();
            }
        }

        public void onDisConnect(final Session session, long start, EventType type) {
            boolean isbg = GlobalAppRuntimeInfo.isAppBackground();
            ALog.d(SessionRequest.TAG, "Connect Disconnect", this.connInfo.getSeq(), "session", session, "host", SessionRequest.this.getHost(), "appIsBg", Boolean.valueOf(isbg), "isHandleFinish", Boolean.valueOf(this.isHandleFinish));
            SessionRequest.this.sessionPool.remove(SessionRequest.this, session);
            if (!this.isHandleFinish) {
                this.isHandleFinish = true;
                if (!session.autoReCreate) {
                    return;
                }
                if (isbg) {
                    ALog.e(SessionRequest.TAG, "[onDisConnect]app background, don't Recreate", this.connInfo.getSeq(), "session", session);
                } else if (!NetworkStatusHelper.isConnected()) {
                    ALog.e(SessionRequest.TAG, "[onDisConnect]no network, don't Recreate", this.connInfo.getSeq(), "session", session);
                } else {
                    try {
                        ALog.d(SessionRequest.TAG, "session disconnected, try to recreate session", this.connInfo.getSeq(), new Object[0]);
                        ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
                            public void run() {
                                try {
                                    SessionRequest.this.start(ConnCb.this.context, session.getConnType().getTypeLevel(), SessionSeq.createSequenceNo(SessionRequest.this.sessionCenter.seqNum));
                                } catch (Exception e) {
                                }
                            }
                        }, (long) (Math.random() * 10.0d * 1000.0d), TimeUnit.MILLISECONDS);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    private List<IConnStrategy> getAvailStrategy(ConnType.TypeLevel typeClass, String seq) {
        List<IConnStrategy> strategies = Collections.EMPTY_LIST;
        try {
            HttpUrl httpUrl = HttpUrl.parse(getHost());
            if (httpUrl == null) {
                return Collections.EMPTY_LIST;
            }
            strategies = StrategyCenter.getInstance().getConnStrategyListByHost(httpUrl.host());
            if (!strategies.isEmpty()) {
                boolean isSsl = "https".equalsIgnoreCase(httpUrl.scheme());
                ListIterator<IConnStrategy> iterator = strategies.listIterator();
                while (iterator.hasNext()) {
                    ConnType connType = ConnType.valueOf(iterator.next().getProtocol());
                    if (!(connType.isSSL() == isSsl && (typeClass == null || connType.getTypeLevel() == typeClass))) {
                        iterator.remove();
                    }
                }
            }
            if (ALog.isPrintLog(1)) {
                ALog.d(TAG, "[getAvailStrategy]", seq, "strategies", strategies);
            }
            return strategies;
        } catch (Throwable t) {
            ALog.e(TAG, "", seq, t, new Object[0]);
        }
    }

    private List<ConnInfo> getConnInfoList(List<IConnStrategy> connStrategyList, String seq) {
        if (connStrategyList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<ConnInfo> connInfos = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < connStrategyList.size(); i++) {
            IConnStrategy strategy = connStrategyList.get(i);
            int maxRetry = strategy.getRetryTimes();
            for (int j = 0; j <= maxRetry; j++) {
                index++;
                ConnInfo connInfo = new ConnInfo(getHost(), seq + "_" + index, strategy);
                connInfo.retryTime = j;
                connInfo.maxRetryTime = maxRetry;
                connInfos.add(connInfo);
            }
        }
        return connInfos;
    }

    /* access modifiers changed from: private */
    public void createSession(Context context, ConnInfo info, IConnCb connCb, String seq) {
        ConnType type = info.getConnType();
        if (context == null || type.isHttpType()) {
            this.connectingSession = new HttpSession(context, info);
        } else {
            this.connectingSession = new TnetSpdySession(context, info, this.sessionCenter.config, this.sessionInfo, this.sessionCenter.attributeManager.getPublicKey(this.mRealHost));
        }
        ALog.i(TAG, "create connection...", seq, "Host", getHost(), "Type", info.getConnType(), "IP", info.getIp(), "Port", Integer.valueOf(info.getPort()), "heartbeat", Integer.valueOf(info.getHeartbeat()), "session", this.connectingSession);
        registerEvent(this.connectingSession, connCb, System.currentTimeMillis(), seq);
        this.connectingSession.connect();
        this.connStat.retryTimes++;
        this.connStat.startConnect = System.currentTimeMillis();
    }

    private void registerEvent(final Session session, final IConnCb connCb, final long start, String seq) {
        if (connCb != null) {
            session.registerEventcb(EventType.ALL.getType(), new EventCb() {
                public void onEvent(Session s, EventType type, Event event) {
                    String str;
                    String str2;
                    String str3;
                    if (s != null && type != null) {
                        int errorcode = event == null ? 0 : event.errorCode;
                        String errormsg = event == null ? "" : event.errorDetail;
                        switch (AnonymousClass3.$SwitchMap$anet$channel$entity$EventType[type.ordinal()]) {
                            case 1:
                                if (s != null) {
                                    str3 = s.mSeq;
                                } else {
                                    str3 = null;
                                }
                                ALog.d(SessionRequest.TAG, (String) null, str3, "Session", s, "EventType", type, "Event", event);
                                SessionRequest.this.sendConnectInfoBroadCastToAccs(s, 0, (String) null);
                                connCb.onSuccess(s, start);
                                return;
                            case 2:
                                if (s != null) {
                                    str2 = s.mSeq;
                                } else {
                                    str2 = null;
                                }
                                ALog.d(SessionRequest.TAG, (String) null, str2, "Session", s, "EventType", type, "Event", event);
                                SessionRequest.this.sendConnectInfoBroadCastToAccs(s, errorcode, errormsg);
                                if (SessionRequest.this.sessionPool.containsValue(SessionRequest.this, s)) {
                                    connCb.onDisConnect(s, start, type);
                                    return;
                                } else {
                                    connCb.onFailed(s, start, type, errorcode);
                                    return;
                                }
                            case 3:
                                if (s != null) {
                                    str = s.mSeq;
                                } else {
                                    str = null;
                                }
                                ALog.d(SessionRequest.TAG, (String) null, str, "Session", s, "EventType", type, "Event", event);
                                SessionRequest.this.sendConnectInfoBroadCastToAccs(s, errorcode, errormsg);
                                connCb.onFailed(s, start, type, errorcode);
                                return;
                            default:
                                return;
                        }
                    }
                }
            });
            session.registerEventcb(EventType.AUTH_SUCC.getType() | EventType.CONNECT_FAIL.getType() | EventType.AUTH_FAIL.getType(), new EventCb() {
                public void onEvent(Session conn, EventType type, Event event) {
                    ALog.d(SessionRequest.TAG, "Receive session event", (String) null, "type", type);
                    ConnEvent connEvent = new ConnEvent();
                    if (type == EventType.AUTH_SUCC) {
                        connEvent.isSuccess = true;
                    }
                    StrategyCenter.getInstance().notifyConnEvent(session.getRealHost(), session.getConnStrategy(), connEvent);
                }
            });
        }
    }

    /* renamed from: anet.channel.SessionRequest$3  reason: invalid class name */
    static /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$anet$channel$entity$EventType = new int[EventType.values().length];

        static {
            try {
                $SwitchMap$anet$channel$entity$EventType[EventType.AUTH_SUCC.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$anet$channel$entity$EventType[EventType.DISCONNECTED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$anet$channel$entity$EventType[EventType.CONNECT_FAIL.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void closeSessions(boolean autoCreate) {
        ALog.d(TAG, "closeSessions", (String) null, "host", this.mHost, "autoCreate", Boolean.valueOf(autoCreate));
        if (!autoCreate && this.connectingSession != null) {
            this.connectingSession.tryNextWhenFail = false;
            this.connectingSession.close(false);
        }
        List<Session> sessions = this.sessionPool.getSessions(this);
        if (sessions != null) {
            for (Session session : sessions) {
                if (session != null) {
                    session.close(autoCreate);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void reCreateSession(String seq) {
        ALog.d(TAG, "reCreateSession", seq, "host", this.mHost);
        closeSessions(true);
    }

    /* access modifiers changed from: protected */
    public void await(long timeoutMs) throws InterruptedException, TimeoutException {
        ALog.d(TAG, "[await]", (String) null, "timeoutMs", Long.valueOf(timeoutMs));
        if (timeoutMs > 0) {
            synchronized (this.locked) {
                long timeout = System.currentTimeMillis() + timeoutMs;
                while (this.isConnecting) {
                    long cur = System.currentTimeMillis();
                    if (cur >= timeout) {
                        break;
                    }
                    this.locked.wait(timeout - cur);
                }
                if (this.isConnecting) {
                    throw new TimeoutException();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public ConnType.TypeLevel getConnectingType() {
        Session s = this.connectingSession;
        if (s != null) {
            return s.mConnType.getTypeLevel();
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void finish() {
        setConnecting(false);
        synchronized (this.locked) {
            this.locked.notifyAll();
        }
    }

    /* access modifiers changed from: private */
    public void sendConnectInfoBroadCastToAccs(Session s, int errorCode, String errorDetail) {
        Context context = GlobalAppRuntimeInfo.getContext();
        if (context != null && this.sessionInfo != null && this.sessionInfo.isAccs) {
            try {
                Intent intent = new Intent("com.taobao.accs.intent.action.RECEIVE");
                intent.setPackage(context.getPackageName());
                intent.setClassName(context, "com.taobao.accs.data.MsgDistributeService");
                intent.putExtra("command", 103);
                intent.putExtra("host", s.getHost());
                intent.putExtra("is_center_host", true);
                boolean available = s.isAvailable();
                if (!available) {
                    intent.putExtra("errorCode", errorCode);
                    intent.putExtra("errorDetail", errorDetail);
                }
                intent.putExtra("connect_avail", available);
                intent.putExtra("type_inapp", true);
                context.startService(intent);
            } catch (Throwable t) {
                ALog.e(TAG, "sendConnectInfoBroadCastToAccs", (String) null, t, new Object[0]);
            }
        }
    }
}
