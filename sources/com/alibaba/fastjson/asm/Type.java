package com.alibaba.fastjson.asm;

public class Type {
    public static final Type BOOLEAN_TYPE = new Type(1, (char[]) null, 1509950721, 1);
    public static final Type BYTE_TYPE = new Type(3, (char[]) null, 1107297537, 1);
    public static final Type CHAR_TYPE = new Type(2, (char[]) null, 1124075009, 1);
    public static final Type DOUBLE_TYPE = new Type(8, (char[]) null, 1141048066, 1);
    public static final Type FLOAT_TYPE = new Type(6, (char[]) null, 1174536705, 1);
    public static final Type INT_TYPE = new Type(5, (char[]) null, 1224736769, 1);
    public static final Type LONG_TYPE = new Type(7, (char[]) null, 1241579778, 1);
    public static final Type SHORT_TYPE = new Type(4, (char[]) null, 1392510721, 1);
    public static final Type VOID_TYPE = new Type(0, (char[]) null, 1443168256, 1);
    private final char[] buf;
    private final int len;
    private final int off;
    protected final int sort;

    private Type(int sort2, char[] buf2, int off2, int len2) {
        this.sort = sort2;
        this.buf = buf2;
        this.off = off2;
        this.len = len2;
    }

    public static Type getType(String typeDescriptor) {
        return getType(typeDescriptor.toCharArray(), 0);
    }

    public static int getArgumentsAndReturnSizes(String desc) {
        int c;
        int n = 1;
        int c2 = 1;
        while (true) {
            c = c2 + 1;
            char car = desc.charAt(c2);
            if (car == ')') {
                break;
            } else if (car == 'L') {
                while (true) {
                    int c3 = c;
                    c = c3 + 1;
                    if (desc.charAt(c3) == ';') {
                        break;
                    }
                }
                n++;
                c2 = c;
            } else if (car == 'D' || car == 'J') {
                n += 2;
                c2 = c;
            } else {
                n++;
                c2 = c;
            }
        }
        char car2 = desc.charAt(c);
        return (car2 == 'V' ? 0 : (car2 == 'D' || car2 == 'J') ? 2 : 1) | (n << 2);
    }

    private static Type getType(char[] buf2, int off2) {
        switch (buf2[off2]) {
            case 'B':
                return BYTE_TYPE;
            case 'C':
                return CHAR_TYPE;
            case 'D':
                return DOUBLE_TYPE;
            case 'F':
                return FLOAT_TYPE;
            case 'I':
                return INT_TYPE;
            case 'J':
                return LONG_TYPE;
            case 'S':
                return SHORT_TYPE;
            case 'V':
                return VOID_TYPE;
            case 'Z':
                return BOOLEAN_TYPE;
            case '[':
                int len2 = 1;
                while (buf2[off2 + len2] == '[') {
                    len2++;
                }
                if (buf2[off2 + len2] == 'L') {
                    while (true) {
                        len2++;
                        if (buf2[off2 + len2] != ';') {
                        }
                    }
                }
                return new Type(9, buf2, off2, len2 + 1);
            default:
                int len3 = 1;
                while (buf2[off2 + len3] != ';') {
                    len3++;
                }
                return new Type(10, buf2, off2 + 1, len3 - 1);
        }
    }

    public String getInternalName() {
        return new String(this.buf, this.off, this.len);
    }

    /* access modifiers changed from: package-private */
    public String getDescriptor() {
        return new String(this.buf, this.off, this.len);
    }
}
