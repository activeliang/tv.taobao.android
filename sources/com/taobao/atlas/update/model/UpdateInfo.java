package com.taobao.atlas.update.model;

import android.taobao.atlas.runtime.RuntimeVariables;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UpdateInfo implements Serializable {
    public String baseVersion;
    public boolean dexPatch;
    public boolean lowDisk = false;
    public List<Item> updateBundles;
    public String updateVersion;
    public File workDir = new File(RuntimeVariables.androidApplication.getCacheDir(), "atlas_update");

    public static class Item implements Serializable {
        public static final int PATCH_DEX_COLD = 1;
        public static final int PATCH_DEX_C_AND_H = 3;
        public static final int PATCH_DEX_HOT = 2;
        public static final int PATCH_DYNAMIC_ = 0;
        public List<String> dependency;
        public long dexpatchVersion = -1;
        public boolean inherit = false;
        public boolean isMainDex;
        public String name;
        public int patchType;
        public boolean reset = false;
        public boolean resetHotPatch = false;
        public String srcUnitTag;
        public String unitTag;

        public static Item makeCopy(Item origin) {
            Item item = new Item();
            item.isMainDex = origin.isMainDex;
            item.name = origin.name;
            item.unitTag = origin.unitTag;
            item.srcUnitTag = origin.srcUnitTag;
            item.inherit = origin.inherit;
            item.patchType = origin.patchType;
            item.dexpatchVersion = origin.dexpatchVersion;
            item.reset = origin.reset;
            item.resetHotPatch = origin.resetHotPatch;
            if (origin.dependency != null) {
                List<String> copyDependency = new ArrayList<>(origin.dependency.size());
                copyDependency.addAll(origin.dependency);
                item.dependency = copyDependency;
            }
            return item;
        }
    }
}
