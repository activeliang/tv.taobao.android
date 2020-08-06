package android.taobao.windvane.util.log;

import android.util.Log;

public class AndroidLog implements ILog {
    public void d(String tag, String msg) {
        if (tag != null && msg != null) {
            Log.d(tag, msg);
        }
    }

    public void d(String tag, String msg, Throwable tr) {
        if (tag != null && msg != null && tr != null) {
            Log.d(tag, msg, tr);
        }
    }

    public void e(String tag, String msg) {
        if (tag != null && msg != null) {
            Log.e(tag, msg);
        }
    }

    public void e(String tag, String msg, Throwable tr) {
        if (tag != null && msg != null && tr != null) {
            Log.e(tag, msg, tr);
        }
    }

    public void i(String tag, String msg) {
        if (tag != null && msg != null) {
            Log.i(tag, msg);
        }
    }

    public void i(String tag, String msg, Throwable tr) {
        if (tag != null && msg != null && tr != null) {
            Log.i(tag, msg, tr);
        }
    }

    public void v(String tag, String msg) {
        if (tag != null && msg != null) {
            Log.v(tag, msg);
        }
    }

    public void v(String tag, String msg, Throwable tr) {
        if (tag != null && msg != null && tr != null) {
            Log.v(tag, msg, tr);
        }
    }

    public void w(String tag, String msg) {
        if (tag != null && msg != null) {
            Log.w(tag, msg);
        }
    }

    public void w(String tag, String msg, Throwable tr) {
        if (tag != null && msg != null && tr != null) {
            Log.w(tag, msg, tr);
        }
    }

    public boolean isLogLevelEnabled(int level) {
        return true;
    }
}
