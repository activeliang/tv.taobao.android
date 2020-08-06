package android.taobao.atlas.runtime;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

public class ContextImplHook extends ContextWrapper {
    private ClassLoader classLoader = null;

    public ContextImplHook(Context base, ClassLoader classLoader2) {
        super(base);
        this.classLoader = classLoader2;
    }

    public Resources getResources() {
        if (RuntimeVariables.delegateResources == null) {
            return getApplicationContext().getResources();
        }
        return RuntimeVariables.delegateResources;
    }

    public AssetManager getAssets() {
        return RuntimeVariables.delegateResources.getAssets();
    }

    public PackageManager getPackageManager() {
        return getApplicationContext().getPackageManager();
    }

    public ClassLoader getClassLoader() {
        if (this.classLoader != null) {
            return this.classLoader;
        }
        return super.getClassLoader();
    }
}
