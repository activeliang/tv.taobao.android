package com.nostra13.universalimageloader.core.listener;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;

public interface ImageLoadingListener {
    void onLoadingCancelled(String str, View view);

    void onLoadingComplete(String str, View view, Bitmap bitmap);

    void onLoadingFailed(String str, View view, FailReason failReason);

    void onLoadingFrom(LoadedFrom loadedFrom, Drawable drawable);

    void onLoadingMemoryKey(String str);

    void onLoadingStarted(String str, View view);
}
