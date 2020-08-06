package com.tvtaobao.android.marketgames.dfw.wares;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public interface IImageLoader {

    public interface ImgLoadListener {
        void onFailed();

        void onSuccess(Bitmap bitmap);
    }

    void load(Context context, String str, ImgLoadListener imgLoadListener);

    void loadInto(Context context, String str, ImageView imageView);
}
