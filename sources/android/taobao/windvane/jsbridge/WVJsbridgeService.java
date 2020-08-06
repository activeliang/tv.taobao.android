package android.taobao.windvane.jsbridge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WVJsbridgeService {
    private static List<WVAsyncAuthCheck> mAyncPreprocessor = Collections.synchronizedList(new ArrayList());
    private static List<WVJSAPIAuthCheck> mPreprocessor = Collections.synchronizedList(new ArrayList());

    public static void unregisterPreprocessor(WVAsyncAuthCheck preprocessor) {
        mAyncPreprocessor.remove(preprocessor);
    }

    public static List<WVAsyncAuthCheck> getJSBridgeayncPreprocessors() {
        return mAyncPreprocessor;
    }

    public static void registerJsbridgePreprocessor(WVAsyncAuthCheck preprocessor) {
        mAyncPreprocessor.add(preprocessor);
    }

    public static void unregisterPreprocessor(WVJSAPIAuthCheck preprocessor) {
        mPreprocessor.remove(preprocessor);
    }

    public static List<WVJSAPIAuthCheck> getJSBridgePreprocessors() {
        return mPreprocessor;
    }

    public static void registerJsbridgePreprocessor(WVJSAPIAuthCheck preprocessor) {
        mPreprocessor.add(preprocessor);
    }
}
