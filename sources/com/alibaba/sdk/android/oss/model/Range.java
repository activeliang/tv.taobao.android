package com.alibaba.sdk.android.oss.model;

public class Range {
    public static final long INFINITE = -1;
    private long begin;
    private long end;

    public Range(long begin2, long end2) {
        setBegin(begin2);
        setEnd(end2);
    }

    public long getEnd() {
        return this.end;
    }

    public void setEnd(long end2) {
        this.end = end2;
    }

    public long getBegin() {
        return this.begin;
    }

    public void setBegin(long begin2) {
        this.begin = begin2;
    }

    public boolean checkIsValid() {
        if (this.begin < -1 || this.end < -1) {
            return false;
        }
        if (this.begin < 0 || this.end < 0 || this.begin <= this.end) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "bytes=" + (this.begin == -1 ? "" : String.valueOf(this.begin)) + "-" + (this.end == -1 ? "" : String.valueOf(this.end));
    }
}
