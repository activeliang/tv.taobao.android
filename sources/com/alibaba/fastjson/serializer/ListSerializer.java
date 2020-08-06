package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.util.TypeUtils;
import com.bftv.fui.constantplugin.Constant;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public final class ListSerializer implements ObjectSerializer {
    public static final ListSerializer instance = new ListSerializer();

    public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        ObjectSerializer itemSerializer;
        ObjectSerializer itemSerializer2;
        boolean writeClassName = serializer.out.isEnabled(SerializerFeature.WriteClassName);
        SerializeWriter out = serializer.out;
        Type elementType = null;
        if (writeClassName) {
            elementType = TypeUtils.getCollectionItemType(fieldType);
        }
        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullListAsEmpty);
            return;
        }
        List<?> list = (List) object;
        if (list.size() == 0) {
            out.append((CharSequence) "[]");
            return;
        }
        SerialContext context = serializer.context;
        serializer.setContext(context, object, fieldName, 0);
        try {
            if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                out.append('[');
                serializer.incrementIndent();
                int i = 0;
                for (Object item : list) {
                    if (i != 0) {
                        out.append(',');
                    }
                    serializer.println();
                    if (item == null) {
                        serializer.out.writeNull();
                    } else if (serializer.containsReference(item)) {
                        serializer.writeReference(item);
                    } else {
                        ObjectSerializer itemSerializer3 = serializer.getObjectWriter(item.getClass());
                        try {
                            serializer.context = new SerialContext(context, object, fieldName, 0, 0);
                            itemSerializer3.write(serializer, item, Integer.valueOf(i), elementType, 0);
                            ObjectSerializer objectSerializer = itemSerializer3;
                        } catch (Throwable th) {
                            th = th;
                            ObjectSerializer objectSerializer2 = itemSerializer;
                            serializer.context = context;
                            throw th;
                        }
                    }
                    i++;
                }
                serializer.decrementIdent();
                serializer.println();
                out.append(']');
                serializer.context = context;
                return;
            }
            out.append('[');
            int i2 = 0;
            int size = list.size();
            itemSerializer = null;
            while (i2 < size) {
                Object item2 = list.get(i2);
                if (i2 != 0) {
                    out.append(',');
                }
                if (item2 == null) {
                    out.append((CharSequence) Constant.NULL);
                    itemSerializer2 = itemSerializer;
                } else {
                    Class<?> clazz = item2.getClass();
                    if (clazz == Integer.class) {
                        out.writeInt(((Integer) item2).intValue());
                        itemSerializer2 = itemSerializer;
                    } else if (clazz == Long.class) {
                        long val = ((Long) item2).longValue();
                        if (writeClassName) {
                            out.writeLong(val);
                            out.write(76);
                        } else {
                            out.writeLong(val);
                        }
                        itemSerializer2 = itemSerializer;
                    } else {
                        if (!out.disableCircularReferenceDetect) {
                            serializer.context = new SerialContext(context, object, fieldName, 0, 0);
                        }
                        if (serializer.containsReference(item2)) {
                            serializer.writeReference(item2);
                            itemSerializer2 = itemSerializer;
                        } else {
                            itemSerializer2 = serializer.getObjectWriter(item2.getClass());
                            itemSerializer2.write(serializer, item2, Integer.valueOf(i2), elementType, 0);
                        }
                    }
                }
                i2++;
                itemSerializer = itemSerializer2;
            }
            out.append(']');
            serializer.context = context;
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
