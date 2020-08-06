package android.taobao.windvane.extra.mtop;

import android.taobao.windvane.connect.api.ApiRequest;
import android.taobao.windvane.connect.api.WVApiWrapper;
import mtopsdk.xstate.util.XStateConstants;

public class ApiUrlManager {
    public static String getUploadTokenUrl(String uniqueKey) {
        ApiRequest request = new ApiRequest();
        request.addParam("api", "com.taobao.mtop.getUploadFileToken");
        request.addParam("v", "2.0");
        request.addDataParam("uniqueKey", uniqueKey);
        return WVApiWrapper.formatUrl(request, MtopApiAdapter.class);
    }

    public static String getUploadUrl(String uniqueKey, String accessToken) {
        ApiRequest request = new ApiRequest();
        request.addParam("api", "com.taobao.mtop.uploadFile");
        request.addParam("v", "2.0");
        request.addDataParam("uniqueKey", uniqueKey);
        request.addDataParam(XStateConstants.KEY_ACCESS_TOKEN, accessToken);
        return WVApiWrapper.formatUrl(request, MtopApiAdapter.class);
    }
}
