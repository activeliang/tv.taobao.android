package com.tvlife.imageloader.core.listener;

import android.graphics.Bitmap;
import android.view.View;
import com.tvlife.imageloader.core.assist.FailReason;

public class SimpleImageLoadingListener implements ImageLoadingListener {
    public void onLoadingStarted(String imageUri, View view) {
    }

    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
    }

    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
    }

    public void onLoadingCancelled(String imageUri, View view) {
    }
}
