package anet.channel;

import android.text.TextUtils;
import anet.channel.entity.ConnType;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.StrategyCenter;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anet.channel.util.StringUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

class AccsSessionManager {
    private static final String TAG = "awcn.AccsSessionManager";
    SessionCenter instance = null;
    Set<String> lastKeys = Collections.EMPTY_SET;

    AccsSessionManager(SessionCenter instance2) {
        this.instance = instance2;
    }

    public synchronized void checkAndStartSession() {
        Collection<SessionInfo> sessionInfos = this.instance.attributeManager.getSessionInfos();
        Set<String> newKeys = Collections.EMPTY_SET;
        if (!sessionInfos.isEmpty()) {
            newKeys = new TreeSet<>();
        }
        for (SessionInfo sessionInfo : sessionInfos) {
            if (sessionInfo.isKeepAlive) {
                newKeys.add(StringUtils.concatString(StrategyCenter.getInstance().getSchemeByHost(sessionInfo.host, sessionInfo.isAccs ? "https" : "http"), HttpConstant.SCHEME_SPLIT, sessionInfo.host));
            }
        }
        for (String s : this.lastKeys) {
            if (!newKeys.contains(s)) {
                closeSessions(s);
            }
        }
        if (isNeedCheckSession()) {
            for (String s2 : newKeys) {
                try {
                    this.instance.get(s2, ConnType.TypeLevel.SPDY, 0);
                } catch (Exception e) {
                    ALog.e("start session failed", (String) null, "host", s2);
                }
            }
            this.lastKeys = newKeys;
        }
    }

    public synchronized void forceReCreateSession() {
        forceCloseSession(true);
    }

    public synchronized void forceCloseSession(boolean recreate) {
        if (ALog.isPrintLog(1)) {
            ALog.d(TAG, "forceCloseSession", this.instance.seqNum, "reCreate", Boolean.valueOf(recreate));
        }
        for (String host : this.lastKeys) {
            closeSessions(host);
        }
        if (recreate) {
            checkAndStartSession();
        }
    }

    private boolean isNeedCheckSession() {
        if (GlobalAppRuntimeInfo.isAppBackground()) {
            ALog.d(TAG, "app is background not need check accs session, return", this.instance.seqNum, "bg", true);
            return false;
        } else if (NetworkStatusHelper.isConnected()) {
            return true;
        } else {
            ALog.d(TAG, "network is not available, not need check accs session, return", this.instance.seqNum, "network", Boolean.valueOf(NetworkStatusHelper.isConnected()));
            return false;
        }
    }

    private void closeSessions(String key) {
        if (!TextUtils.isEmpty(key)) {
            ALog.d(TAG, "closeSessions", this.instance.seqNum, "host", key);
            this.instance.getSessionRequest(key).closeSessions(false);
        }
    }
}
