package com.google.zxing.datamatrix.encoder;

import com.alibaba.wireless.security.SecExceptionCode;
import com.taobao.atlas.dexmerge.dx.io.Opcodes;

public final class ErrorCorrection {
    private static final int[] ALOG = new int[255];
    private static final int[][] FACTORS = {new int[]{228, 48, 15, 111, 62}, new int[]{23, 68, Opcodes.ADD_INT, Opcodes.LONG_TO_DOUBLE, 240, 92, 254}, new int[]{28, 24, 185, 166, Opcodes.XOR_INT_LIT8, 248, 116, 255, 110, 61}, new int[]{Opcodes.REM_DOUBLE, Opcodes.DOUBLE_TO_INT, 205, 12, Opcodes.XOR_LONG_2ADDR, Opcodes.MUL_FLOAT, 39, 245, 60, 97, Opcodes.INVOKE_INTERFACE_RANGE}, new int[]{41, 153, 158, 91, 61, 42, Opcodes.INT_TO_CHAR, Opcodes.AND_INT_LIT16, 97, 178, 100, 242}, new int[]{Opcodes.SUB_LONG, 97, 192, 252, 95, 9, Opcodes.MUL_LONG, 119, Opcodes.DOUBLE_TO_INT, 45, 18, Opcodes.USHR_INT_2ADDR, 83, 185}, new int[]{83, Opcodes.SHL_LONG_2ADDR, 100, 39, Opcodes.SUB_LONG_2ADDR, 75, 66, 61, 241, Opcodes.AND_INT_LIT16, 109, Opcodes.INT_TO_LONG, 94, 254, Opcodes.SHR_INT_LIT8, 48, 90, Opcodes.SUB_LONG_2ADDR}, new int[]{15, Opcodes.SHL_LONG_2ADDR, 244, 9, 233, 71, Opcodes.MUL_FLOAT, 2, Opcodes.SUB_LONG_2ADDR, 160, 153, Opcodes.SUB_INT, 253, 79, 108, 82, 27, Opcodes.DIV_DOUBLE, Opcodes.USHR_INT_2ADDR, Opcodes.SUB_DOUBLE}, new int[]{52, Opcodes.DIV_LONG_2ADDR, 88, 205, 109, 39, 176, 21, Opcodes.ADD_LONG, Opcodes.USHR_LONG_2ADDR, 251, Opcodes.XOR_INT_LIT8, Opcodes.ADD_LONG, 21, 5, Opcodes.SUB_DOUBLE, 254, 124, 12, 181, 184, 96, 50, 193}, new int[]{Opcodes.DIV_INT_LIT16, 231, 43, 97, 71, 96, 103, Opcodes.DIV_DOUBLE, 37, 151, Opcodes.REM_FLOAT, 53, 75, 34, 249, SecExceptionCode.SEC_ERROR_INIT_DATA_FILE_MISMATCH, 17, Opcodes.DOUBLE_TO_INT, 110, Opcodes.AND_INT_LIT16, Opcodes.INT_TO_BYTE, Opcodes.FLOAT_TO_LONG, Opcodes.INVOKE_INTERFACE_RANGE, 151, 233, Opcodes.MUL_FLOAT, 93, 255}, new int[]{245, 127, 242, Opcodes.MUL_INT_LIT8, 130, 250, 162, 181, 102, Opcodes.INVOKE_INTERFACE_RANGE, 84, Opcodes.DIV_INT_2ADDR, Opcodes.REM_INT_LIT8, 251, 80, 182, 229, 18, 2, 4, 68, 33, 101, Opcodes.FLOAT_TO_DOUBLE, 95, 119, SecExceptionCode.SEC_ERROR_INIT_SOURCE_DIR_NOT_EXISTED, 44, Opcodes.REM_DOUBLE, 184, 59, 25, Opcodes.SHR_INT_LIT8, 98, 81, 112}, new int[]{77, 193, Opcodes.FLOAT_TO_DOUBLE, 31, 19, 38, 22, 153, 247, 105, SecExceptionCode.SEC_ERROR_INIT_NO_DATA_FILE, 2, 245, Opcodes.LONG_TO_FLOAT, 242, 8, Opcodes.REM_DOUBLE, 95, 100, 9, 167, 105, Opcodes.OR_INT_LIT16, 111, 57, SecExceptionCode.SEC_ERROR_INIT_DATA_FILE_MISMATCH, 21, 1, 253, 57, 54, 101, 248, 202, 69, 50, 150, 177, Opcodes.USHR_INT_LIT8, 5, 9, 5}, new int[]{245, Opcodes.LONG_TO_INT, Opcodes.SUB_DOUBLE, Opcodes.XOR_INT_LIT8, 96, 32, 117, 22, 238, Opcodes.LONG_TO_FLOAT, 238, 231, 205, Opcodes.SUB_LONG_2ADDR, 237, 87, Opcodes.REM_LONG_2ADDR, 106, 16, Opcodes.DIV_INT, 118, 23, 37, 90, Opcodes.REM_FLOAT, 205, Opcodes.INT_TO_DOUBLE, 88, Opcodes.INVOKE_INTERFACE_RANGE, 100, 66, Opcodes.DOUBLE_TO_INT, Opcodes.USHR_INT_2ADDR, 240, 82, 44, 176, 87, 187, Opcodes.DIV_INT, 160, Opcodes.REM_DOUBLE, 69, Opcodes.AND_INT_LIT16, 92, 253, Opcodes.SHR_INT_LIT8, 19}, new int[]{Opcodes.REM_DOUBLE, 9, Opcodes.XOR_INT_LIT8, 238, 12, 17, Opcodes.REM_INT_LIT8, 208, 100, 29, Opcodes.REM_DOUBLE, Opcodes.REM_FLOAT, 230, 192, Opcodes.XOR_INT_LIT16, 235, 150, 159, 36, Opcodes.XOR_INT_LIT8, 38, 200, Opcodes.LONG_TO_INT, 54, 228, Opcodes.MUL_INT, Opcodes.MUL_INT_LIT8, 234, 117, 203, 29, 232, Opcodes.ADD_INT, 238, 22, 150, 201, 117, 62, 207, Opcodes.SHR_LONG, 13, Opcodes.FLOAT_TO_DOUBLE, 245, 127, 67, 247, 28, Opcodes.ADD_LONG, 43, 203, 107, 233, 53, Opcodes.INT_TO_SHORT, 46}, new int[]{242, 93, 169, 50, Opcodes.ADD_INT, Opcodes.MUL_INT_LIT16, 39, 118, 202, Opcodes.SUB_LONG_2ADDR, 201, Opcodes.MUL_LONG_2ADDR, Opcodes.INT_TO_SHORT, 108, Opcodes.SHR_LONG_2ADDR, 37, 185, 112, Opcodes.LONG_TO_DOUBLE, 230, 245, 63, Opcodes.USHR_LONG_2ADDR, Opcodes.DIV_LONG_2ADDR, 250, 106, 185, Opcodes.AND_INT_LIT8, Opcodes.REM_DOUBLE, 64, 114, 71, 161, 44, Opcodes.DIV_INT, 6, 27, Opcodes.MUL_INT_LIT8, 51, 63, 87, 10, 40, 130, Opcodes.SUB_LONG_2ADDR, 17, 163, 31, 176, Opcodes.REM_FLOAT, 4, 107, 232, 7, 94, 166, Opcodes.SHL_INT_LIT8, 124, 86, 47, 11, 204}, new int[]{Opcodes.REM_INT_LIT8, 228, Opcodes.MUL_DOUBLE, 89, 251, 149, 159, 56, 89, 33, Opcodes.DIV_INT, 244, 154, 36, 73, 127, Opcodes.AND_INT_LIT16, Opcodes.FLOAT_TO_LONG, 248, 180, 234, Opcodes.USHR_LONG_2ADDR, 158, 177, 68, SecExceptionCode.SEC_ERROR_INIT_NO_DATA_FILE, 93, Opcodes.AND_INT_LIT16, 15, 160, 227, 236, 66, Opcodes.DOUBLE_TO_LONG, 153, 185, 202, 167, Opcodes.DIV_INT_2ADDR, 25, Opcodes.REM_INT_LIT8, 232, 96, Opcodes.MUL_INT_LIT16, 231, Opcodes.FLOAT_TO_LONG, Opcodes.XOR_INT_LIT8, 239, 181, 241, 59, 52, Opcodes.SUB_DOUBLE, 25, 49, 232, Opcodes.DIV_INT_LIT16, Opcodes.MUL_LONG_2ADDR, 64, 54, 108, 153, Opcodes.LONG_TO_INT, 63, 96, 103, 82, Opcodes.USHR_INT_2ADDR}};
    private static final int[] FACTOR_SETS = {5, 7, 10, 11, 12, 14, 18, 20, 24, 28, 36, 42, 48, 56, 62, 68};
    private static final int[] LOG = new int[256];
    private static final int MODULO_VALUE = 301;

    static {
        int p = 1;
        for (int i = 0; i < 255; i++) {
            ALOG[i] = p;
            LOG[p] = i;
            p <<= 1;
            if (p >= 256) {
                p ^= 301;
            }
        }
    }

    private ErrorCorrection() {
    }

    public static String encodeECC200(String codewords, SymbolInfo symbolInfo) {
        if (codewords.length() != symbolInfo.getDataCapacity()) {
            throw new IllegalArgumentException("The number of codewords does not match the selected symbol");
        }
        StringBuilder sb = new StringBuilder(symbolInfo.getDataCapacity() + symbolInfo.getErrorCodewords());
        sb.append(codewords);
        int blockCount = symbolInfo.getInterleavedBlockCount();
        if (blockCount == 1) {
            sb.append(createECCBlock(codewords, symbolInfo.getErrorCodewords()));
        } else {
            sb.setLength(sb.capacity());
            int[] dataSizes = new int[blockCount];
            int[] errorSizes = new int[blockCount];
            int[] startPos = new int[blockCount];
            for (int i = 0; i < blockCount; i++) {
                dataSizes[i] = symbolInfo.getDataLengthForInterleavedBlock(i + 1);
                errorSizes[i] = symbolInfo.getErrorLengthForInterleavedBlock(i + 1);
                startPos[i] = 0;
                if (i > 0) {
                    startPos[i] = startPos[i - 1] + dataSizes[i];
                }
            }
            for (int block = 0; block < blockCount; block++) {
                StringBuilder temp = new StringBuilder(dataSizes[block]);
                for (int d = block; d < symbolInfo.getDataCapacity(); d += blockCount) {
                    temp.append(codewords.charAt(d));
                }
                String ecc = createECCBlock(temp.toString(), errorSizes[block]);
                int pos = 0;
                int e = block;
                while (e < errorSizes[block] * blockCount) {
                    sb.setCharAt(symbolInfo.getDataCapacity() + e, ecc.charAt(pos));
                    e += blockCount;
                    pos++;
                }
            }
        }
        return sb.toString();
    }

    private static String createECCBlock(CharSequence codewords, int numECWords) {
        return createECCBlock(codewords, 0, codewords.length(), numECWords);
    }

    private static String createECCBlock(CharSequence codewords, int start, int len, int numECWords) {
        int table = -1;
        int i = 0;
        while (true) {
            if (i >= FACTOR_SETS.length) {
                break;
            } else if (FACTOR_SETS[i] == numECWords) {
                table = i;
                break;
            } else {
                i++;
            }
        }
        if (table < 0) {
            throw new IllegalArgumentException("Illegal number of error correction codewords specified: " + numECWords);
        }
        int[] poly = FACTORS[table];
        char[] ecc = new char[numECWords];
        for (int i2 = 0; i2 < numECWords; i2++) {
            ecc[i2] = 0;
        }
        for (int i3 = start; i3 < start + len; i3++) {
            int m = ecc[numECWords - 1] ^ codewords.charAt(i3);
            for (int k = numECWords - 1; k > 0; k--) {
                if (m == 0 || poly[k] == 0) {
                    ecc[k] = ecc[k - 1];
                } else {
                    ecc[k] = (char) (ecc[k - 1] ^ ALOG[(LOG[m] + LOG[poly[k]]) % 255]);
                }
            }
            if (m == 0 || poly[0] == 0) {
                ecc[0] = 0;
            } else {
                ecc[0] = (char) ALOG[(LOG[m] + LOG[poly[0]]) % 255];
            }
        }
        char[] eccReversed = new char[numECWords];
        for (int i4 = 0; i4 < numECWords; i4++) {
            eccReversed[i4] = ecc[(numECWords - i4) - 1];
        }
        return String.valueOf(eccReversed);
    }
}
