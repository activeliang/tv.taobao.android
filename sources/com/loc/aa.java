package com.loc;

import anet.channel.entity.ConnType;
import java.util.HashMap;
import java.util.Map;

/* compiled from: LogUpdateRequest */
public final class aa extends q {
    private byte[] a;
    private String b = "1";

    public aa(byte[] bArr, String str) {
        this.a = (byte[]) bArr.clone();
        this.b = str;
    }

    public final Map<String, String> b() {
        HashMap hashMap = new HashMap();
        hashMap.put("Content-Type", "application/zip");
        hashMap.put("Content-Length", String.valueOf(this.a.length));
        return hashMap;
    }

    public final Map<String, String> b_() {
        return null;
    }

    public final String c() {
        String c = u.c(w.c);
        byte[] a2 = u.a(w.b);
        byte[] bArr = new byte[(a2.length + 50)];
        System.arraycopy(this.a, 0, bArr, 0, 50);
        System.arraycopy(a2, 0, bArr, 50, a2.length);
        return String.format(c, new Object[]{"1", this.b, "1", ConnType.PK_OPEN, r.a(bArr)});
    }

    public final byte[] d() {
        return this.a;
    }
}
