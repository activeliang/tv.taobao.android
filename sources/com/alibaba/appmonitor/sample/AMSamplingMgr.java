package com.alibaba.appmonitor.sample;

import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.UTOrangeConfBiz;
import com.alibaba.analytics.core.db.Entity;
import com.alibaba.analytics.core.selfmonitor.SelfMonitorEvent;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.appmonitor.event.EventType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AMSamplingMgr extends UTOrangeConfBiz {
    private static final int MAX_SAMPLING_SEED = 10000;
    private static AMSamplingMgr instance = null;
    private static final String[] namespaces = {"ap_stat", "ap_counter", "ap_alarm"};
    private Map<EventType, AMConifg> eventTypeSamplings = Collections.synchronizedMap(new HashMap(3));
    private int samplingSeed;

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r8v6, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v0, resolved type: com.alibaba.appmonitor.sample.AMConifg} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private AMSamplingMgr() {
        /*
            r12 = this;
            r12.<init>()
            java.util.HashMap r8 = new java.util.HashMap
            r9 = 3
            r8.<init>(r9)
            java.util.Map r8 = java.util.Collections.synchronizedMap(r8)
            r12.eventTypeSamplings = r8
            r12.updateSamplingSeed()
            com.alibaba.appmonitor.event.EventType[] r1 = com.alibaba.appmonitor.event.EventType.values()
            int r6 = r1.length
            r5 = 0
        L_0x0018:
            if (r5 >= r6) goto L_0x0053
            r4 = r1[r5]
            java.lang.Class r2 = r4.getCls()
            com.alibaba.analytics.core.Variables r8 = com.alibaba.analytics.core.Variables.getInstance()
            com.alibaba.analytics.core.db.DBMgr r8 = r8.getDbMgr()
            r9 = 0
            java.lang.String r10 = "module,mp ASC "
            r11 = -1
            java.util.List r3 = r8.find(r2, r9, r10, r11)
            com.alibaba.appmonitor.sample.AMConifg r7 = r12.buildRelation(r3)
            if (r7 != 0) goto L_0x004b
            java.lang.Object r8 = r2.newInstance()     // Catch:{ Exception -> 0x0054 }
            r0 = r8
            com.alibaba.appmonitor.sample.AMConifg r0 = (com.alibaba.appmonitor.sample.AMConifg) r0     // Catch:{ Exception -> 0x0054 }
            r7 = r0
            java.lang.String r8 = "event_type"
            r7.module = r8     // Catch:{ Exception -> 0x0054 }
            int r8 = r4.getDefaultSampling()     // Catch:{ Exception -> 0x0054 }
            r7.setSampling(r8)     // Catch:{ Exception -> 0x0054 }
        L_0x004b:
            java.util.Map<com.alibaba.appmonitor.event.EventType, com.alibaba.appmonitor.sample.AMConifg> r8 = r12.eventTypeSamplings
            r8.put(r4, r7)
            int r5 = r5 + 1
            goto L_0x0018
        L_0x0053:
            return
        L_0x0054:
            r8 = move-exception
            goto L_0x004b
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.appmonitor.sample.AMSamplingMgr.<init>():void");
    }

    public static AMSamplingMgr getInstance() {
        if (instance == null) {
            synchronized (AMSamplingMgr.class) {
                if (instance == null) {
                    instance = new AMSamplingMgr();
                }
            }
        }
        return instance;
    }

    public boolean checkSampled(EventType eventType, String module, String monitorPoint) {
        return isSampled(eventType, module, monitorPoint, (Map<String, String>) null);
    }

    public boolean checkSampled(EventType eventType, String module, String monitorPoint, Map<String, String> extras) {
        return isSampled(eventType, module, monitorPoint, extras);
    }

    public boolean checkAlarmSampled(String module, String monitorPoint, Boolean isSuccess, Map<String, String> extras) {
        return isAlarmSampled(module, monitorPoint, isSuccess, extras);
    }

    public boolean isSampled(EventType eventType, String module, String monitorPoint, Map<String, String> extra) {
        AMConifg eventTypeSample = this.eventTypeSamplings.get(eventType);
        if (eventTypeSample != null) {
            return eventTypeSample.isSampled(this.samplingSeed, module, monitorPoint, extra);
        }
        Logger.d("eventTypeSample  ==null", new Object[0]);
        return false;
    }

    public boolean isAlarmSampled(String module, String monitorPoint, Boolean isSuccess, Map<String, String> extra) {
        AMConifg eventTypeSample = this.eventTypeSamplings.get(EventType.ALARM);
        if (eventTypeSample == null || !(eventTypeSample instanceof AlarmConfig)) {
            return false;
        }
        return ((AlarmConfig) eventTypeSample).isSampled(this.samplingSeed, module, monitorPoint, isSuccess, extra);
    }

    public void updateSamplingSeed() {
        this.samplingSeed = new Random(System.currentTimeMillis()).nextInt(10000);
    }

    public void setEventTypeSampling(EventType eventType, int sampling) {
        AMConifg eventTypeSample = this.eventTypeSamplings.get(eventType);
        if (eventTypeSample != null) {
            eventTypeSample.setSampling(sampling);
        }
        Logger.d("setSampling end", new Object[0]);
    }

    private AMConifg parseConfigEntity(Class<? extends AMConifg> cls, JSONObject configJson) {
        try {
            AMConifg moduleConifg = (AMConifg) cls.newInstance();
            if (configJson.containsKey(SampleConfigConstant.TAG_OFFLINE)) {
                moduleConifg.offline = configJson.getString(SampleConfigConstant.TAG_OFFLINE);
            }
            if (configJson.containsKey(SampleConfigConstant.TAG_CP)) {
                moduleConifg.setSampling(configJson.getIntValue(SampleConfigConstant.TAG_CP));
            }
            if (moduleConifg instanceof AlarmConfig) {
                AlarmConfig alarmConfig = (AlarmConfig) moduleConifg;
                if (configJson.containsKey(SampleConfigConstant.TAG_SCP)) {
                    alarmConfig.successSampling = configJson.getIntValue(SampleConfigConstant.TAG_SCP);
                }
                if (configJson.containsKey(SampleConfigConstant.TAG_FCP)) {
                    alarmConfig.failSampling = configJson.getIntValue(SampleConfigConstant.TAG_FCP);
                }
                return alarmConfig;
            } else if (!(moduleConifg instanceof StatConfig)) {
                return moduleConifg;
            } else {
                StatConfig statConfig = (StatConfig) moduleConifg;
                if (!configJson.containsKey("detail")) {
                    return moduleConifg;
                }
                statConfig.detail = configJson.getIntValue("detail");
                return moduleConifg;
            }
        } catch (Throwable th) {
            Logger.e("new AppMonitorConfig error", new Object[0]);
            return null;
        }
    }

    private AMConifg buildRelation(List<AMConifg> configs) {
        int size = configs.size();
        AMConifg rootConfig = null;
        int i = 0;
        while (i < size && !SampleConfigConstant.TAG_ROOT.equalsIgnoreCase(configs.get(i).module)) {
            i++;
        }
        if (i < size) {
            rootConfig = configs.remove(i);
            Logger.d("remove root element", new Object[0]);
        } else {
            Logger.w("cannot found the root element", new Object[0]);
        }
        if (rootConfig == null) {
            return null;
        }
        int size2 = configs.size();
        for (int j = 0; j < size2; j++) {
            AMConifg config = configs.get(j);
            if (TextUtils.isEmpty(config.monitorPoint)) {
                rootConfig.add(config.module, config);
            } else {
                rootConfig.getOrBulidNext(config.module).add(config.monitorPoint, config);
            }
        }
        return rootConfig;
    }

    @Deprecated
    public String[] returnOrangeConfigurationNameList() {
        return namespaces;
    }

    public void onNonOrangeConfigurationArrive(String aGroupname) {
        super.onNonOrangeConfigurationArrive(aGroupname);
    }

    public String[] getOrangeGroupnames() {
        return namespaces;
    }

    public void onOrangeConfigurationArrive(String namespace, Map<String, String> config) {
        AMConifg rootConifg;
        Logger.d("", "namespace", namespace, "config:", config);
        if (!StringUtils.isBlank(namespace) && config != null) {
            ArrayList<Entity> temp = new ArrayList<>();
            EventType eventType = EventType.getEventTypeByNameSpace(namespace);
            Class cls = eventType.getCls();
            try {
                if (config.containsKey(SampleConfigConstant.TAG_ROOT)) {
                    rootConifg = parseConfigEntity(cls, JSON.parseObject(config.get(SampleConfigConstant.TAG_ROOT)));
                    config.remove(SampleConfigConstant.TAG_ROOT);
                } else {
                    try {
                        rootConifg = (AMConifg) cls.newInstance();
                        if (rootConifg instanceof AlarmConfig) {
                            AlarmConfig c = (AlarmConfig) rootConifg;
                            c.successSampling = eventType.getDefaultSampling();
                            c.failSampling = eventType.getDefaultSampling();
                        } else {
                            rootConifg.setSampling(eventType.getDefaultSampling());
                        }
                    } catch (Throwable th) {
                        return;
                    }
                }
                rootConifg.module = SampleConfigConstant.TAG_ROOT;
                for (String moduleKey : config.keySet()) {
                    JSONObject monduleObject = null;
                    try {
                        monduleObject = JSON.parseObject(config.get(moduleKey));
                    } catch (Throwable e) {
                        Logger.e((String) null, e, new Object[0]);
                    }
                    if (monduleObject != null) {
                        AMConifg moduleConifg = parseConfigEntity(cls, monduleObject);
                        moduleConifg.module = moduleKey;
                        JSONObject pointObjects = monduleObject.getJSONObject(SampleConfigConstant.TAG_MPS);
                        if (pointObjects != null) {
                            for (String monitorPointKey : pointObjects.keySet()) {
                                AMConifg pointConifg = parseConfigEntity(cls, pointObjects.getJSONObject(monitorPointKey));
                                pointConifg.module = moduleKey;
                                pointConifg.monitorPoint = monitorPointKey;
                                moduleConifg.add(monitorPointKey, pointConifg);
                                temp.add(pointConifg);
                            }
                        }
                        rootConifg.add(moduleKey, moduleConifg);
                        temp.add(moduleConifg);
                    }
                }
                temp.add(rootConifg);
                this.eventTypeSamplings.put(eventType, rootConifg);
                Variables.getInstance().getDbMgr().clear((Class<? extends Entity>) rootConifg.getClass());
                Variables.getInstance().getDbMgr().insert((List<? extends Entity>) temp);
            } catch (Throwable e2) {
                Logger.e("", "parse config error", e2);
            }
        }
    }

    public void enableOffline(EventType type, String page, String[] monitorPoints) {
        AMConifg root;
        if (type != null && !TextUtils.isEmpty(page) && monitorPoints != null && (root = this.eventTypeSamplings.get(type)) != null) {
            AMConifg module = root.getOrBulidNext(page);
            for (int i = 0; i < monitorPoints.length; i++) {
                if (module.isContains(monitorPoints[i])) {
                    module.getNext(monitorPoints[i]).enableOffline();
                } else {
                    try {
                        AMConifg point = (AMConifg) module.clone();
                        point.module = page;
                        point.monitorPoint = monitorPoints[i];
                        point.enableOffline();
                        module.add(monitorPoints[i], point);
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public boolean isOffline(EventType type, String module, String monitorPoint) {
        if (isSelfMonitorEvent(type, module, monitorPoint)) {
            return true;
        }
        AMConifg eventTypeSample = this.eventTypeSamplings.get(type);
        if (eventTypeSample != null) {
            return eventTypeSample.isOffline(module, monitorPoint);
        }
        return false;
    }

    public boolean isDetail(String page, String monitorPoint) {
        AMConifg root = this.eventTypeSamplings.get(EventType.STAT);
        if (root == null) {
            return false;
        }
        return ((StatConfig) root).isDetail(page, monitorPoint);
    }

    private boolean isSelfMonitorEvent(EventType type, String module, String monitorPoint) {
        if (type == null || type != EventType.COUNTER || !SelfMonitorEvent.module.equalsIgnoreCase(module) || (!SelfMonitorEvent.UPLOAD_TRAFFIC_OFFLINE.equalsIgnoreCase(monitorPoint) && !SelfMonitorEvent.TNET_REQUEST_SEND_OFFLINE.equalsIgnoreCase(monitorPoint))) {
            return false;
        }
        return true;
    }

    public int getSamplingSeed() {
        return this.samplingSeed;
    }
}
