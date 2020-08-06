package com.taobao.atlas.dexmerge.dx.util;

public final class Bits {
    private Bits() {
    }

    public static int[] makeBitSet(int max) {
        return new int[((max + 31) >> 5)];
    }

    public static int getMax(int[] bits) {
        return bits.length * 32;
    }

    public static boolean get(int[] bits, int idx) {
        if ((bits[idx >> 5] & (1 << (idx & 31))) != 0) {
            return true;
        }
        return false;
    }

    public static void set(int[] bits, int idx, boolean value) {
        int arrayIdx = idx >> 5;
        int bit = 1 << (idx & 31);
        if (value) {
            bits[arrayIdx] = bits[arrayIdx] | bit;
        } else {
            bits[arrayIdx] = bits[arrayIdx] & (bit ^ -1);
        }
    }

    public static void set(int[] bits, int idx) {
        int arrayIdx = idx >> 5;
        bits[arrayIdx] = bits[arrayIdx] | (1 << (idx & 31));
    }

    public static void clear(int[] bits, int idx) {
        int arrayIdx = idx >> 5;
        bits[arrayIdx] = bits[arrayIdx] & ((1 << (idx & 31)) ^ -1);
    }

    public static boolean isEmpty(int[] bits) {
        for (int i : bits) {
            if (i != 0) {
                return false;
            }
        }
        return true;
    }

    public static int bitCount(int[] bits) {
        int count = 0;
        for (int bitCount : bits) {
            count += Integer.bitCount(bitCount);
        }
        return count;
    }

    public static boolean anyInRange(int[] bits, int start, int end) {
        int idx = findFirst(bits, start);
        return idx >= 0 && idx < end;
    }

    public static int findFirst(int[] bits, int idx) {
        int bitIdx;
        int len = bits.length;
        int minBit = idx & 31;
        for (int arrayIdx = idx >> 5; arrayIdx < len; arrayIdx++) {
            int word = bits[arrayIdx];
            if (word != 0 && (bitIdx = findFirst(word, minBit)) >= 0) {
                return (arrayIdx << 5) + bitIdx;
            }
            minBit = 0;
        }
        return -1;
    }

    public static int findFirst(int value, int idx) {
        int result = Integer.numberOfTrailingZeros(value & (((1 << idx) - 1) ^ -1));
        if (result == 32) {
            return -1;
        }
        return result;
    }

    public static void or(int[] a, int[] b) {
        for (int i = 0; i < b.length; i++) {
            a[i] = a[i] | b[i];
        }
    }

    public static String toHuman(int[] bits) {
        StringBuilder sb = new StringBuilder();
        boolean needsComma = false;
        sb.append('{');
        int bitsLength = bits.length * 32;
        for (int i = 0; i < bitsLength; i++) {
            if (get(bits, i)) {
                if (needsComma) {
                    sb.append(',');
                }
                needsComma = true;
                sb.append(i);
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
