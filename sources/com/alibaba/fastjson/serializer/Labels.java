package com.alibaba.fastjson.serializer;

import java.util.Arrays;

public class Labels {

    private static class DefaultLabelFilter implements LabelFilter {
        private String[] excludes;
        private String[] includes;

        public DefaultLabelFilter(String[] includes2, String[] excludes2) {
            if (includes2 != null) {
                this.includes = new String[includes2.length];
                System.arraycopy(includes2, 0, this.includes, 0, includes2.length);
                Arrays.sort(this.includes);
            }
            if (excludes2 != null) {
                this.excludes = new String[excludes2.length];
                System.arraycopy(excludes2, 0, this.excludes, 0, excludes2.length);
                Arrays.sort(this.excludes);
            }
        }

        public boolean apply(String label) {
            if (this.excludes != null) {
                if (Arrays.binarySearch(this.excludes, label) == -1) {
                    return true;
                }
                return false;
            } else if (this.includes == null || Arrays.binarySearch(this.includes, label) < 0) {
                return false;
            } else {
                return true;
            }
        }
    }

    public static LabelFilter includes(String... views) {
        return new DefaultLabelFilter(views, (String[]) null);
    }

    public static LabelFilter excludes(String... views) {
        return new DefaultLabelFilter((String[]) null, views);
    }
}
