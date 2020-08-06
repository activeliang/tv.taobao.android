package com.yunos.tv.core.aqm;

import android.app.Activity;
import com.bftv.fui.constantplugin.Constant;
import com.yunos.tv.core.CoreApplication;
import com.zhiping.dev.android.logger.ZpLogger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public final class ActivityQueueManager {
    private static ActivityQueueManager mActivityQueueManager;
    private String TAG = ActivityQueueManager.class.getSimpleName();
    private ArrayList<WeakReference<Activity>> activityList = new ArrayList<>();

    public static ActivityQueueManager getInstance() {
        if (mActivityQueueManager == null) {
            synchronized (ActivityQueueManager.class) {
                if (mActivityQueueManager == null) {
                    mActivityQueueManager = new ActivityQueueManager();
                }
            }
        }
        return mActivityQueueManager;
    }

    private ActivityQueueManager() {
    }

    public void clear() {
        WeakReference<Activity> actWR;
        ZpLogger.i(this.TAG, "[clear] -- before clear activityList size = " + this.activityList.size());
        if (this.activityList != null) {
            synchronized (CoreApplication.getApplication()) {
                for (int i = this.activityList.size() - 1; i >= 0; i--) {
                    if (i < this.activityList.size() && (actWR = this.activityList.get(i)) != null && actWR.get() != null && !((Activity) actWR.get()).isFinishing()) {
                        ((Activity) actWR.get()).finish();
                    }
                }
                this.activityList.clear();
            }
        }
        ZpLogger.i(this.TAG, "[clear] -- after clear activityList size = " + this.activityList.size());
    }

    public ArrayList<WeakReference<Activity>> getActList() {
        ZpLogger.i(this.TAG, "[getActList] getActList size = " + this.activityList.size());
        ArrayList<WeakReference<Activity>> rtn = new ArrayList<>();
        rtn.addAll(this.activityList);
        return rtn;
    }

    /* access modifiers changed from: package-private */
    public void onCreated(Activity activity) {
        WeakReference<Activity> actWR;
        WeakReference<Activity> actWR2;
        ZpLogger.i(this.TAG, "[onCreated] -- before onCreated activityList size = " + this.activityList.size());
        if (activity != null) {
            ZpLogger.i(this.TAG, "[onCreated] Act : " + getOBJ(activity));
            ActGloballyUnique actGloballyUnique = (ActGloballyUnique) activity.getClass().getAnnotation(ActGloballyUnique.class);
            TvTaoHome tvTaoHome = (TvTaoHome) activity.getClass().getAnnotation(TvTaoHome.class);
            ZpLogger.i(this.TAG, "[onCreated] Act Tags : " + actGloballyUnique + "," + tvTaoHome);
            synchronized (CoreApplication.getApplication()) {
                if (actGloballyUnique != null) {
                    for (int i = this.activityList.size() - 1; i >= 0; i--) {
                        if (i < this.activityList.size() && (actWR2 = this.activityList.get(i)) != null && actWR2.get() != null && !((Activity) actWR2.get()).isFinishing() && ((Activity) actWR2.get()).getClass() == activity.getClass()) {
                            ((Activity) actWR2.get()).finish();
                            ZpLogger.i(this.TAG, "[onCreated] finish same (index=" + i + ",name=" + ((Activity) actWR2.get()).getClass().getName() + Constant.NLP_CACHE_TYPE + activity.hashCode());
                        }
                    }
                }
                if (tvTaoHome != null) {
                    for (int i2 = this.activityList.size() - 1; i2 >= 0; i2--) {
                        if (i2 < this.activityList.size() && (actWR = this.activityList.get(i2)) != null && actWR.get() != null && !((Activity) actWR.get()).isFinishing()) {
                            ((Activity) actWR.get()).finish();
                            ZpLogger.i(this.TAG, "[onCreated] finish page before TvTaoHome (index=" + i2 + ",name=" + ((Activity) actWR.get()).getClass().getName() + Constant.NLP_CACHE_TYPE + activity.hashCode());
                        }
                    }
                }
                this.activityList.add(new WeakReference(activity));
            }
        }
        ZpLogger.i(this.TAG, "[onCreated] -- after onCreated activityList size = " + this.activityList.size());
    }

    /* access modifiers changed from: package-private */
    public void onDestroyed(Activity activity) {
        WeakReference<Activity> actWR;
        ZpLogger.i(this.TAG, "[onDestroyed] -- before onDestroyed activityList size = " + this.activityList.size());
        if (activity != null) {
            ZpLogger.i(this.TAG, "[onDestroyed] Act : " + getOBJ(activity));
            synchronized (CoreApplication.getApplication()) {
                int i = this.activityList.size() - 1;
                while (true) {
                    if (i >= 0) {
                        if (i < this.activityList.size() && (actWR = this.activityList.get(i)) != null && actWR.get() != null && actWR.get() == activity) {
                            this.activityList.remove(i);
                            break;
                        }
                        i--;
                    } else {
                        break;
                    }
                }
            }
        }
        ZpLogger.i(this.TAG, "[onDestroyed] -- after onDestroyed activityList size = " + this.activityList.size());
    }

    /* access modifiers changed from: package-private */
    public void onResumed(Activity activity) {
        WeakReference<Activity> actWR;
        ZpLogger.i(this.TAG, "[onResumed] -- before onResumed activityList size = " + this.activityList.size());
        if (activity != null) {
            ZpLogger.i(this.TAG, "[onResumed] Act : " + getOBJ(activity));
            if (activity != this.activityList.get(this.activityList.size() - 1).get()) {
                synchronized (CoreApplication.getApplication()) {
                    int i = this.activityList.size() - 1;
                    while (true) {
                        if (i >= 0) {
                            WeakReference<Activity> actWR2 = this.activityList.get(i);
                            if (actWR2 != null && actWR2.get() != null && actWR2.get() == activity) {
                                this.activityList.remove(i);
                                this.activityList.add(actWR2);
                                ZpLogger.i(this.TAG, "[onResumed] list re-sort done for " + activity.getClass().getName() + Constant.NLP_CACHE_TYPE + activity.hashCode());
                                break;
                            }
                            i--;
                        } else {
                            break;
                        }
                    }
                }
            }
            if (isTvTaoHome(activity)) {
                synchronized (CoreApplication.getApplication()) {
                    for (int i2 = this.activityList.size() - 1; i2 >= 0; i2--) {
                        if (!(i2 >= this.activityList.size() || (actWR = this.activityList.get(i2)) == null || actWR.get() == null || actWR.get() == activity || ((Activity) actWR.get()).isFinishing())) {
                            ((Activity) actWR.get()).finish();
                            ZpLogger.i(this.TAG, "[onResumed] finish page before TvTaoHome (index=" + i2 + ",name=" + ((Activity) actWR.get()).getClass().getName() + Constant.NLP_CACHE_TYPE + activity.hashCode());
                        }
                    }
                }
            }
        }
        ZpLogger.i(this.TAG, "[onResumed] -- after onResumed activityList size = " + this.activityList.size());
    }

    public static Activity getTop() {
        WeakReference<Activity> last;
        ActivityQueueManager tmpInstance = getInstance();
        int actSize = tmpInstance.activityList.size();
        if (actSize <= 0 || (last = tmpInstance.activityList.get(actSize - 1)) == null) {
            return null;
        }
        return (Activity) last.get();
    }

    public static boolean isTvTaoHome(Activity activity) {
        if (activity == null || ((TvTaoHome) activity.getClass().getAnnotation(TvTaoHome.class)) == null) {
            return false;
        }
        return true;
    }

    public static boolean isGloballyUnique(Activity activity) {
        if (activity == null || ((ActGloballyUnique) activity.getClass().getAnnotation(ActGloballyUnique.class)) == null) {
            return false;
        }
        return true;
    }

    public static boolean isKeepLast(Activity activity) {
        if (activity == null || ((KeepLast) activity.getClass().getAnnotation(KeepLast.class)) == null) {
            return false;
        }
        return true;
    }

    private String getOBJ(Activity activity) {
        if (activity != null) {
            return activity.getClass().getName() + Constant.NLP_CACHE_TYPE + Integer.toHexString(hashCode());
        }
        return Constant.NULL;
    }
}
