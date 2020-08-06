package com.alibaba.appmonitor.util;

import com.alibaba.analytics.core.model.Log;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.core.store.LogStoreMgr;
import com.alibaba.appmonitor.delegate.SdkMeta;
import com.alibaba.appmonitor.event.Event;
import com.alibaba.appmonitor.event.EventType;
import com.alibaba.appmonitor.event.UTEvent;
import com.alibaba.appmonitor.model.UTDimensionValueSet;
import com.alibaba.appmonitor.pool.BalancedPool;
import com.alibaba.appmonitor.pool.ReuseJSONArray;
import com.alibaba.fastjson.JSON;
import com.alibaba.motu.crashreporter.utrestapi.UTConstants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UTUtil {
    public static void uploadEvent(Map<UTDimensionValueSet, List<Event>> eventMap) {
        Integer eventId;
        for (Map.Entry<UTDimensionValueSet, List<Event>> entry : eventMap.entrySet()) {
            StringBuilder moduleBuilder = new StringBuilder();
            StringBuilder prointBuilder = new StringBuilder();
            UTDimensionValueSet utDimensionValues = entry.getKey();
            List<Event> events = entry.getValue();
            if (!(events.size() == 0 || (eventId = utDimensionValues.getEventId()) == null)) {
                EventType eventType = EventType.getEventType(eventId.intValue());
                UTEvent utEvent = (UTEvent) BalancedPool.getInstance().poll(UTEvent.class, new Object[0]);
                utEvent.eventId = eventId.intValue();
                if (utDimensionValues.getMap() != null) {
                    utEvent.args.putAll(utDimensionValues.getMap());
                    utEvent.args.remove("commitDay");
                }
                Map<String, Object> eventInfo = new HashMap<>();
                eventInfo.put("meta", SdkMeta.getSDKMetaData());
                ReuseJSONArray array = (ReuseJSONArray) BalancedPool.getInstance().poll(ReuseJSONArray.class, new Object[0]);
                int i = 0;
                for (Event event : events) {
                    array.add(event.dumpToJSONObject());
                    if (i == 0) {
                        moduleBuilder.append(event.module);
                        prointBuilder.append(event.monitorPoint);
                    } else {
                        moduleBuilder.append(",");
                        moduleBuilder.append(event.module);
                        prointBuilder.append(",");
                        prointBuilder.append(event.monitorPoint);
                    }
                    i++;
                    BalancedPool.getInstance().offer(event);
                }
                eventInfo.put("data", array);
                utEvent.args.put(eventType.getAggregateEventArgsKey(), JSON.toJSONString(eventInfo));
                String modules = moduleBuilder.toString();
                String points = prointBuilder.toString();
                utEvent.args.put(LogField.ARG1.toString(), modules);
                utEvent.args.put(LogField.ARG2.toString(), points);
                utEvent.arg1 = modules;
                utEvent.arg2 = points;
                sendUTEventWithPlugin(utEvent);
                BalancedPool.getInstance().offer(array);
            }
            BalancedPool.getInstance().offer(utDimensionValues);
        }
    }

    public static void sendRealDebugEvent(UTDimensionValueSet utDimensionValue, Event event) {
        Integer eventId = utDimensionValue.getEventId();
        if (eventId != null) {
            EventType eventType = EventType.getEventType(eventId.intValue());
            UTEvent utEvent = (UTEvent) BalancedPool.getInstance().poll(UTEvent.class, new Object[0]);
            utEvent.eventId = UTConstants.EventID.AGGREGATION_LOG;
            utEvent.arg1 = event.module;
            utEvent.arg2 = event.monitorPoint;
            if (utDimensionValue.getMap() != null) {
                utEvent.args.putAll(utDimensionValue.getMap());
                utEvent.args.remove("commitDay");
            }
            Map<String, Object> eventInfo = new HashMap<>();
            eventInfo.put("meta", SdkMeta.getSDKMetaData());
            eventInfo.put("_event_id", eventId);
            ReuseJSONArray array = (ReuseJSONArray) BalancedPool.getInstance().poll(ReuseJSONArray.class, new Object[0]);
            array.add(event.dumpToJSONObject());
            BalancedPool.getInstance().offer(event);
            eventInfo.put("data", array);
            utEvent.args.put(eventType.getAggregateEventArgsKey(), JSON.toJSONString(eventInfo));
            utEvent.args.put(LogField.EVENTID.toString(), String.valueOf(UTConstants.EventID.AGGREGATION_LOG));
            sendUTEventWithPlugin(utEvent);
            BalancedPool.getInstance().offer(array);
        }
    }

    public static void sendAppException(UTEvent utEvent) {
        if (utEvent != null) {
            LogStoreMgr.getInstance().add(new Log(utEvent.page, String.valueOf(utEvent.eventId), utEvent.arg1, utEvent.arg2, utEvent.arg3, utEvent.args));
            BalancedPool.getInstance().offer(utEvent);
        }
    }

    public static void sendUTEventWithPlugin(UTEvent utEvent) {
        LogStoreMgr.getInstance().add(new Log(utEvent.page, String.valueOf(utEvent.eventId), utEvent.arg1, utEvent.arg2, utEvent.arg3, utEvent.args));
        BalancedPool.getInstance().offer(utEvent);
    }
}
