package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.IOUtils;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;

public final class SerializeWriter extends Writer {
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final ThreadLocal<char[]> bufLocal = new ThreadLocal<>();
    private static final ThreadLocal<byte[]> bytesBufLocal = new ThreadLocal<>();
    static final int nonDirectFeautres = ((((((((((SerializerFeature.UseSingleQuotes.mask | 0) | SerializerFeature.BrowserSecure.mask) | SerializerFeature.BrowserCompatible.mask) | SerializerFeature.PrettyFormat.mask) | SerializerFeature.WriteEnumUsingToString.mask) | SerializerFeature.WriteNonStringValueAsString.mask) | SerializerFeature.WriteSlashAsSpecial.mask) | SerializerFeature.IgnoreErrorGetter.mask) | SerializerFeature.WriteClassName.mask) | SerializerFeature.NotWriteDefaultValue.mask);
    protected boolean beanToArray;
    protected char[] buf;
    protected int count;
    protected boolean disableCircularReferenceDetect;
    protected int features;
    protected char keySeperator;
    protected boolean notWriteDefaultValue;
    protected boolean quoteFieldNames;
    protected boolean sortField;
    protected boolean useSingleQuotes;
    protected boolean writeDirect;
    protected boolean writeEnumUsingName;
    protected boolean writeEnumUsingToString;
    protected boolean writeNonStringValueAsString;
    private final Writer writer;

    public SerializeWriter() {
        this((Writer) null);
    }

    public SerializeWriter(Writer writer2) {
        this(writer2, JSON.DEFAULT_GENERATE_FEATURE, SerializerFeature.EMPTY);
    }

    public SerializeWriter(SerializerFeature... features2) {
        this((Writer) null, features2);
    }

    public SerializeWriter(Writer writer2, SerializerFeature... features2) {
        this(writer2, 0, features2);
    }

    public SerializeWriter(Writer writer2, int defaultFeatures, SerializerFeature... features2) {
        this.writer = writer2;
        this.buf = bufLocal.get();
        if (this.buf != null) {
            bufLocal.set((Object) null);
        } else {
            this.buf = new char[2048];
        }
        int featuresValue = defaultFeatures;
        for (SerializerFeature feature : features2) {
            featuresValue |= feature.getMask();
        }
        this.features = featuresValue;
        computeFeatures();
    }

    public int getBufferLength() {
        return this.buf.length;
    }

    public SerializeWriter(int initialSize) {
        this((Writer) null, initialSize);
    }

    public SerializeWriter(Writer writer2, int initialSize) {
        this.writer = writer2;
        if (initialSize <= 0) {
            throw new IllegalArgumentException("Negative initial size: " + initialSize);
        }
        this.buf = new char[initialSize];
    }

    public void config(SerializerFeature feature, boolean state) {
        if (state) {
            this.features |= feature.getMask();
            if (feature == SerializerFeature.WriteEnumUsingToString) {
                this.features &= SerializerFeature.WriteEnumUsingName.getMask() ^ -1;
            } else if (feature == SerializerFeature.WriteEnumUsingName) {
                this.features &= SerializerFeature.WriteEnumUsingToString.getMask() ^ -1;
            }
        } else {
            this.features &= feature.getMask() ^ -1;
        }
        computeFeatures();
    }

    /* access modifiers changed from: protected */
    public void computeFeatures() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9 = true;
        this.quoteFieldNames = (this.features & SerializerFeature.QuoteFieldNames.mask) != 0;
        if ((this.features & SerializerFeature.UseSingleQuotes.mask) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.useSingleQuotes = z;
        if ((this.features & SerializerFeature.SortField.mask) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.sortField = z2;
        if ((this.features & SerializerFeature.DisableCircularReferenceDetect.mask) != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.disableCircularReferenceDetect = z3;
        if ((this.features & SerializerFeature.BeanToArray.mask) != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.beanToArray = z4;
        if ((this.features & SerializerFeature.WriteNonStringValueAsString.mask) != 0) {
            z5 = true;
        } else {
            z5 = false;
        }
        this.writeNonStringValueAsString = z5;
        if ((this.features & SerializerFeature.NotWriteDefaultValue.mask) != 0) {
            z6 = true;
        } else {
            z6 = false;
        }
        this.notWriteDefaultValue = z6;
        if ((this.features & SerializerFeature.WriteEnumUsingName.mask) != 0) {
            z7 = true;
        } else {
            z7 = false;
        }
        this.writeEnumUsingName = z7;
        if ((this.features & SerializerFeature.WriteEnumUsingToString.mask) != 0) {
            z8 = true;
        } else {
            z8 = false;
        }
        this.writeEnumUsingToString = z8;
        if (!this.quoteFieldNames || (this.features & nonDirectFeautres) != 0 || (!this.beanToArray && !this.writeEnumUsingName)) {
            z9 = false;
        }
        this.writeDirect = z9;
        this.keySeperator = this.useSingleQuotes ? '\'' : '\"';
    }

    public boolean isSortField() {
        return this.sortField;
    }

    public boolean isNotWriteDefaultValue() {
        return this.notWriteDefaultValue;
    }

    public boolean isEnabled(SerializerFeature feature) {
        return (this.features & feature.mask) != 0;
    }

    public boolean isEnabled(int feature) {
        return (this.features & feature) != 0;
    }

    public void write(int c) {
        int newcount = this.count + 1;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else {
                flush();
                newcount = 1;
            }
        }
        this.buf[this.count] = (char) c;
        this.count = newcount;
    }

    public void write(char[] c, int off, int len) {
        if (off < 0 || off > c.length || len < 0 || off + len > c.length || off + len < 0) {
            throw new IndexOutOfBoundsException();
        } else if (len != 0) {
            int newcount = this.count + len;
            if (newcount > this.buf.length) {
                if (this.writer == null) {
                    expandCapacity(newcount);
                } else {
                    do {
                        int rest = this.buf.length - this.count;
                        System.arraycopy(c, off, this.buf, this.count, rest);
                        this.count = this.buf.length;
                        flush();
                        len -= rest;
                        off += rest;
                    } while (len > this.buf.length);
                    newcount = len;
                }
            }
            System.arraycopy(c, off, this.buf, this.count, len);
            this.count = newcount;
        }
    }

    public void expandCapacity(int minimumCapacity) {
        int newCapacity = ((this.buf.length * 3) / 2) + 1;
        if (newCapacity < minimumCapacity) {
            newCapacity = minimumCapacity;
        }
        char[] newValue = new char[newCapacity];
        System.arraycopy(this.buf, 0, newValue, 0, this.count);
        this.buf = newValue;
    }

    public SerializeWriter append(CharSequence csq) {
        String s = csq == null ? Constant.NULL : csq.toString();
        write(s, 0, s.length());
        return this;
    }

    public SerializeWriter append(CharSequence csq, int start, int end) {
        if (csq == null) {
            csq = Constant.NULL;
        }
        String s = csq.subSequence(start, end).toString();
        write(s, 0, s.length());
        return this;
    }

    public SerializeWriter append(char c) {
        write((int) c);
        return this;
    }

    public void write(String str, int off, int len) {
        int newcount = this.count + len;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else {
                do {
                    int rest = this.buf.length - this.count;
                    str.getChars(off, off + rest, this.buf, this.count);
                    this.count = this.buf.length;
                    flush();
                    len -= rest;
                    off += rest;
                } while (len > this.buf.length);
                newcount = len;
            }
        }
        str.getChars(off, off + len, this.buf, this.count);
        this.count = newcount;
    }

    public void writeTo(Writer out) throws IOException {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        out.write(this.buf, 0, this.count);
    }

    public void writeTo(OutputStream out, String charsetName) throws IOException {
        writeTo(out, Charset.forName(charsetName));
    }

    public void writeTo(OutputStream out, Charset charset) throws IOException {
        writeToEx(out, charset);
    }

    public int writeToEx(OutputStream out, Charset charset) throws IOException {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        } else if (charset == UTF8) {
            return encodeToUTF8(out);
        } else {
            byte[] bytes = new String(this.buf, 0, this.count).getBytes(charset);
            out.write(bytes);
            return bytes.length;
        }
    }

    public char[] toCharArray() {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        char[] newValue = new char[this.count];
        System.arraycopy(this.buf, 0, newValue, 0, this.count);
        return newValue;
    }

    public char[] toCharArrayForSpringWebSocket() {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        }
        char[] newValue = new char[(this.count - 2)];
        System.arraycopy(this.buf, 1, newValue, 0, this.count - 2);
        return newValue;
    }

    public byte[] toBytes(String charsetName) {
        Charset charset;
        if (charsetName == null || "UTF-8".equals(charsetName)) {
            charset = UTF8;
        } else {
            charset = Charset.forName(charsetName);
        }
        return toBytes(charset);
    }

    public byte[] toBytes(Charset charset) {
        if (this.writer != null) {
            throw new UnsupportedOperationException("writer not null");
        } else if (charset == UTF8) {
            return encodeToUTF8Bytes();
        } else {
            return new String(this.buf, 0, this.count).getBytes(charset);
        }
    }

    private int encodeToUTF8(OutputStream out) throws IOException {
        int bytesLength = (int) (((double) this.count) * 3.0d);
        byte[] bytes = bytesBufLocal.get();
        if (bytes == null) {
            bytes = new byte[8192];
            bytesBufLocal.set(bytes);
        }
        if (bytes.length < bytesLength) {
            bytes = new byte[bytesLength];
        }
        int position = IOUtils.encodeUTF8(this.buf, 0, this.count, bytes);
        out.write(bytes, 0, position);
        return position;
    }

    private byte[] encodeToUTF8Bytes() {
        int bytesLength = (int) (((double) this.count) * 3.0d);
        byte[] bytes = bytesBufLocal.get();
        if (bytes == null) {
            bytes = new byte[8192];
            bytesBufLocal.set(bytes);
        }
        if (bytes.length < bytesLength) {
            bytes = new byte[bytesLength];
        }
        int position = IOUtils.encodeUTF8(this.buf, 0, this.count, bytes);
        byte[] copy = new byte[position];
        System.arraycopy(bytes, 0, copy, 0, position);
        return copy;
    }

    public int size() {
        return this.count;
    }

    public String toString() {
        return new String(this.buf, 0, this.count);
    }

    public void close() {
        if (this.writer != null && this.count > 0) {
            flush();
        }
        if (this.buf.length <= 65536) {
            bufLocal.set(this.buf);
        }
        this.buf = null;
    }

    public void write(String text) {
        if (text == null) {
            writeNull();
        } else {
            write(text, 0, text.length());
        }
    }

    public void writeInt(int i) {
        if (i == Integer.MIN_VALUE) {
            write("-2147483648");
            return;
        }
        int size = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
        int newcount = this.count + size;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else {
                char[] chars = new char[size];
                IOUtils.getChars(i, size, chars);
                write(chars, 0, chars.length);
                return;
            }
        }
        IOUtils.getChars(i, newcount, this.buf);
        this.count = newcount;
    }

    public void writeByteArray(byte[] bytes) {
        int bytesLen = bytes.length;
        char quote = this.useSingleQuotes ? '\'' : '\"';
        if (bytesLen == 0) {
            write(this.useSingleQuotes ? "''" : "\"\"");
            return;
        }
        char[] CA = IOUtils.CA;
        int eLen = (bytesLen / 3) * 3;
        int offset = this.count;
        int newcount = this.count + ((((bytesLen - 1) / 3) + 1) << 2) + 2;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write((int) quote);
                int s = 0;
                while (true) {
                    int s2 = s;
                    if (s2 >= eLen) {
                        break;
                    }
                    int s3 = s2 + 1;
                    int s4 = s3 + 1;
                    s = s4 + 1;
                    int i = ((bytes[s2] & OnReminderListener.RET_FULL) << 16) | ((bytes[s3] & OnReminderListener.RET_FULL) << 8) | (bytes[s4] & 255);
                    write((int) CA[(i >>> 18) & 63]);
                    write((int) CA[(i >>> 12) & 63]);
                    write((int) CA[(i >>> 6) & 63]);
                    write((int) CA[i & 63]);
                }
                int left = bytesLen - eLen;
                if (left > 0) {
                    int i2 = ((bytes[eLen] & OnReminderListener.RET_FULL) << 10) | (left == 2 ? (bytes[bytesLen - 1] & OnReminderListener.RET_FULL) << 2 : 0);
                    write((int) CA[i2 >> 12]);
                    write((int) CA[(i2 >>> 6) & 63]);
                    write((int) left == 2 ? CA[i2 & 63] : '=');
                    write(61);
                }
                write((int) quote);
                return;
            }
            expandCapacity(newcount);
        }
        this.count = newcount;
        this.buf[offset] = quote;
        int s5 = 0;
        int d = offset + 1;
        while (true) {
            int s6 = s5;
            if (s6 >= eLen) {
                break;
            }
            int s7 = s6 + 1;
            int s8 = s7 + 1;
            s5 = s8 + 1;
            int i3 = ((bytes[s6] & OnReminderListener.RET_FULL) << 16) | ((bytes[s7] & OnReminderListener.RET_FULL) << 8) | (bytes[s8] & 255);
            int d2 = d + 1;
            this.buf[d] = CA[(i3 >>> 18) & 63];
            int d3 = d2 + 1;
            this.buf[d2] = CA[(i3 >>> 12) & 63];
            int d4 = d3 + 1;
            this.buf[d3] = CA[(i3 >>> 6) & 63];
            d = d4 + 1;
            this.buf[d4] = CA[i3 & 63];
        }
        int left2 = bytesLen - eLen;
        if (left2 > 0) {
            int i4 = ((bytes[eLen] & OnReminderListener.RET_FULL) << 10) | (left2 == 2 ? (bytes[bytesLen - 1] & OnReminderListener.RET_FULL) << 2 : 0);
            this.buf[newcount - 5] = CA[i4 >> 12];
            this.buf[newcount - 4] = CA[(i4 >>> 6) & 63];
            this.buf[newcount - 3] = left2 == 2 ? CA[i4 & 63] : '=';
            this.buf[newcount - 2] = '=';
        }
        this.buf[newcount - 1] = quote;
    }

    public void writeFloat(float value, boolean checkWriteClassName) {
        if (Float.isNaN(value) || Float.isInfinite(value)) {
            writeNull();
            return;
        }
        String floatText = Float.toString(value);
        if (isEnabled(SerializerFeature.WriteNullNumberAsZero) && floatText.endsWith(".0")) {
            floatText = floatText.substring(0, floatText.length() - 2);
        }
        write(floatText);
        if (checkWriteClassName && isEnabled(SerializerFeature.WriteClassName)) {
            write(70);
        }
    }

    public void writeDouble(double doubleValue, boolean checkWriteClassName) {
        if (Double.isNaN(doubleValue) || Double.isInfinite(doubleValue)) {
            writeNull();
            return;
        }
        String doubleText = Double.toString(doubleValue);
        if (isEnabled(SerializerFeature.WriteNullNumberAsZero) && doubleText.endsWith(".0")) {
            doubleText = doubleText.substring(0, doubleText.length() - 2);
        }
        write(doubleText);
        if (checkWriteClassName && isEnabled(SerializerFeature.WriteClassName)) {
            write(68);
        }
    }

    public void writeEnum(Enum<?> value) {
        if (value == null) {
            writeNull();
            return;
        }
        String strVal = null;
        if (this.writeEnumUsingName && !this.writeEnumUsingToString) {
            strVal = value.name();
        } else if (this.writeEnumUsingToString) {
            strVal = value.toString();
        }
        if (strVal != null) {
            char quote = isEnabled(SerializerFeature.UseSingleQuotes) ? '\'' : '\"';
            write((int) quote);
            write(strVal);
            write((int) quote);
            return;
        }
        writeInt(value.ordinal());
    }

    public void writeLong(long i) {
        boolean needQuotationMark;
        if (!isEnabled(SerializerFeature.BrowserCompatible) || isEnabled(SerializerFeature.WriteClassName) || (i <= 9007199254740991L && i >= -9007199254740991L)) {
            needQuotationMark = false;
        } else {
            needQuotationMark = true;
        }
        if (i != Long.MIN_VALUE) {
            int size = i < 0 ? IOUtils.stringSize(-i) + 1 : IOUtils.stringSize(i);
            int newcount = this.count + size;
            if (needQuotationMark) {
                newcount += 2;
            }
            if (newcount > this.buf.length) {
                if (this.writer == null) {
                    expandCapacity(newcount);
                } else {
                    char[] chars = new char[size];
                    IOUtils.getChars(i, size, chars);
                    if (needQuotationMark) {
                        write(34);
                        write(chars, 0, chars.length);
                        write(34);
                        return;
                    }
                    write(chars, 0, chars.length);
                    return;
                }
            }
            if (needQuotationMark) {
                this.buf[this.count] = '\"';
                IOUtils.getChars(i, newcount - 1, this.buf);
                this.buf[newcount - 1] = '\"';
            } else {
                IOUtils.getChars(i, newcount, this.buf);
            }
            this.count = newcount;
        } else if (needQuotationMark) {
            write("\"-9223372036854775808\"");
        } else {
            write("-9223372036854775808");
        }
    }

    public void writeNull() {
        write(Constant.NULL);
    }

    public void writeNull(SerializerFeature feature) {
        writeNull(0, feature.mask);
    }

    public void writeNull(int beanFeatures, int feature) {
        if ((beanFeatures & feature) == 0 && (this.features & feature) == 0) {
            writeNull();
        } else if (feature == SerializerFeature.WriteNullListAsEmpty.mask) {
            write("[]");
        } else if (feature == SerializerFeature.WriteNullStringAsEmpty.mask) {
            writeString("");
        } else if (feature == SerializerFeature.WriteNullBooleanAsFalse.mask) {
            write("false");
        } else if (feature == SerializerFeature.WriteNullNumberAsZero.mask) {
            write(48);
        } else {
            writeNull();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v8, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v9, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r13v11, resolved type: int} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r10v6, resolved type: int} */
    /* JADX WARNING: type inference failed for: r0v42, types: [int] */
    /* JADX WARNING: type inference failed for: r0v133, types: [int] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeStringWithDoubleQuote(java.lang.String r26, char r27) {
        /*
            r25 = this;
            if (r26 != 0) goto L_0x000f
            r25.writeNull()
            if (r27 == 0) goto L_0x000e
            r0 = r25
            r1 = r27
            r0.write((int) r1)
        L_0x000e:
            return
        L_0x000f:
            int r14 = r26.length()
            r0 = r25
            int r0 = r0.count
            r20 = r0
            int r20 = r20 + r14
            int r15 = r20 + 2
            if (r27 == 0) goto L_0x0021
            int r15 = r15 + 1
        L_0x0021:
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r20
            int r0 = r0.length
            r20 = r0
            r0 = r20
            if (r15 <= r0) goto L_0x0279
            r0 = r25
            java.io.Writer r0 = r0.writer
            r20 = r0
            if (r20 == 0) goto L_0x0274
            r20 = 34
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            r11 = 0
        L_0x0042:
            int r20 = r26.length()
            r0 = r20
            if (r11 >= r0) goto L_0x0260
            r0 = r26
            char r7 = r0.charAt(r11)
            com.alibaba.fastjson.serializer.SerializerFeature r20 = com.alibaba.fastjson.serializer.SerializerFeature.BrowserSecure
            r0 = r25
            r1 = r20
            boolean r20 = r0.isEnabled((com.alibaba.fastjson.serializer.SerializerFeature) r1)
            if (r20 == 0) goto L_0x00e2
            r20 = 48
            r0 = r20
            if (r7 < r0) goto L_0x0068
            r20 = 57
            r0 = r20
            if (r7 <= r0) goto L_0x0259
        L_0x0068:
            r20 = 97
            r0 = r20
            if (r7 < r0) goto L_0x0074
            r20 = 122(0x7a, float:1.71E-43)
            r0 = r20
            if (r7 <= r0) goto L_0x0259
        L_0x0074:
            r20 = 65
            r0 = r20
            if (r7 < r0) goto L_0x0080
            r20 = 90
            r0 = r20
            if (r7 <= r0) goto L_0x0259
        L_0x0080:
            r20 = 44
            r0 = r20
            if (r7 == r0) goto L_0x0259
            r20 = 46
            r0 = r20
            if (r7 == r0) goto L_0x0259
            r20 = 95
            r0 = r20
            if (r7 == r0) goto L_0x0259
            r20 = 92
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            r20 = 117(0x75, float:1.64E-43)
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r21 = r7 >>> 12
            r21 = r21 & 15
            char r20 = r20[r21]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r21 = r7 >>> 8
            r21 = r21 & 15
            char r20 = r20[r21]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r21 = r7 >>> 4
            r21 = r21 & 15
            char r20 = r20[r21]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r21 = r7 & 15
            char r20 = r20[r21]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
        L_0x00de:
            int r11 = r11 + 1
            goto L_0x0042
        L_0x00e2:
            com.alibaba.fastjson.serializer.SerializerFeature r20 = com.alibaba.fastjson.serializer.SerializerFeature.BrowserCompatible
            r0 = r25
            r1 = r20
            boolean r20 = r0.isEnabled((com.alibaba.fastjson.serializer.SerializerFeature) r1)
            if (r20 == 0) goto L_0x01cf
            r20 = 8
            r0 = r20
            if (r7 == r0) goto L_0x011e
            r20 = 12
            r0 = r20
            if (r7 == r0) goto L_0x011e
            r20 = 10
            r0 = r20
            if (r7 == r0) goto L_0x011e
            r20 = 13
            r0 = r20
            if (r7 == r0) goto L_0x011e
            r20 = 9
            r0 = r20
            if (r7 == r0) goto L_0x011e
            r20 = 34
            r0 = r20
            if (r7 == r0) goto L_0x011e
            r20 = 47
            r0 = r20
            if (r7 == r0) goto L_0x011e
            r20 = 92
            r0 = r20
            if (r7 != r0) goto L_0x0133
        L_0x011e:
            r20 = 92
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r20 = r20[r7]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            goto L_0x00de
        L_0x0133:
            r20 = 32
            r0 = r20
            if (r7 >= r0) goto L_0x017b
            r20 = 92
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            r20 = 117(0x75, float:1.64E-43)
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            r20 = 48
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            r20 = 48
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.ASCII_CHARS
            int r21 = r7 * 2
            char r20 = r20[r21]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.ASCII_CHARS
            int r21 = r7 * 2
            int r21 = r21 + 1
            char r20 = r20[r21]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            goto L_0x00de
        L_0x017b:
            r20 = 127(0x7f, float:1.78E-43)
            r0 = r20
            if (r7 < r0) goto L_0x0259
            r20 = 92
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            r20 = 117(0x75, float:1.64E-43)
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r21 = r7 >>> 12
            r21 = r21 & 15
            char r20 = r20[r21]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r21 = r7 >>> 8
            r21 = r21 & 15
            char r20 = r20[r21]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r21 = r7 >>> 4
            r21 = r21 & 15
            char r20 = r20[r21]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r21 = r7 & 15
            char r20 = r20[r21]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            goto L_0x00de
        L_0x01cf:
            byte[] r20 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            r0 = r20
            int r0 = r0.length
            r20 = r0
            r0 = r20
            if (r7 >= r0) goto L_0x01e0
            byte[] r20 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r20 = r20[r7]
            if (r20 != 0) goto L_0x01f2
        L_0x01e0:
            r20 = 47
            r0 = r20
            if (r7 != r0) goto L_0x0259
            com.alibaba.fastjson.serializer.SerializerFeature r20 = com.alibaba.fastjson.serializer.SerializerFeature.WriteSlashAsSpecial
            r0 = r25
            r1 = r20
            boolean r20 = r0.isEnabled((com.alibaba.fastjson.serializer.SerializerFeature) r1)
            if (r20 == 0) goto L_0x0259
        L_0x01f2:
            r20 = 92
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            byte[] r20 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r20 = r20[r7]
            r21 = 4
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x024c
            r20 = 117(0x75, float:1.64E-43)
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r21 = r7 >>> 12
            r21 = r21 & 15
            char r20 = r20[r21]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r21 = r7 >>> 8
            r21 = r21 & 15
            char r20 = r20[r21]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r21 = r7 >>> 4
            r21 = r21 & 15
            char r20 = r20[r21]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            char[] r20 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r21 = r7 & 15
            char r20 = r20[r21]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            goto L_0x00de
        L_0x024c:
            char[] r20 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r20 = r20[r7]
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            goto L_0x00de
        L_0x0259:
            r0 = r25
            r0.write((int) r7)
            goto L_0x00de
        L_0x0260:
            r20 = 34
            r0 = r25
            r1 = r20
            r0.write((int) r1)
            if (r27 == 0) goto L_0x000e
            r0 = r25
            r1 = r27
            r0.write((int) r1)
            goto L_0x000e
        L_0x0274:
            r0 = r25
            r0.expandCapacity(r15)
        L_0x0279:
            r0 = r25
            int r0 = r0.count
            r20 = r0
            int r18 = r20 + 1
            int r9 = r18 + r14
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r25
            int r0 = r0.count
            r21 = r0
            r22 = 34
            r20[r21] = r22
            r20 = 0
            r0 = r25
            char[] r0 = r0.buf
            r21 = r0
            r0 = r26
            r1 = r20
            r2 = r21
            r3 = r18
            r0.getChars(r1, r14, r2, r3)
            r0 = r25
            r0.count = r15
            com.alibaba.fastjson.serializer.SerializerFeature r20 = com.alibaba.fastjson.serializer.SerializerFeature.BrowserSecure
            r0 = r25
            r1 = r20
            boolean r20 = r0.isEnabled((com.alibaba.fastjson.serializer.SerializerFeature) r1)
            if (r20 == 0) goto L_0x040d
            r13 = -1
            r11 = r18
        L_0x02b9:
            if (r11 >= r9) goto L_0x02ff
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            char r7 = r20[r11]
            r20 = 48
            r0 = r20
            if (r7 < r0) goto L_0x02cf
            r20 = 57
            r0 = r20
            if (r7 <= r0) goto L_0x02fc
        L_0x02cf:
            r20 = 97
            r0 = r20
            if (r7 < r0) goto L_0x02db
            r20 = 122(0x7a, float:1.71E-43)
            r0 = r20
            if (r7 <= r0) goto L_0x02fc
        L_0x02db:
            r20 = 65
            r0 = r20
            if (r7 < r0) goto L_0x02e7
            r20 = 90
            r0 = r20
            if (r7 <= r0) goto L_0x02fc
        L_0x02e7:
            r20 = 44
            r0 = r20
            if (r7 == r0) goto L_0x02fc
            r20 = 46
            r0 = r20
            if (r7 == r0) goto L_0x02fc
            r20 = 95
            r0 = r20
            if (r7 == r0) goto L_0x02fc
            r13 = r11
            int r15 = r15 + 5
        L_0x02fc:
            int r11 = r11 + 1
            goto L_0x02b9
        L_0x02ff:
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r20
            int r0 = r0.length
            r20 = r0
            r0 = r20
            if (r15 <= r0) goto L_0x0313
            r0 = r25
            r0.expandCapacity(r15)
        L_0x0313:
            r0 = r25
            r0.count = r15
            r11 = r13
        L_0x0318:
            r0 = r18
            if (r11 < r0) goto L_0x03d3
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            char r7 = r20[r11]
            r20 = 48
            r0 = r20
            if (r7 < r0) goto L_0x0330
            r20 = 57
            r0 = r20
            if (r7 <= r0) goto L_0x03cf
        L_0x0330:
            r20 = 97
            r0 = r20
            if (r7 < r0) goto L_0x033c
            r20 = 122(0x7a, float:1.71E-43)
            r0 = r20
            if (r7 <= r0) goto L_0x03cf
        L_0x033c:
            r20 = 65
            r0 = r20
            if (r7 < r0) goto L_0x0348
            r20 = 90
            r0 = r20
            if (r7 <= r0) goto L_0x03cf
        L_0x0348:
            r20 = 44
            r0 = r20
            if (r7 == r0) goto L_0x03cf
            r20 = 46
            r0 = r20
            if (r7 == r0) goto L_0x03cf
            r20 = 95
            r0 = r20
            if (r7 == r0) goto L_0x03cf
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 1
            r0 = r25
            char[] r0 = r0.buf
            r22 = r0
            int r23 = r11 + 6
            int r24 = r9 - r11
            int r24 = r24 + -1
            java.lang.System.arraycopy(r20, r21, r22, r23, r24)
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r21 = 92
            r20[r11] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 1
            r22 = 117(0x75, float:1.64E-43)
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 2
            char[] r22 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r23 = r7 >>> 12
            r23 = r23 & 15
            char r22 = r22[r23]
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 3
            char[] r22 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r23 = r7 >>> 8
            r23 = r23 & 15
            char r22 = r22[r23]
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 4
            char[] r22 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r23 = r7 >>> 4
            r23 = r23 & 15
            char r22 = r22[r23]
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 5
            char[] r22 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r23 = r7 & 15
            char r22 = r22[r23]
            r20[r21] = r22
            int r9 = r9 + 5
        L_0x03cf:
            int r11 = r11 + -1
            goto L_0x0318
        L_0x03d3:
            if (r27 == 0) goto L_0x03f9
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r25
            int r0 = r0.count
            r21 = r0
            int r21 = r21 + -2
            r22 = 34
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r25
            int r0 = r0.count
            r21 = r0
            int r21 = r21 + -1
            r20[r21] = r27
            goto L_0x000e
        L_0x03f9:
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r25
            int r0 = r0.count
            r21 = r0
            int r21 = r21 + -1
            r22 = 34
            r20[r21] = r22
            goto L_0x000e
        L_0x040d:
            com.alibaba.fastjson.serializer.SerializerFeature r20 = com.alibaba.fastjson.serializer.SerializerFeature.BrowserCompatible
            r0 = r25
            r1 = r20
            boolean r20 = r0.isEnabled((com.alibaba.fastjson.serializer.SerializerFeature) r1)
            if (r20 == 0) goto L_0x0653
            r13 = -1
            r11 = r18
        L_0x041c:
            if (r11 >= r9) goto L_0x0474
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            char r7 = r20[r11]
            r20 = 34
            r0 = r20
            if (r7 == r0) goto L_0x0438
            r20 = 47
            r0 = r20
            if (r7 == r0) goto L_0x0438
            r20 = 92
            r0 = r20
            if (r7 != r0) goto L_0x043e
        L_0x0438:
            r13 = r11
            int r15 = r15 + 1
        L_0x043b:
            int r11 = r11 + 1
            goto L_0x041c
        L_0x043e:
            r20 = 8
            r0 = r20
            if (r7 == r0) goto L_0x045c
            r20 = 12
            r0 = r20
            if (r7 == r0) goto L_0x045c
            r20 = 10
            r0 = r20
            if (r7 == r0) goto L_0x045c
            r20 = 13
            r0 = r20
            if (r7 == r0) goto L_0x045c
            r20 = 9
            r0 = r20
            if (r7 != r0) goto L_0x0460
        L_0x045c:
            r13 = r11
            int r15 = r15 + 1
            goto L_0x043b
        L_0x0460:
            r20 = 32
            r0 = r20
            if (r7 >= r0) goto L_0x046a
            r13 = r11
            int r15 = r15 + 5
            goto L_0x043b
        L_0x046a:
            r20 = 127(0x7f, float:1.78E-43)
            r0 = r20
            if (r7 < r0) goto L_0x043b
            r13 = r11
            int r15 = r15 + 5
            goto L_0x043b
        L_0x0474:
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r20
            int r0 = r0.length
            r20 = r0
            r0 = r20
            if (r15 <= r0) goto L_0x0488
            r0 = r25
            r0.expandCapacity(r15)
        L_0x0488:
            r0 = r25
            r0.count = r15
            r11 = r13
        L_0x048d:
            r0 = r18
            if (r11 < r0) goto L_0x0619
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            char r7 = r20[r11]
            r20 = 8
            r0 = r20
            if (r7 == r0) goto L_0x04b7
            r20 = 12
            r0 = r20
            if (r7 == r0) goto L_0x04b7
            r20 = 10
            r0 = r20
            if (r7 == r0) goto L_0x04b7
            r20 = 13
            r0 = r20
            if (r7 == r0) goto L_0x04b7
            r20 = 9
            r0 = r20
            if (r7 != r0) goto L_0x04eb
        L_0x04b7:
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 1
            r0 = r25
            char[] r0 = r0.buf
            r22 = r0
            int r23 = r11 + 2
            int r24 = r9 - r11
            int r24 = r24 + -1
            java.lang.System.arraycopy(r20, r21, r22, r23, r24)
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r21 = 92
            r20[r11] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 1
            char[] r22 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r22 = r22[r7]
            r20[r21] = r22
            int r9 = r9 + 1
        L_0x04e8:
            int r11 = r11 + -1
            goto L_0x048d
        L_0x04eb:
            r20 = 34
            r0 = r20
            if (r7 == r0) goto L_0x04fd
            r20 = 47
            r0 = r20
            if (r7 == r0) goto L_0x04fd
            r20 = 92
            r0 = r20
            if (r7 != r0) goto L_0x052b
        L_0x04fd:
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 1
            r0 = r25
            char[] r0 = r0.buf
            r22 = r0
            int r23 = r11 + 2
            int r24 = r9 - r11
            int r24 = r24 + -1
            java.lang.System.arraycopy(r20, r21, r22, r23, r24)
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r21 = 92
            r20[r11] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 1
            r20[r21] = r7
            int r9 = r9 + 1
            goto L_0x04e8
        L_0x052b:
            r20 = 32
            r0 = r20
            if (r7 >= r0) goto L_0x059c
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 1
            r0 = r25
            char[] r0 = r0.buf
            r22 = r0
            int r23 = r11 + 6
            int r24 = r9 - r11
            int r24 = r24 + -1
            java.lang.System.arraycopy(r20, r21, r22, r23, r24)
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r21 = 92
            r20[r11] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 1
            r22 = 117(0x75, float:1.64E-43)
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 2
            r22 = 48
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 3
            r22 = 48
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 4
            char[] r22 = com.alibaba.fastjson.util.IOUtils.ASCII_CHARS
            int r23 = r7 * 2
            char r22 = r22[r23]
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 5
            char[] r22 = com.alibaba.fastjson.util.IOUtils.ASCII_CHARS
            int r23 = r7 * 2
            int r23 = r23 + 1
            char r22 = r22[r23]
            r20[r21] = r22
            int r9 = r9 + 5
            goto L_0x04e8
        L_0x059c:
            r20 = 127(0x7f, float:1.78E-43)
            r0 = r20
            if (r7 < r0) goto L_0x04e8
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 1
            r0 = r25
            char[] r0 = r0.buf
            r22 = r0
            int r23 = r11 + 6
            int r24 = r9 - r11
            int r24 = r24 + -1
            java.lang.System.arraycopy(r20, r21, r22, r23, r24)
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r21 = 92
            r20[r11] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 1
            r22 = 117(0x75, float:1.64E-43)
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 2
            char[] r22 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r23 = r7 >>> 12
            r23 = r23 & 15
            char r22 = r22[r23]
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 3
            char[] r22 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r23 = r7 >>> 8
            r23 = r23 & 15
            char r22 = r22[r23]
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 4
            char[] r22 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r23 = r7 >>> 4
            r23 = r23 & 15
            char r22 = r22[r23]
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r21 = r11 + 5
            char[] r22 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r23 = r7 & 15
            char r22 = r22[r23]
            r20[r21] = r22
            int r9 = r9 + 5
            goto L_0x04e8
        L_0x0619:
            if (r27 == 0) goto L_0x063f
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r25
            int r0 = r0.count
            r21 = r0
            int r21 = r21 + -2
            r22 = 34
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r25
            int r0 = r0.count
            r21 = r0
            int r21 = r21 + -1
            r20[r21] = r27
            goto L_0x000e
        L_0x063f:
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r25
            int r0 = r0.count
            r21 = r0
            int r21 = r21 + -1
            r22 = 34
            r20[r21] = r22
            goto L_0x000e
        L_0x0653:
            r16 = 0
            r13 = -1
            r10 = -1
            r12 = 0
            r11 = r18
        L_0x065a:
            if (r11 >= r9) goto L_0x06cd
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            char r7 = r20[r11]
            r20 = 8232(0x2028, float:1.1535E-41)
            r0 = r20
            if (r7 != r0) goto L_0x067a
            int r16 = r16 + 1
            r13 = r11
            r12 = r7
            int r15 = r15 + 4
            r20 = -1
            r0 = r20
            if (r10 != r0) goto L_0x0677
            r10 = r11
        L_0x0677:
            int r11 = r11 + 1
            goto L_0x065a
        L_0x067a:
            r20 = 93
            r0 = r20
            if (r7 < r0) goto L_0x069a
            r20 = 127(0x7f, float:1.78E-43)
            r0 = r20
            if (r7 < r0) goto L_0x0677
            r20 = 160(0xa0, float:2.24E-43)
            r0 = r20
            if (r7 >= r0) goto L_0x0677
            r20 = -1
            r0 = r20
            if (r10 != r0) goto L_0x0693
            r10 = r11
        L_0x0693:
            int r16 = r16 + 1
            r13 = r11
            r12 = r7
            int r15 = r15 + 4
            goto L_0x0677
        L_0x069a:
            r0 = r25
            int r0 = r0.features
            r20 = r0
            r0 = r20
            boolean r20 = isSpecial(r7, r0)
            if (r20 == 0) goto L_0x0677
            int r16 = r16 + 1
            r13 = r11
            r12 = r7
            byte[] r20 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            r0 = r20
            int r0 = r0.length
            r20 = r0
            r0 = r20
            if (r7 >= r0) goto L_0x06c5
            byte[] r20 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r20 = r20[r7]
            r21 = 4
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x06c5
            int r15 = r15 + 4
        L_0x06c5:
            r20 = -1
            r0 = r20
            if (r10 != r0) goto L_0x0677
            r10 = r11
            goto L_0x0677
        L_0x06cd:
            if (r16 <= 0) goto L_0x075a
            int r15 = r15 + r16
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r20
            int r0 = r0.length
            r20 = r0
            r0 = r20
            if (r15 <= r0) goto L_0x06e5
            r0 = r25
            r0.expandCapacity(r15)
        L_0x06e5:
            r0 = r25
            r0.count = r15
            r20 = 1
            r0 = r16
            r1 = r20
            if (r0 != r1) goto L_0x084d
            r20 = 8232(0x2028, float:1.1535E-41)
            r0 = r20
            if (r12 != r0) goto L_0x0780
            int r17 = r13 + 1
            int r8 = r13 + 6
            int r20 = r9 - r13
            int r4 = r20 + -1
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r25
            char[] r0 = r0.buf
            r21 = r0
            r0 = r20
            r1 = r17
            r2 = r21
            java.lang.System.arraycopy(r0, r1, r2, r8, r4)
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r21 = 92
            r20[r13] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r13 = r13 + 1
            r21 = 117(0x75, float:1.64E-43)
            r20[r13] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r13 = r13 + 1
            r21 = 50
            r20[r13] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r13 = r13 + 1
            r21 = 48
            r20[r13] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r13 = r13 + 1
            r21 = 50
            r20[r13] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r13 = r13 + 1
            r21 = 56
            r20[r13] = r21
        L_0x075a:
            if (r27 == 0) goto L_0x0980
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r25
            int r0 = r0.count
            r21 = r0
            int r21 = r21 + -2
            r22 = 34
            r20[r21] = r22
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r25
            int r0 = r0.count
            r21 = r0
            int r21 = r21 + -1
            r20[r21] = r27
            goto L_0x000e
        L_0x0780:
            r7 = r12
            byte[] r20 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            r0 = r20
            int r0 = r0.length
            r20 = r0
            r0 = r20
            if (r7 >= r0) goto L_0x0816
            byte[] r20 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r20 = r20[r7]
            r21 = 4
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x0816
            int r17 = r13 + 1
            int r8 = r13 + 6
            int r20 = r9 - r13
            int r4 = r20 + -1
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r25
            char[] r0 = r0.buf
            r21 = r0
            r0 = r20
            r1 = r17
            r2 = r21
            java.lang.System.arraycopy(r0, r1, r2, r8, r4)
            r5 = r13
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r6 = r5 + 1
            r21 = 92
            r20[r5] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r5 = r6 + 1
            r21 = 117(0x75, float:1.64E-43)
            r20[r6] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r6 = r5 + 1
            char[] r21 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r22 = r7 >>> 12
            r22 = r22 & 15
            char r21 = r21[r22]
            r20[r5] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r5 = r6 + 1
            char[] r21 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r22 = r7 >>> 8
            r22 = r22 & 15
            char r21 = r21[r22]
            r20[r6] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r6 = r5 + 1
            char[] r21 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r22 = r7 >>> 4
            r22 = r22 & 15
            char r21 = r21[r22]
            r20[r5] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r5 = r6 + 1
            char[] r21 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r22 = r7 & 15
            char r21 = r21[r22]
            r20[r6] = r21
            goto L_0x075a
        L_0x0816:
            int r17 = r13 + 1
            int r8 = r13 + 2
            int r20 = r9 - r13
            int r4 = r20 + -1
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r25
            char[] r0 = r0.buf
            r21 = r0
            r0 = r20
            r1 = r17
            r2 = r21
            java.lang.System.arraycopy(r0, r1, r2, r8, r4)
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r21 = 92
            r20[r13] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r13 = r13 + 1
            char[] r21 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r21 = r21[r7]
            r20[r13] = r21
            goto L_0x075a
        L_0x084d:
            r20 = 1
            r0 = r16
            r1 = r20
            if (r0 <= r1) goto L_0x075a
            int r19 = r10 - r18
            r5 = r10
            r11 = r19
        L_0x085a:
            int r20 = r26.length()
            r0 = r20
            if (r11 >= r0) goto L_0x075a
            r0 = r26
            char r7 = r0.charAt(r11)
            byte[] r20 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            r0 = r20
            int r0 = r0.length
            r20 = r0
            r0 = r20
            if (r7 >= r0) goto L_0x0879
            byte[] r20 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r20 = r20[r7]
            if (r20 != 0) goto L_0x088b
        L_0x0879:
            r20 = 47
            r0 = r20
            if (r7 != r0) goto L_0x090c
            com.alibaba.fastjson.serializer.SerializerFeature r20 = com.alibaba.fastjson.serializer.SerializerFeature.WriteSlashAsSpecial
            r0 = r25
            r1 = r20
            boolean r20 = r0.isEnabled((com.alibaba.fastjson.serializer.SerializerFeature) r1)
            if (r20 == 0) goto L_0x090c
        L_0x088b:
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r6 = r5 + 1
            r21 = 92
            r20[r5] = r21
            byte[] r20 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r20 = r20[r7]
            r21 = 4
            r0 = r20
            r1 = r21
            if (r0 != r1) goto L_0x08fb
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r5 = r6 + 1
            r21 = 117(0x75, float:1.64E-43)
            r20[r6] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r6 = r5 + 1
            char[] r21 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r22 = r7 >>> 12
            r22 = r22 & 15
            char r21 = r21[r22]
            r20[r5] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r5 = r6 + 1
            char[] r21 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r22 = r7 >>> 8
            r22 = r22 & 15
            char r21 = r21[r22]
            r20[r6] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r6 = r5 + 1
            char[] r21 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r22 = r7 >>> 4
            r22 = r22 & 15
            char r21 = r21[r22]
            r20[r5] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r5 = r6 + 1
            char[] r21 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r22 = r7 & 15
            char r21 = r21[r22]
            r20[r6] = r21
            int r9 = r9 + 5
        L_0x08f7:
            int r11 = r11 + 1
            goto L_0x085a
        L_0x08fb:
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r5 = r6 + 1
            char[] r21 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r21 = r21[r7]
            r20[r6] = r21
            int r9 = r9 + 1
            goto L_0x08f7
        L_0x090c:
            r20 = 8232(0x2028, float:1.1535E-41)
            r0 = r20
            if (r7 != r0) goto L_0x0973
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r6 = r5 + 1
            r21 = 92
            r20[r5] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r5 = r6 + 1
            r21 = 117(0x75, float:1.64E-43)
            r20[r6] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r6 = r5 + 1
            char[] r21 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r22 = r7 >>> 12
            r22 = r22 & 15
            char r21 = r21[r22]
            r20[r5] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r5 = r6 + 1
            char[] r21 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r22 = r7 >>> 8
            r22 = r22 & 15
            char r21 = r21[r22]
            r20[r6] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r6 = r5 + 1
            char[] r21 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r22 = r7 >>> 4
            r22 = r22 & 15
            char r21 = r21[r22]
            r20[r5] = r21
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r5 = r6 + 1
            char[] r21 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r22 = r7 & 15
            char r21 = r21[r22]
            r20[r6] = r21
            int r9 = r9 + 5
            goto L_0x08f7
        L_0x0973:
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            int r6 = r5 + 1
            r20[r5] = r7
            r5 = r6
            goto L_0x08f7
        L_0x0980:
            r0 = r25
            char[] r0 = r0.buf
            r20 = r0
            r0 = r25
            int r0 = r0.count
            r21 = r0
            int r21 = r21 + -1
            r22 = 34
            r20[r21] = r22
            goto L_0x000e
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeWriter.writeStringWithDoubleQuote(java.lang.String, char):void");
    }

    public void writeFieldNameDirect(String text) {
        int len = text.length();
        int newcount = this.count + len + 3;
        if (newcount > this.buf.length) {
            expandCapacity(newcount);
        }
        int start = this.count + 1;
        this.buf[this.count] = '\"';
        text.getChars(0, len, this.buf, start);
        this.count = newcount;
        this.buf[this.count - 2] = '\"';
        this.buf[this.count - 1] = ':';
    }

    public void write(List<String> list) {
        int offset;
        if (list.isEmpty()) {
            write("[]");
            return;
        }
        int offset2 = this.count;
        int initOffset = offset2;
        int list_size = list.size();
        int offset3 = offset2;
        for (int i = 0; i < list_size; i++) {
            String text = list.get(i);
            boolean hasSpecial = false;
            if (text != null) {
                int len = text.length();
                for (int j = 0; j < len; j++) {
                    char ch = text.charAt(j);
                    hasSpecial = ch < ' ' || ch > '~' || ch == '\"' || ch == '\\';
                    if (hasSpecial) {
                        break;
                    }
                }
            } else {
                hasSpecial = true;
            }
            if (hasSpecial) {
                this.count = initOffset;
                write(91);
                for (int j2 = 0; j2 < list.size(); j2++) {
                    String text2 = list.get(j2);
                    if (j2 != 0) {
                        write(44);
                    }
                    if (text2 == null) {
                        write(Constant.NULL);
                    } else {
                        writeStringWithDoubleQuote(text2, 0);
                    }
                }
                write(93);
                return;
            }
            int newcount = text.length() + offset3 + 3;
            if (i == list.size() - 1) {
                newcount++;
            }
            if (newcount > this.buf.length) {
                this.count = offset3;
                expandCapacity(newcount);
            }
            if (i == 0) {
                offset = offset3 + 1;
                this.buf[offset3] = '[';
            } else {
                offset = offset3 + 1;
                this.buf[offset3] = ',';
            }
            int offset4 = offset + 1;
            this.buf[offset] = '\"';
            text.getChars(0, text.length(), this.buf, offset4);
            int offset5 = offset4 + text.length();
            offset3 = offset5 + 1;
            this.buf[offset5] = '\"';
        }
        this.buf[offset3] = ']';
        this.count = offset3 + 1;
    }

    public void writeFieldValue(char seperator, String name, char value) {
        write((int) seperator);
        writeFieldName(name);
        if (value == 0) {
            writeString(DexFormat.MAGIC_SUFFIX);
        } else {
            writeString(Character.toString(value));
        }
    }

    public void writeFieldValue(char seperator, String name, boolean value) {
        int intSize;
        if (!this.quoteFieldNames) {
            write((int) seperator);
            writeFieldName(name);
            write(value);
            return;
        }
        if (value) {
            intSize = 4;
        } else {
            intSize = 5;
        }
        int nameLen = name.length();
        int newcount = this.count + nameLen + 4 + intSize;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write((int) seperator);
                writeString(name);
                write(58);
                write(value);
                return;
            }
            expandCapacity(newcount);
        }
        int start = this.count;
        this.count = newcount;
        this.buf[start] = seperator;
        int nameEnd = start + nameLen + 1;
        this.buf[start + 1] = this.keySeperator;
        name.getChars(0, nameLen, this.buf, start + 2);
        this.buf[nameEnd + 1] = this.keySeperator;
        if (value) {
            System.arraycopy(":true".toCharArray(), 0, this.buf, nameEnd + 2, 5);
        } else {
            System.arraycopy(":false".toCharArray(), 0, this.buf, nameEnd + 2, 6);
        }
    }

    public void write(boolean value) {
        if (value) {
            write("true");
        } else {
            write("false");
        }
    }

    public void writeFieldValue(char seperator, String name, int value) {
        if (value == Integer.MIN_VALUE || !this.quoteFieldNames) {
            write((int) seperator);
            writeFieldName(name);
            writeInt(value);
            return;
        }
        int intSize = value < 0 ? IOUtils.stringSize(-value) + 1 : IOUtils.stringSize(value);
        int nameLen = name.length();
        int newcount = this.count + nameLen + 4 + intSize;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write((int) seperator);
                writeFieldName(name);
                writeInt(value);
                return;
            }
            expandCapacity(newcount);
        }
        int start = this.count;
        this.count = newcount;
        this.buf[start] = seperator;
        int nameEnd = start + nameLen + 1;
        this.buf[start + 1] = this.keySeperator;
        name.getChars(0, nameLen, this.buf, start + 2);
        this.buf[nameEnd + 1] = this.keySeperator;
        this.buf[nameEnd + 2] = ':';
        IOUtils.getChars(value, this.count, this.buf);
    }

    public void writeFieldValue(char seperator, String name, long value) {
        if (value == Long.MIN_VALUE || !this.quoteFieldNames) {
            write((int) seperator);
            writeFieldName(name);
            writeLong(value);
            return;
        }
        int intSize = value < 0 ? IOUtils.stringSize(-value) + 1 : IOUtils.stringSize(value);
        int nameLen = name.length();
        int newcount = this.count + nameLen + 4 + intSize;
        if (newcount > this.buf.length) {
            if (this.writer != null) {
                write((int) seperator);
                writeFieldName(name);
                writeLong(value);
                return;
            }
            expandCapacity(newcount);
        }
        int start = this.count;
        this.count = newcount;
        this.buf[start] = seperator;
        int nameEnd = start + nameLen + 1;
        this.buf[start + 1] = this.keySeperator;
        name.getChars(0, nameLen, this.buf, start + 2);
        this.buf[nameEnd + 1] = this.keySeperator;
        this.buf[nameEnd + 2] = ':';
        IOUtils.getChars(value, this.count, this.buf);
    }

    public void writeFieldValue(char seperator, String name, float value) {
        write((int) seperator);
        writeFieldName(name);
        writeFloat(value, false);
    }

    public void writeFieldValue(char seperator, String name, double value) {
        write((int) seperator);
        writeFieldName(name);
        writeDouble(value, false);
    }

    public void writeFieldValue(char seperator, String name, String value) {
        if (!this.quoteFieldNames) {
            write((int) seperator);
            writeFieldName(name);
            if (value == null) {
                writeNull();
            } else {
                writeString(value);
            }
        } else if (this.useSingleQuotes) {
            write((int) seperator);
            writeFieldName(name);
            if (value == null) {
                writeNull();
            } else {
                writeString(value);
            }
        } else if (isEnabled(SerializerFeature.BrowserSecure)) {
            write((int) seperator);
            writeStringWithDoubleQuote(name, ':');
            writeStringWithDoubleQuote(value, 0);
        } else if (isEnabled(SerializerFeature.BrowserCompatible)) {
            write((int) seperator);
            writeStringWithDoubleQuote(name, ':');
            writeStringWithDoubleQuote(value, 0);
        } else {
            writeFieldValueStringWithDoubleQuoteCheck(seperator, name, value);
        }
    }

    /* JADX WARNING: type inference failed for: r0v46, types: [int] */
    /* JADX WARNING: type inference failed for: r0v136, types: [int] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeFieldValueStringWithDoubleQuoteCheck(char r30, java.lang.String r31, java.lang.String r32) {
        /*
            r29 = this;
            int r17 = r31.length()
            r0 = r29
            int r0 = r0.count
            r19 = r0
            if (r32 != 0) goto L_0x0045
            r24 = 4
            int r26 = r17 + 8
            int r19 = r19 + r26
        L_0x0012:
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            r0 = r26
            int r0 = r0.length
            r26 = r0
            r0 = r19
            r1 = r26
            if (r0 <= r1) goto L_0x0057
            r0 = r29
            java.io.Writer r0 = r0.writer
            r26 = r0
            if (r26 == 0) goto L_0x0050
            r29.write((int) r30)
            r26 = 58
            r0 = r29
            r1 = r31
            r2 = r26
            r0.writeStringWithDoubleQuote(r1, r2)
            r26 = 0
            r0 = r29
            r1 = r32
            r2 = r26
            r0.writeStringWithDoubleQuote(r1, r2)
        L_0x0044:
            return
        L_0x0045:
            int r24 = r32.length()
            int r26 = r17 + r24
            int r26 = r26 + 6
            int r19 = r19 + r26
            goto L_0x0012
        L_0x0050:
            r0 = r29
            r1 = r19
            r0.expandCapacity(r1)
        L_0x0057:
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            r0 = r29
            int r0 = r0.count
            r27 = r0
            r26[r27] = r30
            r0 = r29
            int r0 = r0.count
            r26 = r0
            int r18 = r26 + 2
            int r16 = r18 + r17
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            r0 = r29
            int r0 = r0.count
            r27 = r0
            int r27 = r27 + 1
            r28 = 34
            r26[r27] = r28
            r26 = 0
            r0 = r29
            char[] r0 = r0.buf
            r27 = r0
            r0 = r31
            r1 = r26
            r2 = r17
            r3 = r27
            r4 = r18
            r0.getChars(r1, r2, r3, r4)
            r0 = r19
            r1 = r29
            r1.count = r0
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            r27 = 34
            r26[r16] = r27
            int r12 = r16 + 1
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r13 = r12 + 1
            r27 = 58
            r26[r12] = r27
            if (r32 != 0) goto L_0x00e8
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r12 = r13 + 1
            r27 = 110(0x6e, float:1.54E-43)
            r26[r13] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r13 = r12 + 1
            r27 = 117(0x75, float:1.64E-43)
            r26[r12] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r12 = r13 + 1
            r27 = 108(0x6c, float:1.51E-43)
            r26[r13] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r13 = r12 + 1
            r27 = 108(0x6c, float:1.51E-43)
            r26[r12] = r27
            goto L_0x0044
        L_0x00e8:
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r12 = r13 + 1
            r27 = 34
            r26[r13] = r27
            r25 = r12
            int r23 = r25 + r24
            r26 = 0
            r0 = r29
            char[] r0 = r0.buf
            r27 = r0
            r0 = r32
            r1 = r26
            r2 = r24
            r3 = r27
            r4 = r25
            r0.getChars(r1, r2, r3, r4)
            r20 = 0
            r15 = -1
            r10 = -1
            r14 = 0
            r11 = r25
        L_0x0114:
            r0 = r23
            if (r11 >= r0) goto L_0x017b
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            char r8 = r26[r11]
            r26 = 93
            r0 = r26
            if (r8 < r0) goto L_0x0148
            r26 = 127(0x7f, float:1.78E-43)
            r0 = r26
            if (r8 < r0) goto L_0x0145
            r26 = 8232(0x2028, float:1.1535E-41)
            r0 = r26
            if (r8 == r0) goto L_0x0138
            r26 = 160(0xa0, float:2.24E-43)
            r0 = r26
            if (r8 >= r0) goto L_0x0145
        L_0x0138:
            r26 = -1
            r0 = r26
            if (r10 != r0) goto L_0x013f
            r10 = r11
        L_0x013f:
            int r20 = r20 + 1
            r15 = r11
            r14 = r8
            int r19 = r19 + 4
        L_0x0145:
            int r11 = r11 + 1
            goto L_0x0114
        L_0x0148:
            r0 = r29
            int r0 = r0.features
            r26 = r0
            r0 = r26
            boolean r26 = isSpecial(r8, r0)
            if (r26 == 0) goto L_0x0145
            int r20 = r20 + 1
            r15 = r11
            r14 = r8
            byte[] r26 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            r0 = r26
            int r0 = r0.length
            r26 = r0
            r0 = r26
            if (r8 >= r0) goto L_0x0173
            byte[] r26 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r26 = r26[r8]
            r27 = 4
            r0 = r26
            r1 = r27
            if (r0 != r1) goto L_0x0173
            int r19 = r19 + 4
        L_0x0173:
            r26 = -1
            r0 = r26
            if (r10 != r0) goto L_0x0145
            r10 = r11
            goto L_0x0145
        L_0x017b:
            if (r20 <= 0) goto L_0x020e
            int r19 = r19 + r20
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            r0 = r26
            int r0 = r0.length
            r26 = r0
            r0 = r19
            r1 = r26
            if (r0 <= r1) goto L_0x0197
            r0 = r29
            r1 = r19
            r0.expandCapacity(r1)
        L_0x0197:
            r0 = r19
            r1 = r29
            r1.count = r0
            r26 = 1
            r0 = r20
            r1 = r26
            if (r0 != r1) goto L_0x02ef
            r26 = 8232(0x2028, float:1.1535E-41)
            r0 = r26
            if (r14 != r0) goto L_0x0222
            int r21 = r15 + 1
            int r9 = r15 + 6
            int r26 = r23 - r15
            int r5 = r26 + -1
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            r0 = r29
            char[] r0 = r0.buf
            r27 = r0
            r0 = r26
            r1 = r21
            r2 = r27
            java.lang.System.arraycopy(r0, r1, r2, r9, r5)
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            r27 = 92
            r26[r15] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r15 = r15 + 1
            r27 = 117(0x75, float:1.64E-43)
            r26[r15] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r15 = r15 + 1
            r27 = 50
            r26[r15] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r15 = r15 + 1
            r27 = 48
            r26[r15] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r15 = r15 + 1
            r27 = 50
            r26[r15] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r15 = r15 + 1
            r27 = 56
            r26[r15] = r27
        L_0x020e:
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            r0 = r29
            int r0 = r0.count
            r27 = r0
            int r27 = r27 + -1
            r28 = 34
            r26[r27] = r28
            goto L_0x0044
        L_0x0222:
            r8 = r14
            byte[] r26 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            r0 = r26
            int r0 = r0.length
            r26 = r0
            r0 = r26
            if (r8 >= r0) goto L_0x02b8
            byte[] r26 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r26 = r26[r8]
            r27 = 4
            r0 = r26
            r1 = r27
            if (r0 != r1) goto L_0x02b8
            int r21 = r15 + 1
            int r9 = r15 + 6
            int r26 = r23 - r15
            int r5 = r26 + -1
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            r0 = r29
            char[] r0 = r0.buf
            r27 = r0
            r0 = r26
            r1 = r21
            r2 = r27
            java.lang.System.arraycopy(r0, r1, r2, r9, r5)
            r6 = r15
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r7 = r6 + 1
            r27 = 92
            r26[r6] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r6 = r7 + 1
            r27 = 117(0x75, float:1.64E-43)
            r26[r7] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r7 = r6 + 1
            char[] r27 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r28 = r8 >>> 12
            r28 = r28 & 15
            char r27 = r27[r28]
            r26[r6] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r6 = r7 + 1
            char[] r27 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r28 = r8 >>> 8
            r28 = r28 & 15
            char r27 = r27[r28]
            r26[r7] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r7 = r6 + 1
            char[] r27 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r28 = r8 >>> 4
            r28 = r28 & 15
            char r27 = r27[r28]
            r26[r6] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r6 = r7 + 1
            char[] r27 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r28 = r8 & 15
            char r27 = r27[r28]
            r26[r7] = r27
            goto L_0x020e
        L_0x02b8:
            int r21 = r15 + 1
            int r9 = r15 + 2
            int r26 = r23 - r15
            int r5 = r26 + -1
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            r0 = r29
            char[] r0 = r0.buf
            r27 = r0
            r0 = r26
            r1 = r21
            r2 = r27
            java.lang.System.arraycopy(r0, r1, r2, r9, r5)
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            r27 = 92
            r26[r15] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r15 = r15 + 1
            char[] r27 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r27 = r27[r8]
            r26[r15] = r27
            goto L_0x020e
        L_0x02ef:
            r26 = 1
            r0 = r20
            r1 = r26
            if (r0 <= r1) goto L_0x020e
            int r22 = r10 - r25
            r6 = r10
            r11 = r22
        L_0x02fc:
            int r26 = r32.length()
            r0 = r26
            if (r11 >= r0) goto L_0x020e
            r0 = r32
            char r8 = r0.charAt(r11)
            byte[] r26 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            r0 = r26
            int r0 = r0.length
            r26 = r0
            r0 = r26
            if (r8 >= r0) goto L_0x031b
            byte[] r26 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r26 = r26[r8]
            if (r26 != 0) goto L_0x032d
        L_0x031b:
            r26 = 47
            r0 = r26
            if (r8 != r0) goto L_0x03ae
            com.alibaba.fastjson.serializer.SerializerFeature r26 = com.alibaba.fastjson.serializer.SerializerFeature.WriteSlashAsSpecial
            r0 = r29
            r1 = r26
            boolean r26 = r0.isEnabled((com.alibaba.fastjson.serializer.SerializerFeature) r1)
            if (r26 == 0) goto L_0x03ae
        L_0x032d:
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r7 = r6 + 1
            r27 = 92
            r26[r6] = r27
            byte[] r26 = com.alibaba.fastjson.util.IOUtils.specicalFlags_doubleQuotes
            byte r26 = r26[r8]
            r27 = 4
            r0 = r26
            r1 = r27
            if (r0 != r1) goto L_0x039d
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r6 = r7 + 1
            r27 = 117(0x75, float:1.64E-43)
            r26[r7] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r7 = r6 + 1
            char[] r27 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r28 = r8 >>> 12
            r28 = r28 & 15
            char r27 = r27[r28]
            r26[r6] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r6 = r7 + 1
            char[] r27 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r28 = r8 >>> 8
            r28 = r28 & 15
            char r27 = r27[r28]
            r26[r7] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r7 = r6 + 1
            char[] r27 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r28 = r8 >>> 4
            r28 = r28 & 15
            char r27 = r27[r28]
            r26[r6] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r6 = r7 + 1
            char[] r27 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r28 = r8 & 15
            char r27 = r27[r28]
            r26[r7] = r27
            int r23 = r23 + 5
        L_0x0399:
            int r11 = r11 + 1
            goto L_0x02fc
        L_0x039d:
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r6 = r7 + 1
            char[] r27 = com.alibaba.fastjson.util.IOUtils.replaceChars
            char r27 = r27[r8]
            r26[r7] = r27
            int r23 = r23 + 1
            goto L_0x0399
        L_0x03ae:
            r26 = 8232(0x2028, float:1.1535E-41)
            r0 = r26
            if (r8 != r0) goto L_0x0415
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r7 = r6 + 1
            r27 = 92
            r26[r6] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r6 = r7 + 1
            r27 = 117(0x75, float:1.64E-43)
            r26[r7] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r7 = r6 + 1
            char[] r27 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r28 = r8 >>> 12
            r28 = r28 & 15
            char r27 = r27[r28]
            r26[r6] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r6 = r7 + 1
            char[] r27 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r28 = r8 >>> 8
            r28 = r28 & 15
            char r27 = r27[r28]
            r26[r7] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r7 = r6 + 1
            char[] r27 = com.alibaba.fastjson.util.IOUtils.DIGITS
            int r28 = r8 >>> 4
            r28 = r28 & 15
            char r27 = r27[r28]
            r26[r6] = r27
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r6 = r7 + 1
            char[] r27 = com.alibaba.fastjson.util.IOUtils.DIGITS
            r28 = r8 & 15
            char r27 = r27[r28]
            r26[r7] = r27
            int r23 = r23 + 5
            goto L_0x0399
        L_0x0415:
            r0 = r29
            char[] r0 = r0.buf
            r26 = r0
            int r7 = r6 + 1
            r26[r6] = r8
            r6 = r7
            goto L_0x0399
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.serializer.SerializeWriter.writeFieldValueStringWithDoubleQuoteCheck(char, java.lang.String, java.lang.String):void");
    }

    public void writeFieldValueStringWithDoubleQuote(char seperator, String name, String value) {
        int nameLen = name.length();
        int newcount = this.count;
        int valueLen = value.length();
        int newcount2 = newcount + nameLen + valueLen + 6;
        if (newcount2 > this.buf.length) {
            if (this.writer != null) {
                write((int) seperator);
                writeStringWithDoubleQuote(name, ':');
                writeStringWithDoubleQuote(value, 0);
                return;
            }
            expandCapacity(newcount2);
        }
        this.buf[this.count] = seperator;
        int nameStart = this.count + 2;
        int nameEnd = nameStart + nameLen;
        this.buf[this.count + 1] = '\"';
        name.getChars(0, nameLen, this.buf, nameStart);
        this.count = newcount2;
        this.buf[nameEnd] = '\"';
        int index = nameEnd + 1;
        int index2 = index + 1;
        this.buf[index] = ':';
        this.buf[index2] = '\"';
        value.getChars(0, valueLen, this.buf, index2 + 1);
        this.buf[this.count - 1] = '\"';
    }

    static boolean isSpecial(char ch, int features2) {
        boolean z = true;
        if (ch == ' ') {
            return false;
        }
        if (ch == '/') {
            if ((SerializerFeature.WriteSlashAsSpecial.mask & features2) == 0) {
                z = false;
            }
            return z;
        } else if (ch > '#' && ch != '\\') {
            return false;
        } else {
            if (ch <= 31 || ch == '\\' || ch == '\"') {
                return true;
            }
            return false;
        }
    }

    public void writeFieldValue(char seperator, String name, Enum<?> value) {
        if (value == null) {
            write((int) seperator);
            writeFieldName(name);
            writeNull();
        } else if (this.writeEnumUsingName && !this.writeEnumUsingToString) {
            writeEnumFieldValue(seperator, name, value.name());
        } else if (this.writeEnumUsingToString) {
            writeEnumFieldValue(seperator, name, value.toString());
        } else {
            writeFieldValue(seperator, name, value.ordinal());
        }
    }

    private void writeEnumFieldValue(char seperator, String name, String value) {
        if (this.useSingleQuotes) {
            writeFieldValue(seperator, name, value);
        } else {
            writeFieldValueStringWithDoubleQuote(seperator, name, value);
        }
    }

    public void writeFieldValue(char seperator, String name, BigDecimal value) {
        write((int) seperator);
        writeFieldName(name);
        if (value == null) {
            writeNull();
        } else {
            write(value.toString());
        }
    }

    public void writeString(String text, char seperator) {
        if (this.useSingleQuotes) {
            writeStringWithSingleQuote(text);
            write((int) seperator);
            return;
        }
        writeStringWithDoubleQuote(text, seperator);
    }

    public void writeString(String text) {
        if (this.useSingleQuotes) {
            writeStringWithSingleQuote(text);
        } else {
            writeStringWithDoubleQuote(text, 0);
        }
    }

    /* access modifiers changed from: protected */
    public void writeStringWithSingleQuote(String text) {
        if (text == null) {
            int newcount = this.count + 4;
            if (newcount > this.buf.length) {
                expandCapacity(newcount);
            }
            Constant.NULL.getChars(0, 4, this.buf, this.count);
            this.count = newcount;
            return;
        }
        int len = text.length();
        int newcount2 = this.count + len + 2;
        if (newcount2 > this.buf.length) {
            if (this.writer != null) {
                write(39);
                for (int i = 0; i < text.length(); i++) {
                    char ch = text.charAt(i);
                    if (ch <= 13 || ch == '\\' || ch == '\'' || (ch == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                        write(92);
                        write((int) IOUtils.replaceChars[ch]);
                    } else {
                        write((int) ch);
                    }
                }
                write(39);
                return;
            }
            expandCapacity(newcount2);
        }
        int start = this.count + 1;
        int end = start + len;
        this.buf[this.count] = '\'';
        text.getChars(0, len, this.buf, start);
        this.count = newcount2;
        int specialCount = 0;
        int lastSpecialIndex = -1;
        char lastSpecial = 0;
        for (int i2 = start; i2 < end; i2++) {
            char ch2 = this.buf[i2];
            if (ch2 <= 13 || ch2 == '\\' || ch2 == '\'' || (ch2 == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                specialCount++;
                lastSpecialIndex = i2;
                lastSpecial = ch2;
            }
        }
        int newcount3 = newcount2 + specialCount;
        if (newcount3 > this.buf.length) {
            expandCapacity(newcount3);
        }
        this.count = newcount3;
        if (specialCount == 1) {
            System.arraycopy(this.buf, lastSpecialIndex + 1, this.buf, lastSpecialIndex + 2, (end - lastSpecialIndex) - 1);
            this.buf[lastSpecialIndex] = '\\';
            this.buf[lastSpecialIndex + 1] = IOUtils.replaceChars[lastSpecial];
        } else if (specialCount > 1) {
            System.arraycopy(this.buf, lastSpecialIndex + 1, this.buf, lastSpecialIndex + 2, (end - lastSpecialIndex) - 1);
            this.buf[lastSpecialIndex] = '\\';
            int lastSpecialIndex2 = lastSpecialIndex + 1;
            this.buf[lastSpecialIndex2] = IOUtils.replaceChars[lastSpecial];
            int end2 = end + 1;
            for (int i3 = lastSpecialIndex2 - 2; i3 >= start; i3--) {
                char ch3 = this.buf[i3];
                if (ch3 <= 13 || ch3 == '\\' || ch3 == '\'' || (ch3 == '/' && isEnabled(SerializerFeature.WriteSlashAsSpecial))) {
                    System.arraycopy(this.buf, i3 + 1, this.buf, i3 + 2, (end2 - i3) - 1);
                    this.buf[i3] = '\\';
                    this.buf[i3 + 1] = IOUtils.replaceChars[ch3];
                    end2++;
                }
            }
        }
        this.buf[this.count - 1] = '\'';
    }

    public void writeFieldName(String key) {
        writeFieldName(key, false);
    }

    public void writeFieldName(String key, boolean checkSpecial) {
        boolean hashSpecial;
        if (key == null) {
            write("null:");
        } else if (this.useSingleQuotes) {
            if (this.quoteFieldNames) {
                writeStringWithSingleQuote(key);
                write(58);
                return;
            }
            writeKeyWithSingleQuoteIfHasSpecial(key);
        } else if (this.quoteFieldNames) {
            writeStringWithDoubleQuote(key, ':');
        } else {
            if (key.length() == 0) {
                hashSpecial = true;
            } else {
                hashSpecial = false;
            }
            int i = 0;
            while (true) {
                if (i >= key.length()) {
                    break;
                } else if (isSpecial(key.charAt(i), 0)) {
                    hashSpecial = true;
                    break;
                } else {
                    i++;
                }
            }
            if (hashSpecial) {
                writeStringWithDoubleQuote(key, ':');
                return;
            }
            write(key);
            write(58);
        }
    }

    private void writeKeyWithSingleQuoteIfHasSpecial(String text) {
        byte[] specicalFlags_singleQuotes = IOUtils.specicalFlags_singleQuotes;
        int len = text.length();
        int newcount = this.count + len + 1;
        if (newcount > this.buf.length) {
            if (this.writer == null) {
                expandCapacity(newcount);
            } else if (len == 0) {
                write(39);
                write(39);
                write(58);
                return;
            } else {
                boolean hasSpecial = false;
                int i = 0;
                while (true) {
                    if (i < len) {
                        char ch = text.charAt(i);
                        if (ch < specicalFlags_singleQuotes.length && specicalFlags_singleQuotes[ch] != 0) {
                            hasSpecial = true;
                            break;
                        }
                        i++;
                    } else {
                        break;
                    }
                }
                if (hasSpecial) {
                    write(39);
                }
                for (int i2 = 0; i2 < len; i2++) {
                    char ch2 = text.charAt(i2);
                    if (ch2 >= specicalFlags_singleQuotes.length || specicalFlags_singleQuotes[ch2] == 0) {
                        write((int) ch2);
                    } else {
                        write(92);
                        write((int) IOUtils.replaceChars[ch2]);
                    }
                }
                if (hasSpecial) {
                    write(39);
                }
                write(58);
                return;
            }
        }
        if (len == 0) {
            if (this.count + 3 > this.buf.length) {
                expandCapacity(this.count + 3);
            }
            char[] cArr = this.buf;
            int i3 = this.count;
            this.count = i3 + 1;
            cArr[i3] = '\'';
            char[] cArr2 = this.buf;
            int i4 = this.count;
            this.count = i4 + 1;
            cArr2[i4] = '\'';
            char[] cArr3 = this.buf;
            int i5 = this.count;
            this.count = i5 + 1;
            cArr3[i5] = ':';
            return;
        }
        int start = this.count;
        int end = start + len;
        text.getChars(0, len, this.buf, start);
        this.count = newcount;
        boolean hasSpecial2 = false;
        int i6 = start;
        while (i6 < end) {
            char ch3 = this.buf[i6];
            if (ch3 < specicalFlags_singleQuotes.length && specicalFlags_singleQuotes[ch3] != 0) {
                if (!hasSpecial2) {
                    newcount += 3;
                    if (newcount > this.buf.length) {
                        expandCapacity(newcount);
                    }
                    this.count = newcount;
                    System.arraycopy(this.buf, i6 + 1, this.buf, i6 + 3, (end - i6) - 1);
                    System.arraycopy(this.buf, 0, this.buf, 1, i6);
                    this.buf[start] = '\'';
                    int i7 = i6 + 1;
                    this.buf[i7] = '\\';
                    i6 = i7 + 1;
                    this.buf[i6] = IOUtils.replaceChars[ch3];
                    end += 2;
                    this.buf[this.count - 2] = '\'';
                    hasSpecial2 = true;
                } else {
                    newcount++;
                    if (newcount > this.buf.length) {
                        expandCapacity(newcount);
                    }
                    this.count = newcount;
                    System.arraycopy(this.buf, i6 + 1, this.buf, i6 + 2, end - i6);
                    this.buf[i6] = '\\';
                    i6++;
                    this.buf[i6] = IOUtils.replaceChars[ch3];
                    end++;
                }
            }
            i6++;
        }
        this.buf[newcount - 1] = ':';
    }

    public void flush() {
        if (this.writer != null) {
            try {
                this.writer.write(this.buf, 0, this.count);
                this.writer.flush();
                this.count = 0;
            } catch (IOException e) {
                throw new JSONException(e.getMessage(), e);
            }
        }
    }
}
