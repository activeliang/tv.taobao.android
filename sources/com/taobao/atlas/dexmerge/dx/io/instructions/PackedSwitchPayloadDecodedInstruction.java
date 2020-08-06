package com.taobao.atlas.dexmerge.dx.io.instructions;

import com.taobao.atlas.dexmerge.dx.io.IndexType;

public final class PackedSwitchPayloadDecodedInstruction extends DecodedInstruction {
    private final int firstKey;
    private final int[] targets;

    public PackedSwitchPayloadDecodedInstruction(InstructionCodec format, int opcode, int firstKey2, int[] targets2) {
        super(format, opcode, 0, (IndexType) null, 0, 0);
        this.firstKey = firstKey2;
        this.targets = targets2;
    }

    public int getRegisterCount() {
        return 0;
    }

    public int getFirstKey() {
        return this.firstKey;
    }

    public int[] getTargets() {
        return this.targets;
    }

    public DecodedInstruction withIndex(int newIndex) {
        throw new UnsupportedOperationException("no index in instruction");
    }
}
