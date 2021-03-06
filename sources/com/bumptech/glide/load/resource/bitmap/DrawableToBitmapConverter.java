package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Log;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import java.util.concurrent.locks.Lock;

final class DrawableToBitmapConverter {
    private static final BitmapPool NO_RECYCLE_BITMAP_POOL = new BitmapPoolAdapter() {
        public void put(Bitmap bitmap) {
        }
    };
    private static final String TAG = "DrawableToBitmap";

    private DrawableToBitmapConverter() {
    }

    @Nullable
    static Resource<Bitmap> convert(BitmapPool bitmapPool, Drawable drawable, int width, int height) {
        Drawable drawable2 = drawable.getCurrent();
        Bitmap result = null;
        boolean isRecycleable = false;
        if (drawable2 instanceof BitmapDrawable) {
            result = ((BitmapDrawable) drawable2).getBitmap();
        } else if (!(drawable2 instanceof Animatable)) {
            result = drawToBitmap(bitmapPool, drawable2, width, height);
            isRecycleable = true;
        }
        return BitmapResource.obtain(result, isRecycleable ? bitmapPool : NO_RECYCLE_BITMAP_POOL);
    }

    @Nullable
    private static Bitmap drawToBitmap(BitmapPool bitmapPool, Drawable drawable, int width, int height) {
        int targetWidth;
        int targetHeight;
        Bitmap result = null;
        if (width != Integer.MIN_VALUE || drawable.getIntrinsicWidth() > 0) {
            if (height != Integer.MIN_VALUE || drawable.getIntrinsicHeight() > 0) {
                if (drawable.getIntrinsicWidth() > 0) {
                    targetWidth = drawable.getIntrinsicWidth();
                } else {
                    targetWidth = width;
                }
                if (drawable.getIntrinsicHeight() > 0) {
                    targetHeight = drawable.getIntrinsicHeight();
                } else {
                    targetHeight = height;
                }
                Lock lock = TransformationUtils.getBitmapDrawableLock();
                lock.lock();
                result = bitmapPool.get(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
                try {
                    Canvas canvas = new Canvas(result);
                    drawable.setBounds(0, 0, targetWidth, targetHeight);
                    drawable.draw(canvas);
                    canvas.setBitmap((Bitmap) null);
                } finally {
                    lock.unlock();
                }
            } else if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Unable to draw " + drawable + " to Bitmap with Target.SIZE_ORIGINAL because the Drawable has no intrinsic height");
            }
        } else if (Log.isLoggable(TAG, 5)) {
            Log.w(TAG, "Unable to draw " + drawable + " to Bitmap with Target.SIZE_ORIGINAL because the Drawable has no intrinsic width");
        }
        return result;
    }
}
