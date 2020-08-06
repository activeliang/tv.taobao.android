package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class CollectionCodec implements ObjectSerializer, ObjectDeserializer {
    public static final CollectionCodec instance = new CollectionCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullListAsEmpty);
            return;
        }
        Type elementType = null;
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            elementType = TypeUtils.getCollectionItemType(fieldType);
        }
        Collection<?> collection = (Collection) object;
        SerialContext context = serializer.context;
        serializer.setContext(context, object, fieldName, 0);
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            if (HashSet.class == collection.getClass()) {
                out.append((CharSequence) "Set");
            } else if (TreeSet.class == collection.getClass()) {
                out.append((CharSequence) "TreeSet");
            }
        }
        try {
            out.append('[');
            int i = 0;
            for (Object item : collection) {
                try {
                    int i2 = i + 1;
                    if (i != 0) {
                        out.append(',');
                    }
                    if (item == null) {
                        out.writeNull();
                        i = i2;
                    } else {
                        Class<?> clazz = item.getClass();
                        if (clazz == Integer.class) {
                            out.writeInt(((Integer) item).intValue());
                            i = i2;
                        } else if (clazz == Long.class) {
                            out.writeLong(((Long) item).longValue());
                            if (out.isEnabled(SerializerFeature.WriteClassName)) {
                                out.write(76);
                                i = i2;
                            } else {
                                i = i2;
                            }
                        } else {
                            serializer.getObjectWriter(clazz).write(serializer, item, Integer.valueOf(i2 - 1), elementType, 0);
                            i = i2;
                        }
                    }
                } catch (Throwable th) {
                    th = th;
                    int i3 = i;
                    serializer.context = context;
                    throw th;
                }
            }
            out.append(']');
            serializer.context = context;
        } catch (Throwable th2) {
            th = th2;
            serializer.context = context;
            throw th;
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (parser.lexer.token() == 8) {
            parser.lexer.nextToken(16);
            return null;
        } else if (type == JSONArray.class) {
            JSONArray array = new JSONArray();
            parser.parseArray((Collection) array);
            return array;
        } else {
            Collection list = TypeUtils.createCollection(type);
            parser.parseArray(TypeUtils.getCollectionItemType(type), list, fieldName);
            return list;
        }
    }

    public int getFastMatchToken() {
        return 14;
    }
}
