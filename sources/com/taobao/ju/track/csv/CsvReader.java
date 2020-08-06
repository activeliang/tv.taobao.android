package com.taobao.ju.track.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.HashMap;
import org.apache.commons.codec.CharEncoding;

public class CsvReader {
    public static final int ESCAPE_MODE_BACKSLASH = 2;
    public static final int ESCAPE_MODE_DOUBLED = 1;
    private Charset charset;
    private boolean closed;
    private ColumnBuffer columnBuffer;
    private int columnsCount;
    private long currentRecord;
    private DataBuffer dataBuffer;
    private String fileName;
    private boolean hasMoreData;
    private boolean hasReadNextLine;
    private HeadersHolder headersHolder;
    private boolean initialized;
    private Reader inputStream;
    private boolean[] isQualified;
    private char lastLetter;
    private RawRecordBuffer rawBuffer;
    private String rawRecord;
    private boolean startedColumn;
    private boolean startedWithQualifier;
    private boolean useCustomRecordDelimiter;
    private UserSettings userSettings;
    private String[] values;

    public CsvReader(String fileName2, char delimiter, Charset charset2) throws FileNotFoundException {
        this.inputStream = null;
        this.fileName = null;
        this.userSettings = new UserSettings();
        this.charset = null;
        this.useCustomRecordDelimiter = false;
        this.dataBuffer = new DataBuffer();
        this.columnBuffer = new ColumnBuffer();
        this.rawBuffer = new RawRecordBuffer();
        this.isQualified = null;
        this.rawRecord = "";
        this.headersHolder = new HeadersHolder();
        this.startedColumn = false;
        this.startedWithQualifier = false;
        this.hasMoreData = true;
        this.lastLetter = 0;
        this.hasReadNextLine = false;
        this.columnsCount = 0;
        this.currentRecord = 0;
        this.values = new String[10];
        this.initialized = false;
        this.closed = false;
        if (fileName2 == null) {
            throw new IllegalArgumentException("Parameter fileName can not be null.");
        } else if (charset2 == null) {
            throw new IllegalArgumentException("Parameter charset can not be null.");
        } else if (!new File(fileName2).exists()) {
            throw new FileNotFoundException("File " + fileName2 + " does not exist.");
        } else {
            this.fileName = fileName2;
            this.userSettings.Delimiter = delimiter;
            this.charset = charset2;
            this.isQualified = new boolean[this.values.length];
        }
    }

    public CsvReader(String fileName2, char delimiter) throws FileNotFoundException {
        this(fileName2, delimiter, Charset.forName(CharEncoding.ISO_8859_1));
    }

    public CsvReader(String fileName2) throws FileNotFoundException {
        this(fileName2, ',');
    }

    public CsvReader(Reader inputStream2, char delimiter) {
        this.inputStream = null;
        this.fileName = null;
        this.userSettings = new UserSettings();
        this.charset = null;
        this.useCustomRecordDelimiter = false;
        this.dataBuffer = new DataBuffer();
        this.columnBuffer = new ColumnBuffer();
        this.rawBuffer = new RawRecordBuffer();
        this.isQualified = null;
        this.rawRecord = "";
        this.headersHolder = new HeadersHolder();
        this.startedColumn = false;
        this.startedWithQualifier = false;
        this.hasMoreData = true;
        this.lastLetter = 0;
        this.hasReadNextLine = false;
        this.columnsCount = 0;
        this.currentRecord = 0;
        this.values = new String[10];
        this.initialized = false;
        this.closed = false;
        if (inputStream2 == null) {
            throw new IllegalArgumentException("Parameter inputStream can not be null.");
        }
        this.inputStream = inputStream2;
        this.userSettings.Delimiter = delimiter;
        this.initialized = true;
        this.isQualified = new boolean[this.values.length];
    }

    public CsvReader(Reader inputStream2) {
        this(inputStream2, ',');
    }

    public CsvReader(InputStream inputStream2, char delimiter, Charset charset2) {
        this((Reader) new InputStreamReader(inputStream2, charset2), delimiter);
    }

    public CsvReader(InputStream inputStream2, Charset charset2) {
        this((Reader) new InputStreamReader(inputStream2, charset2));
    }

    public boolean getCaptureRawRecord() {
        return this.userSettings.CaptureRawRecord;
    }

    public void setCaptureRawRecord(boolean captureRawRecord) {
        this.userSettings.CaptureRawRecord = captureRawRecord;
    }

    public String getRawRecord() {
        return this.rawRecord;
    }

    public boolean getTrimWhitespace() {
        return this.userSettings.TrimWhitespace;
    }

    public void setTrimWhitespace(boolean trimWhitespace) {
        this.userSettings.TrimWhitespace = trimWhitespace;
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

    public char getComment() {
        return this.userSettings.Comment;
    }

    public void setComment(char comment) {
        this.userSettings.Comment = comment;
    }

    public boolean getUseComments() {
        return this.userSettings.UseComments;
    }

    public void setUseComments(boolean useComments) {
        this.userSettings.UseComments = useComments;
    }

    public int getEscapeMode() {
        return this.userSettings.EscapeMode;
    }

    public void setEscapeMode(int escapeMode) throws IllegalArgumentException {
        if (escapeMode == 1 || escapeMode == 2) {
            this.userSettings.EscapeMode = escapeMode;
            return;
        }
        throw new IllegalArgumentException("Parameter escapeMode must be a valid value.");
    }

    public boolean getSkipEmptyRecords() {
        return this.userSettings.SkipEmptyRecords;
    }

    public void setSkipEmptyRecords(boolean skipEmptyRecords) {
        this.userSettings.SkipEmptyRecords = skipEmptyRecords;
    }

    public boolean getSafetySwitch() {
        return this.userSettings.SafetySwitch;
    }

    public void setSafetySwitch(boolean safetySwitch) {
        this.userSettings.SafetySwitch = safetySwitch;
    }

    public int getColumnCount() {
        return this.columnsCount;
    }

    public long getCurrentRecord() {
        return this.currentRecord - 1;
    }

    public int getHeaderCount() {
        return this.headersHolder.Length;
    }

    public String[] getHeaders() throws IOException {
        checkClosed();
        if (this.headersHolder.Headers == null) {
            return null;
        }
        String[] clone = new String[this.headersHolder.Length];
        System.arraycopy(this.headersHolder.Headers, 0, clone, 0, this.headersHolder.Length);
        return clone;
    }

    public void setHeaders(String[] headers) {
        this.headersHolder.Headers = headers;
        this.headersHolder.IndexByName.clear();
        if (headers != null) {
            this.headersHolder.Length = headers.length;
        } else {
            this.headersHolder.Length = 0;
        }
        for (int i = 0; i < this.headersHolder.Length; i++) {
            this.headersHolder.IndexByName.put(headers[i], Integer.valueOf(i));
        }
    }

    public String[] getValues() throws IOException {
        checkClosed();
        String[] clone = new String[this.columnsCount];
        System.arraycopy(this.values, 0, clone, 0, this.columnsCount);
        return clone;
    }

    public String get(int columnIndex) throws IOException {
        checkClosed();
        if (columnIndex <= -1 || columnIndex >= this.columnsCount) {
            return "";
        }
        return this.values[columnIndex];
    }

    public String get(String headerName) throws IOException {
        checkClosed();
        return get(getIndex(headerName));
    }

    public static CsvReader parse(String data) {
        if (data != null) {
            return new CsvReader((Reader) new StringReader(data));
        }
        throw new IllegalArgumentException("Parameter data can not be null.");
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:4:0x0034  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean readRecord() throws java.io.IOException {
        /*
            r19 = this;
            r19.checkClosed()
            r13 = 0
            r0 = r19
            r0.columnsCount = r13
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$RawRecordBuffer r13 = r0.rawBuffer
            r14 = 0
            r13.Position = r14
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            r13.LineStart = r14
            r13 = 0
            r0 = r19
            r0.hasReadNextLine = r13
            r0 = r19
            boolean r13 = r0.hasMoreData
            if (r13 == 0) goto L_0x005b
        L_0x0026:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            int r13 = r13.Position
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Count
            if (r13 != r14) goto L_0x00a7
            r19.checkDataLength()
        L_0x0037:
            r0 = r19
            boolean r13 = r0.hasMoreData
            if (r13 == 0) goto L_0x0043
            r0 = r19
            boolean r13 = r0.hasReadNextLine
            if (r13 == 0) goto L_0x0026
        L_0x0043:
            r0 = r19
            boolean r13 = r0.startedColumn
            if (r13 != 0) goto L_0x0055
            r0 = r19
            char r13 = r0.lastLetter
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r14 = r0.userSettings
            char r14 = r14.Delimiter
            if (r13 != r14) goto L_0x005b
        L_0x0055:
            r19.endColumn()
            r19.endRecord()
        L_0x005b:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            boolean r13 = r13.CaptureRawRecord
            if (r13 == 0) goto L_0x06b7
            r0 = r19
            boolean r13 = r0.hasMoreData
            if (r13 == 0) goto L_0x0699
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$RawRecordBuffer r13 = r0.rawBuffer
            int r13 = r13.Position
            if (r13 != 0) goto L_0x0636
            java.lang.String r13 = new java.lang.String
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            char[] r14 = r14.Buffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r15 = r0.dataBuffer
            int r15 = r15.LineStart
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r0 = r0.dataBuffer
            r16 = r0
            r0 = r16
            int r0 = r0.Position
            r16 = r0
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r0 = r0.dataBuffer
            r17 = r0
            r0 = r17
            int r0 = r0.LineStart
            r17 = r0
            int r16 = r16 - r17
            int r16 = r16 + -1
            r13.<init>(r14, r15, r16)
            r0 = r19
            r0.rawRecord = r13
        L_0x00a2:
            r0 = r19
            boolean r13 = r0.hasReadNextLine
            return r13
        L_0x00a7:
            r13 = 0
            r0 = r19
            r0.startedWithQualifier = r13
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            char[] r13 = r13.Buffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            char r2 = r13[r14]
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            boolean r13 = r13.UseTextQualifier
            if (r13 == 0) goto L_0x0358
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            char r13 = r13.TextQualifier
            if (r2 != r13) goto L_0x0358
            r0 = r19
            r0.lastLetter = r2
            r13 = 1
            r0 = r19
            r0.startedColumn = r13
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            int r14 = r14 + 1
            r13.ColumnStart = r14
            r13 = 1
            r0 = r19
            r0.startedWithQualifier = r13
            r11 = 0
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            char r5 = r13.TextQualifier
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            int r13 = r13.EscapeMode
            r14 = 2
            if (r13 != r14) goto L_0x00f8
            r5 = 92
        L_0x00f8:
            r3 = 0
            r10 = 0
            r12 = 0
            r4 = 1
            r6 = 0
            r7 = 0
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            int r14 = r13.Position
            int r14 = r14 + 1
            r13.Position = r14
        L_0x0108:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            int r13 = r13.Position
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Count
            if (r13 != r14) goto L_0x0137
            r19.checkDataLength()
        L_0x0119:
            r0 = r19
            boolean r13 = r0.hasMoreData
            if (r13 == 0) goto L_0x0125
            r0 = r19
            boolean r13 = r0.startedColumn
            if (r13 != 0) goto L_0x0108
        L_0x0125:
            r0 = r19
            boolean r13 = r0.hasMoreData
            if (r13 == 0) goto L_0x0037
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            int r14 = r13.Position
            int r14 = r14 + 1
            r13.Position = r14
            goto L_0x0037
        L_0x0137:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            char[] r13 = r13.Buffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            char r2 = r13[r14]
            if (r3 == 0) goto L_0x0217
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            int r14 = r14 + 1
            r13.ColumnStart = r14
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            char r13 = r13.Delimiter
            if (r2 != r13) goto L_0x01f3
            r19.endColumn()
        L_0x0160:
            r0 = r19
            r0.lastLetter = r2
            r0 = r19
            boolean r13 = r0.startedColumn
            if (r13 == 0) goto L_0x0119
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            int r14 = r13.Position
            int r14 = r14 + 1
            r13.Position = r14
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            boolean r13 = r13.SafetySwitch
            if (r13 == 0) goto L_0x0119
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            int r13 = r13.Position
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.ColumnStart
            int r13 = r13 - r14
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$ColumnBuffer r14 = r0.columnBuffer
            int r14 = r14.Position
            int r13 = r13 + r14
            r14 = 100000(0x186a0, float:1.4013E-40)
            if (r13 <= r14) goto L_0x0119
            r19.close()
            java.io.IOException r13 = new java.io.IOException
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = "Maximum column length of 100,000 exceeded in column "
            java.lang.StringBuilder r14 = r14.append(r15)
            java.text.NumberFormat r15 = java.text.NumberFormat.getIntegerInstance()
            r0 = r19
            int r0 = r0.columnsCount
            r16 = r0
            r0 = r16
            long r0 = (long) r0
            r16 = r0
            java.lang.String r15 = r15.format(r16)
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r15 = " in record "
            java.lang.StringBuilder r14 = r14.append(r15)
            java.text.NumberFormat r15 = java.text.NumberFormat.getIntegerInstance()
            r0 = r19
            long r0 = r0.currentRecord
            r16 = r0
            java.lang.String r15 = r15.format(r16)
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r15 = ". Set the SafetySwitch property to false"
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r15 = " if you're expecting column lengths greater than 100,000 characters to"
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r15 = " avoid this error."
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r14 = r14.toString()
            r13.<init>(r14)
            throw r13
        L_0x01f3:
            r0 = r19
            boolean r13 = r0.useCustomRecordDelimiter
            if (r13 != 0) goto L_0x0201
            r13 = 13
            if (r2 == r13) goto L_0x020f
            r13 = 10
            if (r2 == r13) goto L_0x020f
        L_0x0201:
            r0 = r19
            boolean r13 = r0.useCustomRecordDelimiter
            if (r13 == 0) goto L_0x0160
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            char r13 = r13.RecordDelimiter
            if (r2 != r13) goto L_0x0160
        L_0x020f:
            r19.endColumn()
            r19.endRecord()
            goto L_0x0160
        L_0x0217:
            if (r12 == 0) goto L_0x026d
            int r6 = r6 + 1
            switch(r4) {
                case 1: goto L_0x0227;
                case 2: goto L_0x0235;
                case 3: goto L_0x0242;
                case 4: goto L_0x024f;
                default: goto L_0x021e;
            }
        L_0x021e:
            if (r12 != 0) goto L_0x025d
            r0 = r19
            r0.appendLetter(r7)
            goto L_0x0160
        L_0x0227:
            int r13 = r7 * 16
            char r7 = (char) r13
            char r13 = hexToDec(r2)
            int r13 = r13 + r7
            char r7 = (char) r13
            r13 = 4
            if (r6 != r13) goto L_0x021e
            r12 = 0
            goto L_0x021e
        L_0x0235:
            int r13 = r7 * 8
            char r7 = (char) r13
            int r13 = r2 + -48
            char r13 = (char) r13
            int r13 = r13 + r7
            char r7 = (char) r13
            r13 = 3
            if (r6 != r13) goto L_0x021e
            r12 = 0
            goto L_0x021e
        L_0x0242:
            int r13 = r7 * 10
            char r7 = (char) r13
            int r13 = r2 + -48
            char r13 = (char) r13
            int r13 = r13 + r7
            char r7 = (char) r13
            r13 = 3
            if (r6 != r13) goto L_0x021e
            r12 = 0
            goto L_0x021e
        L_0x024f:
            int r13 = r7 * 16
            char r7 = (char) r13
            char r13 = hexToDec(r2)
            int r13 = r13 + r7
            char r7 = (char) r13
            r13 = 2
            if (r6 != r13) goto L_0x021e
            r12 = 0
            goto L_0x021e
        L_0x025d:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            int r14 = r14 + 1
            r13.ColumnStart = r14
            goto L_0x0160
        L_0x026d:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            char r13 = r13.TextQualifier
            if (r2 != r13) goto L_0x028b
            if (r10 == 0) goto L_0x027b
            r10 = 0
            r11 = 0
            goto L_0x0160
        L_0x027b:
            r19.updateCurrentValue()
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            int r13 = r13.EscapeMode
            r14 = 1
            if (r13 != r14) goto L_0x0288
            r10 = 1
        L_0x0288:
            r11 = 1
            goto L_0x0160
        L_0x028b:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            int r13 = r13.EscapeMode
            r14 = 2
            if (r13 != r14) goto L_0x030d
            if (r10 == 0) goto L_0x030d
            switch(r2) {
                case 48: goto L_0x02db;
                case 49: goto L_0x02db;
                case 50: goto L_0x02db;
                case 51: goto L_0x02db;
                case 52: goto L_0x02db;
                case 53: goto L_0x02db;
                case 54: goto L_0x02db;
                case 55: goto L_0x02db;
                case 68: goto L_0x02f0;
                case 79: goto L_0x02f0;
                case 85: goto L_0x02f0;
                case 88: goto L_0x02f0;
                case 97: goto L_0x02d4;
                case 98: goto L_0x02b4;
                case 100: goto L_0x02f0;
                case 101: goto L_0x02c4;
                case 102: goto L_0x02bc;
                case 110: goto L_0x029c;
                case 111: goto L_0x02f0;
                case 114: goto L_0x02a4;
                case 116: goto L_0x02ac;
                case 117: goto L_0x02f0;
                case 118: goto L_0x02cc;
                case 120: goto L_0x02f0;
                default: goto L_0x0299;
            }
        L_0x0299:
            r10 = 0
            goto L_0x0160
        L_0x029c:
            r13 = 10
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0299
        L_0x02a4:
            r13 = 13
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0299
        L_0x02ac:
            r13 = 9
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0299
        L_0x02b4:
            r13 = 8
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0299
        L_0x02bc:
            r13 = 12
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0299
        L_0x02c4:
            r13 = 27
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0299
        L_0x02cc:
            r13 = 11
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0299
        L_0x02d4:
            r13 = 7
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0299
        L_0x02db:
            r4 = 2
            r12 = 1
            r6 = 1
            int r13 = r2 + -48
            char r7 = (char) r13
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            int r14 = r14 + 1
            r13.ColumnStart = r14
            goto L_0x0299
        L_0x02f0:
            switch(r2) {
                case 68: goto L_0x030b;
                case 79: goto L_0x0309;
                case 85: goto L_0x0305;
                case 88: goto L_0x0307;
                case 100: goto L_0x030b;
                case 111: goto L_0x0309;
                case 117: goto L_0x0305;
                case 120: goto L_0x0307;
                default: goto L_0x02f3;
            }
        L_0x02f3:
            r12 = 1
            r6 = 0
            r7 = 0
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            int r14 = r14 + 1
            r13.ColumnStart = r14
            goto L_0x0299
        L_0x0305:
            r4 = 1
            goto L_0x02f3
        L_0x0307:
            r4 = 4
            goto L_0x02f3
        L_0x0309:
            r4 = 2
            goto L_0x02f3
        L_0x030b:
            r4 = 3
            goto L_0x02f3
        L_0x030d:
            if (r2 != r5) goto L_0x0315
            r19.updateCurrentValue()
            r10 = 1
            goto L_0x0160
        L_0x0315:
            if (r11 == 0) goto L_0x0160
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            char r13 = r13.Delimiter
            if (r2 != r13) goto L_0x0325
            r19.endColumn()
        L_0x0322:
            r11 = 0
            goto L_0x0160
        L_0x0325:
            r0 = r19
            boolean r13 = r0.useCustomRecordDelimiter
            if (r13 != 0) goto L_0x0333
            r13 = 13
            if (r2 == r13) goto L_0x0341
            r13 = 10
            if (r2 == r13) goto L_0x0341
        L_0x0333:
            r0 = r19
            boolean r13 = r0.useCustomRecordDelimiter
            if (r13 == 0) goto L_0x0348
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            char r13 = r13.RecordDelimiter
            if (r2 != r13) goto L_0x0348
        L_0x0341:
            r19.endColumn()
            r19.endRecord()
            goto L_0x0322
        L_0x0348:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            int r14 = r14 + 1
            r13.ColumnStart = r14
            r3 = 1
            goto L_0x0322
        L_0x0358:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            char r13 = r13.Delimiter
            if (r2 != r13) goto L_0x0369
            r0 = r19
            r0.lastLetter = r2
            r19.endColumn()
            goto L_0x0125
        L_0x0369:
            r0 = r19
            boolean r13 = r0.useCustomRecordDelimiter
            if (r13 == 0) goto L_0x03a6
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            char r13 = r13.RecordDelimiter
            if (r2 != r13) goto L_0x03a6
            r0 = r19
            boolean r13 = r0.startedColumn
            if (r13 != 0) goto L_0x038b
            r0 = r19
            int r13 = r0.columnsCount
            if (r13 > 0) goto L_0x038b
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            boolean r13 = r13.SkipEmptyRecords
            if (r13 != 0) goto L_0x0397
        L_0x038b:
            r19.endColumn()
            r19.endRecord()
        L_0x0391:
            r0 = r19
            r0.lastLetter = r2
            goto L_0x0125
        L_0x0397:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            int r14 = r14 + 1
            r13.LineStart = r14
            goto L_0x0391
        L_0x03a6:
            r0 = r19
            boolean r13 = r0.useCustomRecordDelimiter
            if (r13 != 0) goto L_0x03ef
            r13 = 13
            if (r2 == r13) goto L_0x03b4
            r13 = 10
            if (r2 != r13) goto L_0x03ef
        L_0x03b4:
            r0 = r19
            boolean r13 = r0.startedColumn
            if (r13 != 0) goto L_0x03d4
            r0 = r19
            int r13 = r0.columnsCount
            if (r13 > 0) goto L_0x03d4
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            boolean r13 = r13.SkipEmptyRecords
            if (r13 != 0) goto L_0x03e0
            r13 = 13
            if (r2 == r13) goto L_0x03d4
            r0 = r19
            char r13 = r0.lastLetter
            r14 = 13
            if (r13 == r14) goto L_0x03e0
        L_0x03d4:
            r19.endColumn()
            r19.endRecord()
        L_0x03da:
            r0 = r19
            r0.lastLetter = r2
            goto L_0x0125
        L_0x03e0:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            int r14 = r14 + 1
            r13.LineStart = r14
            goto L_0x03da
        L_0x03ef:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            boolean r13 = r13.UseComments
            if (r13 == 0) goto L_0x040e
            r0 = r19
            int r13 = r0.columnsCount
            if (r13 != 0) goto L_0x040e
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            char r13 = r13.Comment
            if (r2 != r13) goto L_0x040e
            r0 = r19
            r0.lastLetter = r2
            r19.skipLine()
            goto L_0x0125
        L_0x040e:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            boolean r13 = r13.TrimWhitespace
            if (r13 == 0) goto L_0x0433
            r13 = 32
            if (r2 == r13) goto L_0x041e
            r13 = 9
            if (r2 != r13) goto L_0x0433
        L_0x041e:
            r13 = 1
            r0 = r19
            r0.startedColumn = r13
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            int r14 = r14 + 1
            r13.ColumnStart = r14
            goto L_0x0125
        L_0x0433:
            r13 = 1
            r0 = r19
            r0.startedColumn = r13
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            r13.ColumnStart = r14
            r9 = 0
            r12 = 0
            r4 = 1
            r6 = 0
            r7 = 0
            r8 = 1
        L_0x044a:
            if (r8 != 0) goto L_0x046b
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            int r13 = r13.Position
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Count
            if (r13 != r14) goto L_0x046b
            r19.checkDataLength()
        L_0x045d:
            r0 = r19
            boolean r13 = r0.hasMoreData
            if (r13 == 0) goto L_0x0125
            r0 = r19
            boolean r13 = r0.startedColumn
            if (r13 != 0) goto L_0x044a
            goto L_0x0125
        L_0x046b:
            if (r8 != 0) goto L_0x047b
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            char[] r13 = r13.Buffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            char r2 = r13[r14]
        L_0x047b:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            boolean r13 = r13.UseTextQualifier
            if (r13 != 0) goto L_0x052d
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            int r13 = r13.EscapeMode
            r14 = 2
            if (r13 != r14) goto L_0x052d
            r13 = 92
            if (r2 != r13) goto L_0x052d
            if (r9 == 0) goto L_0x0527
            r9 = 0
        L_0x0493:
            r0 = r19
            r0.lastLetter = r2
            r8 = 0
            r0 = r19
            boolean r13 = r0.startedColumn
            if (r13 == 0) goto L_0x045d
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            int r14 = r13.Position
            int r14 = r14 + 1
            r13.Position = r14
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            boolean r13 = r13.SafetySwitch
            if (r13 == 0) goto L_0x045d
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            int r13 = r13.Position
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.ColumnStart
            int r13 = r13 - r14
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$ColumnBuffer r14 = r0.columnBuffer
            int r14 = r14.Position
            int r13 = r13 + r14
            r14 = 100000(0x186a0, float:1.4013E-40)
            if (r13 <= r14) goto L_0x045d
            r19.close()
            java.io.IOException r13 = new java.io.IOException
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = "Maximum column length of 100,000 exceeded in column "
            java.lang.StringBuilder r14 = r14.append(r15)
            java.text.NumberFormat r15 = java.text.NumberFormat.getIntegerInstance()
            r0 = r19
            int r0 = r0.columnsCount
            r16 = r0
            r0 = r16
            long r0 = (long) r0
            r16 = r0
            java.lang.String r15 = r15.format(r16)
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r15 = " in record "
            java.lang.StringBuilder r14 = r14.append(r15)
            java.text.NumberFormat r15 = java.text.NumberFormat.getIntegerInstance()
            r0 = r19
            long r0 = r0.currentRecord
            r16 = r0
            java.lang.String r15 = r15.format(r16)
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r15 = ". Set the SafetySwitch property to false"
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r15 = " if you're expecting column lengths greater than 100,000 characters to"
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r15 = " avoid this error."
            java.lang.StringBuilder r14 = r14.append(r15)
            java.lang.String r14 = r14.toString()
            r13.<init>(r14)
            throw r13
        L_0x0527:
            r19.updateCurrentValue()
            r9 = 1
            goto L_0x0493
        L_0x052d:
            if (r12 == 0) goto L_0x0583
            int r6 = r6 + 1
            switch(r4) {
                case 1: goto L_0x053d;
                case 2: goto L_0x054b;
                case 3: goto L_0x0558;
                case 4: goto L_0x0565;
                default: goto L_0x0534;
            }
        L_0x0534:
            if (r12 != 0) goto L_0x0573
            r0 = r19
            r0.appendLetter(r7)
            goto L_0x0493
        L_0x053d:
            int r13 = r7 * 16
            char r7 = (char) r13
            char r13 = hexToDec(r2)
            int r13 = r13 + r7
            char r7 = (char) r13
            r13 = 4
            if (r6 != r13) goto L_0x0534
            r12 = 0
            goto L_0x0534
        L_0x054b:
            int r13 = r7 * 8
            char r7 = (char) r13
            int r13 = r2 + -48
            char r13 = (char) r13
            int r13 = r13 + r7
            char r7 = (char) r13
            r13 = 3
            if (r6 != r13) goto L_0x0534
            r12 = 0
            goto L_0x0534
        L_0x0558:
            int r13 = r7 * 10
            char r7 = (char) r13
            int r13 = r2 + -48
            char r13 = (char) r13
            int r13 = r13 + r7
            char r7 = (char) r13
            r13 = 3
            if (r6 != r13) goto L_0x0534
            r12 = 0
            goto L_0x0534
        L_0x0565:
            int r13 = r7 * 16
            char r7 = (char) r13
            char r13 = hexToDec(r2)
            int r13 = r13 + r7
            char r7 = (char) r13
            r13 = 2
            if (r6 != r13) goto L_0x0534
            r12 = 0
            goto L_0x0534
        L_0x0573:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            int r14 = r14 + 1
            r13.ColumnStart = r14
            goto L_0x0493
        L_0x0583:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            int r13 = r13.EscapeMode
            r14 = 2
            if (r13 != r14) goto L_0x0605
            if (r9 == 0) goto L_0x0605
            switch(r2) {
                case 48: goto L_0x05d3;
                case 49: goto L_0x05d3;
                case 50: goto L_0x05d3;
                case 51: goto L_0x05d3;
                case 52: goto L_0x05d3;
                case 53: goto L_0x05d3;
                case 54: goto L_0x05d3;
                case 55: goto L_0x05d3;
                case 68: goto L_0x05e8;
                case 79: goto L_0x05e8;
                case 85: goto L_0x05e8;
                case 88: goto L_0x05e8;
                case 97: goto L_0x05cc;
                case 98: goto L_0x05ac;
                case 100: goto L_0x05e8;
                case 101: goto L_0x05bc;
                case 102: goto L_0x05b4;
                case 110: goto L_0x0594;
                case 111: goto L_0x05e8;
                case 114: goto L_0x059c;
                case 116: goto L_0x05a4;
                case 117: goto L_0x05e8;
                case 118: goto L_0x05c4;
                case 120: goto L_0x05e8;
                default: goto L_0x0591;
            }
        L_0x0591:
            r9 = 0
            goto L_0x0493
        L_0x0594:
            r13 = 10
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0591
        L_0x059c:
            r13 = 13
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0591
        L_0x05a4:
            r13 = 9
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0591
        L_0x05ac:
            r13 = 8
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0591
        L_0x05b4:
            r13 = 12
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0591
        L_0x05bc:
            r13 = 27
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0591
        L_0x05c4:
            r13 = 11
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0591
        L_0x05cc:
            r13 = 7
            r0 = r19
            r0.appendLetter(r13)
            goto L_0x0591
        L_0x05d3:
            r4 = 2
            r12 = 1
            r6 = 1
            int r13 = r2 + -48
            char r7 = (char) r13
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            int r14 = r14 + 1
            r13.ColumnStart = r14
            goto L_0x0591
        L_0x05e8:
            switch(r2) {
                case 68: goto L_0x0603;
                case 79: goto L_0x0601;
                case 85: goto L_0x05fd;
                case 88: goto L_0x05ff;
                case 100: goto L_0x0603;
                case 111: goto L_0x0601;
                case 117: goto L_0x05fd;
                case 120: goto L_0x05ff;
                default: goto L_0x05eb;
            }
        L_0x05eb:
            r12 = 1
            r6 = 0
            r7 = 0
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r13 = r0.dataBuffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r14 = r0.dataBuffer
            int r14 = r14.Position
            int r14 = r14 + 1
            r13.ColumnStart = r14
            goto L_0x0591
        L_0x05fd:
            r4 = 1
            goto L_0x05eb
        L_0x05ff:
            r4 = 4
            goto L_0x05eb
        L_0x0601:
            r4 = 2
            goto L_0x05eb
        L_0x0603:
            r4 = 3
            goto L_0x05eb
        L_0x0605:
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            char r13 = r13.Delimiter
            if (r2 != r13) goto L_0x0612
            r19.endColumn()
            goto L_0x0493
        L_0x0612:
            r0 = r19
            boolean r13 = r0.useCustomRecordDelimiter
            if (r13 != 0) goto L_0x0620
            r13 = 13
            if (r2 == r13) goto L_0x062e
            r13 = 10
            if (r2 == r13) goto L_0x062e
        L_0x0620:
            r0 = r19
            boolean r13 = r0.useCustomRecordDelimiter
            if (r13 == 0) goto L_0x0493
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$UserSettings r13 = r0.userSettings
            char r13 = r13.RecordDelimiter
            if (r2 != r13) goto L_0x0493
        L_0x062e:
            r19.endColumn()
            r19.endRecord()
            goto L_0x0493
        L_0x0636:
            java.lang.StringBuilder r13 = new java.lang.StringBuilder
            r13.<init>()
            java.lang.String r14 = new java.lang.String
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$RawRecordBuffer r15 = r0.rawBuffer
            char[] r15 = r15.Buffer
            r16 = 0
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$RawRecordBuffer r0 = r0.rawBuffer
            r17 = r0
            r0 = r17
            int r0 = r0.Position
            r17 = r0
            r14.<init>(r15, r16, r17)
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.String r14 = new java.lang.String
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r15 = r0.dataBuffer
            char[] r15 = r15.Buffer
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r0 = r0.dataBuffer
            r16 = r0
            r0 = r16
            int r0 = r0.LineStart
            r16 = r0
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r0 = r0.dataBuffer
            r17 = r0
            r0 = r17
            int r0 = r0.Position
            r17 = r0
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$DataBuffer r0 = r0.dataBuffer
            r18 = r0
            r0 = r18
            int r0 = r0.LineStart
            r18 = r0
            int r17 = r17 - r18
            int r17 = r17 + -1
            r14.<init>(r15, r16, r17)
            java.lang.StringBuilder r13 = r13.append(r14)
            java.lang.String r13 = r13.toString()
            r0 = r19
            r0.rawRecord = r13
            goto L_0x00a2
        L_0x0699:
            java.lang.String r13 = new java.lang.String
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$RawRecordBuffer r14 = r0.rawBuffer
            char[] r14 = r14.Buffer
            r15 = 0
            r0 = r19
            com.taobao.ju.track.csv.CsvReader$RawRecordBuffer r0 = r0.rawBuffer
            r16 = r0
            r0 = r16
            int r0 = r0.Position
            r16 = r0
            r13.<init>(r14, r15, r16)
            r0 = r19
            r0.rawRecord = r13
            goto L_0x00a2
        L_0x06b7:
            java.lang.String r13 = ""
            r0 = r19
            r0.rawRecord = r13
            goto L_0x00a2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.ju.track.csv.CsvReader.readRecord():boolean");
    }

    private void checkDataLength() throws IOException {
        if (!this.initialized) {
            if (this.fileName != null) {
                this.inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(this.fileName), this.charset), 4096);
            }
            this.charset = null;
            this.initialized = true;
        }
        updateCurrentValue();
        if (this.userSettings.CaptureRawRecord && this.dataBuffer.Count > 0) {
            if (this.rawBuffer.Buffer.length - this.rawBuffer.Position < this.dataBuffer.Count - this.dataBuffer.LineStart) {
                char[] holder = new char[(this.rawBuffer.Buffer.length + Math.max(this.dataBuffer.Count - this.dataBuffer.LineStart, this.rawBuffer.Buffer.length))];
                System.arraycopy(this.rawBuffer.Buffer, 0, holder, 0, this.rawBuffer.Position);
                this.rawBuffer.Buffer = holder;
            }
            System.arraycopy(this.dataBuffer.Buffer, this.dataBuffer.LineStart, this.rawBuffer.Buffer, this.rawBuffer.Position, this.dataBuffer.Count - this.dataBuffer.LineStart);
            this.rawBuffer.Position += this.dataBuffer.Count - this.dataBuffer.LineStart;
        }
        try {
            this.dataBuffer.Count = this.inputStream.read(this.dataBuffer.Buffer, 0, this.dataBuffer.Buffer.length);
            if (this.dataBuffer.Count == -1) {
                this.hasMoreData = false;
            }
            this.dataBuffer.Position = 0;
            this.dataBuffer.LineStart = 0;
            this.dataBuffer.ColumnStart = 0;
        } catch (IOException ex) {
            close();
            throw ex;
        }
    }

    public boolean readHeaders() throws IOException {
        boolean result = readRecord();
        this.headersHolder.Length = this.columnsCount;
        this.headersHolder.Headers = new String[this.columnsCount];
        for (int i = 0; i < this.headersHolder.Length; i++) {
            String columnValue = get(i);
            this.headersHolder.Headers[i] = columnValue;
            this.headersHolder.IndexByName.put(columnValue, Integer.valueOf(i));
        }
        if (result) {
            this.currentRecord--;
        }
        this.columnsCount = 0;
        return result;
    }

    public String getHeader(int columnIndex) throws IOException {
        checkClosed();
        if (columnIndex <= -1 || columnIndex >= this.headersHolder.Length) {
            return "";
        }
        return this.headersHolder.Headers[columnIndex];
    }

    public boolean isQualified(int columnIndex) throws IOException {
        checkClosed();
        if (columnIndex >= this.columnsCount || columnIndex <= -1) {
            return false;
        }
        return this.isQualified[columnIndex];
    }

    private void endColumn() throws IOException {
        String currentValue = "";
        if (this.startedColumn) {
            if (this.columnBuffer.Position != 0) {
                updateCurrentValue();
                int lastLetter2 = this.columnBuffer.Position - 1;
                if (this.userSettings.TrimWhitespace && !this.startedWithQualifier) {
                    while (lastLetter2 >= 0 && (this.columnBuffer.Buffer[lastLetter2] == ' ' || this.columnBuffer.Buffer[lastLetter2] == ' ')) {
                        lastLetter2--;
                    }
                }
                currentValue = new String(this.columnBuffer.Buffer, 0, lastLetter2 + 1);
            } else if (this.dataBuffer.ColumnStart < this.dataBuffer.Position) {
                int lastLetter3 = this.dataBuffer.Position - 1;
                if (this.userSettings.TrimWhitespace && !this.startedWithQualifier) {
                    while (lastLetter3 >= this.dataBuffer.ColumnStart && (this.dataBuffer.Buffer[lastLetter3] == ' ' || this.dataBuffer.Buffer[lastLetter3] == 9)) {
                        lastLetter3--;
                    }
                }
                currentValue = new String(this.dataBuffer.Buffer, this.dataBuffer.ColumnStart, (lastLetter3 - this.dataBuffer.ColumnStart) + 1);
            }
        }
        this.columnBuffer.Position = 0;
        this.startedColumn = false;
        if (this.columnsCount < 100000 || !this.userSettings.SafetySwitch) {
            if (this.columnsCount == this.values.length) {
                int newLength = this.values.length * 2;
                String[] holder = new String[newLength];
                System.arraycopy(this.values, 0, holder, 0, this.values.length);
                this.values = holder;
                boolean[] qualifiedHolder = new boolean[newLength];
                System.arraycopy(this.isQualified, 0, qualifiedHolder, 0, this.isQualified.length);
                this.isQualified = qualifiedHolder;
            }
            this.values[this.columnsCount] = currentValue;
            this.isQualified[this.columnsCount] = this.startedWithQualifier;
            this.columnsCount++;
            return;
        }
        close();
        throw new IOException("Maximum column count of 100,000 exceeded in record " + NumberFormat.getIntegerInstance().format(this.currentRecord) + ". Set the SafetySwitch property to false" + " if you're expecting more than 100,000 columns per record to" + " avoid this error.");
    }

    private void appendLetter(char letter) {
        if (this.columnBuffer.Position == this.columnBuffer.Buffer.length) {
            char[] holder = new char[(this.columnBuffer.Buffer.length * 2)];
            System.arraycopy(this.columnBuffer.Buffer, 0, holder, 0, this.columnBuffer.Position);
            this.columnBuffer.Buffer = holder;
        }
        char[] cArr = this.columnBuffer.Buffer;
        ColumnBuffer columnBuffer2 = this.columnBuffer;
        int i = columnBuffer2.Position;
        columnBuffer2.Position = i + 1;
        cArr[i] = letter;
        this.dataBuffer.ColumnStart = this.dataBuffer.Position + 1;
    }

    private void updateCurrentValue() {
        if (this.startedColumn && this.dataBuffer.ColumnStart < this.dataBuffer.Position) {
            if (this.columnBuffer.Buffer.length - this.columnBuffer.Position < this.dataBuffer.Position - this.dataBuffer.ColumnStart) {
                char[] holder = new char[(this.columnBuffer.Buffer.length + Math.max(this.dataBuffer.Position - this.dataBuffer.ColumnStart, this.columnBuffer.Buffer.length))];
                System.arraycopy(this.columnBuffer.Buffer, 0, holder, 0, this.columnBuffer.Position);
                this.columnBuffer.Buffer = holder;
            }
            System.arraycopy(this.dataBuffer.Buffer, this.dataBuffer.ColumnStart, this.columnBuffer.Buffer, this.columnBuffer.Position, this.dataBuffer.Position - this.dataBuffer.ColumnStart);
            this.columnBuffer.Position += this.dataBuffer.Position - this.dataBuffer.ColumnStart;
        }
        this.dataBuffer.ColumnStart = this.dataBuffer.Position + 1;
    }

    private void endRecord() throws IOException {
        this.hasReadNextLine = true;
        this.currentRecord++;
    }

    public int getIndex(String headerName) throws IOException {
        checkClosed();
        Object indexValue = this.headersHolder.IndexByName.get(headerName);
        if (indexValue != null) {
            return ((Integer) indexValue).intValue();
        }
        return -1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0008, code lost:
        r0 = readRecord();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean skipRecord() throws java.io.IOException {
        /*
            r6 = this;
            r6.checkClosed()
            r0 = 0
            boolean r1 = r6.hasMoreData
            if (r1 == 0) goto L_0x0015
            boolean r0 = r6.readRecord()
            if (r0 == 0) goto L_0x0015
            long r2 = r6.currentRecord
            r4 = 1
            long r2 = r2 - r4
            r6.currentRecord = r2
        L_0x0015:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.ju.track.csv.CsvReader.skipRecord():boolean");
    }

    public boolean skipLine() throws IOException {
        checkClosed();
        this.columnsCount = 0;
        boolean skippedLine = false;
        if (this.hasMoreData) {
            boolean foundEol = false;
            do {
                if (this.dataBuffer.Position == this.dataBuffer.Count) {
                    checkDataLength();
                } else {
                    skippedLine = true;
                    char currentLetter = this.dataBuffer.Buffer[this.dataBuffer.Position];
                    if (currentLetter == 13 || currentLetter == 10) {
                        foundEol = true;
                    }
                    this.lastLetter = currentLetter;
                    if (!foundEol) {
                        this.dataBuffer.Position++;
                    }
                }
                if (!this.hasMoreData) {
                    break;
                }
            } while (!foundEol);
            this.columnBuffer.Position = 0;
            this.dataBuffer.LineStart = this.dataBuffer.Position + 1;
        }
        this.rawBuffer.Position = 0;
        this.rawRecord = "";
        return skippedLine;
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
                this.headersHolder.Headers = null;
                this.headersHolder.IndexByName = null;
                this.dataBuffer.Buffer = null;
                this.columnBuffer.Buffer = null;
                this.rawBuffer.Buffer = null;
            }
            try {
                if (this.initialized) {
                    this.inputStream.close();
                }
            } catch (Exception e) {
            }
            this.inputStream = null;
            this.closed = true;
        }
    }

    private void checkClosed() throws IOException {
        if (this.closed) {
            throw new IOException("This instance of the CsvReader class has already been closed.");
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        close(false);
    }

    private class ComplexEscape {
        private static final int DECIMAL = 3;
        private static final int HEX = 4;
        private static final int OCTAL = 2;
        private static final int UNICODE = 1;

        private ComplexEscape() {
        }
    }

    private static char hexToDec(char hex) {
        if (hex >= 'a') {
            return (char) ((hex - 'a') + 10);
        }
        if (hex >= 'A') {
            return (char) ((hex - 'A') + 10);
        }
        return (char) (hex - '0');
    }

    private class DataBuffer {
        public char[] Buffer = new char[1024];
        public int ColumnStart = 0;
        public int Count = 0;
        public int LineStart = 0;
        public int Position = 0;

        public DataBuffer() {
        }
    }

    private class ColumnBuffer {
        public char[] Buffer = new char[50];
        public int Position = 0;

        public ColumnBuffer() {
        }
    }

    private class RawRecordBuffer {
        public char[] Buffer = new char[500];
        public int Position = 0;

        public RawRecordBuffer() {
        }
    }

    private class Letters {
        public static final char ALERT = '\u0007';
        public static final char BACKSLASH = '\\';
        public static final char BACKSPACE = '\b';
        public static final char COMMA = ',';
        public static final char CR = '\r';
        public static final char ESCAPE = '\u001b';
        public static final char FORM_FEED = '\f';
        public static final char LF = '\n';
        public static final char NULL = '\u0000';
        public static final char POUND = '#';
        public static final char QUOTE = '\"';
        public static final char SPACE = ' ';
        public static final char TAB = '\t';
        public static final char VERTICAL_TAB = '\u000b';

        private Letters() {
        }
    }

    private class UserSettings {
        public boolean CaptureRawRecord = true;
        public boolean CaseSensitive = true;
        public char Comment = '#';
        public char Delimiter = ',';
        public int EscapeMode = 1;
        public char RecordDelimiter = 0;
        public boolean SafetySwitch = true;
        public boolean SkipEmptyRecords = true;
        public char TextQualifier = '\"';
        public boolean TrimWhitespace = true;
        public boolean UseComments = false;
        public boolean UseTextQualifier = true;

        public UserSettings() {
        }
    }

    private class HeadersHolder {
        public String[] Headers = null;
        public HashMap IndexByName = new HashMap();
        public int Length = 0;

        public HeadersHolder() {
        }
    }

    private class StaticSettings {
        public static final int INITIAL_COLUMN_BUFFER_SIZE = 50;
        public static final int INITIAL_COLUMN_COUNT = 10;
        public static final int MAX_BUFFER_SIZE = 1024;
        public static final int MAX_FILE_BUFFER_SIZE = 4096;

        private StaticSettings() {
        }
    }
}
