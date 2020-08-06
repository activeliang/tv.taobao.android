package com.taobao.atlas.dexmerge.dx.merge;

import com.taobao.atlas.dex.DexException2;
import com.taobao.atlas.dex.DexIndexOverflowException;
import com.taobao.atlas.dexmerge.dx.io.CodeReader;
import com.taobao.atlas.dexmerge.dx.io.instructions.DecodedInstruction;
import com.taobao.atlas.dexmerge.dx.io.instructions.ShortArrayCodeOutput;

final class InstructionTransformer {
    /* access modifiers changed from: private */
    public IndexMap indexMap;
    private int mappedAt;
    /* access modifiers changed from: private */
    public DecodedInstruction[] mappedInstructions;
    private final CodeReader reader = new CodeReader();

    static /* synthetic */ int access$608(InstructionTransformer x0) {
        int i = x0.mappedAt;
        x0.mappedAt = i + 1;
        return i;
    }

    public InstructionTransformer() {
        this.reader.setAllVisitors(new GenericVisitor());
        this.reader.setStringVisitor(new StringVisitor());
        this.reader.setTypeVisitor(new TypeVisitor());
        this.reader.setFieldVisitor(new FieldVisitor());
        this.reader.setMethodVisitor(new MethodVisitor());
    }

    public short[] transform(IndexMap indexMap2, short[] encodedInstructions) throws DexException2 {
        DecodedInstruction[] decodedInstructions = DecodedInstruction.decodeAll(encodedInstructions);
        int size = decodedInstructions.length;
        this.indexMap = indexMap2;
        this.mappedInstructions = new DecodedInstruction[size];
        this.mappedAt = 0;
        this.reader.visitAll(decodedInstructions);
        ShortArrayCodeOutput out = new ShortArrayCodeOutput(size);
        for (DecodedInstruction instruction : this.mappedInstructions) {
            if (instruction != null) {
                instruction.encode(out);
            }
        }
        this.indexMap = null;
        return out.getArray();
    }

    private class GenericVisitor implements CodeReader.Visitor {
        private GenericVisitor() {
        }

        public void visit(DecodedInstruction[] all, DecodedInstruction one) {
            InstructionTransformer.this.mappedInstructions[InstructionTransformer.access$608(InstructionTransformer.this)] = one;
        }
    }

    private class StringVisitor implements CodeReader.Visitor {
        private StringVisitor() {
        }

        public void visit(DecodedInstruction[] all, DecodedInstruction one) {
            int mappedId = InstructionTransformer.this.indexMap.adjustString(one.getIndex());
            InstructionTransformer.jumboCheck(one.getOpcode() == 27, mappedId);
            InstructionTransformer.this.mappedInstructions[InstructionTransformer.access$608(InstructionTransformer.this)] = one.withIndex(mappedId);
        }
    }

    private class FieldVisitor implements CodeReader.Visitor {
        private FieldVisitor() {
        }

        public void visit(DecodedInstruction[] all, DecodedInstruction one) {
            int mappedId = InstructionTransformer.this.indexMap.adjustField(one.getIndex());
            InstructionTransformer.jumboCheck(one.getOpcode() == 27, mappedId);
            InstructionTransformer.this.mappedInstructions[InstructionTransformer.access$608(InstructionTransformer.this)] = one.withIndex(mappedId);
        }
    }

    private class TypeVisitor implements CodeReader.Visitor {
        private TypeVisitor() {
        }

        public void visit(DecodedInstruction[] all, DecodedInstruction one) {
            int mappedId = InstructionTransformer.this.indexMap.adjustType(one.getIndex());
            InstructionTransformer.jumboCheck(one.getOpcode() == 27, mappedId);
            InstructionTransformer.this.mappedInstructions[InstructionTransformer.access$608(InstructionTransformer.this)] = one.withIndex(mappedId);
        }
    }

    private class MethodVisitor implements CodeReader.Visitor {
        private MethodVisitor() {
        }

        public void visit(DecodedInstruction[] all, DecodedInstruction one) {
            int mappedId = InstructionTransformer.this.indexMap.adjustMethod(one.getIndex());
            InstructionTransformer.jumboCheck(one.getOpcode() == 27, mappedId);
            InstructionTransformer.this.mappedInstructions[InstructionTransformer.access$608(InstructionTransformer.this)] = one.withIndex(mappedId);
        }
    }

    /* access modifiers changed from: private */
    public static void jumboCheck(boolean isJumbo, int newIndex) {
        if (!isJumbo && newIndex > 65535) {
            throw new DexIndexOverflowException("Cannot merge new index " + newIndex + " into a non-jumbo instruction!");
        }
    }
}
