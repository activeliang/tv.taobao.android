package com.alibaba.analytics.utils;

import android.text.TextUtils;
import java.util.Arrays;
import java.util.Comparator;

public class KeyArraySorter {
    private static KeyArraySorter s_instance = new KeyArraySorter();
    private ResourcesASCComparator mASCComparator = new ResourcesASCComparator();
    private ResourcesDESCComparator mDESCComparator = new ResourcesDESCComparator();

    private KeyArraySorter() {
    }

    public static KeyArraySorter getInstance() {
        return s_instance;
    }

    public String[] sortResourcesList(String[] resources, boolean aUseASC) {
        Comparator<String> lCompare;
        if (aUseASC) {
            lCompare = this.mASCComparator;
        } else {
            lCompare = this.mDESCComparator;
        }
        if (lCompare == null || resources == null || resources.length <= 0) {
            return null;
        }
        Arrays.sort(resources, lCompare);
        return resources;
    }

    private class ResourcesDESCComparator implements Comparator<String> {
        private ResourcesDESCComparator() {
        }

        public int compare(String o1, String o2) {
            if (TextUtils.isEmpty(o1) || TextUtils.isEmpty(o2)) {
                return 0;
            }
            return o1.compareTo(o2) * -1;
        }
    }

    private class ResourcesASCComparator implements Comparator<String> {
        private ResourcesASCComparator() {
        }

        public int compare(String o1, String o2) {
            if (TextUtils.isEmpty(o1) || TextUtils.isEmpty(o2)) {
                return 0;
            }
            return o1.compareTo(o2);
        }
    }
}
