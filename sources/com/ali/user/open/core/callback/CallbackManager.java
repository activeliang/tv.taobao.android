package com.ali.user.open.core.callback;

import com.ali.user.open.core.util.Validate;
import java.util.HashMap;
import java.util.Map;

public class CallbackManager {
    private static Map<Integer, Object> staticCallbacks = new HashMap();

    public static synchronized void registerCallback(int callbackType, Object callback) {
        synchronized (CallbackManager.class) {
            Validate.notNull(callback, "callback");
            staticCallbacks.put(Integer.valueOf(callbackType), callback);
        }
    }

    public static synchronized Object getCallback(Integer requestCode) {
        Object obj;
        synchronized (CallbackManager.class) {
            obj = staticCallbacks.get(requestCode);
        }
        return obj;
    }

    public static void unregisterCallback(int requestCode) {
        staticCallbacks.remove(Integer.valueOf(requestCode));
    }
}
