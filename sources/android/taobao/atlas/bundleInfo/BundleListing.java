package android.taobao.atlas.bundleInfo;

import android.text.TextUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class BundleListing implements Serializable {
    public LinkedHashMap<String, BundleInfo> bundles = new LinkedHashMap<>();

    public LinkedHashMap<String, BundleInfo> getBundles() {
        return this.bundles;
    }

    public void setBundles(LinkedHashMap<String, BundleInfo> bundles2) {
        this.bundles = bundles2;
    }

    public static class BundleInfo {
        public HashMap<String, Boolean> activities;
        public String applicationName;
        public HashMap<String, Boolean> contentProviders;
        public List<String> dependency;
        public String desc;
        public boolean isInternal = true;
        public boolean isMBundle = false;
        public String md5;
        public String name;
        public String pkgName;
        public HashMap<String, Boolean> receivers;
        public HashMap<String, String> remoteFragments;
        public HashMap<String, String> remoteTransactors;
        public HashMap<String, String> remoteViews;
        public HashMap<String, Boolean> services;
        public long size;
        public List<String> totalDependency;
        public String unique_tag;
        public String url;
        public String version;

        public long getSize() {
            return this.size;
        }

        public boolean isMBundle() {
            return this.isMBundle;
        }

        public boolean isInternal() {
            return this.isInternal;
        }

        public String getApplicationName() {
            return this.applicationName;
        }

        public HashMap<String, Boolean> getReceivers() {
            return this.receivers;
        }

        public HashMap<String, Boolean> getContentProviders() {
            return this.contentProviders;
        }

        public String getUnique_tag() {
            return this.unique_tag;
        }

        public String getMd5() {
            return this.md5;
        }

        public String getUrl() {
            return this.url;
        }

        public String getDesc() {
            return this.desc;
        }

        public String getName() {
            return this.name;
        }

        public String getPkgName() {
            return this.pkgName;
        }

        public String getVersion() {
            return this.version;
        }

        public List<String> getDependency() {
            return this.dependency;
        }

        public void setDependency(List<String> dependency2) {
            if (dependency2 != null && dependency2.size() > 0) {
                int x = 0;
                while (x < dependency2.size()) {
                    if (TextUtils.isEmpty(dependency2.get(x))) {
                        dependency2.remove(x);
                    } else {
                        x++;
                    }
                }
            }
            this.dependency = dependency2;
        }

        public HashMap<String, Boolean> getActivities() {
            return this.activities;
        }

        public HashMap<String, Boolean> getServices() {
            return this.services;
        }

        public HashMap<String, Boolean> getComponents() {
            HashMap<String, Boolean> components = new HashMap<>();
            if (this.activities != null && this.activities.size() > 0) {
                components.putAll(this.activities);
            }
            if (this.services != null && this.services.size() > 0) {
                components.putAll(this.services);
            }
            if (this.receivers != null && this.receivers.size() > 0) {
                components.putAll(this.receivers);
            }
            if (this.contentProviders != null && this.contentProviders.size() > 0) {
                components.putAll(this.contentProviders);
            }
            return components;
        }

        public synchronized void addRuntimeDependency(String location) {
            if (this.totalDependency != null) {
                getTotalDependency();
            }
            if (!this.totalDependency.contains(location)) {
                this.totalDependency.add(location);
            }
        }

        public synchronized List<String> getTotalDependency() {
            ArrayList<String> temp_dependency;
            if (this.totalDependency == null) {
                this.totalDependency = new ArrayList();
                findBundleTransitively(getPkgName(), this.totalDependency);
            }
            temp_dependency = new ArrayList<>();
            temp_dependency.addAll(this.totalDependency);
            return temp_dependency;
        }

        private void findBundleTransitively(String location, List<String> bundlesListForInstall) {
            findBundleTransitivelyInternal(location, bundlesListForInstall, location);
        }

        private void findBundleTransitivelyInternal(String location, List<String> bundlesListForInstall, String root) {
            if (!bundlesListForInstall.contains(location)) {
                bundlesListForInstall.add(0, location);
                List<String> singleLevelDependencies = AtlasBundleInfoManager.instance().getDependencyForBundle(location);
                if (singleLevelDependencies != null) {
                    for (String dependepcy : singleLevelDependencies) {
                        if (dependepcy != null) {
                            findBundleTransitivelyInternal(dependepcy, bundlesListForInstall, root);
                        }
                    }
                }
            } else if (!location.equals(root)) {
                bundlesListForInstall.remove(location);
                bundlesListForInstall.add(0, location);
            }
        }
    }
}
