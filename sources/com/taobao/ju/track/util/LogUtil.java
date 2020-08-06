package com.taobao.ju.track.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LogUtil {
    private static final String DEFAULT_PREFIX = "LogUtil";
    private static final String LAST_TIME = "last_time";
    private static final String PRINT_INTERVAL = "print_interval";
    private static final String TIMES = "times";
    private static final String TOTAL_TIME = "total_time";
    private static final boolean isDebug = true;
    private static Map<String, Map> mAvgTime = new HashMap();
    private static long time = 0;

    public static void avgTimeStart(String tag, long printInterval, Object... msg) {
        Map map = mAvgTime.get(tag);
        if (map == null) {
            map = new HashMap();
            map.put(TOTAL_TIME, 0L);
            map.put(PRINT_INTERVAL, Long.valueOf(printInterval));
            map.put(TIMES, 0L);
            mAvgTime.put(tag, map);
        }
        map.put(LAST_TIME, Long.valueOf(System.currentTimeMillis()));
    }

    public static void avgTimeEnd(String tag, Object... msg) {
        Map map = mAvgTime.get(tag);
        if (map != null) {
            long totalTime = (Long.valueOf(String.valueOf(map.get(TOTAL_TIME))).longValue() + System.currentTimeMillis()) - Long.valueOf(String.valueOf(map.get(LAST_TIME))).longValue();
            map.put(TOTAL_TIME, Long.valueOf(totalTime));
            long printInterval = Long.valueOf(String.valueOf(map.get(PRINT_INTERVAL))).longValue();
            long times = Long.valueOf(String.valueOf(map.get(TIMES))).longValue() + 1;
            if (times >= printInterval) {
                Log.d(DEFAULT_PREFIX, tag + " end " + (((double) totalTime) / ((double) printInterval)) + " " + getMsg(msg));
                mAvgTime.put(tag, (Object) null);
                return;
            }
            map.put(TIMES, Long.valueOf(times));
        }
    }

    public static void timeStart(String tag, Object... msg) {
        time = System.currentTimeMillis();
        Log.d(DEFAULT_PREFIX, tag + " start " + getMsg(msg));
    }

    public static void timeEnd(String tag, Object... msg) {
        Log.d(DEFAULT_PREFIX, tag + " end " + (System.currentTimeMillis() - time) + " " + getMsg(msg));
    }

    public static void d(String tag, Object... msg) {
        Log.d(DEFAULT_PREFIX, tag + "  " + getMsg(msg));
    }

    public static void fd(String filePath, String tag, Object... msg) {
        toFile(filePath, tag, msg);
    }

    public static void toast(Context context, Object... msg) {
        Toast.makeText(context, getMsg(new Object[0]), 0).show();
    }

    private static String getMsg(Object... msg) {
        StringBuffer sb = new StringBuffer();
        if (msg != null) {
            for (Object s : msg) {
                sb.append(s).append(" ");
            }
        }
        return sb.toString();
    }

    private static void toFile(String filePath, String tag, Object... msg) {
        File logFile = null;
        try {
            logFile = createFile(filePath, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (logFile != null) {
            try {
                BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                buf.append(tag + "  " + getMsg(msg));
                buf.newLine();
                buf.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    private static File createFile(String filepath, boolean recursion) throws IOException {
        File f = null;
        if (!TextUtils.isEmpty(filepath)) {
            f = new File(filepath);
        }
        if (f != null && !f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                if (!recursion) {
                    throw e;
                }
                File parent = f.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                try {
                    f.createNewFile();
                } catch (IOException e1) {
                    throw e1;
                }
            }
        }
        return f;
    }
}
