package com.taobao.atlas.dexmerge.dx.util;

import com.taobao.atlas.dex.Leb128;
import com.taobao.atlas.dex.util.ByteOutput;
import com.taobao.atlas.dex.util.ExceptionWithContext;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public final class ByteArrayAnnotatedOutput implements AnnotatedOutput, ByteOutput {
    private static final int DEFAULT_SIZE = 1000;
    private int annotationWidth;
    private ArrayList<Annotation> annotations;
    private int cursor;
    private byte[] data;
    private int hexCols;
    private final boolean stretchy;
    private boolean verbose;

    public ByteArrayAnnotatedOutput(byte[] data2) {
        this(data2, false);
    }

    public ByteArrayAnnotatedOutput() {
        this(1000);
    }

    public ByteArrayAnnotatedOutput(int size) {
        this(new byte[size], true);
    }

    private ByteArrayAnnotatedOutput(byte[] data2, boolean stretchy2) {
        if (data2 == null) {
            throw new NullPointerException("data == null");
        }
        this.stretchy = stretchy2;
        this.data = data2;
        this.cursor = 0;
        this.verbose = false;
        this.annotations = null;
        this.annotationWidth = 0;
        this.hexCols = 0;
    }

    public byte[] getArray() {
        return this.data;
    }

    public byte[] toByteArray() {
        byte[] result = new byte[this.cursor];
        System.arraycopy(this.data, 0, result, 0, this.cursor);
        return result;
    }

    public int getCursor() {
        return this.cursor;
    }

    public void assertCursor(int expectedCursor) {
        if (this.cursor != expectedCursor) {
            throw new ExceptionWithContext("expected cursor " + expectedCursor + "; actual value: " + this.cursor);
        }
    }

    public void writeByte(int value) {
        int writeAt = this.cursor;
        int end = writeAt + 1;
        if (this.stretchy) {
            ensureCapacity(end);
        } else if (end > this.data.length) {
            throwBounds();
            return;
        }
        this.data[writeAt] = (byte) value;
        this.cursor = end;
    }

    public void writeShort(int value) {
        int writeAt = this.cursor;
        int end = writeAt + 2;
        if (this.stretchy) {
            ensureCapacity(end);
        } else if (end > this.data.length) {
            throwBounds();
            return;
        }
        this.data[writeAt] = (byte) value;
        this.data[writeAt + 1] = (byte) (value >> 8);
        this.cursor = end;
    }

    public void writeInt(int value) {
        int writeAt = this.cursor;
        int end = writeAt + 4;
        if (this.stretchy) {
            ensureCapacity(end);
        } else if (end > this.data.length) {
            throwBounds();
            return;
        }
        this.data[writeAt] = (byte) value;
        this.data[writeAt + 1] = (byte) (value >> 8);
        this.data[writeAt + 2] = (byte) (value >> 16);
        this.data[writeAt + 3] = (byte) (value >> 24);
        this.cursor = end;
    }

    public void writeLong(long value) {
        int writeAt = this.cursor;
        int end = writeAt + 8;
        if (this.stretchy) {
            ensureCapacity(end);
        } else if (end > this.data.length) {
            throwBounds();
            return;
        }
        int half = (int) value;
        this.data[writeAt] = (byte) half;
        this.data[writeAt + 1] = (byte) (half >> 8);
        this.data[writeAt + 2] = (byte) (half >> 16);
        this.data[writeAt + 3] = (byte) (half >> 24);
        int half2 = (int) (value >> 32);
        this.data[writeAt + 4] = (byte) half2;
        this.data[writeAt + 5] = (byte) (half2 >> 8);
        this.data[writeAt + 6] = (byte) (half2 >> 16);
        this.data[writeAt + 7] = (byte) (half2 >> 24);
        this.cursor = end;
    }

    public int writeUleb128(int value) {
        if (this.stretchy) {
            ensureCapacity(this.cursor + 5);
        }
        int cursorBefore = this.cursor;
        Leb128.writeUnsignedLeb128(this, value);
        return this.cursor - cursorBefore;
    }

    public int writeSleb128(int value) {
        if (this.stretchy) {
            ensureCapacity(this.cursor + 5);
        }
        int cursorBefore = this.cursor;
        Leb128.writeSignedLeb128(this, value);
        return this.cursor - cursorBefore;
    }

    public void write(ByteArray bytes) {
        int blen = bytes.size();
        int writeAt = this.cursor;
        int end = writeAt + blen;
        if (this.stretchy) {
            ensureCapacity(end);
        } else if (end > this.data.length) {
            throwBounds();
            return;
        }
        bytes.getBytes(this.data, writeAt);
        this.cursor = end;
    }

    public void write(byte[] bytes, int offset, int length) {
        int writeAt = this.cursor;
        int end = writeAt + length;
        int bytesEnd = offset + length;
        if ((offset | length | end) < 0 || bytesEnd > bytes.length) {
            throw new IndexOutOfBoundsException("bytes.length " + bytes.length + "; " + offset + "..!" + end);
        }
        if (this.stretchy) {
            ensureCapacity(end);
        } else if (end > this.data.length) {
            throwBounds();
            return;
        }
        System.arraycopy(bytes, offset, this.data, writeAt, length);
        this.cursor = end;
    }

    public void write(byte[] bytes) {
        write(bytes, 0, bytes.length);
    }

    public void writeZeroes(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count < 0");
        }
        int end = this.cursor + count;
        if (this.stretchy) {
            ensureCapacity(end);
        } else if (end > this.data.length) {
            throwBounds();
            return;
        }
        this.cursor = end;
    }

    public void alignTo(int alignment) {
        int mask = alignment - 1;
        if (alignment < 0 || (mask & alignment) != 0) {
            throw new IllegalArgumentException("bogus alignment");
        }
        int end = (this.cursor + mask) & (mask ^ -1);
        if (this.stretchy) {
            ensureCapacity(end);
        } else if (end > this.data.length) {
            throwBounds();
            return;
        }
        this.cursor = end;
    }

    public boolean annotates() {
        return this.annotations != null;
    }

    public boolean isVerbose() {
        return this.verbose;
    }

    public void annotate(String msg) {
        if (this.annotations != null) {
            endAnnotation();
            this.annotations.add(new Annotation(this.cursor, msg));
        }
    }

    public void annotate(int amt, String msg) {
        int startAt;
        if (this.annotations != null) {
            endAnnotation();
            int asz = this.annotations.size();
            int lastEnd = asz == 0 ? 0 : this.annotations.get(asz - 1).getEnd();
            if (lastEnd <= this.cursor) {
                startAt = this.cursor;
            } else {
                startAt = lastEnd;
            }
            this.annotations.add(new Annotation(startAt, startAt + amt, msg));
        }
    }

    public void endAnnotation() {
        int sz;
        if (this.annotations != null && (sz = this.annotations.size()) != 0) {
            this.annotations.get(sz - 1).setEndIfUnset(this.cursor);
        }
    }

    public int getAnnotationWidth() {
        return this.annotationWidth - (((this.hexCols * 2) + 8) + (this.hexCols / 2));
    }

    public void enableAnnotations(int annotationWidth2, boolean verbose2) {
        if (this.annotations != null || this.cursor != 0) {
            throw new RuntimeException("cannot enable annotations");
        } else if (annotationWidth2 < 40) {
            throw new IllegalArgumentException("annotationWidth < 40");
        } else {
            int hexCols2 = (((annotationWidth2 - 7) / 15) + 1) & -2;
            if (hexCols2 < 6) {
                hexCols2 = 6;
            } else if (hexCols2 > 10) {
                hexCols2 = 10;
            }
            this.annotations = new ArrayList<>(1000);
            this.annotationWidth = annotationWidth2;
            this.hexCols = hexCols2;
            this.verbose = verbose2;
        }
    }

    public void finishAnnotating() {
        endAnnotation();
        if (this.annotations != null) {
            int asz = this.annotations.size();
            while (asz > 0) {
                Annotation last = this.annotations.get(asz - 1);
                if (last.getStart() > this.cursor) {
                    this.annotations.remove(asz - 1);
                    asz--;
                } else if (last.getEnd() > this.cursor) {
                    last.setEnd(this.cursor);
                    return;
                } else {
                    return;
                }
            }
        }
    }

    public void writeAnnotationsTo(Writer out) throws IOException {
        int end;
        String text;
        int width2 = getAnnotationWidth();
        TwoColumnOutput twoColumnOutput = new TwoColumnOutput(out, (this.annotationWidth - width2) - 1, width2, "|");
        Writer left = twoColumnOutput.getLeft();
        Writer right = twoColumnOutput.getRight();
        int leftAt = 0;
        int rightAt = 0;
        int rightSz = this.annotations.size();
        while (leftAt < this.cursor && rightAt < rightSz) {
            Annotation a = this.annotations.get(rightAt);
            int start = a.getStart();
            if (leftAt < start) {
                end = start;
                start = leftAt;
                text = "";
            } else {
                end = a.getEnd();
                text = a.getText();
                rightAt++;
            }
            left.write(Hex.dump(this.data, start, end - start, start, this.hexCols, 6));
            right.write(text);
            twoColumnOutput.flush();
            leftAt = end;
        }
        if (leftAt < this.cursor) {
            left.write(Hex.dump(this.data, leftAt, this.cursor - leftAt, leftAt, this.hexCols, 6));
        }
        while (rightAt < rightSz) {
            right.write(this.annotations.get(rightAt).getText());
            rightAt++;
        }
        twoColumnOutput.flush();
    }

    private static void throwBounds() {
        throw new IndexOutOfBoundsException("attempt to write past the end");
    }

    private void ensureCapacity(int desiredSize) {
        if (this.data.length < desiredSize) {
            byte[] newData = new byte[((desiredSize * 2) + 1000)];
            System.arraycopy(this.data, 0, newData, 0, this.cursor);
            this.data = newData;
        }
    }

    private static class Annotation {
        private int end;
        private final int start;
        private final String text;

        public Annotation(int start2, int end2, String text2) {
            this.start = start2;
            this.end = end2;
            this.text = text2;
        }

        public Annotation(int start2, String text2) {
            this(start2, Integer.MAX_VALUE, text2);
        }

        public void setEndIfUnset(int end2) {
            if (this.end == Integer.MAX_VALUE) {
                this.end = end2;
            }
        }

        public void setEnd(int end2) {
            this.end = end2;
        }

        public int getStart() {
            return this.start;
        }

        public int getEnd() {
            return this.end;
        }

        public String getText() {
            return this.text;
        }
    }
}
