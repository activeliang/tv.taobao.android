package com.tvlife.imageloader.core;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import com.tvlife.imageloader.cache.disc.DiscCacheAware;
import com.tvlife.imageloader.cache.disc.naming.FileNameGenerator;
import com.tvlife.imageloader.cache.memory.MemoryCacheAware;
import com.tvlife.imageloader.core.assist.ImageSize;
import com.tvlife.imageloader.core.assist.QueueProcessingType;
import com.tvlife.imageloader.core.decode.ImageDecoder;
import com.tvlife.imageloader.core.download.ImageDownloader;
import com.tvlife.imageloader.core.download.NetworkDeniedImageDownloader;
import com.tvlife.imageloader.core.download.SlowNetworkImageDownloader;
import com.tvlife.imageloader.core.process.BitmapProcessor;
import com.tvlife.imageloader.utils.L;
import com.tvlife.imageloader.utils.StorageUtils;
import java.lang.ref.WeakReference;

public final class ImageLoaderConfiguration {
    WeakReference<Context> context;
    WeakReference<Context> contextWeakReference;
    final ImageDecoder decoder;
    final DisplayImageOptions defaultDisplayImageOptions;
    final DiscCacheAware discCache;
    final ImageDownloader downloader;
    private String filename;
    final Bitmap.CompressFormat imageCompressFormatForDiscCache;
    final int imageQualityForDiscCache;
    final int maxImageHeightForDiscCache;
    final int maxImageHeightForMemoryCache;
    final int maxImageWidthForDiscCache;
    final int maxImageWidthForMemoryCache;
    final MemoryCacheAware<String, Bitmap> memoryCache;
    final ImageDownloader networkDeniedDownloader;
    final BitmapProcessor processorForDiscCache;
    final DiscCacheAware reserveDiscCache;
    final ImageDownloader slowNetworkDownloader;
    final QueueProcessingType tasksProcessingType;
    final int threadPoolSize;
    final int threadPriority;
    final boolean writeLogs;

    private ImageLoaderConfiguration(Builder builder) {
        this.contextWeakReference = new WeakReference<>(builder.context);
        this.maxImageWidthForMemoryCache = builder.maxImageWidthForMemoryCache;
        this.maxImageHeightForMemoryCache = builder.maxImageHeightForMemoryCache;
        this.maxImageWidthForDiscCache = builder.maxImageWidthForDiscCache;
        this.maxImageHeightForDiscCache = builder.maxImageHeightForDiscCache;
        this.imageCompressFormatForDiscCache = builder.imageCompressFormatForDiscCache;
        this.imageQualityForDiscCache = builder.imageQualityForDiscCache;
        this.processorForDiscCache = builder.processorForDiscCache;
        this.threadPoolSize = builder.threadPoolSize;
        this.threadPriority = builder.threadPriority;
        this.tasksProcessingType = builder.tasksProcessingType;
        this.discCache = builder.discCache;
        this.memoryCache = builder.memoryCache;
        this.defaultDisplayImageOptions = builder.defaultDisplayImageOptions;
        this.writeLogs = builder.writeLogs;
        this.downloader = builder.downloader;
        this.decoder = builder.decoder;
        this.filename = builder.filename;
        this.networkDeniedDownloader = new NetworkDeniedImageDownloader(this.downloader);
        this.slowNetworkDownloader = new SlowNetworkImageDownloader(this.downloader);
        this.reserveDiscCache = DefaultConfigurationFactory.createReserveDiscCache(StorageUtils.getCacheDirectory(builder.context, false));
    }

    public static ImageLoaderConfiguration createDefault(Context context2) {
        return new Builder(context2).build();
    }

    /* access modifiers changed from: package-private */
    public ImageSize getMaxImageSize() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = this.maxImageWidthForMemoryCache;
        if (width <= 0) {
            width = displayMetrics.widthPixels;
        }
        int height = this.maxImageHeightForMemoryCache;
        if (height <= 0) {
            height = displayMetrics.heightPixels;
        }
        return new ImageSize(width, height);
    }

    public static class Builder {
        public static final QueueProcessingType DEFAULT_TASK_PROCESSING_TYPE = QueueProcessingType.FIFO;
        public static final int DEFAULT_THREAD_POOL_SIZE = 3;
        public static final int DEFAULT_THREAD_PRIORITY = 4;
        private static final String WARNING_OVERLAP_DISC_CACHE_NAME_GENERATOR = "discCache() and discCacheFileNameGenerator() calls overlap each other";
        private static final String WARNING_OVERLAP_DISC_CACHE_PARAMS = "discCache(), discCacheSize() and discCacheFileCount calls overlap each other";
        private static final String WARNING_OVERLAP_EXECUTOR = "threadPoolSize(), threadPriority() and tasksProcessingOrder() calls can overlap taskExecutor() and taskExecutorForCachedImages() calls.";
        private static final String WARNING_OVERLAP_MEMORY_CACHE = "memoryCache() and memoryCacheSize() calls overlap each other";
        /* access modifiers changed from: private */
        public Context context;
        /* access modifiers changed from: private */
        public ImageDecoder decoder;
        /* access modifiers changed from: private */
        public DisplayImageOptions defaultDisplayImageOptions = null;
        private boolean denyCacheImageMultipleSizesInMemory = false;
        /* access modifiers changed from: private */
        public DiscCacheAware discCache = null;
        private int discCacheFileCount = 0;
        private FileNameGenerator discCacheFileNameGenerator = null;
        private int discCacheSize = 0;
        /* access modifiers changed from: private */
        public ImageDownloader downloader = null;
        /* access modifiers changed from: private */
        public String filename;
        /* access modifiers changed from: private */
        public Bitmap.CompressFormat imageCompressFormatForDiscCache = null;
        /* access modifiers changed from: private */
        public int imageQualityForDiscCache = 0;
        /* access modifiers changed from: private */
        public int maxImageHeightForDiscCache = 0;
        /* access modifiers changed from: private */
        public int maxImageHeightForMemoryCache = 0;
        /* access modifiers changed from: private */
        public int maxImageWidthForDiscCache = 0;
        /* access modifiers changed from: private */
        public int maxImageWidthForMemoryCache = 0;
        /* access modifiers changed from: private */
        public MemoryCacheAware<String, Bitmap> memoryCache = null;
        private int memoryCacheSize = 0;
        /* access modifiers changed from: private */
        public BitmapProcessor processorForDiscCache = null;
        /* access modifiers changed from: private */
        public QueueProcessingType tasksProcessingType = DEFAULT_TASK_PROCESSING_TYPE;
        /* access modifiers changed from: private */
        public int threadPoolSize = 3;
        /* access modifiers changed from: private */
        public int threadPriority = 4;
        /* access modifiers changed from: private */
        public boolean writeLogs = false;

        public Builder(Context context2) {
            this.context = context2.getApplicationContext();
        }

        public Builder memoryCacheExtraOptions(int maxImageWidthForMemoryCache2, int maxImageHeightForMemoryCache2) {
            this.maxImageWidthForMemoryCache = maxImageWidthForMemoryCache2;
            this.maxImageHeightForMemoryCache = maxImageHeightForMemoryCache2;
            return this;
        }

        public Builder discCacheExtraOptions(int maxImageWidthForDiscCache2, int maxImageHeightForDiscCache2, Bitmap.CompressFormat compressFormat, int compressQuality, BitmapProcessor processorForDiscCache2) {
            this.maxImageWidthForDiscCache = maxImageWidthForDiscCache2;
            this.maxImageHeightForDiscCache = maxImageHeightForDiscCache2;
            this.imageCompressFormatForDiscCache = compressFormat;
            this.imageQualityForDiscCache = compressQuality;
            this.processorForDiscCache = processorForDiscCache2;
            return this;
        }

        public Builder threadPoolSize(int threadPoolSize2) {
            this.threadPoolSize = threadPoolSize2;
            return this;
        }

        public Builder threadPriority(int threadPriority2) {
            if (threadPriority2 < 1) {
                this.threadPriority = 1;
            } else if (threadPriority2 > 10) {
                this.threadPriority = 10;
            } else {
                this.threadPriority = threadPriority2;
            }
            return this;
        }

        public Builder denyCacheImageMultipleSizesInMemory() {
            this.denyCacheImageMultipleSizesInMemory = true;
            return this;
        }

        public Builder memoryCacheSize(int memoryCacheSize2) {
            if (memoryCacheSize2 <= 0) {
                throw new IllegalArgumentException("memoryCacheSize must be a positive number");
            }
            if (this.memoryCache != null) {
                L.w(WARNING_OVERLAP_MEMORY_CACHE, new Object[0]);
            }
            this.memoryCacheSize = memoryCacheSize2;
            return this;
        }

        public Builder memoryCacheSizePercentage(int availableMemoryPercent) {
            if (availableMemoryPercent <= 0 || availableMemoryPercent >= 100) {
                throw new IllegalArgumentException("availableMemoryPercent must be in range (0 < % < 100)");
            }
            if (this.memoryCache != null) {
                L.w(WARNING_OVERLAP_MEMORY_CACHE, new Object[0]);
            }
            this.memoryCacheSize = (int) (((float) Runtime.getRuntime().maxMemory()) * (((float) availableMemoryPercent) / 100.0f));
            return this;
        }

        public Builder memoryCache(MemoryCacheAware<String, Bitmap> memoryCache2) {
            if (this.memoryCacheSize != 0) {
                L.w(WARNING_OVERLAP_MEMORY_CACHE, new Object[0]);
            }
            this.memoryCache = memoryCache2;
            return this;
        }

        public Builder discCacheSize(int maxCacheSize) {
            if (maxCacheSize <= 0) {
                throw new IllegalArgumentException("maxCacheSize must be a positive number");
            }
            if (this.discCache != null || this.discCacheFileCount > 0) {
                L.w(WARNING_OVERLAP_DISC_CACHE_PARAMS, new Object[0]);
            }
            this.discCacheSize = maxCacheSize;
            return this;
        }

        public Builder discCacheFileFileName(String filename2) {
            this.filename = filename2;
            return this;
        }

        public Builder discCacheFileCount(int maxFileCount) {
            if (maxFileCount <= 0) {
                throw new IllegalArgumentException("maxFileCount must be a positive number");
            }
            if (this.discCache != null || this.discCacheSize > 0) {
                L.w(WARNING_OVERLAP_DISC_CACHE_PARAMS, new Object[0]);
            }
            this.discCacheSize = 0;
            this.discCacheFileCount = maxFileCount;
            return this;
        }

        public Builder discCacheFileNameGenerator(FileNameGenerator fileNameGenerator) {
            if (this.discCache != null) {
                L.w(WARNING_OVERLAP_DISC_CACHE_NAME_GENERATOR, new Object[0]);
            }
            this.discCacheFileNameGenerator = fileNameGenerator;
            return this;
        }

        public Builder imageDownloader(ImageDownloader imageDownloader) {
            this.downloader = imageDownloader;
            return this;
        }

        public Builder imageDecoder(ImageDecoder imageDecoder) {
            this.decoder = imageDecoder;
            return this;
        }

        public Builder discCache(DiscCacheAware discCache2) {
            if (this.discCacheSize > 0 || this.discCacheFileCount > 0) {
                L.w(WARNING_OVERLAP_DISC_CACHE_PARAMS, new Object[0]);
            }
            if (this.discCacheFileNameGenerator != null) {
                L.w(WARNING_OVERLAP_DISC_CACHE_NAME_GENERATOR, new Object[0]);
            }
            this.discCache = discCache2;
            return this;
        }

        public Builder defaultDisplayImageOptions(DisplayImageOptions defaultDisplayImageOptions2) {
            this.defaultDisplayImageOptions = defaultDisplayImageOptions2;
            return this;
        }

        public Builder writeDebugLogs() {
            this.writeLogs = true;
            return this;
        }

        public ImageLoaderConfiguration build() {
            initEmptyFieldsWithDefaultValues();
            return new ImageLoaderConfiguration(this);
        }

        private void initEmptyFieldsWithDefaultValues() {
            if (this.writeLogs) {
                L.enableLogging();
            } else {
                L.disableLogging();
            }
            if (this.discCache == null) {
                if (this.discCacheFileNameGenerator == null) {
                    this.discCacheFileNameGenerator = DefaultConfigurationFactory.createFileNameGenerator();
                }
                this.discCache = DefaultConfigurationFactory.createDiscCache(this.context, this.discCacheFileNameGenerator, this.discCacheSize, this.discCacheFileCount, this.filename);
            }
            if (this.memoryCache == null) {
                this.memoryCache = DefaultConfigurationFactory.createMemoryCache(this.memoryCacheSize);
            }
            if (this.downloader == null) {
                this.downloader = DefaultConfigurationFactory.createImageDownloader(this.context);
            }
            if (this.decoder == null) {
                this.decoder = DefaultConfigurationFactory.createImageDecoder(this.writeLogs);
            }
            if (this.defaultDisplayImageOptions == null) {
                this.defaultDisplayImageOptions = DisplayImageOptions.createSimple();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public Resources getResources() {
        if (this.contextWeakReference == null || this.contextWeakReference.get() == null) {
            return null;
        }
        return ((Context) this.contextWeakReference.get()).getResources();
    }
}
