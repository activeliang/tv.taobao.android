package com.loc;

import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: CellAgeEstimator */
public final class dy {
    private HashMap<Long, dz> a = new HashMap<>();
    private long b = 0;

    private static long a(int i, int i2) {
        return ((((long) i) & 65535) << 32) | (((long) i2) & 65535);
    }

    public final long a(dz dzVar) {
        long a2;
        if (dzVar == null || !dzVar.p) {
            return 0;
        }
        HashMap<Long, dz> hashMap = this.a;
        switch (dzVar.k) {
            case 1:
            case 3:
            case 4:
                a2 = a(dzVar.c, dzVar.d);
                break;
            case 2:
                a2 = a(dzVar.h, dzVar.i);
                break;
            default:
                a2 = 0;
                break;
        }
        dz dzVar2 = hashMap.get(Long.valueOf(a2));
        if (dzVar2 == null) {
            dzVar.m = et.b();
            hashMap.put(Long.valueOf(a2), dzVar);
            return 0;
        } else if (dzVar2.j != dzVar.j) {
            dzVar.m = et.b();
            hashMap.put(Long.valueOf(a2), dzVar);
            return 0;
        } else {
            dzVar.m = dzVar2.m;
            hashMap.put(Long.valueOf(a2), dzVar);
            return (et.b() - dzVar2.m) / 1000;
        }
    }

    public final void a() {
        this.a.clear();
        this.b = 0;
    }

    public final void a(ArrayList<? extends dz> arrayList) {
        long j = 0;
        if (arrayList != null) {
            long b2 = et.b();
            if (this.b <= 0 || b2 - this.b >= 60000) {
                HashMap<Long, dz> hashMap = this.a;
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    dz dzVar = (dz) arrayList.get(i);
                    if (dzVar.p) {
                        switch (dzVar.k) {
                            case 1:
                            case 3:
                            case 4:
                                j = a(dzVar.c, dzVar.d);
                                break;
                            case 2:
                                j = a(dzVar.h, dzVar.i);
                                break;
                        }
                        dz dzVar2 = hashMap.get(Long.valueOf(j));
                        if (dzVar2 != null) {
                            if (dzVar2.j == dzVar.j) {
                                dzVar.m = dzVar2.m;
                            } else {
                                dzVar.m = b2;
                            }
                        }
                    }
                }
                hashMap.clear();
                int size2 = arrayList.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    dz dzVar3 = (dz) arrayList.get(i2);
                    if (dzVar3.p) {
                        switch (dzVar3.k) {
                            case 1:
                            case 3:
                            case 4:
                                j = a(dzVar3.c, dzVar3.d);
                                break;
                            case 2:
                                j = a(dzVar3.h, dzVar3.i);
                                break;
                        }
                        hashMap.put(Long.valueOf(j), dzVar3);
                    }
                }
                this.b = b2;
            }
        }
    }
}
