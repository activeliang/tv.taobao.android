package com.taobao.atlas.dexmerge.dx.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

public final class TwoColumnOutput {
    private final StringBuffer leftBuf;
    private final IndentingWriter leftColumn;
    private final int leftWidth;
    private final Writer out;
    private final StringBuffer rightBuf;
    private final IndentingWriter rightColumn;

    public static String toString(String s1, int width1, String spacer, String s2, int width2) {
        StringWriter sw = new StringWriter((s1.length() + s2.length()) * 3);
        TwoColumnOutput twoOut = new TwoColumnOutput((Writer) sw, width1, width2, spacer);
        try {
            twoOut.getLeft().write(s1);
            twoOut.getRight().write(s2);
            twoOut.flush();
            return sw.toString();
        } catch (IOException ex) {
            throw new RuntimeException("shouldn't happen", ex);
        }
    }

    public TwoColumnOutput(Writer out2, int leftWidth2, int rightWidth, String spacer) {
        if (out2 == null) {
            throw new NullPointerException("out == null");
        } else if (leftWidth2 < 1) {
            throw new IllegalArgumentException("leftWidth < 1");
        } else if (rightWidth < 1) {
            throw new IllegalArgumentException("rightWidth < 1");
        } else if (spacer == null) {
            throw new NullPointerException("spacer == null");
        } else {
            StringWriter leftWriter = new StringWriter(1000);
            StringWriter rightWriter = new StringWriter(1000);
            this.out = out2;
            this.leftWidth = leftWidth2;
            this.leftBuf = leftWriter.getBuffer();
            this.rightBuf = rightWriter.getBuffer();
            this.leftColumn = new IndentingWriter(leftWriter, leftWidth2);
            this.rightColumn = new IndentingWriter(rightWriter, rightWidth, spacer);
        }
    }

    public TwoColumnOutput(OutputStream out2, int leftWidth2, int rightWidth, String spacer) {
        this((Writer) new OutputStreamWriter(out2), leftWidth2, rightWidth, spacer);
    }

    public Writer getLeft() {
        return this.leftColumn;
    }

    public Writer getRight() {
        return this.rightColumn;
    }

    public void flush() {
        try {
            appendNewlineIfNecessary(this.leftBuf, this.leftColumn);
            appendNewlineIfNecessary(this.rightBuf, this.rightColumn);
            outputFullLines();
            flushLeft();
            flushRight();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void outputFullLines() throws IOException {
        int rightLen;
        while (true) {
            int leftLen = this.leftBuf.indexOf("\n");
            if (leftLen >= 0 && (rightLen = this.rightBuf.indexOf("\n")) >= 0) {
                if (leftLen != 0) {
                    this.out.write(this.leftBuf.substring(0, leftLen));
                }
                if (rightLen != 0) {
                    writeSpaces(this.out, this.leftWidth - leftLen);
                    this.out.write(this.rightBuf.substring(0, rightLen));
                }
                this.out.write(10);
                this.leftBuf.delete(0, leftLen + 1);
                this.rightBuf.delete(0, rightLen + 1);
            } else {
                return;
            }
        }
    }

    private void flushLeft() throws IOException {
        appendNewlineIfNecessary(this.leftBuf, this.leftColumn);
        while (this.leftBuf.length() != 0) {
            this.rightColumn.write(10);
            outputFullLines();
        }
    }

    private void flushRight() throws IOException {
        appendNewlineIfNecessary(this.rightBuf, this.rightColumn);
        while (this.rightBuf.length() != 0) {
            this.leftColumn.write(10);
            outputFullLines();
        }
    }

    private static void appendNewlineIfNecessary(StringBuffer buf, Writer out2) throws IOException {
        int len = buf.length();
        if (len != 0 && buf.charAt(len - 1) != 10) {
            out2.write(10);
        }
    }

    private static void writeSpaces(Writer out2, int amt) throws IOException {
        while (amt > 0) {
            out2.write(32);
            amt--;
        }
    }
}
