package com.yunos.tvtaobao.homebundle.listener;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import com.yunos.tvtaobao.biz.dialog.util.SnapshotUtil;
import com.yunos.tvtaobao.businessview.R;
import java.lang.ref.WeakReference;

public class DetainMentFronstedGlassListenner implements SnapshotUtil.OnFronstedGlassSreenDoneListener {
    private Bitmap mBitmap = null;
    private WeakReference<View> mViewReference = null;

    public DetainMentFronstedGlassListenner(WeakReference<View> v) {
        this.mViewReference = v;
    }

    public void onFronstedGlassSreenDone(Bitmap bitmap) {
        this.mBitmap = bitmap;
        if (this.mViewReference != null && this.mViewReference.get() != null) {
            View view = (View) this.mViewReference.get();
            if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
                view.setBackgroundDrawable(new LayerDrawable(new Drawable[]{new BitmapDrawable(this.mBitmap), new ColorDrawable(view.getResources().getColor(R.color.ytbv_shadow_color_50))}));
            }
        }
    }

    public void onRecycleBitmap() {
        if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
            this.mBitmap.recycle();
        }
        this.mBitmap = null;
    }

    public void onDestroy() {
        onRecycleBitmap();
        this.mViewReference = null;
    }
}
