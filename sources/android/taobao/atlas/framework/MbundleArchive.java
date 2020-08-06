package android.taobao.atlas.framework;

import android.taobao.atlas.framework.bundlestorage.BundleArchive;
import android.taobao.atlas.framework.bundlestorage.BundleArchiveRevision;
import android.taobao.atlas.hack.Hack;
import android.taobao.atlas.runtime.RuntimeVariables;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.List;

public class MbundleArchive extends BundleArchive {
    public boolean isDexOpted() {
        return true;
    }

    public void optDexFile() {
    }

    public Class<?> findClass(String className, ClassLoader cl) throws ClassNotFoundException {
        return cl.loadClass(className);
    }

    public File findLibrary(String fileName) {
        try {
            return (File) Hack.into(ClassLoader.class).method("findLibrary", String.class).invoke(Framework.getSystemClassLoader(), fileName);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Hack.HackDeclaration.HackAssertionException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public List<URL> getResources(String resName) throws IOException {
        try {
            return (List) Hack.into(ClassLoader.class).method("getResources", String.class).invoke(Framework.getSystemClassLoader(), resName);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (Hack.HackDeclaration.HackAssertionException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public MbundleArchive(String location) {
        super(location);
    }

    public BundleArchiveRevision getCurrentRevision() {
        return null;
    }

    public File getArchiveFile() {
        return new File(RuntimeVariables.sApkPath);
    }

    public File getBundleDir() {
        return new File(RuntimeVariables.sApkPath).getParentFile();
    }
}
