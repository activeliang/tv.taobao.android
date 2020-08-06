package com.alibaba.motu.crashreporter.utrestapi;

import com.alibaba.motu.crashreporter.utils.StringUtils;
import java.util.Arrays;
import java.util.Comparator;

public class UTKeyArraySorter {
    private static UTKeyArraySorter s_instance = null;
    private ResourcesASCComparator mASCComparator = new ResourcesASCComparator();
    private ResourcesDESCComparator mDESCComparator = new ResourcesDESCComparator();

    private UTKeyArraySorter() {
    }

    public static synchronized UTKeyArraySorter getInstance() {
        UTKeyArraySorter uTKeyArraySorter;
        synchronized (UTKeyArraySorter.class) {
            if (s_instance == null) {
                s_instance = new UTKeyArraySorter();
            }
            uTKeyArraySorter = s_instance;
        }
        return uTKeyArraySorter;
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
            if (StringUtils.isEmpty(o1) || StringUtils.isEmpty(o2)) {
                return 0;
            }
            return o1.compareTo(o2) * -1;
        }
    }

    private class ResourcesASCComparator implements Comparator<String> {
        private ResourcesASCComparator() {
        }

        public int compare(String o1, String o2) {
            if (StringUtils.isEmpty(o1) || StringUtils.isEmpty(o2)) {
                return 0;
            }
            return o1.compareTo(o2);
        }
    }
}
