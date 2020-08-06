package com.taobao.atlas.dex;

public final class Code {
    private final CatchHandler[] catchHandlers;
    private final int debugInfoOffset;
    private final int insSize;
    private final short[] instructions;
    private final int outsSize;
    private final int registersSize;
    private final Try[] tries;

    public Code(int registersSize2, int insSize2, int outsSize2, int debugInfoOffset2, short[] instructions2, Try[] tries2, CatchHandler[] catchHandlers2) {
        this.registersSize = registersSize2;
        this.insSize = insSize2;
        this.outsSize = outsSize2;
        this.debugInfoOffset = debugInfoOffset2;
        this.instructions = instructions2;
        this.tries = tries2;
        this.catchHandlers = catchHandlers2;
    }

    public int getRegistersSize() {
        return this.registersSize;
    }

    public int getInsSize() {
        return this.insSize;
    }

    public int getOutsSize() {
        return this.outsSize;
    }

    public int getDebugInfoOffset() {
        return this.debugInfoOffset;
    }

    public short[] getInstructions() {
        return this.instructions;
    }

    public Try[] getTries() {
        return this.tries;
    }

    public CatchHandler[] getCatchHandlers() {
        return this.catchHandlers;
    }

    public static class Try {
        final int catchHandlerIndex;
        final int instructionCount;
        final int startAddress;

        Try(int startAddress2, int instructionCount2, int catchHandlerIndex2) {
            this.startAddress = startAddress2;
            this.instructionCount = instructionCount2;
            this.catchHandlerIndex = catchHandlerIndex2;
        }

        public int getStartAddress() {
            return this.startAddress;
        }

        public int getInstructionCount() {
            return this.instructionCount;
        }

        public int getCatchHandlerIndex() {
            return this.catchHandlerIndex;
        }
    }

    public static class CatchHandler {
        final int[] addresses;
        final int catchAllAddress;
        final int offset;
        final int[] typeIndexes;

        public CatchHandler(int[] typeIndexes2, int[] addresses2, int catchAllAddress2, int offset2) {
            this.typeIndexes = typeIndexes2;
            this.addresses = addresses2;
            this.catchAllAddress = catchAllAddress2;
            this.offset = offset2;
        }

        public int[] getTypeIndexes() {
            return this.typeIndexes;
        }

        public int[] getAddresses() {
            return this.addresses;
        }

        public int getCatchAllAddress() {
            return this.catchAllAddress;
        }

        public int getOffset() {
            return this.offset;
        }
    }
}
