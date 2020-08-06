package com.taobao.dp;

import java.security.InvalidParameterException;

@Deprecated
public final class OnlineHost {
    private static final int Custrom_Imdex = 3;
    public static final OnlineHost GENERAL = new OnlineHost("GENERAL", "ynuf.aliapp.org", 0);
    public static final OnlineHost JAPAN = new OnlineHost("JAPAN", "ynuf.aliapp.org", 2);
    public static final OnlineHost USA = new OnlineHost("USA", "us.ynuf.aliapp.org", 1);
    private String mHost;
    private int mIndex;
    private String mName;

    private OnlineHost(String name, String host, int index) {
        this.mName = name;
        this.mHost = host;
        this.mIndex = index;
    }

    public static OnlineHost valueof(String host) {
        if (host != null && host.length() != 0) {
            return new OnlineHost("", host, 3);
        }
        throw new InvalidParameterException("Invalid Host");
    }

    public String getName() {
        return this.mName;
    }

    public String getHost() {
        return this.mHost;
    }

    public int getIndex() {
        return this.mIndex;
    }
}
