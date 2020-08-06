package android.taobao.atlas.runtime.newcomponent.activity;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.taobao.atlas.runtime.ActivityTaskMgr;
import android.taobao.atlas.runtime.InstrumentationHook;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.runtime.newcomponent.AdditionalActivityManagerProxy;
import android.taobao.atlas.runtime.newcomponent.AdditionalPackageManager;
import android.taobao.atlas.runtime.newcomponent.BridgeUtil;
import android.taobao.atlas.util.StringUtils;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ActivityBridge {

    public interface OnIntentPreparedObserver {
        void onPrepared(Intent intent);
    }

    public static Instrumentation.ActivityResult execStartActivity(Intent intent, final InstrumentationHook.ExecStartActivityCallback startActivityRunnable) {
        List<ResolveInfo> infos = AdditionalPackageManager.getInstance().queryIntentActivities(intent);
        if (infos == null || infos.get(0).activityInfo == null) {
            return startActivityRunnable.execStartActivity(intent);
        }
        Intent wrappIntent = wrapperOriginalIntent(intent, BridgeUtil.getBridgeName(1, infos.get(0).activityInfo.processName));
        if (infos.get(0).activityInfo.processName.equals(RuntimeVariables.getProcessName(RuntimeVariables.androidApplication))) {
            handleActivityStack(infos.get(0).activityInfo, wrappIntent);
            return startActivityRunnable.execStartActivity(wrappIntent);
        }
        AdditionalActivityManagerProxy.handleActivityStack(wrappIntent, infos.get(0).activityInfo, new OnIntentPreparedObserver() {
            public void onPrepared(Intent intent) {
                startActivityRunnable.execStartActivity(intent);
            }
        });
        return null;
    }

    public static void processActivityIntentIfNeed(Object activityclientrecord) {
        try {
            Class ActivityClientRecord_Class = Class.forName("android.app.ActivityThread$ActivityClientRecord");
            Field intent_Field = ActivityClientRecord_Class.getDeclaredField("intent");
            intent_Field.setAccessible(true);
            Intent intent = (Intent) intent_Field.get(activityclientrecord);
            if (intent.getComponent() != null) {
                if (intent.getComponent().getClassName().startsWith(String.format("%s%s", new Object[]{BridgeUtil.COMPONENT_PACKAGE, BridgeUtil.PROXY_PREFIX}))) {
                    Field activityInfo_Field = ActivityClientRecord_Class.getDeclaredField("activityInfo");
                    activityInfo_Field.setAccessible(true);
                    Intent originalIntent = unWrapperOriginalIntent(intent);
                    activityInfo_Field.set(activityclientrecord, (ActivityInfo) AdditionalPackageManager.getInstance().getNewComponentInfo(originalIntent.getComponent(), ActivityInfo.class));
                    intent_Field.set(activityclientrecord, originalIntent);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void handleActivityStack(ActivityInfo info, Intent intent) {
        int launchMode = info.launchMode;
        int flag = intent.getFlags();
        String launchActivityName = info.name;
        String prevActivityName = null;
        List<WeakReference<Activity>> activityList = ActivityTaskMgr.getInstance().getActivityList();
        if (activityList.size() > 0) {
            prevActivityName = ((Activity) activityList.get(activityList.size() - 1).get()).getClass().getName();
        }
        if (StringUtils.equals(prevActivityName, launchActivityName) && (launchMode == 1 || (flag & 536870912) == 536870912)) {
            intent.addFlags(536870912);
        } else if (launchMode == 2 || launchMode == 3 || (flag & 67108864) == 67108864) {
            boolean isExist = false;
            int i = 0;
            while (true) {
                if (i < activityList.size()) {
                    WeakReference<Activity> ref = activityList.get(i);
                    if (ref != null && ref.get() != null && ((Activity) ref.get()).getClass().getName().equals(launchActivityName)) {
                        isExist = true;
                        break;
                    }
                    i++;
                } else {
                    break;
                }
            }
            if (isExist) {
                for (WeakReference<Activity> act : activityList.subList(i + 1, activityList.size())) {
                    if (!(act == null || act.get() == null)) {
                        ((Activity) act.get()).finish();
                    }
                }
                activityList.subList(i + 1, activityList.size()).clear();
                intent.addFlags(536870912);
            }
        }
    }

    private static Intent wrapperOriginalIntent(Intent origin, String delegatActivityname) {
        Intent wrappIntent = new Intent();
        wrappIntent.addFlags(origin.getFlags());
        wrappIntent.setClassName(origin.getComponent().getPackageName(), delegatActivityname);
        wrappIntent.putExtra("originalIntent", origin);
        return wrappIntent;
    }

    private static Intent unWrapperOriginalIntent(Intent wrapper) {
        return (Intent) wrapper.getParcelableExtra("originalIntent");
    }

    public static void handleNewIntent(Object newIntentData) {
        try {
            Class newIntentData_clazz = Class.forName("android.app.ActivityThread$NewIntentData");
            Class refrenceIntent_clazz = Class.forName("com.android.internal.content.ReferrerIntent");
            Field intent_Field = newIntentData_clazz.getDeclaredField("intents");
            intent_Field.setAccessible(true);
            List<Intent> oldIntents = (List) intent_Field.get(newIntentData);
            List<Intent> newIntents = new ArrayList<>();
            if (oldIntents != null && oldIntents.size() > 0) {
                for (Intent intent : oldIntents) {
                    if (intent.getComponent() != null) {
                        if (intent.getComponent().getClassName().startsWith(String.format("%s%s", new Object[]{BridgeUtil.COMPONENT_PACKAGE, BridgeUtil.PROXY_PREFIX}))) {
                            Intent originalIntent = unWrapperOriginalIntent(intent);
                            Constructor constructor = refrenceIntent_clazz.getDeclaredConstructor(new Class[]{Intent.class, String.class});
                            constructor.setAccessible(true);
                            newIntents.add((Intent) constructor.newInstance(new Object[]{originalIntent, RuntimeVariables.androidApplication.getPackageName()}));
                            intent_Field.set(newIntentData, newIntents);
                        }
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
