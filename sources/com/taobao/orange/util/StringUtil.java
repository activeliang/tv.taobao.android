package com.taobao.orange.util;

import android.text.TextUtils;

@Deprecated
public class StringUtil {
    public static int parseInt(String value) {
        int ret = 0;
        try {
            if (TextUtils.isEmpty(value)) {
                return 0;
            }
            ret = Integer.parseInt(value);
            return ret;
        } catch (Exception e) {
        }
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
