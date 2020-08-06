package android.support.v7.widget;

import java.util.ArrayList;

class PositionMap<E> implements Cloneable {
    private static final Object DELETED = new Object();
    private boolean mGarbage;
    private int[] mKeys;
    private int mSize;
    private Object[] mValues;

    public PositionMap() {
        this(10);
    }

    public PositionMap(int initialCapacity) {
        this.mGarbage = false;
        if (initialCapacity == 0) {
            this.mKeys = ContainerHelpers.EMPTY_INTS;
            this.mValues = ContainerHelpers.EMPTY_OBJECTS;
        } else {
            int initialCapacity2 = idealIntArraySize(initialCapacity);
            this.mKeys = new int[initialCapacity2];
            this.mValues = new Object[initialCapacity2];
        }
        this.mSize = 0;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: android.support.v7.widget.PositionMap<E>} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.support.v7.widget.PositionMap<E> clone() {
        /*
            r3 = this;
            r1 = 0
            java.lang.Object r2 = super.clone()     // Catch:{ CloneNotSupportedException -> 0x001e }
            r0 = r2
            android.support.v7.widget.PositionMap r0 = (android.support.v7.widget.PositionMap) r0     // Catch:{ CloneNotSupportedException -> 0x001e }
            r1 = r0
            int[] r2 = r3.mKeys     // Catch:{ CloneNotSupportedException -> 0x001e }
            java.lang.Object r2 = r2.clone()     // Catch:{ CloneNotSupportedException -> 0x001e }
            int[] r2 = (int[]) r2     // Catch:{ CloneNotSupportedException -> 0x001e }
            r1.mKeys = r2     // Catch:{ CloneNotSupportedException -> 0x001e }
            java.lang.Object[] r2 = r3.mValues     // Catch:{ CloneNotSupportedException -> 0x001e }
            java.lang.Object r2 = r2.clone()     // Catch:{ CloneNotSupportedException -> 0x001e }
            java.lang.Object[] r2 = (java.lang.Object[]) r2     // Catch:{ CloneNotSupportedException -> 0x001e }
            r1.mValues = r2     // Catch:{ CloneNotSupportedException -> 0x001e }
        L_0x001d:
            return r1
        L_0x001e:
            r2 = move-exception
            goto L_0x001d
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.PositionMap.clone():android.support.v7.widget.PositionMap");
    }

    public E get(int key) {
        return get(key, (Object) null);
    }

    public E get(int key, E valueIfKeyNotFound) {
        int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        return (i < 0 || this.mValues[i] == DELETED) ? valueIfKeyNotFound : this.mValues[i];
    }

    public void delete(int key) {
        int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if (i >= 0 && this.mValues[i] != DELETED) {
            this.mValues[i] = DELETED;
            this.mGarbage = true;
        }
    }

    public void remove(int key) {
        delete(key);
    }

    public void removeAt(int index) {
        if (this.mValues[index] != DELETED) {
            this.mValues[index] = DELETED;
            this.mGarbage = true;
        }
    }

    public void removeAtRange(int index, int size) {
        int end = Math.min(this.mSize, index + size);
        for (int i = index; i < end; i++) {
            removeAt(i);
        }
    }

    public void insertKeyRange(int keyStart, int count) {
    }

    public void removeKeyRange(ArrayList<E> arrayList, int keyStart, int count) {
    }

    private void gc() {
        int n = this.mSize;
        int o = 0;
        int[] keys = this.mKeys;
        Object[] values = this.mValues;
        for (int i = 0; i < n; i++) {
            Object val = values[i];
            if (val != DELETED) {
                if (i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                    values[i] = null;
                }
                o++;
            }
        }
        this.mGarbage = false;
        this.mSize = o;
    }

    public void put(int key, E value) {
        int i = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
        if (i >= 0) {
            this.mValues[i] = value;
            return;
        }
        int i2 = i ^ -1;
        if (i2 >= this.mSize || this.mValues[i2] != DELETED) {
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                gc();
                i2 = ContainerHelpers.binarySearch(this.mKeys, this.mSize, key) ^ -1;
            }
            if (this.mSize >= this.mKeys.length) {
                int n = idealIntArraySize(this.mSize + 1);
                int[] nkeys = new int[n];
                Object[] nvalues = new Object[n];
                System.arraycopy(this.mKeys, 0, nkeys, 0, this.mKeys.length);
                System.arraycopy(this.mValues, 0, nvalues, 0, this.mValues.length);
                this.mKeys = nkeys;
                this.mValues = nvalues;
            }
            if (this.mSize - i2 != 0) {
                System.arraycopy(this.mKeys, i2, this.mKeys, i2 + 1, this.mSize - i2);
                System.arraycopy(this.mValues, i2, this.mValues, i2 + 1, this.mSize - i2);
            }
            this.mKeys[i2] = key;
            this.mValues[i2] = value;
            this.mSize++;
            return;
        }
        this.mKeys[i2] = key;
        this.mValues[i2] = value;
    }

    public int size() {
        if (this.mGarbage) {
            gc();
        }
        return this.mSize;
    }

    public int keyAt(int index) {
        if (this.mGarbage) {
            gc();
        }
        return this.mKeys[index];
    }

    public E valueAt(int index) {
        if (this.mGarbage) {
            gc();
        }
        return this.mValues[index];
    }

    public void setValueAt(int index, E value) {
        if (this.mGarbage) {
            gc();
        }
        this.mValues[index] = value;
    }

    public int indexOfKey(int key) {
        if (this.mGarbage) {
            gc();
        }
        return ContainerHelpers.binarySearch(this.mKeys, this.mSize, key);
    }

    public int indexOfValue(E value) {
        if (this.mGarbage) {
            gc();
        }
        for (int i = 0; i < this.mSize; i++) {
            if (this.mValues[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        int n = this.mSize;
        Object[] values = this.mValues;
        for (int i = 0; i < n; i++) {
            values[i] = null;
        }
        this.mSize = 0;
        this.mGarbage = false;
    }

    public void append(int key, E value) {
        if (this.mSize == 0 || key > this.mKeys[this.mSize - 1]) {
            if (this.mGarbage && this.mSize >= this.mKeys.length) {
                gc();
            }
            int pos = this.mSize;
            if (pos >= this.mKeys.length) {
                int n = idealIntArraySize(pos + 1);
                int[] nkeys = new int[n];
                Object[] nvalues = new Object[n];
                System.arraycopy(this.mKeys, 0, nkeys, 0, this.mKeys.length);
                System.arraycopy(this.mValues, 0, nvalues, 0, this.mValues.length);
                this.mKeys = nkeys;
                this.mValues = nvalues;
            }
            this.mKeys[pos] = key;
            this.mValues[pos] = value;
            this.mSize = pos + 1;
            return;
        }
        put(key, value);
    }

    public String toString() {
        if (size() <= 0) {
            return "{}";
        }
        StringBuilder buffer = new StringBuilder(this.mSize * 28);
        buffer.append('{');
        for (int i = 0; i < this.mSize; i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(keyAt(i));
            buffer.append('=');
            Object value = valueAt(i);
            if (value != this) {
                buffer.append(value);
            } else {
                buffer.append("(this Map)");
            }
        }
        buffer.append('}');
        return buffer.toString();
    }

    static int idealByteArraySize(int need) {
        for (int i = 4; i < 32; i++) {
            if (need <= (1 << i) - 12) {
                return (1 << i) - 12;
            }
        }
        return need;
    }

    static int idealBooleanArraySize(int need) {
        return idealByteArraySize(need);
    }

    static int idealShortArraySize(int need) {
        return idealByteArraySize(need * 2) / 2;
    }

    static int idealCharArraySize(int need) {
        return idealByteArraySize(need * 2) / 2;
    }

    static int idealIntArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    static int idealFloatArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    static int idealObjectArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    static int idealLongArraySize(int need) {
        return idealByteArraySize(need * 8) / 8;
    }

    static class ContainerHelpers {
        static final boolean[] EMPTY_BOOLEANS = new boolean[0];
        static final int[] EMPTY_INTS = new int[0];
        static final long[] EMPTY_LONGS = new long[0];
        static final Object[] EMPTY_OBJECTS = new Object[0];

        ContainerHelpers() {
        }

        static int binarySearch(int[] array, int size, int value) {
            int lo = 0;
            int hi = size - 1;
            while (lo <= hi) {
                int mid = (lo + hi) >>> 1;
                int midVal = array[mid];
                if (midVal < value) {
                    lo = mid + 1;
                } else if (midVal <= value) {
                    return mid;
                } else {
                    hi = mid - 1;
                }
            }
            return lo ^ -1;
        }
    }
}
