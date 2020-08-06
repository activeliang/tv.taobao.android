package com.taobao.atlas.dex.util;

public final class ByteArrayByteInput implements ByteInput {
    private final byte[] bytes;
    private int position;

    public ByteArrayByteInput(byte... bytes2) {
        this.bytes = bytes2;
    }

    public byte readByte() {
        byte[] bArr = this.bytes;
        int i = this.position;
        this.position = i + 1;
        return bArr[i];
    }
}
