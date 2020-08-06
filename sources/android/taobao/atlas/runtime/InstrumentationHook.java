package android.taobao.atlas.runtime;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Dialog;
import android.app.Fragment;
import android.app.Instrumentation;
import android.app.UiAutomation;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.preference.PreferenceManager;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.framework.BundleClassLoader;
import android.taobao.atlas.framework.BundleImpl;
import android.taobao.atlas.framework.Framework;
import android.taobao.atlas.hack.AtlasHacks;
import android.taobao.atlas.hack.Hack;
import android.taobao.atlas.runtime.BundleUtil;
import android.taobao.atlas.runtime.newcomponent.activity.ActivityBridge;
import android.taobao.atlas.util.FileUtils;
import android.taobao.atlas.util.StringUtils;
import android.taobao.atlas.util.log.impl.AtlasMonitor;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.taobao.detail.domain.tuwen.TuwenConstants;
import com.yunos.tv.alitvasrsdk.CommonData;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InstrumentationHook extends Instrumentation {
    private static List<String> ErrorActivityRecords = new ArrayList();
    static Boolean sFirstartAfterUpdate = null;
    public static OnIntentRedirectListener sOnIntentRedirectListener;
    private Context context;
    /* access modifiers changed from: private */
    public Instrumentation mBase;

    public interface OnIntentRedirectListener {
        boolean onExternalRedirect(Intent intent, String str, String str2, Activity activity);
    }

    public InstrumentationHook(Instrumentation mBase2, Context context2) {
        this.context = context2;
        this.mBase = mBase2;
    }

    public Instrumentation.ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target, Intent intent, int requestCode) {
        final Intent intent2 = intent;
        final Context context2 = who;
        final IBinder iBinder = contextThread;
        final IBinder iBinder2 = token;
        final Activity activity = target;
        final int i = requestCode;
        return execStartActivityInternal(who, intent, requestCode, new ExecStartActivityCallback() {
            public Instrumentation.ActivityResult execStartActivity() {
                return ActivityBridge.execStartActivity(intent2, new ExecStartActivityCallback() {
                    public Instrumentation.ActivityResult execStartActivity(Intent wrapperIntent) {
                        return InstrumentationHook.this.mBase.execStartActivity(context2, iBinder, iBinder2, activity, wrapperIntent, i);
                    }
                });
            }
        });
    }

    @TargetApi(16)
    public Instrumentation.ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target, Intent intent, int requestCode, Bundle options) {
        final Intent intent2 = intent;
        final Context context2 = who;
        final IBinder iBinder = contextThread;
        final IBinder iBinder2 = token;
        final Activity activity = target;
        final int i = requestCode;
        final Bundle bundle = options;
        return execStartActivityInternal(who, intent, requestCode, new ExecStartActivityCallback() {
            public Instrumentation.ActivityResult execStartActivity() {
                return ActivityBridge.execStartActivity(intent2, new ExecStartActivityCallback() {
                    public Instrumentation.ActivityResult execStartActivity(Intent wrapperIntent) {
                        return InstrumentationHook.this.mBase.execStartActivity(context2, iBinder, iBinder2, activity, wrapperIntent, i, bundle);
                    }
                });
            }
        });
    }

    @TargetApi(14)
    public Instrumentation.ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Fragment target, Intent intent, int requestCode) {
        final Intent intent2 = intent;
        final Context context2 = who;
        final IBinder iBinder = contextThread;
        final IBinder iBinder2 = token;
        final Fragment fragment = target;
        final int i = requestCode;
        return execStartActivityInternal(who, intent, requestCode, new ExecStartActivityCallback() {
            public Instrumentation.ActivityResult execStartActivity() {
                return ActivityBridge.execStartActivity(intent2, new ExecStartActivityCallback() {
                    public Instrumentation.ActivityResult execStartActivity(Intent wrapperIntent) {
                        return InstrumentationHook.this.mBase.execStartActivity(context2, iBinder, iBinder2, fragment, intent2, i);
                    }
                });
            }
        });
    }

    @TargetApi(16)
    public Instrumentation.ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Fragment target, Intent intent, int requestCode, Bundle options) {
        final Intent intent2 = intent;
        final Context context2 = who;
        final IBinder iBinder = contextThread;
        final IBinder iBinder2 = token;
        final Fragment fragment = target;
        final int i = requestCode;
        final Bundle bundle = options;
        return execStartActivityInternal(who, intent, requestCode, new ExecStartActivityCallback() {
            public Instrumentation.ActivityResult execStartActivity() {
                return ActivityBridge.execStartActivity(intent2, new ExecStartActivityCallback() {
                    public Instrumentation.ActivityResult execStartActivity(Intent wrapperIntent) {
                        return InstrumentationHook.this.mBase.execStartActivity(context2, iBinder, iBinder2, fragment, intent2, i, bundle);
                    }
                });
            }
        });
    }

    public static class ExecStartActivityCallback {
        /* access modifiers changed from: package-private */
        public Instrumentation.ActivityResult execStartActivity() {
            return null;
        }

        public Instrumentation.ActivityResult execStartActivity(Intent wrapperIntent) {
            return null;
        }
    }

    private Instrumentation.ActivityResult execStartActivityInternal(Context context2, Intent intent, int requestCode, ExecStartActivityCallback callback) {
        if (intent != null) {
            Atlas.getInstance().checkDownGradeToH5(intent);
        }
        String packageName = null;
        String componentName = null;
        ResolveInfo resolveInfo = context2.getPackageManager().resolveActivity(intent, 0);
        if (!(resolveInfo == null || resolveInfo.activityInfo == null)) {
            packageName = resolveInfo.activityInfo.packageName;
            componentName = !TextUtils.isEmpty(resolveInfo.activityInfo.targetActivity) ? resolveInfo.activityInfo.targetActivity : resolveInfo.activityInfo.name;
        }
        if (componentName == null) {
            try {
                return callback.execStartActivity();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else if (!StringUtils.equals(context2.getPackageName(), packageName)) {
            if (sOnIntentRedirectListener != null) {
                if (!sOnIntentRedirectListener.onExternalRedirect(intent, packageName, componentName, ActivityTaskMgr.getInstance().peekTopActivity())) {
                    Log.e("InstrumentationHook", "fiter app" + packageName);
                    return null;
                }
            }
            return callback.execStartActivity();
        } else {
            String bundleName = AtlasBundleInfoManager.instance().getBundleForComponet(componentName);
            if (TextUtils.isEmpty(bundleName) || Atlas.isDisableBundle(bundleName)) {
                try {
                    if (Framework.getSystemClassLoader().loadClass(componentName) != null) {
                        return callback.execStartActivity();
                    }
                } catch (ClassNotFoundException e2) {
                    fallBackToClassNotFoundCallback(context2, intent, componentName);
                }
                return null;
            }
            BundleImpl impl = (BundleImpl) Atlas.getInstance().getBundle(bundleName);
            if (impl != null && impl.checkValidate()) {
                return callback.execStartActivity();
            }
            if (Atlas.getInstance().getBundle(bundleName) == null && !AtlasBundleInfoManager.instance().getBundleInfo(bundleName).isInternal() && Framework.getInstalledBundle(bundleName, AtlasBundleInfoManager.instance().getBundleInfo(bundleName).unique_tag) == null) {
                fallBackToClassNotFoundCallback(context2, intent, bundleName);
                return null;
            }
            if (ActivityTaskMgr.getInstance().peekTopActivity() == null || Looper.getMainLooper().getThread().getId() != Thread.currentThread().getId() || AtlasBundleInfoManager.instance().getBundleInfo(bundleName).isMBundle) {
                RuntimeVariables.delegateClassLoader.installMbundleWithDependency(bundleName);
                callback.execStartActivity();
                Log.e("InsturmentationHook", "patch execStartActivity finish");
            } else {
                asyncStartActivity(context2, bundleName, intent, requestCode, componentName, callback);
            }
            return null;
        }
    }

    public static void fallBackToClassNotFoundCallback(Context context2, Intent intent, String componentName) {
        if (Framework.getClassNotFoundCallback() != null) {
            TrackH5FallBack(componentName);
            if (intent.getComponent() == null && !TextUtils.isEmpty(componentName)) {
                intent.setClassName(context2, componentName);
            }
            if (intent.getComponent() != null) {
                Framework.getClassNotFoundCallback().returnIntent(intent);
            }
        }
    }

    private void asyncStartActivity(Context context2, String bundleName, Intent intent, int requestCode, String component, ExecStartActivityCallback callback) {
        final Activity current = ActivityTaskMgr.getInstance().peekTopActivity();
        final Dialog dialog = current != null ? RuntimeVariables.alertDialogUntilBundleProcessed(current, bundleName) : null;
        if (current == null || dialog != null) {
            int sizeOfActivityStack = ActivityTaskMgr.getInstance().sizeOfActivityStack();
            final Context context3 = context2;
            final ExecStartActivityCallback execStartActivityCallback = callback;
            final Intent intent2 = intent;
            BundleUtil.CancelableTask cancelableTask = new BundleUtil.CancelableTask(new Runnable() {
                public void run() {
                    Log.e("InstrumentationHook", "async startActivity");
                    if (context3 instanceof Activity) {
                        execStartActivityCallback.execStartActivity();
                        ((Activity) context3).overridePendingTransition(0, 0);
                    } else {
                        context3.startActivity(intent2);
                    }
                    if (dialog != null && current != null && !current.isFinishing()) {
                        try {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                                ActivityTaskMgr.getInstance();
                                ActivityTaskMgr.sReminderDialog = null;
                            }
                        } catch (Throwable th) {
                            ActivityTaskMgr.getInstance();
                            ActivityTaskMgr.sReminderDialog = null;
                        }
                    }
                }
            });
            final Activity activity = current;
            final Context context4 = context2;
            final Intent intent3 = intent;
            final String str = component;
            final Dialog dialog2 = dialog;
            BundleUtil.CancelableTask cancelableTask2 = new BundleUtil.CancelableTask(new Runnable() {
                public void run() {
                    if (activity == ActivityTaskMgr.getInstance().peekTopActivity()) {
                        InstrumentationHook.fallBackToClassNotFoundCallback(context4, intent3, str);
                    }
                    if (dialog2 != null && activity != null && !activity.isFinishing()) {
                        try {
                            if (dialog2.isShowing()) {
                                dialog2.dismiss();
                                ActivityTaskMgr.getInstance();
                                ActivityTaskMgr.sReminderDialog = null;
                            }
                        } catch (Throwable th) {
                            ActivityTaskMgr.getInstance();
                            ActivityTaskMgr.sReminderDialog = null;
                        }
                    }
                }
            });
            if (dialog != null) {
                final BundleUtil.CancelableTask cancelableTask3 = cancelableTask;
                final BundleUtil.CancelableTask cancelableTask4 = cancelableTask2;
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        cancelableTask3.cancel();
                        cancelableTask4.cancel();
                    }
                });
                BundleUtil.checkBundleStateAsync(bundleName, cancelableTask, cancelableTask2);
                if ((Atlas.getInstance().getBundle(bundleName) == null || Build.VERSION.SDK_INT < 22) && dialog != null && current != null && !current.isFinishing() && !dialog.isShowing()) {
                    try {
                        dialog.show();
                        ActivityTaskMgr.getInstance();
                        ActivityTaskMgr.sReminderDialog = dialog;
                    } catch (Throwable th) {
                    }
                }
            }
        } else {
            throw new RuntimeException("alertDialogUntilBundleProcessed can not return null");
        }
    }

    private static void TrackH5FallBack(String componentName) {
        String pkg;
        String bundleName = AtlasBundleInfoManager.instance().getBundleForComponet(componentName);
        if (!TextUtils.isEmpty(bundleName) && AtlasBundleInfoManager.instance().isInternalBundle(bundleName) && (pkg = AtlasBundleInfoManager.instance().getBundleForComponet(componentName)) != null && Atlas.getInstance().getBundle(pkg) == null) {
        }
    }

    public Activity newActivity(Class<?> clazz, Context context2, IBinder token, Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id, Object lastNonConfigurationInstance) throws InstantiationException, IllegalAccessException {
        Activity activity = this.mBase.newActivity(clazz, context2, token, application, intent, info, title, parent, id, lastNonConfigurationInstance);
        if (RuntimeVariables.androidApplication.getPackageName().equals(info.packageName) && AtlasHacks.ContextThemeWrapper_mResources != null) {
            AtlasHacks.ContextThemeWrapper_mResources.set(activity, RuntimeVariables.delegateResources);
        }
        return activity;
    }

    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String launchActivityName;
        Activity activity;
        if (intent != null) {
            try {
                if (intent.getAction() != null && intent.getAction().equals("android.intent.action.MAIN")) {
                    intent.putExtra("android.taobao.atlas.mainAct.wait", false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            activity = this.mBase.newActivity(cl, className, intent);
        } catch (ClassNotFoundException e2) {
            e2.printStackTrace();
            Map<String, Object> detail = new HashMap<>();
            detail.put(CommonData.KEY_CLASS_NAME, className);
            AtlasMonitor.getInstance().report(AtlasMonitor.INSTRUMENTATION_HOOK_CLASS_NOT_FOUND_EXCEPTION, detail, e2);
            launchActivityName = RuntimeVariables.getLauncherClassName();
            if (TextUtils.isEmpty(launchActivityName)) {
                throw e2;
            }
            List<ActivityManager.RunningTaskInfo> runningTaskInfos = ((ActivityManager) this.context.getSystemService(TuwenConstants.MODEL_LIST_KEY.ACTIVITY)).getRunningTasks(1);
            if (runningTaskInfos != null && runningTaskInfos.size() > 0 && runningTaskInfos.get(0).numActivities > 0 && Framework.getClassNotFoundCallback() != null) {
                if (intent.getComponent() == null) {
                    intent.setClassName(this.context, className);
                }
                Framework.getClassNotFoundCallback().returnIntent(intent);
            }
        } catch (Throwable e1) {
            e1.printStackTrace();
        }
        if ((cl instanceof DelegateClassLoader) && AtlasHacks.ContextThemeWrapper_mResources != null) {
            AtlasHacks.ContextThemeWrapper_mResources.set(activity, RuntimeVariables.delegateResources);
        }
        return activity;
        activity = this.mBase.newActivity(cl, launchActivityName, intent);
        AtlasHacks.ContextThemeWrapper_mResources.set(activity, RuntimeVariables.delegateResources);
        return activity;
    }

    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        BundleImpl bundle;
        if (!RuntimeVariables.androidApplication.getPackageName().equals(activity.getPackageName())) {
            this.mBase.callActivityOnCreate(activity, icicle);
            return;
        }
        ContextImplHook hook = new ContextImplHook(activity.getBaseContext(), activity.getClass().getClassLoader());
        if (activity.getBaseContext().getResources() != RuntimeVariables.delegateResources) {
            try {
                AtlasHacks.ContextImpl_mResources.set(activity.getBaseContext(), RuntimeVariables.delegateResources);
            } catch (Throwable th) {
            }
        }
        if (!(AtlasHacks.ContextThemeWrapper_mBase == null || AtlasHacks.ContextThemeWrapper_mBase.getField() == null)) {
            AtlasHacks.ContextThemeWrapper_mBase.set(activity, hook);
        }
        if (AtlasHacks.ContextThemeWrapper_mResources != null) {
            AtlasHacks.ContextThemeWrapper_mResources.set(activity, RuntimeVariables.delegateResources);
        }
        AtlasHacks.ContextWrapper_mBase.set(activity, hook);
        if (activity.getClass().getClassLoader() instanceof BundleClassLoader) {
            ((BundleClassLoader) activity.getClass().getClassLoader()).getBundle().startBundle();
        } else {
            String Location = AtlasBundleInfoManager.instance().getBundleForComponet(activity.getClass().getName());
            if (!(Location == null || (bundle = (BundleImpl) Framework.getBundle(Location)) == null)) {
                bundle.startBundle();
            }
        }
        String launchActivityName = "";
        Intent launchIntentForPackage = RuntimeVariables.androidApplication.getPackageManager().getLaunchIntentForPackage(RuntimeVariables.androidApplication.getPackageName());
        if (launchIntentForPackage != null) {
            launchActivityName = launchIntentForPackage.resolveActivity(RuntimeVariables.androidApplication.getPackageManager()).getClassName();
        }
        if (TextUtils.isEmpty(launchActivityName)) {
            launchActivityName = "com.taobao.tao.welcome.Welcome";
        }
        if (activity.getIntent() != null) {
            activity.getIntent().setExtrasClassLoader(RuntimeVariables.delegateClassLoader);
        }
        if (activity.getClass().getName().equals(launchActivityName)) {
            this.mBase.callActivityOnCreate(activity, (Bundle) null);
            return;
        }
        try {
            if (isAppFirstStartAfterUpdated()) {
                this.mBase.callActivityOnCreate(activity, (Bundle) null);
            } else {
                this.mBase.callActivityOnCreate(activity, icicle);
            }
        } catch (RuntimeException e) {
            if ((e.toString().contains("android.content.res.Resources") || e.toString().contains("Error inflating class") || e.toString().contains("java.lang.ArrayIndexOutOfBoundsException")) && !e.toString().contains("OutOfMemoryError")) {
                HandleResourceNotFound(activity, icicle, e);
                return;
            }
            throw e;
        }
    }

    private void checkIntent(String bundleName, Intent intent) {
        Set<String> keySet;
        int size;
        try {
            String url = intent.getDataString();
            if (url != null && url.getBytes().length > 153600) {
                String urlInfo = String.format("url size:%s", new Object[]{Integer.valueOf(url.getBytes().length)});
                Log.e("too long url", url);
            }
            Bundle bundle = intent.getExtras();
            if (bundle != null && (keySet = bundle.keySet()) != null) {
                for (String key : keySet) {
                    Object value = bundle.get(key);
                    if (value != null && (value instanceof String) && (size = ((String) value).getBytes().length) > 153600) {
                        String argInfo = String.format("arg size:%s", new Object[]{Integer.valueOf(size)});
                        Log.e("too long arg", (String) value);
                        return;
                    }
                }
            }
        } catch (Throwable th) {
        }
    }

    private boolean ValidateActivityResource(Activity activity) {
        Resources resource;
        String exceptionString = null;
        BundleImpl b = (BundleImpl) Framework.getBundle(AtlasBundleInfoManager.instance().getBundleForComponet(activity.getLocalClassName()));
        String bundlePath = null;
        if (b != null) {
            bundlePath = b.getArchive().getArchiveFile().getAbsolutePath();
        }
        if (AtlasHacks.ContextThemeWrapper_mResources != null) {
            resource = AtlasHacks.ContextThemeWrapper_mResources.get(activity);
        } else {
            resource = activity.getResources();
        }
        Resources resource_runtime = RuntimeVariables.delegateResources;
        if (resource == resource_runtime) {
            return true;
        }
        List<String> paths = getAssetPathFromResources(resource);
        String pathsOfHis = DelegateResources.getCurrentAssetpathStr(RuntimeVariables.androidApplication.getAssets());
        List<String> pathsRuntime = getAssetPathFromResources(resource_runtime);
        if (!(bundlePath == null || paths == null || paths.contains(bundlePath))) {
            String exceptionString2 = null + "(1.1) Activity Resources path not contains:" + b.getArchive().getArchiveFile().getAbsolutePath();
            if (!pathsOfHis.contains(bundlePath)) {
                exceptionString2 = exceptionString2 + "(1.2) paths in history not contains:" + b.getArchive().getArchiveFile().getAbsolutePath();
            }
            if (!pathsRuntime.contains(bundlePath)) {
                exceptionString2 = exceptionString2 + "(1.3) paths in runtime not contains:" + b.getArchive().getArchiveFile().getAbsolutePath();
            }
            if (!b.getArchive().getArchiveFile().exists()) {
                exceptionString2 = exceptionString2 + "(1.4) Bundle archive file not exist:" + b.getArchive().getArchiveFile().getAbsolutePath();
            }
            exceptionString = exceptionString2 + "(1.5) Activity Resources paths length:" + paths.size();
        }
        if (exceptionString != null) {
            return false;
        }
        return true;
    }

    private void HandleResourceNotFound(Activity activity, Bundle icicle, Exception e) {
        String exceptionString;
        Resources resource;
        if (activity == null || ErrorActivityRecords.contains(activity.getClass().getName())) {
            try {
                if (AtlasHacks.ContextThemeWrapper_mResources != null) {
                    resource = AtlasHacks.ContextThemeWrapper_mResources.get(activity);
                } else {
                    resource = activity.getResources();
                }
                List<String> paths = getAssetPathFromResources(resource);
                List<String> pathsRuntime = getAssetPathFromResources(RuntimeVariables.delegateResources);
                String pathsOfHis = DelegateResources.getCurrentAssetpathStr(resource.getAssets());
                BundleImpl b = (BundleImpl) Framework.getBundle(AtlasBundleInfoManager.instance().getBundleForComponet(activity.getLocalClassName()));
                String exceptionString2 = null + "Paths: " + paths;
                if (b != null) {
                    String bundlePath = b.getArchive().getArchiveFile().getAbsolutePath();
                    if (!paths.contains(bundlePath)) {
                        exceptionString2 = exceptionString2 + "(2.1) Activity Resources path not contains:" + b.getArchive().getArchiveFile().getAbsolutePath();
                    }
                    if (!pathsRuntime.contains(bundlePath)) {
                        exceptionString2 = exceptionString2 + "(2.2) Activity Resources path not contains:" + b.getArchive().getArchiveFile().getAbsolutePath();
                    }
                    if (!pathsOfHis.contains(bundlePath)) {
                        exceptionString2 = exceptionString2 + "(2.3) paths in history not contains:" + b.getArchive().getArchiveFile().getAbsolutePath();
                    }
                    if (!b.getArchive().getArchiveFile().exists()) {
                        exceptionString2 = exceptionString2 + "(2.4) Bundle archive file not exist:" + b.getArchive().getArchiveFile().getAbsolutePath();
                    }
                    if (FileUtils.CheckFileValidation(b.getArchive().getArchiveFile().getAbsolutePath())) {
                        exceptionString2 = exceptionString2 + "(2.5) Bundle archive file can not opened with stream:" + b.getArchive().getArchiveFile().getAbsolutePath();
                    }
                }
                if (resource == RuntimeVariables.delegateResources) {
                    exceptionString2 = exceptionString2 + "(2.6) DelegateResources equals Activity Resources";
                }
                exceptionString = exceptionString2 + "(2.7) Activity Resources paths length:" + paths.size();
            } catch (Exception e1) {
                exceptionString = "(2.8) paths in history:" + (" " + DelegateResources.getCurrentAssetpathStr(RuntimeVariables.androidApplication.getAssets())) + " getAssetPath fail: " + e1;
            }
            throw new RuntimeException(exceptionString, e);
        }
        ErrorActivityRecords.add(activity.getClass().getName());
        try {
            activity.finish();
        } catch (Throwable th) {
        }
    }

    private List<String> getAssetPathFromResources(Resources resource) {
        try {
            return DelegateResources.getCurrentAssetPath(resource.getAssets());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String assetPathsToString(Set<String> paths) {
        StringBuffer sb = new StringBuffer();
        sb.append("newDelegateResources [");
        for (String path : paths) {
            sb.append(path).append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    public static void notifyAppUpdated() {
        if (RuntimeVariables.androidApplication != null) {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(RuntimeVariables.androidApplication).edit();
            editor.putBoolean("atlas_appIsUpdated", true);
            editor.commit();
        }
    }

    public static boolean isAppFirstStartAfterUpdated() {
        if (RuntimeVariables.androidApplication == null) {
            return false;
        }
        if (sFirstartAfterUpdate == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RuntimeVariables.androidApplication);
            sFirstartAfterUpdate = Boolean.valueOf(preferences.getBoolean("atlas_appIsUpdated", false));
            if (sFirstartAfterUpdate.booleanValue()) {
                preferences.edit().putBoolean("atlas_appIsUpdated", false);
                preferences.edit().commit();
            }
        }
        return sFirstartAfterUpdate.booleanValue();
    }

    @TargetApi(18)
    public UiAutomation getUiAutomation() {
        return this.mBase.getUiAutomation();
    }

    public void onCreate(Bundle arguments) {
        this.mBase.onCreate(arguments);
    }

    public void start() {
        this.mBase.start();
    }

    public void onStart() {
        this.mBase.onStart();
    }

    public boolean onException(Object obj, Throwable e) {
        BundleImpl impl;
        if ((obj instanceof BroadcastReceiver) && obj.getClass().getClassLoader().getClass().getName().equals(BundleClassLoader.class.getName())) {
            try {
                Field locationField = BundleClassLoader.class.getDeclaredField("location");
                locationField.setAccessible(true);
                String location = (String) locationField.get(obj.getClass().getClassLoader());
                if (!(location == null || (impl = (BundleImpl) Atlas.getInstance().getBundle(location)) == null || impl.checkValidate())) {
                    e.printStackTrace();
                    return true;
                }
            } catch (Throwable th) {
            }
        }
        return this.mBase.onException(obj, e);
    }

    public void sendStatus(int resultCode, Bundle results) {
        this.mBase.sendStatus(resultCode, results);
    }

    public void finish(int resultCode, Bundle results) {
        this.mBase.finish(resultCode, results);
    }

    public void setAutomaticPerformanceSnapshots() {
        this.mBase.setAutomaticPerformanceSnapshots();
    }

    public void startPerformanceSnapshot() {
        this.mBase.startPerformanceSnapshot();
    }

    public void endPerformanceSnapshot() {
        this.mBase.endPerformanceSnapshot();
    }

    public void onDestroy() {
        this.mBase.onDestroy();
    }

    public Context getContext() {
        return this.mBase.getContext();
    }

    public ComponentName getComponentName() {
        return this.mBase.getComponentName();
    }

    public Context getTargetContext() {
        return this.mBase.getTargetContext();
    }

    public boolean isProfiling() {
        return this.mBase.isProfiling();
    }

    public void startProfiling() {
        this.mBase.startProfiling();
    }

    public void stopProfiling() {
        this.mBase.stopProfiling();
    }

    public void setInTouchMode(boolean inTouch) {
        this.mBase.setInTouchMode(inTouch);
    }

    public void waitForIdle(Runnable recipient) {
        this.mBase.waitForIdle(recipient);
    }

    public void waitForIdleSync() {
        this.mBase.waitForIdleSync();
    }

    public void runOnMainSync(Runnable runner) {
        this.mBase.runOnMainSync(runner);
    }

    public Activity startActivitySync(Intent intent) {
        return this.mBase.startActivitySync(intent);
    }

    public void addMonitor(Instrumentation.ActivityMonitor monitor) {
        this.mBase.addMonitor(monitor);
    }

    public Instrumentation.ActivityMonitor addMonitor(IntentFilter filter, Instrumentation.ActivityResult result, boolean block) {
        return this.mBase.addMonitor(filter, result, block);
    }

    public Instrumentation.ActivityMonitor addMonitor(String cls, Instrumentation.ActivityResult result, boolean block) {
        return this.mBase.addMonitor(cls, result, block);
    }

    public boolean checkMonitorHit(Instrumentation.ActivityMonitor monitor, int minHits) {
        return this.mBase.checkMonitorHit(monitor, minHits);
    }

    public Activity waitForMonitor(Instrumentation.ActivityMonitor monitor) {
        return this.mBase.waitForMonitor(monitor);
    }

    public Activity waitForMonitorWithTimeout(Instrumentation.ActivityMonitor monitor, long timeOut) {
        return this.mBase.waitForMonitorWithTimeout(monitor, timeOut);
    }

    public void removeMonitor(Instrumentation.ActivityMonitor monitor) {
        this.mBase.removeMonitor(monitor);
    }

    public boolean invokeMenuActionSync(Activity targetActivity, int id, int flag) {
        return this.mBase.invokeMenuActionSync(targetActivity, id, flag);
    }

    public boolean invokeContextMenuAction(Activity targetActivity, int id, int flag) {
        return this.mBase.invokeContextMenuAction(targetActivity, id, flag);
    }

    public void sendStringSync(String text) {
        this.mBase.sendStringSync(text);
    }

    public void sendKeySync(KeyEvent event) {
        this.mBase.sendKeySync(event);
    }

    public void sendKeyDownUpSync(int key) {
        this.mBase.sendKeyDownUpSync(key);
    }

    public void sendCharacterSync(int keyCode) {
        this.mBase.sendCharacterSync(keyCode);
    }

    public void sendPointerSync(MotionEvent event) {
        this.mBase.sendPointerSync(event);
    }

    public void sendTrackballEventSync(MotionEvent event) {
        this.mBase.sendTrackballEventSync(event);
    }

    public Application newApplication(ClassLoader cl, String className, Context context2) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        if (className != null && className.equals(RuntimeVariables.androidApplication.getClass().getName())) {
            ActivityTaskMgr.getInstance().clearActivityStack();
            Process.killProcess(Process.myPid());
        }
        return this.mBase.newApplication(cl, className, context2);
    }

    public void callApplicationOnCreate(Application app) {
        this.mBase.callApplicationOnCreate(app);
    }

    public void callActivityOnDestroy(Activity activity) {
        this.mBase.callActivityOnDestroy(activity);
        if (activity != null && (activity.getBaseContext() instanceof ContextImplHook)) {
            try {
                Hack.HackedMethod scheduleFinalCleanup = AtlasHacks.ContextImpl.method("scheduleFinalCleanup", String.class, String.class);
                if (scheduleFinalCleanup.getMethod() != null) {
                    scheduleFinalCleanup.invoke(((ContextImplHook) activity.getBaseContext()).getBaseContext(), activity.getClass().getName(), "Activity");
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState) {
        Bundle saveInstance;
        if (isAppFirstStartAfterUpdated()) {
            saveInstance = null;
        } else {
            saveInstance = savedInstanceState;
        }
        if (saveInstance != null) {
            this.mBase.callActivityOnRestoreInstanceState(activity, saveInstance);
        }
    }

    public void callActivityOnPostCreate(Activity activity, Bundle savedInstanceState) {
        Bundle saveInstance;
        if (isAppFirstStartAfterUpdated()) {
            saveInstance = null;
        } else {
            saveInstance = savedInstanceState;
        }
        this.mBase.callActivityOnPostCreate(activity, saveInstance);
    }

    public void callActivityOnNewIntent(Activity activity, Intent intent) {
        this.mBase.callActivityOnNewIntent(activity, intent);
    }

    public void callActivityOnStart(Activity activity) {
        this.mBase.callActivityOnStart(activity);
    }

    public void callActivityOnRestart(Activity activity) {
        this.mBase.callActivityOnRestart(activity);
    }

    public void callActivityOnResume(Activity activity) {
        this.mBase.callActivityOnResume(activity);
    }

    public void callActivityOnStop(Activity activity) {
        this.mBase.callActivityOnStop(activity);
    }

    public void callActivityOnSaveInstanceState(Activity activity, Bundle outState) {
        outState.putString("atlas_real_activity", activity.getClass().getName());
        if (!Framework.isUpdated()) {
            this.mBase.callActivityOnSaveInstanceState(activity, outState);
        }
    }

    public void callActivityOnPause(Activity activity) {
        this.mBase.callActivityOnPause(activity);
    }

    public void callActivityOnUserLeaving(Activity activity) {
        this.mBase.callActivityOnUserLeaving(activity);
    }

    public void startAllocCounting() {
        this.mBase.startAllocCounting();
    }

    public void stopAllocCounting() {
        this.mBase.stopAllocCounting();
    }

    public Bundle getAllocCounts() {
        return this.mBase.getAllocCounts();
    }

    public Bundle getBinderCounts() {
        return this.mBase.getBinderCounts();
    }
}
