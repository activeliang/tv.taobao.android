package com.tvlife.imageloader.core.display;

import android.graphics.Bitmap;
import com.tvlife.imageloader.core.assist.LoadedFrom;
import com.tvlife.imageloader.core.imageaware.ImageAware;
import com.tvlife.imageloader.utils.L;

public final class SimpleBitmapDisplayer implements BitmapDisplayer {
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom, boolean loggingEnabled) {
        imageAware.setImageBitmap(bitmap);
        if (loggingEnabled) {
            L.d("bitmap = " + bitmap + "; loadedFrom = " + loadedFrom + "; imageAware = " + imageAware, new Object[0]);
        }
    }
}
