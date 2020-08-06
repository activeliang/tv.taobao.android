package com.tvlife.imageloader.core;

import android.graphics.Bitmap;
import com.tvlife.imageloader.core.assist.FailReason;
import com.tvlife.imageloader.core.assist.LoadedFrom;
import com.tvlife.imageloader.core.display.BitmapDisplayer;
import com.tvlife.imageloader.core.imageaware.ImageAware;
import com.tvlife.imageloader.core.listener.ImageLoadingListener;
import com.tvlife.imageloader.utils.L;

final class DisplayBitmapTask implements Runnable {
    private static final String LOG_DISPLAY_IMAGE_IN_IMAGEAWARE = "DisplayBitmapTask --> Display image in ImageAware (loaded from %1$s) [%2$s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "DisplayBitmapTask --> ImageAware was collected by GC. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_REUSED = "DisplayBitmapTask --> ImageAware is reused for another image. Task is cancelled. [%s]";
    private final Bitmap bitmap;
    private final BitmapDisplayer displayer;
    private final ImageLoaderEngine engine;
    private final ImageAware imageAware;
    private final String imageUri;
    private final boolean isCacheInMemory;
    private final boolean isdisplayshow;
    private final ImageLoadingListener listener;
    private final LoadedFrom loadedFrom;
    private boolean loggingEnabled;
    private final String memoryCacheKey;

    public DisplayBitmapTask(Bitmap bitmap2, ImageLoadingInfo imageLoadingInfo, ImageLoaderEngine engine2, LoadedFrom loadedFrom2, boolean loggingEnabled2) {
        this.bitmap = bitmap2;
        this.imageUri = imageLoadingInfo.uri;
        this.imageAware = imageLoadingInfo.imageAware;
        this.memoryCacheKey = imageLoadingInfo.memoryCacheKey;
        this.displayer = imageLoadingInfo.options.getDisplayer();
        this.isdisplayshow = imageLoadingInfo.options.isDisplayShow();
        this.isCacheInMemory = imageLoadingInfo.options.isCacheInMemory();
        this.listener = imageLoadingInfo.listener;
        this.engine = engine2;
        this.loadedFrom = loadedFrom2;
        this.loggingEnabled = loggingEnabled2;
    }

    public void run() {
        if (this.loggingEnabled) {
            L.d(LOG_DISPLAY_IMAGE_IN_IMAGEAWARE, this.loadedFrom, this.memoryCacheKey);
        }
        boolean iscollected = this.imageAware.isCollected();
        if (iscollected && this.loggingEnabled) {
            L.d(LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED, this.memoryCacheKey);
        }
        if (this.isdisplayshow && !iscollected) {
            this.displayer.display(this.bitmap, this.imageAware, this.loadedFrom, this.loggingEnabled);
        }
        if (this.bitmap == null || this.bitmap.isRecycled()) {
            this.listener.onLoadingFailed(this.imageUri, this.imageAware.getWrappedView(), new FailReason(FailReason.FailType.BITMAP_RECYCLE, (Throwable) null));
        } else {
            this.listener.onLoadingComplete(this.imageUri, this.imageAware.getWrappedView(), this.bitmap);
        }
        this.engine.cancelDisplayTaskFor(this.imageAware);
    }

    private boolean isViewWasReused() {
        if (!this.isCacheInMemory) {
            return false;
        }
        if (!this.memoryCacheKey.equals(this.engine.getLoadingUriForView(this.imageAware))) {
            return true;
        }
        return false;
    }
}
