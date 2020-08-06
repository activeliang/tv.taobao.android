package com.alibaba.fastjson.serializer;

public class ClobSeriliazer implements ObjectSerializer {
    public static final ClobSeriliazer instance = new ClobSeriliazer();

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002a, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0033, code lost:
        throw new java.io.IOException("write clob error", r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0039, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0042, code lost:
        throw new com.alibaba.fastjson.JSONException("read string from reader error", r5);
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:1:0x0002, B:5:0x0015] */
    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void write(com.alibaba.fastjson.serializer.JSONSerializer r12, java.lang.Object r13, java.lang.Object r14, java.lang.reflect.Type r15, int r16) throws java.io.IOException {
        /*
            r11 = this;
            if (r13 != 0) goto L_0x0006
            r12.writeNull()     // Catch:{ SQLException -> 0x002a }
        L_0x0005:
            return
        L_0x0006:
            r0 = r13
            java.sql.Clob r0 = (java.sql.Clob) r0     // Catch:{ SQLException -> 0x002a }
            r3 = r0
            java.io.Reader r7 = r3.getCharacterStream()     // Catch:{ SQLException -> 0x002a }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ SQLException -> 0x002a }
            r1.<init>()     // Catch:{ SQLException -> 0x002a }
            r9 = 2048(0x800, float:2.87E-42)
            char[] r2 = new char[r9]     // Catch:{ Exception -> 0x0039 }
        L_0x0017:
            r9 = 0
            int r10 = r2.length     // Catch:{ Exception -> 0x0039 }
            int r6 = r7.read(r2, r9, r10)     // Catch:{ Exception -> 0x0039 }
            if (r6 >= 0) goto L_0x0034
            java.lang.String r8 = r1.toString()     // Catch:{ SQLException -> 0x002a }
            r7.close()     // Catch:{ SQLException -> 0x002a }
            r12.write((java.lang.String) r8)     // Catch:{ SQLException -> 0x002a }
            goto L_0x0005
        L_0x002a:
            r4 = move-exception
            java.io.IOException r9 = new java.io.IOException
            java.lang.String r10 = "write clob error"
            r9.<init>(r10, r4)
            throw r9
        L_0x0034:
            r9 = 0
            r1.append(r2, r9, r6)     // Catch:{ Exception -> 0x0039 }
            goto L_0x0017
        L_0x0039:
            r5 = move-exception
            com.alibaba.fastjson.JSONException r9 = new com.alibaba.fastjson.JSONException     // Catch:{ SQLException -> 0x002a }
            java.lang.String r10 = "read string from reader error"
            r9.<init>(r10, r5)     // Catch:{ SQLException -> 0x002a }
            throw r9     // Catch:{ SQLException -> 0x002a }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.ClobSeriliazer.write(com.alibaba.fastjson.serializer.JSONSerializer, java.lang.Object, java.lang.Object, java.lang.reflect.Type, int):void");
    }
}
