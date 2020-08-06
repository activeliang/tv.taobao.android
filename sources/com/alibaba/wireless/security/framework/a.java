package com.alibaba.wireless.security.framework;

import android.content.pm.PackageInfo;
import com.alibaba.wireless.security.framework.utils.b;
import java.io.File;
import org.json.JSONObject;

public class a {
    public PackageInfo a = null;
    private JSONObject b = null;
    private String c;

    public a(String str) {
        this.c = str;
    }

    public String a(String str) {
        try {
            return b().getString(str);
        } catch (Exception e) {
            return "";
        }
    }

    public boolean a() {
        try {
            return new File(this.c).exists();
        } catch (Exception e) {
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x00a8  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00b7 A[SYNTHETIC, Splitter:B:23:0x00b7] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00bf  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean a(android.content.pm.PackageInfo r8, java.lang.String r9) {
        /*
            r7 = this;
            r1 = 0
            r2 = 1
            if (r8 == 0) goto L_0x0006
            if (r9 != 0) goto L_0x0008
        L_0x0006:
            r0 = r1
        L_0x0007:
            return r0
        L_0x0008:
            org.json.JSONObject r4 = new org.json.JSONObject
            r4.<init>()
            java.lang.String r0 = "version"
            java.lang.String r3 = r8.versionName     // Catch:{ Exception -> 0x00ab }
            r4.put(r0, r3)     // Catch:{ Exception -> 0x00ab }
            java.lang.String r0 = "hasso"
            android.content.pm.ApplicationInfo r3 = r8.applicationInfo     // Catch:{ Exception -> 0x00ab }
            android.os.Bundle r3 = r3.metaData     // Catch:{ Exception -> 0x00ab }
            java.lang.String r5 = "hasso"
            r6 = 0
            boolean r3 = r3.getBoolean(r5, r6)     // Catch:{ Exception -> 0x00ab }
            r4.put(r0, r3)     // Catch:{ Exception -> 0x00ab }
            java.lang.String r0 = "pluginname"
            android.content.pm.ApplicationInfo r3 = r8.applicationInfo     // Catch:{ Exception -> 0x00ab }
            android.os.Bundle r3 = r3.metaData     // Catch:{ Exception -> 0x00ab }
            java.lang.String r5 = "pluginname"
            java.lang.String r3 = r3.getString(r5)     // Catch:{ Exception -> 0x00ab }
            r4.put(r0, r3)     // Catch:{ Exception -> 0x00ab }
            java.lang.String r0 = "pluginclass"
            android.content.pm.ApplicationInfo r3 = r8.applicationInfo     // Catch:{ Exception -> 0x00ab }
            android.os.Bundle r3 = r3.metaData     // Catch:{ Exception -> 0x00ab }
            java.lang.String r5 = "pluginclass"
            java.lang.String r3 = r3.getString(r5)     // Catch:{ Exception -> 0x00ab }
            r4.put(r0, r3)     // Catch:{ Exception -> 0x00ab }
            java.lang.String r0 = "dependencies"
            android.content.pm.ApplicationInfo r3 = r8.applicationInfo     // Catch:{ Exception -> 0x00ab }
            android.os.Bundle r3 = r3.metaData     // Catch:{ Exception -> 0x00ab }
            java.lang.String r5 = "dependencies"
            java.lang.String r3 = r3.getString(r5)     // Catch:{ Exception -> 0x00ab }
            r4.put(r0, r3)     // Catch:{ Exception -> 0x00ab }
            java.lang.String r0 = "weakdependencies"
            android.content.pm.ApplicationInfo r3 = r8.applicationInfo     // Catch:{ Exception -> 0x00ab }
            android.os.Bundle r3 = r3.metaData     // Catch:{ Exception -> 0x00ab }
            java.lang.String r5 = "weakdependencies"
            java.lang.String r3 = r3.getString(r5)     // Catch:{ Exception -> 0x00ab }
            r4.put(r0, r3)     // Catch:{ Exception -> 0x00ab }
            java.lang.String r0 = "reversedependencies"
            android.content.pm.ApplicationInfo r3 = r8.applicationInfo     // Catch:{ Exception -> 0x00ab }
            android.os.Bundle r3 = r3.metaData     // Catch:{ Exception -> 0x00ab }
            java.lang.String r5 = "reversedependencies"
            java.lang.String r3 = r3.getString(r5)     // Catch:{ Exception -> 0x00ab }
            r4.put(r0, r3)     // Catch:{ Exception -> 0x00ab }
            java.lang.String r0 = "thirdpartyso"
            android.content.pm.ApplicationInfo r3 = r8.applicationInfo     // Catch:{ Exception -> 0x00ab }
            android.os.Bundle r3 = r3.metaData     // Catch:{ Exception -> 0x00ab }
            java.lang.String r5 = "thirdpartyso"
            boolean r3 = r3.getBoolean(r5)     // Catch:{ Exception -> 0x00ab }
            r4.put(r0, r3)     // Catch:{ Exception -> 0x00ab }
            r3 = 0
            java.io.File r0 = new java.io.File     // Catch:{ Exception -> 0x00af }
            java.lang.String r5 = r7.c     // Catch:{ Exception -> 0x00af }
            r0.<init>(r5)     // Catch:{ Exception -> 0x00af }
            boolean r3 = r0.exists()     // Catch:{ Exception -> 0x00bd }
            if (r3 != 0) goto L_0x009e
            r0.createNewFile()     // Catch:{ Exception -> 0x00bd }
        L_0x009e:
            java.lang.String r3 = r4.toString()
            boolean r0 = com.alibaba.wireless.security.framework.utils.b.a(r0, r3)
            if (r0 != 0) goto L_0x00bf
            r0 = r1
            goto L_0x0007
        L_0x00ab:
            r0 = move-exception
            r0 = r2
            goto L_0x0007
        L_0x00af:
            r0 = move-exception
            r0 = r3
        L_0x00b1:
            boolean r3 = r0.exists()
            if (r3 != 0) goto L_0x009e
            r0.createNewFile()     // Catch:{ Exception -> 0x00bb }
            goto L_0x009e
        L_0x00bb:
            r3 = move-exception
            goto L_0x009e
        L_0x00bd:
            r3 = move-exception
            goto L_0x00b1
        L_0x00bf:
            r0 = r2
            goto L_0x0007
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.wireless.security.framework.a.a(android.content.pm.PackageInfo, java.lang.String):boolean");
    }

    public JSONObject b() {
        JSONObject jSONObject;
        if (this.b != null) {
            return this.b;
        }
        try {
            String a2 = b.a(new File(this.c));
            jSONObject = (a2 == null || a2.length() <= 0) ? null : new JSONObject(a2);
        } catch (Exception e) {
            jSONObject = null;
        }
        this.b = jSONObject;
        return jSONObject;
    }
}
