package com.yunos.tv.core.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import com.yunos.tv.core.CoreApplication;
import com.yunos.tv.core.config.UpdateStatus;
import com.yunos.tvtaobao.biz.preference.UpdatePreference;
import com.zhiping.dev.android.logger.ZpLogger;
import java.util.Calendar;

public class AppUpdateBroadcast {
    private static final String APP_UPDATE_ACTION = "com.yunos.taobaotv.update.action.BROADCAST";
    private final String TAG = "AppUpdateBroadcast";
    private AppUpdateBroadcastReceiver mAppUpdateBroadcastReceiver;
    /* access modifiers changed from: private */
    public OnShowAppUpdateListener mOnShowAppUpdateListener;

    public interface OnShowAppUpdateListener {
        void onShowAppUpdate(Bundle bundle);

        void onShowAppUpdateDialog(Bundle bundle);
    }

    public void registerUpdateBroadcast(OnShowAppUpdateListener listener) {
        ZpLogger.i("AppUpdateBroadcast", "registerUpdateBroadcast");
        if (this.mAppUpdateBroadcastReceiver == null) {
            this.mAppUpdateBroadcastReceiver = new AppUpdateBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(APP_UPDATE_ACTION);
            filter.setPriority(1000);
            CoreApplication.getApplication().registerReceiver(this.mAppUpdateBroadcastReceiver, filter);
        }
        this.mOnShowAppUpdateListener = listener;
        ZpLogger.i("AppUpdateBroadcast", "update status: " + UpdateStatus.getUpdateStatus());
        if (UpdateStatus.getUpdateStatus() == UpdateStatus.START_ACTIVITY) {
            Bundle bundle = UpdateStatus.getBundle();
            if (this.mOnShowAppUpdateListener != null && bundle != null) {
                this.mOnShowAppUpdateListener.onShowAppUpdate(bundle);
            }
        }
    }

    public void unregisterUpdateBroadcast() {
        ZpLogger.i("AppUpdateBroadcast", "unregisterUpdateBroadcast");
        if (this.mAppUpdateBroadcastReceiver != null) {
            CoreApplication.getApplication().unregisterReceiver(this.mAppUpdateBroadcastReceiver);
            this.mAppUpdateBroadcastReceiver = null;
        }
        this.mOnShowAppUpdateListener = null;
    }

    public boolean startUpdateActivity(Context context, Bundle bundle) {
        ZpLogger.i("AppUpdateBroadcast", "startUpdateActivity bundle=" + bundle);
        if (bundle == null) {
            return false;
        }
        try {
            if (!Boolean.valueOf(bundle.getBoolean(UpdatePreference.INTENT_KEY_FORCE_INSTALL)).booleanValue()) {
                SharedPreferences sp = context.getSharedPreferences("updateInfo", 0);
                if ("everyDay".equals(sp.getString(UpdatePreference.UPGRADE_MODE, "")) && DateUtils.isToday(sp.getLong("update_dialog_show_time", 0))) {
                    return false;
                }
                startNotForceUpdateActivity(context, bundle);
                SharedPreferences.Editor editor = sp.edit();
                editor.putLong("update_dialog_show_time", Calendar.getInstance().getTime().getTime());
                editor.apply();
            } else {
                Intent startIntent = new Intent();
                startIntent.setData(Uri.parse("update://yunos_tvtaobao_update"));
                startIntent.putExtras(bundle);
                context.startActivity(startIntent);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean startNotForceUpdateActivity(Context context, Bundle bundle) {
        ZpLogger.i("AppUpdateBroadcast", "startUpdateActivity bundle=" + bundle);
        if (bundle != null) {
            try {
                Intent startIntent = new Intent();
                startIntent.setData(Uri.parse("not_force_update://yunos_tvtaobao_not_force_update"));
                startIntent.putExtras(bundle);
                context.startActivity(startIntent);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public class AppUpdateBroadcastReceiver extends BroadcastReceiver {
        public AppUpdateBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (TextUtils.equals(intent.getAction(), AppUpdateBroadcast.APP_UPDATE_ACTION)) {
                Bundle bundle = intent.getExtras();
                ZpLogger.i("AppUpdateBroadcast", "onReceive bundle=" + bundle);
                if (!(bundle == null || AppUpdateBroadcast.this.mOnShowAppUpdateListener == null)) {
                    AppUpdateBroadcast.this.mOnShowAppUpdateListener.onShowAppUpdate(bundle);
                }
                UpdateStatus.setUpdateStatus(UpdateStatus.UNKNOWN, (Bundle) null);
            }
        }
    }
}
