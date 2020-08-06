package com.ali.auth.third.offline.login.util;

import android.text.TextUtils;
import com.bftv.fui.constantplugin.Constant;

public class AccountUtil {
    public static String hideAccount(String account) {
        if (TextUtils.isEmpty(account)) {
            return "";
        }
        String str = account;
        if (account.contains(Constant.NLP_CACHE_TYPE)) {
            int index = account.indexOf(Constant.NLP_CACHE_TYPE);
            String beforeString = account.substring(0, index);
            String hehindString = account.substring(index, account.length());
            if (beforeString.length() >= 3) {
                return beforeString.substring(0, 3) + "***" + hehindString;
            }
            return beforeString + "***" + hehindString;
        } else if (account.matches("1\\d{10}")) {
            return "******" + account.substring(7, account.length());
        } else if (account.length() <= 1) {
            return str;
        } else {
            String head = account.substring(0, 1);
            return head + "***" + account.substring(account.length() - 1, account.length());
        }
    }
}
