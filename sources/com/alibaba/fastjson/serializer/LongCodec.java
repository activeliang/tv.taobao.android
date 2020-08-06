package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicLong;

public class LongCodec implements ObjectSerializer, ObjectDeserializer {
    public static LongCodec instance = new LongCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullNumberAsZero);
            return;
        }
        long value = ((Long) object).longValue();
        out.writeLong(value);
        if (out.isEnabled(SerializerFeature.WriteClassName) && value <= 2147483647L && value >= -2147483648L && fieldType != Long.class) {
            out.write(76);
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        Long longObject;
        JSONLexer lexer = parser.lexer;
        if (lexer.token() == 2) {
            long longValue = lexer.longValue();
            lexer.nextToken(16);
            longObject = Long.valueOf(longValue);
        } else {
            Object value = parser.parse();
            if (value == null) {
                return null;
            }
            longObject = TypeUtils.castToLong(value);
        }
        if (clazz == AtomicLong.class) {
            return new AtomicLong(longObject.longValue());
        }
        return longObject;
    }

    public int getFastMatchToken() {
        return 2;
    }
}
