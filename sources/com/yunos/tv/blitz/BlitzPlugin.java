package com.yunos.tv.blitz;

import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class BlitzPlugin {

    public interface JsCallback {
        void onCall(String str, long j);
    }

    /* access modifiers changed from: private */
    public static native void _bindingJs(String str, JsCallback jsCallback);

    /* access modifiers changed from: private */
    public static native void _removeBindingJs(String str);

    private static native void _response(boolean z, String str, long j, boolean z2);

    private static class BpImpl {
        static BpImpl impl = new BpImpl();
        Map<String, WeakReference<JsCallback>> mJsFuncTable = new HashMap();

        private BpImpl() {
        }

        /* access modifiers changed from: package-private */
        public void bindingJs(String func, JsCallback cb) {
            synchronized (BlitzPlugin.class) {
                if (!this.mJsFuncTable.containsKey(func)) {
                    BlitzPlugin._bindingJs(func, cb);
                }
                if (cb != null) {
                    this.mJsFuncTable.put(func, new WeakReference(cb));
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void removeBinding(String func) {
            synchronized (BlitzPlugin.class) {
                if (this.mJsFuncTable.containsKey(func)) {
                    this.mJsFuncTable.remove(func);
                    BlitzPlugin._removeBindingJs(func);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public boolean callJsBinding(String func, String param, long cbData) {
            boolean z;
            synchronized (BlitzPlugin.class) {
                WeakReference<JsCallback> wcb = this.mJsFuncTable.get(func);
                JsCallback cb = wcb != null ? (JsCallback) wcb.get() : null;
                Log.i("callJsBinding", "callJsBinding cb = " + cb);
                if (cb == null) {
                    z = false;
                } else {
                    cb.onCall(param, cbData);
                    z = true;
                }
            }
            return z;
        }
    }

    public static void bindingJs(String func, JsCallback cb) {
        BpImpl.impl.bindingJs(func, cb);
    }

    public static void removeBinding(String func) {
        BpImpl.impl.removeBinding(func);
    }

    public static void responseJs(boolean success, String replyMsg, long cbData) {
        responseJs(success, replyMsg, cbData, true);
    }

    public static void responseJs(boolean success, String replyMsg, long cbData, boolean releaseCbData) {
        _response(success, replyMsg, cbData, releaseCbData);
    }

    public static boolean callJsBinding(String func, String param, long cbData) {
        return BpImpl.impl.callJsBinding(func, param, cbData);
    }
}
