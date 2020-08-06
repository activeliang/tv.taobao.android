package com.tvtaobao.android.values;

import android.text.TextUtils;
import com.bftv.fui.constantplugin.Constant;

public class StringUtil {
    public static String maskNick(String nick) {
        String rtn = nick;
        if (TextUtils.isEmpty(nick)) {
            return rtn;
        }
        if (nick.length() > 4) {
            String pre = nick.substring(0, 2);
            return pre + "***" + nick.substring(nick.length() - 2);
        } else if (nick.length() <= 2) {
            return nick.substring(0, 1) + "***";
        } else {
            String pre2 = nick.substring(0, 1);
            return pre2 + "***" + nick.substring(nick.length() - 1);
        }
    }

    public static String maskMobile(String mobile) {
        String rtn = mobile;
        if (TextUtils.isEmpty(mobile)) {
            return rtn;
        }
        if (mobile.length() > 7) {
            String pre = mobile.substring(0, 3);
            return pre + "***" + mobile.substring(mobile.length() - 4);
        } else if (mobile.length() <= 3) {
            return mobile.substring(0, 1) + "***";
        } else {
            return mobile.substring(0, 3) + "***";
        }
    }

    public static String noNull(String str) {
        if (!TextUtils.isEmpty(str) && !Constant.NULL.equalsIgnoreCase(str)) {
            return str;
        }
        return "";
    }
}
