package com.taobao.securityjni;

import android.content.ContextWrapper;
import com.taobao.securityjni.tools.DataContext;
import com.taobao.wireless.security.sdk.SecurityGuardManager;
import com.taobao.wireless.security.sdk.staticdatastore.IStaticDataStoreComponent;

@Deprecated
public class StaticDataStore {
    public static final int APP_KEY_TYPE = 1;
    public static final int EXTRA_KEY_TYPE = 3;
    public static final int INVALID_KEY_TYPE = 4;
    public static final int SECURITY_KEY_TYPE = 2;
    private IStaticDataStoreComponent proxy;

    public StaticDataStore(ContextWrapper context) {
        SecurityGuardManager manager = SecurityGuardManager.getInstance(context);
        if (manager != null) {
            this.proxy = manager.getStaticDataStoreComp();
        }
    }

    @Deprecated
    public String getAppKey() {
        return getAppKey(new DataContext(0, (byte[]) null));
    }

    public String getAppKey(DataContext ctx) {
        if (ctx == null) {
            return null;
        }
        return getAppKeyByIndex(ctx.index < 0 ? 0 : ctx.index);
    }

    @Deprecated
    public String getTtid() {
        return null;
    }

    @Deprecated
    public String getMMPid() {
        return null;
    }

    public String getExtraData(String key) {
        if (this.proxy == null || key == null) {
            return null;
        }
        return this.proxy.getExtraData(key);
    }

    public int getKeyType(String key) {
        if (this.proxy == null || key == null) {
            return 4;
        }
        return this.proxy.getKeyType(key);
    }

    public String getAppKeyByIndex(int index) {
        if (this.proxy == null || index < 0 || index > 8) {
            return null;
        }
        return this.proxy.getAppKeyByIndex(index);
    }
}
