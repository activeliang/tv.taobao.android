package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONAware;
import java.io.IOException;
import java.lang.reflect.Type;

public class JSONAwareSerializer implements ObjectSerializer {
    public static JSONAwareSerializer instance = new JSONAwareSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        serializer.out.write(((JSONAware) object).toJSONString());
    }
}
