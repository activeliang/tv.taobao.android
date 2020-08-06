package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.Type;

public class PrimitiveArraySerializer implements ObjectSerializer {
    public static PrimitiveArraySerializer instance = new PrimitiveArraySerializer();

    public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullListAsEmpty);
        } else if (object instanceof int[]) {
            int[] array = (int[]) object;
            out.write(91);
            for (int i = 0; i < array.length; i++) {
                if (i != 0) {
                    out.write(44);
                }
                out.writeInt(array[i]);
            }
            out.write(93);
        } else if (object instanceof short[]) {
            short[] array2 = (short[]) object;
            out.write(91);
            for (int i2 = 0; i2 < array2.length; i2++) {
                if (i2 != 0) {
                    out.write(44);
                }
                out.writeInt(array2[i2]);
            }
            out.write(93);
        } else if (object instanceof long[]) {
            long[] array3 = (long[]) object;
            out.write(91);
            for (int i3 = 0; i3 < array3.length; i3++) {
                if (i3 != 0) {
                    out.write(44);
                }
                out.writeLong(array3[i3]);
            }
            out.write(93);
        } else if (object instanceof boolean[]) {
            boolean[] array4 = (boolean[]) object;
            out.write(91);
            for (int i4 = 0; i4 < array4.length; i4++) {
                if (i4 != 0) {
                    out.write(44);
                }
                out.write(array4[i4]);
            }
            out.write(93);
        } else if (object instanceof float[]) {
            float[] array5 = (float[]) object;
            out.write(91);
            for (int i5 = 0; i5 < array5.length; i5++) {
                if (i5 != 0) {
                    out.write(44);
                }
                float item = array5[i5];
                if (Float.isNaN(item)) {
                    out.writeNull();
                } else {
                    out.append((CharSequence) Float.toString(item));
                }
            }
            out.write(93);
        } else if (object instanceof double[]) {
            double[] array6 = (double[]) object;
            out.write(91);
            for (int i6 = 0; i6 < array6.length; i6++) {
                if (i6 != 0) {
                    out.write(44);
                }
                double item2 = array6[i6];
                if (Double.isNaN(item2)) {
                    out.writeNull();
                } else {
                    out.append((CharSequence) Double.toString(item2));
                }
            }
            out.write(93);
        } else if (object instanceof byte[]) {
            out.writeByteArray((byte[]) object);
        } else {
            out.writeString(new String((char[]) object));
        }
    }
}
