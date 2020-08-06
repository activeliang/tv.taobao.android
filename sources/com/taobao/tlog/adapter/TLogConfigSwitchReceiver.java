package com.taobao.tlog.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.orange.OrangeConfig;
import com.taobao.orange.OrangeConfigListener;
import com.taobao.tao.log.ITLogController;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.TLogUtils;
import com.taobao.tao.log.TaskManager;
import java.util.Map;

public class TLogConfigSwitchReceiver {
    private static final String TAG = "TLogConfigSwitchReceiver";

    public static void init(final Context context) {
        OrangeConfig.getInstance().registerListener(new String[]{"remote_debuger_android"}, (OrangeConfigListener) new OrangeConfigListener() {
            public void onConfigUpdate(String s) {
                long endTime;
                Map<String, String> config = OrangeConfig.getInstance().getConfigs(s);
                if (config != null) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                    String tlogDestroy = config.get("tlog_destroy");
                    String tlogSwitch = config.get("tlog_switch");
                    String tlogLevel = config.get("tlog_level");
                    String tlogModule = config.get("tlog_module");
                    String tlogEndTime = config.get("tlog_endtime");
                    String tlogPull = config.get("tlog_pull");
                    ITLogController controller = TLogInitializer.getTLogControler();
                    if (controller != null) {
                        Log.i(TLogConfigSwitchReceiver.TAG, "The tlogDestroy is : " + tlogDestroy + "  tlogSwitch is : " + tlogSwitch + "  tlogLevel is : " + tlogLevel + "  tlogModule is : " + tlogModule);
                        if (TextUtils.isEmpty(tlogDestroy)) {
                            Log.i(TLogConfigSwitchReceiver.TAG, "The tlogDestroy is null");
                        } else if ("true".equalsIgnoreCase(tlogDestroy)) {
                            TLogInitializer.delete();
                            controller.openLog(false);
                            controller.destroyLog(true);
                            editor.putBoolean("tlog_switch", false);
                        } else {
                            controller.destroyLog(false);
                            if (!TextUtils.isEmpty(tlogSwitch)) {
                                if ("true".equalsIgnoreCase(tlogSwitch)) {
                                    controller.openLog(true);
                                    editor.putBoolean("tlog_switch", true);
                                } else if ("false".equalsIgnoreCase(tlogSwitch)) {
                                    controller.openLog(false);
                                    editor.putBoolean("tlog_switch", false);
                                }
                                if (!TextUtils.isEmpty(tlogLevel)) {
                                    controller.setLogLevel(tlogLevel);
                                    editor.putString("tlog_level", tlogLevel);
                                    if (!TextUtils.isEmpty(tlogModule)) {
                                        controller.setModuleFilter(TLogUtils.makeModule(tlogModule));
                                        editor.putString("tlog_module", tlogModule);
                                        if (!TextUtils.isEmpty(tlogEndTime)) {
                                            try {
                                                endTime = System.currentTimeMillis() + ((long) (Integer.parseInt(tlogEndTime) * 1000));
                                            } catch (NumberFormatException e) {
                                                endTime = System.currentTimeMillis();
                                            }
                                            long limitTime = System.currentTimeMillis() + ZipAppConstants.UPDATEGROUPID_AGE;
                                            if (endTime > System.currentTimeMillis() && endTime < limitTime) {
                                                controller.setEndTime(endTime);
                                                editor.putLong("tlog_endtime", endTime);
                                            } else if (endTime >= limitTime) {
                                                controller.setEndTime(limitTime);
                                                editor.putLong("tlog_endtime", limitTime);
                                            } else {
                                                controller.setEndTime(System.currentTimeMillis());
                                                editor.putLong("tlog_endtime", System.currentTimeMillis());
                                            }
                                        } else {
                                            controller.setEndTime(System.currentTimeMillis());
                                            editor.putLong("tlog_endtime", System.currentTimeMillis());
                                        }
                                        if (!TextUtils.isEmpty(tlogPull)) {
                                            if (tlogPull.equals("true")) {
                                                TaskManager.getInstance().queryTraceStatus(context);
                                            }
                                            editor.putString("tlog_pull", tlogPull);
                                        }
                                        editor.putString("tlog_version", TLogUtils.getAppBuildVersion(context));
                                        editor.apply();
                                        return;
                                    }
                                    Log.i(TLogConfigSwitchReceiver.TAG, "The tlogModule is null");
                                    return;
                                }
                                Log.i(TLogConfigSwitchReceiver.TAG, "The tlogLevel is null");
                                return;
                            }
                            Log.i(TLogConfigSwitchReceiver.TAG, "The tlogSwitch is null");
                        }
                    }
                } else {
                    Log.i(TLogConfigSwitchReceiver.TAG, "TLogConfigSwitchReceiver --> the config is null!");
                }
            }
        });
    }
}
