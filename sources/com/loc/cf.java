package com.loc;

import android.util.Base64;
import com.alibaba.sdk.android.oss.common.OSSConstants;
import java.nio.charset.StandardCharsets;

/* compiled from: CollectionUploader */
public final class cf {
    public static boolean a(byte[] bArr) {
        if (bArr == null) {
            return false;
        }
        byte[] bArr2 = null;
        try {
            dm dmVar = new dm();
            dmVar.b.put("Content-Type", OSSConstants.DEFAULT_OBJECT_CONTENT_TYPE);
            dmVar.b.put("aps_c_src", Base64.encodeToString(("lc_" + di.a()).getBytes(), 2));
            dmVar.b.put("aps_c_key", Base64.encodeToString((di.c() + "*" + di.f()).getBytes(), 2));
            dmVar.d = bArr;
            if (bw.a) {
                dmVar.a = "http://aps.testing.amap.com/collection/collectData?src=baseCol&ver=v74&";
            } else {
                dmVar.a = (bw.b ? "https://" : "http://") + "cgicol.amap.com/collection/collectData?src=baseCol&ver=v74&";
            }
            dn a = cz.a().a(dmVar);
            if (a != null && a.a == 200) {
                bArr2 = a.c;
            }
            return bArr2 != null && "true".equals(new String(bArr2, StandardCharsets.UTF_8));
        } catch (Exception e) {
            dk.a(e);
            return false;
        }
    }
}
