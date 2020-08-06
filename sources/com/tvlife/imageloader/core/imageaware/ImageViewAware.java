package com.tvlife.imageloader.core.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.tvlife.imageloader.core.assist.ViewScaleType;
import com.tvlife.imageloader.utils.L;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class ImageViewAware implements ImageAware {
    public static final String WARN_CANT_SET_BITMAP = "Can't set a bitmap into view. You should call ImageLoader on UI thread for it.";
    public static final String WARN_CANT_SET_DRAWABLE = "Can't set a drawable into view. You should call ImageLoader on UI thread for it.";
    private String TAG;
    protected boolean checkActualViewSize;
    protected Reference<ImageView> imageViewRef;

    public ImageViewAware(ImageView imageView) {
        this(imageView, true);
    }

    public ImageViewAware(ImageView imageView, boolean checkActualViewSize2) {
        this.TAG = "ImageViewAware";
        this.imageViewRef = new WeakReference(imageView);
        this.checkActualViewSize = checkActualViewSize2;
    }

    public int getWidth() {
        ImageView imageView = this.imageViewRef.get();
        if (imageView == null) {
            return 0;
        }
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        int width = 0;
        if (!(!this.checkActualViewSize || params == null || params.width == -2)) {
            width = imageView.getWidth();
        }
        if (width <= 0 && params != null) {
            width = params.width;
        }
        if (width <= 0) {
            return getImageViewFieldValue(imageView, "mMaxWidth");
        }
        return width;
    }

    public int getHeight() {
        ImageView imageView = this.imageViewRef.get();
        if (imageView == null) {
            return 0;
        }
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        int height = 0;
        if (!(!this.checkActualViewSize || params == null || params.height == -2)) {
            height = imageView.getHeight();
        }
        if (height <= 0 && params != null) {
            height = params.height;
        }
        if (height <= 0) {
            return getImageViewFieldValue(imageView, "mMaxHeight");
        }
        return height;
    }

    public ViewScaleType getScaleType() {
        ImageView imageView = this.imageViewRef.get();
        if (imageView != null) {
            return ViewScaleType.fromImageView(imageView);
        }
        return ViewScaleType.CROP;
    }

    public ImageView getWrappedView() {
        return this.imageViewRef.get();
    }

    public boolean isCollected() {
        return this.imageViewRef.get() == null;
    }

    public int getId() {
        ImageView imageView = this.imageViewRef.get();
        return imageView == null ? super.hashCode() : imageView.hashCode();
    }

    private static int getImageViewFieldValue(Object object, String fieldName) {
        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            int fieldValue = ((Integer) field.get(object)).intValue();
            if (fieldValue <= 0 || fieldValue >= Integer.MAX_VALUE) {
                return 0;
            }
            return fieldValue;
        } catch (Exception e) {
            L.e(e);
            return 0;
        }
    }

    public boolean setImageDrawable(Drawable drawable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            ImageView imageView = this.imageViewRef.get();
            if (imageView == null) {
                return false;
            }
            imageView.setImageDrawable(drawable);
            return true;
        }
        L.w("Can't set a drawable into view. You should call ImageLoader on UI thread for it.", new Object[0]);
        return false;
    }

    public boolean setImageBitmap(Bitmap bitmap) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            ImageView imageView = this.imageViewRef.get();
            if (imageView == null) {
                return false;
            }
            imageView.setImageBitmap(bitmap);
            return true;
        }
        L.w("Can't set a bitmap into view. You should call ImageLoader on UI thread for it.", new Object[0]);
        return false;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
    }
}
