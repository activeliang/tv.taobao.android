package com.taobao.atlas.dex;

import com.taobao.atlas.dex.Dex;
import com.taobao.atlas.dex.util.Unsigned;

public final class ProtoId implements Comparable<ProtoId> {
    private final Dex dex;
    private final int parametersOffset;
    private final int returnTypeIndex;
    private final int shortyIndex;

    public ProtoId(Dex dex2, int shortyIndex2, int returnTypeIndex2, int parametersOffset2) {
        this.dex = dex2;
        this.shortyIndex = shortyIndex2;
        this.returnTypeIndex = returnTypeIndex2;
        this.parametersOffset = parametersOffset2;
    }

    public int compareTo(ProtoId other) {
        if (this.returnTypeIndex != other.returnTypeIndex) {
            return Unsigned.compare(this.returnTypeIndex, other.returnTypeIndex);
        }
        return Unsigned.compare(this.parametersOffset, other.parametersOffset);
    }

    public int getShortyIndex() {
        return this.shortyIndex;
    }

    public int getReturnTypeIndex() {
        return this.returnTypeIndex;
    }

    public int getParametersOffset() {
        return this.parametersOffset;
    }

    public void writeTo(Dex.Section out) {
        out.writeInt(this.shortyIndex);
        out.writeInt(this.returnTypeIndex);
        out.writeInt(this.parametersOffset);
    }

    public String toString() {
        if (this.dex == null) {
            return this.shortyIndex + " " + this.returnTypeIndex + " " + this.parametersOffset;
        }
        return this.dex.strings().get(this.shortyIndex) + ": " + this.dex.typeNames().get(this.returnTypeIndex) + " " + this.dex.readTypeList(this.parametersOffset);
    }
}
