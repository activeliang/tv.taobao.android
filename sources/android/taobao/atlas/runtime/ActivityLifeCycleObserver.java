package android.taobao.atlas.runtime;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class ActivityLifeCycleObserver implements Application.ActivityLifecycleCallbacks {
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        ActivityTaskMgr.getInstance().pushToActivityStack(activity);
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
        ActivityTaskMgr.getInstance().popFromActivityStack(activity);
    }
}
