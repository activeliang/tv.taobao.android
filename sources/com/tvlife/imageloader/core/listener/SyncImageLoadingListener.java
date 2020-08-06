package com.tvlife.imageloader.core.listener;

import android.graphics.Bitmap;
import android.view.View;

public class SyncImageLoadingListener extends SimpleImageLoadingListener {
    private Bitmap loadedImage;

    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage2) {
        this.loadedImage = loadedImage2;
    }

    public Bitmap getLoadedBitmap() {
        return this.loadedImage;
    }
}
