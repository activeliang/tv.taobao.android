package com.yunos.tv.core.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.taobao.windvane.jsbridge.utils.WVUtils;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import anet.channel.util.HttpConstant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.degrade.ImageShowDegradeManager;
import java.util.Locale;

public class GlideManager {
    private static GlideManager mGlideManager;

    public static GlideManager get() {
        if (mGlideManager == null) {
            synchronized (GlideManager.class) {
                if (mGlideManager == null) {
                    mGlideManager = new GlideManager();
                }
            }
        }
        return mGlideManager;
    }

    @Deprecated
    public void displayImage(String url, ImageView imageView) {
        displayImage((Context) CoreApplication.getApplication(), url, imageView);
    }

    public void displayImage(Context context, String url, ImageView imageView) {
        displayImage(context, url, imageView, (RequestOptions) null);
    }

    public void displayImage(Context context, String url, ImageView imageView, RequestOptions requestOptions) {
        RequestBuilder<ResourceType> requestBuilder = getRequestBuilder(Glide.with(context).asDrawable(), url);
        if (requestOptions != null) {
            requestBuilder = requestBuilder.apply((BaseRequestOptions<?>) requestOptions);
        }
        requestBuilder.into(imageView);
    }

    public void displayImage(Activity activity, String url, ImageView imageView) {
        displayImage(activity, url, imageView, (RequestOptions) null);
    }

    public void displayImage(Activity activity, String url, ImageView imageView, RequestOptions requestOptions) {
        if (activity != null && !activity.isFinishing()) {
            RequestBuilder<ResourceType> requestBuilder = getRequestBuilder(Glide.with(activity).asDrawable(), url);
            if (requestOptions != null) {
                requestBuilder = requestBuilder.apply((BaseRequestOptions<?>) requestOptions);
            }
            requestBuilder.into(imageView);
        }
    }

    public void displayImage(Fragment fragment, String url, ImageView imageView) {
        displayImage(fragment, url, imageView, (RequestOptions) null);
    }

    public void displayImage(Fragment fragment, String url, ImageView imageView, RequestOptions requestOptions) {
        RequestBuilder<ResourceType> requestBuilder = getRequestBuilder(Glide.with(fragment).asDrawable(), url);
        if (requestOptions != null) {
            requestBuilder = requestBuilder.apply((BaseRequestOptions<?>) requestOptions);
        }
        requestBuilder.into(imageView);
    }

    public void displayImage(View view, String url, ImageView imageView) {
        displayImage(view, url, imageView, (RequestOptions) null);
    }

    public void displayImage(View view, String url, ImageView imageView, RequestOptions requestOptions) {
        RequestBuilder<ResourceType> requestBuilder = getRequestBuilder(Glide.with(view).asDrawable(), url);
        if (requestOptions != null) {
            requestBuilder = requestBuilder.apply((BaseRequestOptions<?>) requestOptions);
        }
        requestBuilder.into(imageView);
    }

    public void loadImage(Context context, String url, Target<Bitmap> target) {
        loadImage(context, url, (RequestOptions) null, target);
    }

    public void loadImage(Context context, String url, Drawable placeHolder, Target<Bitmap> target) {
        RequestOptions ro = new RequestOptions();
        ro.placeholder(placeHolder);
        loadImage(context, url, ro, target);
    }

    public void loadImage(Context context, String url, int placeHolderResId, Target<Bitmap> target) {
        RequestOptions ro = new RequestOptions();
        ro.placeholder(placeHolderResId);
        loadImage(context, url, ro, target);
    }

    public void loadImage(Context context, String url, RequestOptions requestOptions, Target<Bitmap> target) {
        RequestBuilder<ResourceType> requestBuilder = getRequestBuilder(Glide.with(context).asBitmap(), url);
        if (requestOptions != null) {
            requestBuilder = requestBuilder.apply((BaseRequestOptions<?>) requestOptions);
        }
        requestBuilder.into(target);
    }

    public void loadImage(View view, String url, Target<Bitmap> target) {
        loadImage(view, url, (RequestOptions) null, target);
    }

    public void loadImage(View view, String url, RequestOptions requestOptions, Target<Bitmap> target) {
        RequestBuilder<ResourceType> requestBuilder = getRequestBuilder(Glide.with(view).asBitmap(), url);
        if (requestOptions != null) {
            requestBuilder = requestBuilder.apply((BaseRequestOptions<?>) requestOptions);
        }
        requestBuilder.into(target);
    }

    private String checkHttpUrl(String url) {
        String http = url;
        if (TextUtils.isEmpty(http) || checkScheme(http)) {
            return http;
        }
        if (http.indexOf(WVUtils.URL_SEPARATOR) <= 0) {
            return "http:" + url;
        }
        return "http:" + WVUtils.URL_SEPARATOR + url;
    }

    private boolean checkScheme(String url) {
        switch (Scheme.ofUri(url)) {
            case HTTP:
            case HTTPS:
            case FILE:
            case CONTENT:
            case ASSETS:
            case DRAWABLE:
                return true;
            default:
                return false;
        }
    }

    private <ResourceType> RequestBuilder<ResourceType> getRequestBuilder(RequestBuilder<ResourceType> requestBuilder, String url) {
        RequestBuilder<ResourceType> requestBuilder2 = requestBuilder.load(checkHttpUrl(url));
        if (!ImageShowDegradeManager.getInstance().isImageLoaderDegrade() && !ImageShowDegradeManager.getInstance().isImageDegrade()) {
            return requestBuilder2;
        }
        if (ImageShowDegradeManager.getInstance().isImageLoaderDegrade()) {
            requestBuilder2 = (RequestBuilder) requestBuilder2.skipMemoryCache(true);
        }
        if (ImageShowDegradeManager.getInstance().isImageDegrade()) {
            return (RequestBuilder) requestBuilder2.format(DecodeFormat.PREFER_RGB_565);
        }
        return requestBuilder2;
    }

    public enum Scheme {
        HTTP("http"),
        HTTPS("https"),
        FILE("file"),
        CONTENT("content"),
        ASSETS("assets"),
        DRAWABLE("drawable"),
        UNKNOWN("");
        
        private String scheme;
        private String uriPrefix;

        private Scheme(String scheme2) {
            this.scheme = scheme2;
            this.uriPrefix = scheme2 + HttpConstant.SCHEME_SPLIT;
        }

        public static Scheme ofUri(String uri) {
            if (uri != null) {
                for (Scheme s : values()) {
                    if (s.belongsTo(uri)) {
                        return s;
                    }
                }
            }
            return UNKNOWN;
        }

        private boolean belongsTo(String uri) {
            return uri.toLowerCase(Locale.US).startsWith(this.uriPrefix);
        }

        public String wrap(String path) {
            return this.uriPrefix + path;
        }

        public String crop(String uri) {
            if (belongsTo(uri)) {
                return uri.substring(this.uriPrefix.length());
            }
            throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", new Object[]{uri, this.scheme}));
        }
    }
}
