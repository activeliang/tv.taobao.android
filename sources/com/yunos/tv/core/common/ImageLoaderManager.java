package com.yunos.tv.core.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.tvlife.imageloader.cache.disc.naming.Md5FileNameGenerator;
import com.tvlife.imageloader.cache.memory.impl.LruMemoryCache;
import com.tvlife.imageloader.core.DisplayImageOptions;
import com.tvlife.imageloader.core.ImageLoader;
import com.tvlife.imageloader.core.ImageLoaderConfiguration;
import com.tvlife.imageloader.core.assist.FailReason;
import com.tvlife.imageloader.core.listener.ImageLoadingListener;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.config.Config;
import com.yunos.tv.core.degrade.ImageShowDegradeManager;
import com.zhiping.dev.android.logger.ZpLogger;

@Deprecated
public class ImageLoaderManager {
    private static ImageLoaderManager mImageLoaderManager;
    private boolean mEnableDebugLogs = Config.isDebug();
    private ImageLoader mImageLoaderWorker;

    public static class OptionConfig {
        public static boolean CACHE_INMEMORY = true;
        public static boolean CACHE_ONDISC = false;
    }

    public static class ImageLoaderConfig {
        public static int DISCCACHE_MAX_SIZE = 52428800;
        public static int MEMORYCACHE_LIMIT_SIZE = 10485760;
        public static int MEMORYCACHE_LIMIT_SIZE_LOW = 2097152;

        public static void setMemorycacheLimitSize(int size) {
        }
    }

    public static ImageLoaderManager get() {
        if (mImageLoaderManager == null) {
            synchronized (ImageLoaderManager.class) {
                if (mImageLoaderManager == null) {
                    mImageLoaderManager = new ImageLoaderManager(CoreApplication.getApplication());
                }
            }
        }
        return mImageLoaderManager;
    }

    public void displayImage(String url, ImageView imageView) {
        displayImage(url, imageView, (DisplayImageOptions) null);
    }

    public void displayImage(String url, ImageView imageView, DisplayImageOptions option) {
        GlideManager.get().displayImage((Context) CoreApplication.getApplication(), url, imageView, getGlideRequestOptions(option));
    }

    public void displayImage(String url, ImageView imageView, ImageLoadingListener listener) {
        displayImage(url, imageView, (DisplayImageOptions) null, listener);
    }

    public void displayImage(final String url, final ImageView imageView, DisplayImageOptions option, final ImageLoadingListener listener) {
        GlideManager.get().loadImage((Context) CoreApplication.getApplication(), url, getGlideRequestOptions(option), (Target<Bitmap>) new CustomTarget<Bitmap>() {
            public void onLoadStarted(@Nullable Drawable placeholder) {
                if (listener != null) {
                    listener.onLoadingStarted(url, imageView);
                }
            }

            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                if (listener != null) {
                    listener.onLoadingFailed(url, imageView, (FailReason) null);
                }
            }

            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                if (listener != null) {
                    listener.onLoadingComplete(url, imageView, resource);
                }
                if (imageView != null) {
                    imageView.setImageBitmap(resource);
                }
            }

            public void onLoadCleared(@Nullable Drawable placeholder) {
                if (listener != null) {
                    listener.onLoadingCancelled(url, imageView);
                }
            }
        });
    }

    @NonNull
    private RequestOptions getGlideRequestOptions(DisplayImageOptions option) {
        Drawable imageOnLoading = null;
        Drawable imageOnFail = null;
        Drawable imageForEmptyUri = null;
        if (option != null) {
            imageOnLoading = option.getImageOnLoading(CoreApplication.getApplication().getResources());
            imageOnFail = option.getImageOnFail(CoreApplication.getApplication().getResources());
            imageForEmptyUri = option.getImageForEmptyUri(CoreApplication.getApplication().getResources());
        }
        return (RequestOptions) ((RequestOptions) ((RequestOptions) new RequestOptions().placeholder(imageOnLoading)).error(imageOnFail)).fallback(imageForEmptyUri);
    }

    public void loadImage(String url, ImageLoadingListener listener) {
        displayImage(url, (ImageView) null, (DisplayImageOptions) null, listener);
    }

    public void loadImage(String url, DisplayImageOptions option, ImageLoadingListener listener) {
        displayImage(url, (ImageView) null, option, listener);
    }

    public void loadImage(String url, String name, DisplayImageOptions option, ImageLoadingListener listener) {
        displayImage(url, (ImageView) null, option, listener);
    }

    public void stop() {
        this.mImageLoaderWorker.stop();
    }

    public void clearDiscCache() {
        this.mImageLoaderWorker.clearDiscCache();
    }

    public void clearMemoryCache() {
        this.mImageLoaderWorker.clearMemoryCache();
    }

    public void cancelDisplayTask(ImageView imageView) {
        this.mImageLoaderWorker.cancelDisplayTask(imageView);
    }

    public void cancelLoadTask(String url) {
        this.mImageLoaderWorker.cancelLoadTaskFor(url);
    }

    public void cancelLoadAllTaskFor() {
        this.mImageLoaderWorker.cancelLoadAllTaskFor();
    }

    public void setWriteDebugLogs(boolean enable) {
        this.mEnableDebugLogs = enable;
    }

    private ImageLoaderManager(Context context) {
        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().cacheOnDisc(OptionConfig.CACHE_ONDISC).cacheInMemory(OptionConfig.CACHE_INMEMORY).build();
        int cacheSize = Math.min(!DeviceJudge.isMemTypeHigh() ? ImageLoaderConfig.MEMORYCACHE_LIMIT_SIZE_LOW : ImageLoaderConfig.MEMORYCACHE_LIMIT_SIZE, ((int) Runtime.getRuntime().maxMemory()) / 10);
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context.getApplicationContext()).threadPoolSize(3).threadPriority(3).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).memoryCache(new LruMemoryCache(cacheSize)).discCacheSize(ImageLoaderConfig.DISCCACHE_MAX_SIZE).discCacheFileFileName("main").defaultDisplayImageOptions(imageOptions);
        if (this.mEnableDebugLogs) {
            builder.writeDebugLogs();
            ZpLogger.i("ImageLoaderManager", "cacheSize = " + cacheSize);
        }
        getLoaderInstance().init(builder.build());
    }

    private ImageLoader getLoaderInstance() {
        if (this.mImageLoaderWorker == null) {
            this.mImageLoaderWorker = ImageLoader.getInstance();
        }
        return this.mImageLoaderWorker;
    }

    private DisplayImageOptions optimizeDIO(DisplayImageOptions options) {
        if (!ImageShowDegradeManager.getInstance().isImageLoaderDegrade() && !ImageShowDegradeManager.getInstance().isImageDegrade()) {
            return options;
        }
        if (options == null) {
            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
            if (ImageShowDegradeManager.getInstance().isImageLoaderDegrade()) {
                builder.cacheInMemory(false);
            }
            if (ImageShowDegradeManager.getInstance().isImageDegrade()) {
                builder.bitmapConfig(Bitmap.Config.ARGB_4444);
            }
            return builder.build();
        }
        if (ImageShowDegradeManager.getInstance().isImageLoaderDegrade()) {
            options.setCacheInMemory(false);
        }
        if (ImageShowDegradeManager.getInstance().isImageDegrade()) {
            options.getDecodingOptions().inPreferredConfig = Bitmap.Config.ARGB_4444;
        }
        return options;
    }
}
