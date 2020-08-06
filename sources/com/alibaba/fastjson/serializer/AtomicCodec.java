package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

public class AtomicCodec implements ObjectSerializer, ObjectDeserializer {
    public static final AtomicCodec instance = new AtomicCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object instanceof AtomicInteger) {
            out.writeInt(((AtomicInteger) object).get());
        } else if (object instanceof AtomicLong) {
            out.writeLong(((AtomicLong) object).get());
        } else if (object instanceof AtomicBoolean) {
            out.append((CharSequence) ((AtomicBoolean) object).get() ? "true" : "false");
        } else if (object == null) {
            out.writeNull(SerializerFeature.WriteNullListAsEmpty);
        } else if (object instanceof AtomicIntegerArray) {
            AtomicIntegerArray array = (AtomicIntegerArray) object;
            int len = array.length();
            out.write(91);
            for (int i = 0; i < len; i++) {
                int val = array.get(i);
                if (i != 0) {
                    out.write(44);
                }
                out.writeInt(val);
            }
            out.write(93);
        } else {
            AtomicLongArray array2 = (AtomicLongArray) object;
            int len2 = array2.length();
            out.write(91);
            for (int i2 = 0; i2 < len2; i2++) {
                long val2 = array2.get(i2);
                if (i2 != 0) {
                    out.write(44);
                }
                out.writeLong(val2);
            }
            out.write(93);
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        if (parser.lexer.token() == 8) {
            parser.lexer.nextToken(16);
            return null;
        }
        JSONArray array = new JSONArray();
        parser.parseArray((Collection) array);
        if (clazz == AtomicIntegerArray.class) {
            AtomicIntegerArray atomicArray = new AtomicIntegerArray(array.size());
            for (int i = 0; i < array.size(); i++) {
                atomicArray.set(i, array.getInteger(i).intValue());
            }
            return atomicArray;
        }
        AtomicLongArray atomicArray2 = new AtomicLongArray(array.size());
        for (int i2 = 0; i2 < array.size(); i2++) {
            atomicArray2.set(i2, array.getLong(i2).longValue());
        }
        return atomicArray2;
    }

    public int getFastMatchToken() {
        return 14;
    }
}
