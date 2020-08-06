package android.taobao.windvane.util;

import android.webkit.ValueCallback;
import java.util.concurrent.ConcurrentHashMap;

public class WVNativeCallbackUtil {
    public static final String SEPERATER = "/";
    private static ConcurrentHashMap<String, ValueCallback<String>> mNativeCallbackIdMap = new ConcurrentHashMap<>();

    public static void putNativeCallbak(String id, ValueCallback<String> callback) {
        mNativeCallbackIdMap.put(id, callback);
    }

    public static ValueCallback<String> getNativeCallback(String id) {
        return mNativeCallbackIdMap.get(id);
    }

    public static void clearNativeCallback(String id) {
        mNativeCallbackIdMap.remove(id);
    }

    public static void clearAllNativeCallback() {
        mNativeCallbackIdMap.clear();
    }
}
