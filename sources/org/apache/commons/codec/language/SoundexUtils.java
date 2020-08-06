package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

final class SoundexUtils {
    SoundexUtils() {
    }

    static String clean(String str) {
        int count;
        if (str == null || str.length() == 0) {
            return str;
        }
        int len = str.length();
        char[] chars = new char[len];
        int i = 0;
        int count2 = 0;
        while (i < len) {
            if (Character.isLetter(str.charAt(i))) {
                count = count2 + 1;
                chars[count2] = str.charAt(i);
            } else {
                count = count2;
            }
            i++;
            count2 = count;
        }
        if (count2 == len) {
            return str.toUpperCase(Locale.ENGLISH);
        }
        return new String(chars, 0, count2).toUpperCase(Locale.ENGLISH);
    }

    static int difference(StringEncoder encoder, String s1, String s2) throws EncoderException {
        return differenceEncoded(encoder.encode(s1), encoder.encode(s2));
    }

    static int differenceEncoded(String es1, String es2) {
        if (es1 == null || es2 == null) {
            return 0;
        }
        int lengthToMatch = Math.min(es1.length(), es2.length());
        int diff = 0;
        for (int i = 0; i < lengthToMatch; i++) {
            if (es1.charAt(i) == es2.charAt(i)) {
                diff++;
            }
        }
        return diff;
    }
}
