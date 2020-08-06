package org.apache.commons.codec.net;

import java.nio.ByteBuffer;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class PercentCodec implements BinaryEncoder, BinaryDecoder {
    private final byte ESCAPE_CHAR;
    private final BitSet alwaysEncodeChars;
    private int alwaysEncodeCharsMax;
    private int alwaysEncodeCharsMin;
    private final boolean plusForSpace;

    public PercentCodec() {
        this.ESCAPE_CHAR = 37;
        this.alwaysEncodeChars = new BitSet();
        this.alwaysEncodeCharsMin = Integer.MAX_VALUE;
        this.alwaysEncodeCharsMax = Integer.MIN_VALUE;
        this.plusForSpace = false;
        insertAlwaysEncodeChar((byte) 37);
    }

    public PercentCodec(byte[] alwaysEncodeChars2, boolean plusForSpace2) {
        this.ESCAPE_CHAR = 37;
        this.alwaysEncodeChars = new BitSet();
        this.alwaysEncodeCharsMin = Integer.MAX_VALUE;
        this.alwaysEncodeCharsMax = Integer.MIN_VALUE;
        this.plusForSpace = plusForSpace2;
        insertAlwaysEncodeChars(alwaysEncodeChars2);
    }

    private void insertAlwaysEncodeChars(byte[] alwaysEncodeCharsArray) {
        if (alwaysEncodeCharsArray != null) {
            for (byte b : alwaysEncodeCharsArray) {
                insertAlwaysEncodeChar(b);
            }
        }
        insertAlwaysEncodeChar((byte) 37);
    }

    private void insertAlwaysEncodeChar(byte b) {
        this.alwaysEncodeChars.set(b);
        if (b < this.alwaysEncodeCharsMin) {
            this.alwaysEncodeCharsMin = b;
        }
        if (b > this.alwaysEncodeCharsMax) {
            this.alwaysEncodeCharsMax = b;
        }
    }

    public byte[] encode(byte[] bytes) throws EncoderException {
        if (bytes == null) {
            return null;
        }
        int expectedEncodingBytes = expectedEncodingBytes(bytes);
        boolean willEncode = expectedEncodingBytes != bytes.length;
        if (willEncode || (this.plusForSpace && containsSpace(bytes))) {
            return doEncode(bytes, expectedEncodingBytes, willEncode);
        }
        return bytes;
    }

    private byte[] doEncode(byte[] bytes, int expectedLength, boolean willEncode) {
        ByteBuffer buffer = ByteBuffer.allocate(expectedLength);
        for (byte b : bytes) {
            if (willEncode && canEncode(b)) {
                byte bb = b;
                if (bb < 0) {
                    bb = (byte) (bb + 256);
                }
                char hex1 = Utils.hexDigit(bb >> 4);
                char hex2 = Utils.hexDigit(bb);
                buffer.put((byte) 37);
                buffer.put((byte) hex1);
                buffer.put((byte) hex2);
            } else if (!this.plusForSpace || b != 32) {
                buffer.put(b);
            } else {
                buffer.put((byte) 43);
            }
        }
        return buffer.array();
    }

    private int expectedEncodingBytes(byte[] bytes) {
        int byteCount = 0;
        int length = bytes.length;
        for (int i = 0; i < length; i++) {
            byteCount += canEncode(bytes[i]) ? 3 : 1;
        }
        return byteCount;
    }

    private boolean containsSpace(byte[] bytes) {
        for (byte b : bytes) {
            if (b == 32) {
                return true;
            }
        }
        return false;
    }

    private boolean canEncode(byte c) {
        return !isAsciiChar(c) || (inAlwaysEncodeCharsRange(c) && this.alwaysEncodeChars.get(c));
    }

    private boolean inAlwaysEncodeCharsRange(byte c) {
        return c >= this.alwaysEncodeCharsMin && c <= this.alwaysEncodeCharsMax;
    }

    private boolean isAsciiChar(byte c) {
        return c >= 0;
    }

    public byte[] decode(byte[] bytes) throws DecoderException {
        if (bytes == null) {
            return null;
        }
        ByteBuffer buffer = ByteBuffer.allocate(expectedDecodingBytes(bytes));
        int i = 0;
        while (i < bytes.length) {
            byte b = bytes[i];
            if (b == 37) {
                int i2 = i + 1;
                try {
                    int u = Utils.digit16(bytes[i2]);
                    i = i2 + 1;
                    buffer.put((byte) ((u << 4) + Utils.digit16(bytes[i])));
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new DecoderException("Invalid percent decoding: ", e);
                }
            } else if (!this.plusForSpace || b != 43) {
                buffer.put(b);
            } else {
                buffer.put((byte) 32);
            }
            i++;
        }
        return buffer.array();
    }

    private int expectedDecodingBytes(byte[] bytes) {
        int byteCount = 0;
        int i = 0;
        while (i < bytes.length) {
            i += bytes[i] == 37 ? 3 : 1;
            byteCount++;
        }
        return byteCount;
    }

    public Object encode(Object obj) throws EncoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return encode((byte[]) (byte[]) obj);
        }
        throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be Percent encoded");
    }

    public Object decode(Object obj) throws DecoderException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof byte[]) {
            return decode((byte[]) (byte[]) obj);
        }
        throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be Percent decoded");
    }
}
