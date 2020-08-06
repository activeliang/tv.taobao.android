package com.taobao.securityjni;

import android.content.ContextWrapper;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.dynamicdatastore.IDynamicDataStoreComponent;

@Deprecated
public class DynamicDataStore {
    private IDynamicDataStoreComponent proxy;

    public DynamicDataStore(ContextWrapper context) {
        SecurityGuardManager manager = SecurityGuardManager.getInstance(context);
        if (manager != null) {
            this.proxy = manager.getDynamicDataStoreComp();
        }
    }

    public int putString(String key, String value) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.putString(key, value);
    }

    public int putInt(String key, int value) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.putInt(key, value);
    }

    public int putFloat(String key, float value) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.putFloat(key, value);
    }

    public int putBoolean(String key, boolean value) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.putBoolean(key, value);
    }

    public int putLong(String key, long value) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.putLong(key, value);
    }

    public int putByteArray(String key, byte[] value) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.putByteArray(key, value);
    }

    public String getString(String key) {
        if (this.proxy == null) {
            return null;
        }
        return this.proxy.getString(key);
    }

    public String getStringCompat(String key) {
        return getString(key);
    }

    public int getInt(String key) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.getInt(key);
    }

    public float getFloat(String key) {
        if (this.proxy == null) {
            return -1.0f;
        }
        return this.proxy.getFloat(key);
    }

    public long getLong(String key) {
        if (this.proxy == null) {
            return -1;
        }
        return this.proxy.getLong(key);
    }

    public long getLongCompat(String key) {
        return getLong(key);
    }

    public boolean getBoolean(String key) {
        if (this.proxy == null) {
            return false;
        }
        return this.proxy.getBoolean(key);
    }

    public byte[] getByteArray(String key) {
        if (this.proxy == null) {
            return null;
        }
        return this.proxy.getByteArray(key);
    }

    public void removeString(String key) {
        if (this.proxy != null) {
            this.proxy.removeString(key);
        }
    }

    public void removeInt(String key) {
        if (this.proxy != null) {
            this.proxy.removeInt(key);
        }
    }

    public void removeFloat(String key) {
        if (this.proxy != null) {
            this.proxy.removeFloat(key);
        }
    }

    public void removeLong(String key) {
        if (this.proxy != null) {
            this.proxy.removeLong(key);
        }
    }

    public void removeBoolean(String key) {
        if (this.proxy != null) {
            this.proxy.removeBoolean(key);
        }
    }

    public void removeByteArray(String key) {
        if (this.proxy != null) {
            this.proxy.removeByteArray(key);
        }
    }

    public int putStringDDp(String key, String value) {
        return 0;
    }

    public int putByteArrayDDp(String key, byte[] value) {
        return 0;
    }

    public String getStringDDp(String key) {
        return null;
    }

    public byte[] getByteArrayDDp(String key) {
        return null;
    }

    public void removeStringDDp(String key) {
    }

    public void removeByteArrayDDp(String key) {
    }
}
