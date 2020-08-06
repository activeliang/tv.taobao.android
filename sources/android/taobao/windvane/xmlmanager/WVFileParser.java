package android.taobao.windvane.xmlmanager;

import java.lang.reflect.Constructor;

class WVFileParser {
    private Constructor<?> xmlBlockConstructor;

    public WVFileParser() {
        init();
    }

    private void init() {
        try {
            this.xmlBlockConstructor = Class.forName("android.content.res.XmlBlock").getConstructor(new Class[]{byte[].class});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v3, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.xmlpull.v1.XmlPullParser openXmlResourceParser(java.lang.String r10) {
        /*
            r9 = this;
            r5 = 0
            java.lang.reflect.Constructor<?> r6 = r9.xmlBlockConstructor
            if (r6 != 0) goto L_0x0007
            r3 = r5
        L_0x0006:
            return r3
        L_0x0007:
            r0 = 0
            java.lang.Class<android.taobao.windvane.xmlmanager.WVXmlResUtils> r6 = android.taobao.windvane.xmlmanager.WVXmlResUtils.class
            java.io.InputStream r2 = r6.getResourceAsStream(r10)     // Catch:{ Exception -> 0x0019 }
            byte[] r0 = android.taobao.windvane.xmlmanager.WVInputStreamUtils.InputStreamTOByte(r2)     // Catch:{ Exception -> 0x0019 }
        L_0x0012:
            if (r0 == 0) goto L_0x0017
            int r6 = r0.length
            if (r6 != 0) goto L_0x0025
        L_0x0017:
            r3 = r5
            goto L_0x0006
        L_0x0019:
            r1 = move-exception
            java.lang.String r6 = "Read Error"
            java.lang.String r7 = r1.toString()
            android.taobao.windvane.util.TaoLog.e(r6, r7)
            goto L_0x0012
        L_0x0025:
            java.lang.reflect.Constructor<?> r6 = r9.xmlBlockConstructor     // Catch:{ Exception -> 0x0042 }
            r7 = 1
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x0042 }
            r8 = 0
            r7[r8] = r0     // Catch:{ Exception -> 0x0042 }
            java.lang.Object r4 = r6.newInstance(r7)     // Catch:{ Exception -> 0x0042 }
            java.lang.String r6 = "newParser"
            r7 = 0
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ Exception -> 0x0042 }
            java.lang.Object r3 = android.taobao.windvane.xmlmanager.WVReflectUtils.invoke(r4, r6, r7)     // Catch:{ Exception -> 0x0042 }
            boolean r6 = r3 instanceof android.content.res.XmlResourceParser     // Catch:{ Exception -> 0x0042 }
            if (r6 == 0) goto L_0x004d
            android.content.res.XmlResourceParser r3 = (android.content.res.XmlResourceParser) r3     // Catch:{ Exception -> 0x0042 }
            goto L_0x0006
        L_0x0042:
            r1 = move-exception
            java.lang.String r6 = "Read Error"
            java.lang.String r7 = r1.toString()
            android.taobao.windvane.util.TaoLog.e(r6, r7)
        L_0x004d:
            r3 = r5
            goto L_0x0006
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.xmlmanager.WVFileParser.openXmlResourceParser(java.lang.String):org.xmlpull.v1.XmlPullParser");
    }
}
