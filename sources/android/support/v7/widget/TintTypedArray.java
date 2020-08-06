package android.support.v7.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RestrictTo;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.TypedValue;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TintTypedArray {
    private final Context mContext;
    private TypedValue mTypedValue;
    private final TypedArray mWrapped;

    public static TintTypedArray obtainStyledAttributes(Context context, AttributeSet set, int[] attrs) {
        return new TintTypedArray(context, context.obtainStyledAttributes(set, attrs));
    }

    public static TintTypedArray obtainStyledAttributes(Context context, AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes) {
        return new TintTypedArray(context, context.obtainStyledAttributes(set, attrs, defStyleAttr, defStyleRes));
    }

    public static TintTypedArray obtainStyledAttributes(Context context, int resid, int[] attrs) {
        return new TintTypedArray(context, context.obtainStyledAttributes(resid, attrs));
    }

    private TintTypedArray(Context context, TypedArray array) {
        this.mContext = context;
        this.mWrapped = array;
    }

    public Drawable getDrawable(int index) {
        int resourceId;
        if (!this.mWrapped.hasValue(index) || (resourceId = this.mWrapped.getResourceId(index, 0)) == 0) {
            return this.mWrapped.getDrawable(index);
        }
        return AppCompatResources.getDrawable(this.mContext, resourceId);
    }

    public Drawable getDrawableIfKnown(int index) {
        int resourceId;
        if (!this.mWrapped.hasValue(index) || (resourceId = this.mWrapped.getResourceId(index, 0)) == 0) {
            return null;
        }
        return AppCompatDrawableManager.get().getDrawable(this.mContext, resourceId, true);
    }

    public int length() {
        return this.mWrapped.length();
    }

    public int getIndexCount() {
        return this.mWrapped.getIndexCount();
    }

    public int getIndex(int at) {
        return this.mWrapped.getIndex(at);
    }

    public Resources getResources() {
        return this.mWrapped.getResources();
    }

    public CharSequence getText(int index) {
        return this.mWrapped.getText(index);
    }

    public String getString(int index) {
        return this.mWrapped.getString(index);
    }

    public String getNonResourceString(int index) {
        return this.mWrapped.getNonResourceString(index);
    }

    public boolean getBoolean(int index, boolean defValue) {
        return this.mWrapped.getBoolean(index, defValue);
    }

    public int getInt(int index, int defValue) {
        return this.mWrapped.getInt(index, defValue);
    }

    public float getFloat(int index, float defValue) {
        return this.mWrapped.getFloat(index, defValue);
    }

    public int getColor(int index, int defValue) {
        return this.mWrapped.getColor(index, defValue);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0011, code lost:
        r1 = android.support.v7.content.res.AppCompatResources.getColorStateList(r4.mContext, (r0 = r4.mWrapped.getResourceId(r5, 0)));
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.content.res.ColorStateList getColorStateList(int r5) {
        /*
            r4 = this;
            android.content.res.TypedArray r2 = r4.mWrapped
            boolean r2 = r2.hasValue(r5)
            if (r2 == 0) goto L_0x001a
            android.content.res.TypedArray r2 = r4.mWrapped
            r3 = 0
            int r0 = r2.getResourceId(r5, r3)
            if (r0 == 0) goto L_0x001a
            android.content.Context r2 = r4.mContext
            android.content.res.ColorStateList r1 = android.support.v7.content.res.AppCompatResources.getColorStateList(r2, r0)
            if (r1 == 0) goto L_0x001a
        L_0x0019:
            return r1
        L_0x001a:
            android.content.res.TypedArray r2 = r4.mWrapped
            android.content.res.ColorStateList r1 = r2.getColorStateList(r5)
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.TintTypedArray.getColorStateList(int):android.content.res.ColorStateList");
    }

    public int getInteger(int index, int defValue) {
        return this.mWrapped.getInteger(index, defValue);
    }

    public float getDimension(int index, float defValue) {
        return this.mWrapped.getDimension(index, defValue);
    }

    public int getDimensionPixelOffset(int index, int defValue) {
        return this.mWrapped.getDimensionPixelOffset(index, defValue);
    }

    public int getDimensionPixelSize(int index, int defValue) {
        return this.mWrapped.getDimensionPixelSize(index, defValue);
    }

    public int getLayoutDimension(int index, String name) {
        return this.mWrapped.getLayoutDimension(index, name);
    }

    public int getLayoutDimension(int index, int defValue) {
        return this.mWrapped.getLayoutDimension(index, defValue);
    }

    public float getFraction(int index, int base, int pbase, float defValue) {
        return this.mWrapped.getFraction(index, base, pbase, defValue);
    }

    public int getResourceId(int index, int defValue) {
        return this.mWrapped.getResourceId(index, defValue);
    }

    public CharSequence[] getTextArray(int index) {
        return this.mWrapped.getTextArray(index);
    }

    public boolean getValue(int index, TypedValue outValue) {
        return this.mWrapped.getValue(index, outValue);
    }

    public int getType(int index) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.mWrapped.getType(index);
        }
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        this.mWrapped.getValue(index, this.mTypedValue);
        return this.mTypedValue.type;
    }

    public boolean hasValue(int index) {
        return this.mWrapped.hasValue(index);
    }

    public TypedValue peekValue(int index) {
        return this.mWrapped.peekValue(index);
    }

    public String getPositionDescription() {
        return this.mWrapped.getPositionDescription();
    }

    public void recycle() {
        this.mWrapped.recycle();
    }

    public int getChangingConfigurations() {
        return this.mWrapped.getChangingConfigurations();
    }
}
