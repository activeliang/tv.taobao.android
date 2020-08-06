package android.databinding;

import android.app.Activity;
import android.app.Application;
import android.content.res.Resources;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.framework.BundleImpl;
import android.taobao.atlas.runtime.DelegateResources;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.taobao.atlas.dexmerge.MergeConstants;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import org.osgi.framework.Bundle;

public class AtlasDataBindingUtil {
    private static DataBindingComponent sDefaultComponent = null;
    private static final HashMap<String, Object> sMappers = new HashMap<>();

    private AtlasDataBindingUtil() {
    }

    public static void setDefaultComponent(DataBindingComponent bindingComponent) {
        sDefaultComponent = bindingComponent;
    }

    public static DataBindingComponent getDefaultComponent() {
        return sDefaultComponent;
    }

    public static <T extends ViewDataBinding> T inflate(LayoutInflater inflater, int layoutId, ViewGroup parent, boolean attachToParent) {
        return inflate(inflater, layoutId, parent, attachToParent, sDefaultComponent);
    }

    public static <T extends ViewDataBinding> T inflate(LayoutInflater inflater, int layoutId, ViewGroup parent, boolean attachToParent, DataBindingComponent bindingComponent) {
        boolean useChildren;
        int startChildren = 0;
        if (parent == null || !attachToParent) {
            useChildren = false;
        } else {
            useChildren = true;
        }
        if (useChildren) {
            startChildren = parent.getChildCount();
        }
        View view = inflater.inflate(layoutId, parent, attachToParent);
        if (useChildren) {
            return bindToAddedViews(bindingComponent, parent, startChildren, layoutId);
        }
        return bind(bindingComponent, view, layoutId);
    }

    public static <T extends ViewDataBinding> T bind(View root) {
        return bind(root, sDefaultComponent);
    }

    public static <T extends ViewDataBinding> T bind(View root, DataBindingComponent bindingComponent) {
        T binding = getBinding(root);
        if (binding != null) {
            return binding;
        }
        Object tagObj = root.getTag();
        if (!(tagObj instanceof String)) {
            throw new IllegalArgumentException("View is not a binding layout");
        }
        String tag = (String) tagObj;
        int id = root.getId();
        if (id == -1) {
            throw new RuntimeException("must set a valid ID for view " + root);
        }
        Object mapper = getDataBinderMapper((Application) root.getContext().getApplicationContext(), root.getResources(), id);
        try {
            Method getLayoutIdMethod = mapper.getClass().getDeclaredMethod("getLayoutId", new Class[]{String.class});
            getLayoutIdMethod.setAccessible(true);
            int layoutId = ((Integer) getLayoutIdMethod.invoke(mapper, new Object[]{tag})).intValue();
            if (layoutId == 0) {
                throw new IllegalArgumentException("View is not a binding layout");
            }
            Method getDataBinderMethod = mapper.getClass().getDeclaredMethod("getDataBinder", new Class[]{DataBindingComponent.class, View.class, Integer.TYPE});
            getDataBinderMethod.setAccessible(true);
            return (ViewDataBinding) getDataBinderMethod.invoke(bindingComponent, new Object[]{root, Integer.valueOf(layoutId)});
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    static <T extends ViewDataBinding> T bind(DataBindingComponent bindingComponent, View[] roots, int layoutId) {
        Object mapper = getDataBinderMapper((Application) roots[0].getContext().getApplicationContext(), roots[0].getResources(), layoutId);
        try {
            Method getDataBinderMethod = mapper.getClass().getDeclaredMethod("getDataBinder", new Class[]{DataBindingComponent.class, View[].class, Integer.TYPE});
            getDataBinderMethod.setAccessible(true);
            return (ViewDataBinding) getDataBinderMethod.invoke(mapper, new Object[]{bindingComponent, roots, Integer.valueOf(layoutId)});
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    static <T extends ViewDataBinding> T bind(DataBindingComponent bindingComponent, View root, int layoutId) {
        Object mapper = getDataBinderMapper((Application) root.getContext().getApplicationContext(), root.getResources(), layoutId);
        try {
            Method getDataBinderMethod = mapper.getClass().getDeclaredMethod("getDataBinder", new Class[]{DataBindingComponent.class, View.class, Integer.TYPE});
            getDataBinderMethod.setAccessible(true);
            return (ViewDataBinding) getDataBinderMethod.invoke(mapper, new Object[]{bindingComponent, root, Integer.valueOf(layoutId)});
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX WARNING: type inference failed for: r7v0, types: [android.view.ViewParent] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static <T extends android.databinding.ViewDataBinding> T findBinding(android.view.View r14) {
        /*
            r10 = 0
            r8 = 1
            r9 = 0
            r13 = 47
            r12 = -1
        L_0x0006:
            if (r14 == 0) goto L_0x0062
            android.databinding.ViewDataBinding r0 = android.databinding.ViewDataBinding.getBinding(r14)
            if (r0 == 0) goto L_0x000f
        L_0x000e:
            return r0
        L_0x000f:
            java.lang.Object r5 = r14.getTag()
            boolean r11 = r5 instanceof java.lang.String
            if (r11 == 0) goto L_0x0054
            r6 = r5
            java.lang.String r6 = (java.lang.String) r6
            java.lang.String r11 = "layout"
            boolean r11 = r6.startsWith(r11)
            if (r11 == 0) goto L_0x0054
            java.lang.String r11 = "_0"
            boolean r11 = r6.endsWith(r11)
            if (r11 == 0) goto L_0x0054
            r11 = 6
            char r2 = r6.charAt(r11)
            r11 = 7
            int r4 = r6.indexOf(r13, r11)
            r1 = 0
            if (r2 != r13) goto L_0x0042
            if (r4 != r12) goto L_0x0040
            r1 = r8
        L_0x003c:
            if (r1 == 0) goto L_0x0054
            r0 = r10
            goto L_0x000e
        L_0x0040:
            r1 = r9
            goto L_0x003c
        L_0x0042:
            r11 = 45
            if (r2 != r11) goto L_0x003c
            if (r4 == r12) goto L_0x003c
            int r11 = r4 + 1
            int r3 = r6.indexOf(r13, r11)
            if (r3 != r12) goto L_0x0052
            r1 = r8
        L_0x0051:
            goto L_0x003c
        L_0x0052:
            r1 = r9
            goto L_0x0051
        L_0x0054:
            android.view.ViewParent r7 = r14.getParent()
            boolean r11 = r7 instanceof android.view.View
            if (r11 == 0) goto L_0x0060
            r14 = r7
            android.view.View r14 = (android.view.View) r14
            goto L_0x0006
        L_0x0060:
            r14 = 0
            goto L_0x0006
        L_0x0062:
            r0 = r10
            goto L_0x000e
        */
        throw new UnsupportedOperationException("Method not decompiled: android.databinding.AtlasDataBindingUtil.findBinding(android.view.View):android.databinding.ViewDataBinding");
    }

    public static <T extends ViewDataBinding> T getBinding(View view) {
        return ViewDataBinding.getBinding(view);
    }

    public static <T extends ViewDataBinding> T setContentView(Activity activity, int layoutId) {
        return setContentView(activity, layoutId, sDefaultComponent);
    }

    public static <T extends ViewDataBinding> T setContentView(Activity activity, int layoutId, DataBindingComponent bindingComponent) {
        activity.setContentView(layoutId);
        return bindToAddedViews(bindingComponent, (ViewGroup) activity.getWindow().getDecorView().findViewById(16908290), 0, layoutId);
    }

    public static String convertBrIdToString(int id) {
        return "";
    }

    private static <T extends ViewDataBinding> T bindToAddedViews(DataBindingComponent component, ViewGroup parent, int startChildren, int layoutId) {
        int endChildren = parent.getChildCount();
        int childrenAdded = endChildren - startChildren;
        if (childrenAdded == 1) {
            return bind(component, parent.getChildAt(endChildren - 1), layoutId);
        }
        View[] children = new View[childrenAdded];
        for (int i = 0; i < childrenAdded; i++) {
            children[i] = parent.getChildAt(i + startChildren);
        }
        return bind(component, children, layoutId);
    }

    private static Object getDataBinderMapper(Application application, Resources resource, int resourceId) {
        TypedValue value = new TypedValue();
        resource.getValue(resourceId, value, false);
        String className = null;
        String bundleLocation = null;
        try {
            String assetsPath = DelegateResources.getGetCookieName(resource.getAssets(), value.assetCookie);
            if (assetsPath.endsWith(".zip")) {
                bundleLocation = substringBetween(assetsPath, "/storage/", WVNativeCallbackUtil.SEPERATER);
                className = String.format("%s.%s", new Object[]{bundleLocation, "DataBinderMapper"});
            } else if (assetsPath.endsWith(MergeConstants.SO_SUFFIX)) {
                List<Bundle> bundles = Atlas.getInstance().getBundles();
                int x = 0;
                while (true) {
                    if (x >= bundles.size()) {
                        break;
                    }
                    BundleImpl impl = (BundleImpl) bundles.get(x);
                    if (impl.getArchive().getArchiveFile().getAbsolutePath().equals(assetsPath)) {
                        bundleLocation = impl.getLocation();
                        className = String.format("%s.%s", new Object[]{bundleLocation, "DataBinderMapper"});
                        break;
                    }
                    x++;
                }
            } else {
                className = "android.databinding.DataBinderMapper";
            }
            if (TextUtils.isEmpty(className)) {
                throw new RuntimeException("can not find DatabindMapper : " + assetsPath);
            }
            Object dataMapper = application.getClassLoader().loadClass(className).newInstance();
            sMappers.put(bundleLocation, dataMapper);
            return dataMapper;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static String substringBetween(String str, String open, String close) {
        int start;
        int end;
        if (str == null || open == null || close == null || (start = str.indexOf(open)) == -1 || (end = str.indexOf(close, open.length() + start)) == -1) {
            return null;
        }
        return str.substring(open.length() + start, end);
    }
}
