package com.taobao.media;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

public class MediaTimeUtils {
    protected static StringBuilder mFormatBuilder = new StringBuilder();
    protected static Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

    public static String msStringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        }
        return mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
    }

    public static String sStringForTime(int timeMs) {
        int seconds = timeMs % 60;
        int minutes = (timeMs / 60) % 60;
        int hours = timeMs / 3600;
        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%02d:%02d:%02d", new Object[]{Integer.valueOf(hours), Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
        }
        return mFormatter.format("%02d:%02d", new Object[]{Integer.valueOf(minutes), Integer.valueOf(seconds)}).toString();
    }

    public static int getDate() {
        try {
            return Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date(System.currentTimeMillis()))).intValue();
        } catch (Exception e) {
            return 1970101;
        }
    }
}
