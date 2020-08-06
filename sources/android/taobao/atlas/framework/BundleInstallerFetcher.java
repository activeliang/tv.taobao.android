package android.taobao.atlas.framework;

public class BundleInstallerFetcher {
    private static final Object mAccessLock = new Object();
    private static BundleInstaller mCachedBundleInstaller = null;

    public static BundleInstaller obtainInstaller() {
        synchronized (mAccessLock) {
            BundleInstaller installer = mCachedBundleInstaller;
            if (installer != null) {
                mCachedBundleInstaller = null;
                return installer;
            }
            BundleInstaller installer2 = new BundleInstaller();
            return installer2;
        }
    }

    public static void recycle(BundleInstaller installer) {
        synchronized (mAccessLock) {
            if (mCachedBundleInstaller == null) {
                if (installer != null) {
                    installer.release();
                }
                mCachedBundleInstaller = installer;
            }
        }
    }
}
