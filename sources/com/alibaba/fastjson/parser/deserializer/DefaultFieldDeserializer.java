package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.ParseContext;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class DefaultFieldDeserializer extends FieldDeserializer {
    protected ObjectDeserializer fieldValueDeserilizer;

    public DefaultFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        super(clazz, fieldInfo);
    }

    public ObjectDeserializer getFieldValueDeserilizer(ParserConfig config) {
        if (this.fieldValueDeserilizer == null) {
            JSONField annotation = this.fieldInfo.getAnnotation();
            if (annotation == null || annotation.deserializeUsing() == Void.class) {
                this.fieldValueDeserilizer = config.getDeserializer(this.fieldInfo.fieldClass, this.fieldInfo.fieldType);
            } else {
                try {
                    this.fieldValueDeserilizer = (ObjectDeserializer) annotation.deserializeUsing().newInstance();
                } catch (Exception ex) {
                    throw new JSONException("create deserializeUsing ObjectDeserializer error", ex);
                }
            }
        }
        return this.fieldValueDeserilizer;
    }

    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        Object value;
        if (this.fieldValueDeserilizer == null) {
            getFieldValueDeserilizer(parser.getConfig());
        }
        Type fieldType = this.fieldInfo.fieldType;
        if (objectType instanceof ParameterizedType) {
            ParseContext objContext = parser.getContext();
            if (objContext != null) {
                objContext.type = objectType;
            }
            fieldType = FieldInfo.getFieldType(this.clazz, objectType, fieldType);
            this.fieldValueDeserilizer = parser.getConfig().getDeserializer(fieldType);
        }
        if (this.fieldValueDeserilizer instanceof JavaBeanDeserializer) {
            value = ((JavaBeanDeserializer) this.fieldValueDeserilizer).deserialze(parser, fieldType, this.fieldInfo.name, this.fieldInfo.parserFeatures);
        } else if (this.fieldInfo.format == null || !(this.fieldValueDeserilizer instanceof ContextObjectDeserializer)) {
            value = this.fieldValueDeserilizer.deserialze(parser, fieldType, this.fieldInfo.name);
        } else {
            value = ((ContextObjectDeserializer) this.fieldValueDeserilizer).deserialze(parser, fieldType, this.fieldInfo.name, this.fieldInfo.format, this.fieldInfo.parserFeatures);
        }
        if (parser.getResolveStatus() == 1) {
            DefaultJSONParser.ResolveTask task = parser.getLastResolveTask();
            task.fieldDeserializer = this;
            task.ownerContext = parser.getContext();
            parser.setResolveStatus(0);
        } else if (object == null) {
            fieldValues.put(this.fieldInfo.name, value);
        } else {
            setValue(object, value);
        }
    }

    public int getFastMatchToken() {
        if (this.fieldValueDeserilizer != null) {
            return this.fieldValueDeserilizer.getFastMatchToken();
        }
        return 2;
    }
}
