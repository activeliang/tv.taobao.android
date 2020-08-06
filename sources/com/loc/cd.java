package com.loc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/* compiled from: WifiCollector */
public final class cd {
    private List<dh> a = new ArrayList();
    private dg b;
    private ArrayList<dh> c = new ArrayList<>();

    private void a(List<dh> list, List<dh> list2) {
        list.clear();
        if (list2 != null) {
            ArrayList arrayList = new ArrayList();
            HashMap hashMap = new HashMap();
            for (int i = 0; i < list2.size(); i++) {
                dh dhVar = list2.get(i);
                hashMap.put(Integer.valueOf(dhVar.c), dhVar);
            }
            arrayList.addAll(hashMap.values());
            Collections.sort(arrayList, new Comparator<dh>() {
                public final /* bridge */ /* synthetic */ int compare(Object obj, Object obj2) {
                    return ((dh) obj2).c - ((dh) obj).c;
                }
            });
            int size = arrayList.size();
            if (size > 40) {
                size = 40;
            }
            for (int i2 = 0; i2 < size; i2++) {
                list.add(arrayList.get(i2));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final List<dh> a(dg dgVar, List<dh> list, boolean z, long j, long j2) {
        boolean z2;
        boolean z3;
        List<dh> list2;
        if (!z) {
            z2 = false;
        } else {
            boolean z4 = false;
            int i = 3500;
            if (dgVar.g >= 10.0f) {
                i = 2000;
            }
            if (j > 0) {
                z4 = j2 - j < ((long) i);
            }
            if (!z4 || list == null || list.size() <= 0) {
                z2 = false;
            } else if (this.b != null) {
                z2 = dgVar.a(this.b) > ((double) ((dgVar.g > 10.0f ? 1 : (dgVar.g == 10.0f ? 0 : -1)) > 0 ? 200.0f : (dgVar.g > 2.0f ? 1 : (dgVar.g == 2.0f ? 0 : -1)) > 0 ? 50.0f : 10.0f));
                if (!z2) {
                    List<dh> list3 = this.a;
                    if (list == null || list3 == null) {
                        z3 = false;
                    } else {
                        int size = list.size();
                        int size2 = list3.size();
                        int i2 = size + size2;
                        if (size > size2) {
                            list2 = list3;
                            list3 = list;
                        } else {
                            list2 = list;
                        }
                        HashMap hashMap = new HashMap(list3.size());
                        for (dh dhVar : list3) {
                            hashMap.put(Long.valueOf(dhVar.a), 1);
                        }
                        int i3 = 0;
                        for (dh dhVar2 : list2) {
                            i3 = ((Integer) hashMap.get(Long.valueOf(dhVar2.a))) != null ? i3 + 1 : i3;
                        }
                        z3 = ((double) i3) * 2.0d >= ((double) i2) * 0.5d;
                    }
                    z2 = !z3;
                }
            } else {
                z2 = true;
            }
        }
        if (!z2) {
            return null;
        }
        a(this.c, list);
        this.a.clear();
        this.a.addAll(list);
        this.b = dgVar;
        return this.c;
    }
}
