package com.alibaba.analytics.core.config;

import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.db.Entity;
import com.alibaba.analytics.core.model.LogField;
import com.alibaba.analytics.utils.Logger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class SystemConfigMgr extends UTOrangeConfBiz {
    private static final String DELAY = "delay";
    private static SystemConfigMgr mInstance = null;
    private final Map<String, String> mKVStore = Collections.synchronizedMap(new HashMap());
    private final Map<String, List<IKVChangeListener>> mListeners = Collections.synchronizedMap(new HashMap());
    private final Map<String, UTSystemDelayItem> mSystemDelayItemMap = new HashMap();
    private final String[] namespace = {"utap_system"};

    public interface IKVChangeListener {
        void onChange(String str, String str2);
    }

    private SystemConfigMgr() {
        try {
            if (Variables.getInstance().getDbMgr() != null) {
                List<? extends Entity> find = Variables.getInstance().getDbMgr().find(SystemConfig.class, (String) null, (String) null, -1);
                if (find.size() > 0) {
                    Map<String, String> map = Collections.synchronizedMap(new HashMap(find.size()));
                    for (int i = 0; i < find.size(); i++) {
                        map.put(((SystemConfig) find.get(i)).key, ((SystemConfig) find.get(i)).value);
                    }
                    updateConfig(map);
                }
            }
        } catch (Throwable e) {
            Logger.e((String) null, e, new Object[0]);
        }
    }

    public static synchronized SystemConfigMgr getInstance() {
        SystemConfigMgr systemConfigMgr;
        synchronized (SystemConfigMgr.class) {
            if (mInstance == null) {
                mInstance = new SystemConfigMgr();
            }
            systemConfigMgr = mInstance;
        }
        return systemConfigMgr;
    }

    public String get(String key) {
        return this.mKVStore.get(key);
    }

    public void register(String key, IKVChangeListener listener) {
        List<IKVChangeListener> listeners;
        synchronized (this.mListeners) {
            if (this.mListeners.get(key) == null) {
                listeners = new ArrayList<>();
            } else {
                listeners = this.mListeners.get(key);
            }
            listeners.add(listener);
            this.mListeners.put(key, listeners);
        }
    }

    public void unRegister(String key, IKVChangeListener listener) {
        List<IKVChangeListener> lists = this.mListeners.get(key);
        if (lists != null) {
            lists.remove(listener);
        }
        if (lists == null || lists.size() == 0) {
            this.mKVStore.remove(key);
        }
    }

    public String[] getOrangeGroupnames() {
        return this.namespace;
    }

    public void onOrangeConfigurationArrive(String aConfName, Map<String, String> map) {
        if ("utap_system".equalsIgnoreCase(aConfName)) {
            updateConfig(map);
            Variables.getInstance().getDbMgr().clear((Class<? extends Entity>) SystemConfig.class);
            Variables.getInstance().getDbMgr().insert((List<? extends Entity>) mapToList(this.mKVStore));
        }
    }

    private void updateConfig(Map<String, String> map) {
        updateSystemDelayItemMap(map);
        Map<String, String> temp = new HashMap<>(this.mKVStore.size());
        temp.putAll(this.mKVStore);
        this.mKVStore.clear();
        this.mKVStore.putAll(map);
        for (String key : this.mKVStore.keySet()) {
            if ((this.mKVStore.get(key) == null && temp.get(key) != null) || (this.mKVStore.get(key) != null && !this.mKVStore.get(key).equalsIgnoreCase(temp.get(key)))) {
                dispatch(key, this.mKVStore.get(key));
            }
            temp.remove(key);
        }
        for (String key2 : temp.keySet()) {
            dispatch(key2, this.mKVStore.get(key2));
        }
    }

    private void dispatch(String key, String value) {
        List<IKVChangeListener> listeners = this.mListeners.get(key);
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                listeners.get(i).onChange(key, value);
            }
        }
        UTConfigMgr.postServerConfig(key, value);
    }

    private List<Entity> mapToList(Map<String, String> map) {
        List<Entity> es = new ArrayList<>(map.size());
        for (String key : map.keySet()) {
            SystemConfig config = new SystemConfig();
            config.key = key;
            config.value = map.get(key);
            es.add(config);
        }
        return es;
    }

    public int getInt(String key) {
        String value = get(key);
        if (TextUtils.isEmpty(value)) {
            return 0;
        }
        try {
            return Integer.valueOf(value).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    private synchronized void updateSystemDelayItemMap(Map<String, String> map) {
        UTSystemDelayItem uTSampleItem;
        if (map != null) {
            if (map.containsKey(DELAY)) {
                if ((this.mKVStore.get(DELAY) == null || !map.get(DELAY).equals(this.mKVStore.get(DELAY))) && this.mSystemDelayItemMap != null) {
                    this.mSystemDelayItemMap.clear();
                    try {
                        JSONObject delayContent = new JSONObject(map.get(DELAY));
                        Iterator<String> keys = delayContent.keys();
                        if (keys != null) {
                            while (keys.hasNext()) {
                                String key = keys.next();
                                String value = delayContent.getString(key);
                                if (!TextUtils.isEmpty(value) && (uTSampleItem = UTSystemDelayItem.parseJson(value)) != null) {
                                    this.mSystemDelayItemMap.put(key, uTSampleItem);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (this.mSystemDelayItemMap != null) {
            this.mSystemDelayItemMap.clear();
        }
        return;
    }

    public synchronized boolean checkDelayLog(Map<String, String> aLogMap) {
        boolean z;
        if (this.mSystemDelayItemMap == null || this.mSystemDelayItemMap.size() < 1) {
            z = false;
        } else {
            int eventId = -1;
            if (aLogMap.containsKey(LogField.EVENTID.toString())) {
                try {
                    eventId = Integer.parseInt(aLogMap.get(LogField.EVENTID.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (this.mSystemDelayItemMap.containsKey(String.valueOf(eventId))) {
                z = checkDelay(aLogMap, eventId);
            } else {
                int eventId2 = eventId - (eventId % 10);
                if (this.mSystemDelayItemMap.containsKey(String.valueOf(eventId2))) {
                    z = checkDelay(aLogMap, eventId2);
                } else {
                    int eventId3 = eventId2 - (eventId2 % 100);
                    if (this.mSystemDelayItemMap.containsKey(String.valueOf(eventId3))) {
                        z = checkDelay(aLogMap, eventId3);
                    } else {
                        int eventId4 = eventId3 - (eventId3 % 1000);
                        if (this.mSystemDelayItemMap.containsKey(String.valueOf(eventId4))) {
                            z = checkDelay(aLogMap, eventId4);
                        } else {
                            z = this.mSystemDelayItemMap.containsKey(String.valueOf(-1)) ? checkDelay(aLogMap, -1) : false;
                        }
                    }
                }
            }
        }
        return z;
    }

    private boolean checkDelay(Map<String, String> aLogMap, int eventId) {
        UTSystemDelayItem uTSystemDelayItem = this.mSystemDelayItemMap.get(String.valueOf(eventId));
        if (uTSystemDelayItem == null) {
            return false;
        }
        String arg1 = null;
        if (aLogMap.containsKey(LogField.ARG1.toString())) {
            arg1 = aLogMap.get(LogField.ARG1.toString());
        }
        return uTSystemDelayItem.checkDelay(arg1);
    }

    private static class UTSystemDelayItem {
        private static final String KEY_ALL_D = "all_d";
        private static final String KEY_ARG1 = "arg1";
        private int mAllDelay = -1;
        private List<String> mArg1List = new ArrayList();

        private UTSystemDelayItem() {
        }

        public static UTSystemDelayItem parseJson(String jsonStr) {
            try {
                UTSystemDelayItem sampleItem = new UTSystemDelayItem();
                JSONObject dataJson = new JSONObject(jsonStr);
                if (dataJson.has(KEY_ALL_D)) {
                    sampleItem.mAllDelay = dataJson.optInt(KEY_ALL_D, -1);
                }
                if (!dataJson.has(KEY_ARG1)) {
                    return sampleItem;
                }
                List<String> arg1List = new ArrayList<>();
                JSONArray jsonArg1 = dataJson.getJSONArray(KEY_ARG1);
                if (jsonArg1 != null) {
                    for (int i = 0; i < jsonArg1.length(); i++) {
                        arg1List.add(jsonArg1.getString(i));
                    }
                }
                sampleItem.mArg1List = arg1List;
                return sampleItem;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public boolean checkDelay(String arg1) {
            if (this.mAllDelay == 0) {
                return matchArg1Name(arg1);
            }
            if (1 != this.mAllDelay) {
                return false;
            }
            if (matchArg1Name(arg1)) {
                return false;
            }
            return true;
        }

        private boolean matchArg1Name(String arg1) {
            if (!TextUtils.isEmpty(arg1) && this.mArg1List != null) {
                for (int i = 0; i < this.mArg1List.size(); i++) {
                    String value = this.mArg1List.get(i);
                    if (!TextUtils.isEmpty(value)) {
                        if (value.length() <= 2 || !value.startsWith("%") || !value.endsWith("%")) {
                            if (arg1.equals(value)) {
                                return true;
                            }
                        } else if (arg1.contains(value.substring(1, value.length() - 1))) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
}
