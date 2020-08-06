package android.taobao.atlas.runtime;

import android.os.Build;
import android.taobao.atlas.framework.BundleImpl;
import android.taobao.atlas.framework.Framework;
import android.taobao.atlas.hack.AtlasHacks;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.text.TextUtils;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import mtopsdk.common.util.SymbolExpUtil;
import org.osgi.framework.Bundle;

public class ResourceIdFetcher {
    private Map<String, ResInfo> resIdentifierMap = new ConcurrentHashMap();

    public int getIdentifier(String name, String defType, String defPackage) {
        if (Build.VERSION.SDK_INT <= 19) {
            return 0;
        }
        if ("mzThemeColor".equals(name) && "attr".equals(defType)) {
            return 0;
        }
        if (AtlasHacks.AssetManager_getResourceIdentifier == null) {
            return getIdentifierWithRefection(name, defType, defPackage);
        }
        return getIdentifierWithRefection(name, defType, defPackage);
    }

    private int getIdentifierWithRefection(String name, String defType, String defPackage) {
        List<Bundle> bundles;
        ClassLoader classloader;
        ResInfo info;
        if (defType == null && defPackage == null) {
            if (TextUtils.isEmpty(name)) {
                return 0;
            }
            String rawName = name;
            name = rawName.substring(name.indexOf(WVNativeCallbackUtil.SEPERATER) + 1);
            defType = rawName.substring(rawName.indexOf(SymbolExpUtil.SYMBOL_COLON) + 1, rawName.indexOf(WVNativeCallbackUtil.SEPERATER));
        }
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(defType) && (bundles = Framework.getBundles()) != null && !bundles.isEmpty()) {
            for (Bundle b : Framework.getBundles()) {
                String pkgName = b.getLocation();
                String searchKey = pkgName + SymbolExpUtil.SYMBOL_COLON + name;
                if (!this.resIdentifierMap.isEmpty() && this.resIdentifierMap.containsKey(searchKey) && (info = this.resIdentifierMap.get(searchKey)) != null && info.type != null && defType != null && info.type.equals(defType)) {
                    return info.resId;
                }
                BundleImpl bundle = (BundleImpl) b;
                if (bundle.getArchive().isDexOpted() && (classloader = bundle.getClassLoader()) != null) {
                    try {
                        int tmpResID = getFieldValueOfR(classloader.loadClass(pkgName + ".R$" + defType), name);
                        if (tmpResID != 0) {
                            this.resIdentifierMap.put(searchKey, new ResInfo(defType, tmpResID));
                            return tmpResID;
                        }
                    } catch (ClassNotFoundException e) {
                    }
                }
            }
        }
        return 0;
    }

    private static int getFieldValueOfR(Class<?> clazz, String fName) {
        if (clazz != null) {
            try {
                Field field = clazz.getDeclaredField(fName);
                if (field != null) {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    return ((Integer) field.get((Object) null)).intValue();
                }
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException e) {
            }
        }
        return 0;
    }

    public static class ResInfo {
        public int resId;
        public String type;

        public ResInfo(String type2, int resId2) {
            this.resId = resId2;
            this.type = type2;
        }
    }
}
