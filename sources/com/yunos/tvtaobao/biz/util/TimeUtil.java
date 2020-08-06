package com.yunos.tvtaobao.biz.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    public static boolean isBteenStartAndEnd(String start, String end) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        try {
            Date StartD = timeFormat.parse(start);
            Date endD = timeFormat.parse(end);
            boolean after = date.after(StartD);
            boolean before = date.before(endD);
            if (!after || !before) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static int compareToCurrentTime(String time) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            return new Date(System.currentTimeMillis()).compareTo(timeFormat.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static long getCurrentTime(long serverCurrentTime) {
        long clientCurrentTime = System.currentTimeMillis();
        if (serverCurrentTime > clientCurrentTime || Math.abs(serverCurrentTime - clientCurrentTime) > 10) {
            return serverCurrentTime;
        }
        return clientCurrentTime;
    }

    public static String getNativeTime() {
        return new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
    }
}
