package android.taobao.atlas.runtime;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContextWrapper;
import android.os.Build;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ActivityTaskMgr {
    private static ActivityTaskMgr sInstance = null;
    public static Dialog sReminderDialog;
    private List<WeakReference<Activity>> activityList = new ArrayList();

    private ActivityTaskMgr() {
    }

    public static synchronized ActivityTaskMgr getInstance() {
        ActivityTaskMgr activityTaskMgr;
        synchronized (ActivityTaskMgr.class) {
            if (sInstance == null) {
                sInstance = new ActivityTaskMgr();
            }
            activityTaskMgr = sInstance;
        }
        return activityTaskMgr;
    }

    public List<WeakReference<Activity>> getActivityList() {
        return this.activityList;
    }

    public Activity peekTopActivity() {
        WeakReference<Activity> ref;
        if (this.activityList == null || this.activityList.size() <= 0 || (ref = this.activityList.get(this.activityList.size() - 1)) == null || ref.get() == null) {
            return null;
        }
        return (Activity) ref.get();
    }

    public void pushToActivityStack(Activity activity) {
        this.activityList.add(new WeakReference(activity));
    }

    public void popFromActivityStack(Activity activity) {
        if (sReminderDialog != null && (sReminderDialog.getContext() == activity || ((sReminderDialog.getContext() instanceof ContextWrapper) && ((ContextWrapper) sReminderDialog.getContext()).getBaseContext() == activity))) {
            try {
                sReminderDialog.dismiss();
            } catch (Throwable th) {
            } finally {
                sReminderDialog = null;
            }
        }
        for (int x = 0; x < this.activityList.size(); x++) {
            WeakReference<Activity> ref = this.activityList.get(x);
            if (!(ref == null || ref.get() == null || ref.get() != activity)) {
                this.activityList.remove(ref);
            }
        }
    }

    public void clearActivityStack() {
        try {
            for (WeakReference<Activity> ref : this.activityList) {
                if (!(ref == null || ref.get() == null || ((Activity) ref.get()).isFinishing())) {
                    ((Activity) ref.get()).finish();
                }
            }
        } catch (Throwable th) {
        } finally {
            this.activityList.clear();
        }
    }

    public boolean isActivityStackEmpty() {
        return this.activityList.size() == 0;
    }

    public int sizeOfActivityStack() {
        return this.activityList.size();
    }

    public void updateBundleActivityResource(String bundleName) {
        for (int x = 0; x < this.activityList.size(); x++) {
            WeakReference<Activity> ref = this.activityList.get(x);
            if (ref != null && ref.get() != null && !((Activity) ref.get()).isFinishing()) {
                Activity activity = (Activity) ref.get();
                try {
                    if (Build.VERSION.SDK_INT >= 17 && activity.getResources() != RuntimeVariables.delegateResources) {
                        Field mResourcesField = Activity.class.getSuperclass().getDeclaredField("mResources");
                        mResourcesField.setAccessible(true);
                        mResourcesField.set(activity, RuntimeVariables.delegateResources);
                    }
                    Field mThemeField = Activity.class.getSuperclass().getDeclaredField("mTheme");
                    mThemeField.setAccessible(true);
                    Object mTheme = mThemeField.get(activity);
                    Field mReferenceResource = mTheme.getClass().getDeclaredField("this$0");
                    mReferenceResource.setAccessible(true);
                    if (mReferenceResource.get(mTheme) != RuntimeVariables.delegateResources) {
                        mThemeField.set(activity, (Object) null);
                    }
                    Class TintContextWrapper = Class.forName("android.support.v7.widget.TintContextWrapper");
                    Field tintWrapperField = TintContextWrapper.getDeclaredField("sCache");
                    tintWrapperField.setAccessible(true);
                    ArrayList<WeakReference<Object>> sCache = (ArrayList) tintWrapperField.get(TintContextWrapper);
                    if (sCache != null) {
                        int n = 0;
                        while (true) {
                            if (n < sCache.size()) {
                                WeakReference<Object> wrappRef = sCache.get(n);
                                Object wrapper = wrappRef != null ? wrappRef.get() : null;
                                if (wrapper != null && ((ContextWrapper) wrapper).getBaseContext() == activity) {
                                    Field mTintResourcesField = TintContextWrapper.getDeclaredField("mResources");
                                    mTintResourcesField.setAccessible(true);
                                    mTintResourcesField.set(wrapper, RuntimeVariables.delegateResources);
                                    break;
                                }
                                n++;
                            } else {
                                break;
                            }
                        }
                    }
                } catch (Throwable th) {
                }
            }
        }
    }
}
