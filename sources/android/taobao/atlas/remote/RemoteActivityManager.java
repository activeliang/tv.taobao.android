package android.taobao.atlas.remote;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.hack.AndroidHack;
import android.taobao.atlas.hack.AtlasHacks;
import android.taobao.atlas.hack.Hack;
import android.taobao.atlas.runtime.RuntimeVariables;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteActivityManager {
    private static Hack.HackedMethod ActivityThread_startActivityNow;
    private static Hack.HackedClass NonConfigurationInstances;
    private static final HashMap<Activity, RemoteActivityManager> sActivityManager = new HashMap<>();
    private HashMap<String, EmbeddedActivityRecord> mActivityRecords = new HashMap<>();
    /* access modifiers changed from: private */
    public Activity mParent;

    static {
        try {
            NonConfigurationInstances = Hack.into("android.app.Activity$NonConfigurationInstances");
            ActivityThread_startActivityNow = AtlasHacks.ActivityThread.method("startActivityNow", Activity.class, String.class, Intent.class, ActivityInfo.class, IBinder.class, Bundle.class, NonConfigurationInstances.getmClass());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized RemoteActivityManager obtain(Activity parent) {
        RemoteActivityManager remoteActivityManager;
        synchronized (RemoteActivityManager.class) {
            if (parent.isFinishing()) {
                throw new IllegalStateException("this activity has been finished : " + parent.toString());
            }
            if (sActivityManager.get(parent) == null) {
                sActivityManager.put(parent, new RemoteActivityManager(parent));
            }
            remoteActivityManager = sActivityManager.get(parent);
        }
        return remoteActivityManager;
    }

    private RemoteActivityManager(Activity parent) {
        this.mParent = parent;
        RuntimeVariables.androidApplication.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }

            public void onActivityStarted(Activity activity) {
            }

            public void onActivityResumed(Activity activity) {
            }

            public void onActivityPaused(Activity activity) {
            }

            public void onActivityStopped(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }

            public void onActivityDestroyed(Activity activity) {
                if (activity == RemoteActivityManager.this.mParent) {
                    RemoteActivityManager.this.onParentActivityDestroyed();
                }
            }
        });
    }

    public synchronized Activity getRemoteHost(IRemoteContext delegator) throws Exception {
        EmbeddedActivityRecord ad;
        if (this.mParent.isFinishing()) {
            throw new IllegalStateException("this activity has been finished : " + this.mParent.toString());
        }
        String bundleName = delegator.getTargetBundle();
        if (!this.mActivityRecords.containsKey(bundleName)) {
            this.mActivityRecords.put(bundleName, startEmbeddedActivity(bundleName));
        }
        ad = this.mActivityRecords.get(bundleName);
        ad.activity.addBoundRemoteDelegator(delegator);
        return ad.activity;
    }

    public EmbeddedActivityRecord startEmbeddedActivity(String bundleName) throws Exception {
        EmbeddedActivityRecord activityRecord = new EmbeddedActivityRecord();
        activityRecord.id = "embedded_" + this.mParent.getClass().getSimpleName();
        int mThemeResource = ((Integer) AndroidHack.findField(this.mParent, "mThemeResource").get(this.mParent)).intValue();
        Intent intent = new Intent();
        intent.setClassName(this.mParent, EmbeddedActivity.class.getName());
        intent.putExtra("themeId", mThemeResource);
        intent.putExtra("bundleName", bundleName);
        ActivityInfo info = intent.resolveActivityInfo(this.mParent.getPackageManager(), 1);
        activityRecord.activity = (EmbeddedActivity) ActivityThread_startActivityNow.invoke(AndroidHack.getActivityThread(), this.mParent, activityRecord.id, intent, info, activityRecord, null, null);
        activityRecord.activityInfo = info;
        return activityRecord;
    }

    public void onParentActivityDestroyed() {
        for (Map.Entry<String, EmbeddedActivityRecord> entry : this.mActivityRecords.entrySet()) {
            entry.getValue().activity.finish();
        }
        this.mActivityRecords.clear();
        sActivityManager.remove(this.mParent);
    }

    private class EmbeddedActivityRecord extends Binder {
        EmbeddedActivity activity;
        ActivityInfo activityInfo;
        int curState;
        String id;
        Bundle instanceState;

        private EmbeddedActivityRecord() {
        }
    }

    public static class EmbeddedActivity extends FragmentActivity {
        public List<IRemoteContext> mBoundRemoteItems = new ArrayList();

        public void addBoundRemoteDelegator(IRemoteContext delegator) {
            if (!this.mBoundRemoteItems.contains(delegator)) {
                this.mBoundRemoteItems.add(delegator);
            }
        }

        /* access modifiers changed from: protected */
        public void onCreate(@Nullable Bundle savedInstanceState) {
            int themeResource = getIntent().getIntExtra("themeId", 0);
            String bundleName = getIntent().getStringExtra("bundleName");
            if (themeResource > 0) {
                setTheme(themeResource);
            }
            super.onCreate(savedInstanceState);
            RemoteContext context = new RemoteContext(getBaseContext(), Atlas.getInstance().getBundleClassLoader(bundleName));
            if (!(AtlasHacks.ContextThemeWrapper_mBase == null || AtlasHacks.ContextThemeWrapper_mBase.getField() == null)) {
                AtlasHacks.ContextThemeWrapper_mBase.set(this, context);
            }
            if (AtlasHacks.ContextThemeWrapper_mResources != null) {
                AtlasHacks.ContextThemeWrapper_mResources.set(this, RuntimeVariables.delegateResources);
            }
            AtlasHacks.ContextWrapper_mBase.set(this, context);
        }

        public Object getSystemService(String name) {
            if ("window".equals(name)) {
                return getParent().getSystemService(name);
            }
            return super.getSystemService(name);
        }

        public void startActivityForResult(Intent intent, int requestCode) {
            getParent().startActivityForResult(intent, requestCode);
        }

        public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
            ((FragmentActivity) getParent()).startActivityFromFragment(fragment, intent, requestCode);
        }

        public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode, @Nullable Bundle options) {
            ((FragmentActivity) getParent()).startActivityFromFragment(fragment, intent, requestCode, options);
        }
    }

    public static class RemoteContext extends ContextWrapper {
        private ClassLoader classLoader;

        public RemoteContext(Context base, ClassLoader remoteClassLoader) {
            super(base);
            this.classLoader = remoteClassLoader;
        }

        public ClassLoader getClassLoader() {
            return this.classLoader;
        }
    }
}
