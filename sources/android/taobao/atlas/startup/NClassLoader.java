package android.taobao.atlas.startup;

import android.content.Context;
import dalvik.system.PathClassLoader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class NClassLoader extends PathClassLoader {
    public NClassLoader(String dexPath, String libraryPath, ClassLoader parent) {
        super(dexPath, libraryPath, parent);
    }

    public NClassLoader(String dexPath, ClassLoader parent) {
        super(dexPath, parent);
    }

    public static void replacePathClassLoader(Context base, ClassLoader original, NClassLoader target) throws Exception {
        NClassLoader loader = target;
        Field pathListField = findField(original, "pathList");
        pathListField.setAccessible(true);
        Object originPathListObject = pathListField.get(original);
        findField(originPathListObject, "definingContext").set(originPathListObject, loader);
        findField(loader, "pathList").set(loader, originPathListObject);
        Field mPackageInfoField = base.getClass().getDeclaredField("mPackageInfo");
        mPackageInfoField.setAccessible(true);
        Object loadedApk = mPackageInfoField.get(base);
        Field classloaderField = loadedApk.getClass().getDeclaredField("mClassLoader");
        classloaderField.setAccessible(true);
        classloaderField.set(loadedApk, loader);
        Thread.currentThread().setContextClassLoader(loader);
    }

    public static Field findField(Object instance, String name) throws NoSuchFieldException {
        Class cls = instance.getClass();
        while (cls != null) {
            try {
                Field field = cls.getDeclaredField(name);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field;
            } catch (NoSuchFieldException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field " + name + " not found in " + instance.getClass());
    }

    public static Method findMethod(Object instance, String name, Class<?>... parameterTypes) throws NoSuchMethodException {
        Class cls = instance.getClass();
        while (cls != null) {
            try {
                Method method = cls.getDeclaredMethod(name, parameterTypes);
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method;
            } catch (NoSuchMethodException e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchMethodException("Method " + name + " with parameters " + Arrays.asList(parameterTypes) + " not found in " + instance.getClass());
    }
}
