package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import java.io.IOException;
import java.lang.reflect.Type;

public class StringCodec implements ObjectSerializer, ObjectDeserializer {
    public static StringCodec instance = new StringCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        write(serializer, (String) object);
    }

    public void write(JSONSerializer serializer, String value) {
        SerializeWriter out = serializer.out;
        if (value == null) {
            out.writeNull(SerializerFeature.WriteNullStringAsEmpty);
        } else {
            out.writeString(value);
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type clazz, Object fieldName) {
        if (clazz == StringBuffer.class) {
            JSONLexer lexer = parser.lexer;
            if (lexer.token() == 4) {
                String val = lexer.stringVal();
                lexer.nextToken(16);
                return new StringBuffer(val);
            }
            Object value = parser.parse();
            if (value != null) {
                return new StringBuffer(value.toString());
            }
            return null;
        } else if (clazz != StringBuilder.class) {
            return deserialze(parser);
        } else {
            JSONLexer lexer2 = parser.lexer;
            if (lexer2.token() == 4) {
                String val2 = lexer2.stringVal();
                lexer2.nextToken(16);
                return new StringBuilder(val2);
            }
            Object value2 = parser.parse();
            if (value2 != null) {
                return new StringBuilder(value2.toString());
            }
            return null;
        }
    }

    public static <T> T deserialze(DefaultJSONParser parser) {
        JSONLexer lexer = parser.getLexer();
        if (lexer.token() == 4) {
            String val = lexer.stringVal();
            lexer.nextToken(16);
            return val;
        } else if (lexer.token() == 2) {
            String val2 = lexer.numberString();
            lexer.nextToken(16);
            return val2;
        } else {
            Object value = parser.parse();
            if (value == null) {
                return null;
            }
            return value.toString();
        }
    }

    public int getFastMatchToken() {
        return 4;
    }
}
