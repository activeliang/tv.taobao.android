package com.taobao.atlas.dexmerge.dx.io.instructions;

import com.taobao.atlas.dexmerge.dx.io.IndexType;

public final class RegisterRangeDecodedInstruction extends DecodedInstruction {
    private final int a;
    private final int registerCount;

    public RegisterRangeDecodedInstruction(InstructionCodec format, int opcode, int index, IndexType indexType, int target, long literal, int a2, int registerCount2) {
        super(format, opcode, index, indexType, target, literal);
        this.a = a2;
        this.registerCount = registerCount2;
    }

    public int getRegisterCount() {
        return this.registerCount;
    }

    public int getA() {
        return this.a;
    }

    public DecodedInstruction withIndex(int newIndex) {
        return new RegisterRangeDecodedInstruction(getFormat(), getOpcode(), newIndex, getIndexType(), getTarget(), getLiteral(), this.a, this.registerCount);
    }
}
