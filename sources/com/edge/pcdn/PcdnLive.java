package com.edge.pcdn;

import java.io.Serializable;

public class PcdnLive implements Serializable {
    private static final long serialVersionUID = 1;

    public static native String PCDNAddress(String str, String str2);

    public static native String PCDNGet(String str);

    public static native int PCDNSet(String str);

    public static native void exit();

    public static native String getVersion();

    public static native int start(String str, String str2, String str3, String str4);

    public static native void stop();
}
