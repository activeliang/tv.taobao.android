package com.taobao.atlas.dexmerge.dx.util;

public final class HexParser {
    private HexParser() {
    }

    public static byte[] parse(String src) {
        String line;
        int quoteAt;
        int len = src.length();
        byte[] result = new byte[(len / 2)];
        int at = 0;
        int outAt = 0;
        while (at < len) {
            int nlAt = src.indexOf(10, at);
            if (nlAt < 0) {
                nlAt = len;
            }
            int poundAt = src.indexOf(35, at);
            if (poundAt < 0 || poundAt >= nlAt) {
                line = src.substring(at, nlAt);
            } else {
                line = src.substring(at, poundAt);
            }
            at = nlAt + 1;
            int colonAt = line.indexOf(58);
            if (colonAt != -1 && ((quoteAt = line.indexOf(34)) == -1 || quoteAt >= colonAt)) {
                String atStr = line.substring(0, colonAt).trim();
                line = line.substring(colonAt + 1);
                if (Integer.parseInt(atStr, 16) != outAt) {
                    throw new RuntimeException("bogus offset marker: " + atStr);
                }
            }
            int lineLen = line.length();
            int value = -1;
            boolean quoteMode = false;
            for (int i = 0; i < lineLen; i++) {
                char c = line.charAt(i);
                if (quoteMode) {
                    if (c == '\"') {
                        quoteMode = false;
                    } else {
                        result[outAt] = (byte) c;
                        outAt++;
                    }
                } else if (c <= ' ') {
                    continue;
                } else if (c != '\"') {
                    int digVal = Character.digit(c, 16);
                    if (digVal == -1) {
                        throw new RuntimeException("bogus digit character: \"" + c + "\"");
                    } else if (value == -1) {
                        value = digVal;
                    } else {
                        result[outAt] = (byte) ((value << 4) | digVal);
                        outAt++;
                        value = -1;
                    }
                } else if (value != -1) {
                    throw new RuntimeException("spare digit around offset " + Hex.u4(outAt));
                } else {
                    quoteMode = true;
                }
            }
            if (value != -1) {
                throw new RuntimeException("spare digit around offset " + Hex.u4(outAt));
            } else if (quoteMode) {
                throw new RuntimeException("unterminated quote around offset " + Hex.u4(outAt));
            }
        }
        if (outAt >= result.length) {
            return result;
        }
        byte[] newr = new byte[outAt];
        System.arraycopy(result, 0, newr, 0, outAt);
        return newr;
    }
}
