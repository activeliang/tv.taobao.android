package org.apache.commons.codec.binary;

public class CharSequenceUtils {
    static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart, CharSequence substring, int start, int length) {
        if (!(cs instanceof String) || !(substring instanceof String)) {
            int index1 = thisStart;
            int index2 = start;
            int tmpLen = length;
            while (true) {
                int tmpLen2 = tmpLen;
                int index22 = index2;
                int index12 = index1;
                tmpLen = tmpLen2 - 1;
                if (tmpLen2 <= 0) {
                    return true;
                }
                index1 = index12 + 1;
                char c1 = cs.charAt(index12);
                index2 = index22 + 1;
                char c2 = substring.charAt(index22);
                if (c1 != c2) {
                    if (!ignoreCase) {
                        return false;
                    }
                    if (Character.toUpperCase(c1) != Character.toUpperCase(c2) && Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                        return false;
                    }
                }
            }
        } else {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }
    }
}
