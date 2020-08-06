package com.taobao.wireless.trade.mcart.sdk.utils;

import com.taobao.tao.purchase.inject.ExternalInject;
import com.taobao.wireless.trade.mcart.sdk.protocol.LogProtocol;

public class CartLogProfiler {
    @ExternalInject
    public static LogProtocol mLogProfiler;

    public static void i(String tag, String content) {
        if (mLogProfiler != null) {
            mLogProfiler.i(tag, content);
        }
    }

    public static void i(String tag, String... content) {
        if (mLogProfiler != null) {
            mLogProfiler.i(tag, content);
        }
    }

    public static void w(String tag, String content) {
        if (mLogProfiler != null) {
            mLogProfiler.w(tag, content);
        }
    }

    public static void w(String tag, String... content) {
        if (mLogProfiler != null) {
            mLogProfiler.w(tag, content);
        }
    }

    public static void v(String tag, String content) {
        if (mLogProfiler != null) {
            mLogProfiler.v(tag, content);
        }
    }

    public static void v(String tag, String... content) {
        if (mLogProfiler != null) {
            mLogProfiler.v(tag, content);
        }
    }

    public static void e(String tag, String content) {
        if (mLogProfiler != null) {
            mLogProfiler.e(tag, content);
        }
    }

    public static void e(String tag, String... content) {
        if (mLogProfiler != null) {
            mLogProfiler.e(tag, content);
        }
    }

    public static void d(String tag, String content) {
        if (mLogProfiler != null) {
            mLogProfiler.d(tag, content);
        }
    }

    public static void d(String tag, String... content) {
        if (mLogProfiler != null) {
            mLogProfiler.d(tag, content);
        }
    }
}
