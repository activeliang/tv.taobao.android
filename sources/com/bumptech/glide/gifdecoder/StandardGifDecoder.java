package com.bumptech.glide.gifdecoder;

import android.graphics.Bitmap;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.yunos.tvtaobao.biz.listener.OnReminderListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;

public class StandardGifDecoder implements GifDecoder {
    private static final int BYTES_PER_INTEGER = 4;
    @ColorInt
    private static final int COLOR_TRANSPARENT_BLACK = 0;
    private static final int INITIAL_FRAME_POINTER = -1;
    private static final int MASK_INT_LOWEST_BYTE = 255;
    private static final int MAX_STACK_SIZE = 4096;
    private static final int NULL_CODE = -1;
    private static final String TAG = StandardGifDecoder.class.getSimpleName();
    @ColorInt
    private int[] act;
    @NonNull
    private Bitmap.Config bitmapConfig;
    private final GifDecoder.BitmapProvider bitmapProvider;
    private byte[] block;
    private int downsampledHeight;
    private int downsampledWidth;
    private int framePointer;
    private GifHeader header;
    @Nullable
    private Boolean isFirstFrameTransparent;
    private byte[] mainPixels;
    @ColorInt
    private int[] mainScratch;
    private GifHeaderParser parser;
    @ColorInt
    private final int[] pct;
    private byte[] pixelStack;
    private short[] prefix;
    private Bitmap previousImage;
    private ByteBuffer rawData;
    private int sampleSize;
    private boolean savePrevious;
    private int status;
    private byte[] suffix;

    public StandardGifDecoder(@NonNull GifDecoder.BitmapProvider provider, GifHeader gifHeader, ByteBuffer rawData2) {
        this(provider, gifHeader, rawData2, 1);
    }

    public StandardGifDecoder(@NonNull GifDecoder.BitmapProvider provider, GifHeader gifHeader, ByteBuffer rawData2, int sampleSize2) {
        this(provider);
        setData(gifHeader, rawData2, sampleSize2);
    }

    public StandardGifDecoder(@NonNull GifDecoder.BitmapProvider provider) {
        this.pct = new int[256];
        this.bitmapConfig = Bitmap.Config.ARGB_8888;
        this.bitmapProvider = provider;
        this.header = new GifHeader();
    }

    public int getWidth() {
        return this.header.width;
    }

    public int getHeight() {
        return this.header.height;
    }

    @NonNull
    public ByteBuffer getData() {
        return this.rawData;
    }

    public int getStatus() {
        return this.status;
    }

    public void advance() {
        this.framePointer = (this.framePointer + 1) % this.header.frameCount;
    }

    public int getDelay(int n) {
        if (n < 0 || n >= this.header.frameCount) {
            return -1;
        }
        return this.header.frames.get(n).delay;
    }

    public int getNextDelay() {
        if (this.header.frameCount <= 0 || this.framePointer < 0) {
            return 0;
        }
        return getDelay(this.framePointer);
    }

    public int getFrameCount() {
        return this.header.frameCount;
    }

    public int getCurrentFrameIndex() {
        return this.framePointer;
    }

    public void resetFrameIndex() {
        this.framePointer = -1;
    }

    @Deprecated
    public int getLoopCount() {
        if (this.header.loopCount == -1) {
            return 1;
        }
        return this.header.loopCount;
    }

    public int getNetscapeLoopCount() {
        return this.header.loopCount;
    }

    public int getTotalIterationCount() {
        if (this.header.loopCount == -1) {
            return 1;
        }
        if (this.header.loopCount == 0) {
            return 0;
        }
        return this.header.loopCount + 1;
    }

    public int getByteSize() {
        return this.rawData.limit() + this.mainPixels.length + (this.mainScratch.length * 4);
    }

    @Nullable
    public synchronized Bitmap getNextFrame() {
        int[] iArr;
        Bitmap bitmap = null;
        synchronized (this) {
            if (this.header.frameCount <= 0 || this.framePointer < 0) {
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "Unable to decode frame, frameCount=" + this.header.frameCount + ", framePointer=" + this.framePointer);
                }
                this.status = 1;
            }
            if (this.status != 1 && this.status != 2) {
                this.status = 0;
                if (this.block == null) {
                    this.block = this.bitmapProvider.obtainByteArray(255);
                }
                GifFrame currentFrame = this.header.frames.get(this.framePointer);
                GifFrame previousFrame = null;
                int previousIndex = this.framePointer - 1;
                if (previousIndex >= 0) {
                    previousFrame = this.header.frames.get(previousIndex);
                }
                if (currentFrame.lct != null) {
                    iArr = currentFrame.lct;
                } else {
                    iArr = this.header.gct;
                }
                this.act = iArr;
                if (this.act == null) {
                    if (Log.isLoggable(TAG, 3)) {
                        Log.d(TAG, "No valid color table found for frame #" + this.framePointer);
                    }
                    this.status = 1;
                } else {
                    if (currentFrame.transparency) {
                        System.arraycopy(this.act, 0, this.pct, 0, this.act.length);
                        this.act = this.pct;
                        this.act[currentFrame.transIndex] = 0;
                    }
                    bitmap = setPixels(currentFrame, previousFrame);
                }
            } else if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Unable to decode frame, status=" + this.status);
            }
        }
        return bitmap;
    }

    public int read(@Nullable InputStream is, int contentLength) {
        int capacity = 16384;
        if (is != null) {
            if (contentLength > 0) {
                capacity = contentLength + 4096;
            }
            try {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream(capacity);
                byte[] data = new byte[16384];
                while (true) {
                    int nRead = is.read(data, 0, data.length);
                    if (nRead == -1) {
                        break;
                    }
                    buffer.write(data, 0, nRead);
                }
                buffer.flush();
                read(buffer.toByteArray());
            } catch (IOException e) {
                Log.w(TAG, "Error reading data from stream", e);
            }
        } else {
            this.status = 2;
        }
        if (is != null) {
            try {
                is.close();
            } catch (IOException e2) {
                Log.w(TAG, "Error closing stream", e2);
            }
        }
        return this.status;
    }

    public void clear() {
        this.header = null;
        if (this.mainPixels != null) {
            this.bitmapProvider.release(this.mainPixels);
        }
        if (this.mainScratch != null) {
            this.bitmapProvider.release(this.mainScratch);
        }
        if (this.previousImage != null) {
            this.bitmapProvider.release(this.previousImage);
        }
        this.previousImage = null;
        this.rawData = null;
        this.isFirstFrameTransparent = null;
        if (this.block != null) {
            this.bitmapProvider.release(this.block);
        }
    }

    public synchronized void setData(@NonNull GifHeader header2, @NonNull byte[] data) {
        setData(header2, ByteBuffer.wrap(data));
    }

    public synchronized void setData(@NonNull GifHeader header2, @NonNull ByteBuffer buffer) {
        setData(header2, buffer, 1);
    }

    public synchronized void setData(@NonNull GifHeader header2, @NonNull ByteBuffer buffer, int sampleSize2) {
        if (sampleSize2 <= 0) {
            throw new IllegalArgumentException("Sample size must be >=0, not: " + sampleSize2);
        }
        int sampleSize3 = Integer.highestOneBit(sampleSize2);
        this.status = 0;
        this.header = header2;
        this.framePointer = -1;
        this.rawData = buffer.asReadOnlyBuffer();
        this.rawData.position(0);
        this.rawData.order(ByteOrder.LITTLE_ENDIAN);
        this.savePrevious = false;
        Iterator<GifFrame> it = header2.frames.iterator();
        while (true) {
            if (it.hasNext()) {
                if (it.next().dispose == 3) {
                    this.savePrevious = true;
                    break;
                }
            } else {
                break;
            }
        }
        this.sampleSize = sampleSize3;
        this.downsampledWidth = header2.width / sampleSize3;
        this.downsampledHeight = header2.height / sampleSize3;
        this.mainPixels = this.bitmapProvider.obtainByteArray(header2.width * header2.height);
        this.mainScratch = this.bitmapProvider.obtainIntArray(this.downsampledWidth * this.downsampledHeight);
    }

    @NonNull
    private GifHeaderParser getHeaderParser() {
        if (this.parser == null) {
            this.parser = new GifHeaderParser();
        }
        return this.parser;
    }

    public synchronized int read(@Nullable byte[] data) {
        this.header = getHeaderParser().setData(data).parseHeader();
        if (data != null) {
            setData(this.header, data);
        }
        return this.status;
    }

    public void setDefaultBitmapConfig(@NonNull Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888 || config == Bitmap.Config.RGB_565) {
            this.bitmapConfig = config;
            return;
        }
        throw new IllegalArgumentException("Unsupported format: " + config + ", must be one of " + Bitmap.Config.ARGB_8888 + " or " + Bitmap.Config.RGB_565);
    }

    private Bitmap setPixels(GifFrame currentFrame, GifFrame previousFrame) {
        int[] dest = this.mainScratch;
        if (previousFrame == null) {
            if (this.previousImage != null) {
                this.bitmapProvider.release(this.previousImage);
            }
            this.previousImage = null;
            Arrays.fill(dest, 0);
        }
        if (previousFrame != null && previousFrame.dispose == 3 && this.previousImage == null) {
            Arrays.fill(dest, 0);
        }
        if (previousFrame != null && previousFrame.dispose > 0) {
            if (previousFrame.dispose == 2) {
                int c = 0;
                if (!currentFrame.transparency) {
                    c = this.header.bgColor;
                    if (currentFrame.lct != null && this.header.bgIndex == currentFrame.transIndex) {
                        c = 0;
                    }
                } else if (this.framePointer == 0) {
                    this.isFirstFrameTransparent = true;
                }
                int downsampledIH = previousFrame.ih / this.sampleSize;
                int downsampledIY = previousFrame.iy / this.sampleSize;
                int downsampledIW = previousFrame.iw / this.sampleSize;
                int topLeft = (this.downsampledWidth * downsampledIY) + (previousFrame.ix / this.sampleSize);
                int bottomLeft = topLeft + (this.downsampledWidth * downsampledIH);
                int left = topLeft;
                while (left < bottomLeft) {
                    int right = left + downsampledIW;
                    for (int pointer = left; pointer < right; pointer++) {
                        dest[pointer] = c;
                    }
                    left += this.downsampledWidth;
                }
            } else if (previousFrame.dispose == 3 && this.previousImage != null) {
                this.previousImage.getPixels(dest, 0, this.downsampledWidth, 0, 0, this.downsampledWidth, this.downsampledHeight);
            }
        }
        decodeBitmapData(currentFrame);
        if (currentFrame.interlace || this.sampleSize != 1) {
            copyCopyIntoScratchRobust(currentFrame);
        } else {
            copyIntoScratchFast(currentFrame);
        }
        if (this.savePrevious && (currentFrame.dispose == 0 || currentFrame.dispose == 1)) {
            if (this.previousImage == null) {
                this.previousImage = getNextBitmap();
            }
            this.previousImage.setPixels(dest, 0, this.downsampledWidth, 0, 0, this.downsampledWidth, this.downsampledHeight);
        }
        Bitmap result = getNextBitmap();
        result.setPixels(dest, 0, this.downsampledWidth, 0, 0, this.downsampledWidth, this.downsampledHeight);
        return result;
    }

    private void copyIntoScratchFast(GifFrame currentFrame) {
        int[] dest = this.mainScratch;
        int downsampledIH = currentFrame.ih;
        int downsampledIY = currentFrame.iy;
        int downsampledIW = currentFrame.iw;
        int downsampledIX = currentFrame.ix;
        boolean isFirstFrame = this.framePointer == 0;
        int width = this.downsampledWidth;
        byte[] mainPixels2 = this.mainPixels;
        int[] act2 = this.act;
        byte transparentColorIndex = -1;
        for (int i = 0; i < downsampledIH; i++) {
            int k = (i + downsampledIY) * width;
            int dx = k + downsampledIX;
            int dlim = dx + downsampledIW;
            if (k + width < dlim) {
                dlim = k + width;
            }
            int sx = i * currentFrame.iw;
            while (dx < dlim) {
                byte byteCurrentColorIndex = mainPixels2[sx];
                byte currentColorIndex = byteCurrentColorIndex & OnReminderListener.RET_FULL;
                if (currentColorIndex != transparentColorIndex) {
                    int color = act2[currentColorIndex];
                    if (color != 0) {
                        dest[dx] = color;
                    } else {
                        transparentColorIndex = byteCurrentColorIndex;
                    }
                }
                sx++;
                dx++;
            }
        }
        this.isFirstFrameTransparent = Boolean.valueOf(this.isFirstFrameTransparent == null && isFirstFrame && transparentColorIndex != -1);
    }

    private void copyCopyIntoScratchRobust(GifFrame currentFrame) {
        boolean booleanValue;
        int[] dest = this.mainScratch;
        int downsampledIH = currentFrame.ih / this.sampleSize;
        int downsampledIY = currentFrame.iy / this.sampleSize;
        int downsampledIW = currentFrame.iw / this.sampleSize;
        int downsampledIX = currentFrame.ix / this.sampleSize;
        int pass = 1;
        int inc = 8;
        int iline = 0;
        boolean isFirstFrame = this.framePointer == 0;
        int sampleSize2 = this.sampleSize;
        int downsampledWidth2 = this.downsampledWidth;
        int downsampledHeight2 = this.downsampledHeight;
        byte[] mainPixels2 = this.mainPixels;
        int[] act2 = this.act;
        Boolean isFirstFrameTransparent2 = this.isFirstFrameTransparent;
        for (int i = 0; i < downsampledIH; i++) {
            int line = i;
            if (currentFrame.interlace) {
                if (iline >= downsampledIH) {
                    pass++;
                    switch (pass) {
                        case 2:
                            iline = 4;
                            break;
                        case 3:
                            iline = 2;
                            inc = 4;
                            break;
                        case 4:
                            iline = 1;
                            inc = 2;
                            break;
                    }
                }
                line = iline;
                iline += inc;
            }
            int line2 = line + downsampledIY;
            boolean isNotDownsampling = sampleSize2 == 1;
            if (line2 < downsampledHeight2) {
                int k = line2 * downsampledWidth2;
                int dx = k + downsampledIX;
                int dlim = dx + downsampledIW;
                if (k + downsampledWidth2 < dlim) {
                    dlim = k + downsampledWidth2;
                }
                int sx = i * sampleSize2 * currentFrame.iw;
                if (isNotDownsampling) {
                    while (dx < dlim) {
                        int averageColor = act2[mainPixels2[sx] & 255];
                        if (averageColor != 0) {
                            dest[dx] = averageColor;
                        } else if (isFirstFrame && isFirstFrameTransparent2 == null) {
                            isFirstFrameTransparent2 = true;
                        }
                        sx += sampleSize2;
                        dx++;
                    }
                } else {
                    int maxPositionInSource = sx + ((dlim - dx) * sampleSize2);
                    while (dx < dlim) {
                        int averageColor2 = averageColorsNear(sx, maxPositionInSource, currentFrame.iw);
                        if (averageColor2 != 0) {
                            dest[dx] = averageColor2;
                        } else if (isFirstFrame && isFirstFrameTransparent2 == null) {
                            isFirstFrameTransparent2 = true;
                        }
                        sx += sampleSize2;
                        dx++;
                    }
                }
            }
        }
        if (this.isFirstFrameTransparent == null) {
            if (isFirstFrameTransparent2 == null) {
                booleanValue = false;
            } else {
                booleanValue = isFirstFrameTransparent2.booleanValue();
            }
            this.isFirstFrameTransparent = Boolean.valueOf(booleanValue);
        }
    }

    @ColorInt
    private int averageColorsNear(int positionInMainPixels, int maxPositionInMainPixels, int currentFrameIw) {
        int alphaSum = 0;
        int redSum = 0;
        int greenSum = 0;
        int blueSum = 0;
        int totalAdded = 0;
        int i = positionInMainPixels;
        while (i < this.sampleSize + positionInMainPixels && i < this.mainPixels.length && i < maxPositionInMainPixels) {
            int currentColor = this.act[this.mainPixels[i] & 255];
            if (currentColor != 0) {
                alphaSum += (currentColor >> 24) & 255;
                redSum += (currentColor >> 16) & 255;
                greenSum += (currentColor >> 8) & 255;
                blueSum += currentColor & 255;
                totalAdded++;
            }
            i++;
        }
        int i2 = positionInMainPixels + currentFrameIw;
        while (i2 < positionInMainPixels + currentFrameIw + this.sampleSize && i2 < this.mainPixels.length && i2 < maxPositionInMainPixels) {
            int currentColor2 = this.act[this.mainPixels[i2] & 255];
            if (currentColor2 != 0) {
                alphaSum += (currentColor2 >> 24) & 255;
                redSum += (currentColor2 >> 16) & 255;
                greenSum += (currentColor2 >> 8) & 255;
                blueSum += currentColor2 & 255;
                totalAdded++;
            }
            i2++;
        }
        if (totalAdded == 0) {
            return 0;
        }
        return ((alphaSum / totalAdded) << 24) | ((redSum / totalAdded) << 16) | ((greenSum / totalAdded) << 8) | (blueSum / totalAdded);
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v0, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v1, resolved type: byte} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r6v6, resolved type: byte} */
    /* JADX WARNING: Incorrect type for immutable var: ssa=short, code=int, for r9v5, types: [short] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void decodeBitmapData(com.bumptech.glide.gifdecoder.GifFrame r30) {
        /*
            r29 = this;
            if (r30 == 0) goto L_0x0011
            r0 = r29
            java.nio.ByteBuffer r0 = r0.rawData
            r27 = r0
            r0 = r30
            int r0 = r0.bufferFrameStart
            r28 = r0
            r27.position(r28)
        L_0x0011:
            if (r30 != 0) goto L_0x00d9
            r0 = r29
            com.bumptech.glide.gifdecoder.GifHeader r0 = r0.header
            r27 = r0
            r0 = r27
            int r0 = r0.width
            r27 = r0
            r0 = r29
            com.bumptech.glide.gifdecoder.GifHeader r0 = r0.header
            r28 = r0
            r0 = r28
            int r0 = r0.height
            r28 = r0
            int r20 = r27 * r28
        L_0x002d:
            r0 = r29
            byte[] r0 = r0.mainPixels
            r27 = r0
            if (r27 == 0) goto L_0x0046
            r0 = r29
            byte[] r0 = r0.mainPixels
            r27 = r0
            r0 = r27
            int r0 = r0.length
            r27 = r0
            r0 = r27
            r1 = r20
            if (r0 >= r1) goto L_0x005a
        L_0x0046:
            r0 = r29
            com.bumptech.glide.gifdecoder.GifDecoder$BitmapProvider r0 = r0.bitmapProvider
            r27 = r0
            r0 = r27
            r1 = r20
            byte[] r27 = r0.obtainByteArray(r1)
            r0 = r27
            r1 = r29
            r1.mainPixels = r0
        L_0x005a:
            r0 = r29
            byte[] r0 = r0.mainPixels
            r19 = r0
            r0 = r29
            short[] r0 = r0.prefix
            r27 = r0
            if (r27 != 0) goto L_0x0076
            r27 = 4096(0x1000, float:5.74E-42)
            r0 = r27
            short[] r0 = new short[r0]
            r27 = r0
            r0 = r27
            r1 = r29
            r1.prefix = r0
        L_0x0076:
            r0 = r29
            short[] r0 = r0.prefix
            r24 = r0
            r0 = r29
            byte[] r0 = r0.suffix
            r27 = r0
            if (r27 != 0) goto L_0x0092
            r27 = 4096(0x1000, float:5.74E-42)
            r0 = r27
            byte[] r0 = new byte[r0]
            r27 = r0
            r0 = r27
            r1 = r29
            r1.suffix = r0
        L_0x0092:
            r0 = r29
            byte[] r0 = r0.suffix
            r25 = r0
            r0 = r29
            byte[] r0 = r0.pixelStack
            r27 = r0
            if (r27 != 0) goto L_0x00ae
            r27 = 4097(0x1001, float:5.741E-42)
            r0 = r27
            byte[] r0 = new byte[r0]
            r27 = r0
            r0 = r27
            r1 = r29
            r1.pixelStack = r0
        L_0x00ae:
            r0 = r29
            byte[] r0 = r0.pixelStack
            r23 = r0
            int r13 = r29.readByte()
            r27 = 1
            int r8 = r27 << r13
            int r15 = r8 + 1
            int r4 = r8 + 2
            r21 = -1
            int r11 = r13 + 1
            r27 = 1
            int r27 = r27 << r11
            int r10 = r27 + -1
            r9 = 0
        L_0x00cb:
            if (r9 >= r8) goto L_0x00e9
            r27 = 0
            r24[r9] = r27
            byte r0 = (byte) r9
            r27 = r0
            r25[r9] = r27
            int r9 = r9 + 1
            goto L_0x00cb
        L_0x00d9:
            r0 = r30
            int r0 = r0.iw
            r27 = r0
            r0 = r30
            int r0 = r0.ih
            r28 = r0
            int r20 = r27 * r28
            goto L_0x002d
        L_0x00e9:
            r0 = r29
            byte[] r7 = r0.block
            r5 = 0
            r22 = r5
            r26 = r5
            r16 = r5
            r12 = r5
            r6 = r5
            r14 = r5
            r17 = r5
        L_0x00f9:
            r0 = r17
            r1 = r20
            if (r0 >= r1) goto L_0x010f
            if (r12 != 0) goto L_0x011e
            int r12 = r29.readBlock()
            if (r12 > 0) goto L_0x011d
            r27 = 3
            r0 = r27
            r1 = r29
            r1.status = r0
        L_0x010f:
            r27 = 0
            r0 = r19
            r1 = r22
            r2 = r20
            r3 = r27
            java.util.Arrays.fill(r0, r1, r2, r3)
            return
        L_0x011d:
            r5 = 0
        L_0x011e:
            byte r27 = r7[r5]
            r0 = r27
            r0 = r0 & 255(0xff, float:3.57E-43)
            r27 = r0
            int r27 = r27 << r6
            int r14 = r14 + r27
            int r6 = r6 + 8
            int r5 = r5 + 1
            int r12 = r12 + -1
        L_0x0130:
            if (r6 < r11) goto L_0x00f9
            r9 = r14 & r10
            int r14 = r14 >> r11
            int r6 = r6 - r11
            if (r9 != r8) goto L_0x0145
            int r11 = r13 + 1
            r27 = 1
            int r27 = r27 << r11
            int r10 = r27 + -1
            int r4 = r8 + 2
            r21 = -1
            goto L_0x0130
        L_0x0145:
            if (r9 == r15) goto L_0x00f9
            r27 = -1
            r0 = r21
            r1 = r27
            if (r0 != r1) goto L_0x015c
            byte r27 = r25[r9]
            r19[r22] = r27
            int r22 = r22 + 1
            int r17 = r17 + 1
            r21 = r9
            r16 = r9
            goto L_0x0130
        L_0x015c:
            r18 = r9
            if (r9 < r4) goto L_0x016b
            r0 = r16
            byte r0 = (byte) r0
            r27 = r0
            r23[r26] = r27
            int r26 = r26 + 1
            r9 = r21
        L_0x016b:
            if (r9 < r8) goto L_0x0176
            byte r27 = r25[r9]
            r23[r26] = r27
            int r26 = r26 + 1
            short r9 = r24[r9]
            goto L_0x016b
        L_0x0176:
            byte r27 = r25[r9]
            r0 = r27
            r0 = r0 & 255(0xff, float:3.57E-43)
            r16 = r0
            r0 = r16
            byte r0 = (byte) r0
            r27 = r0
            r19[r22] = r27
            int r22 = r22 + 1
            int r17 = r17 + 1
        L_0x0189:
            if (r26 <= 0) goto L_0x0196
            int r26 = r26 + -1
            byte r27 = r23[r26]
            r19[r22] = r27
            int r22 = r22 + 1
            int r17 = r17 + 1
            goto L_0x0189
        L_0x0196:
            r27 = 4096(0x1000, float:5.74E-42)
            r0 = r27
            if (r4 >= r0) goto L_0x01b9
            r0 = r21
            short r0 = (short) r0
            r27 = r0
            r24[r4] = r27
            r0 = r16
            byte r0 = (byte) r0
            r27 = r0
            r25[r4] = r27
            int r4 = r4 + 1
            r27 = r4 & r10
            if (r27 != 0) goto L_0x01b9
            r27 = 4096(0x1000, float:5.74E-42)
            r0 = r27
            if (r4 >= r0) goto L_0x01b9
            int r11 = r11 + 1
            int r10 = r10 + r4
        L_0x01b9:
            r21 = r18
            goto L_0x0130
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.gifdecoder.StandardGifDecoder.decodeBitmapData(com.bumptech.glide.gifdecoder.GifFrame):void");
    }

    private int readByte() {
        return this.rawData.get() & OnReminderListener.RET_FULL;
    }

    private int readBlock() {
        int blockSize = readByte();
        if (blockSize > 0) {
            this.rawData.get(this.block, 0, Math.min(blockSize, this.rawData.remaining()));
        }
        return blockSize;
    }

    private Bitmap getNextBitmap() {
        Bitmap result = this.bitmapProvider.obtain(this.downsampledWidth, this.downsampledHeight, (this.isFirstFrameTransparent == null || this.isFirstFrameTransparent.booleanValue()) ? Bitmap.Config.ARGB_8888 : this.bitmapConfig);
        result.setHasAlpha(true);
        return result;
    }
}
