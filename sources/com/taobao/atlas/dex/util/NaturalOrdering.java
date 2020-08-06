package com.taobao.atlas.dex.util;

import java.util.Comparator;

public class NaturalOrdering implements Comparator<Comparable> {
    public int compare(Comparable left, Comparable right) {
        return left.compareTo(right);
    }
}
