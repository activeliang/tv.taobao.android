package android.taobao.atlas.framework.bundlestorage;

import android.taobao.atlas.framework.Framework;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.atlas.versionInfo.BaselineInfoManager;
import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class BundleArchive {
    public static final String DEXPATCH_DIR = "dexpatch/";
    private File bundleDir = null;
    private BundleArchiveRevision currentRevision;

    public BundleArchive(String location, File bundleDir2, String uniqueTag, long dexPatchVersion) throws IOException {
        File revisionDir;
        this.bundleDir = bundleDir2;
        if (RuntimeVariables.sCurrentProcessName.equals(RuntimeVariables.androidApplication.getPackageName()) && !Framework.updateHappend) {
            purge(uniqueTag, dexPatchVersion);
        }
        if (dexPatchVersion > 0) {
            revisionDir = new File(bundleDir2, "dexpatch/" + dexPatchVersion);
            if (!revisionDir.exists()) {
                revisionDir = new File(bundleDir2, uniqueTag);
            }
        } else {
            revisionDir = new File(bundleDir2, uniqueTag);
        }
        if (revisionDir.exists()) {
            try {
                BundleArchiveRevision archiveRevision = new BundleArchiveRevision(location, revisionDir);
                if (archiveRevision != null) {
                    this.currentRevision = archiveRevision;
                } else {
                    this.currentRevision = null;
                }
            } catch (IOException e) {
                throw e;
            } catch (Throwable th) {
                if (0 != 0) {
                    this.currentRevision = null;
                } else {
                    this.currentRevision = null;
                }
                throw th;
            }
        } else if (BaselineInfoManager.instance().isUpdated(location)) {
            throw new MisMatchException("can not find bundle");
        } else {
            throw new IOException("can not find bundle");
        }
    }

    public BundleArchive(String location, File bundleDir2, InputStream input, String uniqueTag, long dexPatchVersion) throws IOException {
        if (dexPatchVersion > 0) {
            this.bundleDir = bundleDir2;
            long j = dexPatchVersion;
            this.currentRevision = new BundleArchiveRevision(location, new File(bundleDir2, "dexpatch/" + dexPatchVersion), input);
            return;
        }
        this.bundleDir = bundleDir2;
        this.currentRevision = new BundleArchiveRevision(location, new File(bundleDir2, uniqueTag), input);
    }

    public BundleArchive(String location, File bundleDir2, File file, String uniqueTag, long dexPatchVersion) throws IOException {
        if (dexPatchVersion > 0) {
            this.bundleDir = bundleDir2;
            this.currentRevision = new BundleArchiveRevision(location, new File(bundleDir2, "dexpatch/" + dexPatchVersion), file);
            return;
        }
        this.bundleDir = bundleDir2;
        this.currentRevision = new BundleArchiveRevision(location, new File(bundleDir2, uniqueTag), file);
    }

    public BundleArchive(String location) {
    }

    public BundleArchiveRevision newRevision(String location, File bundleDir2, File input, String uniqueTag, long dexPatchVersion) throws IOException {
        if (!RuntimeVariables.sCurrentProcessName.equals(RuntimeVariables.androidApplication.getPackageName())) {
            throw new RuntimeException("can not update bundle in child process");
        } else if (dexPatchVersion > 0) {
            return new BundleArchiveRevision(location, new File(bundleDir2, "dexpatch/" + dexPatchVersion), input);
        } else {
            return new BundleArchiveRevision(location, new File(bundleDir2, uniqueTag), input);
        }
    }

    public BundleArchiveRevision getCurrentRevision() {
        return this.currentRevision;
    }

    public File getArchiveFile() {
        return this.currentRevision.getRevisionFile();
    }

    public File getBundleDir() {
        return this.bundleDir;
    }

    public boolean isDexOpted() {
        return this.currentRevision.isDexOpted();
    }

    public void optDexFile() {
        this.currentRevision.optDexFile();
    }

    public Class<?> findClass(String className, ClassLoader cl) throws ClassNotFoundException {
        return this.currentRevision.findClass(className, cl);
    }

    public File findLibrary(String fileName) {
        return this.currentRevision.findSoLibrary(fileName);
    }

    public List<URL> getResources(String resName) throws IOException {
        return this.currentRevision.getResources(resName);
    }

    public synchronized void purge(String uniqueTag, final long dexPatchVersion) {
        synchronized (this) {
            File[] dexPatchs = new File(this.bundleDir, "dexpatch/").listFiles(new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    return !String.valueOf(dexPatchVersion).equals(filename);
                }
            });
            if (dexPatchs != null) {
                for (File patch : dexPatchs) {
                    if (patch.isDirectory()) {
                        Framework.deleteDirectory(patch);
                    }
                }
            }
            for (File dir : this.bundleDir.listFiles()) {
                if (dir.isDirectory() && !dir.getName().contains("dexpatch") && !dir.getName().equals(uniqueTag)) {
                    Log.e("BundleArchive", "purge " + this.bundleDir + " : " + dir.getName());
                    Framework.deleteDirectory(dir);
                }
            }
        }
    }

    public static class MisMatchException extends RuntimeException {
        public MisMatchException(String detailMessage) {
            super(detailMessage);
        }

        public MisMatchException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }
    }
}
