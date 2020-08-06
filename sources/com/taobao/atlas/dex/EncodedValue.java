package com.taobao.atlas.dex;

import com.taobao.atlas.dex.Dex;
import com.taobao.atlas.dex.util.ByteArrayByteInput;
import com.taobao.atlas.dex.util.ByteInput;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;

public final class EncodedValue implements Comparable<EncodedValue> {
    private final byte[] data;

    public EncodedValue(byte[] data2) {
        this.data = data2;
    }

    public ByteInput asByteInput() {
        return new ByteArrayByteInput(this.data);
    }

    public byte[] getBytes() {
        return this.data;
    }

    public void writeTo(Dex.Section out) {
        out.write(this.data);
    }

    public int compareTo(EncodedValue other) {
        int size = Math.min(this.data.length, other.data.length);
        for (int i = 0; i < size; i++) {
            if (this.data[i] != other.data[i]) {
                return (this.data[i] & OnReminderListener.RET_FULL) - (other.data[i] & OnReminderListener.RET_FULL);
            }
        }
        return this.data.length - other.data.length;
    }

    public String toString() {
        return Integer.toHexString(this.data[0] & OnReminderListener.RET_FULL) + "...(" + this.data.length + ")";
    }
}
