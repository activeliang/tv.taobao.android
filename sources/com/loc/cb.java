package com.loc;

import java.util.ArrayList;
import java.util.List;

/* compiled from: CellCollector */
public final class cb {
    private da a;
    private da b;
    private dg c;
    private a d = new a();
    private final List<da> e = new ArrayList(3);

    /* compiled from: CellCollector */
    public static class a {
        public byte a;
        public String b;
        public da c;
        public da d;
        public da e;
        public List<da> f = new ArrayList();
        public List<da> g = new ArrayList();

        public static boolean a(da daVar, da daVar2) {
            if (daVar == null || daVar2 == null) {
                return (daVar == null) == (daVar2 == null);
            } else if ((daVar instanceof dc) && (daVar2 instanceof dc)) {
                dc dcVar = (dc) daVar;
                dc dcVar2 = (dc) daVar2;
                return dcVar.j == dcVar2.j && dcVar.k == dcVar2.k;
            } else if ((daVar instanceof db) && (daVar2 instanceof db)) {
                db dbVar = (db) daVar;
                db dbVar2 = (db) daVar2;
                return dbVar.l == dbVar2.l && dbVar.k == dbVar2.k && dbVar.j == dbVar2.j;
            } else if ((daVar instanceof dd) && (daVar2 instanceof dd)) {
                dd ddVar = (dd) daVar;
                dd ddVar2 = (dd) daVar2;
                return ddVar.j == ddVar2.j && ddVar.k == ddVar2.k;
            } else if (!(daVar instanceof de) || !(daVar2 instanceof de)) {
                return false;
            } else {
                de deVar = (de) daVar;
                de deVar2 = (de) daVar2;
                return deVar.j == deVar2.j && deVar.k == deVar2.k;
            }
        }

        public final void a() {
            this.a = 0;
            this.b = "";
            this.c = null;
            this.d = null;
            this.e = null;
            this.f.clear();
            this.g.clear();
        }

        public final String toString() {
            return "CellInfo{radio=" + this.a + ", operator='" + this.b + '\'' + ", mainCell=" + this.c + ", mainOldInterCell=" + this.d + ", mainNewInterCell=" + this.e + ", cells=" + this.f + ", historyMainCellList=" + this.g + '}';
        }
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x0091  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0160 A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.loc.cb.a a(com.loc.dg r15, boolean r16, byte r17, java.lang.String r18, java.util.List<com.loc.da> r19) {
        /*
            r14 = this;
            if (r16 == 0) goto L_0x0009
            com.loc.cb$a r2 = r14.d
            r2.a()
            r2 = 0
        L_0x0008:
            return r2
        L_0x0009:
            com.loc.cb$a r3 = r14.d
            r3.a()
            r0 = r17
            r3.a = r0
            r0 = r18
            r3.b = r0
            if (r19 == 0) goto L_0x0047
            java.util.List<com.loc.da> r2 = r3.f
            r0 = r19
            r2.addAll(r0)
            java.util.List<com.loc.da> r2 = r3.f
            java.util.Iterator r4 = r2.iterator()
        L_0x0025:
            boolean r2 = r4.hasNext()
            if (r2 == 0) goto L_0x0047
            java.lang.Object r2 = r4.next()
            com.loc.da r2 = (com.loc.da) r2
            boolean r5 = r2.i
            if (r5 != 0) goto L_0x003c
            boolean r5 = r2.h
            if (r5 == 0) goto L_0x003c
            r3.d = r2
            goto L_0x0025
        L_0x003c:
            boolean r5 = r2.i
            if (r5 == 0) goto L_0x0025
            boolean r5 = r2.h
            if (r5 == 0) goto L_0x0025
            r3.e = r2
            goto L_0x0025
        L_0x0047:
            com.loc.da r2 = r3.d
            if (r2 != 0) goto L_0x0057
            com.loc.da r2 = r3.e
        L_0x004d:
            r3.c = r2
            com.loc.cb$a r2 = r14.d
            com.loc.da r2 = r2.c
            if (r2 != 0) goto L_0x005a
            r2 = 0
            goto L_0x0008
        L_0x0057:
            com.loc.da r2 = r3.d
            goto L_0x004d
        L_0x005a:
            com.loc.dg r2 = r14.c
            if (r2 == 0) goto L_0x008e
            float r2 = r15.g
            r3 = 1092616192(0x41200000, float:10.0)
            int r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r2 <= 0) goto L_0x00de
            r2 = 1157234688(0x44fa0000, float:2000.0)
        L_0x0068:
            com.loc.dg r3 = r14.c
            double r4 = r15.a(r3)
            double r2 = (double) r2
            int r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r2 <= 0) goto L_0x00ed
            r2 = 1
        L_0x0074:
            if (r2 != 0) goto L_0x008e
            com.loc.cb$a r2 = r14.d
            com.loc.da r2 = r2.d
            com.loc.da r3 = r14.a
            boolean r2 = com.loc.cb.a.a(r2, r3)
            if (r2 == 0) goto L_0x008e
            com.loc.cb$a r2 = r14.d
            com.loc.da r2 = r2.e
            com.loc.da r3 = r14.b
            boolean r2 = com.loc.cb.a.a(r2, r3)
            if (r2 != 0) goto L_0x00ef
        L_0x008e:
            r2 = 1
        L_0x008f:
            if (r2 == 0) goto L_0x0160
            com.loc.cb$a r2 = r14.d
            com.loc.da r2 = r2.d
            r14.a = r2
            com.loc.cb$a r2 = r14.d
            com.loc.da r2 = r2.e
            r14.b = r2
            r14.c = r15
            com.loc.cb$a r2 = r14.d
            java.util.List<com.loc.da> r2 = r2.f
            com.loc.cx.a((java.util.List<com.loc.da>) r2)
            com.loc.cb$a r2 = r14.d
            java.util.List<com.loc.da> r8 = r14.e
            monitor-enter(r8)
            java.util.List<com.loc.da> r2 = r2.f     // Catch:{ all -> 0x00db }
            java.util.Iterator r9 = r2.iterator()     // Catch:{ all -> 0x00db }
        L_0x00b1:
            boolean r2 = r9.hasNext()     // Catch:{ all -> 0x00db }
            if (r2 == 0) goto L_0x014b
            java.lang.Object r2 = r9.next()     // Catch:{ all -> 0x00db }
            com.loc.da r2 = (com.loc.da) r2     // Catch:{ all -> 0x00db }
            if (r2 == 0) goto L_0x00b1
            boolean r3 = r2.h     // Catch:{ all -> 0x00db }
            if (r3 == 0) goto L_0x00b1
            com.loc.da r10 = r2.clone()     // Catch:{ all -> 0x00db }
            long r2 = android.os.SystemClock.elapsedRealtime()     // Catch:{ all -> 0x00db }
            r10.e = r2     // Catch:{ all -> 0x00db }
            java.util.List<com.loc.da> r2 = r14.e     // Catch:{ all -> 0x00db }
            int r11 = r2.size()     // Catch:{ all -> 0x00db }
            if (r11 != 0) goto L_0x00f1
            java.util.List<com.loc.da> r2 = r14.e     // Catch:{ all -> 0x00db }
            r2.add(r10)     // Catch:{ all -> 0x00db }
            goto L_0x00b1
        L_0x00db:
            r2 = move-exception
            monitor-exit(r8)     // Catch:{ all -> 0x00db }
            throw r2
        L_0x00de:
            float r2 = r15.g
            r3 = 1073741824(0x40000000, float:2.0)
            int r2 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            if (r2 <= 0) goto L_0x00e9
            r2 = 1140457472(0x43fa0000, float:500.0)
            goto L_0x0068
        L_0x00e9:
            r2 = 1120403456(0x42c80000, float:100.0)
            goto L_0x0068
        L_0x00ed:
            r2 = 0
            goto L_0x0074
        L_0x00ef:
            r2 = 0
            goto L_0x008f
        L_0x00f1:
            r5 = -1
            r2 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r4 = 0
            r6 = r2
            r3 = r4
            r4 = r5
        L_0x00fb:
            if (r3 >= r11) goto L_0x011b
            java.util.List<com.loc.da> r2 = r14.e     // Catch:{ all -> 0x00db }
            java.lang.Object r2 = r2.get(r3)     // Catch:{ all -> 0x00db }
            com.loc.da r2 = (com.loc.da) r2     // Catch:{ all -> 0x00db }
            boolean r5 = r10.equals(r2)     // Catch:{ all -> 0x00db }
            if (r5 == 0) goto L_0x0126
            r4 = -1
            int r3 = r10.c     // Catch:{ all -> 0x00db }
            int r5 = r2.c     // Catch:{ all -> 0x00db }
            if (r3 == r5) goto L_0x011b
            int r3 = r10.c     // Catch:{ all -> 0x00db }
            long r12 = (long) r3     // Catch:{ all -> 0x00db }
            r2.e = r12     // Catch:{ all -> 0x00db }
            int r3 = r10.c     // Catch:{ all -> 0x00db }
            r2.c = r3     // Catch:{ all -> 0x00db }
        L_0x011b:
            if (r4 < 0) goto L_0x00b1
            r2 = 3
            if (r11 >= r2) goto L_0x0137
            java.util.List<com.loc.da> r2 = r14.e     // Catch:{ all -> 0x00db }
            r2.add(r10)     // Catch:{ all -> 0x00db }
            goto L_0x00b1
        L_0x0126:
            long r12 = r2.e     // Catch:{ all -> 0x00db }
            long r6 = java.lang.Math.min(r6, r12)     // Catch:{ all -> 0x00db }
            long r12 = r2.e     // Catch:{ all -> 0x00db }
            int r2 = (r6 > r12 ? 1 : (r6 == r12 ? 0 : -1))
            if (r2 != 0) goto L_0x0163
            r2 = r3
        L_0x0133:
            int r3 = r3 + 1
            r4 = r2
            goto L_0x00fb
        L_0x0137:
            long r2 = r10.e     // Catch:{ all -> 0x00db }
            int r2 = (r2 > r6 ? 1 : (r2 == r6 ? 0 : -1))
            if (r2 <= 0) goto L_0x00b1
            if (r4 >= r11) goto L_0x00b1
            java.util.List<com.loc.da> r2 = r14.e     // Catch:{ all -> 0x00db }
            r2.remove(r4)     // Catch:{ all -> 0x00db }
            java.util.List<com.loc.da> r2 = r14.e     // Catch:{ all -> 0x00db }
            r2.add(r10)     // Catch:{ all -> 0x00db }
            goto L_0x00b1
        L_0x014b:
            com.loc.cb$a r2 = r14.d     // Catch:{ all -> 0x00db }
            java.util.List<com.loc.da> r2 = r2.g     // Catch:{ all -> 0x00db }
            r2.clear()     // Catch:{ all -> 0x00db }
            com.loc.cb$a r2 = r14.d     // Catch:{ all -> 0x00db }
            java.util.List<com.loc.da> r2 = r2.g     // Catch:{ all -> 0x00db }
            java.util.List<com.loc.da> r3 = r14.e     // Catch:{ all -> 0x00db }
            r2.addAll(r3)     // Catch:{ all -> 0x00db }
            monitor-exit(r8)     // Catch:{ all -> 0x00db }
            com.loc.cb$a r2 = r14.d
            goto L_0x0008
        L_0x0160:
            r2 = 0
            goto L_0x0008
        L_0x0163:
            r2 = r4
            goto L_0x0133
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.cb.a(com.loc.dg, boolean, byte, java.lang.String, java.util.List):com.loc.cb$a");
    }
}
