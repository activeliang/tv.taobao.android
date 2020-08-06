package android.taobao.windvane.debug;

import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.service.WVWebViewClientFilter;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;

public class WVPageFinishJSRender {
    /* access modifiers changed from: private */
    public static String jsContent = null;
    /* access modifiers changed from: private */
    public static boolean renderJs = false;

    static {
        WVEventService.getInstance().addEventListener(new WVDevelopToolWebViewClientFilter(), WVEventService.WV_BACKWARD_EVENT);
    }

    public static void setJsContent(String js) {
        if (!TextUtils.isEmpty(js)) {
            renderJs = true;
            jsContent = js;
        }
    }

    public static void clearJsRender() {
        renderJs = false;
        jsContent = null;
    }

    public static boolean isRenderJs() {
        return renderJs;
    }

    public static class WVDevelopToolWebViewClientFilter extends WVWebViewClientFilter {
        public void onPageFinished(IWVWebView view, String url) {
            if (WVPageFinishJSRender.renderJs && !TextUtils.isEmpty(WVPageFinishJSRender.jsContent) && (view instanceof IWVWebView)) {
                view.evaluateJavascript(WVPageFinishJSRender.jsContent);
            }
        }
    }
}
