package com.yunos.tv.core.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import com.yunos.tv.core.degrade.ImageShowDegradeManager;

public class BitMapUtil {
    public static Bitmap readBmp(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        if (ImageShowDegradeManager.getInstance().isImageDegrade()) {
            opt.inPreferredConfig = Bitmap.Config.ARGB_4444;
        } else {
            opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        }
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        return BitmapFactory.decodeStream(context.getResources().openRawResource(resId), (Rect) null, opt);
    }

    public static BitmapDrawable getBmpDrawable(Context context, int resId) {
        return new BitmapDrawable(context.getResources(), readBmp(context, resId));
    }
}
