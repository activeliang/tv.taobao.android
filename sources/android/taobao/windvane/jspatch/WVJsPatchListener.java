package android.taobao.windvane.jspatch;

import android.taobao.windvane.service.WVEventContext;
import android.taobao.windvane.service.WVEventId;
import android.taobao.windvane.service.WVEventListener;
import android.taobao.windvane.service.WVEventResult;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.webview.IWVWebView;
import java.lang.ref.WeakReference;

public class WVJsPatchListener implements WVEventListener {
    private WeakReference<IWVWebView> webviewReference;

    public WVJsPatchListener(IWVWebView webview) {
        this.webviewReference = new WeakReference<>(webview);
    }

    public WVEventResult onEvent(int eventId, WVEventContext eventContext, Object... objs) {
        switch (eventId) {
            case WVEventId.NATIVETOH5_EVENT /*3006*/:
                if (this.webviewReference == null) {
                    return null;
                }
                IWVWebView reference = (IWVWebView) this.webviewReference.get();
                if (reference != null) {
                    try {
                        reference.fireEvent(objs[0], objs[1]);
                        return null;
                    } catch (Exception e) {
                        return null;
                    }
                } else if (!TaoLog.getLogStatus()) {
                    return null;
                } else {
                    TaoLog.i("WVJsPatchListener", "WVJsPatchListener is free");
                    return null;
                }
            default:
                return null;
        }
    }
}
