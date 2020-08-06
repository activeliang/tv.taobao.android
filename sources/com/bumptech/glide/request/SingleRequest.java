package com.bumptech.glide.request;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.GuardedBy;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pools;
import android.util.Log;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.DrawableDecoderCompat;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.List;
import java.util.concurrent.Executor;

public final class SingleRequest<R> implements Request, SizeReadyCallback, ResourceCallback, FactoryPools.Poolable {
    private static final String GLIDE_TAG = "Glide";
    private static final boolean IS_VERBOSE_LOGGABLE = Log.isLoggable(TAG, 2);
    private static final Pools.Pool<SingleRequest<?>> POOL = FactoryPools.threadSafe(150, new FactoryPools.Factory<SingleRequest<?>>() {
        public SingleRequest<?> create() {
            return new SingleRequest<>();
        }
    });
    private static final String TAG = "Request";
    private TransitionFactory<? super R> animationFactory;
    private Executor callbackExecutor;
    private Context context;
    private Engine engine;
    private Drawable errorDrawable;
    private Drawable fallbackDrawable;
    private GlideContext glideContext;
    private int height;
    private boolean isCallingCallbacks;
    private Engine.LoadStatus loadStatus;
    @Nullable
    private Object model;
    private int overrideHeight;
    private int overrideWidth;
    private Drawable placeholderDrawable;
    private Priority priority;
    private RequestCoordinator requestCoordinator;
    @Nullable
    private List<RequestListener<R>> requestListeners;
    private BaseRequestOptions<?> requestOptions;
    @Nullable
    private RuntimeException requestOrigin;
    private Resource<R> resource;
    private long startTime;
    private final StateVerifier stateVerifier;
    @GuardedBy("this")
    private Status status;
    @Nullable
    private final String tag;
    private Target<R> target;
    @Nullable
    private RequestListener<R> targetListener;
    private Class<R> transcodeClass;
    private int width;

    private enum Status {
        PENDING,
        RUNNING,
        WAITING_FOR_SIZE,
        COMPLETE,
        FAILED,
        CLEARED
    }

    public static <R> SingleRequest<R> obtain(Context context2, GlideContext glideContext2, Object model2, Class<R> transcodeClass2, BaseRequestOptions<?> requestOptions2, int overrideWidth2, int overrideHeight2, Priority priority2, Target<R> target2, RequestListener<R> targetListener2, @Nullable List<RequestListener<R>> requestListeners2, RequestCoordinator requestCoordinator2, Engine engine2, TransitionFactory<? super R> animationFactory2, Executor callbackExecutor2) {
        SingleRequest<R> request = POOL.acquire();
        if (request == null) {
            request = new SingleRequest<>();
        }
        request.init(context2, glideContext2, model2, transcodeClass2, requestOptions2, overrideWidth2, overrideHeight2, priority2, target2, targetListener2, requestListeners2, requestCoordinator2, engine2, animationFactory2, callbackExecutor2);
        return request;
    }

    SingleRequest() {
        this.tag = IS_VERBOSE_LOGGABLE ? String.valueOf(super.hashCode()) : null;
        this.stateVerifier = StateVerifier.newInstance();
    }

    private synchronized void init(Context context2, GlideContext glideContext2, Object model2, Class<R> transcodeClass2, BaseRequestOptions<?> requestOptions2, int overrideWidth2, int overrideHeight2, Priority priority2, Target<R> target2, RequestListener<R> targetListener2, @Nullable List<RequestListener<R>> requestListeners2, RequestCoordinator requestCoordinator2, Engine engine2, TransitionFactory<? super R> animationFactory2, Executor callbackExecutor2) {
        this.context = context2;
        this.glideContext = glideContext2;
        this.model = model2;
        this.transcodeClass = transcodeClass2;
        this.requestOptions = requestOptions2;
        this.overrideWidth = overrideWidth2;
        this.overrideHeight = overrideHeight2;
        this.priority = priority2;
        this.target = target2;
        this.targetListener = targetListener2;
        this.requestListeners = requestListeners2;
        this.requestCoordinator = requestCoordinator2;
        this.engine = engine2;
        this.animationFactory = animationFactory2;
        this.callbackExecutor = callbackExecutor2;
        this.status = Status.PENDING;
        if (this.requestOrigin == null && glideContext2.isLoggingRequestOriginsEnabled()) {
            this.requestOrigin = new RuntimeException("Glide request origin trace");
        }
    }

    @NonNull
    public StateVerifier getVerifier() {
        return this.stateVerifier;
    }

    public synchronized void recycle() {
        assertNotCallingCallbacks();
        this.context = null;
        this.glideContext = null;
        this.model = null;
        this.transcodeClass = null;
        this.requestOptions = null;
        this.overrideWidth = -1;
        this.overrideHeight = -1;
        this.target = null;
        this.requestListeners = null;
        this.targetListener = null;
        this.requestCoordinator = null;
        this.animationFactory = null;
        this.loadStatus = null;
        this.errorDrawable = null;
        this.placeholderDrawable = null;
        this.fallbackDrawable = null;
        this.width = -1;
        this.height = -1;
        this.requestOrigin = null;
        POOL.release(this);
    }

    public synchronized void begin() {
        assertNotCallingCallbacks();
        this.stateVerifier.throwIfRecycled();
        this.startTime = LogTime.getLogTime();
        if (this.model == null) {
            if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
                this.width = this.overrideWidth;
                this.height = this.overrideHeight;
            }
            onLoadFailed(new GlideException("Received null model"), getFallbackDrawable() == null ? 5 : 3);
        } else if (this.status == Status.RUNNING) {
            throw new IllegalArgumentException("Cannot restart a running request");
        } else if (this.status == Status.COMPLETE) {
            onResourceReady(this.resource, DataSource.MEMORY_CACHE);
        } else {
            this.status = Status.WAITING_FOR_SIZE;
            if (Util.isValidDimensions(this.overrideWidth, this.overrideHeight)) {
                onSizeReady(this.overrideWidth, this.overrideHeight);
            } else {
                this.target.getSize(this);
            }
            if ((this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE) && canNotifyStatusChanged()) {
                this.target.onLoadStarted(getPlaceholderDrawable());
            }
            if (IS_VERBOSE_LOGGABLE) {
                logV("finished run method in " + LogTime.getElapsedMillis(this.startTime));
            }
        }
    }

    private void cancel() {
        assertNotCallingCallbacks();
        this.stateVerifier.throwIfRecycled();
        this.target.removeCallback(this);
        if (this.loadStatus != null) {
            this.loadStatus.cancel();
            this.loadStatus = null;
        }
    }

    private void assertNotCallingCallbacks() {
        if (this.isCallingCallbacks) {
            throw new IllegalStateException("You can't start or clear loads in RequestListener or Target callbacks. If you're trying to start a fallback request when a load fails, use RequestBuilder#error(RequestBuilder). Otherwise consider posting your into() or clear() calls to the main thread using a Handler instead.");
        }
    }

    public synchronized void clear() {
        assertNotCallingCallbacks();
        this.stateVerifier.throwIfRecycled();
        if (this.status != Status.CLEARED) {
            cancel();
            if (this.resource != null) {
                releaseResource(this.resource);
            }
            if (canNotifyCleared()) {
                this.target.onLoadCleared(getPlaceholderDrawable());
            }
            this.status = Status.CLEARED;
        }
    }

    private void releaseResource(Resource<?> resource2) {
        this.engine.release(resource2);
        this.resource = null;
    }

    public synchronized boolean isRunning() {
        return this.status == Status.RUNNING || this.status == Status.WAITING_FOR_SIZE;
    }

    public synchronized boolean isComplete() {
        return this.status == Status.COMPLETE;
    }

    public synchronized boolean isResourceSet() {
        return isComplete();
    }

    public synchronized boolean isCleared() {
        return this.status == Status.CLEARED;
    }

    public synchronized boolean isFailed() {
        return this.status == Status.FAILED;
    }

    private Drawable getErrorDrawable() {
        if (this.errorDrawable == null) {
            this.errorDrawable = this.requestOptions.getErrorPlaceholder();
            if (this.errorDrawable == null && this.requestOptions.getErrorId() > 0) {
                this.errorDrawable = loadDrawable(this.requestOptions.getErrorId());
            }
        }
        return this.errorDrawable;
    }

    private Drawable getPlaceholderDrawable() {
        if (this.placeholderDrawable == null) {
            this.placeholderDrawable = this.requestOptions.getPlaceholderDrawable();
            if (this.placeholderDrawable == null && this.requestOptions.getPlaceholderId() > 0) {
                this.placeholderDrawable = loadDrawable(this.requestOptions.getPlaceholderId());
            }
        }
        return this.placeholderDrawable;
    }

    private Drawable getFallbackDrawable() {
        if (this.fallbackDrawable == null) {
            this.fallbackDrawable = this.requestOptions.getFallbackDrawable();
            if (this.fallbackDrawable == null && this.requestOptions.getFallbackId() > 0) {
                this.fallbackDrawable = loadDrawable(this.requestOptions.getFallbackId());
            }
        }
        return this.fallbackDrawable;
    }

    private Drawable loadDrawable(@DrawableRes int resourceId) {
        return DrawableDecoderCompat.getDrawable((Context) this.glideContext, resourceId, this.requestOptions.getTheme() != null ? this.requestOptions.getTheme() : this.context.getTheme());
    }

    private synchronized void setErrorPlaceholder() {
        if (canNotifyStatusChanged()) {
            Drawable error = null;
            if (this.model == null) {
                error = getFallbackDrawable();
            }
            if (error == null) {
                error = getErrorDrawable();
            }
            if (error == null) {
                error = getPlaceholderDrawable();
            }
            this.target.onLoadFailed(error);
        }
    }

    public synchronized void onSizeReady(int width2, int height2) {
        this.stateVerifier.throwIfRecycled();
        if (IS_VERBOSE_LOGGABLE) {
            logV("Got onSizeReady in " + LogTime.getElapsedMillis(this.startTime));
        }
        if (this.status == Status.WAITING_FOR_SIZE) {
            this.status = Status.RUNNING;
            float sizeMultiplier = this.requestOptions.getSizeMultiplier();
            this.width = maybeApplySizeMultiplier(width2, sizeMultiplier);
            this.height = maybeApplySizeMultiplier(height2, sizeMultiplier);
            if (IS_VERBOSE_LOGGABLE) {
                logV("finished setup for calling load in " + LogTime.getElapsedMillis(this.startTime));
            }
            this.loadStatus = this.engine.load(this.glideContext, this.model, this.requestOptions.getSignature(), this.width, this.height, this.requestOptions.getResourceClass(), this.transcodeClass, this.priority, this.requestOptions.getDiskCacheStrategy(), this.requestOptions.getTransformations(), this.requestOptions.isTransformationRequired(), this.requestOptions.isScaleOnlyOrNoTransform(), this.requestOptions.getOptions(), this.requestOptions.isMemoryCacheable(), this.requestOptions.getUseUnlimitedSourceGeneratorsPool(), this.requestOptions.getUseAnimationPool(), this.requestOptions.getOnlyRetrieveFromCache(), this, this.callbackExecutor);
            if (this.status != Status.RUNNING) {
                this.loadStatus = null;
            }
            if (IS_VERBOSE_LOGGABLE) {
                logV("finished onSizeReady in " + LogTime.getElapsedMillis(this.startTime));
            }
        }
    }

    private static int maybeApplySizeMultiplier(int size, float sizeMultiplier) {
        return size == Integer.MIN_VALUE ? size : Math.round(((float) size) * sizeMultiplier);
    }

    private boolean canSetResource() {
        return this.requestCoordinator == null || this.requestCoordinator.canSetImage(this);
    }

    private boolean canNotifyCleared() {
        return this.requestCoordinator == null || this.requestCoordinator.canNotifyCleared(this);
    }

    private boolean canNotifyStatusChanged() {
        return this.requestCoordinator == null || this.requestCoordinator.canNotifyStatusChanged(this);
    }

    private boolean isFirstReadyResource() {
        return this.requestCoordinator == null || !this.requestCoordinator.isAnyResourceSet();
    }

    private void notifyLoadSuccess() {
        if (this.requestCoordinator != null) {
            this.requestCoordinator.onRequestSuccess(this);
        }
    }

    private void notifyLoadFailed() {
        if (this.requestCoordinator != null) {
            this.requestCoordinator.onRequestFailed(this);
        }
    }

    public synchronized void onResourceReady(Resource<?> resource2, DataSource dataSource) {
        this.stateVerifier.throwIfRecycled();
        this.loadStatus = null;
        if (resource2 == null) {
            onLoadFailed(new GlideException("Expected to receive a Resource<R> with an object of " + this.transcodeClass + " inside, but instead got null."));
        } else {
            Object received = resource2.get();
            if (received == null || !this.transcodeClass.isAssignableFrom(received.getClass())) {
                releaseResource(resource2);
                onLoadFailed(new GlideException("Expected to receive an object of " + this.transcodeClass + " but instead got " + (received != null ? received.getClass() : "") + "{" + received + "} inside Resource{" + resource2 + "}." + (received != null ? "" : " To indicate failure return a null Resource object, rather than a Resource object containing null data.")));
            } else if (!canSetResource()) {
                releaseResource(resource2);
                this.status = Status.COMPLETE;
            } else {
                onResourceReady(resource2, received, dataSource);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x00e5 A[Catch:{ all -> 0x0104 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private synchronized void onResourceReady(com.bumptech.glide.load.engine.Resource<R> r17, R r18, com.bumptech.glide.load.DataSource r19) {
        /*
            r16 = this;
            monitor-enter(r16)
            boolean r7 = r16.isFirstReadyResource()     // Catch:{ all -> 0x010b }
            com.bumptech.glide.request.SingleRequest$Status r3 = com.bumptech.glide.request.SingleRequest.Status.COMPLETE     // Catch:{ all -> 0x010b }
            r0 = r16
            r0.status = r3     // Catch:{ all -> 0x010b }
            r0 = r17
            r1 = r16
            r1.resource = r0     // Catch:{ all -> 0x010b }
            r0 = r16
            com.bumptech.glide.GlideContext r3 = r0.glideContext     // Catch:{ all -> 0x010b }
            int r3 = r3.getLogLevel()     // Catch:{ all -> 0x010b }
            r4 = 3
            if (r3 > r4) goto L_0x0092
            java.lang.String r3 = "Glide"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x010b }
            r4.<init>()     // Catch:{ all -> 0x010b }
            java.lang.String r5 = "Finished loading "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x010b }
            java.lang.Class r5 = r18.getClass()     // Catch:{ all -> 0x010b }
            java.lang.String r5 = r5.getSimpleName()     // Catch:{ all -> 0x010b }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x010b }
            java.lang.String r5 = " from "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x010b }
            r0 = r19
            java.lang.StringBuilder r4 = r4.append(r0)     // Catch:{ all -> 0x010b }
            java.lang.String r5 = " for "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x010b }
            r0 = r16
            java.lang.Object r5 = r0.model     // Catch:{ all -> 0x010b }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x010b }
            java.lang.String r5 = " with size ["
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x010b }
            r0 = r16
            int r5 = r0.width     // Catch:{ all -> 0x010b }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x010b }
            java.lang.String r5 = "x"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x010b }
            r0 = r16
            int r5 = r0.height     // Catch:{ all -> 0x010b }
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x010b }
            java.lang.String r5 = "] in "
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x010b }
            r0 = r16
            long r8 = r0.startTime     // Catch:{ all -> 0x010b }
            double r8 = com.bumptech.glide.util.LogTime.getElapsedMillis(r8)     // Catch:{ all -> 0x010b }
            java.lang.StringBuilder r4 = r4.append(r8)     // Catch:{ all -> 0x010b }
            java.lang.String r5 = " ms"
            java.lang.StringBuilder r4 = r4.append(r5)     // Catch:{ all -> 0x010b }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x010b }
            android.util.Log.d(r3, r4)     // Catch:{ all -> 0x010b }
        L_0x0092:
            r3 = 1
            r0 = r16
            r0.isCallingCallbacks = r3     // Catch:{ all -> 0x010b }
            r15 = 0
            r0 = r16
            java.util.List<com.bumptech.glide.request.RequestListener<R>> r3 = r0.requestListeners     // Catch:{ all -> 0x0104 }
            if (r3 == 0) goto L_0x00c4
            r0 = r16
            java.util.List<com.bumptech.glide.request.RequestListener<R>> r3 = r0.requestListeners     // Catch:{ all -> 0x0104 }
            java.util.Iterator r8 = r3.iterator()     // Catch:{ all -> 0x0104 }
        L_0x00a6:
            boolean r3 = r8.hasNext()     // Catch:{ all -> 0x0104 }
            if (r3 == 0) goto L_0x00c4
            java.lang.Object r2 = r8.next()     // Catch:{ all -> 0x0104 }
            com.bumptech.glide.request.RequestListener r2 = (com.bumptech.glide.request.RequestListener) r2     // Catch:{ all -> 0x0104 }
            r0 = r16
            java.lang.Object r4 = r0.model     // Catch:{ all -> 0x0104 }
            r0 = r16
            com.bumptech.glide.request.target.Target<R> r5 = r0.target     // Catch:{ all -> 0x0104 }
            r3 = r18
            r6 = r19
            boolean r3 = r2.onResourceReady(r3, r4, r5, r6, r7)     // Catch:{ all -> 0x0104 }
            r15 = r15 | r3
            goto L_0x00a6
        L_0x00c4:
            r0 = r16
            com.bumptech.glide.request.RequestListener<R> r3 = r0.targetListener     // Catch:{ all -> 0x0104 }
            if (r3 == 0) goto L_0x0102
            r0 = r16
            com.bumptech.glide.request.RequestListener<R> r8 = r0.targetListener     // Catch:{ all -> 0x0104 }
            r0 = r16
            java.lang.Object r10 = r0.model     // Catch:{ all -> 0x0104 }
            r0 = r16
            com.bumptech.glide.request.target.Target<R> r11 = r0.target     // Catch:{ all -> 0x0104 }
            r9 = r18
            r12 = r19
            r13 = r7
            boolean r3 = r8.onResourceReady(r9, r10, r11, r12, r13)     // Catch:{ all -> 0x0104 }
            if (r3 == 0) goto L_0x0102
            r3 = 1
        L_0x00e2:
            r15 = r15 | r3
            if (r15 != 0) goto L_0x00f8
            r0 = r16
            com.bumptech.glide.request.transition.TransitionFactory<? super R> r3 = r0.animationFactory     // Catch:{ all -> 0x0104 }
            r0 = r19
            com.bumptech.glide.request.transition.Transition r14 = r3.build(r0, r7)     // Catch:{ all -> 0x0104 }
            r0 = r16
            com.bumptech.glide.request.target.Target<R> r3 = r0.target     // Catch:{ all -> 0x0104 }
            r0 = r18
            r3.onResourceReady(r0, r14)     // Catch:{ all -> 0x0104 }
        L_0x00f8:
            r3 = 0
            r0 = r16
            r0.isCallingCallbacks = r3     // Catch:{ all -> 0x010b }
            r16.notifyLoadSuccess()     // Catch:{ all -> 0x010b }
            monitor-exit(r16)
            return
        L_0x0102:
            r3 = 0
            goto L_0x00e2
        L_0x0104:
            r3 = move-exception
            r4 = 0
            r0 = r16
            r0.isCallingCallbacks = r4     // Catch:{ all -> 0x010b }
            throw r3     // Catch:{ all -> 0x010b }
        L_0x010b:
            r3 = move-exception
            monitor-exit(r16)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.request.SingleRequest.onResourceReady(com.bumptech.glide.load.engine.Resource, java.lang.Object, com.bumptech.glide.load.DataSource):void");
    }

    public synchronized void onLoadFailed(GlideException e) {
        onLoadFailed(e, 5);
    }

    private synchronized void onLoadFailed(GlideException e, int maxLogLevel) {
        boolean z = true;
        synchronized (this) {
            this.stateVerifier.throwIfRecycled();
            e.setOrigin(this.requestOrigin);
            int logLevel = this.glideContext.getLogLevel();
            if (logLevel <= maxLogLevel) {
                Log.w(GLIDE_TAG, "Load failed for " + this.model + " with size [" + this.width + "x" + this.height + "]", e);
                if (logLevel <= 4) {
                    e.logRootCauses(GLIDE_TAG);
                }
            }
            this.loadStatus = null;
            this.status = Status.FAILED;
            this.isCallingCallbacks = true;
            boolean anyListenerHandledUpdatingTarget = false;
            try {
                if (this.requestListeners != null) {
                    for (RequestListener<R> listener : this.requestListeners) {
                        anyListenerHandledUpdatingTarget |= listener.onLoadFailed(e, this.model, this.target, isFirstReadyResource());
                    }
                }
                if (this.targetListener == null || !this.targetListener.onLoadFailed(e, this.model, this.target, isFirstReadyResource())) {
                    z = false;
                }
                if (!anyListenerHandledUpdatingTarget && !z) {
                    setErrorPlaceholder();
                }
                this.isCallingCallbacks = false;
                notifyLoadFailed();
            } catch (Throwable th) {
                this.isCallingCallbacks = false;
                throw th;
            }
        }
    }

    public synchronized boolean isEquivalentTo(Request o) {
        boolean z = false;
        synchronized (this) {
            if (o instanceof SingleRequest) {
                SingleRequest<?> that = (SingleRequest) o;
                synchronized (that) {
                    if (this.overrideWidth == that.overrideWidth && this.overrideHeight == that.overrideHeight && Util.bothModelsNullEquivalentOrEquals(this.model, that.model) && this.transcodeClass.equals(that.transcodeClass) && this.requestOptions.equals(that.requestOptions) && this.priority == that.priority && listenerCountEquals(that)) {
                        z = true;
                    }
                }
            }
        }
        return z;
    }

    private synchronized boolean listenerCountEquals(SingleRequest<?> other) {
        boolean z = false;
        synchronized (this) {
            synchronized (other) {
                if ((this.requestListeners == null ? 0 : this.requestListeners.size()) == (other.requestListeners == null ? 0 : other.requestListeners.size())) {
                    z = true;
                }
            }
        }
        return z;
    }

    private void logV(String message) {
        Log.v(TAG, message + " this: " + this.tag);
    }
}
