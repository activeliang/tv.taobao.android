package com.alibaba.fastjson.serializer;

import android.taobao.windvane.connect.HttpConnector;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

public class JSONLibDataFormatSerializer implements ObjectSerializer {
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        if (object == null) {
            serializer.out.writeNull();
            return;
        }
        Date date = (Date) object;
        JSONObject json = new JSONObject();
        json.put(HttpConnector.DATE, (Object) Integer.valueOf(date.getDate()));
        json.put("day", (Object) Integer.valueOf(date.getDay()));
        json.put("hours", (Object) Integer.valueOf(date.getHours()));
        json.put("minutes", (Object) Integer.valueOf(date.getMinutes()));
        json.put("month", (Object) Integer.valueOf(date.getMonth()));
        json.put("seconds", (Object) Integer.valueOf(date.getSeconds()));
        json.put("time", (Object) Long.valueOf(date.getTime()));
        json.put("timezoneOffset", (Object) Integer.valueOf(date.getTimezoneOffset()));
        json.put("year", (Object) Integer.valueOf(date.getYear()));
        serializer.write((Object) json);
    }
}
