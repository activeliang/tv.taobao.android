package com.taobao.alimama.net.core.state;

public class NetRequestRetryPolicy {
    public static final NetRequestRetryPolicy DEFAULT_NO_RETRY = new NetRequestRetryPolicy(0);
    public static final NetRequestRetryPolicy RETRY_FIVE_TIMES = new NetRequestRetryPolicy(5);
    public static final NetRequestRetryPolicy RETRY_INFINITY = new NetRequestRetryPolicy(Integer.MAX_VALUE);
    public static final NetRequestRetryPolicy RETRY_TREE_TIMES = new NetRequestRetryPolicy(3);
    public final int maxRetryCount;

    public NetRequestRetryPolicy(int i) {
        this.maxRetryCount = i;
    }
}
