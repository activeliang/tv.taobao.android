package com.alibaba.analytics.core.device;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import com.alibaba.analytics.utils.SPHelper;
import com.alibaba.analytics.utils.StringUtils;
import java.util.Map;

public class PersistentConfiguration {
    private String mConfigName = "";
    private Context mContext = null;
    private SharedPreferences.Editor mEditor = null;
    private SharedPreferences mSp = null;

    public PersistentConfiguration(Context context, String folderName, String configName, boolean isSafety, boolean isLessMode) {
        this.mConfigName = configName;
        this.mContext = context;
        if (context != null) {
            this.mSp = context.getSharedPreferences(configName, 0);
        }
    }

    private void initEditor() {
        if (this.mEditor == null && this.mSp != null) {
            this.mEditor = this.mSp.edit();
        }
    }

    public void putInt(String key, int value) {
        initEditor();
        if (this.mEditor != null) {
            this.mEditor.putInt(key, value);
        }
    }

    public void putLong(String key, long value) {
        initEditor();
        if (this.mEditor != null) {
            this.mEditor.putLong(key, value);
        }
    }

    public void putBoolean(String key, boolean value) {
        initEditor();
        if (this.mEditor != null) {
            this.mEditor.putBoolean(key, value);
        }
    }

    public void putFloat(String key, float value) {
        initEditor();
        if (this.mEditor != null) {
            this.mEditor.putFloat(key, value);
        }
    }

    public void putString(String key, String value) {
        initEditor();
        if (this.mEditor != null) {
            this.mEditor.putString(key, value);
        }
    }

    public void remove(String key) {
        initEditor();
        if (this.mEditor != null) {
            this.mEditor.remove(key);
        }
    }

    public void reload() {
        if (this.mSp != null && this.mContext != null) {
            this.mSp = this.mContext.getSharedPreferences(this.mConfigName, 0);
        }
    }

    public void clear() {
        initEditor();
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mEditor != null) {
            this.mEditor.clear();
        }
    }

    public boolean commit() {
        if (this.mEditor != null) {
            if (Build.VERSION.SDK_INT >= 9) {
                SPHelper.apply(this.mEditor);
            } else {
                this.mEditor.commit();
            }
        }
        if (!(this.mSp == null || this.mContext == null)) {
            this.mSp = this.mContext.getSharedPreferences(this.mConfigName, 0);
        }
        return true;
    }

    public String getString(String key) {
        if (this.mSp != null) {
            String value = this.mSp.getString(key, "");
            if (!StringUtils.isEmpty(value)) {
                return value;
            }
        }
        return "";
    }

    public int getInt(String key) {
        if (this.mSp != null) {
            return this.mSp.getInt(key, 0);
        }
        return 0;
    }

    public long getLong(String key) {
        if (this.mSp != null) {
            return this.mSp.getLong(key, 0);
        }
        return 0;
    }

    public float getFloat(String key) {
        if (this.mSp != null) {
            return this.mSp.getFloat(key, 0.0f);
        }
        return 0.0f;
    }

    public boolean getBoolean(String key) {
        if (this.mSp != null) {
            return this.mSp.getBoolean(key, false);
        }
        return false;
    }

    public Map<String, ?> getAll() {
        if (this.mSp != null) {
            return this.mSp.getAll();
        }
        return null;
    }
}
