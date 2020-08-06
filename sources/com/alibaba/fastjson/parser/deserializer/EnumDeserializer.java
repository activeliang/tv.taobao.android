package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import java.lang.reflect.Type;
import java.util.Arrays;

public class EnumDeserializer implements ObjectDeserializer {
    private final Class<?> enumClass;
    protected long[] enumNameHashCodes = new long[this.ordinalEnums.length];
    protected final Enum[] enums;
    protected final Enum[] ordinalEnums;

    public EnumDeserializer(Class<?> enumClass2) {
        this.enumClass = enumClass2;
        this.ordinalEnums = (Enum[]) enumClass2.getEnumConstants();
        long[] enumNameHashCodes2 = new long[this.ordinalEnums.length];
        for (int i = 0; i < this.ordinalEnums.length; i++) {
            String name = this.ordinalEnums[i].name();
            long hash = -2128831035;
            for (int j = 0; j < name.length(); j++) {
                hash = (hash ^ ((long) name.charAt(j))) * 16777619;
            }
            enumNameHashCodes2[i] = hash;
            this.enumNameHashCodes[i] = hash;
        }
        Arrays.sort(this.enumNameHashCodes);
        this.enums = new Enum[this.ordinalEnums.length];
        for (int i2 = 0; i2 < this.enumNameHashCodes.length; i2++) {
            int j2 = 0;
            while (true) {
                if (j2 >= enumNameHashCodes2.length) {
                    break;
                } else if (this.enumNameHashCodes[i2] == enumNameHashCodes2[j2]) {
                    this.enums[i2] = this.ordinalEnums[j2];
                    break;
                } else {
                    j2++;
                }
            }
        }
    }

    public Enum getEnumByHashCode(long hashCode) {
        int enumIndex;
        if (this.enums != null && (enumIndex = Arrays.binarySearch(this.enumNameHashCodes, hashCode)) >= 0) {
            return this.enums[enumIndex];
        }
        return null;
    }

    public Enum<?> valueOf(int ordinal) {
        return this.ordinalEnums[ordinal];
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        try {
            JSONLexer lexer = parser.lexer;
            int token = lexer.token();
            if (token == 2) {
                int intValue = lexer.intValue();
                lexer.nextToken(16);
                if (intValue >= 0 && intValue <= this.ordinalEnums.length) {
                    return this.ordinalEnums[intValue];
                }
                throw new JSONException("parse enum " + this.enumClass.getName() + " error, value : " + intValue);
            } else if (token == 4) {
                String strVal = lexer.stringVal();
                lexer.nextToken(16);
                if (strVal.length() != 0) {
                    return Enum.valueOf(this.enumClass, strVal);
                }
                return null;
            } else if (token == 8) {
                lexer.nextToken(16);
                return null;
            } else {
                throw new JSONException("parse enum " + this.enumClass.getName() + " error, value : " + parser.parse());
            }
        } catch (JSONException e) {
            throw e;
        } catch (Exception e2) {
            throw new JSONException(e2.getMessage(), e2);
        }
    }

    public int getFastMatchToken() {
        return 2;
    }
}
