package com.alibaba.fastjson.support.spring;

public class MappingFastJsonValue {
    private String jsonpFunction;
    private Object value;

    public MappingFastJsonValue(Object value2) {
        this.value = value2;
    }

    public void setValue(Object value2) {
        this.value = value2;
    }

    public Object getValue() {
        return this.value;
    }

    public void setJsonpFunction(String functionName) {
        this.jsonpFunction = functionName;
    }

    public String getJsonpFunction() {
        return this.jsonpFunction;
    }
}
