package android.taobao.windvane.connect;

import android.taobao.windvane.connect.api.ApiConstants;
import android.taobao.windvane.connect.api.ApiRequest;
import android.taobao.windvane.connect.api.WVApiWrapper;
import android.taobao.windvane.util.TaoLog;
import java.util.Hashtable;
import java.util.Map;

public class ApiUrlManager {
    private static String TAG = "core.ApiUrlManager";
    private static Map<String, String> configUrlMap = new Hashtable();

    public static String getConfigUrl(String apiName, String bizType) {
        if (apiName == null) {
            return null;
        }
        String url = configUrlMap.get(apiName);
        if (url != null) {
            return logUrl(url);
        }
        synchronized (TAG) {
            String url2 = configUrlMap.get(apiName);
            if (url2 != null) {
                String logUrl = logUrl(url2);
                return logUrl;
            }
            ApiRequest request = new ApiRequest();
            request.addParam(ApiConstants.CDN_API_BIZTYPE, bizType);
            request.addParam("api", apiName);
            String url3 = WVApiWrapper.formatUrl(request, CdnApiAdapter.class);
            configUrlMap.put(apiName, url3);
            String logUrl2 = logUrl(url3);
            return logUrl2;
        }
    }

    private static String logUrl(String url) {
        TaoLog.d(TAG, "config url: " + url);
        return url;
    }
}
