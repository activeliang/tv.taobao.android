package android.support.v4.print;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@TargetApi(19)
@RequiresApi(19)
class PrintHelperKitkat {
    public static final int COLOR_MODE_COLOR = 2;
    public static final int COLOR_MODE_MONOCHROME = 1;
    private static final String LOG_TAG = "PrintHelperKitkat";
    private static final int MAX_PRINT_SIZE = 3500;
    public static final int ORIENTATION_LANDSCAPE = 1;
    public static final int ORIENTATION_PORTRAIT = 2;
    public static final int SCALE_MODE_FILL = 2;
    public static final int SCALE_MODE_FIT = 1;
    int mColorMode = 2;
    final Context mContext;
    BitmapFactory.Options mDecodeOptions = null;
    protected boolean mIsMinMarginsHandlingCorrect = true;
    /* access modifiers changed from: private */
    public final Object mLock = new Object();
    int mOrientation;
    protected boolean mPrintActivityRespectsOrientation = true;
    int mScaleMode = 2;

    public interface OnPrintFinishCallback {
        void onFinish();
    }

    PrintHelperKitkat(Context context) {
        this.mContext = context;
    }

    public void setScaleMode(int scaleMode) {
        this.mScaleMode = scaleMode;
    }

    public int getScaleMode() {
        return this.mScaleMode;
    }

    public void setColorMode(int colorMode) {
        this.mColorMode = colorMode;
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    public int getOrientation() {
        if (this.mOrientation == 0) {
            return 1;
        }
        return this.mOrientation;
    }

    public int getColorMode() {
        return this.mColorMode;
    }

    /* access modifiers changed from: private */
    public static boolean isPortrait(Bitmap bitmap) {
        if (bitmap.getWidth() <= bitmap.getHeight()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public PrintAttributes.Builder copyAttributes(PrintAttributes other) {
        PrintAttributes.Builder b = new PrintAttributes.Builder().setMediaSize(other.getMediaSize()).setResolution(other.getResolution()).setMinMargins(other.getMinMargins());
        if (other.getColorMode() != 0) {
            b.setColorMode(other.getColorMode());
        }
        return b;
    }

    public void printBitmap(String jobName, Bitmap bitmap, OnPrintFinishCallback callback) {
        PrintAttributes.MediaSize mediaSize;
        if (bitmap != null) {
            final int fittingMode = this.mScaleMode;
            PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
            if (isPortrait(bitmap)) {
                mediaSize = PrintAttributes.MediaSize.UNKNOWN_PORTRAIT;
            } else {
                mediaSize = PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE;
            }
            final String str = jobName;
            final Bitmap bitmap2 = bitmap;
            final OnPrintFinishCallback onPrintFinishCallback = callback;
            printManager.print(jobName, new PrintDocumentAdapter() {
                private PrintAttributes mAttributes;

                public void onLayout(PrintAttributes oldPrintAttributes, PrintAttributes newPrintAttributes, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
                    boolean changed = true;
                    this.mAttributes = newPrintAttributes;
                    PrintDocumentInfo info = new PrintDocumentInfo.Builder(str).setContentType(1).setPageCount(1).build();
                    if (newPrintAttributes.equals(oldPrintAttributes)) {
                        changed = false;
                    }
                    layoutResultCallback.onLayoutFinished(info, changed);
                }

                public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
                    PrintHelperKitkat.this.writeBitmap(this.mAttributes, fittingMode, bitmap2, fileDescriptor, cancellationSignal, writeResultCallback);
                }

                public void onFinish() {
                    if (onPrintFinishCallback != null) {
                        onPrintFinishCallback.onFinish();
                    }
                }
            }, new PrintAttributes.Builder().setMediaSize(mediaSize).setColorMode(this.mColorMode).build());
        }
    }

    /* access modifiers changed from: private */
    public Matrix getMatrix(int imageWidth, int imageHeight, RectF content, int fittingMode) {
        float scale;
        Matrix matrix = new Matrix();
        float scale2 = content.width() / ((float) imageWidth);
        if (fittingMode == 2) {
            scale = Math.max(scale2, content.height() / ((float) imageHeight));
        } else {
            scale = Math.min(scale2, content.height() / ((float) imageHeight));
        }
        matrix.postScale(scale, scale);
        matrix.postTranslate((content.width() - (((float) imageWidth) * scale)) / 2.0f, (content.height() - (((float) imageHeight) * scale)) / 2.0f);
        return matrix;
    }

    /* access modifiers changed from: private */
    public void writeBitmap(PrintAttributes attributes, int fittingMode, Bitmap bitmap, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
        final PrintAttributes pdfAttributes;
        if (this.mIsMinMarginsHandlingCorrect) {
            pdfAttributes = attributes;
        } else {
            pdfAttributes = copyAttributes(attributes).setMinMargins(new PrintAttributes.Margins(0, 0, 0, 0)).build();
        }
        final CancellationSignal cancellationSignal2 = cancellationSignal;
        final Bitmap bitmap2 = bitmap;
        final PrintAttributes printAttributes = attributes;
        final int i = fittingMode;
        final ParcelFileDescriptor parcelFileDescriptor = fileDescriptor;
        final PrintDocumentAdapter.WriteResultCallback writeResultCallback2 = writeResultCallback;
        new AsyncTask<Void, Void, Throwable>() {
            /* access modifiers changed from: protected */
            /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
            /* JADX WARNING: Unknown top exception splitter block from list: {B:33:0x00b3=Splitter:B:33:0x00b3, B:46:0x00e4=Splitter:B:46:0x00e4, B:20:0x0078=Splitter:B:20:0x0078} */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public java.lang.Throwable doInBackground(java.lang.Void... r13) {
                /*
                    r12 = this;
                    r7 = 0
                    android.os.CancellationSignal r8 = r2     // Catch:{ Throwable -> 0x0080 }
                    boolean r8 = r8.isCanceled()     // Catch:{ Throwable -> 0x0080 }
                    if (r8 == 0) goto L_0x000a
                L_0x0009:
                    return r7
                L_0x000a:
                    android.print.pdf.PrintedPdfDocument r6 = new android.print.pdf.PrintedPdfDocument     // Catch:{ Throwable -> 0x0080 }
                    android.support.v4.print.PrintHelperKitkat r8 = android.support.v4.print.PrintHelperKitkat.this     // Catch:{ Throwable -> 0x0080 }
                    android.content.Context r8 = r8.mContext     // Catch:{ Throwable -> 0x0080 }
                    android.print.PrintAttributes r9 = r3     // Catch:{ Throwable -> 0x0080 }
                    r6.<init>(r8, r9)     // Catch:{ Throwable -> 0x0080 }
                    android.support.v4.print.PrintHelperKitkat r8 = android.support.v4.print.PrintHelperKitkat.this     // Catch:{ Throwable -> 0x0080 }
                    android.graphics.Bitmap r9 = r4     // Catch:{ Throwable -> 0x0080 }
                    android.print.PrintAttributes r10 = r3     // Catch:{ Throwable -> 0x0080 }
                    int r10 = r10.getColorMode()     // Catch:{ Throwable -> 0x0080 }
                    android.graphics.Bitmap r4 = r8.convertBitmapForColorMode(r9, r10)     // Catch:{ Throwable -> 0x0080 }
                    android.os.CancellationSignal r8 = r2     // Catch:{ Throwable -> 0x0080 }
                    boolean r8 = r8.isCanceled()     // Catch:{ Throwable -> 0x0080 }
                    if (r8 != 0) goto L_0x0009
                    r8 = 1
                    android.graphics.pdf.PdfDocument$Page r5 = r6.startPage(r8)     // Catch:{ all -> 0x00a6 }
                    android.support.v4.print.PrintHelperKitkat r8 = android.support.v4.print.PrintHelperKitkat.this     // Catch:{ all -> 0x00a6 }
                    boolean r8 = r8.mIsMinMarginsHandlingCorrect     // Catch:{ all -> 0x00a6 }
                    if (r8 == 0) goto L_0x0082
                    android.graphics.RectF r0 = new android.graphics.RectF     // Catch:{ all -> 0x00a6 }
                    android.graphics.pdf.PdfDocument$PageInfo r8 = r5.getInfo()     // Catch:{ all -> 0x00a6 }
                    android.graphics.Rect r8 = r8.getContentRect()     // Catch:{ all -> 0x00a6 }
                    r0.<init>(r8)     // Catch:{ all -> 0x00a6 }
                L_0x0043:
                    android.support.v4.print.PrintHelperKitkat r8 = android.support.v4.print.PrintHelperKitkat.this     // Catch:{ all -> 0x00a6 }
                    int r9 = r4.getWidth()     // Catch:{ all -> 0x00a6 }
                    int r10 = r4.getHeight()     // Catch:{ all -> 0x00a6 }
                    int r11 = r6     // Catch:{ all -> 0x00a6 }
                    android.graphics.Matrix r3 = r8.getMatrix(r9, r10, r0, r11)     // Catch:{ all -> 0x00a6 }
                    android.support.v4.print.PrintHelperKitkat r8 = android.support.v4.print.PrintHelperKitkat.this     // Catch:{ all -> 0x00a6 }
                    boolean r8 = r8.mIsMinMarginsHandlingCorrect     // Catch:{ all -> 0x00a6 }
                    if (r8 == 0) goto L_0x00bb
                L_0x0059:
                    android.graphics.Canvas r8 = r5.getCanvas()     // Catch:{ all -> 0x00a6 }
                    r9 = 0
                    r8.drawBitmap(r4, r3, r9)     // Catch:{ all -> 0x00a6 }
                    r6.finishPage(r5)     // Catch:{ all -> 0x00a6 }
                    android.os.CancellationSignal r8 = r2     // Catch:{ all -> 0x00a6 }
                    boolean r8 = r8.isCanceled()     // Catch:{ all -> 0x00a6 }
                    if (r8 == 0) goto L_0x00ca
                    r6.close()     // Catch:{ Throwable -> 0x0080 }
                    android.os.ParcelFileDescriptor r8 = r7     // Catch:{ Throwable -> 0x0080 }
                    if (r8 == 0) goto L_0x0078
                    android.os.ParcelFileDescriptor r8 = r7     // Catch:{ IOException -> 0x00f1 }
                    r8.close()     // Catch:{ IOException -> 0x00f1 }
                L_0x0078:
                    android.graphics.Bitmap r8 = r4     // Catch:{ Throwable -> 0x0080 }
                    if (r4 == r8) goto L_0x0009
                    r4.recycle()     // Catch:{ Throwable -> 0x0080 }
                    goto L_0x0009
                L_0x0080:
                    r7 = move-exception
                    goto L_0x0009
                L_0x0082:
                    android.print.pdf.PrintedPdfDocument r1 = new android.print.pdf.PrintedPdfDocument     // Catch:{ all -> 0x00a6 }
                    android.support.v4.print.PrintHelperKitkat r8 = android.support.v4.print.PrintHelperKitkat.this     // Catch:{ all -> 0x00a6 }
                    android.content.Context r8 = r8.mContext     // Catch:{ all -> 0x00a6 }
                    android.print.PrintAttributes r9 = r5     // Catch:{ all -> 0x00a6 }
                    r1.<init>(r8, r9)     // Catch:{ all -> 0x00a6 }
                    r8 = 1
                    android.graphics.pdf.PdfDocument$Page r2 = r1.startPage(r8)     // Catch:{ all -> 0x00a6 }
                    android.graphics.RectF r0 = new android.graphics.RectF     // Catch:{ all -> 0x00a6 }
                    android.graphics.pdf.PdfDocument$PageInfo r8 = r2.getInfo()     // Catch:{ all -> 0x00a6 }
                    android.graphics.Rect r8 = r8.getContentRect()     // Catch:{ all -> 0x00a6 }
                    r0.<init>(r8)     // Catch:{ all -> 0x00a6 }
                    r1.finishPage(r2)     // Catch:{ all -> 0x00a6 }
                    r1.close()     // Catch:{ all -> 0x00a6 }
                    goto L_0x0043
                L_0x00a6:
                    r8 = move-exception
                    r6.close()     // Catch:{ Throwable -> 0x0080 }
                    android.os.ParcelFileDescriptor r9 = r7     // Catch:{ Throwable -> 0x0080 }
                    if (r9 == 0) goto L_0x00b3
                    android.os.ParcelFileDescriptor r9 = r7     // Catch:{ IOException -> 0x00ed }
                    r9.close()     // Catch:{ IOException -> 0x00ed }
                L_0x00b3:
                    android.graphics.Bitmap r9 = r4     // Catch:{ Throwable -> 0x0080 }
                    if (r4 == r9) goto L_0x00ba
                    r4.recycle()     // Catch:{ Throwable -> 0x0080 }
                L_0x00ba:
                    throw r8     // Catch:{ Throwable -> 0x0080 }
                L_0x00bb:
                    float r8 = r0.left     // Catch:{ all -> 0x00a6 }
                    float r9 = r0.top     // Catch:{ all -> 0x00a6 }
                    r3.postTranslate(r8, r9)     // Catch:{ all -> 0x00a6 }
                    android.graphics.Canvas r8 = r5.getCanvas()     // Catch:{ all -> 0x00a6 }
                    r8.clipRect(r0)     // Catch:{ all -> 0x00a6 }
                    goto L_0x0059
                L_0x00ca:
                    java.io.FileOutputStream r8 = new java.io.FileOutputStream     // Catch:{ all -> 0x00a6 }
                    android.os.ParcelFileDescriptor r9 = r7     // Catch:{ all -> 0x00a6 }
                    java.io.FileDescriptor r9 = r9.getFileDescriptor()     // Catch:{ all -> 0x00a6 }
                    r8.<init>(r9)     // Catch:{ all -> 0x00a6 }
                    r6.writeTo(r8)     // Catch:{ all -> 0x00a6 }
                    r6.close()     // Catch:{ Throwable -> 0x0080 }
                    android.os.ParcelFileDescriptor r8 = r7     // Catch:{ Throwable -> 0x0080 }
                    if (r8 == 0) goto L_0x00e4
                    android.os.ParcelFileDescriptor r8 = r7     // Catch:{ IOException -> 0x00ef }
                    r8.close()     // Catch:{ IOException -> 0x00ef }
                L_0x00e4:
                    android.graphics.Bitmap r8 = r4     // Catch:{ Throwable -> 0x0080 }
                    if (r4 == r8) goto L_0x0009
                    r4.recycle()     // Catch:{ Throwable -> 0x0080 }
                    goto L_0x0009
                L_0x00ed:
                    r9 = move-exception
                    goto L_0x00b3
                L_0x00ef:
                    r8 = move-exception
                    goto L_0x00e4
                L_0x00f1:
                    r8 = move-exception
                    goto L_0x0078
                */
                throw new UnsupportedOperationException("Method not decompiled: android.support.v4.print.PrintHelperKitkat.AnonymousClass2.doInBackground(java.lang.Void[]):java.lang.Throwable");
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(Throwable throwable) {
                if (cancellationSignal2.isCanceled()) {
                    writeResultCallback2.onWriteCancelled();
                } else if (throwable == null) {
                    writeResultCallback2.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
                } else {
                    Log.e(PrintHelperKitkat.LOG_TAG, "Error writing printed content", throwable);
                    writeResultCallback2.onWriteFailed((CharSequence) null);
                }
            }
        }.execute(new Void[0]);
    }

    public void printBitmap(String jobName, Uri imageFile, OnPrintFinishCallback callback) throws FileNotFoundException {
        final int fittingMode = this.mScaleMode;
        final String str = jobName;
        final Uri uri = imageFile;
        final OnPrintFinishCallback onPrintFinishCallback = callback;
        PrintDocumentAdapter printDocumentAdapter = new PrintDocumentAdapter() {
            /* access modifiers changed from: private */
            public PrintAttributes mAttributes;
            Bitmap mBitmap = null;
            AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;

            public void onLayout(PrintAttributes oldPrintAttributes, PrintAttributes newPrintAttributes, CancellationSignal cancellationSignal, PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle bundle) {
                boolean changed = true;
                synchronized (this) {
                    this.mAttributes = newPrintAttributes;
                }
                if (cancellationSignal.isCanceled()) {
                    layoutResultCallback.onLayoutCancelled();
                } else if (this.mBitmap != null) {
                    PrintDocumentInfo info = new PrintDocumentInfo.Builder(str).setContentType(1).setPageCount(1).build();
                    if (newPrintAttributes.equals(oldPrintAttributes)) {
                        changed = false;
                    }
                    layoutResultCallback.onLayoutFinished(info, changed);
                } else {
                    final CancellationSignal cancellationSignal2 = cancellationSignal;
                    final PrintAttributes printAttributes = newPrintAttributes;
                    final PrintAttributes printAttributes2 = oldPrintAttributes;
                    final PrintDocumentAdapter.LayoutResultCallback layoutResultCallback2 = layoutResultCallback;
                    this.mLoadBitmap = new AsyncTask<Uri, Boolean, Bitmap>() {
                        /* access modifiers changed from: protected */
                        public void onPreExecute() {
                            cancellationSignal2.setOnCancelListener(new CancellationSignal.OnCancelListener() {
                                public void onCancel() {
                                    AnonymousClass3.this.cancelLoad();
                                    AnonymousClass1.this.cancel(false);
                                }
                            });
                        }

                        /* access modifiers changed from: protected */
                        public Bitmap doInBackground(Uri... uris) {
                            try {
                                return PrintHelperKitkat.this.loadConstrainedBitmap(uri, PrintHelperKitkat.MAX_PRINT_SIZE);
                            } catch (FileNotFoundException e) {
                                return null;
                            }
                        }

                        /* access modifiers changed from: protected */
                        public void onPostExecute(Bitmap bitmap) {
                            boolean changed;
                            PrintAttributes.MediaSize mediaSize;
                            super.onPostExecute(bitmap);
                            if (bitmap != null && (!PrintHelperKitkat.this.mPrintActivityRespectsOrientation || PrintHelperKitkat.this.mOrientation == 0)) {
                                synchronized (this) {
                                    mediaSize = AnonymousClass3.this.mAttributes.getMediaSize();
                                }
                                if (!(mediaSize == null || mediaSize.isPortrait() == PrintHelperKitkat.isPortrait(bitmap))) {
                                    Matrix rotation = new Matrix();
                                    rotation.postRotate(90.0f);
                                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), rotation, true);
                                }
                            }
                            AnonymousClass3.this.mBitmap = bitmap;
                            if (bitmap != null) {
                                PrintDocumentInfo info = new PrintDocumentInfo.Builder(str).setContentType(1).setPageCount(1).build();
                                if (!printAttributes.equals(printAttributes2)) {
                                    changed = true;
                                } else {
                                    changed = false;
                                }
                                layoutResultCallback2.onLayoutFinished(info, changed);
                            } else {
                                layoutResultCallback2.onLayoutFailed((CharSequence) null);
                            }
                            AnonymousClass3.this.mLoadBitmap = null;
                        }

                        /* access modifiers changed from: protected */
                        public void onCancelled(Bitmap result) {
                            layoutResultCallback2.onLayoutCancelled();
                            AnonymousClass3.this.mLoadBitmap = null;
                        }
                    }.execute(new Uri[0]);
                }
            }

            /* access modifiers changed from: private */
            public void cancelLoad() {
                synchronized (PrintHelperKitkat.this.mLock) {
                    if (PrintHelperKitkat.this.mDecodeOptions != null) {
                        PrintHelperKitkat.this.mDecodeOptions.requestCancelDecode();
                        PrintHelperKitkat.this.mDecodeOptions = null;
                    }
                }
            }

            public void onFinish() {
                super.onFinish();
                cancelLoad();
                if (this.mLoadBitmap != null) {
                    this.mLoadBitmap.cancel(true);
                }
                if (onPrintFinishCallback != null) {
                    onPrintFinishCallback.onFinish();
                }
                if (this.mBitmap != null) {
                    this.mBitmap.recycle();
                    this.mBitmap = null;
                }
            }

            public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor fileDescriptor, CancellationSignal cancellationSignal, PrintDocumentAdapter.WriteResultCallback writeResultCallback) {
                PrintHelperKitkat.this.writeBitmap(this.mAttributes, fittingMode, this.mBitmap, fileDescriptor, cancellationSignal, writeResultCallback);
            }
        };
        PrintManager printManager = (PrintManager) this.mContext.getSystemService("print");
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setColorMode(this.mColorMode);
        if (this.mOrientation == 1 || this.mOrientation == 0) {
            builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
        } else if (this.mOrientation == 2) {
            builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
        }
        printManager.print(jobName, printDocumentAdapter, builder.build());
    }

    /* access modifiers changed from: private */
    public Bitmap loadConstrainedBitmap(Uri uri, int maxSideLength) throws FileNotFoundException {
        BitmapFactory.Options decodeOptions;
        Bitmap bitmap = null;
        if (maxSideLength <= 0 || uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to getScaledBitmap");
        }
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        loadBitmap(uri, opt);
        int w = opt.outWidth;
        int h = opt.outHeight;
        if (w > 0 && h > 0) {
            int imageSide = Math.max(w, h);
            int sampleSize = 1;
            while (imageSide > maxSideLength) {
                imageSide >>>= 1;
                sampleSize <<= 1;
            }
            if (sampleSize > 0 && Math.min(w, h) / sampleSize > 0) {
                synchronized (this.mLock) {
                    this.mDecodeOptions = new BitmapFactory.Options();
                    this.mDecodeOptions.inMutable = true;
                    this.mDecodeOptions.inSampleSize = sampleSize;
                    decodeOptions = this.mDecodeOptions;
                }
                try {
                    bitmap = loadBitmap(uri, decodeOptions);
                    synchronized (this.mLock) {
                        this.mDecodeOptions = null;
                    }
                } catch (Throwable th) {
                    synchronized (this.mLock) {
                        this.mDecodeOptions = null;
                        throw th;
                    }
                }
            }
        }
        return bitmap;
    }

    private Bitmap loadBitmap(Uri uri, BitmapFactory.Options o) throws FileNotFoundException {
        if (uri == null || this.mContext == null) {
            throw new IllegalArgumentException("bad argument to loadBitmap");
        }
        InputStream is = null;
        try {
            is = this.mContext.getContentResolver().openInputStream(uri);
            Bitmap decodeStream = BitmapFactory.decodeStream(is, (Rect) null, o);
            if (is != null) {
                try {
                } catch (IOException t) {
                    Log.w(LOG_TAG, "close fail ", t);
                }
            }
            return decodeStream;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException t2) {
                    Log.w(LOG_TAG, "close fail ", t2);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public Bitmap convertBitmapForColorMode(Bitmap original, int colorMode) {
        if (colorMode != 1) {
            return original;
        }
        Bitmap grayscale = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(grayscale);
        Paint p = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0.0f);
        p.setColorFilter(new ColorMatrixColorFilter(cm));
        c.drawBitmap(original, 0.0f, 0.0f, p);
        c.setBitmap((Bitmap) null);
        return grayscale;
    }
}
