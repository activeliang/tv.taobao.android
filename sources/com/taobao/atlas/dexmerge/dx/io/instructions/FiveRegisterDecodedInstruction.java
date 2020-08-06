package com.taobao.atlas.dexmerge.dx.io.instructions;

import com.taobao.atlas.dexmerge.dx.io.IndexType;

public final class FiveRegisterDecodedInstruction extends DecodedInstruction {
    private final int a;
    private final int b;
    private final int c;
    private final int d;
    private final int e;

    public FiveRegisterDecodedInstruction(InstructionCodec format, int opcode, int index, IndexType indexType, int target, long literal, int a2, int b2, int c2, int d2, int e2) {
        super(format, opcode, index, indexType, target, literal);
        this.a = a2;
        this.b = b2;
        this.c = c2;
        this.d = d2;
        this.e = e2;
    }

    public int getRegisterCount() {
        return 5;
    }

    public int getA() {
        return this.a;
    }

    public int getB() {
        return this.b;
    }

    public int getC() {
        return this.c;
    }

    public int getD() {
        return this.d;
    }

    public int getE() {
        return this.e;
    }

    public DecodedInstruction withIndex(int newIndex) {
        return new FiveRegisterDecodedInstruction(getFormat(), getOpcode(), newIndex, getIndexType(), getTarget(), getLiteral(), this.a, this.b, this.c, this.d, this.e);
    }
}
