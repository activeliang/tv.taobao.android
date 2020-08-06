package com.taobao.atlas.dexmerge.dx.util;

import java.util.NoSuchElementException;

public class BitIntSet implements IntSet {
    int[] bits;

    public BitIntSet(int max) {
        this.bits = Bits.makeBitSet(max);
    }

    public void add(int value) {
        ensureCapacity(value);
        Bits.set(this.bits, value, true);
    }

    private void ensureCapacity(int value) {
        if (value >= Bits.getMax(this.bits)) {
            int[] newBits = Bits.makeBitSet(Math.max(value + 1, Bits.getMax(this.bits) * 2));
            System.arraycopy(this.bits, 0, newBits, 0, this.bits.length);
            this.bits = newBits;
        }
    }

    public void remove(int value) {
        if (value < Bits.getMax(this.bits)) {
            Bits.set(this.bits, value, false);
        }
    }

    public boolean has(int value) {
        return value < Bits.getMax(this.bits) && Bits.get(this.bits, value);
    }

    public void merge(IntSet other) {
        if (other instanceof BitIntSet) {
            BitIntSet o = (BitIntSet) other;
            ensureCapacity(Bits.getMax(o.bits) + 1);
            Bits.or(this.bits, o.bits);
        } else if (other instanceof ListIntSet) {
            ListIntSet o2 = (ListIntSet) other;
            int sz = o2.ints.size();
            if (sz > 0) {
                ensureCapacity(o2.ints.get(sz - 1));
            }
            for (int i = 0; i < o2.ints.size(); i++) {
                Bits.set(this.bits, o2.ints.get(i), true);
            }
        } else {
            IntIterator iter = other.iterator();
            while (iter.hasNext()) {
                add(iter.next());
            }
        }
    }

    public int elements() {
        return Bits.bitCount(this.bits);
    }

    public IntIterator iterator() {
        return new IntIterator() {
            private int idx = Bits.findFirst(BitIntSet.this.bits, 0);

            public boolean hasNext() {
                return this.idx >= 0;
            }

            public int next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                int ret = this.idx;
                this.idx = Bits.findFirst(BitIntSet.this.bits, this.idx + 1);
                return ret;
            }
        };
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        boolean first = true;
        int i = Bits.findFirst(this.bits, 0);
        while (i >= 0) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(i);
            i = Bits.findFirst(this.bits, i + 1);
        }
        sb.append('}');
        return sb.toString();
    }
}
