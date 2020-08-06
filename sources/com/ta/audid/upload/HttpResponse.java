package com.ta.audid.upload;

import android.text.TextUtils;
import com.ta.audid.utils.MD5Utils;
import com.ta.audid.utils.UtdidLogger;
import com.ta.utdid2.android.utils.Base64;

public class HttpResponse {
    public byte[] data = null;
    public int httpResponseCode = -1;
    public long rt = 0;
    public String signature = "";
    public long timestamp = 0;

    public static boolean checkSignature(String result, String signature2) {
        try {
            if (!TextUtils.isEmpty(result) && !TextUtils.isEmpty(signature2)) {
                UtdidLogger.sd("", "result", result, "signature", signature2);
                if (signature2.equals(Base64.encodeToString(MD5Utils.getHmacMd5Hex(result).getBytes(), 2))) {
                    UtdidLogger.d("", "signature is ok");
                    return true;
                }
                UtdidLogger.d("", "signature is error");
            }
        } catch (Exception e) {
            UtdidLogger.d("", e);
        }
        return false;
    }
}
