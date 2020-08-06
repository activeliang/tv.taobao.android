package com.taobao.atlas.dexmerge.dx.io.instructions;

import com.taobao.atlas.dexmerge.dx.io.IndexType;

public final class SparseSwitchPayloadDecodedInstruction extends DecodedInstruction {
    private final int[] keys;
    private final int[] targets;

    public SparseSwitchPayloadDecodedInstruction(InstructionCodec format, int opcode, int[] keys2, int[] targets2) {
        super(format, opcode, 0, (IndexType) null, 0, 0);
        if (keys2.length != targets2.length) {
            throw new IllegalArgumentException("keys/targets length mismatch");
        }
        this.keys = keys2;
        this.targets = targets2;
    }

    public int getRegisterCount() {
        return 0;
    }

    public int[] getKeys() {
        return this.keys;
    }

    public int[] getTargets() {
        return this.targets;
    }

    public DecodedInstruction withIndex(int newIndex) {
        throw new UnsupportedOperationException("no index in instruction");
    }
}
