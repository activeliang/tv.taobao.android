package com.tvlife.imageloader.core;

import android.content.Context;
import android.graphics.Bitmap;
import com.tvlife.imageloader.cache.disc.DiscCacheAware;
import com.tvlife.imageloader.cache.disc.impl.FileCountLimitedDiscCache;
import com.tvlife.imageloader.cache.disc.impl.TotalSizeLimitedDiscCache;
import com.tvlife.imageloader.cache.disc.impl.UnlimitedDiscCache;
import com.tvlife.imageloader.cache.disc.naming.FileNameGenerator;
import com.tvlife.imageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.tvlife.imageloader.cache.memory.MemoryCacheAware;
import com.tvlife.imageloader.cache.memory.impl.LruMemoryCache;
import com.tvlife.imageloader.core.assist.QueueProcessingType;
import com.tvlife.imageloader.core.assist.deque.LIFOLinkedBlockingDeque;
import com.tvlife.imageloader.core.decode.BaseImageDecoder;
import com.tvlife.imageloader.core.decode.ImageDecoder;
import com.tvlife.imageloader.core.display.BitmapDisplayer;
import com.tvlife.imageloader.core.display.SimpleBitmapDisplayer;
import com.tvlife.imageloader.core.download.BaseImageDownloader;
import com.tvlife.imageloader.core.download.ImageDownloader;
import com.tvlife.imageloader.utils.StorageUtils;
import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultConfigurationFactory {
    static final String CREAT_DISCCACHEAWARE = "create  cache  file  [%s]";

    public static Executor createExecutor(String name, int threadPoolSize, int threadPriority, QueueProcessingType tasksProcessingType) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0, TimeUnit.MILLISECONDS, tasksProcessingType == QueueProcessingType.LIFO ? new LIFOLinkedBlockingDeque<>() : new LinkedBlockingQueue<>(), createThreadFactory(name, threadPriority));
        threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return threadPoolExecutor;
    }

    public static FileNameGenerator createFileNameGenerator() {
        return new HashCodeFileNameGenerator();
    }

    public static DiscCacheAware createDiscCache(Context context, FileNameGenerator discCacheFileNameGenerator, int discCacheSize, int discCacheFileCount, String filename) {
        if (discCacheSize > 0) {
            return new TotalSizeLimitedDiscCache(StorageUtils.getIndividualCacheDirectory(context, filename), discCacheFileNameGenerator, discCacheSize);
        }
        if (discCacheFileCount > 0) {
            return new FileCountLimitedDiscCache(StorageUtils.getIndividualCacheDirectory(context, filename), discCacheFileNameGenerator, discCacheFileCount);
        }
        return new UnlimitedDiscCache(StorageUtils.getCacheDirectory(context), discCacheFileNameGenerator);
    }

    public static DiscCacheAware createReserveDiscCache(File cacheDir) {
        File individualDir = new File(cacheDir, "uil-images");
        if (individualDir.exists() || individualDir.mkdir()) {
            cacheDir = individualDir;
        }
        return new TotalSizeLimitedDiscCache(cacheDir, 2097152);
    }

    public static MemoryCacheAware<String, Bitmap> createMemoryCache(int memoryCacheSize) {
        if (memoryCacheSize == 0) {
            memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        }
        return new LruMemoryCache(memoryCacheSize);
    }

    public static ImageDownloader createImageDownloader(Context context) {
        return new BaseImageDownloader(context);
    }

    public static ImageDecoder createImageDecoder(boolean loggingEnabled) {
        return new BaseImageDecoder(loggingEnabled);
    }

    public static BitmapDisplayer createBitmapDisplayer() {
        return new SimpleBitmapDisplayer();
    }

    private static ThreadFactory createThreadFactory(String name, int threadPriority) {
        return new DefaultThreadFactory(name, threadPriority);
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final String namePrefix;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final int threadPriority;

        DefaultThreadFactory(String name, int threadPriority2) {
            ThreadGroup threadGroup;
            this.threadPriority = threadPriority2;
            SecurityManager s = System.getSecurityManager();
            if (s != null) {
                threadGroup = s.getThreadGroup();
            } else {
                threadGroup = Thread.currentThread().getThreadGroup();
            }
            this.group = threadGroup;
            this.namePrefix = "Load image from " + name + ": pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            t.setPriority(this.threadPriority);
            return t;
        }
    }

    public static class CachedThreadPoolThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final String namePrefix;
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        CachedThreadPoolThreadFactory(String name) {
            ThreadGroup threadGroup;
            SecurityManager s = System.getSecurityManager();
            if (s != null) {
                threadGroup = s.getThreadGroup();
            } else {
                threadGroup = Thread.currentThread().getThreadGroup();
            }
            this.group = threadGroup;
            this.namePrefix = name + "：pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != 5) {
                t.setPriority(5);
            }
            return t;
        }
    }
}
