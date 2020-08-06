package com.yunos.tvtaobao.biz.common;

import android.content.Context;
import android.text.TextUtils;
import com.yunos.tvtaobao.businessview.R;

public class DocumentUtil {
    public static String replaceWireless(Context context, String src) {
        if (context == null || TextUtils.isEmpty(src)) {
            return src;
        }
        String[] wirelesssstr = context.getResources().getStringArray(R.array.ytbv_document_wireless);
        String tv2 = context.getResources().getString(R.string.ytbv_document_tv);
        int length = wirelesssstr.length;
        String replaceText = src;
        for (int i = 0; i < length; i++) {
            if (replaceText.contains(wirelesssstr[i])) {
                replaceText = replaceText.replace(wirelesssstr[i], tv2);
            }
        }
        return replaceText;
    }
}
