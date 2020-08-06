package com.alibaba.analytics.core.selfmonitor;

import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.core.config.UTDBConfigEntity;
import com.alibaba.analytics.core.config.UTSampleConfBiz;
import com.alibaba.analytics.core.db.Entity;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.sync.TnetUtil;
import com.alibaba.analytics.core.sync.UploadLogFromDB;
import com.alibaba.analytics.core.sync.UploadMgr;
import com.alibaba.analytics.core.sync.UrlWrapper;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import com.alibaba.analytics.version.UTBuildInfo;
import com.alibaba.appmonitor.event.EventType;
import com.alibaba.appmonitor.sample.AMSamplingMgr;
import com.alibaba.appmonitor.sample.SampleConfigConstant;
import com.alibaba.motu.crashreporter.utrestapi.UTRestUrlWrapper;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import com.ut.mini.core.sign.UTBaseRequestAuthentication;
import com.ut.mini.core.sign.UTSecuritySDKRequestAuthentication;
import com.ut.mini.core.sign.UTSecurityThridRequestAuthentication;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import org.json.JSONException;
import org.json.JSONObject;

public class SelfChecker implements SystemConfigMgr.IKVChangeListener {
    private static SelfChecker instance = new SelfChecker();

    public static SelfChecker getInstance() {
        return instance;
    }

    public void register() {
        SystemConfigMgr.getInstance().register("selfcheck", this);
    }

    public void onChange(final String key, final String value) {
        Logger.e("SelfChecker", "key", key, "value", value);
        TaskExecutor.getInstance().schedule((ScheduledFuture) null, new Runnable() {
            public void run() {
                SelfChecker.this.check(key, value);
            }
        }, 5000);
    }

    public String check(String key, String value) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("current_time", "" + System.currentTimeMillis());
            jsonObject.put("is_init", "" + Variables.getInstance().isInit());
            jsonObject.put(UTRestUrlWrapper.FIELD_SDK_VERSION, "" + UTBuildInfo.getInstance().getFullSDKVersion());
            jsonObject.put("appkey", "" + Variables.getInstance().getAppkey());
            jsonObject.put("secret", "" + Variables.getInstance().getSecret());
            IUTRequestAuthentication authRequest = Variables.getInstance().getRequestAuthenticationInstance();
            if (authRequest == null) {
                jsonObject.put("security_mode", "-1");
            } else if (authRequest instanceof UTBaseRequestAuthentication) {
                jsonObject.put("security_mode", "1");
            } else if (authRequest instanceof UTSecuritySDKRequestAuthentication) {
                jsonObject.put("security_mode", "2");
            } else if (authRequest instanceof UTSecurityThridRequestAuthentication) {
                jsonObject.put("security_mode", "3");
            }
            jsonObject.put("run_process", AppInfoUtil.getCurProcessName(Variables.getInstance().getContext()));
            jsonObject.put("ut_realtime_debug_switch", Variables.getInstance().isRealTimeDebug());
            jsonObject.put("ap_realtime_debug_switch", Variables.getInstance().isApRealTimeDebugging());
            jsonObject.put("ap_sampling_seed", AMSamplingMgr.getInstance().getSamplingSeed());
            jsonObject.put("upload_interval", UploadMgr.getInstance().getCurrentUploadInterval());
            samplingCheck(jsonObject, value);
            boolean uploadSuccess = UploadLogFromDB.getInstance().hasSuccess();
            jsonObject.put("upload_success", uploadSuccess);
            jsonObject.put("upload_mode", UploadMgr.getInstance().getCurrentMode() + "");
            boolean isDegradeToHttp = Variables.getInstance().isHttpService();
            jsonObject.put("tnet_degrade", isDegradeToHttp);
            if (isDegradeToHttp) {
                jsonObject.put("tnet_error_code", TnetUtil.mErrorCode);
            }
            if (!uploadSuccess) {
                jsonObject.put("http_error_code", UrlWrapper.mErrorCode);
            }
            List<? extends Entity> find = Variables.getInstance().getDbMgr().find(UTDBConfigEntity.class, (String) null, (String) null, -1);
            if (find != null) {
                for (int i = 0; i < find.size(); i++) {
                    UTDBConfigEntity entity = (UTDBConfigEntity) find.get(i);
                    jsonObject.put("entity.getGroupname()" + entity.getGroupname(), entity.getConfContent());
                }
            }
        } catch (Throwable e) {
            try {
                jsonObject.put("resport_error", e.getLocalizedMessage() + "");
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        String ret = jsonObject.toString();
        report(ret);
        return ret;
    }

    private void samplingCheck(JSONObject jsonObject, String value) {
        String event_type = null;
        String monitor_point = null;
        String module = null;
        String eventId = null;
        String arg1 = null;
        try {
            if (!TextUtils.isEmpty(value)) {
                try {
                    JSONObject jo = new JSONObject(value);
                    if (jo != null) {
                        JSONObject cpObject = jo.optJSONObject(SampleConfigConstant.TAG_CP);
                        if (cpObject != null) {
                            JSONObject apObject = cpObject.optJSONObject("ap");
                            if (apObject != null) {
                                event_type = apObject.optString("type");
                                module = apObject.optString("module");
                                monitor_point = apObject.optString(BaseConfig.INTENT_KEY_MODULE_POING);
                            }
                            JSONObject utObject = cpObject.optJSONObject("ut");
                            if (utObject != null) {
                                eventId = utObject.optString("eventId");
                                arg1 = utObject.optString("arg1");
                            }
                        }
                        if (!TextUtils.isEmpty(event_type) && !TextUtils.isEmpty(module) && !TextUtils.isEmpty(monitor_point)) {
                            jsonObject.put("ap_sampling_result", AMSamplingMgr.getInstance().checkSampled(EventType.getEventTypeByNameSpace(event_type), module, monitor_point));
                        }
                        if (!TextUtils.isEmpty(eventId)) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(LogField.EVENTID.toString(), eventId);
                            if (!TextUtils.isEmpty(arg1)) {
                                map.put(LogField.ARG1.toString(), arg1);
                            }
                            jsonObject.put("ut_sampling_result", UTSampleConfBiz.getInstance().isSampleSuccess(map));
                        }
                    }
                } catch (Throwable th) {
                }
            }
        } catch (Throwable th2) {
        }
    }

    public void report(String result) {
        File logsDir = Variables.getInstance().getContext().getExternalFilesDir("logs");
        if (logsDir != null) {
            File analytics = new File(logsDir.getAbsolutePath() + File.separator + "analytics.log");
            if (analytics.exists()) {
                analytics.delete();
            } else {
                try {
                    if (!logsDir.exists()) {
                        analytics.getParentFile().mkdirs();
                    }
                    analytics.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                FileOutputStream fos = new FileOutputStream(analytics);
                fos.write(result.getBytes());
                fos.flush();
                fos.close();
            } catch (Throwable th) {
                Logger.e();
            }
        }
    }
}
