package com.taobao.atlas.dexmerge.dx.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public final class IndentingWriter extends FilterWriter {
    private boolean collectingIndent;
    private int column;
    private int indent;
    private final int maxIndent;
    private final String prefix;
    private final int width;

    public IndentingWriter(Writer out, int width2, String prefix2) {
        super(out);
        if (out == null) {
            throw new NullPointerException("out == null");
        } else if (width2 < 0) {
            throw new IllegalArgumentException("width < 0");
        } else if (prefix2 == null) {
            throw new NullPointerException("prefix == null");
        } else {
            this.width = width2 != 0 ? width2 : Integer.MAX_VALUE;
            this.maxIndent = width2 >> 1;
            this.prefix = prefix2.length() == 0 ? null : prefix2;
            bol();
        }
    }

    public IndentingWriter(Writer out, int width2) {
        this(out, width2, "");
    }

    public void write(int c) throws IOException {
        synchronized (this.lock) {
            if (this.collectingIndent) {
                if (c == 32) {
                    this.indent++;
                    if (this.indent >= this.maxIndent) {
                        this.indent = this.maxIndent;
                        this.collectingIndent = false;
                    }
                } else {
                    this.collectingIndent = false;
                }
            }
            if (this.column == this.width && c != 10) {
                this.out.write(10);
                this.column = 0;
            }
            if (this.column == 0) {
                if (this.prefix != null) {
                    this.out.write(this.prefix);
                }
                if (!this.collectingIndent) {
                    for (int i = 0; i < this.indent; i++) {
                        this.out.write(32);
                    }
                    this.column = this.indent;
                }
            }
            this.out.write(c);
            if (c == 10) {
                bol();
            } else {
                this.column++;
            }
        }
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        synchronized (this.lock) {
            while (len > 0) {
                write(cbuf[off]);
                off++;
                len--;
            }
        }
    }

    public void write(String str, int off, int len) throws IOException {
        synchronized (this.lock) {
            while (len > 0) {
                write(str.charAt(off));
                off++;
                len--;
            }
        }
    }

    private void bol() {
        boolean z;
        this.column = 0;
        if (this.maxIndent != 0) {
            z = true;
        } else {
            z = false;
        }
        this.collectingIndent = z;
        this.indent = 0;
    }
}
