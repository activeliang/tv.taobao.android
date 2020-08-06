package com.taobao.atlas.dexmerge.dx.io.instructions;

import com.taobao.atlas.dexmerge.dx.io.IndexType;

public final class FillArrayDataPayloadDecodedInstruction extends DecodedInstruction {
    private final Object data;
    private final int elementWidth;
    private final int size;

    private FillArrayDataPayloadDecodedInstruction(InstructionCodec format, int opcode, Object data2, int size2, int elementWidth2) {
        super(format, opcode, 0, (IndexType) null, 0, 0);
        this.data = data2;
        this.size = size2;
        this.elementWidth = elementWidth2;
    }

    public FillArrayDataPayloadDecodedInstruction(InstructionCodec format, int opcode, byte[] data2) {
        this(format, opcode, data2, data2.length, 1);
    }

    public FillArrayDataPayloadDecodedInstruction(InstructionCodec format, int opcode, short[] data2) {
        this(format, opcode, data2, data2.length, 2);
    }

    public FillArrayDataPayloadDecodedInstruction(InstructionCodec format, int opcode, int[] data2) {
        this(format, opcode, data2, data2.length, 4);
    }

    public FillArrayDataPayloadDecodedInstruction(InstructionCodec format, int opcode, long[] data2) {
        this(format, opcode, data2, data2.length, 8);
    }

    public int getRegisterCount() {
        return 0;
    }

    public short getElementWidthUnit() {
        return (short) this.elementWidth;
    }

    public int getSize() {
        return this.size;
    }

    public Object getData() {
        return this.data;
    }

    public DecodedInstruction withIndex(int newIndex) {
        throw new UnsupportedOperationException("no index in instruction");
    }
}
