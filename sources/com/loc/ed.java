package com.loc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.text.TextUtils;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClientOption;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import com.yunos.tv.alitvasrsdk.DMAction;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import org.json.JSONObject;

/* compiled from: Cache */
public final class ed {
    Hashtable<String, ArrayList<a>> a = new Hashtable<>();
    boolean b = true;
    long c = 0;
    String d = null;
    dz e = null;
    boolean f = true;
    boolean g = true;
    String h = String.valueOf(AMapLocationClientOption.GeoLanguage.DEFAULT);
    private long i = 0;
    private boolean j = false;
    private String k = "2.0.201501131131".replace(".", "");
    private String l = null;
    private String m = null;
    private long n = 0;

    /* compiled from: Cache */
    static class a {
        private dw a = null;
        private String b = null;

        protected a() {
        }

        public final dw a() {
            return this.a;
        }

        public final void a(dw dwVar) {
            this.a = dwVar;
        }

        public final void a(String str) {
            if (TextUtils.isEmpty(str)) {
                this.b = null;
            } else {
                this.b = str.replace("##", Constant.INTENT_JSON_MARK);
            }
        }

        public final String b() {
            return this.b;
        }
    }

    private dw a(String str, StringBuilder sb) {
        try {
            a a2 = str.contains("cgiwifi") ? a(sb, str) : str.contains("wifi") ? a(sb, str) : (!str.contains("cgi") || !this.a.containsKey(str) || this.a.get(str).size() <= 0) ? null : (a) this.a.get(str).get(0);
            if (a2 != null && et.a(a2.a())) {
                dw a3 = a2.a();
                a3.e("mem");
                a3.h(a2.b());
                if (em.b(a3.getTime())) {
                    if (et.a(a3)) {
                        this.c = 0;
                    }
                    a3.setLocationType(4);
                    return a3;
                } else if (this.a != null && this.a.containsKey(str)) {
                    this.a.get(str).remove(a2);
                }
            }
        } catch (Throwable th) {
            en.a(th, DMAction.Cache, "get1");
        }
        return null;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v3, resolved type: com.loc.ed$a} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private com.loc.ed.a a(java.lang.StringBuilder r19, java.lang.String r20) {
        /*
            r18 = this;
            r0 = r18
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ed$a>> r2 = r0.a
            boolean r2 = r2.isEmpty()
            if (r2 != 0) goto L_0x0010
            boolean r2 = android.text.TextUtils.isEmpty(r19)
            if (r2 == 0) goto L_0x0012
        L_0x0010:
            r4 = 0
        L_0x0011:
            return r4
        L_0x0012:
            r0 = r18
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ed$a>> r2 = r0.a
            r0 = r20
            boolean r2 = r2.containsKey(r0)
            if (r2 != 0) goto L_0x0020
            r4 = 0
            goto L_0x0011
        L_0x0020:
            r8 = 0
            java.util.Hashtable r11 = new java.util.Hashtable
            r11.<init>()
            java.util.Hashtable r12 = new java.util.Hashtable
            r12.<init>()
            java.util.Hashtable r13 = new java.util.Hashtable
            r13.<init>()
            r0 = r18
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ed$a>> r2 = r0.a
            r0 = r20
            java.lang.Object r2 = r2.get(r0)
            java.util.ArrayList r2 = (java.util.ArrayList) r2
            int r3 = r2.size()
            int r3 = r3 + -1
            r9 = r3
        L_0x0043:
            if (r9 < 0) goto L_0x01a7
            java.lang.Object r3 = r2.get(r9)
            r4 = r3
            com.loc.ed$a r4 = (com.loc.ed.a) r4
            java.lang.String r3 = r4.b()
            boolean r3 = android.text.TextUtils.isEmpty(r3)
            if (r3 != 0) goto L_0x01a2
            r5 = 0
            java.lang.String r3 = r4.b()
            boolean r6 = android.text.TextUtils.isEmpty(r3)
            if (r6 != 0) goto L_0x0067
            boolean r6 = android.text.TextUtils.isEmpty(r19)
            if (r6 == 0) goto L_0x0083
        L_0x0067:
            r3 = 0
        L_0x0068:
            if (r3 == 0) goto L_0x00ea
            r3 = 1
            java.lang.String r5 = r4.b()
            java.lang.String r6 = r19.toString()
            boolean r5 = com.loc.et.a((java.lang.String) r5, (java.lang.String) r6)
            if (r5 == 0) goto L_0x00e9
        L_0x0079:
            r11.clear()
            r12.clear()
            r13.clear()
            goto L_0x0011
        L_0x0083:
            java.lang.String r6 = ",access"
            boolean r6 = r3.contains(r6)
            if (r6 == 0) goto L_0x0098
            java.lang.String r6 = ",access"
            r0 = r19
            int r6 = r0.indexOf(r6)
            r7 = -1
            if (r6 != r7) goto L_0x009a
        L_0x0098:
            r3 = 0
            goto L_0x0068
        L_0x009a:
            java.lang.String r6 = ",access"
            java.lang.String[] r3 = r3.split(r6)
            r6 = 0
            r6 = r3[r6]
            java.lang.String r7 = "#"
            boolean r6 = r6.contains(r7)
            if (r6 == 0) goto L_0x00c8
            r6 = 0
            r6 = r3[r6]
            r7 = 0
            r3 = r3[r7]
            java.lang.String r7 = "#"
            int r3 = r3.lastIndexOf(r7)
            int r3 = r3 + 1
            java.lang.String r3 = r6.substring(r3)
        L_0x00c0:
            boolean r6 = android.text.TextUtils.isEmpty(r3)
            if (r6 == 0) goto L_0x00cc
            r3 = 0
            goto L_0x0068
        L_0x00c8:
            r6 = 0
            r3 = r3[r6]
            goto L_0x00c0
        L_0x00cc:
            java.lang.String r6 = r19.toString()
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.StringBuilder r3 = r7.append(r3)
            java.lang.String r7 = ",access"
            java.lang.StringBuilder r3 = r3.append(r7)
            java.lang.String r3 = r3.toString()
            boolean r3 = r6.contains(r3)
            goto L_0x0068
        L_0x00e9:
            r5 = r3
        L_0x00ea:
            java.lang.String r3 = r4.b()
            a((java.lang.String) r3, (java.util.Hashtable<java.lang.String, java.lang.String>) r11)
            java.lang.String r3 = r19.toString()
            a((java.lang.String) r3, (java.util.Hashtable<java.lang.String, java.lang.String>) r12)
            r13.clear()
            java.util.Set r3 = r11.keySet()
            java.util.Iterator r6 = r3.iterator()
        L_0x0103:
            boolean r3 = r6.hasNext()
            if (r3 == 0) goto L_0x0116
            java.lang.Object r3 = r6.next()
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r7 = ""
            r13.put(r3, r7)
            goto L_0x0103
        L_0x0116:
            java.util.Set r3 = r12.keySet()
            java.util.Iterator r6 = r3.iterator()
        L_0x011e:
            boolean r3 = r6.hasNext()
            if (r3 == 0) goto L_0x0131
            java.lang.Object r3 = r6.next()
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r7 = ""
            r13.put(r3, r7)
            goto L_0x011e
        L_0x0131:
            java.util.Set r14 = r13.keySet()
            int r3 = r14.size()
            double[] r15 = new double[r3]
            int r3 = r14.size()
            double[] r0 = new double[r3]
            r16 = r0
            r3 = 0
            java.util.Iterator r17 = r14.iterator()
            r10 = r3
        L_0x0149:
            if (r17 == 0) goto L_0x0175
            boolean r3 = r17.hasNext()
            if (r3 == 0) goto L_0x0175
            java.lang.Object r3 = r17.next()
            java.lang.String r3 = (java.lang.String) r3
            boolean r6 = r11.containsKey(r3)
            if (r6 == 0) goto L_0x016f
            r6 = 4607182418800017408(0x3ff0000000000000, double:1.0)
        L_0x015f:
            r15[r10] = r6
            boolean r3 = r12.containsKey(r3)
            if (r3 == 0) goto L_0x0172
            r6 = 4607182418800017408(0x3ff0000000000000, double:1.0)
        L_0x0169:
            r16[r10] = r6
            int r3 = r10 + 1
            r10 = r3
            goto L_0x0149
        L_0x016f:
            r6 = 0
            goto L_0x015f
        L_0x0172:
            r6 = 0
            goto L_0x0169
        L_0x0175:
            r14.clear()
            double[] r3 = a((double[]) r15, (double[]) r16)
            r6 = 0
            r6 = r3[r6]
            r14 = 4605380979056443392(0x3fe99999a0000000, double:0.800000011920929)
            int r6 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
            if (r6 >= 0) goto L_0x0079
            r6 = 1
            r6 = r3[r6]
            r14 = 4603741668684706349(0x3fe3c6a7ef9db22d, double:0.618)
            int r6 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
            if (r6 >= 0) goto L_0x0079
            if (r5 == 0) goto L_0x01a2
            r5 = 0
            r6 = r3[r5]
            r14 = 4603741668684706349(0x3fe3c6a7ef9db22d, double:0.618)
            int r3 = (r6 > r14 ? 1 : (r6 == r14 ? 0 : -1))
            if (r3 >= 0) goto L_0x0079
        L_0x01a2:
            int r3 = r9 + -1
            r9 = r3
            goto L_0x0043
        L_0x01a7:
            r4 = r8
            goto L_0x0079
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ed.a(java.lang.StringBuilder, java.lang.String):com.loc.ed$a");
    }

    private String a(String str, StringBuilder sb, Context context) {
        if (context == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            if (this.l == null) {
                this.l = ec.a("MD5", k.c(context));
            }
            if (str.contains("&")) {
                str = str.substring(0, str.indexOf("&"));
            }
            String substring = str.substring(str.lastIndexOf(Constant.INTENT_JSON_MARK) + 1);
            if (substring.equals("cgi")) {
                jSONObject.put("cgi", str.substring(0, str.length() - 12));
            } else if (!TextUtils.isEmpty(sb) && sb.indexOf(",access") != -1) {
                jSONObject.put("cgi", str.substring(0, str.length() - (substring.length() + 9)));
                String[] split = sb.toString().split(",access");
                jSONObject.put("mmac", split[0].contains(Constant.INTENT_JSON_MARK) ? split[0].substring(split[0].lastIndexOf(Constant.INTENT_JSON_MARK) + 1) : split[0]);
            }
            try {
                return o.b(ec.b(jSONObject.toString().getBytes("UTF-8"), this.l));
            } catch (UnsupportedEncodingException e2) {
                return null;
            }
        } catch (Throwable th) {
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:109:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0038  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0043  */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x0266  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(android.content.Context r13, java.lang.String r14) throws java.lang.Exception {
        /*
            r12 = this;
            r9 = 0
            boolean r0 = com.loc.em.m()
            if (r0 != 0) goto L_0x0008
        L_0x0007:
            return
        L_0x0008:
            if (r13 == 0) goto L_0x0007
            java.lang.String r0 = "hmdb"
            r1 = 0
            r2 = 0
            android.database.sqlite.SQLiteDatabase r0 = r13.openOrCreateDatabase(r0, r1, r2)     // Catch:{ Throwable -> 0x0318, all -> 0x030a }
            java.lang.String r1 = "hist"
            boolean r1 = com.loc.et.a((android.database.sqlite.SQLiteDatabase) r0, (java.lang.String) r1)     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            if (r1 != 0) goto L_0x0047
            if (r0 == 0) goto L_0x0007
            boolean r1 = r0.isOpen()     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            if (r1 == 0) goto L_0x0007
            r0.close()     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            goto L_0x0007
        L_0x0028:
            r1 = move-exception
            r11 = r1
            r1 = r9
            r9 = r0
            r0 = r11
        L_0x002d:
            java.lang.String r2 = "DB"
            java.lang.String r3 = "fetchHist p2"
            com.loc.en.a(r0, r2, r3)     // Catch:{ all -> 0x0314 }
            if (r1 == 0) goto L_0x003b
            r1.close()
        L_0x003b:
            if (r9 == 0) goto L_0x0007
            boolean r0 = r9.isOpen()
            if (r0 == 0) goto L_0x0007
            r9.close()
            goto L_0x0007
        L_0x0047:
            long r2 = com.loc.et.a()     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            long r4 = com.loc.em.l()     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            long r2 = r2 - r4
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            r10.<init>()     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            java.lang.String r1 = "time >"
            java.lang.StringBuilder r1 = r10.append(r1)     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            r1.append(r2)     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            if (r14 == 0) goto L_0x007f
            java.lang.String r1 = " and feature = '"
            java.lang.StringBuilder r1 = r10.append(r1)     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            r2.<init>()     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            java.lang.StringBuilder r2 = r2.append(r14)     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            java.lang.String r3 = "'"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            java.lang.String r2 = r2.toString()     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            r1.append(r2)     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
        L_0x007f:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            java.lang.String r2 = "hist"
            r1.<init>(r2)     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            java.lang.String r2 = r12.k     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            r2 = 3
            java.lang.String[] r2 = new java.lang.String[r2]     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            r3 = 0
            java.lang.String r4 = "feature"
            r2[r3] = r4     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            r3 = 1
            java.lang.String r4 = " nb"
            r2[r3] = r4     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            r3 = 2
            java.lang.String r4 = "loc"
            r2[r3] = r4     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            java.lang.String r3 = r10.toString()     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            r4 = 0
            r5 = 0
            r6 = 0
            java.lang.String r7 = "time ASC"
            r8 = 0
            android.database.Cursor r7 = r0.query(r1, r2, r3, r4, r5, r6, r7, r8)     // Catch:{ Throwable -> 0x0028, all -> 0x030e }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r3.<init>()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r1 = r12.l     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r1 != 0) goto L_0x00cb
            java.lang.String r1 = "MD5"
            java.lang.String r2 = com.loc.k.c(r13)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r1 = com.loc.ec.a((java.lang.String) r1, (java.lang.String) r2)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r12.l = r1     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
        L_0x00cb:
            if (r7 == 0) goto L_0x01ae
            boolean r1 = r7.moveToFirst()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r1 == 0) goto L_0x01ae
        L_0x00d3:
            r1 = 0
            java.lang.String r1 = r7.getString(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r2 = "{"
            boolean r1 = r1.startsWith(r2)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r1 == 0) goto L_0x01e8
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r1 = 0
            java.lang.String r1 = r7.getString(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r2.<init>(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r1 = 0
            int r4 = r3.length()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r3.delete(r1, r4)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r1 = 1
            java.lang.String r1 = r7.getString(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r1 != 0) goto L_0x01c0
            r1 = 1
            java.lang.String r1 = r7.getString(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r3.append(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
        L_0x0106:
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r4 = 2
            java.lang.String r4 = r7.getString(r4)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r1.<init>(r4)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r4 = "type"
            boolean r4 = com.loc.et.a((org.json.JSONObject) r1, (java.lang.String) r4)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r4 == 0) goto L_0x0122
            java.lang.String r4 = "type"
            java.lang.String r5 = "new"
            r1.put(r4, r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
        L_0x0122:
            com.loc.dw r4 = new com.loc.dw     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = ""
            r4.<init>(r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r4.b((org.json.JSONObject) r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r1 = "mmac"
            boolean r1 = com.loc.et.a((org.json.JSONObject) r2, (java.lang.String) r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r1 == 0) goto L_0x02ac
            java.lang.String r1 = "cgi"
            boolean r1 = com.loc.et.a((org.json.JSONObject) r2, (java.lang.String) r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r1 == 0) goto L_0x02ac
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r1.<init>()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = "cgi"
            java.lang.String r5 = r2.getString(r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.StringBuilder r1 = r1.append(r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = "#"
            java.lang.StringBuilder r1 = r1.append(r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r5.<init>()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.StringBuilder r1 = r5.append(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = "network#"
            java.lang.StringBuilder r1 = r1.append(r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = "cgi"
            java.lang.String r2 = r2.getString(r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = "#"
            boolean r2 = r2.contains(r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r2 == 0) goto L_0x0296
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r2.<init>()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.StringBuilder r1 = r2.append(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r2 = "cgiwifi"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r2 = r1.toString()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
        L_0x0192:
            r6 = 0
            r1 = r12
            r5 = r13
            r1.a(r2, r3, r4, r5, r6)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
        L_0x0198:
            boolean r1 = r7.moveToNext()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r1 != 0) goto L_0x00d3
            r1 = 0
            int r2 = r3.length()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r3.delete(r1, r2)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r1 = 0
            int r2 = r10.length()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r10.delete(r1, r2)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
        L_0x01ae:
            if (r7 == 0) goto L_0x01b3
            r7.close()
        L_0x01b3:
            if (r0 == 0) goto L_0x0007
            boolean r1 = r0.isOpen()
            if (r1 == 0) goto L_0x0007
            r0.close()
            goto L_0x0007
        L_0x01c0:
            java.lang.String r1 = "mmac"
            boolean r1 = com.loc.et.a((org.json.JSONObject) r2, (java.lang.String) r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r1 == 0) goto L_0x0106
            java.lang.String r1 = "#"
            java.lang.StringBuilder r1 = r3.append(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r4 = "mmac"
            java.lang.String r4 = r2.getString(r4)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r1.append(r4)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r1 = ",access"
            r3.append(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            goto L_0x0106
        L_0x01e2:
            r1 = move-exception
            r9 = r0
            r0 = r1
            r1 = r7
            goto L_0x002d
        L_0x01e8:
            r1 = 0
            java.lang.String r1 = r7.getString(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            byte[] r1 = com.loc.o.b((java.lang.String) r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            org.json.JSONObject r2 = new org.json.JSONObject     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r4 = new java.lang.String     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = r12.l     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            byte[] r1 = com.loc.ec.c(r1, r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = "UTF-8"
            r4.<init>(r1, r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r2.<init>(r4)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r1 = 0
            int r4 = r3.length()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r3.delete(r1, r4)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r1 = 1
            java.lang.String r1 = r7.getString(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r1 != 0) goto L_0x0275
            r1 = 1
            java.lang.String r1 = r7.getString(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            byte[] r1 = com.loc.o.b((java.lang.String) r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r4 = new java.lang.String     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = r12.l     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            byte[] r1 = com.loc.ec.c(r1, r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = "UTF-8"
            r4.<init>(r1, r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r3.append(r4)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
        L_0x0231:
            r1 = 2
            java.lang.String r1 = r7.getString(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            byte[] r4 = com.loc.o.b((java.lang.String) r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            org.json.JSONObject r1 = new org.json.JSONObject     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = new java.lang.String     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r6 = r12.l     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            byte[] r4 = com.loc.ec.c(r4, r6)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r6 = "UTF-8"
            r5.<init>(r4, r6)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r1.<init>(r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r4 = "type"
            boolean r4 = com.loc.et.a((org.json.JSONObject) r1, (java.lang.String) r4)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r4 == 0) goto L_0x0122
            java.lang.String r4 = "type"
            java.lang.String r5 = "new"
            r1.put(r4, r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            goto L_0x0122
        L_0x0261:
            r1 = move-exception
            r9 = r0
            r0 = r1
        L_0x0264:
            if (r7 == 0) goto L_0x0269
            r7.close()
        L_0x0269:
            if (r9 == 0) goto L_0x0274
            boolean r1 = r9.isOpen()
            if (r1 == 0) goto L_0x0274
            r9.close()
        L_0x0274:
            throw r0
        L_0x0275:
            java.lang.String r1 = "mmac"
            boolean r1 = com.loc.et.a((org.json.JSONObject) r2, (java.lang.String) r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r1 == 0) goto L_0x0231
            java.lang.String r1 = "#"
            java.lang.StringBuilder r1 = r3.append(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r4 = "mmac"
            java.lang.String r4 = r2.getString(r4)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r1.append(r4)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r1 = ",access"
            r3.append(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            goto L_0x0231
        L_0x0296:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r2.<init>()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.StringBuilder r1 = r2.append(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r2 = "wifi"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r2 = r1.toString()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            goto L_0x0192
        L_0x02ac:
            java.lang.String r1 = "cgi"
            boolean r1 = com.loc.et.a((org.json.JSONObject) r2, (java.lang.String) r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r1 == 0) goto L_0x0198
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r1.<init>()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = "cgi"
            java.lang.String r5 = r2.getString(r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.StringBuilder r1 = r1.append(r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = "#"
            java.lang.StringBuilder r1 = r1.append(r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r5.<init>()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.StringBuilder r1 = r5.append(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = "network#"
            java.lang.StringBuilder r1 = r1.append(r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r1 = r1.toString()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = "cgi"
            java.lang.String r2 = r2.getString(r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r5 = "#"
            boolean r2 = r2.contains(r5)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            if (r2 == 0) goto L_0x0198
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            r2.<init>()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.StringBuilder r1 = r2.append(r1)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r2 = "cgi"
            java.lang.StringBuilder r1 = r1.append(r2)     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            java.lang.String r2 = r1.toString()     // Catch:{ Throwable -> 0x01e2, all -> 0x0261 }
            goto L_0x0192
        L_0x030a:
            r0 = move-exception
            r7 = r9
            goto L_0x0264
        L_0x030e:
            r1 = move-exception
            r7 = r9
            r9 = r0
            r0 = r1
            goto L_0x0264
        L_0x0314:
            r0 = move-exception
            r7 = r1
            goto L_0x0264
        L_0x0318:
            r0 = move-exception
            r1 = r9
            goto L_0x002d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ed.a(android.content.Context, java.lang.String):void");
    }

    private void a(String str, AMapLocation aMapLocation, StringBuilder sb, Context context) throws Exception {
        SQLiteDatabase sQLiteDatabase = null;
        if (context != null) {
            if (this.l == null) {
                this.l = ec.a("MD5", k.c(context));
            }
            String a2 = a(str, sb, context);
            StringBuilder sb2 = new StringBuilder();
            try {
                SQLiteDatabase openOrCreateDatabase = context.openOrCreateDatabase("hmdb", 0, (SQLiteDatabase.CursorFactory) null);
                sb2.append("CREATE TABLE IF NOT EXISTS hist");
                sb2.append(this.k);
                sb2.append(" (feature VARCHAR PRIMARY KEY, nb VARCHAR, loc VARCHAR, time VARCHAR);");
                openOrCreateDatabase.execSQL(sb2.toString());
                sb2.delete(0, sb2.length());
                sb2.append("REPLACE INTO ");
                sb2.append("hist").append(this.k);
                sb2.append(" VALUES (?, ?, ?, ?)");
                Object[] objArr = {a2, ec.b(sb.toString().getBytes("UTF-8"), this.l), ec.b(aMapLocation.toStr().getBytes("UTF-8"), this.l), Long.valueOf(aMapLocation.getTime())};
                for (int i2 = 1; i2 < 3; i2++) {
                    objArr[i2] = o.b((byte[]) objArr[i2]);
                }
                openOrCreateDatabase.execSQL(sb2.toString(), objArr);
                sb2.delete(0, sb2.length());
                sb2.delete(0, sb2.length());
                if (openOrCreateDatabase != null && openOrCreateDatabase.isOpen()) {
                    openOrCreateDatabase.close();
                }
            } catch (Throwable th) {
                sb2.delete(0, sb2.length());
                if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
                    sQLiteDatabase.close();
                }
                throw th;
            }
        }
    }

    private static void a(String str, Hashtable<String, String> hashtable) {
        if (!TextUtils.isEmpty(str)) {
            hashtable.clear();
            for (String str2 : str.split(Constant.INTENT_JSON_MARK)) {
                if (!TextUtils.isEmpty(str2) && !str2.contains("|")) {
                    hashtable.put(str2, "");
                }
            }
        }
    }

    private static double[] a(double[] dArr, double[] dArr2) {
        double[] dArr3 = new double[3];
        double d2 = ClientTraceData.b.f47a;
        double d3 = ClientTraceData.b.f47a;
        double d4 = ClientTraceData.b.f47a;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < dArr.length; i4++) {
            d3 += dArr[i4] * dArr[i4];
            d4 += dArr2[i4] * dArr2[i4];
            d2 += dArr[i4] * dArr2[i4];
            if (dArr2[i4] == 1.0d) {
                i2++;
                if (dArr[i4] == 1.0d) {
                    i3++;
                }
            }
        }
        dArr3[0] = d2 / (Math.sqrt(d4) * Math.sqrt(d3));
        dArr3[1] = (1.0d * ((double) i3)) / ((double) i2);
        dArr3[2] = (double) i3;
        for (int i5 = 0; i5 < 2; i5++) {
            if (dArr3[i5] > 1.0d) {
                dArr3[i5] = 1.0d;
            }
        }
        return dArr3;
    }

    private boolean b() {
        long b2 = et.b() - this.i;
        if (this.i == 0) {
            return false;
        }
        return this.a.size() > 360 || b2 > 36000000;
    }

    private void c() {
        this.i = 0;
        if (!this.a.isEmpty()) {
            this.a.clear();
        }
        this.j = false;
    }

    public final dw a(Context context, String str, StringBuilder sb, boolean z) {
        if (TextUtils.isEmpty(str) || !em.m()) {
            return null;
        }
        String str2 = str + "&" + this.f + "&" + this.g + "&" + this.h;
        if (str2.contains("gps") || !em.m() || sb == null) {
            return null;
        }
        if (b()) {
            c();
            return null;
        }
        if (z && !this.j) {
            try {
                String a2 = a(str2, sb, context);
                c();
                a(context, a2);
            } catch (Throwable th) {
            }
        }
        if (!this.a.isEmpty()) {
            return a(str2, sb);
        }
        return null;
    }

    public final dw a(ea eaVar, boolean z, dw dwVar, eb ebVar, StringBuilder sb, String str, Context context, boolean z2) {
        boolean z3;
        boolean z4;
        if (!(!(this.b && (em.m() || z2)) ? false : dwVar == null || (em.b(dwVar.getTime()) && !z2))) {
            return null;
        }
        try {
            dz d2 = eaVar.d();
            boolean z5 = !(d2 == null && this.e == null) && (this.e == null || !this.e.equals(d2));
            if (dwVar != null) {
                z3 = dwVar.getAccuracy() > 299.0f && ebVar.e().size() > 5;
            } else {
                z3 = false;
            }
            if (dwVar == null || this.d == null || z3 || z5) {
                z4 = false;
            } else {
                z4 = et.a(this.d, sb.toString());
                boolean z6 = this.c != 0 && et.b() - this.c < 3000;
                if ((z4 || z6) && et.a(dwVar)) {
                    dwVar.e("mem");
                    dwVar.setLocationType(2);
                    return dwVar;
                }
            }
            if (!z4) {
                this.c = et.b();
            } else {
                this.c = 0;
            }
            if (this.m == null || str.equals(this.m)) {
                if (this.m == null) {
                    this.n = et.a();
                    this.m = str;
                } else {
                    this.n = et.a();
                }
            } else if (et.a() - this.n < 3000) {
                str = this.m;
            } else {
                this.n = et.a();
                this.m = str;
            }
            dw dwVar2 = null;
            if (!z3 && !z) {
                dwVar2 = a(context, str, sb, false);
            }
            if ((!z && !et.a(dwVar2)) || z3) {
                return null;
            }
            if (z) {
                return null;
            }
            this.c = 0;
            dwVar2.setLocationType(4);
            return dwVar2;
        } catch (Throwable th) {
            return null;
        }
    }

    public final void a() {
        this.c = 0;
        this.d = null;
    }

    public final void a(Context context) {
        if (!this.j) {
            try {
                c();
                a(context, (String) null);
            } catch (Throwable th) {
                en.a(th, DMAction.Cache, "loadDB");
            }
            this.j = true;
        }
    }

    public final void a(AMapLocationClientOption aMapLocationClientOption) {
        this.g = aMapLocationClientOption.isNeedAddress();
        this.f = aMapLocationClientOption.isOffset();
        this.b = aMapLocationClientOption.isLocationCacheEnable();
        this.h = String.valueOf(aMapLocationClientOption.getGeoLanguage());
    }

    public final void a(dz dzVar) {
        this.e = dzVar;
    }

    public final void a(String str) {
        this.d = str;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void a(java.lang.String r8, java.lang.StringBuilder r9, com.loc.dw r10, android.content.Context r11, boolean r12) {
        /*
            r7 = this;
            r0 = 1
            r1 = 0
            boolean r2 = com.loc.et.a((com.loc.dw) r10)     // Catch:{ Throwable -> 0x0182 }
            if (r2 != 0) goto L_0x0009
        L_0x0008:
            return
        L_0x0009:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0182 }
            r2.<init>()     // Catch:{ Throwable -> 0x0182 }
            java.lang.StringBuilder r2 = r2.append(r8)     // Catch:{ Throwable -> 0x0182 }
            java.lang.String r3 = "&"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x0182 }
            boolean r3 = r10.isOffset()     // Catch:{ Throwable -> 0x0182 }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x0182 }
            java.lang.String r3 = "&"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x0182 }
            boolean r3 = r10.i()     // Catch:{ Throwable -> 0x0182 }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x0182 }
            java.lang.String r3 = "&"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x0182 }
            java.lang.String r3 = r10.j()     // Catch:{ Throwable -> 0x0182 }
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch:{ Throwable -> 0x0182 }
            java.lang.String r6 = r2.toString()     // Catch:{ Throwable -> 0x0182 }
            boolean r2 = android.text.TextUtils.isEmpty(r6)     // Catch:{ Throwable -> 0x0182 }
            if (r2 != 0) goto L_0x004f
            boolean r2 = com.loc.et.a((com.loc.dw) r10)     // Catch:{ Throwable -> 0x0182 }
            if (r2 != 0) goto L_0x00dd
        L_0x004f:
            r0 = r1
        L_0x0050:
            if (r0 == 0) goto L_0x0008
            java.lang.String r0 = r10.e()     // Catch:{ Throwable -> 0x0182 }
            java.lang.String r2 = "mem"
            boolean r0 = r0.equals(r2)     // Catch:{ Throwable -> 0x0182 }
            if (r0 != 0) goto L_0x0008
            java.lang.String r0 = r10.e()     // Catch:{ Throwable -> 0x0182 }
            java.lang.String r2 = "file"
            boolean r0 = r0.equals(r2)     // Catch:{ Throwable -> 0x0182 }
            if (r0 != 0) goto L_0x0008
            java.lang.String r0 = r10.e()     // Catch:{ Throwable -> 0x0182 }
            java.lang.String r2 = "wifioff"
            boolean r0 = r0.equals(r2)     // Catch:{ Throwable -> 0x0182 }
            if (r0 != 0) goto L_0x0008
            java.lang.String r0 = "-3"
            java.lang.String r2 = r10.d()     // Catch:{ Throwable -> 0x0182 }
            boolean r0 = r0.equals(r2)     // Catch:{ Throwable -> 0x0182 }
            if (r0 != 0) goto L_0x0008
            boolean r0 = r7.b()     // Catch:{ Throwable -> 0x0182 }
            if (r0 == 0) goto L_0x008f
            r7.c()     // Catch:{ Throwable -> 0x0182 }
        L_0x008f:
            org.json.JSONObject r0 = r10.f()     // Catch:{ Throwable -> 0x0182 }
            java.lang.String r2 = "offpct"
            boolean r2 = com.loc.et.a((org.json.JSONObject) r0, (java.lang.String) r2)     // Catch:{ Throwable -> 0x0182 }
            if (r2 == 0) goto L_0x00a5
            java.lang.String r2 = "offpct"
            r0.remove(r2)     // Catch:{ Throwable -> 0x0182 }
            r10.a((org.json.JSONObject) r0)     // Catch:{ Throwable -> 0x0182 }
        L_0x00a5:
            java.lang.String r0 = "wifi"
            boolean r0 = r6.contains(r0)     // Catch:{ Throwable -> 0x0182 }
            if (r0 == 0) goto L_0x019a
            boolean r0 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x0182 }
            if (r0 != 0) goto L_0x0008
            float r0 = r10.getAccuracy()     // Catch:{ Throwable -> 0x0182 }
            r2 = 1133903872(0x43960000, float:300.0)
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 < 0) goto L_0x018e
            java.lang.String r0 = r9.toString()     // Catch:{ Throwable -> 0x0182 }
            java.lang.String r2 = "#"
            java.lang.String[] r2 = r0.split(r2)     // Catch:{ Throwable -> 0x0182 }
            int r3 = r2.length     // Catch:{ Throwable -> 0x0182 }
            r0 = r1
        L_0x00cb:
            if (r1 >= r3) goto L_0x00f5
            r4 = r2[r1]     // Catch:{ Throwable -> 0x0182 }
            java.lang.String r5 = ","
            boolean r4 = r4.contains(r5)     // Catch:{ Throwable -> 0x0182 }
            if (r4 == 0) goto L_0x00da
            int r0 = r0 + 1
        L_0x00da:
            int r1 = r1 + 1
            goto L_0x00cb
        L_0x00dd:
            java.lang.String r2 = "#"
            boolean r2 = r6.startsWith(r2)     // Catch:{ Throwable -> 0x0182 }
            if (r2 == 0) goto L_0x00e9
            r0 = r1
            goto L_0x0050
        L_0x00e9:
            java.lang.String r2 = "network"
            boolean r2 = r6.contains(r2)     // Catch:{ Throwable -> 0x0182 }
            if (r2 != 0) goto L_0x0050
            r0 = r1
            goto L_0x0050
        L_0x00f5:
            r1 = 8
            if (r0 >= r1) goto L_0x0008
        L_0x00f9:
            java.lang.String r0 = "cgiwifi"
            boolean r0 = r6.contains(r0)     // Catch:{ Throwable -> 0x0182 }
            if (r0 == 0) goto L_0x012b
            java.lang.String r0 = r10.g()     // Catch:{ Throwable -> 0x0182 }
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch:{ Throwable -> 0x0182 }
            if (r0 != 0) goto L_0x012b
            java.lang.String r0 = "cgiwifi"
            java.lang.String r1 = "cgi"
            java.lang.String r1 = r6.replace(r0, r1)     // Catch:{ Throwable -> 0x0182 }
            com.loc.dw r3 = r10.h()     // Catch:{ Throwable -> 0x0182 }
            boolean r0 = com.loc.et.a((com.loc.dw) r3)     // Catch:{ Throwable -> 0x0182 }
            if (r0 == 0) goto L_0x012b
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0182 }
            r2.<init>()     // Catch:{ Throwable -> 0x0182 }
            r5 = 1
            r0 = r7
            r4 = r11
            r0.a(r1, r2, r3, r4, r5)     // Catch:{ Throwable -> 0x0182 }
        L_0x012b:
            com.loc.dw r0 = r7.a((java.lang.String) r6, (java.lang.StringBuilder) r9)     // Catch:{ Throwable -> 0x0182 }
            boolean r1 = com.loc.et.a((com.loc.dw) r0)     // Catch:{ Throwable -> 0x0182 }
            if (r1 == 0) goto L_0x0144
            java.lang.String r0 = r0.toStr()     // Catch:{ Throwable -> 0x0182 }
            r1 = 3
            java.lang.String r1 = r10.toStr(r1)     // Catch:{ Throwable -> 0x0182 }
            boolean r0 = r0.equals(r1)     // Catch:{ Throwable -> 0x0182 }
            if (r0 != 0) goto L_0x0008
        L_0x0144:
            long r0 = com.loc.et.b()     // Catch:{ Throwable -> 0x0182 }
            r7.i = r0     // Catch:{ Throwable -> 0x0182 }
            com.loc.ed$a r1 = new com.loc.ed$a     // Catch:{ Throwable -> 0x0182 }
            r1.<init>()     // Catch:{ Throwable -> 0x0182 }
            r1.a((com.loc.dw) r10)     // Catch:{ Throwable -> 0x0182 }
            boolean r0 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x0182 }
            if (r0 == 0) goto L_0x01be
            r0 = 0
        L_0x0159:
            r1.a((java.lang.String) r0)     // Catch:{ Throwable -> 0x0182 }
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ed$a>> r0 = r7.a     // Catch:{ Throwable -> 0x0182 }
            boolean r0 = r0.containsKey(r6)     // Catch:{ Throwable -> 0x0182 }
            if (r0 == 0) goto L_0x01c3
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ed$a>> r0 = r7.a     // Catch:{ Throwable -> 0x0182 }
            java.lang.Object r0 = r0.get(r6)     // Catch:{ Throwable -> 0x0182 }
            java.util.ArrayList r0 = (java.util.ArrayList) r0     // Catch:{ Throwable -> 0x0182 }
            r0.add(r1)     // Catch:{ Throwable -> 0x0182 }
        L_0x016f:
            if (r12 == 0) goto L_0x0008
            r7.a((java.lang.String) r6, (com.amap.api.location.AMapLocation) r10, (java.lang.StringBuilder) r9, (android.content.Context) r11)     // Catch:{ Throwable -> 0x0176 }
            goto L_0x0008
        L_0x0176:
            r0 = move-exception
            java.lang.String r1 = "Cache"
            java.lang.String r2 = "add"
            com.loc.en.a(r0, r1, r2)     // Catch:{ Throwable -> 0x0182 }
            goto L_0x0008
        L_0x0182:
            r0 = move-exception
            java.lang.String r1 = "Cache"
            java.lang.String r2 = "add"
            com.loc.en.a(r0, r1, r2)
            goto L_0x0008
        L_0x018e:
            float r0 = r10.getAccuracy()     // Catch:{ Throwable -> 0x0182 }
            r1 = 1077936128(0x40400000, float:3.0)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto L_0x00f9
            goto L_0x0008
        L_0x019a:
            java.lang.String r0 = "cgi"
            boolean r0 = r6.contains(r0)     // Catch:{ Throwable -> 0x0182 }
            if (r0 == 0) goto L_0x012b
            if (r9 == 0) goto L_0x01af
            java.lang.String r0 = ","
            int r0 = r9.indexOf(r0)     // Catch:{ Throwable -> 0x0182 }
            r1 = -1
            if (r0 != r1) goto L_0x0008
        L_0x01af:
            java.lang.String r0 = "4"
            java.lang.String r1 = r10.d()     // Catch:{ Throwable -> 0x0182 }
            boolean r0 = r0.equals(r1)     // Catch:{ Throwable -> 0x0182 }
            if (r0 == 0) goto L_0x012b
            goto L_0x0008
        L_0x01be:
            java.lang.String r0 = r9.toString()     // Catch:{ Throwable -> 0x0182 }
            goto L_0x0159
        L_0x01c3:
            java.util.ArrayList r0 = new java.util.ArrayList     // Catch:{ Throwable -> 0x0182 }
            r0.<init>()     // Catch:{ Throwable -> 0x0182 }
            r0.add(r1)     // Catch:{ Throwable -> 0x0182 }
            java.util.Hashtable<java.lang.String, java.util.ArrayList<com.loc.ed$a>> r1 = r7.a     // Catch:{ Throwable -> 0x0182 }
            r1.put(r6, r0)     // Catch:{ Throwable -> 0x0182 }
            goto L_0x016f
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ed.a(java.lang.String, java.lang.StringBuilder, com.loc.dw, android.content.Context, boolean):void");
    }

    public final void b(Context context) {
        SQLiteDatabase sQLiteDatabase = null;
        try {
            c();
            if (context != null) {
                try {
                    sQLiteDatabase = context.openOrCreateDatabase("hmdb", 0, (SQLiteDatabase.CursorFactory) null);
                    if (et.a(sQLiteDatabase, "hist")) {
                        sQLiteDatabase.delete("hist" + this.k, "time<?", new String[]{String.valueOf(et.a() - ZipAppConstants.UPDATEGROUPID_AGE)});
                        if (sQLiteDatabase != null) {
                            if (sQLiteDatabase.isOpen()) {
                                sQLiteDatabase.close();
                            }
                        }
                    } else if (sQLiteDatabase != null && sQLiteDatabase.isOpen()) {
                        sQLiteDatabase.close();
                    }
                } catch (Throwable th) {
                    en.a(th, "DB", "clearHist p2");
                    if (sQLiteDatabase != null) {
                        if (sQLiteDatabase.isOpen()) {
                            sQLiteDatabase.close();
                        }
                    }
                }
            }
            this.j = false;
            this.d = null;
            this.n = 0;
        } catch (Throwable th2) {
            en.a(th2, DMAction.Cache, "destroy part");
        }
    }
}
