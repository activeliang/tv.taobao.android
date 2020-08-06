package com.tvtaobao.voicesdk.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static String dateToStamp(String time) {
        try {
            return String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).parse(time).getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateToStampYearAndMonth(String timeStamp) {
        return new SimpleDateFormat("MM-dd").format(new Date(Long.valueOf(timeStamp).longValue()));
    }

    public static String getStrTime(String timeStamp) {
        return new SimpleDateFormat("MM-dd HH:mm").format(new Date(Long.valueOf(timeStamp).longValue()));
    }
}
