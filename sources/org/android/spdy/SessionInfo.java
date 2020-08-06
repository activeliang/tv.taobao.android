package org.android.spdy;

import android.taobao.windvane.util.WVNativeCallbackUtil;
import mtopsdk.common.util.SymbolExpUtil;

public class SessionInfo {
    private int connTimeoutMs = -1;
    private String domain;
    private String host;
    private int mode;
    private int port;
    private String proxyHost;
    private int proxyPort;
    private int pubkey_seqnum = 0;
    private SessionCb sessionCb;
    private Object sessionUserData;

    public SessionInfo(String host2, int port2, String domain2, String proxyHost2, int proxyPort2, Object sessionUserData2, SessionCb sessionCb2, int mode2) {
        this.host = host2;
        this.port = port2;
        this.domain = domain2;
        this.proxyHost = proxyHost2;
        this.proxyPort = proxyPort2;
        this.sessionUserData = sessionUserData2;
        this.sessionCb = sessionCb2;
        this.mode = mode2;
    }

    /* access modifiers changed from: package-private */
    public String getAuthority() {
        if (this.proxyHost == null || this.proxyPort == 0) {
            return this.host + SymbolExpUtil.SYMBOL_COLON + this.port;
        }
        return this.host + SymbolExpUtil.SYMBOL_COLON + this.port + WVNativeCallbackUtil.SEPERATER + this.proxyHost + SymbolExpUtil.SYMBOL_COLON + this.proxyPort;
    }

    /* access modifiers changed from: package-private */
    public Object getSessonUserData() {
        return this.sessionUserData;
    }

    /* access modifiers changed from: package-private */
    public SessionCb getSessionCb() {
        return this.sessionCb;
    }

    /* access modifiers changed from: package-private */
    public int getMode() {
        return this.mode;
    }

    /* access modifiers changed from: package-private */
    public String getDomain() {
        return this.domain;
    }

    public void setConnectionTimeoutMs(int timeout) {
        this.connTimeoutMs = timeout;
    }

    /* access modifiers changed from: package-private */
    public int getConnectionTimeoutMs() {
        return this.connTimeoutMs;
    }

    public void setPubKeySeqNum(int seqnum) {
        this.pubkey_seqnum = seqnum;
    }

    /* access modifiers changed from: package-private */
    public int getPubKeySeqNum() {
        return this.pubkey_seqnum;
    }
}
