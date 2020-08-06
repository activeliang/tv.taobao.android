package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.IOUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.TimeZone;

public final class JSONScanner extends JSONLexerBase {
    private final int len;
    private final String text;

    public JSONScanner(String input) {
        this(input, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(String input, int features) {
        super(features);
        this.text = input;
        this.len = this.text.length();
        this.bp = -1;
        next();
        if (this.ch == 65279) {
            next();
        }
    }

    public final char charAt(int index) {
        if (index >= this.len) {
            return JSONLexer.EOI;
        }
        return this.text.charAt(index);
    }

    public final char next() {
        char charAt;
        int index = this.bp + 1;
        this.bp = index;
        if (index >= this.len) {
            charAt = JSONLexer.EOI;
        } else {
            charAt = this.text.charAt(index);
        }
        this.ch = charAt;
        return charAt;
    }

    public JSONScanner(char[] input, int inputLength) {
        this(input, inputLength, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(char[] input, int inputLength, int features) {
        this(new String(input, 0, inputLength), features);
    }

    /* access modifiers changed from: protected */
    public final void copyTo(int offset, int count, char[] dest) {
        this.text.getChars(offset, offset + count, dest, 0);
    }

    static boolean charArrayCompare(String src, int offset, char[] dest) {
        int destLen = dest.length;
        if (destLen + offset > src.length()) {
            return false;
        }
        for (int i = 0; i < destLen; i++) {
            if (dest[i] != src.charAt(offset + i)) {
                return false;
            }
        }
        return true;
    }

    public final boolean charArrayCompare(char[] chars) {
        return charArrayCompare(this.text, this.bp, chars);
    }

    public final int indexOf(char ch, int startIndex) {
        return this.text.indexOf(ch, startIndex);
    }

    public final String addSymbol(int offset, int len2, int hash, SymbolTable symbolTable) {
        return symbolTable.addSymbol(this.text, offset, len2, hash);
    }

    public byte[] bytesValue() {
        return IOUtils.decodeBase64(this.text, this.np + 1, this.sp);
    }

    public final String stringVal() {
        if (!this.hasSpecial) {
            return subString(this.np + 1, this.sp);
        }
        return new String(this.sbuf, 0, this.sp);
    }

    public final String subString(int offset, int count) {
        if (!ASMUtils.IS_ANDROID) {
            return this.text.substring(offset, offset + count);
        }
        if (count < this.sbuf.length) {
            this.text.getChars(offset, offset + count, this.sbuf, 0);
            return new String(this.sbuf, 0, count);
        }
        char[] chars = new char[count];
        this.text.getChars(offset, offset + count, chars, 0);
        return new String(chars);
    }

    public final char[] sub_chars(int offset, int count) {
        if (!ASMUtils.IS_ANDROID || count >= this.sbuf.length) {
            char[] chars = new char[count];
            this.text.getChars(offset, offset + count, chars, 0);
            return chars;
        }
        this.text.getChars(offset, offset + count, this.sbuf, 0);
        return this.sbuf;
    }

    public final String numberString() {
        char chLocal = charAt((this.np + this.sp) - 1);
        int sp = this.sp;
        if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B' || chLocal == 'F' || chLocal == 'D') {
            sp--;
        }
        return subString(this.np, sp);
    }

    public final BigDecimal decimalValue() {
        char chLocal = charAt((this.np + this.sp) - 1);
        int sp = this.sp;
        if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B' || chLocal == 'F' || chLocal == 'D') {
            sp--;
        }
        int offset = this.np;
        int count = sp;
        if (count < this.sbuf.length) {
            this.text.getChars(offset, offset + count, this.sbuf, 0);
            return new BigDecimal(this.sbuf, 0, count);
        }
        char[] chars = new char[count];
        this.text.getChars(offset, offset + count, chars, 0);
        return new BigDecimal(chars);
    }

    public boolean scanISO8601DateIfMatch() {
        return scanISO8601DateIfMatch(true);
    }

    public boolean scanISO8601DateIfMatch(boolean strict) {
        int hour;
        int minute;
        int seconds;
        int millis;
        char d1;
        char d0;
        char M1;
        char M0;
        char y3;
        char y2;
        char y1;
        char y0;
        char t1;
        int rest = this.len - this.bp;
        if (!strict && rest > 13) {
            char c0 = charAt(this.bp);
            char c1 = charAt(this.bp + 1);
            char c2 = charAt(this.bp + 2);
            char c3 = charAt(this.bp + 3);
            char c4 = charAt(this.bp + 4);
            char c5 = charAt(this.bp + 5);
            char c_r0 = charAt((this.bp + rest) - 1);
            char c_r1 = charAt((this.bp + rest) - 2);
            if (c0 == '/' && c1 == 'D' && c2 == 'a' && c3 == 't' && c4 == 'e' && c5 == '(' && c_r0 == '/' && c_r1 == ')') {
                int plusIndex = -1;
                for (int i = 6; i < rest; i++) {
                    char c = charAt(this.bp + i);
                    if (c != '+') {
                        if (c < '0' || c > '9') {
                            break;
                        }
                    } else {
                        plusIndex = i;
                    }
                }
                if (plusIndex == -1) {
                    return false;
                }
                int offset = this.bp + 6;
                long millis2 = Long.parseLong(subString(offset, plusIndex - offset));
                this.calendar = Calendar.getInstance(this.timeZone, this.locale);
                this.calendar.setTimeInMillis(millis2);
                this.token = 5;
                return true;
            }
        }
        if (rest == 8 || rest == 14 || rest == 17) {
            if (strict) {
                return false;
            }
            char y02 = charAt(this.bp);
            char y12 = charAt(this.bp + 1);
            char y22 = charAt(this.bp + 2);
            char y32 = charAt(this.bp + 3);
            char M02 = charAt(this.bp + 4);
            char M12 = charAt(this.bp + 5);
            char d02 = charAt(this.bp + 6);
            char d12 = charAt(this.bp + 7);
            if (!checkDate(y02, y12, y22, y32, M02, M12, d02, d12)) {
                return false;
            }
            setCalendar(y02, y12, y22, y32, M02, M12, d02, d12);
            if (rest != 8) {
                char h0 = charAt(this.bp + 8);
                char h1 = charAt(this.bp + 9);
                char m0 = charAt(this.bp + 10);
                char m1 = charAt(this.bp + 11);
                char s0 = charAt(this.bp + 12);
                char s1 = charAt(this.bp + 13);
                if (!checkTime(h0, h1, m0, m1, s0, s1)) {
                    return false;
                }
                if (rest == 17) {
                    char S0 = charAt(this.bp + 14);
                    char S1 = charAt(this.bp + 15);
                    char S2 = charAt(this.bp + 16);
                    if (S0 < '0' || S0 > '9' || S1 < '0' || S1 > '9' || S2 < '0' || S2 > '9') {
                        return false;
                    }
                    millis = ((S0 - '0') * 100) + ((S1 - '0') * 10) + (S2 - '0');
                } else {
                    millis = 0;
                }
                hour = ((h0 - '0') * 10) + (h1 - '0');
                minute = ((m0 - '0') * 10) + (m1 - '0');
                seconds = ((s0 - '0') * 10) + (s1 - '0');
            } else {
                hour = 0;
                minute = 0;
                seconds = 0;
                millis = 0;
            }
            this.calendar.set(11, hour);
            this.calendar.set(12, minute);
            this.calendar.set(13, seconds);
            this.calendar.set(14, millis);
            this.token = 5;
            return true;
        } else if (rest < 9) {
            return false;
        } else {
            char c02 = charAt(this.bp);
            char c12 = charAt(this.bp + 1);
            char c22 = charAt(this.bp + 2);
            char c32 = charAt(this.bp + 3);
            char c42 = charAt(this.bp + 4);
            char c52 = charAt(this.bp + 5);
            char c6 = charAt(this.bp + 6);
            char c7 = charAt(this.bp + 7);
            char c8 = charAt(this.bp + 8);
            char c9 = charAt(this.bp + 9);
            if ((c42 == '-' && c7 == '-') || (c42 == '/' && c7 == '/')) {
                y0 = c02;
                y1 = c12;
                y2 = c22;
                y3 = c32;
                M0 = c52;
                M1 = c6;
                d0 = c8;
                d1 = c9;
            } else if ((c22 == '.' && c52 == '.') || (c22 == '-' && c52 == '-')) {
                d0 = c02;
                d1 = c12;
                M0 = c32;
                M1 = c42;
                y0 = c6;
                y1 = c7;
                y2 = c8;
                y3 = c9;
            } else if (c42 != 24180 && c42 != 45380) {
                return false;
            } else {
                y0 = c02;
                y1 = c12;
                y2 = c22;
                y3 = c32;
                if (c7 == 26376 || c7 == 50900) {
                    M0 = c52;
                    M1 = c6;
                    if (c9 == 26085 || c9 == 51068) {
                        d0 = '0';
                        d1 = c8;
                    } else {
                        if (charAt(this.bp + 10) != 26085) {
                            if (charAt(this.bp + 10) != 51068) {
                                return false;
                            }
                        }
                        d0 = c8;
                        d1 = c9;
                    }
                } else if (c6 != 26376 && c6 != 50900) {
                    return false;
                } else {
                    M0 = '0';
                    M1 = c52;
                    if (c8 == 26085 || c8 == 51068) {
                        d0 = '0';
                        d1 = c7;
                    } else if (c9 != 26085 && c9 != 51068) {
                        return false;
                    } else {
                        d0 = c7;
                        d1 = c8;
                    }
                }
            }
            if (!checkDate(y0, y1, y2, y3, M0, M1, d0, d1)) {
                return false;
            }
            setCalendar(y0, y1, y2, y3, M0, M1, d0, d1);
            char t = charAt(this.bp + 10);
            if (t == 'T' || (t == ' ' && !strict)) {
                if (rest < 19) {
                    return false;
                }
                if (charAt(this.bp + 13) != ':') {
                    return false;
                }
                if (charAt(this.bp + 16) != ':') {
                    return false;
                }
                char h02 = charAt(this.bp + 11);
                char h12 = charAt(this.bp + 12);
                char m02 = charAt(this.bp + 14);
                char m12 = charAt(this.bp + 15);
                char s02 = charAt(this.bp + 17);
                char s12 = charAt(this.bp + 18);
                if (!checkTime(h02, h12, m02, m12, s02, s12)) {
                    return false;
                }
                setTime(h02, h12, m02, m12, s02, s12);
                char dot = charAt(this.bp + 19);
                if (dot != '.') {
                    this.calendar.set(14, 0);
                    int i2 = this.bp + 19;
                    this.bp = i2;
                    this.ch = charAt(i2);
                    this.token = 5;
                    if (dot == 'Z' && this.calendar.getTimeZone().getRawOffset() != 0) {
                        String[] timeZoneIDs = TimeZone.getAvailableIDs(0);
                        if (timeZoneIDs.length > 0) {
                            this.calendar.setTimeZone(TimeZone.getTimeZone(timeZoneIDs[0]));
                        }
                    }
                    return true;
                } else if (rest < 21) {
                    return false;
                } else {
                    char S02 = charAt(this.bp + 20);
                    if (S02 < '0' || S02 > '9') {
                        return false;
                    }
                    int millis3 = S02 - '0';
                    int millisLen = 1;
                    if (rest > 21) {
                        char S12 = charAt(this.bp + 21);
                        if (S12 >= '0' && S12 <= '9') {
                            millis3 = (millis3 * 10) + (S12 - '0');
                            millisLen = 2;
                        }
                    }
                    if (millisLen == 2) {
                        char S22 = charAt(this.bp + 22);
                        if (S22 >= '0' && S22 <= '9') {
                            millis3 = (millis3 * 10) + (S22 - '0');
                            millisLen = 3;
                        }
                    }
                    this.calendar.set(14, millis3);
                    int timzeZoneLength = 0;
                    char timeZoneFlag = charAt(this.bp + 20 + millisLen);
                    if (timeZoneFlag == '+' || timeZoneFlag == '-') {
                        char t0 = charAt(this.bp + 20 + millisLen + 1);
                        if (t0 < '0' || t0 > '1' || (t1 = charAt(this.bp + 20 + millisLen + 2)) < '0' || t1 > '9') {
                            return false;
                        }
                        char t2 = charAt(this.bp + 20 + millisLen + 3);
                        if (t2 == ':') {
                            if (charAt(this.bp + 20 + millisLen + 4) != '0') {
                                return false;
                            }
                            if (charAt(this.bp + 20 + millisLen + 5) != '0') {
                                return false;
                            }
                            timzeZoneLength = 6;
                        } else if (t2 == '0') {
                            if (charAt(this.bp + 20 + millisLen + 4) != '0') {
                                return false;
                            }
                            timzeZoneLength = 5;
                        } else {
                            timzeZoneLength = 3;
                        }
                        setTimeZone(timeZoneFlag, t0, t1);
                    } else if (timeZoneFlag == 'Z') {
                        timzeZoneLength = 1;
                        if (this.calendar.getTimeZone().getRawOffset() != 0) {
                            String[] timeZoneIDs2 = TimeZone.getAvailableIDs(0);
                            if (timeZoneIDs2.length > 0) {
                                this.calendar.setTimeZone(TimeZone.getTimeZone(timeZoneIDs2[0]));
                            }
                        }
                    }
                    char end = charAt(this.bp + millisLen + 20 + timzeZoneLength);
                    if (end != 26 && end != '\"') {
                        return false;
                    }
                    int i3 = this.bp + millisLen + 20 + timzeZoneLength;
                    this.bp = i3;
                    this.ch = charAt(i3);
                    this.token = 5;
                    return true;
                }
            } else if (t == '\"' || t == 26 || t == 26085 || t == 51068) {
                this.calendar.set(11, 0);
                this.calendar.set(12, 0);
                this.calendar.set(13, 0);
                this.calendar.set(14, 0);
                int i4 = this.bp + 10;
                this.bp = i4;
                this.ch = charAt(i4);
                this.token = 5;
                return true;
            } else if ((t != '+' && t != '-') || this.len != 16) {
                return false;
            } else {
                if (charAt(this.bp + 13) == ':') {
                    if (charAt(this.bp + 14) == '0') {
                        if (charAt(this.bp + 15) == '0') {
                            setTime('0', '0', '0', '0', '0', '0');
                            this.calendar.set(14, 0);
                            setTimeZone(t, charAt(this.bp + 11), charAt(this.bp + 12));
                            return true;
                        }
                    }
                }
                return false;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setTime(char h0, char h1, char m0, char m1, char s0, char s1) {
        this.calendar.set(11, ((h0 - '0') * 10) + (h1 - '0'));
        this.calendar.set(12, ((m0 - '0') * 10) + (m1 - '0'));
        this.calendar.set(13, ((s0 - '0') * 10) + (s1 - '0'));
    }

    /* access modifiers changed from: protected */
    public void setTimeZone(char timeZoneFlag, char t0, char t1) {
        int timeZoneOffset = (((t0 - '0') * 10) + (t1 - '0')) * 3600 * 1000;
        if (timeZoneFlag == '-') {
            timeZoneOffset = -timeZoneOffset;
        }
        if (this.calendar.getTimeZone().getRawOffset() != timeZoneOffset) {
            String[] timeZoneIDs = TimeZone.getAvailableIDs(timeZoneOffset);
            if (timeZoneIDs.length > 0) {
                this.calendar.setTimeZone(TimeZone.getTimeZone(timeZoneIDs[0]));
            }
        }
    }

    private boolean checkTime(char h0, char h1, char m0, char m1, char s0, char s1) {
        if (h0 == '0') {
            if (h1 < '0' || h1 > '9') {
                return false;
            }
        } else if (h0 == '1') {
            if (h1 < '0' || h1 > '9') {
                return false;
            }
        } else if (h0 != '2' || h1 < '0' || h1 > '4') {
            return false;
        }
        if (m0 < '0' || m0 > '5') {
            if (!(m0 == '6' && m1 == '0')) {
                return false;
            }
        } else if (m1 < '0' || m1 > '9') {
            return false;
        }
        if (s0 < '0' || s0 > '5') {
            if (!(s0 == '6' && s1 == '0')) {
                return false;
            }
        } else if (s1 < '0' || s1 > '9') {
            return false;
        }
        return true;
    }

    private void setCalendar(char y0, char y1, char y2, char y3, char M0, char M1, char d0, char d1) {
        this.calendar = Calendar.getInstance(this.timeZone, this.locale);
        this.calendar.set(1, ((y0 - '0') * 1000) + ((y1 - '0') * 100) + ((y2 - '0') * 10) + (y3 - '0'));
        this.calendar.set(2, (((M0 - '0') * 10) + (M1 - '0')) - 1);
        this.calendar.set(5, ((d0 - '0') * 10) + (d1 - '0'));
    }

    static boolean checkDate(char y0, char y1, char y2, char y3, char M0, char M1, int d0, int d1) {
        if ((y0 != '1' && y0 != '2') || y1 < '0' || y1 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9') {
            return false;
        }
        if (M0 == '0') {
            if (M1 < '1' || M1 > '9') {
                return false;
            }
        } else if (M0 != '1') {
            return false;
        } else {
            if (!(M1 == '0' || M1 == '1' || M1 == '2')) {
                return false;
            }
        }
        if (d0 == 48) {
            if (d1 < 49 || d1 > 57) {
                return false;
            }
        } else if (d0 == 49 || d0 == 50) {
            if (d1 < 48 || d1 > 57) {
                return false;
            }
        } else if (d0 != 51) {
            return false;
        } else {
            if (d1 == 48 || d1 == 49) {
                return true;
            }
            return false;
        }
        return true;
    }

    public boolean isEOF() {
        return this.bp == this.len || (this.ch == 26 && this.bp + 1 == this.len);
    }

    public int scanFieldInt(char[] fieldName) {
        int index;
        int index2;
        char ch;
        this.matchStat = 0;
        int startPos = this.bp;
        char startChar = this.ch;
        if (!charArrayCompare(this.text, this.bp, fieldName)) {
            this.matchStat = -2;
            return 0;
        }
        int index3 = this.bp + fieldName.length;
        int index4 = index3 + 1;
        char ch2 = charAt(index3);
        boolean negative = false;
        if (ch2 == '-') {
            index = index4 + 1;
            ch2 = charAt(index4);
            negative = true;
        } else {
            index = index4;
        }
        if (ch2 < '0' || ch2 > '9') {
            this.matchStat = -1;
            return 0;
        }
        int value = ch2 - '0';
        while (true) {
            index2 = index + 1;
            ch = charAt(index);
            if (ch >= '0' && ch <= '9') {
                value = (value * 10) + (ch - '0');
                index = index2;
            }
        }
        if (ch == '.') {
            this.matchStat = -1;
            return 0;
        } else if (value < 0) {
            this.matchStat = -1;
            return 0;
        } else {
            if (ch == ',' || ch == '}') {
                this.bp = index2 - 1;
            }
            if (ch == ',') {
                int i = this.bp + 1;
                this.bp = i;
                this.ch = charAt(i);
                this.matchStat = 3;
                this.token = 16;
                if (negative) {
                    return -value;
                }
                return value;
            }
            if (ch == '}') {
                int i2 = this.bp + 1;
                this.bp = i2;
                char ch3 = charAt(i2);
                if (ch3 == ',') {
                    this.token = 16;
                    int i3 = this.bp + 1;
                    this.bp = i3;
                    this.ch = charAt(i3);
                } else if (ch3 == ']') {
                    this.token = 15;
                    int i4 = this.bp + 1;
                    this.bp = i4;
                    this.ch = charAt(i4);
                } else if (ch3 == '}') {
                    this.token = 13;
                    int i5 = this.bp + 1;
                    this.bp = i5;
                    this.ch = charAt(i5);
                } else if (ch3 == 26) {
                    this.token = 20;
                } else {
                    this.bp = startPos;
                    this.ch = startChar;
                    this.matchStat = -1;
                    return 0;
                }
                this.matchStat = 4;
            }
            if (negative) {
                return -value;
            }
            return value;
        }
    }

    public String scanFieldString(char[] fieldName) {
        this.matchStat = 0;
        int startPos = this.bp;
        char startChar = this.ch;
        if (!charArrayCompare(this.text, this.bp, fieldName)) {
            this.matchStat = -2;
            return stringDefaultValue();
        }
        int index = this.bp + fieldName.length;
        int index2 = index + 1;
        if (charAt(index) != '\"') {
            this.matchStat = -1;
            return stringDefaultValue();
        }
        int startIndex = index2;
        int endIndex = indexOf('\"', startIndex);
        if (endIndex == -1) {
            throw new JSONException("unclosed str");
        }
        String stringVal = subString(startIndex, endIndex - startIndex);
        if (stringVal.indexOf(92) != -1) {
            while (true) {
                int slashCount = 0;
                int i = endIndex - 1;
                while (i >= 0 && charAt(i) == '\\') {
                    slashCount++;
                    i--;
                }
                if (slashCount % 2 == 0) {
                    break;
                }
                endIndex = indexOf('\"', endIndex + 1);
            }
            int chars_len = endIndex - ((this.bp + fieldName.length) + 1);
            stringVal = readString(sub_chars(this.bp + fieldName.length + 1, chars_len), chars_len);
        }
        char ch = charAt(endIndex + 1);
        if (ch == ',' || ch == '}') {
            this.bp = endIndex + 1;
            this.ch = ch;
            String str = stringVal;
            if (ch == ',') {
                int i2 = this.bp + 1;
                this.bp = i2;
                this.ch = charAt(i2);
                this.matchStat = 3;
                return str;
            }
            int i3 = this.bp + 1;
            this.bp = i3;
            char ch2 = charAt(i3);
            if (ch2 == ',') {
                this.token = 16;
                int i4 = this.bp + 1;
                this.bp = i4;
                this.ch = charAt(i4);
            } else if (ch2 == ']') {
                this.token = 15;
                int i5 = this.bp + 1;
                this.bp = i5;
                this.ch = charAt(i5);
            } else if (ch2 == '}') {
                this.token = 13;
                int i6 = this.bp + 1;
                this.bp = i6;
                this.ch = charAt(i6);
            } else if (ch2 == 26) {
                this.token = 20;
            } else {
                this.bp = startPos;
                this.ch = startChar;
                this.matchStat = -1;
                return stringDefaultValue();
            }
            this.matchStat = 4;
            return str;
        }
        this.matchStat = -1;
        return stringDefaultValue();
    }

    public long scanFieldSymbol(char[] fieldName) {
        this.matchStat = 0;
        if (!charArrayCompare(this.text, this.bp, fieldName)) {
            this.matchStat = -2;
            return 0;
        }
        int index = this.bp + fieldName.length;
        int index2 = index + 1;
        if (charAt(index) != '\"') {
            this.matchStat = -1;
            return 0;
        }
        long hash = -2128831035;
        while (true) {
            int index3 = index2;
            index2 = index3 + 1;
            char ch = charAt(index3);
            if (ch == '\"') {
                this.bp = index2;
                char ch2 = charAt(this.bp);
                this.ch = ch2;
                if (ch2 == ',') {
                    int i = this.bp + 1;
                    this.bp = i;
                    this.ch = charAt(i);
                    this.matchStat = 3;
                    return hash;
                } else if (ch2 == '}') {
                    next();
                    skipWhitespace();
                    char ch3 = getCurrent();
                    if (ch3 == ',') {
                        this.token = 16;
                        int i2 = this.bp + 1;
                        this.bp = i2;
                        this.ch = charAt(i2);
                    } else if (ch3 == ']') {
                        this.token = 15;
                        int i3 = this.bp + 1;
                        this.bp = i3;
                        this.ch = charAt(i3);
                    } else if (ch3 == '}') {
                        this.token = 13;
                        int i4 = this.bp + 1;
                        this.bp = i4;
                        this.ch = charAt(i4);
                    } else if (ch3 == 26) {
                        this.token = 20;
                    } else {
                        this.matchStat = -1;
                        return 0;
                    }
                    this.matchStat = 4;
                    return hash;
                } else {
                    this.matchStat = -1;
                    return 0;
                }
            } else if (index2 > this.len) {
                this.matchStat = -1;
                return 0;
            } else {
                hash = (hash ^ ((long) ch)) * 16777619;
            }
        }
    }

    public Collection<String> scanFieldStringArray(char[] fieldName, Class<?> type) {
        Collection<String> list;
        char ch;
        int index;
        boolean space;
        int index2;
        int index3;
        char ch2;
        this.matchStat = 0;
        if (!charArrayCompare(this.text, this.bp, fieldName)) {
            this.matchStat = -2;
            return null;
        }
        if (type.isAssignableFrom(HashSet.class)) {
            list = new HashSet<>();
        } else {
            if (type.isAssignableFrom(ArrayList.class)) {
                list = new ArrayList<>();
            } else {
                try {
                    list = (Collection) type.newInstance();
                } catch (Exception e) {
                    throw new JSONException(e.getMessage(), e);
                }
            }
        }
        int index4 = this.bp + fieldName.length;
        int index5 = index4 + 1;
        if (charAt(index4) == '[') {
            int index6 = index5 + 1;
            char ch3 = charAt(index5);
            while (true) {
                index2 = index6;
                if (ch3 == '\"') {
                    int startIndex = index2;
                    int endIndex = indexOf('\"', startIndex);
                    if (endIndex == -1) {
                        throw new JSONException("unclosed str");
                    }
                    String stringVal = subString(startIndex, endIndex - startIndex);
                    if (stringVal.indexOf(92) != -1) {
                        while (true) {
                            int slashCount = 0;
                            int i = endIndex - 1;
                            while (i >= 0 && charAt(i) == '\\') {
                                slashCount++;
                                i--;
                            }
                            if (slashCount % 2 == 0) {
                                break;
                            }
                            endIndex = indexOf('\"', endIndex + 1);
                        }
                        int chars_len = endIndex - startIndex;
                        stringVal = readString(sub_chars(startIndex, chars_len), chars_len);
                    }
                    int index7 = endIndex + 1;
                    index3 = index7 + 1;
                    ch2 = charAt(index7);
                    list.add(stringVal);
                } else if (ch3 == 'n' && this.text.startsWith("ull", index2)) {
                    int index8 = index2 + 3;
                    index3 = index8 + 1;
                    ch2 = charAt(index8);
                    list.add((Object) null);
                }
                if (ch2 == ',') {
                    index6 = index3 + 1;
                    ch3 = charAt(index3);
                } else if (ch2 == ']') {
                    index = index3 + 1;
                    ch = charAt(index3);
                    while (isWhitespace(ch)) {
                        ch = charAt(index);
                        index++;
                    }
                } else {
                    this.matchStat = -1;
                    return null;
                }
            }
            if (ch3 == ']' && list.size() == 0) {
                index = index2 + 1;
                ch = charAt(index2);
            } else {
                this.matchStat = -1;
                return null;
            }
        } else if (this.text.startsWith("ull", index5)) {
            int index9 = index5 + 3;
            ch = charAt(index9);
            list = null;
            index = index9 + 1;
        } else {
            this.matchStat = -1;
            return null;
        }
        this.bp = index;
        if (ch == ',') {
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            return list;
        } else if (ch == '}') {
            char ch4 = charAt(this.bp);
            do {
                if (ch4 == ',') {
                    this.token = 16;
                    int i2 = this.bp + 1;
                    this.bp = i2;
                    this.ch = charAt(i2);
                } else if (ch4 == ']') {
                    this.token = 15;
                    int i3 = this.bp + 1;
                    this.bp = i3;
                    this.ch = charAt(i3);
                } else if (ch4 == '}') {
                    this.token = 13;
                    int i4 = this.bp + 1;
                    this.bp = i4;
                    this.ch = charAt(i4);
                } else if (ch4 == 26) {
                    this.token = 20;
                    this.ch = ch4;
                } else {
                    space = false;
                    while (isWhitespace(ch4)) {
                        int index10 = index + 1;
                        ch4 = charAt(index);
                        this.bp = index10;
                        space = true;
                        index = index10;
                    }
                }
                this.matchStat = 4;
                return list;
            } while (space);
            this.matchStat = -1;
            return null;
        } else {
            this.matchStat = -1;
            return null;
        }
    }

    public long scanFieldLong(char[] fieldName) {
        int index;
        int index2;
        char ch;
        this.matchStat = 0;
        int startPos = this.bp;
        char startChar = this.ch;
        if (!charArrayCompare(this.text, this.bp, fieldName)) {
            this.matchStat = -2;
            return 0;
        }
        int index3 = this.bp + fieldName.length;
        int index4 = index3 + 1;
        char ch2 = charAt(index3);
        boolean negative = false;
        if (ch2 == '-') {
            index = index4 + 1;
            ch2 = charAt(index4);
            negative = true;
        } else {
            index = index4;
        }
        if (ch2 < '0' || ch2 > '9') {
            this.bp = startPos;
            this.ch = startChar;
            this.matchStat = -1;
            return 0;
        }
        long value = (long) (ch2 - '0');
        while (true) {
            index2 = index + 1;
            ch = charAt(index);
            if (ch >= '0' && ch <= '9') {
                value = (10 * value) + ((long) (ch - '0'));
                index = index2;
            }
        }
        if (ch == '.') {
            this.matchStat = -1;
            return 0;
        }
        if (ch == ',' || ch == '}') {
            this.bp = index2 - 1;
        }
        if (value < 0) {
            this.bp = startPos;
            this.ch = startChar;
            this.matchStat = -1;
            return 0;
        } else if (ch == ',') {
            int i = this.bp + 1;
            this.bp = i;
            this.ch = charAt(i);
            this.matchStat = 3;
            this.token = 16;
            if (negative) {
                return -value;
            }
            return value;
        } else if (ch == '}') {
            int i2 = this.bp + 1;
            this.bp = i2;
            char ch3 = charAt(i2);
            if (ch3 == ',') {
                this.token = 16;
                int i3 = this.bp + 1;
                this.bp = i3;
                this.ch = charAt(i3);
            } else if (ch3 == ']') {
                this.token = 15;
                int i4 = this.bp + 1;
                this.bp = i4;
                this.ch = charAt(i4);
            } else if (ch3 == '}') {
                this.token = 13;
                int i5 = this.bp + 1;
                this.bp = i5;
                this.ch = charAt(i5);
            } else if (ch3 == 26) {
                this.token = 20;
            } else {
                this.bp = startPos;
                this.ch = startChar;
                this.matchStat = -1;
                return 0;
            }
            this.matchStat = 4;
            if (negative) {
                return -value;
            }
            return value;
        } else {
            this.matchStat = -1;
            return 0;
        }
    }

    public boolean scanFieldBoolean(char[] fieldName) {
        char ch;
        boolean value;
        this.matchStat = 0;
        if (!charArrayCompare(this.text, this.bp, fieldName)) {
            this.matchStat = -2;
            return false;
        }
        int index = this.bp + fieldName.length;
        int index2 = index + 1;
        char ch2 = charAt(index);
        if (ch2 == 't') {
            int index3 = index2 + 1;
            if (charAt(index2) != 'r') {
                this.matchStat = -1;
                return false;
            }
            int index4 = index3 + 1;
            if (charAt(index3) != 'u') {
                this.matchStat = -1;
                return false;
            }
            int index5 = index4 + 1;
            if (charAt(index4) != 'e') {
                this.matchStat = -1;
                return false;
            }
            this.bp = index5;
            ch = charAt(this.bp);
            value = true;
        } else if (ch2 == 'f') {
            int index6 = index2 + 1;
            if (charAt(index2) != 'a') {
                this.matchStat = -1;
                return false;
            }
            int index7 = index6 + 1;
            if (charAt(index6) != 'l') {
                this.matchStat = -1;
                return false;
            }
            int index8 = index7 + 1;
            if (charAt(index7) != 's') {
                this.matchStat = -1;
                return false;
            }
            int index9 = index8 + 1;
            if (charAt(index8) != 'e') {
                this.matchStat = -1;
                return false;
            }
            this.bp = index9;
            ch = charAt(this.bp);
            value = false;
            int i = index9;
        } else {
            this.matchStat = -1;
            return false;
        }
        if (ch == ',') {
            int i2 = this.bp + 1;
            this.bp = i2;
            this.ch = charAt(i2);
            this.matchStat = 3;
            this.token = 16;
            return value;
        } else if (ch == '}') {
            int i3 = this.bp + 1;
            this.bp = i3;
            char ch3 = charAt(i3);
            if (ch3 == ',') {
                this.token = 16;
                int i4 = this.bp + 1;
                this.bp = i4;
                this.ch = charAt(i4);
            } else if (ch3 == ']') {
                this.token = 15;
                int i5 = this.bp + 1;
                this.bp = i5;
                this.ch = charAt(i5);
            } else if (ch3 == '}') {
                this.token = 13;
                int i6 = this.bp + 1;
                this.bp = i6;
                this.ch = charAt(i6);
            } else if (ch3 == 26) {
                this.token = 20;
            } else {
                this.matchStat = -1;
                return false;
            }
            this.matchStat = 4;
            return value;
        } else {
            this.matchStat = -1;
            return false;
        }
    }

    public final int scanInt(char expectNext) {
        boolean negative;
        int offset;
        int offset2;
        char chLocal;
        this.matchStat = 0;
        int offset3 = this.bp;
        int offset4 = offset3 + 1;
        char chLocal2 = charAt(offset3);
        if (chLocal2 == '-') {
            negative = true;
        } else {
            negative = false;
        }
        if (negative) {
            offset = offset4 + 1;
            chLocal2 = charAt(offset4);
        } else {
            offset = offset4;
        }
        if (chLocal2 < '0' || chLocal2 > '9') {
            this.matchStat = -1;
            return 0;
        }
        int value = chLocal2 - '0';
        while (true) {
            offset2 = offset + 1;
            chLocal = charAt(offset);
            if (chLocal >= '0' && chLocal <= '9') {
                value = (value * 10) + (chLocal - '0');
                offset = offset2;
            }
        }
        if (chLocal == '.') {
            this.matchStat = -1;
            int i = offset2;
            return 0;
        } else if (value < 0) {
            this.matchStat = -1;
            int i2 = offset2;
            return 0;
        } else {
            while (chLocal != expectNext) {
                if (isWhitespace(chLocal)) {
                    chLocal = charAt(offset2);
                    offset2++;
                } else {
                    this.matchStat = -1;
                    if (negative) {
                        value = -value;
                    }
                    int i3 = offset2;
                    return value;
                }
            }
            this.bp = offset2;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            if (negative) {
                value = -value;
            }
            int i4 = offset2;
            return value;
        }
    }

    public long scanLong(char expectNextChar) {
        int offset;
        int offset2;
        char chLocal;
        this.matchStat = 0;
        int offset3 = this.bp;
        int offset4 = offset3 + 1;
        char chLocal2 = charAt(offset3);
        boolean negative = chLocal2 == '-';
        if (negative) {
            offset = offset4 + 1;
            chLocal2 = charAt(offset4);
        } else {
            offset = offset4;
        }
        if (chLocal2 < '0' || chLocal2 > '9') {
            this.matchStat = -1;
            return 0;
        }
        long value = (long) (chLocal2 - '0');
        while (true) {
            offset2 = offset + 1;
            chLocal = charAt(offset);
            if (chLocal >= '0' && chLocal <= '9') {
                value = (10 * value) + ((long) (chLocal - '0'));
                offset = offset2;
            }
        }
        if (chLocal == '.') {
            this.matchStat = -1;
            int i = offset2;
            return 0;
        } else if (value < 0) {
            this.matchStat = -1;
            int i2 = offset2;
            return 0;
        } else {
            while (chLocal != expectNextChar) {
                if (isWhitespace(chLocal)) {
                    chLocal = charAt(offset2);
                    offset2++;
                } else {
                    this.matchStat = -1;
                    int i3 = offset2;
                    return value;
                }
            }
            this.bp = offset2;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            if (negative) {
                value = -value;
            }
            int i4 = offset2;
            return value;
        }
    }

    /* access modifiers changed from: protected */
    public final void arrayCopy(int srcPos, char[] dest, int destPos, int length) {
        this.text.getChars(srcPos, srcPos + length, dest, destPos);
    }

    public String info() {
        String substring;
        StringBuilder append = new StringBuilder().append("pos ").append(this.bp).append(", json : ");
        if (this.text.length() < 65536) {
            substring = this.text;
        } else {
            substring = this.text.substring(0, 65536);
        }
        return append.append(substring).toString();
    }
}
