package anet.channel.heartbeat;

import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.Session;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import java.util.concurrent.TimeUnit;

public class DefaultHeartbeatImpl implements IHeartbeat, Runnable {
    private static final String TAG = "awcn.DefaultHeartbeatImpl";
    private int bgHeartbeatCount = 0;
    private volatile long executeTime = 0;
    private long interval = 0;
    private volatile boolean isCancelled = false;
    private final Session session;

    public DefaultHeartbeatImpl(Session session2) {
        this.session = session2;
        this.interval = (long) session2.getConnStrategy().getHeartbeat();
    }

    public void start() {
        ALog.i(TAG, "heartbeat start", this.session.mSeq, "session", this.session);
        submit(this.interval);
    }

    public void stop() {
        ALog.i(TAG, "heartbeat stop", this.session.mSeq, "session", this.session);
        this.isCancelled = true;
    }

    public long getInterval() {
        return this.interval;
    }

    public void heartbeat() {
        this.session.ping(true);
    }

    public void reSchedule() {
        long toDelay = System.currentTimeMillis() + this.interval;
        if (this.executeTime + 1000 < toDelay) {
            if (ALog.isPrintLog(1)) {
                ALog.d(TAG, "reSchedule", this.session.mSeq, "session", this.session, "delay", Long.valueOf(toDelay - this.executeTime));
            }
            this.executeTime = toDelay;
        }
    }

    public void run() {
        int i = 0;
        if (!this.isCancelled) {
            long now = System.currentTimeMillis();
            if (now < this.executeTime) {
                submit(this.executeTime - now);
                return;
            }
            boolean isAppBackground = GlobalAppRuntimeInfo.isAppBackground();
            if (!isAppBackground) {
                if (ALog.isPrintLog(1)) {
                    ALog.d(TAG, "heartbeat", this.session.mSeq, "session", this.session);
                }
                heartbeat();
                if (isAppBackground) {
                    i = this.bgHeartbeatCount + 1;
                }
                this.bgHeartbeatCount = i;
                submit(this.interval);
                return;
            }
            ALog.e(TAG, "close session in background", this.session.mSeq, "session", this.session);
            this.session.close(false);
        }
    }

    private void submit(long delay) {
        try {
            this.executeTime = System.currentTimeMillis() + delay;
            ThreadPoolExecutorFactory.submitScheduledTask(this, 50 + delay, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            ALog.e(TAG, "Submit heartbeat task to thread pool failed.", this.session.mSeq, e, new Object[0]);
        }
    }
}
