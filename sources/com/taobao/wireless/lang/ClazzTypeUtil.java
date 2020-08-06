package com.taobao.wireless.lang;

import com.taobao.detail.domain.base.PriceUnit;
import com.taobao.detail.domain.base.ServiceUnit;
import com.taobao.detail.domain.base.TipDO;
import com.taobao.detail.domain.base.Unit;
import java.util.HashSet;
import java.util.Set;

public class ClazzTypeUtil {
    private static final Set<Class<?>> BASE_TYPES = getBaseTypes();

    public static boolean isBaseType(Class<?> clazz) {
        return BASE_TYPES.contains(clazz);
    }

    private static Set<Class<?>> getBaseTypes() {
        Set<Class<?>> ret = new HashSet<>();
        ret.add(Boolean.class);
        ret.add(Boolean.TYPE);
        ret.add(Character.class);
        ret.add(Character.TYPE);
        ret.add(Byte.class);
        ret.add(Byte.TYPE);
        ret.add(Short.class);
        ret.add(Short.TYPE);
        ret.add(Integer.class);
        ret.add(Integer.TYPE);
        ret.add(Long.class);
        ret.add(Long.TYPE);
        ret.add(Float.class);
        ret.add(Float.TYPE);
        ret.add(Double.class);
        ret.add(Double.TYPE);
        ret.add(Void.class);
        ret.add(String.class);
        ret.add(TipDO.class);
        ret.add(Unit.class);
        ret.add(PriceUnit.class);
        ret.add(ServiceUnit.class);
        return ret;
    }
}
