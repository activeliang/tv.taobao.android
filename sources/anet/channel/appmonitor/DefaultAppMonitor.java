package anet.channel.appmonitor;

import android.text.TextUtils;
import anet.channel.statist.AlarmObject;
import anet.channel.statist.CountObject;
import anet.channel.statist.Dimension;
import anet.channel.statist.Measure;
import anet.channel.statist.Monitor;
import anet.channel.statist.StatObject;
import anet.channel.util.ALog;
import anet.channel.util.StringUtils;
import com.alibaba.mtl.appmonitor.AppMonitor;
import com.alibaba.mtl.appmonitor.model.DimensionSet;
import com.alibaba.mtl.appmonitor.model.DimensionValueSet;
import com.alibaba.mtl.appmonitor.model.MeasureSet;
import com.alibaba.mtl.appmonitor.model.MeasureValueSet;
import com.taobao.orange.OConstant;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultAppMonitor implements IAppMonitor {
    private static final String TAG = "awcn.DefaultAppMonitor";
    private static Map<Class<?>, List<Field>> dimensionFieldsCache = new ConcurrentHashMap();
    private static Map<Field, String> field2Name = new ConcurrentHashMap();
    private static boolean mAppMonitorValid = false;
    private static Map<Class<?>, List<Field>> measureFieldsCache = new ConcurrentHashMap();
    private static Random random = new Random();
    private static Set<Class<?>> registeredStatClassSet = Collections.newSetFromMap(new ConcurrentHashMap());

    public DefaultAppMonitor() {
        try {
            Class.forName(OConstant.REFLECT_APPMONITOR);
            mAppMonitorValid = true;
        } catch (Exception e) {
            mAppMonitorValid = false;
        }
    }

    @Deprecated
    public void register() {
    }

    @Deprecated
    public void register(Class<?> cls) {
    }

    public void commitStat(StatObject obj) {
        String obj2;
        if (mAppMonitorValid && obj != null) {
            Class<?> cls = obj.getClass();
            Monitor monitor = (Monitor) cls.getAnnotation(Monitor.class);
            if (monitor != null) {
                if (!registeredStatClassSet.contains(cls)) {
                    registerStatClass(cls);
                }
                if (obj.beforeCommit()) {
                    int sampleRate = monitor.sampleRate();
                    if (sampleRate > 10000 || sampleRate < 0) {
                        sampleRate = 10000;
                    }
                    if (sampleRate == 10000 || random.nextInt(10000) < sampleRate) {
                        try {
                            DimensionValueSet dimensionValueSet = DimensionValueSet.create();
                            MeasureValueSet measureValueSet = MeasureValueSet.create();
                            List<Field> dimensionFields = dimensionFieldsCache.get(cls);
                            Map<String, Double> toPrintMeasures = null;
                            if (ALog.isPrintLog(1)) {
                                toPrintMeasures = new HashMap<>();
                            }
                            if (dimensionFields != null) {
                                for (Field field : dimensionFields) {
                                    Object value = field.get(obj);
                                    String str = field2Name.get(field);
                                    if (value == null) {
                                        obj2 = "";
                                    } else {
                                        obj2 = value.toString();
                                    }
                                    dimensionValueSet.setValue(str, obj2);
                                }
                                for (Field field2 : measureFieldsCache.get(cls)) {
                                    Double value2 = Double.valueOf(field2.getDouble(obj));
                                    measureValueSet.setValue(field2Name.get(field2), value2.doubleValue());
                                    if (toPrintMeasures != null) {
                                        toPrintMeasures.put(field2Name.get(field2), value2);
                                    }
                                }
                            }
                            AppMonitor.Stat.commit(monitor.module(), monitor.monitorPoint(), dimensionValueSet, measureValueSet);
                            if (ALog.isPrintLog(1)) {
                                ALog.d(TAG, "commit stat: " + monitor.monitorPoint(), (String) null, "\nDimensions", dimensionValueSet.getMap().toString(), "\nMeasures", toPrintMeasures.toString());
                            }
                        } catch (Throwable e) {
                            ALog.e(TAG, "commit monitor point failed", (String) null, e, new Object[0]);
                        }
                    }
                }
            }
        }
    }

    public void commitAlarm(AlarmObject obj) {
        if (mAppMonitorValid && obj != null && !TextUtils.isEmpty(obj.module) && !TextUtils.isEmpty(obj.modulePoint)) {
            if (ALog.isPrintLog(1)) {
                ALog.d(TAG, "commit alarm: " + obj, (String) null, new Object[0]);
            }
            if (!obj.isSuccess) {
                AppMonitor.Alarm.commitFail(obj.module, obj.modulePoint, StringUtils.stringNull2Empty(obj.arg), StringUtils.stringNull2Empty(obj.errorCode), StringUtils.stringNull2Empty(obj.errorMsg));
            } else {
                AppMonitor.Alarm.commitSuccess(obj.module, obj.modulePoint, StringUtils.stringNull2Empty(obj.arg));
            }
        }
    }

    public void commitCount(CountObject obj) {
        if (mAppMonitorValid && obj != null && !TextUtils.isEmpty(obj.module) && !TextUtils.isEmpty(obj.modulePoint)) {
            if (ALog.isPrintLog(2)) {
                ALog.i(TAG, "commit count: " + obj, (String) null, new Object[0]);
            }
            AppMonitor.Counter.commit(obj.module, obj.modulePoint, StringUtils.stringNull2Empty(obj.arg), obj.value);
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void registerStatClass(Class<?> cls) {
        if (cls != null) {
            if (mAppMonitorValid) {
                try {
                    if (!registeredStatClassSet.contains(cls)) {
                        Monitor monitor = (Monitor) cls.getAnnotation(Monitor.class);
                        if (monitor != null) {
                            Field[] fields = cls.getDeclaredFields();
                            List<Field> dimensionFieldList = new ArrayList<>();
                            List<Field> measureFieldList = new ArrayList<>();
                            DimensionSet dimensionSet = DimensionSet.create();
                            MeasureSet measureSet = MeasureSet.create();
                            for (Field field : fields) {
                                Dimension dimension = (Dimension) field.getAnnotation(Dimension.class);
                                if (dimension != null) {
                                    field.setAccessible(true);
                                    dimensionFieldList.add(field);
                                    String name = dimension.name().equals("") ? field.getName() : dimension.name();
                                    field2Name.put(field, name);
                                    dimensionSet.addDimension(name);
                                } else {
                                    Measure measure = (Measure) field.getAnnotation(Measure.class);
                                    if (measure != null) {
                                        field.setAccessible(true);
                                        measureFieldList.add(field);
                                        String name2 = measure.name().equals("") ? field.getName() : measure.name();
                                        field2Name.put(field, name2);
                                        if (measure.max() != Double.MAX_VALUE) {
                                            measureSet.addMeasure(new com.alibaba.mtl.appmonitor.model.Measure(name2, Double.valueOf(measure.constantValue()), Double.valueOf(measure.min()), Double.valueOf(measure.max())));
                                        } else {
                                            measureSet.addMeasure(name2);
                                        }
                                    }
                                }
                            }
                            dimensionFieldsCache.put(cls, dimensionFieldList);
                            measureFieldsCache.put(cls, measureFieldList);
                            AppMonitor.register(monitor.module(), monitor.monitorPoint(), measureSet, dimensionSet);
                            registeredStatClassSet.add(cls);
                        }
                    }
                } catch (Exception e) {
                    ALog.e(TAG, "register fail", (String) null, e, new Object[0]);
                }
            }
        }
        return;
    }
}
