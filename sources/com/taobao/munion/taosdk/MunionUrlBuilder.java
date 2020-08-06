package com.taobao.munion.taosdk;

import android.net.Uri;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.taobao.alimama.global.Constants;
import com.taobao.muniontaobaosdk.util.a;

public class MunionUrlBuilder {
    @Deprecated
    public static Uri a(Uri uri, String str, String str2) {
        if (uri == null) {
            return null;
        }
        return b(b(uri, "eurl", str), "etype", str2);
    }

    @Deprecated
    public static Uri a(Uri uri, String str, String str2, String str3) {
        if (uri == null) {
            return null;
        }
        return b(b(b(uri, "eurl", str), "etype", str2), a.i, str3);
    }

    public static Uri a(Uri uri, String str, String str2, boolean z) {
        if (str == null || str.trim().length() == 0) {
            return uri;
        }
        String queryParameter = uri.getQueryParameter(str);
        if (queryParameter == null || queryParameter.trim().length() == 0) {
            Uri.Builder buildUpon = uri.buildUpon();
            String path = uri.getPath();
            if (path == null || path.length() == 0) {
                buildUpon.appendPath("");
            }
            return buildUpon.appendQueryParameter(str, str2).build();
        } else if (!z) {
            return uri;
        } else {
            String uri2 = uri.toString();
            String replace = uri2.replace(String.format("%s=%s", new Object[]{str, queryParameter}), String.format("%s=%s", new Object[]{str, str2}));
            AppMonitor.Alarm.commitSuccess("Munion", Constants.Monitor.Points.URL_PARAM_REPACE, String.format("%s%c%s", new Object[]{uri2, 1, replace}));
            return Uri.parse(replace);
        }
    }

    @Deprecated
    public static String a(String str, String str2, String str3) {
        return (str == null || str.trim().length() == 0) ? "" : b(b(str, "eurl", str2), "etype", str3);
    }

    @Deprecated
    public static String a(String str, String str2, String str3, String str4) {
        return (str == null || str.trim().length() == 0) ? "" : b(b(b(str, "eurl", str2), "etype", str3), a.i, str4);
    }

    public static String a(String str, String str2, String str3, boolean z) {
        if (str2 == null || str2.trim().length() == 0) {
            return str;
        }
        Uri parse = Uri.parse(str);
        String queryParameter = parse.getQueryParameter(str2);
        if (queryParameter == null || queryParameter.trim().length() == 0) {
            Uri.Builder buildUpon = parse.buildUpon();
            String path = parse.getPath();
            if (path == null || path.length() == 0) {
                buildUpon.appendPath("");
            }
            return buildUpon.appendQueryParameter(str2, str3).toString();
        } else if (!z) {
            return str;
        } else {
            String replace = str.replace(String.format("%s=%s", new Object[]{str2, queryParameter}), String.format("%s=%s", new Object[]{str2, str3}));
            AppMonitor.Alarm.commitSuccess("Munion", Constants.Monitor.Points.URL_PARAM_REPACE, String.format("%s%c%s", new Object[]{str, 1, replace}));
            return replace;
        }
    }

    public static Uri appendClickidToTargetUrl(Uri uri, String str) {
        if (uri == null) {
            return null;
        }
        return a(uri, "clickid", str, true);
    }

    public static String appendClickidToTargetUrl(String str, String str2) {
        return (str == null || str.trim().length() == 0) ? "" : a(str, "clickid", str2, true);
    }

    public static Uri b(Uri uri, String str, String str2) {
        return a(uri, str, str2, false);
    }

    public static String b(String str, String str2, String str3) {
        return a(str, str2, str3, false);
    }
}
