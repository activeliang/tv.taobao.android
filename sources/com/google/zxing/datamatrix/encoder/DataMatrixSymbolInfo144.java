package com.google.zxing.datamatrix.encoder;

import com.taobao.atlas.dexmerge.dx.io.Opcodes;

final class DataMatrixSymbolInfo144 extends SymbolInfo {
    DataMatrixSymbolInfo144() {
        super(false, 1558, 620, 22, 22, 36, -1, 62);
    }

    public int getInterleavedBlockCount() {
        return 10;
    }

    public int getDataLengthForInterleavedBlock(int index) {
        return index <= 8 ? Opcodes.SUB_LONG : Opcodes.ADD_LONG;
    }
}
