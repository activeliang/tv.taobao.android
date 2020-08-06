package android.taobao.windvane.xmlmanager;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.io.IOException;
import java.lang.reflect.Method;

public class WVLoadableResources {
    private static final Method AssetManager_addAssetPath;
    private static final String TAG = "DynRes";
    final AssetManager mAssets;
    final int mCookie;

    public WVLoadableResources(String path) {
        try {
            this.mAssets = AssetManager.class.newInstance();
            Method AssetManager_addAssetPath2 = AssetManager_addAssetPath;
            if (AssetManager_addAssetPath2 == null) {
                throw new IllegalStateException();
            }
            this.mCookie = ((Integer) AssetManager_addAssetPath2.invoke(this.mAssets, new Object[]{path})).intValue();
            if (this.mCookie == 0) {
                throw new IllegalArgumentException("Failed to set asset path: " + path);
            }
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    public XmlResourceParser openXmlResourceParser(String file) throws IOException {
        return this.mAssets.openXmlResourceParser(this.mCookie, file);
    }

    public View loadLayout(Context context, String file) {
        Resources current = context.getResources();
        try {
            return LayoutInflater.from(context).inflate(new Resources(this.mAssets, current.getDisplayMetrics(), current.getConfiguration()).getAssets().openXmlResourceParser(file), (ViewGroup) null);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mAssets != null) {
                this.mAssets.close();
            }
        } catch (Throwable e) {
            e.fillInStackTrace();
        }
        super.finalize();
    }

    static {
        Method method;
        try {
            method = AssetManager.class.getMethod("addAssetPath", new Class[]{String.class});
        } catch (NoSuchMethodException e) {
            Log.w(TAG, "Incompatible ROM: No matched AssetManager.addAssetPath().");
            for (Method candidate : AssetManager.class.getMethods()) {
                if (candidate.getName().startsWith("addAssetPath")) {
                    Log.w(TAG, "  Possible variant: " + candidate);
                }
            }
            method = null;
        }
        AssetManager_addAssetPath = method;
    }
}
