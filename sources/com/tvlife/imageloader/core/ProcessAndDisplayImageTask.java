package com.tvlife.imageloader.core;

import android.graphics.Bitmap;
import android.os.Handler;
import com.tvlife.imageloader.core.assist.LoadedFrom;
import com.tvlife.imageloader.utils.L;

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
        LoadAndDisplayImageTask.runTask(new DisplayBitmapTask(this.imageLoadingInfo.options.getPostProcessor().process(this.bitmap), this.imageLoadingInfo, this.engine, LoadedFrom.MEMORY_CACHE, this.engine.configuration.writeLogs), this.imageLoadingInfo.options.isSyncLoading(), this.handler, this.engine);
    }
}
