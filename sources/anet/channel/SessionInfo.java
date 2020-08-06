package anet.channel;

import anet.channel.heartbeat.IHeartbeat;

public class SessionInfo {
    public final IAuth auth;
    public final DataFrameCb dataFrameCb;
    public final IHeartbeat heartbeat;
    public final String host;
    public final boolean isAccs;
    public final boolean isKeepAlive;

    public static SessionInfo create(String host2, boolean isKeepAlive2, boolean isAccs2, IAuth auth2, IHeartbeat heartbeat2, DataFrameCb dataFrameCb2) {
        return new SessionInfo(host2, isKeepAlive2, isAccs2, auth2, heartbeat2, dataFrameCb2);
    }

    private SessionInfo(String host2, boolean isKeepAlive2, boolean isAccs2, IAuth auth2, IHeartbeat heartbeat2, DataFrameCb dataFrameCb2) {
        this.host = host2;
        this.isAccs = isAccs2;
        this.auth = auth2;
        this.isKeepAlive = isKeepAlive2;
        this.heartbeat = heartbeat2;
        this.dataFrameCb = dataFrameCb2;
    }
}
