package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.os.Handler;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.utils.L;

class ProcessAndDisplayImageTask implements Runnable {
    private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";
    private final Bitmap bitmap;
    private final ImageLoaderEngine engine;
    private final Handler handler;
    private final ImageLoadingInfo imageLoadingInfo;

    public ProcessAndDisplayImageTask(ImageLoaderEngine engine2, Bitmap bitmap2, ImageLoadingInfo imageLoadingInfo2, Handler handler2) {
        this.engine = engine2;
        this.bitmap = bitmap2;
        this.imageLoadingInfo = imageLoadingInfo2;
        this.handler = handler2;
    }

    public void run() {
        if (this.engine.configuration.writeLogs) {
            L.d(LOG_POSTPROCESS_IMAGE, this.imageLoadingInfo.memoryCacheKey);
        }
        DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(this.imageLoadingInfo.options.getPostProcessor().process(this.bitmap), this.imageLoadingInfo, this.engine, LoadedFrom.MEMORY_CACHE);
        displayBitmapTask.setLoggingEnabled(this.engine.configuration.writeLogs);
        LoadAndDisplayImageTask.runTask(displayBitmapTask, this.imageLoadingInfo.options.isSyncLoading(), this.handler);
    }
}
