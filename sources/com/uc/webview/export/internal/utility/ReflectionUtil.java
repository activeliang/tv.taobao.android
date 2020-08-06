package com.uc.webview.export.internal.utility;

import com.uc.webview.export.annotations.Interface;
import com.uc.webview.export.internal.d;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Interface
/* compiled from: ProGuard */
public class ReflectionUtil {
    public static Object invokeNoThrow(String str, String str2) {
        try {
            return invoke(str, str2);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object invokeNoThrow(Class<?> cls, String str) {
        try {
            return invoke((Object) cls, str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object invokeNoThrow(Object obj, String str) {
        try {
            return invoke(obj, str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object invokeNoThrow(Object obj, String str, Class[] clsArr, Object[] objArr) {
        try {
            return invoke(obj, str, clsArr, objArr);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object invokeNoThrow(String str, String str2, Class[] clsArr, Object[] objArr) {
        try {
            return invoke(Class.forName(str, true, d.c), str2, clsArr, objArr);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object invoke(String str, String str2) {
        return invoke(str, str2, (Class[]) null, (Object[]) null);
    }

    public static Object invoke(String str, String str2, Class[] clsArr) {
        return invoke(str, str2, clsArr, (Object[]) null);
    }

    public static Object invoke(String str, String str2, Object[] objArr) {
        Class[] clsArr = null;
        if (objArr != null) {
            clsArr = new Class[objArr.length];
            int length = objArr.length;
            for (int i = 0; i < length; i++) {
                clsArr[i] = objArr[i].getClass();
            }
        }
        return invoke(str, str2, clsArr, objArr);
    }

    public static Object invoke(String str, String str2, Class[] clsArr, Object[] objArr) {
        return invoke(Class.forName(str, true, d.c), str2, clsArr, objArr);
    }

    public static Object invoke(Object obj, String str) {
        return invoke(obj, str, (Class[]) null, new Object[0]);
    }

    public static Object invoke(Object obj, String str, Class[] clsArr, Object[] objArr) {
        if (obj == null) {
            return null;
        }
        return invoke(obj, obj.getClass(), str, clsArr, objArr);
    }

    public static Object invoke(Class<?> cls, String str, Class[] clsArr, Object[] objArr) {
        return invoke((Object) null, cls, str, clsArr, objArr);
    }

    public static Object invoke(Object obj, Class<?> cls, String str, Class[] clsArr, Object[] objArr) {
        Method method;
        try {
            method = cls.getDeclaredMethod(str, clsArr);
        } catch (Throwable th) {
            method = cls.getMethod(str, clsArr);
        }
        return invoke(obj, cls, method, objArr);
    }

    public static Object invoke(Object obj, Class<?> cls, Method method, Object[] objArr) {
        method.setAccessible(true);
        try {
            return method.invoke(obj, objArr);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof Exception) {
                throw ((Exception) targetException);
            } else if (targetException instanceof Error) {
                throw ((Error) targetException);
            } else {
                throw new RuntimeException(targetException);
            }
        }
    }

    public static Object newInstanceNoThrow(String str) {
        try {
            return newInstance(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object newInstance(String str) {
        return newInstance(str, (Class[]) null, (Object[]) null);
    }

    public static Object newInstance(String str, Class[] clsArr, Object[] objArr) {
        try {
            Constructor<?> constructor = Class.forName(str, true, d.c).getConstructor(clsArr);
            constructor.setAccessible(true);
            return constructor.newInstance(objArr);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof Exception) {
                throw ((Exception) targetException);
            } else if (targetException instanceof Error) {
                throw ((Error) targetException);
            } else {
                throw new RuntimeException(targetException);
            }
        }
    }

    public static ClassLoader getCoreClassLoader() {
        return d.c;
    }

    @Interface
    /* compiled from: ProGuard */
    public static final class BindingMethod<T> {
        private Class<?> a;
        private Method b;
        private T c;

        public BindingMethod(Class<?> cls, String str) {
            this(cls, str, (Class<?>[]) null);
        }

        public BindingMethod(Class<?> cls, String str, Class<?>[] clsArr) {
            this.a = null;
            this.b = null;
            this.c = null;
            this.a = cls;
            try {
                this.b = this.a.getDeclaredMethod(str, clsArr);
            } catch (Throwable th) {
                try {
                    this.b = this.a.getMethod(str, clsArr);
                } catch (NoSuchMethodException e) {
                    throw new NoSuchMethodError(e.getMessage());
                }
            }
        }

        public final T invoke() {
            return invoke((Object) null, (Object[]) null);
        }

        public final T invoke(Object[] objArr) {
            return invoke((Object) null, objArr);
        }

        public final T invoke(Object obj) {
            return invoke(obj, (Object[]) null);
        }

        public final T invoke(Object obj, Object[] objArr) {
            try {
                return this.b.invoke(obj, objArr);
            } catch (InvocationTargetException e) {
                Throwable targetException = e.getTargetException();
                if (targetException instanceof RuntimeException) {
                    throw ((RuntimeException) targetException);
                } else if (targetException instanceof Error) {
                    throw ((Error) targetException);
                } else {
                    throw new RuntimeException(targetException);
                }
            } catch (RuntimeException e2) {
                throw e2;
            } catch (Exception e3) {
                throw new RuntimeException(e3);
            }
        }

        public final T getInstance() {
            if (this.c == null) {
                this.c = a();
            }
            return this.c;
        }

        private synchronized T a() {
            if (this.c == null) {
                this.c = invoke();
            }
            return this.c;
        }
    }
}
