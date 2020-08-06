package android.taobao.windvane.connect;

import android.net.Uri;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.connect.api.ApiConstants;
import android.taobao.windvane.connect.api.ApiRequest;
import android.taobao.windvane.connect.api.IApiAdapter;
import com.ali.auth.third.offline.login.LoginConstants;

public class CdnApiAdapter implements IApiAdapter {
    private ApiRequest request;

    public String formatUrl(ApiRequest request2) {
        if (request2 == null) {
            return "";
        }
        this.request = request2;
        return wrapUrl(GlobalConfig.getCdnConfigUrlPre());
    }

    public String formatBody(ApiRequest request2) {
        return "";
    }

    private String wrapUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.length() <= 0) {
            return "";
        }
        Uri.Builder urlBuilder = Uri.parse(baseUrl).buildUpon();
        urlBuilder.appendPath(this.request.getParam(ApiConstants.CDN_API_BIZTYPE));
        urlBuilder.appendPath("windvane");
        urlBuilder.appendPath(LoginConstants.CONFIG);
        if (this.request.getParam("api").contains(ApiConstants.H5APP_API)) {
            urlBuilder.appendPath(this.request.getParam(ApiConstants.H5APP_GROUPID));
            urlBuilder.appendPath(this.request.getParam(ApiConstants.H5APP_GROUPVERSION));
        }
        StringBuilder sbstr = new StringBuilder();
        sbstr.append(GlobalConfig.getInstance().getAppKey()).append("-").append(GlobalConfig.getInstance().getTtid()).append("-").append(GlobalConfig.VERSION);
        int size = this.request.getDataParams().size();
        for (int i = 0; i < size; i++) {
            sbstr.append("-").append(this.request.getDataParam(String.valueOf(i)));
        }
        urlBuilder.appendPath(sbstr.toString());
        if (this.request.getParam("api").contains(ApiConstants.H5APP_API)) {
            urlBuilder.appendPath(this.request.getParam(ApiConstants.H5APP_ABT));
        }
        urlBuilder.appendPath(this.request.getParam("api"));
        return urlBuilder.toString();
    }
}
