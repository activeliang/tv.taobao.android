package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Base64;

public class BCodec extends RFC1522Codec implements StringEncoder, StringDecoder {
    private final Charset charset;

    public BCodec() {
        this(Charsets.UTF_8);
    }

    public BCodec(Charset charset2) {
        this.charset = charset2;
    }

    public BCodec(String charsetName) {
        this(Charset.forName(charsetName));
    }

    /* access modifiers changed from: protected */
    public String getEncoding() {
        return "B";
    }

    /* access modifiers changed from: protected */
    public byte[] doEncoding(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return Base64.encodeBase64(bytes);
    }

    /* access modifiers changed from: protected */
    public byte[] doDecoding(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return Base64.decodeBase64(bytes);
    }

    public String encode(String strSource, Charset sourceCharset) throws EncoderException {
        if (strSource == null) {
            return null;
        }
        return encodeText(strSource, sourceCharset);
    }

    public String encode(String strSource, String sourceCharset) throws EncoderException {
        if (strSource == null) {
            return null;
        }
        try {
            return encodeText(strSource, sourceCharset);
        } catch (UnsupportedEncodingException e) {
            throw new EncoderException(e.getMessage(), e);
        }
    }

    public String encode(String strSource) throws EncoderException {
        if (strSource == null) {
            return null;
        }
        return encode(strSource, getCharset());
    }

    public String decode(String value) throws DecoderException {
        if (value == null) {
            return null;
        }
        try {
            return decodeText(value);
        } catch (UnsupportedEncodingException e) {
            throw new DecoderException(e.getMessage(), e);
        }
    }

    public Object encode(Object value) throws EncoderException {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return encode((String) value);
        }
        throw new EncoderException("Objects of type " + value.getClass().getName() + " cannot be encoded using BCodec");
    }

    public Object decode(Object value) throws DecoderException {
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return decode((String) value);
        }
        throw new DecoderException("Objects of type " + value.getClass().getName() + " cannot be decoded using BCodec");
    }

    public Charset getCharset() {
        return this.charset;
    }

    public String getDefaultCharset() {
        return this.charset.name();
    }
}
