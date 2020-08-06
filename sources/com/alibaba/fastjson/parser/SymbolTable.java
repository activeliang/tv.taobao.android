package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;

public class SymbolTable {
    private final int indexMask;
    private final String[] symbols;

    public SymbolTable(int tableSize) {
        this.indexMask = tableSize - 1;
        this.symbols = new String[tableSize];
        addSymbol("$ref", 0, 4, "$ref".hashCode());
        addSymbol(JSON.DEFAULT_TYPE_KEY, 0, JSON.DEFAULT_TYPE_KEY.length(), JSON.DEFAULT_TYPE_KEY.hashCode());
    }

    public String addSymbol(char[] buffer, int offset, int len) {
        return addSymbol(buffer, offset, len, hash(buffer, offset, len));
    }

    public String addSymbol(char[] buffer, int offset, int len, int hash) {
        int bucket = hash & this.indexMask;
        String symbol = this.symbols[bucket];
        if (symbol != null) {
            boolean eq = true;
            if (hash == symbol.hashCode() && len == symbol.length()) {
                int i = 0;
                while (true) {
                    if (i >= len) {
                        break;
                    } else if (buffer[offset + i] != symbol.charAt(i)) {
                        eq = false;
                        break;
                    } else {
                        i++;
                    }
                }
            } else {
                eq = false;
            }
            if (eq) {
                return symbol;
            }
            return new String(buffer, offset, len);
        }
        String symbol2 = new String(buffer, offset, len).intern();
        this.symbols[bucket] = symbol2;
        return symbol2;
    }

    public String addSymbol(String buffer, int offset, int len, int hash) {
        String symbol;
        int bucket = hash & this.indexMask;
        String symbol2 = this.symbols[bucket];
        if (symbol2 == null) {
            if (len == buffer.length()) {
                symbol = buffer;
            } else {
                symbol = subString(buffer, offset, len);
            }
            String symbol3 = symbol.intern();
            this.symbols[bucket] = symbol3;
            return symbol3;
        } else if (hash == symbol2.hashCode() && len == symbol2.length() && buffer.startsWith(symbol2, offset)) {
            return symbol2;
        } else {
            return subString(buffer, offset, len);
        }
    }

    private static String subString(String src, int offset, int len) {
        char[] chars = new char[len];
        src.getChars(offset, offset + len, chars, 0);
        return new String(chars);
    }

    public static int hash(char[] buffer, int offset, int len) {
        int h = 0;
        int i = 0;
        int off = offset;
        while (i < len) {
            h = (h * 31) + buffer[off];
            i++;
            off++;
        }
        return h;
    }
}
