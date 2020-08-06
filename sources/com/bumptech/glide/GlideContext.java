package com.bumptech.glide;

import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.widget.ImageView;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTargetFactory;
import com.bumptech.glide.request.target.ViewTarget;
import java.util.List;
import java.util.Map;

public class GlideContext extends ContextWrapper {
    @VisibleForTesting
    static final TransitionOptions<?, ?> DEFAULT_TRANSITION_OPTIONS = new GenericTransitionOptions();
    private final ArrayPool arrayPool;
    private final List<RequestListener<Object>> defaultRequestListeners;
    private final RequestOptions defaultRequestOptions;
    private final Map<Class<?>, TransitionOptions<?, ?>> defaultTransitionOptions;
    private final Engine engine;
    private final ImageViewTargetFactory imageViewTargetFactory;
    private final boolean isLoggingRequestOriginsEnabled;
    private final int logLevel;
    private final Registry registry;

    public GlideContext(@NonNull Context context, @NonNull ArrayPool arrayPool2, @NonNull Registry registry2, @NonNull ImageViewTargetFactory imageViewTargetFactory2, @NonNull RequestOptions defaultRequestOptions2, @NonNull Map<Class<?>, TransitionOptions<?, ?>> defaultTransitionOptions2, @NonNull List<RequestListener<Object>> defaultRequestListeners2, @NonNull Engine engine2, boolean isLoggingRequestOriginsEnabled2, int logLevel2) {
        super(context.getApplicationContext());
        this.arrayPool = arrayPool2;
        this.registry = registry2;
        this.imageViewTargetFactory = imageViewTargetFactory2;
        this.defaultRequestOptions = defaultRequestOptions2;
        this.defaultRequestListeners = defaultRequestListeners2;
        this.defaultTransitionOptions = defaultTransitionOptions2;
        this.engine = engine2;
        this.isLoggingRequestOriginsEnabled = isLoggingRequestOriginsEnabled2;
        this.logLevel = logLevel2;
    }

    public List<RequestListener<Object>> getDefaultRequestListeners() {
        return this.defaultRequestListeners;
    }

    public RequestOptions getDefaultRequestOptions() {
        return this.defaultRequestOptions;
    }

    @NonNull
    public <T> TransitionOptions<?, T> getDefaultTransitionOptions(@NonNull Class<T> transcodeClass) {
        TransitionOptions<?, ?> result = this.defaultTransitionOptions.get(transcodeClass);
        if (result == null) {
            for (Map.Entry<Class<?>, TransitionOptions<?, ?>> value : this.defaultTransitionOptions.entrySet()) {
                if (value.getKey().isAssignableFrom(transcodeClass)) {
                    result = value.getValue();
                }
            }
        }
        if (result == null) {
            return DEFAULT_TRANSITION_OPTIONS;
        }
        return result;
    }

    @NonNull
    public <X> ViewTarget<ImageView, X> buildImageViewTarget(@NonNull ImageView imageView, @NonNull Class<X> transcodeClass) {
        return this.imageViewTargetFactory.buildTarget(imageView, transcodeClass);
    }

    @NonNull
    public Engine getEngine() {
        return this.engine;
    }

    @NonNull
    public Registry getRegistry() {
        return this.registry;
    }

    public int getLogLevel() {
        return this.logLevel;
    }

    @NonNull
    public ArrayPool getArrayPool() {
        return this.arrayPool;
    }

    public boolean isLoggingRequestOriginsEnabled() {
        return this.isLoggingRequestOriginsEnabled;
    }
}
