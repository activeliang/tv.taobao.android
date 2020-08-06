package org.apache.commons.codec.language;

import com.taobao.atlas.dexmerge.dx.io.Opcodes;
import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class ColognePhonetic implements StringEncoder {
    private static final char[] AEIJOUY = {'A', 'E', 'I', 'J', 'O', 'U', 'Y'};
    private static final char[] AHKLOQRUX = {'A', 'H', 'K', 'L', 'O', 'Q', 'R', 'U', 'X'};
    private static final char[] AHOUKQX = {'A', 'H', 'O', 'U', 'K', 'Q', 'X'};
    private static final char[] CKQ = {'C', 'K', 'Q'};
    private static final char[] GKQ = {'G', 'K', 'Q'};
    private static final char[] SCZ = {'S', 'C', 'Z'};
    private static final char[] SZ = {'S', 'Z'};
    private static final char[] TDX = {'T', 'D', 'X'};
    private static final char[] WFPV = {'W', 'F', 'P', 'V'};

    private abstract class CologneBuffer {
        protected final char[] data;
        protected int length = 0;

        /* access modifiers changed from: protected */
        public abstract char[] copyData(int i, int i2);

        public CologneBuffer(char[] data2) {
            this.data = data2;
            this.length = data2.length;
        }

        public CologneBuffer(int buffSize) {
            this.data = new char[buffSize];
            this.length = 0;
        }

        public int length() {
            return this.length;
        }

        public String toString() {
            return new String(copyData(0, this.length));
        }
    }

    private class CologneOutputBuffer extends CologneBuffer {
        public CologneOutputBuffer(int buffSize) {
            super(buffSize);
        }

        public void addRight(char chr) {
            this.data[this.length] = chr;
            this.length++;
        }

        /* access modifiers changed from: protected */
        public char[] copyData(int start, int length) {
            char[] newData = new char[length];
            System.arraycopy(this.data, start, newData, 0, length);
            return newData;
        }
    }

    private class CologneInputBuffer extends CologneBuffer {
        public CologneInputBuffer(char[] data) {
            super(data);
        }

        public void addLeft(char ch) {
            this.length++;
            this.data[getNextPos()] = ch;
        }

        /* access modifiers changed from: protected */
        public char[] copyData(int start, int length) {
            char[] newData = new char[length];
            System.arraycopy(this.data, (this.data.length - this.length) + start, newData, 0, length);
            return newData;
        }

        public char getNextChar() {
            return this.data[getNextPos()];
        }

        /* access modifiers changed from: protected */
        public int getNextPos() {
            return this.data.length - this.length;
        }

        public char removeNext() {
            this.length--;
            return getNextChar();
        }
    }

    private static boolean arrayContains(char[] arr, char key) {
        for (char element : arr) {
            if (element == key) {
                return true;
            }
        }
        return false;
    }

    public String colognePhonetic(String text) {
        char nextChar;
        char code;
        if (text == null) {
            return null;
        }
        CologneInputBuffer input = new CologneInputBuffer(preprocess(text));
        CologneOutputBuffer output = new CologneOutputBuffer(input.length() * 2);
        char lastChar = Soundex.SILENT_MARKER;
        char lastCode = '/';
        while (input.length() > 0) {
            char chr = input.removeNext();
            if (input.length() > 0) {
                nextChar = input.getNextChar();
            } else {
                nextChar = Soundex.SILENT_MARKER;
            }
            if (chr != 'H' && chr >= 'A' && chr <= 'Z') {
                if (arrayContains(AEIJOUY, chr)) {
                    code = '0';
                } else if (chr == 'B' || (chr == 'P' && nextChar != 'H')) {
                    code = '1';
                } else if ((chr == 'D' || chr == 'T') && !arrayContains(SCZ, nextChar)) {
                    code = '2';
                } else if (arrayContains(WFPV, chr)) {
                    code = '3';
                } else if (arrayContains(GKQ, chr)) {
                    code = '4';
                } else if (chr == 'X' && !arrayContains(CKQ, lastChar)) {
                    code = '4';
                    input.addLeft('S');
                } else if (chr == 'S' || chr == 'Z') {
                    code = '8';
                } else if (chr == 'C') {
                    if (lastCode == '/') {
                        if (arrayContains(AHKLOQRUX, nextChar)) {
                            code = '4';
                        } else {
                            code = '8';
                        }
                    } else if (arrayContains(SZ, lastChar) || !arrayContains(AHOUKQX, nextChar)) {
                        code = '8';
                    } else {
                        code = '4';
                    }
                } else if (arrayContains(TDX, chr)) {
                    code = '8';
                } else if (chr == 'R') {
                    code = '7';
                } else if (chr == 'L') {
                    code = '5';
                } else if (chr == 'M' || chr == 'N') {
                    code = '6';
                } else {
                    code = chr;
                }
                if (code != '-' && ((lastCode != code && (code != '0' || lastCode == '/')) || code < '0' || code > '8')) {
                    output.addRight(code);
                }
                lastChar = chr;
                lastCode = code;
            }
        }
        return output.toString();
    }

    public Object encode(Object object) throws EncoderException {
        if (object instanceof String) {
            return encode((String) object);
        }
        throw new EncoderException("This method's parameter was expected to be of the type " + String.class.getName() + ". But actually it was of the type " + object.getClass().getName() + ".");
    }

    public String encode(String text) {
        return colognePhonetic(text);
    }

    public boolean isEncodeEqual(String text1, String text2) {
        return colognePhonetic(text1).equals(colognePhonetic(text2));
    }

    private char[] preprocess(String text) {
        char[] chrs = text.toUpperCase(Locale.GERMAN).toCharArray();
        for (int index = 0; index < chrs.length; index++) {
            switch (chrs[index]) {
                case Opcodes.SHR_LONG_2ADDR:
                    chrs[index] = 'A';
                    break;
                case Opcodes.OR_INT_LIT16:
                    chrs[index] = 'O';
                    break;
                case Opcodes.REM_INT_LIT8:
                    chrs[index] = 'U';
                    break;
            }
        }
        return chrs;
    }
}
