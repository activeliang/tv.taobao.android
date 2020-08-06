package com.taobao.alimama;

import android.net.Uri;
import android.text.TextUtils;
import com.taobao.munion.taosdk.CpcEventCommitter;
import com.taobao.munion.taosdk.CpmEventCommitter;
import com.taobao.utils.Global;
import java.util.ArrayList;

public class AlimamaAdUrlHandler {
    public static final String a = "eurl";
    public static final String b = "etype";
    public static final String c = "clickid";

    private static class a {
        static volatile AlimamaAdUrlHandler a = new AlimamaAdUrlHandler();

        private a() {
        }
    }

    private Uri a(Uri uri) {
        if (uri == null) {
            return null;
        }
        String encodedQuery = uri.getEncodedQuery();
        if (TextUtils.isEmpty(encodedQuery)) {
            return uri;
        }
        ArrayList arrayList = new ArrayList();
        for (String str : encodedQuery.split("&")) {
            String lowerCase = str.toLowerCase();
            if (!lowerCase.startsWith("eurl=") && !lowerCase.startsWith("etype=")) {
                arrayList.add(str);
            }
        }
        Uri.Builder buildUpon = uri.buildUpon();
        if (!arrayList.isEmpty()) {
            buildUpon.encodedQuery(TextUtils.join("&", arrayList));
        }
        return buildUpon.build();
    }

    public static AlimamaAdUrlHandler getDefault() {
        return a.a;
    }

    public String a(String str, String str2) {
        return (!TextUtils.isEmpty(str) && "1".equals(str2)) ? new CpcEventCommitter(Global.getApplication(), false).a(str, true) : str;
    }

    public String handleAdUrl(String str, boolean z, boolean z2) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            Uri parse = Uri.parse(str);
            String queryParameter = parse.getQueryParameter("eurl");
            if (TextUtils.isEmpty(queryParameter)) {
                return str;
            }
            String queryParameter2 = parse.getQueryParameter("etype");
            Uri uri = null;
            if ("1".equals(queryParameter2)) {
                uri = new CpcEventCommitter(Global.getApplication(), z).commitEvent(queryParameter, parse);
            } else if ("3".equals(queryParameter2)) {
                uri = new CpmEventCommitter(Global.getApplication(), z).commitEvent(queryParameter, parse);
            }
            if (uri != null && z2) {
                uri = a(uri);
            }
            return uri != null ? uri.toString() : str;
        } catch (Exception e) {
            return str;
        }
    }

    public String handleAdUrlForClickid(String str, boolean z) {
        return TextUtils.isEmpty(str) ? str : Uri.parse(handleAdUrl(str, z, false)).getQueryParameter("clickid");
    }
}
