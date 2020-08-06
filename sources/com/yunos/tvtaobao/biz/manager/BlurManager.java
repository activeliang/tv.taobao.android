package com.yunos.tvtaobao.biz.manager;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.widget.FrostedGlass;
import com.yunos.tvtaobao.businessview.R;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.ArrayList;
import java.util.List;

public class BlurManager {
    private static final String TAG = "BlurManager";

    private BlurManager() {
    }

    private static class Singleton {
        /* access modifiers changed from: private */
        public static BlurManager instance = new BlurManager();

        private Singleton() {
        }
    }

    public static BlurManager getInstance() {
        return Singleton.instance;
    }

    public boolean blurInYunOS(Activity activity) throws Throwable {
        Bitmap forstedglassBitmap = new FrostedGlass().getFrostedGlassBitmap(activity);
        if (forstedglassBitmap != null) {
            ZpLogger.d(TAG, "BlurManager.blurInYunOS.successfully screenshot");
            List<Drawable> drawableList = new ArrayList<>();
            drawableList.add(new BitmapDrawable(activity.getResources(), forstedglassBitmap));
            drawableList.add(new ColorDrawable(activity.getResources().getColor(R.color.bs_up_update_black_50)));
            Drawable[] layers = new Drawable[drawableList.size()];
            drawableList.toArray(layers);
            activity.getWindow().setBackgroundDrawable(new LayerDrawable(layers));
            return true;
        }
        ZpLogger.w(TAG, "BlurManager.blurInYunOS.cannot screenshot");
        return false;
    }
}
