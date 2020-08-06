package com.yunos.tvtaobao.biz;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import com.google.zxing.WriterException;
import com.yunos.CloudUUIDWrapper;
import com.yunos.tvtaobao.biz.activity.BaseActivity;
import com.yunos.tvtaobao.biz.dialog.QRCodeDialog;
import com.yunos.tvtaobao.biz.dialog.TvTaoBaoDialog;
import com.yunos.tvtaobao.biz.qrcode.QRCodeManager;
import com.yunos.tvtaobao.biz.request.info.GlobalConfigInfo;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;

public class TradeBaseActivity extends BaseActivity {
    private static String TAG = TradeBaseActivity.class.getSimpleName();
    protected String mFROM = "tvhongbao";
    protected QRCodeDialog mQRCodeDialog;
    protected TvTaoBaoDialog mSuccessAddCartDialog;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /* access modifiers changed from: protected */
    public void onStartActivityNetWorkError() {
        showNetworkErrorDialog(false);
    }

    /* access modifiers changed from: protected */
    public String getAppTag() {
        return "Tt";
    }

    public Bitmap getQrCodeBitmap(String itemUrl, Bitmap icon) {
        int width = getResources().getDimensionPixelSize(R.dimen.dp_422);
        try {
            return QRCodeManager.create2DCode(itemUrl, width, width, icon);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showItemQRCodeFromUrl(String text, String itemUrl, Bitmap icon, boolean show, DialogInterface.OnKeyListener onKeyListener) {
        onQRCodeDialog(text, getQrCodeBitmap(itemUrl, icon), show, onKeyListener);
    }

    public void showItemQRCodeFromItemId(String text, String itemId, Bitmap icon, boolean show, DialogInterface.OnKeyListener onKeyListener) {
        String itemUrl = "http://m.tb.cn/ZvJs9c?id=" + itemId + "&orderMarker=v:w-ostvuuid*" + CloudUUIDWrapper.getCloudUUID() + ",w-ostvclient*tvtaobao";
        ZpLogger.i(TAG, "ItemQRCode:" + itemUrl);
        showItemQRCodeFromUrl(text, itemUrl, icon, show, onKeyListener);
    }

    public void showItemQRCodeFromItemId(String text, String itemId, Bitmap icon, boolean show, DialogInterface.OnKeyListener onKeyListener, boolean isFeizhu) {
        StringBuilder params = new StringBuilder();
        params.append(itemId);
        if (isFeizhu) {
            params.append("&hybrid=true");
        }
        params.append("&w-ostvuuid=");
        params.append(CloudUUIDWrapper.getCloudUUID());
        params.append("&w-ostvclient=tvtaobao&orderMarker=v:w-ostvuuid*");
        params.append(CloudUUIDWrapper.getCloudUUID());
        params.append(",w-ostvclient*tvtaobao");
        String itemUrl = "http://m.tb.cn/ZvCmA0?id=" + params.toString();
        ZpLogger.i(TAG, "ItemQRCode:" + itemUrl);
        showItemQRCodeFromUrl(text, itemUrl, icon, show, onKeyListener);
    }

    public void showSuccessAddCartDialog(boolean show, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, DialogInterface.OnKeyListener onKeyListener) {
        onSuccessAddCartDialog(show, positiveListener, negativeListener, onKeyListener);
    }

    private void onQRCodeDialog(String text, Bitmap bitmap, boolean show, DialogInterface.OnKeyListener onKeyListener) {
        if (this.mQRCodeDialog != null && this.mQRCodeDialog.isShowing()) {
            this.mQRCodeDialog.dismiss();
            this.mQRCodeDialog = null;
        }
        if (!isFinishing()) {
            this.mQRCodeDialog = new QRCodeDialog.Builder(this).setQRCodeText(text).setQrCodeBitmap(bitmap).create();
            if (onKeyListener != null) {
                this.mQRCodeDialog.setOnKeyListener(onKeyListener);
            }
            if (show) {
                this.mQRCodeDialog.show();
            } else {
                this.mQRCodeDialog.dismiss();
            }
        }
    }

    private void onSuccessAddCartDialog(boolean show, DialogInterface.OnClickListener positiveListener, DialogInterface.OnClickListener negativeListener, DialogInterface.OnKeyListener onKeyListener) {
        if (!isFinishing()) {
            this.mSuccessAddCartDialog = new TvTaoBaoDialog.Builder(this).setMessage(getResources().getString(R.string.ytbv_success_add_cart)).setPositiveButton(getResources().getString(R.string.ytbv_goto_jiesuan), positiveListener).setNegativeButton(getResources().getString(R.string.ytbv_zai_guangguang), negativeListener).create();
            if (onKeyListener != null) {
                this.mSuccessAddCartDialog.setOnKeyListener(onKeyListener);
            }
            if (show) {
                this.mSuccessAddCartDialog.show();
            } else {
                this.mSuccessAddCartDialog.dismiss();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mQRCodeDialog != null) {
            this.mQRCodeDialog.dismiss();
        }
        if (this.mSuccessAddCartDialog != null) {
            this.mSuccessAddCartDialog.dismiss();
        }
        if (GlobalConfigInfo.getInstance().getGlobalConfig() != null && GlobalConfigInfo.getInstance().getGlobalConfig().isBeta()) {
            System.gc();
            System.runFinalization();
        }
    }
}
