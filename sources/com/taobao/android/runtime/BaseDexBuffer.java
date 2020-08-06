package com.taobao.android.runtime;

import android.support.annotation.NonNull;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;

public class BaseDexBuffer {
    final int baseOffset;
    @NonNull
    final byte[] buf;

    public BaseDexBuffer(@NonNull byte[] buf2) {
        this(buf2, 0);
    }

    public BaseDexBuffer(@NonNull byte[] buf2, int offset) {
        this.buf = buf2;
        this.baseOffset = offset;
    }

    public int readSmallUint(int offset) {
        byte[] buf2 = this.buf;
        int offset2 = offset + this.baseOffset;
        int result = (buf2[offset2] & 255) | ((buf2[offset2 + 1] & OnReminderListener.RET_FULL) << 8) | ((buf2[offset2 + 2] & OnReminderListener.RET_FULL) << 16) | (buf2[offset2 + 3] << 24);
        if (result >= 0) {
            return result;
        }
        throw new ExceptionWithContext("Encountered small uint that is out of range at offset 0x%x", Integer.valueOf(offset2));
    }

    public int readOptionalUint(int offset) {
        byte[] buf2 = this.buf;
        int offset2 = offset + this.baseOffset;
        int result = (buf2[offset2] & 255) | ((buf2[offset2 + 1] & OnReminderListener.RET_FULL) << 8) | ((buf2[offset2 + 2] & OnReminderListener.RET_FULL) << 16) | (buf2[offset2 + 3] << 24);
        if (result >= -1) {
            return result;
        }
        throw new ExceptionWithContext("Encountered optional uint that is out of range at offset 0x%x", Integer.valueOf(offset2));
    }

    public int readUshort(int offset) {
        byte[] buf2 = this.buf;
        int offset2 = offset + this.baseOffset;
        return (buf2[offset2] & OnReminderListener.RET_FULL) | ((buf2[offset2 + 1] & OnReminderListener.RET_FULL) << 8);
    }

    public int readUbyte(int offset) {
        return this.buf[this.baseOffset + offset] & OnReminderListener.RET_FULL;
    }

    public long readLong(int offset) {
        byte[] buf2 = this.buf;
        int offset2 = offset + this.baseOffset;
        return ((long) ((buf2[offset2] & OnReminderListener.RET_FULL) | ((buf2[offset2 + 1] & OnReminderListener.RET_FULL) << 8) | ((buf2[offset2 + 2] & OnReminderListener.RET_FULL) << 16))) | ((((long) buf2[offset2 + 3]) & 255) << 24) | ((((long) buf2[offset2 + 4]) & 255) << 32) | ((((long) buf2[offset2 + 5]) & 255) << 40) | ((((long) buf2[offset2 + 6]) & 255) << 48) | (((long) buf2[offset2 + 7]) << 56);
    }

    public int readLongAsSmallUint(int offset) {
        byte[] buf2 = this.buf;
        int offset2 = offset + this.baseOffset;
        long result = ((long) ((buf2[offset2] & OnReminderListener.RET_FULL) | ((buf2[offset2 + 1] & OnReminderListener.RET_FULL) << 8) | ((buf2[offset2 + 2] & OnReminderListener.RET_FULL) << 16))) | ((((long) buf2[offset2 + 3]) & 255) << 24) | ((((long) buf2[offset2 + 4]) & 255) << 32) | ((((long) buf2[offset2 + 5]) & 255) << 40) | ((((long) buf2[offset2 + 6]) & 255) << 48) | (((long) buf2[offset2 + 7]) << 56);
        if (result >= 0 && result <= 2147483647L) {
            return (int) result;
        }
        throw new ExceptionWithContext("Encountered out-of-range ulong at offset 0x%x", Integer.valueOf(offset2));
    }

    public int readInt(int offset) {
        byte[] buf2 = this.buf;
        int offset2 = offset + this.baseOffset;
        return (buf2[offset2] & OnReminderListener.RET_FULL) | ((buf2[offset2 + 1] & OnReminderListener.RET_FULL) << 8) | ((buf2[offset2 + 2] & OnReminderListener.RET_FULL) << 16) | (buf2[offset2 + 3] << 24);
    }

    public int readShort(int offset) {
        byte[] buf2 = this.buf;
        int offset2 = offset + this.baseOffset;
        return (buf2[offset2] & OnReminderListener.RET_FULL) | (buf2[offset2 + 1] << 8);
    }

    public int readByte(int offset) {
        return this.buf[this.baseOffset + offset];
    }

    /* access modifiers changed from: protected */
    @NonNull
    public byte[] getBuf() {
        return this.buf;
    }

    /* access modifiers changed from: protected */
    public int getBaseOffset() {
        return this.baseOffset;
    }
}
