package com.taobao.atlas.dexmerge.dx.io.instructions;

import com.taobao.atlas.dexmerge.dx.io.IndexType;

public final class ThreeRegisterDecodedInstruction extends DecodedInstruction {
    private final int a;
    private final int b;
    private final int c;

    public ThreeRegisterDecodedInstruction(InstructionCodec format, int opcode, int index, IndexType indexType, int target, long literal, int a2, int b2, int c2) {
        super(format, opcode, index, indexType, target, literal);
        this.a = a2;
        this.b = b2;
        this.c = c2;
    }

    public int getRegisterCount() {
        return 3;
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

    public DecodedInstruction withIndex(int newIndex) {
        return new ThreeRegisterDecodedInstruction(getFormat(), getOpcode(), newIndex, getIndexType(), getTarget(), getLiteral(), this.a, this.b, this.c);
    }
}
