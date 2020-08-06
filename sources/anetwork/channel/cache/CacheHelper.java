package anetwork.channel.cache;

import anet.channel.util.HttpHelper;
import anetwork.channel.cache.Cache;
import com.alibaba.sdk.android.oss.common.utils.HttpHeaders;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import mtopsdk.common.util.HttpHeaderConstant;

public class CacheHelper {
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    private static final DateFormat STANDARD_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);

    static {
        STANDARD_FORMAT.setTimeZone(GMT);
    }

    public static String toGMTDate(long ttl) {
        return STANDARD_FORMAT.format(new Date(ttl));
    }

    private static long parseGMTDate(String date) {
        if (date.length() == 0) {
            return 0;
        }
        try {
            ParsePosition position = new ParsePosition(0);
            Date result = STANDARD_FORMAT.parse(date, position);
            if (position.getIndex() == date.length()) {
                return result.getTime();
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public static Cache.Entry parseCacheHeaders(Map<String, List<String>> headers) {
        long now = System.currentTimeMillis();
        long serverDate = 0;
        long lastModified = 0;
        long serverExpires = 0;
        long ttl = 0;
        long maxAge = 0;
        boolean hasCacheControl = false;
        String headerValue = HttpHelper.getSingleHeaderFieldByKey(headers, "Cache-Control");
        if (headerValue != null) {
            hasCacheControl = true;
            String[] tokens = headerValue.split(",");
            for (String trim : tokens) {
                String token = trim.trim();
                if (token.equals(HttpHeaderConstant.NO_CACHE) || token.equals("no-store")) {
                    return null;
                }
                if (token.startsWith("max-age=")) {
                    try {
                        maxAge = Long.parseLong(token.substring(8));
                    } catch (Exception e) {
                    }
                }
            }
        }
        String headerValue2 = HttpHelper.getSingleHeaderFieldByKey(headers, "Date");
        if (headerValue2 != null) {
            serverDate = parseGMTDate(headerValue2);
        }
        String headerValue3 = HttpHelper.getSingleHeaderFieldByKey(headers, HttpHeaders.EXPIRES);
        if (headerValue3 != null) {
            serverExpires = parseGMTDate(headerValue3);
        }
        String headerValue4 = HttpHelper.getSingleHeaderFieldByKey(headers, HttpHeaders.LAST_MODIFIED);
        if (headerValue4 != null) {
            lastModified = parseGMTDate(headerValue4);
        }
        String etag = HttpHelper.getSingleHeaderFieldByKey(headers, HttpHeaders.ETAG);
        if (hasCacheControl) {
            ttl = now + (1000 * maxAge);
        } else if (serverDate > 0 && serverExpires >= serverDate) {
            ttl = now + (serverExpires - serverDate);
        }
        Cache.Entry entry = new Cache.Entry();
        entry.etag = etag;
        entry.ttl = ttl;
        entry.serverDate = serverDate;
        entry.lastModified = lastModified;
        entry.responseHeaders = headers;
        return entry;
    }
}
