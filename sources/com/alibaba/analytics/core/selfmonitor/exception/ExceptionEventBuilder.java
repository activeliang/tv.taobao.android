package com.alibaba.analytics.core.selfmonitor.exception;

import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.appmonitor.delegate.SdkMeta;
import com.alibaba.appmonitor.event.EventType;
import com.alibaba.appmonitor.event.UTEvent;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.appmonitor.pool.ReuseJSONArray;
import com.alibaba.appmonitor.pool.ReuseJSONObject;
import com.alibaba.appmonitor.sample.SampleConfigConstant;
import com.alibaba.appmonitor.util.UTUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExceptionEventBuilder {
    private static final String AP_MODULE = "APPMONITOR";
    private static final String AP_MONITOR_POINT = "sdk-exception";
    private static final String UT_COMMON_MONITOR_POINT = "ut-common-exception";
    private static final String UT_MONITOR_POINT = "ut-exception";

    public enum ExceptionType {
        UT,
        AP,
        COMMON
    }

    public static void log(ExceptionType type, Throwable t) {
        if (t != null) {
            try {
                UTEvent utEvent = (UTEvent) BalancedPool.getInstance().poll(UTEvent.class, new Object[0]);
                utEvent.eventId = EventType.ALARM.getEventId();
                Map<String, Object> eventInfo = new HashMap<>();
                eventInfo.put("meta", SdkMeta.getSDKMetaData());
                ReuseJSONArray array = (ReuseJSONArray) BalancedPool.getInstance().poll(ReuseJSONArray.class, new Object[0]);
                array.add(simulateDumpAlarmEvent(type, t));
                eventInfo.put("data", array);
                utEvent.args.put(EventType.ALARM.getAggregateEventArgsKey(), JSON.toJSONString(eventInfo));
                utEvent.arg1 = AP_MODULE;
                utEvent.arg2 = getPoint(type);
                UTUtil.sendAppException(utEvent);
                BalancedPool.getInstance().offer(array);
            } catch (Throwable tt) {
                tt.printStackTrace();
            }
        }
    }

    private static String getPoint(ExceptionType type) {
        if (ExceptionType.UT == type) {
            return UT_MONITOR_POINT;
        }
        if (ExceptionType.COMMON == type) {
            return UT_COMMON_MONITOR_POINT;
        }
        return AP_MONITOR_POINT;
    }

    private static JSONObject simulateDumpAlarmEvent(ExceptionType type, Throwable t) throws IOException {
        JSONObject jobject = (JSONObject) BalancedPool.getInstance().poll(ReuseJSONObject.class, new Object[0]);
        if (Variables.getInstance().getContext() != null) {
            jobject.put("pname", (Object) AppInfoUtil.getCurProcessName(Variables.getInstance().getContext()));
        }
        jobject.put("page", (Object) AP_MODULE);
        jobject.put(SampleConfigConstant.MONITORPOINT, (Object) getPoint(type));
        jobject.put("arg", (Object) t.getClass().getSimpleName());
        jobject.put("successCount", (Object) 0);
        jobject.put("failCount", (Object) 1);
        List<JSONObject> errorInfos = new ArrayList<>();
        String errorMsg = getErrorMsg(t);
        if (errorMsg != null) {
            JSONObject errorInfo = (JSONObject) BalancedPool.getInstance().poll(ReuseJSONObject.class, new Object[0]);
            errorInfo.put("errorCode", (Object) errorMsg);
            errorInfo.put("errorCount", (Object) 1);
            errorInfos.add(errorInfo);
        }
        jobject.put("errors", (Object) errorInfos);
        return jobject;
    }

    private static String getErrorMsg(Throwable t) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(t.getClass().getName());
        StackTraceElement[] stackTraces = t.getStackTrace();
        if (stackTraces != null) {
            for (StackTraceElement stackTrace : stackTraces) {
                sb.append(stackTrace.toString());
            }
        }
        String errorMsg = sb.toString();
        if (StringUtils.isBlank(errorMsg)) {
            return t.toString();
        }
        return errorMsg;
    }
}
