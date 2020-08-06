package android.taobao.atlas.runtime.newcomponent.provider;

import android.app.ContentProviderHolder;
import android.app.IActivityManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.taobao.atlas.hack.AndroidHack;
import android.taobao.atlas.hack.AtlasHacks;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.runtime.newcomponent.BridgeUtil;
import android.util.Log;
import java.util.HashMap;

public class ContentProviderBridge extends ContentProvider {
    public static final String METHOD_INSTALLPROVIDER = "atlas_install_provider";
    public static final String PROVIDER_HOLDER_KEY = "holder";
    public static final String PROVIDER_INFO_KEY = "info";
    private HashMap<String, Object> providerRecord = new HashMap<>();

    public static Object getContentProvider(ProviderInfo info) {
        String targetProcessName = info.processName != null ? info.processName : RuntimeVariables.androidApplication.getPackageName();
        String currentProcessName = RuntimeVariables.getProcessName(RuntimeVariables.androidApplication);
        if (info.multiprocess || targetProcessName.equals(RuntimeVariables.getProcessName(RuntimeVariables.androidApplication))) {
            return transactProviderInstall(currentProcessName, info);
        }
        return transactProviderInstall(info.processName, info);
    }

    public static void completeRemoveProvider() {
    }

    public static void removeContentProvider() {
    }

    private static Uri accquireRemoteBridgeToken(String processName) {
        return Uri.parse("content://" + BridgeUtil.getBridgeName(3, processName));
    }

    private static Object transactProviderInstall(String processName, ProviderInfo info) {
        Bundle extras = new Bundle();
        extras.putParcelable("info", info);
        return RuntimeVariables.androidApplication.getContentResolver().call(accquireRemoteBridgeToken(processName), METHOD_INSTALLPROVIDER, "", extras).getParcelable(PROVIDER_HOLDER_KEY);
    }

    public boolean onCreate() {
        return false;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    public Bundle call(String method, String arg, Bundle extras) {
        Object original;
        Object newHolder;
        Log.e("ContentProviderBridge", "call");
        if (!method.equals(METHOD_INSTALLPROVIDER)) {
            return super.call(method, arg, extras);
        }
        ProviderInfo info = (ProviderInfo) extras.getParcelable("info");
        if (info == null) {
            return null;
        }
        Object holder = this.providerRecord.get(info.authority);
        if (holder == null) {
            if (Build.VERSION.SDK_INT > 25 || (Build.VERSION.SDK_INT == 25 && Build.VERSION.PREVIEW_SDK_INT > 0)) {
                original = new ContentProviderHolder((ProviderInfo) null);
            } else {
                original = new IActivityManager.ContentProviderHolder((ProviderInfo) null);
            }
            try {
                Object activityThread = AndroidHack.getActivityThread();
                if (activityThread != null) {
                    if (Build.VERSION.SDK_INT == 14) {
                        newHolder = AtlasHacks.ActivityThread_installProvider.invoke(activityThread, RuntimeVariables.androidApplication, original, info, false);
                    } else if (Build.VERSION.SDK_INT == 15) {
                        newHolder = AtlasHacks.ActivityThread_installProvider.invoke(activityThread, RuntimeVariables.androidApplication, original, info, false, true);
                    } else {
                        newHolder = AtlasHacks.ActivityThread_installProvider.invoke(activityThread, RuntimeVariables.androidApplication, original, info, false, true, true);
                    }
                    holder = newHolder;
                    this.providerRecord.put(info.authority, holder);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        Bundle result = new Bundle();
        if (Build.VERSION.SDK_INT > 25 || (Build.VERSION.SDK_INT == 25 && Build.VERSION.PREVIEW_SDK_INT > 0)) {
            result.putParcelable(PROVIDER_HOLDER_KEY, (ContentProviderHolder) holder);
            return result;
        }
        result.putParcelable(PROVIDER_HOLDER_KEY, (IActivityManager.ContentProviderHolder) holder);
        return result;
    }
}
