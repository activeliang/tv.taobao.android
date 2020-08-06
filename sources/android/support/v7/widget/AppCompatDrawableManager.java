package android.support.v7.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LongSparseArray;
import android.support.v4.util.LruCache;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.appcompat.R;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public final class AppCompatDrawableManager {
    private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY = {R.drawable.abc_popup_background_mtrl_mult, R.drawable.abc_cab_background_internal_bg, R.drawable.abc_menu_hardkey_panel_mtrl_mult};
    private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED = {R.drawable.abc_textfield_activated_mtrl_alpha, R.drawable.abc_textfield_search_activated_mtrl_alpha, R.drawable.abc_cab_background_top_mtrl_alpha, R.drawable.abc_text_cursor_material, R.drawable.abc_text_select_handle_left_mtrl_dark, R.drawable.abc_text_select_handle_middle_mtrl_dark, R.drawable.abc_text_select_handle_right_mtrl_dark, R.drawable.abc_text_select_handle_left_mtrl_light, R.drawable.abc_text_select_handle_middle_mtrl_light, R.drawable.abc_text_select_handle_right_mtrl_light};
    private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL = {R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_textfield_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha};
    private static final ColorFilterLruCache COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
    private static final boolean DEBUG = false;
    private static final PorterDuff.Mode DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
    private static AppCompatDrawableManager INSTANCE = null;
    private static final String PLATFORM_VD_CLAZZ = "android.graphics.drawable.VectorDrawable";
    private static final String SKIP_DRAWABLE_TAG = "appcompat_skip_skip";
    private static final String TAG = "AppCompatDrawableManager";
    private static final int[] TINT_CHECKABLE_BUTTON_LIST = {R.drawable.abc_btn_check_material, R.drawable.abc_btn_radio_material};
    private static final int[] TINT_COLOR_CONTROL_NORMAL = {R.drawable.abc_ic_commit_search_api_mtrl_alpha, R.drawable.abc_seekbar_tick_mark_material, R.drawable.abc_ic_menu_share_mtrl_alpha, R.drawable.abc_ic_menu_copy_mtrl_am_alpha, R.drawable.abc_ic_menu_cut_mtrl_alpha, R.drawable.abc_ic_menu_selectall_mtrl_alpha, R.drawable.abc_ic_menu_paste_mtrl_am_alpha};
    private static final int[] TINT_COLOR_CONTROL_STATE_LIST = {R.drawable.abc_tab_indicator_material, R.drawable.abc_textfield_search_material};
    private ArrayMap<String, InflateDelegate> mDelegates;
    private final Object mDrawableCacheLock = new Object();
    private final WeakHashMap<Context, LongSparseArray<WeakReference<Drawable.ConstantState>>> mDrawableCaches = new WeakHashMap<>(0);
    private boolean mHasCheckedVectorDrawableSetup;
    private SparseArrayCompat<String> mKnownDrawableIdTags;
    private WeakHashMap<Context, SparseArrayCompat<ColorStateList>> mTintLists;
    private TypedValue mTypedValue;

    private interface InflateDelegate {
        Drawable createFromXmlInner(@NonNull Context context, @NonNull XmlPullParser xmlPullParser, @NonNull AttributeSet attributeSet, @Nullable Resources.Theme theme);
    }

    public static AppCompatDrawableManager get() {
        if (INSTANCE == null) {
            INSTANCE = new AppCompatDrawableManager();
            installDefaultInflateDelegates(INSTANCE);
        }
        return INSTANCE;
    }

    private static void installDefaultInflateDelegates(@NonNull AppCompatDrawableManager manager) {
        if (Build.VERSION.SDK_INT < 24) {
            manager.addDelegate("vector", new VdcInflateDelegate());
            if (Build.VERSION.SDK_INT >= 11) {
                manager.addDelegate("animated-vector", new AvdcInflateDelegate());
            }
        }
    }

    public Drawable getDrawable(@NonNull Context context, @DrawableRes int resId) {
        return getDrawable(context, resId, false);
    }

    /* access modifiers changed from: package-private */
    public Drawable getDrawable(@NonNull Context context, @DrawableRes int resId, boolean failIfNotKnown) {
        checkVectorDrawableSetup(context);
        Drawable drawable = loadDrawableFromDelegates(context, resId);
        if (drawable == null) {
            drawable = createDrawableIfNeeded(context, resId);
        }
        if (drawable == null) {
            drawable = ContextCompat.getDrawable(context, resId);
        }
        if (drawable != null) {
            drawable = tintDrawable(context, resId, failIfNotKnown, drawable);
        }
        if (drawable != null) {
            DrawableUtils.fixDrawable(drawable);
        }
        return drawable;
    }

    public void onConfigurationChanged(@NonNull Context context) {
        synchronized (this.mDrawableCacheLock) {
            LongSparseArray<WeakReference<Drawable.ConstantState>> cache = this.mDrawableCaches.get(context);
            if (cache != null) {
                cache.clear();
            }
        }
    }

    private static long createCacheKey(TypedValue tv2) {
        return (((long) tv2.assetCookie) << 32) | ((long) tv2.data);
    }

    private Drawable createDrawableIfNeeded(@NonNull Context context, @DrawableRes int resId) {
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        TypedValue tv2 = this.mTypedValue;
        context.getResources().getValue(resId, tv2, true);
        long key = createCacheKey(tv2);
        Drawable dr = getCachedDrawable(context, key);
        if (dr != null) {
            return dr;
        }
        if (resId == R.drawable.abc_cab_background_top_material) {
            dr = new LayerDrawable(new Drawable[]{getDrawable(context, R.drawable.abc_cab_background_internal_bg), getDrawable(context, R.drawable.abc_cab_background_top_mtrl_alpha)});
        }
        if (dr != null) {
            dr.setChangingConfigurations(tv2.changingConfigurations);
            addDrawableToCache(context, key, dr);
        }
        return dr;
    }

    private Drawable tintDrawable(@NonNull Context context, @DrawableRes int resId, boolean failIfNotKnown, @NonNull Drawable drawable) {
        ColorStateList tintList = getTintList(context, resId);
        if (tintList != null) {
            if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
                drawable = drawable.mutate();
            }
            Drawable drawable2 = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(drawable2, tintList);
            PorterDuff.Mode tintMode = getTintMode(resId);
            if (tintMode == null) {
                return drawable2;
            }
            DrawableCompat.setTintMode(drawable2, tintMode);
            return drawable2;
        } else if (resId == R.drawable.abc_seekbar_track_material) {
            LayerDrawable ld = (LayerDrawable) drawable;
            setPorterDuffColorFilter(ld.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(ld.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(ld.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), DEFAULT_MODE);
            return drawable;
        } else if (resId == R.drawable.abc_ratingbar_material || resId == R.drawable.abc_ratingbar_indicator_material || resId == R.drawable.abc_ratingbar_small_material) {
            LayerDrawable ld2 = (LayerDrawable) drawable;
            setPorterDuffColorFilter(ld2.findDrawableByLayerId(16908288), ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorControlNormal), DEFAULT_MODE);
            setPorterDuffColorFilter(ld2.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), DEFAULT_MODE);
            setPorterDuffColorFilter(ld2.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), DEFAULT_MODE);
            return drawable;
        } else if (tintDrawableUsingColorFilter(context, resId, drawable) || !failIfNotKnown) {
            return drawable;
        } else {
            return null;
        }
    }

    private Drawable loadDrawableFromDelegates(@NonNull Context context, @DrawableRes int resId) {
        int type;
        if (this.mDelegates == null || this.mDelegates.isEmpty()) {
            return null;
        }
        if (this.mKnownDrawableIdTags != null) {
            String cachedTagName = this.mKnownDrawableIdTags.get(resId);
            if (SKIP_DRAWABLE_TAG.equals(cachedTagName) || (cachedTagName != null && this.mDelegates.get(cachedTagName) == null)) {
                return null;
            }
        } else {
            this.mKnownDrawableIdTags = new SparseArrayCompat<>();
        }
        if (this.mTypedValue == null) {
            this.mTypedValue = new TypedValue();
        }
        TypedValue tv2 = this.mTypedValue;
        Resources res = context.getResources();
        res.getValue(resId, tv2, true);
        long key = createCacheKey(tv2);
        Drawable dr = getCachedDrawable(context, key);
        if (dr != null) {
            return dr;
        }
        if (tv2.string != null && tv2.string.toString().endsWith(".xml")) {
            try {
                XmlPullParser parser = res.getXml(resId);
                AttributeSet attrs = Xml.asAttributeSet(parser);
                do {
                    type = parser.next();
                    if (type == 2 || type == 1) {
                    }
                    type = parser.next();
                    break;
                } while (type == 1);
                if (type != 2) {
                    throw new XmlPullParserException("No start tag found");
                }
                String tagName = parser.getName();
                this.mKnownDrawableIdTags.append(resId, tagName);
                InflateDelegate delegate = this.mDelegates.get(tagName);
                if (delegate != null) {
                    dr = delegate.createFromXmlInner(context, parser, attrs, context.getTheme());
                }
                if (dr != null) {
                    dr.setChangingConfigurations(tv2.changingConfigurations);
                    if (addDrawableToCache(context, key, dr)) {
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception while inflating drawable", e);
            }
        }
        if (dr != null) {
            return dr;
        }
        this.mKnownDrawableIdTags.append(resId, SKIP_DRAWABLE_TAG);
        return dr;
    }

    private Drawable getCachedDrawable(@NonNull Context context, long key) {
        Drawable drawable = null;
        synchronized (this.mDrawableCacheLock) {
            LongSparseArray<WeakReference<Drawable.ConstantState>> cache = this.mDrawableCaches.get(context);
            if (cache != null) {
                WeakReference<Drawable.ConstantState> wr = cache.get(key);
                if (wr != null) {
                    Drawable.ConstantState entry = (Drawable.ConstantState) wr.get();
                    if (entry != null) {
                        drawable = entry.newDrawable(context.getResources());
                    } else {
                        cache.delete(key);
                    }
                }
            }
        }
        return drawable;
    }

    private boolean addDrawableToCache(@NonNull Context context, long key, @NonNull Drawable drawable) {
        Drawable.ConstantState cs = drawable.getConstantState();
        if (cs == null) {
            return false;
        }
        synchronized (this.mDrawableCacheLock) {
            LongSparseArray<WeakReference<Drawable.ConstantState>> cache = this.mDrawableCaches.get(context);
            if (cache == null) {
                cache = new LongSparseArray<>();
                this.mDrawableCaches.put(context, cache);
            }
            cache.put(key, new WeakReference(cs));
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public Drawable onDrawableLoadedFromResources(@NonNull Context context, @NonNull VectorEnabledTintResources resources, @DrawableRes int resId) {
        Drawable drawable = loadDrawableFromDelegates(context, resId);
        if (drawable == null) {
            drawable = resources.superGetDrawable(resId);
        }
        if (drawable != null) {
            return tintDrawable(context, resId, false, drawable);
        }
        return null;
    }

    static boolean tintDrawableUsingColorFilter(@NonNull Context context, @DrawableRes int resId, @NonNull Drawable drawable) {
        PorterDuff.Mode tintMode = DEFAULT_MODE;
        boolean colorAttrSet = false;
        int colorAttr = 0;
        int alpha = -1;
        if (arrayContains(COLORFILTER_TINT_COLOR_CONTROL_NORMAL, resId)) {
            colorAttr = R.attr.colorControlNormal;
            colorAttrSet = true;
        } else if (arrayContains(COLORFILTER_COLOR_CONTROL_ACTIVATED, resId)) {
            colorAttr = R.attr.colorControlActivated;
            colorAttrSet = true;
        } else if (arrayContains(COLORFILTER_COLOR_BACKGROUND_MULTIPLY, resId)) {
            colorAttr = 16842801;
            colorAttrSet = true;
            tintMode = PorterDuff.Mode.MULTIPLY;
        } else if (resId == R.drawable.abc_list_divider_mtrl_alpha) {
            colorAttr = 16842800;
            colorAttrSet = true;
            alpha = Math.round(40.8f);
        } else if (resId == R.drawable.abc_dialog_material_background) {
            colorAttr = 16842801;
            colorAttrSet = true;
        }
        if (!colorAttrSet) {
            return false;
        }
        if (DrawableUtils.canSafelyMutateDrawable(drawable)) {
            drawable = drawable.mutate();
        }
        drawable.setColorFilter(getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(context, colorAttr), tintMode));
        if (alpha != -1) {
            drawable.setAlpha(alpha);
        }
        return true;
    }

    private void addDelegate(@NonNull String tagName, @NonNull InflateDelegate delegate) {
        if (this.mDelegates == null) {
            this.mDelegates = new ArrayMap<>();
        }
        this.mDelegates.put(tagName, delegate);
    }

    private void removeDelegate(@NonNull String tagName, @NonNull InflateDelegate delegate) {
        if (this.mDelegates != null && this.mDelegates.get(tagName) == delegate) {
            this.mDelegates.remove(tagName);
        }
    }

    private static boolean arrayContains(int[] array, int value) {
        for (int id : array) {
            if (id == value) {
                return true;
            }
        }
        return false;
    }

    static PorterDuff.Mode getTintMode(int resId) {
        if (resId == R.drawable.abc_switch_thumb_material) {
            return PorterDuff.Mode.MULTIPLY;
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public ColorStateList getTintList(@NonNull Context context, @DrawableRes int resId) {
        ColorStateList tint = getTintListFromCache(context, resId);
        if (tint == null) {
            if (resId == R.drawable.abc_edit_text_material) {
                tint = AppCompatResources.getColorStateList(context, R.color.abc_tint_edittext);
            } else if (resId == R.drawable.abc_switch_track_mtrl_alpha) {
                tint = AppCompatResources.getColorStateList(context, R.color.abc_tint_switch_track);
            } else if (resId == R.drawable.abc_switch_thumb_material) {
                tint = AppCompatResources.getColorStateList(context, R.color.abc_tint_switch_thumb);
            } else if (resId == R.drawable.abc_btn_default_mtrl_shape) {
                tint = createDefaultButtonColorStateList(context);
            } else if (resId == R.drawable.abc_btn_borderless_material) {
                tint = createBorderlessButtonColorStateList(context);
            } else if (resId == R.drawable.abc_btn_colored_material) {
                tint = createColoredButtonColorStateList(context);
            } else if (resId == R.drawable.abc_spinner_mtrl_am_alpha || resId == R.drawable.abc_spinner_textfield_background_material) {
                tint = AppCompatResources.getColorStateList(context, R.color.abc_tint_spinner);
            } else if (arrayContains(TINT_COLOR_CONTROL_NORMAL, resId)) {
                tint = ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorControlNormal);
            } else if (arrayContains(TINT_COLOR_CONTROL_STATE_LIST, resId)) {
                tint = AppCompatResources.getColorStateList(context, R.color.abc_tint_default);
            } else if (arrayContains(TINT_CHECKABLE_BUTTON_LIST, resId)) {
                tint = AppCompatResources.getColorStateList(context, R.color.abc_tint_btn_checkable);
            } else if (resId == R.drawable.abc_seekbar_thumb_material) {
                tint = AppCompatResources.getColorStateList(context, R.color.abc_tint_seek_thumb);
            }
            if (tint != null) {
                addTintListToCache(context, resId, tint);
            }
        }
        return tint;
    }

    private ColorStateList getTintListFromCache(@NonNull Context context, @DrawableRes int resId) {
        SparseArrayCompat<ColorStateList> tints;
        if (this.mTintLists == null || (tints = this.mTintLists.get(context)) == null) {
            return null;
        }
        return tints.get(resId);
    }

    private void addTintListToCache(@NonNull Context context, @DrawableRes int resId, @NonNull ColorStateList tintList) {
        if (this.mTintLists == null) {
            this.mTintLists = new WeakHashMap<>();
        }
        SparseArrayCompat<ColorStateList> themeTints = this.mTintLists.get(context);
        if (themeTints == null) {
            themeTints = new SparseArrayCompat<>();
            this.mTintLists.put(context, themeTints);
        }
        themeTints.append(resId, tintList);
    }

    private ColorStateList createDefaultButtonColorStateList(@NonNull Context context) {
        return createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, R.attr.colorButtonNormal));
    }

    private ColorStateList createBorderlessButtonColorStateList(@NonNull Context context) {
        return createButtonColorStateList(context, 0);
    }

    private ColorStateList createColoredButtonColorStateList(@NonNull Context context) {
        return createButtonColorStateList(context, ThemeUtils.getThemeAttrColor(context, R.attr.colorAccent));
    }

    private ColorStateList createButtonColorStateList(@NonNull Context context, @ColorInt int baseColor) {
        int[][] states = new int[4][];
        int[] colors = new int[4];
        int colorControlHighlight = ThemeUtils.getThemeAttrColor(context, R.attr.colorControlHighlight);
        int disabledColor = ThemeUtils.getDisabledThemeAttrColor(context, R.attr.colorButtonNormal);
        states[0] = ThemeUtils.DISABLED_STATE_SET;
        colors[0] = disabledColor;
        int i = 0 + 1;
        states[i] = ThemeUtils.PRESSED_STATE_SET;
        colors[i] = ColorUtils.compositeColors(colorControlHighlight, baseColor);
        int i2 = i + 1;
        states[i2] = ThemeUtils.FOCUSED_STATE_SET;
        colors[i2] = ColorUtils.compositeColors(colorControlHighlight, baseColor);
        int i3 = i2 + 1;
        states[i3] = ThemeUtils.EMPTY_STATE_SET;
        colors[i3] = baseColor;
        int i4 = i3 + 1;
        return new ColorStateList(states, colors);
    }

    private static class ColorFilterLruCache extends LruCache<Integer, PorterDuffColorFilter> {
        public ColorFilterLruCache(int maxSize) {
            super(maxSize);
        }

        /* access modifiers changed from: package-private */
        public PorterDuffColorFilter get(int color, PorterDuff.Mode mode) {
            return (PorterDuffColorFilter) get(Integer.valueOf(generateCacheKey(color, mode)));
        }

        /* access modifiers changed from: package-private */
        public PorterDuffColorFilter put(int color, PorterDuff.Mode mode, PorterDuffColorFilter filter) {
            return (PorterDuffColorFilter) put(Integer.valueOf(generateCacheKey(color, mode)), filter);
        }

        private static int generateCacheKey(int color, PorterDuff.Mode mode) {
            return ((color + 31) * 31) + mode.hashCode();
        }
    }

    static void tintDrawable(Drawable drawable, TintInfo tint, int[] state) {
        if (!DrawableUtils.canSafelyMutateDrawable(drawable) || drawable.mutate() == drawable) {
            if (tint.mHasTintList || tint.mHasTintMode) {
                drawable.setColorFilter(createTintFilter(tint.mHasTintList ? tint.mTintList : null, tint.mHasTintMode ? tint.mTintMode : DEFAULT_MODE, state));
            } else {
                drawable.clearColorFilter();
            }
            if (Build.VERSION.SDK_INT <= 23) {
                drawable.invalidateSelf();
                return;
            }
            return;
        }
        Log.d(TAG, "Mutated drawable is not the same instance as the input.");
    }

    private static PorterDuffColorFilter createTintFilter(ColorStateList tint, PorterDuff.Mode tintMode, int[] state) {
        if (tint == null || tintMode == null) {
            return null;
        }
        return getPorterDuffColorFilter(tint.getColorForState(state, 0), tintMode);
    }

    public static PorterDuffColorFilter getPorterDuffColorFilter(int color, PorterDuff.Mode mode) {
        PorterDuffColorFilter filter = COLOR_FILTER_CACHE.get(color, mode);
        if (filter != null) {
            return filter;
        }
        PorterDuffColorFilter filter2 = new PorterDuffColorFilter(color, mode);
        COLOR_FILTER_CACHE.put(color, mode, filter2);
        return filter2;
    }

    private static void setPorterDuffColorFilter(Drawable d, int color, PorterDuff.Mode mode) {
        if (DrawableUtils.canSafelyMutateDrawable(d)) {
            d = d.mutate();
        }
        if (mode == null) {
            mode = DEFAULT_MODE;
        }
        d.setColorFilter(getPorterDuffColorFilter(color, mode));
    }

    private void checkVectorDrawableSetup(@NonNull Context context) {
        if (!this.mHasCheckedVectorDrawableSetup) {
            this.mHasCheckedVectorDrawableSetup = true;
            Drawable d = getDrawable(context, R.drawable.abc_vector_test);
            if (d == null || !isVectorDrawable(d)) {
                this.mHasCheckedVectorDrawableSetup = false;
                throw new IllegalStateException("This app has been built with an incorrect configuration. Please configure your build for VectorDrawableCompat.");
            }
        }
    }

    private static boolean isVectorDrawable(@NonNull Drawable d) {
        return (d instanceof VectorDrawableCompat) || PLATFORM_VD_CLAZZ.equals(d.getClass().getName());
    }

    private static class VdcInflateDelegate implements InflateDelegate {
        VdcInflateDelegate() {
        }

        @SuppressLint({"NewApi"})
        public Drawable createFromXmlInner(@NonNull Context context, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Resources.Theme theme) {
            try {
                return VectorDrawableCompat.createFromXmlInner(context.getResources(), parser, attrs, theme);
            } catch (Exception e) {
                Log.e("VdcInflateDelegate", "Exception while inflating <vector>", e);
                return null;
            }
        }
    }

    @RequiresApi(11)
    @TargetApi(11)
    private static class AvdcInflateDelegate implements InflateDelegate {
        AvdcInflateDelegate() {
        }

        @SuppressLint({"NewApi"})
        public Drawable createFromXmlInner(@NonNull Context context, @NonNull XmlPullParser parser, @NonNull AttributeSet attrs, @Nullable Resources.Theme theme) {
            try {
                return AnimatedVectorDrawableCompat.createFromXmlInner(context, context.getResources(), parser, attrs, theme);
            } catch (Exception e) {
                Log.e("AvdcInflateDelegate", "Exception while inflating <animated-vector>", e);
                return null;
            }
        }
    }
}
