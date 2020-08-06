package com.yunos.tvtaobao.uuid.utils;

import android.annotation.SuppressLint;
import com.bftv.fui.constantplugin.Constant;

public class StringUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    @SuppressLint({"DefaultLocale"})
    public static boolean isAvailableString(String str) {
        return isNotEmpty(str) && !Constant.NULL.equals(str.toLowerCase());
    }
}
