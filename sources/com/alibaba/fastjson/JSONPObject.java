package com.alibaba.fastjson;

import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JSONPObject implements JSONSerializable {
    private String function;
    private final List<Object> parameters = new ArrayList();

    public JSONPObject() {
    }

    public JSONPObject(String function2) {
        this.function = function2;
    }

    public String getFunction() {
        return this.function;
    }

    public void setFunction(String function2) {
        this.function = function2;
    }

    public List<Object> getParameters() {
        return this.parameters;
    }

    public void addParameter(Object parameter) {
        this.parameters.add(parameter);
    }

    public String toJSONString() {
        return toString();
    }

    public void write(JSONSerializer serializer, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter writer = serializer.out;
        writer.write(this.function);
        writer.write(40);
        for (int i = 0; i < this.parameters.size(); i++) {
            if (i != 0) {
                writer.write(44);
            }
            serializer.write(this.parameters.get(i));
        }
        writer.write(41);
    }

    public String toString() {
        return JSON.toJSONString(this);
    }
}
