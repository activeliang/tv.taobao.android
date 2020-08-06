package com.taobao.ju.track.csv;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import org.apache.commons.codec.CharEncoding;

public class CsvWriter {
    public static final int ESCAPE_MODE_BACKSLASH = 2;
    public static final int ESCAPE_MODE_DOUBLED = 1;
    private Charset charset;
    private boolean closed;
    private String fileName;
    private boolean firstColumn;
    private boolean initialized;
    private Writer outputStream;
    private String systemRecordDelimiter;
    private boolean useCustomRecordDelimiter;
    private UserSettings userSettings;

    public CsvWriter(String fileName2, char delimiter, Charset charset2) {
        this.outputStream = null;
        this.fileName = null;
        this.firstColumn = true;
        this.useCustomRecordDelimiter = false;
        this.charset = null;
        this.userSettings = new UserSettings();
        this.initialized = false;
        this.closed = false;
        this.systemRecordDelimiter = System.getProperty("line.separator");
        if (fileName2 == null) {
            throw new IllegalArgumentException("Parameter fileName can not be null.");
        } else if (charset2 == null) {
            throw new IllegalArgumentException("Parameter charset can not be null.");
        } else {
            this.fileName = fileName2;
            this.userSettings.Delimiter = delimiter;
            this.charset = charset2;
        }
    }

    public CsvWriter(String fileName2) {
        this(fileName2, ',', Charset.forName(CharEncoding.ISO_8859_1));
    }

    public CsvWriter(Writer outputStream2, char delimiter) {
        this.outputStream = null;
        this.fileName = null;
        this.firstColumn = true;
        this.useCustomRecordDelimiter = false;
        this.charset = null;
        this.userSettings = new UserSettings();
        this.initialized = false;
        this.closed = false;
        this.systemRecordDelimiter = System.getProperty("line.separator");
        if (outputStream2 == null) {
            throw new IllegalArgumentException("Parameter outputStream can not be null.");
        }
        this.outputStream = outputStream2;
        this.userSettings.Delimiter = delimiter;
        this.initialized = true;
    }

    public CsvWriter(OutputStream outputStream2, char delimiter, Charset charset2) {
        this(new OutputStreamWriter(outputStream2, charset2), delimiter);
    }

    public char getDelimiter() {
        return this.userSettings.Delimiter;
    }

    public void setDelimiter(char delimiter) {
        this.userSettings.Delimiter = delimiter;
    }

    public char getRecordDelimiter() {
        return this.userSettings.RecordDelimiter;
    }

    public void setRecordDelimiter(char recordDelimiter) {
        this.useCustomRecordDelimiter = true;
        this.userSettings.RecordDelimiter = recordDelimiter;
    }

    public char getTextQualifier() {
        return this.userSettings.TextQualifier;
    }

    public void setTextQualifier(char textQualifier) {
        this.userSettings.TextQualifier = textQualifier;
    }

    public boolean getUseTextQualifier() {
        return this.userSettings.UseTextQualifier;
    }

    public void setUseTextQualifier(boolean useTextQualifier) {
        this.userSettings.UseTextQualifier = useTextQualifier;
    }

    public int getEscapeMode() {
        return this.userSettings.EscapeMode;
    }

    public void setEscapeMode(int escapeMode) {
        this.userSettings.EscapeMode = escapeMode;
    }

    public void setComment(char comment) {
        this.userSettings.Comment = comment;
    }

    public char getComment() {
        return this.userSettings.Comment;
    }

    public boolean getForceQualifier() {
        return this.userSettings.ForceQualifier;
    }

    public void setForceQualifier(boolean forceQualifier) {
        this.userSettings.ForceQualifier = forceQualifier;
    }

    public void write(String content, boolean preserveSpaces) throws IOException {
        char lastLetter;
        checkClosed();
        checkInit();
        if (content == null) {
            content = "";
        }
        if (!this.firstColumn) {
            this.outputStream.write(this.userSettings.Delimiter);
        }
        boolean textQualify = this.userSettings.ForceQualifier;
        if (!preserveSpaces && content.length() > 0) {
            content = content.trim();
        }
        if (!textQualify && this.userSettings.UseTextQualifier && (content.indexOf(this.userSettings.TextQualifier) > -1 || content.indexOf(this.userSettings.Delimiter) > -1 || ((!this.useCustomRecordDelimiter && (content.indexOf(10) > -1 || content.indexOf(13) > -1)) || ((this.useCustomRecordDelimiter && content.indexOf(this.userSettings.RecordDelimiter) > -1) || ((this.firstColumn && content.length() > 0 && content.charAt(0) == this.userSettings.Comment) || (this.firstColumn && content.length() == 0)))))) {
            textQualify = true;
        }
        if (this.userSettings.UseTextQualifier && !textQualify && content.length() > 0 && preserveSpaces) {
            char firstLetter = content.charAt(0);
            if (firstLetter == ' ' || firstLetter == 9) {
                textQualify = true;
            }
            if (!textQualify && content.length() > 1 && ((lastLetter = content.charAt(content.length() - 1)) == ' ' || lastLetter == 9)) {
                textQualify = true;
            }
        }
        if (textQualify) {
            this.outputStream.write(this.userSettings.TextQualifier);
            if (this.userSettings.EscapeMode == 2) {
                content = replace(replace(content, "\\", "\\\\"), "" + this.userSettings.TextQualifier, "\\" + this.userSettings.TextQualifier);
            } else {
                content = replace(content, "" + this.userSettings.TextQualifier, "" + this.userSettings.TextQualifier + this.userSettings.TextQualifier);
            }
        } else if (this.userSettings.EscapeMode == 2) {
            String content2 = replace(replace(content, "\\", "\\\\"), "" + this.userSettings.Delimiter, "\\" + this.userSettings.Delimiter);
            if (this.useCustomRecordDelimiter) {
                content = replace(content2, "" + this.userSettings.RecordDelimiter, "\\" + this.userSettings.RecordDelimiter);
            } else {
                content = replace(replace(content2, "\r", "\\\r"), "\n", "\\\n");
            }
            if (this.firstColumn && content.length() > 0 && content.charAt(0) == this.userSettings.Comment) {
                if (content.length() > 1) {
                    content = "\\" + this.userSettings.Comment + content.substring(1);
                } else {
                    content = "\\" + this.userSettings.Comment;
                }
            }
        }
        this.outputStream.write(content);
        if (textQualify) {
            this.outputStream.write(this.userSettings.TextQualifier);
        }
        this.firstColumn = false;
    }

    public void write(String content) throws IOException {
        write(content, false);
    }

    public void writeComment(String commentText) throws IOException {
        checkClosed();
        checkInit();
        this.outputStream.write(this.userSettings.Comment);
        this.outputStream.write(commentText);
        if (this.useCustomRecordDelimiter) {
            this.outputStream.write(this.userSettings.RecordDelimiter);
        } else {
            this.outputStream.write(this.systemRecordDelimiter);
        }
        this.firstColumn = true;
    }

    public void writeRecord(String[] values, boolean preserveSpaces) throws IOException {
        if (values != null && values.length > 0) {
            for (String write : values) {
                write(write, preserveSpaces);
            }
            endRecord();
        }
    }

    public void writeRecord(String[] values) throws IOException {
        writeRecord(values, false);
    }

    public void endRecord() throws IOException {
        checkClosed();
        checkInit();
        if (this.useCustomRecordDelimiter) {
            this.outputStream.write(this.userSettings.RecordDelimiter);
        } else {
            this.outputStream.write(this.systemRecordDelimiter);
        }
        this.firstColumn = true;
    }

    private void checkInit() throws IOException {
        if (!this.initialized) {
            if (this.fileName != null) {
                this.outputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.fileName), this.charset));
            }
            this.initialized = true;
        }
    }

    public void flush() throws IOException {
        this.outputStream.flush();
    }

    public void close() {
        if (!this.closed) {
            close(true);
            this.closed = true;
        }
    }

    private void close(boolean closing) {
        if (!this.closed) {
            if (closing) {
                this.charset = null;
            }
            try {
                if (this.initialized) {
                    this.outputStream.close();
                }
            } catch (Exception e) {
            }
            this.outputStream = null;
            this.closed = true;
        }
    }

    private void checkClosed() throws IOException {
        if (this.closed) {
            throw new IOException("This instance of the CsvWriter class has already been closed.");
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        close(false);
    }

    private class Letters {
        public static final char BACKSLASH = '\\';
        public static final char COMMA = ',';
        public static final char CR = '\r';
        public static final char LF = '\n';
        public static final char NULL = '\u0000';
        public static final char POUND = '#';
        public static final char QUOTE = '\"';
        public static final char SPACE = ' ';
        public static final char TAB = '\t';

        private Letters() {
        }
    }

    private class UserSettings {
        public char Comment = '#';
        public char Delimiter = ',';
        public int EscapeMode = 1;
        public boolean ForceQualifier = false;
        public char RecordDelimiter = 0;
        public char TextQualifier = '\"';
        public boolean UseTextQualifier = true;

        public UserSettings() {
        }
    }

    public static String replace(String original, String pattern, String replace) {
        int len = pattern.length();
        int found = original.indexOf(pattern);
        if (found <= -1) {
            return original;
        }
        StringBuffer sb = new StringBuffer();
        int start = 0;
        while (found != -1) {
            sb.append(original.substring(start, found));
            sb.append(replace);
            start = found + len;
            found = original.indexOf(pattern, start);
        }
        sb.append(original.substring(start));
        return sb.toString();
    }
}
