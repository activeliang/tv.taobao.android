package org.apache.commons.codec.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;

public class URLCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder {
    protected static final byte ESCAPE_CHAR = 37;
    @Deprecated
    protected static final BitSet WWW_FORM_URL = ((BitSet) WWW_FORM_URL_SAFE.clone());
    private static final BitSet WWW_FORM_URL_SAFE = new BitSet(256);
    @Deprecated
    protected volatile String charset;

    static {
        for (int i = 97; i <= 122; i++) {
            WWW_FORM_URL_SAFE.set(i);
        }
        for (int i2 = 65; i2 <= 90; i2++) {
            WWW_FORM_URL_SAFE.set(i2);
        }
        for (int i3 = 48; i3 <= 57; i3++) {
            WWW_FORM_URL_SAFE.set(i3);
        }
        WWW_FORM_URL_SAFE.set(45);
        WWW_FORM_URL_SAFE.set(95);
        WWW_FORM_URL_SAFE.set(46);
        WWW_FORM_URL_SAFE.set(42);
        WWW_FORM_URL_SAFE.set(32);
    }

    public URLCodec() {
        this("UTF-8");
    }

    public URLCodec(String charset2) {
        this.charset = charset2;
    }

    public static final byte[] encodeUrl(BitSet urlsafe, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        if (urlsafe == null) {
            urlsafe = WWW_FORM_URL_SAFE;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            int b = bytes[i];
            if (b < 0) {
                b += 256;
            }
            if (urlsafe.get(b)) {
                if (b == 32) {
                    b = 43;
                }
                buffer.write(b);
            } else {
                buffer.write(37);
                char hex1 = Utils.hexDigit(b >> 4);
                char hex2 = Utils.hexDigit(b);
                buffer.write(hex1);
                buffer.write(hex2);
            }
        }
        return buffer.toByteArray();
    }

    public static final byte[] decodeUrl(byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int i = 0;
        while (i < bytes.length) {
            byte b = bytes[i];
            if (b == 43) {
                buffer.write(32);
            } else if (b == 37) {
                int i2 = i + 1;
                try {
                    int u = Utils.digit16(bytes[i2]);
                    i = i2 + 1;
                    buffer.write((char) ((u << 4) + Utils.digit16(bytes[i])));
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new DecoderException("Invalid URL encoding: ", e);
                }
            } else {
                buffer.write(b);
            }
            i++;
        }
        return buffer.toByteArray();
    }

    public byte[] encode(byte[] bytes) {
        return encodeUrl(WWW_FORM_URL_SAFE, bytes);
    }

    public byte[] decode(byte[] bytes) throws DecoderException {
        return decodeUrl(bytes);
    }

    public String encode(String str, String charsetName) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return StringUtils.newStringUsAscii(encode(str.getBytes(charsetName)));
    }

    public String encode(String str) throws EncoderException {
        if (str == null) {
            return null;
        }
        try {
            return encode(str, getDefaultCharset());
        } catch (UnsupportedEncodingException e) {
            throw new EncoderException(e.getMessage(), e);
        }
    }

    public String decode(String str, String charsetName) throws DecoderException, UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        return new String(decode(StringUtils.getBytesUsAscii(str)), charsetName);
    }

    public String decode(String str) throws DecoderException {
        if (str == null) {
            return null;
        }
        try {
            return decode(str, getDefaultCharset());
        } catch (UnsupportedEncodingException e) {
            throw new DecoderException(e.getMessage(), e);
        }
    }

    public Object encode(Object obj) throws EncoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return encode((byte[]) (byte[]) obj);
        }
        if (obj instanceof String) {
            return encode((String) obj);
        }
        throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be URL encoded");
    }

    public Object decode(Object obj) throws DecoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return decode((byte[]) (byte[]) obj);
        }
        if (obj instanceof String) {
            return decode((String) obj);
        }
        throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be URL decoded");
    }

    public String getDefaultCharset() {
        return this.charset;
    }

    @Deprecated
    public String getEncoding() {
        return this.charset;
    }
}
