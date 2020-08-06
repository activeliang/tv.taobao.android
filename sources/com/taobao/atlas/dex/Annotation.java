package com.taobao.atlas.dex;

import com.taobao.atlas.dex.Dex;

public final class Annotation implements Comparable<Annotation> {
    private final Dex dex;
    private final EncodedValue encodedAnnotation;
    private final byte visibility;

    public Annotation(Dex dex2, byte visibility2, EncodedValue encodedAnnotation2) {
        this.dex = dex2;
        this.visibility = visibility2;
        this.encodedAnnotation = encodedAnnotation2;
    }

    public byte getVisibility() {
        return this.visibility;
    }

    public EncodedValueReader getReader() {
        return new EncodedValueReader(this.encodedAnnotation, 29);
    }

    public int getTypeIndex() {
        EncodedValueReader reader = getReader();
        reader.readAnnotation();
        return reader.getAnnotationType();
    }

    public void writeTo(Dex.Section out) {
        out.writeByte(this.visibility);
        this.encodedAnnotation.writeTo(out);
    }

    public int compareTo(Annotation other) {
        return this.encodedAnnotation.compareTo(other.encodedAnnotation);
    }

    public String toString() {
        if (this.dex == null) {
            return this.visibility + " " + getTypeIndex();
        }
        return this.visibility + " " + this.dex.typeNames().get(getTypeIndex());
    }
}
