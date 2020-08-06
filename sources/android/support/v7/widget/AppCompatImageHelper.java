package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.RestrictTo;
import android.support.v7.content.res.AppCompatResources;
import android.widget.ImageView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class AppCompatImageHelper {
    private final ImageView mView;

    public AppCompatImageHelper(ImageView view) {
        this.mView = view;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x000a, code lost:
        r0 = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(r7.mView.getContext(), r8, android.support.v7.appcompat.R.styleable.AppCompatImageView, r9, 0);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadFromAttributes(android.util.AttributeSet r8, int r9) {
        /*
            r7 = this;
            r6 = -1
            r0 = 0
            android.widget.ImageView r3 = r7.mView     // Catch:{ all -> 0x003c }
            android.graphics.drawable.Drawable r1 = r3.getDrawable()     // Catch:{ all -> 0x003c }
            if (r1 != 0) goto L_0x0031
            android.widget.ImageView r3 = r7.mView     // Catch:{ all -> 0x003c }
            android.content.Context r3 = r3.getContext()     // Catch:{ all -> 0x003c }
            int[] r4 = android.support.v7.appcompat.R.styleable.AppCompatImageView     // Catch:{ all -> 0x003c }
            r5 = 0
            android.support.v7.widget.TintTypedArray r0 = android.support.v7.widget.TintTypedArray.obtainStyledAttributes(r3, r8, r4, r9, r5)     // Catch:{ all -> 0x003c }
            int r3 = android.support.v7.appcompat.R.styleable.AppCompatImageView_srcCompat     // Catch:{ all -> 0x003c }
            r4 = -1
            int r2 = r0.getResourceId(r3, r4)     // Catch:{ all -> 0x003c }
            if (r2 == r6) goto L_0x0031
            android.widget.ImageView r3 = r7.mView     // Catch:{ all -> 0x003c }
            android.content.Context r3 = r3.getContext()     // Catch:{ all -> 0x003c }
            android.graphics.drawable.Drawable r1 = android.support.v7.content.res.AppCompatResources.getDrawable(r3, r2)     // Catch:{ all -> 0x003c }
            if (r1 == 0) goto L_0x0031
            android.widget.ImageView r3 = r7.mView     // Catch:{ all -> 0x003c }
            r3.setImageDrawable(r1)     // Catch:{ all -> 0x003c }
        L_0x0031:
            if (r1 == 0) goto L_0x0036
            android.support.v7.widget.DrawableUtils.fixDrawable(r1)     // Catch:{ all -> 0x003c }
        L_0x0036:
            if (r0 == 0) goto L_0x003b
            r0.recycle()
        L_0x003b:
            return
        L_0x003c:
            r3 = move-exception
            if (r0 == 0) goto L_0x0042
            r0.recycle()
        L_0x0042:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.v7.widget.AppCompatImageHelper.loadFromAttributes(android.util.AttributeSet, int):void");
    }

    public void setImageResource(int resId) {
        if (resId != 0) {
            Drawable d = AppCompatResources.getDrawable(this.mView.getContext(), resId);
            if (d != null) {
                DrawableUtils.fixDrawable(d);
            }
            this.mView.setImageDrawable(d);
            return;
        }
        this.mView.setImageDrawable((Drawable) null);
    }

    /* access modifiers changed from: package-private */
    public boolean hasOverlappingRendering() {
        Drawable background = this.mView.getBackground();
        if (Build.VERSION.SDK_INT < 21 || !(background instanceof RippleDrawable)) {
            return true;
        }
        return false;
    }
}
