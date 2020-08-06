package com.taobao.orange.candidate;

public class VersionCompare extends DefCandidateCompare {
    public boolean equals(String clientVal, String serverVal) {
        return clientVal.equals(serverVal);
    }

    public boolean equalsNot(String clientVal, String serverVal) {
        return !clientVal.equals(serverVal);
    }

    public boolean greater(String clientVal, String serverVal) {
        return compareVersion(clientVal, serverVal) == 1;
    }

    public boolean greaterEquals(String clientVal, String serverVal) {
        return compareVersion(clientVal, serverVal) != -1;
    }

    public boolean less(String clientVal, String serverVal) {
        return compareVersion(clientVal, serverVal) == -1;
    }

    public boolean lessEquals(String clientVal, String serverVal) {
        return compareVersion(clientVal, serverVal) != 1;
    }

    private static int compareVersion(String version1, String version2) {
        if (version1.equals(version2)) {
            return 0;
        }
        String[] version1Array = version1.split("\\.");
        String[] version2Array = version2.split("\\.");
        int index = 0;
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        while (index < minLen) {
            diff = Integer.parseInt(version1Array[index]) - Integer.parseInt(version2Array[index]);
            if (diff != 0) {
                break;
            }
            index++;
        }
        if (diff == 0) {
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }
            for (int i2 = index; i2 < version2Array.length; i2++) {
                if (Integer.parseInt(version2Array[i2]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else if (diff <= 0) {
            return -1;
        } else {
            return 1;
        }
    }
}
