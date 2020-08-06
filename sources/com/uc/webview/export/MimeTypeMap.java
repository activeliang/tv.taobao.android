package com.uc.webview.export;

import com.ali.auth.third.core.model.KernelMessageConstants;
import com.uc.webview.export.annotations.Api;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.interfaces.IMimeTypeMap;
import java.util.HashMap;

@Api
/* compiled from: ProGuard */
public class MimeTypeMap {
    private static HashMap<Integer, MimeTypeMap> a;
    private IMimeTypeMap b;

    private MimeTypeMap(IMimeTypeMap iMimeTypeMap) {
        this.b = iMimeTypeMap;
    }

    public static String getFileExtensionFromUrl(String str) {
        return getSingleton().b.getFileExtensionFromUrlEx(str);
    }

    public boolean hasMimeType(String str) {
        return this.b.hasMimeType(str);
    }

    public String getMimeTypeFromExtension(String str) {
        return this.b.getMimeTypeFromExtension(str);
    }

    public boolean hasExtension(String str) {
        return this.b.hasExtension(str);
    }

    public String getExtensionFromMimeType(String str) {
        return this.b.getExtensionFromMimeType(str);
    }

    public static MimeTypeMap getSingleton() {
        return a(((Integer) d.a((int) KernelMessageConstants.PARAM_ERROR, new Object[0])).intValue());
    }

    public static MimeTypeMap getSingleton(WebView webView) {
        return a(webView.getCurrentViewCoreType());
    }

    private static synchronized MimeTypeMap a(int i) {
        MimeTypeMap mimeTypeMap;
        synchronized (MimeTypeMap.class) {
            if (a == null) {
                a = new HashMap<>();
            }
            mimeTypeMap = a.get(Integer.valueOf(i));
            if (mimeTypeMap == null) {
                MimeTypeMap mimeTypeMap2 = new MimeTypeMap((IMimeTypeMap) d.a(10019, Integer.valueOf(i)));
                a.put(Integer.valueOf(i), mimeTypeMap2);
                mimeTypeMap = mimeTypeMap2;
            }
        }
        return mimeTypeMap;
    }

    public String toString() {
        return "MimeTypeMap@" + hashCode() + "[" + this.b + "]";
    }
}
