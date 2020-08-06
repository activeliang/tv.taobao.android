package com.loc;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.sdk.android.oss.common.utils.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

/* compiled from: GeoFenceNetManager */
public final class b {
    aw a = null;

    public b(Context context) {
        try {
            p.a().a(context);
        } catch (Throwable th) {
        }
        this.a = aw.a();
    }

    private static String a(Context context, String str, Map<String, String> map) {
        byte[] b;
        try {
            HashMap hashMap = new HashMap(16);
            eh ehVar = new eh();
            hashMap.clear();
            hashMap.put("Content-Type", "application/x-www-form-urlencoded");
            hashMap.put("Connection", "Keep-Alive");
            hashMap.put(HttpHeaders.USER_AGENT, "AMAP_Location_SDK_Android 4.9.0");
            String a2 = m.a();
            String a3 = m.a(context, a2, u.b(map));
            map.put("ts", a2);
            map.put("scode", a3);
            ehVar.b(map);
            ehVar.a((Map<String, String>) hashMap);
            ehVar.a(str);
            ehVar.a(s.a(context));
            ehVar.a(en.g);
            ehVar.b(en.g);
            if (et.k(context)) {
                ehVar.a(str.replace("http:", "https:"));
                b = aw.a(ehVar);
            } else {
                b = aw.b(ehVar);
            }
            return new String(b, "utf-8");
        } catch (Throwable th) {
            return null;
        }
    }

    private static Map<String, String> b(Context context, String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        HashMap hashMap = new HashMap(16);
        hashMap.put("key", k.f(context));
        if (!TextUtils.isEmpty(str)) {
            hashMap.put("keywords", str);
        }
        if (!TextUtils.isEmpty(str2)) {
            hashMap.put("types", str2);
        }
        if (!TextUtils.isEmpty(str5) && !TextUtils.isEmpty(str6)) {
            hashMap.put("location", str6 + "," + str5);
        }
        if (!TextUtils.isEmpty(str3)) {
            hashMap.put("city", str3);
        }
        if (!TextUtils.isEmpty(str4)) {
            hashMap.put("offset", str4);
        }
        if (!TextUtils.isEmpty(str7)) {
            hashMap.put("radius", str7);
        }
        return hashMap;
    }

    public final String a(Context context, String str, String str2) {
        Map<String, String> b = b(context, str2, (String) null, (String) null, (String) null, (String) null, (String) null, (String) null);
        b.put("extensions", "all");
        return a(context, str, b);
    }

    public final String a(Context context, String str, String str2, String str3, String str4, String str5) {
        Map<String, String> b = b(context, str2, str3, str4, str5, (String) null, (String) null, (String) null);
        b.put("children", "1");
        b.put("page", "1");
        b.put("extensions", "base");
        return a(context, str, b);
    }

    public final String a(Context context, String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        Map<String, String> b = b(context, str2, str3, (String) null, str4, str5, str6, str7);
        b.put("children", "1");
        b.put("page", "1");
        b.put("extensions", "base");
        return a(context, str, b);
    }
}
