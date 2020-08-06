package com.tvlife.imageloader.core.decode;

import android.annotation.TargetApi;
import android.graphics.BitmapFactory;
import android.os.Build;
import com.tvlife.imageloader.core.DisplayImageOptions;
import com.tvlife.imageloader.core.ImageLoaderEngine;
import com.tvlife.imageloader.core.assist.ImageScaleType;
import com.tvlife.imageloader.core.assist.ImageSize;
import com.tvlife.imageloader.core.assist.ViewScaleType;
import com.tvlife.imageloader.core.download.ImageDownloader;

public class ImageDecodingInfo {
    private final String bitmapName;
    private final String bitmapPath;
    private final boolean considerExifParams;
    private final BitmapFactory.Options decodingOptions = new BitmapFactory.Options();
    private final ImageDownloader downloader;
    private final ImageLoaderEngine engine;
    private final Object extraForDownloader;
    private final String imageKey;
    private final ImageScaleType imageScaleType;
    private final String imageUri;
    private boolean isHaveDecodeSample;
    private final ImageSize targetSize;
    private final ViewScaleType viewScaleType;

    public ImageDecodingInfo(String imageKey2, String imageUri2, String filename, ImageSize targetSize2, ImageLoaderEngine engine2, ViewScaleType viewScaleType2, ImageDownloader downloader2, DisplayImageOptions displayOptions) {
        this.imageKey = imageKey2;
        this.imageUri = imageUri2;
        this.targetSize = targetSize2;
        this.engine = engine2;
        this.imageScaleType = displayOptions.getImageScaleType();
        this.viewScaleType = viewScaleType2;
        this.downloader = downloader2;
        this.extraForDownloader = displayOptions.getExtraForDownloader();
        this.considerExifParams = displayOptions.isConsiderExifParams();
        this.isHaveDecodeSample = displayOptions.isDecodeSample();
        this.bitmapPath = displayOptions.getBitmapPath();
        this.bitmapName = filename;
        copyOptions(displayOptions.getDecodingOptions(), this.decodingOptions);
    }

    private void copyOptions(BitmapFactory.Options srcOptions, BitmapFactory.Options destOptions) {
        destOptions.inDensity = srcOptions.inDensity;
        destOptions.inDither = srcOptions.inDither;
        destOptions.inInputShareable = srcOptions.inInputShareable;
        destOptions.inJustDecodeBounds = srcOptions.inJustDecodeBounds;
        destOptions.inPreferredConfig = srcOptions.inPreferredConfig;
        destOptions.inPurgeable = srcOptions.inPurgeable;
        destOptions.inSampleSize = srcOptions.inSampleSize;
        destOptions.inScaled = srcOptions.inScaled;
        destOptions.inScreenDensity = srcOptions.inScreenDensity;
        destOptions.inTargetDensity = srcOptions.inTargetDensity;
        destOptions.inTempStorage = srcOptions.inTempStorage;
        if (Build.VERSION.SDK_INT >= 10) {
            copyOptions10(srcOptions, destOptions);
        }
        if (Build.VERSION.SDK_INT >= 11) {
            copyOptions11(srcOptions, destOptions);
        }
    }

    @TargetApi(10)
    private void copyOptions10(BitmapFactory.Options srcOptions, BitmapFactory.Options destOptions) {
        destOptions.inPreferQualityOverSpeed = srcOptions.inPreferQualityOverSpeed;
    }

    @TargetApi(11)
    private void copyOptions11(BitmapFactory.Options srcOptions, BitmapFactory.Options destOptions) {
        destOptions.inBitmap = srcOptions.inBitmap;
        destOptions.inMutable = srcOptions.inMutable;
    }

    public String getImageKey() {
        return this.imageKey;
    }

    public String getImageUri() {
        return this.imageUri;
    }

    public ImageSize getTargetSize() {
        return this.targetSize;
    }

    public ImageScaleType getImageScaleType() {
        return this.imageScaleType;
    }

    public ViewScaleType getViewScaleType() {
        return this.viewScaleType;
    }

    public ImageDownloader getDownloader() {
        return this.downloader;
    }

    public Object getExtraForDownloader() {
        return this.extraForDownloader;
    }

    public boolean shouldConsiderExifParams() {
        return this.considerExifParams;
    }

    public BitmapFactory.Options getDecodingOptions() {
        return this.decodingOptions;
    }

    public String getBitmapPath() {
        return this.bitmapPath;
    }

    public String getBitmapName() {
        return this.bitmapName;
    }

    public boolean isDecodeSample() {
        return this.isHaveDecodeSample;
    }

    public boolean cancelLoadIfneed() {
        if (this.engine != null && this.engine.loadIfNeed(this.imageKey)) {
            return false;
        }
        return true;
    }
}
