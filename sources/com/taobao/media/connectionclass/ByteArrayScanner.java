package com.taobao.media.connectionclass;

import java.util.NoSuchElementException;

class ByteArrayScanner {
    private int mCurrentOffset;
    private byte[] mData;
    private char mDelimiter;
    private boolean mDelimiterSet;
    private int mTotalLength;

    ByteArrayScanner() {
    }

    public ByteArrayScanner reset(byte[] buffer, int length) {
        this.mData = buffer;
        this.mCurrentOffset = 0;
        this.mTotalLength = length;
        this.mDelimiterSet = false;
        return this;
    }

    public ByteArrayScanner useDelimiter(char delimiter) {
        throwIfNotReset();
        this.mDelimiter = delimiter;
        this.mDelimiterSet = true;
        return this;
    }

    private void throwIfNotReset() {
        if (this.mData == null) {
            throw new IllegalStateException("Must call reset first");
        }
    }

    private void throwIfDelimiterNotSet() {
        if (!this.mDelimiterSet) {
            throw new IllegalStateException("Must call useDelimiter first");
        }
    }

    public String nextString() throws NoSuchElementException {
        throwIfNotReset();
        throwIfDelimiterNotSet();
        return new String(this.mData, this.mCurrentOffset, advance());
    }

    public boolean nextStringEquals(String str) throws NoSuchElementException {
        int offset = this.mCurrentOffset;
        if (str.length() != advance()) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != this.mData[offset]) {
                return false;
            }
            offset++;
        }
        return true;
    }

    public int nextInt() throws NoSuchElementException {
        throwIfNotReset();
        throwIfDelimiterNotSet();
        int offset = this.mCurrentOffset;
        return parseInt(this.mData, offset, offset + advance());
    }

    public void skip() throws NoSuchElementException {
        throwIfNotReset();
        throwIfDelimiterNotSet();
        advance();
    }

    private int advance() throws NoSuchElementException {
        throwIfNotReset();
        throwIfDelimiterNotSet();
        if (this.mTotalLength <= this.mCurrentOffset) {
            throw new NoSuchElementException("Reading past end of input stream at " + this.mCurrentOffset + ".");
        }
        int index = indexOf(this.mData, this.mCurrentOffset, this.mTotalLength, this.mDelimiter);
        if (index == -1) {
            int length = this.mTotalLength - this.mCurrentOffset;
            this.mCurrentOffset = this.mTotalLength;
            return length;
        }
        int length2 = index - this.mCurrentOffset;
        this.mCurrentOffset = index + 1;
        return length2;
    }

    private static int parseInt(byte[] buffer, int start, int end) throws NumberFormatException {
        int result = 0;
        int start2 = start;
        while (start2 < end) {
            int start3 = start2 + 1;
            int digit = buffer[start2] - 48;
            if (digit < 0 || digit > 9) {
                throw new NumberFormatException("Invalid int in buffer at " + (start3 - 1) + ".");
            }
            result = (result * 10) + digit;
            start2 = start3;
        }
        return result;
    }

    private static int indexOf(byte[] data, int start, int end, char ch) {
        for (int i = start; i < end; i++) {
            if (data[i] == ch) {
                return i;
            }
        }
        return -1;
    }
}
