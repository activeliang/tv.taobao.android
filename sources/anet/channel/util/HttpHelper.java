package anet.channel.util;

import android.text.TextUtils;
import anet.channel.request.Request;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class HttpHelper {
    static final Pattern SIMPLE_IP_PATTERN = Pattern.compile("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");

    public static boolean isIPAddress(String ip) {
        if (ip == null) {
            return false;
        }
        return SIMPLE_IP_PATTERN.matcher(ip).matches();
    }

    public static boolean checkHostValidAndNotIp(String host) {
        if (TextUtils.isEmpty(host)) {
            return false;
        }
        char[] bytes = host.toCharArray();
        if (bytes.length <= 0 || bytes.length > 255) {
            return false;
        }
        boolean containLetter = false;
        for (int i = 0; i < bytes.length; i++) {
            if ((bytes[i] >= 'A' && bytes[i] <= 'Z') || (bytes[i] >= 'a' && bytes[i] <= 'z')) {
                containLetter = true;
            } else if (!((bytes[i] >= '0' && bytes[i] <= '9') || bytes[i] == '.' || bytes[i] == '-')) {
                return false;
            }
        }
        return containLetter;
    }

    public static Map<String, List<String>> cloneMap(Map<String, List<String>> map) {
        if (map == null) {
            return null;
        }
        if (map.isEmpty()) {
            return Collections.EMPTY_MAP;
        }
        HashMap<String, List<String>> cloneMap = new HashMap<>(map.size());
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            cloneMap.put(entry.getKey(), new ArrayList(entry.getValue()));
        }
        return cloneMap;
    }

    public static List<String> getHeaderFieldByKey(Map<String, List<String>> header, String key) {
        if (header == null || header.isEmpty() || TextUtils.isEmpty(key)) {
            return null;
        }
        for (Map.Entry<String, List<String>> entry : header.entrySet()) {
            if (key.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static String getSingleHeaderFieldByKey(Map<String, List<String>> headers, String key) {
        List<String> value = getHeaderFieldByKey(headers, key);
        if (value == null || value.isEmpty()) {
            return null;
        }
        return value.get(0);
    }

    public static void removeHeaderFiledByKey(Map<String, List<String>> headers, String header) {
        if (header != null) {
            String key = null;
            Iterator<String> iterator = headers.keySet().iterator();
            while (true) {
                if (iterator.hasNext()) {
                    if (header.equalsIgnoreCase(iterator.next())) {
                        key = header;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (key != null) {
                headers.remove(key);
            }
        }
    }

    public static boolean checkRedirect(Request request, int httpCode) {
        return request.isRedirectEnable() && httpCode >= 300 && httpCode < 400 && httpCode != 304 && request.getRedirectTimes() < 10;
    }

    public static boolean checkContentEncodingGZip(Map<String, List<String>> header) {
        try {
            if ("gzip".equalsIgnoreCase(getSingleHeaderFieldByKey(header, "Content-Encoding"))) {
                return true;
            }
            return false;
        } catch (Exception e) {
        }
    }

    public static int parseContentLength(Map<String, List<String>> headers) {
        try {
            return Integer.parseInt(getSingleHeaderFieldByKey(headers, "Content-Length"));
        } catch (Exception e) {
            return 0;
        }
    }

    public static long parseServerRT(Map<String, List<String>> headers) {
        try {
            List<String> list = headers.get(HttpConstant.SERVER_RT);
            if (list != null && !list.isEmpty()) {
                return Long.parseLong(list.get(0));
            }
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public static String trySolveFileExtFromUrlPath(String path) {
        if (path == null) {
            return null;
        }
        try {
            int length = path.length();
            if (length <= 1) {
                return null;
            }
            int lastSplash = path.lastIndexOf(47);
            if (lastSplash == -1 || lastSplash == length - 1) {
                return null;
            }
            int pos = path.lastIndexOf(46);
            if (pos == -1 || pos <= lastSplash) {
                return null;
            }
            return path.substring(pos + 1, length);
        } catch (Exception e) {
            return null;
        }
    }

    @Deprecated
    public static String trySolveFileExtFromURL(URL url) {
        return trySolveFileExtFromUrlPath(url.getPath());
    }
}
