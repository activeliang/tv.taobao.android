package com.alibaba.analytics.core.logbuilder;

import com.alibaba.analytics.utils.Logger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class FieldCheck {
    private static Pattern mPattern = Pattern.compile("(\\|\\||[\t\r\n]|\u0001|\u0000)+");

    private static String _getStringNoBlankAndDLine(String pStr) {
        if (pStr == null || "".equals(pStr)) {
            return pStr;
        }
        return mPattern.matcher(pStr).replaceAll("");
    }

    private static String _checkField(String pField) {
        return _getStringNoBlankAndDLine(pField);
    }

    public static Map<String, String> checkMapFields(Map<String, String> aLogMap) {
        if (aLogMap == null) {
            return null;
        }
        Map<String, String> lNewMap = new HashMap<>();
        Iterator<String> lKeyItor = aLogMap.keySet().iterator();
        if (lKeyItor == null) {
            return lNewMap;
        }
        while (lKeyItor.hasNext()) {
            try {
                String lKey = lKeyItor.next();
                if (lKey != null) {
                    lNewMap.put(lKey, _checkField(aLogMap.get(lKey)));
                }
            } catch (Throwable e) {
                Logger.e("[_checkMapFields]", e, new Object[0]);
            }
        }
        return lNewMap;
    }
}
