package com.edge.pcdn;

import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;

public class PcdnLog {
    private static final int LEVEL = 4;
    public static final String TAG = "PCDN_TAG";

    public static void d(String msg) {
    }

    public static void i(String msg) {
        Log.i(TAG, msg);
    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public static void e(String msg) {
        Log.e(TAG, msg);
    }

    public static String toString(Throwable e) {
        if (e == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
}
