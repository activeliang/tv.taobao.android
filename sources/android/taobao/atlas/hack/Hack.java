package android.taobao.atlas.hack;

import android.taobao.atlas.hack.Interception;
import android.taobao.atlas.runtime.DelegateClassLoader;
import android.util.Log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Hack {
    private static AssertionFailureHandler sFailureHandler;

    public interface AssertionFailureHandler {
        boolean onAssertionFailure(HackDeclaration.HackAssertionException hackAssertionException);
    }

    public static abstract class HackDeclaration {

        public static class HackAssertionException extends Throwable {
            private static final long serialVersionUID = 1;
            private Class<?> mHackedClass;
            private String mHackedFieldName;
            private String mHackedMethodName;

            public HackAssertionException(String e) {
                super(e);
            }

            public HackAssertionException(Exception e) {
                super(e);
            }

            public String toString() {
                return getCause() != null ? getClass().getName() + ": " + getCause() : super.toString();
            }

            public Class<?> getHackedClass() {
                return this.mHackedClass;
            }

            public void setHackedClass(Class<?> mHackedClass2) {
                this.mHackedClass = mHackedClass2;
            }

            public String getHackedMethodName() {
                return this.mHackedMethodName;
            }

            public void setHackedMethodName(String methodName) {
                this.mHackedMethodName = methodName;
            }

            public String getHackedFieldName() {
                return this.mHackedFieldName;
            }

            public void setHackedFieldName(String fieldName) {
                this.mHackedFieldName = fieldName;
            }
        }
    }

    public static class HackedField<C, T> {
        private final Field mField;

        public <T2> HackedField<C, T2> ofGenericType(Class<?> type) throws HackDeclaration.HackAssertionException {
            if (this.mField != null && !type.isAssignableFrom(this.mField.getType())) {
                Hack.fail(new HackDeclaration.HackAssertionException((Exception) new ClassCastException(this.mField + " is not of type " + type)));
            }
            return this;
        }

        public <T2> HackedField<C, T2> ofType(Class<T2> type) throws HackDeclaration.HackAssertionException {
            if (this.mField != null && !type.isAssignableFrom(this.mField.getType())) {
                Hack.fail(new HackDeclaration.HackAssertionException((Exception) new ClassCastException(this.mField + " is not of type " + type)));
            }
            return this;
        }

        public HackedField<C, T> ofType(String type_name) throws HackDeclaration.HackAssertionException {
            try {
                return ofType(Class.forName(type_name));
            } catch (ClassNotFoundException e) {
                Hack.fail(new HackDeclaration.HackAssertionException((Exception) e));
                return this;
            }
        }

        public T get(C instance) {
            try {
                return this.mField.get(instance);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void set(C instance, Object value) {
            try {
                this.mField.set(instance, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                if (value instanceof DelegateClassLoader) {
                    throw new RuntimeException("set DelegateClassLoader fail", e);
                }
            }
        }

        public void hijack(C instance, Interception.InterceptionHandler<?> handler) {
            Object delegatee = get(instance);
            if (delegatee == null) {
                throw new IllegalStateException("Cannot hijack null");
            }
            set(instance, Interception.proxy(delegatee, handler, delegatee.getClass().getInterfaces()));
        }

        HackedField(Class<C> clazz, String name, int modifiers) throws HackDeclaration.HackAssertionException {
            Field field = null;
            if (clazz == null) {
                this.mField = field;
                return;
            }
            try {
                field = clazz.getDeclaredField(name);
                if (modifiers > 0 && (field.getModifiers() & modifiers) != modifiers) {
                    Hack.fail(new HackDeclaration.HackAssertionException(field + " does not match modifiers: " + modifiers));
                }
                field.setAccessible(true);
            } catch (NoSuchFieldException e) {
                HackDeclaration.HackAssertionException hae = new HackDeclaration.HackAssertionException((Exception) e);
                hae.setHackedClass(clazz);
                hae.setHackedFieldName(name);
                Hack.fail(hae);
            } finally {
                this.mField = field;
            }
        }

        public Field getField() {
            return this.mField;
        }
    }

    public static class HackedMethod {
        private static final String TAG = "HackedMethod";
        protected final Method mMethod;

        public Object invoke(Object receiver, Object... args) throws IllegalArgumentException, InvocationTargetException {
            try {
                return this.mMethod.invoke(receiver, args);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }

        HackedMethod(Class<?> clazz, String name, Class<?>[] arg_types, int modifiers) throws HackDeclaration.HackAssertionException {
            Method method = null;
            if (clazz == null) {
                this.mMethod = method;
                return;
            }
            try {
                method = clazz.getDeclaredMethod(name, arg_types);
                if (modifiers > 0 && (method.getModifiers() & modifiers) != modifiers) {
                    Hack.fail(new HackDeclaration.HackAssertionException(method + " does not match modifiers: " + modifiers));
                }
                method.setAccessible(true);
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "No such method: " + e.getMessage());
                HackDeclaration.HackAssertionException hae = new HackDeclaration.HackAssertionException((Exception) e);
                hae.setHackedClass(clazz);
                hae.setHackedMethodName(name);
                Hack.fail(hae);
            } finally {
                this.mMethod = method;
            }
        }

        public Method getMethod() {
            return this.mMethod;
        }
    }

    public static class HackedConstructor {
        protected Constructor<?> mConstructor;

        HackedConstructor(Class<?> clazz, Class<?>[] arg_types) throws HackDeclaration.HackAssertionException {
            if (clazz != null) {
                try {
                    this.mConstructor = clazz.getDeclaredConstructor(arg_types);
                } catch (NoSuchMethodException e) {
                    HackDeclaration.HackAssertionException hae = new HackDeclaration.HackAssertionException((Exception) e);
                    hae.setHackedClass(clazz);
                    Hack.fail(hae);
                }
            }
        }

        public Object getInstance(Object... arg_types) throws IllegalArgumentException {
            this.mConstructor.setAccessible(true);
            try {
                return this.mConstructor.newInstance(arg_types);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static class HackedClass<C> {
        protected Class<C> mClass;

        public HackedField<C, Object> staticField(String name) throws HackDeclaration.HackAssertionException {
            return new HackedField<>(this.mClass, name, 8);
        }

        public HackedField<C, Object> field(String name) throws HackDeclaration.HackAssertionException {
            return new HackedField<>(this.mClass, name, 0);
        }

        public HackedMethod staticMethod(String name, Class<?>... arg_types) throws HackDeclaration.HackAssertionException {
            return new HackedMethod(this.mClass, name, arg_types, 8);
        }

        public HackedMethod method(String name, Class<?>... arg_types) throws HackDeclaration.HackAssertionException {
            return new HackedMethod(this.mClass, name, arg_types, 0);
        }

        public HackedConstructor constructor(Class<?>... arg_types) throws HackDeclaration.HackAssertionException {
            return new HackedConstructor(this.mClass, arg_types);
        }

        public HackedClass(Class<C> clazz) {
            this.mClass = clazz;
        }

        public Class<C> getmClass() {
            return this.mClass;
        }
    }

    public static <T> HackedClass<T> into(Class<T> clazz) {
        return new HackedClass<>(clazz);
    }

    public static <T> HackedClass<T> into(String class_name) throws HackDeclaration.HackAssertionException {
        try {
            return new HackedClass<>(Class.forName(class_name));
        } catch (ClassNotFoundException e) {
            fail(new HackDeclaration.HackAssertionException((Exception) e));
            return new HackedClass<>((Class) null);
        }
    }

    /* access modifiers changed from: private */
    public static void fail(HackDeclaration.HackAssertionException e) throws HackDeclaration.HackAssertionException {
        if (sFailureHandler == null || !sFailureHandler.onAssertionFailure(e)) {
            throw e;
        }
    }

    public static void setAssertionFailureHandler(AssertionFailureHandler handler) {
        sFailureHandler = handler;
    }

    private Hack() {
    }
}
