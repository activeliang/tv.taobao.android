package anetwork.channel.interceptor;

import anet.channel.util.ALog;
import java.util.concurrent.CopyOnWriteArrayList;

public class InterceptorManager {
    private static final String TAG = "anet.InterceptorManager";
    private static final CopyOnWriteArrayList<Interceptor> interceptors = new CopyOnWriteArrayList<>();

    private InterceptorManager() {
    }

    public static void addInterceptor(Interceptor interceptor) {
        if (!interceptors.contains(interceptor)) {
            interceptors.add(interceptor);
            ALog.i(TAG, "[addInterceptor]", (String) null, "interceptors", interceptors.toString());
        }
    }

    public static void removeInterceptor(Interceptor interceptor) {
        interceptors.remove(interceptor);
        ALog.i(TAG, "[remoteInterceptor]", (String) null, "interceptors", interceptors.toString());
    }

    public static Interceptor getInterceptor(int index) {
        return interceptors.get(index);
    }

    public static boolean contains(Interceptor interceptor) {
        return interceptors.contains(interceptor);
    }

    public static int getSize() {
        return interceptors.size();
    }
}
