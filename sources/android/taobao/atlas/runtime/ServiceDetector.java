package android.taobao.atlas.runtime;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.framework.BundleImpl;
import android.taobao.atlas.framework.Framework;
import android.taobao.atlas.runtime.newcomponent.AdditionalPackageManager;
import android.taobao.atlas.util.StringUtils;
import android.text.TextUtils;
import java.util.List;

public class ServiceDetector {
    public static final String ADDITIONAL_SERVICE = "atlas_external_service";

    public static class DetectResult {
        public static final int BUNDLE_PREPARED = 1;
        public static final int BUNDLE_UNPREPARED = -1;
        public static final int BUNDLE_WAIT = 0;
        public ResultListener mListener;
        public int resultCode;

        public interface ResultListener {
            void onPrepared(int i);
        }

        public void setResultListener(ResultListener listener) {
            this.mListener = listener;
        }
    }

    public static DetectResult prepareServiceBundle(Intent intent, int retryCount) {
        return prepareServiceBundle(intent, retryCount, (DetectResult) null);
    }

    /* access modifiers changed from: private */
    public static DetectResult prepareServiceBundle(final Intent service, final int retryCount, DetectResult result) {
        if (service == null) {
            return updateResult(-1, result);
        }
        String componentName = findComponent(service);
        if (TextUtils.isEmpty(componentName)) {
            return updateResult(1, result);
        }
        String bundleName = AtlasBundleInfoManager.instance().getBundleForComponet(componentName);
        if (!TextUtils.isEmpty(bundleName)) {
            BundleImpl impl = (BundleImpl) Atlas.getInstance().getBundle(bundleName);
            if (impl != null && impl.checkValidate()) {
                return updateResult(1, result);
            }
            if (retryCount <= 0) {
                return updateResult(-1, result);
            }
            final DetectResult newResult = updateResult(0, result);
            BundleUtil.checkBundleStateAsync(bundleName, new Runnable() {
                public void run() {
                    DetectResult unused = ServiceDetector.prepareServiceBundle(service, retryCount - 1, newResult);
                }
            }, (Runnable) null);
            return newResult;
        } else if (isClassExistInMaindex(componentName)) {
            return updateResult(1, result);
        } else {
            return updateResult(-1, result);
        }
    }

    private static DetectResult updateResult(int resultCode, DetectResult result) {
        if (!(result == null || result.mListener == null)) {
            result.mListener.onPrepared(resultCode);
        }
        if (result == null) {
            result = new DetectResult();
        }
        result.resultCode = resultCode;
        return result;
    }

    public static boolean isClassExistInMaindex(String componentName) {
        try {
            if (Framework.getSystemClassLoader().loadClass(componentName) != null) {
                return true;
            }
            return false;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static Intent createExplicitFromImplicitIntent(Context context, Intent implicitIntent) {
        List<ResolveInfo> resolveInfo = context.getPackageManager().queryIntentServices(implicitIntent, 0);
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return implicitIntent;
        }
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        if (packageName == null || !packageName.equals(context.getPackageName())) {
            return implicitIntent;
        }
        ComponentName component = new ComponentName(packageName, className);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    public static String findComponent(Intent intent) {
        String packageName = null;
        String componentName = null;
        if (intent.getComponent() != null) {
            packageName = intent.getComponent().getPackageName();
            componentName = intent.getComponent().getClassName();
            ResolveInfo resolveInfo = RuntimeVariables.androidApplication.getPackageManager().resolveService(intent, 0);
            if (!(resolveInfo == null || resolveInfo.serviceInfo == null || !(resolveInfo instanceof AdditionalPackageManager.ExternalResolverInfo))) {
                intent.putExtra(ADDITIONAL_SERVICE, true);
            }
        } else {
            ResolveInfo resolveInfo2 = RuntimeVariables.androidApplication.getPackageManager().resolveService(intent, 0);
            if (!(resolveInfo2 == null || resolveInfo2.serviceInfo == null)) {
                packageName = resolveInfo2.serviceInfo.packageName;
                if (resolveInfo2 instanceof AdditionalPackageManager.ExternalResolverInfo) {
                    intent.putExtra(ADDITIONAL_SERVICE, true);
                    intent.setClassName(RuntimeVariables.androidApplication.getPackageName(), resolveInfo2.serviceInfo.name);
                }
                componentName = resolveInfo2.serviceInfo.name;
            }
        }
        if (!StringUtils.equals(RuntimeVariables.androidApplication.getPackageName(), packageName)) {
            return null;
        }
        return componentName;
    }
}
