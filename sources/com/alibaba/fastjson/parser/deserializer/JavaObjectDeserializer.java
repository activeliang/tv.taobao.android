package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JavaObjectDeserializer implements ObjectDeserializer {
    public static final JavaObjectDeserializer instance = new JavaObjectDeserializer();

    /* JADX WARNING: type inference failed for: r0v4, types: [T, java.lang.Object[]] */
    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            if (componentType instanceof TypeVariable) {
                componentType = ((TypeVariable) componentType).getBounds()[0];
            }
            List<Object> list = new ArrayList<>();
            parser.parseArray(componentType, (Collection) list);
            if (!(componentType instanceof Class)) {
                return list.toArray();
            }
            ? r0 = (Object[]) Array.newInstance((Class) componentType, list.size());
            list.toArray(r0);
            return r0;
        } else if (!(type instanceof Class) || type == Object.class || type == Serializable.class) {
            return parser.parse(fieldName);
        } else {
            return parser.parseObject(type);
        }
    }

    public int getFastMatchToken() {
        return 12;
    }
}
