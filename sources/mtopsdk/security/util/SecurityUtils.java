package mtopsdk.security.util;

import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.security.MessageDigest;
import mtopsdk.common.util.StringUtils;
import mtopsdk.common.util.TBSdkLog;

public class SecurityUtils {
    public static final int DEFAULT_WUA_FLAG = 0;
    public static final int GENERAL_WUA_FLAG = 4;
    public static final int MINI_WUA_FLAG = 8;
    private static final String TAG = "mtopsdk.SecurityUtils";

    public static String convertNull2Default(String param) {
        return param == null ? "" : param;
    }

    public static String getMd5(String source) {
        if (StringUtils.isBlank(source)) {
            return source;
        }
        try {
            MessageDigest digester = MessageDigest.getInstance("MD5");
            digester.update(source.getBytes("utf-8"));
            byte[] digest = digester.digest();
            StringBuffer hexString = new StringBuffer();
            for (byte b : digest) {
                String h = Integer.toHexString(b & OnReminderListener.RET_FULL);
                while (h.length() < 2) {
                    h = "0" + h;
                }
                hexString.append(h);
            }
            return hexString.toString();
        } catch (Exception e) {
            TBSdkLog.e(TAG, "[getMd5] compute md5 value failed for source str=" + source, (Throwable) e);
            return null;
        }
    }
}
