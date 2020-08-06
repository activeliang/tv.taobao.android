package com.nostra13.universalimageloader.core.listener;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;

public class SimpleImageLoadingListener implements ImageLoadingListener {
    public void onLoadingStarted(String imageUri, View view) {
    }

    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
    }

    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
    }

    public void onLoadingCancelled(String imageUri, View view) {
    }

    public void onLoadingMemoryKey(String memoryKey) {
    }

    public void onLoadingFrom(LoadedFrom loadedFrom, Drawable drawable) {
    }
}
