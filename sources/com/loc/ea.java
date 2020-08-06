package com.loc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import com.bftv.fui.constantplugin.Constant;
import java.util.ArrayList;
import java.util.List;

@SuppressLint({"NewApi"})
/* compiled from: CgiManager */
public final class ea {
    private static boolean v = false;
    int a = 0;
    ArrayList<dz> b = new ArrayList<>();
    ArrayList<da> c = new ArrayList<>();
    TelephonyManager d = null;
    long e = 0;
    CellLocation f;
    boolean g = false;
    PhoneStateListener h = null;
    String i = null;
    boolean j = false;
    StringBuilder k = null;
    private Context l;
    private String m = null;
    private ArrayList<dz> n = new ArrayList<>();
    private int o = -113;
    private dy p = null;
    private Object q;
    private int r = 0;
    /* access modifiers changed from: private */
    public long s = 0;
    @SuppressLint({"NewApi"})
    private TelephonyManager.CellInfoCallback t;
    /* access modifiers changed from: private */
    public boolean u = false;
    /* access modifiers changed from: private */
    public ds w;
    /* access modifiers changed from: private */
    public boolean x = false;
    /* access modifiers changed from: private */
    public Object y = new Object();

    @SuppressLint({"NewApi"})
    /* compiled from: CgiManager */
    class a extends TelephonyManager.CellInfoCallback {
        a() {
        }

        public final void onCellInfo(List<CellInfo> list) {
            boolean unused = ea.this.u = true;
            CellLocation a2 = ea.this.a(list);
            if (a2 != null) {
                ea.this.f = a2;
                ea.this.g = true;
                ea.this.a(false);
                long unused2 = ea.this.s = et.b();
            }
        }
    }

    /* compiled from: CgiManager */
    class b extends PhoneStateListener {
        b() {
        }

        public final void onCellInfoChanged(List<CellInfo> list) {
            try {
                if (ea.this.w != null) {
                    ea.this.w.c();
                }
            } catch (Throwable th) {
            }
        }

        public final void onCellLocationChanged(CellLocation cellLocation) {
            try {
                if (ea.this.a(cellLocation)) {
                    ea.this.f = cellLocation;
                    ea.this.g = true;
                    ea.this.a(false);
                    long unused = ea.this.s = et.b();
                }
            } catch (Throwable th) {
            }
        }

        public final void onServiceStateChanged(ServiceState serviceState) {
            try {
                switch (serviceState.getState()) {
                    case 0:
                        ea.this.a(false, false);
                        return;
                    case 1:
                        ea.this.i();
                        return;
                    default:
                        return;
                }
            } catch (Throwable th) {
            }
        }

        public final void onSignalStrengthChanged(int i) {
            int i2 = -113;
            try {
                switch (ea.this.a) {
                    case 1:
                        i2 = et.a(i);
                        break;
                    case 2:
                        i2 = et.a(i);
                        break;
                }
                ea.this.b(i2);
            } catch (Throwable th) {
            }
        }

        public final void onSignalStrengthsChanged(SignalStrength signalStrength) {
            if (signalStrength != null) {
                int i = -113;
                try {
                    switch (ea.this.a) {
                        case 1:
                            i = et.a(signalStrength.getGsmSignalStrength());
                            break;
                        case 2:
                            i = signalStrength.getCdmaDbm();
                            break;
                    }
                    ea.this.b(i);
                    if (ea.this.w != null) {
                        ea.this.w.c();
                    }
                } catch (Throwable th) {
                }
            }
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ea(android.content.Context r7) {
        /*
            r6 = this;
            r4 = 0
            r2 = 0
            r3 = 0
            r6.<init>()
            r6.a = r3
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r6.b = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r6.c = r0
            r6.m = r2
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r6.n = r0
            r0 = -113(0xffffffffffffff8f, float:NaN)
            r6.o = r0
            r6.d = r2
            r6.p = r2
            r6.e = r4
            r6.r = r3
            r6.s = r4
            r6.g = r3
            r6.h = r2
            r6.u = r3
            r6.i = r2
            r6.j = r3
            r6.k = r2
            r6.x = r3
            java.lang.Object r0 = new java.lang.Object
            r0.<init>()
            r6.y = r0
            r6.l = r7
            android.telephony.TelephonyManager r0 = r6.d
            if (r0 != 0) goto L_0x0056
            android.content.Context r0 = r6.l
            java.lang.String r1 = "phone"
            java.lang.Object r0 = com.loc.et.a((android.content.Context) r0, (java.lang.String) r1)
            android.telephony.TelephonyManager r0 = (android.telephony.TelephonyManager) r0
            r6.d = r0
        L_0x0056:
            android.telephony.TelephonyManager r0 = r6.d
            if (r0 == 0) goto L_0x0088
            android.telephony.TelephonyManager r0 = r6.d     // Catch:{ SecurityException -> 0x0090, Throwable -> 0x0098 }
            android.telephony.CellLocation r0 = r0.getCellLocation()     // Catch:{ SecurityException -> 0x0090, Throwable -> 0x0098 }
            int r0 = r6.c((android.telephony.CellLocation) r0)     // Catch:{ SecurityException -> 0x0090, Throwable -> 0x0098 }
            r6.a = r0     // Catch:{ SecurityException -> 0x0090, Throwable -> 0x0098 }
        L_0x0066:
            int r0 = r6.s()     // Catch:{ Throwable -> 0x00b3 }
            r6.r = r0     // Catch:{ Throwable -> 0x00b3 }
            int r0 = r6.r     // Catch:{ Throwable -> 0x00b3 }
            switch(r0) {
                case 1: goto L_0x00a7;
                case 2: goto L_0x00b5;
                default: goto L_0x0071;
            }     // Catch:{ Throwable -> 0x00b3 }
        L_0x0071:
            android.content.Context r0 = r6.l     // Catch:{ Throwable -> 0x00b3 }
            java.lang.String r1 = "phone2"
            java.lang.Object r0 = com.loc.et.a((android.content.Context) r0, (java.lang.String) r1)     // Catch:{ Throwable -> 0x00b3 }
            r6.q = r0     // Catch:{ Throwable -> 0x00b3 }
        L_0x007c:
            java.util.concurrent.ExecutorService r0 = com.loc.ab.d()
            com.loc.ea$1 r1 = new com.loc.ea$1
            r1.<init>()
            r0.submit(r1)
        L_0x0088:
            com.loc.dy r0 = new com.loc.dy
            r0.<init>()
            r6.p = r0
            return
        L_0x0090:
            r0 = move-exception
            java.lang.String r0 = r0.getMessage()
            r6.i = r0
            goto L_0x0066
        L_0x0098:
            r0 = move-exception
            r6.i = r2
            java.lang.String r1 = "CgiManager"
            java.lang.String r2 = "CgiManager"
            com.loc.en.a(r0, r1, r2)
            r6.a = r3
            goto L_0x0066
        L_0x00a7:
            android.content.Context r0 = r6.l     // Catch:{ Throwable -> 0x00b3 }
            java.lang.String r1 = "phone_msim"
            java.lang.Object r0 = com.loc.et.a((android.content.Context) r0, (java.lang.String) r1)     // Catch:{ Throwable -> 0x00b3 }
            r6.q = r0     // Catch:{ Throwable -> 0x00b3 }
            goto L_0x007c
        L_0x00b3:
            r0 = move-exception
            goto L_0x007c
        L_0x00b5:
            android.content.Context r0 = r6.l     // Catch:{ Throwable -> 0x00b3 }
            java.lang.String r1 = "phone2"
            java.lang.Object r0 = com.loc.et.a((android.content.Context) r0, (java.lang.String) r1)     // Catch:{ Throwable -> 0x00b3 }
            r6.q = r0     // Catch:{ Throwable -> 0x00b3 }
            goto L_0x007c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ea.<init>(android.content.Context):void");
    }

    private CellLocation a(Object obj, String str, Object... objArr) {
        if (obj == null) {
            return null;
        }
        try {
            Object a2 = eq.a(obj, str, objArr);
            CellLocation cellLocation = a2 != null ? (CellLocation) a2 : null;
            if (b(cellLocation)) {
                return cellLocation;
            }
        } catch (Throwable th) {
        }
        return null;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: android.telephony.gsm.GsmCellLocation} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: android.telephony.gsm.GsmCellLocation} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v9, resolved type: android.telephony.gsm.GsmCellLocation} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v14, resolved type: android.telephony.cdma.CdmaCellLocation} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v2, resolved type: android.telephony.gsm.GsmCellLocation} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v22, resolved type: android.telephony.gsm.GsmCellLocation} */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0044  */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized android.telephony.CellLocation a(java.util.List<android.telephony.CellInfo> r9) {
        /*
            r8 = this;
            r6 = 0
            monitor-enter(r8)
            if (r9 == 0) goto L_0x000a
            boolean r0 = r9.isEmpty()     // Catch:{ all -> 0x005f }
            if (r0 == 0) goto L_0x000c
        L_0x000a:
            monitor-exit(r8)
            return r6
        L_0x000c:
            r0 = 0
            r2 = r0
            r1 = r6
        L_0x000f:
            int r0 = r9.size()     // Catch:{ all -> 0x005f }
            if (r2 >= r0) goto L_0x006b
            java.lang.Object r0 = r9.get(r2)     // Catch:{ all -> 0x005f }
            android.telephony.CellInfo r0 = (android.telephony.CellInfo) r0     // Catch:{ all -> 0x005f }
            if (r0 == 0) goto L_0x0047
            com.loc.dz r0 = r8.a((android.telephony.CellInfo) r0)     // Catch:{ Throwable -> 0x0046 }
            if (r0 != 0) goto L_0x0028
        L_0x0023:
            int r1 = r2 + 1
            r2 = r1
            r1 = r0
            goto L_0x000f
        L_0x0028:
            r5 = r0
        L_0x0029:
            if (r5 == 0) goto L_0x0069
            int r0 = r5.k     // Catch:{ Throwable -> 0x0059 }
            r1 = 2
            if (r0 != r1) goto L_0x0049
            android.telephony.cdma.CdmaCellLocation r0 = new android.telephony.cdma.CdmaCellLocation     // Catch:{ Throwable -> 0x0059 }
            r0.<init>()     // Catch:{ Throwable -> 0x0059 }
            int r1 = r5.i     // Catch:{ Throwable -> 0x0062 }
            int r2 = r5.e     // Catch:{ Throwable -> 0x0062 }
            int r3 = r5.f     // Catch:{ Throwable -> 0x0062 }
            int r4 = r5.g     // Catch:{ Throwable -> 0x0062 }
            int r5 = r5.h     // Catch:{ Throwable -> 0x0062 }
            r0.setCellLocationData(r1, r2, r3, r4, r5)     // Catch:{ Throwable -> 0x0062 }
        L_0x0042:
            if (r0 == 0) goto L_0x000a
            r6 = r0
            goto L_0x000a
        L_0x0046:
            r0 = move-exception
        L_0x0047:
            r0 = r1
            goto L_0x0023
        L_0x0049:
            android.telephony.gsm.GsmCellLocation r0 = new android.telephony.gsm.GsmCellLocation     // Catch:{ Throwable -> 0x0059 }
            r0.<init>()     // Catch:{ Throwable -> 0x0059 }
            int r1 = r5.c     // Catch:{ Throwable -> 0x0067 }
            int r2 = r5.d     // Catch:{ Throwable -> 0x0067 }
            r0.setLacAndCid(r1, r2)     // Catch:{ Throwable -> 0x0067 }
        L_0x0055:
            r7 = r6
            r6 = r0
            r0 = r7
            goto L_0x0042
        L_0x0059:
            r0 = move-exception
            r0 = r6
        L_0x005b:
            r7 = r6
            r6 = r0
            r0 = r7
            goto L_0x0042
        L_0x005f:
            r0 = move-exception
            monitor-exit(r8)
            throw r0
        L_0x0062:
            r1 = move-exception
            r7 = r0
            r0 = r6
            r6 = r7
            goto L_0x005b
        L_0x0067:
            r1 = move-exception
            goto L_0x005b
        L_0x0069:
            r0 = r6
            goto L_0x0055
        L_0x006b:
            r5 = r1
            goto L_0x0029
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ea.a(java.util.List):android.telephony.CellLocation");
    }

    private static dz a(int i2, boolean z, int i3, int i4, int i5, int i6, int i7) {
        dz dzVar = new dz(i2, z);
        dzVar.a = i3;
        dzVar.b = i4;
        dzVar.c = i5;
        dzVar.d = i6;
        dzVar.j = i7;
        return dzVar;
    }

    private dz a(CellInfo cellInfo) {
        CellInfoLte cellInfoLte;
        boolean isRegistered = cellInfo.isRegistered();
        if (cellInfo instanceof CellInfoCdma) {
            return a((CellInfoCdma) cellInfo, isRegistered);
        }
        if (cellInfo instanceof CellInfoGsm) {
            CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
            if (cellInfoGsm == null || cellInfoGsm.getCellIdentity() == null) {
                return null;
            }
            CellIdentityGsm cellIdentity = cellInfoGsm.getCellIdentity();
            if (!c(cellIdentity.getLac()) || !d(cellIdentity.getCid())) {
                return null;
            }
            return a(1, isRegistered, cellIdentity.getMcc(), cellIdentity.getMnc(), cellIdentity.getLac(), cellIdentity.getCid(), cellInfoGsm.getCellSignalStrength().getDbm());
        } else if (cellInfo instanceof CellInfoWcdma) {
            CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
            if (cellInfoWcdma == null || cellInfoWcdma.getCellIdentity() == null) {
                return null;
            }
            CellIdentityWcdma cellIdentity2 = cellInfoWcdma.getCellIdentity();
            if (!c(cellIdentity2.getLac()) || !d(cellIdentity2.getCid())) {
                return null;
            }
            dz a2 = a(4, isRegistered, cellIdentity2.getMcc(), cellIdentity2.getMnc(), cellIdentity2.getLac(), cellIdentity2.getCid(), cellInfoWcdma.getCellSignalStrength().getDbm());
            a2.o = cellIdentity2.getPsc();
            return a2;
        } else if (!(cellInfo instanceof CellInfoLte) || (cellInfoLte = (CellInfoLte) cellInfo) == null || cellInfoLte.getCellIdentity() == null) {
            return null;
        } else {
            CellIdentityLte cellIdentity3 = cellInfoLte.getCellIdentity();
            if (!c(cellIdentity3.getTac()) || !d(cellIdentity3.getCi())) {
                return null;
            }
            dz a3 = a(3, isRegistered, cellIdentity3.getMcc(), cellIdentity3.getMnc(), cellIdentity3.getTac(), cellIdentity3.getCi(), cellInfoLte.getCellSignalStrength().getDbm());
            a3.o = cellIdentity3.getPci();
            return a3;
        }
    }

    private dz a(CellInfoCdma cellInfoCdma, boolean z) {
        int i2;
        int i3;
        if (cellInfoCdma == null || cellInfoCdma.getCellIdentity() == null) {
            return null;
        }
        CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
        if (cellIdentity.getSystemId() <= 0 || cellIdentity.getNetworkId() < 0 || cellIdentity.getBasestationId() < 0) {
            return null;
        }
        CellIdentityCdma cellIdentity2 = cellInfoCdma.getCellIdentity();
        String[] a2 = et.a(this.d);
        try {
            i2 = Integer.parseInt(a2[0]);
            try {
                i3 = Integer.parseInt(a2[1]);
            } catch (Throwable th) {
            }
        } catch (Throwable th2) {
            i2 = 0;
            i3 = 0;
            dz a3 = a(2, z, i2, i3, 0, 0, cellInfoCdma.getCellSignalStrength().getCdmaDbm());
            a3.g = cellIdentity2.getSystemId();
            a3.h = cellIdentity2.getNetworkId();
            a3.i = cellIdentity2.getBasestationId();
            a3.e = cellIdentity2.getLatitude();
            a3.f = cellIdentity2.getLongitude();
            return a3;
        }
        dz a32 = a(2, z, i2, i3, 0, 0, cellInfoCdma.getCellSignalStrength().getCdmaDbm());
        a32.g = cellIdentity2.getSystemId();
        a32.h = cellIdentity2.getNetworkId();
        a32.i = cellIdentity2.getBasestationId();
        a32.e = cellIdentity2.getLatitude();
        a32.f = cellIdentity2.getLongitude();
        return a32;
    }

    private static dz a(NeighboringCellInfo neighboringCellInfo, String[] strArr) {
        try {
            dz dzVar = new dz(1, false);
            dzVar.a = Integer.parseInt(strArr[0]);
            dzVar.b = Integer.parseInt(strArr[1]);
            dzVar.c = eq.b(neighboringCellInfo, "getLac", new Object[0]);
            dzVar.d = neighboringCellInfo.getCid();
            dzVar.j = et.a(neighboringCellInfo.getRssi());
            return dzVar;
        } catch (Throwable th) {
            en.a(th, "CgiManager", "getGsm");
            return null;
        }
    }

    private synchronized void a(CellLocation cellLocation, String[] strArr, boolean z) {
        List<NeighboringCellInfo> list;
        dz a2;
        if (cellLocation != null) {
            if (this.d != null) {
                this.b.clear();
                if (b(cellLocation)) {
                    this.a = 1;
                    ArrayList<dz> arrayList = this.b;
                    GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                    dz dzVar = new dz(1, true);
                    dzVar.a = et.g(strArr[0]);
                    dzVar.b = et.g(strArr[1]);
                    dzVar.c = gsmCellLocation.getLac();
                    dzVar.d = gsmCellLocation.getCid();
                    dzVar.j = this.o;
                    arrayList.add(dzVar);
                    if (!z && Build.VERSION.SDK_INT <= 28 && (list = (List) eq.a(this.d, "getNeighboringCellInfo", new Object[0])) != null && !list.isEmpty()) {
                        for (NeighboringCellInfo neighboringCellInfo : list) {
                            if (neighboringCellInfo != null && a(neighboringCellInfo.getLac(), neighboringCellInfo.getCid()) && (a2 = a(neighboringCellInfo, strArr)) != null && !this.b.contains(a2)) {
                                this.b.add(a2);
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0054, code lost:
        if (r0 != false) goto L_0x0015;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void a(boolean r9) {
        /*
            r8 = this;
            r7 = 2147483647(0x7fffffff, float:NaN)
            r1 = 1
            r2 = 0
            monitor-enter(r8)
            android.telephony.TelephonyManager r0 = r8.d     // Catch:{ all -> 0x001d }
            java.lang.String[] r3 = com.loc.et.a((android.telephony.TelephonyManager) r0)     // Catch:{ all -> 0x001d }
            android.telephony.CellLocation r0 = r8.f     // Catch:{ all -> 0x001d }
            int r0 = r8.c((android.telephony.CellLocation) r0)     // Catch:{ all -> 0x001d }
            switch(r0) {
                case 1: goto L_0x0017;
                case 2: goto L_0x0020;
                default: goto L_0x0015;
            }
        L_0x0015:
            monitor-exit(r8)
            return
        L_0x0017:
            android.telephony.CellLocation r0 = r8.f     // Catch:{ all -> 0x001d }
            r8.a((android.telephony.CellLocation) r0, (java.lang.String[]) r3, (boolean) r9)     // Catch:{ all -> 0x001d }
            goto L_0x0015
        L_0x001d:
            r0 = move-exception
            monitor-exit(r8)
            throw r0
        L_0x0020:
            android.telephony.CellLocation r4 = r8.f     // Catch:{ all -> 0x001d }
            if (r4 == 0) goto L_0x0015
            java.util.ArrayList<com.loc.dz> r0 = r8.b     // Catch:{ all -> 0x001d }
            r0.clear()     // Catch:{ all -> 0x001d }
            java.lang.Object r0 = r8.q     // Catch:{ Throwable -> 0x00e9 }
            if (r0 == 0) goto L_0x0056
            java.lang.Class r0 = r4.getClass()     // Catch:{ Throwable -> 0x00f5 }
            java.lang.String r5 = "mGsmCellLoc"
            java.lang.reflect.Field r0 = r0.getDeclaredField(r5)     // Catch:{ Throwable -> 0x00f5 }
            boolean r5 = r0.isAccessible()     // Catch:{ Throwable -> 0x00f5 }
            if (r5 != 0) goto L_0x0042
            r5 = 1
            r0.setAccessible(r5)     // Catch:{ Throwable -> 0x00f5 }
        L_0x0042:
            java.lang.Object r0 = r0.get(r4)     // Catch:{ Throwable -> 0x00f5 }
            android.telephony.gsm.GsmCellLocation r0 = (android.telephony.gsm.GsmCellLocation) r0     // Catch:{ Throwable -> 0x00f5 }
            if (r0 == 0) goto L_0x00f6
            boolean r5 = r8.b((android.telephony.CellLocation) r0)     // Catch:{ Throwable -> 0x00f5 }
            if (r5 == 0) goto L_0x00f6
            r8.a((android.telephony.CellLocation) r0, (java.lang.String[]) r3, (boolean) r9)     // Catch:{ Throwable -> 0x00f5 }
            r0 = r1
        L_0x0054:
            if (r0 != 0) goto L_0x0015
        L_0x0056:
            boolean r0 = r8.b((android.telephony.CellLocation) r4)     // Catch:{ Throwable -> 0x00e9 }
            if (r0 == 0) goto L_0x0015
            r0 = 2
            r8.a = r0     // Catch:{ Throwable -> 0x00e9 }
            com.loc.dz r0 = new com.loc.dz     // Catch:{ Throwable -> 0x00e9 }
            r5 = 2
            r6 = 1
            r0.<init>(r5, r6)     // Catch:{ Throwable -> 0x00e9 }
            r5 = 0
            r5 = r3[r5]     // Catch:{ Throwable -> 0x00e9 }
            int r5 = java.lang.Integer.parseInt(r5)     // Catch:{ Throwable -> 0x00e9 }
            r0.a = r5     // Catch:{ Throwable -> 0x00e9 }
            r5 = 1
            r3 = r3[r5]     // Catch:{ Throwable -> 0x00e9 }
            int r3 = java.lang.Integer.parseInt(r3)     // Catch:{ Throwable -> 0x00e9 }
            r0.b = r3     // Catch:{ Throwable -> 0x00e9 }
            java.lang.String r3 = "getSystemId"
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x00e9 }
            int r3 = com.loc.eq.b(r4, r3, r5)     // Catch:{ Throwable -> 0x00e9 }
            r0.g = r3     // Catch:{ Throwable -> 0x00e9 }
            java.lang.String r3 = "getNetworkId"
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x00e9 }
            int r3 = com.loc.eq.b(r4, r3, r5)     // Catch:{ Throwable -> 0x00e9 }
            r0.h = r3     // Catch:{ Throwable -> 0x00e9 }
            java.lang.String r3 = "getBaseStationId"
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x00e9 }
            int r3 = com.loc.eq.b(r4, r3, r5)     // Catch:{ Throwable -> 0x00e9 }
            r0.i = r3     // Catch:{ Throwable -> 0x00e9 }
            int r3 = r8.o     // Catch:{ Throwable -> 0x00e9 }
            r0.j = r3     // Catch:{ Throwable -> 0x00e9 }
            java.lang.String r3 = "getBaseStationLatitude"
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x00e9 }
            int r3 = com.loc.eq.b(r4, r3, r5)     // Catch:{ Throwable -> 0x00e9 }
            r0.e = r3     // Catch:{ Throwable -> 0x00e9 }
            java.lang.String r3 = "getBaseStationLongitude"
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch:{ Throwable -> 0x00e9 }
            int r3 = com.loc.eq.b(r4, r3, r5)     // Catch:{ Throwable -> 0x00e9 }
            r0.f = r3     // Catch:{ Throwable -> 0x00e9 }
            int r3 = r0.e     // Catch:{ Throwable -> 0x00e9 }
            int r4 = r0.f     // Catch:{ Throwable -> 0x00e9 }
            if (r3 != r4) goto L_0x00f9
            int r3 = r0.e     // Catch:{ Throwable -> 0x00e9 }
            if (r3 <= 0) goto L_0x00f9
        L_0x00c2:
            int r2 = r0.e     // Catch:{ Throwable -> 0x00e9 }
            if (r2 < 0) goto L_0x00d4
            int r2 = r0.f     // Catch:{ Throwable -> 0x00e9 }
            if (r2 < 0) goto L_0x00d4
            int r2 = r0.e     // Catch:{ Throwable -> 0x00e9 }
            if (r2 == r7) goto L_0x00d4
            int r2 = r0.f     // Catch:{ Throwable -> 0x00e9 }
            if (r2 == r7) goto L_0x00d4
            if (r1 == 0) goto L_0x00da
        L_0x00d4:
            r1 = 0
            r0.e = r1     // Catch:{ Throwable -> 0x00e9 }
            r1 = 0
            r0.f = r1     // Catch:{ Throwable -> 0x00e9 }
        L_0x00da:
            java.util.ArrayList<com.loc.dz> r1 = r8.b     // Catch:{ Throwable -> 0x00e9 }
            boolean r1 = r1.contains(r0)     // Catch:{ Throwable -> 0x00e9 }
            if (r1 != 0) goto L_0x0015
            java.util.ArrayList<com.loc.dz> r1 = r8.b     // Catch:{ Throwable -> 0x00e9 }
            r1.add(r0)     // Catch:{ Throwable -> 0x00e9 }
            goto L_0x0015
        L_0x00e9:
            r0 = move-exception
            java.lang.String r1 = "CgiManager"
            java.lang.String r2 = "hdlCdmaLocChange"
            com.loc.en.a(r0, r1, r2)     // Catch:{ all -> 0x001d }
            goto L_0x0015
        L_0x00f5:
            r0 = move-exception
        L_0x00f6:
            r0 = r2
            goto L_0x0054
        L_0x00f9:
            r1 = r2
            goto L_0x00c2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ea.a(boolean):void");
    }

    public static boolean a(int i2) {
        return i2 > 0 && i2 <= 15;
    }

    private static boolean a(int i2, int i3) {
        return (i2 == -1 || i2 == 0 || i2 > 65535 || i3 == -1 || i3 == 0 || i3 == 65535 || i3 >= 268435455) ? false : true;
    }

    /* access modifiers changed from: private */
    public synchronized void b(int i2) {
        if (i2 != -113) {
            this.o = i2;
            switch (this.a) {
                case 1:
                case 2:
                    if (this.b != null && !this.b.isEmpty()) {
                        try {
                            this.b.get(0).j = this.o;
                            break;
                        } catch (Throwable th) {
                            break;
                        }
                    }
            }
        } else {
            this.o = -113;
        }
    }

    @SuppressLint({"NewApi"})
    private synchronized void b(boolean z, boolean z2) {
        List<CellInfo> list;
        List<CellInfo> list2 = null;
        synchronized (this) {
            if (!this.j && this.d != null) {
                if (Build.VERSION.SDK_INT >= 29 && this.l.getApplicationInfo().targetSdkVersion >= 29) {
                    if (this.t == null) {
                        this.t = new a();
                    }
                    this.d.requestCellInfoUpdate(ab.d(), this.t);
                    if (z2 || z) {
                        int i2 = 0;
                        while (!this.u && i2 < 20) {
                            try {
                                Thread.sleep(5);
                            } catch (Throwable th) {
                            }
                            i2++;
                        }
                    }
                }
                CellLocation p2 = p();
                if (!b(p2)) {
                    p2 = q();
                }
                if (b(p2)) {
                    this.f = p2;
                    this.s = et.b();
                } else if (et.b() - this.s > 60000) {
                    this.f = null;
                    this.b.clear();
                    this.n.clear();
                }
            }
            if (!this.g && this.f == null && z2) {
                int i3 = 0;
                do {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    i3++;
                    if (this.f != null) {
                        break;
                    }
                } while (i3 >= 50);
            }
            this.g = true;
            if (b(this.f)) {
                a(z);
            }
            try {
                if (et.c() >= 18 && this.d != null) {
                    ArrayList<dz> arrayList = this.n;
                    dy dyVar = this.p;
                    list2 = this.d.getAllCellInfo();
                    this.i = null;
                    list = list2;
                    if (list != null) {
                        int size = list.size();
                        if (size != 0) {
                            if (arrayList != null) {
                                arrayList.clear();
                            }
                            for (int i4 = 0; i4 < size; i4++) {
                                CellInfo cellInfo = list.get(i4);
                                if (cellInfo != null) {
                                    try {
                                        dz a2 = a(cellInfo);
                                        if (a2 != null) {
                                            a2.l = (short) ((int) Math.min(65535, dyVar.a(a2)));
                                            arrayList.add(a2);
                                        }
                                    } catch (Throwable th2) {
                                    }
                                }
                            }
                        }
                    }
                    if (arrayList != null) {
                        if (arrayList.size() > 0) {
                            this.a |= 4;
                            dyVar.a((ArrayList<? extends dz>) arrayList);
                        }
                    }
                }
            } catch (SecurityException e3) {
                this.i = e3.getMessage();
                list = list2;
            } catch (Throwable th3) {
            }
            if (this.d != null) {
                this.m = this.d.getNetworkOperator();
                if (!TextUtils.isEmpty(this.m)) {
                    this.a |= 8;
                }
            }
        }
        return;
    }

    private boolean b(CellLocation cellLocation) {
        boolean a2 = a(cellLocation);
        if (!a2) {
            this.a = 0;
        }
        return a2;
    }

    private int c(CellLocation cellLocation) {
        if (this.j || cellLocation == null) {
            return 0;
        }
        if (cellLocation instanceof GsmCellLocation) {
            return 1;
        }
        try {
            Class.forName("android.telephony.cdma.CdmaCellLocation");
            return 2;
        } catch (Throwable th) {
            en.a(th, "Utils", "getCellLocT");
            return 0;
        }
    }

    static /* synthetic */ void c(ea eaVar) {
        eaVar.h = new b();
        int i2 = 0;
        try {
            i2 = eq.b("android.telephony.PhoneStateListener", "LISTEN_SIGNAL_STRENGTHS");
        } catch (Throwable th) {
        }
        if (i2 == 0) {
            try {
                eaVar.d.listen(eaVar.h, 16);
            } catch (Throwable th2) {
            }
        } else {
            try {
                eaVar.d.listen(eaVar.h, i2 | 16);
            } catch (Throwable th3) {
            }
        }
    }

    private static boolean c(int i2) {
        return (i2 == -1 || i2 == 0 || i2 > 65535) ? false : true;
    }

    private static boolean d(int i2) {
        return (i2 == -1 || i2 == 0 || i2 == 65535 || i2 >= 268435455) ? false : true;
    }

    private CellLocation n() {
        if (this.d != null) {
            try {
                CellLocation cellLocation = this.d.getCellLocation();
                this.i = null;
                if (b(cellLocation)) {
                    this.f = cellLocation;
                    return cellLocation;
                }
            } catch (SecurityException e2) {
                this.i = e2.getMessage();
            } catch (Throwable th) {
                this.i = null;
                en.a(th, "CgiManager", "getCellLocation");
            }
        }
        return null;
    }

    private synchronized void o() {
        switch (this.a & 3) {
            case 1:
                if (this.b.isEmpty()) {
                    this.a = 0;
                    break;
                }
                break;
            case 2:
                if (this.b.isEmpty()) {
                    this.a = 0;
                    break;
                }
                break;
        }
    }

    @SuppressLint({"NewApi"})
    private CellLocation p() {
        CellLocation cellLocation = null;
        TelephonyManager telephonyManager = this.d;
        if (telephonyManager != null) {
            if (et.c() >= 18) {
                try {
                    cellLocation = a(telephonyManager.getAllCellInfo());
                } catch (SecurityException e2) {
                    this.i = e2.getMessage();
                }
            }
            if (cellLocation == null) {
                cellLocation = n();
                if (!(!b(cellLocation) && (cellLocation = a((Object) telephonyManager, "getCellLocationExt", 1)) == null && (cellLocation = a((Object) telephonyManager, "getCellLocationGemini", 1)) == null)) {
                }
            }
        }
        return cellLocation;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
        r1 = r2.cast(r1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.telephony.CellLocation q() {
        /*
            r6 = this;
            r0 = 0
            r2 = 1
            boolean r1 = v
            if (r1 != 0) goto L_0x0008
            v = r2
        L_0x0008:
            java.lang.Object r1 = r6.q
            if (r1 != 0) goto L_0x000d
        L_0x000c:
            return r0
        L_0x000d:
            java.lang.Class r2 = r6.r()     // Catch:{ Throwable -> 0x0061 }
            boolean r3 = r2.isInstance(r1)     // Catch:{ Throwable -> 0x0061 }
            if (r3 == 0) goto L_0x000c
            java.lang.Object r1 = r2.cast(r1)     // Catch:{ Throwable -> 0x0061 }
            java.lang.String r2 = "getCellLocation"
            r3 = 0
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x0061 }
            android.telephony.CellLocation r0 = r6.a((java.lang.Object) r1, (java.lang.String) r2, (java.lang.Object[]) r3)     // Catch:{ Throwable -> 0x0061 }
            if (r0 != 0) goto L_0x000c
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x0061 }
            r4 = 0
            r5 = 1
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x0061 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x0061 }
            android.telephony.CellLocation r0 = r6.a((java.lang.Object) r1, (java.lang.String) r2, (java.lang.Object[]) r3)     // Catch:{ Throwable -> 0x0061 }
            if (r0 != 0) goto L_0x000c
            java.lang.String r2 = "getCellLocationGemini"
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x0061 }
            r4 = 0
            r5 = 1
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x0061 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x0061 }
            android.telephony.CellLocation r0 = r6.a((java.lang.Object) r1, (java.lang.String) r2, (java.lang.Object[]) r3)     // Catch:{ Throwable -> 0x0061 }
            if (r0 != 0) goto L_0x000c
            java.lang.String r2 = "getAllCellInfo"
            r3 = 1
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Throwable -> 0x0061 }
            r4 = 0
            r5 = 1
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Throwable -> 0x0061 }
            r3[r4] = r5     // Catch:{ Throwable -> 0x0061 }
            android.telephony.CellLocation r0 = r6.a((java.lang.Object) r1, (java.lang.String) r2, (java.lang.Object[]) r3)     // Catch:{ Throwable -> 0x0061 }
            if (r0 == 0) goto L_0x000c
            goto L_0x000c
        L_0x0061:
            r1 = move-exception
            java.lang.String r2 = "CgiManager"
            java.lang.String r3 = "getSim2Cgi"
            com.loc.en.a(r1, r2, r3)
            goto L_0x000c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.loc.ea.q():android.telephony.CellLocation");
    }

    private Class<?> r() {
        String str;
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        switch (this.r) {
            case 0:
                str = "android.telephony.TelephonyManager";
                break;
            case 1:
                str = "android.telephony.MSimTelephonyManager";
                break;
            case 2:
                str = "android.telephony.TelephonyManager2";
                break;
            default:
                str = null;
                break;
        }
        try {
            return systemClassLoader.loadClass(str);
        } catch (Throwable th) {
            en.a(th, "CgiManager", "getSim2TmClass");
            return null;
        }
    }

    private int s() {
        try {
            Class.forName("android.telephony.MSimTelephonyManager");
            this.r = 1;
        } catch (Throwable th) {
        }
        if (this.r == 0) {
            try {
                Class.forName("android.telephony.TelephonyManager2");
                this.r = 2;
            } catch (Throwable th2) {
            }
        }
        return this.r;
    }

    public final List<da> a() {
        ArrayList arrayList = new ArrayList();
        List<CellInfo> allCellInfo = this.d.getAllCellInfo();
        if (Build.VERSION.SDK_INT >= 17) {
            for (CellInfo next : allCellInfo) {
                if (next instanceof CellInfoCdma) {
                    CellInfoCdma cellInfoCdma = (CellInfoCdma) next;
                    CellIdentityCdma cellIdentity = cellInfoCdma.getCellIdentity();
                    db dbVar = new db(next.isRegistered(), true);
                    dbVar.m = cellIdentity.getLatitude();
                    dbVar.n = cellIdentity.getLongitude();
                    dbVar.j = cellIdentity.getSystemId();
                    dbVar.k = cellIdentity.getNetworkId();
                    dbVar.l = cellIdentity.getBasestationId();
                    dbVar.d = cellInfoCdma.getCellSignalStrength().getAsuLevel();
                    dbVar.c = cellInfoCdma.getCellSignalStrength().getCdmaDbm();
                    arrayList.add(dbVar);
                } else if (next instanceof CellInfoGsm) {
                    CellInfoGsm cellInfoGsm = (CellInfoGsm) next;
                    CellIdentityGsm cellIdentity2 = cellInfoGsm.getCellIdentity();
                    dc dcVar = new dc(next.isRegistered(), true);
                    dcVar.a = String.valueOf(cellIdentity2.getMcc());
                    dcVar.b = String.valueOf(cellIdentity2.getMnc());
                    dcVar.j = cellIdentity2.getLac();
                    dcVar.k = cellIdentity2.getCid();
                    dcVar.c = cellInfoGsm.getCellSignalStrength().getDbm();
                    dcVar.d = cellInfoGsm.getCellSignalStrength().getAsuLevel();
                    if (Build.VERSION.SDK_INT >= 24) {
                        dcVar.m = cellIdentity2.getArfcn();
                        dcVar.n = cellIdentity2.getBsic();
                    }
                    arrayList.add(dcVar);
                } else if (next instanceof CellInfoLte) {
                    CellInfoLte cellInfoLte = (CellInfoLte) next;
                    CellIdentityLte cellIdentity3 = cellInfoLte.getCellIdentity();
                    dd ddVar = new dd(next.isRegistered());
                    ddVar.a = String.valueOf(cellIdentity3.getMcc());
                    ddVar.b = String.valueOf(cellIdentity3.getMnc());
                    ddVar.l = cellIdentity3.getPci();
                    ddVar.d = cellInfoLte.getCellSignalStrength().getAsuLevel();
                    ddVar.k = cellIdentity3.getCi();
                    ddVar.m = cellIdentity3.getEarfcn();
                    ddVar.j = cellIdentity3.getTac();
                    ddVar.n = cellInfoLte.getCellSignalStrength().getTimingAdvance();
                    ddVar.c = cellInfoLte.getCellSignalStrength().getDbm();
                    if (Build.VERSION.SDK_INT >= 24) {
                        ddVar.m = cellIdentity3.getEarfcn();
                    }
                    arrayList.add(ddVar);
                } else if (Build.VERSION.SDK_INT >= 18 && (next instanceof CellInfoWcdma)) {
                    CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) next;
                    CellIdentityWcdma cellIdentity4 = cellInfoWcdma.getCellIdentity();
                    de deVar = new de(next.isRegistered(), true);
                    deVar.a = String.valueOf(cellIdentity4.getMcc());
                    deVar.b = String.valueOf(cellIdentity4.getMnc());
                    deVar.j = cellIdentity4.getLac();
                    deVar.k = cellIdentity4.getCid();
                    deVar.l = cellIdentity4.getPsc();
                    deVar.d = cellInfoWcdma.getCellSignalStrength().getAsuLevel();
                    deVar.c = cellInfoWcdma.getCellSignalStrength().getDbm();
                    if (Build.VERSION.SDK_INT >= 24) {
                        deVar.m = cellIdentity4.getUarfcn();
                    }
                    arrayList.add(deVar);
                }
            }
        }
        return arrayList;
    }

    public final void a(ds dsVar) {
        this.w = dsVar;
    }

    public final synchronized void a(boolean z, boolean z2) {
        boolean z3 = false;
        synchronized (this) {
            try {
                this.j = et.a(this.l);
                if (!this.j) {
                    if (et.b() - this.e >= 10000) {
                        z3 = true;
                    }
                }
                if (z3 || this.b.isEmpty()) {
                    b(z, z2);
                    this.e = et.b();
                }
                if (this.j) {
                    i();
                } else {
                    o();
                }
            } catch (SecurityException e2) {
                this.i = e2.getMessage();
            } catch (Throwable th) {
                en.a(th, "CgiManager", "refresh");
            }
        }
        return;
    }

    /* access modifiers changed from: package-private */
    public final boolean a(CellLocation cellLocation) {
        if (cellLocation == null) {
            return false;
        }
        boolean z = true;
        switch (c(cellLocation)) {
            case 1:
                try {
                    GsmCellLocation gsmCellLocation = (GsmCellLocation) cellLocation;
                    z = a(gsmCellLocation.getLac(), gsmCellLocation.getCid());
                    break;
                } catch (Throwable th) {
                    en.a(th, "CgiManager", "cgiUseful Cgi.I_GSM_T");
                    break;
                }
            case 2:
                try {
                    if (eq.b(cellLocation, "getSystemId", new Object[0]) <= 0 || eq.b(cellLocation, "getNetworkId", new Object[0]) < 0 || eq.b(cellLocation, "getBaseStationId", new Object[0]) < 0) {
                        z = false;
                        break;
                    }
                } catch (Throwable th2) {
                    en.a(th2, "CgiManager", "cgiUseful Cgi.I_CDMA_T");
                    break;
                }
        }
        return z;
    }

    public final synchronized ArrayList<dz> b() {
        return this.b;
    }

    public final ArrayList<dz> c() {
        return this.n;
    }

    public final synchronized dz d() {
        dz dzVar = null;
        synchronized (this) {
            if (!this.j) {
                ArrayList<dz> arrayList = this.b;
                if (arrayList.size() > 0) {
                    dzVar = arrayList.get(0);
                }
            }
        }
        return dzVar;
    }

    public final int e() {
        return this.a;
    }

    public final int f() {
        return this.a & 3;
    }

    public final TelephonyManager g() {
        return this.d;
    }

    public final void h() {
        this.p.a();
        this.s = 0;
        synchronized (this.y) {
            this.x = true;
        }
        if (!(this.d == null || this.h == null)) {
            try {
                this.d.listen(this.h, 0);
            } catch (Throwable th) {
                en.a(th, "CgiManager", "destroy");
            }
        }
        this.h = null;
        this.o = -113;
        this.d = null;
        this.q = null;
    }

    /* access modifiers changed from: package-private */
    public final synchronized void i() {
        this.i = null;
        this.f = null;
        this.a = 0;
        this.b.clear();
        this.n.clear();
    }

    public final String j() {
        return this.i;
    }

    public final String k() {
        return this.m;
    }

    public final synchronized String l() {
        if (this.j) {
            i();
        }
        if (this.k == null) {
            this.k = new StringBuilder();
        } else {
            this.k.delete(0, this.k.length());
        }
        switch (this.a & 3) {
            case 1:
                int i2 = 1;
                while (true) {
                    int i3 = i2;
                    if (i3 >= this.b.size()) {
                        break;
                    } else {
                        this.k.append(Constant.INTENT_JSON_MARK).append(this.b.get(i3).b);
                        this.k.append("|").append(this.b.get(i3).c);
                        this.k.append("|").append(this.b.get(i3).d);
                        i2 = i3 + 1;
                    }
                }
        }
        if (this.k.length() > 0) {
            this.k.deleteCharAt(0);
        }
        return this.k.toString();
    }

    public final boolean m() {
        try {
            if (this.d != null && (!TextUtils.isEmpty(this.d.getSimOperator()) || !TextUtils.isEmpty(this.d.getSimCountryIso()))) {
                return true;
            }
        } catch (Throwable th) {
        }
        try {
            int a2 = et.a(et.c(this.l));
            return a2 == 0 || a2 == 4 || a2 == 2 || a2 == 5 || a2 == 3;
        } catch (Throwable th2) {
        }
    }
}
