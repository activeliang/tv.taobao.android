package com.tvtaobao.android.values;

public class Flag {
    private int[] flags = {0, 0, 0, 0};

    public synchronized void setFlag(int groudIndex, int flagVal) {
        int[] iArr = this.flags;
        int length = groudIndex % this.flags.length;
        iArr[length] = iArr[length] | flagVal;
    }

    public synchronized void clrFlag(int groudIndex, int flagVal) {
        int[] iArr = this.flags;
        int length = groudIndex % this.flags.length;
        iArr[length] = iArr[length] & (flagVal ^ -1);
    }

    public synchronized boolean hasFlag(int groudIndex, int flagVal) {
        boolean z;
        if ((this.flags[groudIndex % this.flags.length] & flagVal) == flagVal) {
            z = true;
        } else {
            z = false;
        }
        return z;
    }

    public synchronized void clrFlags() {
        for (int i = 0; i < this.flags.length; i++) {
            int[] iArr = this.flags;
            iArr[i] = iArr[i] & 0;
        }
    }

    public void setFlag(int flagVal) {
        setFlag(0, flagVal);
    }

    public void clrFlag(int flagVal) {
        clrFlag(0, flagVal);
    }

    public boolean hasFlag(int flagVal) {
        return hasFlag(0, flagVal);
    }
}
