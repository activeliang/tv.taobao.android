package android.taobao.atlas.runtime;

import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.Atlas;
import android.taobao.atlas.framework.BundleImpl;
import android.taobao.atlas.framework.BundleInstaller;
import android.taobao.atlas.framework.BundleInstallerFetcher;
import android.text.TextUtils;

public class BundleUtil {
    public static boolean checkBundleStateSyncOnChildThread(String className) {
        String bundleName = AtlasBundleInfoManager.instance().getBundleForComponet(className);
        if (TextUtils.isEmpty(bundleName)) {
            return false;
        }
        BundleImpl impl = (BundleImpl) Atlas.getInstance().getBundle(bundleName);
        if (impl == null || !impl.checkValidate()) {
            BundleInstallerFetcher.obtainInstaller().installTransitivelySync(new String[]{bundleName});
        }
        return true;
    }

    public static boolean checkBundleStateSyncOnUIThread(String className) {
        String bundleName = AtlasBundleInfoManager.instance().getBundleForComponet(className);
        if (TextUtils.isEmpty(bundleName)) {
            return false;
        }
        BundleImpl impl = (BundleImpl) Atlas.getInstance().getBundle(bundleName);
        if (impl == null || !impl.checkValidate()) {
            BundleInstallerFetcher.obtainInstaller().installTransitivelySync(new String[]{bundleName});
        }
        return true;
    }

    public static void checkBundleStateSync(String[] bundleName) {
        BundleInstallerFetcher.obtainInstaller().installSync(bundleName);
    }

    public static boolean checkBundleStateAsync(String bundleName, Runnable bundleActivated, Runnable bundleDisabled) {
        checkBundleArrayStateAsync(new String[]{bundleName}, bundleActivated, bundleDisabled);
        return true;
    }

    public static boolean checkBundleArrayStateAsync(final String[] bundlesName, final Runnable bundleActivated, final Runnable bundleDisabled) {
        BundleInstallerFetcher.obtainInstaller().installTransitivelyAsync(bundlesName, new BundleInstaller.InstallListener() {
            public void onFinished() {
                boolean success = true;
                for (String bundleName : bundlesName) {
                    BundleImpl tmp = (BundleImpl) Atlas.getInstance().getBundle(bundleName);
                    if (tmp == null || !tmp.checkValidate()) {
                        success = false;
                    } else {
                        tmp.startBundle();
                    }
                }
                if (success) {
                    if (bundleActivated != null) {
                        bundleActivated.run();
                    }
                } else if (bundleDisabled != null) {
                    bundleDisabled.run();
                }
            }
        });
        return true;
    }

    public static class CancelableTask implements Runnable {
        private boolean canceled = false;
        private Runnable task;

        public CancelableTask(Runnable task2) {
            this.task = task2;
        }

        public void cancel() {
            this.canceled = true;
        }

        public void run() {
            if (!this.canceled) {
                this.task.run();
            }
        }
    }
}
