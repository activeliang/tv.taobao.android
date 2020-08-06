package com.tvlife.imageloader.core;

import com.tvlife.imageloader.core.assist.ImageSize;
import com.tvlife.imageloader.core.imageaware.ImageAware;
import com.tvlife.imageloader.core.listener.ImageLoadingListener;
import com.tvlife.imageloader.core.listener.ImageLoadingProgressListener;
import java.util.concurrent.locks.ReentrantLock;

final class ImageLoadingInfo {
    final String bitmapname;
    final ImageAware imageAware;
    final ImageLoadingListener listener;
    final ReentrantLock loadFromUriLock;
    final String memoryCacheKey;
    final DisplayImageOptions options;
    final ImageLoadingProgressListener progressListener;
    final ImageSize targetSize;
    final String uri;

    public ImageLoadingInfo(String uri2, String bitmapname2, ImageAware imageAware2, ImageSize targetSize2, String memoryCacheKey2, DisplayImageOptions options2, ImageLoadingListener listener2, ImageLoadingProgressListener progressListener2, ReentrantLock loadFromUriLock2) {
        this.uri = uri2;
        this.bitmapname = bitmapname2;
        this.imageAware = imageAware2;
        this.targetSize = targetSize2;
        this.options = options2;
        this.listener = listener2;
        this.progressListener = progressListener2;
        this.loadFromUriLock = loadFromUriLock2;
        this.memoryCacheKey = memoryCacheKey2;
    }
}
