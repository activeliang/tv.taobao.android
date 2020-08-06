package mtopsdk.mtop.protocol.converter.util;

import android.taobao.windvane.jsbridge.utils.WVUtils;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;

public class NetworkConverterUtils {
    private static final String TAG = "mtopsdk.NetworkConverterUtils";

    public static URL initUrl(String baseUrl, Map<String, String> queryParams) {
        URL url = null;
        if (StringUtils.isBlank(baseUrl)) {
            TBSdkLog.e(TAG, "[initUrl]baseUrl is blank,initUrl error");
            return null;
        }
        try {
            StringBuilder fullUrl = new StringBuilder(baseUrl);
            if (queryParams != null) {
                String queryStr = createParamQueryStr(queryParams, "utf-8");
                if (StringUtils.isNotBlank(queryStr) && baseUrl.indexOf(WVUtils.URL_DATA_CHAR) == -1) {
                    fullUrl.append(WVUtils.URL_DATA_CHAR).append(queryStr);
                }
            }
            url = new URL(fullUrl.toString());
        } catch (Exception e) {
            TBSdkLog.e(TAG, "[initUrl] build fullUrl error", (Throwable) e);
        }
        return url;
    }

    public static String createParamQueryStr(Map<String, String> params, String charset) {
        String key;
        String value;
        if (params == null) {
            return null;
        }
        if (StringUtils.isBlank(charset)) {
            charset = "utf-8";
        }
        StringBuilder builder = new StringBuilder(64);
        Iterator<Map.Entry<String, String>> entries = params.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, String> entry = entries.next();
            try {
                if (entry.getKey() != null) {
                    key = URLEncoder.encode(entry.getKey(), charset);
                } else {
                    key = null;
                }
                if (entry.getValue() != null) {
                    value = URLEncoder.encode(entry.getValue(), charset);
                } else {
                    value = null;
                }
                builder.append(key).append("=").append(value);
                if (entries.hasNext()) {
                    builder.append("&");
                }
            } catch (Throwable e) {
                TBSdkLog.e(TAG, "[createParamQueryStr]getQueryStr error ---" + e.toString());
            }
        }
        return builder.toString();
    }
}
