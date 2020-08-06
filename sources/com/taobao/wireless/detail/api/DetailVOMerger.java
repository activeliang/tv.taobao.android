package com.taobao.wireless.detail.api;

import com.taobao.detail.clientDomain.TBDetailResultVO;
import com.taobao.wireless.lang.ClazzTypeUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

public class DetailVOMerger {
    public static TBDetailResultVO merge(TBDetailResultVO dest, TBDetailResultVO orig) {
        try {
            return (TBDetailResultVO) copyProperties(dest, orig);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object copyProperties(Object dest, Object orig) throws IllegalAccessException, InvocationTargetException {
        if (orig == null) {
            return dest;
        }
        if (dest == null) {
            return orig;
        }
        if (orig instanceof Map) {
            for (Map.Entry entry : ((Map) orig).entrySet()) {
                Object name = entry.getKey();
                ((Map) dest).put(name, copyProperties(((Map) dest).get(name), ((Map) orig).get(name)));
            }
            return dest;
        } else if (orig instanceof Collection) {
            Collection collection = (Collection) orig;
            if (collection == null || collection.isEmpty()) {
                return dest;
            }
            return collection;
        } else if (ClazzTypeUtil.isBaseType(orig.getClass())) {
            return orig;
        } else {
            Field[] fields = orig.getClass().getFields();
            if (fields == null || fields.length <= 0) {
                return dest;
            }
            for (Field field : fields) {
                field.set(dest, copyProperties(field.get(dest), field.get(orig)));
            }
            return dest;
        }
    }
}
