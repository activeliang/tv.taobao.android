package android.taobao.atlas.framework;

import android.os.Build;
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager;
import android.taobao.atlas.framework.bundlestorage.BundleArchive;
import android.taobao.atlas.framework.bundlestorage.BundleArchiveRevision;
import android.taobao.atlas.hack.AtlasHacks;
import android.taobao.atlas.runtime.RuntimeVariables;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.util.Log;
import dalvik.system.BaseDexClassLoader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import org.apache.commons.codec.language.Soundex;
import org.osgi.framework.BundleException;

public final class BundleClassLoader extends BaseDexClassLoader {
    private static final List<URL> EMPTY_LIST = new ArrayList();
    final BundleArchive archive;
    BundleImpl bundle;
    public ClassLoadListener classLoadListener;
    List<String> dependencies = null;
    String location = null;

    public interface ClassLoadListener {
        void onClassLoaded(Class cls);
    }

    BundleClassLoader(BundleImpl bundle2, List<String> dependencies2, String nativeLibPath) throws BundleException {
        super(".", (File) null, nativeLibPath, Object.class.getClassLoader());
        Log.e("BundleClassLoader", "nativeLibPath : " + nativeLibPath);
        if (Build.VERSION.SDK_INT >= 27) {
            try {
                Class PatchClassLoaderFactory = Class.forName("com.android.internal.os.ClassLoaderFactory");
                Method method = PatchClassLoaderFactory.getDeclaredMethod("createClassloaderNamespace", new Class[]{ClassLoader.class, Integer.TYPE, String.class, String.class, Boolean.TYPE, Boolean.TYPE});
                method.setAccessible(true);
                method.invoke(PatchClassLoaderFactory, new Object[]{this, 24, nativeLibPath, nativeLibPath, true, false});
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else if (Build.VERSION.SDK_INT >= 25) {
            try {
                Class PatchClassLoaderFactory2 = Class.forName("com.android.internal.os.PathClassLoaderFactory");
                Method method2 = PatchClassLoaderFactory2.getDeclaredMethod("createClassloaderNamespace", new Class[]{ClassLoader.class, Integer.TYPE, String.class, String.class, Boolean.TYPE});
                method2.setAccessible(true);
                method2.invoke(PatchClassLoaderFactory2, new Object[]{this, 24, nativeLibPath, nativeLibPath, true});
            } catch (Throwable e2) {
                e2.printStackTrace();
            }
        }
        this.bundle = bundle2;
        this.archive = bundle2.archive;
        this.location = bundle2.location;
        this.dependencies = dependencies2;
    }

    public boolean validateClasses() {
        if (this.archive == null) {
            return false;
        }
        if (!this.archive.isDexOpted()) {
            Log.e("BundleClassLoader", "dexopt is failed: " + this.location);
            return false;
        }
        List<String> dependencies2 = AtlasBundleInfoManager.instance().getBundleInfo(this.location).getTotalDependency();
        for (String bundleName : dependencies2) {
            BundleImpl dependencyBundle = (BundleImpl) Atlas.getInstance().getBundle(bundleName);
            if (dependencyBundle == null && AtlasBundleInfoManager.instance().isMbundle(bundleName)) {
                RuntimeVariables.delegateClassLoader.installMbundle(bundleName);
            } else if (dependencyBundle == null || dependencyBundle.getArchive() == null || !dependencyBundle.getArchive().isDexOpted()) {
                Log.e("BundleClassLoader", "dexopt is failed: " + dependencyBundle + ", bundleName=" + bundleName + ", dependencies=" + dependencies2 + ", this=" + this + ", thread=" + Thread.currentThread(), new Exception());
                return false;
            }
        }
        return true;
    }

    public BundleImpl getBundle() {
        return this.bundle;
    }

    private void checkEE(String[] req, String[] having) throws BundleException {
        if (req.length != 0) {
            Set<String> havingEEs = new HashSet<>(Arrays.asList(having));
            int i = 0;
            while (i < req.length) {
                if (!havingEEs.contains(req[i])) {
                    i++;
                } else {
                    return;
                }
            }
            throw new BundleException("Platform does not provide EEs " + Arrays.asList(req));
        }
    }

    public Class<?> loadOwnClass(String className) throws ClassNotFoundException {
        Class<?> clazz = findLoadedClass(className);
        if (clazz == null) {
            return findOwnClass(className);
        }
        return clazz;
    }

    /* access modifiers changed from: package-private */
    public void cleanup(boolean full) {
    }

    /* access modifiers changed from: protected */
    public Class<?> findClass(String classname) throws ClassNotFoundException {
        Class<?> clazz = findOwnClass(classname);
        if (clazz != null) {
            if (this.classLoadListener != null) {
                this.classLoadListener.onClassLoaded(clazz);
            }
            return clazz;
        }
        try {
            Class<?> clazz2 = Framework.systemClassLoader.loadClass(classname);
            if (clazz2 != null) {
                return clazz2;
            }
        } catch (Exception e) {
        }
        if (this.dependencies != null) {
            for (String dependencyBundle : this.dependencies) {
                try {
                    BundleImpl impl = (BundleImpl) Atlas.getInstance().getBundle(dependencyBundle);
                    if (impl == null) {
                        Log.e("BundleClassLoader", String.format("%s is not success installed by %s", new Object[]{"" + dependencyBundle, this.location}));
                    } else if (AtlasBundleInfoManager.instance().isMbundle(dependencyBundle)) {
                        impl.startBundle();
                    } else {
                        Class<?> clazz3 = ((BundleClassLoader) impl.getClassLoader()).loadOwnClass(classname);
                        if (clazz3 != null) {
                            impl.startBundle();
                            return clazz3;
                        }
                    }
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
            }
        }
        throw new ClassNotFoundException("Can't find class " + classname + " in BundleClassLoader: " + this.bundle.getLocation() + ", dependencies=" + this.dependencies + ", thread=" + Thread.currentThread());
    }

    private Class<?> findOwnClass(String classname) {
        try {
            return this.archive.findClass(classname, this);
        } catch (Exception e) {
            if (!(e instanceof BundleArchiveRevision.DexLoadException)) {
                return null;
            }
            throw ((BundleArchiveRevision.DexLoadException) e);
        }
    }

    private static Class<?> findDelegatedClass(BundleClassLoader delegation, String classname) {
        Class<?> clazz;
        synchronized (delegation) {
            clazz = delegation.findLoadedClass(classname);
            if (clazz == null) {
                clazz = delegation.findOwnClass(classname);
            }
        }
        return clazz;
    }

    /* access modifiers changed from: protected */
    public URL findResource(String filename) {
        String name = stripTrailing(filename);
        List<URL> results = findOwnResources(name, false);
        if (results.size() > 0) {
            return results.get(0);
        }
        try {
            List<URL> results2 = findImportedResources(name, false);
            if (results2.size() > 0) {
                return results2.get(0);
            }
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public Enumeration<URL> findResources(String filename) {
        String name = stripTrailing(filename);
        List<URL> results = findOwnResources(name, true);
        results.addAll(findImportedResources(name, true));
        return Collections.enumeration(results);
    }

    private List<URL> findOwnResources(String name, boolean multiple) {
        try {
            return this.archive.getResources(name);
        } catch (IOException e) {
            e.printStackTrace();
            return EMPTY_LIST;
        }
    }

    private List<URL> findImportedResources(String name, boolean multiple) {
        return EMPTY_LIST;
    }

    public String findLibrary(String libraryName) {
        File soFile = this.archive.findLibrary(System.mapLibraryName(libraryName));
        if (soFile != null) {
            return soFile.getAbsolutePath();
        }
        try {
            return (String) AtlasHacks.ClassLoader_findLibrary.invoke(Framework.systemClassLoader, libraryName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addRuntimeDependency(String bundleName) {
        if (this.dependencies == null) {
            this.dependencies = new ArrayList();
        }
        if (!this.dependencies.contains(bundleName)) {
            AtlasBundleInfoManager.instance().getBundleInfo(this.location).addRuntimeDependency(bundleName);
            this.dependencies.add(bundleName);
        }
    }

    public String toString() {
        return "BundleClassLoader[Bundle" + this.bundle + "]";
    }

    private static String[] readProperty(Attributes attrs, String property) throws BundleException {
        String values = attrs.getValue(property);
        if (values == null || !values.equals("")) {
            return splitString(values);
        }
        return new String[0];
    }

    private static String[] splitString(String values) {
        if (values == null) {
            return new String[0];
        }
        StringTokenizer tokenizer = new StringTokenizer(values, ",");
        if (tokenizer.countTokens() == 0) {
            return new String[]{values};
        }
        String[] result = new String[tokenizer.countTokens()];
        for (int i = 0; i < result.length; i++) {
            result[i] = tokenizer.nextToken().trim();
        }
        return result;
    }

    private static String stripTrailing(String filename) {
        return (filename.startsWith(WVNativeCallbackUtil.SEPERATER) || filename.startsWith("\\")) ? filename.substring(1) : filename;
    }

    private static String packageOf(String classname) {
        int pos = classname.lastIndexOf(46);
        return pos > -1 ? classname.substring(0, pos) : "";
    }

    private static String pseudoClassname(String filename) {
        return stripTrailing(filename).replace('.', Soundex.SILENT_MARKER).replace('/', '.').replace('\\', '.');
    }
}
