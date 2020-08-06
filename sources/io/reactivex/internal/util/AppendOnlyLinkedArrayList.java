package io.reactivex.internal.util;

import io.reactivex.Observer;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Predicate;
import org.reactivestreams.Subscriber;

public class AppendOnlyLinkedArrayList<T> {
    final int capacity;
    final Object[] head;
    int offset;
    Object[] tail = this.head;

    public interface NonThrowingPredicate<T> extends Predicate<T> {
        boolean test(T t);
    }

    public AppendOnlyLinkedArrayList(int capacity2) {
        this.capacity = capacity2;
        this.head = new Object[(capacity2 + 1)];
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: java.lang.Object[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void add(T r5) {
        /*
            r4 = this;
            int r0 = r4.capacity
            int r2 = r4.offset
            if (r2 != r0) goto L_0x0011
            int r3 = r0 + 1
            java.lang.Object[] r1 = new java.lang.Object[r3]
            java.lang.Object[] r3 = r4.tail
            r3[r0] = r1
            r4.tail = r1
            r2 = 0
        L_0x0011:
            java.lang.Object[] r3 = r4.tail
            r3[r2] = r5
            int r3 = r2 + 1
            r4.offset = r3
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: io.reactivex.internal.util.AppendOnlyLinkedArrayList.add(java.lang.Object):void");
    }

    public void setFirst(T value) {
        this.head[0] = value;
    }

    public void forEachWhile(NonThrowingPredicate<? super T> consumer) {
        int c = this.capacity;
        for (Object[] a = this.head; a != null; a = (Object[]) a[c]) {
            for (int i = 0; i < c; i++) {
                Object o = a[i];
                if (o == null || consumer.test(o)) {
                    break;
                }
            }
        }
    }

    public <U> boolean accept(Subscriber<? super U> subscriber) {
        int c = this.capacity;
        for (Object[] a = this.head; a != null; a = (Object[]) a[c]) {
            int i = 0;
            while (i < c) {
                Object o = a[i];
                if (o == null) {
                    continue;
                    break;
                } else if (NotificationLite.acceptFull(o, subscriber)) {
                    return true;
                } else {
                    i++;
                }
            }
        }
        return false;
    }

    public <U> boolean accept(Observer<? super U> observer) {
        int c = this.capacity;
        for (Object[] a = this.head; a != null; a = (Object[]) a[c]) {
            int i = 0;
            while (i < c) {
                Object o = a[i];
                if (o == null) {
                    continue;
                    break;
                } else if (NotificationLite.acceptFull(o, observer)) {
                    return true;
                } else {
                    i++;
                }
            }
        }
        return false;
    }

    public <S> void forEachWhile(S state, BiPredicate<? super S, ? super T> consumer) throws Exception {
        Object[] a = this.head;
        int c = this.capacity;
        while (true) {
            int i = 0;
            while (i < c) {
                Object o = a[i];
                if (o != null && !consumer.test(state, o)) {
                    i++;
                } else {
                    return;
                }
            }
            a = (Object[]) a[c];
        }
    }
}
