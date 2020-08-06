package mtopsdk.network.util;

import anet.channel.request.Request;
import java.io.Closeable;
import java.util.List;
import java.util.Map;
import mtopsdk.common.util.HeaderHandlerUtil;

public final class NetworkUtils {
    private NetworkUtils() {
    }

    public static boolean requiresRequestBody(String method) {
        return method.equals("POST") || method.equals(Request.Method.PUT) || method.equals("PATCH");
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
            }
        }
    }

    public static boolean checkContentEncodingGZip(Map<String, List<String>> header) {
        try {
            if ("gzip".equalsIgnoreCase(HeaderHandlerUtil.getSingleHeaderFieldByKey(header, "Content-Encoding"))) {
                return true;
            }
            return false;
        } catch (Exception e) {
        }
    }
}
