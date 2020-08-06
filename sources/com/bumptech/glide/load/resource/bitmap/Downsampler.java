package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParserUtils;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public final class Downsampler {
    public static final Option<Boolean> ALLOW_HARDWARE_CONFIG = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.AllowHardwareDecode", false);
    public static final Option<DecodeFormat> DECODE_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeFormat", DecodeFormat.DEFAULT);
    @Deprecated
    public static final Option<DownsampleStrategy> DOWNSAMPLE_STRATEGY = DownsampleStrategy.OPTION;
    private static final DecodeCallbacks EMPTY_CALLBACKS = new DecodeCallbacks() {
        public void onObtainBounds() {
        }

        public void onDecodeComplete(BitmapPool bitmapPool, Bitmap downsampled) {
        }
    };
    public static final Option<Boolean> FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.FixBitmapSize", false);
    private static final String ICO_MIME_TYPE = "image/x-ico";
    private static final int MARK_POSITION = 10485760;
    private static final Set<String> NO_DOWNSAMPLE_PRE_N_MIME_TYPES = Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{WBMP_MIME_TYPE, ICO_MIME_TYPE})));
    private static final Queue<BitmapFactory.Options> OPTIONS_QUEUE = Util.createQueue(0);
    static final String TAG = "Downsampler";
    private static final Set<ImageHeaderParser.ImageType> TYPES_THAT_USE_POOL_PRE_KITKAT = Collections.unmodifiableSet(EnumSet.of(ImageHeaderParser.ImageType.JPEG, ImageHeaderParser.ImageType.PNG_A, ImageHeaderParser.ImageType.PNG));
    private static final String WBMP_MIME_TYPE = "image/vnd.wap.wbmp";
    private final BitmapPool bitmapPool;
    private final ArrayPool byteArrayPool;
    private final DisplayMetrics displayMetrics;
    private final HardwareConfigState hardwareConfigState = HardwareConfigState.getInstance();
    private final List<ImageHeaderParser> parsers;

    public interface DecodeCallbacks {
        void onDecodeComplete(BitmapPool bitmapPool, Bitmap bitmap) throws IOException;

        void onObtainBounds();
    }

    public Downsampler(List<ImageHeaderParser> parsers2, DisplayMetrics displayMetrics2, BitmapPool bitmapPool2, ArrayPool byteArrayPool2) {
        this.parsers = parsers2;
        this.displayMetrics = (DisplayMetrics) Preconditions.checkNotNull(displayMetrics2);
        this.bitmapPool = (BitmapPool) Preconditions.checkNotNull(bitmapPool2);
        this.byteArrayPool = (ArrayPool) Preconditions.checkNotNull(byteArrayPool2);
    }

    public boolean handles(InputStream is) {
        return true;
    }

    public boolean handles(ByteBuffer byteBuffer) {
        return true;
    }

    public Resource<Bitmap> decode(InputStream is, int outWidth, int outHeight, Options options) throws IOException {
        return decode(is, outWidth, outHeight, options, EMPTY_CALLBACKS);
    }

    public Resource<Bitmap> decode(InputStream is, int requestedWidth, int requestedHeight, Options options, DecodeCallbacks callbacks) throws IOException {
        boolean isHardwareConfigAllowed;
        Preconditions.checkArgument(is.markSupported(), "You must provide an InputStream that supports mark()");
        byte[] bytesForOptions = (byte[]) this.byteArrayPool.get(65536, byte[].class);
        BitmapFactory.Options bitmapFactoryOptions = getDefaultOptions();
        bitmapFactoryOptions.inTempStorage = bytesForOptions;
        DecodeFormat decodeFormat = (DecodeFormat) options.get(DECODE_FORMAT);
        DownsampleStrategy downsampleStrategy = (DownsampleStrategy) options.get(DownsampleStrategy.OPTION);
        boolean fixBitmapToRequestedDimensions = ((Boolean) options.get(FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS)).booleanValue();
        if (options.get(ALLOW_HARDWARE_CONFIG) != null) {
            if (((Boolean) options.get(ALLOW_HARDWARE_CONFIG)).booleanValue()) {
                isHardwareConfigAllowed = true;
                return BitmapResource.obtain(decodeFromWrappedStreams(is, bitmapFactoryOptions, downsampleStrategy, decodeFormat, isHardwareConfigAllowed, requestedWidth, requestedHeight, fixBitmapToRequestedDimensions, callbacks), this.bitmapPool);
            }
        }
        isHardwareConfigAllowed = false;
        try {
            return BitmapResource.obtain(decodeFromWrappedStreams(is, bitmapFactoryOptions, downsampleStrategy, decodeFormat, isHardwareConfigAllowed, requestedWidth, requestedHeight, fixBitmapToRequestedDimensions, callbacks), this.bitmapPool);
        } finally {
            releaseOptions(bitmapFactoryOptions);
            this.byteArrayPool.put(bytesForOptions);
        }
    }

    private Bitmap decodeFromWrappedStreams(InputStream is, BitmapFactory.Options options, DownsampleStrategy downsampleStrategy, DecodeFormat decodeFormat, boolean isHardwareConfigAllowed, int requestedWidth, int requestedHeight, boolean fixBitmapToRequestedDimensions, DecodeCallbacks callbacks) throws IOException {
        int targetWidth;
        int targetHeight;
        int expectedWidth;
        int expectedHeight;
        long startTime = LogTime.getLogTime();
        int[] sourceDimensions = getDimensions(is, options, callbacks, this.bitmapPool);
        int sourceWidth = sourceDimensions[0];
        int sourceHeight = sourceDimensions[1];
        String sourceMimeType = options.outMimeType;
        if (sourceWidth == -1 || sourceHeight == -1) {
            isHardwareConfigAllowed = false;
        }
        int orientation = ImageHeaderParserUtils.getOrientation(this.parsers, is, this.byteArrayPool);
        int degreesToRotate = TransformationUtils.getExifOrientationDegrees(orientation);
        boolean isExifOrientationRequired = TransformationUtils.isExifOrientationRequired(orientation);
        if (requestedWidth == Integer.MIN_VALUE) {
            targetWidth = sourceWidth;
        } else {
            targetWidth = requestedWidth;
        }
        if (requestedHeight == Integer.MIN_VALUE) {
            targetHeight = sourceHeight;
        } else {
            targetHeight = requestedHeight;
        }
        ImageHeaderParser.ImageType imageType = ImageHeaderParserUtils.getType(this.parsers, is, this.byteArrayPool);
        calculateScaling(imageType, is, callbacks, this.bitmapPool, downsampleStrategy, degreesToRotate, sourceWidth, sourceHeight, targetWidth, targetHeight, options);
        calculateConfig(is, decodeFormat, isHardwareConfigAllowed, isExifOrientationRequired, options, targetWidth, targetHeight);
        boolean isKitKatOrGreater = Build.VERSION.SDK_INT >= 19;
        if ((options.inSampleSize == 1 || isKitKatOrGreater) && shouldUsePool(imageType)) {
            if (sourceWidth < 0 || sourceHeight < 0 || !fixBitmapToRequestedDimensions || !isKitKatOrGreater) {
                float densityMultiplier = isScaling(options) ? ((float) options.inTargetDensity) / ((float) options.inDensity) : 1.0f;
                int sampleSize = options.inSampleSize;
                int downsampledWidth = (int) Math.ceil((double) (((float) sourceWidth) / ((float) sampleSize)));
                int downsampledHeight = (int) Math.ceil((double) (((float) sourceHeight) / ((float) sampleSize)));
                expectedWidth = Math.round(((float) downsampledWidth) * densityMultiplier);
                expectedHeight = Math.round(((float) downsampledHeight) * densityMultiplier);
                if (Log.isLoggable(TAG, 2)) {
                    Log.v(TAG, "Calculated target [" + expectedWidth + "x" + expectedHeight + "] for source [" + sourceWidth + "x" + sourceHeight + "], sampleSize: " + sampleSize + ", targetDensity: " + options.inTargetDensity + ", density: " + options.inDensity + ", density multiplier: " + densityMultiplier);
                }
            } else {
                expectedWidth = targetWidth;
                expectedHeight = targetHeight;
            }
            if (expectedWidth > 0 && expectedHeight > 0) {
                setInBitmap(options, this.bitmapPool, expectedWidth, expectedHeight);
            }
        }
        Bitmap downsampled = decodeStream(is, options, callbacks, this.bitmapPool);
        callbacks.onDecodeComplete(this.bitmapPool, downsampled);
        if (Log.isLoggable(TAG, 2)) {
            logDecode(sourceWidth, sourceHeight, sourceMimeType, options, downsampled, requestedWidth, requestedHeight, startTime);
        }
        Bitmap rotated = null;
        if (downsampled != null) {
            downsampled.setDensity(this.displayMetrics.densityDpi);
            rotated = TransformationUtils.rotateImageExif(this.bitmapPool, downsampled, orientation);
            if (!downsampled.equals(rotated)) {
                this.bitmapPool.put(downsampled);
            }
        }
        return rotated;
    }

    private static void calculateScaling(ImageHeaderParser.ImageType imageType, InputStream is, DecodeCallbacks decodeCallbacks, BitmapPool bitmapPool2, DownsampleStrategy downsampleStrategy, int degreesToRotate, int sourceWidth, int sourceHeight, int targetWidth, int targetHeight, BitmapFactory.Options options) throws IOException {
        float exactScaleFactor;
        int scaleFactor;
        int powerOfTwoSampleSize;
        int powerOfTwoWidth;
        int powerOfTwoHeight;
        if (sourceWidth > 0 && sourceHeight > 0) {
            if (degreesToRotate == 90 || degreesToRotate == 270) {
                exactScaleFactor = downsampleStrategy.getScaleFactor(sourceHeight, sourceWidth, targetWidth, targetHeight);
            } else {
                exactScaleFactor = downsampleStrategy.getScaleFactor(sourceWidth, sourceHeight, targetWidth, targetHeight);
            }
            if (exactScaleFactor <= 0.0f) {
                throw new IllegalArgumentException("Cannot scale with factor: " + exactScaleFactor + " from: " + downsampleStrategy + ", source: [" + sourceWidth + "x" + sourceHeight + "], target: [" + targetWidth + "x" + targetHeight + "]");
            }
            DownsampleStrategy.SampleSizeRounding rounding = downsampleStrategy.getSampleSizeRounding(sourceWidth, sourceHeight, targetWidth, targetHeight);
            if (rounding == null) {
                throw new IllegalArgumentException("Cannot round with null rounding");
            }
            int widthScaleFactor = sourceWidth / round((double) (((float) sourceWidth) * exactScaleFactor));
            int heightScaleFactor = sourceHeight / round((double) (((float) sourceHeight) * exactScaleFactor));
            if (rounding == DownsampleStrategy.SampleSizeRounding.MEMORY) {
                scaleFactor = Math.max(widthScaleFactor, heightScaleFactor);
            } else {
                scaleFactor = Math.min(widthScaleFactor, heightScaleFactor);
            }
            if (Build.VERSION.SDK_INT > 23 || !NO_DOWNSAMPLE_PRE_N_MIME_TYPES.contains(options.outMimeType)) {
                powerOfTwoSampleSize = Math.max(1, Integer.highestOneBit(scaleFactor));
                if (rounding == DownsampleStrategy.SampleSizeRounding.MEMORY && ((float) powerOfTwoSampleSize) < 1.0f / exactScaleFactor) {
                    powerOfTwoSampleSize <<= 1;
                }
            } else {
                powerOfTwoSampleSize = 1;
            }
            options.inSampleSize = powerOfTwoSampleSize;
            if (imageType == ImageHeaderParser.ImageType.JPEG) {
                int nativeScaling = Math.min(powerOfTwoSampleSize, 8);
                powerOfTwoWidth = (int) Math.ceil((double) (((float) sourceWidth) / ((float) nativeScaling)));
                powerOfTwoHeight = (int) Math.ceil((double) (((float) sourceHeight) / ((float) nativeScaling)));
                int secondaryScaling = powerOfTwoSampleSize / 8;
                if (secondaryScaling > 0) {
                    powerOfTwoWidth /= secondaryScaling;
                    powerOfTwoHeight /= secondaryScaling;
                }
            } else if (imageType == ImageHeaderParser.ImageType.PNG || imageType == ImageHeaderParser.ImageType.PNG_A) {
                powerOfTwoWidth = (int) Math.floor((double) (((float) sourceWidth) / ((float) powerOfTwoSampleSize)));
                powerOfTwoHeight = (int) Math.floor((double) (((float) sourceHeight) / ((float) powerOfTwoSampleSize)));
            } else if (imageType == ImageHeaderParser.ImageType.WEBP || imageType == ImageHeaderParser.ImageType.WEBP_A) {
                if (Build.VERSION.SDK_INT >= 24) {
                    powerOfTwoWidth = Math.round(((float) sourceWidth) / ((float) powerOfTwoSampleSize));
                    powerOfTwoHeight = Math.round(((float) sourceHeight) / ((float) powerOfTwoSampleSize));
                } else {
                    powerOfTwoWidth = (int) Math.floor((double) (((float) sourceWidth) / ((float) powerOfTwoSampleSize)));
                    powerOfTwoHeight = (int) Math.floor((double) (((float) sourceHeight) / ((float) powerOfTwoSampleSize)));
                }
            } else if (sourceWidth % powerOfTwoSampleSize == 0 && sourceHeight % powerOfTwoSampleSize == 0) {
                powerOfTwoWidth = sourceWidth / powerOfTwoSampleSize;
                powerOfTwoHeight = sourceHeight / powerOfTwoSampleSize;
            } else {
                int[] dimensions = getDimensions(is, options, decodeCallbacks, bitmapPool2);
                powerOfTwoWidth = dimensions[0];
                powerOfTwoHeight = dimensions[1];
            }
            double adjustedScaleFactor = (double) downsampleStrategy.getScaleFactor(powerOfTwoWidth, powerOfTwoHeight, targetWidth, targetHeight);
            if (Build.VERSION.SDK_INT >= 19) {
                options.inTargetDensity = adjustTargetDensityForError(adjustedScaleFactor);
                options.inDensity = getDensityMultiplier(adjustedScaleFactor);
            }
            if (isScaling(options)) {
                options.inScaled = true;
            } else {
                options.inTargetDensity = 0;
                options.inDensity = 0;
            }
            if (Log.isLoggable(TAG, 2)) {
                Log.v(TAG, "Calculate scaling, source: [" + sourceWidth + "x" + sourceHeight + "], target: [" + targetWidth + "x" + targetHeight + "], power of two scaled: [" + powerOfTwoWidth + "x" + powerOfTwoHeight + "], exact scale factor: " + exactScaleFactor + ", power of 2 sample size: " + powerOfTwoSampleSize + ", adjusted scale factor: " + adjustedScaleFactor + ", target density: " + options.inTargetDensity + ", density: " + options.inDensity);
            }
        } else if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "Unable to determine dimensions for: " + imageType + " with target [" + targetWidth + "x" + targetHeight + "]");
        }
    }

    private static int adjustTargetDensityForError(double adjustedScaleFactor) {
        int densityMultiplier = getDensityMultiplier(adjustedScaleFactor);
        int targetDensity = round(((double) densityMultiplier) * adjustedScaleFactor);
        return round(((double) targetDensity) * (adjustedScaleFactor / ((double) (((float) targetDensity) / ((float) densityMultiplier)))));
    }

    private static int getDensityMultiplier(double adjustedScaleFactor) {
        if (adjustedScaleFactor > 1.0d) {
            adjustedScaleFactor = 1.0d / adjustedScaleFactor;
        }
        return (int) Math.round(2.147483647E9d * adjustedScaleFactor);
    }

    private static int round(double value) {
        return (int) (0.5d + value);
    }

    private boolean shouldUsePool(ImageHeaderParser.ImageType imageType) {
        if (Build.VERSION.SDK_INT >= 19) {
            return true;
        }
        return TYPES_THAT_USE_POOL_PRE_KITKAT.contains(imageType);
    }

    private void calculateConfig(InputStream is, DecodeFormat format, boolean isHardwareConfigAllowed, boolean isExifOrientationRequired, BitmapFactory.Options optionsWithScaling, int targetWidth, int targetHeight) {
        if (!this.hardwareConfigState.setHardwareConfigIfAllowed(targetWidth, targetHeight, optionsWithScaling, format, isHardwareConfigAllowed, isExifOrientationRequired)) {
            if (format == DecodeFormat.PREFER_ARGB_8888 || Build.VERSION.SDK_INT == 16) {
                optionsWithScaling.inPreferredConfig = Bitmap.Config.ARGB_8888;
                return;
            }
            boolean hasAlpha = false;
            try {
                hasAlpha = ImageHeaderParserUtils.getType(this.parsers, is, this.byteArrayPool).hasAlpha();
            } catch (IOException e) {
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "Cannot determine whether the image has alpha or not from header, format " + format, e);
                }
            }
            optionsWithScaling.inPreferredConfig = hasAlpha ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            if (optionsWithScaling.inPreferredConfig == Bitmap.Config.RGB_565) {
                optionsWithScaling.inDither = true;
            }
        }
    }

    private static int[] getDimensions(InputStream is, BitmapFactory.Options options, DecodeCallbacks decodeCallbacks, BitmapPool bitmapPool2) throws IOException {
        options.inJustDecodeBounds = true;
        decodeStream(is, options, decodeCallbacks, bitmapPool2);
        options.inJustDecodeBounds = false;
        return new int[]{options.outWidth, options.outHeight};
    }

    private static Bitmap decodeStream(InputStream is, BitmapFactory.Options options, DecodeCallbacks callbacks, BitmapPool bitmapPool2) throws IOException {
        IOException bitmapAssertionException;
        Bitmap result;
        if (options.inJustDecodeBounds) {
            is.mark(MARK_POSITION);
        } else {
            callbacks.onObtainBounds();
        }
        int sourceWidth = options.outWidth;
        int sourceHeight = options.outHeight;
        String outMimeType = options.outMimeType;
        TransformationUtils.getBitmapDrawableLock().lock();
        try {
            result = BitmapFactory.decodeStream(is, (Rect) null, options);
            TransformationUtils.getBitmapDrawableLock().unlock();
            if (options.inJustDecodeBounds) {
                is.reset();
            }
        } catch (IOException e) {
            throw bitmapAssertionException;
        } catch (IllegalArgumentException e2) {
            bitmapAssertionException = newIoExceptionForInBitmapAssertion(e2, sourceWidth, sourceHeight, outMimeType, options);
            if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Failed to decode with inBitmap, trying again without Bitmap re-use", bitmapAssertionException);
            }
            if (options.inBitmap != null) {
                is.reset();
                bitmapPool2.put(options.inBitmap);
                options.inBitmap = null;
                result = decodeStream(is, options, callbacks, bitmapPool2);
                TransformationUtils.getBitmapDrawableLock().unlock();
            } else {
                throw bitmapAssertionException;
            }
        } catch (Throwable th) {
            TransformationUtils.getBitmapDrawableLock().unlock();
            throw th;
        }
        return result;
    }

    private static boolean isScaling(BitmapFactory.Options options) {
        return options.inTargetDensity > 0 && options.inDensity > 0 && options.inTargetDensity != options.inDensity;
    }

    private static void logDecode(int sourceWidth, int sourceHeight, String outMimeType, BitmapFactory.Options options, Bitmap result, int requestedWidth, int requestedHeight, long startTime) {
        Log.v(TAG, "Decoded " + getBitmapString(result) + " from [" + sourceWidth + "x" + sourceHeight + "] " + outMimeType + " with inBitmap " + getInBitmapString(options) + " for [" + requestedWidth + "x" + requestedHeight + "], sample size: " + options.inSampleSize + ", density: " + options.inDensity + ", target density: " + options.inTargetDensity + ", thread: " + Thread.currentThread().getName() + ", duration: " + LogTime.getElapsedMillis(startTime));
    }

    private static String getInBitmapString(BitmapFactory.Options options) {
        return getBitmapString(options.inBitmap);
    }

    @Nullable
    @TargetApi(19)
    private static String getBitmapString(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        return "[" + bitmap.getWidth() + "x" + bitmap.getHeight() + "] " + bitmap.getConfig() + (Build.VERSION.SDK_INT >= 19 ? " (" + bitmap.getAllocationByteCount() + ")" : "");
    }

    private static IOException newIoExceptionForInBitmapAssertion(IllegalArgumentException e, int outWidth, int outHeight, String outMimeType, BitmapFactory.Options options) {
        return new IOException("Exception decoding bitmap, outWidth: " + outWidth + ", outHeight: " + outHeight + ", outMimeType: " + outMimeType + ", inBitmap: " + getInBitmapString(options), e);
    }

    @TargetApi(26)
    private static void setInBitmap(BitmapFactory.Options options, BitmapPool bitmapPool2, int width, int height) {
        Bitmap.Config expectedConfig = null;
        if (Build.VERSION.SDK_INT >= 26) {
            if (options.inPreferredConfig != Bitmap.Config.HARDWARE) {
                expectedConfig = options.outConfig;
            } else {
                return;
            }
        }
        if (expectedConfig == null) {
            expectedConfig = options.inPreferredConfig;
        }
        options.inBitmap = bitmapPool2.getDirty(width, height, expectedConfig);
    }

    private static synchronized BitmapFactory.Options getDefaultOptions() {
        BitmapFactory.Options decodeBitmapOptions;
        synchronized (Downsampler.class) {
            synchronized (OPTIONS_QUEUE) {
                decodeBitmapOptions = OPTIONS_QUEUE.poll();
            }
            if (decodeBitmapOptions == null) {
                decodeBitmapOptions = new BitmapFactory.Options();
                resetOptions(decodeBitmapOptions);
            }
        }
        return decodeBitmapOptions;
    }

    private static void releaseOptions(BitmapFactory.Options decodeBitmapOptions) {
        resetOptions(decodeBitmapOptions);
        synchronized (OPTIONS_QUEUE) {
            OPTIONS_QUEUE.offer(decodeBitmapOptions);
        }
    }

    private static void resetOptions(BitmapFactory.Options decodeBitmapOptions) {
        decodeBitmapOptions.inTempStorage = null;
        decodeBitmapOptions.inDither = false;
        decodeBitmapOptions.inScaled = false;
        decodeBitmapOptions.inSampleSize = 1;
        decodeBitmapOptions.inPreferredConfig = null;
        decodeBitmapOptions.inJustDecodeBounds = false;
        decodeBitmapOptions.inDensity = 0;
        decodeBitmapOptions.inTargetDensity = 0;
        decodeBitmapOptions.outWidth = 0;
        decodeBitmapOptions.outHeight = 0;
        decodeBitmapOptions.outMimeType = null;
        decodeBitmapOptions.inBitmap = null;
        decodeBitmapOptions.inMutable = true;
    }
}
