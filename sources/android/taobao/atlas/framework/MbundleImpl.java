package android.taobao.atlas.framework;

import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.bundleInfo.BundleListing;
import android.taobao.atlas.framework.bundlestorage.BundleArchive;
import android.taobao.atlas.util.log.impl.AtlasMonitor;
import android.util.Log;
import com.yunos.tvtaobao.biz.common.BaseConfig;
import java.util.HashMap;
import java.util.Map;
import org.osgi.framework.BundleException;

public class MbundleImpl extends BundleImpl {
    public MbundleImpl(String location) throws Exception {
        super(location);
        this.archive = new MbundleArchive(location);
        Framework.bundles.put(location, this);
    }

    public BundleArchive getArchive() {
        return this.archive;
    }

    public ClassLoader getClassLoader() {
        return Framework.getSystemClassLoader();
    }

    public boolean checkValidate() {
        for (String bundle : AtlasBundleInfoManager.instance().getBundleInfo(this.location).getTotalDependency()) {
            BundleListing.BundleInfo bundleInfo = AtlasBundleInfoManager.instance().getBundleInfo(bundle);
            if (bundleInfo == null || bundleInfo.isMBundle()) {
                BundleImpl dependencyBundle = (BundleImpl) Atlas.getInstance().getBundle(bundle);
                if (dependencyBundle != null) {
                    try {
                        dependencyBundle.start();
                    } catch (BundleException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("MbundleImpl", this.location + " dependency -->" + bundle + " is not installed");
                    return false;
                }
            } else {
                Map<String, Object> detailMap = new HashMap<>();
                detailMap.put(BaseConfig.INTENT_KEY_SOURCE, this.location);
                detailMap.put("dependency", bundle);
                detailMap.put("method", "checkValidate()");
                AtlasMonitor.getInstance().report(AtlasMonitor.BUNDLE_DEPENDENCY_ERROR, detailMap, new IllegalArgumentException());
                Log.e("MbundleImpl", this.location + " Mbundle can not dependency bundle--> " + bundle);
            }
        }
        return true;
    }

    public void optDexFile() {
        this.archive.optDexFile();
    }
}
