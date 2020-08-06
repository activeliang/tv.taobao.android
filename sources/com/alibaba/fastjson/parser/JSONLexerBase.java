package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.atlas.dexmerge.dx.io.Opcodes;
import com.taobao.ju.track.csv.CsvReader;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.io.Closeable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.TimeZone;

public abstract class JSONLexerBase implements JSONLexer, Closeable {
    protected static final int INT_MULTMIN_RADIX_TEN = -214748364;
    protected static final long MULTMIN_RADIX_TEN = -922337203685477580L;
    private static final ThreadLocal<char[]> SBUF_LOCAL = new ThreadLocal<>();
    protected static final int[] digits = new int[103];
    protected static final char[] typeFieldName = ("\"" + JSON.DEFAULT_TYPE_KEY + "\":\"").toCharArray();
    protected int bp;
    protected Calendar calendar = null;
    protected char ch;
    protected int eofPos;
    protected int features;
    protected boolean hasSpecial;
    protected Locale locale = JSON.defaultLocale;
    public int matchStat = 0;
    protected int np;
    protected int pos;
    protected char[] sbuf;
    protected int sp;
    protected String stringDefaultValue = null;
    protected TimeZone timeZone = JSON.defaultTimeZone;
    protected int token;

    public abstract String addSymbol(int i, int i2, int i3, SymbolTable symbolTable);

    /* access modifiers changed from: protected */
    public abstract void arrayCopy(int i, char[] cArr, int i2, int i3);

    public abstract byte[] bytesValue();

    /* access modifiers changed from: protected */
    public abstract boolean charArrayCompare(char[] cArr);

    public abstract char charAt(int i);

    /* access modifiers changed from: protected */
    public abstract void copyTo(int i, int i2, char[] cArr);

    public abstract BigDecimal decimalValue();

    public abstract int indexOf(char c, int i);

    public abstract boolean isEOF();

    public abstract char next();

    public abstract String numberString();

    public abstract String stringVal();

    public abstract String subString(int i, int i2);

    /* access modifiers changed from: protected */
    public abstract char[] sub_chars(int i, int i2);

    /* access modifiers changed from: protected */
    public void lexError(String key, Object... args) {
        this.token = 1;
    }

    static {
        for (int i = 48; i <= 57; i++) {
            digits[i] = i - 48;
        }
        for (int i2 = 97; i2 <= 102; i2++) {
            digits[i2] = (i2 - 97) + 10;
        }
        for (int i3 = 65; i3 <= 70; i3++) {
            digits[i3] = (i3 - 65) + 10;
        }
    }

    public JSONLexerBase(int features2) {
        this.features = features2;
        if ((Feature.InitStringFieldAsEmpty.mask & features2) != 0) {
            this.stringDefaultValue = "";
        }
        this.sbuf = SBUF_LOCAL.get();
        if (this.sbuf == null) {
            this.sbuf = new char[512];
        }
    }

    public final int matchStat() {
        return this.matchStat;
    }

    public void setToken(int token2) {
        this.token = token2;
    }

    public final void nextToken() {
        this.sp = 0;
        while (true) {
            this.pos = this.bp;
            if (this.ch == '/') {
                skipComment();
            } else if (this.ch == '\"') {
                scanString();
                return;
            } else if (this.ch == ',') {
                next();
                this.token = 16;
                return;
            } else if (this.ch >= '0' && this.ch <= '9') {
                scanNumber();
                return;
            } else if (this.ch == '-') {
                scanNumber();
                return;
            } else {
                switch (this.ch) {
                    case 8:
                    case 9:
                    case 10:
                    case 12:
                    case 13:
                    case ' ':
                        next();
                        break;
                    case '\'':
                        if (!isEnabled(Feature.AllowSingleQuotes)) {
                            throw new JSONException("Feature.AllowSingleQuotes is false");
                        }
                        scanStringSingleQuote();
                        return;
                    case '(':
                        next();
                        this.token = 10;
                        return;
                    case ')':
                        next();
                        this.token = 11;
                        return;
                    case ':':
                        next();
                        this.token = 17;
                        return;
                    case 'N':
                    case 'S':
                    case 'T':
                    case 'u':
                        scanIdent();
                        return;
                    case '[':
                        next();
                        this.token = 14;
                        return;
                    case ']':
                        next();
                        this.token = 15;
                        return;
                    case 'f':
                        scanFalse();
                        return;
                    case 'n':
                        scanNullOrNew();
                        return;
                    case 't':
                        scanTrue();
                        return;
                    case '{':
                        next();
                        this.token = 12;
                        return;
                    case '}':
                        next();
                        this.token = 13;
                        return;
                    default:
                        if (isEOF()) {
                            if (this.token == 20) {
                                throw new JSONException("EOF error");
                            }
                            this.token = 20;
                            int i = this.eofPos;
                            this.bp = i;
                            this.pos = i;
                            return;
                        } else if (this.ch <= 31 || this.ch == 127) {
                            next();
                            break;
                        } else {
                            lexError("illegal.char", String.valueOf(this.ch));
                            next();
                            return;
                        }
                }
            }
        }
    }

    public final void nextToken(int expect) {
        this.sp = 0;
        while (true) {
            switch (expect) {
                case 2:
                    if (this.ch >= '0' && this.ch <= '9') {
                        this.pos = this.bp;
                        scanNumber();
                        return;
                    } else if (this.ch == '\"') {
                        this.pos = this.bp;
                        scanString();
                        return;
                    } else if (this.ch == '[') {
                        this.token = 14;
                        next();
                        return;
                    } else if (this.ch == '{') {
                        this.token = 12;
                        next();
                        return;
                    }
                    break;
                case 4:
                    if (this.ch == '\"') {
                        this.pos = this.bp;
                        scanString();
                        return;
                    } else if (this.ch >= '0' && this.ch <= '9') {
                        this.pos = this.bp;
                        scanNumber();
                        return;
                    } else if (this.ch == '[') {
                        this.token = 14;
                        next();
                        return;
                    } else if (this.ch == '{') {
                        this.token = 12;
                        next();
                        return;
                    }
                    break;
                case 12:
                    if (this.ch == '{') {
                        this.token = 12;
                        next();
                        return;
                    } else if (this.ch == '[') {
                        this.token = 14;
                        next();
                        return;
                    }
                    break;
                case 14:
                    if (this.ch == '[') {
                        this.token = 14;
                        next();
                        return;
                    } else if (this.ch == '{') {
                        this.token = 12;
                        next();
                        return;
                    }
                    break;
                case 15:
                    if (this.ch == ']') {
                        this.token = 15;
                        next();
                        return;
                    }
                    break;
                case 16:
                    if (this.ch == ',') {
                        this.token = 16;
                        next();
                        return;
                    } else if (this.ch == '}') {
                        this.token = 13;
                        next();
                        return;
                    } else if (this.ch == ']') {
                        this.token = 15;
                        next();
                        return;
                    } else if (this.ch == 26) {
                        this.token = 20;
                        return;
                    }
                    break;
                case 18:
                    nextIdent();
                    return;
                case 20:
                    break;
            }
            if (this.ch == 26) {
                this.token = 20;
                return;
            }
            if (this.ch == ' ' || this.ch == 10 || this.ch == 13 || this.ch == 9 || this.ch == 12 || this.ch == 8) {
                next();
            } else {
                nextToken();
                return;
            }
        }
    }

    public final void nextIdent() {
        while (isWhitespace(this.ch)) {
            next();
        }
        if (this.ch == '_' || Character.isLetter(this.ch)) {
            scanIdent();
        } else {
            nextToken();
        }
    }

    public final void nextTokenWithColon() {
        nextTokenWithChar(':');
    }

    public final void nextTokenWithChar(char expect) {
        this.sp = 0;
        while (this.ch != expect) {
            if (this.ch == ' ' || this.ch == 10 || this.ch == 13 || this.ch == 9 || this.ch == 12 || this.ch == 8) {
                next();
            } else {
                throw new JSONException("not match " + expect + " - " + this.ch);
            }
        }
        next();
        nextToken();
    }

    public final int token() {
        return this.token;
    }

    public final String tokenName() {
        return JSONToken.name(this.token);
    }

    public final int pos() {
        return this.pos;
    }

    public final String stringDefaultValue() {
        return this.stringDefaultValue;
    }

    public final Number integerValue() throws NumberFormatException {
        long limit;
        int i;
        long result = 0;
        boolean negative = false;
        if (this.np == -1) {
            this.np = 0;
        }
        int i2 = this.np;
        int max = this.np + this.sp;
        char type = ' ';
        switch (charAt(max - 1)) {
            case 'B':
                max--;
                type = 'B';
                break;
            case 'L':
                max--;
                type = 'L';
                break;
            case 'S':
                max--;
                type = 'S';
                break;
        }
        if (charAt(this.np) == '-') {
            negative = true;
            limit = Long.MIN_VALUE;
            i = i2 + 1;
        } else {
            limit = -9223372036854775807L;
            i = i2;
        }
        if (i < max) {
            result = (long) (-(charAt(i) - '0'));
            i++;
        }
        while (i < max) {
            int i3 = i + 1;
            int digit = charAt(i) - '0';
            if (result < MULTMIN_RADIX_TEN) {
                return new BigInteger(numberString());
            }
            long result2 = result * 10;
            if (result2 < ((long) digit) + limit) {
                return new BigInteger(numberString());
            }
            result = result2 - ((long) digit);
            i = i3;
        }
        if (!negative) {
            long result3 = -result;
            if (result3 > 2147483647L || type == 'L') {
                int i4 = i;
                return Long.valueOf(result3);
            } else if (type == 'S') {
                int i5 = i;
                return Short.valueOf((short) ((int) result3));
            } else if (type == 'B') {
                int i6 = i;
                return Byte.valueOf((byte) ((int) result3));
            } else {
                int i7 = i;
                return Integer.valueOf((int) result3);
            }
        } else if (i <= this.np + 1) {
            throw new NumberFormatException(numberString());
        } else if (result < -2147483648L || type == 'L') {
            int i8 = i;
            return Long.valueOf(result);
        } else if (type == 'S') {
            int i9 = i;
            return Short.valueOf((short) ((int) result));
        } else if (type == 'B') {
            int i10 = i;
            return Byte.valueOf((byte) ((int) result));
        } else {
            int i11 = i;
            return Integer.valueOf((int) result);
        }
    }

    public final void nextTokenWithColon(int expect) {
        nextTokenWithChar(':');
    }

    public float floatValue() {
        char c0;
        String strVal = numberString();
        float floatValue = Float.parseFloat(strVal);
        if ((floatValue != 0.0f && floatValue != Float.POSITIVE_INFINITY) || (c0 = strVal.charAt(0)) <= '0' || c0 > '9') {
            return floatValue;
        }
        throw new JSONException("float overflow : " + strVal);
    }

    public double doubleValue() {
        return Double.parseDouble(numberString());
    }

    public void config(Feature feature, boolean state) {
        this.features = Feature.config(this.features, feature, state);
        if ((this.features & Feature.InitStringFieldAsEmpty.mask) != 0) {
            this.stringDefaultValue = "";
        }
    }

    public final boolean isEnabled(Feature feature) {
        return isEnabled(feature.mask);
    }

    public final boolean isEnabled(int feature) {
        return (this.features & feature) != 0;
    }

    public final boolean isEnabled(int features2, int feature) {
        return ((this.features & feature) == 0 && (features2 & feature) == 0) ? false : true;
    }

    public final char getCurrent() {
        return this.ch;
    }

    /* access modifiers changed from: protected */
    public void skipComment() {
        next();
        if (this.ch == '/') {
            do {
                next();
            } while (this.ch != 10);
            next();
        } else if (this.ch == '*') {
            next();
            while (this.ch != 26) {
                if (this.ch == '*') {
                    next();
                    if (this.ch == '/') {
                        next();
                        return;
                    }
                } else {
                    next();
                }
            }
        } else {
            throw new JSONException("invalid comment");
        }
    }

    public final String scanSymbol(SymbolTable symbolTable) {
        skipWhitespace();
        if (this.ch == '\"') {
            return scanSymbol(symbolTable, '\"');
        }
        if (this.ch == '\'') {
            if (isEnabled(Feature.AllowSingleQuotes)) {
                return scanSymbol(symbolTable, '\'');
            }
            throw new JSONException("syntax error");
        } else if (this.ch == '}') {
            next();
            this.token = 13;
            return null;
        } else if (this.ch == ',') {
            next();
            this.token = 16;
            return null;
        } else if (this.ch == 26) {
            this.token = 20;
            return null;
        } else if (isEnabled(Feature.AllowUnQuotedFieldNames)) {
            return scanSymbolUnQuoted(symbolTable);
        } else {
            throw new JSONException("syntax error");
        }
    }

    public final String scanSymbol(SymbolTable symbolTable, char quote) {
        String value;
        int offset;
        int hash = 0;
        this.np = this.bp;
        this.sp = 0;
        boolean hasSpecial2 = false;
        while (true) {
            char chLocal = next();
            if (chLocal == quote) {
                this.token = 4;
                if (!hasSpecial2) {
                    if (this.np == -1) {
                        offset = 0;
                    } else {
                        offset = this.np + 1;
                    }
                    value = addSymbol(offset, this.sp, hash, symbolTable);
                } else {
                    value = symbolTable.addSymbol(this.sbuf, 0, this.sp, hash);
                }
                this.sp = 0;
                next();
                return value;
            } else if (chLocal == 26) {
                throw new JSONException("unclosed.str");
            } else if (chLocal == '\\') {
                if (!hasSpecial2) {
                    hasSpecial2 = true;
                    if (this.sp >= this.sbuf.length) {
                        int newCapcity = this.sbuf.length * 2;
                        if (this.sp > newCapcity) {
                            newCapcity = this.sp;
                        }
                        char[] newsbuf = new char[newCapcity];
                        System.arraycopy(this.sbuf, 0, newsbuf, 0, this.sbuf.length);
                        this.sbuf = newsbuf;
                    }
                    arrayCopy(this.np + 1, this.sbuf, 0, this.sp);
                }
                char chLocal2 = next();
                switch (chLocal2) {
                    case '\"':
                        hash = (hash * 31) + 34;
                        putChar('\"');
                        break;
                    case '\'':
                        hash = (hash * 31) + 39;
                        putChar('\'');
                        break;
                    case '/':
                        hash = (hash * 31) + 47;
                        putChar('/');
                        break;
                    case '0':
                        hash = (hash * 31) + chLocal2;
                        putChar(0);
                        break;
                    case '1':
                        hash = (hash * 31) + chLocal2;
                        putChar(1);
                        break;
                    case '2':
                        hash = (hash * 31) + chLocal2;
                        putChar(2);
                        break;
                    case '3':
                        hash = (hash * 31) + chLocal2;
                        putChar(3);
                        break;
                    case '4':
                        hash = (hash * 31) + chLocal2;
                        putChar(4);
                        break;
                    case '5':
                        hash = (hash * 31) + chLocal2;
                        putChar(5);
                        break;
                    case '6':
                        hash = (hash * 31) + chLocal2;
                        putChar(6);
                        break;
                    case '7':
                        hash = (hash * 31) + chLocal2;
                        putChar(7);
                        break;
                    case 'F':
                    case 'f':
                        hash = (hash * 31) + 12;
                        putChar(CsvReader.Letters.FORM_FEED);
                        break;
                    case '\\':
                        hash = (hash * 31) + 92;
                        putChar('\\');
                        break;
                    case 'b':
                        hash = (hash * 31) + 8;
                        putChar(8);
                        break;
                    case 'n':
                        hash = (hash * 31) + 10;
                        putChar(10);
                        break;
                    case 'r':
                        hash = (hash * 31) + 13;
                        putChar(13);
                        break;
                    case 't':
                        hash = (hash * 31) + 9;
                        putChar(9);
                        break;
                    case 'u':
                        int val = Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16);
                        hash = (hash * 31) + val;
                        putChar((char) val);
                        break;
                    case 'v':
                        hash = (hash * 31) + 11;
                        putChar(CsvReader.Letters.VERTICAL_TAB);
                        break;
                    case Opcodes.INVOKE_INTERFACE_RANGE /*120*/:
                        char x1 = next();
                        this.ch = x1;
                        char x2 = next();
                        this.ch = x2;
                        char x_char = (char) ((digits[x1] * 16) + digits[x2]);
                        hash = (hash * 31) + x_char;
                        putChar(x_char);
                        break;
                    default:
                        this.ch = chLocal2;
                        throw new JSONException("unclosed.str.lit");
                }
            } else {
                hash = (hash * 31) + chLocal;
                if (!hasSpecial2) {
                    this.sp++;
                } else {
                    if (this.sp == this.sbuf.length) {
                        putChar(chLocal);
                    } else {
                        char[] cArr = this.sbuf;
                        int i = this.sp;
                        this.sp = i + 1;
                        cArr[i] = chLocal;
                    }
                }
            }
        }
    }

    public final void resetStringPosition() {
        this.sp = 0;
    }

    public String info() {
        return "";
    }

    public final String scanSymbolUnQuoted(SymbolTable symbolTable) {
        boolean[] firstIdentifierFlags = IOUtils.firstIdentifierFlags;
        char first = this.ch;
        if (!(this.ch >= firstIdentifierFlags.length || firstIdentifierFlags[first])) {
            throw new JSONException("illegal identifier : " + this.ch + info());
        }
        boolean[] identifierFlags = IOUtils.identifierFlags;
        int hash = first;
        this.np = this.bp;
        this.sp = 1;
        while (true) {
            char chLocal = next();
            if (chLocal < identifierFlags.length && !identifierFlags[chLocal]) {
                break;
            }
            hash = (hash * 31) + chLocal;
            this.sp++;
        }
        this.ch = charAt(this.bp);
        this.token = 18;
        if (this.sp == 4 && hash == 3392903 && charAt(this.np) == 'n' && charAt(this.np + 1) == 'u' && charAt(this.np + 2) == 'l' && charAt(this.np + 3) == 'l') {
            return null;
        }
        return addSymbol(this.np, this.sp, hash, symbolTable);
    }

    public final void scanString() {
        this.np = this.bp;
        this.hasSpecial = false;
        while (true) {
            char ch2 = next();
            if (ch2 == '\"') {
                this.token = 4;
                this.ch = next();
                return;
            } else if (ch2 == 26) {
                if (!isEOF()) {
                    putChar(JSONLexer.EOI);
                } else {
                    throw new JSONException("unclosed string : " + ch2);
                }
            } else if (ch2 == '\\') {
                if (!this.hasSpecial) {
                    this.hasSpecial = true;
                    if (this.sp >= this.sbuf.length) {
                        int newCapcity = this.sbuf.length * 2;
                        if (this.sp > newCapcity) {
                            newCapcity = this.sp;
                        }
                        char[] newsbuf = new char[newCapcity];
                        System.arraycopy(this.sbuf, 0, newsbuf, 0, this.sbuf.length);
                        this.sbuf = newsbuf;
                    }
                    copyTo(this.np + 1, this.sp, this.sbuf);
                }
                char ch3 = next();
                switch (ch3) {
                    case '\"':
                        putChar('\"');
                        break;
                    case '\'':
                        putChar('\'');
                        break;
                    case '/':
                        putChar('/');
                        break;
                    case '0':
                        putChar(0);
                        break;
                    case '1':
                        putChar(1);
                        break;
                    case '2':
                        putChar(2);
                        break;
                    case '3':
                        putChar(3);
                        break;
                    case '4':
                        putChar(4);
                        break;
                    case '5':
                        putChar(5);
                        break;
                    case '6':
                        putChar(6);
                        break;
                    case '7':
                        putChar(7);
                        break;
                    case 'F':
                    case 'f':
                        putChar(CsvReader.Letters.FORM_FEED);
                        break;
                    case '\\':
                        putChar('\\');
                        break;
                    case 'b':
                        putChar(8);
                        break;
                    case 'n':
                        putChar(10);
                        break;
                    case 'r':
                        putChar(13);
                        break;
                    case 't':
                        putChar(9);
                        break;
                    case 'u':
                        putChar((char) Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16));
                        break;
                    case 'v':
                        putChar(CsvReader.Letters.VERTICAL_TAB);
                        break;
                    case Opcodes.INVOKE_INTERFACE_RANGE /*120*/:
                        putChar((char) ((digits[next()] * 16) + digits[next()]));
                        break;
                    default:
                        this.ch = ch3;
                        throw new JSONException("unclosed string : " + ch3);
                }
            } else if (!this.hasSpecial) {
                this.sp++;
            } else if (this.sp == this.sbuf.length) {
                putChar(ch2);
            } else {
                char[] cArr = this.sbuf;
                int i = this.sp;
                this.sp = i + 1;
                cArr[i] = ch2;
            }
        }
    }

    public Calendar getCalendar() {
        return this.calendar;
    }

    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(TimeZone timeZone2) {
        this.timeZone = timeZone2;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale2) {
        this.locale = locale2;
    }

    public final int intValue() {
        int limit;
        int i;
        int i2;
        if (this.np == -1) {
            this.np = 0;
        }
        int result = 0;
        boolean negative = false;
        int i3 = this.np;
        int max = this.np + this.sp;
        if (charAt(this.np) == '-') {
            negative = true;
            limit = Integer.MIN_VALUE;
            i = i3 + 1;
        } else {
            limit = -2147483647;
            i = i3;
        }
        if (i < max) {
            result = -(charAt(i) - '0');
            i++;
        }
        while (true) {
            if (i >= max) {
                i2 = i;
                break;
            }
            i2 = i + 1;
            char chLocal = charAt(i);
            if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B') {
                break;
            }
            int digit = chLocal - '0';
            if (((long) result) < -214748364) {
                throw new NumberFormatException(numberString());
            }
            int result2 = result * 10;
            if (result2 < limit + digit) {
                throw new NumberFormatException(numberString());
            }
            result = result2 - digit;
            i = i2;
        }
        if (!negative) {
            return -result;
        }
        if (i2 > this.np + 1) {
            return result;
        }
        throw new NumberFormatException(numberString());
    }

    public void close() {
        if (this.sbuf.length <= 8192) {
            SBUF_LOCAL.set(this.sbuf);
        }
        this.sbuf = null;
    }

    public final boolean isRef() {
        if (this.sp == 4 && charAt(this.np + 1) == '$' && charAt(this.np + 2) == 'r' && charAt(this.np + 3) == 'e' && charAt(this.np + 4) == 'f') {
            return true;
        }
        return false;
    }

    public final int scanType(String type) {
        this.matchStat = 0;
        if (!charArrayCompare(typeFieldName)) {
            return -2;
        }
        int bpLocal = this.bp + typeFieldName.length;
        int typeLength = type.length();
        for (int i = 0; i < typeLength; i++) {
            if (type.charAt(i) != charAt(bpLocal + i)) {
                return -1;
            }
        }
        int bpLocal2 = bpLocal + typeLength;
        if (charAt(bpLocal2) != '\"') {
            return -1;
        }
        int bpLocal3 = bpLocal2 + 1;
        this.ch = charAt(bpLocal3);
        if (this.ch == ',') {
            int bpLocal4 = bpLocal3 + 1;
            this.ch = charAt(bpLocal4);
            this.bp = bpLocal4;
            this.token = 16;
            return 3;
        }
        if (this.ch == '}') {
            bpLocal3++;
            this.ch = charAt(bpLocal3);
            if (this.ch == ',') {
                this.token = 16;
                bpLocal3++;
                this.ch = charAt(bpLocal3);
            } else if (this.ch == ']') {
                this.token = 15;
                bpLocal3++;
                this.ch = charAt(bpLocal3);
            } else if (this.ch == '}') {
                this.token = 13;
                bpLocal3++;
                this.ch = charAt(bpLocal3);
            } else if (this.ch != 26) {
                return -1;
            } else {
                this.token = 20;
            }
            this.matchStat = 4;
        }
        this.bp = bpLocal3;
        return this.matchStat;
    }

    public final boolean matchField(char[] fieldName) {
        if (!charArrayCompare(fieldName)) {
            return false;
        }
        this.bp += fieldName.length;
        this.ch = charAt(this.bp);
        if (this.ch == '{') {
            next();
            this.token = 12;
        } else if (this.ch == '[') {
            next();
            this.token = 14;
        } else if (this.ch == 'S' && charAt(this.bp + 1) == 'e' && charAt(this.bp + 2) == 't' && charAt(this.bp + 3) == '[') {
            this.bp += 3;
            this.ch = charAt(this.bp);
            this.token = 21;
        } else {
            nextToken();
        }
        return true;
    }

    public String scanFieldString(char[] fieldName) {
        this.matchStat = 0;
        if (!charArrayCompare(fieldName)) {
            this.matchStat = -2;
            return stringDefaultValue();
        }
        int offset = fieldName.length;
        int offset2 = offset + 1;
        if (charAt(this.bp + offset) != '\"') {
            this.matchStat = -1;
            return stringDefaultValue();
        }
        int endIndex = indexOf('\"', this.bp + fieldName.length + 1);
        if (endIndex == -1) {
            throw new JSONException("unclosed str");
        }
        int startIndex2 = this.bp + fieldName.length + 1;
        String stringVal = subString(startIndex2, endIndex - startIndex2);
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
        int offset3 = offset2 + (endIndex - ((this.bp + fieldName.length) + 1)) + 1;
        int offset4 = offset3 + 1;
        char chLocal = charAt(this.bp + offset3);
        String str = stringVal;
        if (chLocal == ',') {
            this.bp += offset4;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            return str;
        } else if (chLocal == '}') {
            int offset5 = offset4 + 1;
            char chLocal2 = charAt(this.bp + offset4);
            if (chLocal2 == ',') {
                this.token = 16;
                this.bp += offset5;
                this.ch = charAt(this.bp);
            } else if (chLocal2 == ']') {
                this.token = 15;
                this.bp += offset5;
                this.ch = charAt(this.bp);
            } else if (chLocal2 == '}') {
                this.token = 13;
                this.bp += offset5;
                this.ch = charAt(this.bp);
            } else if (chLocal2 == 26) {
                this.token = 20;
                this.bp += offset5 - 1;
                this.ch = JSONLexer.EOI;
            } else {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            this.matchStat = 4;
            return str;
        } else {
            this.matchStat = -1;
            return stringDefaultValue();
        }
    }

    public String scanString(char expectNextChar) {
        this.matchStat = 0;
        int offset = 0 + 1;
        char chLocal = charAt(this.bp + 0);
        if (chLocal == 'n') {
            if (charAt(this.bp + 1) == 'u' && charAt(this.bp + 1 + 1) == 'l' && charAt(this.bp + 1 + 2) == 'l') {
                int offset2 = offset + 3 + 1;
                if (charAt(this.bp + 4) == expectNextChar) {
                    this.bp += 5;
                    this.ch = charAt(this.bp);
                    this.matchStat = 3;
                    return null;
                }
                this.matchStat = -1;
                return null;
            }
            this.matchStat = -1;
            int i = offset;
            return null;
        } else if (chLocal != '\"') {
            this.matchStat = -1;
            int i2 = offset;
            return stringDefaultValue();
        } else {
            int startIndex = this.bp + 1;
            int endIndex = indexOf('\"', startIndex);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }
            String stringVal = subString(this.bp + 1, endIndex - startIndex);
            if (stringVal.indexOf(92) != -1) {
                while (true) {
                    int slashCount = 0;
                    int i3 = endIndex - 1;
                    while (i3 >= 0 && charAt(i3) == '\\') {
                        slashCount++;
                        i3--;
                    }
                    if (slashCount % 2 == 0) {
                        break;
                    }
                    endIndex = indexOf('\"', endIndex + 1);
                }
                int chars_len = endIndex - startIndex;
                stringVal = readString(sub_chars(this.bp + 1, chars_len), chars_len);
            }
            int offset3 = (endIndex - (this.bp + 1)) + 1 + 1;
            int offset4 = offset3 + 1;
            String str = stringVal;
            if (charAt(this.bp + offset3) == expectNextChar) {
                this.bp += offset4;
                this.ch = charAt(this.bp);
                this.matchStat = 3;
                int i4 = offset4;
                return str;
            }
            this.matchStat = -1;
            int i5 = offset4;
            return str;
        }
    }

    public long scanFieldSymbol(char[] fieldName) {
        this.matchStat = 0;
        if (!charArrayCompare(fieldName)) {
            this.matchStat = -2;
            return 0;
        }
        int offset = fieldName.length;
        int offset2 = offset + 1;
        if (charAt(this.bp + offset) != '\"') {
            this.matchStat = -1;
            return 0;
        }
        long hash = -2128831035;
        while (true) {
            int offset3 = offset2;
            offset2 = offset3 + 1;
            char chLocal = charAt(this.bp + offset3);
            if (chLocal == '\"') {
                int offset4 = offset2 + 1;
                char chLocal2 = charAt(this.bp + offset2);
                if (chLocal2 == ',') {
                    this.bp += offset4;
                    this.ch = charAt(this.bp);
                    this.matchStat = 3;
                    return hash;
                } else if (chLocal2 == '}') {
                    int offset5 = offset4 + 1;
                    char chLocal3 = charAt(this.bp + offset4);
                    if (chLocal3 == ',') {
                        this.token = 16;
                        this.bp += offset5;
                        this.ch = charAt(this.bp);
                    } else if (chLocal3 == ']') {
                        this.token = 15;
                        this.bp += offset5;
                        this.ch = charAt(this.bp);
                    } else if (chLocal3 == '}') {
                        this.token = 13;
                        this.bp += offset5;
                        this.ch = charAt(this.bp);
                    } else if (chLocal3 == 26) {
                        this.token = 20;
                        this.bp += offset5 - 1;
                        this.ch = JSONLexer.EOI;
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
            } else {
                hash = (hash ^ ((long) chLocal)) * 16777619;
                if (chLocal == '\\') {
                    this.matchStat = -1;
                    return 0;
                }
            }
        }
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [java.lang.Class<?>, java.lang.Class] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Enum<?> scanEnum(java.lang.Class<?> r3, com.alibaba.fastjson.parser.SymbolTable r4, char r5) {
        /*
            r2 = this;
            java.lang.String r0 = r2.scanSymbolWithSeperator(r4, r5)
            if (r0 != 0) goto L_0x0008
            r1 = 0
        L_0x0007:
            return r1
        L_0x0008:
            java.lang.Enum r1 = java.lang.Enum.valueOf(r3, r0)
            goto L_0x0007
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanEnum(java.lang.Class, com.alibaba.fastjson.parser.SymbolTable, char):java.lang.Enum");
    }

    public String scanSymbolWithSeperator(SymbolTable symbolTable, char serperator) {
        String str = null;
        this.matchStat = 0;
        int offset = 0 + 1;
        char chLocal = charAt(this.bp + 0);
        if (chLocal == 'n') {
            if (charAt(this.bp + 1) == 'u' && charAt(this.bp + 1 + 1) == 'l' && charAt(this.bp + 1 + 2) == 'l') {
                int offset2 = offset + 3 + 1;
                if (charAt(this.bp + 4) == serperator) {
                    this.bp += 5;
                    this.ch = charAt(this.bp);
                    this.matchStat = 3;
                } else {
                    this.matchStat = -1;
                }
            } else {
                this.matchStat = -1;
                int i = offset;
            }
        } else if (chLocal != '\"') {
            this.matchStat = -1;
            int i2 = offset;
        } else {
            int hash = 0;
            while (true) {
                int offset3 = offset;
                offset = offset3 + 1;
                char chLocal2 = charAt(this.bp + offset3);
                if (chLocal2 == '\"') {
                    int start = this.bp + 0 + 1;
                    str = addSymbol(start, ((this.bp + offset) - start) - 1, hash, symbolTable);
                    char chLocal3 = charAt(this.bp + offset);
                    int offset4 = offset + 1;
                    while (true) {
                        if (chLocal3 != serperator) {
                            if (!isWhitespace(chLocal3)) {
                                this.matchStat = -1;
                                int i3 = offset4;
                                break;
                            }
                            chLocal3 = charAt(this.bp + offset4);
                            offset4++;
                        } else {
                            this.bp += offset4;
                            this.ch = charAt(this.bp);
                            this.matchStat = 3;
                            int i4 = offset4;
                            break;
                        }
                    }
                } else {
                    hash = (hash * 31) + chLocal2;
                    if (chLocal2 == '\\') {
                        this.matchStat = -1;
                        int i5 = offset;
                        break;
                    }
                }
            }
        }
        return str;
    }

    public Collection<String> scanFieldStringArray(char[] fieldName, Class<?> type) {
        Collection<String> list;
        int offset;
        int offset2;
        char chLocal;
        int offset3;
        char chLocal2;
        this.matchStat = 0;
        if (!charArrayCompare(fieldName)) {
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
        int offset4 = fieldName.length;
        int offset5 = offset4 + 1;
        if (charAt(this.bp + offset4) != '[') {
            this.matchStat = -1;
            return null;
        }
        int offset6 = offset5 + 1;
        char chLocal3 = charAt(this.bp + offset5);
        while (true) {
            offset = offset6;
            if (chLocal3 != '\"') {
                if (chLocal3 != 'n') {
                    break;
                }
                if (charAt(this.bp + offset) != 'u') {
                    break;
                }
                if (charAt(this.bp + offset + 1) != 'l') {
                    break;
                }
                if (charAt(this.bp + offset + 2) != 'l') {
                    break;
                }
                int offset7 = offset + 3;
                offset3 = offset7 + 1;
                chLocal2 = charAt(this.bp + offset7);
                list.add((Object) null);
            } else {
                int endIndex = indexOf('\"', this.bp + offset);
                if (endIndex == -1) {
                    throw new JSONException("unclosed str");
                }
                int startIndex2 = this.bp + offset;
                String stringVal = subString(startIndex2, endIndex - startIndex2);
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
                    int chars_len = endIndex - (this.bp + offset);
                    stringVal = readString(sub_chars(this.bp + offset, chars_len), chars_len);
                }
                int offset8 = offset + (endIndex - (this.bp + offset)) + 1;
                offset3 = offset8 + 1;
                chLocal2 = charAt(this.bp + offset8);
                list.add(stringVal);
            }
            if (chLocal2 == ',') {
                offset6 = offset3 + 1;
                chLocal3 = charAt(this.bp + offset3);
            } else if (chLocal2 == ']') {
                chLocal = charAt(this.bp + offset3);
                offset2 = offset3 + 1;
            } else {
                this.matchStat = -1;
                return null;
            }
        }
        if (chLocal3 == ']' && list.size() == 0) {
            chLocal = charAt(this.bp + offset);
            offset2 = offset + 1;
            if (chLocal == ',') {
                this.bp += offset2;
                this.ch = charAt(this.bp);
                this.matchStat = 3;
                return list;
            } else if (chLocal == '}') {
                int offset9 = offset2 + 1;
                char chLocal4 = charAt(this.bp + offset2);
                if (chLocal4 == ',') {
                    this.token = 16;
                    this.bp += offset9;
                    this.ch = charAt(this.bp);
                } else if (chLocal4 == ']') {
                    this.token = 15;
                    this.bp += offset9;
                    this.ch = charAt(this.bp);
                } else if (chLocal4 == '}') {
                    this.token = 13;
                    this.bp += offset9;
                    this.ch = charAt(this.bp);
                } else if (chLocal4 == 26) {
                    this.bp += offset9 - 1;
                    this.token = 20;
                    this.ch = JSONLexer.EOI;
                } else {
                    this.matchStat = -1;
                    return null;
                }
                this.matchStat = 4;
                return list;
            } else {
                this.matchStat = -1;
                return null;
            }
        } else {
            throw new JSONException("illega str");
        }
    }

    public void scanStringArray(Collection<String> list, char seperator) {
        int offset;
        char chLocal;
        int offset2;
        char chLocal2;
        this.matchStat = 0;
        int offset3 = 0 + 1;
        char chLocal3 = charAt(this.bp + 0);
        if (chLocal3 == 'n' && charAt(this.bp + 1) == 'u' && charAt(this.bp + 1 + 1) == 'l' && charAt(this.bp + 1 + 2) == 'l' && charAt(this.bp + 1 + 3) == seperator) {
            this.bp += 5;
            this.ch = charAt(this.bp);
            this.matchStat = 5;
            int i = offset3;
        } else if (chLocal3 != '[') {
            this.matchStat = -1;
            int i2 = offset3;
        } else {
            int offset4 = offset3 + 1;
            char chLocal4 = charAt(this.bp + 1);
            while (true) {
                int offset5 = offset4;
                if (chLocal4 != 'n' || charAt(this.bp + offset5) != 'u' || charAt(this.bp + offset5 + 1) != 'l' || charAt(this.bp + offset5 + 2) != 'l') {
                    if (chLocal4 == ']' && list.size() == 0) {
                        offset = offset5 + 1;
                        chLocal = charAt(this.bp + offset5);
                        break;
                    } else if (chLocal4 != '\"') {
                        this.matchStat = -1;
                        int i3 = offset5;
                        return;
                    } else {
                        int startIndex = this.bp + offset5;
                        int endIndex = indexOf('\"', startIndex);
                        if (endIndex == -1) {
                            throw new JSONException("unclosed str");
                        }
                        String stringVal = subString(this.bp + offset5, endIndex - startIndex);
                        if (stringVal.indexOf(92) != -1) {
                            while (true) {
                                int slashCount = 0;
                                int i4 = endIndex - 1;
                                while (i4 >= 0 && charAt(i4) == '\\') {
                                    slashCount++;
                                    i4--;
                                }
                                if (slashCount % 2 == 0) {
                                    break;
                                }
                                endIndex = indexOf('\"', endIndex + 1);
                            }
                            int chars_len = endIndex - startIndex;
                            stringVal = readString(sub_chars(this.bp + offset5, chars_len), chars_len);
                        }
                        int offset6 = offset5 + (endIndex - (this.bp + offset5)) + 1;
                        offset2 = offset6 + 1;
                        chLocal2 = charAt(this.bp + offset6);
                        list.add(stringVal);
                    }
                } else {
                    int offset7 = offset5 + 3;
                    offset2 = offset7 + 1;
                    chLocal2 = charAt(this.bp + offset7);
                    list.add((Object) null);
                }
                if (chLocal2 == ',') {
                    offset4 = offset2 + 1;
                    chLocal4 = charAt(this.bp + offset2);
                } else if (chLocal2 == ']') {
                    offset = offset2 + 1;
                    chLocal = charAt(this.bp + offset2);
                } else {
                    this.matchStat = -1;
                    int i5 = offset2;
                    return;
                }
            }
            if (chLocal == seperator) {
                this.bp += offset;
                this.ch = charAt(this.bp);
                this.matchStat = 3;
                return;
            }
            this.matchStat = -1;
        }
    }

    public int scanFieldInt(char[] fieldName) {
        boolean negative;
        int offset;
        int offset2;
        char chLocal;
        this.matchStat = 0;
        if (!charArrayCompare(fieldName)) {
            this.matchStat = -2;
            return 0;
        }
        int offset3 = fieldName.length;
        int offset4 = offset3 + 1;
        char chLocal2 = charAt(this.bp + offset3);
        if (chLocal2 == '-') {
            negative = true;
        } else {
            negative = false;
        }
        if (negative) {
            offset = offset4 + 1;
            chLocal2 = charAt(this.bp + offset4);
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
            chLocal = charAt(this.bp + offset);
            if (chLocal >= '0' && chLocal <= '9') {
                value = (value * 10) + (chLocal - '0');
                offset = offset2;
            }
        }
        if (chLocal == '.') {
            this.matchStat = -1;
            return 0;
        } else if ((value < 0 || offset2 > fieldName.length + 14) && !(value == Integer.MIN_VALUE && offset2 == 17 && negative)) {
            this.matchStat = -1;
            return 0;
        } else if (chLocal == ',') {
            this.bp += offset2;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            if (negative) {
                return -value;
            }
            return value;
        } else if (chLocal == '}') {
            int offset5 = offset2 + 1;
            char chLocal3 = charAt(this.bp + offset2);
            if (chLocal3 == ',') {
                this.token = 16;
                this.bp += offset5;
                this.ch = charAt(this.bp);
            } else if (chLocal3 == ']') {
                this.token = 15;
                this.bp += offset5;
                this.ch = charAt(this.bp);
            } else if (chLocal3 == '}') {
                this.token = 13;
                this.bp += offset5;
                this.ch = charAt(this.bp);
            } else if (chLocal3 == 26) {
                this.token = 20;
                this.bp += offset5 - 1;
                this.ch = JSONLexer.EOI;
            } else {
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

    /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final int[] scanFieldIntArray(char[] r12) {
        /*
            r11 = this;
            r9 = 0
            r11.matchStat = r9
            boolean r9 = r11.charArrayCompare(r12)
            if (r9 != 0) goto L_0x000e
            r9 = -2
            r11.matchStat = r9
            r0 = 0
        L_0x000d:
            return r0
        L_0x000e:
            int r5 = r12.length
            int r9 = r11.bp
            int r6 = r5 + 1
            int r9 = r9 + r5
            char r3 = r11.charAt(r9)
            r9 = 91
            if (r3 == r9) goto L_0x0021
            r9 = -2
            r11.matchStat = r9
            r0 = 0
            goto L_0x000d
        L_0x0021:
            int r9 = r11.bp
            int r5 = r6 + 1
            int r9 = r9 + r6
            char r3 = r11.charAt(r9)
            r9 = 16
            int[] r0 = new int[r9]
            r1 = 0
            r9 = 93
            if (r3 != r9) goto L_0x013b
            int r9 = r11.bp
            int r6 = r5 + 1
            int r9 = r9 + r5
            char r3 = r11.charAt(r9)
            r5 = r6
        L_0x003d:
            int r9 = r0.length
            if (r1 == r9) goto L_0x0048
            int[] r7 = new int[r1]
            r9 = 0
            r10 = 0
            java.lang.System.arraycopy(r0, r9, r7, r10, r1)
            r0 = r7
        L_0x0048:
            r9 = 44
            if (r3 != r9) goto L_0x00cb
            int r9 = r11.bp
            int r10 = r5 + -1
            int r9 = r9 + r10
            r11.bp = r9
            r11.next()
            r9 = 3
            r11.matchStat = r9
            r9 = 16
            r11.token = r9
            goto L_0x000d
        L_0x005e:
            int r9 = r0.length
            if (r2 < r9) goto L_0x006e
            int r9 = r0.length
            int r9 = r9 * 3
            int r9 = r9 / 2
            int[] r7 = new int[r9]
            r9 = 0
            r10 = 0
            java.lang.System.arraycopy(r0, r9, r7, r10, r2)
            r0 = r7
        L_0x006e:
            int r1 = r2 + 1
            if (r4 == 0) goto L_0x0073
            int r8 = -r8
        L_0x0073:
            r0[r2] = r8
            r9 = 44
            if (r3 != r9) goto L_0x00b6
            int r9 = r11.bp
            int r5 = r6 + 1
            int r9 = r9 + r6
            char r3 = r11.charAt(r9)
        L_0x0082:
            r2 = r1
            r6 = r5
        L_0x0084:
            r4 = 0
            r9 = 45
            if (r3 != r9) goto L_0x0138
            int r9 = r11.bp
            int r5 = r6 + 1
            int r9 = r9 + r6
            char r3 = r11.charAt(r9)
            r4 = 1
        L_0x0093:
            r9 = 48
            if (r3 < r9) goto L_0x00c5
            r9 = 57
            if (r3 > r9) goto L_0x00c5
            int r8 = r3 + -48
        L_0x009d:
            int r9 = r11.bp
            int r6 = r5 + 1
            int r9 = r9 + r5
            char r3 = r11.charAt(r9)
            r9 = 48
            if (r3 < r9) goto L_0x005e
            r9 = 57
            if (r3 > r9) goto L_0x005e
            int r9 = r8 * 10
            int r10 = r3 + -48
            int r8 = r9 + r10
            r5 = r6
            goto L_0x009d
        L_0x00b6:
            r9 = 93
            if (r3 != r9) goto L_0x0135
            int r9 = r11.bp
            int r5 = r6 + 1
            int r9 = r9 + r6
            char r3 = r11.charAt(r9)
            goto L_0x003d
        L_0x00c5:
            r9 = -1
            r11.matchStat = r9
            r0 = 0
            goto L_0x000d
        L_0x00cb:
            r9 = 125(0x7d, float:1.75E-43)
            if (r3 != r9) goto L_0x012f
            int r9 = r11.bp
            int r6 = r5 + 1
            int r9 = r9 + r5
            char r3 = r11.charAt(r9)
            r9 = 44
            if (r3 != r9) goto L_0x00ef
            r9 = 16
            r11.token = r9
            int r9 = r11.bp
            int r10 = r6 + -1
            int r9 = r9 + r10
            r11.bp = r9
            r11.next()
        L_0x00ea:
            r9 = 4
            r11.matchStat = r9
            goto L_0x000d
        L_0x00ef:
            r9 = 93
            if (r3 != r9) goto L_0x0102
            r9 = 15
            r11.token = r9
            int r9 = r11.bp
            int r10 = r6 + -1
            int r9 = r9 + r10
            r11.bp = r9
            r11.next()
            goto L_0x00ea
        L_0x0102:
            r9 = 125(0x7d, float:1.75E-43)
            if (r3 != r9) goto L_0x0115
            r9 = 13
            r11.token = r9
            int r9 = r11.bp
            int r10 = r6 + -1
            int r9 = r9 + r10
            r11.bp = r9
            r11.next()
            goto L_0x00ea
        L_0x0115:
            r9 = 26
            if (r3 != r9) goto L_0x0129
            int r9 = r11.bp
            int r10 = r6 + -1
            int r9 = r9 + r10
            r11.bp = r9
            r9 = 20
            r11.token = r9
            r9 = 26
            r11.ch = r9
            goto L_0x00ea
        L_0x0129:
            r9 = -1
            r11.matchStat = r9
            r0 = 0
            goto L_0x000d
        L_0x012f:
            r9 = -1
            r11.matchStat = r9
            r0 = 0
            goto L_0x000d
        L_0x0135:
            r5 = r6
            goto L_0x0082
        L_0x0138:
            r5 = r6
            goto L_0x0093
        L_0x013b:
            r2 = r1
            r6 = r5
            goto L_0x0084
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldIntArray(char[]):int[]");
    }

    public boolean scanBoolean(char expectNext) {
        this.matchStat = 0;
        int offset = 0 + 1;
        char chLocal = charAt(this.bp + 0);
        boolean value = false;
        if (chLocal == 't') {
            if (charAt(this.bp + 1) == 'r' && charAt(this.bp + 1 + 1) == 'u' && charAt(this.bp + 1 + 2) == 'e') {
                chLocal = charAt(this.bp + 4);
                value = true;
                offset = offset + 3 + 1;
            } else {
                this.matchStat = -1;
                int i = offset;
                return false;
            }
        } else if (chLocal == 'f') {
            if (charAt(this.bp + 1) == 'a' && charAt(this.bp + 1 + 1) == 'l' && charAt(this.bp + 1 + 2) == 's' && charAt(this.bp + 1 + 3) == 'e') {
                chLocal = charAt(this.bp + 5);
                value = false;
                offset = offset + 4 + 1;
            } else {
                this.matchStat = -1;
                int i2 = offset;
                return false;
            }
        } else if (chLocal == '1') {
            chLocal = charAt(this.bp + 1);
            value = true;
            offset++;
        } else if (chLocal == '0') {
            chLocal = charAt(this.bp + 1);
            value = false;
            offset++;
        }
        while (chLocal != expectNext) {
            if (isWhitespace(chLocal)) {
                chLocal = charAt(this.bp + offset);
                offset++;
            } else {
                this.matchStat = -1;
                int i3 = offset;
                return value;
            }
        }
        this.bp += offset;
        this.ch = charAt(this.bp);
        this.matchStat = 3;
        int i4 = offset;
        return value;
    }

    public int scanInt(char expectNext) {
        boolean negative;
        int offset;
        int offset2;
        char chLocal;
        this.matchStat = 0;
        int offset3 = 0 + 1;
        char chLocal2 = charAt(this.bp + 0);
        if (chLocal2 == '-') {
            negative = true;
        } else {
            negative = false;
        }
        if (negative) {
            offset = offset3 + 1;
            chLocal2 = charAt(this.bp + 1);
        } else {
            offset = offset3;
        }
        if (chLocal2 < '0' || chLocal2 > '9') {
            this.matchStat = -1;
            return 0;
        }
        int value = chLocal2 - '0';
        while (true) {
            offset2 = offset + 1;
            chLocal = charAt(this.bp + offset);
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
                    chLocal = charAt(this.bp + offset2);
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
            this.bp += offset2;
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

    public boolean scanFieldBoolean(char[] fieldName) {
        boolean value;
        int offset;
        this.matchStat = 0;
        if (!charArrayCompare(fieldName)) {
            this.matchStat = -2;
            return false;
        }
        int offset2 = fieldName.length;
        int offset3 = offset2 + 1;
        char chLocal = charAt(this.bp + offset2);
        if (chLocal == 't') {
            int offset4 = offset3 + 1;
            if (charAt(this.bp + offset3) != 'r') {
                this.matchStat = -1;
                return false;
            }
            int offset5 = offset4 + 1;
            if (charAt(this.bp + offset4) != 'u') {
                this.matchStat = -1;
                return false;
            }
            offset = offset5 + 1;
            if (charAt(this.bp + offset5) != 'e') {
                this.matchStat = -1;
                return false;
            }
            value = true;
        } else if (chLocal == 'f') {
            int offset6 = offset3 + 1;
            if (charAt(this.bp + offset3) != 'a') {
                this.matchStat = -1;
                return false;
            }
            int offset7 = offset6 + 1;
            if (charAt(this.bp + offset6) != 'l') {
                this.matchStat = -1;
                return false;
            }
            int offset8 = offset7 + 1;
            if (charAt(this.bp + offset7) != 's') {
                this.matchStat = -1;
                return false;
            }
            int offset9 = offset8 + 1;
            if (charAt(this.bp + offset8) != 'e') {
                this.matchStat = -1;
                return false;
            }
            value = false;
            offset = offset9;
        } else {
            this.matchStat = -1;
            return false;
        }
        int offset10 = offset + 1;
        char chLocal2 = charAt(this.bp + offset);
        if (chLocal2 == ',') {
            this.bp += offset10;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return value;
        } else if (chLocal2 == '}') {
            int offset11 = offset10 + 1;
            char chLocal3 = charAt(this.bp + offset10);
            if (chLocal3 == ',') {
                this.token = 16;
                this.bp += offset11;
                this.ch = charAt(this.bp);
            } else if (chLocal3 == ']') {
                this.token = 15;
                this.bp += offset11;
                this.ch = charAt(this.bp);
            } else if (chLocal3 == '}') {
                this.token = 13;
                this.bp += offset11;
                this.ch = charAt(this.bp);
            } else if (chLocal3 == 26) {
                this.token = 20;
                this.bp += offset11 - 1;
                this.ch = JSONLexer.EOI;
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

    public long scanFieldLong(char[] fieldName) {
        int offset;
        int offset2;
        char chLocal;
        this.matchStat = 0;
        if (!charArrayCompare(fieldName)) {
            this.matchStat = -2;
            return 0;
        }
        int offset3 = fieldName.length;
        int offset4 = offset3 + 1;
        char chLocal2 = charAt(this.bp + offset3);
        boolean negative = false;
        if (chLocal2 == '-') {
            offset = offset4 + 1;
            chLocal2 = charAt(this.bp + offset4);
            negative = true;
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
            chLocal = charAt(this.bp + offset);
            if (chLocal >= '0' && chLocal <= '9') {
                value = (10 * value) + ((long) (chLocal - '0'));
                offset = offset2;
            }
        }
        if (chLocal == '.') {
            this.matchStat = -1;
            return 0;
        } else if (value < 0 || offset2 > 21) {
            this.matchStat = -1;
            return 0;
        } else if (chLocal == ',') {
            this.bp += offset2;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            if (negative) {
                return -value;
            }
            return value;
        } else if (chLocal == '}') {
            int offset5 = offset2 + 1;
            char chLocal3 = charAt(this.bp + offset2);
            if (chLocal3 == ',') {
                this.token = 16;
                this.bp += offset5;
                this.ch = charAt(this.bp);
            } else if (chLocal3 == ']') {
                this.token = 15;
                this.bp += offset5;
                this.ch = charAt(this.bp);
            } else if (chLocal3 == '}') {
                this.token = 13;
                this.bp += offset5;
                this.ch = charAt(this.bp);
            } else if (chLocal3 == 26) {
                this.token = 20;
                this.bp += offset5 - 1;
                this.ch = JSONLexer.EOI;
            } else {
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

    public long scanLong(char expectNextChar) {
        int offset;
        int offset2;
        char chLocal;
        this.matchStat = 0;
        int offset3 = 0 + 1;
        char chLocal2 = charAt(this.bp + 0);
        boolean negative = chLocal2 == '-';
        if (negative) {
            offset = offset3 + 1;
            chLocal2 = charAt(this.bp + 1);
        } else {
            offset = offset3;
        }
        if (chLocal2 < '0' || chLocal2 > '9') {
            this.matchStat = -1;
            return 0;
        }
        long value = (long) (chLocal2 - '0');
        while (true) {
            offset2 = offset + 1;
            chLocal = charAt(this.bp + offset);
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
                    chLocal = charAt(this.bp + offset2);
                    offset2++;
                } else {
                    this.matchStat = -1;
                    int i3 = offset2;
                    return value;
                }
            }
            this.bp += offset2;
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

    public final float scanFieldFloat(char[] fieldName) {
        char chLocal;
        this.matchStat = 0;
        if (!charArrayCompare(fieldName)) {
            this.matchStat = -2;
            return 0.0f;
        }
        int offset = fieldName.length;
        int offset2 = offset + 1;
        char chLocal2 = charAt(this.bp + offset);
        if (chLocal2 < '0' || chLocal2 > '9') {
            this.matchStat = -1;
            return 0.0f;
        }
        while (true) {
            int offset3 = offset2;
            offset2 = offset3 + 1;
            chLocal = charAt(this.bp + offset3);
            if (chLocal < '0' || chLocal > '9') {
            }
        }
        if (chLocal == '.') {
            int offset4 = offset2 + 1;
            char chLocal3 = charAt(this.bp + offset2);
            if (chLocal3 >= '0' && chLocal3 <= '9') {
                while (true) {
                    offset2 = offset4 + 1;
                    chLocal = charAt(this.bp + offset4);
                    if (chLocal < '0' || chLocal > '9') {
                        break;
                    }
                    offset4 = offset2;
                }
            } else {
                this.matchStat = -1;
                return 0.0f;
            }
        }
        int offset5 = offset2;
        int start = this.bp + fieldName.length;
        float parseFloat = Float.parseFloat(subString(start, ((this.bp + offset5) - start) - 1));
        if (chLocal == ',') {
            this.bp += offset5;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return parseFloat;
        } else if (chLocal == '}') {
            int offset6 = offset5 + 1;
            char chLocal4 = charAt(this.bp + offset5);
            if (chLocal4 == ',') {
                this.token = 16;
                this.bp += offset6;
                this.ch = charAt(this.bp);
            } else if (chLocal4 == ']') {
                this.token = 15;
                this.bp += offset6;
                this.ch = charAt(this.bp);
            } else if (chLocal4 == '}') {
                this.token = 13;
                this.bp += offset6;
                this.ch = charAt(this.bp);
            } else if (chLocal4 == 26) {
                this.bp += offset6 - 1;
                this.token = 20;
                this.ch = JSONLexer.EOI;
            } else {
                this.matchStat = -1;
                return 0.0f;
            }
            this.matchStat = 4;
            return parseFloat;
        } else {
            this.matchStat = -1;
            return 0.0f;
        }
    }

    public final float scanFloat(char seperator) {
        char chLocal;
        float f = 0.0f;
        this.matchStat = 0;
        int offset = 0 + 1;
        char chLocal2 = charAt(this.bp + 0);
        if (chLocal2 < '0' || chLocal2 > '9') {
            this.matchStat = -1;
            int i = offset;
        } else {
            while (true) {
                int offset2 = offset;
                offset = offset2 + 1;
                chLocal = charAt(this.bp + offset2);
                if (chLocal < '0' || chLocal > '9') {
                }
            }
            if (chLocal == '.') {
                int offset3 = offset + 1;
                char chLocal3 = charAt(this.bp + offset);
                if (chLocal3 >= '0' && chLocal3 <= '9') {
                    while (true) {
                        offset = offset3 + 1;
                        chLocal = charAt(this.bp + offset3);
                        if (chLocal < '0' || chLocal > '9') {
                            break;
                        }
                        offset3 = offset;
                    }
                } else {
                    this.matchStat = -1;
                }
            }
            int offset4 = offset;
            int start = this.bp;
            f = Float.parseFloat(subString(start, ((this.bp + offset4) - start) - 1));
            if (chLocal == seperator) {
                this.bp += offset4;
                this.ch = charAt(this.bp);
                this.matchStat = 3;
                this.token = 16;
            } else {
                this.matchStat = -1;
            }
        }
        return f;
    }

    public final double scanDouble(char seperator) {
        char chLocal;
        double d = ClientTraceData.b.f47a;
        this.matchStat = 0;
        int offset = 0 + 1;
        char chLocal2 = charAt(this.bp + 0);
        if (chLocal2 < '0' || chLocal2 > '9') {
            this.matchStat = -1;
            int i = offset;
        } else {
            while (true) {
                int offset2 = offset;
                offset = offset2 + 1;
                chLocal = charAt(this.bp + offset2);
                if (chLocal < '0' || chLocal > '9') {
                }
            }
            if (chLocal == '.') {
                int offset3 = offset + 1;
                char chLocal3 = charAt(this.bp + offset);
                if (chLocal3 >= '0' && chLocal3 <= '9') {
                    while (true) {
                        offset = offset3 + 1;
                        chLocal = charAt(this.bp + offset3);
                        if (chLocal < '0' || chLocal > '9') {
                            break;
                        }
                        offset3 = offset;
                    }
                } else {
                    this.matchStat = -1;
                }
            }
            int offset4 = offset;
            int start = this.bp;
            d = Double.parseDouble(subString(start, ((this.bp + offset4) - start) - 1));
            if (chLocal == seperator) {
                this.bp += offset4;
                this.ch = charAt(this.bp);
                this.matchStat = 3;
                this.token = 16;
            } else {
                this.matchStat = -1;
            }
        }
        return d;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:116:?, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final float[] scanFieldFloatArray(char[] r22) {
        /*
            r21 = this;
            r19 = 0
            r0 = r19
            r1 = r21
            r1.matchStat = r0
            boolean r19 = r21.charArrayCompare(r22)
            if (r19 != 0) goto L_0x0018
            r19 = -2
            r0 = r19
            r1 = r21
            r1.matchStat = r0
            r3 = 0
        L_0x0017:
            return r3
        L_0x0018:
            r0 = r22
            int r11 = r0.length
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r12 = r11 + 1
            int r19 = r19 + r11
            r0 = r21
            r1 = r19
            char r6 = r0.charAt(r1)
            r19 = 91
            r0 = r19
            if (r6 == r0) goto L_0x003d
            r19 = -2
            r0 = r19
            r1 = r21
            r1.matchStat = r0
            r3 = 0
            goto L_0x0017
        L_0x003d:
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r11 = r12 + 1
            int r19 = r19 + r12
            r0 = r21
            r1 = r19
            char r6 = r0.charAt(r1)
            r19 = 16
            r0 = r19
            float[] r3 = new float[r0]
            r4 = 0
        L_0x0056:
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r19 = r19 + r11
            int r15 = r19 + -1
            r19 = 45
            r0 = r19
            if (r6 != r0) goto L_0x00b0
            r10 = 1
        L_0x0067:
            if (r10 == 0) goto L_0x007c
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r12 = r11 + 1
            int r19 = r19 + r11
            r0 = r21
            r1 = r19
            char r6 = r0.charAt(r1)
            r11 = r12
        L_0x007c:
            r19 = 48
            r0 = r19
            if (r6 < r0) goto L_0x0247
            r19 = 57
            r0 = r19
            if (r6 > r0) goto L_0x0247
            int r9 = r6 + -48
        L_0x008a:
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r12 = r11 + 1
            int r19 = r19 + r11
            r0 = r21
            r1 = r19
            char r6 = r0.charAt(r1)
            r19 = 48
            r0 = r19
            if (r6 < r0) goto L_0x00b2
            r19 = 57
            r0 = r19
            if (r6 > r0) goto L_0x00b2
            int r19 = r9 * 10
            int r20 = r6 + -48
            int r9 = r19 + r20
            r11 = r12
            goto L_0x008a
        L_0x00b0:
            r10 = 0
            goto L_0x0067
        L_0x00b2:
            r13 = 1
            r19 = 46
            r0 = r19
            if (r6 != r0) goto L_0x010a
            r14 = 1
        L_0x00ba:
            if (r14 == 0) goto L_0x0117
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r11 = r12 + 1
            int r19 = r19 + r12
            r0 = r21
            r1 = r19
            char r6 = r0.charAt(r1)
            int r13 = r13 * 10
            r19 = 48
            r0 = r19
            if (r6 < r0) goto L_0x010c
            r19 = 57
            r0 = r19
            if (r6 > r0) goto L_0x010c
            int r19 = r9 * 10
            int r20 = r6 + -48
            int r9 = r19 + r20
        L_0x00e2:
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r12 = r11 + 1
            int r19 = r19 + r11
            r0 = r21
            r1 = r19
            char r6 = r0.charAt(r1)
            r19 = 48
            r0 = r19
            if (r6 < r0) goto L_0x0117
            r19 = 57
            r0 = r19
            if (r6 > r0) goto L_0x0117
            int r19 = r9 * 10
            int r20 = r6 + -48
            int r9 = r19 + r20
            int r13 = r13 * 10
            r11 = r12
            goto L_0x00e2
        L_0x010a:
            r14 = 0
            goto L_0x00ba
        L_0x010c:
            r19 = -1
            r0 = r19
            r1 = r21
            r1.matchStat = r0
            r3 = 0
            goto L_0x0017
        L_0x0117:
            r19 = 101(0x65, float:1.42E-43)
            r0 = r19
            if (r6 == r0) goto L_0x0123
            r19 = 69
            r0 = r19
            if (r6 != r0) goto L_0x0176
        L_0x0123:
            r8 = 1
        L_0x0124:
            if (r8 == 0) goto L_0x0178
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r11 = r12 + 1
            int r19 = r19 + r12
            r0 = r21
            r1 = r19
            char r6 = r0.charAt(r1)
            r19 = 43
            r0 = r19
            if (r6 == r0) goto L_0x0144
            r19 = 45
            r0 = r19
            if (r6 != r0) goto L_0x0316
        L_0x0144:
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r12 = r11 + 1
            int r19 = r19 + r11
            r0 = r21
            r1 = r19
            char r6 = r0.charAt(r1)
        L_0x0156:
            r19 = 48
            r0 = r19
            if (r6 < r0) goto L_0x0178
            r19 = 57
            r0 = r19
            if (r6 > r0) goto L_0x0178
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r11 = r12 + 1
            int r19 = r19 + r12
            r0 = r21
            r1 = r19
            char r6 = r0.charAt(r1)
            r12 = r11
            goto L_0x0156
        L_0x0176:
            r8 = 0
            goto L_0x0124
        L_0x0178:
            r11 = r12
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r19 = r19 + r11
            int r19 = r19 - r15
            int r7 = r19 + -1
            if (r8 != 0) goto L_0x01df
            r19 = 10
            r0 = r19
            if (r7 >= r0) goto L_0x01df
            float r0 = (float) r9
            r19 = r0
            float r0 = (float) r13
            r20 = r0
            float r18 = r19 / r20
            if (r10 == 0) goto L_0x019c
            r0 = r18
            float r0 = -r0
            r18 = r0
        L_0x019c:
            int r0 = r3.length
            r19 = r0
            r0 = r19
            if (r4 < r0) goto L_0x01bf
            int r0 = r3.length
            r19 = r0
            int r19 = r19 * 3
            int r19 = r19 / 2
            r0 = r19
            float[] r0 = new float[r0]
            r17 = r0
            r19 = 0
            r20 = 0
            r0 = r19
            r1 = r17
            r2 = r20
            java.lang.System.arraycopy(r3, r0, r1, r2, r4)
            r3 = r17
        L_0x01bf:
            int r5 = r4 + 1
            r3[r4] = r18
            r19 = 44
            r0 = r19
            if (r6 != r0) goto L_0x01ea
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r12 = r11 + 1
            int r19 = r19 + r11
            r0 = r21
            r1 = r19
            char r6 = r0.charAt(r1)
            r11 = r12
        L_0x01dc:
            r4 = r5
            goto L_0x0056
        L_0x01df:
            r0 = r21
            java.lang.String r16 = r0.subString(r15, r7)
            float r18 = java.lang.Float.parseFloat(r16)
            goto L_0x019c
        L_0x01ea:
            r19 = 93
            r0 = r19
            if (r6 != r0) goto L_0x01dc
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r12 = r11 + 1
            int r19 = r19 + r11
            r0 = r21
            r1 = r19
            char r6 = r0.charAt(r1)
            int r0 = r3.length
            r19 = r0
            r0 = r19
            if (r5 == r0) goto L_0x021c
            float[] r0 = new float[r5]
            r17 = r0
            r19 = 0
            r20 = 0
            r0 = r19
            r1 = r17
            r2 = r20
            java.lang.System.arraycopy(r3, r0, r1, r2, r5)
            r3 = r17
        L_0x021c:
            r19 = 44
            r0 = r19
            if (r6 != r0) goto L_0x0252
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r20 = r12 + -1
            int r19 = r19 + r20
            r0 = r19
            r1 = r21
            r1.bp = r0
            r21.next()
            r19 = 3
            r0 = r19
            r1 = r21
            r1.matchStat = r0
            r19 = 16
            r0 = r19
            r1 = r21
            r1.token = r0
            goto L_0x0017
        L_0x0247:
            r19 = -1
            r0 = r19
            r1 = r21
            r1.matchStat = r0
            r3 = 0
            goto L_0x0017
        L_0x0252:
            r19 = 125(0x7d, float:1.75E-43)
            r0 = r19
            if (r6 != r0) goto L_0x030b
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r11 = r12 + 1
            int r19 = r19 + r12
            r0 = r21
            r1 = r19
            char r6 = r0.charAt(r1)
            r19 = 44
            r0 = r19
            if (r6 != r0) goto L_0x0295
            r19 = 16
            r0 = r19
            r1 = r21
            r1.token = r0
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r20 = r11 + -1
            int r19 = r19 + r20
            r0 = r19
            r1 = r21
            r1.bp = r0
            r21.next()
        L_0x028b:
            r19 = 4
            r0 = r19
            r1 = r21
            r1.matchStat = r0
            goto L_0x0017
        L_0x0295:
            r19 = 93
            r0 = r19
            if (r6 != r0) goto L_0x02b7
            r19 = 15
            r0 = r19
            r1 = r21
            r1.token = r0
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r20 = r11 + -1
            int r19 = r19 + r20
            r0 = r19
            r1 = r21
            r1.bp = r0
            r21.next()
            goto L_0x028b
        L_0x02b7:
            r19 = 125(0x7d, float:1.75E-43)
            r0 = r19
            if (r6 != r0) goto L_0x02d9
            r19 = 13
            r0 = r19
            r1 = r21
            r1.token = r0
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r20 = r11 + -1
            int r19 = r19 + r20
            r0 = r19
            r1 = r21
            r1.bp = r0
            r21.next()
            goto L_0x028b
        L_0x02d9:
            r19 = 26
            r0 = r19
            if (r6 != r0) goto L_0x0300
            r0 = r21
            int r0 = r0.bp
            r19 = r0
            int r20 = r11 + -1
            int r19 = r19 + r20
            r0 = r19
            r1 = r21
            r1.bp = r0
            r19 = 20
            r0 = r19
            r1 = r21
            r1.token = r0
            r19 = 26
            r0 = r19
            r1 = r21
            r1.ch = r0
            goto L_0x028b
        L_0x0300:
            r19 = -1
            r0 = r19
            r1 = r21
            r1.matchStat = r0
            r3 = 0
            goto L_0x0017
        L_0x030b:
            r19 = -1
            r0 = r19
            r1 = r21
            r1.matchStat = r0
            r3 = 0
            goto L_0x0017
        L_0x0316:
            r12 = r11
            goto L_0x0156
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldFloatArray(char[]):float[]");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:132:?, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final float[][] scanFieldFloatArray2(char[] r24) {
        /*
            r23 = this;
            r21 = 0
            r0 = r21
            r1 = r23
            r1.matchStat = r0
            boolean r21 = r23.charArrayCompare(r24)
            if (r21 != 0) goto L_0x001b
            r21 = -2
            r0 = r21
            r1 = r23
            r1.matchStat = r0
            r21 = 0
            float[][] r21 = (float[][]) r21
        L_0x001a:
            return r21
        L_0x001b:
            r0 = r24
            int r14 = r0.length
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r15 = r14 + 1
            int r21 = r21 + r14
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
            r21 = 91
            r0 = r21
            if (r9 == r0) goto L_0x0043
            r21 = -2
            r0 = r21
            r1 = r23
            r1.matchStat = r0
            r21 = 0
            float[][] r21 = (float[][]) r21
            goto L_0x001a
        L_0x0043:
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r14 = r15 + 1
            int r21 = r21 + r15
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
            r21 = 16
            r0 = r21
            float[][] r6 = new float[r0][]
            r7 = 0
            r8 = r7
            r15 = r14
        L_0x005e:
            r21 = 91
            r0 = r21
            if (r9 != r0) goto L_0x005e
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r14 = r15 + 1
            int r21 = r21 + r15
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
            r21 = 16
            r0 = r21
            float[] r3 = new float[r0]
            r4 = 0
        L_0x007d:
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r21 = r21 + r14
            int r17 = r21 + -1
            r21 = 45
            r0 = r21
            if (r9 != r0) goto L_0x00d7
            r13 = 1
        L_0x008e:
            if (r13 == 0) goto L_0x00a3
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r15 = r14 + 1
            int r21 = r21 + r14
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
            r14 = r15
        L_0x00a3:
            r21 = 48
            r0 = r21
            if (r9 < r0) goto L_0x0289
            r21 = 57
            r0 = r21
            if (r9 > r0) goto L_0x0289
            int r12 = r9 + -48
        L_0x00b1:
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r15 = r14 + 1
            int r21 = r21 + r14
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
            r21 = 48
            r0 = r21
            if (r9 < r0) goto L_0x00d9
            r21 = 57
            r0 = r21
            if (r9 > r0) goto L_0x00d9
            int r21 = r12 * 10
            int r22 = r9 + -48
            int r12 = r21 + r22
            r14 = r15
            goto L_0x00b1
        L_0x00d7:
            r13 = 0
            goto L_0x008e
        L_0x00d9:
            r16 = 1
            r21 = 46
            r0 = r21
            if (r9 != r0) goto L_0x013d
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r14 = r15 + 1
            int r21 = r21 + r15
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
            r21 = 48
            r0 = r21
            if (r9 < r0) goto L_0x012f
            r21 = 57
            r0 = r21
            if (r9 > r0) goto L_0x012f
            int r21 = r12 * 10
            int r22 = r9 + -48
            int r12 = r21 + r22
            int r16 = r16 * 10
        L_0x0107:
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r15 = r14 + 1
            int r21 = r21 + r14
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
            r21 = 48
            r0 = r21
            if (r9 < r0) goto L_0x013d
            r21 = 57
            r0 = r21
            if (r9 > r0) goto L_0x013d
            int r21 = r12 * 10
            int r22 = r9 + -48
            int r12 = r21 + r22
            int r16 = r16 * 10
            r14 = r15
            goto L_0x0107
        L_0x012f:
            r21 = -1
            r0 = r21
            r1 = r23
            r1.matchStat = r0
            r21 = 0
            float[][] r21 = (float[][]) r21
            goto L_0x001a
        L_0x013d:
            r21 = 101(0x65, float:1.42E-43)
            r0 = r21
            if (r9 == r0) goto L_0x0149
            r21 = 69
            r0 = r21
            if (r9 != r0) goto L_0x019c
        L_0x0149:
            r11 = 1
        L_0x014a:
            if (r11 == 0) goto L_0x019e
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r14 = r15 + 1
            int r21 = r21 + r15
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
            r21 = 43
            r0 = r21
            if (r9 == r0) goto L_0x016a
            r21 = 45
            r0 = r21
            if (r9 != r0) goto L_0x03c5
        L_0x016a:
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r15 = r14 + 1
            int r21 = r21 + r14
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
        L_0x017c:
            r21 = 48
            r0 = r21
            if (r9 < r0) goto L_0x019e
            r21 = 57
            r0 = r21
            if (r9 > r0) goto L_0x019e
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r14 = r15 + 1
            int r21 = r21 + r15
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
            r15 = r14
            goto L_0x017c
        L_0x019c:
            r11 = 0
            goto L_0x014a
        L_0x019e:
            r14 = r15
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r21 = r21 + r14
            int r21 = r21 - r17
            int r10 = r21 + -1
            if (r11 != 0) goto L_0x0207
            r21 = 10
            r0 = r21
            if (r10 >= r0) goto L_0x0207
            float r0 = (float) r12
            r21 = r0
            r0 = r16
            float r0 = (float) r0
            r22 = r0
            float r20 = r21 / r22
            if (r13 == 0) goto L_0x01c4
            r0 = r20
            float r0 = -r0
            r20 = r0
        L_0x01c4:
            int r0 = r3.length
            r21 = r0
            r0 = r21
            if (r4 < r0) goto L_0x01e7
            int r0 = r3.length
            r21 = r0
            int r21 = r21 * 3
            int r21 = r21 / 2
            r0 = r21
            float[] r0 = new float[r0]
            r19 = r0
            r21 = 0
            r22 = 0
            r0 = r21
            r1 = r19
            r2 = r22
            java.lang.System.arraycopy(r3, r0, r1, r2, r4)
            r3 = r19
        L_0x01e7:
            int r5 = r4 + 1
            r3[r4] = r20
            r21 = 44
            r0 = r21
            if (r9 != r0) goto L_0x0214
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r15 = r14 + 1
            int r21 = r21 + r14
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
            r14 = r15
        L_0x0204:
            r4 = r5
            goto L_0x007d
        L_0x0207:
            r0 = r23
            r1 = r17
            java.lang.String r18 = r0.subString(r1, r10)
            float r20 = java.lang.Float.parseFloat(r18)
            goto L_0x01c4
        L_0x0214:
            r21 = 93
            r0 = r21
            if (r9 != r0) goto L_0x0204
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r15 = r14 + 1
            int r21 = r21 + r14
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
            int r0 = r3.length
            r21 = r0
            r0 = r21
            if (r5 == r0) goto L_0x0246
            float[] r0 = new float[r5]
            r19 = r0
            r21 = 0
            r22 = 0
            r0 = r21
            r1 = r19
            r2 = r22
            java.lang.System.arraycopy(r3, r0, r1, r2, r5)
            r3 = r19
        L_0x0246:
            int r0 = r6.length
            r21 = r0
            r0 = r21
            if (r8 < r0) goto L_0x0269
            int r0 = r6.length
            r21 = r0
            int r21 = r21 * 3
            int r21 = r21 / 2
            r0 = r21
            float[][] r0 = new float[r0][]
            r19 = r0
            r21 = 0
            r22 = 0
            r0 = r21
            r1 = r19
            r2 = r22
            java.lang.System.arraycopy(r3, r0, r1, r2, r5)
            r6 = r19
        L_0x0269:
            int r7 = r8 + 1
            r6[r8] = r3
            r21 = 44
            r0 = r21
            if (r9 != r0) goto L_0x0297
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r14 = r15 + 1
            int r21 = r21 + r15
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
        L_0x0285:
            r8 = r7
            r15 = r14
            goto L_0x005e
        L_0x0289:
            r21 = -1
            r0 = r21
            r1 = r23
            r1.matchStat = r0
            r21 = 0
            float[][] r21 = (float[][]) r21
            goto L_0x001a
        L_0x0297:
            r21 = 93
            r0 = r21
            if (r9 != r0) goto L_0x03c2
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r14 = r15 + 1
            int r21 = r21 + r15
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
            int r0 = r6.length
            r21 = r0
            r0 = r21
            if (r7 == r0) goto L_0x02c9
            float[][] r0 = new float[r7][]
            r19 = r0
            r21 = 0
            r22 = 0
            r0 = r21
            r1 = r19
            r2 = r22
            java.lang.System.arraycopy(r6, r0, r1, r2, r7)
            r6 = r19
        L_0x02c9:
            r21 = 44
            r0 = r21
            if (r9 != r0) goto L_0x02f6
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r22 = r14 + -1
            int r21 = r21 + r22
            r0 = r21
            r1 = r23
            r1.bp = r0
            r23.next()
            r21 = 3
            r0 = r21
            r1 = r23
            r1.matchStat = r0
            r21 = 16
            r0 = r21
            r1 = r23
            r1.token = r0
            r21 = r6
            goto L_0x001a
        L_0x02f6:
            r21 = 125(0x7d, float:1.75E-43)
            r0 = r21
            if (r9 != r0) goto L_0x03b4
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r15 = r14 + 1
            int r21 = r21 + r14
            r0 = r23
            r1 = r21
            char r9 = r0.charAt(r1)
            r21 = 44
            r0 = r21
            if (r9 != r0) goto L_0x033b
            r21 = 16
            r0 = r21
            r1 = r23
            r1.token = r0
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r22 = r15 + -1
            int r21 = r21 + r22
            r0 = r21
            r1 = r23
            r1.bp = r0
            r23.next()
        L_0x032f:
            r21 = 4
            r0 = r21
            r1 = r23
            r1.matchStat = r0
            r21 = r6
            goto L_0x001a
        L_0x033b:
            r21 = 93
            r0 = r21
            if (r9 != r0) goto L_0x035d
            r21 = 15
            r0 = r21
            r1 = r23
            r1.token = r0
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r22 = r15 + -1
            int r21 = r21 + r22
            r0 = r21
            r1 = r23
            r1.bp = r0
            r23.next()
            goto L_0x032f
        L_0x035d:
            r21 = 125(0x7d, float:1.75E-43)
            r0 = r21
            if (r9 != r0) goto L_0x037f
            r21 = 13
            r0 = r21
            r1 = r23
            r1.token = r0
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r22 = r15 + -1
            int r21 = r21 + r22
            r0 = r21
            r1 = r23
            r1.bp = r0
            r23.next()
            goto L_0x032f
        L_0x037f:
            r21 = 26
            r0 = r21
            if (r9 != r0) goto L_0x03a6
            r0 = r23
            int r0 = r0.bp
            r21 = r0
            int r22 = r15 + -1
            int r21 = r21 + r22
            r0 = r21
            r1 = r23
            r1.bp = r0
            r21 = 20
            r0 = r21
            r1 = r23
            r1.token = r0
            r21 = 26
            r0 = r21
            r1 = r23
            r1.ch = r0
            goto L_0x032f
        L_0x03a6:
            r21 = -1
            r0 = r21
            r1 = r23
            r1.matchStat = r0
            r21 = 0
            float[][] r21 = (float[][]) r21
            goto L_0x001a
        L_0x03b4:
            r21 = -1
            r0 = r21
            r1 = r23
            r1.matchStat = r0
            r21 = 0
            float[][] r21 = (float[][]) r21
            goto L_0x001a
        L_0x03c2:
            r14 = r15
            goto L_0x0285
        L_0x03c5:
            r15 = r14
            goto L_0x017c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONLexerBase.scanFieldFloatArray2(char[]):float[][]");
    }

    public final double scanFieldDouble(char[] fieldName) {
        char chLocal;
        int offset;
        char chLocal2;
        this.matchStat = 0;
        if (!charArrayCompare(fieldName)) {
            this.matchStat = -2;
            return ClientTraceData.b.f47a;
        }
        int offset2 = fieldName.length;
        int offset3 = offset2 + 1;
        char chLocal3 = charAt(this.bp + offset2);
        if (chLocal3 < '0' || chLocal3 > '9') {
            this.matchStat = -1;
            return ClientTraceData.b.f47a;
        }
        while (true) {
            int offset4 = offset3;
            offset3 = offset4 + 1;
            chLocal = charAt(this.bp + offset4);
            if (chLocal < '0' || chLocal > '9') {
            }
        }
        if (chLocal == '.') {
            int offset5 = offset3 + 1;
            char chLocal4 = charAt(this.bp + offset3);
            if (chLocal4 >= '0' && chLocal4 <= '9') {
                while (true) {
                    offset3 = offset5 + 1;
                    chLocal = charAt(this.bp + offset5);
                    if (chLocal < '0' || chLocal > '9') {
                        break;
                    }
                    offset5 = offset3;
                }
            } else {
                this.matchStat = -1;
                return ClientTraceData.b.f47a;
            }
        }
        if (chLocal2 == 'e' || chLocal2 == 'E') {
            int offset6 = offset + 1;
            chLocal2 = charAt(this.bp + offset);
            if (chLocal2 == '+' || chLocal2 == '-') {
                offset = offset6 + 1;
                chLocal2 = charAt(this.bp + offset6);
            } else {
                offset = offset6;
            }
            while (chLocal2 >= '0' && chLocal2 <= '9') {
                chLocal2 = charAt(this.bp + offset);
                offset++;
            }
        }
        int offset7 = offset;
        int start = this.bp + fieldName.length;
        double parseDouble = Double.parseDouble(subString(start, ((this.bp + offset7) - start) - 1));
        if (chLocal2 == ',') {
            this.bp += offset7;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            return parseDouble;
        } else if (chLocal2 == '}') {
            int offset8 = offset7 + 1;
            char chLocal5 = charAt(this.bp + offset7);
            if (chLocal5 == ',') {
                this.token = 16;
                this.bp += offset8;
                this.ch = charAt(this.bp);
            } else if (chLocal5 == ']') {
                this.token = 15;
                this.bp += offset8;
                this.ch = charAt(this.bp);
            } else if (chLocal5 == '}') {
                this.token = 13;
                this.bp += offset8;
                this.ch = charAt(this.bp);
            } else if (chLocal5 == 26) {
                this.token = 20;
                this.bp += offset8 - 1;
                this.ch = JSONLexer.EOI;
            } else {
                this.matchStat = -1;
                return ClientTraceData.b.f47a;
            }
            this.matchStat = 4;
            return parseDouble;
        } else {
            this.matchStat = -1;
            return ClientTraceData.b.f47a;
        }
    }

    public final void scanTrue() {
        if (this.ch != 't') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'r') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'u') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse true");
        }
        next();
        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == 10 || this.ch == 13 || this.ch == 9 || this.ch == 26 || this.ch == 12 || this.ch == 8 || this.ch == ':' || this.ch == '/') {
            this.token = 6;
            return;
        }
        throw new JSONException("scan true error");
    }

    public final void scanNullOrNew() {
        if (this.ch != 'n') {
            throw new JSONException("error parse null or new");
        }
        next();
        if (this.ch == 'u') {
            next();
            if (this.ch != 'l') {
                throw new JSONException("error parse null");
            }
            next();
            if (this.ch != 'l') {
                throw new JSONException("error parse null");
            }
            next();
            if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == 10 || this.ch == 13 || this.ch == 9 || this.ch == 26 || this.ch == 12 || this.ch == 8) {
                this.token = 8;
                return;
            }
            throw new JSONException("scan null error");
        } else if (this.ch != 'e') {
            throw new JSONException("error parse new");
        } else {
            next();
            if (this.ch != 'w') {
                throw new JSONException("error parse new");
            }
            next();
            if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == 10 || this.ch == 13 || this.ch == 9 || this.ch == 26 || this.ch == 12 || this.ch == 8) {
                this.token = 9;
                return;
            }
            throw new JSONException("scan new error");
        }
    }

    public final void scanFalse() {
        if (this.ch != 'f') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'a') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'l') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 's') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch != 'e') {
            throw new JSONException("error parse false");
        }
        next();
        if (this.ch == ' ' || this.ch == ',' || this.ch == '}' || this.ch == ']' || this.ch == 10 || this.ch == 13 || this.ch == 9 || this.ch == 26 || this.ch == 12 || this.ch == 8 || this.ch == ':' || this.ch == '/') {
            this.token = 7;
            return;
        }
        throw new JSONException("scan false error");
    }

    public final void scanIdent() {
        this.np = this.bp - 1;
        this.hasSpecial = false;
        do {
            this.sp++;
            next();
        } while (Character.isLetterOrDigit(this.ch));
        String ident = stringVal();
        if (Constant.NULL.equalsIgnoreCase(ident)) {
            this.token = 8;
        } else if ("new".equals(ident)) {
            this.token = 9;
        } else if ("true".equals(ident)) {
            this.token = 6;
        } else if ("false".equals(ident)) {
            this.token = 7;
        } else if ("undefined".equals(ident)) {
            this.token = 23;
        } else if ("Set".equals(ident)) {
            this.token = 21;
        } else if ("TreeSet".equals(ident)) {
            this.token = 22;
        } else {
            this.token = 18;
        }
    }

    public static String readString(char[] chars, int chars_len) {
        int len;
        char[] sbuf2 = new char[chars_len];
        int i = 0;
        int len2 = 0;
        while (i < chars_len) {
            char ch2 = chars[i];
            if (ch2 != '\\') {
                len = len2 + 1;
                sbuf2[len2] = ch2;
            } else {
                i++;
                switch (chars[i]) {
                    case '\"':
                        len = len2 + 1;
                        sbuf2[len2] = '\"';
                        break;
                    case '\'':
                        len = len2 + 1;
                        sbuf2[len2] = '\'';
                        break;
                    case '/':
                        len = len2 + 1;
                        sbuf2[len2] = '/';
                        break;
                    case '0':
                        len = len2 + 1;
                        sbuf2[len2] = 0;
                        break;
                    case '1':
                        len = len2 + 1;
                        sbuf2[len2] = 1;
                        break;
                    case '2':
                        len = len2 + 1;
                        sbuf2[len2] = 2;
                        break;
                    case '3':
                        len = len2 + 1;
                        sbuf2[len2] = 3;
                        break;
                    case '4':
                        len = len2 + 1;
                        sbuf2[len2] = 4;
                        break;
                    case '5':
                        len = len2 + 1;
                        sbuf2[len2] = 5;
                        break;
                    case '6':
                        len = len2 + 1;
                        sbuf2[len2] = 6;
                        break;
                    case '7':
                        len = len2 + 1;
                        sbuf2[len2] = 7;
                        break;
                    case 'F':
                    case 'f':
                        len = len2 + 1;
                        sbuf2[len2] = CsvReader.Letters.FORM_FEED;
                        break;
                    case '\\':
                        len = len2 + 1;
                        sbuf2[len2] = '\\';
                        break;
                    case 'b':
                        len = len2 + 1;
                        sbuf2[len2] = 8;
                        break;
                    case 'n':
                        len = len2 + 1;
                        sbuf2[len2] = 10;
                        break;
                    case 'r':
                        len = len2 + 1;
                        sbuf2[len2] = 13;
                        break;
                    case 't':
                        len = len2 + 1;
                        sbuf2[len2] = 9;
                        break;
                    case 'u':
                        len = len2 + 1;
                        int i2 = i + 1;
                        int i3 = i2 + 1;
                        int i4 = i3 + 1;
                        i = i4 + 1;
                        sbuf2[len2] = (char) Integer.parseInt(new String(new char[]{chars[i2], chars[i3], chars[i4], chars[i]}), 16);
                        break;
                    case 'v':
                        len = len2 + 1;
                        sbuf2[len2] = CsvReader.Letters.VERTICAL_TAB;
                        break;
                    case Opcodes.INVOKE_INTERFACE_RANGE /*120*/:
                        len = len2 + 1;
                        int i5 = i + 1;
                        i = i5 + 1;
                        sbuf2[len2] = (char) ((digits[chars[i5]] * 16) + digits[chars[i]]);
                        break;
                    default:
                        throw new JSONException("unclosed.str.lit");
                }
            }
            i++;
            len2 = len;
        }
        return new String(sbuf2, 0, len2);
    }

    public boolean isBlankInput() {
        int i = 0;
        while (true) {
            char chLocal = charAt(i);
            if (chLocal == 26) {
                this.token = 20;
                return true;
            } else if (!isWhitespace(chLocal)) {
                return false;
            } else {
                i++;
            }
        }
    }

    public final void skipWhitespace() {
        while (this.ch <= '/') {
            if (this.ch == ' ' || this.ch == 13 || this.ch == 10 || this.ch == 9 || this.ch == 12 || this.ch == 8) {
                next();
            } else if (this.ch == '/') {
                skipComment();
            } else {
                return;
            }
        }
    }

    private void scanStringSingleQuote() {
        this.np = this.bp;
        this.hasSpecial = false;
        while (true) {
            char chLocal = next();
            if (chLocal == '\'') {
                this.token = 4;
                next();
                return;
            } else if (chLocal == 26) {
                if (!isEOF()) {
                    putChar(JSONLexer.EOI);
                } else {
                    throw new JSONException("unclosed single-quote string");
                }
            } else if (chLocal == '\\') {
                if (!this.hasSpecial) {
                    this.hasSpecial = true;
                    if (this.sp > this.sbuf.length) {
                        char[] newsbuf = new char[(this.sp * 2)];
                        System.arraycopy(this.sbuf, 0, newsbuf, 0, this.sbuf.length);
                        this.sbuf = newsbuf;
                    }
                    copyTo(this.np + 1, this.sp, this.sbuf);
                }
                char chLocal2 = next();
                switch (chLocal2) {
                    case '\"':
                        putChar('\"');
                        break;
                    case '\'':
                        putChar('\'');
                        break;
                    case '/':
                        putChar('/');
                        break;
                    case '0':
                        putChar(0);
                        break;
                    case '1':
                        putChar(1);
                        break;
                    case '2':
                        putChar(2);
                        break;
                    case '3':
                        putChar(3);
                        break;
                    case '4':
                        putChar(4);
                        break;
                    case '5':
                        putChar(5);
                        break;
                    case '6':
                        putChar(6);
                        break;
                    case '7':
                        putChar(7);
                        break;
                    case 'F':
                    case 'f':
                        putChar(CsvReader.Letters.FORM_FEED);
                        break;
                    case '\\':
                        putChar('\\');
                        break;
                    case 'b':
                        putChar(8);
                        break;
                    case 'n':
                        putChar(10);
                        break;
                    case 'r':
                        putChar(13);
                        break;
                    case 't':
                        putChar(9);
                        break;
                    case 'u':
                        putChar((char) Integer.parseInt(new String(new char[]{next(), next(), next(), next()}), 16));
                        break;
                    case 'v':
                        putChar(CsvReader.Letters.VERTICAL_TAB);
                        break;
                    case Opcodes.INVOKE_INTERFACE_RANGE /*120*/:
                        putChar((char) ((digits[next()] * 16) + digits[next()]));
                        break;
                    default:
                        this.ch = chLocal2;
                        throw new JSONException("unclosed single-quote string");
                }
            } else if (!this.hasSpecial) {
                this.sp++;
            } else if (this.sp == this.sbuf.length) {
                putChar(chLocal);
            } else {
                char[] cArr = this.sbuf;
                int i = this.sp;
                this.sp = i + 1;
                cArr[i] = chLocal;
            }
        }
    }

    /* access modifiers changed from: protected */
    public final void putChar(char ch2) {
        if (this.sp == this.sbuf.length) {
            char[] newsbuf = new char[(this.sbuf.length * 2)];
            System.arraycopy(this.sbuf, 0, newsbuf, 0, this.sbuf.length);
            this.sbuf = newsbuf;
        }
        char[] cArr = this.sbuf;
        int i = this.sp;
        this.sp = i + 1;
        cArr[i] = ch2;
    }

    public final void scanNumber() {
        this.np = this.bp;
        if (this.ch == '-') {
            this.sp++;
            next();
        }
        while (this.ch >= '0' && this.ch <= '9') {
            this.sp++;
            next();
        }
        boolean isDouble = false;
        if (this.ch == '.') {
            this.sp++;
            next();
            isDouble = true;
            while (this.ch >= '0' && this.ch <= '9') {
                this.sp++;
                next();
            }
        }
        if (this.ch == 'L') {
            this.sp++;
            next();
        } else if (this.ch == 'S') {
            this.sp++;
            next();
        } else if (this.ch == 'B') {
            this.sp++;
            next();
        } else if (this.ch == 'F') {
            this.sp++;
            next();
            isDouble = true;
        } else if (this.ch == 'D') {
            this.sp++;
            next();
            isDouble = true;
        } else if (this.ch == 'e' || this.ch == 'E') {
            this.sp++;
            next();
            if (this.ch == '+' || this.ch == '-') {
                this.sp++;
                next();
            }
            while (this.ch >= '0' && this.ch <= '9') {
                this.sp++;
                next();
            }
            if (this.ch == 'D' || this.ch == 'F') {
                this.sp++;
                next();
            }
            isDouble = true;
        }
        if (isDouble) {
            this.token = 3;
        } else {
            this.token = 2;
        }
    }

    public final long longValue() throws NumberFormatException {
        long limit;
        int i;
        int i2;
        long result = 0;
        boolean negative = false;
        if (this.np == -1) {
            this.np = 0;
        }
        int i3 = this.np;
        int max = this.np + this.sp;
        if (charAt(this.np) == '-') {
            negative = true;
            limit = Long.MIN_VALUE;
            i = i3 + 1;
        } else {
            limit = -9223372036854775807L;
            i = i3;
        }
        if (i < max) {
            result = (long) (-(charAt(i) - '0'));
            i++;
        }
        while (true) {
            if (i >= max) {
                i2 = i;
                break;
            }
            i2 = i + 1;
            char chLocal = charAt(i);
            if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B') {
                break;
            }
            int digit = chLocal - '0';
            if (result < MULTMIN_RADIX_TEN) {
                throw new NumberFormatException(numberString());
            }
            long result2 = result * 10;
            if (result2 < ((long) digit) + limit) {
                throw new NumberFormatException(numberString());
            }
            result = result2 - ((long) digit);
            i = i2;
        }
        if (!negative) {
            return -result;
        }
        if (i2 > this.np + 1) {
            return result;
        }
        throw new NumberFormatException(numberString());
    }

    public final Number decimalValue(boolean decimal) {
        char chLocal = charAt((this.np + this.sp) - 1);
        if (chLocal == 'F') {
            try {
                return Float.valueOf(Float.parseFloat(numberString()));
            } catch (NumberFormatException ex) {
                throw new JSONException(ex.getMessage() + ", " + info());
            }
        } else if (chLocal == 'D') {
            return Double.valueOf(Double.parseDouble(numberString()));
        } else {
            if (decimal) {
                return decimalValue();
            }
            return Double.valueOf(doubleValue());
        }
    }

    public static boolean isWhitespace(char ch2) {
        return ch2 <= ' ' && (ch2 == ' ' || ch2 == 10 || ch2 == 13 || ch2 == 9 || ch2 == 12 || ch2 == 8);
    }
}
