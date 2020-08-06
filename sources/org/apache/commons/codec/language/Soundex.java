package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class Soundex implements StringEncoder {
    public static final char SILENT_MARKER = '-';
    public static final Soundex US_ENGLISH = new Soundex();
    public static final Soundex US_ENGLISH_GENEALOGY = new Soundex("-123-12--22455-12623-1-2-2");
    private static final char[] US_ENGLISH_MAPPING = US_ENGLISH_MAPPING_STRING.toCharArray();
    public static final String US_ENGLISH_MAPPING_STRING = "01230120022455012623010202";
    public static final Soundex US_ENGLISH_SIMPLIFIED = new Soundex(US_ENGLISH_MAPPING_STRING, false);
    @Deprecated
    private int maxLength;
    private final char[] soundexMapping;
    private final boolean specialCaseHW;

    public Soundex() {
        this.maxLength = 4;
        this.soundexMapping = US_ENGLISH_MAPPING;
        this.specialCaseHW = true;
    }

    public Soundex(char[] mapping) {
        boolean z = false;
        this.maxLength = 4;
        this.soundexMapping = new char[mapping.length];
        System.arraycopy(mapping, 0, this.soundexMapping, 0, mapping.length);
        this.specialCaseHW = !hasMarker(this.soundexMapping) ? true : z;
    }

    private boolean hasMarker(char[] mapping) {
        for (char ch : mapping) {
            if (ch == '-') {
                return true;
            }
        }
        return false;
    }

    public Soundex(String mapping) {
        this.maxLength = 4;
        this.soundexMapping = mapping.toCharArray();
        this.specialCaseHW = !hasMarker(this.soundexMapping);
    }

    public Soundex(String mapping, boolean specialCaseHW2) {
        this.maxLength = 4;
        this.soundexMapping = mapping.toCharArray();
        this.specialCaseHW = specialCaseHW2;
    }

    public int difference(String s1, String s2) throws EncoderException {
        return SoundexUtils.difference(this, s1, s2);
    }

    public Object encode(Object obj) throws EncoderException {
        if (obj instanceof String) {
            return soundex((String) obj);
        }
        throw new EncoderException("Parameter supplied to Soundex encode is not of type java.lang.String");
    }

    public String encode(String str) {
        return soundex(str);
    }

    @Deprecated
    public int getMaxLength() {
        return this.maxLength;
    }

    private char map(char ch) {
        int index = ch - 'A';
        if (index >= 0 && index < this.soundexMapping.length) {
            return this.soundexMapping[index];
        }
        throw new IllegalArgumentException("The character is not mapped: " + ch + " (index=" + index + ")");
    }

    @Deprecated
    public void setMaxLength(int maxLength2) {
        this.maxLength = maxLength2;
    }

    public String soundex(String str) {
        char digit;
        if (str == null) {
            return null;
        }
        String str2 = SoundexUtils.clean(str);
        if (str2.length() == 0) {
            return str2;
        }
        char[] out = {'0', '0', '0', '0'};
        char first = str2.charAt(0);
        out[0] = first;
        char lastDigit = map(first);
        int count = 0 + 1;
        for (int i = 1; i < str2.length() && count < out.length; i++) {
            char ch = str2.charAt(i);
            if ((!this.specialCaseHW || !(ch == 'H' || ch == 'W')) && (digit = map(ch)) != '-') {
                if (!(digit == '0' || digit == lastDigit)) {
                    out[count] = digit;
                    count++;
                }
                lastDigit = digit;
            }
        }
        return new String(out);
    }
}
