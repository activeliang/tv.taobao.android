package com.taobao.atlas.dexmerge.dx.io.instructions;

import com.taobao.atlas.dexmerge.dx.io.IndexType;

public final class TwoRegisterDecodedInstruction extends DecodedInstruction {
    private final int a;
    private final int b;

    public TwoRegisterDecodedInstruction(InstructionCodec format, int opcode, int index, IndexType indexType, int target, long literal, int a2, int b2) {
        super(format, opcode, index, indexType, target, literal);
        this.a = a2;
        this.b = b2;
    }

    public int getRegisterCount() {
        return 2;
    }

    public int getA() {
        return this.a;
    }

    public int getB() {
        return this.b;
    }

    public DecodedInstruction withIndex(int newIndex) {
        return new TwoRegisterDecodedInstruction(getFormat(), getOpcode(), newIndex, getIndexType(), getTarget(), getLiteral(), this.a, this.b);
    }
}
