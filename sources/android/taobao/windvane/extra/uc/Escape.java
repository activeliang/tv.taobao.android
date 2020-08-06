package android.taobao.windvane.extra.uc;

import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.taobao.windvane.monitor.WVMonitorConstants;
import android.taobao.windvane.monitor.WVPackageMonitorInterface;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.uc.webview.export.extension.UCCore;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import mtopsdk.common.util.SymbolExpUtil;

public class Escape {
    private static final String[] hex = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E", "0F", "10", "11", "12", "13", "14", WVMonitorConstants.FORCE_ONLINE_FAILED, WVMonitorConstants.MAPPING_URL_NULL_FAILED, WVMonitorConstants.MAPPING_URL_MATCH_FAILED, "18", "19", "1A", "1B", "1C", "1D", "1E", "1F", WVPackageMonitorInterface.NOT_INSTALL_FAILED, WVPackageMonitorInterface.FORCE_UPDATE_FAILED, WVPackageMonitorInterface.FORCE_ONLINE_FAILED, WVPackageMonitorInterface.CONFIG_CLOSED_FAILED, "24", "25", "26", "27", "28", "29", "2A", "2B", "2C", "2D", "2E", "2F", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "3A", "3B", "3C", "3D", "3E", "3F", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "4A", "4B", "4C", "4D", "4E", "4F", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "5A", "5B", "5C", "5D", "5E", "5F", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6A", "6B", "6C", "6D", "6E", "6F", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7A", "7B", "7C", "7D", "7E", "7F", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8A", "8B", "8C", "8D", "8E", "8F", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9A", "9B", "9C", "9D", "9E", "9F", "A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "A9", "AA", "AB", UCCore.OPTION_HARDWARE_ACCELERATED, "AD", "AE", "AF", "B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8", "B9", "BA", "BB", "BC", "BD", "BE", "BF", "C0", "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "C9", "CA", "CB", "CC", "CD", "CE", "CF", "D0", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9", "DA", "DB", "DC", "DD", "DE", "DF", "E0", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9", "EA", "EB", "EC", "ED", "EE", "EF", "F0", "F1", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9", "FA", "FB", "FC", "FD", "FE", "FF"};
    private static final byte[] val = {63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 63, 63, 63, 63, 63, 63, 63, 10, 11, 12, 13, 14, 15, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 10, 11, 12, 13, 14, 15, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63};

    public static String escape(String s) {
        StringBuffer sbuf = new StringBuffer();
        int len = s.length();
        for (int i = 0; i < len; i++) {
            int ch = s.charAt(i);
            if (ch == 32) {
                sbuf.append('+');
            } else if (65 <= ch && ch <= 90) {
                sbuf.append((char) ch);
            } else if (97 <= ch && ch <= 122) {
                sbuf.append((char) ch);
            } else if (48 <= ch && ch <= 57) {
                sbuf.append((char) ch);
            } else if (ch == 45 || ch == 95 || ch == 46 || ch == 33 || ch == 126 || ch == 42 || ch == 47 || ch == 40 || ch == 41) {
                sbuf.append((char) ch);
            } else if (ch <= 127) {
                sbuf.append('%');
                sbuf.append(hex[ch]);
            } else {
                sbuf.append('%');
                sbuf.append('u');
                sbuf.append(hex[ch >>> 8]);
                sbuf.append(hex[ch & 255]);
            }
        }
        return sbuf.toString();
    }

    public static String unescape(String s) {
        int i;
        int cint;
        StringBuffer sbuf = new StringBuffer();
        int i2 = 0;
        int len = s.length();
        while (i < len) {
            int ch = s.charAt(i);
            if (ch == 43) {
                sbuf.append(' ');
            } else if (65 <= ch && ch <= 90) {
                sbuf.append((char) ch);
            } else if (97 <= ch && ch <= 122) {
                sbuf.append((char) ch);
            } else if (48 <= ch && ch <= 57) {
                sbuf.append((char) ch);
            } else if (ch == 45 || ch == 95 || ch == 46 || ch == 33 || ch == 126 || ch == 42 || ch == 47 || ch == 40 || ch == 41) {
                sbuf.append((char) ch);
            } else if (ch == 37) {
                if ('u' != s.charAt(i + 1)) {
                    cint = ((val[s.charAt(i + 1)] | 0) << 4) | val[s.charAt(i + 2)];
                    i += 2;
                } else {
                    cint = ((((((val[s.charAt(i + 2)] | 0) << 4) | val[s.charAt(i + 3)]) << 4) | val[s.charAt(i + 4)]) << 4) | val[s.charAt(i + 5)];
                    i += 5;
                }
                sbuf.append((char) cint);
            }
            i2 = i + 1;
        }
        return sbuf.toString();
    }

    public static String tryDecodeUrl(String urlStr) {
        try {
            URI.create(urlStr);
            return urlStr;
        } catch (Throwable th) {
            return decodeUrl(urlStr);
        }
    }

    private static String tryDecode(String path) {
        String _path = "";
        if (TextUtils.isEmpty(path)) {
            return _path;
        }
        try {
            _path = URLDecoder.decode(path, "utf-8");
        } catch (Throwable th) {
        }
        if (TextUtils.isEmpty(_path)) {
            try {
                _path = URLDecoder.decode(path, "gbk");
            } catch (Throwable th2) {
            }
        }
        if (TextUtils.isEmpty(_path)) {
            try {
                _path = unescape(path);
            } catch (Throwable th3) {
            }
        }
        if (TextUtils.isEmpty(_path)) {
            return path;
        }
        return _path;
    }

    private static String decodeUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            StringBuffer urlBuffer = new StringBuffer();
            urlBuffer.append(url.getProtocol());
            urlBuffer.append(HttpConstant.SCHEME_SPLIT);
            urlBuffer.append(url.getHost());
            int port = url.getPort();
            if (!(port == 80 || port == -1)) {
                urlBuffer.append(SymbolExpUtil.SYMBOL_COLON + port);
            }
            String path = url.getPath();
            String query = url.getQuery();
            if (!TextUtils.isEmpty(path)) {
                boolean lastChar = path.lastIndexOf(WVNativeCallbackUtil.SEPERATER) == path.length() + -1;
                String[] paths = path.split(WVNativeCallbackUtil.SEPERATER);
                if (paths != null) {
                    urlBuffer.append(WVNativeCallbackUtil.SEPERATER);
                    int n = paths.length;
                    for (int j = 0; j < n; j++) {
                        if (!TextUtils.isEmpty(paths[j])) {
                            urlBuffer.append(URLEncoder.encode(tryDecode(paths[j]), "utf-8"));
                            if (j < n - 1) {
                                urlBuffer.append(WVNativeCallbackUtil.SEPERATER);
                            }
                        }
                    }
                }
                if (lastChar) {
                    urlBuffer.append(WVNativeCallbackUtil.SEPERATER);
                }
            }
            if (!TextUtils.isEmpty(query)) {
                urlBuffer.append(WVUtils.URL_DATA_CHAR);
                String[] queryString = query.split("&");
                if (queryString != null) {
                    int n2 = queryString.length;
                    for (int i = 0; i < n2; i++) {
                        String[] q = queryString[i].split("=");
                        if (q != null && q.length == 2) {
                            urlBuffer.append(q[0] + "=" + URLEncoder.encode(tryDecode(q[1]), "utf-8"));
                            if (i < n2 - 1) {
                                urlBuffer.append("&");
                            }
                        }
                    }
                }
            }
            return urlBuffer.toString();
        } catch (Throwable th) {
            return urlStr;
        }
    }
}
