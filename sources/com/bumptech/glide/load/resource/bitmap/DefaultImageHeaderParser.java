package com.bumptech.glide.load.resource.bitmap;

import android.support.annotation.NonNull;
import android.support.v4.internal.view.SupportMenu;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public final class DefaultImageHeaderParser implements ImageHeaderParser {
    private static final int[] BYTES_PER_FORMAT = {0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};
    static final int EXIF_MAGIC_NUMBER = 65496;
    static final int EXIF_SEGMENT_TYPE = 225;
    private static final int GIF_HEADER = 4671814;
    private static final int INTEL_TIFF_MAGIC_NUMBER = 18761;
    private static final String JPEG_EXIF_SEGMENT_PREAMBLE = "Exif\u0000\u0000";
    static final byte[] JPEG_EXIF_SEGMENT_PREAMBLE_BYTES = JPEG_EXIF_SEGMENT_PREAMBLE.getBytes(Charset.forName("UTF-8"));
    private static final int MARKER_EOI = 217;
    private static final int MOTOROLA_TIFF_MAGIC_NUMBER = 19789;
    private static final int ORIENTATION_TAG_TYPE = 274;
    private static final int PNG_HEADER = -1991225785;
    private static final int RIFF_HEADER = 1380533830;
    private static final int SEGMENT_SOS = 218;
    static final int SEGMENT_START_ID = 255;
    private static final String TAG = "DfltImageHeaderParser";
    private static final int VP8_HEADER = 1448097792;
    private static final int VP8_HEADER_MASK = -256;
    private static final int VP8_HEADER_TYPE_EXTENDED = 88;
    private static final int VP8_HEADER_TYPE_LOSSLESS = 76;
    private static final int VP8_HEADER_TYPE_MASK = 255;
    private static final int WEBP_EXTENDED_ALPHA_FLAG = 16;
    private static final int WEBP_HEADER = 1464156752;
    private static final int WEBP_LOSSLESS_ALPHA_FLAG = 8;

    private interface Reader {
        int getByte() throws IOException;

        int getUInt16() throws IOException;

        short getUInt8() throws IOException;

        int read(byte[] bArr, int i) throws IOException;

        long skip(long j) throws IOException;
    }

    @NonNull
    public ImageHeaderParser.ImageType getType(@NonNull InputStream is) throws IOException {
        return getType((Reader) new StreamReader((InputStream) Preconditions.checkNotNull(is)));
    }

    @NonNull
    public ImageHeaderParser.ImageType getType(@NonNull ByteBuffer byteBuffer) throws IOException {
        return getType((Reader) new ByteBufferReader((ByteBuffer) Preconditions.checkNotNull(byteBuffer)));
    }

    public int getOrientation(@NonNull InputStream is, @NonNull ArrayPool byteArrayPool) throws IOException {
        return getOrientation((Reader) new StreamReader((InputStream) Preconditions.checkNotNull(is)), (ArrayPool) Preconditions.checkNotNull(byteArrayPool));
    }

    public int getOrientation(@NonNull ByteBuffer byteBuffer, @NonNull ArrayPool byteArrayPool) throws IOException {
        return getOrientation((Reader) new ByteBufferReader((ByteBuffer) Preconditions.checkNotNull(byteBuffer)), (ArrayPool) Preconditions.checkNotNull(byteArrayPool));
    }

    @NonNull
    private ImageHeaderParser.ImageType getType(Reader reader) throws IOException {
        int firstTwoBytes = reader.getUInt16();
        if (firstTwoBytes == EXIF_MAGIC_NUMBER) {
            return ImageHeaderParser.ImageType.JPEG;
        }
        int firstFourBytes = ((firstTwoBytes << 16) & SupportMenu.CATEGORY_MASK) | (reader.getUInt16() & 65535);
        if (firstFourBytes == PNG_HEADER) {
            reader.skip(21);
            return reader.getByte() >= 3 ? ImageHeaderParser.ImageType.PNG_A : ImageHeaderParser.ImageType.PNG;
        } else if ((firstFourBytes >> 8) == GIF_HEADER) {
            return ImageHeaderParser.ImageType.GIF;
        } else {
            if (firstFourBytes != RIFF_HEADER) {
                return ImageHeaderParser.ImageType.UNKNOWN;
            }
            reader.skip(4);
            if ((((reader.getUInt16() << 16) & SupportMenu.CATEGORY_MASK) | (reader.getUInt16() & 65535)) != WEBP_HEADER) {
                return ImageHeaderParser.ImageType.UNKNOWN;
            }
            int fourthFourBytes = ((reader.getUInt16() << 16) & SupportMenu.CATEGORY_MASK) | (reader.getUInt16() & 65535);
            if ((fourthFourBytes & -256) != VP8_HEADER) {
                return ImageHeaderParser.ImageType.UNKNOWN;
            }
            if ((fourthFourBytes & 255) == 88) {
                reader.skip(4);
                return (reader.getByte() & 16) != 0 ? ImageHeaderParser.ImageType.WEBP_A : ImageHeaderParser.ImageType.WEBP;
            } else if ((fourthFourBytes & 255) != 76) {
                return ImageHeaderParser.ImageType.WEBP;
            } else {
                reader.skip(4);
                return (reader.getByte() & 8) != 0 ? ImageHeaderParser.ImageType.WEBP_A : ImageHeaderParser.ImageType.WEBP;
            }
        }
    }

    private int getOrientation(Reader reader, ArrayPool byteArrayPool) throws IOException {
        int i = -1;
        int magicNumber = reader.getUInt16();
        if (handles(magicNumber)) {
            int exifSegmentLength = moveToExifSegmentAndGetLength(reader);
            if (exifSegmentLength != -1) {
                byte[] exifData = (byte[]) byteArrayPool.get(exifSegmentLength, byte[].class);
                try {
                    i = parseExifSegment(reader, exifData, exifSegmentLength);
                } finally {
                    byteArrayPool.put(exifData);
                }
            } else if (Log.isLoggable(TAG, 3)) {
                Log.d(TAG, "Failed to parse exif segment length, or exif segment not found");
            }
        } else if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "Parser doesn't handle magic number: " + magicNumber);
        }
        return i;
    }

    private int parseExifSegment(Reader reader, byte[] tempArray, int exifSegmentLength) throws IOException {
        int read = reader.read(tempArray, exifSegmentLength);
        if (read != exifSegmentLength) {
            if (!Log.isLoggable(TAG, 3)) {
                return -1;
            }
            Log.d(TAG, "Unable to read exif segment data, length: " + exifSegmentLength + ", actually read: " + read);
            return -1;
        } else if (hasJpegExifPreamble(tempArray, exifSegmentLength)) {
            return parseExifSegment(new RandomAccessReader(tempArray, exifSegmentLength));
        } else {
            if (!Log.isLoggable(TAG, 3)) {
                return -1;
            }
            Log.d(TAG, "Missing jpeg exif preamble");
            return -1;
        }
    }

    private boolean hasJpegExifPreamble(byte[] exifData, int exifSegmentLength) {
        boolean result = exifData != null && exifSegmentLength > JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length;
        if (!result) {
            return result;
        }
        for (int i = 0; i < JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length; i++) {
            if (exifData[i] != JPEG_EXIF_SEGMENT_PREAMBLE_BYTES[i]) {
                return false;
            }
        }
        return result;
    }

    private int moveToExifSegmentAndGetLength(Reader reader) throws IOException {
        short segmentType;
        int segmentLength;
        long skipped;
        do {
            short segmentId = reader.getUInt8();
            if (segmentId != 255) {
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "Unknown segmentId=" + segmentId);
                }
                return -1;
            }
            segmentType = reader.getUInt8();
            if (segmentType == 218) {
                return -1;
            }
            if (segmentType == 217) {
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "Found MARKER_EOI in exif segment");
                }
                return -1;
            }
            segmentLength = reader.getUInt16() - 2;
            if (segmentType == 225) {
                return segmentLength;
            }
            skipped = reader.skip((long) segmentLength);
        } while (skipped == ((long) segmentLength));
        if (Log.isLoggable(TAG, 3)) {
            Log.d(TAG, "Unable to skip enough data, type: " + segmentType + ", wanted to skip: " + segmentLength + ", but actually skipped: " + skipped);
        }
        return -1;
    }

    private static int parseExifSegment(RandomAccessReader segmentData) {
        ByteOrder byteOrder;
        int headerOffsetSize = JPEG_EXIF_SEGMENT_PREAMBLE.length();
        short byteOrderIdentifier = segmentData.getInt16(headerOffsetSize);
        switch (byteOrderIdentifier) {
            case INTEL_TIFF_MAGIC_NUMBER /*18761*/:
                byteOrder = ByteOrder.LITTLE_ENDIAN;
                break;
            case MOTOROLA_TIFF_MAGIC_NUMBER /*19789*/:
                byteOrder = ByteOrder.BIG_ENDIAN;
                break;
            default:
                if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "Unknown endianness = " + byteOrderIdentifier);
                }
                byteOrder = ByteOrder.BIG_ENDIAN;
                break;
        }
        segmentData.order(byteOrder);
        int firstIfdOffset = segmentData.getInt32(headerOffsetSize + 4) + headerOffsetSize;
        int tagCount = segmentData.getInt16(firstIfdOffset);
        for (int i = 0; i < tagCount; i++) {
            int tagOffset = calcTagOffset(firstIfdOffset, i);
            int tagType = segmentData.getInt16(tagOffset);
            if (tagType == ORIENTATION_TAG_TYPE) {
                int formatCode = segmentData.getInt16(tagOffset + 2);
                if (formatCode >= 1 && formatCode <= 12) {
                    int componentCount = segmentData.getInt32(tagOffset + 4);
                    if (componentCount >= 0) {
                        if (Log.isLoggable(TAG, 3)) {
                            Log.d(TAG, "Got tagIndex=" + i + " tagType=" + tagType + " formatCode=" + formatCode + " componentCount=" + componentCount);
                        }
                        int byteCount = componentCount + BYTES_PER_FORMAT[formatCode];
                        if (byteCount <= 4) {
                            int tagValueOffset = tagOffset + 8;
                            if (tagValueOffset < 0 || tagValueOffset > segmentData.length()) {
                                if (Log.isLoggable(TAG, 3)) {
                                    Log.d(TAG, "Illegal tagValueOffset=" + tagValueOffset + " tagType=" + tagType);
                                }
                            } else if (byteCount >= 0 && tagValueOffset + byteCount <= segmentData.length()) {
                                return segmentData.getInt16(tagValueOffset);
                            } else {
                                if (Log.isLoggable(TAG, 3)) {
                                    Log.d(TAG, "Illegal number of bytes for TI tag data tagType=" + tagType);
                                }
                            }
                        } else if (Log.isLoggable(TAG, 3)) {
                            Log.d(TAG, "Got byte count > 4, not orientation, continuing, formatCode=" + formatCode);
                        }
                    } else if (Log.isLoggable(TAG, 3)) {
                        Log.d(TAG, "Negative tiff component count");
                    }
                } else if (Log.isLoggable(TAG, 3)) {
                    Log.d(TAG, "Got invalid format code = " + formatCode);
                }
            }
        }
        return -1;
    }

    private static int calcTagOffset(int ifdOffset, int tagIndex) {
        return ifdOffset + 2 + (tagIndex * 12);
    }

    private static boolean handles(int imageMagicNumber) {
        return (imageMagicNumber & EXIF_MAGIC_NUMBER) == EXIF_MAGIC_NUMBER || imageMagicNumber == MOTOROLA_TIFF_MAGIC_NUMBER || imageMagicNumber == INTEL_TIFF_MAGIC_NUMBER;
    }

    private static final class RandomAccessReader {
        private final ByteBuffer data;

        RandomAccessReader(byte[] data2, int length) {
            this.data = (ByteBuffer) ByteBuffer.wrap(data2).order(ByteOrder.BIG_ENDIAN).limit(length);
        }

        /* access modifiers changed from: package-private */
        public void order(ByteOrder byteOrder) {
            this.data.order(byteOrder);
        }

        /* access modifiers changed from: package-private */
        public int length() {
            return this.data.remaining();
        }

        /* access modifiers changed from: package-private */
        public int getInt32(int offset) {
            if (isAvailable(offset, 4)) {
                return this.data.getInt(offset);
            }
            return -1;
        }

        /* access modifiers changed from: package-private */
        public short getInt16(int offset) {
            if (isAvailable(offset, 2)) {
                return this.data.getShort(offset);
            }
            return -1;
        }

        private boolean isAvailable(int offset, int byteSize) {
            return this.data.remaining() - offset >= byteSize;
        }
    }

    private static final class ByteBufferReader implements Reader {
        private final ByteBuffer byteBuffer;

        ByteBufferReader(ByteBuffer byteBuffer2) {
            this.byteBuffer = byteBuffer2;
            byteBuffer2.order(ByteOrder.BIG_ENDIAN);
        }

        public int getUInt16() {
            return ((getByte() << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (getByte() & 255);
        }

        public short getUInt8() {
            return (short) (getByte() & 255);
        }

        public long skip(long total) {
            int toSkip = (int) Math.min((long) this.byteBuffer.remaining(), total);
            this.byteBuffer.position(this.byteBuffer.position() + toSkip);
            return (long) toSkip;
        }

        public int read(byte[] buffer, int byteCount) {
            int toRead = Math.min(byteCount, this.byteBuffer.remaining());
            if (toRead == 0) {
                return -1;
            }
            this.byteBuffer.get(buffer, 0, toRead);
            return toRead;
        }

        public int getByte() {
            if (this.byteBuffer.remaining() < 1) {
                return -1;
            }
            return this.byteBuffer.get();
        }
    }

    private static final class StreamReader implements Reader {
        private final InputStream is;

        StreamReader(InputStream is2) {
            this.is = is2;
        }

        public int getUInt16() throws IOException {
            return ((this.is.read() << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (this.is.read() & 255);
        }

        public short getUInt8() throws IOException {
            return (short) (this.is.read() & 255);
        }

        public long skip(long total) throws IOException {
            if (total < 0) {
                return 0;
            }
            long toSkip = total;
            while (toSkip > 0) {
                long skipped = this.is.skip(toSkip);
                if (skipped > 0) {
                    toSkip -= skipped;
                } else if (this.is.read() == -1) {
                    break;
                } else {
                    toSkip--;
                }
            }
            return total - toSkip;
        }

        public int read(byte[] buffer, int byteCount) throws IOException {
            int toRead = byteCount;
            while (toRead > 0) {
                int read = this.is.read(buffer, byteCount - toRead, toRead);
                if (read == -1) {
                    break;
                }
                toRead -= read;
            }
            return byteCount - toRead;
        }

        public int getByte() throws IOException {
            return this.is.read();
        }
    }
}
