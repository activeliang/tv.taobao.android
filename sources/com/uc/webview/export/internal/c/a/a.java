package com.uc.webview.export.internal.c.a;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.HandlerThread;
import com.ali.auth.third.core.model.KernelMessageConstants;
import com.bftv.fui.constantplugin.Constant;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.internal.d;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.utility.Utils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/* compiled from: ProGuard */
public final class a {
    public static a a;
    Context b;
    public Handler c;
    public Map<String, C0009a> d;
    public List<b> e;
    public SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
    public SimpleDateFormat g = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public Object h = new Object();
    private HandlerThread i = new HandlerThread("SDKWaStatThread", 0);

    static /* synthetic */ byte[] a(a aVar, String[] strArr) {
        ArrayList<String[]> arrayList;
        Object[] b2 = aVar.b();
        if (b2 == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("lt=uc");
        Map map = (Map) b2[0];
        List<b> list = (List) b2[1];
        List<PackageInfo> installedPackages = aVar.b.getPackageManager().getInstalledPackages(0);
        List<String[]> a2 = aVar.a(installedPackages);
        strArr[0] = b(map, list);
        for (Map.Entry entry : map.entrySet()) {
            String str = ((C0009a) entry.getValue()).b.get("tm");
            String substring = (str == null || str.length() <= 10) ? null : str.substring(0, 10);
            sb.append("\n");
            for (String[] next : a2) {
                sb.append(next[0]).append("=").append(next[1]).append("`");
            }
            if (substring.equals(strArr[0])) {
                String str2 = strArr[0];
                if (str2 == null) {
                    arrayList = new ArrayList<>(0);
                } else {
                    arrayList = new ArrayList<>();
                    String string = aVar.b.getSharedPreferences("UC_WA_STAT", 0).getString("4", (String) null);
                    if (string == null || !string.equals(str2)) {
                        arrayList.add(new String[]{"sdk_3rdappf", b(installedPackages)});
                    }
                }
                for (String[] strArr2 : arrayList) {
                    sb.append(strArr2[0]).append("=").append(strArr2[1]).append("`");
                }
            }
            for (Map.Entry next2 : ((C0009a) entry.getValue()).a.entrySet()) {
                sb.append((String) next2.getKey()).append("=").append(String.valueOf(next2.getValue())).append("`");
            }
            for (Map.Entry next3 : ((C0009a) entry.getValue()).b.entrySet()) {
                sb.append((String) next3.getKey()).append("=").append((String) next3.getValue()).append("`");
            }
            for (Map.Entry next4 : d.v.entrySet()) {
                sb.append((String) next4.getKey()).append("=").append(String.valueOf(((Integer) next4.getValue()).intValue())).append("`");
            }
        }
        for (b bVar : list) {
            sb.append("\n");
            for (String[] next5 : a2) {
                sb.append(next5[0]).append("=").append(next5[1]).append("`");
            }
            for (Map.Entry next6 : bVar.b.entrySet()) {
                sb.append((String) next6.getKey()).append("=").append((String) next6.getValue()).append("`");
            }
        }
        if (Utils.sWAPrintLog) {
            Log.i("SDKWaStat", "getUploadData:\n" + sb.toString());
        }
        return sb.toString().getBytes();
    }

    /* renamed from: com.uc.webview.export.internal.c.a.a$a  reason: collision with other inner class name */
    /* compiled from: ProGuard */
    private class C0009a {
        public Map<String, Integer> a;
        public Map<String, String> b;

        private C0009a() {
            this.a = new HashMap();
            this.b = new HashMap();
        }

        public /* synthetic */ C0009a(a aVar, byte b2) {
            this();
        }
    }

    /* compiled from: ProGuard */
    private class b {
        String a;
        Map<String, String> b;

        public b(String str, Map<String, String> map) {
            this.a = str;
            this.b = map;
        }
    }

    private a() {
        this.i.start();
        this.c = new Handler(this.i.getLooper());
    }

    public static synchronized void a(Context context) {
        synchronized (a.class) {
            if (!d.f) {
                if (a == null) {
                    a = new a();
                }
                a.b = context.getApplicationContext();
            }
        }
    }

    /*  JADX ERROR: IndexOutOfBoundsException in pass: RegionMakerVisitor
        java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
        	at java.util.ArrayList.rangeCheck(ArrayList.java:657)
        	at java.util.ArrayList.get(ArrayList.java:433)
        	at jadx.core.dex.nodes.InsnNode.getArg(InsnNode.java:101)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:611)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:561)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:693)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMaker.processMonitorEnter(RegionMaker.java:598)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:133)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:86)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:49)
        */
    final synchronized void a() {
        /*
            r7 = this;
            r2 = 1
            r1 = 0
            monitor-enter(r7)
            boolean r0 = com.uc.webview.export.internal.d.f     // Catch:{ all -> 0x008f }
            if (r0 != 0) goto L_0x0026
            r0 = 10006(0x2716, float:1.4021E-41)
            r3 = 2
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ all -> 0x008f }
            r4 = 0
            java.lang.String r5 = "stat"
            r3[r4] = r5     // Catch:{ all -> 0x008f }
            r4 = 1
            r5 = 1
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)     // Catch:{ all -> 0x008f }
            r3[r4] = r5     // Catch:{ all -> 0x008f }
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r3)     // Catch:{ all -> 0x008f }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ all -> 0x008f }
            boolean r0 = r0.booleanValue()     // Catch:{ all -> 0x008f }
            if (r0 != 0) goto L_0x0028
        L_0x0026:
            monitor-exit(r7)
            return
        L_0x0028:
            boolean r0 = com.uc.webview.export.utility.Utils.sWAPrintLog     // Catch:{ Exception -> 0x0086 }
            if (r0 == 0) goto L_0x0035
            java.lang.String r0 = "SDKWaStat"
            java.lang.String r3 = "saveData"
            com.uc.webview.export.internal.utility.Log.d(r0, r3)     // Catch:{ Exception -> 0x0086 }
        L_0x0035:
            java.util.HashMap r3 = new java.util.HashMap     // Catch:{ Exception -> 0x0086 }
            r3.<init>()     // Catch:{ Exception -> 0x0086 }
            java.util.ArrayList r4 = new java.util.ArrayList     // Catch:{ Exception -> 0x0086 }
            r4.<init>()     // Catch:{ Exception -> 0x0086 }
            java.lang.Object r5 = r7.h     // Catch:{ Exception -> 0x0086 }
            monitor-enter(r5)     // Catch:{ Exception -> 0x0086 }
            r0 = 10010(0x271a, float:1.4027E-41)
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x008c }
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r6)     // Catch:{ all -> 0x008c }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ all -> 0x008c }
            boolean r0 = r0.booleanValue()     // Catch:{ all -> 0x008c }
            if (r0 == 0) goto L_0x006a
            java.util.Map<java.lang.String, com.uc.webview.export.internal.c.a.a$a> r0 = r7.d     // Catch:{ all -> 0x008c }
            if (r0 == 0) goto L_0x005d
            int r0 = r0.size()     // Catch:{ all -> 0x008c }
            if (r0 != 0) goto L_0x0088
        L_0x005d:
            r0 = r2
        L_0x005e:
            if (r0 != 0) goto L_0x006a
            java.util.Map<java.lang.String, com.uc.webview.export.internal.c.a.a$a> r0 = r7.d     // Catch:{ all -> 0x008c }
            r3.putAll(r0)     // Catch:{ all -> 0x008c }
            java.util.Map<java.lang.String, com.uc.webview.export.internal.c.a.a$a> r0 = r7.d     // Catch:{ all -> 0x008c }
            r0.clear()     // Catch:{ all -> 0x008c }
        L_0x006a:
            java.util.List<com.uc.webview.export.internal.c.a.a$b> r0 = r7.e     // Catch:{ all -> 0x008c }
            if (r0 == 0) goto L_0x0074
            int r0 = r0.size()     // Catch:{ all -> 0x008c }
            if (r0 != 0) goto L_0x008a
        L_0x0074:
            r0 = r2
        L_0x0075:
            if (r0 != 0) goto L_0x0081
            java.util.List<com.uc.webview.export.internal.c.a.a$b> r0 = r7.e     // Catch:{ all -> 0x008c }
            r4.addAll(r0)     // Catch:{ all -> 0x008c }
            java.util.List<com.uc.webview.export.internal.c.a.a$b> r0 = r7.e     // Catch:{ all -> 0x008c }
            r0.clear()     // Catch:{ all -> 0x008c }
        L_0x0081:
            monitor-exit(r5)     // Catch:{ all -> 0x008c }
            r7.a((java.util.Map<java.lang.String, com.uc.webview.export.internal.c.a.a.C0009a>) r3, (java.util.List<com.uc.webview.export.internal.c.a.a.b>) r4)     // Catch:{ Exception -> 0x0086 }
            goto L_0x0026
        L_0x0086:
            r0 = move-exception
            goto L_0x0026
        L_0x0088:
            r0 = r1
            goto L_0x005e
        L_0x008a:
            r0 = r1
            goto L_0x0075
        L_0x008c:
            r0 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x008c }
            throw r0     // Catch:{ Exception -> 0x0086 }
        L_0x008f:
            r0 = move-exception
            monitor-exit(r7)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.c.a.a.a():void");
    }

    private void a(Map<String, C0009a> map, List<b> list) {
        boolean z;
        FileWriter fileWriter;
        FileWriter fileWriter2;
        BufferedWriter bufferedWriter;
        Throwable th;
        BufferedWriter bufferedWriter2 = null;
        boolean z2 = true;
        if (map.size() == 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            if (list.size() != 0) {
                z2 = false;
            }
            if (z2) {
                return;
            }
        }
        String str = this.b.getApplicationContext().getApplicationInfo().dataDir + "/ucwa";
        File file = new File(str);
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(str, "wa_upload_new.wa");
        if (Utils.sWAPrintLog) {
            Log.d("SDKWaStat", "savePVToFile:" + file2);
        }
        try {
            fileWriter2 = new FileWriter(file2, true);
            try {
                bufferedWriter = new BufferedWriter(fileWriter2, 1024);
            } catch (Exception e2) {
                fileWriter = fileWriter2;
                UCCyclone.close(bufferedWriter2);
                UCCyclone.close(fileWriter);
            } catch (Throwable th2) {
                bufferedWriter = null;
                th = th2;
                UCCyclone.close(bufferedWriter);
                UCCyclone.close(fileWriter2);
                throw th;
            }
            try {
                for (Map.Entry next : map.entrySet()) {
                    bufferedWriter.write("@0");
                    bufferedWriter.write("@k@");
                    String str2 = (String) next.getKey();
                    String[] split = str2.split("~");
                    if (split[1].equals("0")) {
                        int intValue = ((Integer) d.a((int) KernelMessageConstants.PARAM_ERROR, new Object[0])).intValue();
                        if (intValue != 2) {
                            intValue = (intValue * 10) + d.l;
                        }
                        str2 = split[0] + "~" + intValue;
                    }
                    bufferedWriter.write(str2);
                    bufferedWriter.write("@d@");
                    a(bufferedWriter, ((C0009a) next.getValue()).a, 0);
                    a(bufferedWriter, ((C0009a) next.getValue()).b, 1);
                    bufferedWriter.newLine();
                }
                for (int i2 = 0; i2 < 10; i2++) {
                    if (i2 >= list.size()) {
                        break;
                    }
                    b bVar = list.get(i2);
                    bufferedWriter.write("@1");
                    bufferedWriter.write("@k@");
                    bufferedWriter.write(bVar.a);
                    bufferedWriter.write("@d@");
                    a(bufferedWriter, bVar.b, 1);
                    bufferedWriter.newLine();
                }
                UCCyclone.close(bufferedWriter);
                UCCyclone.close(fileWriter2);
            } catch (Exception e3) {
                bufferedWriter2 = bufferedWriter;
                fileWriter = fileWriter2;
                UCCyclone.close(bufferedWriter2);
                UCCyclone.close(fileWriter);
            } catch (Throwable th3) {
                th = th3;
                UCCyclone.close(bufferedWriter);
                UCCyclone.close(fileWriter2);
                throw th;
            }
        } catch (Exception e4) {
            fileWriter = null;
            UCCyclone.close(bufferedWriter2);
            UCCyclone.close(fileWriter);
        } catch (Throwable th4) {
            bufferedWriter = null;
            fileWriter2 = null;
            th = th4;
            UCCyclone.close(bufferedWriter);
            UCCyclone.close(fileWriter2);
            throw th;
        }
    }

    private static <T> void a(BufferedWriter bufferedWriter, Map<String, T> map, int i2) {
        if (!(map == null || map.size() == 0)) {
            for (Map.Entry next : map.entrySet()) {
                bufferedWriter.write((String) next.getKey());
                bufferedWriter.write("=");
                bufferedWriter.write(Constant.INTENT_JSON_MARK + i2);
                bufferedWriter.write(next.getValue() + "`");
            }
        }
    }

    private Object[] b() {
        FileReader fileReader;
        BufferedReader bufferedReader;
        Throwable th;
        C0009a aVar;
        String str = this.b.getApplicationContext().getApplicationInfo().dataDir + "/ucwa";
        File file = new File(str);
        if (!file.exists()) {
            file.mkdir();
        }
        File file2 = new File(str, "wa_upload_new.wa");
        if (Utils.sWAPrintLog) {
            Log.d("SDKWaStat", "getPVFromFile:" + file2 + " exists:" + file2.exists());
        }
        if (!file2.exists()) {
            return null;
        }
        FileReader fileReader2 = null;
        BufferedReader bufferedReader2 = null;
        HashMap hashMap = new HashMap();
        ArrayList arrayList = new ArrayList();
        try {
            fileReader = new FileReader(file2);
            try {
                bufferedReader = new BufferedReader(fileReader, 1024);
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        if (Utils.sWAPrintLog) {
                            Log.d("SDKWaStat", readLine);
                        }
                        if (!(readLine == null || readLine.trim().length() == 0)) {
                            String trim = readLine.trim();
                            if (trim.startsWith("@0") || trim.startsWith("@1")) {
                                int indexOf = trim.indexOf("@k@");
                                int indexOf2 = trim.indexOf("@d@");
                                if (indexOf >= 0 && indexOf2 >= 0) {
                                    String substring = trim.substring(indexOf + 3, indexOf2);
                                    String[] split = trim.substring(indexOf2 + 3).split("`");
                                    if (trim.startsWith("@0")) {
                                        String[] split2 = substring.split("~");
                                        if (split2.length == 2 && split2[0].length() == 8 && split2[1].length() <= 2) {
                                            C0009a aVar2 = (C0009a) hashMap.get(substring);
                                            if (aVar2 != null) {
                                                aVar = aVar2;
                                            } else if (hashMap.size() != 8) {
                                                C0009a aVar3 = new C0009a(this, (byte) 0);
                                                hashMap.put(substring, aVar3);
                                                aVar = aVar3;
                                            } else if (arrayList.size() == 10) {
                                                break;
                                            }
                                            for (String split3 : split) {
                                                String[] split4 = split3.split("=");
                                                if (split4.length == 2 && split4[1].length() > 2) {
                                                    String substring2 = split4[1].substring(2);
                                                    if (split4[1].startsWith("#0")) {
                                                        Integer num = aVar.a.get(split4[0]);
                                                        if (num == null) {
                                                            aVar.a.put(split4[0], Integer.valueOf(Integer.parseInt(substring2)));
                                                        } else {
                                                            aVar.a.put(split4[0], Integer.valueOf(num.intValue() + Integer.parseInt(substring2)));
                                                        }
                                                    } else if (split4[1].startsWith("#1")) {
                                                        aVar.b.put(split4[0], substring2);
                                                    }
                                                }
                                            }
                                            aVar.b.put("core_t", split2[1]);
                                        }
                                    } else if (arrayList.size() != 10) {
                                        HashMap hashMap2 = new HashMap(split.length);
                                        for (String split5 : split) {
                                            String[] split6 = split5.split("=");
                                            if (split6.length == 2) {
                                                hashMap2.put(split6[0], split6[1].substring(2));
                                            }
                                        }
                                        hashMap2.put("ev_ac", substring);
                                        arrayList.add(new b(substring, hashMap2));
                                    }
                                }
                            }
                        }
                    } catch (Exception e2) {
                        bufferedReader2 = bufferedReader;
                        fileReader2 = fileReader;
                        UCCyclone.close(bufferedReader2);
                        UCCyclone.close(fileReader2);
                        return null;
                    } catch (Throwable th2) {
                        th = th2;
                        UCCyclone.close(bufferedReader);
                        UCCyclone.close(fileReader);
                        throw th;
                    }
                }
                if (hashMap.size() > 0 || arrayList.size() > 0) {
                    Object[] objArr = {hashMap, arrayList};
                    UCCyclone.close(bufferedReader);
                    UCCyclone.close(fileReader);
                    return objArr;
                }
                UCCyclone.close(bufferedReader);
                UCCyclone.close(fileReader);
                return null;
            } catch (Exception e3) {
                fileReader2 = fileReader;
                UCCyclone.close(bufferedReader2);
                UCCyclone.close(fileReader2);
                return null;
            } catch (Throwable th3) {
                Throwable th4 = th3;
                bufferedReader = null;
                th = th4;
                UCCyclone.close(bufferedReader);
                UCCyclone.close(fileReader);
                throw th;
            }
        } catch (Exception e4) {
            UCCyclone.close(bufferedReader2);
            UCCyclone.close(fileReader2);
            return null;
        } catch (Throwable th5) {
            Throwable th6 = th5;
            fileReader = null;
            bufferedReader = null;
            th = th6;
            UCCyclone.close(bufferedReader);
            UCCyclone.close(fileReader);
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:109:0x03e8  */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x03ed  */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x03f2  */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x03f7  */
    /* JADX WARNING: Removed duplicated region for block: B:113:0x03fc  */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x0401  */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x0406  */
    /* JADX WARNING: Removed duplicated region for block: B:116:0x040b  */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x0410  */
    /* JADX WARNING: Removed duplicated region for block: B:118:0x0415  */
    /* JADX WARNING: Removed duplicated region for block: B:119:0x041a  */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x041f  */
    /* JADX WARNING: Removed duplicated region for block: B:121:0x0424  */
    /* JADX WARNING: Removed duplicated region for block: B:122:0x0429  */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x042e  */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x0433  */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x0438  */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x043d  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x0442  */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x0447  */
    /* JADX WARNING: Removed duplicated region for block: B:129:0x044c  */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x044f  */
    /* JADX WARNING: Removed duplicated region for block: B:131:0x0461  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x0469  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00e0  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0108  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0125  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0142  */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x015f  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x017c  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0199  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x01b6  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x01d3  */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x01f0  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x020d  */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x022a  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x0247  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0264  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x0281  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x029f  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x02bd  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x02db  */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x02f9  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x032d  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0330  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x0362  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x0365  */
    /* JADX WARNING: Removed duplicated region for block: B:93:0x0379  */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x03b4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.List<java.lang.String[]> a(java.util.List<android.content.pm.PackageInfo> r14) {
        /*
            r13 = this;
            r3 = 2
            r12 = 10003(0x2713, float:1.4017E-41)
            r2 = 1
            r4 = 0
            java.util.Iterator r1 = r14.iterator()
        L_0x0009:
            boolean r0 = r1.hasNext()
            if (r0 == 0) goto L_0x03cb
            java.lang.Object r0 = r1.next()
            android.content.pm.PackageInfo r0 = (android.content.pm.PackageInfo) r0
            java.lang.String r5 = r0.packageName
            java.lang.String r6 = "com.UCMobile"
            boolean r5 = r5.equals(r6)
            if (r5 == 0) goto L_0x03bd
            r1 = r2
        L_0x0021:
            java.util.ArrayList r5 = new java.util.ArrayList
            r5.<init>()
            java.lang.String[] r0 = new java.lang.String[r3]
            java.lang.String r6 = "lt"
            r0[r4] = r6
            java.lang.String r6 = "ev"
            r0[r2] = r6
            r5.add(r0)
            java.lang.String[] r0 = new java.lang.String[r3]
            java.lang.String r6 = "ct"
            r0[r4] = r6
            java.lang.String r6 = "corepv"
            r0[r2] = r6
            r5.add(r0)
            java.lang.String[] r0 = new java.lang.String[r3]
            java.lang.String r6 = "ver"
            r0[r4] = r6
            java.lang.String r6 = com.uc.webview.export.Build.Version.NAME
            r0[r2] = r6
            r5.add(r0)
            java.lang.String[] r0 = new java.lang.String[r3]
            java.lang.String r6 = "pkg"
            r0[r4] = r6
            android.content.Context r6 = r13.b
            java.lang.String r6 = r6.getPackageName()
            r0[r2] = r6
            r5.add(r0)
            java.lang.String[] r0 = new java.lang.String[r3]
            java.lang.String r6 = "sdk_sn"
            r0[r4] = r6
            java.lang.String r6 = com.uc.webview.export.Build.TIME
            r0[r2] = r6
            r5.add(r0)
            java.lang.String[] r6 = new java.lang.String[r3]
            java.lang.String r0 = "sdk_pm"
            r6[r4] = r0
            java.lang.String r0 = android.os.Build.MODEL
            if (r0 == 0) goto L_0x0087
            java.lang.String r0 = r0.trim()
            int r0 = r0.length()
            if (r0 != 0) goto L_0x03ce
        L_0x0087:
            r0 = r2
        L_0x0088:
            if (r0 == 0) goto L_0x03d1
            java.lang.String r0 = "unknown"
        L_0x008d:
            r6[r2] = r0
            r5.add(r6)
            java.lang.String[] r6 = new java.lang.String[r3]
            java.lang.String r0 = "sdk_f"
            r6[r4] = r0
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            r7.<init>()
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r8 = 524288(0x80000, double:2.590327E-318)
            java.lang.Long r8 = java.lang.Long.valueOf(r8)
            r0[r4] = r8
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 != 0) goto L_0x00c3
            r0 = 10036(0x2734, float:1.4063E-41)
            java.lang.Object[] r8 = new java.lang.Object[r2]
            android.content.Context r9 = r13.b
            r8[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r0, (java.lang.Object[]) r8)
            if (r0 != 0) goto L_0x03e3
        L_0x00c3:
            java.lang.String r0 = "0"
        L_0x00c6:
            java.lang.StringBuilder r8 = r7.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 1
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x03e8
            java.lang.String r0 = "1"
        L_0x00e3:
            java.lang.StringBuilder r8 = r8.append(r0)
            boolean r0 = com.uc.webview.export.internal.d.j
            if (r0 == 0) goto L_0x03ed
            java.lang.String r0 = "1"
        L_0x00ee:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 2
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x03f2
            java.lang.String r0 = "1"
        L_0x010b:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 4
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x03f7
            java.lang.String r0 = "1"
        L_0x0128:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 8
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x03fc
            java.lang.String r0 = "1"
        L_0x0145:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 16
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0401
            java.lang.String r0 = "1"
        L_0x0162:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 32
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0406
            java.lang.String r0 = "1"
        L_0x017f:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 64
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x040b
            java.lang.String r0 = "1"
        L_0x019c:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 128(0x80, double:6.32E-322)
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0410
            java.lang.String r0 = "1"
        L_0x01b9:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 256(0x100, double:1.265E-321)
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0415
            java.lang.String r0 = "1"
        L_0x01d6:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 512(0x200, double:2.53E-321)
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x041a
            java.lang.String r0 = "1"
        L_0x01f3:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 1024(0x400, double:5.06E-321)
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x041f
            java.lang.String r0 = "1"
        L_0x0210:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 2048(0x800, double:1.0118E-320)
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0424
            java.lang.String r0 = "1"
        L_0x022d:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 4096(0x1000, double:2.0237E-320)
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0429
            java.lang.String r0 = "1"
        L_0x024a:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 8192(0x2000, double:4.0474E-320)
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x042e
            java.lang.String r0 = "1"
        L_0x0267:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 16384(0x4000, double:8.0948E-320)
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0433
            java.lang.String r0 = "1"
        L_0x0284:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 32768(0x8000, double:1.61895E-319)
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0438
            java.lang.String r0 = "1"
        L_0x02a2:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 65536(0x10000, double:3.2379E-319)
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x043d
            java.lang.String r0 = "1"
        L_0x02c0:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 131072(0x20000, double:6.47582E-319)
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0442
            java.lang.String r0 = "1"
        L_0x02de:
            java.lang.StringBuilder r8 = r8.append(r0)
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r10 = 262144(0x40000, double:1.295163E-318)
            java.lang.Long r9 = java.lang.Long.valueOf(r10)
            r0[r4] = r9
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0447
            java.lang.String r0 = "1"
        L_0x02fc:
            r8.append(r0)
            java.lang.String r0 = r7.toString()
            r6[r2] = r0
            r5.add(r6)
            java.lang.String[] r0 = new java.lang.String[r3]
            java.lang.String r6 = "sdk_uf"
            r0[r4] = r6
            java.lang.String r6 = java.lang.String.valueOf(r1)
            r0[r2] = r6
            r5.add(r0)
            java.lang.String[] r6 = new java.lang.String[r3]
            java.lang.String r0 = "sdk_bd"
            r6[r4] = r0
            java.lang.String r0 = android.os.Build.BRAND
            if (r0 == 0) goto L_0x032d
            java.lang.String r0 = r0.trim()
            int r0 = r0.length()
            if (r0 != 0) goto L_0x044c
        L_0x032d:
            r0 = r2
        L_0x032e:
            if (r0 == 0) goto L_0x044f
            java.lang.String r0 = "unknown"
        L_0x0333:
            r6[r2] = r0
            r5.add(r6)
            java.lang.String[] r0 = new java.lang.String[r3]
            java.lang.String r6 = "sdk_osv"
            r0[r4] = r6
            java.lang.String r6 = android.os.Build.VERSION.RELEASE
            r0[r2] = r6
            r5.add(r0)
            java.lang.String[] r0 = new java.lang.String[r3]
            java.lang.String r6 = "sdk_prd"
            r0[r4] = r6
            java.lang.String r6 = com.uc.webview.export.Build.SDK_PRD
            r0[r2] = r6
            r5.add(r0)
            java.lang.String r0 = com.uc.webview.export.Build.CORE_VERSION
            if (r0 == 0) goto L_0x0362
            java.lang.String r0 = r0.trim()
            int r0 = r0.length()
            if (r0 != 0) goto L_0x0461
        L_0x0362:
            r0 = r2
        L_0x0363:
            if (r0 != 0) goto L_0x0377
            java.lang.String[] r0 = new java.lang.String[r3]
            java.lang.String r6 = "sdk_sdk_cv"
            r0[r4] = r6
            java.lang.String r6 = com.uc.webview.export.Build.CORE_VERSION
            java.lang.String r6 = r6.trim()
            r0[r2] = r6
            r5.add(r0)
        L_0x0377:
            if (r1 != 0) goto L_0x0396
            java.lang.String[] r1 = new java.lang.String[r3]
            java.lang.String r0 = "sdk_ucbackup"
            r1[r4] = r0
            java.io.File r0 = new java.io.File
            java.lang.String r6 = "/sdcard/Backucup/com.UCMobile"
            r0.<init>(r6)
            boolean r0 = r0.exists()
            if (r0 == 0) goto L_0x0464
            java.lang.String r0 = "1"
        L_0x0391:
            r1[r2] = r0
            r5.add(r1)
        L_0x0396:
            java.lang.String[] r1 = new java.lang.String[r3]
            java.lang.String r0 = "sdk_vac"
            r1[r4] = r0
            java.lang.Object[] r0 = new java.lang.Object[r2]
            r6 = 1048576(0x100000, double:5.180654E-318)
            java.lang.Long r3 = java.lang.Long.valueOf(r6)
            r0[r4] = r3
            java.lang.Object r0 = com.uc.webview.export.internal.d.a((int) r12, (java.lang.Object[]) r0)
            java.lang.Boolean r0 = (java.lang.Boolean) r0
            boolean r0 = r0.booleanValue()
            if (r0 == 0) goto L_0x0469
            java.lang.String r0 = "1"
        L_0x03b7:
            r1[r2] = r0
            r5.add(r1)
            return r5
        L_0x03bd:
            java.lang.String r0 = r0.packageName
            java.lang.String r5 = "com.UCMobile.intl"
            boolean r0 = r0.equals(r5)
            if (r0 == 0) goto L_0x0009
            r1 = r3
            goto L_0x0021
        L_0x03cb:
            r1 = r4
            goto L_0x0021
        L_0x03ce:
            r0 = r4
            goto L_0x0088
        L_0x03d1:
            java.lang.String r0 = android.os.Build.MODEL
            java.lang.String r0 = r0.trim()
            java.lang.String r7 = "[`|=]"
            java.lang.String r8 = ""
            java.lang.String r0 = r0.replaceAll(r7, r8)
            goto L_0x008d
        L_0x03e3:
            java.lang.String r0 = "1"
            goto L_0x00c6
        L_0x03e8:
            java.lang.String r0 = "0"
            goto L_0x00e3
        L_0x03ed:
            java.lang.String r0 = "0"
            goto L_0x00ee
        L_0x03f2:
            java.lang.String r0 = "0"
            goto L_0x010b
        L_0x03f7:
            java.lang.String r0 = "0"
            goto L_0x0128
        L_0x03fc:
            java.lang.String r0 = "0"
            goto L_0x0145
        L_0x0401:
            java.lang.String r0 = "0"
            goto L_0x0162
        L_0x0406:
            java.lang.String r0 = "0"
            goto L_0x017f
        L_0x040b:
            java.lang.String r0 = "0"
            goto L_0x019c
        L_0x0410:
            java.lang.String r0 = "0"
            goto L_0x01b9
        L_0x0415:
            java.lang.String r0 = "0"
            goto L_0x01d6
        L_0x041a:
            java.lang.String r0 = "0"
            goto L_0x01f3
        L_0x041f:
            java.lang.String r0 = "0"
            goto L_0x0210
        L_0x0424:
            java.lang.String r0 = "0"
            goto L_0x022d
        L_0x0429:
            java.lang.String r0 = "0"
            goto L_0x024a
        L_0x042e:
            java.lang.String r0 = "0"
            goto L_0x0267
        L_0x0433:
            java.lang.String r0 = "0"
            goto L_0x0284
        L_0x0438:
            java.lang.String r0 = "0"
            goto L_0x02a2
        L_0x043d:
            java.lang.String r0 = "0"
            goto L_0x02c0
        L_0x0442:
            java.lang.String r0 = "0"
            goto L_0x02de
        L_0x0447:
            java.lang.String r0 = "0"
            goto L_0x02fc
        L_0x044c:
            r0 = r4
            goto L_0x032e
        L_0x044f:
            java.lang.String r0 = android.os.Build.BRAND
            java.lang.String r0 = r0.trim()
            java.lang.String r7 = "[`|=]"
            java.lang.String r8 = ""
            java.lang.String r0 = r0.replaceAll(r7, r8)
            goto L_0x0333
        L_0x0461:
            r0 = r4
            goto L_0x0363
        L_0x0464:
            java.lang.String r0 = "0"
            goto L_0x0391
        L_0x0469:
            java.lang.String r0 = "0"
            goto L_0x03b7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.c.a.a.a(java.util.List):java.util.List");
    }

    private static String b(Map<String, C0009a> map, List<b> list) {
        String str;
        String str2;
        String str3 = null;
        for (Map.Entry<String, C0009a> value : map.entrySet()) {
            String str4 = ((C0009a) value.getValue()).b.get("tm");
            if (str4 == null || str4.length() <= 10) {
                str2 = null;
            } else {
                str2 = str4.substring(0, 10);
            }
            if (str2 == null || !str2.endsWith("01") || (str3 != null && str2.compareTo(str3) <= 0)) {
                str2 = str3;
            }
            str3 = str2;
        }
        for (b bVar : list) {
            String str5 = bVar.b.get("tm");
            if (str5 == null || str5.length() <= 10) {
                str = null;
            } else {
                str = str5.substring(0, 10);
            }
            if (str != null && str.endsWith("01")) {
                if (str3 == null || str.compareTo(str3) > 0) {
                    str3 = str;
                }
            }
        }
        return str3;
    }

    /* access modifiers changed from: package-private */
    public final String a(String str, String[] strArr) {
        ArrayList<String[]> arrayList;
        Object[] b2 = b();
        if (b2 == null) {
            return null;
        }
        Map map = (Map) b2[0];
        List<b> list = (List) b2[1];
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("uuid", str);
            List<PackageInfo> installedPackages = this.b.getPackageManager().getInstalledPackages(0);
            for (String[] next : a(installedPackages)) {
                jSONObject.put(next[0], next[1]);
            }
            strArr[0] = b(map, list);
            String str2 = strArr[0];
            if (str2 == null) {
                arrayList = new ArrayList<>(0);
            } else {
                arrayList = new ArrayList<>();
                String string = this.b.getSharedPreferences("UC_WA_STAT", 0).getString("4", (String) null);
                if (string == null || !string.equals(str2)) {
                    arrayList.add(new String[]{"sdk_3rdappf", b(installedPackages)});
                }
            }
            for (String[] strArr2 : arrayList) {
                jSONObject.put(strArr2[0], strArr2[1]);
            }
            for (Map.Entry next2 : d.v.entrySet()) {
                jSONObject.put((String) next2.getKey(), ((Integer) next2.getValue()).intValue());
            }
            JSONArray jSONArray = new JSONArray();
            for (Map.Entry entry : map.entrySet()) {
                JSONObject jSONObject2 = new JSONObject();
                for (Map.Entry next3 : ((C0009a) entry.getValue()).a.entrySet()) {
                    jSONObject2.put((String) next3.getKey(), String.valueOf(next3.getValue()));
                }
                for (Map.Entry next4 : ((C0009a) entry.getValue()).b.entrySet()) {
                    jSONObject2.put((String) next4.getKey(), next4.getValue());
                }
                jSONArray.put(jSONObject2);
            }
            for (b bVar : list) {
                JSONObject jSONObject3 = new JSONObject();
                for (Map.Entry next5 : bVar.b.entrySet()) {
                    jSONObject3.put((String) next5.getKey(), next5.getValue());
                }
                jSONArray.put(jSONObject3);
            }
            jSONObject.put("items", jSONArray);
            return jSONObject.toString();
        } catch (Exception e2) {
            Log.e("SDKWaStat", "getJsonUploadData", e2);
            return null;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v11, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v5, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v6, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v7, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: java.io.OutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v8, resolved type: java.io.OutputStream} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x008a A[Catch:{ all -> 0x00f6 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean a(java.lang.String r9, byte[] r10) {
        /*
            r2 = 1
            r1 = 0
            r3 = 0
            java.net.URL r0 = new java.net.URL     // Catch:{ Throwable -> 0x00fb, all -> 0x00df }
            r0.<init>(r9)     // Catch:{ Throwable -> 0x00fb, all -> 0x00df }
            java.net.URLConnection r0 = r0.openConnection()     // Catch:{ Throwable -> 0x00fb, all -> 0x00df }
            java.net.HttpURLConnection r0 = (java.net.HttpURLConnection) r0     // Catch:{ Throwable -> 0x00fb, all -> 0x00df }
            r4 = 5000(0x1388, float:7.006E-42)
            r0.setConnectTimeout(r4)     // Catch:{ Throwable -> 0x00fb, all -> 0x00df }
            r4 = 1
            r0.setDoInput(r4)     // Catch:{ Throwable -> 0x00fb, all -> 0x00df }
            r4 = 1
            r0.setDoOutput(r4)     // Catch:{ Throwable -> 0x00fb, all -> 0x00df }
            java.lang.String r4 = "POST"
            r0.setRequestMethod(r4)     // Catch:{ Throwable -> 0x00fb, all -> 0x00df }
            r4 = 0
            r0.setUseCaches(r4)     // Catch:{ Throwable -> 0x00fb, all -> 0x00df }
            java.lang.String r4 = "Content-Type"
            java.lang.String r5 = "application/x-www-form-urlencoded"
            r0.setRequestProperty(r4, r5)     // Catch:{ Throwable -> 0x00fb, all -> 0x00df }
            java.lang.String r4 = "Content-Length"
            int r5 = r10.length     // Catch:{ Throwable -> 0x00fb, all -> 0x00df }
            java.lang.String r5 = java.lang.String.valueOf(r5)     // Catch:{ Throwable -> 0x00fb, all -> 0x00df }
            r0.setRequestProperty(r4, r5)     // Catch:{ Throwable -> 0x00fb, all -> 0x00df }
            java.io.OutputStream r5 = r0.getOutputStream()     // Catch:{ Throwable -> 0x00fb, all -> 0x00df }
            r5.write(r10)     // Catch:{ Throwable -> 0x00ff, all -> 0x00ec }
            int r4 = r0.getResponseCode()     // Catch:{ Throwable -> 0x00ff, all -> 0x00ec }
            r6 = 200(0xc8, float:2.8E-43)
            if (r4 == r6) goto L_0x0065
            boolean r0 = com.uc.webview.export.utility.Utils.sWAPrintLog     // Catch:{ Throwable -> 0x00ff, all -> 0x00ec }
            if (r0 == 0) goto L_0x005a
            java.lang.String r0 = "SDKWaStat"
            java.lang.String r2 = "response == null"
            java.lang.Throwable r4 = new java.lang.Throwable     // Catch:{ Throwable -> 0x00ff, all -> 0x00ec }
            r4.<init>()     // Catch:{ Throwable -> 0x00ff, all -> 0x00ec }
            com.uc.webview.export.internal.utility.Log.e(r0, r2, r4)     // Catch:{ Throwable -> 0x00ff, all -> 0x00ec }
        L_0x005a:
            com.uc.webview.export.cyclone.UCCyclone.close(r5)
            com.uc.webview.export.cyclone.UCCyclone.close(r3)
            com.uc.webview.export.cyclone.UCCyclone.close(r3)
            r0 = r1
        L_0x0064:
            return r0
        L_0x0065:
            java.io.InputStream r4 = r0.getInputStream()     // Catch:{ Throwable -> 0x00ff, all -> 0x00ec }
            r0 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r0]     // Catch:{ Throwable -> 0x0104, all -> 0x00ef }
            java.io.ByteArrayOutputStream r6 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x0104, all -> 0x00ef }
            int r7 = r4.available()     // Catch:{ Throwable -> 0x0104, all -> 0x00ef }
            r6.<init>(r7)     // Catch:{ Throwable -> 0x0104, all -> 0x00ef }
        L_0x0076:
            int r3 = r4.read(r0)     // Catch:{ Throwable -> 0x0082, all -> 0x00f3 }
            r7 = -1
            if (r3 == r7) goto L_0x009e
            r7 = 0
            r6.write(r0, r7, r3)     // Catch:{ Throwable -> 0x0082, all -> 0x00f3 }
            goto L_0x0076
        L_0x0082:
            r0 = move-exception
            r2 = r4
            r3 = r5
            r4 = r6
        L_0x0086:
            boolean r5 = com.uc.webview.export.utility.Utils.sWAPrintLog     // Catch:{ all -> 0x00f6 }
            if (r5 == 0) goto L_0x0093
            java.lang.String r5 = "SDKWaStat"
            java.lang.String r6 = ""
            com.uc.webview.export.internal.utility.Log.e(r5, r6, r0)     // Catch:{ all -> 0x00f6 }
        L_0x0093:
            com.uc.webview.export.cyclone.UCCyclone.close(r3)
            com.uc.webview.export.cyclone.UCCyclone.close(r2)
            com.uc.webview.export.cyclone.UCCyclone.close(r4)
        L_0x009c:
            r0 = r1
            goto L_0x0064
        L_0x009e:
            java.lang.String r0 = new java.lang.String     // Catch:{ Throwable -> 0x0082, all -> 0x00f3 }
            byte[] r3 = r6.toByteArray()     // Catch:{ Throwable -> 0x0082, all -> 0x00f3 }
            r0.<init>(r3)     // Catch:{ Throwable -> 0x0082, all -> 0x00f3 }
            boolean r3 = com.uc.webview.export.utility.Utils.sWAPrintLog     // Catch:{ Throwable -> 0x0082, all -> 0x00f3 }
            if (r3 == 0) goto L_0x00c1
            java.lang.String r3 = "SDKWaStat"
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0082, all -> 0x00f3 }
            java.lang.String r8 = "response:"
            r7.<init>(r8)     // Catch:{ Throwable -> 0x0082, all -> 0x00f3 }
            java.lang.StringBuilder r7 = r7.append(r0)     // Catch:{ Throwable -> 0x0082, all -> 0x00f3 }
            java.lang.String r7 = r7.toString()     // Catch:{ Throwable -> 0x0082, all -> 0x00f3 }
            com.uc.webview.export.internal.utility.Log.i(r3, r7)     // Catch:{ Throwable -> 0x0082, all -> 0x00f3 }
        L_0x00c1:
            java.lang.String r3 = "retcode=0"
            boolean r0 = r0.contains(r3)     // Catch:{ Throwable -> 0x0082, all -> 0x00f3 }
            if (r0 == 0) goto L_0x00d5
            com.uc.webview.export.cyclone.UCCyclone.close(r5)
            com.uc.webview.export.cyclone.UCCyclone.close(r4)
            com.uc.webview.export.cyclone.UCCyclone.close(r6)
            r0 = r2
            goto L_0x0064
        L_0x00d5:
            com.uc.webview.export.cyclone.UCCyclone.close(r5)
            com.uc.webview.export.cyclone.UCCyclone.close(r4)
            com.uc.webview.export.cyclone.UCCyclone.close(r6)
            goto L_0x009c
        L_0x00df:
            r0 = move-exception
            r5 = r3
            r6 = r3
        L_0x00e2:
            com.uc.webview.export.cyclone.UCCyclone.close(r5)
            com.uc.webview.export.cyclone.UCCyclone.close(r3)
            com.uc.webview.export.cyclone.UCCyclone.close(r6)
            throw r0
        L_0x00ec:
            r0 = move-exception
            r6 = r3
            goto L_0x00e2
        L_0x00ef:
            r0 = move-exception
            r6 = r3
            r3 = r4
            goto L_0x00e2
        L_0x00f3:
            r0 = move-exception
            r3 = r4
            goto L_0x00e2
        L_0x00f6:
            r0 = move-exception
            r5 = r3
            r6 = r4
            r3 = r2
            goto L_0x00e2
        L_0x00fb:
            r0 = move-exception
            r2 = r3
            r4 = r3
            goto L_0x0086
        L_0x00ff:
            r0 = move-exception
            r2 = r3
            r4 = r3
            r3 = r5
            goto L_0x0086
        L_0x0104:
            r0 = move-exception
            r2 = r4
            r4 = r3
            r3 = r5
            goto L_0x0086
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.c.a.a.a(java.lang.String, byte[]):boolean");
    }

    private static String b(List<PackageInfo> list) {
        long currentTimeMillis = System.currentTimeMillis();
        HashSet hashSet = new HashSet(list.size());
        for (PackageInfo packageInfo : list) {
            hashSet.add(Integer.valueOf(packageInfo.packageName.hashCode()));
        }
        StringBuilder sb = new StringBuilder();
        int[] iArr = {744792033, -796004189, 1536737232, -1864872766, -245593387, 559984781, 1254578009, 460049591, -103524201, -191341086, 2075805265, -860300598, 195266379, 851655498, -172581751, -1692253156, -1709882794, 978047406, -1447376190, 1085732649, 400412247, 1007750384, 321803898, 1319538838, -1459422248, -173313837, 1488133239, 551552610, 1310504938, 633261597, -548160304, 1971200218, 757982267, 996952171, 1855462465, 2049668591, -189253699, -761937585};
        for (int i2 = 0; i2 < 38; i2++) {
            if (hashSet.contains(Integer.valueOf(iArr[i2]))) {
                sb.append("1");
            } else {
                sb.append("0");
            }
        }
        Log.i("SDKWaStat", "getOtherAppInstallFlag用时:" + (System.currentTimeMillis() - currentTimeMillis) + " " + sb);
        return sb.toString();
    }
}
