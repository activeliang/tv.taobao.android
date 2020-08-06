package com.ali.auth.third.core.callback;

import android.graphics.Bitmap;

public interface NQrCodeLoginCallback extends LoginCallback {

    public interface NQrCodeLoginController {
        void cancle();
    }

    void onQrImageLoaded(String str, Bitmap bitmap, NQrCodeLoginController nQrCodeLoginController);

    void onQrImageStatusChanged(String str, int i);
}
