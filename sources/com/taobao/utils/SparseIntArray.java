package com.taobao.utils;

public class SparseIntArray implements Cloneable {
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private int[] mKeys;
    private int mSize;
    private int[] mValues;

    public SparseIntArray() {
        this(10);
    }

    public SparseIntArray(int initialCapacity) {
        if (initialCapacity == 0) {
            this.mKeys = EMPTY_INT_ARRAY;
            this.mValues = EMPTY_INT_ARRAY;
        } else {
            this.mKeys = new int[initialCapacity];
            this.mValues = new int[this.mKeys.length];
        }
        this.mSize = 0;
    }

    public static int growSize(int currentSize) {
        if (currentSize <= 4) {
            return 8;
        }
        return (currentSize >> 1) + currentSize;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: com.taobao.utils.SparseIntArray} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.taobao.utils.SparseIntArray clone() {
        /*
            r3 = this;
            r1 = 0
            java.lang.Object r2 = super.clone()     // Catch:{ CloneNotSupportedException -> 0x001e }
            r0 = r2
            com.taobao.utils.SparseIntArray r0 = (com.taobao.utils.SparseIntArray) r0     // Catch:{ CloneNotSupportedException -> 0x001e }
            r1 = r0
            int[] r2 = r3.mKeys     // Catch:{ CloneNotSupportedException -> 0x001e }
            java.lang.Object r2 = r2.clone()     // Catch:{ CloneNotSupportedException -> 0x001e }
            int[] r2 = (int[]) r2     // Catch:{ CloneNotSupportedException -> 0x001e }
            r1.mKeys = r2     // Catch:{ CloneNotSupportedException -> 0x001e }
            int[] r2 = r3.mValues     // Catch:{ CloneNotSupportedException -> 0x001e }
            java.lang.Object r2 = r2.clone()     // Catch:{ CloneNotSupportedException -> 0x001e }
            int[] r2 = (int[]) r2     // Catch:{ CloneNotSupportedException -> 0x001e }
            r1.mValues = r2     // Catch:{ CloneNotSupportedException -> 0x001e }
        L_0x001d:
            return r1
        L_0x001e:
            r2 = move-exception
            goto L_0x001d
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.utils.SparseIntArray.clone():com.taobao.utils.SparseIntArray");
    }

    public int get(int key) {
        return get(key, 0);
    }

    public int get(int key, int valueIfKeyNotFound) {
        int i = binarySearch(this.mKeys, this.mSize, key);
        return i < 0 ? valueIfKeyNotFound : this.mValues[i];
    }

    public void delete(int key) {
        int i = binarySearch(this.mKeys, this.mSize, key);
        if (i >= 0) {
            removeAt(i);
        }
    }

    public void removeAt(int index) {
        System.arraycopy(this.mKeys, index + 1, this.mKeys, index, this.mSize - (index + 1));
        System.arraycopy(this.mValues, index + 1, this.mValues, index, this.mSize - (index + 1));
        this.mSize--;
    }

    public void put(int key, int value) {
        int i = binarySearch(this.mKeys, this.mSize, key);
        if (i >= 0) {
            this.mValues[i] = value;
            return;
        }
        int i2 = i ^ -1;
        this.mKeys = insertElementIntoIntArray(this.mKeys, this.mSize, i2, key);
        this.mValues = insertElementIntoIntArray(this.mValues, this.mSize, i2, value);
        this.mSize++;
    }

    public int size() {
        return this.mSize;
    }

    public int keyAt(int index) {
        return this.mKeys[index];
    }

    public int valueAt(int index) {
        return this.mValues[index];
    }

    public int indexOfKey(int key) {
        return binarySearch(this.mKeys, this.mSize, key);
    }

    public int indexOfValue(int value) {
        for (int i = 0; i < this.mSize; i++) {
            if (this.mValues[i] == value) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        this.mSize = 0;
    }

    public void append(int key, int value) {
        if (this.mSize == 0 || key > this.mKeys[this.mSize - 1]) {
            this.mKeys = appendElementIntoIntArray(this.mKeys, this.mSize, key);
            this.mValues = appendElementIntoIntArray(this.mValues, this.mSize, value);
            this.mSize++;
            return;
        }
        put(key, value);
    }

    private int binarySearch(int[] array, int size, int value) {
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

    private int[] appendElementIntoIntArray(int[] array, int currentSize, int element) {
        if (currentSize > array.length) {
            throw new IllegalArgumentException("Bad currentSize, originalSize: " + array.length + " currentSize: " + currentSize);
        }
        if (currentSize + 1 > array.length) {
            int[] newArray = new int[growSize(currentSize)];
            System.arraycopy(array, 0, newArray, 0, currentSize);
            array = newArray;
        }
        array[currentSize] = element;
        return array;
    }

    private int[] insertElementIntoIntArray(int[] array, int currentSize, int index, int element) {
        if (currentSize > array.length) {
            throw new IllegalArgumentException("Bad currentSize, originalSize: " + array.length + " currentSize: " + currentSize);
        } else if (currentSize + 1 <= array.length) {
            System.arraycopy(array, index, array, index + 1, currentSize - index);
            array[index] = element;
            return array;
        } else {
            int[] newArray = new int[growSize(currentSize)];
            System.arraycopy(array, 0, newArray, 0, index);
            newArray[index] = element;
            System.arraycopy(array, index, newArray, index + 1, array.length - index);
            return newArray;
        }
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
            buffer.append(valueAt(i));
        }
        buffer.append('}');
        return buffer.toString();
    }
}
