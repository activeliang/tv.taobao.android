package com.taobao.muniontaobaosdk.util;

import android.content.Context;
import android.text.TextUtils;
import com.taobao.utils.Global;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.zip.CRC32;
import org.json.JSONException;
import org.json.JSONObject;

public class SdkUtil {
    private static String a;
    private static String b;

    public static boolean a(String str) {
        int length;
        if (str == null || (length = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String buildUTKvs(Throwable th, int i, int i2) {
        if (th == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        try {
            StackTraceElement[] stackTrace = th.getStackTrace();
            if (stackTrace != null) {
                int i3 = i;
                while (i3 < stackTrace.length && i3 < i + i2) {
                    if (sb.length() > 1) {
                        sb.append("<-");
                    }
                    sb.append(stackTrace[i3].getClassName());
                    sb.append("::");
                    sb.append(stackTrace[i3].getMethodName());
                    i3++;
                }
            }
        } catch (Throwable th2) {
        }
        return "track=" + sb.toString();
    }

    public static String buildUTKvs(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "";
        }
        ArrayList arrayList = new ArrayList(map.size());
        for (Map.Entry next : map.entrySet()) {
            arrayList.add(String.format("%s=%s", new Object[]{next.getKey(), next.getValue()}));
        }
        return TextUtils.join(",", arrayList);
    }

    public static String createClickID() {
        return createClickID(Global.getApplication());
    }

    public static String createClickID(Context context) {
        if (b == null) {
            a = MunionDeviceUtil.getUtdid(context);
            CRC32 crc32 = new CRC32();
            crc32.update(a.getBytes());
            b = String.valueOf(crc32.getValue());
        }
        return b + System.currentTimeMillis() + new Random().nextInt(10000);
    }

    public static JSONObject getParseStr(String str, String str2) {
        int indexOf;
        JSONObject jSONObject = new JSONObject();
        if (isNotEmpty(str)) {
            if (isEmpty(str2)) {
            }
            String[] split = str.split("&");
            for (String str3 : split) {
                if (isNotEmpty(str3) && (indexOf = str3.indexOf("=")) >= 0 && indexOf < str3.length()) {
                    String substring = str3.substring(0, indexOf);
                    String substring2 = str3.substring(indexOf + 1);
                    if (isNotEmpty(substring) && isNotEmpty(substring2)) {
                        try {
                            jSONObject.put(substring, substring2);
                        } catch (JSONException e) {
                        }
                    }
                }
            }
        }
        return jSONObject;
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotBlank(String str) {
        return !a(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String md5(String str) {
        try {
            return String.format("%032x", new Object[]{new BigInteger(1, MessageDigest.getInstance("MD5").digest(str.getBytes()))});
        } catch (Exception e) {
            return "";
        }
    }
}
