package io.reactivex.internal.util;

import java.util.ArrayList;
import java.util.List;

public class LinkedArrayList {
    final int capacityHint;
    Object[] head;
    int indexInTail;
    volatile int size;
    Object[] tail;

    public LinkedArrayList(int capacityHint2) {
        this.capacityHint = capacityHint2;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r1v9, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void add(java.lang.Object r6) {
        /*
            r5 = this;
            r4 = 0
            r3 = 1
            int r1 = r5.size
            if (r1 != 0) goto L_0x001b
            int r1 = r5.capacityHint
            int r1 = r1 + 1
            java.lang.Object[] r1 = new java.lang.Object[r1]
            r5.head = r1
            java.lang.Object[] r1 = r5.head
            r5.tail = r1
            java.lang.Object[] r1 = r5.head
            r1[r4] = r6
            r5.indexInTail = r3
            r5.size = r3
        L_0x001a:
            return
        L_0x001b:
            int r1 = r5.indexInTail
            int r2 = r5.capacityHint
            if (r1 != r2) goto L_0x003a
            int r1 = r5.capacityHint
            int r1 = r1 + 1
            java.lang.Object[] r0 = new java.lang.Object[r1]
            r0[r4] = r6
            java.lang.Object[] r1 = r5.tail
            int r2 = r5.capacityHint
            r1[r2] = r0
            r5.tail = r0
            r5.indexInTail = r3
            int r1 = r5.size
            int r1 = r1 + 1
            r5.size = r1
            goto L_0x001a
        L_0x003a:
            java.lang.Object[] r1 = r5.tail
            int r2 = r5.indexInTail
            r1[r2] = r6
            int r1 = r5.indexInTail
            int r1 = r1 + 1
            r5.indexInTail = r1
            int r1 = r5.size
            int r1 = r1 + 1
            r5.size = r1
            goto L_0x001a
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.util.LinkedArrayList.add(java.lang.Object):void");
    }

    public Object[] head() {
        return this.head;
    }

    public int size() {
        return this.size;
    }

    public String toString() {
        int cap = this.capacityHint;
        int s = this.size;
        List<Object> list = new ArrayList<>(s + 1);
        Object[] h = head();
        int j = 0;
        int k = 0;
        while (j < s) {
            list.add(h[k]);
            j++;
            k++;
            if (k == cap) {
                k = 0;
                h = (Object[]) h[cap];
            }
        }
        return list.toString();
    }
}
