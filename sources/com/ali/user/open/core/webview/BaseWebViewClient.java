package com.ali.user.open.core.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.ali.user.open.core.util.DialogHelper;
import com.bftv.fui.constantplugin.Constant;

public class BaseWebViewClient extends WebViewClient {
    protected boolean firstAlert = true;
    protected boolean proceed = false;

    public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
        if (this.firstAlert) {
            DialogInterface.OnClickListener postiveListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    handler.proceed();
                    BaseWebViewClient.this.proceed = true;
                }
            };
            DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    handler.cancel();
                    BaseWebViewClient.this.proceed = false;
                }
            };
            if (view.getContext() instanceof Activity) {
                DialogHelper.getInstance().alert((Activity) view.getContext(), "SSL证书错误", "证书错误. 是否继续访问?", Constant.OK, postiveListener, "取消", cancelListener);
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setPositiveButton(Constant.OK, postiveListener);
            builder.setNeutralButton("取消", cancelListener);
            try {
                AlertDialog ad = builder.create();
                ad.setTitle("SSL证书错误");
                ad.setMessage("证书错误. 是否继续访问?");
                ad.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (this.proceed) {
            handler.proceed();
        } else {
            super.onReceivedSslError(view, handler, error);
        }
    }
}
