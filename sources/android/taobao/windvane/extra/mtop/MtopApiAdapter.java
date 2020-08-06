package android.taobao.windvane.extra.mtop;

import android.content.ContextWrapper;
import android.net.Uri;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.connect.api.ApiRequest;
import android.taobao.windvane.connect.api.IApiAdapter;
import android.taobao.windvane.extra.security.SecurityManager;
import android.taobao.windvane.extra.security.TaoApiSign;
import android.taobao.windvane.util.TaoLog;
import java.util.Map;

public class MtopApiAdapter implements IApiAdapter {
    private ApiRequest request;

    public String formatUrl(ApiRequest request2) {
        if (request2 == null) {
            return "";
        }
        this.request = request2;
        checkParams();
        return wrapUrl(GlobalConfig.getMtopUrl());
    }

    public String formatBody(ApiRequest request2) {
        if (request2 == null) {
            return "";
        }
        this.request = request2;
        checkParams();
        return wrapBody();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x00a0, code lost:
        if (r6 == 0) goto L_0x00a2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void checkParams() {
        /*
            r12 = this;
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.lang.String r8 = "ttid"
            android.taobao.windvane.config.GlobalConfig r9 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r9 = r9.getTtid()
            r5.addParam(r8, r9)
            android.taobao.windvane.config.GlobalConfig r5 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r0 = r5.getImei()
            boolean r5 = android.text.TextUtils.isEmpty(r0)
            if (r5 == 0) goto L_0x0021
            java.lang.String r0 = "111111111111111"
        L_0x0021:
            android.taobao.windvane.config.GlobalConfig r5 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r1 = r5.getImsi()
            boolean r5 = android.text.TextUtils.isEmpty(r1)
            if (r5 == 0) goto L_0x0032
            java.lang.String r1 = "111111111111111"
        L_0x0032:
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.lang.String r8 = "imei"
            r5.addParam(r8, r0)
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.lang.String r8 = "imsi"
            r5.addParam(r8, r1)
            android.taobao.windvane.config.GlobalConfig r5 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r5 = r5.getDeviceId()
            boolean r5 = android.text.TextUtils.isEmpty(r5)
            if (r5 != 0) goto L_0x0060
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.lang.String r8 = "deviceId"
            android.taobao.windvane.config.GlobalConfig r9 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r9 = r9.getDeviceId()
            r5.addParam(r8, r9)
        L_0x0060:
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.util.Map r5 = r5.getDataParams()
            int r5 = r5.size()
            if (r5 <= 0) goto L_0x0083
            org.json.JSONObject r2 = new org.json.JSONObject
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.util.Map r5 = r5.getDataParams()
            r2.<init>(r5)
            java.lang.String r3 = r2.toString()
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.lang.String r8 = "data"
            r5.addParam(r8, r3)
        L_0x0083:
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.util.Map r5 = r5.getParams()
            java.lang.String r8 = "t"
            boolean r5 = r5.containsKey(r8)
            if (r5 != 0) goto L_0x00b6
            android.taobao.windvane.extra.WVIAdapter r5 = android.taobao.windvane.WindVaneSDKForTB.wvAdapter
            if (r5 == 0) goto L_0x00a2
            android.taobao.windvane.extra.WVIAdapter r5 = android.taobao.windvane.WindVaneSDKForTB.wvAdapter
            long r6 = r5.getTimestamp()
            r8 = 0
            int r5 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r5 != 0) goto L_0x00a6
        L_0x00a2:
            long r6 = java.lang.System.currentTimeMillis()
        L_0x00a6:
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.lang.String r8 = "t"
            r10 = 1000(0x3e8, double:4.94E-321)
            long r10 = r6 / r10
            java.lang.String r9 = java.lang.String.valueOf(r10)
            r5.addParam(r8, r9)
        L_0x00b6:
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.lang.String r8 = "appKey"
            android.taobao.windvane.config.GlobalConfig r9 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r9 = r9.getAppKey()
            r5.addParam(r8, r9)
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            boolean r5 = r5.isSec()
            if (r5 == 0) goto L_0x00dc
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.lang.String r8 = "wua"
            android.app.Application r9 = android.taobao.windvane.config.GlobalConfig.context
            java.lang.String r9 = r12.getSecBodyData(r9)
            r5.addParam(r8, r9)
        L_0x00dc:
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.lang.String r8 = "appSecret"
            android.taobao.windvane.config.GlobalConfig r9 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String r9 = r9.getAppSecret()
            r5.addParam(r8, r9)
            java.lang.String r4 = r12.getSign()
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.lang.String r8 = "sign"
            r5.addParam(r8, r4)
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.util.Map r5 = r5.getParams()
            java.lang.String r8 = "v"
            boolean r5 = r5.containsKey(r8)
            if (r5 != 0) goto L_0x0112
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.lang.String r8 = "v"
            java.lang.String r9 = "*"
            r5.addParam(r8, r9)
        L_0x0112:
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.util.Map r5 = r5.getParams()
            java.lang.String r8 = "appSecret"
            boolean r5 = r5.containsKey(r8)
            if (r5 == 0) goto L_0x0129
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.lang.String r8 = "appSecret"
            r5.removeParam(r8)
        L_0x0129:
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.util.Map r5 = r5.getParams()
            java.lang.String r8 = "ecode"
            boolean r5 = r5.containsKey(r8)
            if (r5 == 0) goto L_0x0140
            android.taobao.windvane.connect.api.ApiRequest r5 = r12.request
            java.lang.String r8 = "ecode"
            r5.removeParam(r8)
        L_0x0140:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.mtop.MtopApiAdapter.checkParams():void");
    }

    private String wrapUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.length() <= 0) {
            return "";
        }
        Uri base = Uri.parse(baseUrl);
        Uri.Builder urlBuilder = base.buildUpon();
        String path = base.getPath();
        if (path == null || path.length() == 0) {
            urlBuilder.appendPath("");
        }
        for (Map.Entry<String, String> entry : this.request.getParams().entrySet()) {
            urlBuilder = urlBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return urlBuilder.toString();
    }

    private String wrapBody() {
        StringBuilder builder = new StringBuilder();
        boolean needAndSymbol = false;
        for (Map.Entry<String, String> entry : this.request.getParams().entrySet()) {
            if (needAndSymbol) {
                builder.append("&");
            } else {
                needAndSymbol = true;
            }
            builder.append(Uri.encode(entry.getKey())).append("=").append(Uri.encode(entry.getValue()));
        }
        return builder.toString();
    }

    private String getSign() {
        String sign = SecurityManager.getInstance().getSign(0, this.request.getParams(), this.request.getParam("appKey"));
        if (TaoLog.getLogStatus()) {
            TaoLog.d("MtopApiAdapter", "appkey: " + this.request.getParam("appKey") + " params: " + this.request.getParams());
        }
        if (sign != null) {
            return sign;
        }
        TaoLog.w("MtopApiAdapter", "SecurityManager.getSign failed, execute TaoApiSign.getSign");
        return TaoApiSign.getSign(this.request.getParams());
    }

    private String getSecBodyData(ContextWrapper context) {
        return SecurityManager.getInstance().getSecBody(context, this.request.getParam("t"), this.request.getParam("appKey"));
    }
}
