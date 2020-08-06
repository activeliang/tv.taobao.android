package com.taobao.atlas.dexmerge.dx.io.instructions;

import com.taobao.atlas.dexmerge.dx.io.IndexType;

public final class OneRegisterDecodedInstruction extends DecodedInstruction {
    private final int a;

    public OneRegisterDecodedInstruction(InstructionCodec format, int opcode, int index, IndexType indexType, int target, long literal, int a2) {
        super(format, opcode, index, indexType, target, literal);
        this.a = a2;
    }

    public int getRegisterCount() {
        return 1;
    }

    public int getA() {
        return this.a;
    }

    public DecodedInstruction withIndex(int newIndex) {
        return new OneRegisterDecodedInstruction(getFormat(), getOpcode(), newIndex, getIndexType(), getTarget(), getLiteral(), this.a);
    }
}
