package android.taobao.atlas.runtime.newcomponent;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.taobao.atlas.hack.AtlasHacks;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdditionalComponentIntentResolver<T> extends IntentResolver<IntentFilter, ResolveInfo> {
    public static final int TYPE_ACTIVITY = 1;
    public static final int TYPE_PROVIDER = 3;
    public static final int TYPE_SERVICE = 2;
    protected final HashMap<ComponentName, Object> mComponents = new HashMap<>();
    private int mFlags;
    private int type;

    public AdditionalComponentIntentResolver(int type2) {
        this.type = type2;
    }

    /* access modifiers changed from: protected */
    public boolean isPackageForFilter(String packageName, IntentFilter filter) {
        return true;
    }

    /* access modifiers changed from: protected */
    public IntentFilter[] newArray(int size) {
        return new IntentFilter[size];
    }

    public List queryIntent(Intent intent, String resolvedType, boolean defaultOnly, int userId) {
        this.mFlags = defaultOnly ? 65536 : 0;
        return super.queryIntent(intent, resolvedType, defaultOnly);
    }

    public List<?> queryIntent(Intent intent, String resolvedType, int flags, int userId) {
        return super.queryIntent(intent, resolvedType, (65536 & flags) != 0);
    }

    public final void addComponent(Object activityObj) {
        ComponentName component = null;
        try {
            component = (ComponentName) AtlasHacks.PackageParser$Component_getComponentName.invoke(activityObj, new Object[0]);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (component != null) {
            this.mComponents.put(component, activityObj);
        }
        ArrayList<?> intents = AtlasHacks.PackageParser$Component_intents.get(activityObj);
        int NI = intents.size();
        for (int j = 0; j < NI; j++) {
            addFilter((IntentFilter) intents.get(j));
        }
    }

    /* access modifiers changed from: protected */
    public ResolveInfo newResult(IntentFilter info, int match) {
        try {
            ResolveInfo res = new ResolveInfo();
            if (this.type == 1) {
                Object activity = AtlasHacks.PackageParser$ActivityIntentInfo_activity.get(info);
                ActivityInfo ai = (ActivityInfo) activity.getClass().getField("info").get(activity);
                if (ai == null) {
                    return null;
                }
                res.activityInfo = ai;
            } else if (this.type == 2) {
                Object service = AtlasHacks.PackageParser$ServiceIntentInfo_service.get(info);
                ServiceInfo ai2 = (ServiceInfo) service.getClass().getField("info").get(service);
                if (ai2 == null) {
                    return null;
                }
                res.serviceInfo = ai2;
            } else if (this.type == 3) {
                throw new RuntimeException("not support provider query");
            }
            if ((this.mFlags & 64) != 0) {
                res.filter = info;
            }
            res.priority = info.getPriority();
            res.preferredOrder = 0;
            res.match = match;
            res.isDefault = true;
            res.labelRes = 0;
            res.nonLocalizedLabel = null;
            res.icon = 0;
            return res;
        } catch (Exception e) {
            return null;
        }
    }
}
