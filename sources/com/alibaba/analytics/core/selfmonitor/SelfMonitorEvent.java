package com.alibaba.analytics.core.selfmonitor;

import com.alibaba.appmonitor.event.EventType;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import java.util.HashMap;

public class SelfMonitorEvent {
    public static int CLEAN_DB = 2;
    public static int CONFIG_ARRIVE = 6;
    public static int DATALEN_OVERFLOW = 11;
    public static int DB_MONITOR = 5;
    public static int INTERFACE = 1;
    public static int LOGS_TIMEOUT = 12;
    public static int TNET_CREATE_SESSION = 8;
    public static int TNET_REQUEST_ERROR = 10;
    public static int TNET_REQUEST_SEND = 7;
    public static final String TNET_REQUEST_SEND_OFFLINE = "tnet_request_send";
    public static int TNET_REQUEST_TIMEOUT = 9;
    public static int UPLOAD_FAILED = 3;
    public static int UPLOAD_TRAFFIC = 4;
    public static final String UPLOAD_TRAFFIC_OFFLINE = "upload_traffic";
    private static HashMap<Integer, String> mMonitorPoints = new HashMap<>();
    public static final String module = "AppMonitor";
    public String arg;
    public DimensionValueSet dvs;
    public String monitorPoint = "";
    public MeasureValueSet mvs;
    public EventType type = null;
    public Double value;

    static {
        mMonitorPoints.put(Integer.valueOf(INTERFACE), "sampling_monitor");
        mMonitorPoints.put(Integer.valueOf(CLEAN_DB), "db_clean");
        mMonitorPoints.put(Integer.valueOf(DB_MONITOR), "db_monitor");
        mMonitorPoints.put(Integer.valueOf(UPLOAD_FAILED), "upload_failed");
        mMonitorPoints.put(Integer.valueOf(UPLOAD_TRAFFIC), UPLOAD_TRAFFIC_OFFLINE);
        mMonitorPoints.put(Integer.valueOf(CONFIG_ARRIVE), "config_arrive");
        mMonitorPoints.put(Integer.valueOf(TNET_REQUEST_SEND), TNET_REQUEST_SEND_OFFLINE);
        mMonitorPoints.put(Integer.valueOf(TNET_CREATE_SESSION), "tnet_create_session");
        mMonitorPoints.put(Integer.valueOf(TNET_REQUEST_TIMEOUT), "tnet_request_timeout");
        mMonitorPoints.put(Integer.valueOf(TNET_REQUEST_ERROR), "tent_request_error");
        mMonitorPoints.put(Integer.valueOf(DATALEN_OVERFLOW), "datalen_overflow");
        mMonitorPoints.put(Integer.valueOf(LOGS_TIMEOUT), "logs_timeout");
    }

    public static SelfMonitorEvent buildCountEvent(int type2, String arg2, Double value2) {
        return new SelfMonitorEvent(getMonitorPoint(type2), arg2, value2);
    }

    @Deprecated
    public static SelfMonitorEvent buildStatEvent(int type2, DimensionValueSet dvs2, MeasureValueSet mvs2) {
        return new SelfMonitorEvent(getMonitorPoint(type2), dvs2, mvs2);
    }

    private SelfMonitorEvent(String monitorPoint2, String arg2, Double value2) {
        this.monitorPoint = monitorPoint2;
        this.arg = arg2;
        this.value = value2;
        this.type = EventType.COUNTER;
    }

    public SelfMonitorEvent(String monitorPoint2, DimensionValueSet dvs2, MeasureValueSet mvs2) {
        this.monitorPoint = monitorPoint2;
        this.dvs = dvs2;
        this.mvs = mvs2;
        this.type = EventType.STAT;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("SelfMonitorEvent{");
        sb.append("arg='").append(this.arg).append('\'');
        sb.append(", monitorPoint='").append(this.monitorPoint).append('\'');
        sb.append(", type=").append(this.type);
        sb.append(", value=").append(this.value);
        sb.append(", dvs=").append(this.dvs);
        sb.append(", mvs=").append(this.mvs);
        sb.append('}');
        return sb.toString();
    }

    private static String getMonitorPoint(int type2) {
        return mMonitorPoints.get(Integer.valueOf(type2));
    }
}
