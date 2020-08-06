package mtopsdk.extra.antiattack;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.uc.webview.export.WebView;
import com.yunos.tv.blitz.service.BlitzServiceUtils;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import java.net.MalformedURLException;
import java.net.URL;

public class CheckCodeValidateActivity extends Activity {
    private static final String HTTP_REFER_KEY = "http_referer=";
    private static final String NATIVE_FLAG = "native=1";
    private static final String RESULT_BROADCAST_ACTION = "mtopsdk.extra.antiattack.result.notify.action";
    private static final String TAG = "mtopsdk.CheckActivity";
    private static final String TMD_NC_FLAG = "tmd_nc=1";
    String httpReferValue = "";
    WVUCWebView webView = null;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(1);
            linearLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
            linearLayout.setBackgroundColor(Color.parseColor("#000000"));
            linearLayout.setGravity(17);
            setContentView(linearLayout);
            this.webView = new WVUCWebView(this);
            this.webView.setBackgroundColor(0);
            this.webView.getBackground().setAlpha(0);
            linearLayout.addView(this.webView, new LinearLayout.LayoutParams(558, -1, 1.0f));
            this.webView.setWebViewClient(new WVUCWebViewClient(this) {
                public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                    if (TextUtils.isEmpty(s) || !s.equalsIgnoreCase(CheckCodeValidateActivity.this.httpReferValue)) {
                        return super.shouldOverrideUrlLoading(webView, s);
                    }
                    CheckCodeValidateActivity.this.sendResult(BlitzServiceUtils.CSUCCESS);
                    CheckCodeValidateActivity.this.finish();
                    return true;
                }
            });
            String location = getIntent().getStringExtra("Location");
            LogTool.print(16, TAG, "origin load url. " + location, (Throwable) null);
            String location2 = dealWithLocationUrl(location);
            LogTool.print(16, TAG, "load url. " + location2, (Throwable) null);
            this.webView.loadUrl(location2);
        } catch (Exception e) {
            LogTool.print(16, TAG, "onCreate failed.", e);
            sendResult("fail");
            finish();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        sendResult(UpdatePreference.UT_CANCEL);
        finish();
    }

    /* access modifiers changed from: private */
    public void sendResult(String result) {
        LogTool.print(16, TAG, "sendResult: " + result, (Throwable) null);
        Intent intent = new Intent(RESULT_BROADCAST_ACTION);
        intent.setPackage(getApplicationContext().getPackageName());
        intent.putExtra("Result", result);
        sendBroadcast(intent);
    }

    private String dealWithLocationUrl(String location) throws MalformedURLException {
        String queryStr = new URL(location).getQuery();
        StringBuilder sb = new StringBuilder(32);
        if (!TextUtils.isEmpty(queryStr)) {
            String httpRefer = null;
            String[] queryElements = queryStr.split("&");
            for (String element : queryElements) {
                if (element.startsWith(HTTP_REFER_KEY)) {
                    httpRefer = element;
                    this.httpReferValue = element.substring(HTTP_REFER_KEY.length());
                } else if (!element.equalsIgnoreCase(NATIVE_FLAG)) {
                    sb.append(element).append("&");
                }
            }
            sb.append(TMD_NC_FLAG);
            if (httpRefer != null) {
                sb.append("&").append(httpRefer);
            }
            return location.replace(queryStr, sb.toString());
        }
        sb.append(location);
        if (!location.endsWith(WVUtils.URL_DATA_CHAR)) {
            sb.append(WVUtils.URL_DATA_CHAR);
        }
        sb.append(TMD_NC_FLAG);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.webView != null) {
            try {
                this.webView.setVisibility(8);
                this.webView.removeAllViews();
                this.webView.coreDestroy();
                this.webView = null;
            } catch (Exception e) {
                LogTool.print(16, TAG, "WVUCWebView onDestroy error.", e);
            }
        }
    }

    class MyJavaScriptInterface {
        MyJavaScriptInterface() {
        }

        @JavascriptInterface
        public void processHTML(String html) {
            Log.e("webview_htmllll", "webview.processHTML: ===" + html);
        }
    }
}
