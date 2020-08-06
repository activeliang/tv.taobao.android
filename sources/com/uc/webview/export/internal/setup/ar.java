package com.uc.webview.export.internal.setup;

import android.os.Handler;
import android.os.Looper;

/* compiled from: ProGuard */
final class ar extends Handler {
    final /* synthetic */ aq a;
    private UCAsyncTask b = null;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ar(aq aqVar, Looper looper) {
        super(looper);
        this.a = aqVar;
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
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverseMonitorExits(RegionMaker.java:619)
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
        	at jadx.core.dex.visitors.regions.RegionMaker.processExcHandler(RegionMaker.java:1043)
        	at jadx.core.dex.visitors.regions.RegionMaker.processTryCatchBlocks(RegionMaker.java:975)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
        */
    public final void dispatchMessage(android.os.Message r12) {
        /*
            r11 = this;
            r10 = 1120403456(0x42c80000, float:100.0)
            r1 = 0
            r3 = 0
            java.lang.Boolean r0 = com.uc.webview.export.internal.setup.UCAsyncTask.m     // Catch:{ Throwable -> 0x0162 }
            boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x0162 }
            if (r0 == 0) goto L_0x0230
            com.uc.webview.export.cyclone.UCElapseTime r0 = new com.uc.webview.export.cyclone.UCElapseTime     // Catch:{ Throwable -> 0x0162 }
            r0.<init>()     // Catch:{ Throwable -> 0x0162 }
        L_0x0013:
            r4 = r0
        L_0x0014:
            java.lang.Runnable r0 = r12.getCallback()     // Catch:{ Throwable -> 0x0169 }
            boolean r2 = r0 instanceof com.uc.webview.export.internal.setup.UCAsyncTask     // Catch:{ Throwable -> 0x0169 }
            if (r2 == 0) goto L_0x0042
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = (com.uc.webview.export.internal.setup.UCAsyncTask) r0     // Catch:{ Throwable -> 0x0169 }
            r11.b = r0     // Catch:{ Throwable -> 0x0169 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ Throwable -> 0x0169 }
            long r6 = r0.k     // Catch:{ Throwable -> 0x0169 }
            r8 = 0
            int r0 = (r6 > r8 ? 1 : (r6 == r8 ? 0 : -1))
            if (r0 <= 0) goto L_0x003a
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ Throwable -> 0x022d }
            long r6 = r0.k     // Catch:{ Throwable -> 0x022d }
            java.lang.Thread.sleep(r6)     // Catch:{ Throwable -> 0x022d }
        L_0x0035:
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ Throwable -> 0x0169 }
            long unused = r0.k = 0     // Catch:{ Throwable -> 0x0169 }
        L_0x003a:
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ Throwable -> 0x0169 }
            java.lang.String r2 = "start"
            r0.callback(r2)     // Catch:{ Throwable -> 0x0169 }
        L_0x0042:
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ Throwable -> 0x0169 }
            com.uc.webview.export.internal.setup.ap r2 = r0.g     // Catch:{ Throwable -> 0x0169 }
            monitor-enter(r2)     // Catch:{ Throwable -> 0x0169 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ all -> 0x0166 }
            boolean r0 = r0.f     // Catch:{ all -> 0x0166 }
            if (r0 == 0) goto L_0x0052
            r12 = r1
        L_0x0052:
            monitor-exit(r2)     // Catch:{ all -> 0x0166 }
            if (r12 == 0) goto L_0x008e
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ Throwable -> 0x0169 }
            com.uc.webview.export.internal.setup.aq r2 = r11.a     // Catch:{ Throwable -> 0x0169 }
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = r2.a     // Catch:{ Throwable -> 0x0169 }
            com.uc.webview.export.internal.setup.ap r2 = r2.g     // Catch:{ Throwable -> 0x0169 }
            monitor-enter(r2)     // Catch:{ Throwable -> 0x0169 }
            com.uc.webview.export.internal.setup.aq r5 = r11.a     // Catch:{ all -> 0x018e }
            com.uc.webview.export.internal.setup.UCAsyncTask r5 = r5.a     // Catch:{ all -> 0x018e }
            boolean r5 = r5.e     // Catch:{ all -> 0x018e }
            if (r5 == 0) goto L_0x008d
            com.uc.webview.export.internal.setup.aq r5 = r11.a     // Catch:{ all -> 0x018e }
            com.uc.webview.export.internal.setup.UCAsyncTask r5 = r5.a     // Catch:{ all -> 0x018e }
            boolean unused = r5.e = false     // Catch:{ all -> 0x018e }
            java.lang.String r5 = "pause"
            r0.callback(r5)     // Catch:{ all -> 0x018e }
            com.uc.webview.export.internal.setup.aq r5 = r11.a     // Catch:{ all -> 0x018e }
            com.uc.webview.export.internal.setup.UCAsyncTask r5 = r5.a     // Catch:{ all -> 0x018e }
            com.uc.webview.export.internal.setup.ap r5 = r5.g     // Catch:{ all -> 0x018e }
            r6 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
            r5.a(r6)     // Catch:{ all -> 0x018e }
            java.lang.String r5 = "resume"
            r0.callback(r5)     // Catch:{ all -> 0x018e }
        L_0x008d:
            monitor-exit(r2)     // Catch:{ all -> 0x018e }
        L_0x008e:
            if (r12 == 0) goto L_0x00c5
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ Throwable -> 0x0169 }
            com.uc.webview.export.internal.setup.UCSetupException r0 = r0.mException     // Catch:{ Throwable -> 0x0169 }
            if (r0 != 0) goto L_0x00c5
            super.dispatchMessage(r12)     // Catch:{ Throwable -> 0x0169 }
            com.uc.webview.export.internal.setup.UCAsyncTask r2 = r11.b     // Catch:{ Throwable -> 0x0169 }
            if (r2 == 0) goto L_0x00c5
            java.lang.Integer r5 = r2.d     // Catch:{ Throwable -> 0x0169 }
            monitor-enter(r5)     // Catch:{ Throwable -> 0x0169 }
            int r0 = com.uc.webview.export.internal.setup.UCAsyncTask.e(r2)     // Catch:{ all -> 0x0191 }
            float r0 = (float) r0     // Catch:{ all -> 0x0191 }
            float r6 = r0 * r10
            r0 = 10014(0x271e, float:1.4033E-41)
            r7 = 0
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch:{ all -> 0x0191 }
            java.lang.Object r0 = r2.invokeO(r0, r7)     // Catch:{ all -> 0x0191 }
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ all -> 0x0191 }
            int r0 = r0.intValue()     // Catch:{ all -> 0x0191 }
            float r0 = (float) r0     // Catch:{ all -> 0x0191 }
            float r0 = r6 / r0
            int r0 = (int) r0     // Catch:{ all -> 0x0191 }
            r2.mPercent = r0     // Catch:{ all -> 0x0191 }
            monitor-exit(r5)     // Catch:{ all -> 0x0191 }
            java.lang.String r0 = "progress"
            r2.callback(r0)     // Catch:{ Throwable -> 0x0169 }
        L_0x00c5:
            java.lang.Boolean r0 = com.uc.webview.export.internal.setup.UCAsyncTask.m     // Catch:{ Throwable -> 0x022a }
            boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x022a }
            if (r0 == 0) goto L_0x00d1
            com.uc.webview.export.internal.setup.UCAsyncTask r1 = r11.b     // Catch:{ Throwable -> 0x022a }
        L_0x00d1:
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b
            java.lang.Integer r2 = r0.d
            monitor-enter(r2)
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ all -> 0x01eb }
            com.uc.webview.export.internal.setup.UCSetupException r0 = r0.mException     // Catch:{ all -> 0x01eb }
            if (r0 == 0) goto L_0x019b
            r0 = 1
        L_0x00df:
            com.uc.webview.export.internal.setup.UCAsyncTask r5 = r11.b     // Catch:{ all -> 0x01eb }
            boolean r5 = r5.f     // Catch:{ all -> 0x01eb }
            if (r0 != 0) goto L_0x00e9
            if (r5 == 0) goto L_0x00f3
        L_0x00e9:
            com.uc.webview.export.internal.setup.UCAsyncTask r6 = r11.b     // Catch:{ all -> 0x01eb }
            r7 = 10012(0x271c, float:1.403E-41)
            r8 = 0
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ all -> 0x01eb }
            r6.invoke(r7, r8)     // Catch:{ all -> 0x01eb }
        L_0x00f3:
            com.uc.webview.export.internal.setup.UCAsyncTask r6 = r11.b     // Catch:{ all -> 0x01eb }
            java.util.LinkedList r6 = r6.b     // Catch:{ all -> 0x01eb }
            if (r6 == 0) goto L_0x019e
            com.uc.webview.export.internal.setup.UCAsyncTask r6 = r11.b     // Catch:{ all -> 0x01eb }
            java.util.LinkedList r6 = r6.b     // Catch:{ all -> 0x01eb }
            boolean r6 = r6.isEmpty()     // Catch:{ all -> 0x01eb }
            if (r6 != 0) goto L_0x019e
            monitor-exit(r2)     // Catch:{ all -> 0x01eb }
        L_0x0108:
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b
            if (r0 == 0) goto L_0x0210
            com.uc.webview.export.internal.setup.aq r0 = r11.a
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.a
            android.os.Handler r2 = r0.i
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b
            java.util.LinkedList r0 = r0.b
            java.lang.Object r0 = r0.poll()
            java.lang.Runnable r0 = (java.lang.Runnable) r0
            r2.post(r0)
        L_0x0123:
            java.lang.Boolean r0 = com.uc.webview.export.internal.setup.UCAsyncTask.m     // Catch:{ Throwable -> 0x0227 }
            boolean r0 = r0.booleanValue()     // Catch:{ Throwable -> 0x0227 }
            if (r0 == 0) goto L_0x0161
            com.uc.webview.export.internal.setup.aq r0 = r11.a     // Catch:{ Throwable -> 0x0227 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.a     // Catch:{ Throwable -> 0x0227 }
            java.util.Vector r2 = r0.n     // Catch:{ Throwable -> 0x0227 }
            android.util.Pair r3 = new android.util.Pair     // Catch:{ Throwable -> 0x0227 }
            if (r1 != 0) goto L_0x021d
            java.lang.String r0 = "null"
        L_0x013c:
            android.util.Pair r1 = new android.util.Pair     // Catch:{ Throwable -> 0x0227 }
            long r6 = r4.getMilis()     // Catch:{ Throwable -> 0x0227 }
            java.lang.Long r5 = java.lang.Long.valueOf(r6)     // Catch:{ Throwable -> 0x0227 }
            long r6 = r4.getMilisCpu()     // Catch:{ Throwable -> 0x0227 }
            java.lang.Long r4 = java.lang.Long.valueOf(r6)     // Catch:{ Throwable -> 0x0227 }
            r1.<init>(r5, r4)     // Catch:{ Throwable -> 0x0227 }
            r3.<init>(r0, r1)     // Catch:{ Throwable -> 0x0227 }
            r2.add(r3)     // Catch:{ Throwable -> 0x0227 }
            com.uc.webview.export.internal.setup.aq r0 = r11.a     // Catch:{ Throwable -> 0x0227 }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.a     // Catch:{ Throwable -> 0x0227 }
            java.lang.String r1 = "cost"
            r0.callback(r1)     // Catch:{ Throwable -> 0x0227 }
        L_0x0161:
            return
        L_0x0162:
            r0 = move-exception
            r4 = r1
            goto L_0x0014
        L_0x0166:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0166 }
            throw r0     // Catch:{ Throwable -> 0x0169 }
        L_0x0169:
            r0 = move-exception
            com.uc.webview.export.internal.setup.UCAsyncTask r5 = r11.b
            boolean r2 = r0 instanceof com.uc.webview.export.internal.setup.UCSetupException
            if (r2 == 0) goto L_0x0194
            com.uc.webview.export.internal.setup.UCSetupException r0 = (com.uc.webview.export.internal.setup.UCSetupException) r0
        L_0x0172:
            r5.mException = r0
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ Throwable -> 0x018b }
            java.lang.Integer r2 = r0.d     // Catch:{ Throwable -> 0x018b }
            monitor-enter(r2)     // Catch:{ Throwable -> 0x018b }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ all -> 0x0188 }
            r5 = 10012(0x271c, float:1.403E-41)
            r6 = 0
            java.lang.Object[] r6 = new java.lang.Object[r6]     // Catch:{ all -> 0x0188 }
            r0.invoke(r5, r6)     // Catch:{ all -> 0x0188 }
            monitor-exit(r2)     // Catch:{ all -> 0x0188 }
            goto L_0x00c5
        L_0x0188:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x0188 }
            throw r0     // Catch:{ Throwable -> 0x018b }
        L_0x018b:
            r0 = move-exception
            goto L_0x00c5
        L_0x018e:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x018e }
            throw r0     // Catch:{ Throwable -> 0x0169 }
        L_0x0191:
            r0 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0191 }
            throw r0     // Catch:{ Throwable -> 0x0169 }
        L_0x0194:
            com.uc.webview.export.internal.setup.UCSetupException r2 = new com.uc.webview.export.internal.setup.UCSetupException
            r2.<init>((java.lang.Throwable) r0)
            r0 = r2
            goto L_0x0172
        L_0x019b:
            r0 = r3
            goto L_0x00df
        L_0x019e:
            if (r5 == 0) goto L_0x01ee
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ all -> 0x01eb }
            java.lang.String r5 = "stop"
            r0.callback(r5)     // Catch:{ all -> 0x01eb }
        L_0x01a8:
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ all -> 0x01eb }
            java.lang.String r5 = "die"
            r0.callback(r5)     // Catch:{ all -> 0x01eb }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ all -> 0x01eb }
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.a     // Catch:{ all -> 0x01eb }
            r11.b = r0     // Catch:{ all -> 0x01eb }
            com.uc.webview.export.internal.setup.UCAsyncTask r5 = r11.b     // Catch:{ all -> 0x01eb }
            if (r5 == 0) goto L_0x01e4
            java.lang.Integer r6 = r5.d     // Catch:{ all -> 0x01eb }
            monitor-enter(r6)     // Catch:{ all -> 0x01eb }
            int r0 = com.uc.webview.export.internal.setup.UCAsyncTask.e(r5)     // Catch:{ all -> 0x020a }
            float r0 = (float) r0     // Catch:{ all -> 0x020a }
            float r7 = r0 * r10
            r0 = 10014(0x271e, float:1.4033E-41)
            r8 = 0
            java.lang.Object[] r8 = new java.lang.Object[r8]     // Catch:{ all -> 0x020a }
            java.lang.Object r0 = r5.invokeO(r0, r8)     // Catch:{ all -> 0x020a }
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch:{ all -> 0x020a }
            int r0 = r0.intValue()     // Catch:{ all -> 0x020a }
            float r0 = (float) r0     // Catch:{ all -> 0x020a }
            float r0 = r7 / r0
            int r0 = (int) r0     // Catch:{ all -> 0x020a }
            r5.mPercent = r0     // Catch:{ all -> 0x020a }
            monitor-exit(r6)     // Catch:{ all -> 0x020a }
            java.lang.String r0 = "progress"
            r5.callback(r0)     // Catch:{ all -> 0x01eb }
        L_0x01e4:
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ all -> 0x01eb }
            if (r0 != 0) goto L_0x020d
            monitor-exit(r2)     // Catch:{ all -> 0x01eb }
            goto L_0x0108
        L_0x01eb:
            r0 = move-exception
            monitor-exit(r2)     // Catch:{ all -> 0x01eb }
            throw r0
        L_0x01ee:
            if (r0 == 0) goto L_0x0201
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ all -> 0x01eb }
            java.lang.String r5 = "exception"
            r0.callback(r5)     // Catch:{ all -> 0x01eb }
        L_0x01f8:
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ all -> 0x01eb }
            java.lang.String r5 = "gone"
            r0.callback(r5)     // Catch:{ all -> 0x01eb }
            goto L_0x01a8
        L_0x0201:
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r11.b     // Catch:{ all -> 0x01eb }
            java.lang.String r5 = "success"
            r0.callback(r5)     // Catch:{ all -> 0x01eb }
            goto L_0x01f8
        L_0x020a:
            r0 = move-exception
            monitor-exit(r6)     // Catch:{ all -> 0x020a }
            throw r0     // Catch:{ all -> 0x01eb }
        L_0x020d:
            monitor-exit(r2)     // Catch:{ all -> 0x01eb }
            goto L_0x00d1
        L_0x0210:
            com.uc.webview.export.internal.setup.aq r0 = r11.a
            com.uc.webview.export.internal.setup.UCAsyncTask r0 = r0.a
            r2 = 10008(0x2718, float:1.4024E-41)
            java.lang.Object[] r3 = new java.lang.Object[r3]
            r0.invokeO(r2, r3)
            goto L_0x0123
        L_0x021d:
            java.lang.Class r0 = r1.getClass()     // Catch:{ Throwable -> 0x0227 }
            java.lang.String r0 = r0.getSimpleName()     // Catch:{ Throwable -> 0x0227 }
            goto L_0x013c
        L_0x0227:
            r0 = move-exception
            goto L_0x0161
        L_0x022a:
            r0 = move-exception
            goto L_0x00d1
        L_0x022d:
            r0 = move-exception
            goto L_0x0035
        L_0x0230:
            r0 = r1
            goto L_0x0013
        */
        throw new UnsupportedOperationException("Method not decompiled: com.uc.webview.export.internal.setup.ar.dispatchMessage(android.os.Message):void");
    }
}
