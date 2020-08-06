package com.taobao.atlas.dexmerge.dx.util;

import java.util.Arrays;

public final class IntList extends MutabilityControl {
    public static final IntList EMPTY = new IntList(0);
    private int size;
    private boolean sorted;
    private int[] values;

    static {
        EMPTY.setImmutable();
    }

    public static IntList makeImmutable(int value) {
        IntList result = new IntList(1);
        result.add(value);
        result.setImmutable();
        return result;
    }

    public static IntList makeImmutable(int value0, int value1) {
        IntList result = new IntList(2);
        result.add(value0);
        result.add(value1);
        result.setImmutable();
        return result;
    }

    public IntList() {
        this(4);
    }

    public IntList(int initialCapacity) {
        super(true);
        try {
            this.values = new int[initialCapacity];
            this.size = 0;
            this.sorted = true;
        } catch (NegativeArraySizeException e) {
            throw new IllegalArgumentException("size < 0");
        }
    }

    public int hashCode() {
        int result = 0;
        for (int i = 0; i < this.size; i++) {
            result = (result * 31) + this.values[i];
        }
        return result;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof IntList)) {
            return false;
        }
        IntList otherList = (IntList) other;
        if (this.sorted != otherList.sorted) {
            return false;
        }
        if (this.size != otherList.size) {
            return false;
        }
        for (int i = 0; i < this.size; i++) {
            if (this.values[i] != otherList.values[i]) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer((this.size * 5) + 10);
        sb.append('{');
        for (int i = 0; i < this.size; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(this.values[i]);
        }
        sb.append('}');
        return sb.toString();
    }

    public int size() {
        return this.size;
    }

    public int get(int n) {
        if (n >= this.size) {
            throw new IndexOutOfBoundsException("n >= size()");
        }
        try {
            return this.values[n];
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("n < 0");
        }
    }

    public void set(int n, int value) {
        throwIfImmutable();
        if (n >= this.size) {
            throw new IndexOutOfBoundsException("n >= size()");
        }
        try {
            this.values[n] = value;
            this.sorted = false;
        } catch (ArrayIndexOutOfBoundsException e) {
            if (n < 0) {
                throw new IllegalArgumentException("n < 0");
            }
        }
    }

    public void add(int value) {
        boolean z = true;
        throwIfImmutable();
        growIfNeeded();
        int[] iArr = this.values;
        int i = this.size;
        this.size = i + 1;
        iArr[i] = value;
        if (this.sorted && this.size > 1) {
            if (value < this.values[this.size - 2]) {
                z = false;
            }
            this.sorted = z;
        }
    }

    public void insert(int n, int value) {
        throwIfImmutable();
        if (n > this.size) {
            throw new IndexOutOfBoundsException("n > size()");
        }
        growIfNeeded();
        System.arraycopy(this.values, n, this.values, n + 1, this.size - n);
        this.values[n] = value;
        this.size++;
        this.sorted = this.sorted && (n == 0 || value > this.values[n + -1]) && (n == this.size + -1 || value < this.values[n + 1]);
    }

    public void removeIndex(int n) {
        throwIfImmutable();
        if (n >= this.size) {
            throw new IndexOutOfBoundsException("n >= size()");
        }
        System.arraycopy(this.values, n + 1, this.values, n, (this.size - n) - 1);
        this.size--;
    }

    private void growIfNeeded() {
        if (this.size == this.values.length) {
            int[] newv = new int[(((this.size * 3) / 2) + 10)];
            System.arraycopy(this.values, 0, newv, 0, this.size);
            this.values = newv;
        }
    }

    public int top() {
        return get(this.size - 1);
    }

    public int pop() {
        throwIfImmutable();
        this.size--;
        return get(this.size - 1);
    }

    public void pop(int n) {
        throwIfImmutable();
        this.size -= n;
    }

    public void shrink(int newSize) {
        if (newSize < 0) {
            throw new IllegalArgumentException("newSize < 0");
        } else if (newSize > this.size) {
            throw new IllegalArgumentException("newSize > size");
        } else {
            throwIfImmutable();
            this.size = newSize;
        }
    }

    public IntList mutableCopy() {
        int sz = this.size;
        IntList result = new IntList(sz);
        for (int i = 0; i < sz; i++) {
            result.add(this.values[i]);
        }
        return result;
    }

    public void sort() {
        throwIfImmutable();
        if (!this.sorted) {
            Arrays.sort(this.values, 0, this.size);
            this.sorted = true;
        }
    }

    public int indexOf(int value) {
        int ret = binarysearch(value);
        if (ret >= 0) {
            return ret;
        }
        return -1;
    }

    public int binarysearch(int value) {
        int sz = this.size;
        if (!this.sorted) {
            for (int i = 0; i < sz; i++) {
                if (this.values[i] == value) {
                    return i;
                }
            }
            return -sz;
        }
        int min = -1;
        int max = sz;
        while (max > min + 1) {
            int guessIdx = min + ((max - min) >> 1);
            if (value <= this.values[guessIdx]) {
                max = guessIdx;
            } else {
                min = guessIdx;
            }
        }
        if (max == sz) {
            return (-sz) - 1;
        }
        if (value != this.values[max]) {
            max = (-max) - 1;
        }
        return max;
    }

    public boolean contains(int value) {
        return indexOf(value) >= 0;
    }
}
