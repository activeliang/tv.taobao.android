package com.yunos.tvtaobao.biz.controller;

import android.content.Context;
import android.os.Handler;
import com.bftv.fui.constantplugin.Constant;
import com.taobao.atlas.update.AtlasUpdater;
import com.taobao.atlas.update.exception.MergeException;
import com.yunos.tvtaobao.biz.model.AppInfo;
import com.zhiping.dev.android.logger.ZpLogger;
import java.io.File;
import org.osgi.framework.BundleException;

public class Updater {
    private static final String TAG = "Updater";
    private static boolean isUpdate;

    public static void update(final Handler serviceHandler, Context context, final AppInfo updateInfo) {
        if (isUpdate) {
            ZpLogger.e(TAG, "当前进程下update already success");
        } else if (String.valueOf(com.yunos.tv.core.config.AppInfo.getAppVersionNum()).compareTo(updateInfo.version) < 0) {
            isUpdate = true;
            try {
                new Thread(new Runnable() {
                    public void run() {
                        File patchFile = new File("/sdcard/Android/data/com.yunos.tvtaobao/cache", "patch-" + updateInfo.mUpdateInfo.updateVersion + Constant.NLP_CACHE_TYPE + updateInfo.mUpdateInfo.baseVersion + ".tpatch");
                        ZpLogger.d(Updater.TAG, "path:" + patchFile.getAbsolutePath());
                        try {
                            AtlasUpdater.update(updateInfo.mUpdateInfo, patchFile);
                        } catch (MergeException e) {
                            ZpLogger.e(Updater.TAG, "e:" + e);
                            e.printStackTrace();
                            serviceHandler.sendMessage(serviceHandler.obtainMessage(104));
                        } catch (BundleException e2) {
                            ZpLogger.e(Updater.TAG, "e:" + e2);
                            e2.printStackTrace();
                            serviceHandler.sendMessage(serviceHandler.obtainMessage(104));
                        } finally {
                            serviceHandler.sendMessage(serviceHandler.obtainMessage(105));
                        }
                        ZpLogger.e(Updater.TAG, "update_finish");
                    }
                }).start();
            } catch (Throwable e) {
                e.printStackTrace();
                serviceHandler.sendMessage(serviceHandler.obtainMessage(104));
            }
        }
    }

    private static void toast(String msg, Context context) {
    }
}
