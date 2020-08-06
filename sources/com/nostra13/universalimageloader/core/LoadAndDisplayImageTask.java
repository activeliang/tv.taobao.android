package com.nostra13.universalimageloader.core;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.assist.ViewScaleType;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.utils.IoUtils;
import com.nostra13.universalimageloader.utils.L;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

final class LoadAndDisplayImageTask implements Runnable, IoUtils.CopyListener {
    private static final int BUFFER_SIZE = 32768;
    private static final String ERROR_POST_PROCESSOR_NULL = "Post-processor returned null [%s]";
    private static final String ERROR_PRE_PROCESSOR_NULL = "Pre-processor returned null [%s]";
    private static final String ERROR_PROCESSOR_FOR_DISC_CACHE_NULL = "Bitmap processor for disc cache returned null [%s]";
    private static final String LOG_CACHE_IMAGE_IN_MEMORY = "Cache image in memory [%s]";
    private static final String LOG_CACHE_IMAGE_ON_DISC = "Cache image on disc [%s]";
    private static final String LOG_DELAY_BEFORE_LOADING = "Delay %d ms before loading...  [%s]";
    private static final String LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING = "...Get cached bitmap from memory after waiting. [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_DISC_CACHE = "Load image from disc cache [%s]";
    private static final String LOG_LOAD_IMAGE_FROM_NETWORK = "Load image from network [%s]";
    private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";
    private static final String LOG_PREPROCESS_IMAGE = "PreProcess image before caching in memory [%s]";
    private static final String LOG_PROCESS_IMAGE_BEFORE_CACHE_ON_DISC = "Process image before cache on disc [%s]";
    private static final String LOG_RESIZE_CACHED_IMAGE_FILE = "Resize image in disc cache [%s]";
    private static final String LOG_RESUME_AFTER_PAUSE = ".. Resume loading [%s]";
    private static final String LOG_START_DISPLAY_IMAGE_TASK = "Start display image task [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED = "ImageAware was collected by GC. Task is cancelled. [%s]";
    private static final String LOG_TASK_CANCELLED_IMAGEAWARE_REUSED = "ImageAware is reused for another image. Task is cancelled. [%s]";
    private static final String LOG_TASK_INTERRUPTED = "Task was interrupted [%s]";
    private static final String LOG_WAITING_FOR_IMAGE_LOADED = "Image already is loading. Waiting... [%s]";
    private static final String LOG_WAITING_FOR_RESUME = "ImageLoader is paused. Waiting...  [%s]";
    /* access modifiers changed from: private */
    public final ImageLoaderConfiguration configuration;
    private final ImageDecoder decoder;
    private final ImageDownloader downloader;
    private final ImageLoaderEngine engine;
    private final Handler handler;
    final ImageAware imageAware;
    private final ImageLoadingInfo imageLoadingInfo;
    final ImageLoadingListener listener;
    /* access modifiers changed from: private */
    public LoadedFrom loadedFrom = LoadedFrom.NETWORK;
    private final String memoryCacheKey;
    private final ImageDownloader networkDeniedDownloader;
    final DisplayImageOptions options;
    final ImageLoadingProgressListener progressListener;
    private final ImageDownloader slowNetworkDownloader;
    private final ImageSize targetSize;
    final String uri;
    private final boolean writeLogs;

    public LoadAndDisplayImageTask(ImageLoaderEngine engine2, ImageLoadingInfo imageLoadingInfo2, Handler handler2) {
        this.engine = engine2;
        this.imageLoadingInfo = imageLoadingInfo2;
        this.handler = handler2;
        this.configuration = engine2.configuration;
        this.downloader = this.configuration.downloader;
        this.networkDeniedDownloader = this.configuration.networkDeniedDownloader;
        this.slowNetworkDownloader = this.configuration.slowNetworkDownloader;
        this.decoder = this.configuration.decoder;
        this.writeLogs = this.configuration.writeLogs;
        this.uri = imageLoadingInfo2.uri;
        this.memoryCacheKey = imageLoadingInfo2.memoryCacheKey;
        this.imageAware = imageLoadingInfo2.imageAware;
        this.targetSize = imageLoadingInfo2.targetSize;
        this.options = imageLoadingInfo2.options;
        this.listener = imageLoadingInfo2.listener;
        this.progressListener = imageLoadingInfo2.progressListener;
    }

    public void run() {
        ReentrantLock loadFromUriLock;
        boolean isPaused = waitIfPaused();
        Log.d("Task", " threadName is " + Thread.currentThread().getName() + ",isPaused-------" + isPaused);
        if (!isPaused && !delayIfNeed() && (loadFromUriLock = this.imageLoadingInfo.loadFromUriLock) != null) {
            log(LOG_START_DISPLAY_IMAGE_TASK);
            if (loadFromUriLock.isLocked()) {
                log(LOG_WAITING_FOR_IMAGE_LOADED);
            }
            loadFromUriLock.lock();
            try {
                checkTaskNotActual();
                Bitmap bmp = this.configuration.memoryCache.get(this.memoryCacheKey);
                if (bmp == null) {
                    bmp = tryLoadBitmap();
                    if (bmp != null) {
                        checkTaskNotActual();
                        checkTaskInterrupted();
                        if (this.options.shouldPreProcess()) {
                            log(LOG_PREPROCESS_IMAGE);
                            bmp = this.options.getPreProcessor().process(bmp);
                            if (bmp == null) {
                                L.e(ERROR_PRE_PROCESSOR_NULL, this.memoryCacheKey);
                            }
                        }
                        if (bmp != null && this.options.isCacheInMemory()) {
                            log(LOG_CACHE_IMAGE_IN_MEMORY);
                            this.configuration.memoryCache.put(this.memoryCacheKey, bmp);
                        }
                    } else {
                        return;
                    }
                } else {
                    this.loadedFrom = LoadedFrom.MEMORY_CACHE;
                    log(LOG_GET_IMAGE_FROM_MEMORY_CACHE_AFTER_WAITING);
                }
                if (bmp != null && this.options.shouldPostProcess()) {
                    log(LOG_POSTPROCESS_IMAGE);
                    bmp = this.options.getPostProcessor().process(bmp);
                    if (bmp == null) {
                        L.e(ERROR_POST_PROCESSOR_NULL, this.memoryCacheKey);
                    }
                }
                checkTaskNotActual();
                checkTaskInterrupted();
                loadFromUriLock.unlock();
                DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(bmp, this.imageLoadingInfo, this.engine, this.loadedFrom);
                displayBitmapTask.setLoggingEnabled(this.writeLogs);
                runTask(displayBitmapTask, this.options.isSyncLoading(), this.handler);
            } catch (TaskCancelledException e) {
                fireCancelEvent();
            } finally {
                loadFromUriLock.unlock();
            }
        }
    }

    private boolean waitIfPaused() {
        AtomicBoolean pause = this.engine.getPause();
        if (pause.get()) {
            synchronized (this.engine.getPauseLock()) {
                if (pause.get()) {
                    log(LOG_WAITING_FOR_RESUME);
                    try {
                        this.engine.getPauseLock().wait();
                        log(LOG_RESUME_AFTER_PAUSE);
                    } catch (InterruptedException e) {
                        L.e(LOG_TASK_INTERRUPTED, this.memoryCacheKey);
                        return true;
                    }
                }
            }
        }
        return isTaskNotActual();
    }

    private boolean delayIfNeed() {
        if (!this.options.shouldDelayBeforeLoading()) {
            return false;
        }
        log(LOG_DELAY_BEFORE_LOADING, Integer.valueOf(this.options.getDelayBeforeLoading()), this.memoryCacheKey);
        try {
            Thread.sleep((long) this.options.getDelayBeforeLoading());
            return isTaskNotActual();
        } catch (InterruptedException e) {
            L.e(LOG_TASK_INTERRUPTED, this.memoryCacheKey);
            return true;
        }
    }

    private Bitmap tryLoadBitmap() throws TaskCancelledException {
        File imageFile = getImageFileInDiscCache();
        Bitmap bitmap = null;
        try {
            String cacheFileUri = ImageDownloader.Scheme.FILE.wrap(imageFile.getAbsolutePath());
            if (imageFile.exists()) {
                log(LOG_LOAD_IMAGE_FROM_DISC_CACHE);
                this.loadedFrom = LoadedFrom.DISC_CACHE;
                fireLoadingEventFrom(this.loadedFrom);
                checkTaskNotActual();
                bitmap = decodeImage(cacheFileUri);
            }
            if (bitmap == null || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
                log(LOG_LOAD_IMAGE_FROM_NETWORK);
                this.loadedFrom = LoadedFrom.NETWORK;
                fireLoadingEventFrom(this.loadedFrom);
                String imageUriForDecoding = (!this.options.isCacheOnDisc() || !tryCacheImageOnDisc(imageFile)) ? this.uri : cacheFileUri;
                checkTaskNotActual();
                bitmap = decodeImage(imageUriForDecoding);
                if (bitmap == null || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
                    fireFailEvent(FailReason.FailType.DECODING_ERROR, (Throwable) null);
                }
            }
        } catch (IllegalStateException e) {
            fireFailEvent(FailReason.FailType.NETWORK_DENIED, (Throwable) null);
        } catch (TaskCancelledException e2) {
            throw e2;
        } catch (IOException e3) {
            L.e(e3);
            fireFailEvent(FailReason.FailType.IO_ERROR, e3);
            if (imageFile.exists()) {
                imageFile.delete();
            }
        } catch (OutOfMemoryError e4) {
            L.e(e4);
            fireFailEvent(FailReason.FailType.OUT_OF_MEMORY, e4);
        } catch (Throwable e5) {
            L.e(e5);
            fireFailEvent(FailReason.FailType.UNKNOWN, e5);
        }
        return bitmap;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x001c, code lost:
        r2 = r5.configuration.reserveDiscCache.get(r5.uri);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.io.File getImageFileInDiscCache() {
        /*
            r5 = this;
            com.nostra13.universalimageloader.core.ImageLoaderConfiguration r3 = r5.configuration
            com.nostra13.universalimageloader.cache.disc.DiscCacheAware r1 = r3.discCache
            java.lang.String r3 = r5.uri
            java.io.File r2 = r1.get(r3)
            java.io.File r0 = r2.getParentFile()
            if (r0 == 0) goto L_0x001c
            boolean r3 = r0.exists()
            if (r3 != 0) goto L_0x0035
            boolean r3 = r0.mkdirs()
            if (r3 != 0) goto L_0x0035
        L_0x001c:
            com.nostra13.universalimageloader.core.ImageLoaderConfiguration r3 = r5.configuration
            com.nostra13.universalimageloader.cache.disc.DiscCacheAware r3 = r3.reserveDiscCache
            java.lang.String r4 = r5.uri
            java.io.File r2 = r3.get(r4)
            java.io.File r0 = r2.getParentFile()
            if (r0 == 0) goto L_0x0035
            boolean r3 = r0.exists()
            if (r3 != 0) goto L_0x0035
            r0.mkdirs()
        L_0x0035:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.getImageFileInDiscCache():java.io.File");
    }

    private Bitmap decodeImage(String imageUri) throws IOException {
        String str = imageUri;
        return this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, str, this.targetSize, this.imageAware.getScaleType(), getDownloader(), this.options));
    }

    private boolean tryCacheImageOnDisc(File targetFile) throws TaskCancelledException {
        log(LOG_CACHE_IMAGE_ON_DISC);
        boolean loaded = false;
        try {
            loaded = downloadImage(targetFile);
            if (loaded) {
                int width = this.configuration.maxImageWidthForDiscCache;
                int height = this.configuration.maxImageHeightForDiscCache;
                if (width > 0 || height > 0) {
                    log(LOG_RESIZE_CACHED_IMAGE_FILE);
                    loaded = resizeAndSaveImage(targetFile, width, height);
                }
                this.configuration.discCache.put(this.uri, targetFile);
            }
        } catch (IOException e) {
            L.e(e);
            if (targetFile.exists()) {
                targetFile.delete();
            }
        }
        return loaded;
    }

    private boolean downloadImage(File targetFile) throws IOException {
        OutputStream os = getDownloader().getStream(this.uri, this.options.getExtraForDownloader());
        try {
            os = new BufferedOutputStream(new FileOutputStream(targetFile), 32768);
            return IoUtils.copyStream(os, os, this);
        } catch (Throwable th) {
            throw th;
        } finally {
            IoUtils.closeSilently(os);
        }
    }

    /* JADX INFO: finally extract failed */
    private boolean resizeAndSaveImage(File targetFile, int maxWidth, int maxHeight) throws IOException {
        Bitmap bmp = this.decoder.decode(new ImageDecodingInfo(this.memoryCacheKey, ImageDownloader.Scheme.FILE.wrap(targetFile.getAbsolutePath()), new ImageSize(maxWidth, maxHeight), ViewScaleType.FIT_INSIDE, getDownloader(), new DisplayImageOptions.Builder().cloneFrom(this.options).imageScaleType(ImageScaleType.IN_SAMPLE_INT).build()));
        if (!(bmp == null || this.configuration.processorForDiscCache == null)) {
            log(LOG_PROCESS_IMAGE_BEFORE_CACHE_ON_DISC);
            bmp = this.configuration.processorForDiscCache.process(bmp);
            if (bmp == null) {
                L.e(ERROR_PROCESSOR_FOR_DISC_CACHE_NULL, this.memoryCacheKey);
            }
        }
        if (bmp != null) {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile), 32768);
            try {
                bmp.compress(this.configuration.imageCompressFormatForDiscCache, this.configuration.imageQualityForDiscCache, os);
                IoUtils.closeSilently(os);
                bmp.recycle();
            } catch (Throwable th) {
                IoUtils.closeSilently(os);
                throw th;
            }
        }
        return true;
    }

    public boolean onBytesCopied(int current, int total) {
        return this.progressListener == null || fireProgressEvent(current, total);
    }

    private boolean fireProgressEvent(final int current, final int total) {
        if (this.options.isSyncLoading() || isTaskInterrupted() || isTaskNotActual()) {
            return false;
        }
        runTask(new Runnable() {
            public void run() {
                LoadAndDisplayImageTask.this.progressListener.onProgressUpdate(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView(), current, total);
            }
        }, false, this.handler);
        return true;
    }

    private void fireFailEvent(final FailReason.FailType failType, final Throwable failCause) {
        if (!this.options.isSyncLoading() && !isTaskInterrupted() && !isTaskNotActual()) {
            runTask(new Runnable() {
                public void run() {
                    Drawable drawable;
                    if (LoadAndDisplayImageTask.this.options.shouldShowImageOnFail() && (drawable = LoadAndDisplayImageTask.this.options.getImageOnFail(LoadAndDisplayImageTask.this.configuration.resources)) != null) {
                        LoadAndDisplayImageTask.this.imageAware.setImageDrawable(drawable);
                    }
                    LoadAndDisplayImageTask.this.listener.onLoadingFailed(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView(), new FailReason(failType, failCause));
                }
            }, false, this.handler);
        }
    }

    private void fireCancelEvent() {
        if (!this.options.isSyncLoading() && !isTaskInterrupted()) {
            runTask(new Runnable() {
                public void run() {
                    LoadAndDisplayImageTask.this.listener.onLoadingCancelled(LoadAndDisplayImageTask.this.uri, LoadAndDisplayImageTask.this.imageAware.getWrappedView());
                }
            }, false, this.handler);
        }
    }

    private void fireLoadingEventFrom(LoadedFrom from) {
        if (!this.options.isSyncLoading() && !isTaskInterrupted()) {
            runTask(new Runnable() {
                public void run() {
                    LoadAndDisplayImageTask.this.listener.onLoadingFrom(LoadAndDisplayImageTask.this.loadedFrom, LoadAndDisplayImageTask.this.options.getImageOnLoading(LoadAndDisplayImageTask.this.configuration.resources));
                }
            }, false, this.handler);
        }
    }

    private ImageDownloader getDownloader() {
        if (this.engine.isNetworkDenied()) {
            return this.networkDeniedDownloader;
        }
        if (this.engine.isSlowNetwork()) {
            return this.slowNetworkDownloader;
        }
        return this.downloader;
    }

    private void checkTaskNotActual() throws TaskCancelledException {
        checkViewCollected();
        checkViewReused();
    }

    private boolean isTaskNotActual() {
        return isViewCollected() || isViewReused();
    }

    private void checkViewCollected() throws TaskCancelledException {
        if (isViewCollected()) {
            throw new TaskCancelledException();
        }
    }

    private boolean isViewCollected() {
        if (!this.imageAware.isCollected()) {
            return false;
        }
        log(LOG_TASK_CANCELLED_IMAGEAWARE_COLLECTED);
        return true;
    }

    private void checkViewReused() throws TaskCancelledException {
        if (isViewReused()) {
            throw new TaskCancelledException();
        }
    }

    private boolean isViewReused() {
        boolean imageAwareWasReused;
        if (!this.memoryCacheKey.equals(this.engine.getLoadingUriForView(this.imageAware))) {
            imageAwareWasReused = true;
        } else {
            imageAwareWasReused = false;
        }
        if (!imageAwareWasReused) {
            return false;
        }
        log(LOG_TASK_CANCELLED_IMAGEAWARE_REUSED);
        return true;
    }

    private void checkTaskInterrupted() throws TaskCancelledException {
        if (isTaskInterrupted()) {
            throw new TaskCancelledException();
        }
    }

    private boolean isTaskInterrupted() {
        if (!Thread.interrupted()) {
            return false;
        }
        log(LOG_TASK_INTERRUPTED);
        return true;
    }

    /* access modifiers changed from: package-private */
    public String getLoadingUri() {
        return this.uri;
    }

    private void log(String message) {
        if (this.writeLogs) {
            L.d(message, this.memoryCacheKey);
        }
    }

    private void log(String message, Object... args) {
        if (this.writeLogs) {
            L.d(message, args);
        }
    }

    static void runTask(Runnable r, boolean sync, Handler handler2) {
        if (sync) {
            r.run();
        } else if (handler2 == null) {
            new Thread(r).start();
        } else {
            handler2.post(r);
        }
    }

    class TaskCancelledException extends Exception {
        TaskCancelledException() {
        }
    }
}
