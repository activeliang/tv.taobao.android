package android.taobao.atlas.remote;

import java.lang.reflect.Field;

public class Util {
    public static Field findFieldFromInterface(Object instance, String name) throws NoSuchFieldException {
        for (Class<?> clazz : instance.getClass().getInterfaces()) {
            while (clazz != null) {
                try {
                    Field field = clazz.getDeclaredField(name);
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    return field;
                } catch (NoSuchFieldException e) {
                    clazz = clazz.getSuperclass();
                }
            }
        }
        throw new NoSuchFieldException("Field " + name + " not found in " + instance.getClass());
    }
}
