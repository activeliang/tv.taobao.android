package com.alibaba.sdk.android.oss.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class DateUtil {
    private static final String ALTERNATIVE_ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String ISO8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String RFC822_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss 'GMT'";
    private static volatile long amendTimeSkewed = 0;

    public static String formatRfc822Date(Date date) {
        return getRfc822DateFormat().format(date);
    }

    public static Date parseRfc822Date(String dateString) throws ParseException {
        return getRfc822DateFormat().parse(dateString);
    }

    private static DateFormat getRfc822DateFormat() {
        SimpleDateFormat rfc822DateFormat = new SimpleDateFormat(RFC822_DATE_FORMAT, Locale.US);
        rfc822DateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return rfc822DateFormat;
    }

    public static String formatIso8601Date(Date date) {
        return getIso8601DateFormat().format(date);
    }

    public static String formatAlternativeIso8601Date(Date date) {
        return getAlternativeIso8601DateFormat().format(date);
    }

    public static Date parseIso8601Date(String dateString) throws ParseException {
        try {
            return getIso8601DateFormat().parse(dateString);
        } catch (ParseException e) {
            return getAlternativeIso8601DateFormat().parse(dateString);
        }
    }

    private static DateFormat getIso8601DateFormat() {
        SimpleDateFormat df = new SimpleDateFormat(ISO8601_DATE_FORMAT, Locale.US);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df;
    }

    private static DateFormat getAlternativeIso8601DateFormat() {
        SimpleDateFormat df = new SimpleDateFormat(ALTERNATIVE_ISO8601_DATE_FORMAT, Locale.US);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df;
    }

    public static long getFixedSkewedTimeMillis() {
        return System.currentTimeMillis() + amendTimeSkewed;
    }

    public static synchronized String currentFixedSkewedTimeInRFC822Format() {
        String formatRfc822Date;
        synchronized (DateUtil.class) {
            formatRfc822Date = formatRfc822Date(new Date(getFixedSkewedTimeMillis()));
        }
        return formatRfc822Date;
    }

    public static synchronized void setCurrentServerTime(long serverTime) {
        synchronized (DateUtil.class) {
            amendTimeSkewed = serverTime - System.currentTimeMillis();
        }
    }
}
