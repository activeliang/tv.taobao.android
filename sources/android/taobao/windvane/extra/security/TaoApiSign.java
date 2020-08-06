package android.taobao.windvane.extra.security;

import android.taobao.windvane.connect.api.ApiConstants;
import android.taobao.windvane.util.DigestUtils;
import android.taobao.windvane.util.TaoLog;
import java.util.Map;

public class TaoApiSign {
    public static String getSign(Map<String, String> map) {
        try {
            String appKey = map.get("appKey");
            String appSecret = map.get(ApiConstants.APPSECRET);
            String api = map.get("api");
            String v = map.get("v");
            String imei = map.get("imei");
            String imsi = map.get("imsi");
            String data = map.get("data");
            String t = map.get("t");
            String ecode = map.get(ApiConstants.ECODE);
            if (v == null || "".equals(v)) {
                v = "*";
            }
            if (data == null) {
                data = "";
            }
            String appkeySign = DigestUtils.md5ToHex(appKey);
            StringBuilder sb = new StringBuilder();
            if (ecode != null) {
                sb.append(ecode);
                sb.append("&");
            }
            sb.append(appSecret);
            sb.append("&");
            sb.append(appkeySign);
            sb.append("&");
            sb.append(api);
            sb.append("&");
            sb.append(v);
            sb.append("&");
            sb.append(imei);
            sb.append("&");
            sb.append(imsi);
            sb.append("&");
            sb.append(DigestUtils.md5ToHex(data));
            sb.append("&");
            sb.append(t);
            return DigestUtils.md5ToHex(sb.toString());
        } catch (Exception e) {
            TaoLog.e("TaoApiSign", "generate sign fail." + e);
            return null;
        }
    }
}
