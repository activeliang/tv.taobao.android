package com.tvlife.imageloader.core;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;
import com.tvlife.imageloader.cache.disc.DiscCacheAware;
import com.tvlife.imageloader.cache.memory.MemoryCacheAware;
import com.tvlife.imageloader.core.DisplayImageOptions;
import com.tvlife.imageloader.core.assist.FailReason;
import com.tvlife.imageloader.core.assist.ImageSize;
import com.tvlife.imageloader.core.assist.LoadedFrom;
import com.tvlife.imageloader.core.assist.ViewScaleType;
import com.tvlife.imageloader.core.imageaware.ImageAware;
import com.tvlife.imageloader.core.imageaware.ImageNonViewAware;
import com.tvlife.imageloader.core.imageaware.ImageViewAware;
import com.tvlife.imageloader.core.listener.ImageLoadingListener;
import com.tvlife.imageloader.core.listener.ImageLoadingProgressListener;
import com.tvlife.imageloader.core.listener.SimpleImageLoadingListener;
import com.tvlife.imageloader.core.listener.SyncImageLoadingListener;
import com.tvlife.imageloader.utils.ImageSizeUtils;
import com.tvlife.imageloader.utils.L;
import java.util.concurrent.RejectedExecutionException;

public class ImageLoader {
    private static final String ERROR_INIT_CONFIG_WITH_NULL = "ImageLoader configuration can not be initialized with null";
    private static final String ERROR_NOT_INIT = "ImageLoader must be init with configuration before using";
    private static final String ERROR_NOT_MAIN = "ImageLoader must be work  in  main ui thread";
    private static final String ERROR_WRONG_ARGUMENTS = "Wrong arguments were passed to displayImage() method (ImageView reference must not be null)";
    static final String LOG_DESTROY = "Destroy ImageLoader";
    static final String LOG_INIT = "Init ImageLoader";
    static final String LOG_INIT_CONFIG = "Initialize ImageLoader with configuration";
    static final String LOG_LOAD_IMAGE_FROM_MEMORY_CACHE = "Load image from memory cache [%s]";
    private static final String LOG_WAITING_FOR_REJECTEXECUTION = "Task is Rejected. url = [%s]";
    public static final String TAG = "ImageLoaderWorker";
    private static final String WARNING_RE_INIT_CONFIG = "Try to initialize ImageLoader which had already been initialized before. To re-init ImageLoader with new configuration call ImageLoader.destroy() at first.";
    private static volatile ImageLoader instance;
    private ImageLoaderConfiguration configuration;
    private final ImageLoadingListener emptyListener = new SimpleImageLoadingListener();
    private ImageLoaderEngine engine;

    public static ImageLoader getInstance() {
        L.i("getInstance --> instance = " + instance, new Object[0]);
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    protected ImageLoader() {
    }

    public synchronized void init(ImageLoaderConfiguration configuration2) {
        if (configuration2 == null) {
            throw new IllegalArgumentException(ERROR_INIT_CONFIG_WITH_NULL);
        } else if (this.configuration == null) {
            L.w(LOG_INIT, new Object[0]);
            if (configuration2.writeLogs) {
                L.d(LOG_INIT_CONFIG, new Object[0]);
            }
            this.engine = new ImageLoaderEngine(configuration2);
            this.configuration = configuration2;
        } else {
            L.w(WARNING_RE_INIT_CONFIG, new Object[0]);
        }
    }

    public boolean isInited() {
        return this.configuration != null;
    }

    public void displayImage(String uri, ImageAware imageAware) {
        displayImage(uri, (String) null, imageAware, (DisplayImageOptions) null, (ImageLoadingListener) null, (ImageLoadingProgressListener) null);
    }

    public void displayImage(String uri, ImageAware imageAware, ImageLoadingListener listener) {
        displayImage(uri, (String) null, imageAware, (DisplayImageOptions) null, listener, (ImageLoadingProgressListener) null);
    }

    public void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options) {
        displayImage(uri, (String) null, imageAware, options, (ImageLoadingListener) null, (ImageLoadingProgressListener) null);
    }

    public void displayImage(String uri, ImageAware imageAware, DisplayImageOptions options, ImageLoadingListener listener) {
        displayImage(uri, (String) null, imageAware, options, listener, (ImageLoadingProgressListener) null);
    }

    public void displayImage(String uri, String bitmapname, ImageAware imageAware, DisplayImageOptions options, ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        checkMainUiThread();
        checkConfiguration();
        if (imageAware == null) {
            throw new IllegalArgumentException(ERROR_WRONG_ARGUMENTS);
        }
        if (listener == null) {
            listener = this.emptyListener;
        }
        if (options == null) {
            options = this.configuration.defaultDisplayImageOptions;
        }
        if (TextUtils.isEmpty(uri)) {
            this.engine.cancelDisplayTaskFor(imageAware);
            listener.onLoadingStarted(uri, imageAware.getWrappedView());
            if (options.shouldShowImageForEmptyUri()) {
                imageAware.setImageDrawable(options.getImageForEmptyUri(this.configuration.getResources()));
            } else {
                imageAware.setImageDrawable((Drawable) null);
            }
            listener.onLoadingComplete(uri, imageAware.getWrappedView(), (Bitmap) null);
            return;
        }
        ImageSize targetSize = ImageSizeUtils.defineTargetSizeForView(imageAware, this.configuration.getMaxImageSize());
        String memoryCacheKey = uri;
        this.engine.prepareDisplayTaskFor(imageAware, memoryCacheKey);
        listener.onLoadingStarted(uri, imageAware.getWrappedView());
        Bitmap bmp = null;
        if (options.isCacheInMemory()) {
            bmp = this.configuration.memoryCache.get(memoryCacheKey);
        }
        if (bmp == null || bmp.isRecycled()) {
            if (options.shouldShowImageOnLoading()) {
                imageAware.setImageDrawable(options.getImageOnLoading(this.configuration.getResources()));
            } else if (options.isResetViewBeforeLoading()) {
                imageAware.setImageDrawable((Drawable) null);
            }
            LoadAndDisplayImageTask displayTask = new LoadAndDisplayImageTask(this.engine, new ImageLoadingInfo(uri, bitmapname, imageAware, targetSize, memoryCacheKey, options, listener, progressListener, this.engine.getLockForUri(uri)), defineHandler(options));
            if (options.isSyncLoading()) {
                displayTask.run();
                return;
            }
            try {
                this.engine.submit(displayTask);
            } catch (RejectedExecutionException localRejectedExecutionException) {
                L.w(LOG_WAITING_FOR_REJECTEXECUTION, uri);
                if (listener != null) {
                    listener.onLoadingFailed(uri, imageAware.getWrappedView(), new FailReason(FailReason.FailType.THREADPOOL_REJECTED, localRejectedExecutionException));
                }
            }
        } else {
            if (this.configuration.writeLogs) {
                L.d(LOG_LOAD_IMAGE_FROM_MEMORY_CACHE, memoryCacheKey);
            }
            if (options.isDisplayShow()) {
                options.getDisplayer().display(bmp, imageAware, LoadedFrom.MEMORY_CACHE, this.configuration.writeLogs);
            }
            listener.onLoadingComplete(uri, imageAware.getWrappedView(), bmp);
        }
    }

    public void displayImage(String uri, ImageView imageView) {
        displayImage(uri, (String) null, new ImageViewAware(imageView), (DisplayImageOptions) null, (ImageLoadingListener) null, (ImageLoadingProgressListener) null);
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        displayImage(uri, (String) null, new ImageViewAware(imageView), options, (ImageLoadingListener) null, (ImageLoadingProgressListener) null);
    }

    public void displayImage(String uri, ImageView imageView, ImageLoadingListener listener) {
        displayImage(uri, (String) null, new ImageViewAware(imageView), (DisplayImageOptions) null, listener, (ImageLoadingProgressListener) null);
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener) {
        displayImage(uri, imageView, options, listener, (ImageLoadingProgressListener) null);
    }

    public void displayImage(String uri, ImageView imageView, DisplayImageOptions options, ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        displayImage(uri, (String) null, new ImageViewAware(imageView), options, listener, progressListener);
    }

    public void loadImage(String uri, ImageLoadingListener listener) {
        loadImage(uri, (String) null, (ImageSize) null, (DisplayImageOptions) null, listener, (ImageLoadingProgressListener) null);
    }

    public void loadImage(String uri, ImageSize targetImageSize, ImageLoadingListener listener) {
        loadImage(uri, (String) null, targetImageSize, (DisplayImageOptions) null, listener, (ImageLoadingProgressListener) null);
    }

    public void loadImage(String uri, DisplayImageOptions options, ImageLoadingListener listener) {
        loadImage(uri, (String) null, (ImageSize) null, options, listener, (ImageLoadingProgressListener) null);
    }

    public void loadImage(String uri, String bitmapname, DisplayImageOptions options, ImageLoadingListener listener) {
        loadImage(uri, bitmapname, (ImageSize) null, options, listener, (ImageLoadingProgressListener) null);
    }

    public void loadImage(String uri, ImageSize targetImageSize, DisplayImageOptions options, ImageLoadingListener listener) {
        loadImage(uri, (String) null, targetImageSize, options, listener, (ImageLoadingProgressListener) null);
    }

    public void loadImage(String uri, String bitmapname, ImageSize targetImageSize, DisplayImageOptions options, ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
        checkConfiguration();
        if (targetImageSize == null) {
            targetImageSize = this.configuration.getMaxImageSize();
        }
        if (options == null) {
            options = this.configuration.defaultDisplayImageOptions;
        }
        options.setDisplayShow(false);
        displayImage(uri, bitmapname, new ImageNonViewAware(uri, targetImageSize, ViewScaleType.CROP), options, listener, progressListener);
    }

    public Bitmap loadImageSync(String uri) {
        return loadImageSync(uri, (ImageSize) null, (DisplayImageOptions) null);
    }

    public Bitmap loadImageSync(String uri, DisplayImageOptions options) {
        return loadImageSync(uri, (ImageSize) null, options);
    }

    public Bitmap loadImageSync(String uri, ImageSize targetImageSize) {
        return loadImageSync(uri, targetImageSize, (DisplayImageOptions) null);
    }

    public Bitmap loadImageSync(String uri, ImageSize targetImageSize, DisplayImageOptions options) {
        if (options == null) {
            options = this.configuration.defaultDisplayImageOptions;
        }
        DisplayImageOptions options2 = new DisplayImageOptions.Builder().cloneFrom(options).syncLoading(true).build();
        SyncImageLoadingListener listener = new SyncImageLoadingListener();
        loadImage(uri, targetImageSize, options2, (ImageLoadingListener) listener);
        return listener.getLoadedBitmap();
    }

    private void checkConfiguration() {
        if (this.configuration == null) {
            throw new IllegalStateException(ERROR_NOT_INIT);
        }
    }

    private void checkMainUiThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new IllegalStateException(ERROR_NOT_MAIN);
        }
    }

    public MemoryCacheAware<String, Bitmap> getMemoryCache() {
        checkConfiguration();
        return this.configuration.memoryCache;
    }

    public void clearMemoryCache() {
        checkConfiguration();
        this.configuration.memoryCache.clear();
    }

    public DiscCacheAware getDiscCache() {
        checkConfiguration();
        return this.configuration.discCache;
    }

    public void clearDiscCache() {
        checkConfiguration();
        this.configuration.discCache.clear();
    }

    public String getLoadingUriForView(ImageAware imageAware) {
        return this.engine.getLoadingUriForView(imageAware);
    }

    public String getLoadingUriForView(ImageView imageView) {
        return this.engine.getLoadingUriForView(new ImageViewAware(imageView));
    }

    public void cancelDisplayTask(ImageAware imageAware) {
        this.engine.cancelDisplayTaskFor(imageAware);
    }

    public void cancelDisplayTask(ImageView imageView) {
        this.engine.cancelDisplayTaskFor(new ImageViewAware(imageView));
    }

    public void cancelLoadTaskFor(String url) {
        this.engine.cancelLoadTaskFor(url);
    }

    public void cancelLoadAllTaskFor() {
        L.i("ImageLoader:  cancelLoadAllTaskFor -->", new Object[0]);
        this.engine.cancelLoadAllTaskFor();
    }

    public void setTestBitmap(Bitmap bm) {
        this.engine.setTestBitmap(bm);
    }

    public void denyNetworkDownloads(boolean denyNetworkDownloads) {
        this.engine.denyNetworkDownloads(denyNetworkDownloads);
    }

    public void handleSlowNetwork(boolean handleSlowNetwork) {
        this.engine.handleSlowNetwork(handleSlowNetwork);
    }

    public void pause() {
        this.engine.pause();
    }

    public void resume() {
        this.engine.resume();
    }

    public void stop() {
        this.engine.stop();
    }

    public void destroy() {
        if (this.configuration != null && this.configuration.writeLogs) {
            L.d(LOG_DESTROY, new Object[0]);
        }
        stop();
        this.engine = null;
        this.configuration = null;
    }

    private static Handler defineHandler(DisplayImageOptions options) {
        Handler handler = options.getHandler();
        if (options.isSyncLoading()) {
            return null;
        }
        if (handler == null && Looper.myLooper() == Looper.getMainLooper()) {
            return new Handler();
        }
        return handler;
    }
}
