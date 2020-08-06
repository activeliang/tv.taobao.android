package anet.channel.session;

import android.content.Context;
import anet.channel.AwcnConfig;
import anet.channel.RequestCb;
import anet.channel.Session;
import anet.channel.bytes.ByteArray;
import anet.channel.entity.ConnInfo;
import anet.channel.entity.ConnType;
import anet.channel.entity.ConnectedEvent;
import anet.channel.entity.Event;
import anet.channel.entity.EventType;
import anet.channel.request.Cancelable;
import anet.channel.request.FutureCancelable;
import anet.channel.request.Request;
import anet.channel.session.HttpConnector;
import anet.channel.statist.RequestStatistic;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpHelper;
import anet.channel.util.RequestPriorityTable;
import anet.channel.util.TlsSniSocketFactory;
import anet.channel.util.Utils;
import java.util.List;
import java.util.Map;
import javax.net.ssl.SSLSocketFactory;

public class HttpSession extends Session {
    private static final String TAG = "awcn.HttpSession";
    private SSLSocketFactory sslSocketFactory;

    public HttpSession(Context context, ConnInfo info) {
        super(context, info);
        if (this.mConnStrategy == null) {
            this.mConnType = (this.mHost == null || !this.mHost.startsWith("https")) ? ConnType.HTTP : ConnType.HTTPS;
        } else if (AwcnConfig.isHttpsSniEnable() && this.mConnType.equals(ConnType.HTTPS)) {
            this.sslSocketFactory = new TlsSniSocketFactory(this.mRealHost);
        }
    }

    public boolean isAvailable() {
        return this.mStatus == Session.Status.AUTH_SUCC;
    }

    public void connect() {
        try {
            ALog.i(TAG, "HttpSession connect", (String) null, "host", this.mHost);
            Request.Builder builder = new Request.Builder().setUrl(this.mHost).setSeq(this.mSeq).setConnectTimeout((int) (((float) this.mConnTimeout) * Utils.getNetworkTimeFactor())).setReadTimeout((int) (((float) this.mReqTimeout) * Utils.getNetworkTimeFactor())).setRedirectEnable(false);
            if (this.sslSocketFactory != null) {
                builder.setSslSocketFactory(this.sslSocketFactory);
            }
            final Request request = builder.build();
            request.setDnsOptimize(this.mIp, this.mPort);
            ThreadPoolExecutorFactory.submitPriorityTask(new Runnable() {
                public void run() {
                    long start = System.currentTimeMillis();
                    HttpConnector.Response response = HttpConnector.connect(request);
                    if (response.httpCode > 0) {
                        ConnectedEvent event = new ConnectedEvent(EventType.CONNECTED);
                        event.mConnectedTime = System.currentTimeMillis() - start;
                        HttpSession.this.notifyStatus(Session.Status.AUTH_SUCC, event);
                        return;
                    }
                    HttpSession.this.handleCallbacks(EventType.CONNECT_FAIL, new Event(EventType.CONNECT_FAIL, response.httpCode, "Http connect fail"));
                }
            }, 6);
        } catch (Throwable e) {
            ALog.e(TAG, "HTTP connect fail.", (String) null, e, new Object[0]);
        }
    }

    public void close() {
        notifyStatus(Session.Status.DISCONNECTED, (Event) null);
    }

    public void close(boolean autoReCreate) {
        this.autoReCreate = false;
        close();
    }

    /* access modifiers changed from: protected */
    public Runnable getRecvTimeOutRunnable() {
        return null;
    }

    public Cancelable request(Request request, final RequestCb cb) {
        final RequestStatistic rs;
        Cancelable ret = FutureCancelable.NULL;
        if (request != null) {
            rs = request.rs;
        } else {
            rs = new RequestStatistic(this.mRealHost, (String) null);
        }
        rs.setConnType(this.mConnType);
        if (rs.start == 0) {
            rs.start = System.currentTimeMillis();
        }
        if (request == null || cb == null) {
            if (cb != null) {
                cb.onFinish(-102, ErrorConstant.getErrMsg(-102), rs);
            }
            return ret;
        }
        try {
            if (request.getSslSocketFactory() == null && this.sslSocketFactory != null) {
                request = request.newBuilder().setSslSocketFactory(this.sslSocketFactory).build();
            }
            request.setDnsOptimize(this.mIp, this.mPort);
            request.setUrlScheme(this.mConnType.isSSL());
            if (this.mConnStrategy != null) {
                request.rs.ipRefer = this.mConnStrategy.getIpSource();
                request.rs.ipType = this.mConnStrategy.getIpType();
            } else {
                request.rs.ipRefer = 1;
                request.rs.ipType = 1;
            }
            request.rs.unit = this.unit;
            final Request req = request;
            ret = new FutureCancelable(ThreadPoolExecutorFactory.submitPriorityTask(new Runnable() {
                public void run() {
                    req.rs.sendBeforeTime = System.currentTimeMillis() - req.rs.requestStart;
                    HttpConnector.connect(req, new RequestCb() {
                        public void onResponseCode(int code, Map<String, List<String>> headers) {
                            ALog.i(HttpSession.TAG, "", req.getSeq(), "httpStatusCode", Integer.valueOf(code));
                            ALog.i(HttpSession.TAG, "", req.getSeq(), "response headers", headers);
                            if (code <= 0) {
                                HttpSession.this.handleCallbacks(EventType.DISCONNECTED, new Event(EventType.DISCONNECTED, 0, "Http connect fail"));
                            }
                            cb.onResponseCode(code, headers);
                            rs.serverRT = HttpHelper.parseServerRT(headers);
                            HttpSession.this.handleResponseCode(req, code);
                            HttpSession.this.handleResponseHeaders(req, headers);
                        }

                        public void onDataReceive(ByteArray data, boolean fin) {
                            cb.onDataReceive(data, fin);
                        }

                        public void onFinish(int statusCode, String msg, RequestStatistic rs) {
                            cb.onFinish(statusCode, msg, rs);
                        }
                    });
                }
            }, RequestPriorityTable.lookup(request)), request.getSeq());
        } catch (Throwable t) {
            if (cb != null) {
                cb.onFinish(-101, ErrorConstant.formatMsg(-101, t.toString()), rs);
            }
        }
        return ret;
    }
}
