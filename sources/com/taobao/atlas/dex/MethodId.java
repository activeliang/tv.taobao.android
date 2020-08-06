package com.taobao.atlas.dex;

import com.taobao.atlas.dex.Dex;
import com.taobao.atlas.dex.util.Unsigned;

public final class MethodId implements Comparable<MethodId> {
    private final int declaringClassIndex;
    private final Dex dex;
    private final int nameIndex;
    private final int protoIndex;

    public MethodId(Dex dex2, int declaringClassIndex2, int protoIndex2, int nameIndex2) {
        this.dex = dex2;
        this.declaringClassIndex = declaringClassIndex2;
        this.protoIndex = protoIndex2;
        this.nameIndex = nameIndex2;
    }

    public int getDeclaringClassIndex() {
        return this.declaringClassIndex;
    }

    public int getProtoIndex() {
        return this.protoIndex;
    }

    public int getNameIndex() {
        return this.nameIndex;
    }

    public int compareTo(MethodId other) {
        if (this.declaringClassIndex != other.declaringClassIndex) {
            return Unsigned.compare(this.declaringClassIndex, other.declaringClassIndex);
        }
        if (this.nameIndex != other.nameIndex) {
            return Unsigned.compare(this.nameIndex, other.nameIndex);
        }
        return Unsigned.compare(this.protoIndex, other.protoIndex);
    }

    public void writeTo(Dex.Section out) {
        out.writeUnsignedShort(this.declaringClassIndex);
        out.writeUnsignedShort(this.protoIndex);
        out.writeInt(this.nameIndex);
    }

    public String toString() {
        if (this.dex == null) {
            return this.declaringClassIndex + " " + this.protoIndex + " " + this.nameIndex;
        }
        return this.dex.typeNames().get(this.declaringClassIndex) + "." + this.dex.strings().get(this.nameIndex) + this.dex.readTypeList(this.dex.protoIds().get(this.protoIndex).getParametersOffset());
    }
}
