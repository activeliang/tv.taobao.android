package android.taobao.atlas.hack;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Interception {

    private interface Intercepted {
    }

    public static abstract class InterceptionHandler<T> implements InvocationHandler {
        private T mDelegatee;

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                return method.invoke(delegatee(), args);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
                return null;
            } catch (InvocationTargetException e3) {
                throw e3.getTargetException();
            }
        }

        /* access modifiers changed from: protected */
        public T delegatee() {
            return this.mDelegatee;
        }

        /* access modifiers changed from: package-private */
        public void setDelegatee(T delegatee) {
            this.mDelegatee = delegatee;
        }
    }

    public static <T> T proxy(Object delegatee, Class<T> interface_class, InterceptionHandler<T> handler) throws IllegalArgumentException {
        if (delegatee instanceof Intercepted) {
            return delegatee;
        }
        handler.setDelegatee(delegatee);
        return Proxy.newProxyInstance(Interception.class.getClassLoader(), new Class[]{interface_class, Intercepted.class}, handler);
    }

    public static <T> T proxy(Object delegatee, InterceptionHandler<T> handler, Class<?>... interfaces) throws IllegalArgumentException {
        handler.setDelegatee(delegatee);
        return Proxy.newProxyInstance(Interception.class.getClassLoader(), interfaces, handler);
    }

    private Interception() {
    }
}
