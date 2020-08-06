package com.tvlife.imageloader.core.display;

import android.graphics.Bitmap;
import com.tvlife.imageloader.core.assist.LoadedFrom;
import com.tvlife.imageloader.core.imageaware.ImageAware;

public interface BitmapDisplayer {
    void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom, boolean z);
}
