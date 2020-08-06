package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.BitSet;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;

public class QCodec extends RFC1522Codec implements StringEncoder, StringDecoder {
    private static final BitSet PRINTABLE_CHARS = new BitSet(256);
    private static final byte SPACE = 32;
    private static final byte UNDERSCORE = 95;
    private final Charset charset;
    private boolean encodeBlanks;

    static {
        PRINTABLE_CHARS.set(32);
        PRINTABLE_CHARS.set(33);
        PRINTABLE_CHARS.set(34);
        PRINTABLE_CHARS.set(35);
        PRINTABLE_CHARS.set(36);
        PRINTABLE_CHARS.set(37);
        PRINTABLE_CHARS.set(38);
        PRINTABLE_CHARS.set(39);
        PRINTABLE_CHARS.set(40);
        PRINTABLE_CHARS.set(41);
        PRINTABLE_CHARS.set(42);
        PRINTABLE_CHARS.set(43);
        PRINTABLE_CHARS.set(44);
        PRINTABLE_CHARS.set(45);
        PRINTABLE_CHARS.set(46);
        PRINTABLE_CHARS.set(47);
        for (int i = 48; i <= 57; i++) {
            PRINTABLE_CHARS.set(i);
        }
        PRINTABLE_CHARS.set(58);
        PRINTABLE_CHARS.set(59);
        PRINTABLE_CHARS.set(60);
        PRINTABLE_CHARS.set(62);
        PRINTABLE_CHARS.set(64);
        for (int i2 = 65; i2 <= 90; i2++) {
            PRINTABLE_CHARS.set(i2);
        }
        PRINTABLE_CHARS.set(91);
        PRINTABLE_CHARS.set(92);
        PRINTABLE_CHARS.set(93);
        PRINTABLE_CHARS.set(94);
        PRINTABLE_CHARS.set(96);
        for (int i3 = 97; i3 <= 122; i3++) {
            PRINTABLE_CHARS.set(i3);
        }
        PRINTABLE_CHARS.set(123);
        PRINTABLE_CHARS.set(124);
        PRINTABLE_CHARS.set(125);
        PRINTABLE_CHARS.set(126);
    }

    public QCodec() {
        this(Charsets.UTF_8);
    }

    public QCodec(Charset charset2) {
        this.encodeBlanks = false;
        this.charset = charset2;
    }

    public QCodec(String charsetName) {
        this(Charset.forName(charsetName));
    }

    /* access modifiers changed from: protected */
    public String getEncoding() {
        return "Q";
    }

    /* access modifiers changed from: protected */
    public byte[] doEncoding(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        byte[] data = QuotedPrintableCodec.encodeQuotedPrintable(PRINTABLE_CHARS, bytes);
        if (!this.encodeBlanks) {
            return data;
        }
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 32) {
                data[i] = UNDERSCORE;
            }
        }
        return data;
    }

    /* access modifiers changed from: protected */
    public byte[] doDecoding(byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }
        boolean hasUnderscores = false;
        int length = bytes.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            } else if (bytes[i] == 95) {
                hasUnderscores = true;
                break;
            } else {
                i++;
            }
        }
        if (!hasUnderscores) {
            return QuotedPrintableCodec.decodeQuotedPrintable(bytes);
        }
        byte[] tmp = new byte[bytes.length];
        for (int i2 = 0; i2 < bytes.length; i2++) {
            byte b = bytes[i2];
            if (b != 95) {
                tmp[i2] = b;
            } else {
                tmp[i2] = SPACE;
            }
        }
        return QuotedPrintableCodec.decodeQuotedPrintable(tmp);
    }

    public String encode(String sourceStr, Charset sourceCharset) throws EncoderException {
        if (sourceStr == null) {
            return null;
        }
        return encodeText(sourceStr, sourceCharset);
    }

    public String encode(String sourceStr, String sourceCharset) throws EncoderException {
        if (sourceStr == null) {
            return null;
        }
        try {
            return encodeText(sourceStr, sourceCharset);
        } catch (UnsupportedEncodingException e) {
            throw new EncoderException(e.getMessage(), e);
        }
    }

    public String encode(String sourceStr) throws EncoderException {
        if (sourceStr == null) {
            return null;
        }
        return encode(sourceStr, getCharset());
    }

    public String decode(String str) throws DecoderException {
        if (str == null) {
            return null;
        }
        try {
            return decodeText(str);
        } catch (UnsupportedEncodingException e) {
            throw new DecoderException(e.getMessage(), e);
        }
    }

    public Object encode(Object obj) throws EncoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return encode((String) obj);
        }
        throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be encoded using Q codec");
    }

    public Object decode(Object obj) throws DecoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return decode((String) obj);
        }
        throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be decoded using Q codec");
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getDefaultCharset() {
        return this.charset.name();
    }

    public boolean isEncodeBlanks() {
        return this.encodeBlanks;
    }

    public void setEncodeBlanks(boolean b) {
        this.encodeBlanks = b;
    }
}
