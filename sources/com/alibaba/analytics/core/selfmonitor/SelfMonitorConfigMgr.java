package com.alibaba.analytics.core.selfmonitor;

import android.text.TextUtils;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.appmonitor.event.EventType;
import com.alibaba.appmonitor.model.Metric;
import com.alibaba.appmonitor.model.MetricRepo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.mtl.appmonitor.model.Measure;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.taobao.muniontaobaosdk.p4p.anticheat.model.ClientTraceData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mtopsdk.common.util.SymbolExpUtil;

public class SelfMonitorConfigMgr implements SystemConfigMgr.IKVChangeListener {
    private static SelfMonitorConfigMgr instance = new SelfMonitorConfigMgr();
    private final String TAG_ABTEST_BUCKET = "abtest_bucket";
    private final String TAG_ABTEST_OFFLINE = "abtest_offline";
    private final String TAG_SAMPLING_MONITOR_AP = "sampling_monitor_ap";
    private final String TAG_SAMPLING_MONITOR_UT = "sampling_monitor_ut";
    private Map<String, MeasureSet> mAMBucketConfig = Collections.synchronizedMap(new HashMap());
    private Set<String> mAMOfflineConfig = Collections.synchronizedSet(new HashSet());
    private Set<String> mAMSamplingConfig = Collections.synchronizedSet(new HashSet());
    private Set<String> mUtSamplingConfig = Collections.synchronizedSet(new HashSet());

    private SelfMonitorConfigMgr() {
        SystemConfigMgr.getInstance().register("sampling_monitor_ut", this);
        SystemConfigMgr.getInstance().register("sampling_monitor_ap", this);
        SystemConfigMgr.getInstance().register("abtest_bucket", this);
        SystemConfigMgr.getInstance().register("abtest_offline", this);
        parseConfig(this.mUtSamplingConfig, SystemConfigMgr.getInstance().get("sampling_monitor_ut"));
        parseConfig(this.mAMSamplingConfig, SystemConfigMgr.getInstance().get("sampling_monitor_ap"));
        parseConfig(this.mAMOfflineConfig, SystemConfigMgr.getInstance().get("abtest_offline"));
        parseBucketConfig("abtest_bucket", SystemConfigMgr.getInstance().get("abtest_bucket"));
        new ConfigArrivedMonitor().start();
        SelfChecker.getInstance().register();
    }

    public static SelfMonitorConfigMgr getInstance() {
        return instance;
    }

    public void onChange(String key, String value) {
        Set temp = null;
        if ("sampling_monitor_ut".equalsIgnoreCase(key)) {
            temp = this.mUtSamplingConfig;
        } else if ("sampling_monitor_ap".equalsIgnoreCase(key)) {
            temp = this.mAMSamplingConfig;
        } else if ("abtest_offline".equalsIgnoreCase(key)) {
            temp = this.mAMOfflineConfig;
        }
        if (temp != null) {
            parseConfig(temp, value);
        }
        if ("abtest_bucket".equalsIgnoreCase(key)) {
            parseBucketConfig("abtest_bucket", value);
        }
    }

    private void parseConfig(Set<String> set, String value) {
        String[] points;
        if (set != null) {
            set.clear();
            if (!TextUtils.isEmpty(value) && (points = value.split(",")) != null && points.length > 0) {
                set.addAll(Arrays.asList(points));
            }
        }
    }

    public boolean isNeedMonitorForAM(EventType type, String module, String monitorPoint) {
        if (!SelfMonitorEvent.module.equalsIgnoreCase(module) && this.mAMSamplingConfig.contains(type + SymbolExpUtil.SYMBOL_COLON + module + SymbolExpUtil.SYMBOL_COLON + monitorPoint)) {
            return true;
        }
        return false;
    }

    public boolean isNeedMonitorForUT(String eventId) {
        if (this.mUtSamplingConfig.contains(eventId)) {
            return true;
        }
        return false;
    }

    public MeasureSet getMeasureSet(String module, String monitorPoint) {
        return this.mAMBucketConfig.get(module + SymbolExpUtil.SYMBOL_COLON + monitorPoint);
    }

    public boolean isNeedMonitorForBucket(String module, String monitorPoints) {
        return getMeasureSet(module, monitorPoints) != null;
    }

    public boolean isNeedMonitorForOffline(EventType type, String module, String monitorPoint) {
        if (this.mAMOfflineConfig.contains(type + SymbolExpUtil.SYMBOL_COLON + module + SymbolExpUtil.SYMBOL_COLON + monitorPoint)) {
            return true;
        }
        return false;
    }

    private void parseBucketConfig(String key, String value) {
        Set<String> measureNameSet;
        String[] boundArray;
        this.mAMBucketConfig.clear();
        if (!TextUtils.isEmpty(value)) {
            try {
                JSONArray points = JSON.parseArray(value);
                if (points != null) {
                    for (int i = 0; i < points.size(); i++) {
                        JSONObject pointJson = (JSONObject) points.get(i);
                        if (pointJson != null) {
                            String module = pointJson.getString("module");
                            String monitorPoint = pointJson.getString("mp");
                            JSONObject bucketsJson = (JSONObject) pointJson.get("buckets");
                            if (!(bucketsJson == null || (measureNameSet = bucketsJson.keySet()) == null)) {
                                MeasureSet ms = MeasureSet.create();
                                for (String measureName : measureNameSet) {
                                    String boundString = bucketsJson.getString(measureName);
                                    if (!TextUtils.isEmpty(boundString) && (boundArray = boundString.split(",")) != null) {
                                        List<Double> bounds = new ArrayList<>();
                                        for (int j = 0; j < boundArray.length; j++) {
                                            try {
                                                bounds.add(Double.valueOf(boundArray[j]));
                                            } catch (Throwable th) {
                                            }
                                        }
                                        ms.addMeasure(new Measure(measureName, Double.valueOf(ClientTraceData.b.f47a), bounds));
                                    }
                                }
                                this.mAMBucketConfig.put(module + SymbolExpUtil.SYMBOL_COLON + monitorPoint, ms);
                                Metric metric = MetricRepo.getRepo().getMetric(module, monitorPoint);
                                if (metric != null) {
                                    MetricRepo.getRepo().add(new Metric(module + "_abtest", monitorPoint, ms, metric.getDimensionSet(), false));
                                }
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                Logger.w("Parse Monitor Bucket error ", e, new Object[0]);
            }
        }
    }
}
