package com.taobao.atlas.dex;

public final class ClassDef {
    public static final int NO_INDEX = -1;
    private final int accessFlags;
    private final int annotationsOffset;
    private final Dex buffer;
    private final int classDataOffset;
    private final int interfacesOffset;
    private final int offset;
    private final int sourceFileIndex;
    private final int staticValuesOffset;
    private final int supertypeIndex;
    private final int typeIndex;

    public ClassDef(Dex buffer2, int offset2, int typeIndex2, int accessFlags2, int supertypeIndex2, int interfacesOffset2, int sourceFileIndex2, int annotationsOffset2, int classDataOffset2, int staticValuesOffset2) {
        this.buffer = buffer2;
        this.offset = offset2;
        this.typeIndex = typeIndex2;
        this.accessFlags = accessFlags2;
        this.supertypeIndex = supertypeIndex2;
        this.interfacesOffset = interfacesOffset2;
        this.sourceFileIndex = sourceFileIndex2;
        this.annotationsOffset = annotationsOffset2;
        this.classDataOffset = classDataOffset2;
        this.staticValuesOffset = staticValuesOffset2;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getTypeIndex() {
        return this.typeIndex;
    }

    public int getSupertypeIndex() {
        return this.supertypeIndex;
    }

    public int getInterfacesOffset() {
        return this.interfacesOffset;
    }

    public short[] getInterfaces() {
        return this.buffer.readTypeList(this.interfacesOffset).getTypes();
    }

    public int getAccessFlags() {
        return this.accessFlags;
    }

    public int getSourceFileIndex() {
        return this.sourceFileIndex;
    }

    public int getAnnotationsOffset() {
        return this.annotationsOffset;
    }

    public int getClassDataOffset() {
        return this.classDataOffset;
    }

    public int getStaticValuesOffset() {
        return this.staticValuesOffset;
    }

    public String toString() {
        if (this.buffer == null) {
            return this.typeIndex + " " + this.supertypeIndex;
        }
        StringBuilder result = new StringBuilder();
        result.append(this.buffer.typeNames().get(this.typeIndex));
        if (this.supertypeIndex != -1) {
            result.append(" extends ").append(this.buffer.typeNames().get(this.supertypeIndex));
        }
        return result.toString();
    }
}
