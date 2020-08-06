package com.taobao.atlas.dexmerge.dx.util;

public interface IntSet {
    void add(int i);

    int elements();

    boolean has(int i);

    IntIterator iterator();

    void merge(IntSet intSet);

    void remove(int i);
}
