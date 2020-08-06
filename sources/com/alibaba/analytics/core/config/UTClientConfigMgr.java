package com.alibaba.analytics.core.config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UTClientConfigMgr {
    private static final String TAG = "UTClientConfigMgr";
    private static UTClientConfigMgr mInstance = null;
    private boolean bInit = false;
    private Map<String, String> mConfigMap = Collections.synchronizedMap(new HashMap());
    private Map<String, List<IConfigChangeListener>> mListeners = Collections.synchronizedMap(new HashMap());

    public interface IConfigChangeListener {
        String getKey();

        void onChange(String str);
    }

    private UTClientConfigMgr() {
    }

    public static UTClientConfigMgr getInstance() {
        if (mInstance == null) {
            synchronized (UTClientConfigMgr.class) {
                if (mInstance == null) {
                    mInstance = new UTClientConfigMgr();
                }
            }
        }
        return mInstance;
    }

    public synchronized void init() {
        if (!this.bInit) {
            try {
                ClientVariables.getInstance().getContext().registerReceiver(new ConfigReceiver(), new IntentFilter("com.alibaba.analytics.config.change"));
                this.bInit = true;
                Logger.d(TAG, "registerReceiver");
            } catch (Throwable throwable) {
                Logger.w(TAG, throwable, new Object[0]);
            }
        }
        return;
    }

    public synchronized String get(String key) {
        return this.mConfigMap.get(key);
    }

    public synchronized void registerConfigChangeListener(IConfigChangeListener listener) {
        List<IConfigChangeListener> listeners;
        if (listener != null) {
            if (!TextUtils.isEmpty(listener.getKey())) {
                String key = listener.getKey();
                if (this.mConfigMap.containsKey(key)) {
                    listener.onChange(this.mConfigMap.get(key));
                }
                if (this.mListeners.get(key) == null) {
                    listeners = new ArrayList<>();
                } else {
                    listeners = this.mListeners.get(key);
                }
                if (!listeners.contains(listener)) {
                    listeners.add(listener);
                }
                this.mListeners.put(key, listeners);
            }
        }
    }

    public synchronized void unRegisterConfigChangeListener(IConfigChangeListener listener) {
        if (listener != null) {
            if (!TextUtils.isEmpty(listener.getKey())) {
                List<IConfigChangeListener> lists = this.mListeners.get(listener.getKey());
                if (lists != null) {
                    lists.remove(listener);
                }
            }
        }
    }

    class ConfigReceiver extends BroadcastReceiver {
        ConfigReceiver() {
        }

        public void onReceive(final Context context, final Intent intent) {
            TaskExecutor.getInstance().submit(new Runnable() {
                public void run() {
                    try {
                        String packageName = context.getPackageName();
                        if (!TextUtils.isEmpty(packageName)) {
                            String intentPackage = intent.getPackage();
                            if (!TextUtils.isEmpty(intentPackage) && packageName.equalsIgnoreCase(intentPackage)) {
                                UTClientConfigMgr.this.dispatchConfig(intent.getStringExtra("key"), intent.getStringExtra("value"));
                            }
                        }
                    } catch (Throwable throwable) {
                        Logger.e(UTClientConfigMgr.TAG, throwable, new Object[0]);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public synchronized void dispatchConfig(String key, String value) {
        Logger.d(TAG, "dispatchConfig key", key, "value", value);
        if (!TextUtils.isEmpty(key)) {
            this.mConfigMap.put(key, value);
            List<IConfigChangeListener> listeners = this.mListeners.get(key);
            if (listeners != null) {
                for (int i = 0; i < listeners.size(); i++) {
                    listeners.get(i).onChange(value);
                }
            }
        }
    }
}
